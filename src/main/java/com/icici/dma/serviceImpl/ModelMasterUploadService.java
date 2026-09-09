package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.GSTMasterError;
import com.icici.dma.model.GSTMasterTemp;
import com.icici.dma.model.ModelMasterError;
import com.icici.dma.model.ModelMasterTemp;
import com.icici.dma.repository.ModelMasterErrorRepository;

@Service
public class ModelMasterUploadService {

	private static final Logger logger = LogManager.getLogger(ModelMasterUploadService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	// Excel header → Entity field mapping
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField1 = "manufacturerId";
	private static final String PkField2 = "modelId";

	static {

		HEADER_MAP.put("MANUFACTURER_ID", "manufacturerId");
		HEADER_MAP.put("MANUFACTURER_DESC", "manufacturerDesc");
		HEADER_MAP.put("MODEL_ID", "modelId");
		HEADER_MAP.put("MODEL_DESC", "modelDesc");
		HEADER_MAP.put("ASSET_CATEGORY", "assetCategory");
		HEADER_MAP.put("BAND", "band");

		// Cache all fields once
		for (Field field : ModelMasterTemp.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}

		for (Field field : ModelMasterError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	public Map<String, Object> upload(MultipartFile file, String user) {

		long startTime = System.currentTimeMillis();
		logger.info("========== Channel Master UPLOAD STARTED ==========");

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			if (sheet == null)
				throw new RuntimeException("Sheet not found in Excel file.");

			DataFormatter formatter = new DataFormatter();

			// ========================================================
			// HEADER VALIDATION
			// ========================================================

			logger.info("Header Validation Started");

			Row headerRow = sheet.getRow(0);

			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			logger.info("Header Validation Complate . Total column mapped : {}", columnFieldMap.size());

			// ========================================================
			// PRIMARY KEY IDENTIFICATION
			// ========================================================

			Map<String, Integer> primaryKeyColumnIndex = getPrimaryKeyColumnIndex(columnFieldMap);

			// hold thw which column index hold the primary key ==> 1
			Integer manufacturerIndex = primaryKeyColumnIndex.get(PkField1);
			Integer modelIndex = primaryKeyColumnIndex.get(PkField2);

			// hold the valid primary key set ==> ["ABC12_12", "ABC17_34", "ABC78"]
			Set<String> validPKSet = new HashSet<>();

			// hold primary key value and row number were appear ==> [ "AB12" -> [2,4,7] ]
			Map<String, List<Integer>> pkRowMap = new HashMap<>();

			// hold the invalid row number = > [1,2,3,4]
			Set<Integer> invalidRows = new HashSet<>();

			// hold the duplicate excel row number = > [1,2,3,4]
			Set<Integer> duplicateExcelRows = new HashSet<>();

			// hold the duplicate row number in DB = > [1,2,3,4]
			Set<Integer> dbDuplicateRows = new HashSet<>();

			// ========================================================
			// FIRST PASS – PK VALIDATION
			// ========================================================

			logger.info("First Phase : Pk validation and Excel duplicated check");
			for (int r = 1; r <= sheet.getLastRowNum(); r++) {

				Row row = sheet.getRow(r);

				if (isRowEmpty(row)) {
					continue;
				}

				Cell manufacturerCell = row.getCell(manufacturerIndex);
				Cell modelCell = row.getCell(modelIndex);

				if (manufacturerCell == null || modelCell == null) {
					invalidRows.add(r);
					continue;
				}

				String manufacturerValue = formatter.formatCellValue(manufacturerCell).trim();
				String modelValue = formatter.formatCellValue(modelCell).trim();
				
				manufacturerValue = manufacturerValue.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ").trim();
				modelValue = modelValue.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ").trim();
				
				// Null equivalent check
				if (isNullEquivalent(manufacturerValue) || isNullEquivalent(modelValue)) {
					logger.info("Row index {} marked as invalid due to null Eqvalent PK", r);
					invalidRows.add(r);
					continue;
				}

				try {
					Integer pk1 = Integer.parseInt(manufacturerValue);
					Integer pk2 = Integer.parseInt(modelValue);

					// composite Key
					String compositeKey = pk1 + "_" + pk2;
					String pk = compositeKey.trim().toUpperCase();

					// store row index against pk
					pkRowMap.computeIfAbsent(pk, k -> new ArrayList<>()).add(r);

					validPKSet.add(pk);
//					

				} catch (Exception e) {
					logger.info("Row index {} marked as invalid due to PK Parse Error. values :{}" + "_" + "{}", r,
							manufacturerValue, modelValue);
					invalidRows.add(r);
				}

			}

			logger.info("Total Invalid PK rows: {}", invalidRows.size());
			logger.info("Duplicate PK rows in Excel: {}", duplicateExcelRows.size());

			// ========================================================
			// Excel DUPLICATE CHECK
			// ========================================================
			for (Map.Entry<String, List<Integer>> entry : pkRowMap.entrySet()) {

				List<Integer> rows = entry.getValue();

//				entry.getValue();

				if (rows.size() > 1) {

					duplicateExcelRows.addAll(rows);

					logger.info("duplicate pk : {} found in rows : {}", entry.getValue(), rows);
				}
			}

			logger.info("Total Duplicate Pk rows in Excel : {} ", duplicateExcelRows.size());

			// ========================================================
			// DB DUPLICATE CHECK (Batch Operation)
			// ========================================================

//			logger.info("DB duplicate batch check started.");
//
//			if (validPKSet.isEmpty()) {
//
//				logger.info("No valid Pk Found. Skipped DB duplicate check.");
//
//			} else {
//
//				Set<String> existingInDB = fetchExistingInBatch(validPKSet);
//
//				logger.info("Total Pk found in DB: {}", existingInDB.size());
//
//				for (String pk : existingInDB) {
//
//					// fetch rows mapped to this Pk
//					List<Integer> rows = pkRowMap.get(pk);
//
//					if (rows != null && !rows.isEmpty()) {
//
//						dbDuplicateRows.addAll(rows);
//
//						logger.info("DB duplicate Pk {} found in rows {}", pk, rows);
//					}
//				}
//
//			}
//
//			logger.info("Duplicate PK rows in DB: {}", dbDuplicateRows.size());

			// ========================================================
			// Create Error Reason Map And FINAL ROWS TO SKIP
			// ========================================================

			int expectedSize = invalidRows.size() + duplicateExcelRows.size();

			Map<Integer, String> errorReasonMap = new HashMap<>(expectedSize, 1.0f);

			// invalid Pk
			for (Integer r : invalidRows) {
				errorReasonMap.put(r, "Invalid Row - Invalid Primary Key");
			}

			// duplicate in Excel
			for (Integer r : duplicateExcelRows) {
				errorReasonMap.put(r, "Duplicate Row - Duplicate in Excel");
			}

			// Duplicate in DB ( do not override existing reason)
//			for (Integer r : dbDuplicateRows) {
//				errorReasonMap.putIfAbsent(r, "Duplicate Row - Already Exist in DB");
//			}

			Set<Integer> rowsToSkip = errorReasonMap.keySet();

			logger.info("Invalid row count : {}", invalidRows.size());
			logger.info("Excel duplicate row count : {}", duplicateExcelRows.size());
//			logger.info("DB duplicate count : {}", dbDuplicateRows.size());
			logger.info("ErrorReasonMap count : {}", errorReasonMap.size());
			logger.info("Total rows to skip: {}", rowsToSkip.size());

			// ========================================================
			// PREPARE VALID Or INVALID ROWS List
			// ========================================================

			List<Row> validRowsList = new ArrayList<>();
			List<Row> invalidRowsList = new ArrayList<>();

			for (int r = 1; r <= sheet.getLastRowNum(); r++) {

				Row row = sheet.getRow(r);

				// skip completely empty row
				if (isRowEmpty(row)) {
					continue;
				}

				if (rowsToSkip.contains(r)) {

					invalidRowsList.add(row);

				} else {

					validRowsList.add(row);
				}
			}

			logger.info("Valid row count : {}", validRowsList.size());
			logger.info("Invalid row count: {}", invalidRowsList.size());

			// ========================================================
			// Generate unique Upload Id
			// ========================================================

			String uploadId = generateUploadId(file.getOriginalFilename());
			String originalFilename = file.getOriginalFilename();

			// ========================================================
			// PARALLEL VALID ENTITY CREATION
			// ========================================================

			long entityStartTime = System.currentTimeMillis();
			logger.info("======= Valid Entity cration Started ============");

			List<ModelMasterTemp> entityList = validRowsList.parallelStream().map(row -> {

				try {

					ModelMasterTemp entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setStatus(StatusConstant.PENDING);
					entity.setCreatedBy(user);
					entity.setCreateDate(new Date());
					
					entity.setModifiedBy(user);
					entity.setModifiedDate(new Date());

					entity.setActionType(ActionConstant.INSERT);
					entity.setActionDate(new Date());
					entity.setActionUser(user);
					
					entity.setFileName(originalFilename); // audit
					entity.setUploadId(uploadId); // audit

//					logger.info("Valid Entity Successfully created for row : {} ", row.getRowNum());
					return entity;

				} catch (Exception e) {
					logger.error("Valid Entity creation failed  at row {}. Reason : {}", row.getRowNum(),
							e.getMessage());
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

			logger.info("Total Entities prepared: {}", entityList.size());
			logger.info(" Entities creation comleted in {} ms : {}", (System.currentTimeMillis() - entityStartTime));
			logger.info("========== Valid Entity Creation Completed ================");

			// ========================================================
			// Build Error Column field map
			// ========================================================

			Map<Integer, Field> errorColumnFieldMap = new HashMap<>();

			for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

				String fieldName = entry.getValue().getName();

				Field errorField = ERROR_FIELD_CACHE.get(fieldName.toUpperCase());

				if (errorField != null) {
					errorColumnFieldMap.put(entry.getKey(), errorField);
				}
			}

			// ========================================================
			// PARALLEL ERROR ENTITY CREATION
			// ========================================================

			logger.info("======= Error Entity cration Started ============");
			logger.info("Total Invalid Rows for processing: {}", invalidRowsList.size());

			List<ModelMasterError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					ModelMasterError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

					entity.setCreatedBy(user);
					entity.setCreateDate(new Date());
					entity.setErrorMsg(reason);
					entity.setUploadId(uploadId);
					entity.setRowNumber(row.getRowNum() + 1);

					System.out.println(entity.toString());
					return entity;
				} catch (Exception e) {

					logger.error("Error Entity creation failed  at row {}. Reason : {}", row.getRowNum(),
							e.getMessage());
					return null;
				}

			}).filter(Objects::nonNull).collect(Collectors.toList());

			logger.info("Total Error Entities prepared: {}", errorEntityList.size());
			
			logger.info("========== Error Entity Creation Completed ================");

			process(entityList, errorEntityList, uploadId);

			Map<String, Integer> counts = getCounts(uploadId, originalFilename);

			Map<String, Object> response = new HashMap<>();

			response.put("totalRecords", entityList.size() + errorEntityList.size());
			response.put("successfulRecords", entityList.size());
			response.put("InsertCount", counts.get("insertCount"));
			response.put("UpdateCount", counts.get("updateCount"));
			response.put("ErrorCount", counts.get("errorCount"));

			long endtime = System.currentTimeMillis();

			logger.info(response.toString());

			logger.info("========== MOdel Master Upload Complated ========== : " + (endtime - startTime)+" ms");

			return response;

		} catch (Exception e2) {
			throw new RuntimeException("Model Upload Failed: " + e2.getMessage(), e2);
		}
	}

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		try {
			// get latest upload Id
			// String uploadId =
			// modelMasterErrorRepo.findTopUploadIdByCreatedByOrderByCreateDateDesc(user);

			String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_MODEL_MST_ERROR WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
//		String uploadId = jdbcTemplate.queryForObject(sql1, String.class, user);

			List<String> list = jdbcTemplate.query(sql1, (rs, rowNum) -> rs.getString("final_upload_id"), user);

			String uploadId = list.isEmpty() ? null : list.get(0);

			if (uploadId == null || uploadId.isEmpty()) {
				throw new ResourceNotFoundException("No Error Record");
			}

			logger.info("Starting error export for uploadId: {}", uploadId);

			// ---------------------------------------------------
			// Step 1: Fetch error records from DB
			// ---------------------------------------------------

//		List<ModelMasterError> errors = modelMasterErrorRepo.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * FROM tm_vhl_model_mst_error WHERE upload_id = ? OR ora_err_tag$= ?";

			List<Map<String, Object>> errors = jdbcTemplate.queryForList(sql, uploadId, uploadId);

			if (errors == null || errors.isEmpty()) {

				logger.warn("No error records found for uploadId: {}", uploadId);

				throw new IllegalArgumentException("No error records found for uploadId: " + uploadId);
			}

			logger.info("Total error records fetched: {}", errors.size());

			// ---------------------------------------------------
			// Step 2: Create Workbook
			// ---------------------------------------------------

			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet("Channel_MST_ERROR");

			// HEADER FONT STYLE

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("Mulish");
			headerFont.setFontHeightInPoints((short) 8);

			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);

			// DATA FONT STYLE
			Font dataFont = workbook.createFont();
			dataFont.setFontName("Mulish");
			dataFont.setFontHeightInPoints((short) 8);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setFont(dataFont);

			// ---------------------------------------------------
			// Step 3: Create Header Row
			// ---------------------------------------------------

			Row header = sheet.createRow(0);

			Cell cell0 = header.createCell(0);
			cell0.setCellValue("MANUFACTURER_ID");
			cell0.setCellStyle(headerStyle);

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("MANUFACTURER_DESC");
			cell1.setCellStyle(headerStyle);

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("MODEL_ID");
			cell2.setCellStyle(headerStyle);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("MODEL_DESC");
			cell3.setCellStyle(headerStyle);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("ASSET_CATEGORY");
			cell4.setCellStyle(headerStyle);

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("BAND");
			cell5.setCellStyle(headerStyle);

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("ERROR_REASON");
			cell6.setCellStyle(headerStyle);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("ROW_NUMBER");
			cell7.setCellStyle(headerStyle);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("UPLOAD_DATE");
			cell8.setCellStyle(headerStyle);
//
//		Cell cell41 = header.createCell(1);
//		cell41.setCellValue("NAME");
//		cell41.setCellStyle(headerStyle);
//
//		Cell cell42 = header.createCell(2);
//		cell42.setCellValue("STATE");
//		cell42.setCellStyle(headerStyle);
//
//		Cell cell43 = header.createCell(3);
//		cell43.setCellValue("LOCATION");
//		cell43.setCellStyle(headerStyle);

			// ---------------------------------------------------
			// Step 4: Write Data Rows
			// ---------------------------------------------------

			int rowIndex = 1;

//		for (ModelMasterError error : errors) {
//
//			Row row = sheet.createRow(rowIndex++);
//
//			Cell cell00 = row.createCell(0);
//			cell00.setCellValue(formatValue(error.getManufacturerId()));
//			cell00.setCellStyle(dataStyle);
//
//			Cell cell01 = row.createCell(1);
//			cell01.setCellValue(formatValue(error.getManufacturerDesc()));
//			cell01.setCellStyle(dataStyle);
//
//			Cell cell02 = row.createCell(2);
//			cell02.setCellValue(formatValue(error.getModelId()));
//			cell02.setCellStyle(dataStyle);
//
//			Cell cell03 = row.createCell(3);
//			cell03.setCellValue(formatValue(error.getModelDesc()));
//			cell03.setCellStyle(dataStyle);
//
//			Cell cell04 = row.createCell(4);
//			cell04.setCellValue(formatValue(error.getAssetCategory()));
//			cell04.setCellStyle(dataStyle);
//
//			Cell cell05 = row.createCell(5);
//			cell05.setCellValue(formatValue(error.getBand()));
//			cell05.setCellStyle(dataStyle);
//
//			Cell cell06 = row.createCell(6);
//			cell06.setCellValue(formatValue(error.getErrorMsg()));
//			cell06.setCellStyle(dataStyle);
//
//			Cell cell07 = row.createCell(7);
//			cell07.setCellValue(formatValue(error.getRowNumber()));
//			cell07.setCellStyle(dataStyle);
//
//			Cell cell08 = row.createCell(8);
//			cell08.setCellValue(formatValue(error.getCreateDate()));
//			cell08.setCellStyle(dataStyle);
//
//		}

			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("MANUFACTURER_ID")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("MANUFACTURER_DESC")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("MODEL_ID")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("MODEL_DESC")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("ASSET_CATEGORY")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("BAND")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell06.setCellStyle(dataStyle);

//			Cell cell07 = row.createCell(7);
//			cell07.setCellValue(formatValue(error.getRowNumber()));
//			cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(7);
				cell08.setCellValue(formatValue(error.get("CREATED_DATE")));
				cell08.setCellStyle(dataStyle);

			}

			// ---------------------------------------------------
			// Step 5: Auto size columns
			// ---------------------------------------------------

			for (int i = 0; i < 41; i++) {
				sheet.autoSizeColumn(i);
			}

			// ---------------------------------------------------
			// Step 6: Convert Workbook to InputStream
			// ---------------------------------------------------

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			workbook.write(out);

			workbook.close();

			logger.info("Excel export completed successfully for uploadId: {}", uploadId);

			return new ByteArrayInputStream(out.toByteArray());

		} catch (DataAccessException e) {
			logger.info("DB error while fetching the Finnone error Records ", e);
			throw new RuntimeException("Database Error occured");
		} catch (IOException ex) {
			logger.info("Excel Genration Failed ", ex);
			throw new RuntimeException("Failed to genrate error file");
		}
	}

	// ============================================================
	// HEADER VALIDATION
	// ============================================================

	private Map<Integer, Field> validateHeaders(Row headerRow, DataFormatter formatter) {

		logger.info("========== HEADER VALIDATION STARTED ==========");

		if (headerRow == null) {
			logger.error("Header row is missing in uploaded file.");
			throw new RuntimeException("Header row is missing in Excel file.");
		}

		// Column index → Entity field mapping
		Map<Integer, Field> columnFieldMap = new HashMap<>();

		// Expected headers from configuration
		Set<String> expectedHeaders = HEADER_MAP.keySet();

		// Track actual headers found in Excel
		Set<String> actualHeaders = new HashSet<>();

		// Validation trackers
		Set<String> duplicateHeaders = new HashSet<>();
		Set<String> extraHeaders = new HashSet<>();

		int expectedHeaderCount = HEADER_MAP.size();
		int matchedHeaderCount = 0;

		logger.info("Expected Header Count : {}", expectedHeaderCount);

		// Iterate through Excel header row

		for (int c = 0; c < headerRow.getLastCellNum(); c++) {

			Cell cell = headerRow.getCell(c);

			if (cell == null) {
				logger.debug("Skipping null cell at column {}", c);
				continue;
			}

			String rawHeader = formatter.formatCellValue(cell).trim();

			if (rawHeader.isEmpty()) {
				logger.debug("Skipping empty header at column {}", c);
				continue;
			}

			String normalized = normalizeHeader(rawHeader);

			logger.debug("Header Found at column {} : Raw='{}' | Normalized='{}'", c, rawHeader, normalized);

			// Duplicate header check (inside Excel)

			if (!actualHeaders.add(normalized)) {
				duplicateHeaders.add(normalized);
			}

			// Unexpected header check

			if (!HEADER_MAP.containsKey(normalized)) {
				extraHeaders.add(normalized);
				continue;
			}

			matchedHeaderCount++;

			// Map header to entity field

			String fieldName = HEADER_MAP.get(normalized);

			Field field = FIELD_CACHE.get(fieldName.toUpperCase());

			if (field == null) {
				logger.error("Entity field not found for header : {}", normalized);
				throw new RuntimeException("Entity field mapping not found for header: " + normalized);
			}

			columnFieldMap.put(c, field);

			// Optimization: stop when all expected headers matched
			if (matchedHeaderCount == expectedHeaderCount) {
				break;
			}
		}

		// Missing header check

		Set<String> missingHeaders = new HashSet<>(expectedHeaders);

		missingHeaders.removeAll(actualHeaders);

		logger.info("Actual Header Count   : {}", actualHeaders.size());
		logger.info("Matched Header Count  : {}", matchedHeaderCount);

		logger.info("Duplicate Headers     : {}", duplicateHeaders.size());
		logger.info("Unexpected Headers    : {}", extraHeaders.size());
		logger.info("Missing Headers       : {}", missingHeaders.size());

		// Final validation failure check

		if (!duplicateHeaders.isEmpty() || !extraHeaders.isEmpty() || !missingHeaders.isEmpty()) {

			StringBuilder error = new StringBuilder("\nHeader validation failed:\n");

			if (!duplicateHeaders.isEmpty()) {
				error.append("Duplicate Headers (").append(duplicateHeaders.size()).append(") : ")
						.append(duplicateHeaders).append("\n");
			}

			if (!extraHeaders.isEmpty()) {
				error.append("Unexpected Headers (").append(extraHeaders.size()).append(") : ").append(extraHeaders)
						.append("\n");
			}

			if (!missingHeaders.isEmpty()) {
				error.append("Missing Headers (").append(missingHeaders.size()).append(") : ").append(missingHeaders)
						.append("\n");
			}

			logger.error(error.toString());

			throw new RuntimeException(error.toString());
		}

		logger.info("========== HEADER VALIDATION SUCCESS ==========");
		logger.info("Total columns mapped to entity fields : {}", columnFieldMap.size());

		return columnFieldMap;
	}

	// ============================================================
	// PRIMARY KEY COLUMN FINDER
	// ============================================================

	private Map<String, Integer> getPrimaryKeyColumnIndex(Map<Integer, Field> columnFieldMap) {

		Integer manufactureIndex = null;
		Integer modelIdIndex = null;

		// ---- Find PK column index ----
		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			if (entry.getValue().getName().equalsIgnoreCase(PkField1)) {
				manufactureIndex = entry.getKey();
			}

			if (entry.getValue().getName().equalsIgnoreCase(PkField2)) {
				modelIdIndex = entry.getKey();
			}
		}

		// ---- Header missing check ----
		if (manufactureIndex == null || modelIdIndex == null) {
			throw new RuntimeException("ManufacturerId or ModelId column missing in Header");
		}

		Map<String, Integer> pkindexmap = new HashMap<>();
		pkindexmap.put(PkField1, manufactureIndex);
		pkindexmap.put(PkField2, modelIdIndex);

		return pkindexmap;
	}

	// ============================================================
	// ROW EMPTY CHECK
	// ============================================================

	private boolean isRowEmpty(Row row) {

		if (row == null)
			return true;

		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();

		if (firstCellNum == -1 || lastCellNum == -1)
			return true;
		DataFormatter formatter = new DataFormatter();

		for (int c = firstCellNum; c < lastCellNum; c++) {

			Cell cell = row.getCell(c);

			if (cell != null && cell.getCellType() != CellType.BLANK) {

				String value = formatter.formatCellValue(cell).trim();

				if (!value.isEmpty())
					return false;
			}
		}

		return true;
	}

	// ============================================================
	// Normalize Header
	// ============================================================

	private String normalizeHeader(String header) {
		return header == null ? null
				: header.trim().replace("-", "_").replace("&", "_").replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "_")
						.replaceAll("_+", "_").replaceAll("^_|_$", "_").toUpperCase();
	}

	// ============================================================
	// FETCH EXISTING PK IN BATCH
	// ============================================================

	private Set<String> fetchExistingInBatch(Set<String> validPKSet) {

		if (validPKSet == null || validPKSet.isEmpty())
			return Collections.emptySet();

		List<String> pkList = new ArrayList<>(validPKSet);

		List<Integer> manufacturerIds = pkList.stream().map(pk -> Integer.parseInt(pk.split("_")[0])).distinct()
				.collect(Collectors.toList());

		List<Integer> ModelIds = pkList.stream().map(pk -> Integer.parseInt(pk.split("_")[1])).distinct()
				.collect(Collectors.toList());

		Set<String> existingRecords = new HashSet<>();

		int batchSize = 900;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, manufacturerIds.size());

			List<Integer> manufactureIdsbatch = manufacturerIds.subList(start, end);

			List<Integer> ModelIdsbatch = ModelIds.subList(start, end);

			List<Object[]> result = entityManager.createQuery(
					"SELECT g.manufacturerId, g.modelId FROM ModelMasterTemp g WHERE g.manufacturerId IN :manufacturerIds AND g.modelId IN :modelIds",
					Object[].class).setParameter("manufacturerIds", manufactureIdsbatch)
					.setParameter("modelIds", ModelIdsbatch).setHint("org.hibernate.readOnly", true).getResultList();

			for (Object[] r : result) {
				String key = r[0] + "_" + r[1];
				existingRecords.add(key);
			}

		}

		return existingRecords;

	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private ModelMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		ModelMasterTemp entity = new ModelMasterTemp();

//		logger.info("Mapping field for row : {}", row.getRowNum());

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			Cell cell = row.getCell(entry.getKey());

			if (cell == null) {
				logger.info("Row : {} , Column index : {} is null, Skipped.", row.getRowNum(), entry.getKey());
				continue;
			}

			String value = formatter.formatCellValue(cell).trim();

			setFieldValue(entry.getValue(), entity, value, cell);

//			logger.info("Row : {} , Coumn index : {} --> Field '{}' Value {}.", row.getRowNum(), entry.getKey(),
//					entry.getValue().getName(), value);

		}

		return entity;
	}

	// ============================================================
	// YOUR setFieldValue() – FULL DATE SUPPORT
	// ============================================================

	private void setFieldValue(Field field, Object entity, String value, Cell cell) throws Exception {

		Class<?> type = field.getType();
		String fieldName = field.getName();

		if (isNullEquivalent(value)) {

			logger.info("Field '{}' received null equivalent value '{}'", fieldName, value);

			if (type.equals(String.class))
				field.set(entity, null);

			else if (type.equals(Integer.class) || type.equals(int.class))
				field.set(entity, 0);

			else if (type.equals(Long.class) || type.equals(long.class))
				field.set(entity, 0L);

			else
				field.set(entity, null);

			return;
		}
		try {

			if (type.equals(String.class)) {
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");
				field.set(entity, value);

			} else if (type.equals(Integer.class) || type.equals(int.class)) {
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");
				field.set(entity, Integer.parseInt(value));

			} else if (type.equals(Long.class) || type.equals(long.class)) {
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");
				field.set(entity, Long.parseLong(value));

			} else if (type.equals(BigDecimal.class)) {
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");
				field.set(entity, new BigDecimal(value));

			} else if (type.equals(Date.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getDateCellValue());
				else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					field.set(entity, sdf.parse(value));
				}
			}

			else if (type.equals(LocalDate.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue().toLocalDate());
				else
					field.set(entity, LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			else if (type.equals(LocalDateTime.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue());
				else
					field.set(entity, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}

//			logger.info("Field : {} Successfully set with value : {}", fieldName, value);

		} catch (Exception e) {

			logger.error("Error while setting field : '{}' with value : '{}' , type : {} ", fieldName, value,
					type.getSimpleName(), e);
			throw e;
		}
	}

	// ============================================================
	// MAP ROW TO Error ENTITY (Using Reflection)
	// ============================================================

	private ModelMasterError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		ModelMasterError errorEntity = new ModelMasterError();
//		logger.info("Mapping Error row to Field : {}", row.getRowNum());

		for (Map.Entry<Integer, Field> entry : errorColumnFieldMap.entrySet()) {
			Integer columnIndex = entry.getKey();
			Field field = entry.getValue();
			Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

			String value = formatter.formatCellValue(cell).trim();

			field.setAccessible(true);
			field.set(errorEntity, value);
		}
		return errorEntity;
	}

	// ============================================================
	// CHECK Cell Null Equivalent
	// ============================================================

	private boolean isNullEquivalent(String value) {
		if (value == null)
			return true;
		String val = value.trim();
		return val.isEmpty() || val.equalsIgnoreCase("N/A")
				|| val.equalsIgnoreCase("#N/A")
				|| val.equalsIgnoreCase("-") || val.equalsIgnoreCase("#")
				|| val.equalsIgnoreCase("##") || val.equalsIgnoreCase("###");
	}

	// ============================================================
	// Create Unique Upload Id
	// ============================================================

	private String generateUploadId(String fileName) {

		// remove extension
		String baseName = fileName.replace(".xlsx", "").replace(".xls", "");

		// take first 3 characters
		String prefix = baseName.substring(0, Math.min(3, baseName.length())).toUpperCase();

		// timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		// random number
//		int random = ThreadLocalRandom.current().nextInt(1000, 9999);
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_MODEL_MST_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	// ============================================================
	// Batch Insert Channel Temp
	// ============================================================
	@Transactional
	public void batchInsertStage(List<ModelMasterTemp> entityList, String uploadId) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}

		logger.info("Starting JDBC batch insert for TEMP table. Total records: {}", entityList.size());

		String sql = "INSERT INTO TM_VHL_MODEL_MST_STG ("
				+ "MANUFACTURER_ID, MANUFACTURER_DESC, MODEL_ID, MODEL_DESC, ASSET_CATEGORY, BAND,"
				+ "CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, REMARKS, STATUS,"
				+ "ACTION_TYPE, ACTION_DATE, ACTION_USER, UPLOAD_ID, FILE_NAME )" + " VALUES (" + " ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?)" + "LOG ERRORS INTO TM_VHL_MODEL_MST_ERROR ('" + uploadId + "') "
				+ "REJECT LIMIT UNLIMITED";

		int batchSize = 500;

		long start = System.currentTimeMillis();

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, ModelMasterTemp entity) -> {

			// 1 MANUFACTURER_ID
			ps.setInt(1, entity.getManufacturerId());

			// 2 MANUFACTURER_DESC
			ps.setString(2, entity.getManufacturerDesc());

			// 3 MODEL_ID
			ps.setInt(3, entity.getModelId());

			// 4 MODEL_DESC
			ps.setString(4, entity.getModelDesc());

			// 5 ASSET_CATEGORY
			ps.setString(5, entity.getAssetCategory());

			// 6 BAND
			ps.setString(6, entity.getBand());

			// 7 CREATED_BY
			ps.setString(7, entity.getCreatedBy());

			// 8 CREATED_DATE
			if (entity.getCreateDate() != null) {
				ps.setTimestamp(8, new Timestamp(entity.getCreateDate().getTime()));
			} else {
				ps.setNull(8, Types.TIMESTAMP);
			}

			// 9 MODIFIED_BY
			ps.setString(9, entity.getModifiedBy());

			// 10 MODIFIED_DATE
			if (entity.getModifiedDate() != null) {
				ps.setTimestamp(10, new Timestamp(entity.getModifiedDate().getTime()));
			} else {
				ps.setNull(10, Types.TIMESTAMP);
			}

			// 11 REMARKS
			ps.setString(11, entity.getRemarks());

			// 12 STATUS
			ps.setString(12, entity.getStatus());

			// 13 ACTION_TYPE
			ps.setString(13, entity.getActionType());

			// 14 ACTION_DATE
			if (entity.getActionDate() != null) {
				ps.setTimestamp(14, new Timestamp(entity.getActionDate().getTime()));
			} else {
				ps.setNull(14, Types.TIMESTAMP);
			}

			// 15 ACTION_USER
			ps.setString(15, entity.getActionUser());
			
			// 16 uploadId
			ps.setString(16, entity.getUploadId());

			// 17 filename
			ps.setString(17, entity.getFileName());
		});

		logger.info("Model Stage batch insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// Batch Insert Channel Error
	// ============================================================
	@Transactional
	public void batchInsertError(List<ModelMasterError> errorList) {

		System.out.println(errorList);
		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for Model ERROR table. Total records: {}", errorList.size());

		long startTime = System.currentTimeMillis();

		// SQL without ID column because it is auto-generated
		String sql = "INSERT INTO TM_VHL_MODEL_MST_ERROR ("
				+ "MANUFACTURER_ID, MANUFACTURER_DESC, MODEL_ID, MODEL_DESC, ASSET_CATEGORY, BAND,"
				+ "CREATED_BY, CREATED_DATE, ERROR_MSG, UPLOAD_ID, ROW_NUMBER )" + " VALUES (?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?)";

		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, ModelMasterError entity) -> {

			// 1 MANUFACTURER_ID
			ps.setString(1, entity.getManufacturerId());

			// 2 MANUFACTURER_DESC
			ps.setString(2, entity.getManufacturerDesc());

			// 3 MODEL_ID
			ps.setString(3, entity.getModelId());

			// 4 MODEL_DESC
			ps.setString(4, entity.getModelDesc());

			// 5 ASSET_CATEGORY
			ps.setString(5, entity.getAssetCategory());

			// 6 BAND
			ps.setString(6, entity.getBand());

			// 7 CREATED_BY
			ps.setString(7, entity.getCreatedBy());

			// 8 CREATED_DATE
			if (entity.getCreateDate() != null) {
				ps.setTimestamp(8, new Timestamp(entity.getCreateDate().getTime()));
			} else {
				ps.setNull(8, Types.TIMESTAMP);
			}

			// 9 ERROR_MSG
			ps.setString(9, entity.getErrorMsg());

			// 10 UPLOAD_ID
			ps.setString(10, entity.getUploadId());

			// 11 ROW_NUMBER
			if (entity.getRowNumber() != null) {
				ps.setInt(11, entity.getRowNumber());
			} else {
				ps.setNull(11, Types.INTEGER);
			}
		});

		logger.info("Model ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
				(System.currentTimeMillis() - startTime));
	}

	private String formatValue(Object value) {

		if (value == null) {
			return "";
		}

		if (value instanceof Date) {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			return sdf.format((Date) value);
		}

		if (value instanceof LocalDate) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

			return ((LocalDate) value).format(formatter);
		}

		if (value instanceof LocalDateTime) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

			return ((LocalDateTime) value).toLocalDate().format(formatter);
		}

		return value.toString();
	}

	@Transactional(rollbackFor = Exception.class)
	public void process(List<ModelMasterTemp> validEntityList, List<ModelMasterError> invalidIntityList,
			String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into stage
			batchInsertStage(validEntityList, uploadId);

			

			logger.info(" update stage to temp");
			// update Existing
			updateTemp(uploadId);

			logger.info(" insert stage to temp");
			// insert new
			insertTemp(uploadId);
			
			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Model Master File upload Process Failed");
		} finally {
//			jdbcTemplate.execute("DELETE FROM TM_VHL_MODEL_MST_STG WHERE UPLOAD_ID =' "+uploadId+"'");
		}
	}

	private void insertTemp(String uploadId) {

		logger.info(" insert Gst Master Temp start");

		String insertSql = " INSERT /*+ APPEND PARALLEL(4) */ "
				+ "INTO TM_VHL_MODEL_MST_TEMP ( "
				+ "	   TEMP_ID,"
				+ "    MANUFACTURER_ID, "
				+ "    MANUFACTURER_DESC, "
				+ "    MODEL_ID, "
				+ "    MODEL_DESC, "
				+ "    ASSET_CATEGORY, "
				+ "    BAND, "
				+ "    CREATED_BY, "
				+ "    CREATED_DATE, "
				+ "    REMARKS, "
				+ "    STATUS, "
				+ "    ACTION_TYPE, "
				+ "    ACTION_DATE, "
				+ "    ACTION_USER, "
				+ "    UPLOAD_ID, "
				+ "    FILE_NAME "
				+ ") "
				+ "SELECT "
				+ "	   ISEQ$$_136535.NEXTVAL,"
				+ "    src.MANUFACTURER_ID, "
				+ "    src.MANUFACTURER_DESC, "
				+ "    src.MODEL_ID, "
				+ "    src.MODEL_DESC, "
				+ "    src.ASSET_CATEGORY, "
				+ "    src.BAND, "
				+ "    src.CREATED_BY, "
				+ "    src.CREATED_DATE, "
				+ "    src.REMARKS, "
				+ "    src.STATUS, "
				+ "    src.ACTION_TYPE, "
				+ "    src.ACTION_DATE, "
				+ "    src.ACTION_USER, "
				+ "    src.UPLOAD_ID, "
				+ "    src.FILE_NAME "
				+ "FROM TM_VHL_MODEL_MST_STG src "
				+ "WHERE src.UPLOAD_ID = ?  "
				+ "AND NOT EXISTS ( "
				+ "    SELECT 1 "
				+ "    FROM TM_VHL_MODEL_MST_TEMP t "
				+ "    WHERE t.MODEL_ID = src.MODEL_ID "
				+ "      AND t.MANUFACTURER_ID = src.MANUFACTURER_ID "
				+ ") "
				+ "  "
				+ "LOG ERRORS INTO TM_VHL_MODEL_MST_ERROR (?) "
				+ "REJECT LIMIT UNLIMITED";

		jdbcTemplate.update(insertSql, uploadId, uploadId);

	}

	private void updateTemp(String uploadId) {

		logger.info(" update Gst Master Temp start");


		long updateStart = System.currentTimeMillis();


		String updateSql ="UPDATE TM_VHL_MODEL_MST_TEMP t "
		      + "SET ( "
		      + "    MANUFACTURER_DESC, "
		      + "    MODEL_DESC, "
		      + "    ASSET_CATEGORY, "
		      + "    BAND, "
		      + "    MODIFIED_BY, "
		      + "    MODIFIED_DATE, "
		      + "    REMARKS, "
		      + "    STATUS, "
		      + "    ACTION_TYPE, "
		      + "    ACTION_DATE, "
		      + "    ACTION_USER, "
		      + "    UPLOAD_ID, "
		      + "    FILE_NAME "
		      + ") = ( "
		      + "    SELECT /*+ INDEX(s IDX_MODEL_STG_UPL_MODEL) */ "
		      + "        s.MANUFACTURER_DESC, "
		      + "        s.MODEL_DESC, "
		      + "        s.ASSET_CATEGORY, "
		      + "        s.BAND, "
		      + "        s.MODIFIED_BY, "
		      + "        s.MODIFIED_DATE, "
		      + "        s.REMARKS, "
		      + "        s.STATUS, "
		      + "        'U', "
		      + "        s.ACTION_DATE, "
		      + "        s.ACTION_USER, "
		      + "        s.UPLOAD_ID, "
		      + "        s.FILE_NAME "
		      + "    FROM TM_VHL_MODEL_MST_STG s "
		      + "    WHERE s.MODEL_ID = t.MODEL_ID "
		      + "      AND s.MANUFACTURER_ID = t.MANUFACTURER_ID "
		      + "      AND s.UPLOAD_ID = ? "
		      + ") "
		      + "WHERE (t.MODEL_ID, t.MANUFACTURER_ID) IN ( "
		      + "    SELECT /*+ INDEX(s IDX_MODEL_STG_UPL_MODEL) */ "
		      + "        s.MODEL_ID, "
		      + "        s.MANUFACTURER_ID "
		      + "    FROM TM_VHL_MODEL_MST_STG s "
		      + "    WHERE s.UPLOAD_ID = ? "
		      + ") "
		      + "LOG ERRORS INTO TM_VHL_MODEL_MST_ERROR (?) "
		      + "REJECT LIMIT UNLIMITED";
		
		
//		String updateSql = "UPDATE TM_VHL_MODEL_MST_TEMP t "
//		      + "SET ( "
//		      + "    MANUFACTURER_DESC, "
//		      + "    MODEL_DESC, "
//		      + "    ASSET_CATEGORY, "
//		      + "    BAND, "
//		      + "    MODIFIED_BY, "
//		      + "    MODIFIED_DATE, "
//		      + "    REMARKS, "
//		      + "    STATUS, "
//		      + "    ACTION_TYPE, "
//		      + "    ACTION_DATE, "
//		      + "    ACTION_USER, "
//		      + "    UPLOAD_ID, "
//		      + "    FILE_NAME "
//		      + ") = ( "
//		      + "    SELECT "
//		      + "        s.MANUFACTURER_DESC, "
//		      + "        s.MODEL_DESC, "
//		      + "        s.ASSET_CATEGORY, "
//		      + "        s.BAND, "
//		      + "        s.MODIFIED_BY, "
//		      + "        s.MODIFIED_DATE, "
//		      + "        s.REMARKS, "
//		      + "        s.STATUS, "
//		      + "        'U', "
//		      + "        s.ACTION_DATE, "
//		      + "        s.ACTION_USER, "
//		      + "        s.UPLOAD_ID, "
//		      + "        s.FILE_NAME "
//		      + "    FROM TM_VHL_MODEL_MST_STG s "
//		      + "    WHERE s.MODEL_ID = t.MODEL_ID "
//		      + "      AND s.MANUFACTURER_ID = t.MANUFACTURER_ID "
//		      + "      AND s.UPLOAD_ID = ? "
//		      + ") "
//		      + "WHERE EXISTS ( "
//		      + "    SELECT 1 "
//		      + "    FROM TM_VHL_MODEL_MST_STG s "
//		      + "    WHERE s.MODEL_ID = t.MODEL_ID "
//		      + "      AND s.MANUFACTURER_ID = t.MANUFACTURER_ID "
//		      + "      AND s.UPLOAD_ID = ? "
//		      + ") "
//		      + "LOG ERRORS INTO TM_VHL_MODEL_MST_ERROR (?) "
//		      + "REJECT LIMIT UNLIMITED";
//
		jdbcTemplate.update(updateSql, uploadId, uploadId, uploadId);

		logger.info("Total update : " + (System.currentTimeMillis() - updateStart));

	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_MODEL_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='I'",
				Integer.class, uploadId, originalFilename);

		int updateCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_MODEL_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='U'",
				Integer.class, uploadId, originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_MODEL_MST_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("updateCount", updateCount);
		result.put("errorCount", errorCount);

		return result;
	}
}

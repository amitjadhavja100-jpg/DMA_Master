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
import com.icici.dma.model.BranchMasterError;
import com.icici.dma.model.BranchMasterTemp;

@Service
public class BranchMasterUploadService {

	private static final Logger logger = LogManager.getLogger(BranchMasterUploadService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Excel header → Entity field mapping
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "branchCode";

	static {

		HEADER_MAP.put("BRANCH_CODE", "branchCode");
		HEADER_MAP.put("BRANCH_NAME", "branchName");
		HEADER_MAP.put("HUB", "hub");
		HEADER_MAP.put("AL_STATE", "aLState");
		HEADER_MAP.put("ZONE", "zone");
		HEADER_MAP.put("RBH", "rBH");
		HEADER_MAP.put("ED_STATE", "eDState");
		HEADER_MAP.put("ED_ZONE", "eDZone");
		HEADER_MAP.put("ZH_NAME", "zHName");
		HEADER_MAP.put("STATE_HEAD", "stateHead");
		HEADER_MAP.put("MIS_STATE", "mISState");
		HEADER_MAP.put("RC_STATE", "rCState");
		HEADER_MAP.put("LOCATION", "location");

		// Cache all fields once
		for (Field field : BranchMasterTemp.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : BranchMasterError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	public Map<String, Object> upload(MultipartFile file, String user) {

		long startTime = System.currentTimeMillis();
		logger.info("========== Branch UPLOAD STARTED ==========");

		if (file == null || file.isEmpty()) {
			throw new RuntimeException("File is empty");
		}

		logger.info("File uploaded started. File Name : {}", file.getOriginalFilename());

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

			// hold column index and entity field ==> [ 0 -> Field (branchCode) ]
			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			logger.info("Header Validation Complate . Total column mapped : {}", columnFieldMap.size());

			// ========================================================
			// PRIMARY KEY IDENTIFICATION
			// ========================================================

			// hold thw which column index hold the primary key ==> 1
			Integer pkColumnIndex = getPrimaryKeyColumnIndex(columnFieldMap);

			// hold the valid primary key set ==> ["ABC12", "ABC17", "ABC78"]
			Set<String> validPKSet = new HashSet<>();

			// hold primary key valye and row number were appear ==> [ "AB12" -> [2,4,7] ]
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

				Cell cell = row.getCell(pkColumnIndex);

				// exact cell value
				String value = formatter.formatCellValue(cell).trim();
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ").trim();
				// Null equivalent check
				if (isNullEquivalent(value)) {
					logger.info("Row index {} marked as invalid due to null Eqvalent PK", r);
					invalidRows.add(r);
					continue;
				}

				try {
					// parse/convert into integer
//					Integer pk = Integer.parseInt(value);
					String pk = value.trim().toUpperCase();

					// store row index against pk
					pkRowMap.computeIfAbsent(pk, k -> new ArrayList<>()).add(r);

					validPKSet.add(pk);

				} catch (Exception e) {
					logger.info("Row index {} marked as invalid due to PK Parse Error. value :{}", r, value);
					invalidRows.add(r);
				}

			}

			logger.info("Total Invalid PK rows: {}", invalidRows.size());

			// ========================================================
			// Excel DUPLICATE CHECK
			// ========================================================
			logger.info("Excel duplicated check");

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

//			int expectedSize = invalidRows.size() + duplicateExcelRows.size() + dbDuplicateRows.size();

			int expectedSize = invalidRows.size() + duplicateExcelRows.size();

			Map<Integer, String> errorReasonMap = new HashMap<>(expectedSize, 1.0f);

			// invalid Pk
			for (Integer r : invalidRows) {
				errorReasonMap.put(r, "Invalid Row - Invalid " + PkField + " Key");
			}

			// duplicate in Excel
			for (Integer r : duplicateExcelRows) {
				errorReasonMap.put(r, "Duplicate Row -" + PkField + " duplicate in Excel");
			}

			// Duplicate in DB ( do not override existing reason)
//			for (Integer r : dbDuplicateRows) {
//				errorReasonMap.putIfAbsent(r, "Duplicate Row - " + PkField + " Already pending for approval");
//			}

//			Set<Integer> rowsToSkip = new HashSet<>(errorReasonMap.keySet());
			Set<Integer> rowsToSkip = errorReasonMap.keySet();

			logger.info("Invalid row count : {}", invalidRows.size());
			logger.info("Excel duplicate row count : {}", duplicateExcelRows.size());
			logger.info("DB duplicate count : {}", dbDuplicateRows.size());
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

//					logger.info("Row {} marked Invalid. reason : {}",r,errorReasonMap.get(r));

				} else {

					validRowsList.add(row);

//					logger.info("Row {} marked VALID.",r);
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

			logger.info("======= Valid Entity cration Started ============");

			List<BranchMasterTemp> entityList = validRowsList.parallelStream().map(row -> {

				try {
					BranchMasterTemp entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setStatus(StatusConstant.PENDING);
					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());
					
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

			logger.info("Total valid Entities prepared: {}", entityList.size());
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

			List<BranchMasterError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					BranchMasterError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());
					entity.setErrorMsg(reason);
					entity.setUploadId(uploadId);
					entity.setRowNumber(row.getRowNum() + 1);

					return entity;
				} catch (Exception e) {

					logger.error("Error Entity creation failed  at row {}. Reason : {}", row.getRowNum(),
							e.getMessage());
					return null;
				}

			}).collect(Collectors.toList());

			logger.info("Total Error Entities prepared: {}", errorEntityList.size());

			logger.info("========== Error Entity Creation Completed ================");

//			batchInsertTemp(entityList, uploadId);
//			batchInsertStage(entityList, uploadId);
//			batchInsertError(errorEntityList);

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

			logger.info("========== Branch UPLOAD Complated ========== : " + (endtime - startTime));

			return response;

		} catch (Exception e2) {
//			throw new RuntimeException("Model Upload Failed: " + e2.getMessage(), e2);
			throw new RuntimeException("Branch Master Upload Failed: " + e2.getMessage());
		}
	}

	@Transactional
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Starting error export for User: {}", user);

		try {
			// get latest upload Id
			// String uploadId =
			// branchErrorRepository.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

			String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_BRANCH_MST_ERROR WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
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

//		List<BranchMasterError> errors = branchErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * FROM tm_vhl_branch_mst_error WHERE upload_id = ? OR ora_err_tag$= ?";

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

			Sheet sheet = workbook.createSheet("Branch_ERROR");

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
			cell0.setCellValue("BRANCH_CODE");
			cell0.setCellStyle(headerStyle);

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("BRANCH_NAME");
			cell1.setCellStyle(headerStyle);

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("HUB");
			cell2.setCellStyle(headerStyle);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("AL_STATE");
			cell3.setCellStyle(headerStyle);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("ZONE");
			cell4.setCellStyle(headerStyle);

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("RBH");
			cell5.setCellStyle(headerStyle);

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("ED_STATE");
			cell6.setCellStyle(headerStyle);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("ED_ZONE");
			cell7.setCellStyle(headerStyle);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("ZH_NAME");
			cell8.setCellStyle(headerStyle);

			Cell cell9 = header.createCell(9);
			cell9.setCellValue("STATE_HEAD");
			cell9.setCellStyle(headerStyle);

			Cell cell10 = header.createCell(10);
			cell10.setCellValue("MIS_STATE");
			cell10.setCellStyle(headerStyle);

			Cell cell11 = header.createCell(11);
			cell11.setCellValue("RC_STATE");
			cell11.setCellStyle(headerStyle);

			Cell cell12 = header.createCell(12);
			cell12.setCellValue("LOCATION");
			cell12.setCellStyle(headerStyle);

//		Cell cell13 = header.createCell(13);
//		cell13.setCellValue("ROW_NUMBER");
//		cell13.setCellStyle(headerStyle);

			Cell cell14 = header.createCell(13);
			cell14.setCellValue("ERROR_REASON");
			cell14.setCellStyle(headerStyle);

			Cell cell15 = header.createCell(14);
			cell15.setCellValue("UPLOAD_DATE");
			cell15.setCellStyle(headerStyle);

			// ---------------------------------------------------
			// Step 4: Write Data Rows
			// ---------------------------------------------------

			int rowIndex = 1;

//		for (BranchMasterError error : errors) {
//
//			Row row = sheet.createRow(rowIndex++);
//
//			Cell cell00 = row.createCell(0);
//			cell00.setCellValue(formatValue(error.getBranchCode()));
//			cell00.setCellStyle(dataStyle);
//
//			Cell cell01 = row.createCell(1);
//			cell01.setCellValue(formatValue(error.getBranchName()));
//			cell01.setCellStyle(dataStyle);
//
//			Cell cell02 = row.createCell(2);
//			cell02.setCellValue(formatValue(error.getHub()));
//			cell02.setCellStyle(dataStyle);
//
//			Cell cell03 = row.createCell(3);
//			cell03.setCellValue(formatValue(error.getaLState()));
//			cell03.setCellStyle(dataStyle);
//
//			Cell cell04 = row.createCell(4);
//			cell04.setCellValue(formatValue(error.getZone()));
//			cell04.setCellStyle(dataStyle);
//
//			Cell cell05 = row.createCell(5);
//			cell05.setCellValue(formatValue(error.getrBH()));
//			cell05.setCellStyle(dataStyle);
//
//			Cell cell06 = row.createCell(6);
//			cell06.setCellValue(formatValue(error.geteDState()));
//			cell06.setCellStyle(dataStyle);
//			
//			Cell cell07 = row.createCell(7);
//			cell07.setCellValue(formatValue(error.geteDZone()));
//			cell07.setCellStyle(dataStyle);
//
//			Cell cell08 = row.createCell(8);
//			cell08.setCellValue(formatValue(error.getzHName()));
//			cell08.setCellStyle(dataStyle);
//
//			Cell cell09 = row.createCell(9);
//			cell09.setCellValue(formatValue(error.getStateHead()));
//			cell09.setCellStyle(dataStyle);
//
//			Cell cell010 = row.createCell(10);
//			cell010.setCellValue(formatValue(error.getmISState()));
//			cell010.setCellStyle(dataStyle);
//
//			Cell cell011 = row.createCell(11);
//			cell011.setCellValue(formatValue(error.getrCState()));
//			cell011.setCellStyle(dataStyle);
//
//			Cell cell012 = row.createCell(12);
//			cell012.setCellValue(formatValue(error.getLocation()));
//			cell012.setCellStyle(dataStyle);
//
//			Cell cell013 = row.createCell(13);
//			cell013.setCellValue(formatValue(error.getRowNumber()));
//			cell013.setCellStyle(dataStyle);
//
//			Cell cell014 = row.createCell(14);
//			cell014.setCellValue(formatValue(error.getErrorMsg()));
//			cell014.setCellStyle(dataStyle);
//
//			Cell cell015 = row.createCell(15);
//			cell015.setCellValue(formatValue(error.getCreatedDate()));
//			cell015.setCellStyle(dataStyle);
//		}

			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("BRANCH_CODE")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("BRANCH_NAME")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("HUB")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("AL_STATE")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("ZONE")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("RBH")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(error.get("ED_STATE")));
				cell06.setCellStyle(dataStyle);

				Cell cell07 = row.createCell(7);
				cell07.setCellValue(formatValue(error.get("ED_ZONE")));
				cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(8);
				cell08.setCellValue(formatValue(error.get("ZH_NAME")));
				cell08.setCellStyle(dataStyle);

				Cell cell09 = row.createCell(9);
				cell09.setCellValue(formatValue(error.get("STATE_HEAD")));
				cell09.setCellStyle(dataStyle);

				Cell cell010 = row.createCell(10);
				cell010.setCellValue(formatValue(error.get("MIS_STATE")));
				cell010.setCellStyle(dataStyle);

				Cell cell011 = row.createCell(11);
				cell011.setCellValue(formatValue(error.get("RC_STATE")));
				cell011.setCellStyle(dataStyle);

				Cell cell012 = row.createCell(12);
				cell012.setCellValue(formatValue(error.get("LOCATION")));
				cell012.setCellStyle(dataStyle);

//			Cell cell013 = row.createCell(13);
//			cell013.setCellValue(formatValue(error.getRowNumber()));
//			cell013.setCellStyle(dataStyle);

				Cell cell014 = row.createCell(13);
				cell014.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell014.setCellStyle(dataStyle);

				Cell cell015 = row.createCell(14);
				cell015.setCellValue(formatValue(error.get("CREATED_DATE")));
				cell015.setCellStyle(dataStyle);
			}

			// ---------------------------------------------------
			// Step 5: Auto size columns
			// ---------------------------------------------------

			for (int i = 0; i < 7; i++) {
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

	private Integer getPrimaryKeyColumnIndex(Map<Integer, Field> columnFieldMap) {

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			if (entry.getValue().getName().equalsIgnoreCase(PkField))
				return entry.getKey();
		}
		throw new RuntimeException("Primary key column APSCODE not found.");
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

	private Set<String> fetchExistingInBatch(Set<String> uploadedPKSet) {

		if (uploadedPKSet == null || uploadedPKSet.isEmpty())
			return Collections.emptySet();

		List<String> pkList = new ArrayList<>(uploadedPKSet);
		Set<String> existingRecords = new HashSet<>();

		int batchSize = 900;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, pkList.size());

			List<String> batch = pkList.subList(start, end);

			List<String> result = entityManager
					.createQuery("SELECT g.branchCode FROM BranchMasterTemp g WHERE g.branchCode IN :list",
							String.class)
					.setParameter("list", batch).setHint("org.hibernate.readOnly", true).getResultList();

			existingRecords.addAll(result);
		}

		return existingRecords;
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private BranchMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		BranchMasterTemp entity = new BranchMasterTemp();

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
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
					field.set(entity, sdf.parse(value));
				}
			}

			else if (type.equals(LocalDate.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue().toLocalDate());
				else
					field.set(entity, LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
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

	private BranchMasterError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap,
			DataFormatter formatter) throws Exception {

		BranchMasterError errorEntity = new BranchMasterError();
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
		return val.isEmpty() || val.equalsIgnoreCase("N/A") || val.equalsIgnoreCase("#N/A") || val.equalsIgnoreCase("-")
				|| val.equalsIgnoreCase("#") || val.equalsIgnoreCase("##") || val.equalsIgnoreCase("###");
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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_BRANCH_MST_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	// ============================================================
	// Batch Insert Branch Stage
	// ============================================================
	@Transactional
	public void batchInsertStage(List<BranchMasterTemp> entityList, String uploadId) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}

		logger.info("Starting JDBC batch insert for Channel TEMP table. Total records: {}", entityList.size());

		String sql = "INSERT INTO TM_VHL_BRANCH_MST_STG (" + "BRANCH_CODE, BRANCH_NAME, HUB, AL_STATE, "
				+ "ZONE, RBH, ED_STATE, ED_ZONE, " + "ZH_NAME, STATE_HEAD, MIS_STATE, RC_STATE, "
				+ "LOCATION, CREATED_BY, CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, "
				+ "REMARKS, STATUS, ACTION_DATE, ACTION_TYPE, ACTION_USER,  UPLOAD_ID, FILE_NAME"
				+ ") VALUES (?, ?, ?, ?," + " ?, ?, ?, ?," + " ?, ?, ?, ?," + " ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?)" + "LOG ERRORS INTO TM_VHL_BRANCH_MST_ERROR ('" + uploadId + "') "
				+ "REJECT LIMIT UNLIMITED";

		int batchSize = 500;

		long start = System.currentTimeMillis();

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, BranchMasterTemp entity) -> {

			// 1 BRANCH_CODE
			ps.setString(1, entity.getBranchCode());

			// 2 BRANCH_NAME
			ps.setString(2, entity.getBranchName());

			// 3 HUB
			ps.setString(3, entity.getHub());

			// 4 AL_STATE
			ps.setString(4, entity.getaLState());

			// 5 ZONE
			ps.setString(5, entity.getZone());

			// 6 RBH
			ps.setString(6, entity.getrBH());

			// 7 ED_STATE
			ps.setString(7, entity.geteDState());

			// 8 ED_ZONE
			ps.setString(8, entity.geteDZone());

			// 9 ZH_NAME
			ps.setString(9, entity.getzHName());

			// 10 STATE_HEAD
			ps.setString(10, entity.getStateHead());

			// 11 MIS_STATE
			ps.setString(11, entity.getmISState());

			// 12 RC_STATE
			ps.setString(12, entity.getrCState());

			// 13 LOCATION
			ps.setString(13, entity.getLocation());

			// 14 CREATED_BY
			ps.setString(14, entity.getCreatedBy());

			// 15 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(15, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(15, Types.TIMESTAMP);
			}

			// 16 MODIFIED_BY
			ps.setString(16, entity.getModifiedBy());

			// 17 MODIFIED_DATE
			if (entity.getModifiedDate() != null) {
				ps.setTimestamp(17, new Timestamp(entity.getModifiedDate().getTime()));
			} else {
				ps.setNull(17, Types.TIMESTAMP);
			}

			// 18 REMARKS
			ps.setString(18, entity.getRemark());

			// 19 STATUS
			ps.setString(19, entity.getStatus());

			// 20 ACTION_DATE
			if (entity.getActionDate() != null) {
				ps.setTimestamp(20, new Timestamp(entity.getActionDate().getTime()));
			} else {
				ps.setNull(20, Types.TIMESTAMP);
			}

			// 21 ACTION_TYPE
			ps.setString(21, entity.getActionType());

			// 22 ACTION_USER
			ps.setString(22, entity.getActionUser());

			// 23 uploadId
			ps.setString(23, entity.getUploadId());

			// 24 uploadId
			ps.setString(24, entity.getFileName());

		});

		logger.info("Branch TEMP batch insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// Batch Insert Branch Error
	// ============================================================
	@Transactional
	public void batchInsertError(List<BranchMasterError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for Branch ERROR table. Total records: {}", errorList.size());

		long startTime = System.currentTimeMillis();

		String sql = "INSERT INTO TM_VHL_BRANCH_MST_ERROR (" + "BRANCH_CODE, BRANCH_NAME, HUB, AL_STATE, "
				+ "ZONE, RBH, ED_STATE, ED_ZONE, ZH_NAME, STATE_HEAD, MIS_STATE, RC_STATE, "
				+ "LOCATION, ERROR_MSG, CREATED_BY, CREATED_DATE, UPLOAD_ID, ROW_NUMBER"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, BranchMasterError entity) -> {

			// 1 BRANCH_CODE
			ps.setString(1, entity.getBranchCode());

			// 2 BRANCH_NAME
			ps.setString(2, entity.getBranchName());

			// 3 HUB
			ps.setString(3, entity.getHub());

			// 4 AL_STATE
			ps.setString(4, entity.getaLState());

			// 5 ZONE
			ps.setString(5, entity.getZone());

			// 6 RBH
			ps.setString(6, entity.getrBH());

			// 7 ED_STATE
			ps.setString(7, entity.geteDState());

			// 8 ED_ZONE
			ps.setString(8, entity.geteDZone());

			// 9 ZH_NAME
			ps.setString(9, entity.getzHName());

			// 10 STATE_HEAD
			ps.setString(10, entity.getStateHead());

			// 11 MIS_STATE
			ps.setString(11, entity.getmISState());

			// 12 RC_STATE
			ps.setString(12, entity.getrCState());

			// 13 LOCATION
			ps.setString(13, entity.getLocation());

			// 14 ERROR_MSG
			ps.setString(14, entity.getErrorMsg());

			// 15 CREATED_BY
			ps.setString(15, entity.getCreatedBy());

			// 16 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(16, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(16, Types.TIMESTAMP);
			}

			// 17 UPLOAD_ID
			ps.setString(17, entity.getUploadId());

			// 18 ROW_NUMBER
			if (entity.getRowNumber() != null) {
				ps.setInt(18, entity.getRowNumber());
			} else {
				ps.setNull(18, Types.INTEGER);
			}
		});

		logger.info("Branch ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
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
	public void process(List<BranchMasterTemp> validEntityList, List<BranchMasterError> invalidIntityList,
			String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into stage
			batchInsertStage(validEntityList, uploadId);

			logger.info("update stage to temp");
			// update Existing
			updateBranchTemp(uploadId);

			logger.info("insert stage to temp");
			// insert new
			insertBranchTemp(uploadId);
			
			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Branch Master File upload Process Failed");
		} finally {
			jdbcTemplate.execute("DELETE FROM TM_VHL_BRANCH_MST_STG");
		}
	}

	private void insertBranchTemp(String uploadId) {

		logger.info(" insert Branch Master Temp start");

		String insertSql = "INSERT /*+ APPEND PARALLEL(4) */ "
				+ "INTO TM_VHL_BRANCH_MST_TEMP ("
				+ "	BRANCH_CODE, BRANCH_NAME,HUB,AL_STATE,ZONE,RBH,ED_STATE,"
				+ "     ED_ZONE,ZH_NAME,STATE_HEAD, MIS_STATE,RC_STATE,"
				+ "     LOCATION,CREATED_BY,CREATED_DATE,REMARKS,STATUS,"
				+ "     ACTION_TYPE,ACTION_DATE,ACTION_USER,"
				+ "	 UPLOAD_ID, FILE_NAME )"
				+ "	 "
				+ "  SELECT"
				+ "     src.BRANCH_CODE ,src.BRANCH_NAME,src.HUB,src.AL_STATE, src.ZONE, src.rbh,src.ED_STATE,"
				+ "     src.ED_ZONE,src.ZH_NAME,src.STATE_HEAD,src.MIS_STATE,src.RC_STATE,"
				+ "     src.LOCATION,src.CREATED_BY,src.CREATED_DATE,src.REMARKS,src.STATUS,"
				+ "     src.ACTION_TYPE,src.ACTION_DATE,src.ACTION_USER,"
				+ "	 src.UPLOAD_ID,src.FILE_NAME"
				+ "	 "
				+ "	FROM TM_VHL_BRANCH_MST_STG src  "
				+ "	WHERE src.UPLOAD_ID = ?"
				+ "	AND NOT EXISTS ( "
				+ "		SELECT 1 FROM TM_VHL_BRANCH_MST_TEMP t WHERE t.BRANCH_CODE = src.BRANCH_CODE )"
				+ "LOG ERRORS INTO TM_VHL_GST_MST_ERROR (?) REJECT LIMIT UNLIMITED";

		jdbcTemplate.update(insertSql, uploadId, uploadId);

	}

	private void updateBranchTemp(String uploadId) {

		logger.info(" update Branch Master Temp start");

		String updateSql = "UPDATE TM_VHL_BRANCH_MST_TEMP t " + "SET ( " + "    BRANCH_NAME, " + "    HUB, "
				+ "    AL_STATE, " + "    ZONE, " + "    RBH, " + "    ED_STATE, " + "    ED_ZONE, " + "    ZH_NAME, "
				+ "    STATE_HEAD, " + "    MIS_STATE, " + "    RC_STATE, " + "    LOCATION, " + "    MODIFIED_BY, "
				+ "    MODIFIED_DATE, " + "    REMARKS, " + "    STATUS, " + "    ACTION_TYPE, " + "    ACTION_DATE, "
				+ "    ACTION_USER, " + "    UPLOAD_ID, " + "    FILE_NAME " + ") =( "
				+ "    SELECT /*+ INDEX(s IDX_BRANCH_STG_UPL_BR) */ " + "        s.BRANCH_NAME, " + "        s.HUB, "
				+ "        s.AL_STATE, " + "        s.ZONE, " + "        s.RBH, " + "        s.ED_STATE, "
				+ "        s.ED_ZONE, " + "        s.ZH_NAME, " + "        s.STATE_HEAD, " + "        s.MIS_STATE, "
				+ "        s.RC_STATE, " + "        s.LOCATION, " + "        s.MODIFIED_BY, "
				+ "        s.MODIFIED_DATE, " + "        s.REMARKS, " + "        s.STATUS, " + "        'U', "
				+ "        s.ACTION_DATE, " + "        s.ACTION_USER, " + "        s.UPLOAD_ID, "
				+ "        s.FILE_NAME " + "    FROM TM_VHL_BRANCH_MST_STG s "
				+ "    WHERE s.BRANCH_CODE = t.BRANCH_CODE " + "      AND s.UPLOAD_ID = ? " + ") "
				+ "WHERE t.BRANCH_CODE IN " + "( " + "    SELECT /*+ INDEX(s IDX_BRANCH_STG_UPL_BR) */ "
				+ "        s.BRANCH_CODE " + "    FROM TM_VHL_BRANCH_MST_STG s " + "    WHERE s.UPLOAD_ID = ? " + ")"
				+ "LOG ERRORS INTO TM_VHL_BRANCH_MST_ERROR (?) REJECT LIMIT UNLIMITED";

		jdbcTemplate.update(updateSql, uploadId, uploadId, uploadId);

	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_BRANCH_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='I'",
				Integer.class, uploadId, originalFilename);

		int updateCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_BRANCH_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='U'",
				Integer.class, uploadId, originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_BRANCH_MST_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("updateCount", updateCount);
		result.put("errorCount", errorCount);

		return result;
	}

}

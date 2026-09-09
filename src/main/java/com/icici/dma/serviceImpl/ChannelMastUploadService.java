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
import com.icici.dma.model.ChannelMasterError;
import com.icici.dma.model.ChannelMasterTemp;

@Service
public class ChannelMastUploadService {

	private static final Logger logger = LogManager.getLogger(ChannelMastUploadService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;


	// Excel header → Entity field mapping
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "apsCode";

	static {

		HEADER_MAP.put("APS_CODE", "apsCode");
		HEADER_MAP.put("IBOXID", "iBoxId");
		HEADER_MAP.put("SUPPLIER_ID", "supplierId");
		HEADER_MAP.put("DATE", "dates");
		HEADER_MAP.put("SUSPENDED", "suspended");
		HEADER_MAP.put("NAME_OF_CHANNEL_AS_PER_AGMT", "nameOfChannel");

		HEADER_MAP.put("TYPE_OF_DSA", "typeOfDsa");
		HEADER_MAP.put("PAN_NO", "panNo");
		HEADER_MAP.put("YY", "yy");

		HEADER_MAP.put("SUPPLIER_ID_1", "supplierId1");
		HEADER_MAP.put("SUPPLIER_ID_2", "supplierId2");
		HEADER_MAP.put("SUPPLIER_ID_3", "supplierId3");
		HEADER_MAP.put("SUPPLIER_ID_4", "supplierId4");
		HEADER_MAP.put("SUPPLIER_ID_5", "supplierId5");
		HEADER_MAP.put("SUPPLIER_ID_6", "supplierId6");
		HEADER_MAP.put("SUPPLIER_ID_7", "supplierId7");
		HEADER_MAP.put("SUPPLIER_ID_8", "supplierId8");
		HEADER_MAP.put("SUPPLIER_ID_9", "supplierId9");
		HEADER_MAP.put("SUPPLIER_ID_10", "supplierId10");
		HEADER_MAP.put("SUPPLIER_ID_11", "supplierId11");
		HEADER_MAP.put("SUPPLIER_ID_12", "supplierId12");

		HEADER_MAP.put("REMARK", "remark");
		HEADER_MAP.put("LOCATION", "location");
		HEADER_MAP.put("MIS_STATE", "misState");
		HEADER_MAP.put("C_STATE", "cState");
		HEADER_MAP.put("ED_STATE", "edState");
		HEADER_MAP.put("ED_ZONE", "edZone");
		HEADER_MAP.put("SOURCING", "sourcing");
		HEADER_MAP.put("SOURCING_1", "sourcing1");

		HEADER_MAP.put("MANUFACTUE_NAME", "manufactuName");
		HEADER_MAP.put("NEW_MANUFACTUE_NAME", "newManufactuName");
		HEADER_MAP.put("OLD_I_BOX_ID", "oldIBoxId");
		HEADER_MAP.put("RC_LIMIT", "rcLimit");
		HEADER_MAP.put("SAP_CODE", "sapCode");
		HEADER_MAP.put("ACCOUNT_NO", "accountNo");
		HEADER_MAP.put("IFSC_CODE", "ifscCode");
		HEADER_MAP.put("BANK_NAME", "bankName");
		HEADER_MAP.put("I_BANK_YES_NON_I_BANK_NO", "iBankYesNonIBankNo");

		// Cache all fields once
		for (Field field : ChannelMasterTemp.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}

		for (Field field : ChannelMasterError.class.getDeclaredFields()) {
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

			Integer pkColumnIndex = getPrimaryKeyColumnIndex(columnFieldMap);

			Set<Integer> validPKSet = new HashSet<>();

			Map<Integer, List<Integer>> pkRowMap = new HashMap<>();

			Set<Integer> invalidRows = new HashSet<>();

			Set<Integer> duplicateExcelRows = new HashSet<>();

			Set<Integer> dbDuplicateRows = new HashSet<>();

			// ========================================================
			// FIRST PASS – PK VALIDATION
			// ========================================================

			logger.info("First Phase : Pk validation");
			for (int r = 1; r <= sheet.getLastRowNum(); r++) {

				Row row = sheet.getRow(r);

				if (isRowEmpty(row)) {
					continue;
				}

				Cell cell = row.getCell(pkColumnIndex);
				String value = formatter.formatCellValue(cell).trim();
				value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ").trim();
				// Null equivalent check
				if (isNullEquivalent(value)) {
					logger.info("Row index {} marked as invalid due to null Eqvalent PK", r);
					invalidRows.add(r);
					continue;
				}

				try {
					Integer pk = Integer.parseInt(value);

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
			for (Map.Entry<Integer, List<Integer>> entry : pkRowMap.entrySet()) {

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
//				Set<Integer> existingInDB = fetchExistingInBatch(validPKSet);
//
//				logger.info("Total Pk found in DB: {}", existingInDB.size());
//
//				for (Integer pk : existingInDB) {
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

			int expectedSize = invalidRows.size() + duplicateExcelRows.size() + dbDuplicateRows.size();

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

			logger.info("======= Valid Entity cration Started ============");

			List<ChannelMasterTemp> entityList = validRowsList.parallelStream().map(row -> {

				try {

					ChannelMasterTemp entity = mapRowToEntity(row, columnFieldMap, formatter);

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

			logger.info("Total Entities prepared: {}", entityList.size());

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


			List<ChannelMasterError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					ChannelMasterError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

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

			}).filter(Objects::nonNull).collect(Collectors.toList());

			logger.info("Total Error Entities prepared: {}", errorEntityList.size());

			logger.info("========== Error Entity Creation Completed ================");

			// ========================================================
			// BATCH SAVE USING JDBC TEMPLATE
			// ========================================================

//			batchInsertTemp(entityList, uploadId);
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

			logger.info("========== Channel UPLOAD COMPLETED ==========: " + (endtime - startTime) +" ms");
			return response;

		} catch (Exception e2) {
			throw new RuntimeException("Channel Upload Failed: " + e2.getMessage(), e2);
		}
	}

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

//		logger.info("Starting error export for uploadId: {}", uploadId);

		// get latest upload Id
		try {
			String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_CHANNEL_MST_ERROR WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
//		String uploadId = jdbcTemplate.queryForObject(sql1, String.class, user);

			// String uploadId =
			// channelmstErrorRepo.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

			List<String> list = jdbcTemplate.query(sql1, (rs, rowNum) -> rs.getString("final_upload_id"), user);

			String uploadId = list.isEmpty() ? null : list.get(0);

			if (uploadId == null || uploadId.isEmpty()) {
				throw new ResourceNotFoundException("No Error Record");
			}

			logger.info("Starting error export for uploadId: {}", uploadId);
			// ---------------------------------------------------
			// Step 1: Fetch error records from DB
			// ---------------------------------------------------

//		List<ChannelMasterError> errors = channelmstErrorRepo.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * FROM TM_VHL_CHANNEL_MST_ERROR WHERE upload_id = ? OR ora_err_tag$= ?";

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
			cell0.setCellValue("APS_CODE");
			cell0.setCellStyle(headerStyle);

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("IBOXID");
			cell1.setCellStyle(headerStyle);

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("SUPPLIER_ID");
			cell2.setCellStyle(headerStyle);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("DATE");
			cell3.setCellStyle(headerStyle);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("SUSPENDED");
			cell4.setCellStyle(headerStyle);

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("NAME_OF_CHANNEL_AS_PER_AGMT");
			cell5.setCellStyle(headerStyle);

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("TYPE_OF_DSA");
			cell6.setCellStyle(headerStyle);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("PAN_NO");
			cell7.setCellStyle(headerStyle);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("YY");
			cell8.setCellStyle(headerStyle);

			Cell cell9 = header.createCell(9);
			cell9.setCellValue("SUPPLIER_ID_1");
			cell9.setCellStyle(headerStyle);

			Cell cell10 = header.createCell(10);
			cell10.setCellValue("SUPPLIER_ID_2");
			cell10.setCellStyle(headerStyle);

			Cell cell11 = header.createCell(11);
			cell11.setCellValue("SUPPLIER_ID_3");
			cell11.setCellStyle(headerStyle);

			Cell cell12 = header.createCell(12);
			cell12.setCellValue("SUPPLIER_ID_4");
			cell12.setCellStyle(headerStyle);

			Cell cell13 = header.createCell(13);
			cell13.setCellValue("SUPPLIER_ID_5");
			cell13.setCellStyle(headerStyle);

			Cell cell14 = header.createCell(14);
			cell14.setCellValue("SUPPLIER_ID_6");
			cell14.setCellStyle(headerStyle);

			Cell cell15 = header.createCell(15);
			cell15.setCellValue("SUPPLIER_ID_7");
			cell15.setCellStyle(headerStyle);

			Cell cell16 = header.createCell(16);
			cell16.setCellValue("SUPPLIER_ID_8");
			cell16.setCellStyle(headerStyle);

			Cell cell17 = header.createCell(17);
			cell17.setCellValue("SUPPLIER_ID_9");
			cell17.setCellStyle(headerStyle);

			Cell cell18 = header.createCell(18);
			cell18.setCellValue("SUPPLIER_ID_10");
			cell18.setCellStyle(headerStyle);

			Cell cell19 = header.createCell(19);
			cell19.setCellValue("SUPPLIER_ID_11");
			cell19.setCellStyle(headerStyle);

			Cell cell20 = header.createCell(20);
			cell20.setCellValue("SUPPLIER_ID_12");
			cell20.setCellStyle(headerStyle);

			Cell cell21 = header.createCell(21);
			cell21.setCellValue("REMARK");
			cell21.setCellStyle(headerStyle);

			Cell cell22 = header.createCell(22);
			cell22.setCellValue("LOCATION");
			cell22.setCellStyle(headerStyle);

			Cell cell23 = header.createCell(23);
			cell23.setCellValue("MIS_STATE");
			cell23.setCellStyle(headerStyle);

			Cell cell24 = header.createCell(24);
			cell24.setCellValue("C_STATE");
			cell24.setCellStyle(headerStyle);

			Cell cell25 = header.createCell(25);
			cell25.setCellValue("ED_STATE");
			cell25.setCellStyle(headerStyle);

			Cell cell26 = header.createCell(26);
			cell26.setCellValue("ED_ZONE");
			cell26.setCellStyle(headerStyle);

			Cell cell27 = header.createCell(27);
			cell27.setCellValue("SOURCING");
			cell27.setCellStyle(headerStyle);

			Cell cell28 = header.createCell(28);
			cell28.setCellValue("SOURCING_1");
			cell28.setCellStyle(headerStyle);

			Cell cell29 = header.createCell(29);
			cell29.setCellValue("MANUFACTUE_NAME");
			cell29.setCellStyle(headerStyle);

			Cell cell30 = header.createCell(30);
			cell30.setCellValue("NEW_MANUFACTUE_NAME");
			cell30.setCellStyle(headerStyle);

			Cell cell31 = header.createCell(31);
			cell31.setCellValue("OLD_I_BOX_ID");
			cell31.setCellStyle(headerStyle);

			Cell cell32 = header.createCell(32);
			cell32.setCellValue("RC_LIMIT");
			cell32.setCellStyle(headerStyle);

			Cell cell33 = header.createCell(33);
			cell33.setCellValue("SAP_CODE");
			cell33.setCellStyle(headerStyle);

			Cell cell34 = header.createCell(34);
			cell34.setCellValue("ACCOUNT_NO");
			cell34.setCellStyle(headerStyle);

			Cell cell35 = header.createCell(35);
			cell35.setCellValue("IFSC_CODE");
			cell35.setCellStyle(headerStyle);

			Cell cell36 = header.createCell(36);
			cell36.setCellValue("BANK_NAME");
			cell36.setCellStyle(headerStyle);

			Cell cell37 = header.createCell(37);
			cell37.setCellValue("I_BANK_YES_NON_I_BANK_NO");
			cell37.setCellStyle(headerStyle);

//		Cell cell38 = header.createCell(38);
//		cell38.setCellValue("ROW_NUMBER");
//		cell38.setCellStyle(headerStyle);

			Cell cell39 = header.createCell(38);
			cell39.setCellValue("ERROR_REASON");
			cell39.setCellStyle(headerStyle);

			Cell cell40 = header.createCell(39);
			cell40.setCellValue("UPLOAD_DATE");
			cell40.setCellStyle(headerStyle);
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

//		for (ChannelMasterError error : errors) {
//
//			Row row = sheet.createRow(rowIndex++);
//
//			Cell cell00 = row.createCell(0);
//			cell00.setCellValue(formatValue(error.getApsCode()));
//			cell00.setCellStyle(dataStyle);
//
//			Cell cell01 = row.createCell(1);
//			cell01.setCellValue(formatValue(error.getiBoxId()));
//			cell01.setCellStyle(dataStyle);
//
//			Cell cell02 = row.createCell(2);
//			cell02.setCellValue(formatValue(error.getSupplierId()));
//			cell02.setCellStyle(dataStyle);
//
//			Cell cell03 = row.createCell(3);
//			cell03.setCellValue(formatValue(error.getDates()));
//			cell03.setCellStyle(dataStyle);
//
//			Cell cell04 = row.createCell(4);
//			cell04.setCellValue(formatValue(error.getSuspended()));
//			cell04.setCellStyle(dataStyle);
//
//			Cell cell05 = row.createCell(5);
//			cell05.setCellValue(formatValue(error.getNameOfChannel()));
//			cell05.setCellStyle(dataStyle);
//
//			Cell cell06 = row.createCell(6);
//			cell06.setCellValue(formatValue(error.getTypeOfDsa()));
//			cell06.setCellStyle(dataStyle);
//
//			Cell cell07 = row.createCell(7);
//			cell07.setCellValue(formatValue(error.getPanNo()));
//			cell07.setCellStyle(dataStyle);
//
//			Cell cell08 = row.createCell(8);
//			cell08.setCellValue(formatValue(error.getYy()));
//			cell08.setCellStyle(dataStyle);
//
//			Cell cell09 = row.createCell(9);
//			cell09.setCellValue(formatValue(error.getSupplierId1()));
//			cell09.setCellStyle(dataStyle);
//
//			Cell cell010 = row.createCell(10);
//			cell010.setCellValue(formatValue(error.getSupplierId2()));
//			cell010.setCellStyle(dataStyle);
//
//			Cell cell011 = row.createCell(11);
//			cell011.setCellValue(formatValue(error.getSupplierId3()));
//			cell011.setCellStyle(dataStyle);
//
//			Cell cell012 = row.createCell(12);
//			cell012.setCellValue(formatValue(error.getSupplierId4()));
//			cell012.setCellStyle(dataStyle);
//
//			Cell cell013 = row.createCell(13);
//			cell013.setCellValue(formatValue(error.getSupplierId5()));
//			cell013.setCellStyle(dataStyle);
//
//			Cell cell014 = row.createCell(14);
//			cell014.setCellValue(formatValue(error.getSupplierId6()));
//			cell014.setCellStyle(dataStyle);
//
//			Cell cell015 = row.createCell(15);
//			cell015.setCellValue(formatValue(error.getSupplierId7()));
//			cell015.setCellStyle(dataStyle);
//
//			Cell cell016 = row.createCell(16);
//			cell016.setCellValue(formatValue(error.getSupplierId8()));
//			cell016.setCellStyle(dataStyle);
//
//			Cell cell017 = row.createCell(17);
//			cell017.setCellValue(formatValue(error.getSupplierId9()));
//			cell017.setCellStyle(dataStyle);
//
//			Cell cell018 = row.createCell(18);
//			cell018.setCellValue(formatValue(error.getSupplierId10()));
//			cell018.setCellStyle(dataStyle);
//
//			Cell cell019 = row.createCell(19);
//			cell019.setCellValue(formatValue(error.getSupplierId11()));
//			cell019.setCellStyle(dataStyle);
//
//			Cell cell020 = row.createCell(20);
//			cell020.setCellValue(formatValue(error.getSupplierId12()));
//			cell020.setCellStyle(dataStyle);
//
//			Cell cell021 = row.createCell(21);
//			cell021.setCellValue(formatValue(error.getRemark()));
//			cell021.setCellStyle(dataStyle);
//
//			Cell cell022 = row.createCell(22);
//			cell022.setCellValue(formatValue(error.getLocation()));
//			cell022.setCellStyle(dataStyle);
//
//			Cell cell023 = row.createCell(23);
//			cell023.setCellValue(formatValue(error.getMisState()));
//			cell023.setCellStyle(dataStyle);
//
//			Cell cell024 = row.createCell(24);
//			cell024.setCellValue(formatValue(error.getcState()));
//			cell024.setCellStyle(dataStyle);
//
//			Cell cell025 = row.createCell(25);
//			cell025.setCellValue(formatValue(error.getEdState()));
//			cell025.setCellStyle(dataStyle);
//
//			Cell cell026 = row.createCell(26);
//			cell026.setCellValue(formatValue(error.getEdZone()));
//			cell026.setCellStyle(dataStyle);
//
//			Cell cell027 = row.createCell(27);
//			cell027.setCellValue(formatValue(error.getSourcing()));
//			cell027.setCellStyle(dataStyle);
//
//			Cell cell028 = row.createCell(28);
//			cell028.setCellValue(formatValue(error.getSourcing1()));
//			cell028.setCellStyle(dataStyle);
//
//			Cell cell029 = row.createCell(29);
//			cell029.setCellValue(formatValue(error.getManufactuName()));
//			cell029.setCellStyle(dataStyle);
//
//			Cell cell030 = row.createCell(30);
//			cell030.setCellValue(formatValue(error.getNewManufactuName()));
//			cell030.setCellStyle(dataStyle);
//
//			Cell cell031 = row.createCell(31);
//			cell031.setCellValue(formatValue(error.getOldIBoxId()));
//			cell031.setCellStyle(dataStyle);
//
//			Cell cell032 = row.createCell(32);
//			cell032.setCellValue(formatValue(error.getRcLimit()));
//			cell032.setCellStyle(dataStyle);
//
//			Cell cell033 = row.createCell(33);
//			cell033.setCellValue(formatValue(error.getSapCode()));
//			cell033.setCellStyle(dataStyle);
//
//			Cell cell034 = row.createCell(34);
//			cell034.setCellValue(formatValue(error.getAccountNo()));
//			cell034.setCellStyle(dataStyle);
//
//			Cell cell035 = row.createCell(35);
//			cell035.setCellValue(formatValue(error.getIfscCode()));
//			cell035.setCellStyle(dataStyle);
//
//			Cell cell036 = row.createCell(36);
//			cell036.setCellValue(formatValue(error.getBankName()));
//			cell036.setCellStyle(dataStyle);
//
//			Cell cell037 = row.createCell(37);
//			cell037.setCellValue(formatValue(error.getiBankYesNonIBankNo()));
//			cell037.setCellStyle(dataStyle);
//
//			Cell cell038 = row.createCell(38);
//			cell038.setCellValue(formatValue(error.getRowNumber()));
//			cell038.setCellStyle(dataStyle);
//
//			Cell cell039 = row.createCell(39);
//			cell039.setCellValue(formatValue(error.getErrorMsg()));
//			cell039.setCellStyle(dataStyle);
//
//			Cell cell040 = row.createCell(40);
//			cell040.setCellValue(formatValue(error.getCreatedDate()));
//			cell040.setCellStyle(dataStyle);
//
//		}

			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("APS_CODE")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("I_BOX_ID")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("SUPPLIER_ID")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("DATES")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("SUSPENDED")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("NAME_OF_CHANNEL")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(error.get("TYPE_OF_DSA")));
				cell06.setCellStyle(dataStyle);

				Cell cell07 = row.createCell(7);
				cell07.setCellValue(formatValue(error.get("PAN_NO")));
				cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(8);
				cell08.setCellValue(formatValue(error.get("YY")));
				cell08.setCellStyle(dataStyle);

				Cell cell09 = row.createCell(9);
				cell09.setCellValue(formatValue(error.get("SUPPLIER_ID_1")));
				cell09.setCellStyle(dataStyle);

				Cell cell010 = row.createCell(10);
				cell010.setCellValue(formatValue(error.get("SUPPLIER_ID_2")));
				cell010.setCellStyle(dataStyle);

				Cell cell011 = row.createCell(11);
				cell011.setCellValue(formatValue(error.get("SUPPLIER_ID_3")));
				cell011.setCellStyle(dataStyle);

				Cell cell012 = row.createCell(12);
				cell012.setCellValue(formatValue(error.get("SUPPLIER_ID_4")));
				cell012.setCellStyle(dataStyle);

				Cell cell013 = row.createCell(13);
				cell013.setCellValue(formatValue(error.get("SUPPLIER_ID_5")));
				cell013.setCellStyle(dataStyle);

				Cell cell014 = row.createCell(14);
				cell014.setCellValue(formatValue(error.get("SUPPLIER_ID_6")));
				cell014.setCellStyle(dataStyle);

				Cell cell015 = row.createCell(15);
				cell015.setCellValue(formatValue(error.get("SUPPLIER_ID_7")));
				cell015.setCellStyle(dataStyle);

				Cell cell016 = row.createCell(16);
				cell016.setCellValue(formatValue(error.get("SUPPLIER_ID_8")));
				cell016.setCellStyle(dataStyle);

				Cell cell017 = row.createCell(17);
				cell017.setCellValue(formatValue(error.get("SUPPLIER_ID_9")));
				cell017.setCellStyle(dataStyle);

				Cell cell018 = row.createCell(18);
				cell018.setCellValue(formatValue(error.get("SUPPLIER_ID_10")));
				cell018.setCellStyle(dataStyle);

				Cell cell019 = row.createCell(19);
				cell019.setCellValue(formatValue(error.get("SUPPLIER_ID_11")));
				cell019.setCellStyle(dataStyle);

				Cell cell020 = row.createCell(20);
				cell020.setCellValue(formatValue(error.get("SUPPLIER_ID_12")));
				cell020.setCellStyle(dataStyle);

				Cell cell021 = row.createCell(21);
				cell021.setCellValue(formatValue(error.get("REMARK")));
				cell021.setCellStyle(dataStyle);

				Cell cell022 = row.createCell(22);
				cell022.setCellValue(formatValue(error.get("LOCATION")));
				cell022.setCellStyle(dataStyle);

				Cell cell023 = row.createCell(23);
				cell023.setCellValue(formatValue(error.get("MIS_STATE")));
				cell023.setCellStyle(dataStyle);

				Cell cell024 = row.createCell(24);
				cell024.setCellValue(formatValue(error.get("C_STATE")));
				cell024.setCellStyle(dataStyle);

				Cell cell025 = row.createCell(25);
				cell025.setCellValue(formatValue(error.get("ED_STATE")));
				cell025.setCellStyle(dataStyle);

				Cell cell026 = row.createCell(26);
				cell026.setCellValue(formatValue(error.get("ED_ZONE")));
				cell026.setCellStyle(dataStyle);

				Cell cell027 = row.createCell(27);
				cell027.setCellValue(formatValue(error.get("SOURCING")));
				cell027.setCellStyle(dataStyle);

				Cell cell028 = row.createCell(28);
				cell028.setCellValue(formatValue(error.get("SOURCING_1")));
				cell028.setCellStyle(dataStyle);

				Cell cell029 = row.createCell(29);
				cell029.setCellValue(formatValue(error.get("MANUFACTU_NAME")));
				cell029.setCellStyle(dataStyle);

				Cell cell030 = row.createCell(30);
				cell030.setCellValue(formatValue(error.get("NEW_MANUFACTU_NAME")));
				cell030.setCellStyle(dataStyle);

				Cell cell031 = row.createCell(31);
				cell031.setCellValue(formatValue(error.get("OLD_I_BOX_ID")));
				cell031.setCellStyle(dataStyle);

				Cell cell032 = row.createCell(32);
				cell032.setCellValue(formatValue(error.get("RC_LIMIT")));
				cell032.setCellStyle(dataStyle);

				Cell cell033 = row.createCell(33);
				cell033.setCellValue(formatValue(error.get("SAP_CODE")));
				cell033.setCellStyle(dataStyle);

				Cell cell034 = row.createCell(34);
				cell034.setCellValue(formatValue(error.get("ACCOUNT_NO")));
				cell034.setCellStyle(dataStyle);

				Cell cell035 = row.createCell(35);
				cell035.setCellValue(formatValue(error.get("IFSC_CODE")));
				cell035.setCellStyle(dataStyle);

				Cell cell036 = row.createCell(36);
				cell036.setCellValue(formatValue(error.get("BANK_NAME")));
				cell036.setCellStyle(dataStyle);

				Cell cell037 = row.createCell(37);
				cell037.setCellValue(formatValue(error.get("I_BANK_YES_NON_I_BANK_NO")));
				cell037.setCellStyle(dataStyle);

//			Cell cell038 = row.createCell(38);
//			cell038.setCellValue(formatValue(error.get("ROW_NUMBER")));
//			cell038.setCellStyle(dataStyle);

				Cell cell039 = row.createCell(38);
				cell039.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell039.setCellStyle(dataStyle);

				Cell cell040 = row.createCell(39);
				cell040.setCellValue(formatValue(error.get("CREATED_DATE")));
				cell040.setCellStyle(dataStyle);

			}

			// ---------------------------------------------------
			// Step 5: Auto size columns
			// ---------------------------------------------------

			for (int i = 0; i < 40; i++) {
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

	private Set<Integer> fetchExistingInBatch(Set<Integer> uploadedPKSet) {

		if (uploadedPKSet == null || uploadedPKSet.isEmpty())
			return Collections.emptySet();

		List<Integer> pkList = new ArrayList<>(uploadedPKSet);
		Set<Integer> existingRecords = new HashSet<>();

		int batchSize = 900;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, pkList.size());

			List<Integer> batch = pkList.subList(start, end);

			List<Integer> result = entityManager
					.createQuery("SELECT g.apsCode FROM ChannelMasterTemp g WHERE g.apsCode IN :list", Integer.class)
					.setParameter("list", batch).setHint("org.hibernate.readOnly", true).getResultList();

			existingRecords.addAll(result);
		}

		return existingRecords;
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private ChannelMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		ChannelMasterTemp entity = new ChannelMasterTemp();

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

	private ChannelMasterError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap,
			DataFormatter formatter) throws Exception {

		ChannelMasterError errorEntity = new ChannelMasterError();
		logger.info("Mapping Error row to Field : {}", row.getRowNum());

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
		return val.isEmpty() || val.equalsIgnoreCase("NA") || val.equalsIgnoreCase("N/A")
				|| val.equalsIgnoreCase("#N/A") || val.equalsIgnoreCase("NONE") || val.equalsIgnoreCase("NULL")
				|| val.equalsIgnoreCase("NAN") || val.equalsIgnoreCase("-") || val.equalsIgnoreCase("#")
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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_CHANNEL_MST_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	// ============================================================
	// Batch Insert Channel Temp
	// ============================================================
	@Transactional
	public void batchInsertStage(List<ChannelMasterTemp> entityList, String uploadId) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}

		logger.info("Starting JDBC batch insert for TEMP table. Total records: {}", entityList.size());

		String sql = "INSERT INTO TM_VHL_CHANNEL_MST_STG (" + "APS_CODE, I_BOX_ID, SUPPLIER_ID, DATES, SUSPENDED, "
				+ "NAME_OF_CHANNEL, TYPE_OF_DSA, PAN_NO, YY, "
				+ "SUPPLIER_ID_1, SUPPLIER_ID_2, SUPPLIER_ID_3, SUPPLIER_ID_4, SUPPLIER_ID_5, "
				+ "SUPPLIER_ID_6, SUPPLIER_ID_7, SUPPLIER_ID_8, SUPPLIER_ID_9, SUPPLIER_ID_10, "
				+ "SUPPLIER_ID_11, SUPPLIER_ID_12, REMARK, LOCATION, MIS_STATE, "
				+ "C_STATE, ED_STATE, ED_ZONE, SOURCING, SOURCING_1, "
				+ "MANUFACTU_NAME, NEW_MANUFACTU_NAME, OLD_I_BOX_ID, RC_LIMIT, SAP_CODE, "
				+ "ACCOUNT_NO, IFSC_CODE, BANK_NAME, I_BANK_YES_NON_I_BANK_NO, CREATED_BY, "
				+ "CREATED_DATE, MODIFIED_BY, MODIFIED_DATE, REMARK_BY_CHK, STATUS, "
				+ "ACTION_TYPE, ACTION_DATE, ACTION_USER, UPLOAD_ID, FILE_NAME )" + " VALUES (" + " ?, ?, ?, ?, ?," + " ?, ?, ?, ? ,"
				+ " ? ,?, ?, ?, ?," + " ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?," + "?, ?, ?, ?, ?" + ")"
				+ "LOG ERRORS INTO TM_VHL_CHANNEL_MST_ERROR ('" + uploadId + "') " + "REJECT LIMIT UNLIMITED";

		int batchSize = 500;

		long start = System.currentTimeMillis();

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, ChannelMasterTemp entity) -> {

			// 1 APS_CODE
			if(entity.getApsCode() != null) {
				ps.setInt(1, entity.getApsCode());
			}else {
				ps.setNull(1, Types.INTEGER);
			}
			// 2 I_BOX_ID
			ps.setString(2, entity.getiBoxId());

			// 3 SUPPLIER_ID
			if( entity.getSupplierId() != null) {
				ps.setLong(3, entity.getSupplierId());
			}else {
				ps.setNull(3, Types.BIGINT);
			}
			

			// 4 DATES
			if (entity.getDates() != null) {
				ps.setTimestamp(4, new Timestamp(entity.getDates().getTime()));
			} else {
				ps.setNull(4, Types.TIMESTAMP);
			}

			// 5 SUSPENDED
			ps.setString(5, entity.getSuspended());

			// 6 NAME_OF_CHANNEL
			ps.setString(6, entity.getNameOfChannel());

			// 7 TYPE_OF_DSA
			ps.setString(7, entity.getTypeOfDsa());

			// 8 PAN_NO
			ps.setString(8, entity.getPanNo());

			// 9 YY
			ps.setString(9, entity.getYy());

			// 10 SUPPLIER_ID_1
			if(entity.getSupplierId1() != null) {
				ps.setLong(10, entity.getSupplierId1());
			}else {
				ps.setNull(10, Types.BIGINT);
			}
			

			// 11 SUPPLIER_ID_2
			if(entity.getSupplierId2() != null) {
				ps.setLong(11, entity.getSupplierId2());
			}else {
				ps.setNull(11, Types.BIGINT);
			}
			

			// 12 SUPPLIER_ID_3
			if( entity.getSupplierId3() != null) {
				ps.setLong(12, entity.getSupplierId3());
			}else {
				ps.setNull(12, Types.BIGINT);
			}
			

			// 13 SUPPLIER_ID_4
			if(entity.getSupplierId4() != null) {
				ps.setLong(13, entity.getSupplierId4());
			}else {
				ps.setNull(13, Types.BIGINT);
			}
			

			// 14 SUPPLIER_ID_5
			if(entity.getSupplierId5() != null) {
				ps.setLong(14, entity.getSupplierId5());
			}else {
				ps.setNull(14, Types.BIGINT);
			}
			

			// 15 SUPPLIER_ID_6
			if(entity.getSupplierId6() != null) {
				ps.setLong(15, entity.getSupplierId6());
			}else {
				ps.setNull(15, Types.BIGINT);
			}
			

			// 16 SUPPLIER_ID_7
			if(entity.getSupplierId7() != null) {
				ps.setLong(16, entity.getSupplierId7());
			}else {
				ps.setNull(16, Types.BIGINT);
			}
			

			// 17 SUPPLIER_ID_8
			if(entity.getSupplierId8() != null) {
				ps.setLong(17, entity.getSupplierId8());
			}else {
				ps.setNull(17, Types.BIGINT);
			}
			

			// 18 SUPPLIER_ID_9
			if(entity.getSupplierId9() != null) {
				ps.setLong(18, entity.getSupplierId9());
			}else {
				ps.setNull(18, Types.BIGINT);
			}
			

			// 19 SUPPLIER_ID_10
			if(entity.getSupplierId10() != null) {
				ps.setLong(19, entity.getSupplierId10());
			}else {
				ps.setNull(19, Types.BIGINT);
			}
			

			// 20 SUPPLIER_ID_11
			if(entity.getSupplierId11() != null) {
				ps.setLong(20, entity.getSupplierId11());
			}else {
				ps.setNull(20, Types.BIGINT);
			}
			

			// 21 SUPPLIER_ID_12
			if(entity.getSupplierId12() != null) {
				ps.setLong(21, entity.getSupplierId12());
			}else {
				ps.setNull(21, Types.BIGINT);
			}
			

			// 22 REMARK
			ps.setString(22, entity.getRemark());

			// 23 LOCATION
			ps.setString(23, entity.getLocation());

			// 24 MIS_STATE
			ps.setString(24, entity.getMisState());

			// 25 C_STATE
			ps.setString(25, entity.getcState());

			// 26 ED_STATE
			ps.setString(26, entity.getEdState());

			// 27 ED_ZONE
			ps.setString(27, entity.getEdZone());

			// 28 SOURCING
			ps.setString(28, entity.getSourcing());

			// 29 SOURCING_1
			ps.setString(29, entity.getSourcing1());

			// 30 MANUFACTU_NAME
			ps.setString(30, entity.getManufactuName());

			// 31 NEW_MANUFACTU_NAME
			ps.setString(31, entity.getNewManufactuName());

			// 32 OLD_I_BOX_ID
			ps.setString(32, entity.getOldIBoxId());

			// 33 RC_LIMIT
			ps.setString(33, entity.getRcLimit());

			// 34 SAP_CODE
			ps.setString(34, entity.getSapCode());

			// 35 ACCOUNT_NO
			if(entity.getAccountNo() != null) {
				ps.setLong(35, entity.getAccountNo());
			}else {
				ps.setNull(35, Types.BIGINT);
			}
			

			// 36 IFSC_CODE
			ps.setString(36, entity.getIfscCode());

			// 37 BANK_NAME
			ps.setString(37, entity.getBankName());

			// 38 I_BANK_YES_NON_I_BANK_NO
			ps.setString(38, entity.getiBankYesNonIBankNo());

			// 39 CREATED_BY
			ps.setString(39, entity.getCreatedBy());

			// 40 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(40, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(40, Types.TIMESTAMP);
			}

			// 41 MODIFIED_BY
			ps.setString(41, entity.getModifiedBy());

			// 42 MODIFIED_DATE
			if (entity.getModifiedDate() != null) {
				ps.setTimestamp(42, new Timestamp(entity.getModifiedDate().getTime()));
			} else {
				ps.setNull(42, Types.TIMESTAMP);
			}

			// 43 REMARK_BY_CHK
			ps.setString(43, entity.getRemarkByChk());

			// 44 STATUS
			ps.setString(44, entity.getStatus());

			// 45 ACTION_TYPE
			ps.setString(45, entity.getActionType());

			// 46 ACTION_DATE
			if (entity.getActionDate() != null) {
				ps.setTimestamp(46, new Timestamp(entity.getActionDate().getTime()));
			} else {
				ps.setNull(46, Types.TIMESTAMP);
			}

			// 47 ACTION_USER
			ps.setString(47, entity.getActionUser());
			
			// 48 UploadId
			ps.setString(48, entity.getUploadId());
						
			// 49 FileName
			ps.setString(49, entity.getFileName());
		});

		logger.info("Channel TEMP batch insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// Batch Insert Channel Error
	// ============================================================
	@Transactional
	public void batchInsertError(List<ChannelMasterError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for Channel ERROR table. Total records: {}", errorList.size());

		long startTime = System.currentTimeMillis();

		// SQL without ID column because it is auto-generated
		String sql = "INSERT INTO TM_VHL_CHANNEL_MST_ERROR (" + "APS_CODE, I_BOX_ID, SUPPLIER_ID, DATES, SUSPENDED, "
				+ "NAME_OF_CHANNEL, TYPE_OF_DSA, PAN_NO, YY, "
				+ "SUPPLIER_ID_1, SUPPLIER_ID_2, SUPPLIER_ID_3, SUPPLIER_ID_4, SUPPLIER_ID_5, "
				+ "SUPPLIER_ID_6, SUPPLIER_ID_7, SUPPLIER_ID_8, SUPPLIER_ID_9, SUPPLIER_ID_10, "
				+ "SUPPLIER_ID_11, SUPPLIER_ID_12, REMARK, LOCATION, MIS_STATE, "
				+ "C_STATE, ED_STATE, ED_ZONE, SOURCING, SOURCING_1, "
				+ "MANUFACTU_NAME, NEW_MANUFACTU_NAME, OLD_I_BOX_ID, RC_LIMIT, SAP_CODE, "
				+ "ACCOUNT_NO, IFSC_CODE, BANK_NAME, I_BANK_YES_NON_I_BANK_NO,"
				+ "CREATED_BY, CREATED_DATE, ERROR_MSG, UPLOAD_ID, ROW_NUMBER " + ")" + " VALUES (?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ? ," + " ? ,?, ?, ?, ?," + " ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?," + " ?, ?, ?, ?, " + " ?, ?, ?, ?, ?" + ")";

		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, ChannelMasterError entity) -> {

			// 1 APS_CODE
			ps.setString(1, entity.getApsCode());

			// 2 I_BOX_ID
			ps.setString(2, entity.getiBoxId());

			// 3 SUPPLIER_ID
			ps.setString(3, entity.getSupplierId());

			// 4 DATES
			ps.setString(4, entity.getDates());

			// 5 SUSPENDED
			ps.setString(5, entity.getSuspended());

			// 6 NAME_OF_CHANNEL
			ps.setString(6, entity.getNameOfChannel());

			// 7 TYPE_OF_DSA
			ps.setString(7, entity.getTypeOfDsa());

			// 8 PAN_NO
			ps.setString(8, entity.getPanNo());

			// 9 YY
			ps.setString(9, entity.getYy());

			// 10 SUPPLIER_ID_1
			ps.setString(10, entity.getSupplierId1());

			// 11 SUPPLIER_ID_2
			ps.setString(11, entity.getSupplierId2());

			// 12 SUPPLIER_ID_3
			ps.setString(12, entity.getSupplierId3());

			// 13 SUPPLIER_ID_4
			ps.setString(13, entity.getSupplierId4());

			// 14 SUPPLIER_ID_5
			ps.setString(14, entity.getSupplierId5());

			// 15 SUPPLIER_ID_6
			ps.setString(15, entity.getSupplierId6());

			// 16 SUPPLIER_ID_7
			ps.setString(16, entity.getSupplierId7());

			// 17 SUPPLIER_ID_8
			ps.setString(17, entity.getSupplierId8());

			// 18 SUPPLIER_ID_9
			ps.setString(18, entity.getSupplierId9());

			// 19 SUPPLIER_ID_10
			ps.setString(19, entity.getSupplierId10());

			// 20 SUPPLIER_ID_11
			ps.setString(20, entity.getSupplierId11());

			// 21 SUPPLIER_ID_12
			ps.setString(21, entity.getSupplierId12());

			// 22 REMARK
			ps.setString(22, entity.getRemark());

			// 23 LOCATION
			ps.setString(23, entity.getLocation());

			// 24 MIS_STATE
			ps.setString(24, entity.getMisState());

			// 25 C_STATE
			ps.setString(25, entity.getcState());

			// 26 ED_STATE
			ps.setString(26, entity.getEdState());

			// 27 ED_ZONE
			ps.setString(27, entity.getEdZone());

			// 28 SOURCING
			ps.setString(28, entity.getSourcing());

			// 29 SOURCING_1
			ps.setString(29, entity.getSourcing1());

			// 30 MANUFACTU_NAME
			ps.setString(30, entity.getManufactuName());

			// 31 NEW_MANUFACTU_NAME
			ps.setString(31, entity.getNewManufactuName());

			// 32 OLD_I_BOX_ID
			ps.setString(32, entity.getOldIBoxId());

			// 33 RC_LIMIT
			ps.setString(33, entity.getRcLimit());

			// 34 SAP_CODE
			ps.setString(34, entity.getSapCode());

			// 35 ACCOUNT_NO
			ps.setString(35, entity.getAccountNo());

			// 36 IFSC_CODE
			ps.setString(36, entity.getIfscCode());

			// 37 BANK_NAME
			ps.setString(37, entity.getBankName());

			// 38 I_BANK_YES_NON_I_BANK_NO
			ps.setString(38, entity.getiBankYesNonIBankNo());

			// 39 CREATED_BY
			ps.setString(39, entity.getCreatedBy());

			// 40 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(40, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(40, Types.TIMESTAMP);
			}

			// 41 ERROR_MSG
			ps.setString(41, entity.getErrorMsg());

			// 42 UPLOAD_ID
			ps.setString(42, entity.getUploadId());

			// 43 ROW_NUMBER
			if (entity.getRowNumber() != null) {
				ps.setInt(43, entity.getRowNumber());
			} else {
				ps.setNull(43, Types.BIGINT);
			}
		});

		logger.info("Channel ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
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
	public void process(List<ChannelMasterTemp> validEntityList, List<ChannelMasterError> invalidIntityList,
			String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into stage
			batchInsertStage(validEntityList, uploadId);

			logger.info("batch update stage to temp");
			// update Existing
			updateTemp(uploadId);

			logger.info("batch insert stage to temp");
			// insert new
			insertTemp(uploadId);
			
			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Channel Master File upload Process Failed");
		} finally {
			jdbcTemplate.execute("DELETE FROM TM_VHL_CHANNEL_MST_STG");
		}
	}

	private void insertTemp(String uploadId) {

		logger.info(" insert Channel Master Temp start");

		String insertSql = "INSERT /*+ APPEND PARALLEL(4) */ "
				+ "INTO TM_VHL_CHANNEL_MST_TEMP ( "
				+ "    APS_CODE, "
				+ "    I_BOX_ID, "
				+ "    SUPPLIER_ID, "
				+ "    DATES, "
				+ "    SUSPENDED, "
				+ "    NAME_OF_CHANNEL, "
				+ "    TYPE_OF_DSA, "
				+ "    PAN_NO, "
				+ "    YY, "
				+ "    SUPPLIER_ID_1, "
				+ "    SUPPLIER_ID_2, "
				+ "    SUPPLIER_ID_3, "
				+ "    SUPPLIER_ID_4, "
				+ "    SUPPLIER_ID_5, "
				+ "    SUPPLIER_ID_6, "
				+ "    SUPPLIER_ID_7, "
				+ "    SUPPLIER_ID_8, "
				+ "    SUPPLIER_ID_9, "
				+ "    SUPPLIER_ID_10, "
				+ "    SUPPLIER_ID_11, "
				+ "    SUPPLIER_ID_12, "
				+ "    REMARK, "
				+ "    LOCATION, "
				+ "    MIS_STATE, "
				+ "    C_STATE, "
				+ "    ED_STATE, "
				+ "    ED_ZONE, "
				+ "    SOURCING, "
				+ "    SOURCING_1, "
				+ "    MANUFACTU_NAME, "
				+ "    NEW_MANUFACTU_NAME, "
				+ "    OLD_I_BOX_ID, "
				+ "    RC_LIMIT, "
				+ "    SAP_CODE, "
				+ "    ACCOUNT_NO, "
				+ "    IFSC_CODE, "
				+ "    BANK_NAME, "
				+ "    I_BANK_YES_NON_I_BANK_NO, "
				+ "    CREATED_BY, "
				+ "    CREATED_DATE, "
				+ "    STATUS, "
				+ "    ACTION_TYPE, "
				+ "    ACTION_DATE, "
				+ "    ACTION_USER, "
				+ "    REMARK_BY_CHK, "
				+ "    UPLOAD_ID, "
				+ "    FILE_NAME "
				+ ") "
				+ "SELECT "
				+ "    src.APS_CODE, "
				+ "    src.I_BOX_ID, "
				+ "    src.SUPPLIER_ID, "
				+ "    src.DATES, "
				+ "    src.SUSPENDED, "
				+ "    src.NAME_OF_CHANNEL, "
				+ "    src.TYPE_OF_DSA, "
				+ "    src.PAN_NO, "
				+ "    src.YY, "
				+ "    src.SUPPLIER_ID_1, "
				+ "    src.SUPPLIER_ID_2, "
				+ "    src.SUPPLIER_ID_3, "
				+ "    src.SUPPLIER_ID_4, "
				+ "    src.SUPPLIER_ID_5, "
				+ "    src.SUPPLIER_ID_6, "
				+ "    src.SUPPLIER_ID_7, "
				+ "    src.SUPPLIER_ID_8, "
				+ "    src.SUPPLIER_ID_9, "
				+ "    src.SUPPLIER_ID_10, "
				+ "    src.SUPPLIER_ID_11, "
				+ "    src.SUPPLIER_ID_12, "
				+ "    src.REMARK, "
				+ "    src.LOCATION, "
				+ "    src.MIS_STATE, "
				+ "    src.C_STATE, "
				+ "    src.ED_STATE, "
				+ "    src.ED_ZONE, "
				+ "    src.SOURCING, "
				+ "    src.SOURCING_1, "
				+ "    src.MANUFACTU_NAME, "
				+ "    src.NEW_MANUFACTU_NAME, "
				+ "    src.OLD_I_BOX_ID, "
				+ "    src.RC_LIMIT, "
				+ "    src.SAP_CODE, "
				+ "    src.ACCOUNT_NO, "
				+ "    src.IFSC_CODE, "
				+ "    src.BANK_NAME, "
				+ "    src.I_BANK_YES_NON_I_BANK_NO, "
				+ "    src.CREATED_BY, "
				+ "    src.CREATED_DATE, "
				+ "    src.STATUS, "
				+ "    src.ACTION_TYPE, "
				+ "    src.ACTION_DATE, "
				+ "    src.ACTION_USER, "
				+ "    src.REMARK_BY_CHK, "
				+ "    src.UPLOAD_ID, "
				+ "    src.FILE_NAME "
				+ "FROM TM_VHL_CHANNEL_MST_STG src "
				+ "WHERE src.UPLOAD_ID = ?  "
				+ "AND NOT EXISTS ( "
				+ "    SELECT 1 "
				+ "    FROM TM_VHL_CHANNEL_MST_TEMP t "
				+ "    WHERE t.APS_CODE = src.APS_CODE "
				+ ") "
				+ "LOG ERRORS INTO TM_VHL_CHANNEL_MST_ERROR (?) "
				+ "REJECT LIMIT UNLIMITED";

		jdbcTemplate.update(insertSql, uploadId, uploadId);

	}

	private void updateTemp(String uploadId) {

		logger.info(" update Channel Master Temp start");

		long updateStart = System.currentTimeMillis();

		String updateSql = "UPDATE TM_VHL_CHANNEL_MST_TEMP t "
				+ "SET ( "
				+ "    I_BOX_ID, "
				+ "    SUPPLIER_ID, "
				+ "    DATES, "
				+ "    SUSPENDED, "
				+ "    NAME_OF_CHANNEL, "
				+ "    TYPE_OF_DSA, "
				+ "    PAN_NO, "
				+ "    YY, "
				+ "    SUPPLIER_ID_1, "
				+ "    SUPPLIER_ID_2, "
				+ "    SUPPLIER_ID_3, "
				+ "    SUPPLIER_ID_4, "
				+ "    SUPPLIER_ID_5, "
				+ "    SUPPLIER_ID_6, "
				+ "    SUPPLIER_ID_7, "
				+ "    SUPPLIER_ID_8, "
				+ "    SUPPLIER_ID_9, "
				+ "    SUPPLIER_ID_10, "
				+ "    SUPPLIER_ID_11, "
				+ "    SUPPLIER_ID_12, "
				+ "    REMARK, "
				+ "    LOCATION, "
				+ "    MIS_STATE, "
				+ "    C_STATE, "
				+ "    ED_STATE, "
				+ "    ED_ZONE, "
				+ "    SOURCING, "
				+ "    SOURCING_1, "
				+ "    MANUFACTU_NAME, "
				+ "    NEW_MANUFACTU_NAME, "
				+ "    OLD_I_BOX_ID, "
				+ "    RC_LIMIT, "
				+ "    SAP_CODE, "
				+ "    ACCOUNT_NO, "
				+ "    IFSC_CODE, "
				+ "    BANK_NAME, "
				+ "    I_BANK_YES_NON_I_BANK_NO, "
				+ "    MODIFIED_BY, "
				+ "    MODIFIED_DATE, "
				+ "    STATUS, "
				+ "    ACTION_TYPE, "
				+ "    ACTION_DATE, "
				+ "    ACTION_USER, "
				+ "    REMARK_BY_CHK, "
				+ "    UPLOAD_ID, "
				+ "    FILE_NAME "
				+ ") = "
				+ "( "
				+ "    SELECT /*+ INDEX(s IDX_CHANNEL_STG_UPL_APS) */ "
				+ "        s.I_BOX_ID, "
				+ "        s.SUPPLIER_ID, "
				+ "        s.DATES, "
				+ "        s.SUSPENDED, "
				+ "        s.NAME_OF_CHANNEL, "
				+ "        s.TYPE_OF_DSA, "
				+ "        s.PAN_NO, "
				+ "        s.YY, "
				+ "        s.SUPPLIER_ID_1, "
				+ "        s.SUPPLIER_ID_2, "
				+ "        s.SUPPLIER_ID_3, "
				+ "        s.SUPPLIER_ID_4, "
				+ "        s.SUPPLIER_ID_5, "
				+ "        s.SUPPLIER_ID_6, "
				+ "        s.SUPPLIER_ID_7, "
				+ "        s.SUPPLIER_ID_8, "
				+ "        s.SUPPLIER_ID_9, "
				+ "        s.SUPPLIER_ID_10, "
				+ "        s.SUPPLIER_ID_11, "
				+ "        s.SUPPLIER_ID_12, "
				+ "        s.REMARK, "
				+ "        s.LOCATION, "
				+ "        s.MIS_STATE, "
				+ "        s.C_STATE, "
				+ "        s.ED_STATE, "
				+ "        s.ED_ZONE, "
				+ "        s.SOURCING, "
				+ "        s.SOURCING_1, "
				+ "        s.MANUFACTU_NAME, "
				+ "        s.NEW_MANUFACTU_NAME, "
				+ "        s.OLD_I_BOX_ID, "
				+ "        s.RC_LIMIT, "
				+ "        s.SAP_CODE, "
				+ "        s.ACCOUNT_NO, "
				+ "        s.IFSC_CODE, "
				+ "        s.BANK_NAME, "
				+ "        s.I_BANK_YES_NON_I_BANK_NO, "
				+ "        s.MODIFIED_BY, "
				+ "        s.MODIFIED_DATE, "
				+ "        s.STATUS, "
				+ "        'U', "
				+ "        s.ACTION_DATE, "
				+ "        s.ACTION_USER, "
				+ "        s.REMARK_BY_CHK, "
				+ "        s.UPLOAD_ID, "
				+ "        s.FILE_NAME "
				+ "    FROM TM_VHL_CHANNEL_MST_STG s "
				+ "    WHERE s.APS_CODE = t.APS_CODE "
				+ "      AND s.UPLOAD_ID = ? "
				+ ") "
				+ "WHERE t.APS_CODE IN "
				+ "( "
				+ "    SELECT /*+ INDEX(s IDX_CHANNEL_STG_UPL_APS) */ "
				+ "        s.APS_CODE "
				+ "    FROM TM_VHL_CHANNEL_MST_STG s "
				+ "    WHERE s.UPLOAD_ID = ? "
				+ ") "
				+ "LOG ERRORS INTO TM_VHL_CHANNEL_MST_ERROR (?) " + "REJECT LIMIT UNLIMITED";

		jdbcTemplate.update(updateSql, uploadId, uploadId, uploadId);

		logger.info("Total update : " + (System.currentTimeMillis() - updateStart));

	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_CHANNEL_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='I'",
				Integer.class, uploadId, originalFilename);

		int updateCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_CHANNEL_MST_TEMP WHERE UPLOAD_ID=? AND FILE_NAME=? AND ACTION_TYPE='U'",
				Integer.class, uploadId, originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_CHANNEL_MST_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("updateCount", updateCount);
		result.put("errorCount", errorCount);

		return result;
	}
}

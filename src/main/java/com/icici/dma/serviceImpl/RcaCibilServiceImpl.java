package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.model.RCA_CIBIL;
import com.icici.dma.model.RCA_CIBIL_Error;
import com.icici.dma.repository.RCASCibilErrorRepository;
import com.icici.dma.repository.RcaCibilRepository;

@Service
public class RcaCibilServiceImpl {

	private static final Logger logger = LogManager.getLogger(RcaCibilServiceImpl.class);

	@Autowired
	private RcaCibilRepository repository;

	@Autowired
	private RCASCibilErrorRepository rcaCibilErrorrepository;

	public static String uploadId;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "rcasNo";

	static {

		HEADER_MAP.put("RCASNO", "rcasNo");
		HEADER_MAP.put("LANNO", "lanNo");
		HEADER_MAP.put("DISBURSEMENTDATE", "disbursementDate");
		HEADER_MAP.put("CIBILSCORE", "cibilScore");
		HEADER_MAP.put("APPLICANTTYPE", "applicantType");
		HEADER_MAP.put("CUSTOMERNAME", "customerName");
		HEADER_MAP.put("CUSTOMERTYPE", "customerType");

		// Cache all fields once
		for (Field field : RCA_CIBIL.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : RCA_CIBIL_Error.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	public Map<String, Integer> saveCibilSheet(Sheet sheet, String user, Date cycleFromDate, Date cycleToDate,
			String uploadId, String fileName) {

		// uploadId = generateUploadId("CIBIL&Entity mapping");
		long startTime = System.currentTimeMillis();
		logger.info("========== RCA UPLOAD STARTED ==========");

		if (sheet == null) {
			throw new RuntimeException("File is empty");
		}

		try {

			DataFormatter formatter = new DataFormatter();

			// ========================================================
			// HEADER VALIDATION
			// ========================================================

			logger.info("Header Validation Started");

			Row headerRow = sheet.getRow(0);

			// hold column index and entity field ==> [ 0 -> Field (branchCode)
			// ]
			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			logger.info("Header Validation Complate . Total column mapped : {}", columnFieldMap.size());

			// ========================================================
			// PRIMARY KEY IDENTIFICATION
			// ========================================================

			// hold thw which column index hold the primary key ==> 1
			Integer pkColumnIndex = getPrimaryKeyColumnIndex(columnFieldMap);

			// hold the valid primary key set ==> ["ABC12", "ABC17", "ABC78"]
			Set<String> validPKSet = new HashSet<>();

			// hold primary key valye and row number were appear ==> [ "AB12" ->
			// [2,4,7] ]
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
					// Integer pk = Integer.parseInt(value);
					String pk = value.trim().toUpperCase();

					// store row index against pk
					pkRowMap.computeIfAbsent(pk, k -> new ArrayList<>()).add(r);

					validPKSet.add(pk);
					// if (!validPKSet.add(pk)) {
					// duplicateExcelRows.add(r);
					// }

				} catch (Exception e) {
					logger.info("Row index {} marked as invalid due to PK Parse Error. value :{}", r, value);
					invalidRows.add(r);
				}

			}

			logger.info("Total Invalid PK rows: {}", invalidRows.size());
			logger.info("Duplicate PK rows in Excel: {}", duplicateExcelRows.size());

			// ========================================================
			// Excel DUPLICATE CHECK -> check each pk row value occurs in which
			// row
			// ========================================================
			for (Map.Entry<String, List<Integer>> entry : pkRowMap.entrySet()) {

				List<Integer> rows = entry.getValue();

				// entry.getValue();

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
////				Set<String> existingInDB = fetchExistingInBatch(validPKSet);
//				Set<String> existingInDB = fetchExistingInBatch(validPKSet, cycleFromDate, cycleToDate);
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

			// Set<Integer> rowsToSkip = new HashSet<>(errorReasonMap.keySet());
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

					logger.info("Row {} marked VALID.", r);
				}
			}

			logger.info("Valid row count : {}", validRowsList.size());
			logger.info("Invalid row count: {}", invalidRowsList.size());

			// ========================================================
			// PARALLEL VALID ENTITY CREATION
			// ========================================================

			long entityStartTime = System.currentTimeMillis();
			logger.info("======= Valid Entity cration Started ============");
			logger.info("Total Valid Rows for processing: {}", validRowsList.size());

			List<RCA_CIBIL> entityList = validRowsList.parallelStream().map(row -> {

				try {
					RCA_CIBIL entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());
					entity.setFromCycleDate(cycleFromDate);
					entity.setToCycleDate(cycleToDate);
					entity.setUploadId(uploadId);
					entity.setFileName(fileName);

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

			// genrate unique Upload Id
			logger.info("Upload Id : {}", uploadId);

			long entityStartTime1 = System.currentTimeMillis();

			logger.info("======= Error Entity cration Started ============");
			logger.info("Total Invalid Rows for processing: {}", invalidRowsList.size());

			List<RCA_CIBIL_Error> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());
				
				try {
					RCA_CIBIL_Error entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

					entity.setCreatedBy(user);
					entity.setFromCycleDate(cycleFromDate);
					entity.setToCycleDate(cycleToDate);
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
			logger.info(" Entities creation comleted in {} ms : {}", (System.currentTimeMillis() - entityStartTime1));
			logger.info("========== Error Entity Creation Completed ================");

			// ========================================================
			// 🔟 BATCH SAVE USING ENTITY MANAGER
			// ========================================================
			
//			batchInsertTemp(entityList);
//			logger.info("Batch Insert");
//			batchInsertError(errorEntityList);
//			logger.info("Error Batch Insert");
//
//			logger.info("========== GST UPLOAD COMPLETED ==========");
//
//			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));
//			logger.info("Upload completed successfully.\n" + "Inserted: " + entityList.size() + "\n" + "Skipped: "
//					+ rowsToSkip.size() + "\n" + "Invalid Rows: " + invalidRows.size() + "\n" + "Duplicate in Excel: "
//					+ duplicateExcelRows.size() + "\n" + "Duplicate in DB: " + dbDuplicateRows.size());
//
//			Map<String, Object> response = new HashMap<>();
//
//			response.put("totalRecords", entityList.size() + rowsToSkip.size());
//			response.put("successfulRecords", entityList.size());
//			response.put("UnsuccessfulRecords", rowsToSkip.size());
//			response.put("invalidRecords", invalidRows.size());
//			response.put("duplicateInFile", duplicateExcelRows.size());
//			response.put("alreadyExistingRecords", dbDuplicateRows.size());
//
//			boolean errorFileAvailable = invalidRows.size() > 0 || duplicateExcelRows.size() > 0
//					|| dbDuplicateRows.size() > 0;
//
//			response.put("errorFileAvailable", errorFileAvailable);
//			return response;
			
			process(entityList, errorEntityList, cycleFromDate, cycleToDate, uploadId);
			
			Map<String, Integer> counts = getCounts(uploadId, fileName);

			counts.put("TotalRecordInFile", entityList.size() + rowsToSkip.size());
			
			String message = "Upload Completed Successfully " + "\n" 
							+ "Total Records in file : " + (entityList.size() + rowsToSkip.size()) + "\n" 
							+ "Count of added records : " + counts.get("insertCount") + "\n"
							+ "Count of error records : " + counts.get("errorCount");

			
			logger.info(counts);

			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));
			logger.info("========== RCAS CBC DUMP UPLOAD COMPLETED ==========");

			return counts;

		} catch (Exception e2) {
			throw new RuntimeException("Model Upload Failed: " + e2.getMessage());
		}
	}

	@Transactional
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Starting error export for User: {}", user);

		// get latest upload Id
		String uploadId = rcaCibilErrorrepository.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

		if (uploadId == null || uploadId.isEmpty()) {
			throw new ResourceNotFoundException("No Error Record");
		}

		logger.info("Starting error export for uploadId: {}", uploadId);
		// ---------------------------------------------------
		// Step 1: Fetch error records from DB
		// ---------------------------------------------------

		List<RCA_CIBIL_Error> errors = rcaCibilErrorrepository.findByUploadIdOrderByRowNumber(uploadId);

		if (errors == null || errors.isEmpty()) {

			logger.warn("No error records found for uploadId: {}", uploadId);

			throw new IllegalArgumentException("No error records found for uploadId: " + uploadId);
		}

		logger.info("Total error records fetched: {}", errors.size());

		// ---------------------------------------------------
		// Step 2: Create Workbook
		// ---------------------------------------------------

		Workbook workbook = new XSSFWorkbook();

		Sheet sheet = workbook.createSheet("CIBIL_ERROR");

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
		cell0.setCellValue("RCAS_NO");
		cell0.setCellStyle(headerStyle);

		Cell cell1 = header.createCell(1);
		cell1.setCellValue("LAN_NO");
		cell1.setCellStyle(headerStyle);

		Cell cell2 = header.createCell(2);
		cell2.setCellValue("DISBURSEMENT_DATE");
		cell2.setCellStyle(headerStyle);

		Cell cell3 = header.createCell(3);
		cell3.setCellValue("CIBIL_SCORE");
		cell3.setCellStyle(headerStyle);

		Cell cell4 = header.createCell(4);
		cell4.setCellValue("APPLICANT_TYPE");
		cell4.setCellStyle(headerStyle);

		Cell cell5 = header.createCell(5);
		cell5.setCellValue("CUSTOMER_NAME");
		cell5.setCellStyle(headerStyle);

		Cell cell6 = header.createCell(6);
		cell6.setCellValue("CUSTOMER_TYPE");
		cell6.setCellStyle(headerStyle);

		Cell cell7 = header.createCell(7);
		cell7.setCellValue("CREATED_DATE");
		cell7.setCellStyle(headerStyle);

		Cell cell8 = header.createCell(8);
		cell8.setCellValue("CREATED_BY");
		cell8.setCellStyle(headerStyle);

//		Cell cell9 = header.createCell(9);
//		cell9.setCellValue("REMARKS");
//		cell9.setCellStyle(headerStyle);
//
//		Cell cell10 = header.createCell(10);
//		cell10.setCellValue("STATUS");
//		cell10.setCellStyle(headerStyle);

		Cell cell11 = header.createCell(9);
		cell11.setCellValue("ERROR_MSG");
		cell11.setCellStyle(headerStyle);

//		Cell cell12 = header.createCell(12);
//		cell12.setCellValue("ROW_NUMBER");
//		cell12.setCellStyle(headerStyle);
//
//		Cell cell13 = header.createCell(13);
//		cell13.setCellValue("ERROR_ID");
//		cell13.setCellStyle(headerStyle);
		// ---------------------------------------------------
		// Step 4: Write Data Rows
		// ---------------------------------------------------

		int rowIndex = 1;

		for (RCA_CIBIL_Error error : errors) {

			Row row = sheet.createRow(rowIndex++);

			Cell cell00 = row.createCell(0);
			cell00.setCellValue(formatValue(error.getRcasNo()));
			cell00.setCellStyle(dataStyle);

			Cell cell01 = row.createCell(1);
			cell01.setCellValue(formatValue(error.getLanNo()));
			cell01.setCellStyle(dataStyle);

			Cell cell02 = row.createCell(2);
			cell02.setCellValue(formatValue(error.getDisbursementDate()));
			cell02.setCellStyle(dataStyle);

			Cell cell03 = row.createCell(3);
			cell03.setCellValue(formatValue(error.getCibilScore()));
			cell03.setCellStyle(dataStyle);

			Cell cell04 = row.createCell(4);
			cell04.setCellValue(formatValue(error.getApplicantType()));
			cell04.setCellStyle(dataStyle);

			Cell cell05 = row.createCell(5);
			cell05.setCellValue(formatValue(error.getCustomerName()));
			cell05.setCellStyle(dataStyle);

			Cell cell06 = row.createCell(6);
			cell06.setCellValue(formatValue(error.getCustomerType()));
			cell06.setCellStyle(dataStyle);

			Cell cell07 = row.createCell(7);
			cell07.setCellValue(formatValue(error.getCreatedDate()));
			cell07.setCellStyle(dataStyle);

			Cell cell08 = row.createCell(8);
			cell08.setCellValue(formatValue(error.getCreatedBy()));
			cell08.setCellStyle(dataStyle);

//			Cell cell09 = row.createCell(9);
//			cell09.setCellValue(formatValue(error.getRemarks()));
//			cell09.setCellStyle(dataStyle);
//
//			Cell cell010 = row.createCell(10);
//			cell010.setCellValue(formatValue(error.getStatus()));
//			cell010.setCellStyle(dataStyle);

			Cell cell011 = row.createCell(9);
			cell011.setCellValue(formatValue(error.getErrorMsg()));
			cell011.setCellStyle(dataStyle);

//			Cell cell013 = row.createCell(10);
//			cell013.setCellValue(formatValue(error.getRowNumber()));
//			cell013.setCellStyle(dataStyle);
//
//			Cell cell014 = row.createCell(11);
//			cell014.setCellValue(formatValue(error.getErroId()));
//			cell014.setCellStyle(dataStyle);
		}

		// ---------------------------------------------------
		// Step 5: Auto size columns
		// ---------------------------------------------------

		for (int i = 0; i < 9; i++) {
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
				logger.info("Skipping null cell at column {}", c);
				continue;
			}

			String rawHeader = formatter.formatCellValue(cell).trim();

			if (rawHeader.isEmpty()) {
				logger.info("Skipping empty header at column {}", c);
				continue;
			}

			String normalized = normalizeHeader(rawHeader);

			logger.info("Header Found at column {} : Raw='{}' | Normalized='{}'", c, rawHeader, normalized);

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

			logger.info("field::; " + field);

			if (field == null) {
				logger.error("Entity field not found for header : {}", normalized);
				throw new RuntimeException("Entity field mapping not found for header: " + normalized);
			}

			columnFieldMap.put(c, field);

			logger.info("expectedHeaderCount:: " + expectedHeaderCount);

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
		throw new RuntimeException("Primary key column rcaNo not found.");
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

	/*
	 * private String normalizeHeader(String header) { return header == null ?
	 * null : header.trim().replace("-", "_").replace("&", "_").replace(" ",
	 * "_").replaceAll("[^a-zA-Z0-9_]", "_") .replaceAll("_+",
	 * "_").replaceAll("^_|_$", "_").toUpperCase(); }
	 */
	private String normalizeHeader(String header) {
		return header == null ? null
				: header.trim().replace(" ", "").replace("_", "").replace("-", "").replace(".", "")
						.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
	}

	// ============================================================
	// FETCH EXISTING PK IN BATCH
	// ============================================================

	private Set<String> fetchExistingInBatch(Set<String> uploadedPKSet, Date cycleFromDate, Date cycleToDate) {

		if (uploadedPKSet == null || uploadedPKSet.isEmpty())
			return Collections.emptySet();

		List<String> pkList = new ArrayList<>(uploadedPKSet);
		Set<String> existingRecords = new HashSet<>();

		int batchSize = 900;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, pkList.size());

			List<String> batch = pkList.subList(start, end);

			List<String> result = entityManager
					.createQuery("SELECT r.rcasNo FROM RCA_CIBIL r "
							+ "WHERE r.rcasNo IN :list AND r.fromCycleDate = :fromDate AND r.toCycleDate = :toDate", String.class)
					.setParameter("list", batch)
					.setParameter("fromDate", cycleFromDate, javax.persistence.TemporalType.DATE)
					.setParameter("toDate", cycleToDate, javax.persistence.TemporalType.DATE)
					.setHint("org.hibernate.readOnly", true).getResultList();

			existingRecords.addAll(result);
		}

		return existingRecords;
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private RCA_CIBIL mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		RCA_CIBIL entity = new RCA_CIBIL();

		logger.info("Mapping field for row : {}", row.getRowNum());

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

			if (type.equals(String.class))
				field.set(entity, value);

			else if (type.equals(Integer.class) || type.equals(int.class))
				field.set(entity, Integer.parseInt(value));

			else if (type.equals(Long.class) || type.equals(long.class))
				field.set(entity, Long.parseLong(value));

			else if (type.equals(BigDecimal.class))
				field.set(entity, new BigDecimal(value));

			else if (type.equals(Date.class)) {

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

	private RCA_CIBIL_Error mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		RCA_CIBIL_Error errorEntity = new RCA_CIBIL_Error();
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
				|| val.equalsIgnoreCase("#N/A")  || val.equalsIgnoreCase("#") || val.equalsIgnoreCase("##") || val.equalsIgnoreCase("###");
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
		int random = ThreadLocalRandom.current().nextInt(1000, 9999);

		return prefix + "_" + timestamp + "_" + random;
	}

	// ============================================================
	// Batch Insert GST Temp
	// ============================================================
	/*
	 * public void batchInsertTemp(List<RCA_CIBIL> entityList) {
	 * 
	 * if (entityList == null || entityList.isEmpty()) { logger.info(
	 * "No records to insert in TEMP table"); return; }
	 * 
	 * logger.info(
	 * "Starting JDBC batch insert for Channel TEMP table. Total records: {}",
	 * entityList.size());
	 * 
	 * String sql = "INSERT INTO TM_VHL_RCAS_CIBIL_DUMP (" +
	 * "RCAS_NO, LAN_NO, DISBURSEMENT_DATE, CIBIL_SCORE, " +
	 * "APPLICANT_TYPE, CUSTOMER_NAME, CUSTOMER_TYPE, " +
	 * "CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, " +
	 * "MODIFIED_BY, MODIFIED_DATE) " +
	 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	 * 
	 * int batchSize = 500; logger.info("entityList::: " +
	 * entityList.toString()); jdbcTemplate.batchUpdate(sql, entityList,
	 * batchSize, (PreparedStatement ps, RCA_CIBIL entity) -> { ps.setString(1,
	 * entity.getRcasNo()); ps.setString(2, entity.getLanNo()); ps.setDate(3,
	 * new java.sql.Date(entity.getDisbursementDate().getTime()));
	 * ps.setString(4, entity.getCibilScore()); ps.setString(5,
	 * entity.getApplicantType()); ps.setString(6, entity.getCustomerName());
	 * ps.setString(7, entity.getCustomerType()); ps.setString(8,
	 * entity.getCreatedBy()); ps.setTimestamp(9, new
	 * java.sql.Timestamp(entity.getCreatedDate().getTime())); ps.setDate(10,
	 * new java.sql.Date(entity.getFromCycleDate().getTime())); ps.setDate(11,
	 * new java.sql.Date(entity.getToCycleDate().getTime())); ps.setString(12,
	 * entity.getModifiedBy()); ps.setTimestamp(13, new
	 * java.sql.Timestamp(entity.getModifiedDate().getTime())); });
	 * 
	 * }
	 */
	public void batchInsertDump(List<RCA_CIBIL> entityList, Date cycleFromDate, Date cycleToDate, String uploadId) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}
		
		

		logger.info("Starting delete Aldd Dump table");

		// delete existing record for from date and To date
		jdbcTemplate.update("DELETE FROM TM_VHL_RCAS_CIBIL_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("End delete Aldd dump table");
		
		
		

		logger.info("Starting JDBC batch insert... Total records: {}", entityList.size());

		String sql = "INSERT INTO TM_VHL_RCAS_CIBIL_DUMP " + "(RCAS_NO, LAN_NO, DISBURSEMENT_DATE, CIBIL_SCORE, "
				+ "APPLICANT_TYPE, CUSTOMER_NAME, CUSTOMER_TYPE, "
				+ "CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, " + "MODIFIED_BY, MODIFIED_DATE , UPLOAD_ID, FILE_NAME) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?) " + "LOG ERRORS INTO TM_VHL_RCAS_CIBIL_DUMP_ERROR ('"
				+ uploadId + "') " + "REJECT LIMIT UNLIMITED";

		int batchSize = 500;

		logger.info("Starting JDBC batch insert. Total records: {}", entityList.size());
		long start = System.currentTimeMillis();
		 
		jdbcTemplate.batchUpdate(
		        sql,
		        entityList,
		        batchSize,
		        (PreparedStatement ps, RCA_CIBIL entity) -> {
		 
		            ps.setString(1, entity.getRcasNo());   // Ensure correct getter
		            ps.setString(2, entity.getLanNo());
		 
		            ps.setDate(3,
		                    entity.getDisbursementDate() != null
		                            ? new java.sql.Date(entity.getDisbursementDate().getTime())
		                            : null);
		 
		            ps.setString(4, entity.getCibilScore());
		            ps.setString(5, entity.getApplicantType());
		            ps.setString(6, entity.getCustomerName());
		            ps.setString(7, entity.getCustomerType());
		            ps.setString(8, entity.getCreatedBy());
		 
		            ps.setTimestamp(9,
		                    entity.getCreatedDate() != null
		                            ? new java.sql.Timestamp(entity.getCreatedDate().getTime())
		                            : null);
		 
		            ps.setDate(10,
		                    entity.getFromCycleDate() != null
		                            ? new java.sql.Date(entity.getFromCycleDate().getTime())
		                            : null);
		 
		            ps.setDate(11,
		                    entity.getToCycleDate() != null
		                            ? new java.sql.Date(entity.getToCycleDate().getTime())
		                            : null);
		 
		            ps.setString(12, entity.getModifiedBy());
		 
		            ps.setTimestamp(13,
		                    entity.getModifiedDate() != null
		                            ? new java.sql.Timestamp(entity.getModifiedDate().getTime())
		                            : null);
		            
		            ps.setString(14, entity.getUploadId());
		            
		            ps.setString(15, entity.getFileName());
		        }
		);
		 
		long end = System.currentTimeMillis();
		logger.info("Batch Insert Successful. Time taken: {} ms", (end - start));

	}

	// ============================================================
	// Batch Insert GST Error
	// ============================================================
	/*
	 * public void batchInsertError(List<RCA_CIBIL_Error> errorList) {
	 * 
	 * if (errorList == null || errorList.isEmpty()) { logger.info(
	 * "No error records to insert."); return; }
	 * 
	 * logger.info(
	 * "Starting JDBC batch insert for Branch ERROR table. Total records: {}",
	 * errorList.size());
	 * 
	 * long startTime = System.currentTimeMillis();
	 * 
	 * String sql = "INSERT INTO TM_VHL_RCAS_CIBIL_DUMP_ERROR (" +
	 * "RCAS_NO, LAN_NO, DISBURSEMENT_DATE, CIBIL_SCORE, " +
	 * "APPLICANT_TYPE, CUSTOMER_NAME, CUSTOMER_TYPE, " +
	 * "CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, " +
	 * "ERROR_MSG, ERROR_ID, UPLOAD_ID,ROW_NUMBER) " +
	 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; int batchSize =
	 * 500; logger.info("In the Error files::: "); jdbcTemplate.batchUpdate(sql,
	 * errorList, batchSize, (ps, entity) -> { // 1 // BRANCH_CODE
	 * ps.setString(1, entity.getRcasNo()); ps.setString(2, entity.getLanNo());
	 * // ps.setDate(3, new //
	 * java.sql.Date(entity.getDisbursementDate().getTime())); if
	 * (entity.getDisbursementDate() != null) { ps.setTimestamp(3, new
	 * Timestamp(entity.getDisbursementDate().getTime())); } else {
	 * ps.setNull(3, Types.TIMESTAMP); } ps.setString(4,
	 * entity.getCibilScore()); ps.setString(5, entity.getApplicantType());
	 * ps.setString(6, entity.getCustomerName()); ps.setString(7,
	 * entity.getCustomerType()); ps.setString(8, entity.getCreatedBy()); if
	 * (entity.getCreatedDate() != null) { ps.setTimestamp(9, new
	 * Timestamp(entity.getCreatedDate().getTime())); } else { ps.setNull(9,
	 * Types.TIMESTAMP); } if (entity.getFromCycleDate() != null) {
	 * ps.setTimestamp(10, new Timestamp(entity.getFromCycleDate().getTime()));
	 * } else { ps.setNull(10, Types.TIMESTAMP); }
	 * 
	 * if (entity.getToCycleDate() != null) { ps.setTimestamp(10, new
	 * Timestamp(entity.getToCycleDate().getTime())); } else { ps.setNull(10,
	 * Types.TIMESTAMP); } ps.setString(12, entity.getErrorMsg());
	 * ps.setBigDecimal(13, entity.getErroId()); ps.setString(14,
	 * entity.getUploadId()); ps.setInt(15, entity.getRowNumber()); });
	 * 
	 * logger.info(
	 * "Branch ERROR batch insert completed. Inserted {} records in {} ms",
	 * errorList.size(), (System.currentTimeMillis() - startTime)); }
	 */

	/*
	 * public void batchInsertError(List<RCA_CIBIL_Error> errorList) {
	 * 
	 * if (errorList == null || errorList.isEmpty()) { logger.info(
	 * "No error records to insert."); return; } logger.info(
	 * "Starting JDBC batch insert for Branch ERROR table. Total records: {}",
	 * errorList.size());
	 * 
	 * long startTime = System.currentTimeMillis(); String sql =
	 * "INSERT INTO TM_VHL_RCAS_CIBIL_DUMP_ERROR (" +
	 * "RCAS_NO, LAN_NO, DISBURSEMENT_DATE, CIBIL_SCORE, " +
	 * "APPLICANT_TYPE, CUSTOMER_NAME, CUSTOMER_TYPE, " +
	 * "CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, " +
	 * "ERROR_MSG, UPLOAD_ID, ROW_NUMBER) " +
	 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	 * 
	 * int batchSize = 500; logger.info("Before JDBC Batch");
	 * 
	 * jdbcTemplate.batchUpdate(sql, errorList, batchSize, (ps, entity) -> {
	 * jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement
	 * ps, RCA_CIBIL_Error entity) -> {
	 * 
	 * logger.info("After JDBC Batch");
	 * 
	 * ps.setString(1, entity.getRcasNo()); ps.setString(2, entity.getLanNo());
	 * 
	 * // DISBURSEMENT_DATE if (entity.getDisbursementDate() != null) {
	 * ps.setTimestamp(3, new
	 * Timestamp(entity.getDisbursementDate().getTime())); } else {
	 * ps.setNull(3, Types.TIMESTAMP); }
	 * 
	 * ps.setString(4, entity.getCibilScore()); ps.setString(5,
	 * entity.getApplicantType()); ps.setString(6, entity.getCustomerName());
	 * ps.setString(7, entity.getCustomerType()); ps.setString(8,
	 * entity.getCreatedBy());
	 * 
	 * // CREATED_DATE if (entity.getCreatedDate() != null) { ps.setTimestamp(9,
	 * new Timestamp(entity.getCreatedDate().getTime())); } else { ps.setNull(9,
	 * Types.TIMESTAMP); }
	 * 
	 * // FROM_CYCLE_DATE if (entity.getFromCycleDate() != null) {
	 * ps.setTimestamp(10, new Timestamp(entity.getFromCycleDate().getTime()));
	 * } else { ps.setNull(10, Types.TIMESTAMP); }
	 * 
	 * // TO_CYCLE_DATE if (entity.getToCycleDate() != null) {
	 * ps.setTimestamp(11, new Timestamp(entity.getToCycleDate().getTime())); }
	 * else { ps.setNull(11, Types.TIMESTAMP); } logger.info("Set before 12::: "
	 * + entity.getErrorMsg()); if (entity.getErrorMsg() != null) {
	 * ps.setString(12, entity.getErrorMsg()); } else { ps.setNull(12,
	 * Types.VARCHAR); } logger.info("Set before 13::: " +
	 * entity.getUploadId()); if (entity.getUploadId() != null) {
	 * ps.setString(13, entity.getUploadId()); } else { ps.setNull(13,
	 * Types.VARCHAR); } logger.info("Set before 14::: " +
	 * entity.getRowNumber()); if (entity.getRowNumber() != null) {
	 * ps.setInt(14, entity.getRowNumber()); } else { ps.setNull(14,
	 * Types.INTEGER); }
	 * 
	 * });
	 * 
	 * long endTime = System.currentTimeMillis(); logger.info(
	 * "Batch insert completed in {} ms", (endTime - startTime)); }
	 */

	public void batchInsertError(List<RCA_CIBIL_Error> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}
		try {
			logger.info("Starting JDBC batch insert for Branch ERROR table. Total records: {}", errorList.size());

			long startTime = System.currentTimeMillis();

			// SQL without ID column because it is auto-generated
			String sql = "INSERT INTO TM_VHL_RCAS_CIBIL_DUMP_ERROR ("
					+ "RCAS_NO, LAN_NO, DISBURSEMENT_DATE, CIBIL_SCORE, "
					+ "APPLICANT_TYPE, CUSTOMER_NAME, CUSTOMER_TYPE, "
					+ "CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, "
					+ "ERROR_MSG, UPLOAD_ID, ROW_NUMBER) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			int batchSize = 500;

			jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, RCA_CIBIL_Error entity) -> {

				ps.setString(1, entity.getRcasNo());
				ps.setString(2, entity.getLanNo());

//				// DISBURSEMENT_DATE
//				if (entity.getDisbursementDate() != null) {
//					ps.setTimestamp(3, new Timestamp(entity.getDisbursementDate().getTime()));
//				} else {
//					ps.setNull(3, Types.TIMESTAMP);
//				}
				
				ps.setString(3, entity.getDisbursementDate() );
				ps.setString(4, entity.getCibilScore());
				ps.setString(5, entity.getApplicantType());
				ps.setString(6, entity.getCustomerName());
				ps.setString(7, entity.getCustomerType());
				ps.setString(8, entity.getCreatedBy());

				// CREATED_DATE
				if (entity.getCreatedDate() != null) {
					ps.setTimestamp(9, new Timestamp(entity.getCreatedDate().getTime()));
				} else {
					ps.setNull(9, Types.TIMESTAMP);
				}

				// FROM_CYCLE_DATE
				if (entity.getFromCycleDate() != null) {
					ps.setTimestamp(10, new Timestamp(entity.getFromCycleDate().getTime()));
				} else {
					ps.setNull(10, Types.TIMESTAMP);
				}

				// TO_CYCLE_DATE
				if (entity.getToCycleDate() != null) {
					ps.setTimestamp(11, new Timestamp(entity.getToCycleDate().getTime()));
				} else {
					ps.setNull(11, Types.TIMESTAMP);
				}
				
				if (entity.getErrorMsg() != null) {
					ps.setString(12, entity.getErrorMsg());
				} else {
					ps.setNull(12, Types.VARCHAR);
				}
			
				if (entity.getUploadId() != null) {
					ps.setString(13, entity.getUploadId());
				} else {
					ps.setNull(13, Types.VARCHAR);
				}
				
				if (entity.getRowNumber() != null) {
					ps.setInt(14, entity.getRowNumber());
				} else {
					ps.setNull(14, Types.INTEGER);
				}

			});

			logger.info("RCAS ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
					(System.currentTimeMillis() - startTime));
		} catch (Exception e) {
			logger.error("Exception Occured::: " + e);
		}
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

	private void process(List<RCA_CIBIL> validEntityList, List<RCA_CIBIL_Error> invalidIntityList,
			Date cycleFromDate, Date cycleToDate, String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into main
			batchInsertDump(validEntityList, cycleFromDate, cycleToDate, uploadId);

			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("RCAS Cibil  Dump File upload Process Failed");
		}
	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_RCAS_CIBIL_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_RCAS_CIBIL_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}
}

package com.icici.dma.serviceImpl;

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
import java.util.Locale;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.model.RCA_CBC;
import com.icici.dma.model.RcasCbcError;
import com.icici.dma.repository.RcaCbcRepository;

@Service
public class RcasCBCServiceimpl {

	private static final Logger logger = LogManager.getLogger(RcasCBCServiceimpl.class);

	@Autowired
	private RcaCbcRepository cbcRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "cbcCode";
	private static String uploadId;

	DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	static {

		HEADER_MAP.put("CBCCODE", "cbcCode");
		HEADER_MAP.put("PROCESSSHOP", "processShop");

		// Cache all fields once
		for (Field field : RCA_CBC.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : RcasCbcError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	public Map<String, Integer> uploadRcasCBCExcel(Sheet sheet, String user, Date cycleFromDate, Date cycleToDate, String uploadId, String fileName) {

		long startTime = System.currentTimeMillis();
		logger.info("========== RCA CBC UPLOAD STARTED ==========");

		// if (file == null || file.isEmpty()) {
		// throw new RuntimeException("File is empty");
		// }

		// logger.info("File uploaded started. File Name : {}",
		// file.getOriginalFilename());

		// try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
		try {
			
			// Sheet sheet = workbook.getSheetAt(0);

			if (sheet == null)
				throw new RuntimeException("Sheet not found in Excel file.");

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
					// Long pk = Long.parseLong(value);

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
//				Set<String> existingInDB =  fetchExistingInBatch(validPKSet, cycleFromDate, cycleToDate);
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
//				errorReasonMap.putIfAbsent(r, "Duplicate Row - " + PkField + " Already Existed in server");
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

					// logger.info("Row {} marked Invalid. reason :
					// {}",r,errorReasonMap.get(r));

				} else {

					validRowsList.add(row);

					// logger.info("Row {} marked VALID.",r);
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

			List<RCA_CBC> entityList = validRowsList.parallelStream().map(row -> {

				try {
					RCA_CBC entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setFromCycleDate(cycleFromDate);
					entity.setToCycleDate(cycleToDate);
					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());
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

			logger.info("Upload Id : {}", uploadId);

			long entityStartTime1 = System.currentTimeMillis();

			logger.info("======= Error Entity cration Started ============");
			logger.info("Total Invalid Rows for processing: {}", invalidRowsList.size());

			List<RcasCbcError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					RcasCbcError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

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
			logger.info(" Entities creation comleted in {} ms : {}", (System.currentTimeMillis() - entityStartTime1));
			logger.info("========== Error Entity Creation Completed ================");


//			batchInsertMainTable(entityList, uploadId,cycleFromDate, cycleToDate);
//			batchInsertError(errorEntityList);
			
			
			process(entityList, errorEntityList, cycleFromDate, cycleToDate, uploadId);
			
			Map<String, Integer> counts = getCounts(uploadId, fileName);

			counts.put("TotalRecordInFile", entityList.size() + rowsToSkip.size());
			
			String message = "Upload Completed Successfully " + "\n" 
							+ "Total Records in file : " + (entityList.size() + rowsToSkip.size()) + "\n" 
							+ "Count of added records : " + counts.get("insertCount") + "\n"
							+ "Count of error records : " + counts.get("errorCount");

			
			logger.info(message);

			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));
			logger.info("========== RCAS CBC DUMP UPLOAD COMPLETED ==========");

			return counts;

		} catch (Exception e2) {
			// throw new RuntimeException("Rcas CBC Upload Failed: " +
			// e2.getMessage(), e2);
			throw new RuntimeException("Rcas CBCDump Upload Failed: " + e2.getMessage());
		}

	}

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
					.createQuery("SELECT f.cbcCode FROM RCA_CBC f WHERE f.cbcCode IN :list AND f.fromCycleDate = :fromDate AND f.toCycleDate = :toDate", String.class)
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

	private RCA_CBC mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		RCA_CBC entity = new RCA_CBC();

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

			// value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");

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

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
					field.set(entity, cell.getLocalDateTimeCellValue().toLocalDate());
				} else {
					field.set(entity, LocalDate.parse(value, DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
				}

			} else if (type.equals(LocalDateTime.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
					field.set(entity, cell.getLocalDateTimeCellValue());
				} else {
					field.set(entity, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
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

	private RcasCbcError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		RcasCbcError errorEntity = new RcasCbcError();
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
		return val.isEmpty()  || val.equalsIgnoreCase("N/A")
				|| val.equalsIgnoreCase("#N/A") || val.equalsIgnoreCase("##") || val.equalsIgnoreCase("###") 
				 || val.equalsIgnoreCase("-");
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
	// Batch Insert Rcas CBC Temp
	// ============================================================
	public void batchInsertMainTable(List<RCA_CBC> entityList, String uploadId, Date cycleFromDate,
			Date cycleToDate) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}
		
		logger.info("Starting delete Rcas CBC Dump table");

		// delete existing record for from date and To date
		jdbcTemplate.update("DELETE FROM TM_VHL_RCAS_CBC_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("End delete Rcas CBC dump table");
		

		logger.info("Starting JDBC batch insert for Rcas CBCDump table. Total records: {}", entityList.size());
		long start = System.currentTimeMillis();

		String sql = "INSERT INTO TM_VHL_RCAS_CBC_DUMP( " + "CBC_CODE,PROCESS_SHOP, " + "CREATED_BY, CREATED_DATE, "
				+ "FROM_CYCLE_DATE, TO_CYCLE_DATE, UPLOAD_ID, FILE_NAME) " + "VALUES (" + "?,?," + "?,?," + "?,?, ?, ?)"
				+ "LOG ERRORS INTO TM_VHL_RCAS_CBC_DUMP_ERROR ('" + uploadId + "') " + "REJECT LIMIT UNLIMITED";

		logger.info("Starting JDBC batch insert query genrated");
		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, RCA_CBC entity) -> {

			// 1 CBC_CODE
			ps.setString(1, entity.getCbcCode());

			// 2 PROCESS_SHOP
			ps.setString(2, entity.getProcessShop());

			// 3 CREATED_BY
			ps.setString(3, entity.getCreatedBy());

			// 4 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(4, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(4, Types.TIMESTAMP);
			}

			// 5 FROM_CYCLE_DATE
			if (entity.getFromCycleDate() != null) {
				ps.setTimestamp(5, new Timestamp(entity.getFromCycleDate().getTime()));
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}

			// 6 TO_CYCLE_DATE
			if (entity.getToCycleDate() != null) {
				ps.setTimestamp(6, new Timestamp(entity.getToCycleDate().getTime()));
			} else {
				ps.setNull(6, Types.TIMESTAMP);
			}
			
			// 7 UPLOAD_ID
			ps.setString(7, entity.getUploadId());
						
			// 8 FileName
			ps.setString(8, entity.getFileName());

		});

		// repository.saveAll(entityList);
		logger.info("RCA CBC insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// Batch Insert RCAS CBC Error
	// ============================================================
	public void batchInsertError(List<RcasCbcError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for RCA CBC ERROR table. Total records: {}", errorList.size());

		/*
		 * String sql =
		 * "INSERT INTO TM_VHL_RCAS_CBC_DUMP_ERROR ( CBC_CODE, PROCESS_SHOP, CREATED_BY, CREATED_DATE ,"
		 * + "FROM_CYCLE_DATE, TO_CYCLE_DATE, ERROR MSG, ROW_NUMBER, UPLOAD_ID"
		 * + ") VALUES (?,?,?,?,?,?,?,?,?)";
		 */

		/*String sql = "INSERT INTO TM_VHL_RCAS_CBC_DUMP_ERROR( " + "CBC_CODE,PROCESS_SHOP, "
				+ "CREATED_BY, CREATED_DATE, " + "FROM_CYCLE_DATE, TO_CYCLE_DATE, ERROR MSG, ROW_NUMBER, UPLOAD_ID) "
				+ "VALUES (" + "?,?," + "?,?," + "?,?,?,?,?)";
*/
		String sql = "INSERT INTO TM_VHL_RCAS_CBC_DUMP_ERROR ("
		        + "CBC_CODE, PROCESS_SHOP, CREATED_BY, CREATED_DATE, "
		        + "FROM_CYCLE_DATE, TO_CYCLE_DATE, ERROR_MSG, ROW_NUMBER, UPLOAD_ID"
		        + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int batchSize = 500;

		/*jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, RcasCbcError entity) -> {

			// 1 CBC_CODE
			ps.setString(1, entity.getCbcCode());

			// 2 PROCESS_SHOP
			ps.setString(2, entity.getProcessShop());

			// 3 CREATED_BY
			ps.setString(3, entity.getCreatedBy());

			// 4 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(4, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(4, Types.TIMESTAMP);
			}

			// 5 FROM_CYCLE_DATE
			if (entity.getFromCycleDate() != null) {
				ps.setTimestamp(5, new Timestamp(entity.getFromCycleDate().getTime()));
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}

			// 6 TO_CYCLE_DATE
			if (entity.getToCycleDate() != null) {
				ps.setTimestamp(6, new Timestamp(entity.getToCycleDate().getTime()));
			} else {
				ps.setNull(6, Types.TIMESTAMP);
			}
			ps.setString(7, entity.getErrorMsg());
			ps.setInt(8, entity.getRowNumber());
			ps.setString(9, entity.getUploadId());
		});

		logger.info("RCA CBC ERROR batch insert completed.");
	}*/
		
		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, RcasCbcError entity) -> {

			// 1 CBC_CODE
			ps.setString(1, entity.getCbcCode());

			// 2 PROCESS_SHOP
			ps.setString(2, entity.getProcessShop());

			// 3 CREATED_BY
			ps.setString(3, entity.getCreatedBy());

			// 4 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(4, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(4, Types.TIMESTAMP);
			}

			// 5 FROM_CYCLE_DATE
			if (entity.getFromCycleDate() != null) {
				ps.setTimestamp(5, new Timestamp(entity.getFromCycleDate().getTime()));
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}

			// 6 TO_CYCLE_DATE
			if (entity.getToCycleDate() != null) {
				ps.setTimestamp(6, new Timestamp(entity.getToCycleDate().getTime()));
			} else {
				ps.setNull(6, Types.TIMESTAMP);
			}
			
			ps.setString(7, entity.getErrorMsg());
			ps.setInt(8, entity.getRowNumber());
			ps.setString(9, entity.getUploadId());

		});
	}
	
	
	private void process(List<RCA_CBC> validEntityList, List<RcasCbcError> invalidIntityList,
			Date cycleFromDate, Date cycleToDate, String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into main
			batchInsertMainTable(validEntityList, uploadId, cycleFromDate, cycleToDate);

			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("RCAS CBC Trans  Dump File upload Process Failed");
		}
	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_RCAS_CBC_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_RCAS_CBC_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}

}

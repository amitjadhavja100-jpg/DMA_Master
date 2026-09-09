package com.icici.dma.serviceImpl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.icici.dma.model.RCA_ProcessShop;
//import com.icici.dma.model.RCA_ProcessShop_Error;
import com.icici.dma.model.RCA_ProcessShop_Error;

@Service
public class RcaProcessShopServiceImpl {

	private static final Logger logger = LogManager.getLogger(RcaProcessShopServiceImpl.class);


	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "rcasId";

	static {
		HEADER_MAP.put("RCASID", "rcasID");
		HEADER_MAP.put("CUSTMOERNAME", "customerName");
		HEADER_MAP.put("LANNO", "lanNo");
		HEADER_MAP.put("LEADID", "leadId");
		HEADER_MAP.put("CREATEDDATE", "createdDate");
		HEADER_MAP.put("ENTRYDATE", "entryDate");
		HEADER_MAP.put("ACTIVITYNAME", "activityName");
		HEADER_MAP.put("STATUS", "status");
		HEADER_MAP.put("TRAY", "tray");
		HEADER_MAP.put("BROKERNAME", "brokerName");
		HEADER_MAP.put("DISBURSALDATE", "disbursalDate");
		HEADER_MAP.put("MOBILE", "Mobile");
		HEADER_MAP.put("ENTRYDATE", "entryDate");
		HEADER_MAP.put("ACTIVITYNAME", "activityName");
		HEADER_MAP.put("EMPLOYERID", "employerId");
		HEADER_MAP.put("EMPLOYERNAME", "employerName");
		HEADER_MAP.put("CASESTATUS", "caseStatus");
		HEADER_MAP.put("RMNAME", "rmName");
		HEADER_MAP.put("DMECODE", "dmeCode");
		HEADER_MAP.put("DMENAME", "dmeName");
		HEADER_MAP.put("HUBNAME", "hubName");
		HEADER_MAP.put("LOANAMNT", "loanAmnt");
		HEADER_MAP.put("PROCESSSHOP", "processShop");
		HEADER_MAP.put("BRANCHNAME", "branchName");
		HEADER_MAP.put("SCHEMENAME", "schemeName");
		HEADER_MAP.put("PROMOTIONCODE", "promotionCode");
		HEADER_MAP.put("CHANNELCODE", "channelCode");
		HEADER_MAP.put("PSLDESCRIPTION", "pslDescription");
		HEADER_MAP.put("PSLTYPE", "pslType");
		HEADER_MAP.put("PRECFOC", "precfoc");
		HEADER_MAP.put("LARCOMMENTS", "larComments");

		// Cache all fields once
		for (Field field : RCA_ProcessShop.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : RCA_ProcessShop_Error.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	public Map<String, Integer> saveProcessShopSheet(Sheet sheet, String user, Date cycleFromDate, Date cycleToDate,
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

			List<RCA_ProcessShop> entityList = validRowsList.parallelStream().map(row -> {

				try {
					RCA_ProcessShop entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setCreatedBy(user);
					//entity.setCreatedDate(new Date());
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

			long entityStartTime1 = System.currentTimeMillis();

			logger.info("======= Error Entity cration Started ============");
			logger.info("Total Invalid Rows for processing: {}", invalidRowsList.size());

			List<RCA_ProcessShop_Error> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());
				logger.info("Error  Reasons::: " + reason + "UploadId::: " + uploadId);
				logger.info("row.getRowNum() + 1" + row.getRowNum() + 1);

				try {
					RCA_ProcessShop_Error entity_error = mapToErrorEntity(row, errorColumnFieldMap, formatter);

					entity_error.setCreatedBy(user);
					entity_error.setFromCycleDate(cycleFromDate);
					entity_error.setToCycleDate(cycleToDate);
					//entity_error.setCreatedDate(new Date());
					entity_error.setSystemCreatedDate(new Date());
					entity_error.setErrorMsg(reason);
					entity_error.setUploadId(uploadId);
					entity_error.setRowNumber(row.getRowNum() + 1);

					return entity_error;
				} catch (Exception e) {

					logger.error("Error Entity creation failed  at row {}. Reason : {}", row.getRowNum(),
							e.getMessage());
					return null;
				}

			}).filter(Objects::nonNull).collect(Collectors.toList());

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
//			logger.info("========== RCAS UPLOAD COMPLETED ==========");
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
					.createQuery("SELECT r.rcasId FROM RCA_ProcessShop r WHERE r.rcasId IN :list AND r.fromCycleDate = :fromDate AND r.toCycleDate = :toDate", String.class)
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

	private RCA_ProcessShop mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		RCA_ProcessShop entity = new RCA_ProcessShop();

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

	private RCA_ProcessShop_Error mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap,
			DataFormatter formatter) throws Exception {

		RCA_ProcessShop_Error errorEntity = new RCA_ProcessShop_Error();
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
				|| val.equalsIgnoreCase("NAN") || val.equalsIgnoreCase("-");
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
	// Batch Insert RCAS Temp
	// ============================================================

	public void batchInsertDump(List<RCA_ProcessShop> entityList,Date cycleFromDate, Date cycleToDate, String uploadId) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}
		
		
		logger.info("Starting delete Aldd Dump table");

		// delete existing record for from date and To date
		jdbcTemplate.update("DELETE FROM TM_VHL_RCAS_PROCESS_SHOP_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("End delete Aldd dump table");
		

		logger.info("Starting JDBC PROCESS SHOP batch insert... Total records: {}", entityList.size());
		
		String sql = "INSERT INTO TM_VHL_RCAS_PROCESS_SHOP_DUMP (" + "RCAS_ID, CUSTOMER_NAME, LAN_NO, LEAD_ID, "
				+ "ENTRY_DATE, ACTIVITYNAME, STATUS, TRAY, " + "BROKER_NAME, DISBURSAL_DATE, MOBILE, EMPLOYER_ID, "
				+ "EMPLOYER_NAME, CASE_STATUS, RM_NAME, DME_CODE, " + "DME_NAME, HUB_NAME, LOAN_AMNT, PROCESS_SHOP, "
				+ "BRANCH_NAME, SCHEME_NAME, PROMOTION_CODE, CHANNEL_CODE, "
				+ "PSL_DESCRIPTION, PSL_TYPE, PRECFOC, LAR_COMMENTS, "
				+ "CREATED_DATE, CREATED_BY, FROM_CYCLE_DATE, TO_CYCLE_DATE, UPLOAD_ID, FILE_NAME" + ") VALUES ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
				+ ") " + "LOG ERRORS INTO TM_VHL_RCAS_PROCESS_SHOP_DUMP_ERROR ('" + uploadId + "') "
				+ "REJECT LIMIT UNLIMITED";

		int batchSize = 500;
		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (ps, entity) -> {

			ps.setString(1, entity.getRcasId());
			ps.setString(2, entity.getCustomerName());
			ps.setString(3, entity.getLanNo());
			ps.setString(4, entity.getLeadId());
			ps.setTimestamp(5,
					entity.getEntryDate() != null ? new java.sql.Timestamp(entity.getEntryDate().getTime()) : null);
			ps.setString(6, entity.getActivityName());
			ps.setString(7, entity.getStatus());
			ps.setString(8, entity.getTray());
			ps.setString(9, entity.getBrokerName());
			ps.setTimestamp(10, entity.getDisbursalDate() != null
					? new java.sql.Timestamp(entity.getDisbursalDate().getTime()) : null);
			ps.setString(11, entity.getMobile());
			ps.setString(12, entity.getEmployerId());
			ps.setString(13, entity.getEmployerName());
			ps.setString(14, entity.getCaseStatus());
			ps.setString(15, entity.getRmName());
			ps.setString(16, entity.getDmeCode());
			ps.setString(17, entity.getDmeName());
			ps.setString(18, entity.getHubName());
			ps.setString(19, entity.getLoanAmnt());
			ps.setString(20, entity.getProcessShop());
			ps.setString(21, entity.getBranchName());
			ps.setString(22, entity.getSchemeName());
			ps.setString(23, entity.getPromotionCode());
			ps.setString(24, entity.getChannelCode());
			ps.setString(25, entity.getPslDescription());
			ps.setString(26, entity.getPslType());
			ps.setString(27, entity.getPrecfoc());
			ps.setString(28, entity.getLarComments());
			ps.setTimestamp(29,
					entity.getCreatedDate() != null ? new java.sql.Timestamp(entity.getCreatedDate().getTime()) : null);
			ps.setString(30, entity.getCreatedBy());
			ps.setDate(31,
					entity.getFromCycleDate() != null ? new java.sql.Date(entity.getFromCycleDate().getTime()) : null);

			ps.setDate(32,
					entity.getToCycleDate() != null ? new java.sql.Date(entity.getToCycleDate().getTime()) : null);
			
			ps.setString(33, entity.getUploadId());
			ps.setString(34, entity.getFileName());

		});

		logger.info("Batch Insert Successful");

	}

	// ============================================================
	// Batch Insert RCAS Error
	// ============================================================

	public void batchInsertError(List<RCA_ProcessShop_Error> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}
		try{
		logger.info("Starting JDBC batch insert for Branch ERROR table. Total records: {}", errorList.size());

		long startTime = System.currentTimeMillis();

		// SQL without ID column because it is auto-generated
		String sql = "INSERT INTO TM_VHL_RCAS_PROCESS_SHOP_DUMP_ERROR (" + "RCAS_ID, CUSTOMER_NAME, LAN_NO, LEAD_ID, "
				+ "ENTRY_DATE, ACTIVITYNAME, STATUS, TRAY, " + "BROKER_NAME, DISBURSAL_DATE, MOBILE, EMPLOYER_ID, "
				+ "EMPLOYER_NAME, CASE_STATUS, RM_NAME, DME_CODE, " + "DME_NAME, HUB_NAME, LOAN_AMNT, PROCESS_SHOP, "
				+ "BRANCH_NAME, SCHEME_NAME, PROMOTION_CODE, CHANNEL_CODE, "
				+ "PSL_DESCRIPTION, PSL_TYPE, PRECFOC, LAR_COMMENTS, "
				+ "CREATED_DATE, CREATED_BY, FROM_CYCLE_DATE, TO_CYCLE_DATE, " + "ERROR_MSG, ROW_NUMBER, UPLOAD_ID, SYSTEM_CREATED_DATE"
				+ ") VALUES ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
				+ ")";

		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (ps, entity_error) -> {

			ps.setString(1, entity_error.getRcasId());
			ps.setString(2, entity_error.getCustomerName());
			ps.setString(3, entity_error.getLanNo());
			ps.setString(4, entity_error.getLeadId());
//			ps.setTimestamp(5, entity_error.getEntryDate() != null
//					? new java.sql.Timestamp(entity_error.getEntryDate().getTime()) : null);
			ps.setString(5, entity_error.getEntryDate());
			ps.setString(6, entity_error.getActivityName());
			ps.setString(7, entity_error.getStatus());
			ps.setString(8, entity_error.getTray());
			ps.setString(9, entity_error.getBrokerName());
//			ps.setTimestamp(10, entity_error.getDisbursalDate() != null
//					? new java.sql.Timestamp(entity_error.getDisbursalDate().getTime()) : null);
			ps.setString(10, entity_error.getDisbursalDate());
			ps.setString(11, entity_error.getMobile());
			ps.setString(12, entity_error.getEmployerId());
			ps.setString(13, entity_error.getEmployerName());
			ps.setString(14, entity_error.getCaseStatus());
			ps.setString(15, entity_error.getRmName());
			ps.setString(16, entity_error.getDmeCode());
			ps.setString(17, entity_error.getDmeName());
			ps.setString(18, entity_error.getHubName());
			ps.setString(19, entity_error.getLoanAmnt());
			ps.setString(20, entity_error.getProcessShop());
			ps.setString(21, entity_error.getBranchName());
			ps.setString(22, entity_error.getSchemeName());
			ps.setString(23, entity_error.getPromotionCode());
			ps.setString(24, entity_error.getChannelCode());
			ps.setString(25, entity_error.getPslDescription());
			ps.setString(26, entity_error.getPslType());
			ps.setString(27, entity_error.getPrecfoc());
			ps.setString(28, entity_error.getLarComments());
			
//			ps.setTimestamp(29, entity_error.getCreatedDate() != null
//					? new java.sql.Timestamp(entity_error.getCreatedDate().getTime()) : null);
			
			ps.setString(29, entity_error.getCreatedDate());
			
			ps.setString(30, entity_error.getCreatedBy());
			
//			ps.setDate(31, entity_error.getFromCycleDate() != null
//					? new java.sql.Date(entity_error.getFromCycleDate().getTime()) : null);
			
			if (entity_error.getFromCycleDate() != null) {
				ps.setTimestamp(31, new Timestamp(entity_error.getFromCycleDate().getTime()));
			} else {
				ps.setNull(31, Types.TIMESTAMP);
			}
			

//			ps.setDate(32, entity_error.getToCycleDate() != null
//					? new java.sql.Date(entity_error.getToCycleDate().getTime()) : null);
			
			if (entity_error.getToCycleDate() != null) {
				ps.setTimestamp(32, new Timestamp(entity_error.getToCycleDate().getTime()));
			} else {
				ps.setNull(32, Types.TIMESTAMP);
			}
			
			ps.setString(33, entity_error.getErrorMsg());
			ps.setInt(34, entity_error.getRowNumber());
			ps.setString(35, entity_error.getUploadId());
			
//			ps.setDate(36,  entity_error.getSystemCreatedDate() != null
//					? new java.sql.Date( entity_error.getSystemCreatedDate().getTime()) : null);
//			ps.setDate(36, entity_error.getSystemCreatedDate());
			
			if (entity_error.getSystemCreatedDate() != null) {
				ps.setTimestamp(36, new Timestamp(entity_error.getSystemCreatedDate().getTime()));
			} else {
				ps.setNull(36, Types.TIMESTAMP);
			}

		});
		logger.info("RCAS PROCESS SHOP ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
				(System.currentTimeMillis() - startTime));
		}
		catch(Exception e){
			logger.error("Error Occured::: " + e);
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
	
	
	private void process(List<RCA_ProcessShop> validEntityList, List<RCA_ProcessShop_Error> invalidIntityList,
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
			throw new RuntimeException("RCAS ProcessShop  Dump File upload Process Failed");
		}
	}

	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_RCAS_PROCESS_SHOP_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_RCAS_PROCESS_SHOP_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}

	
	
	/*@Transactional
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Starting error export for User: {}", user);

		// get latest upload Id
		String uploadId = RcaCibilrepository.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

		if (uploadId == null || uploadId.isEmpty()) {
			throw new ResourceNotFoundException("No Error Record");
		}

		logger.info("Starting error export for uploadId: {}", uploadId);
		// ---------------------------------------------------
		// Step 1: Fetch error records from DB
		// ---------------------------------------------------

		List<RCA_ProcessShop_Error> errors = rcasProcessShopErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

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
		cell0.setCellValue("RCAS_ID");
		cell0.setCellStyle(headerStyle);

		Cell cell1 = header.createCell(1);
		cell1.setCellValue("CUSTMOER_NAME");
		cell1.setCellStyle(headerStyle);

		Cell cell2 = header.createCell(2);
		cell2.setCellValue("LAN_NO");
		cell2.setCellStyle(headerStyle);

		Cell cell3 = header.createCell(3);
		cell3.setCellValue("LEAD_ID");
		cell3.setCellStyle(headerStyle);

		Cell cell4 = header.createCell(4);
		cell4.setCellValue("CREATED_DATE");
		cell4.setCellStyle(headerStyle);

		Cell cell5 = header.createCell(5);
		cell5.setCellValue("ENTRY_DATE");
		cell5.setCellStyle(headerStyle);

		Cell cell6 = header.createCell(6);
		cell6.setCellValue("ACTIVITYNAME");
		cell6.setCellStyle(headerStyle);

		Cell cell7 = header.createCell(7);
		cell7.setCellValue("STATUS");
		cell7.setCellStyle(headerStyle);

		Cell cell8 = header.createCell(8);
		cell8.setCellValue("TRAY");
		cell8.setCellStyle(headerStyle);

		Cell cell9 = header.createCell(9);
		cell9.setCellValue("BROKER_NAME");
		cell9.setCellStyle(headerStyle);

		Cell cell10 = header.createCell(10);
		cell10.setCellValue("DISBURSAL_DATE");
		cell10.setCellStyle(headerStyle);
		
		Cell cell11 = header.createCell(11);
		cell11.setCellValue("MOBILE");
		cell11.setCellStyle(headerStyle);

		Cell cell12 = header.createCell(12);
		cell12.setCellValue("EMPLOYER_ID");
		cell12.setCellStyle(headerStyle);

		Cell cell13 = header.createCell(13);
		cell13.setCellValue("EMPLOYER_NAME");
		cell13.setCellStyle(headerStyle);

		Cell cell14 = header.createCell(14);
		cell14.setCellValue("CASE_STATUS");
		cell14.setCellStyle(headerStyle);

		Cell cell15 = header.createCell(15);
		cell15.setCellValue("RM_NAME");
		cell15.setCellStyle(headerStyle);
		
		Cell cell16 = header.createCell(16);
		cell16.setCellValue("DME_CODE");
		cell16.setCellStyle(headerStyle);
		
		Cell cell17 = header.createCell(17);
		cell17.setCellValue("DME_NAME");
		cell17.setCellStyle(headerStyle);
		
		Cell cell18 = header.createCell(18);
		cell18.setCellValue("HUB_NAME");
		cell18.setCellStyle(headerStyle);
			
		Cell cell19 = header.createCell(19);
		cell19.setCellValue("LOAN_AMNT");
		cell19.setCellStyle(headerStyle);		
		
		Cell cell20 = header.createCell(20);
		cell20.setCellValue("PROCESS_SHOP");
		cell20.setCellStyle(headerStyle);
				
		Cell cell21 = header.createCell(22);
		cell21.setCellValue("SCHEME_NAME");
		cell21.setCellStyle(headerStyle);
		
		Cell cell22= header.createCell(23);
		cell22.setCellValue("BRANCH_NAME");
		cell22.setCellStyle(headerStyle);
		
		Cell cell23 = header.createCell(24);
		cell23.setCellValue("PROMOTION_CODE");
		cell23.setCellStyle(headerStyle);
		
		Cell cell24 = header.createCell(25);
		cell24.setCellValue("CHANNEL_CODE");
		cell24.setCellStyle(headerStyle);
			
		Cell cell25 = header.createCell(26);
		cell25.setCellValue("PSL_DESCRIPTION");
		cell25.setCellStyle(headerStyle);		
		
		Cell cell26 = header.createCell(27);
		cell26.setCellValue("PSL_TYPE");
		cell26.setCellStyle(headerStyle);
		
		Cell cell27 = header.createCell(28);
		cell27.setCellValue("PRECFOC");
		cell27.setCellStyle(headerStyle);
		
		Cell cell28 = header.createCell(29);
		cell28.setCellValue("LAR_COMMENTS");
		cell28.setCellStyle(headerStyle);

		Cell cell29 = header.createCell(30);
		cell29.setCellValue("ERROR_MSG");
		cell29.setCellStyle(headerStyle);

		Cell cell30 = header.createCell(30);
		cell30.setCellValue("ROW_NUMBER");
		cell30.setCellStyle(headerStyle);

		Cell cell31 = header.createCell(31);
		cell31.setCellValue("ERROR_ID");
		cell31.setCellStyle(headerStyle);
		// ---------------------------------------------------
		// Step 4: Write Data Rows
		// ---------------------------------------------------

		int rowIndex = 1;

		for (RCA_ProcessShop_Error error : errors) {

			Row row = sheet.createRow(rowIndex++);

			Cell cell00 = row.createCell(0);
			cell00.setCellValue(formatValue(error.getRcasId()));
			cell00.setCellStyle(dataStyle);

			Cell cell01 = row.createCell(1);
			cell01.setCellValue(formatValue(error.getCustomerName()));
			cell01.setCellStyle(dataStyle);

			Cell cell02 = row.createCell(2);
			cell02.setCellValue(formatValue(error.getLanNo()));
			cell02.setCellStyle(dataStyle);

			Cell cell03 = row.createCell(3);
			cell03.setCellValue(formatValue(error.getLeadId()));
			cell03.setCellStyle(dataStyle);

			Cell cell04 = row.createCell(4);
			cell04.setCellValue(formatValue(error.getCreatedDate()));
			cell04.setCellStyle(dataStyle);

			Cell cell05 = row.createCell(5);
			cell05.setCellValue(formatValue(error.getEntryDate()));
			cell05.setCellStyle(dataStyle);

			Cell cell06 = row.createCell(6);
			cell06.setCellValue(formatValue(error.getActivityName()));
			cell06.setCellStyle(dataStyle);

			Cell cell07 = row.createCell(7);
			cell07.setCellValue(formatValue(error.getStatus()));
			cell07.setCellStyle(dataStyle);

			Cell cell08 = row.createCell(8);
			cell08.setCellValue(formatValue(error.getTray()));
			cell08.setCellStyle(dataStyle);

			Cell cell09 = row.createCell(9);
			cell09.setCellValue(formatValue(error.getBrokerName()));
			cell09.setCellStyle(dataStyle);

			Cell cell010 = row.createCell(10);
			cell010.setCellValue(formatValue(error.getDisbursalDate()));
			cell010.setCellStyle(dataStyle);

			Cell cell011 = row.createCell(11);
			cell011.setCellValue(formatValue(error.getMobile()));
			cell011.setCellStyle(dataStyle);
			
			Cell cell012 = row.createCell(12);
			cell012.setCellValue(formatValue(error.getEmployerId()));
			cell012.setCellStyle(dataStyle);
						
			Cell cell013 = row.createCell(13);
			cell013.setCellValue(formatValue(error.getEmployerName()));
			cell013.setCellStyle(dataStyle);
			
			Cell cell014 = row.createCell(14);
			cell014.setCellValue(formatValue(error.getCaseStatus()));
			cell014.setCellStyle(dataStyle);
						
			Cell cell015 = row.createCell(15);
			cell015.setCellValue(formatValue(error.getRmName()));
			cell015.setCellStyle(dataStyle);
					
			Cell cell016 = row.createCell(16);
			cell016.setCellValue(formatValue(error.getDmeCode()));
			cell016.setCellStyle(dataStyle);
					
			Cell cell017 = row.createCell(17);
			cell017.setCellValue(formatValue(error.getLoanAmnt()));
			cell017.setCellStyle(dataStyle);
						
			Cell cell018 = row.createCell(18);
			cell018.setCellValue(formatValue(error.getProcessShop()));
			cell018.setCellStyle(dataStyle);
					
			Cell cell019 = row.createCell(19);
			cell019.setCellValue(formatValue(error.getBranchName()));
			cell020.setCellStyle(dataStyle);
			
			
			Cell cell023 = row.createCell(23);
			cell023.setCellValue(formatValue(error.getSchemeName()));
			cell023.setCellStyle(dataStyle);
					

			Cell cell024= row.createCell(24);
			cell024.setCellValue(formatValue(error.getProcessShop()));
			cell024.setCellStyle(dataStyle);
			
			
			Cell cell027 = row.createCell(25);
			cell027.setCellValue(formatValue(error.getBranchName()));
			cell027.setCellStyle(dataStyle);
			
			
			Cell cell028 = row.createCell(28);
			cell028.setCellValue(formatValue(error.getSchemeName()));
			cell028.setCellStyle(dataStyle);
			
			
			Cell cell029 = row.createCell(29);
			cell029.setCellValue(formatValue(error.getBranchName()));
			cell029.setCellStyle(dataStyle);
			
			
			Cell cell030 = row.createCell(30);
			cell030.setCellValue(formatValue(error.getSchemeName()));
			cell030.setCellStyle(dataStyle);
			
			
			
		}

		// ---------------------------------------------------
		// Step 5: Auto size columns
		// ---------------------------------------------------

		for (int i = 0; i < 13; i++) {
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
	*/
	

}

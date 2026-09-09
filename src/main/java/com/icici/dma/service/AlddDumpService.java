package com.icici.dma.service;

import lombok.RequiredArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.AlddReportError;
import com.icici.dma.model.AlddTransaction;
import com.icici.dma.model.BranchMasterError;
import com.icici.dma.model.BranchMasterTemp;
import com.icici.dma.model.FinnoneDump;
import com.icici.dma.model.FinnoneDumpError;
import com.icici.dma.model.GSTMasterError;
import com.icici.dma.repository.AlddDumpErrorRepository;
import com.icici.dma.repository.AlddDumpRepository;
import com.icici.dma.repository.BranchMasterErrorRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
@RequiredArgsConstructor
public class AlddDumpService {
	@Autowired
	private AlddDumpRepository repository;

	private FormulaEvaluator evaluator;

	private static final Logger logger = LogManager.getLogger(AlddDumpService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AlddDumpErrorRepository alddErrorRepository;

	private static final String PkField = "applicationNo";

	// Excel header → Entity field mapping
	private static final Map<String, String> HEADER_FIELD_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	/*
	 * private static final List<SimpleDateFormat> DATE_FORMATS = Arrays.asList(new
	 * SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd/MM/yyyy"), new
	 * SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy/MM/dd")
	 * 
	 * );
	 * 
	 * private Date parseDate(String value) {
	 * 
	 * for (SimpleDateFormat sdf : DATE_FORMATS) { try { return sdf.parse(value); }
	 * catch (ParseException ignored) { } } throw new
	 * RuntimeException("Invalid date format: " + value); }
	 */

	/* ================= REQUIRED HEADERS ================= */
	/*
	 * private static final Set<String> REQUIRED_HEADERS = new HashSet<>(
	 * Arrays.asList("APPLICATION_NO", "currAdd1", "fileNo"));
	 */

	/* ================= HEADER → ENTITY MAP ================= */
	// private static final Map<String, String> HEADER_FIELD_MAP = new HashMap<>();

	static {
		// ==== EXCEL MAPPED FIELDS ====
		HEADER_FIELD_MAP.put("ID", "id");
		HEADER_FIELD_MAP.put("MONTH", "month");
		HEADER_FIELD_MAP.put("APPLICATION_NO", "applicationNo");
		HEADER_FIELD_MAP.put("LAN", "lan");
		HEADER_FIELD_MAP.put("FILENO", "fileNo");
		HEADER_FIELD_MAP.put("BRANCH_CODE", "branchCode");
		HEADER_FIELD_MAP.put("PRODUCT", "product");
		HEADER_FIELD_MAP.put("SCHEME", "scheme");
		HEADER_FIELD_MAP.put("SCHEME_ID", "schemeId");
		HEADER_FIELD_MAP.put("SOL_ID", "solId");
		HEADER_FIELD_MAP.put("CUST_FNAME", "custFname");
		HEADER_FIELD_MAP.put("CUST_MNAME", "custMname");
		HEADER_FIELD_MAP.put("CUST_LNAME", "custLname");
		HEADER_FIELD_MAP.put("CONSTID", "constitution");
		HEADER_FIELD_MAP.put("DOB", "dob");
		HEADER_FIELD_MAP.put("IND_CORP_FLAG", "indCorpFlag");
		HEADER_FIELD_MAP.put("CURR_ADD1", "currAdd1");
		HEADER_FIELD_MAP.put("CURR_ADD2", "currAdd2");
		HEADER_FIELD_MAP.put("CURR_ADD3", "currAdd3");
		HEADER_FIELD_MAP.put("CITY", "city");
		HEADER_FIELD_MAP.put("STATE", "state");
		HEADER_FIELD_MAP.put("ZIPCODE", "zipcode");
		HEADER_FIELD_MAP.put("PHONE1", "phone1");
		HEADER_FIELD_MAP.put("PHONE2", "phone2");
		HEADER_FIELD_MAP.put("MOBILE", "mobile");
		HEADER_FIELD_MAP.put("ASSET_TYPE", "assetType");
		HEADER_FIELD_MAP.put("MAKE", "make");
		HEADER_FIELD_MAP.put("MODEL", "modelBk");
		HEADER_FIELD_MAP.put("SUB_MODEL", "subModel");
		HEADER_FIELD_MAP.put("ASSETCOST", "assetCost");
		HEADER_FIELD_MAP.put("MARGIN_MONEY", "marginMoney");
		HEADER_FIELD_MAP.put("AMOUNTFINANCED", "amountFinanced");
		HEADER_FIELD_MAP.put("TENURE", "tenure");
		HEADER_FIELD_MAP.put("EMI", "emi");
		HEADER_FIELD_MAP.put("IRR", "irr");
		HEADER_FIELD_MAP.put("ADVANCE_EMI", "advanceEmi");
		/*
		 * HEADER_FIELD_MAP.put("INSTALMENT_START_DATE", "installmentStartDate");
		 */
		HEADER_FIELD_MAP.put("INSTL_TYPE", "instlType");
		HEADER_FIELD_MAP.put("FREQ", "freq");
		HEADER_FIELD_MAP.put("SUPPLIER", "supplier");
		HEADER_FIELD_MAP.put("BROKER", "broker");
		HEADER_FIELD_MAP.put("REP_MODE", "repMode");
		HEADER_FIELD_MAP.put("ACCT_NUMBER", "acctNumber");
		HEADER_FIELD_MAP.put("BANK_ACC_NO", "bankAccNo");
		HEADER_FIELD_MAP.put("ECS_MICR", "ecsMicr");
		HEADER_FIELD_MAP.put("INDUSTRYDESC", "industryDesc");
		HEADER_FIELD_MAP.put("PROMOTION_DESC", "promotionDesc");
		HEADER_FIELD_MAP.put("CHANNELCODE", "channelCode");
		HEADER_FIELD_MAP.put("EMPLOYEE_NAME", "employeeName");
		HEADER_FIELD_MAP.put("DME", "dme");
		HEADER_FIELD_MAP.put("MKTG_OFFICER", "mktgOfficer");
		HEADER_FIELD_MAP.put("FIRST_SOURCE", "firstSource");
		HEADER_FIELD_MAP.put("FINAL_SOURCE", "finalSource");
		HEADER_FIELD_MAP.put("UN_FIRST_SOURCE", "unFirstSource");
		HEADER_FIELD_MAP.put("UN_FINAL_SOURCE", "unFinalSource");
		HEADER_FIELD_MAP.put("CONNECTOR_NAME", "connectorName");
		HEADER_FIELD_MAP.put("RELIGION", "religion");
		HEADER_FIELD_MAP.put("PROFESSION", "profession");
		HEADER_FIELD_MAP.put("SC_ST_FLAG", "scStFlag");
		HEADER_FIELD_MAP.put("SEX", "sex");
		HEADER_FIELD_MAP.put("MARITAL_STATUS", "maritalStatus");
		HEADER_FIELD_MAP.put("QUALIFICATION", "qualification");
		HEADER_FIELD_MAP.put("ADDRESSTYPE", "addressType");
		HEADER_FIELD_MAP.put("EMAIL_COMMUNICATION", "emailCommunication");
		HEADER_FIELD_MAP.put("CALL_COMMUNICATION", "callCommunication");
		HEADER_FIELD_MAP.put("PPI_AMT", "ppiAmt");
		HEADER_FIELD_MAP.put("APPLICATION_DATE", "applicationDate");
		HEADER_FIELD_MAP.put("UPLOAD_DISBURSAL_DATE", "uploadDisbursalDate");
		HEADER_FIELD_MAP.put("LOANTYPE", "loanType");
		HEADER_FIELD_MAP.put("EFFRATE", "effRate");
		HEADER_FIELD_MAP.put("CHARGE_CODE1", "chargeCode1");
		HEADER_FIELD_MAP.put("CHARGE_AMOUNT1", "chargeAmount1");
		HEADER_FIELD_MAP.put("CHARGE_CODE2", "chargeCode2");
		HEADER_FIELD_MAP.put("CHARGE_AMOUNT2", "chargeAmount2");
		HEADER_FIELD_MAP.put("ANNUALISEDAPR", "annualisedApr");
		HEADER_FIELD_MAP.put("PSL_FLAG", "pslFlag");
		HEADER_FIELD_MAP.put("PSL_CODE", "pslCode");
		HEADER_FIELD_MAP.put("SME_CODE", "smeCode");
		HEADER_FIELD_MAP.put("HNICODE", "hniCode");
		HEADER_FIELD_MAP.put("OWNED_IRRIGATED", "ownedIrrigated");
		HEADER_FIELD_MAP.put("OWNED_NON_IRRIGATED", "ownedNonIrrigated");
		HEADER_FIELD_MAP.put("LEASED_IN_IRRIGATED", "leasedInIrrigated");
		HEADER_FIELD_MAP.put("LEASED_IN_NON_IRRIGATED", "leasedInNonIrrigated");
		HEADER_FIELD_MAP.put("LEASED_OUT_IRRIGATED", "leasedOutIrrigated");
		HEADER_FIELD_MAP.put("LEASED_OUT_NON_IRRIGATED", "leasedOutNonIrrigated");
		HEADER_FIELD_MAP.put("DISBURSAL_TO", "disbursalTo");
		HEADER_FIELD_MAP.put("CROSSCOLL_WITH", "crossCollWith");
		HEADER_FIELD_MAP.put("UMRN_NUMBER", "umrnNumber");
		HEADER_FIELD_MAP.put("NPCI_MANDATE_UPLD_DATE", "npciMandateUpldDate");
		HEADER_FIELD_MAP.put("REGISTRATION_STATUS", "registrationStatus");
		HEADER_FIELD_MAP.put("NACH_EFFECTIVE_DATE", "nachEffectiveDate");
		HEADER_FIELD_MAP.put("REGISTRATION_AMOUNT", "registrationAmount");
		HEADER_FIELD_MAP.put("UMRN_ACCEPTANCE_DATE", "umrnAcceptanceDate");
		HEADER_FIELD_MAP.put("REJECT_CODE", "rejectCode");
		HEADER_FIELD_MAP.put("FATHER_NAME", "fatherName");
		HEADER_FIELD_MAP.put("SPOUSE_NAME", "spouseName");
		HEADER_FIELD_MAP.put("INDUSTRY", "industry");
		HEADER_FIELD_MAP.put("LOAN_PURPOSE", "loanPurpose");
		HEADER_FIELD_MAP.put("MARGIN_MONEY_CODE", "marginMoneyCode");
		HEADER_FIELD_MAP.put("DMA_CODE", "dmaCode");
		HEADER_FIELD_MAP.put("ECODE", "ecode");
		HEADER_FIELD_MAP.put("REFERRAL_CODE", "referralCode");
		HEADER_FIELD_MAP.put("INSTALMENT_START_DATE", "instalmentStartDate");
		HEADER_FIELD_MAP.put("ECS_ACCTNO", "ecsAccNo");
		HEADER_FIELD_MAP.put("ASSET_CATAGORY", "assetCategory");
		HEADER_FIELD_MAP.put("IS_RBI_DECLARATION_SELECTED", "isrbideclarationselected");

		// Cache all fields once
		for (Field field : AlddTransaction.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : AlddReportError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	/* ================= UPLOAD ================= */

	public String uploadExcel(MultipartFile file, String user, Date fromDate, Date toDate) {

		long startTime = System.currentTimeMillis();

		if (file == null || file.isEmpty()) {
			throw new RuntimeException("File is empty");
		}

		// List<AlddTransaction> saveList = new ArrayList<>();

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			int sheetCount = workbook.getNumberOfSheets();

			if (sheetCount != 1) {

				logger.info("Excel file should contain only one sheet");
				return "Excel file should contain only one sheet";
			}

			if (sheet == null) {
				throw new RuntimeException("Excel Sheet not Found");
			}
			

			DataFormatter formatter = new DataFormatter();

			Row headerRow = sheet.getRow(0);

			// hold column index and entity field ==> [ 0 -> Field (branchCode) ]
			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			logger.info("Header Validation Complate . Total column mapped : {}", columnFieldMap.size());

			// ========================================================
			// PRIMARY KEY IDENTIFICATION
			// ========================================================

			// hold the which column index hold the primary key ==> 1
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
//								Integer pk = Integer.parseInt(value);
					String pk = value.trim().toUpperCase();

					// store row index against pk
					pkRowMap.computeIfAbsent(pk, k -> new ArrayList<>()).add(r);

					validPKSet.add(pk);
//								if (!validPKSet.add(pk)) {
//									duplicateExcelRows.add(r);
//								}

				} catch (Exception e) {
					logger.info("Row index {} marked as invalid due to PK Parse Error. value :{}", r, value);
					invalidRows.add(r);
				}

			}

			logger.info("Total Invalid PK rows: {}", invalidRows.size());
			logger.info("PK rowsMap: {}", pkRowMap);
			logger.info("Duplicate PK rows in Excel: {}", duplicateExcelRows.size());
			// ========================================================
			// Excel DUPLICATE CHECK -> check each pk row value occurs in which row
			// ========================================================
			for (Map.Entry<String, List<Integer>> entry : pkRowMap.entrySet()) {

				List<Integer> rows = entry.getValue();

//							entry.getValue();

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
//				Set<String> existingInDB = fetchExistingInBatch(validPKSet, fromDate, toDate);
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

//						Set<Integer> rowsToSkip = new HashSet<>(errorReasonMap.keySet());
			Set<Integer> rowsToSkip = errorReasonMap.keySet();

			logger.info("Invalid row count : {}", invalidRows.size());
			logger.info("Excel duplicate row count : {}", duplicateExcelRows.size());
//			logger.info("DB duplicate count : {}", dbDuplicateRows.size());
			logger.info("ErrorReasonMap count : {}", errorReasonMap.size());
			logger.info("Total rows to skip: {}", rowsToSkip.size());

			// ========================================================
			// Upload Id creation
			// ========================================================

			// Generate unique Upload Id
			String uploadId = generateUploadId(file.getOriginalFilename());
			String originalFilename = file.getOriginalFilename();

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

//								logger.info("Row {} marked Invalid. reason : {}",r,errorReasonMap.get(r));

				} else {

					validRowsList.add(row);

//								logger.info("Row {} marked VALID.",r);
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

			// validateHeaders(headerRow);
			// repository.deleteAllInBatch();

			// Map<Integer, String> columnMap = mapHeaders(headerRow);
			List<AlddTransaction> entityList = validRowsList.parallelStream().map(row -> {

				try {
					AlddTransaction entity = mapRowToEntity(row, columnFieldMap, formatter);

					// setFields(row, columnMap, entity);
					entity.setCreatedDate(new Date());
					entity.setCreatedBy(user);
					entity.setToCycleDate(toDate);
					entity.setFromCycleDate(fromDate);
					entity.setUploadId(uploadId);
					entity.setFileName(originalFilename);

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
				System.out.println("ErrorColumnFieldMap Field Name::" + fieldName);

				Field errorField = ERROR_FIELD_CACHE.get(fieldName.toUpperCase());
				System.out.println("ErrorColumnFieldMap Error Field Name::" + errorField);

				if (errorField != null) {
					errorColumnFieldMap.put(entry.getKey(), errorField);
				}
			}

			// ========================================================
			// PARALLEL ERROR ENTITY CREATION
			// ========================================================

			logger.info("======= Error Entity cration Started ============");
			logger.info("Total Invalid Rows for processing: {}", invalidRowsList.size());

			List<AlddReportError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					AlddReportError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

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

//			alddInsertTransaction(entityList, uploadId);
//			AlddReportError(errorEntityList);

			process(entityList, errorEntityList, fromDate, toDate, uploadId);

			Map<String, Integer> counts = getCounts(uploadId, originalFilename);

			String message = "Upload Completed Successfully " + "\n" + "Total Records in file : "
					+ (entityList.size() + rowsToSkip.size()) + "\n" + "Count of added records : "
					+ counts.get("insertCount") + "\n" + "Count of error records : " + counts.get("errorCount");


			logger.info(message);

			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));
			logger.info("========== ALDD UPLOAD COMPLETED ==========");

			return message;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Upload Failed : " + e.getMessage());
		}

		// return "ALDD transaction Report upload Successfully! Records saved : " +
		// saveList.size();
	}

	/* ================= HEADER NORMALIZATION ================= */

	/*
	 * private void AlddReportError(List<AlddReportError> errorEntityList) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 */

	private void AlddReportError(List<AlddReportError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for Branch ERROR table. Total records: {}", errorList.size());


		int batchSize = 1500;

		// SQL without ID column because it is auto-generated
		String sql = "INSERT INTO TM_VHL_ALDD_TRANS_DUMP_ERROR ("
				+ "MONTH, APPLICATION_NO, LAN, ID, FILENO, APPLICATION_NO_BK, BRANCH_CODE, PRODUCT,"
				+ " SCHEME, SCHEME_ID, SOL_ID, CUST_FNAME, CUST_MNAME, CUST_LNAME, CONSTID, DOB, IND_CORP_FLAG, CURR_ADD1, CURR_ADD2, CURR_ADD3,"
				+ " CITY, STATE, ZIPCODE, PHONE1, PHONE2, MOBILE, ASSET_TYPE, MAKE, MODEL_BK, SUB_MODEL, ASSETCOST, MARGIN_MONEY, AMOUNTFINANCED,"
				+ " TENURE, EMI, IRR, ADVANCE_EMI, INSTALMENT_START_DATE, INSTL_TYPE, FREQ, SUPPLIER, BROKER, REP_MODE, ACCT_NUMBER, BANK_ACC_NO,"
				+ " ECS_MICR, INDUSTRYDESC, PROMOTION_DESC, ASSET_CATAGORY, CHANNELCODE, EMPLOYEE_NAME, DME, MKTG_OFFICER, FIRST_SOURCE, FINAL_SOURCE,"
				+ " UN_FIRST_SOURCE, UN_FINAL_SOURCE, CONNECTOR_NAME, LAN_BK, RELIGION, PROFESSION, SC_ST_FLAG, SEX, MARITAL_STATUS, QUALIFICATION,"
				+ " ADDRESSTYPE, EMAIL_COMMUNICATION, CALL_COMMUNICATION, PPI_AMT, APPLICATION_DATE, UPLOAD_DISBURSAL_DATE, LOANTYPE, EFFRATE, CHARGE_CODE1,"
				+ " CHARGE_AMOUNT1, CHARGE_CODE2, CHARGE_AMOUNT2, ANNUALISEDAPR, PSL_FLAG, PSL_CODE, SME_CODE, HNICODE, OWNED_IRRIGATED, OWNED_NON_IRRIGATED,"
				+ " LEASED_IN_IRRIGATED, LEASED_IN_NON_IRRIGATED, LEASED_OUT_IRRIGATED, LEASED_OUT_NON_IRRIGATED, DISBURSAL_TO, CROSSCOLL_WITH, UMRN_NUMBER,"
				+ " NPCI_MANDATE_UPLD_DATE, REGISTRATION_STATUS, NACH_EFFECTIVE_DATE, REGISTRATION_AMOUNT, UMRN_ACCEPTANCE_DATE, REJECT_CODE, FATHER_NAME,"
				+ " SPOUSE_NAME, INDUSTRY, LOAN_PURPOSE, MARGIN_MONEY_CODE, DMA_CODE, IS_RBI_DECLARATION_SELECTED, ECODE, REFERRAL_CODE, CREATED_BY,"
				+ " CREATED_DATE, ECS_ACCNO, FROM_CYCLE_DATE, TO_CYCLE_DATE, REMARKS, CONSTITUTION,"
				+ " ERROR_MSG, ROW_NUMBER, UPLOAD_ID" + ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, AlddReportError entity) -> {

			// 1 MONTH
			ps.setString(1, entity.getMonth());

			// 2 APPLICATION NUMBER
			ps.setString(2, entity.getApplicationNo());

			// 3 LAN
			ps.setString(3, entity.getLan());

			// 4 ID
			ps.setString(4, entity.getId());

			// 5 FILE NO
			ps.setString(5, entity.getFileNo());

			// 6 APPLICATION_NO_BK
			ps.setString(6, entity.getApplicationNoBk());

			// 7 BRANCH CODE
			ps.setString(7, entity.getBranchCode());

			// 8 PRODUCT
			ps.setString(8, entity.getProduct());

			// 9 SCHEME
			ps.setString(9, entity.getScheme());

			// 10 SCHEME_ID
			ps.setString(10, entity.getSchemeId());

			// 11 SOLE_ID
			ps.setString(11, entity.getSolId());

			// 12 CUST_FNAME
			ps.setString(12, entity.getCustFname());

			// 13 CUST_MNAME
			ps.setString(13, entity.getCustMname());

			// 14 CUST_LNAME
			ps.setString(14, entity.getCustLname());

			// 15 CONSTID
			ps.setString(15, entity.getConstitution());

			// 16 MODIFIED_BY
			// ps.setString(15, entity.getModifiedBy());

			// 16 DOB
			ps.setString(16, entity.getDob());
			/*
			 * if (entity.getDob() != null) { //ps.setDate(16, (java.sql.Date)
			 * entity.getDob()); java.sql.Date sqlDate = new
			 * java.sql.Date(entity.getDob().getTime()); ps.setDate(16, sqlDate); } else {
			 * ps.setNull(16, Types.TIMESTAMP); }
			 */

			/*
			 * // 17 MODIFIED_DATE if (entity.getModifiedDate() != null) {
			 * ps.setTimestamp(17, new Timestamp(entity.getModifiedDate().getTime())); }
			 * else { ps.setNull(17, Types.TIMESTAMP); }
			 */

			// 17 IND_CORP_FLAG
			ps.setString(17, entity.getIndCorpFlag());

			// 18 CURR_ADD1
			ps.setString(18, entity.getCurrAdd1());

			// 19 CURR_ADD2
			ps.setString(19, entity.getCurrAdd2());

			// 20 CURR_ADD3
			ps.setString(20, entity.getCurrAdd3());

			// 21 CITY
			ps.setString(21, entity.getCity());

			// 22 STATE
			ps.setString(22, entity.getState());

			// 23 ZIPCODE
			ps.setString(23, entity.getZipcode());

			// 24 PHONE1
			ps.setString(24, entity.getPhone1());

			// 25 PHONE2
			ps.setString(25, entity.getPhone2());

			// 26 MOBILE
			ps.setString(26, entity.getMobile());

			// 27 ASSET_TYPE
			ps.setString(27, entity.getAssetType());

			// 28 MAKE
			ps.setString(28, entity.getMake());

			// 29 MODEL
			ps.setString(29, entity.getModelBk());

			// 30 SUB_MODEL
			ps.setString(30, entity.getSubModel());

			// 31 ASSETCOST
			ps.setString(31, entity.getAssetCost());

			// 32 MARGIN_MONEY
			ps.setString(32, entity.getMarginMoney());

			// 33 AMOUNTFINANCED
			ps.setString(33, entity.getAmountFinanced());

			// 34 TENURE
			ps.setString(34, entity.getTenure());

			// 35 EMI
			ps.setString(35, entity.getEmi());

			// 36 IRR
			ps.setString(36, entity.getIrr());

			// 37 ADVANCE_EMI
			ps.setString(37, entity.getAdvanceEmi());

			// 38 INSTALMENT_START_DATE
			ps.setString(38, entity.getInstalmentStartDate());

			// 39 INSTL_TYPE
			ps.setString(39, entity.getInstlType());

			// 40 FREQ
			ps.setString(40, entity.getFreq());

			// 41 SUPPLIER
			ps.setString(41, entity.getSupplier());

			// 42 BROKER
			ps.setString(42, entity.getBroker());

			// 43 REP_MODE
			ps.setString(43, entity.getRepMode());

			// 44 ACCT_NUMBER
			ps.setString(44, entity.getAcctNumber());

			// 45 BANK_ACC_NO
			ps.setString(45, entity.getBankAccNo());

			// 46 ECS_MICR
			ps.setString(46, entity.getEcsMicr());

			// 47 INDUSTRYDESC
			ps.setString(47, entity.getIndustryDesc());

			// 48 PROMOTION_DESC
			ps.setString(48, entity.getPromotionDesc());

			// 49 ASSET_CATAGORY
			ps.setString(49, entity.getAssetCategory());

			// 50 CHANNELCODE
			ps.setString(50, entity.getChannelCode());

			// 51 EMPLOYEE_NAME
			ps.setString(51, entity.getEmployeeName());

			// 52 DME
			ps.setString(52, entity.getDme());

			// 53 MKTG_OFFICER
			ps.setString(53, entity.getMktgOfficer());

			// 54 FIRST_SOURCE
			ps.setString(54, entity.getFirstSource());

			// 55 FINAL_SOURCE
			ps.setString(55, entity.getFinalSource());

			// 56 UN_FIRST_SOURCE
			ps.setString(56, entity.getFirstSource());

			// 57 UN_FINAL_SOURCE
			ps.setString(57, entity.getFinalSource());

			// 58 CONNECTOR_NAME
			ps.setString(58, entity.getConnectorName());

			// 59 LAN
			ps.setString(59, entity.getLan());

			// 60 RELIGION
			ps.setString(60, entity.getReligion());

			// 61 PROFESSION
			ps.setString(61, entity.getProfession());

			// 62 SC_ST_FLAG
			ps.setString(62, entity.getScStFlag());

			// 63 SEX
			ps.setString(63, entity.getSex());

			// 64 MARITAL_STATUS
			ps.setString(64, entity.getMaritalStatus());

			// 65 QUALIFICATION
			ps.setString(65, entity.getQualification());

			// 66 ADDRESSTYPE
			ps.setString(66, entity.getAddressType());

			// 67 EMAIL_COMMUNICATION
			ps.setString(67, entity.getEmailCommunication());

			// 68 CALL_COMMUNICATION
			ps.setString(68, entity.getCallCommunication());

			// 69 PPI_AMT
			ps.setString(69, entity.getPpiAmt());

			// 70 APPLICATION_DATE
			ps.setString(70, entity.getApplicationDate());
			/*
			 * if (entity.getApplicationDate() != null) { ps.setTimestamp(70, new
			 * Timestamp(entity.getApplicationDate().getTime())); } else { ps.setNull(70,
			 * Types.TIMESTAMP); }
			 */

			// 71 UPLOAD_DISBURSAL_DATE
			ps.setString(71, entity.getUploadDisbursalDate());
			/*
			 * if (entity.getUploadDisbursalDate() != null) { ps.setTimestamp(71, new
			 * Timestamp(entity.getUploadDisbursalDate().getTime())); } else {
			 * ps.setNull(71, Types.TIMESTAMP); }
			 */

			// 72 LOANTYPE
			ps.setString(72, entity.getLoanType());

			// 73 EFFRATE
			ps.setString(73, entity.getEffRate());

			// 74 CHARGE_CODE1
			ps.setString(74, entity.getChargeCode1());

			// 75 CHARGE_AMOUNT1
			ps.setString(75, entity.getChargeAmount1());

			// 76 CHARGE_CODE2
			ps.setString(76, entity.getChargeCode2());

			// 77 CHARGE_AMOUNT2
			ps.setString(77, entity.getChargeAmount2());

			// 78 AnnualisedAPR
			ps.setString(78, entity.getAnnualisedApr());

			// 79 PSL_FLAG
			ps.setString(79, entity.getPslFlag());

			// 80 PSL_CODE
			ps.setString(80, entity.getPslCode());

			// 81 SME_CODE
			ps.setString(81, entity.getSmeCode());

			// 82 HNICODE
			ps.setString(82, entity.getHniCode());

			// 83 OWNED_IRRIGATED
			ps.setString(83, entity.getOwnedIrrigated());

			// 84 OWNED_NON_IRRIGATED
			ps.setString(84, entity.getOwnedNonIrrigated());

			// 85 LEASED_IN_IRRIGATED
			ps.setString(85, entity.getLeasedInIrrigated());

			// 86 LEASED_IN_NON_IRRIGATED
			ps.setString(86, entity.getLeasedOutNonIrrigated());

			// 87 LEASED_OUT_IRRIGATED
			ps.setString(87, entity.getLeasedOutIrrigated());

			// 88 LEASED_OUT_NON_IRRIGATED
			ps.setString(88, entity.getLeasedOutNonIrrigated());

			// 89 DISBURSAL_TO
			ps.setString(89, entity.getDisbursalTo());

			// 90 CROSSCOLL_WITH
			ps.setString(90, entity.getCrossCollWith());

			// 91 UMRN_NUMBER
			ps.setString(91, entity.getUmrnNumber());

			// 92 NPCI_MANDATE_UPLD_DATE
			ps.setString(92, entity.getNpciMandateUpldDate());
			/*
			 * if (entity.getNpciMandateUpldDate() != null) { ps.setTimestamp(92, new
			 * Timestamp(entity.getNpciMandateUpldDate().getTime())); } else {
			 * ps.setNull(92, Types.TIMESTAMP); }
			 */

			// 93 REGISTRATION_STATUS
			ps.setString(93, entity.getRegistrationStatus());

			// 94 NACH_EFFECTIVE_DATE
			ps.setString(94, entity.getNachEffectiveDate());
			/*
			 * if (entity.getNachEffectiveDate() != null) { ps.setTimestamp(94, new
			 * Timestamp(entity.getNachEffectiveDate().getTime())); } else { ps.setNull(94,
			 * Types.TIMESTAMP); }
			 */

			// 95 REGISTRATION_AMOUNT
			ps.setString(95, entity.getRegistrationAmount());

			// 96 UMRN_ACCEPTANCE_DATE
			ps.setString(96, entity.getUmrnAcceptanceDate());
			/*
			 * if (entity.getUmrnAcceptanceDate() != null) { ps.setTimestamp(96, new
			 * Timestamp(entity.getUmrnAcceptanceDate().getTime())); } else { ps.setNull(96,
			 * Types.TIMESTAMP); }
			 */

			// 97 REJECT_CODE
			ps.setString(97, entity.getRejectCode());

			// 98 FATHER_NAME
			ps.setString(98, entity.getFatherName());

			// 99 SPOUSE_NAME
			ps.setString(99, entity.getSpouseName());

			// 100 INDUSTRY
			ps.setString(100, entity.getIndustry());

			// 101 LOAN_PURPOSE
			ps.setString(101, entity.getLoanPurpose());

			// 102 MARGIN_MONEY_CODE
			ps.setString(102, entity.getMarginMoneyCode());

			// 103 DMA_CODE
			ps.setString(103, entity.getDmaCode());

			// 104 is_rbi_declaration_selected
			// ps.setLong(104, entity.getIsRbiDeclarationSelected()? 1:0);
			ps.setLong(104, Boolean.TRUE.equals(entity.getIsRbiDeclarationSelected()) ? 1 : 0);

			// 105 Ecode
			ps.setString(105, entity.getEcode());

			// 106 Referral code
			ps.setString(106, entity.getReferralCode());

			// 107 CREATED_BY
			ps.setString(107, entity.getCreatedBy());

			// 108 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(108, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(108, Types.TIMESTAMP);
			}

			// 109 ECS_ACCTNO
			ps.setString(109, entity.getEcsAccNo());

			// 110 FROM_CYCLE_DATE
			if (entity.getFromCycleDate() != null) {
				ps.setDate(110, new java.sql.Date(entity.getFromCycleDate().getTime()));
			} else {
				ps.setNull(110, java.sql.Types.DATE);
			}

			// 111 TO_CYCLE_DATE
			if (entity.getToCycleDate() != null) {
				ps.setDate(111, new java.sql.Date(entity.getToCycleDate().getTime()));
			} else {
				ps.setNull(111, java.sql.Types.DATE);
			}

			/*
			 * // 110 FROM_CYCLE_DATE ps.setDate(110, new java.sql.Date
			 * (entity.getFromCycleDate().getTime()));
			 * 
			 * // 111 TO_CYCLE_DATE ps.setDate(111, new java.sql.Date
			 * (entity.getToCycleDate().getTime()));
			 */

			// 112 REMARKS
			ps.setString(112, entity.getRemarks());

			// 113 CONSTITUTION
			ps.setString(113, entity.getConstitution());

			// 114 INSTALMENT_START_DATE
			// ps.setString(114, entity.getConstitution());

			// 117 ERROR_MSG
			ps.setString(114, entity.getErrorMsg());

			// 119 ROW_NUMBER
			if (entity.getRowNumber() != null) {
				ps.setInt(115, entity.getRowNumber());
			} else {
				ps.setNull(115, Types.INTEGER);
			}

			// 119 UPLOAD_ID
			ps.setString(116, entity.getUploadId());

		});

	}

	private String normalizeHeader(String header) {
		if (header == null)
			return null;
		System.out.println("header:: " + header);
		return header.trim().replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_").toUpperCase();
	}

	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		try {
			// String uploadId =
			// alddErrorRepository.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

			String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_ALDD_TRANS_DUMP_ERROR WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
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

			// List<AlddReportError> errors =
			// alddErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * " + "FROM TM_VHL_ALDD_TRANS_DUMP_ERROR " + "WHERE upload_id = ?"
					+ " OR ora_err_tag$ = ?";

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

			Sheet sheet = workbook.createSheet("ALDD_ERROR");

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
			cell0.setCellValue("MONTH");
			cell0.setCellStyle(headerStyle);

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("APPLICATION_NO");
			cell1.setCellStyle(headerStyle);

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("LAN");
			cell2.setCellStyle(headerStyle);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("ID");
			cell3.setCellStyle(headerStyle);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("FILENO");
			cell4.setCellStyle(headerStyle);

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("APPLICATION_NO_BK");
			cell5.setCellStyle(headerStyle);

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("BRANCH_CODE");
			cell6.setCellStyle(headerStyle);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("PRODUCT");
			cell7.setCellStyle(headerStyle);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("SCHEME");
			cell8.setCellStyle(headerStyle);

			Cell cell9 = header.createCell(9);
			cell9.setCellValue("SCHEME_ID");
			cell9.setCellStyle(headerStyle);

			Cell cell10 = header.createCell(10);
			cell10.setCellValue("SOL_ID");
			cell10.setCellStyle(headerStyle);

			Cell cell11 = header.createCell(11);
			cell11.setCellValue("CUST_FNAME");
			cell11.setCellStyle(headerStyle);

			Cell cell12 = header.createCell(12);
			cell12.setCellValue("CUST_MNAME");
			cell12.setCellStyle(headerStyle);

			Cell cell13 = header.createCell(13);
			cell13.setCellValue("CUST_LNAME");
			cell13.setCellStyle(headerStyle);

			Cell cell14 = header.createCell(14);
			cell14.setCellValue("CONSTID");
			cell14.setCellStyle(headerStyle);

			Cell cell15 = header.createCell(15);
			cell15.setCellValue("DOB");
			cell15.setCellStyle(headerStyle);

			Cell cell16 = header.createCell(16);
			cell16.setCellValue("IND_CORP_FLAG");
			cell16.setCellStyle(headerStyle);

			Cell cell17 = header.createCell(17);
			cell17.setCellValue("CURR_ADD1");
			cell17.setCellStyle(headerStyle);

			Cell cell18 = header.createCell(18);
			cell18.setCellValue("CURR_ADD2");
			cell18.setCellStyle(headerStyle);

			Cell cell19 = header.createCell(19);
			cell19.setCellValue("CURR_ADD3");
			cell19.setCellStyle(headerStyle);

			Cell cell20 = header.createCell(20);
			cell20.setCellValue("CITY");
			cell20.setCellStyle(headerStyle);

			Cell cell21 = header.createCell(21);
			cell21.setCellValue("STATE");
			cell21.setCellStyle(headerStyle);

			Cell cell22 = header.createCell(22);
			cell22.setCellValue("ZIPCODE");
			cell22.setCellStyle(headerStyle);

			Cell cell23 = header.createCell(23);
			cell23.setCellValue("PHONE1");
			cell23.setCellStyle(headerStyle);

			Cell cell24 = header.createCell(24);
			cell24.setCellValue("PHONE2");
			cell24.setCellStyle(headerStyle);

			Cell cell25 = header.createCell(25);
			cell25.setCellValue("MOBILE");
			cell25.setCellStyle(headerStyle);

			Cell cell26 = header.createCell(26);
			cell26.setCellValue("ASSET_TYPE");
			cell26.setCellStyle(headerStyle);

			Cell cell27 = header.createCell(27);
			cell27.setCellValue("MAKE");
			cell27.setCellStyle(headerStyle);

			Cell cell28 = header.createCell(28);
			cell28.setCellValue("MODEL_BK");
			cell28.setCellStyle(headerStyle);

			Cell cell29 = header.createCell(29);
			cell29.setCellValue("SUB_MODEL");
			cell29.setCellStyle(headerStyle);

			Cell cell30 = header.createCell(30);
			cell30.setCellValue("ASSETCOST");
			cell30.setCellStyle(headerStyle);

			Cell cell31 = header.createCell(31);
			cell31.setCellValue("MARGIN_MONEY");
			cell31.setCellStyle(headerStyle);

			Cell cell32 = header.createCell(32);
			cell32.setCellValue("AMOUNTFINANCED");
			cell32.setCellStyle(headerStyle);

			Cell cell33 = header.createCell(33);
			cell33.setCellValue("TENURE");
			cell33.setCellStyle(headerStyle);

			Cell cell34 = header.createCell(34);
			cell34.setCellValue("EMI");
			cell34.setCellStyle(headerStyle);

			Cell cell35 = header.createCell(35);
			cell35.setCellValue("IRR");
			cell35.setCellStyle(headerStyle);

			Cell cell36 = header.createCell(36);
			cell36.setCellValue("ADVANCE_EMI");
			cell36.setCellStyle(headerStyle);

			Cell cell37 = header.createCell(37);
			cell37.setCellValue("INSTALMENT_START_DATE");
			cell37.setCellStyle(headerStyle);

			Cell cell38 = header.createCell(38);
			cell38.setCellValue("INSTL_TYPE");
			cell38.setCellStyle(headerStyle);

			Cell cell39 = header.createCell(39);
			cell39.setCellValue("FREQ");
			cell39.setCellStyle(headerStyle);

			Cell cell40 = header.createCell(40);
			cell40.setCellValue("SUPPLIER");
			cell40.setCellStyle(headerStyle);

			Cell cell41 = header.createCell(41);
			cell41.setCellValue("BROKER");
			cell41.setCellStyle(headerStyle);

			Cell cell42 = header.createCell(42);
			cell42.setCellValue("REP_MODE");
			cell42.setCellStyle(headerStyle);

			Cell cell43 = header.createCell(43);
			cell43.setCellValue("ACCT_NUMBER");
			cell43.setCellStyle(headerStyle);

			Cell cell44 = header.createCell(44);
			cell44.setCellValue("BANK_ACC_NO");
			cell44.setCellStyle(headerStyle);

			Cell cell45 = header.createCell(45);
			cell45.setCellValue("ECS_ACCTNO");
			cell45.setCellStyle(headerStyle);

			Cell cell46 = header.createCell(46);
			cell46.setCellValue("ECS_MICR");
			cell46.setCellStyle(headerStyle);

			Cell cell47 = header.createCell(47);
			cell47.setCellValue("INDUSTRYDESC");
			cell47.setCellStyle(headerStyle);

			Cell cell48 = header.createCell(48);
			cell48.setCellValue("PROMOTION_DESC");
			cell48.setCellStyle(headerStyle);

			Cell cell49 = header.createCell(49);
			cell49.setCellValue("ASSET_CATAGORY");
			cell49.setCellStyle(headerStyle);

			Cell cell50 = header.createCell(50);
			cell50.setCellValue("CHANNELCODE");
			cell50.setCellStyle(headerStyle);

			Cell cell51 = header.createCell(51);
			cell51.setCellValue("EMPLOYEE_NAME");
			cell51.setCellStyle(headerStyle);

			Cell cell52 = header.createCell(52);
			cell52.setCellValue("DME");
			cell52.setCellStyle(headerStyle);

			Cell cell53 = header.createCell(53);
			cell53.setCellValue("MKTG_OFFICER");
			cell53.setCellStyle(headerStyle);

			Cell cell54 = header.createCell(54);
			cell54.setCellValue("FIRST_SOURCE");
			cell54.setCellStyle(headerStyle);

			Cell cell55 = header.createCell(55);
			cell55.setCellValue("FINAL_SOURCE");
			cell55.setCellStyle(headerStyle);

			Cell cell56 = header.createCell(56);
			cell56.setCellValue("UN_FIRST_SOURCE");
			cell56.setCellStyle(headerStyle);

			Cell cell57 = header.createCell(57);
			cell57.setCellValue("UN_FINAL_SOURCE");
			cell57.setCellStyle(headerStyle);

			Cell cell58 = header.createCell(58);
			cell58.setCellValue("CONNECTOR_NAME");
			cell58.setCellStyle(headerStyle);

			Cell cell59 = header.createCell(59);
			cell59.setCellValue("LAN_BK");
			cell59.setCellStyle(headerStyle);

			Cell cell60 = header.createCell(60);
			cell60.setCellValue("RELIGION");
			cell60.setCellStyle(headerStyle);

			Cell cell61 = header.createCell(61);
			cell61.setCellValue("PROFESSION");
			cell61.setCellStyle(headerStyle);

			Cell cell62 = header.createCell(62);
			cell62.setCellValue("SC_ST_FLAG");
			cell62.setCellStyle(headerStyle);

			Cell cell63 = header.createCell(63);
			cell63.setCellValue("SEX");
			cell63.setCellStyle(headerStyle);

			Cell cell64 = header.createCell(64);
			cell64.setCellValue("MARITAL_STATUS");
			cell64.setCellStyle(headerStyle);

			Cell cell65 = header.createCell(65);
			cell65.setCellValue("QUALIFICATION");
			cell65.setCellStyle(headerStyle);

			Cell cell66 = header.createCell(66);
			cell66.setCellValue("ADDRESSTYPE");
			cell66.setCellStyle(headerStyle);

			Cell cell67 = header.createCell(67);
			cell67.setCellValue("EMAIL_COMMUNICATION");
			cell67.setCellStyle(headerStyle);

			Cell cell68 = header.createCell(68);
			cell68.setCellValue("CALL_COMMUNICATION");
			cell68.setCellStyle(headerStyle);

			Cell cell69 = header.createCell(69);
			cell69.setCellValue("PPI_AMT");
			cell69.setCellStyle(headerStyle);

			Cell cell70 = header.createCell(70);
			cell70.setCellValue("APPLICATION_DATE");
			cell70.setCellStyle(headerStyle);

			Cell cell71 = header.createCell(71);
			cell71.setCellValue("UPLOAD_DISBURSAL_DATE");
			cell71.setCellStyle(headerStyle);

			Cell cell72 = header.createCell(72);
			cell72.setCellValue("LOANTYPE");
			cell72.setCellStyle(headerStyle);

			Cell cell73 = header.createCell(73);
			cell73.setCellValue("EFFRATE");
			cell73.setCellStyle(headerStyle);

			Cell cell74 = header.createCell(74);
			cell74.setCellValue("CHARGE_CODE1");
			cell74.setCellStyle(headerStyle);

			Cell cell75 = header.createCell(75);
			cell75.setCellValue("CHARGE_AMOUNT1");
			cell75.setCellStyle(headerStyle);

			Cell cell76 = header.createCell(76);
			cell76.setCellValue("CHARGE_CODE2");
			cell76.setCellStyle(headerStyle);

			Cell cell77 = header.createCell(77);
			cell77.setCellValue("CHARGE_AMOUNT2");
			cell77.setCellStyle(headerStyle);

			Cell cell78 = header.createCell(78);
			cell78.setCellValue("ANNUALISEDAPR");
			cell78.setCellStyle(headerStyle);

			Cell cell79 = header.createCell(79);
			cell79.setCellValue("PSL_FLAG");
			cell79.setCellStyle(headerStyle);

			Cell cell80 = header.createCell(80);
			cell80.setCellValue("PSL_CODE");
			cell80.setCellStyle(headerStyle);

			Cell cell81 = header.createCell(81);
			cell81.setCellValue("SME_CODE");
			cell81.setCellStyle(headerStyle);

			Cell cell82 = header.createCell(82);
			cell82.setCellValue("HNICODE");
			cell82.setCellStyle(headerStyle);

			Cell cell83 = header.createCell(83);
			cell83.setCellValue("OWNED_IRRIGATED");
			cell83.setCellStyle(headerStyle);

			Cell cell84 = header.createCell(84);
			cell84.setCellValue("OWNED_NON_IRRIGATED");
			cell84.setCellStyle(headerStyle);

			Cell cell85 = header.createCell(85);
			cell85.setCellValue("LEASED_IN_IRRIGATED");
			cell85.setCellStyle(headerStyle);

			Cell cell86 = header.createCell(86);
			cell86.setCellValue("LEASED_IN_NON_IRRIGATED");
			cell86.setCellStyle(headerStyle);

			Cell cell87 = header.createCell(87);
			cell87.setCellValue("LEASED_OUT_IRRIGATED");
			cell87.setCellStyle(headerStyle);

			Cell cell88 = header.createCell(88);
			cell88.setCellValue("LEASED_OUT_NON_IRRIGATED");
			cell88.setCellStyle(headerStyle);

			Cell cell89 = header.createCell(89);
			cell89.setCellValue("DISBURSAL_TO");
			cell89.setCellStyle(headerStyle);

			Cell cell90 = header.createCell(90);
			cell90.setCellValue("CROSSCOLL_WITH");
			cell90.setCellStyle(headerStyle);

			Cell cell91 = header.createCell(91);
			cell91.setCellValue("UMRN_NUMBER");
			cell91.setCellStyle(headerStyle);

			Cell cell92 = header.createCell(92);
			cell92.setCellValue("NPCI_MANDATE_UPLD_DATE");
			cell92.setCellStyle(headerStyle);

			Cell cell93 = header.createCell(93);
			cell93.setCellValue("NPCI_MANDATE_UPLD_DATE");
			cell93.setCellStyle(headerStyle);

			Cell cell94 = header.createCell(94);
			cell94.setCellValue("REGISTRATION_STATUS");
			cell94.setCellStyle(headerStyle);

			Cell cell95 = header.createCell(95);
			cell95.setCellValue("NACH_EFFECTIVE_DATE");
			cell95.setCellStyle(headerStyle);

			Cell cell96 = header.createCell(96);
			cell96.setCellValue("REGISTRATION_AMOUNT");
			cell96.setCellStyle(headerStyle);

			Cell cell97 = header.createCell(97);
			cell97.setCellValue("UMRN_ACCEPTANCE_DATE");
			cell97.setCellStyle(headerStyle);

			Cell cell98 = header.createCell(98);
			cell98.setCellValue("REJECT_CODE");
			cell98.setCellStyle(headerStyle);

			Cell cell99 = header.createCell(99);
			cell99.setCellValue("FATHER_NAME");
			cell99.setCellStyle(headerStyle);

			Cell cell100 = header.createCell(100);
			cell100.setCellValue("SPOUSE_NAME");
			cell100.setCellStyle(headerStyle);

			Cell cell101 = header.createCell(101);
			cell101.setCellValue("INDUSTRY");
			cell101.setCellStyle(headerStyle);

			Cell cell102 = header.createCell(102);
			cell102.setCellValue("LOAN_PURPOSE");
			cell102.setCellStyle(headerStyle);

			Cell cell103 = header.createCell(103);
			cell103.setCellValue("MARGIN_MONEY_CODE");
			cell103.setCellStyle(headerStyle);

			Cell cell104 = header.createCell(104);
			cell104.setCellValue("DMA_CODE");
			cell104.setCellStyle(headerStyle);

			Cell cell105 = header.createCell(105);
			cell105.setCellValue("is_rbi_declaration_selected");
			cell105.setCellStyle(headerStyle);

			Cell cell106 = header.createCell(106);
			cell106.setCellValue("Ecode");
			cell106.setCellStyle(headerStyle);

			Cell cell107 = header.createCell(107);
			cell107.setCellValue("Referral code");
			cell107.setCellStyle(headerStyle);

			/*
			 * Cell cell107 = header.createCell(6); cell107.setCellValue("ERROR_ID");
			 * cell107.setCellStyle(headerStyle);
			 * 
			 * Cell cell108 = header.createCell(6); cell108.setCellValue("ROW_NUMBER");
			 * cell108.setCellStyle(headerStyle);
			 */

			Cell cell108 = header.createCell(108);
			cell108.setCellValue("ERROR_REASON");
			cell108.setCellStyle(headerStyle);

			Cell cell109 = header.createCell(109);
			cell109.setCellValue("UPLOAD_DATE");
			cell109.setCellStyle(headerStyle);

			// ---------------------------------------------------
			// Step 4: Write Data Rows
			// ---------------------------------------------------

			int rowIndex = 1;

			/* for (AlddReportError error : errors) { */
			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("MONTH")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("APPLICATION_NO")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("LAN")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("ID")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("FILENO")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("APPLICATION_NO_BK")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(error.get("BRANCH_CODE")));
				cell06.setCellStyle(dataStyle);

				Cell cell07 = row.createCell(7);
				cell07.setCellValue(formatValue(error.get("PRODUCT")));
				cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(8);
				cell08.setCellValue(formatValue(error.get("SCHEME")));
				cell08.setCellStyle(dataStyle);

				Cell cell09 = row.createCell(9);
				cell09.setCellValue(formatValue(error.get("SCHEME_ID")));
				cell09.setCellStyle(dataStyle);

				Cell cell111 = row.createCell(10);
				cell111.setCellValue(formatValue(error.get("SOL_ID")));
				cell111.setCellStyle(dataStyle);

				Cell cell112 = row.createCell(11);
				cell112.setCellValue(formatValue(error.get("CUST_FNAME")));
				cell112.setCellStyle(dataStyle);

				Cell cell113 = row.createCell(12);
				cell113.setCellValue(formatValue(error.get("CUST_MNAME")));
				cell113.setCellStyle(dataStyle);

				Cell cell114 = row.createCell(13);
				cell114.setCellValue(formatValue(error.get("CUST_LNAME")));
				cell114.setCellStyle(dataStyle);

				Cell cell115 = row.createCell(14);
				cell115.setCellValue(formatValue(error.get("CONSTID")));
				cell115.setCellStyle(dataStyle);

				Cell cell116 = row.createCell(15);
				cell116.setCellValue(formatValue(error.get("DOB")));
				cell116.setCellStyle(dataStyle);

				Cell cell117 = row.createCell(16);
				cell117.setCellValue(formatValue(error.get("IND_CORP_FLAG")));
				cell117.setCellStyle(dataStyle);

				Cell cell118 = row.createCell(17);
				cell118.setCellValue(formatValue(error.get("CURR_ADD1")));
				cell118.setCellStyle(dataStyle);

				Cell cell119 = row.createCell(18);
				cell119.setCellValue(formatValue(error.get("CURR_ADD2")));
				cell119.setCellStyle(dataStyle);

				Cell cell120 = row.createCell(19);
				cell120.setCellValue(formatValue(error.get("CURR_ADD3")));
				cell120.setCellStyle(dataStyle);

				Cell cell121 = row.createCell(20);
				cell121.setCellValue(formatValue(error.get("CITY")));
				cell121.setCellStyle(dataStyle);

				Cell cell122 = row.createCell(21);
				cell122.setCellValue(formatValue(error.get("STATE")));
				cell122.setCellStyle(dataStyle);

				Cell cell123 = row.createCell(22);
				cell123.setCellValue(formatValue(error.get("ZIPCODE")));
				cell123.setCellStyle(dataStyle);

				Cell cell124 = row.createCell(23);
				cell124.setCellValue(formatValue(error.get("PHONE1")));
				cell124.setCellStyle(dataStyle);

				Cell cell125 = row.createCell(24);
				cell125.setCellValue(formatValue(error.get("PHONE2")));
				cell125.setCellStyle(dataStyle);

				Cell cell126 = row.createCell(25);
				cell126.setCellValue(formatValue(error.get("MOBILE")));
				cell126.setCellStyle(dataStyle);

				Cell cell127 = row.createCell(26);
				cell127.setCellValue(formatValue(error.get("ASSET_TYPE")));
				cell127.setCellStyle(dataStyle);

				Cell cell128 = row.createCell(27);
				cell128.setCellValue(formatValue(error.get("MAKE")));
				cell128.setCellStyle(dataStyle);

				Cell cell129 = row.createCell(28);
				cell129.setCellValue(formatValue(error.get("MODEL_BK")));
				cell129.setCellStyle(dataStyle);

				Cell cell130 = row.createCell(29);
				cell130.setCellValue(formatValue(error.get("SUB_MODEL")));
				cell130.setCellStyle(dataStyle);

				Cell cell131 = row.createCell(30);
				cell131.setCellValue(formatValue(error.get("ASSETCOST")));
				cell131.setCellStyle(dataStyle);

				Cell cell132 = row.createCell(31);
				cell132.setCellValue(formatValue(error.get("MARGIN_MONEY")));
				cell132.setCellStyle(dataStyle);

				Cell cell133 = row.createCell(32);
				cell133.setCellValue(formatValue(error.get("AMOUNTFINANCED")));
				cell133.setCellStyle(dataStyle);

				Cell cell134 = row.createCell(33);
				cell134.setCellValue(formatValue(error.get("TENURE")));
				cell134.setCellStyle(dataStyle);

				Cell cell135 = row.createCell(34);
				cell135.setCellValue(formatValue(error.get("EMI")));
				cell135.setCellStyle(dataStyle);

				Cell cell136 = row.createCell(35);
				cell136.setCellValue(formatValue(error.get("IRR")));
				cell136.setCellStyle(dataStyle);

				Cell cell137 = row.createCell(36);
				cell137.setCellValue(formatValue(error.get("ADVANCE_EMI")));
				cell137.setCellStyle(dataStyle);

				Cell cell138 = row.createCell(37);
				cell138.setCellValue(formatValue(error.get("INSTALMENT_START_DATE")));
				cell138.setCellStyle(dataStyle);

				Cell cell139 = row.createCell(38);
				cell139.setCellValue(formatValue(error.get("INSTL_TYPE")));
				cell139.setCellStyle(dataStyle);

				Cell cell140 = row.createCell(39);
				cell140.setCellValue(formatValue(error.get("FREQ")));
				cell140.setCellStyle(dataStyle);

				Cell cell141 = row.createCell(40);
				cell141.setCellValue(formatValue(error.get("SUPPLIER")));
				cell141.setCellStyle(dataStyle);

				Cell cell142 = row.createCell(41);
				cell142.setCellValue(formatValue(error.get("BROKER")));
				cell142.setCellStyle(dataStyle);

				Cell cell143 = row.createCell(42);
				cell143.setCellValue(formatValue(error.get("REP_MODE")));
				cell143.setCellStyle(dataStyle);

				Cell cell144 = row.createCell(43);
				cell144.setCellValue(formatValue(error.get("ACCT_NUMBER")));
				cell144.setCellStyle(dataStyle);

				Cell cell145 = row.createCell(44);
				cell145.setCellValue(formatValue(error.get("BANK_ACC_NO")));
				cell145.setCellStyle(dataStyle);

				Cell cell146 = row.createCell(45);
				cell146.setCellValue(formatValue(error.get("ECS_ACCNO")));
				cell146.setCellStyle(dataStyle);

				Cell cell147 = row.createCell(46);
				cell147.setCellValue(formatValue(error.get("ECS_MICR")));
				cell147.setCellStyle(dataStyle);

				Cell cell148 = row.createCell(47);
				cell148.setCellValue(formatValue(error.get("INDUSTRYDESC")));
				cell148.setCellStyle(dataStyle);

				Cell cell149 = row.createCell(48);
				cell149.setCellValue(formatValue(error.get("PROMOTION_DESC")));
				cell149.setCellStyle(dataStyle);

				Cell cell150 = row.createCell(49);
				cell150.setCellValue(formatValue(error.get("ASSET_CATAGORY")));
				cell150.setCellStyle(dataStyle);

				Cell cell151 = row.createCell(50);
				cell151.setCellValue(formatValue(error.get("CHANNELCODE")));
				cell151.setCellStyle(dataStyle);

				Cell cell152 = row.createCell(51);
				cell152.setCellValue(formatValue(error.get("EMPLOYEE_NAME")));
				cell152.setCellStyle(dataStyle);

				Cell cell153 = row.createCell(52);
				cell153.setCellValue(formatValue(error.get("DME")));
				cell153.setCellStyle(dataStyle);

				Cell cell154 = row.createCell(53);
				cell154.setCellValue(formatValue(error.get("MKTG_OFFICER")));
				cell154.setCellStyle(dataStyle);

				Cell cell155 = row.createCell(54);
				cell155.setCellValue(formatValue(error.get("FIRST_SOURCE")));
				cell155.setCellStyle(dataStyle);

				Cell cell156 = row.createCell(55);
				cell156.setCellValue(formatValue(error.get("FINAL_SOURCE")));
				cell156.setCellStyle(dataStyle);

				Cell cell157 = row.createCell(56);
				cell157.setCellValue(formatValue(error.get("UN_FIRST_SOURCE")));
				cell157.setCellStyle(dataStyle);

				Cell cell158 = row.createCell(57);
				cell158.setCellValue(formatValue(error.get("UN_FINAL_SOURCE")));
				cell158.setCellStyle(dataStyle);

				Cell cell159 = row.createCell(58);
				cell159.setCellValue(formatValue(error.get("CONNECTOR_NAME")));
				cell159.setCellStyle(dataStyle);

				Cell cell160 = row.createCell(59);
				cell160.setCellValue(formatValue(error.get("LAN_BK")));
				cell160.setCellStyle(dataStyle);

				Cell cell161 = row.createCell(60);
				cell161.setCellValue(formatValue(error.get("RELIGION")));
				cell161.setCellStyle(dataStyle);

				Cell cell162 = row.createCell(61);
				cell162.setCellValue(formatValue(error.get("PROFESSION")));
				cell162.setCellStyle(dataStyle);

				Cell cell163 = row.createCell(62);
				cell163.setCellValue(formatValue(error.get("SC_ST_FLAG")));
				cell163.setCellStyle(dataStyle);

				Cell cell164 = row.createCell(63);
				cell164.setCellValue(formatValue(error.get("SEX")));
				cell164.setCellStyle(dataStyle);

				Cell cell165 = row.createCell(64);
				cell165.setCellValue(formatValue(error.get("MARITAL_STATUS")));
				cell165.setCellStyle(dataStyle);

				Cell cell166 = row.createCell(65);
				cell166.setCellValue(formatValue(error.get("QUALIFICATION")));
				cell166.setCellStyle(dataStyle);

				Cell cell167 = row.createCell(66);
				cell167.setCellValue(formatValue(error.get("ADDRESSTYPE")));
				cell167.setCellStyle(dataStyle);

				Cell cell168 = row.createCell(67);
				cell168.setCellValue(formatValue(error.get("EMAIL_COMMUNICATION")));
				cell168.setCellStyle(dataStyle);

				Cell cell169 = row.createCell(68);
				cell169.setCellValue(formatValue(error.get("CALL_COMMUNICATION")));
				cell169.setCellStyle(dataStyle);

				Cell cell170 = row.createCell(69);
				cell170.setCellValue(formatValue(error.get("PPI_AMT")));
				cell170.setCellStyle(dataStyle);

				Cell cell171 = row.createCell(70);
				cell171.setCellValue(formatValue(error.get("APPLICATION_DATE")));
				cell171.setCellStyle(dataStyle);

				Cell cell172 = row.createCell(71);
				cell172.setCellValue(formatValue(error.get("UPLOAD_DISBURSAL_DATE")));
				cell172.setCellStyle(dataStyle);

				Cell cell173 = row.createCell(72);
				cell173.setCellValue(formatValue(error.get("LOANTYPE")));
				cell173.setCellStyle(dataStyle);

				Cell cell174 = row.createCell(73);
				cell174.setCellValue(formatValue(error.get("EFFRATE")));
				cell174.setCellStyle(dataStyle);

				Cell cell175 = row.createCell(74);
				cell175.setCellValue(formatValue(error.get("CHARGE_CODE1")));
				cell175.setCellStyle(dataStyle);

				Cell cell176 = row.createCell(75);
				cell176.setCellValue(formatValue(error.get("CHARGE_AMOUNT1")));
				cell176.setCellStyle(dataStyle);

				Cell cell177 = row.createCell(76);
				cell177.setCellValue(formatValue(error.get("CHARGE_CODE2")));
				cell177.setCellStyle(dataStyle);

				Cell cell178 = row.createCell(77);
				cell178.setCellValue(formatValue(error.get("CHARGE_AMOUNT2")));
				cell178.setCellStyle(dataStyle);

				Cell cell179 = row.createCell(78);
				cell179.setCellValue(formatValue(error.get("ANNUALISEDAPR")));
				cell179.setCellStyle(dataStyle);

				Cell cell180 = row.createCell(79);
				cell180.setCellValue(formatValue(error.get("PSL_FLAG")));
				cell180.setCellStyle(dataStyle);

				Cell cell181 = row.createCell(80);
				cell181.setCellValue(formatValue(error.get("PSL_CODE")));
				cell181.setCellStyle(dataStyle);

				Cell cell182 = row.createCell(81);
				cell182.setCellValue(formatValue(error.get("SME_CODE")));
				cell182.setCellStyle(dataStyle);

				Cell cell183 = row.createCell(82);
				cell183.setCellValue(formatValue(error.get("HNICODE")));
				cell183.setCellStyle(dataStyle);

				Cell cell184 = row.createCell(83);
				cell184.setCellValue(formatValue(error.get("OWNED_IRRIGATED")));
				cell184.setCellStyle(dataStyle);

				Cell cell185 = row.createCell(84);
				cell185.setCellValue(formatValue(error.get("OWNED_NON_IRRIGATED")));
				cell185.setCellStyle(dataStyle);

				Cell cell186 = row.createCell(85);
				cell186.setCellValue(formatValue(error.get("LEASED_IN_IRRIGATED")));
				cell186.setCellStyle(dataStyle);

				Cell cell187 = row.createCell(86);
				cell187.setCellValue(formatValue(error.get("LEASED_IN_NON_IRRIGATED")));
				cell187.setCellStyle(dataStyle);

				Cell cell188 = row.createCell(87);
				cell188.setCellValue(formatValue(error.get("LEASED_OUT_IRRIGATED")));
				cell188.setCellStyle(dataStyle);

				Cell cell189 = row.createCell(88);
				cell189.setCellValue(formatValue(error.get("LEASED_OUT_NON_IRRIGATED")));
				cell189.setCellStyle(dataStyle);

				Cell cell190 = row.createCell(89);
				cell190.setCellValue(formatValue(error.get("DISBURSAL_TO")));
				cell190.setCellStyle(dataStyle);

				Cell cell191 = row.createCell(90);
				cell191.setCellValue(formatValue(error.get("CROSSCOLL_WITH")));
				cell191.setCellStyle(dataStyle);

				Cell cell192 = row.createCell(91);
				cell192.setCellValue(formatValue(error.get("UMRN_NUMBER")));
				cell192.setCellStyle(dataStyle);

				Cell cell193 = row.createCell(92);
				cell193.setCellValue(formatValue(error.get("NPCI_MANDATE_UPLD_DATE")));
				cell193.setCellStyle(dataStyle);

				Cell cell194 = row.createCell(93);
				cell194.setCellValue(formatValue(error.get("REGISTRATION_STATUS")));
				cell194.setCellStyle(dataStyle);

				Cell cell195 = row.createCell(94);
				cell195.setCellValue(formatValue(error.get("NACH_EFFECTIVE_DATE")));
				cell195.setCellStyle(dataStyle);

				Cell cell196 = row.createCell(95);
				cell196.setCellValue(formatValue(error.get("REGISTRATION_AMOUNT")));
				cell196.setCellStyle(dataStyle);

				Cell cell197 = row.createCell(96);
				cell197.setCellValue(formatValue(error.get("UMRN_ACCEPTANCE_DATE")));
				cell197.setCellStyle(dataStyle);

				Cell cell198 = row.createCell(97);
				cell198.setCellValue(formatValue(error.get("REJECT_CODE")));
				cell198.setCellStyle(dataStyle);

				Cell cell199 = row.createCell(98);
				cell199.setCellValue(formatValue(error.get("FATHER_NAME")));
				cell199.setCellStyle(dataStyle);

				Cell cell1111 = row.createCell(99);
				cell1111.setCellValue(formatValue(error.get("SPOUSE_NAME")));
				cell1111.setCellStyle(dataStyle);

				Cell cell1112 = row.createCell(100);
				cell1112.setCellValue(formatValue(error.get("INDUSTRY")));
				cell1112.setCellStyle(dataStyle);

				Cell cell1113 = row.createCell(101);
				cell1113.setCellValue(formatValue(error.get("LOAN_PURPOSE")));
				cell1113.setCellStyle(dataStyle);

				Cell cell1114 = row.createCell(102);
				cell1114.setCellValue(formatValue(error.get("MARGIN_MONEY_CODE")));
				cell1114.setCellStyle(dataStyle);

				Cell cell1115 = row.createCell(103);
				cell1115.setCellValue(formatValue(error.get("DMA_CODE")));
				cell1115.setCellStyle(dataStyle);

				Cell cell1116 = row.createCell(104);
				cell1116.setCellValue(formatValue(error.get("IS_RBI_DECLARATION_SELECTED")));
				cell1116.setCellStyle(dataStyle);

				Cell cell1117 = row.createCell(105);
				cell1117.setCellValue(formatValue(error.get("ECODE")));
				cell1117.setCellStyle(dataStyle);

				Cell cell1118 = row.createCell(106);
				cell1118.setCellValue(formatValue(error.get("REFERRAL_CODE")));
				cell1118.setCellStyle(dataStyle);
				/*
				 * Cell cell1119 = row.createCell(107);
				 * cell11110.setCellValue(formatValue(error.get("ERROR_ID")));
				 * cell11110.setCellStyle(dataStyle);
				 * 
				 * Cell cell1120 = row.createCell(108);
				 * cell1120.setCellValue(formatValue(error.get("ROW_NUMBER")));
				 * cell1120.setCellStyle(dataStyle);
				 */

				Cell cell1119 = row.createCell(107);
				cell1119.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell1119.setCellStyle(dataStyle);

				Cell cell11120 = row.createCell(108);
				cell11120.setCellValue(formatValue(error.get("UPLOAD_DATE")));
				cell11120.setCellStyle(dataStyle);
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
		Set<String> expectedHeaders = HEADER_FIELD_MAP.keySet();

		// Track actual headers found in Excel
		Set<String> actualHeaders = new HashSet<>();

		// Validation trackers
		Set<String> duplicateHeaders = new HashSet<>();
		Set<String> extraHeaders = new HashSet<>();

		int expectedHeaderCount = HEADER_FIELD_MAP.size();
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

			if (!HEADER_FIELD_MAP.containsKey(normalized)) {
				extraHeaders.add(normalized);
				continue;
			}

			matchedHeaderCount++;

			// Map header to entity field

			String fieldName = HEADER_FIELD_MAP.get(normalized);

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
		throw new RuntimeException("Primary key column Application No not found.");
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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_ALDD_TRANS_DUMP_UPL_SEQ.NEXTVAL FROM DUAL",
				long.class);

		return prefix + "_" + timestamp + "_" + sequence;
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

			List<String> result = entityManager.createQuery(
					"SELECT g.applicationNo FROM AlddTransaction g WHERE g.applicationNo IN :list AND g.fromCycleDate = :fromDate AND g.toCycleDate = :toDate",
					String.class).setParameter("list", batch)
					.setParameter("fromDate", cycleFromDate, javax.persistence.TemporalType.DATE)
					.setParameter("toDate", cycleToDate, javax.persistence.TemporalType.DATE)
					.setHint("org.hibernate.readOnly", true).getResultList();

			existingRecords.addAll(result);
		}

		return existingRecords;
	}

	// ============================================================
	// YOUR setFieldValue() – FULL DATE SUPPORT
	// ============================================================

	private void setFieldValue(Field field, Object entity, String value, Cell cell) throws Exception {

		Class<?> type = field.getType();
		String fieldName = field.getName();

		// logger.info("Field '{}' received equivalent value '{}' '{}'", fieldName,
		// value, type);

		if (isNullEquivalent(value)) {

			// logger.info("Field '{}' received null equivalent value '{}' '{}'", fieldName,
			// value, type);

			if (type.equals(String.class))
				field.set(entity, null);

			else if (type.equals(Integer.class) || type.equals(int.class))
				field.set(entity, 0);
			// field.set(entity, Integer.parseInt(value));

			else if (type.equals(Long.class) || type.equals(long.class))
				field.set(entity, 0L);
			// field.set(entity, Long.parseLong(value));

			else if (type.equals(Double.class) || type.equals(double.class))
				field.set(entity, (double) 0);

			else
				field.set(entity, null);

			return;
		}
		try {

			if (type.equals(String.class))
				field.set(entity, value);

			/*
			 * else if (type.equals(Integer.class) || type.equals(int.class))
			 * field.set(entity, Integer.parseInt(value));
			 * 
			 * else if (type.equals(Long.class) || type.equals(long.class))
			 * field.set(entity, Long.parseLong(value));
			 * 
			 * else if (type.equals(Long.class) || type.equals(long.class))
			 * field.set(entity, (long) Double.parseDouble(value));
			 * 
			 * else if (type.equals(BigDecimal.class)) field.set(entity, new
			 * BigDecimal(value));
			 */

			/*
			 * else if (type.equals(Long.class) || type.equals(long.class))
			 * field.set(entity, Long.parseLong(value));
			 */

			else if (type.equals(Integer.class) || type.equals(int.class))
				field.set(entity, (int) Double.parseDouble(value));

			else if (type.equals(Long.class) || type.equals(long.class))
				field.set(entity, (long) Double.parseDouble(value));

			else if (type.equals(Double.class) || type.equals(double.class))
				field.set(entity, Double.parseDouble(value));

			else if (type.equals(BigDecimal.class))
				field.set(entity, new BigDecimal(value));

			else if (type.equals(Date.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getDateCellValue());
				else {
					/*
					 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); field.set(entity,
					 * sdf.parse(value));
					 */

					String[] formats = { "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy" };

					Date parseDate = null;

					for (String format : formats) {
						try {
							parseDate = new SimpleDateFormat(format).parse(value);
							break;
						} catch (Exception e) {

						}
					}

					field.set(entity, parseDate);
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

			// logger.info("Field : {} Successfully set with value : {}", fieldName, value);

		} catch (Exception e) {

			
			  logger.
			  error("Error while setting field : '{}' with value : '{}' , type : {} ",
			  fieldName, value, type.getSimpleName(), e);
			 
			throw e;
		}
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private AlddTransaction mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		AlddTransaction entity = new AlddTransaction();

		// logger.info("Mapping field for row : {}", row.getRowNum());

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			Cell cell = row.getCell(entry.getKey());

			if (cell == null) {
				// logger.info("Row : {} , Column index : {} is null, Skipped.",
				// row.getRowNum(), entry.getKey());
				continue;
			}

			String value = formatter.formatCellValue(cell).trim();

			setFieldValue(entry.getValue(), entity, value, cell);

			/*
			 * logger.info("Row : {} , Coumn index : {} --> Field '{}' Value {}.",
			 * row.getRowNum(), entry.getKey(), entry.getValue().getName(), value);
			 */

		}

		return entity;
	}

	public void alddInsertTransaction(List<AlddTransaction> entityList, String uploadId, Date cycleFromDate,
			Date cycleToDate) {

		if (entityList == null || entityList.isEmpty()) {
			// logger.info("No records to insert in ALDD Transaction table");
			return;
		}

		logger.info("Starting delete Aldd Dump table");

		// delete existing record for from date and To date
		jdbcTemplate.update("DELETE FROM TM_VHL_ALDD_TRANS_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("End delete Aldd dump table");
		
		
		
		logger.info("Starting Insert Aldd Dump table");

		String sql = "INSERT INTO TM_VHL_ALDD_TRANS_DUMP ("
				+ "MONTH, APPLICATION_NO, LAN, ID, FILENO, APPLICATION_NO_BK, BRANCH_CODE, PRODUCT,"
				+ " SCHEME, SCHEME_ID, SOL_ID, CUST_FNAME, CUST_MNAME, CUST_LNAME, CONSTID, DOB, IND_CORP_FLAG, CURR_ADD1, CURR_ADD2, CURR_ADD3,"
				+ " CITY, STATE, ZIPCODE, PHONE1, PHONE2, MOBILE, ASSET_TYPE, MAKE, MODEL_BK, SUB_MODEL, ASSETCOST, MARGIN_MONEY, AMOUNTFINANCED,"
				+ " TENURE, EMI, IRR, ADVANCE_EMI, INSTALMENT_START_DATE, INSTL_TYPE, FREQ, SUPPLIER, BROKER, REP_MODE, ACCT_NUMBER, BANK_ACC_NO,"
				+ " ECS_MICR, INDUSTRYDESC, PROMOTION_DESC, ASSET_CATAGORY, CHANNELCODE, EMPLOYEE_NAME, DME, MKTG_OFFICER, FIRST_SOURCE, FINAL_SOURCE,"
				+ " UN_FIRST_SOURCE, UN_FINAL_SOURCE, CONNECTOR_NAME, LAN_BK, RELIGION, PROFESSION, SC_ST_FLAG, SEX, MARITAL_STATUS, QUALIFICATION,"
				+ " ADDRESSTYPE, EMAIL_COMMUNICATION, CALL_COMMUNICATION, PPI_AMT, APPLICATION_DATE, UPLOAD_DISBURSAL_DATE, LOANTYPE, EFFRATE, CHARGE_CODE1,"
				+ " CHARGE_AMOUNT1, CHARGE_CODE2, CHARGE_AMOUNT2, ANNUALISEDAPR, PSL_FLAG, PSL_CODE, SME_CODE, HNICODE, OWNED_IRRIGATED, OWNED_NON_IRRIGATED,"
				+ " LEASED_IN_IRRIGATED, LEASED_IN_NON_IRRIGATED, LEASED_OUT_IRRIGATED, LEASED_OUT_NON_IRRIGATED, DISBURSAL_TO, CROSSCOLL_WITH, UMRN_NUMBER,"
				+ " NPCI_MANDATE_UPLD_DATE, REGISTRATION_STATUS, NACH_EFFECTIVE_DATE, REGISTRATION_AMOUNT, UMRN_ACCEPTANCE_DATE, REJECT_CODE, FATHER_NAME,"
				+ " SPOUSE_NAME, INDUSTRY, LOAN_PURPOSE, MARGIN_MONEY_CODE, DMA_CODE, IS_RBI_DECLARATION_SELECTED, ECODE, REFERRAL_CODE, CREATED_BY,"
				+ " CREATED_DATE, ECS_ACCNO, FROM_CYCLE_DATE, TO_CYCLE_DATE, REMARKS, CONSTITUTION, UPLOAD_ID, FILE_NAME "
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
				+ "LOG ERRORS INTO TM_VHL_ALDD_TRANS_DUMP_ERROR ('" + uploadId + "') " + "REJECT LIMIT UNLIMITED";

		int batchSize = 500;

		long start = System.currentTimeMillis();

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, AlddTransaction entity) -> {

			// 1 MONTH
			ps.setString(1, entity.getMonth());

			// 2 APPLICATION NUMBER
			ps.setString(2, entity.getApplicationNo());

			// 3 LAN
			ps.setString(3, entity.getLan());

			// 4 ID
			if(entity.getId() != null) {
				ps.setLong(4, entity.getId());
			}else {
				ps.setNull(4, Types.BIGINT);
			}
			

			// 5 FILE NO
			ps.setString(5, entity.getFileNo());

			// 6 APPLICATION_NO_BK
			ps.setString(6, entity.getApplicationNoBk());

			// 7 BRANCH CODE
			ps.setString(7, entity.getBranchCode());

			// 8 PRODUCT
			ps.setString(8, entity.getProduct());

			// 9 SCHEME
			ps.setString(9, entity.getScheme());

			// 10 SCHEME_ID
			if(entity.getSchemeId() != null) {
				ps.setLong(10, entity.getSchemeId());
			}else {
				ps.setNull(10, Types.BIGINT);
			}
			

			// 11 SOLE_ID
			if(entity.getSolId() != null) {
				ps.setLong(11, entity.getSolId());
			}else {
				ps.setNull(11, Types.BIGINT);
			}
			

			// 12 CUST_FNAME
			ps.setString(12, entity.getCustFname());

			// 13 CUST_MNAME
			ps.setString(13, entity.getCustMname());

			// 14 CUST_LNAME
			ps.setString(14, entity.getCustLname());

			// 15 CONSTID
			if(entity.getConstitution() != null) {
				ps.setLong(15, entity.getConstitution());
			}else {
				ps.setNull(15, Types.BIGINT);
			}
			

			// 16 MODIFIED_BY
			// ps.setString(15, entity.getModifiedBy());

			// 16 DOB
			// ps.setLong(16,entity.getDob());
			if (entity.getDob() != null) {
				// ps.setDate(16, (java.sql.Date) entity.getDob());
				java.sql.Date sqlDate = new java.sql.Date(entity.getDob().getTime());
				ps.setDate(16, sqlDate);
			} else {
				ps.setNull(16, Types.TIMESTAMP);
			}

			/*
			 * // 17 MODIFIED_DATE if (entity.getModifiedDate() != null) {
			 * ps.setTimestamp(17, new Timestamp(entity.getModifiedDate().getTime())); }
			 * else { ps.setNull(17, Types.TIMESTAMP); }
			 */

			// 17 IND_CORP_FLAG
			ps.setString(17, entity.getIndCorpFlag());

			// 18 CURR_ADD1
			ps.setString(18, entity.getCurrAdd1());

			// 19 CURR_ADD2
			ps.setString(19, entity.getCurrAdd2());

			// 20 CURR_ADD3
			ps.setString(20, entity.getCurrAdd3());

			// 21 CITY
			ps.setString(21, entity.getCity());

			// 22 STATE
			ps.setString(22, entity.getState());

			// 23 ZIPCODE
			if(entity.getZipcode()!= null) {
				ps.setLong(23, entity.getZipcode());
			}else {
				ps.setNull(23, Types.BIGINT);
			}
			

			// 24 PHONE1
			if(entity.getPhone1() != null) {
				ps.setLong(24, entity.getPhone1());
			}else {
				ps.setNull(24, Types.BIGINT);
			}
			

			// 25 PHONE2
			if(entity.getPhone2() != null) {
				ps.setLong(25, entity.getPhone2());
			}else {
				ps.setNull(25, Types.BIGINT);
			}
			

			// 26 MOBILE
			if(entity.getMobile() != null) {
				ps.setLong(26, entity.getMobile());
			}else {
				ps.setNull(26, Types.BIGINT);
			}
			

			// 27 ASSET_TYPE
			ps.setString(27, entity.getAssetType());

			// 28 MAKE
			if(entity.getMake() != null) {
				ps.setLong(28, entity.getMake());
			}else {
				ps.setNull(28, Types.BIGINT);
			}
			

			// 29 MODEL
			if(entity.getModelBk() != null) {
				ps.setLong(29, entity.getModelBk());
			}else {
				ps.setNull(29, Types.BIGINT);
			}
			

			// 30 SUB_MODEL
			if(entity.getSubModel() != null) {
				ps.setLong(30, entity.getSubModel());
			}else {
				ps.setNull(30, Types.BIGINT);
			}
			

			// 31 ASSETCOST
			if(entity.getAssetCost() != null) {
				ps.setLong(31, entity.getAssetCost());
			}else {
				ps.setNull(31, Types.BIGINT);
			}
			

			// 32 MARGIN_MONEY
			if(entity.getMarginMoney() != null) {
				ps.setLong(32, entity.getMarginMoney());
			}else {
				ps.setNull(32, Types.BIGINT);
			}
			

			// 33 AMOUNTFINANCED
			if(entity.getAmountFinanced() != null) {
				ps.setLong(33, entity.getAmountFinanced());
			}else {
				ps.setNull(33, Types.BIGINT);
			}
			

			// 34 TENURE
			if(entity.getTenure() != null) {
				ps.setLong(34, entity.getTenure());
			}else {
				ps.setNull(34, Types.BIGINT);
			}
			

			// 35 EMI
			if(entity.getEmi() != null) {
				ps.setLong(35, entity.getEmi());
			}else {
				ps.setNull(35, Types.BIGINT);
			}
			

			// 36 IRR
			if(entity.getIrr() != null) {
				ps.setLong(36, entity.getIrr());
			}else {
				ps.setNull(36, Types.BIGINT);
			}
			

			// 37 ADVANCE_EMI
			if(entity.getAdvanceEmi() != null) {
				ps.setLong(37, entity.getAdvanceEmi());
			}else {
				ps.setNull(37, Types.BIGINT);
			}
			

			// 38 INSTALMENT_START_DATE
			// ps.setString(38, entity.getInstallmentStartDate());
			if (entity.getInstalmentStartDate() != null) {
				ps.setTimestamp(38, new Timestamp(entity.getInstalmentStartDate().getTime()));
			} else {
				ps.setNull(38, Types.TIMESTAMP);
			}

			// 39 INSTL_TYPE
			ps.setString(39, entity.getInstlType());

			// 40 FREQ
			ps.setString(40, entity.getFreq());

			// 41 SUPPLIER
			if(entity.getSupplier() != null) {
				ps.setLong(41, entity.getSupplier());
			}else {
				ps.setNull(41, Types.BIGINT);
			}
			

			// 42 BROKER
			ps.setString(42, entity.getBroker());

			// 43 REP_MODE
			ps.setString(43, entity.getRepMode());

			// 44 ACCT_NUMBER
			if(entity.getAcctNumber() != null) {
				ps.setLong(44, entity.getAcctNumber());
			}else {
				ps.setNull(44, Types.BIGINT);
			}
			

			// 45 BANK_ACC_NO
			if(entity.getBankAccNo() != null) {
				ps.setLong(45, entity.getBankAccNo());
			}else {
				ps.setNull(45, Types.BIGINT);
			}
			

			// 46 ECS_MICR
			if(entity.getEcsMicr() != null) {
				ps.setLong(46, entity.getEcsMicr());
			}else {
				ps.setNull(46, Types.BIGINT);
			}
			

			// 47 INDUSTRYDESC
			ps.setString(47, entity.getIndustryDesc());

			// 48 PROMOTION_DESC
			ps.setString(48, entity.getPromotionDesc());

			// 49 ASSET_CATAGORY
			ps.setString(49, entity.getAssetCategory());

			// 50 CHANNELCODE
			ps.setString(50, entity.getChannelCode());

			// 51 EMPLOYEE_NAME
			ps.setString(51, entity.getEmployeeName());

			// 52 DME
			ps.setString(52, entity.getDme());

			// 53 MKTG_OFFICER
			ps.setString(53, entity.getMktgOfficer());

			// 54 FIRST_SOURCE
			ps.setString(54, entity.getFirstSource());

			// 55 FINAL_SOURCE
			ps.setString(55, entity.getFinalSource());

			// 56 UN_FIRST_SOURCE
			ps.setString(56, entity.getFirstSource());

			// 57 UN_FINAL_SOURCE
			ps.setString(57, entity.getFinalSource());

			// 58 CONNECTOR_NAME
			ps.setString(58, entity.getConnectorName());

			// 59 LAN
			ps.setString(59, entity.getLan());

			// 60 RELIGION
			ps.setString(60, entity.getReligion());

			// 61 PROFESSION
			ps.setString(61, entity.getProfession());

			// 62 SC_ST_FLAG
			ps.setString(62, entity.getScStFlag());

			// 63 SEX
			ps.setString(63, entity.getSex());

			// 64 MARITAL_STATUS
			ps.setString(64, entity.getMaritalStatus());

			// 65 QUALIFICATION
			ps.setString(65, entity.getQualification());

			// 66 ADDRESSTYPE
			ps.setString(66, entity.getAddressType());

			// 67 EMAIL_COMMUNICATION
			ps.setString(67, entity.getEmailCommunication());

			// 68 CALL_COMMUNICATION
			ps.setString(68, entity.getCallCommunication());

			// 69 PPI_AMT
			ps.setString(69, entity.getPpiAmt());

			// 70 APPLICATION_DATE
			// ps.setString(71, entity.getApplicationDate());
			if (entity.getApplicationDate() != null) {
				ps.setTimestamp(70, new Timestamp(entity.getApplicationDate().getTime()));
			} else {
				ps.setNull(70, Types.TIMESTAMP);
			}

			// 71 UPLOAD_DISBURSAL_DATE
			// ps.setString(72, entity.getUploadDisbursalDate());
			if (entity.getUploadDisbursalDate() != null) {
				ps.setTimestamp(71, new Timestamp(entity.getUploadDisbursalDate().getTime()));
			} else {
				ps.setNull(71, Types.TIMESTAMP);
			}

			// 72 LOANTYPE
			ps.setString(72, entity.getLoanType());

			// 73 EFFRATE
			if(entity.getEffRate() != null) {
				ps.setLong(73, entity.getEffRate());
			}else {
				ps.setNull(73, Types.BIGINT);
			}
			

			// 74 CHARGE_CODE1
			if(entity.getChargeCode1() != null) {
				ps.setLong(74, entity.getChargeCode1());
			}else {
				ps.setNull(74, Types.BIGINT);
			}
			

			// 75 CHARGE_AMOUNT1
			if(entity.getChargeAmount1() != null) {
				ps.setLong(75, entity.getChargeAmount1());
			}else {
				ps.setNull(75, Types.BIGINT);
			}
			

			// 76 CHARGE_CODE2
			if(entity.getChargeCode2() != null) {
				ps.setLong(76, entity.getChargeCode2());
			}else {
				ps.setNull(76, Types.BIGINT);
			}
			

			// 77 CHARGE_AMOUNT2
			if(entity.getChargeAmount2() != null) {
				ps.setLong(77, entity.getChargeAmount2());
			}else {
				ps.setNull(77, Types.BIGINT);
			}

			// 78 AnnualisedAPR
			if(entity.getAnnualisedApr() != null) {
				ps.setLong(78, entity.getAnnualisedApr());
			}else {
				ps.setNull(78, Types.BIGINT);
			}


			// 79 PSL_FLAG
			ps.setString(79, entity.getPslFlag());

			// 80 PSL_CODE
			ps.setString(80, entity.getPslCode());

			// 81 SME_CODE
			ps.setString(81, entity.getSmeCode());

			// 82 HNICODE
			ps.setString(82, entity.getHniCode());

			// 83 OWNED_IRRIGATED
			if(entity.getOwnedIrrigated() != null) {
				ps.setLong(83, entity.getOwnedIrrigated());
			}else {
				ps.setNull(83, Types.BIGINT);
			}
			

			// 84 OWNED_NON_IRRIGATED
			if(entity.getOwnedNonIrrigated() != null) {
				ps.setLong(84, entity.getOwnedNonIrrigated());
			}else {
				ps.setNull(84, Types.BIGINT);
			}
			

			// 85 LEASED_IN_IRRIGATED
			if(entity.getLeasedInIrrigated() != null) {
				ps.setLong(85, entity.getLeasedInIrrigated());
			}else {
				ps.setNull(85, Types.BIGINT);
			}
			

			// 86 LEASED_IN_NON_IRRIGATED
			if(entity.getLeasedOutNonIrrigated() != null) {
				ps.setLong(86, entity.getLeasedOutNonIrrigated());
			}else {
				ps.setNull(86, Types.BIGINT);
			}
			

			// 87 LEASED_OUT_IRRIGATED
			if(entity.getLeasedOutIrrigated() != null) {
				ps.setLong(87, entity.getLeasedOutIrrigated());
			}else {
				ps.setNull(87, Types.BIGINT);
			}
			

			// 88 LEASED_OUT_NON_IRRIGATED
			if(entity.getLeasedOutNonIrrigated() != null) {
				ps.setLong(88, entity.getLeasedOutNonIrrigated());
			}else {
				ps.setNull(88, Types.BIGINT);
			}
			

			// 89 DISBURSAL_TO
			ps.setString(89, entity.getDisbursalTo());

			// 90 CROSSCOLL_WITH
			ps.setString(90, entity.getCrossCollWith());

			// 91 UMRN_NUMBER
			if(entity.getUmrnNumber() != null) {
				ps.setLong(91, entity.getUmrnNumber());
			}else {
				ps.setNull(91, Types.BIGINT);
			}
			

			// 92 NPCI_MANDATE_UPLD_DATE
			// ps.setLong(93, entity.getNpciMandateUpldDate());
			if (entity.getNpciMandateUpldDate() != null) {
				ps.setTimestamp(92, new Timestamp(entity.getNpciMandateUpldDate().getTime()));
			} else {
				ps.setNull(92, Types.TIMESTAMP);
			}

			// 93 REGISTRATION_STATUS
			ps.setString(93, entity.getRegistrationStatus());

			// 94 NACH_EFFECTIVE_DATE
			// ps.setLong(95, entity.getNachEffectiveDate());
			if (entity.getNachEffectiveDate() != null) {
				ps.setTimestamp(94, new Timestamp(entity.getNachEffectiveDate().getTime()));
			} else {
				ps.setNull(94, Types.TIMESTAMP);
			}

			// 95 REGISTRATION_AMOUNT
			if(entity.getRegistrationAmount() != null) {
				ps.setLong(95, entity.getRegistrationAmount());
			}else {
				ps.setNull(95, Types.BIGINT);
			}
			

			// 96 UMRN_ACCEPTANCE_DATE
			// ps.setString(97, entity.getUmrnAcceptanceDate());
			if (entity.getUmrnAcceptanceDate() != null) {
				ps.setTimestamp(96, new Timestamp(entity.getUmrnAcceptanceDate().getTime()));
			} else {
				ps.setNull(96, Types.TIMESTAMP);
			}

			// 97 REJECT_CODE
			ps.setString(97, entity.getRejectCode());

			// 98 FATHER_NAME
			ps.setString(98, entity.getFatherName());

			// 99 SPOUSE_NAME
			ps.setString(99, entity.getSpouseName());

			// 100 INDUSTRY
			ps.setString(100, entity.getIndustry());

			// 101 LOAN_PURPOSE
			ps.setString(101, entity.getLoanPurpose());

			// 102 MARGIN_MONEY_CODE
			ps.setString(102, entity.getMarginMoneyCode());

			// 103 DMA_CODE
			if(entity.getDmaCode() != null) {
				ps.setLong(103, entity.getDmaCode());
			}else {
				ps.setNull(103, Types.BIGINT);
			}
			

			// 104 is_rbi_declaration_selected
			// ps.setLong(104, entity.getIsRbiDeclarationSelected()? 1:0);
			ps.setLong(104, Boolean.TRUE.equals(entity.getIsRbiDeclarationSelected()) ? 1 : 0);

			// 105 Ecode
			if(entity.getEcode() != null) {
				ps.setLong(105, entity.getEcode());
			}else {
				ps.setNull(105, Types.BIGINT);
			}
			

			// 106 Referral code
			ps.setString(106, entity.getReferralCode());

			// 107 CREATED_BY
			ps.setString(107, entity.getCreatedBy());

			// 108 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(108, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(108, Types.TIMESTAMP);
			}

			// 109 ECS_ACCTNO
			if(entity.getEcsAccNo() != null) {
				ps.setLong(109, entity.getEcsAccNo());
			}else {
				ps.setNull(109, Types.BIGINT);
			}
			

			// 110 FROM_CYCLE_DATE
			ps.setDate(110, new java.sql.Date(entity.getFromCycleDate().getTime()));

			// 111 TO_CYCLE_DATE
			ps.setDate(111, new java.sql.Date(entity.getToCycleDate().getTime()));

			// 112 REMARKS
			ps.setString(112, entity.getRemarks());

			// 113 CONSTITUTION
			if(entity.getConstitution() != null) {
				ps.setLong(113, entity.getConstitution());
			}else {
				ps.setNull(113, Types.BIGINT);
			}
			

			// 114 CONSTITUTION
			ps.setString(114, entity.getUploadId());

			// 115 CONSTITUTION
			ps.setString(115, entity.getFileName());
		});

		logger.info("Aldd Dump batch insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// MAP ROW TO Error ENTITY (Using Reflection)
	// ============================================================

	private AlddReportError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		AlddReportError errorEntity = new AlddReportError();
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
	private void process(List<AlddTransaction> validEntityList, List<AlddReportError> invalidIntityList,
			Date cycleFromDate, Date cycleToDate, String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into main
			alddInsertTransaction(validEntityList, uploadId, cycleFromDate, cycleToDate);

			logger.info("batch insert Error");
			// batch Error insert
			AlddReportError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ALDD Trans  Dump File upload Process Failed");
		}
	}

	@Transactional
	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_ALDD_TRANS_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_ALDD_TRANS_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}

}
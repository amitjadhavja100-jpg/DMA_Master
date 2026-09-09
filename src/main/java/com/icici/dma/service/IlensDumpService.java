package com.icici.dma.service;

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
import com.icici.dma.model.IlensDump;
import com.icici.dma.model.IlensDumpError;
import com.icici.dma.repository.IlensDumpErrorRepository;
import com.icici.dma.repository.IlensDumpRepository;

import lombok.RequiredArgsConstructor;

@Service
public class IlensDumpService {

	@Autowired
	private IlensDumpRepository repository;

	private static final Logger logger = LogManager.getLogger(IlensDumpService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IlensDumpErrorRepository ilensErrorRepository;

	private static final String PkField = "applicationNumber";

	// Excel header → Entity field mapping
	private static final Map<String, String> HEADER_FIELD_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	static {
		HEADER_FIELD_MAP.put("S.NO.", "sNo");
		HEADER_FIELD_MAP.put("APPLICATION NUMBER", "applicationNumber");
		HEADER_FIELD_MAP.put("ILENS ID", "ilensId");
		HEADER_FIELD_MAP.put("APPLICANT NAME", "applicantName");
		HEADER_FIELD_MAP.put("APPLICATION LOGIN DATE", "applicationLoginDate");
		HEADER_FIELD_MAP.put("DOCKET LOGIN DATE", "docketLoginDate");
		HEADER_FIELD_MAP.put("CRM ID", "crmId");
		HEADER_FIELD_MAP.put("SOURCING CPC", "sourcingCpc");
		HEADER_FIELD_MAP.put("PROCESSING CPC", "processingCpc");
		HEADER_FIELD_MAP.put("CITY", "city");
		HEADER_FIELD_MAP.put("DISBURSEMENT SEQ", "disbursementSeq");
		HEADER_FIELD_MAP.put("CHANNEL TYPE", "channelType");
		HEADER_FIELD_MAP.put("SOL ID", "solId");
		HEADER_FIELD_MAP.put("CHANNEL NAME & ID", "channelNameId");
		HEADER_FIELD_MAP.put("BROKER ID", "brokerId");
		HEADER_FIELD_MAP.put("SUPPLIER ID", "supplierId");
		HEADER_FIELD_MAP.put("SALES EXECUTIVE NAME & ID", "salesExecutiveNameId");
		HEADER_FIELD_MAP.put("CHANNEL/DEALER SALES EXECUTIVE", "channelDealerSalesExecutive");
		HEADER_FIELD_MAP.put("PROFILE", "profile");
		HEADER_FIELD_MAP.put("PRODUCT", "product");
		HEADER_FIELD_MAP.put("DEALER NAME", "dealerName");
		HEADER_FIELD_MAP.put("SCHEME", "scheme");
		HEADER_FIELD_MAP.put("VARIANT", "variant");
		HEADER_FIELD_MAP.put("SUBVARIANT", "subVariant");
		HEADER_FIELD_MAP.put("STAGE", "stage");
		HEADER_FIELD_MAP.put("STATUS", "status");
		HEADER_FIELD_MAP.put("CUSTOMER SEGMENT", "customerSegment");
		HEADER_FIELD_MAP.put("BT FLAG", "btFlag");
		HEADER_FIELD_MAP.put("BT TYPE", "btType");
		HEADER_FIELD_MAP.put("PSL FLAG", "pslFlag");
		HEADER_FIELD_MAP.put("RI/NRI", "riNri");
		HEADER_FIELD_MAP.put("LOAN AMOUNT", "loanAmount");
		HEADER_FIELD_MAP.put("RC NUMBER", "rcNumber");
		HEADER_FIELD_MAP.put("SANCTION DATE", "sanctionDate");
		HEADER_FIELD_MAP.put("DISBURSEMENT TYPE", "disbursementType");
		HEADER_FIELD_MAP.put("DISBURSEMENT DATE", "disbursementDate");
		HEADER_FIELD_MAP.put("PROCESSING FEE", "processingFee");
		HEADER_FIELD_MAP.put("DISBURSEMENT AMOUNT", "disbursementAmount");
		HEADER_FIELD_MAP.put("MODE OF DISBURSMENT", "modeOfDisbursement");
		HEADER_FIELD_MAP.put("COLLATERAL STATUS", "collateralStatus");
		HEADER_FIELD_MAP.put("ROI", "roi");
		HEADER_FIELD_MAP.put("TENOR", "tenor");
		HEADER_FIELD_MAP.put("MTD SPILLOVER", "mtdSpillover");
		HEADER_FIELD_MAP.put("CRO NAME & ID", "croNameId");
		HEADER_FIELD_MAP.put("MAPPED BSM NAME & ID", "mappedBsmNameId");
		HEADER_FIELD_MAP.put("MAPPED BCM NAME & ID", "mappedBcmNameId");
		HEADER_FIELD_MAP.put("DISBURSEMENT INTENDED DATE", "disbursementIntendedDate");
		HEADER_FIELD_MAP.put("CHEQUE HANDOVER DATE", "chequeHandoverDate");
		HEADER_FIELD_MAP.put("SUBSEQUENT FLAG", "subsequentFlag");
		HEADER_FIELD_MAP.put("SUBSEQUENT RECEIVED REQUEST CHANNEL", "subsequentReceivedChannel");
		HEADER_FIELD_MAP.put("FCPG STATUS", "fcpgStatus");
		HEADER_FIELD_MAP.put("FCPG RECOMMENDATION", "fcpgRecommendation");
		HEADER_FIELD_MAP.put("HUNTER STATUS", "hunterStatus");
		HEADER_FIELD_MAP.put("VALUATION AMOUNT", "valuationAmount");
		HEADER_FIELD_MAP.put("LAN", "lan");
		HEADER_FIELD_MAP.put("AGREEMENT ID", "agreementId");
		HEADER_FIELD_MAP.put("LTV", "ltv");
		HEADER_FIELD_MAP.put("QUERY CLASSIFICATION", "queryClassification");
		HEADER_FIELD_MAP.put("PROPERTY TYPE", "propertyType");
		HEADER_FIELD_MAP.put("LAST STAGE", "lastStage");
		HEADER_FIELD_MAP.put("LAST STATUS", "lastStatus");
		HEADER_FIELD_MAP.put("TYPE OF FACILITY", "typeOfFacility");
		HEADER_FIELD_MAP.put("LAST STAGE/STATUS TIMESTAMP", "lastStageStatusTimestamp");
		HEADER_FIELD_MAP.put("CHEQUE HANDOVER FLAG", "chequeHandoverFlag");
		HEADER_FIELD_MAP.put("HIGHEST BUREAU SCORE", "highestBureauScore");
		HEADER_FIELD_MAP.put("PROCESS TYPE", "processType");
		HEADER_FIELD_MAP.put("STP REMARKS", "stpRemarks");
		HEADER_FIELD_MAP.put("ADMISSION STATUS", "admissionStatus");
		HEADER_FIELD_MAP.put("MORATORIUM STATUS", "moratoriumStatus");
		HEADER_FIELD_MAP.put("MORATORIUM TYPE", "moratoriumType");
		HEADER_FIELD_MAP.put("ZONE", "zone");
		HEADER_FIELD_MAP.put("STATE", "state");
		HEADER_FIELD_MAP.put("TRANCHE NUMBER", "trancheNumber");
		HEADER_FIELD_MAP.put("MODE OF REPAYMENT", "modeOfRepayment");
		HEADER_FIELD_MAP.put("NEO APPLICATION NUMBER", "neoApplicationNumber");
		HEADER_FIELD_MAP.put("BENEFICIARY TYPE", "beneficiaryType");
		HEADER_FIELD_MAP.put("TYPE OF REMITTANCE", "typeOfRemittance");
		HEADER_FIELD_MAP.put("REASON FOR DISBURSEMENT", "reasonForDisbursement");
		HEADER_FIELD_MAP.put("MORATORIUM IN MONTHS", "moratoriumInMonths");
		HEADER_FIELD_MAP.put("MODE OF KYC", "modeOfKyc");
		HEADER_FIELD_MAP.put("FINAL CREDIT DECISION", "finalCreditDecision");
		HEADER_FIELD_MAP.put("FUND TRANSFER METHOD", "fundTransferMethod");
		HEADER_FIELD_MAP.put("DISBURSEMENT STAGE", "disbursementStage");
		HEADER_FIELD_MAP.put("RHS USER NAME & ID", "rhsUserNameId");

		for (Field field : IlensDump.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : IlensDumpError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	/* ================= UPLOAD ================= */
	@Transactional
	public String uploadExcel(MultipartFile file, String user, Date fromDate, Date toDate) {

		long startTime = System.currentTimeMillis();

		if (file == null || file.isEmpty()) {
			throw new RuntimeException("File is empty");
		}

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			int sheetCount = workbook.getNumberOfSheets();

			if (sheetCount != 1) {

				logger.info("Excel file should contain only one sheet");
				return "Excel file should contain only one sheet";
			}

			String sheetName = sheet.getSheetName();

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
//			logger.info("PK rowsMap: {}", pkRowMap);
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

//						Set<Integer> rowsToSkip = new HashSet<>(errorReasonMap.keySet());
			Set<Integer> rowsToSkip = errorReasonMap.keySet();

			logger.info("Invalid row count : {}", invalidRows.size());
			logger.info("Excel duplicate row count : {}", duplicateExcelRows.size());
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

//					logger.info("Row {} marked Invalid. reason : {}", r, errorReasonMap.get(r));

				} else {

					validRowsList.add(row);

//					logger.info("Row {} marked VALID.", r);
				}
			}

			logger.info("Valid row count : {}", validRowsList.size());
			logger.info("Invalid row count: {}", invalidRowsList.size());

			// ========================================================
			// Upload Id creation
			// ========================================================

			// Generate unique Upload Id
			String uploadId = generateUploadId(file.getOriginalFilename());
			String originalFilename = file.getOriginalFilename();

			// ========================================================
			// PARALLEL VALID ENTITY CREATION
			// ========================================================

			logger.info("======= Valid Entity cration Started ============");

			List<IlensDump> entityList = validRowsList.parallelStream().map(row -> {

				try {
					IlensDump entity = mapRowToEntity(row, columnFieldMap, formatter);

					// setFields(row, columnMap, entity);
					entity.setCreatedDate(new Date());
					entity.setCreatedby(user);
					entity.setCycleToDate(toDate);
					entity.setCycleFromDate(fromDate);
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

			List<IlensDumpError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					IlensDumpError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

					entity.setCreatedby(user);
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

			logger.info("========== Error Entity Creation Completed ================");

			logger.info("Total Entities prepared: {}", entityList.size());
			logger.info("Total Error Entities prepared: {}", errorEntityList.size());

			process(entityList, errorEntityList, fromDate, toDate, uploadId);

			Map<String, Integer> counts = getCounts(uploadId, originalFilename);

			String message = "Upload Completed Successfully " + "\n" + "Total Records in file : "
					+ (entityList.size() + rowsToSkip.size()) + "\n" + "Count of added records : "
					+ counts.get("insertCount") + "\n" + "Count of error records : " + counts.get("errorCount");

			logger.info(message);

			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));

			logger.info("========== ILENS UPLOAD COMPLETED ==========");

//			return  "Upload Completed Successfully";
			return message;
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Upload Failed : " + e.getMessage());
		}
	}

	private void IlensDumpError(List<IlensDumpError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for Ilens ERROR table. Total records: {}", errorList.size());

		int batchSize = 1500;

		// SQL without ID column because it is auto-generated
		String sql = "INSERT INTO TM_VHL_ILENS_DUMP_ERROR (ERROR_ID, "
				+ "S_NO, APPLICATION_NUMBER, ILENS_ID, APPLICANT_NAME, APPLICATION_LOGIN_DATE, DOCKET_LOGIN_DATE, CRM_ID, SOURCING_CPC, PROCESSING_CPC, CITY, "
				+ "DISBURSEMENT_SEQ, CHANNEL_TYPE, SOL_ID, CHANNEL_NAME_ID, BROKER_ID, SUPPLIER_ID, SALES_EXECUTIVE_NAME_ID, CHANNEL_DEALER_SALES_EXEC, PROFILE, PRODUCT, "
				+ "DEALER_NAME, SCHEME, VARIANT, SUB_VARIANT, STAGE, STATUS, CUSTOMER_SEGMENT, BT_FLAG, BT_TYPE, PSL_FLAG, "
				+ "RI_NRI, LOAN_AMOUNT, RC_NUMBER, SANCTION_DATE, DISBURSEMENT_TYPE, DISBURSEMENT_DATE, PROCESSING_FEE, DISBURSEMENT_AMOUNT, MODE_OF_DISBURSEMENT, COLLATERAL_STATUS, ROI, "
				+ "TENOR, MTD_SPILLOVER, CRO_NAME_ID, MAPPED_BSM_NAME_ID, MAPPED_BCM_NAME_ID, DISBURSEMENT_INTENDED_DATE, CHEQUE_HANDOVER_DATE, SUBSEQUENT_FLAG, SUBSEQUENT_RECEIVED_CHANNEL, "
				+ "FCPG_STATUS, FCPG_RECOMMENDATION, HUNTER_STATUS, VALUATION_AMOUNT, LAN, AGREEMENT_ID, LTV, QUERY_CLASSIFICATION, PROPERTY_TYPE, LAST_STAGE, "
				+ "LAST_STATUS, TYPE_OF_FACILITY, LAST_STAGE_STATUS_TIMESTAMP, CHEQUE_HANDOVER_FLAG, HIGHEST_BUREAU_SCORE, PROCESS_TYPE, STP_REMARKS, ADMISSION_STATUS, MORATORIUM_STATUS, MORATORIUM_TYPE, ZONE, "
				+ "STATE, TRANCHE_NUMBER, MODE_OF_REPAYMENT, NEO_APPLICATION_NUMBER, BENEFICIARY_TYPE, TYPE_OF_REMITTANCE, REASON_FOR_DISBURSEMENT, MORATORIUM_IN_MONTHS, MODE_OF_KYC, "
				+ "FINAL_CREDIT_DECISION, FUND_TRANSFER_METHOD, DISBURSEMENT_STAGE, RHS_USER_NAME_ID, CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, UPLOAD_ID , FILE_NAME,  ERROR_MSG, ROW_NUMBER "
				+ ") VALUES (ILENS_ERROR_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, IlensDumpError entity) -> {

			// 1 S_NO
			ps.setString(1, entity.getsNo());

			// 2 APPLICATION NUMBER
			ps.setString(2, entity.getApplicationNumber());

			// 3 ILENS_ID
			ps.setString(3, entity.getIlensId());

			// 4 APPLICANT_NAME
			ps.setString(4, entity.getApplicantName());

			// 5 APPLICATION_LOGIN_DATE
			ps.setString(5, entity.getApplicationLoginDate());

			// 6 DOCKET_LOGIN_DATE
			ps.setString(6, entity.getDocketLoginDate());

			// 7 CRM_ID
			ps.setString(7, entity.getCrmId());

			// 8 SOURCING_CPC
			ps.setString(8, entity.getSourcingCpc());

			// 9 PROCESSING_CPC
			ps.setString(9, entity.getProcessingCpc());

			// 10 CITY
			ps.setString(10, entity.getCity());

			// 11 DISBURSEMENT_SEQ
			ps.setString(11, entity.getDisbursementSeq());

			// 12 CHANNEL_TYPE
			ps.setString(12, entity.getChannelType());

			// 13 SOL_ID
			ps.setString(13, entity.getSolId());

			// 14 CHANNEL_NAME_ID
			ps.setString(14, entity.getChannelNameId());

			// 15 BROKER_ID
			ps.setString(15, entity.getBrokerId());

			// 16 SUPPLIER_ID
			ps.setString(16, entity.getSupplierId());

			// 17 SALES_EXECUTIVE_NAME_ID
			ps.setString(17, entity.getSalesExecutiveNameId());

			// 18 CHANNEL_DEALER_SALES_EXECUTIVE
			ps.setString(18, entity.getChannelDealerSalesExecutive());

			// 19 PROFILE
			ps.setString(19, entity.getProfile());

			// 20 PRODUCT
			ps.setString(20, entity.getProduct());

			// 21 DEALER_NAME
			ps.setString(21, entity.getDealerName());

			// 22 SCHEME
			ps.setString(22, entity.getScheme());

			// 23 VARIANT
			ps.setString(23, entity.getVariant());

			// 24 SUB_VARIANT
			ps.setString(24, entity.getSubVariant());

			// 25 STAGE
			ps.setString(25, entity.getStage());

			// 26 STATUS
			ps.setString(26, entity.getStatus());

			// 27 CUSTOMER_SEGMENT
			ps.setString(27, entity.getCustomerSegment());

			// 28 BT_FLAG
			ps.setString(28, entity.getBtFlag());

			// 29 BT_TYPE
			ps.setString(29, entity.getBtType());

			// 30 PSL_FLAG
			ps.setString(30, entity.getPslFlag());

			// 31 RIN
			ps.setString(31, entity.getRiNri());

			// 32 LOAN_AMOUNT
			ps.setString(32, entity.getLoanAmount());

			// 33 RC_NUMBER
			ps.setString(33, entity.getRcNumber());

			// 34 SANCTION_DATE
			ps.setString(34, entity.getSanctionDate());

			// 35 DISBURSEMENT_TYPE
			ps.setString(35, entity.getDisbursementType());

			// 36 DISBURSEMENT_DATE
			ps.setString(36, entity.getDisbursementDate());

			// 37 PROCESSING_FEE
			ps.setString(37, entity.getProcessingFee());

			// 38 DISBURSEMENT_AMOUNT
			ps.setString(38, entity.getDisbursementAmount());

			// 39 MODE_OF_DISBURSEMENT
			ps.setString(39, entity.getModeOfDisbursement());

			// 40 COLLATERAL_STATUS
			ps.setString(40, entity.getCollateralStatus());

			// 41 ROI
			ps.setString(41, entity.getRoi());

			// 42 TENOR
			ps.setString(42, entity.getTenor());

			// 43 MTD_SPILLOVER
			ps.setString(43, entity.getMtdSpillover());

			// 44 CRO_NAME_ID
			ps.setString(44, entity.getCroNameId());

			// 45 MAPPED_BSM_NAME_ID
			ps.setString(45, entity.getMappedBsmNameId());

			// 46 MAPPED_BCM_NAME_ID
			ps.setString(46, entity.getMappedBcmNameId());

			// 47 DISBURSEMENT_INTENDED_DATE
			ps.setString(47, entity.getDisbursementIntendedDate());

			// 48 CHEQUE_HANDOVER_DATE
			ps.setString(48, entity.getChequeHandoverDate());

			// 49 SUBSEQUENT_FLAG
			ps.setString(49, entity.getSubsequentFlag());

			// 50 SUBSEQUENT_RECEIVED_CHANNEL
			ps.setString(50, entity.getSubsequentReceivedChannel());

			// 51 FCPG_STATUS
			ps.setString(51, entity.getFcpgStatus());

			// 52 FCPG_RECOMMENDATION
			ps.setString(52, entity.getFcpgRecommendation());

			// 53 HUNTER_STATUS
			ps.setString(53, entity.getHunterStatus());

			// 54 VALUATION_AMOUNT
			ps.setString(54, entity.getValuationAmount());

			// 55 LAN
			ps.setString(55, entity.getLan());

			// 56 AGREEMENT_ID
			ps.setString(56, entity.getAgreementId());

			// 57 LTV
			ps.setString(57, entity.getLtv());

			// 58 QUERY_CLASSIFICATION
			ps.setString(58, entity.getQueryClassification());

			// 59 PROPERTY_TYPE
			ps.setString(59, entity.getPropertyType());

			// 60 LAST_STAGE
			ps.setString(60, entity.getLastStage());

			// 61 LAST_STATUS
			ps.setString(61, entity.getLastStatus());

			// 62 TYPE_OF_FACILITY
			ps.setString(62, entity.getTypeOfFacility());

			// 63 LAST_STAGE_STATUS_TS
			ps.setString(63, entity.getLastStageStatusTimestamp());

			// 64 CHEQUE_HANDOVER_FLAG
			ps.setString(64, entity.getChequeHandoverFlag());

			// 65 HIGHEST_BUREAU_SCORE
			ps.setString(65, entity.getHighestBureauScore());

			// 66 PROCESS_TYPE
			ps.setString(66, entity.getProcessType());

			// 67 STP_REMARKS
			ps.setString(67, entity.getStpRemarks());

			// 68 ADMISSION_STATUS
			ps.setString(68, entity.getAdmissionStatus());

			// 69 MORATORIUM_STATUS
			ps.setString(69, entity.getMoratoriumStatus());

			// 70 MORATORIUM_TYPE
			ps.setString(70, entity.getMoratoriumType());

			// 71 ZONE
			ps.setString(71, entity.getZone());

			// 72 STATE
			ps.setString(72, entity.getState());

			// 73 TRANCHE_NUMBER
			ps.setString(73, entity.getTrancheNumber());

			// 74 MODE_OF_REPAYMENT
			ps.setString(74, entity.getModeOfRepayment());

			// 75 NEO_APPLICATION_NUMBER
			ps.setString(75, entity.getNeoApplicationNumber());

			// 76 BENEFICIARY_TYPE
			ps.setString(76, entity.getBeneficiaryType());

			// 77 TYPE_OF_REMITTANCE
			ps.setString(77, entity.getTypeOfRemittance());

			// 78 REASON_FOR_DISBURSEMENT
			ps.setString(78, entity.getReasonForDisbursement());

			// 79 MORATORIUM_IN_MONTHS
			ps.setString(79, entity.getMoratoriumInMonths());

			// 80 MODE_OF_KYC
			ps.setString(80, entity.getModeOfKyc());

			// 81 FINAL_CREDIT_DECISION
			ps.setString(81, entity.getFinalCreditDecision());

			// 82 FUND_TRANSFER_METHOD
			ps.setString(82, entity.getFundTransferMethod());

			// 83 DISBURSEMENT_STAGE
			ps.setString(83, entity.getDisbursementStage());

			// 84 RHS_USER_NAME_ID
			ps.setString(84, entity.getRhsUserNameId());

			// ============================================================================

			// 85 CREATED_BY
			ps.setString(85, entity.getCreatedby());

			// 86 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setDate(86, new java.sql.Date(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(86, Types.TIMESTAMP);
			}

			// 87 FROM_CYCLE_DATE
			if (entity.getCycleFromDate() != null) {
				ps.setDate(87, new java.sql.Date(entity.getCycleFromDate().getTime()));
			} else {
				ps.setNull(87, java.sql.Types.DATE);
			}

			// 88 TO_CYCLE_DATE
			if (entity.getCycleToDate() != null) {
				ps.setDate(88, new java.sql.Date(entity.getCycleToDate().getTime()));
			} else {
				ps.setNull(88, java.sql.Types.DATE);
			}

			// 89 UPLOAD_ID
			ps.setString(89, entity.getUploadId());

			// 90 FILE_NAME
			ps.setString(90, entity.getFileName());

			// 91 ERROR_MSG
			ps.setString(91, entity.getErrorMsg());

			// 92 ROW_NUMBER
			if (entity.getRowNumber() != null) {
				ps.setInt(92, entity.getRowNumber());
			} else {
				ps.setNull(92, Types.INTEGER);
			}

		});

	}

	private String normalizeHeader(String header) {
		if (header == null)
			return null;

		logger.info("Normalize Header::" + header.trim().replaceAll("\\s+", " ").toUpperCase());
		// return header.trim().replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+",
		// "_").toUpperCase();
		return header.trim().replaceAll("\\s+", " ").toUpperCase();
	}

	@Transactional
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		try {
			// String uploadId =
			// ilensErrorRepository.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

			String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_ILENS_DUMP_ERROR WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
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

//		List<IlensDumpError> errors = ilensErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * FROM TM_VHL_ILENS_DUMP_ERROR WHERE upload_id = ? OR ora_err_tag$= ?";

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

			Sheet sheet = workbook.createSheet("ILENS_ERROR");

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
			cell0.setCellValue("S.No.");
			cell0.setCellStyle(headerStyle);

			Cell cell1 = header.createCell(1);
			cell1.setCellValue("Application Number");
			cell1.setCellStyle(headerStyle);

			Cell cell2 = header.createCell(2);
			cell2.setCellValue("ILens ID");
			cell2.setCellStyle(headerStyle);

			Cell cell3 = header.createCell(3);
			cell3.setCellValue("Applicant Name");
			cell3.setCellStyle(headerStyle);

			Cell cell4 = header.createCell(4);
			cell4.setCellValue("Application Login Date");
			cell4.setCellStyle(headerStyle);

			Cell cell5 = header.createCell(5);
			cell5.setCellValue("Docket Login Date");
			cell5.setCellStyle(headerStyle);

			Cell cell6 = header.createCell(6);
			cell6.setCellValue("CRM ID");
			cell6.setCellStyle(headerStyle);

			Cell cell7 = header.createCell(7);
			cell7.setCellValue("Sourcing CPC");
			cell7.setCellStyle(headerStyle);

			Cell cell8 = header.createCell(8);
			cell8.setCellValue("Processing CPC");
			cell8.setCellStyle(headerStyle);

			Cell cell9 = header.createCell(9);
			cell9.setCellValue("City");
			cell9.setCellStyle(headerStyle);

			Cell cell10 = header.createCell(10);
			cell10.setCellValue("Disbursement Seq");
			cell10.setCellStyle(headerStyle);

			Cell cell11 = header.createCell(11);
			cell11.setCellValue("Channel Type");
			cell11.setCellStyle(headerStyle);

			Cell cell12 = header.createCell(12);
			cell12.setCellValue("Sol ID");
			cell12.setCellStyle(headerStyle);

			Cell cell13 = header.createCell(13);
			cell13.setCellValue("Channel Name & ID");
			cell13.setCellStyle(headerStyle);

			Cell cell14 = header.createCell(14);
			cell14.setCellValue("Broker ID");
			cell14.setCellStyle(headerStyle);

			Cell cell15 = header.createCell(15);
			cell15.setCellValue("Supplier ID");
			cell15.setCellStyle(headerStyle);

			Cell cell16 = header.createCell(16);
			cell16.setCellValue("Sales executive Name & ID");
			cell16.setCellStyle(headerStyle);

			Cell cell17 = header.createCell(17);
			cell17.setCellValue("Channel/Dealer Sales Executive");
			cell17.setCellStyle(headerStyle);

			Cell cell18 = header.createCell(18);
			cell18.setCellValue("Profile");
			cell18.setCellStyle(headerStyle);

			Cell cell19 = header.createCell(19);
			cell19.setCellValue("Product");
			cell19.setCellStyle(headerStyle);

			Cell cell20 = header.createCell(20);
			cell20.setCellValue("Dealer Name");
			cell20.setCellStyle(headerStyle);

			Cell cell21 = header.createCell(21);
			cell21.setCellValue("Scheme");
			cell21.setCellStyle(headerStyle);

			Cell cell22 = header.createCell(22);
			cell22.setCellValue("Variant");
			cell22.setCellStyle(headerStyle);

			Cell cell23 = header.createCell(23);
			cell23.setCellValue("Subvariant");
			cell23.setCellStyle(headerStyle);

			Cell cell24 = header.createCell(24);
			cell24.setCellValue("Stage");
			cell24.setCellStyle(headerStyle);

			Cell cell25 = header.createCell(25);
			cell25.setCellValue("Status");
			cell25.setCellStyle(headerStyle);

			Cell cell26 = header.createCell(26);
			cell26.setCellValue("Customer Segment");
			cell26.setCellStyle(headerStyle);

			Cell cell27 = header.createCell(27);
			cell27.setCellValue("BT Flag");
			cell27.setCellStyle(headerStyle);

			Cell cell28 = header.createCell(28);
			cell28.setCellValue("BT Type");
			cell28.setCellStyle(headerStyle);

			Cell cell29 = header.createCell(29);
			cell29.setCellValue("PSL Flag");
			cell29.setCellStyle(headerStyle);

			Cell cell30 = header.createCell(30);
			cell30.setCellValue("RI/NRI");
			cell30.setCellStyle(headerStyle);

			Cell cell31 = header.createCell(31);
			cell31.setCellValue("Loan Amount");
			cell31.setCellStyle(headerStyle);

			Cell cell32 = header.createCell(32);
			cell32.setCellValue("RC Number");
			cell32.setCellStyle(headerStyle);

			Cell cell33 = header.createCell(33);
			cell33.setCellValue("Sanction Date");
			cell33.setCellStyle(headerStyle);

			Cell cell34 = header.createCell(34);
			cell34.setCellValue("Disbursement Type");
			cell34.setCellStyle(headerStyle);

			Cell cell35 = header.createCell(35);
			cell35.setCellValue("Disbursement Date");
			cell35.setCellStyle(headerStyle);

			Cell cell36 = header.createCell(36);
			cell36.setCellValue("Processing Fee");
			cell36.setCellStyle(headerStyle);

			Cell cell37 = header.createCell(37);
			cell37.setCellValue("Disbursement amount");
			cell37.setCellStyle(headerStyle);

			Cell cell38 = header.createCell(38);
			cell38.setCellValue("Mode of Disbursment");
			cell38.setCellStyle(headerStyle);

			Cell cell39 = header.createCell(39);
			cell39.setCellValue("Collateral Status");
			cell39.setCellStyle(headerStyle);

			Cell cell40 = header.createCell(40);
			cell40.setCellValue("ROI");
			cell40.setCellStyle(headerStyle);

			Cell cell41 = header.createCell(41);
			cell41.setCellValue("Tenor");
			cell41.setCellStyle(headerStyle);

			Cell cell42 = header.createCell(42);
			cell42.setCellValue("MTD SpillOver");
			cell42.setCellStyle(headerStyle);

			Cell cell43 = header.createCell(43);
			cell43.setCellValue("CRO Name & ID");
			cell43.setCellStyle(headerStyle);

			Cell cell44 = header.createCell(44);
			cell44.setCellValue("Mapped BSM Name & ID");
			cell44.setCellStyle(headerStyle);

			Cell cell45 = header.createCell(45);
			cell45.setCellValue("Mapped BCM Name & ID");
			cell45.setCellStyle(headerStyle);

			Cell cell46 = header.createCell(46);
			cell46.setCellValue("Disbursement Intended Date");
			cell46.setCellStyle(headerStyle);

			Cell cell47 = header.createCell(47);
			cell47.setCellValue("Cheque Handover Date");
			cell47.setCellStyle(headerStyle);

			Cell cell48 = header.createCell(48);
			cell48.setCellValue("Subsequent flag");
			cell48.setCellStyle(headerStyle);

			Cell cell49 = header.createCell(49);
			cell49.setCellValue("Subsequent Received Request Channel");
			cell49.setCellStyle(headerStyle);

			Cell cell50 = header.createCell(50);
			cell50.setCellValue("FCPG Status");
			cell50.setCellStyle(headerStyle);

			Cell cell51 = header.createCell(51);
			cell51.setCellValue("FCPG Recommendation");
			cell51.setCellStyle(headerStyle);

			Cell cell52 = header.createCell(52);
			cell52.setCellValue("Hunter Status");
			cell52.setCellStyle(headerStyle);

			Cell cell53 = header.createCell(53);
			cell53.setCellValue("Valuation Amount");
			cell53.setCellStyle(headerStyle);

			Cell cell54 = header.createCell(54);
			cell54.setCellValue("LAN");
			cell54.setCellStyle(headerStyle);

			Cell cell55 = header.createCell(55);
			cell55.setCellValue("Agreement Id");
			cell55.setCellStyle(headerStyle);

			Cell cell56 = header.createCell(56);
			cell56.setCellValue("LTV");
			cell56.setCellStyle(headerStyle);

			Cell cell57 = header.createCell(57);
			cell57.setCellValue("Query Classification");
			cell57.setCellStyle(headerStyle);

			Cell cell58 = header.createCell(58);
			cell58.setCellValue("Property Type");
			cell58.setCellStyle(headerStyle);

			Cell cell59 = header.createCell(59);
			cell59.setCellValue("Last Stage");
			cell59.setCellStyle(headerStyle);

			Cell cell60 = header.createCell(60);
			cell60.setCellValue("Last Status");
			cell60.setCellStyle(headerStyle);

			Cell cell61 = header.createCell(61);
			cell61.setCellValue("Type of Facility");
			cell61.setCellStyle(headerStyle);

			Cell cell62 = header.createCell(62);
			cell62.setCellValue("Last Stage/Status Timestamp");
			cell62.setCellStyle(headerStyle);

			Cell cell63 = header.createCell(63);
			cell63.setCellValue("Cheque Handover Flag");
			cell63.setCellStyle(headerStyle);

			Cell cell64 = header.createCell(64);
			cell64.setCellValue("Highest Bureau Score");
			cell64.setCellStyle(headerStyle);

			Cell cell65 = header.createCell(65);
			cell65.setCellValue("Process Type");
			cell65.setCellStyle(headerStyle);

			Cell cell66 = header.createCell(66);
			cell66.setCellValue("STP Remarks");
			cell66.setCellStyle(headerStyle);

			Cell cell67 = header.createCell(67);
			cell67.setCellValue("Admission Status");
			cell67.setCellStyle(headerStyle);

			Cell cell68 = header.createCell(68);
			cell68.setCellValue("Moratorium Status");
			cell68.setCellStyle(headerStyle);

			Cell cell69 = header.createCell(69);
			cell69.setCellValue("Moratorium Type");
			cell69.setCellStyle(headerStyle);

			Cell cell70 = header.createCell(70);
			cell70.setCellValue("Zone");
			cell70.setCellStyle(headerStyle);

			Cell cell71 = header.createCell(71);
			cell71.setCellValue("State");
			cell71.setCellStyle(headerStyle);

			Cell cell72 = header.createCell(72);
			cell72.setCellValue("Tranche Number");
			cell72.setCellStyle(headerStyle);

			Cell cell73 = header.createCell(73);
			cell73.setCellValue("Mode of Repayment");
			cell73.setCellStyle(headerStyle);

			Cell cell74 = header.createCell(74);
			cell74.setCellValue("Neo Application Number");
			cell74.setCellStyle(headerStyle);

			Cell cell75 = header.createCell(75);
			cell75.setCellValue("Beneficiary Type");
			cell75.setCellStyle(headerStyle);

			Cell cell76 = header.createCell(76);
			cell76.setCellValue("Type of remittance");
			cell76.setCellStyle(headerStyle);

			Cell cell77 = header.createCell(77);
			cell77.setCellValue("Reason for disbursement");
			cell77.setCellStyle(headerStyle);

			Cell cell78 = header.createCell(78);
			cell78.setCellValue("Moratorium in months");
			cell78.setCellStyle(headerStyle);

			Cell cell79 = header.createCell(79);
			cell79.setCellValue("Mode of KYC");
			cell79.setCellStyle(headerStyle);

			Cell cell80 = header.createCell(80);
			cell80.setCellValue("Final Credit Decision");
			cell80.setCellStyle(headerStyle);

			Cell cell81 = header.createCell(81);
			cell81.setCellValue("Fund Transfer Method");
			cell81.setCellStyle(headerStyle);

			Cell cell82 = header.createCell(82);
			cell82.setCellValue("Disbursement Stage");
			cell82.setCellStyle(headerStyle);

			Cell cell83 = header.createCell(83);
			cell83.setCellValue("RHS User Name & ID");
			cell83.setCellStyle(headerStyle);

			Cell cell84 = header.createCell(84);
			cell84.setCellValue("ERROR REASON");
			cell84.setCellStyle(headerStyle);

			Cell cell85 = header.createCell(85);
			cell85.setCellValue("UPLOAD_DATE");
			cell85.setCellStyle(headerStyle);
			
			Cell cell86 = header.createCell(86);
			cell86.setCellValue("ROW NUMBER");
			cell86.setCellStyle(headerStyle);

			// ---------------------------------------------------
			// Step 4: Write Data Rows
			// ---------------------------------------------------

			int rowIndex = 1;

			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("S_NO")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("APPLICATION_NUMBER")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("ILENS_ID")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("APPLICANT_NAME")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("APPLICATION_LOGIN_DATE")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("DOCKET_LOGIN_DATE")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(error.get("CRM_ID")));
				cell06.setCellStyle(dataStyle);

				Cell cell07 = row.createCell(7);
				cell07.setCellValue(formatValue(error.get("SOURCING_CPC")));
				cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(8);
				cell08.setCellValue(formatValue(error.get("PROCESSING_CPC")));
				cell08.setCellStyle(dataStyle);

				Cell cell09 = row.createCell(9);
				cell09.setCellValue(formatValue(error.get("CITY")));
				cell09.setCellStyle(dataStyle);

				Cell cell010 = row.createCell(10);
				cell010.setCellValue(formatValue(error.get("DISBURSEMENT_SEQ")));
				cell010.setCellStyle(dataStyle);

				Cell cell011 = row.createCell(11);
				cell011.setCellValue(formatValue(error.get("CHANNEL_TYPE")));
				cell011.setCellStyle(dataStyle);

				Cell cell012 = row.createCell(12);
				cell012.setCellValue(formatValue(error.get("SOL_ID")));
				cell012.setCellStyle(dataStyle);

				Cell cell013 = row.createCell(13);
				cell013.setCellValue(formatValue(error.get("CHANNEL_NAME_ID")));
				cell013.setCellStyle(dataStyle);

				Cell cell014 = row.createCell(14);
				cell014.setCellValue(formatValue(error.get("BROKER_ID")));
				cell014.setCellStyle(dataStyle);

				Cell cell015 = row.createCell(15);
				cell015.setCellValue(formatValue(error.get("SUPPLIER_ID")));
				cell015.setCellStyle(dataStyle);

				Cell cell016 = row.createCell(16);
				cell016.setCellValue(formatValue(error.get("SALES_EXECUTIVE_NAME_ID")));
				cell016.setCellStyle(dataStyle);

				Cell cell017 = row.createCell(17);
				cell017.setCellValue(formatValue(error.get("CHANNEL_DEALER_SALES_EXEC")));
				cell017.setCellStyle(dataStyle);

				Cell cell018 = row.createCell(18);
				cell018.setCellValue(formatValue(error.get("PROFILE")));
				cell018.setCellStyle(dataStyle);

				Cell cell019 = row.createCell(19);
				cell019.setCellValue(formatValue(error.get("PRODUCT")));
				cell019.setCellStyle(dataStyle);

				Cell cell020 = row.createCell(20);
				cell020.setCellValue(formatValue(error.get("DEALER_NAME")));
				cell020.setCellStyle(dataStyle);

				Cell cell021 = row.createCell(21);
				cell021.setCellValue(formatValue(error.get("SCHEME")));
				cell021.setCellStyle(dataStyle);

				Cell cell022 = row.createCell(22);
				cell022.setCellValue(formatValue(error.get("VARIANT")));
				cell022.setCellStyle(dataStyle);

				Cell cell023 = row.createCell(23);
				cell023.setCellValue(formatValue(error.get("SUB_VARIANT")));
				cell023.setCellStyle(dataStyle);

				Cell cell024 = row.createCell(24);
				cell024.setCellValue(formatValue(error.get("STAGE")));
				cell024.setCellStyle(dataStyle);

				Cell cell025 = row.createCell(25);
				cell025.setCellValue(formatValue(error.get("STATUS")));
				cell025.setCellStyle(dataStyle);

				Cell cell026 = row.createCell(26);
				cell026.setCellValue(formatValue(error.get("CUSTOMER_SEGMENT")));
				cell026.setCellStyle(dataStyle);

				Cell cell027 = row.createCell(27);
				cell027.setCellValue(formatValue(error.get("BT_FLAG")));
				cell027.setCellStyle(dataStyle);

				Cell cell028 = row.createCell(28);
				cell028.setCellValue(formatValue(error.get("BT_TYPE")));
				cell028.setCellStyle(dataStyle);

				Cell cell029 = row.createCell(29);
				cell029.setCellValue(formatValue(error.get("PSL_FLAG")));
				cell029.setCellStyle(dataStyle);

				Cell cell030 = row.createCell(30);
				cell030.setCellValue(formatValue(error.get("RI_NRI")));
				cell030.setCellStyle(dataStyle);

				Cell cell031 = row.createCell(31);
				cell031.setCellValue(formatValue(error.get("LOAN_AMOUNT")));
				cell031.setCellStyle(dataStyle);

				Cell cell032 = row.createCell(32);
				cell032.setCellValue(formatValue(error.get("RC_NUMBER")));
				cell032.setCellStyle(dataStyle);

				Cell cell033 = row.createCell(33);
				cell033.setCellValue(formatValue(error.get("SANCTION_DATE")));
				cell033.setCellStyle(dataStyle);

				Cell cell034 = row.createCell(34);
				cell034.setCellValue(formatValue(error.get("DISBURSEMENT_TYPE")));
				cell034.setCellStyle(dataStyle);

				Cell cell035 = row.createCell(35);
				cell035.setCellValue(formatValue(error.get("DISBURSEMENT_DATE")));
				cell035.setCellStyle(dataStyle);

				Cell cell036 = row.createCell(36);
				cell036.setCellValue(formatValue(error.get("PROCESSING_FEE")));
				cell036.setCellStyle(dataStyle);

				Cell cell037 = row.createCell(37);
				cell037.setCellValue(formatValue(error.get("DISBURSEMENT_AMOUNT")));
				cell037.setCellStyle(dataStyle);

				Cell cell038 = row.createCell(38);
				cell038.setCellValue(formatValue(error.get("MODE_OF_DISBURSEMENT")));
				cell038.setCellStyle(dataStyle);

				Cell cell039 = row.createCell(39);
				cell039.setCellValue(formatValue(error.get("COLLATERAL_STATUS")));
				cell039.setCellStyle(dataStyle);

				Cell cell040 = row.createCell(40);
				cell040.setCellValue(formatValue(error.get("ROI")));
				cell040.setCellStyle(dataStyle);

				Cell cell041 = row.createCell(41);
				cell041.setCellValue(formatValue(error.get("TENOR")));
				cell041.setCellStyle(dataStyle);

				Cell cell042 = row.createCell(42);
				cell042.setCellValue(formatValue(error.get("MTD_SPILLOVER")));
				cell042.setCellStyle(dataStyle);

				Cell cell043 = row.createCell(43);
				cell043.setCellValue(formatValue(error.get("CRO_NAME_ID")));
				cell043.setCellStyle(dataStyle);

				Cell cell044 = row.createCell(44);
				cell044.setCellValue(formatValue(error.get("MAPPED_BSM_NAME_ID")));
				cell044.setCellStyle(dataStyle);

				Cell cell045 = row.createCell(45);
				cell045.setCellValue(formatValue(error.get("MAPPED_BCM_NAME_ID")));
				cell045.setCellStyle(dataStyle);

				Cell cell046 = row.createCell(46);
				cell046.setCellValue(formatValue(error.get("DISBURSEMENT_INTENDED_DATE")));
				cell046.setCellStyle(dataStyle);

				Cell cell047 = row.createCell(47);
				cell047.setCellValue(formatValue(error.get("CHEQUE_HANDOVER_DATE")));
				cell047.setCellStyle(dataStyle);

				Cell cell048 = row.createCell(48);
				cell048.setCellValue(formatValue(error.get("SUBSEQUENT_FLAG")));
				cell048.setCellStyle(dataStyle);

				Cell cell049 = row.createCell(49);
				cell049.setCellValue(formatValue(error.get("SUBSEQUENT_RECEIVED_CHANNEL")));
				cell049.setCellStyle(dataStyle);

				Cell cell050 = row.createCell(50);
				cell050.setCellValue(formatValue(error.get("FCPG_STATUS")));
				cell050.setCellStyle(dataStyle);

				Cell cell051 = row.createCell(51);
				cell051.setCellValue(formatValue(error.get("FCPG_RECOMMENDATION")));
				cell051.setCellStyle(dataStyle);

				Cell cell052 = row.createCell(52);
				cell052.setCellValue(formatValue(error.get("HUNTER_STATUS")));
				cell052.setCellStyle(dataStyle);

				Cell cell053 = row.createCell(53);
				cell053.setCellValue(formatValue(error.get("VALUATION_AMOUNT")));
				cell053.setCellStyle(dataStyle);

				Cell cell054 = row.createCell(54);
				cell054.setCellValue(formatValue(error.get("LAN")));
				cell054.setCellStyle(dataStyle);

				Cell cell055 = row.createCell(55);
				cell055.setCellValue(formatValue(error.get("AGREEMENT_ID")));
				cell055.setCellStyle(dataStyle);

				Cell cell056 = row.createCell(56);
				cell056.setCellValue(formatValue(error.get("LTV")));
				cell056.setCellStyle(dataStyle);

				Cell cell057 = row.createCell(57);
				cell057.setCellValue(formatValue(error.get("QUERY_CLASSIFICATION")));
				cell057.setCellStyle(dataStyle);

				Cell cell058 = row.createCell(58);
				cell058.setCellValue(formatValue(error.get("PROPERTY_TYPE")));
				cell058.setCellStyle(dataStyle);

				Cell cell059 = row.createCell(59);
				cell059.setCellValue(formatValue(error.get("LAST_STAGE")));
				cell059.setCellStyle(dataStyle);

				Cell cell060 = row.createCell(60);
				cell060.setCellValue(formatValue(error.get("LAST_STATUS")));
				cell060.setCellStyle(dataStyle);

				Cell cell061 = row.createCell(61);
				cell061.setCellValue(formatValue(error.get("TYPE_OF_FACILITY")));
				cell061.setCellStyle(dataStyle);

				Cell cell062 = row.createCell(62);
				cell062.setCellValue(formatValue(error.get("LAST_STAGE_STATUS_TIMESTAMP")));
				cell062.setCellStyle(dataStyle);

				Cell cell063 = row.createCell(63);
				cell063.setCellValue(formatValue(error.get("CHEQUE_HANDOVER_FLAG")));
				cell063.setCellStyle(dataStyle);

				Cell cell064 = row.createCell(64);
				cell064.setCellValue(formatValue(error.get("HIGHEST_BUREAU_SCORE")));
				cell064.setCellStyle(dataStyle);

				Cell cell065 = row.createCell(65);
				cell065.setCellValue(formatValue(error.get("PROCESS_TYPE")));
				cell065.setCellStyle(dataStyle);

				Cell cell066 = row.createCell(66);
				cell066.setCellValue(formatValue(error.get("STP_REMARKS")));
				cell066.setCellStyle(dataStyle);

				Cell cell067 = row.createCell(67);
				cell067.setCellValue(formatValue(error.get("ADMISSION_STATUS")));
				cell067.setCellStyle(dataStyle);

				Cell cell068 = row.createCell(68);
				cell068.setCellValue(formatValue(error.get("MORATORIUM_STATUS")));
				cell068.setCellStyle(dataStyle);

				Cell cell069 = row.createCell(69);
				cell069.setCellValue(formatValue(error.get("MORATORIUM_TYPE")));
				cell069.setCellStyle(dataStyle);

				Cell cell070 = row.createCell(70);
				cell070.setCellValue(formatValue(error.get("ZONE")));
				cell070.setCellStyle(dataStyle);

				Cell cell071 = row.createCell(71);
				cell071.setCellValue(formatValue(error.get("STATE")));
				cell071.setCellStyle(dataStyle);

				Cell cell072 = row.createCell(72);
				cell072.setCellValue(formatValue(error.get("TRANCHE_NUMBER")));
				cell072.setCellStyle(dataStyle);

				Cell cell073 = row.createCell(73);
				cell073.setCellValue(formatValue(error.get("MODE_OF_REPAYMENT")));
				cell073.setCellStyle(dataStyle);

				Cell cell074 = row.createCell(74);
				cell074.setCellValue(formatValue(error.get("NEO_APPLICATION_NUMBER")));
				cell074.setCellStyle(dataStyle);

				Cell cell075 = row.createCell(75);
				cell075.setCellValue(formatValue(error.get("BENEFICIARY_TYPE")));
				cell075.setCellStyle(dataStyle);

				Cell cell076 = row.createCell(76);
				cell076.setCellValue(formatValue(error.get("TYPE_OF_REMITTANCE")));
				cell076.setCellStyle(dataStyle);

				Cell cell077 = row.createCell(77);
				cell077.setCellValue(formatValue(error.get("REASON_FOR_DISBURSEMENT")));
				cell077.setCellStyle(dataStyle);

				Cell cell078 = row.createCell(78);
				cell078.setCellValue(formatValue(error.get("MORATORIUM_IN_MONTHS")));
				cell078.setCellStyle(dataStyle);

				Cell cell079 = row.createCell(79);
				cell079.setCellValue(formatValue(error.get("MODE_OF_KYC")));
				cell079.setCellStyle(dataStyle);

				Cell cell080 = row.createCell(80);
				cell080.setCellValue(formatValue(error.get("FINAL_CREDIT_DECISION")));
				cell080.setCellStyle(dataStyle);

				Cell cell081 = row.createCell(81);
				cell081.setCellValue(formatValue(error.get("FUND_TRANSFER_METHOD")));
				cell081.setCellStyle(dataStyle);

				Cell cell082 = row.createCell(82);
				cell082.setCellValue(formatValue(error.get("DISBURSEMENT_STAGE")));
				cell082.setCellStyle(dataStyle);

				Cell cell083 = row.createCell(83);
				cell083.setCellValue(formatValue(error.get("RHS_USER_NAME_ID")));
				cell083.setCellStyle(dataStyle);

//			Cell cell084 = row.createCell(84);
//			cell084.setCellValue(formatValue(error.getErrorId()));
//			cell084.setCellStyle(dataStyle);
//			
//			Cell cell085 = row.createCell(85);
//			cell085.setCellValue(formatValue(error.getRowNumber()));
//			cell085.setCellStyle(dataStyle);

				Cell cell084 = row.createCell(84);
				cell084.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell084.setCellStyle(dataStyle);

				Cell cell085 = row.createCell(85);
				cell085.setCellValue(formatValue(error.get("CREATED_DATE")));
				cell085.setCellStyle(dataStyle);
				
				Cell cell086 = row.createCell(86);
				cell086.setCellValue(formatValue(error.get("ROW_NUMBER")));
				cell086.setCellStyle(dataStyle);
			}

			// ---------------------------------------------------
			// Step 5: Auto size columns
			// ---------------------------------------------------

			for (int i = 0; i < 88; i++) {
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
		return val.isEmpty() ||val.equalsIgnoreCase("NA") || val.equalsIgnoreCase("N/A") || val.equalsIgnoreCase("#N/A") || val.equalsIgnoreCase("-")
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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_ILENS_DUMP_UPL_SEQ.NEXTVAL FROM DUAL", long.class);

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

		int batchSize = 1000;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, pkList.size());

			List<String> batch = pkList.subList(start, end);

			List<String> result = entityManager.createQuery(
					"SELECT g.applicationNumber FROM IlensDump g WHERE g.applicationNumber IN :list AND g.cycleFromDate = :fromDate AND g.cycleToDate = :toDate",
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


		if (isNullEquivalent(value)) {

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

					String[] formats = { "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd/MM/yyyy", "dd/MM/yyyy HH:mm",
							"yyyy-MM-dd HH:mm" };

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
				else {
					// field.set(entity, LocalDate.parse(value,
					// DateTimeFormatter.ofPattern("yyyy-MM-dd")));

					String[] formats = { "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd/MM/yyyy", "dd/MM/yyyy HH:mm",
							"yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss" };

					LocalDateTime parseDate = null;

					for (String format : formats) {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
							parseDate = LocalDateTime.parse(value, formatter);
							break;
						} catch (Exception e) {

						}
					}
				}
			}

			else if (type.equals(LocalDateTime.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue());
//					else
//						field.set(entity, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				else {
					/*
					 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); field.set(entity,
					 * sdf.parse(value));
					 */

					String[] formats = { "dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy", "dd/MM/yyyy", "dd/MM/yyyy HH:mm",
							"yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss" };

					LocalDateTime parseDate = null;

					for (String format : formats) {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
							parseDate = LocalDateTime.parse(value, formatter);
							break;
						} catch (Exception e) {

						}
					}

					field.set(entity, parseDate);
				}
			}

			// logger.info("Field : {} Successfully set with value : {}", fieldName, value);

		} catch (Exception e) {

			
			  logger.
			  error("Error while setting field : '{}' with value : '{}' , type : {} ",
			  fieldName, value, type.getSimpleName());
			 
			throw e;
		}
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private IlensDump mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		IlensDump entity = new IlensDump();

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

	public void IlensInsertTransaction(List<IlensDump> entityList, String uploadId, Date cycleFromDate,
			Date cycleToDate) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in Ilens Transaction table");
			return;
		}

		logger.info("Starting delete Ilens Dump table");

		// delete existing record for from date and To date
		jdbcTemplate.update("DELETE FROM TM_VHL_ILENS_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("End delete Ilens dump table");

		String sql = "INSERT INTO TM_VHL_ILENS_DUMP ( "
				+ "S_NO, APPLICATION_NUMBER, ILENS_ID, APPLICANT_NAME, APPLICATION_LOGIN_DATE, DOCKET_LOGIN_DATE, CRM_ID, SOURCING_CPC, PROCESSING_CPC, CITY, "
				+ " DISBURSEMENT_SEQ, CHANNEL_TYPE, SOL_ID, CHANNEL_NAME_ID, BROKER_ID, SUPPLIER_ID, SALES_EXECUTIVE_NAME_ID, CHANNEL_DEALER_SALES_EXEC, PROFILE, PRODUCT, "
				+ "DEALER_NAME, SCHEME, VARIANT, SUB_VARIANT, STAGE, STATUS, CUSTOMER_SEGMENT, BT_FLAG, BT_TYPE, PSL_FLAG, "
				+ "RI_NRI, LOAN_AMOUNT, RC_NUMBER, SANCTION_DATE, DISBURSEMENT_TYPE, DISBURSEMENT_DATE, PROCESSING_FEE, DISBURSEMENT_AMOUNT, MODE_OF_DISBURSEMENT, COLLATERAL_STATUS, "
				+ "ROI, TENOR, MTD_SPILLOVER, CRO_NAME_ID, MAPPED_BSM_NAME_ID, MAPPED_BCM_NAME_ID, DISBURSEMENT_INTENDED_DATE, CHEQUE_HANDOVER_DATE, SUBSEQUENT_FLAG, SUBSEQUENT_RECEIVED_CHANNEL, "
				+ "FCPG_STATUS, FCPG_RECOMMENDATION, HUNTER_STATUS, VALUATION_AMOUNT, LAN, AGREEMENT_ID, LTV, QUERY_CLASSIFICATION, PROPERTY_TYPE, LAST_STAGE, "
				+ "LAST_STATUS, TYPE_OF_FACILITY, LAST_STAGE_STATUS_TIMESTAMP, CHEQUE_HANDOVER_FLAG, HIGHEST_BUREAU_SCORE, PROCESS_TYPE, STP_REMARKS, ADMISSION_STATUS, MORATORIUM_STATUS, MORATORIUM_TYPE, "
				+ "ZONE, STATE, TRANCHE_NUMBER, MODE_OF_REPAYMENT, NEO_APPLICATION_NUMBER, BENEFICIARY_TYPE, TYPE_OF_REMITTANCE, REASON_FOR_DISBURSEMENT, MORATORIUM_IN_MONTHS, MODE_OF_KYC, "
				+ "FINAL_CREDIT_DECISION, FUND_TRANSFER_METHOD, DISBURSEMENT_STAGE, RHS_USER_NAME_ID, CREATED_BY, CREATED_DATE, FROM_CYCLE_DATE, TO_CYCLE_DATE, UPLOAD_ID, FILE_NAME "
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
				+ "LOG ERRORS INTO TM_VHL_ILENS_DUMP_ERROR ('" + uploadId + "') " + "REJECT LIMIT UNLIMITED ";
		;

		int batchSize = 500;

		long start = System.currentTimeMillis();

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, IlensDump entity) -> {

			// 1 S_NO
			ps.setLong(1, entity.getsNo());

			// 2 APPLICATION_NUMBER
			ps.setString(2, entity.getApplicationNumber());

			// 3 ILENS_ID
			if (entity.getIlensId() != null) {
				ps.setLong(3, entity.getIlensId());
			} else {
				ps.setNull(3, Types.BIGINT);
			}

			// 4 APPLICANT_NAME
			ps.setString(4, entity.getApplicantName());

			// 5 APPLICATION_LOGIN_DATE
			if (entity.getApplicationLoginDate() != null) {
				ps.setTimestamp(5, Timestamp.valueOf(entity.getApplicationLoginDate()));
			} else {
				ps.setNull(5, Types.TIMESTAMP);
			}

			// 6 DOCKET_LOGIN_DATE
			if (entity.getDocketLoginDate() != null) {
				ps.setTimestamp(6, Timestamp.valueOf(entity.getDocketLoginDate()));
			} else {
				ps.setNull(6, Types.TIMESTAMP);
			}

			// 7 CRM_ID
			ps.setString(7, entity.getCrmId());

			// 8 SOURCING_CPC
			ps.setString(8, entity.getSourcingCpc());

			// 9 PROCESSING_CPC
			ps.setString(9, entity.getProcessingCpc());

			// 10 CITY
			ps.setString(10, entity.getCity());

			// 11 DISBURSEMENT_SEQ
			if (entity.getDisbursementSeq() != null) {
				ps.setLong(11, entity.getDisbursementSeq());
			} else {
				ps.setNull(11, Types.NUMERIC);
			}

			// 12 CHANNEL_TYPE
			ps.setString(12, entity.getChannelType());

			// 13 SOL_ID
			ps.setString(13, entity.getSolId());

			// 14 CHANNEL_NAME_ID
			ps.setString(14, entity.getChannelNameId());

			// 15 BROKER_ID
			if (entity.getBrokerId() != null) {
				ps.setLong(15, entity.getBrokerId());
			} else {
				ps.setNull(15, Types.NUMERIC);
			}

			// 16 SUPPLIER_ID
			if (entity.getSupplierId() != null) {
				ps.setLong(16, entity.getSupplierId());
			} else {
				ps.setNull(16, Types.NUMERIC);
			}

			// 17 SALES_EXECUTIVE_NAME_ID
			if (entity.getSalesExecutiveNameId() != null) {
				ps.setString(17, entity.getSalesExecutiveNameId());
			} else {
				ps.setNull(17, java.sql.Types.VARCHAR);
			}

			// 18 CHANNEL_DEALER_SALES_EXECUTIVE
			if (entity.getChannelDealerSalesExecutive() != null) {
				ps.setString(18, entity.getChannelDealerSalesExecutive());
			} else {
				ps.setNull(18, java.sql.Types.VARCHAR);
			}

			// 19 PROFILE
			ps.setString(19, entity.getProfile());

			// 20 PRODUCT
			ps.setString(20, entity.getProduct());

			// 21 DEALER_NAME
			ps.setString(21, entity.getDealerName());

			// 22 SCHEME
			ps.setString(22, entity.getScheme());

			// 23 VARIANT
			ps.setString(23, entity.getVariant());

			// 24 SUB_VARIANT
			ps.setString(24, entity.getSubVariant());

			// 25 STAGE
			ps.setString(25, entity.getStage());

			// 26 STATUS
			ps.setString(26, entity.getStatus());

			// 27 CUSTOMER_SEGMENT
			ps.setString(27, entity.getCustomerSegment());

			// 28 BT_FLAG
			ps.setString(28, entity.getBtFlag());

			// 29 BT_TYPE
			ps.setString(29, entity.getBtType());

			// 30 PSL_FLAG
			ps.setString(30, entity.getPslFlag());

			// 31 RIN
			ps.setString(31, entity.getRiNri());

			// 32 LOAN_AMOUNT
			if (entity.getLoanAmount() != null) {
				ps.setBigDecimal(32, entity.getLoanAmount());
			} else {
				ps.setNull(32, Types.NUMERIC);
			}

			// 33 RC_NUMBER
			ps.setString(33, entity.getRcNumber());

			// 34 SANCTION_DATE
			if (entity.getSanctionDate() != null) {
				ps.setTimestamp(34, Timestamp.valueOf(entity.getSanctionDate()));
			} else {
				ps.setNull(34, Types.TIMESTAMP);
			}

			// 35 DISBURSEMENT_TYPE
			ps.setString(35, entity.getDisbursementType());

			// 36 DISBURSEMENT_DATE
			if (entity.getDisbursementDate() != null) {
				ps.setTimestamp(36, Timestamp.valueOf(entity.getDisbursementDate()));
			} else {
				ps.setNull(36, Types.TIMESTAMP);
			}

			// 37 PROCESSING_FEE
			if (entity.getProcessingFee() != null) {
				ps.setBigDecimal(37, entity.getProcessingFee());
			} else {
				ps.setNull(37, Types.NUMERIC);
			}

			// 38 DISBURSEMENT_AMOUNT
			if (entity.getDisbursementAmount() != null) {
				ps.setBigDecimal(38, entity.getDisbursementAmount());
			} else {
				ps.setNull(38, Types.NUMERIC);
			}

			// 39 MODE_OF_DISBURSEMENT
			ps.setString(39, entity.getModeOfDisbursement());

			// 40 COLLATERAL_STATUS
			ps.setString(40, entity.getCollateralStatus());

			// 41 ROI
			if (entity.getRoi() != null) {
				ps.setBigDecimal(41, entity.getRoi());
			} else {
				ps.setNull(41, Types.NUMERIC);
			}

			// 42 TENOR
			if (entity.getTenor() != null) {
				ps.setLong(42, entity.getTenor());
			} else {
				ps.setNull(42, Types.BIGINT);
			}

			// 43 MTD_SPILLOVER
			ps.setString(43, entity.getMtdSpillover());

			// 44 CRO_NAME_ID
			ps.setString(44, entity.getCroNameId());

			// 45 MAPPED_BSM_NAME_ID
			ps.setString(45, entity.getMappedBsmNameId());

			// 46 MAPPED_BCM_NAME_ID
			ps.setString(46, entity.getMappedBcmNameId());

			// 47 DISBURSEMENT_INTENDED_DATE
			if (entity.getDisbursementIntendedDate() != null) {
				ps.setTimestamp(47, Timestamp.valueOf(entity.getDisbursementIntendedDate()));
			} else {
				ps.setNull(47, Types.TIMESTAMP);
			}

			// 48 CHEQUE_HANDOVER_DATE
			if (entity.getChequeHandoverDate() != null) {
				ps.setTimestamp(48, Timestamp.valueOf(entity.getChequeHandoverDate()));
			} else {
				ps.setNull(48, Types.TIMESTAMP);
			}

			// 49 SUBSEQUENT_FLAG
			ps.setString(49, entity.getSubsequentFlag());

			// 50 SUBSEQUENT_RECEIVED_CHANNEL
			ps.setString(50, entity.getSubsequentReceivedChannel());

			// 51 FCPG_STATUS
			ps.setString(51, entity.getFcpgStatus());

			// 52 FCPG_RECOMMENDATION
			ps.setString(52, entity.getFcpgRecommendation());

			// 53 HUNTER_STATUS
			ps.setString(53, entity.getHunterStatus());

			// 54 VALUATION_AMOUNT
			if (entity.getValuationAmount() != null) {
				ps.setBigDecimal(54, entity.getValuationAmount());
			} else {
				ps.setNull(54, Types.NUMERIC);
			}

			// 55 LAN
			ps.setString(55, entity.getLan());

			// 56 AGREEMENT_ID
			ps.setString(56, entity.getAgreementId());

			// 57 LTV
			if (entity.getLtv() != null) {
				ps.setBigDecimal(57, entity.getLtv());
			} else {
				ps.setNull(57, Types.NUMERIC);
			}

			// 58 QUERY_CLASSIFICATION
			ps.setString(58, entity.getQueryClassification());

			// 59 PROPERTY_TYPE
			ps.setString(59, entity.getPropertyType());

			// 60 LAST_STAGE
			ps.setString(60, entity.getLastStage());

			// 61 LAST_STATUS
			ps.setString(61, entity.getLastStatus());

			// 62 TYPE_OF_FACILITY
			ps.setString(62, entity.getTypeOfFacility());

			// 63 LAST_STAGE_STATUS_TS
			if (entity.getLastStageStatusTimestamp() != null) {
				ps.setTimestamp(63, Timestamp.valueOf(entity.getLastStageStatusTimestamp()));
			} else {
				ps.setNull(63, Types.TIMESTAMP);
			}

			// 64 CHEQUE_HANDOVER_FLAG
			ps.setString(64, entity.getChequeHandoverFlag());

			// 65 HIGHEST_BUREAU_SCORE
			if (entity.getHighestBureauScore() != null) {
				ps.setInt(65, entity.getHighestBureauScore());
			} else {
				ps.setNull(65, Types.INTEGER);
			}

			// 66 PROCESS_TYPE
			ps.setString(66, entity.getProcessType());

			// 67 STP_REMARKS
			ps.setString(67, entity.getStpRemarks());

			// 68 ADMISSION_STATUS
			ps.setString(68, entity.getAdmissionStatus());

			// 69 MORATORIUM_STATUS
			ps.setString(69, entity.getMoratoriumStatus());

			// 70 MORATORIUM_TYPE
			ps.setString(70, entity.getMoratoriumType());

			// 71 ZONE
			ps.setString(71, entity.getZone());

			// 72 STATE
			ps.setString(72, entity.getState());

			// 73 TRANCHE_NUMBER
			ps.setLong(73, entity.getTrancheNumber());

			// 74 MODE_OF_REPAYMENT
			ps.setString(74, entity.getModeOfRepayment());

			// 75 NOC_APPLICATION_NUMBER
			ps.setString(75, entity.getNeoApplicationNumber());

			// 76 BENEFICIARY_TYPE
			ps.setString(76, entity.getBeneficiaryType());

			// 77 TYPE_OF_REMITTANCE
			ps.setString(77, entity.getTypeOfRemittance());

			// 78 REASON_FOR_DISBURSEMENT
			ps.setString(78, entity.getReasonForDisbursement());

			// 79 MORATORIUM_IN_MONTHS
			ps.setString(79, entity.getMoratoriumInMonths());

			// 80 MODE_OF_KYC
			ps.setString(80, entity.getModeOfKyc());

			// 81 FINAL_CREDIT_DECISION
			ps.setString(81, entity.getFinalCreditDecision());

			// 82 FUND_TRANSFER_METHOD
			ps.setString(82, entity.getFundTransferMethod());

			// 83 DISBURSEMENT_STAGE
			ps.setString(83, entity.getDisbursementStage());

			// 84 RHS_USER_NAME_ID
			ps.setString(84, entity.getRhsUserNameId());

			// 85 CREATED_BY
			ps.setString(85, entity.getCreatedby());

			// 86 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(86, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(86, Types.TIMESTAMP);
			}

			// 87 FROM_CYCLE_DATE
			if (entity.getCycleFromDate() != null) {
				ps.setDate(87, new java.sql.Date(entity.getCycleFromDate().getTime()));
			} else {
				ps.setNull(87, Types.DATE);
			}

			// 88 TO_CYCLE_DATE
			if (entity.getCycleToDate() != null) {
				ps.setDate(88, new java.sql.Date(entity.getCycleToDate().getTime()));
			} else {
				ps.setNull(88, Types.DATE);
			}

			// 89 created_user
			ps.setString(89, entity.getUploadId());

			// 90 created_user
			ps.setString(90, entity.getFileName());

		});

		logger.info("Ilense Dump batch insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// MAP ROW TO Error ENTITY (Using Reflection)
	// ============================================================

	private IlensDumpError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		IlensDumpError errorEntity = new IlensDumpError();
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
	private void process(List<IlensDump> validEntityList, List<IlensDumpError> invalidIntityList, Date cycleFromDate,
			Date cycleToDate, String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into main
			IlensInsertTransaction(validEntityList, uploadId, cycleFromDate, cycleToDate);

			logger.info("batch insert Error");
			// batch Error insert
			IlensDumpError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Ilens Trans  Dump File upload Process Failed");
		}
	}

	@Transactional
	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_ILENS_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_ILENS_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}

}
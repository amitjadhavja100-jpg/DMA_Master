package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.model.FinnoneDump;
import com.icici.dma.model.FinnoneDumpError;
import com.icici.dma.repository.AlddDumpRepository;
import com.icici.dma.repository.BranchMasterRepository;
import com.icici.dma.repository.BranchMasterTempRepository;
import com.icici.dma.repository.ChannelMasterRepository;
import com.icici.dma.repository.ChannnelMasterTempRepository;
import com.icici.dma.repository.FinnoneDumpErrorRepository;
import com.icici.dma.repository.FinnoneDumpRepository;
import com.icici.dma.repository.GSTStateMasterMainRepository;
import com.icici.dma.repository.GSTStateMasterTempRepository;
import com.icici.dma.repository.GstMfMasterMainRepository;
import com.icici.dma.repository.GstMfMasterTempRepository;
import com.icici.dma.repository.GstToMasterMainRepository;
import com.icici.dma.repository.GstToMasterTempRepository;
import com.icici.dma.repository.ILensChannelMasterMainRepository;
import com.icici.dma.repository.ILensChannelMasterTempRepository;
import com.icici.dma.repository.IlensDumpRepository;
import com.icici.dma.repository.ModelMasterRepository;
import com.icici.dma.repository.ModelMasterTempRepository;
import com.icici.dma.repository.OutsourceMasterMainRepository;
import com.icici.dma.repository.OutsourceMasterTempRepository;
import com.icici.dma.repository.RcaCbcRepository;
import com.icici.dma.repository.RcaCibilRepository;
import com.icici.dma.repository.RcaProcessShop;
import com.icici.dma.service.FinnoneDumpService;

@Service
public class FinnoneDumpServiceImpl2 implements FinnoneDumpService {

	private static final Logger logger = LogManager.getLogger(FinnoneDumpServiceImpl2.class);

	@Autowired
	private FinnoneDumpRepository repository;

	@Autowired
	private FinnoneDumpErrorRepository FinnoneDumpErrorrepo;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private AlddDumpRepository alddrepository;

	@Autowired
	private IlensDumpRepository ilensrepository;

	@Autowired
	private RcaCbcRepository rcadbcrepository;

	@Autowired
	private RcaCibilRepository rcacibilrepository;

	@Autowired
	private RcaProcessShop rcaprocesshop;

	@Autowired
	private BranchMasterRepository branchMasterRepo;

	@Autowired
	private BranchMasterTempRepository branchMasterTempRepo;

	@Autowired
	private ChannnelMasterTempRepository channnelMasterTempRepo;

	@Autowired
	private ChannelMasterRepository channelMasterRepo;

	@Autowired
	private GstMfMasterMainRepository gstMfMasterMainRepo;

	@Autowired
	private GstMfMasterTempRepository gstMfMasterTempRepo;

	@Autowired
	private GSTStateMasterMainRepository gSTStateMasterMainRepo;

	@Autowired
	private GSTStateMasterTempRepository gSTStateMasterTempRepo;

	@Autowired
	private GstToMasterMainRepository gstToMasterMainRepo;

	@Autowired
	private GstToMasterTempRepository gstToMasterTempRepo;

	@Autowired
	private ILensChannelMasterMainRepository iLensChannelMasterMainRepo;

	@Autowired
	private ILensChannelMasterTempRepository iLensChannelMasterTempRepo;

	@Autowired
	private ModelMasterRepository modelMasterRepo;

	@Autowired
	private ModelMasterTempRepository modelMasterTempRepo;

	@Autowired
	private OutsourceMasterMainRepository outsourceMasterMainRepo;

	@Autowired
	private OutsourceMasterTempRepository outsourceMasterTempRepo;

//	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	// Cache entity fields
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();

	private static final String PkField = "agreementId";

	DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);

	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

	static {
		HEADER_MAP.put("AGREEMENTID", "agreementId");
		HEADER_MAP.put("AGREEMENTNO", "agreementNo");
		HEADER_MAP.put("AGREEMENTDATE", "agreementDate");
		HEADER_MAP.put("DISB_DATE", "disbDate");
		HEADER_MAP.put("DISBURSALAMOUNT", "disbursalAmount");
		HEADER_MAP.put("DISB_AMT", "disbAmt");
		HEADER_MAP.put("AMTFIN", "amtFin");
		HEADER_MAP.put("PRETAXIRR", "preTaxIrr");
		HEADER_MAP.put("LESSEEID", "lesseeId");
		HEADER_MAP.put("TENURE", "tenure");

		HEADER_MAP.put("EMI", "emi");
		HEADER_MAP.put("FILENO", "fileNo");
		HEADER_MAP.put("BRANCHNM", "branchNm");
		HEADER_MAP.put("MODELNO", "modelNo");
		HEADER_MAP.put("MANUFACTURERDESC", "manufacturerDesc");
		HEADER_MAP.put("NAME", "name");
		HEADER_MAP.put("DEALERNAME", "dealerName");
		HEADER_MAP.put("ADVANCEINSTL", "advanceInstl");
		HEADER_MAP.put("PROCESSINGFEE", "processingFee");
		HEADER_MAP.put("MFR_SUBVENTION_IN", "mfrSubventionIn");

		HEADER_MAP.put("MFR_SUBVENTION_PAID", "mfrSubventionPaid");
		HEADER_MAP.put("DEALER_SUBVENTION", "dealerSubvention");
		HEADER_MAP.put("DMA_SUBVENTION", "dmaSubvention");
		HEADER_MAP.put("PROMOTIONDESC", "promotionDesc");
		HEADER_MAP.put("MARGINMONEY", "marginMoney");
		HEADER_MAP.put("ADVANCE_EMI", "advanceEmi");
		HEADER_MAP.put("EMPLOYERNAME", "employerName");
		HEADER_MAP.put("STATUS", "status");
		HEADER_MAP.put("MAKE", "make");
		HEADER_MAP.put("V_ASSET_CATG", "vAssetCatg");

		HEADER_MAP.put("PRODUCTFLAG", "productFlag");
		HEADER_MAP.put("BRANCH_CODE", "branchCode");
		HEADER_MAP.put("DMABROKERCODE", "dmaBrokerCode");
		HEADER_MAP.put("SCHEMECODE", "schemeCode");
		HEADER_MAP.put("PROMOTIONSCHEME", "promotionScheme");
		HEADER_MAP.put("EFFRATE", "effRate");
		HEADER_MAP.put("MODELCODE", "modelCode");
		HEADER_MAP.put("SUBMODELCODE", "subModelCode");
		HEADER_MAP.put("GROSS_LTV", "grossLtv");
		HEADER_MAP.put("NET_LTV", "netLtv");

		HEADER_MAP.put("FINALSOURCE", "finalSource");
		HEADER_MAP.put("FIRSTSOURCE", "firstSource");
		HEADER_MAP.put("CUSTCATG", "custCatg");
		HEADER_MAP.put("DMA_SUBVENTION_NOT_DED", "dmaSubventionNot");
		HEADER_MAP.put("EMPTYPE", "empType");
		HEADER_MAP.put("CFOC", "cfoc");
		HEADER_MAP.put("STATE", "state");
		HEADER_MAP.put("CHANNELCODE", "channelCode");
		HEADER_MAP.put("MANUFACTURERID", "manufacturerId");
		HEADER_MAP.put("EMPLOYERID", "employerId");

		HEADER_MAP.put("INFAVOUROF", "inFavourOf");
		HEADER_MAP.put("CHEQUESTATUS", "chequeStatus");
		HEADER_MAP.put("OSP_CODE", "ospCode");
		HEADER_MAP.put("DME_NAME", "dmeName");
		HEADER_MAP.put("DUMMY", "dummy");
		HEADER_MAP.put("CUSTOMER_NAME", "customerName");

		HEADER_MAP.put("CHARGE ID1", "chargeId1");
		HEADER_MAP.put("CHARGE DESC1", "chargeDesc1");
		HEADER_MAP.put("CHARGE_AMT1", "chargeAmt1");
		HEADER_MAP.put("CHARGE ID2", "chargeId2");
		HEADER_MAP.put("CHARGE DESC2", "chargeDesc2");
		HEADER_MAP.put("CHARGE_AMT2", "chargeAmt2");
//		HEADER_MAP.put("CHARGE ID3", "chargeId3");
//		HEADER_MAP.put("CHARGE DESC3", "chargeDesc3");
//		HEADER_MAP.put("CHARGE_AMT3", "chargeAmt3");
		HEADER_MAP.put("CHARGE ID4", "chargeId4");
		HEADER_MAP.put("CHARGE DESC4", "chargeDesc4");
		HEADER_MAP.put("CHARGE_AMT4", "chargeAmt4");
		HEADER_MAP.put("CHARGE ID5", "chargeId5");
		HEADER_MAP.put("CHARGE DESC5", "chargeDesc5");
		HEADER_MAP.put("CHARGE_AMT5", "chargeAmt5");

		HEADER_MAP.put("CHARGE ID6", "chargeId6");
		HEADER_MAP.put("CHARGE DESC6", "chargeDesc6");
		HEADER_MAP.put("CHARGE_AMT6", "chargeAmt6");
//		HEADER_MAP.put("CHARGE ID7", "chargeId7");
//		HEADER_MAP.put("CHARGE DESC7", "chargeDesc7");
//		HEADER_MAP.put("CHARGE_AMT7", "chargeAmt7");
		HEADER_MAP.put("CHARGE ID8", "chargeId8");
		HEADER_MAP.put("CHARGE DESC8", "chargeDesc8");
		HEADER_MAP.put("CHARGE_AMT8", "chargeAmt8");
		HEADER_MAP.put("CHARGE ID9", "chargeId9");
		HEADER_MAP.put("CHARGE DESC9", "chargeDesc9");
		HEADER_MAP.put("CHARGE_AMT9", "chargeAmt9");
		HEADER_MAP.put("CHARGE ID10", "chargeId10");

		HEADER_MAP.put("CHARGE DESC10", "chargeDesc10");
		HEADER_MAP.put("CHARGE_AMT10", "chargeAmt10");
		HEADER_MAP.put("CHARGE ID11", "chargeId11");
		HEADER_MAP.put("CHARGE DESC11", "chargeDesc11");
		HEADER_MAP.put("CHARGE_AMT11", "chargeAmt11");

		HEADER_MAP.put("EMPLOYMENT_TYPE", "employmentType");
		HEADER_MAP.put("PSL_FLAG", "pslFlag");
		HEADER_MAP.put("INSTRUMENT_TYPE", "instrumentType");
		HEADER_MAP.put("BANK", "bank");
		HEADER_MAP.put("BANK_BRANCH", "bankBranch");
		HEADER_MAP.put("CUSTOMER_AC", "customerAc");
		HEADER_MAP.put("MICR", "micr");
		HEADER_MAP.put("DEST_BANK_AC_TYPE", "destBankAcType");
		HEADER_MAP.put("PROCESS_SHOP", "processShop");

		// Cache all fields once
		for (Field field : FinnoneDump.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		for (Field field : FinnoneDumpError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
	}

	@Override
	public String uploadFinnoneExcel(MultipartFile file, String user, Date cycleFromDate, Date cycleToDate) {

		long startTime = System.currentTimeMillis();
		logger.info("========== Finnone Dump UPLOAD STARTED ==========");

		logger.info("========== User : {} ==========", user);
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
			Set<Long> validPKSet = new HashSet<>();

			// hold primary key valye and row number were appear ==> [ "AB12" -> [2,4,7] ]
			Map<Long, List<Integer>> pkRowMap = new HashMap<>();

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
//					String pk = value.trim().toUpperCase();
					Long pk = Long.parseLong(value);

					// store row index against pk
					pkRowMap.computeIfAbsent(pk, k -> new ArrayList<>()).add(r);

					validPKSet.add(pk);
//					if (!validPKSet.add(pk)) {
//						duplicateExcelRows.add(r);
//					}

				} catch (Exception e) {
					logger.info("Row index {} marked as invalid due to PK Parse Error. value :{}", r, value);
					invalidRows.add(r);
				}

			}

			logger.info("Total Invalid PK rows: {}", invalidRows.size());
			logger.info("Duplicate PK rows in Excel: {}", duplicateExcelRows.size());

			// ========================================================
			// Excel DUPLICATE CHECK -> check each pk row value occurs in which row
			// ========================================================
			for (Map.Entry<Long, List<Integer>> entry : pkRowMap.entrySet()) {

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
////				Set<Long> existingInDB = fetchExistingInBatch(validPKSet);
//				Set<Long> existingInDB = fetchExistingInBatch(validPKSet, cycleFromDate, cycleToDate);
//
//				logger.info("Total Pk found in DB: {}", existingInDB.size());
//
//				for (Long pk : existingInDB) {
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

//			Set<Integer> rowsToSkip = new HashSet<>(errorReasonMap.keySet());
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

//					logger.info("Row {} marked Invalid. reason : {}",r,errorReasonMap.get(r));

				} else {

					validRowsList.add(row);

//					logger.info("Row {} marked VALID.",r);
				}
			}

			logger.info("Valid row count : {}", validRowsList.size());
			logger.info("Invalid row count: {}", invalidRowsList.size());

			// ========================================================
			// Upload Id Genrate
			// ========================================================

			// Generate unique Upload Id
			String uploadId = generateUploadId(file.getOriginalFilename());
			String originalFilename = file.getOriginalFilename();

			// ========================================================
			// PARALLEL VALID ENTITY CREATION
			// ========================================================

			long entityStartTime = System.currentTimeMillis();
			logger.info("======= Valid Entity cration Started ============");
			logger.info("Total Valid Rows for processing: {}", validRowsList.size());

			List<FinnoneDump> entityList = validRowsList.parallelStream().map(row -> {

				try {
					FinnoneDump entity = mapRowToEntity(row, columnFieldMap, formatter);

					// Audit fields
					entity.setCycleFromDate(cycleFromDate);
					entity.setCycleToDate(cycleToDate);
					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());
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

			List<FinnoneDumpError> errorEntityList = invalidRowsList.parallelStream().map(row -> {

				String reason = errorReasonMap.get(row.getRowNum());

				try {
					FinnoneDumpError entity = mapToErrorEntity(row, errorColumnFieldMap, formatter);

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

//			batchInsertMainTable(entityList, cycleFromDate, cycleToDate);
//			batchInsertError(errorEntityList);

			process(entityList, errorEntityList, cycleFromDate, cycleToDate, uploadId);

			Map<String, Integer> counts = getCounts(uploadId, originalFilename);

			String message = "Upload Completed Successfully " + "\n" + "Total Records in file : "
					+ (entityList.size() + rowsToSkip.size()) + "\n" + "Count of added records : "
					+ counts.get("insertCount") + "\n" + "Count of error records : " + counts.get("errorCount");

//			String message = "Upload Completed Successfully";

			logger.info(message);

			logger.info("Total time taken: {} ms", (System.currentTimeMillis() - startTime));
			logger.info("========== GST UPLOAD COMPLETED ==========");

			return message;

		} catch (Exception e2) {
//			throw new RuntimeException("Model Upload Failed: " + e2.getMessage(), e2);
			throw new RuntimeException("FinnoneDump Upload Failed: " + e2.getMessage());
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
//		return header == null ? null
////				: header.trim().replace("-", "_").replace("&", "_").replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "_")
////						.replaceAll("_+", "_").replaceAll("^_|_$", "_").toUpperCase();
//				: header.trim().replaceAll("[^A-Za-z0-9_ ]", "") // ❗ keep SPACE
//						// and _
//						.replaceAll("\\s+", " ") // normalize multiple spaces → single
//// space
//						.toUpperCase();
		if (header == null)
			return null;

		String headerValue = header.trim().replaceAll("[^A-Za-z0-9_ ]", "") // ❗ keep SPACE
				// and _
				.replaceAll("\\s+", " ") // normalize multiple spaces → single
// space
				.toUpperCase();

		System.out.println(headerValue);
		return headerValue;

	}

	// ============================================================
	// FETCH EXISTING PK IN BATCH
	// ============================================================
	@Transactional
	private Set<Long> fetchExistingInBatch(Set<Long> uploadedPKSet, Date cycleFromDate, Date cycleToDate) {

		if (uploadedPKSet == null || uploadedPKSet.isEmpty())
			return Collections.emptySet();

		List<Long> pkList = new ArrayList<>(uploadedPKSet);
		Set<Long> existingRecords = new HashSet<>();

		int batchSize = 900;

		for (int start = 0; start < pkList.size(); start += batchSize) {

			int end = Math.min(start + batchSize, pkList.size());

			List<Long> batch = pkList.subList(start, end);

			List<Long> result = entityManager.createQuery("SELECT f.agreementId FROM FinnoneDump f "
					+ "WHERE f.agreementId IN :list AND f.cycleFromDate = :fromDate AND f.cycleToDate = :toDate",
					Long.class).setParameter("list", batch)

					.setParameter("fromDate", cycleFromDate, javax.persistence.TemporalType.DATE)
					.setParameter("toDate", cycleToDate, javax.persistence.TemporalType.DATE)
//					.setParameter("fromDate", new Timestamp(cycleFromDate.getTime()))
//					.setParameter("list", new Timestamp(cycleToDate.getTime()))
					.setHint("org.hibernate.readOnly", true).getResultList();

			existingRecords.addAll(result);
		}

		return existingRecords;
	}

	// ============================================================
	// MAP ROW TO ENTITY (Using Reflection)
	// ============================================================

	private FinnoneDump mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		FinnoneDump entity = new FinnoneDump();

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

//			logger.info("Field '{}' received null equivalent value '{}'", fieldName, value);

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

//			value = value.replaceAll("[^a-zA-Z0-9 ._\\-+]", " ");		

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

	private FinnoneDumpError mapToErrorEntity(Row row, Map<Integer, Field> errorColumnFieldMap, DataFormatter formatter)
			throws Exception {

		FinnoneDumpError errorEntity = new FinnoneDumpError();
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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_FINNONE_DUMP_UPL_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	// ============================================================
	// Batch Insert GST Temp
	// ============================================================
	@Transactional
	public void batchInsertMainTable(List<FinnoneDump> entityList, String uploadId, Date cycleFromDate,
			Date cycleToDate) {

		if (entityList == null || entityList.isEmpty()) {
			logger.info("No records to insert in TEMP table");
			return;
		}

		logger.info("Upload Id :{}", uploadId);

		logger.info("Starting delete FinnoneDump table");

		// delete existing record for from date and To date
		int update = jdbcTemplate.update("DELETE FROM TM_VHL_FINNONE_DUMP WHERE FROM_CYCLE_DATE = ? AND TO_CYCLE_DATE = ?",
				new Timestamp(cycleFromDate.getTime()), new Timestamp(cycleToDate.getTime()));

		logger.info("{} delete FinnoneDump table complate . ",update);

		logger.info("Starting JDBC batch insert for FinnoneDump table. Total records: {}", entityList.size());
		long start = System.currentTimeMillis();

		String sql = "INSERT INTO TM_VHL_FINNONE_DUMP( "
				+ "AGREEMENT_ID,AGREEMENTNO, AGREEMENTDATE , DISB_DATE, DISBURSALAMOUNT, "
				+ "DISB_AMT, AMTFIN, PRETAXIRR, LESSEEID, TENURE, "
				+ "EMI, FILENO, BRANCHNM, MODELNO, MANUFACTURERDESC, "
				+ "NAME, DEALERNAME, ADVANCEINSTL, PROCESSINGFEE, MFR_SUBVENTION_IN, "
				+ "MFR_SUBVENTION_PAID, DEALER_SUBVENTION, DMA_SUBVENTION, PROMOTIONDESC, MARGINMONEY, "
				+ "ADVANCE_EMI, EMPLOYERNAME, STATUS, MAKE, V_ASSET_CATG, "
				+ "PRODUCTFLAG, BRANCH_CODE, DMABROKERCODE, SCHEMECODE, PROMOTIONSCHEME, "

				+ "EFFRATE, MODELCODE, SUBMODELCODE, GROSS_LTV, "

				+ "NET_LTV, FINALSOURCE, FIRSTSOURCE, CUSTCATG, DMA_SUBVENTION_NOT_DED, "
				+ "EMPTYPE, CFOC, STATE, CHANNELCODE, MANUFACTURERID, "
				+ "EMPLOYERID, INFAVOUROF, CHEQUESTATUS, OSP_CODE, DME_NAME, "
				+ "DUMMY, CUSTOMER_NAME, CHARGE_ID1, CHARGE_DESC1, CHARGE_AMT1, "
				+ "CHARGE_ID2, CHARGE_DESC2, CHARGE_AMT2, CHARGE_ID4, CHARGE_DESC4, "
				+ "CHARGE_AMT4, CHARGE_ID5, CHARGE_DESC5, CHARGE_AMT5, CHARGE_ID6, "
				+ "CHARGE_DESC6, CHARGE_AMT6, CHARGE_ID8, CHARGE_DESC8, CHARGE_AMT8, "
				+ "CHARGE_ID9, CHARGE_DESC9, CHARGE_AMT9, CHARGE_ID10, CHARGE_DESC10, "
				+ "CHARGE_AMT10, CHARGE_ID11, CHARGE_DESC11, CHARGE_AMT11, EMPLOYMENT_TYPE, "
				+ "PSL_FLAG, INSTRUMENT_TYPE, BANK, BANK_BRANCH, CUSTOMER_AC, "
				
				+ "MICR, DEST_BANK_AC_TYPE, PROCESS_SHOP, " + "CREATED_BY, CREATED_DATE, "
				+ "FROM_CYCLE_DATE, TO_CYCLE_DATE, " + "REMARKS, UPLOAD_ID, FILE_NAME ) " 
				+ "VALUES (" + "?,?,?,?,?, "
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?, "
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?, "
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?," + "?,?," + "?,?," + "?, ?, ?) "
				+ "LOG ERRORS INTO TM_VHL_FINNONE_DUMP_ERROR ('" + uploadId + "') " + "REJECT LIMIT UNLIMITED ";

		logger.info("Starting JDBC batch insert query genrated");
		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, entityList, batchSize, (PreparedStatement ps, FinnoneDump entity) -> {

			// 1 AGREEMENT_ID
			ps.setLong(1, entity.getAgreementId());

			// 2 AGREEMENTNO
			ps.setString(2, entity.getAgreementNo());

			// 3 AGREEMENTDATE
			if (entity.getAgreementDate() != null) {
				ps.setTimestamp(3, new Timestamp(entity.getAgreementDate().getTime()));
			} else {
				ps.setNull(3, Types.TIMESTAMP);
			}

			// 4 DISB_DATE
			if (entity.getDisbDate() != null) {
				ps.setTimestamp(4, new Timestamp(entity.getDisbDate().getTime()));
			} else {
				ps.setNull(4, Types.TIMESTAMP);
			}

			// 5 DISBURSALAMOUNT
			if (entity.getDisbursalAmount() != null) {
				ps.setBigDecimal(5, entity.getDisbursalAmount());
			} else {
				ps.setNull(5, Types.NUMERIC);
			}

			// 6 DISB_AMT
			if (entity.getDisbAmt() != null) {
				ps.setBigDecimal(6, entity.getDisbAmt());
			} else {
				ps.setNull(6, Types.NUMERIC);
			}

			// 7 AMTFIN
			if (entity.getAmtFin() != null) {
				ps.setBigDecimal(7, entity.getAmtFin());
			} else {
				ps.setNull(7, Types.NUMERIC);
			}

			// 8 PRETAXIRR
			if (entity.getPreTaxIrr() != null) {
				ps.setBigDecimal(8, entity.getPreTaxIrr());
			} else {
				ps.setNull(8, Types.NUMERIC);
			}

			// 9 LESSEEID
			if (entity.getLesseeId() != null) {
				ps.setBigDecimal(9, entity.getLesseeId());
			} else {
				ps.setNull(9, Types.NUMERIC);
			}

			// 10 TENURE
			if (entity.getTenure() != null) {
				ps.setBigDecimal(10, entity.getTenure());
			} else {
				ps.setNull(10, Types.NUMERIC);
			}

			// 11 EMI
			if (entity.getEmi() != null) {
				ps.setBigDecimal(11, entity.getEmi());
			} else {
				ps.setNull(11, Types.NUMERIC);
			}

			// 12 FILENO
			ps.setString(12, entity.getFileNo());

			// 13 BRANCHNM
			ps.setString(13, entity.getBranchNm());

			// 14 MODELNO
			ps.setString(14, entity.getModelNo());

			// 15 MANUFACTURERDESC
			ps.setString(15, entity.getManufacturerDesc());

			// 16 NAME
			ps.setString(16, entity.getName());

			// 17 DEALERNAME
			ps.setString(17, entity.getDealerName());

			// 18 ADVANCEINSTL
			if (entity.getAdvanceInstl() != null) {
				ps.setBigDecimal(18, entity.getAdvanceInstl());
			} else {
				ps.setNull(18, Types.NUMERIC);
			}

			// 19 PROCESSINGFEE
			if (entity.getProcessingFee() != null) {
				ps.setBigDecimal(19, entity.getProcessingFee());
			} else {
				ps.setNull(19, Types.NUMERIC);
			}

			// 20 MFR_SUBVENTION_IN
			if (entity.getMfrSubventionIn() != null) {
				ps.setBigDecimal(20, entity.getMfrSubventionIn());
			} else {
				ps.setNull(20, Types.NUMERIC);
			}

			// 21 MFR_SUBVENTION_PAID 
			if (entity.getMfrSubventionPaid() != null) {
				ps.setBigDecimal(21, entity.getMfrSubventionPaid());
			} else {
				ps.setNull(21, Types.NUMERIC);
			}
			
			

			// 22 DEALER_SUBVENTION
			if (entity.getDealerSubvention() != null) {
				ps.setBigDecimal(22, entity.getDealerSubvention());
			} else {
				ps.setNull(22, Types.NUMERIC);
			}

			// 23 DMA_SUBVENTION
			if (entity.getDmaSubvention() != null) {
				ps.setBigDecimal(23, entity.getDmaSubvention());
			} else {
				ps.setNull(23, Types.NUMERIC);
			}

			// 24 PROMOTIONDESC
				ps.setString(24, entity.getPromotionDesc());
			
			// 25 MARGINMONEY
			if (entity.getMarginMoney() != null) {
				ps.setBigDecimal(25, entity.getMarginMoney());
			} else {
				ps.setNull(25, Types.NUMERIC);
			}

			// 26 ADVANCE_EMI
			if (entity.getAdvanceEmi() != null) {
				ps.setBigDecimal(26, entity.getAdvanceEmi());
			} else {
				ps.setNull(26, Types.NUMERIC);
			}

			// 27 EMPLOYERNAME
			ps.setString(27, entity.getEmployerName());

			// 28 STATUS
			ps.setString(28, entity.getStatus());

			// 29 MAKE
			ps.setString(29, entity.getMake());

			// 30 V_ASSET_CATEG
			ps.setString(30, entity.getvAssetCatg());

			// 31 PRODUCTFLAG
			ps.setString(31, entity.getProductFlag());

			// 32 BRANCH_CODE
			ps.setString(32, entity.getBranchCode());

			// 33 DMABROKERCODE
			if (entity.getDmaBrokerCode() != null) {
				ps.setBigDecimal(33, entity.getDmaBrokerCode());
			} else {
				ps.setNull(33, Types.NUMERIC);
			}

			// 34 SCHEMECODE
			ps.setString(34, entity.getSchemeCode());

			// 35 PROMOTIONSCHEME
			ps.setString(35, entity.getPromotionScheme());

			// 36 EFFRATE
			if (entity.getEffRate() != null) {
				ps.setBigDecimal(36, entity.getEffRate());
			} else {
				ps.setNull(36, Types.NUMERIC);
			}

			// 37 MODELCODE
			if (entity.getModelCode() != null) {
				ps.setBigDecimal(37, entity.getModelCode());
			} else {
				ps.setNull(37, Types.NUMERIC);
			}

			// 38 SUBMODELCODE
			if (entity.getSubModelCode() != null) {
				ps.setBigDecimal(38, entity.getSubModelCode());
			} else {
				ps.setNull(38, Types.NUMERIC);
			}

			// 39 GROSS_LTV
			if (entity.getGrossLtv() != null) {
				ps.setBigDecimal(39, entity.getGrossLtv());
			} else {
				ps.setNull(39, Types.NUMERIC);
			}

			// 40 NET_LTV
			if (entity.getNetLtv() != null) {
				ps.setBigDecimal(40, entity.getNetLtv());
			} else {
				ps.setNull(40, Types.NUMERIC);
			}

			// 41 FINALSOURCE
			ps.setString(41, entity.getFinalSource());

			// 42 FIRSTSOURCE
			ps.setString(42, entity.getFirstSource());

			// 43 CUSTCATG
			ps.setString(43, entity.getCustCatg());

			// 44 DMA_SUBVENTION_NOT_DED
			if (entity.getDmaSubventionNot() != null) {
				ps.setBigDecimal(44, entity.getDmaSubventionNot());
			} else {
				ps.setNull(44, Types.NUMERIC);
			}

			// 45 EMPTYPE
			ps.setString(45, entity.getEmpType());

			// 46 CFOC
			if (entity.getCfoc() != null) {
				ps.setBigDecimal(46, entity.getCfoc());
			} else {
				ps.setNull(46, Types.NUMERIC);
			}

			// 47 STATE
			ps.setString(47, entity.getState());

			// 48 CHANNELCODE
			ps.setString(48, entity.getChannelCode());

			// 49 MANUFACTURERID
			if (entity.getManufacturerId() != null) {
				ps.setBigDecimal(49, entity.getManufacturerId());
			} else {
				ps.setNull(49, Types.NUMERIC);
			}

			// 50 EMPLOYERID
			if (entity.getEmployerId() != null) {
				ps.setBigDecimal(50, entity.getEmployerId());
			} else {
				ps.setNull(50, Types.NUMERIC);
			}

			// 51 INFAVOUROF
			ps.setString(51, entity.getInFavourOf());

			// 52 CHEQUESTATUS
			ps.setString(52, entity.getChequeStatus());

			// 53 OSP_CODE
			ps.setString(53, entity.getOspCode());

			// 54 DME_NAME
			ps.setString(54, entity.getDmeName());

			// 55 DUMMY
			ps.setString(55, entity.getDummy());

			// 56 CUSTOMER_NAME
			ps.setString(56, entity.getCustomerName());

			// 57 CHARGE_ID1
			if (entity.getChargeId1() != null) {
				ps.setBigDecimal(57, entity.getChargeId1());
			} else {
				ps.setNull(57, Types.NUMERIC);
			}

			// 58 CHARGE_DESC1
			ps.setString(58, entity.getChargeDesc1());

			// 59 CHARGE_AMT1
			if (entity.getChargeAmt1() != null) {
				ps.setBigDecimal(59, entity.getChargeAmt1());
			} else {
				ps.setNull(59, Types.NUMERIC);
			}

			// 60 CHARGE_ID2
			if (entity.getChargeId2() != null) {
				ps.setBigDecimal(60, entity.getChargeId2());
			} else {
				ps.setNull(60, Types.NUMERIC);
			}

			// 61 CHARGE_DESC2
			ps.setString(61, entity.getChargeDesc2());

			// 62 CHARGE_AMT2
			if (entity.getChargeAmt2() != null) {
				ps.setBigDecimal(62, entity.getChargeAmt2());
			} else {
				ps.setNull(62, Types.NUMERIC);
			}

			// 63 CHARGE_ID4
			if (entity.getChargeId4() != null) {
				ps.setBigDecimal(63, entity.getChargeId4());
			} else {
				ps.setNull(63, Types.NUMERIC);
			}

			// 64 CHARGE_DESC4
			ps.setString(64, entity.getChargeDesc4());

			// 65 CHARGE_AMT4
			if (entity.getChargeAmt4() != null) {
				ps.setBigDecimal(65, entity.getChargeAmt4());
			} else {
				ps.setNull(65, Types.NUMERIC);
			}

			// 66 CHARGE_ID5
			if (entity.getChargeId5() != null) {
				ps.setBigDecimal(66, entity.getChargeId5());
			} else {
				ps.setNull(66, Types.NUMERIC);
			}

			// 67 CHARGE_DESC5
			ps.setString(67, entity.getChargeDesc5());

			// 68 CHARGE_AMT5
			if (entity.getChargeAmt5() != null) {
				ps.setBigDecimal(68, entity.getChargeAmt5());
			} else {
				ps.setNull(68, Types.NUMERIC);
			}

			// 69 CHARGE_ID6
			if (entity.getChargeId6() != null) {
				ps.setBigDecimal(69, entity.getChargeId6());
			} else {
				ps.setNull(69, Types.NUMERIC);
			}

			// 70 CHARGE_DESC6
			ps.setString(70, entity.getChargeDesc6());

			// 71 CHARGE_AMT6
			if (entity.getChargeAmt6() != null) {
				ps.setBigDecimal(71, entity.getChargeAmt6());
			} else {
				ps.setNull(71, Types.NUMERIC);
			}

			// 72 CHARGE_ID8
			if (entity.getChargeId8() != null) {
				ps.setBigDecimal(72, entity.getChargeId8());
			} else {
				ps.setNull(72, Types.NUMERIC);
			}

			// 73 CHARGE_DESC8
			ps.setString(73, entity.getChargeDesc8());

			// 74 CHARGE_AMT8
			if (entity.getChargeAmt8() != null) {
				ps.setBigDecimal(74, entity.getChargeAmt8());
			} else {
				ps.setNull(74, Types.NUMERIC);
			}

			// 75 CHARGE_ID9
			if (entity.getChargeId9() != null) {
				ps.setBigDecimal(75, entity.getChargeId9());
			} else {
				ps.setNull(75, Types.NUMERIC);
			}

			// 76 CHARGE_DESC9
			ps.setString(76, entity.getChargeDesc9());

			// 77 CHARGE_AMT9
			if (entity.getChargeAmt9() != null) {
				ps.setBigDecimal(77, entity.getChargeAmt9());
			} else {
				ps.setNull(77, Types.NUMERIC);
			}

			// 78 CHARGE_ID10
			if (entity.getChargeId10() != null) {
				ps.setBigDecimal(78, entity.getChargeId10());
			} else {
				ps.setNull(78, Types.NUMERIC);
			}

			// 79 CHARGE_DESC10
			ps.setString(79, entity.getChargeDesc10());

			// 80 CHARGE_AMT10
			if (entity.getChargeAmt10() != null) {
				ps.setBigDecimal(80, entity.getChargeAmt10());
			} else {
				ps.setNull(80, Types.NUMERIC);
			}

			// 81 CHARGE_ID11
			if (entity.getChargeId11() != null) {
				ps.setBigDecimal(81, entity.getChargeId11());
			} else {
				ps.setNull(81, Types.NUMERIC);
			}

			// 82 CHARGE_DESC11
			ps.setString(82, entity.getChargeDesc11());

			// 83 CHARGE_AMT11
			if (entity.getChargeAmt11() != null) {
				ps.setBigDecimal(83, entity.getChargeAmt11());
			} else {
				ps.setNull(83, Types.NUMERIC);
			}

			// 84 EMPLOYMENT_TYPE
			ps.setString(84, entity.getEmploymentType());

			// 85 PSL_FLAG
			ps.setString(85, entity.getPslFlag());

			// 86 INSTRUMENT_TYPE
			ps.setString(86, entity.getInstrumentType());

			// 87 BANK
			ps.setString(87, entity.getBank());

			// 88 BANK_BRANCH
			ps.setString(88, entity.getBankBranch());

			// 89 CUSTOMER_AC
			if (entity.getCustomerAc() != null) {
				ps.setBigDecimal(89, entity.getCustomerAc());
			} else {
				ps.setNull(89, Types.NUMERIC);
			}

			// 90 MICR
			if (entity.getMicr() != null) {
				ps.setBigDecimal(90, entity.getMicr());
			} else {
				ps.setNull(90, Types.NUMERIC);
			}

			// 91 DEST_BANK_AC_TYPE
			ps.setString(91, entity.getDestBankAcType());

			// 92 PROCESS_SHOP
			ps.setString(92, entity.getProcessShop());

			// 93 CREATED_BY
			ps.setString(93, entity.getCreatedBy());

			// 94 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(94, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(94, Types.TIMESTAMP);
			}

			// 95 FROM_CYCLE_DATE
			if (entity.getCycleFromDate() != null) {
				Timestamp timestamp = new Timestamp(entity.getCycleFromDate().getTime());
				System.out.println(timestamp.toString());
				ps.setTimestamp(95, new Timestamp(entity.getCycleFromDate().getTime()));
			} else {
				ps.setNull(95, Types.TIMESTAMP);
			}

			// 96 TO_CYCLE_DATE
			if (entity.getCycleToDate() != null) {
				ps.setTimestamp(96, new Timestamp(entity.getCycleToDate().getTime()));
			} else {
				ps.setNull(96, Types.TIMESTAMP);
			}

			// 97 REMARKS
			ps.setString(97, entity.getRemarks());

			// 98 UPLOAD_ID
			ps.setString(98, entity.getUploadId());

			// 99 FILE_NAME
			ps.setString(99, entity.getFileName());
		});

//		repository.saveAll(entityList);
		logger.info("Finnone Dump insert completed in {} ms", (System.currentTimeMillis() - start));
	}

	// ============================================================
	// Batch Insert GST Error
	// ============================================================
	@Transactional
	public void batchInsertError(List<FinnoneDumpError> errorList) {

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records to insert.");
			return;
		}

		logger.info("Starting JDBC batch insert for FinnoneDump ERROR table. Total records: {}", errorList.size());

		long startTime = System.currentTimeMillis();

		String sql = "INSERT INTO tm_vhl_finnone_dump_error( "

				+ "AGREEMENT_ID,AGREEMENTNO, AGREEMENTDATE , DISB_DATE, DISBURSALAMOUNT, "
				+ "DISB_AMT, AMTFIN, PRETAXIRR, LESSEEID, TENURE, "
				+ "EMI, FILENO, BRANCHNM, MODELNO, MANUFACTURERDESC, "
				+ "NAME, DEALERNAME, ADVANCEINSTL, PROCESSINGFEE, MFR_SUBVENTION_IN, "
				+ "MFR_SUBVENTION_PAID, DEALER_SUBVENTION, DMA_SUBVENTION, PROMOTIONDESC, MARGINMONEY, "
				+ "ADVANCE_EMI, EMPLOYERNAME, STATUS, MAKE, V_ASSET_CATG, "
				+ "PRODUCTFLAG, BRANCH_CODE, DMABROKERCODE, SCHEMECODE, PROMOTIONSCHEME, "

				+ "EFFRATE, MODELCODE, SUBMODELCODE, GROSS_LTV, "

				+ "NET_LTV, FINALSOURCE, FIRSTSOURCE, CUSTCATG, DMA_SUBVENTION_NOT_DED, "
				+ "EMPTYPE, CFOC, STATE, CHANNELCODE, MANUFACTURERID, "
				+ "EMPLOYERID, INFAVOUROF, CHEQUESTATUS, OSP_CODE, DME_NAME, "
				+ "DUMMY, CUSTOMER_NAME, CHARGE_ID1, CHARGE_DESC1, CHARGE_AMT1, "
				+ "CHARGE_ID2, CHARGE_DESC2, CHARGE_AMT2, CHARGE_ID4, CHARGE_DESC4, "
				+ "CHARGE_AMT4, CHARGE_ID5, CHARGE_DESC5, CHARGE_AMT5, CHARGE_ID6, "
				+ "CHARGE_DESC6, CHARGE_AMT6, CHARGE_ID8, CHARGE_DESC8, CHARGE_AMT8, "
				+ "CHARGE_ID9, CHARGE_DESC9, CHARGE_AMT9, CHARGE_ID10, CHARGE_DESC10, "
				+ "CHARGE_AMT10, CHARGE_ID11, CHARGE_DESC11, CHARGE_AMT11, EMPLOYMENT_TYPE, "
				+ "PSL_FLAG, INSTRUMENT_TYPE, BANK, BANK_BRANCH, CUSTOMER_AC, "

				+ "MICR, DEST_BANK_AC_TYPE, PROCESS_SHOP, " + "CREATED_BY, CREATED_DATE, " + "ERROR_MSG, ROW_NUMBER, "
				+ "UPLOAD_ID, REMARKS ) " + "VALUES (" + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
				+ "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?,"
				+ "?,?,?," + "?,?," + "?,?," + "?,?)";

		int batchSize = 500;

		jdbcTemplate.batchUpdate(sql, errorList, batchSize, (PreparedStatement ps, FinnoneDumpError entity) -> {

			// 1 AGREEMENT_ID
			ps.setString(1, entity.getAgreementId());

			// 2 AGREEMENTNO
			ps.setString(2, entity.getAgreementNo());

			// 3 AGREEMENTDATE
			ps.setString(3, entity.getAgreementDate());

			// 4 DISB_DATE
			ps.setString(4, entity.getDisbDate());

			// 5 DISBURSALAMOUNT
			ps.setString(5, entity.getDisbursalAmount());

			// 6 DISB_AMT
			ps.setString(6, entity.getDisbAmt());

			// 7 AMTFIN
			ps.setString(7, entity.getAmtFin());

			// 8 PRETAXIRR
			ps.setString(8, entity.getPreTaxIrr());

			// 9 LESSEEID
			ps.setString(9, entity.getLesseeId());

			// 10 TENURE
			ps.setString(10, entity.getTenure());

			// 11 EMI
			ps.setString(11, entity.getEmi());

			// 12 FILENO
			ps.setString(12, entity.getFileNo());

			// 13 BRANCHNM
			ps.setString(13, entity.getBranchNm());

			// 14 MODELNO
			ps.setString(14, entity.getModelNo());

			// 15 MANUFACTURERDESC
			ps.setString(15, entity.getManufacturerDesc());

			// 16 NAME
			ps.setString(16, entity.getName());

			// 17 DEALERNAME
			ps.setString(17, entity.getDealerName());

			// 18 ADVANCEINSTL
			ps.setString(18, entity.getAdvanceInstl());

			// 19 PROCESSINGFEE
			ps.setString(19, entity.getProcessingFee());

			// 20 MFR_SUBVENTION_IN
			ps.setString(20, entity.getMfrSubventionIn());

			// 21 DEALER_SUBVENTION
			ps.setString(21, entity.getDealerSubvention());

			// 22 MFR_SUBVENTION_PAID
			ps.setString(22, entity.getMfrSubventionPaid());

			// 23 DMA_SUBVENTION
			ps.setString(23, entity.getDmaSubvention());

			// 24 PROMOTIONDESC
			ps.setString(24, entity.getPromotionDesc());

			// 25 MARGINMONEY
			ps.setString(25, entity.getMarginMoney());

			// 26 ADVANCE_EMI
			ps.setString(26, entity.getAdvanceEmi());

			// 27 EMPLOYERNAME
			ps.setString(27, entity.getEmployerName());

			// 28 STATUS
			ps.setString(28, entity.getStatus());

			// 29 MAKE
			ps.setString(29, entity.getMake());

			// 30 V_ASSET_CATEG
			ps.setString(30, entity.getvAssetCatg());

			// 31 PRODUCTFLAG
			ps.setString(31, entity.getProductFlag());

			// 32 BRANCH_CODE
			ps.setString(32, entity.getBranchCode());

			// 33 DMABROKERCODE
			ps.setString(33, entity.getDmaBrokerCode());

			// 34 SCHEMECODE
			ps.setString(34, entity.getSchemeCode());

			// 35 PROMOTIONSCHEME
			ps.setString(35, entity.getPromotionScheme());

			// 36 EFFRATE
			ps.setString(36, entity.getEffRate());

			// 37 MODELCODE
			ps.setString(37, entity.getModelCode());

			// 38 SUBMODELCODE
			ps.setString(38, entity.getSubModelCode());

			// 39 GROSS_LTV
			ps.setString(39, entity.getGrossLtv());

			// 40 NET_LTV
			ps.setString(40, entity.getNetLtv());

			// 41 FINALSOURCE
			ps.setString(41, entity.getFinalSource());

			// 42 FIRSTSOURCE
			ps.setString(42, entity.getFirstSource());

			// 43 CUSTCATG
			ps.setString(43, entity.getCustCatg());

			// 44 DMA_SUBVENTION_NOT_DED
			ps.setString(44, entity.getDmaSubventionNotDed());

			// 45 EMPTYPE
			ps.setString(45, entity.getEmpType());

			// 46 CFOC
			ps.setString(46, entity.getCfoc());

			// 47 STATE
			ps.setString(47, entity.getState());

			// 48 CHANNELCODE
			ps.setString(48, entity.getChannelCode());

			// 49 MANUFACTURERID
			ps.setString(49, entity.getManufacturerId());

			// 50 EMPLOYERID
			ps.setString(50, entity.getEmployerId());

			// 51 INFAVOUROF
			ps.setString(51, entity.getInFavourOf());

			// 52 CHEQUESTATUS
			ps.setString(52, entity.getChequeStatus());

			// 53 OSP_CODE
			ps.setString(53, entity.getOspCode());

			// 54 DME_NAME
			ps.setString(54, entity.getDmeName());

			// 55 DUMMY
			ps.setString(55, entity.getDummy());

			// 56 CUSTOMER_NAME
			ps.setString(56, entity.getCustomerName());

			// 57 CHARGE_ID1
			ps.setString(57, entity.getChargeId1());

			// 58 CHARGE_DESC1
			ps.setString(58, entity.getChargeDesc1());

			// 59 CHARGE_AMT1
			ps.setString(59, entity.getChargeAmt1());

			// 60 CHARGE_ID2
			ps.setString(60, entity.getChargeId2());

			// 61 CHARGE_DESC2
			ps.setString(61, entity.getChargeDesc2());

			// 62 CHARGE_AMT2
			ps.setString(62, entity.getChargeAmt2());

			// 63 CHARGE_ID4
			ps.setString(63, entity.getChargeId4());

			// 64 CHARGE_DESC4
			ps.setString(64, entity.getChargeDesc4());

			// 65 CHARGE_AMT4
			ps.setString(65, entity.getChargeAmt4());

			// 66 CHARGE_ID5
			ps.setString(66, entity.getChargeId5());

			// 67 CHARGE_DESC5
			ps.setString(67, entity.getChargeDesc5());

			// 68 CHARGE_AMT5
			ps.setString(68, entity.getChargeAmt5());

			// 69 CHARGE_ID6
			ps.setString(69, entity.getChargeId6());

			// 70 CHARGE_DESC6
			ps.setString(70, entity.getChargeDesc6());

			// 71 CHARGE_AMT6
			ps.setString(71, entity.getChargeAmt6());

			// 72 CHARGE_ID8
			ps.setString(72, entity.getChargeId8());

			// 73 CHARGE_DESC8
			ps.setString(73, entity.getChargeDesc8());

			// 74 CHARGE_AMT8
			ps.setString(74, entity.getChargeAmt8());

			// 75 CHARGE_ID9
			ps.setString(75, entity.getChargeId9());

			// 76 CHARGE_DESC9
			ps.setString(76, entity.getChargeDesc9());

			// 77 CHARGE_AMT9
			ps.setString(77, entity.getChargeAmt9());

			// 78 CHARGE_ID10
			ps.setString(78, entity.getChargeId10());

			// 79 CHARGE_DESC10
			ps.setString(79, entity.getChargeDesc10());

			// 80 CHARGE_AMT10
			ps.setString(80, entity.getChargeAmt10());

			// 81 CHARGE_ID11
			ps.setString(81, entity.getChargeId11());

			// 82 CHARGE_DESC11
			ps.setString(82, entity.getChargeDesc11());

			// 83 CHARGE_AMT11
			ps.setString(83, entity.getChargeAmt11());

			// 84 EMPLOYMENT_TYPE
			ps.setString(84, entity.getEmploymentType());

			// 85 pslFlag
			ps.setString(85, entity.getPslFlag());

			// 86 INSTRUMENT_TYPE
			ps.setString(86, entity.getInstrumentType());

			// 87 BANK
			ps.setString(87, entity.getBank());

			// 88 BANK_BRANCH
			ps.setString(88, entity.getBankBranch());

			// 89 CUSTOMER_AC
			ps.setString(89, entity.getCustomerAc());

			// 90 MICR
			ps.setString(90, entity.getMicr());

			// 91 DEST_BANK_AC_TYPE
			ps.setString(91, entity.getDestBankAcType());

			// 92 PROCESS_SHOP
			ps.setString(92, entity.getProcessShop());

			// 93 CREATED_BY
			ps.setString(93, entity.getCreatedBy());

			// 94 CREATED_DATE
			if (entity.getCreatedDate() != null) {
				ps.setTimestamp(94, new Timestamp(entity.getCreatedDate().getTime()));
			} else {
				ps.setNull(94, Types.TIMESTAMP);
			}

			// 95 ERROR_MSG
			ps.setString(95, entity.getErrorMsg());

			// 96 ROW_NUMBER
			ps.setInt(96, entity.getRowNumber());

			// 97 UPLOAD_ID
			ps.setString(97, entity.getUploadId());

			// 98 REMARKS
			ps.setString(98, entity.getRemarks());

//
//			// 99 FROM_CYCLE_DATE
//			ps.setDate(99, (java.sql.Date) entity.getCycleFromDate());
//						
//			// 100 TO_CYCLE_DATE
//			ps.setDate(100, (java.sql.Date) entity.getCycleToDate());

		});

//		FinnoneDumprepo.saveAll(errorList);
		logger.info("Finnone Dump ERROR batch insert completed. Inserted {} records in {} ms", errorList.size(),
				(System.currentTimeMillis() - startTime));
	}

	@Override
	public byte[] exportToExcel(Date cycleFromDate, Date cycleToDate) {
		logger.info("Export the service::: " + cycleFromDate + "cycleToDate " + cycleToDate);

		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(cycleFromDate);

		Calendar calTo = Calendar.getInstance();
		calTo.setTime(cycleToDate);

		Calendar cal = Calendar.getInstance();
		cal.setTime(cycleToDate);
		cal.add(Calendar.DATE, 1);
		Date nextDate = cal.getTime();
		logger.info("cycleFromDate::: " + cycleFromDate + "cycleTODate:::: " + nextDate);

		/*
		 * ==== Dump record Validation =====
		 */
		try {

			logger.info("Dump record avalability Vadidation Start ");

			long ALDDcount = alddrepository.AlddcountByDateRange(cycleFromDate, nextDate);// countByCycleFromDateBetween(cycleFromDate,
																							// nextDate);
			long ilenscount = ilensrepository.ilnescountByDateRange(cycleFromDate, nextDate);

			long finncount = repository.countByStatusAndCycleFromDateBetween("A", cycleFromDate, nextDate);

			logger.info("FinnoneCount :::  " + finncount + " ,ALDDcount::: " + ALDDcount + ", ilenscount:::: "
					+ ilenscount);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			String fromDateA = sdf.format(cycleFromDate);
			String toDateA = sdf.format(cycleToDate);

			if (ALDDcount == 0 && ilenscount == 0 && finncount == 0) {
				throw new RuntimeException(String.format(
						"No records found in any source dump (ALDD Transaction, iLENS and FinnOne) for the selected date range [%s - %s]. Please verify that the required dump files have been uploaded.",
						fromDateA, toDateA));
			}

			if (ALDDcount == 0) {
				throw new RuntimeException(String.format(
						"No records found in the ALDD Transaction dump for the selected date range [%s - %s].",
						fromDateA, toDateA));
			}

			if (ilenscount == 0) {
				throw new RuntimeException(
						String.format("No records found in the iLENS dump for the selected date range [%s - %s].",
								fromDateA, toDateA));
			}

			if (finncount == 0) {
				throw new RuntimeException(
						String.format("No records found in the FinnOne dump for the selected date range [%s - %s].",
								fromDateA, toDateA));
			}

			logger.info("Dump record avalability Vadidation End ");

			/*
			 * ===== Master record validation =======
			 */

			logger.info("Master record avalability Vadidation Start ");

			// branch master
			if (!branchMasterRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the Branch Master. Please upload and approve the Branch Master before generating the Tagging File.");
			}

			if (branchMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"Branch Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// Channel master
			if (!channelMasterRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the Channel Master. Please upload and approve the Channel Master before generating the Tagging File.");
			}

			if (channnelMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"Channel Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// GST MF master
			if (!gstMfMasterMainRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the GST MF Master. Please upload and approve the GST MF Master before generating the Tagging File.");
			}

			if (gstMfMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"GST MF Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// GST State master
			if (!gSTStateMasterMainRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the GST State Master. Please upload and approve the GST State Master before generating the Tagging File.");
			}

			if (gSTStateMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"GST State Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// GST To master
			if (!gstToMasterMainRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the GST To Master. Please upload and approve the GST To Master before generating the Tagging File.");
			}

			if (gstToMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"GST To Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// Ilens Channel master
			if (!iLensChannelMasterMainRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the Ilens Channel Master. Please upload and approve the Ilens Channel Master before generating the Tagging File.");
			}

			if (iLensChannelMasterTempRepo.existsByStatusA("P")) {
				throw new RuntimeException(
						"Ilens Channel Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// Model master
			if (!modelMasterRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the Model Master. Please upload and approve the Model Master before generating the Tagging File.");
			}

			if (modelMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"Model Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			// Outsource master
			if (!outsourceMasterMainRepo.existsBy()) {
				throw new RuntimeException(
						"No approved records found in the Outsource Master. Please upload and approve the Outsource Master before generating the Tagging File.");
			}

			if (outsourceMasterTempRepo.existsByStatus("P")) {
				throw new RuntimeException(
						"Outsource Master contains pending records. Please approve or reject all pending records before generating the Tagging File.");
			}

			logger.info("Master record avalability Vadidation End ");

			/*
			 * Evaluate All master and dump record for the Tagging file
			 */

			logger.info("Evaluate All master and dump record Into the Tagging Table : Store procedure call ");

			Map<String, Object> result = jdbcTemplate.execute((Connection con) -> {
				CallableStatement cs = con.prepareCall("{call SP_VHL_TAGGING_FILE(?, ?, ?, ?)}");

				// IN Parameters
				cs.setDate(1, new java.sql.Date(cycleFromDate.getTime()));
				cs.setDate(2, new java.sql.Date(cycleToDate.getTime()));

				// OUT Parameters
				cs.registerOutParameter(3, Types.VARCHAR);
				cs.registerOutParameter(4, Types.VARCHAR);

				return cs;
			}, (CallableStatementCallback<Map<String, Object>>) cs -> {

				cs.execute();

				Map<String, Object> output = new HashMap<>();
				output.put("STATUS_FLAG", cs.getString(3));
				output.put("MESSAGE", cs.getString(4));

				return output;
			});

			String statusFlag = (String) result.get("STATUS_FLAG");
			String message = (String) result.get("MESSAGE");

			if (!"Y".equalsIgnoreCase(statusFlag)) {
				throw new RuntimeException(message);
			}

			logger.info("Data Mapped to the Tagging main Table : {} ", message);

			logger.info(
					"Evaluate All master and dump record Into the Tagging Table  is complate : Store procedure call End ");

			/*
			 * Generate Tagging File record For Selected date
			 */

			logger.info("Start Generate Tagging File from  Tagging main Table ");

			try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

				Sheet sheet = workbook.createSheet("Finnone Dump");

				// ===== HEADER STYLE =====
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);
				headerFont.setFontName("Mulish");
				headerFont.setFontHeightInPoints((short) 11);

				Font header_Font = workbook.createFont();
				header_Font.setFontName("Mulish");
				header_Font.setFontHeightInPoints((short) 11);

				CellStyle headerStyle = workbook.createCellStyle();
				headerStyle.setFont(headerFont);

				// ===== DATA STYLE =====
				CellStyle dataStyle = workbook.createCellStyle();
				dataStyle.setFont(header_Font);

				Session session = entityManager.unwrap(Session.class);
				session.doWork(connection -> {

//				String sql = "SELECT AGREEMENT_ID, " + "FILENO, " + "AGREEMENTNO, " + "PRODUCT_CLASSIFICATION, "
//						+ "CIBIL_SCORE_ENTITY_CLASSIFICATION, " + "DISB_DATE, " + "AMTFIN, " + "AMT_FIN, "
//						+ "PRETAXIRR, " + "IRR_ROUNDUP, " + "TENURE, " + "EMI, " + "BRANCHNM, " + "MODELNO, "
//						+ "MANUFACTURERDESC, " + "NAME, " + "OLD_NAME, " + "DEALERNAME, " + "PROCESSINGFEE, "
//						+ "PROMOTIONDESC, " + "STATUS, " + "MAKE, " + "SEGMENT, " + "DONE, " + "DMABROKERCODE, "
//						+ "OLD_DMA_BROKER_CODE, " + "SCHEMECODE, " + "STATE, " + "TOP_TIERII_TIERIII, "
//						+ "BRANCH_CODE, " + "HUB, " + "ZONE, " + "AL_STATE, " + "CONSOLIDATED_STATE_PO_PROCESSING, "
//						+ "MIS_STATE, " + "ED_STATE, " + "ED_ZONE, " + "I_BOX_ID, " + "GST_STATE_FROM, "
//						+ "GST_STATE_TO, " + "PROCESS_SHOP, " + "CBC_CODE, " + "CHANNELCODE, " + "DME_NAME, "
//						+ "PRODUCT, " + "VARIANT, " + "CORRECT_OSP_CODE, " + "DME_NAME_1, " + "CUSTOMER_NAME, "
//						+ "PO_RATE, " + "REMARK, " + "BOOSTER_CONDITION, " + "QRTY, " + "SOURCING, " + "SOURCING_2, "
//						+ "CHANNEL_ACTIVITY, " + "RETAINER_MAPPING, " + "ASSET_INSURANCE_CHARGE_ID_500080, "
//						+ "ASSET_INSURANCE_PO, " + "SALARIED_NON_SALARIED, " + "SELF_EMPLOYED, " + "SE_TO_BE_RELEASED, "
//						+ "PSL_FLAG, " + "AUTO_DEBIT, " + "USED_TYPES, " + "ADD_REMARKS, " + "DEEMED_DEMO, "
//						+ "PROCESS_SHOP_1 "
////						+ "FROM_DATE, "
////						+ "TO_DATE "
//						+ "FROM TR_VHL_TAGGING_MAIN " + "WHERE  FROM_DATE = :cycleFromDate AND TO_DATE =:cycleToDate";

					String sql = "Select *  FROM TR_VHL_TAGGING_MAIN WHERE FROM_DATE = ? AND TO_DATE = ? ";

					try (PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
							ResultSet.CONCUR_READ_ONLY)) {
						ps.setTimestamp(1, new java.sql.Timestamp(cycleFromDate.getTime()));
						ps.setTimestamp(2, new java.sql.Timestamp(cycleToDate.getTime()));

						try (ResultSet rs = ps.executeQuery()) {

							ResultSetMetaData meta = rs.getMetaData();
							int columnCount = meta.getColumnCount();

							// HEADER
							Row headerRow = sheet.createRow(0);

							for (int i = 1; i <= columnCount; i++) {
								Cell cell = headerRow.createCell(i - 1);
								cell.setCellValue(meta.getColumnName(i));
								cell.setCellStyle(headerStyle);
							}

							// DATA
							int rowNum = 1;
							int excelRowNum = rowNum + 1; // for message

							logger.info("Excel Row Num::" + excelRowNum);

							while (rs.next()) {

								Row row = sheet.createRow(rowNum++);
								for (int i = 1; i <= columnCount; i++) {

									Object val = rs.getObject(i);
									String colLabel = meta.getColumnLabel(i);
									String normalizedCol = colLabel == null ? "" : colLabel.trim().toUpperCase();
									Cell cell = row.createCell(i - 1);
									if (val == null) {
										cell.setCellValue("");
										cell.setCellStyle(dataStyle);
									} else if (val instanceof java.sql.Date) {
										LocalDate ld = ((java.sql.Date) val).toLocalDate();
										cell.setCellValue(ld.format(DATE_FMT));
										cell.setCellStyle(dataStyle);
									} else if (val instanceof java.sql.Timestamp) {
										LocalDate ld = ((java.sql.Timestamp) val).toLocalDateTime().toLocalDate();
										cell.setCellValue(ld.format(DATE_FMT));
										cell.setCellStyle(dataStyle);
									} else {
										cell.setCellValue(val.toString());
										cell.setCellStyle(dataStyle);
									}
								}
							}

							for (int i = 0; i < columnCount; i++) {
								sheet.autoSizeColumn(i);
							}
//						}
						}
					}
				});
				session.close();

				logger.info("sheet:: " + sheet);

				workbook.write(out);

				logger.info("Exit in the service");

				return out.toByteArray();

			} catch (RuntimeException e) {
				throw e;

			} catch (Exception e) {

				logger.error("Excel export failed", e);
				throw new RuntimeException("Excel export failed", e);
			}

		} catch (DataAccessException e) {
			logger.info("error : {}", e);
			logger.info("error message: {}", e.getMessage());

			throw new RuntimeException(" Tagging File Excel generation failed");
		}
	}

	@Transactional
	public ByteArrayInputStream exportFinnoneErrorExcel(String user) throws Exception {

		logger.info("Starting error export for User: {}", user);

		// get latest upload Id
		// String uploadId =
		// FinnoneDumpErrorrepo.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

		try {
			List<String> list = null;
			try {
				String sql1 = "SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id FROM TM_VHL_FINNONE_DUMP_ERROR "
						+ "WHERE created_by = ? ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";
//			String uploadId = jdbcTemplate.queryForObject(sql1, String.class, user);

				list = jdbcTemplate.query(sql1, (rs, rowNum) -> rs.getString("final_upload_id"), user);

			} catch (DataAccessException e) {
				logger.info("error : {}", e);
				e.printStackTrace();
				logger.info("error message: {}", e.getMessage());
				throw e;
			}

			String uploadId = list.isEmpty() ? null : list.get(0);

			if (uploadId == null || uploadId.isEmpty()) {
				throw new ResourceNotFoundException("No Error record found");
			}

			logger.info("Starting error export for uploadId: {}", uploadId);
			// ---------------------------------------------------
			// Step 1: Fetch error records from DB
			// ---------------------------------------------------

//		List<BranchMasterError> errors = branchErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

			String sql = "SELECT * " + "FROM TM_VHL_FINNONE_DUMP_ERROR " + "WHERE upload_id = ?"
					+ " OR ora_err_tag$ = ?";

			List<Map<String, Object>> errors = jdbcTemplate.queryForList(sql, uploadId, uploadId);

			if (errors == null || errors.isEmpty()) {

				logger.warn("No error records found for uploadId: {}", uploadId);

				throw new ResourceNotFoundException("No error records found for uploadId: " + uploadId);
			}

			// ---------------------------------------------------
			// Step 2: Create Workbook
			// ---------------------------------------------------

			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet("GST_ERROR");

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

			String[] headerList = { "AGREEMENT_ID", "AGREEMENTNO", "AGREEMENTDATE", "DISB_DATE", "DISBURSALAMOUNT", // 5
					"DISB_AMT", "AMTFIN", "PRETAXIRR", "LESSEEID", "TENURE", "EMI", "FILENO", "BRANCHNM", "MODELNO", // 9
					"MANUFACTURERDESC", "NAME", "DEALERNAME", "ADVANCEINSTL", "PROCESSINGFEE", "MFR_SUBVENTION_IN", // 6
					"MFR_SUBVENTION_PAID", "DEALER_SUBVENTION", "DMA_SUBVENTION", "PROMOTIONDESC", "MARGINMONEY", // 5
					"ADVANCE_EMI", "EMPLOYERNAME", "STATUS", "MAKE", "V_ASSET_CATG", "PRODUCTFLAG", "BRANCH_CODE", // 7
					"DMABROKERCODE", "SCHEMECODE", "PROMOTIONSCHEME", "EFFRATE", "MODELCODE", "SUBMODELCODE",
					"GROSS_LTV", // 7
					"NET_LTV", "FINALSOURCE", "FIRSTSOURCE", "CUSTCATG", "DMA_SUBVENTION_NOT_DED", "EMPTYPE", "CFOC",
					"STATE", // 8
					"CHANNELCODE", "MANUFACTURERID", "EMPLOYERID", "INFAVOUROF", "CHEQUESTATUS", "OSP_CODE", "DME_NAME", // 7
					"DUMMY", "CUSTOMER_NAME", "CHARGE_ID1", "CHARGE_DESC1", "CHARGE_AMT1", "CHARGE_ID2", "CHARGE_DESC2", // 7
					"CHARGE_AMT2", "CHARGE_ID4", "CHARGE_DESC4", "CHARGE_AMT4", "CHARGE_ID5", "CHARGE_DESC5",
					"CHARGE_AMT5", // 7
					"CHARGE_ID6", "CHARGE_DESC6", "CHARGE_AMT6", "CHARGE_ID8", "CHARGE_DESC8", "CHARGE_AMT8",
					"CHARGE_ID9", // 7
					"CHARGE_DESC9", "CHARGE_AMT9", "CHARGE_ID10", "CHARGE_DESC10", "CHARGE_AMT10", "CHARGE_ID11",
					"CHARGE_DESC11", // 7
					"CHARGE_AMT11", "EMPLOYMENT_TYPE", "PSL_FLAG", "INSTRUMENT_TYPE", // 4
					"BANK", "BANK_BRANCH", "CUSTOMER_AC", "MICR", "DEST_BANK_AC_TYPE", "PROCESS_SHOP", "CREATED_BY", // 7
					"CREATED_DATE", "ERROR_MSG", "ROW_NUMBER", "UPLOAD_ID", "REMARKS" }; // 5

			Row header = sheet.createRow(0);
			for (int i = 0; i < headerList.length; i++) {

				Cell cell = header.createCell(i);
				cell.setCellValue(headerList[i]);
				cell.setCellStyle(headerStyle);
			}

			// ---------------------------------------------------
			// Step 4: Write Data Rows
			// ---------------------------------------------------

			int rowIndex = 1;

			for (Map<String, Object> error : errors) {

				Row row = sheet.createRow(rowIndex++);

				Cell cell00 = row.createCell(0);
				cell00.setCellValue(formatValue(error.get("AGREEMENT_ID")));
				cell00.setCellStyle(dataStyle);

				Cell cell01 = row.createCell(1);
				cell01.setCellValue(formatValue(error.get("AGREEMENTNO")));
				cell01.setCellStyle(dataStyle);

				Cell cell02 = row.createCell(2);
				cell02.setCellValue(formatValue(error.get("AGREEMENTDATE")));
				cell02.setCellStyle(dataStyle);

				Cell cell03 = row.createCell(3);
				cell03.setCellValue(formatValue(error.get("DISB_DATE")));
				cell03.setCellStyle(dataStyle);

				Cell cell04 = row.createCell(4);
				cell04.setCellValue(formatValue(error.get("DISBURSALAMOUNT")));
				cell04.setCellStyle(dataStyle);

				Cell cell05 = row.createCell(5);
				cell05.setCellValue(formatValue(error.get("DISB_AMT")));
				cell05.setCellStyle(dataStyle);

				Cell cell06 = row.createCell(6);
				cell06.setCellValue(formatValue(error.get("AMTFIN")));
				cell06.setCellStyle(dataStyle);

				Cell cell07 = row.createCell(7);
				cell07.setCellValue(formatValue(error.get("PRETAXIRR")));
				cell07.setCellStyle(dataStyle);

				Cell cell08 = row.createCell(8);
				cell08.setCellValue(formatValue(error.get("LESSEEID")));
				cell08.setCellStyle(dataStyle);

				Cell cell09 = row.createCell(9);
				cell09.setCellValue(formatValue(error.get("TENURE")));
				cell09.setCellStyle(dataStyle);

				Cell cell010 = row.createCell(10);
				cell010.setCellValue(formatValue(error.get("EMI")));
				cell010.setCellStyle(dataStyle);

				Cell cell011 = row.createCell(11);
				cell011.setCellValue(formatValue(error.get("FILENO")));
				cell011.setCellStyle(dataStyle);

				Cell cell012 = row.createCell(12);
				cell012.setCellValue(formatValue(error.get("BRANCHNM")));
				cell012.setCellStyle(dataStyle);

				Cell cell013 = row.createCell(13);
				cell013.setCellValue(formatValue(error.get("MODELNO")));
				cell013.setCellStyle(dataStyle);

				Cell cell014 = row.createCell(14);
				cell014.setCellValue(formatValue(error.get("MANUFACTURERDESC")));
				cell014.setCellStyle(dataStyle);

				Cell cell015 = row.createCell(15);
				cell015.setCellValue(formatValue(error.get("NAME")));
				cell015.setCellStyle(dataStyle);

				Cell cell016 = row.createCell(16);
				cell016.setCellValue(formatValue(error.get("DEALERNAME")));
				cell016.setCellStyle(dataStyle);

				Cell cell017 = row.createCell(17);
				cell017.setCellValue(formatValue(error.get("ADVANCEINSTL")));
				cell017.setCellStyle(dataStyle);

				Cell cell018 = row.createCell(18);
				cell018.setCellValue(formatValue(error.get("PROCESSINGFEE")));
				cell018.setCellStyle(dataStyle);

				Cell cell019 = row.createCell(19);
				cell019.setCellValue(formatValue(error.get("MFR_SUBVENTION_IN")));
				cell019.setCellStyle(dataStyle);

				Cell cell020 = row.createCell(20);
				cell020.setCellValue(formatValue(error.get("MFR_SUBVENTION_PAID")));
				cell020.setCellStyle(dataStyle);

				Cell cell021 = row.createCell(21);
				cell021.setCellValue(formatValue(error.get("DEALER_SUBVENTION")));
				cell021.setCellStyle(dataStyle);

				Cell cell022 = row.createCell(22);
				cell022.setCellValue(formatValue(error.get("DMA_SUBVENTION")));
				cell022.setCellStyle(dataStyle);

				Cell cell023 = row.createCell(23);
				cell023.setCellValue(formatValue(error.get("PROMOTIONDESC")));
				cell023.setCellStyle(dataStyle);

				Cell cell024 = row.createCell(24);
				cell024.setCellValue(formatValue(error.get("MARGINMONEY")));
				cell024.setCellStyle(dataStyle);

				Cell cell025 = row.createCell(25);
				cell025.setCellValue(formatValue(error.get("ADVANCE_EMI")));
				cell025.setCellStyle(dataStyle);

				Cell cell026 = row.createCell(26);
				cell026.setCellValue(formatValue(error.get("EMPLOYERNAME")));
				cell026.setCellStyle(dataStyle);

				Cell cell027 = row.createCell(27);
				cell027.setCellValue(formatValue(error.get("STATUS")));
				cell027.setCellStyle(dataStyle);

				Cell cell028 = row.createCell(28);
				cell028.setCellValue(formatValue(error.get("MAKE")));
				cell028.setCellStyle(dataStyle);

				Cell cell029 = row.createCell(29);
				cell029.setCellValue(formatValue(error.get("V_ASSET_CATG")));
				cell029.setCellStyle(dataStyle);

				Cell cell030 = row.createCell(30);
				cell030.setCellValue(formatValue(error.get("PRODUCTFLAG")));
				cell030.setCellStyle(dataStyle);

				Cell cell031 = row.createCell(31);
				cell031.setCellValue(formatValue(error.get("BRANCH_CODE")));
				cell031.setCellStyle(dataStyle);

				Cell cell032 = row.createCell(32);
				cell032.setCellValue(formatValue(error.get("DMABROKERCODE")));
				cell032.setCellStyle(dataStyle);

				Cell cell033 = row.createCell(33);
				cell033.setCellValue(formatValue(error.get("SCHEMECODE")));
				cell033.setCellStyle(dataStyle);

				Cell cell034 = row.createCell(34);
				cell034.setCellValue(formatValue(error.get("PROMOTIONSCHEME")));
				cell034.setCellStyle(dataStyle);

				Cell cell035 = row.createCell(35);
				cell035.setCellValue(formatValue(error.get("EFFRATE")));
				cell035.setCellStyle(dataStyle);

				Cell cell036 = row.createCell(36);
				cell036.setCellValue(formatValue(error.get("MODELCODE")));
				cell036.setCellStyle(dataStyle);

				Cell cell037 = row.createCell(37);
				cell037.setCellValue(formatValue(error.get("SUBMODELCODE")));
				cell037.setCellStyle(dataStyle);

				Cell cell038 = row.createCell(38);
				cell038.setCellValue(formatValue(error.get("GROSS_LTV")));
				cell038.setCellStyle(dataStyle);

				Cell cell039 = row.createCell(39);
				cell039.setCellValue(formatValue(error.get("NET_LTV")));
				cell039.setCellStyle(dataStyle);

				Cell cell040 = row.createCell(40);
				cell040.setCellValue(formatValue(error.get("FINALSOURCE")));
				cell040.setCellStyle(dataStyle);

				Cell cell041 = row.createCell(41);
				cell041.setCellValue(formatValue(error.get("FIRSTSOURCE")));
				cell041.setCellStyle(dataStyle);

				Cell cell042 = row.createCell(42);
				cell042.setCellValue(formatValue(error.get("CUSTCATG")));
				cell042.setCellStyle(dataStyle);

				Cell cell043 = row.createCell(43);
				cell043.setCellValue(formatValue(error.get("DMA_SUBVENTION_NOT_DED")));
				cell043.setCellStyle(dataStyle);

				Cell cell044 = row.createCell(44);
				cell044.setCellValue(formatValue(error.get("EMPTYPE")));
				cell044.setCellStyle(dataStyle);

				Cell cell045 = row.createCell(45);
				cell045.setCellValue(formatValue(error.get("CFOC")));
				cell045.setCellStyle(dataStyle);

				Cell cell046 = row.createCell(46);
				cell046.setCellValue(formatValue(error.get("STATE")));
				cell046.setCellStyle(dataStyle);

				Cell cell047 = row.createCell(47);
				cell047.setCellValue(formatValue(error.get("CHANNELCODE")));
				cell047.setCellStyle(dataStyle);

				Cell cell048 = row.createCell(48);
				cell048.setCellValue(formatValue(error.get("MANUFACTURERID")));
				cell048.setCellStyle(dataStyle);

				Cell cell049 = row.createCell(49);
				cell049.setCellValue(formatValue(error.get("EMPLOYERID")));
				cell049.setCellStyle(dataStyle);

				Cell cell050 = row.createCell(50);
				cell050.setCellValue(formatValue(error.get("INFAVOUROF")));
				cell050.setCellStyle(dataStyle);

				Cell cell051 = row.createCell(51);
				cell051.setCellValue(formatValue(error.get("CHEQUESTATUS")));
				cell051.setCellStyle(dataStyle);

				Cell cell052 = row.createCell(52);
				cell052.setCellValue(formatValue(error.get("OSP_CODE")));
				cell052.setCellStyle(dataStyle);

				Cell cell053 = row.createCell(53);
				cell053.setCellValue(formatValue(error.get("DME_NAME")));
				cell053.setCellStyle(dataStyle);

				Cell cell054 = row.createCell(54);
				cell054.setCellValue(formatValue(error.get("DUMMY")));
				cell054.setCellStyle(dataStyle);

				Cell cell055 = row.createCell(55);
				cell055.setCellValue(formatValue(error.get("CUSTOMER_NAME")));
				cell055.setCellStyle(dataStyle);

				Cell cell056 = row.createCell(56);
				cell056.setCellValue(formatValue(error.get("CHARGE_ID1")));
				cell056.setCellStyle(dataStyle);

				Cell cell057 = row.createCell(57);
				cell057.setCellValue(formatValue(error.get("CHARGE_DESC1")));
				cell057.setCellStyle(dataStyle);

				Cell cell058 = row.createCell(58);
				cell058.setCellValue(formatValue(error.get("CHARGE_AMT1")));
				cell058.setCellStyle(dataStyle);

				Cell cell059 = row.createCell(59);
				cell059.setCellValue(formatValue(error.get("CHARGE_ID2")));
				cell059.setCellStyle(dataStyle);

				Cell cell060 = row.createCell(60);
				cell060.setCellValue(formatValue(error.get("CHARGE_DESC2")));
				cell060.setCellStyle(dataStyle);

				Cell cell061 = row.createCell(61);
				cell061.setCellValue(formatValue(error.get("CHARGE_AMT2")));
				cell061.setCellStyle(dataStyle);

				Cell cell062 = row.createCell(62);
				cell062.setCellValue(formatValue(error.get("CHARGE_ID4")));
				cell062.setCellStyle(dataStyle);

				Cell cell063 = row.createCell(63);
				cell063.setCellValue(formatValue(error.get("CHARGE_DESC4")));
				cell063.setCellStyle(dataStyle);

				Cell cell064 = row.createCell(64);
				cell064.setCellValue(formatValue(error.get("CHARGE_AMT4")));
				cell064.setCellStyle(dataStyle);

				Cell cell065 = row.createCell(65);
				cell065.setCellValue(formatValue(error.get("CHARGE_ID5")));
				cell065.setCellStyle(dataStyle);

				Cell cell066 = row.createCell(66);
				cell066.setCellValue(formatValue(error.get("CHARGE_DESC5")));
				cell066.setCellStyle(dataStyle);

				Cell cell067 = row.createCell(67);
				cell067.setCellValue(formatValue(error.get("CHARGE_AMT5")));
				cell067.setCellStyle(dataStyle);

				Cell cell068 = row.createCell(68);
				cell068.setCellValue(formatValue(error.get("CHARGE_ID6")));
				cell068.setCellStyle(dataStyle);

				Cell cell069 = row.createCell(69);
				cell069.setCellValue(formatValue(error.get("CHARGE_DESC6")));
				cell069.setCellStyle(dataStyle);

				Cell cell070 = row.createCell(70);
				cell070.setCellValue(formatValue(error.get("CHARGE_AMT6")));
				cell070.setCellStyle(dataStyle);

				Cell cell071 = row.createCell(71);
				cell071.setCellValue(formatValue(error.get("CHARGE_ID8")));
				cell071.setCellStyle(dataStyle);

				Cell cell072 = row.createCell(72);
				cell072.setCellValue(formatValue(error.get("CHARGE_DESC8")));
				cell072.setCellStyle(dataStyle);

				Cell cell073 = row.createCell(73);
				cell073.setCellValue(formatValue(error.get("CHARGE_AMT8")));
				cell073.setCellStyle(dataStyle);

				Cell cell074 = row.createCell(74);
				cell074.setCellValue(formatValue(error.get("CHARGE_ID9")));
				cell074.setCellStyle(dataStyle);

				Cell cell075 = row.createCell(75);
				cell075.setCellValue(formatValue(error.get("CHARGE_DESC9")));
				cell075.setCellStyle(dataStyle);

				Cell cell076 = row.createCell(76);
				cell076.setCellValue(formatValue(error.get("CHARGE_AMT9")));
				cell076.setCellStyle(dataStyle);

				Cell cell077 = row.createCell(77);
				cell077.setCellValue(formatValue(error.get("CHARGE_ID10")));
				cell077.setCellStyle(dataStyle);

				Cell cell078 = row.createCell(78);
				cell078.setCellValue(formatValue(error.get("CHARGE_DESC10")));
				cell078.setCellStyle(dataStyle);

				Cell cell079 = row.createCell(79);
				cell079.setCellValue(formatValue(error.get("CHARGE_AMT10")));
				cell079.setCellStyle(dataStyle);

				Cell cell080 = row.createCell(80);
				cell080.setCellValue(formatValue(error.get("CHARGE_ID11")));
				cell080.setCellStyle(dataStyle);

				Cell cell081 = row.createCell(81);
				cell081.setCellValue(formatValue(error.get("CHARGE_DESC11")));
				cell081.setCellStyle(dataStyle);

				Cell cell082 = row.createCell(82);
				cell082.setCellValue(formatValue(error.get("CHARGE_AMT11")));
				cell082.setCellStyle(dataStyle);

				Cell cell083 = row.createCell(83);
				cell083.setCellValue(formatValue(error.get("EMPLOYMENT_TYPE")));
				cell083.setCellStyle(dataStyle);

				Cell cell084 = row.createCell(84);
				cell084.setCellValue(formatValue(error.get("PSL_FLAG")));
				cell014.setCellStyle(dataStyle);

				Cell cell085 = row.createCell(85);
				cell085.setCellValue(formatValue(error.get("INSTRUMENT_TYPE")));
				cell085.setCellStyle(dataStyle);

				Cell cell086 = row.createCell(86);
				cell086.setCellValue(formatValue(error.get("BANK")));
				cell086.setCellStyle(dataStyle);

				Cell cell087 = row.createCell(87);
				cell087.setCellValue(formatValue(error.get("BANK_BRANCH")));
				cell087.setCellStyle(dataStyle);

				Cell cell088 = row.createCell(88);
				cell088.setCellValue(formatValue(error.get("CUSTOMER_AC")));
				cell088.setCellStyle(dataStyle);

				Cell cell089 = row.createCell(89);
				cell089.setCellValue(formatValue(error.get("MICR")));
				cell089.setCellStyle(dataStyle);

				Cell cell090 = row.createCell(90);
				cell090.setCellValue(formatValue(error.get("DEST_BANK_AC_TYPE")));
				cell090.setCellStyle(dataStyle);

				Cell cell091 = row.createCell(91);
				cell091.setCellValue(formatValue(error.get("PROCESS_SHOP")));
				cell091.setCellStyle(dataStyle);

				Cell cell092 = row.createCell(92);
				cell092.setCellValue(formatValue(error.get("CREATED_BY")));
				cell092.setCellStyle(dataStyle);

				Cell cell093 = row.createCell(93);
				cell093.setCellValue(formatValue(error.get("CREATED_DATE")));
				cell093.setCellStyle(dataStyle);

				Cell cell094 = row.createCell(94);
				cell094.setCellValue(formatValue(
						error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
				cell094.setCellStyle(dataStyle);

				Cell cell095 = row.createCell(95);
				cell095.setCellValue(formatValue(error.get("ROW_NUMBER")));
				cell095.setCellStyle(dataStyle);

				Cell cell096 = row.createCell(96);
				cell096.setCellValue(formatValue(
						error.get("UPLOAD_ID") != null ? error.get("UPLOAD_ID") : error.get("ORA_ERR_TAG$")));
				cell096.setCellStyle(dataStyle);

				Cell cell097 = row.createCell(97);
				cell097.setCellValue(formatValue(error.get("REMARKS")));
				cell097.setCellStyle(dataStyle);

//			Cell cell098 = row.createCell(98);
//			cell098.setCellValue(formatValue(error.get("REMARKS")));
//			cell088.setCellStyle(dataStyle);
			}

			// ---------------------------------------------------
			// Step 5: Auto size columns
			// ---------------------------------------------------

			for (int i = 0; i < 98; i++) {
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
	private void process(List<FinnoneDump> validEntityList, List<FinnoneDumpError> invalidIntityList,
			Date cycleFromDate, Date cycleToDate, String uploadId) {

		try {

			logger.info("batch insert stage");
			// batch insert into stage
			batchInsertMainTable(validEntityList, uploadId, cycleFromDate, cycleToDate);

			logger.info("batch insert Error");
			// batch Error insert
			batchInsertError(invalidIntityList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Finnone Dump File upload Process Failed");
		}
	}

	@Transactional
	public Map<String, Integer> getCounts(String uploadId, String originalFilename) {

		int insertCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM TM_VHL_FINNONE_DUMP WHERE UPLOAD_ID=? AND FILE_NAME=? ", Integer.class, uploadId,
				originalFilename);

		int errorCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM TM_VHL_FINNONE_DUMP_ERROR WHERE UPLOAD_ID=?",
				Integer.class, uploadId);

		Map<String, Integer> result = new HashMap<>();
		result.put("insertCount", insertCount);
		result.put("errorCount", errorCount);

		return result;
	}

}

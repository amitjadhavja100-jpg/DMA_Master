package com.icici.dma.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.model.FinnoneDump;
import com.icici.dma.repository.FinnoneDumpRepository;

@Service
@Transactional
//public class FinnoneDumpServiceImpl implements FinnoneDumpService {
public class FinnoneDumpServiceImpl {

	@Autowired
	private FinnoneDumpRepository repository;

	@PersistenceContext
	private EntityManager entityManager;

	private static final Set<String> REQUIRED_HEADERS = new HashSet<>(
			Arrays.asList("AGREEMENTID", "AGREEMENTNO", "AGREEMENTDATE",
					"DISBURSALAMOUNT", "AMTFIN", "PRETAXIRR", "LESSEEID",
					"TENURE"));

	private static final Set<String> MANDATORY_COLS = new HashSet<>(
			Arrays.asList("AL_STATE", "MIS_STATE", "GST_STATE", "ZONE",
					"SEGMENT", "SOURCING", "SOURCING_1", "CIBIL_SCORE"));

	private static final Map<String, String> MANDATORY_COL_MASTER_MAP = new HashMap<>();
	
	private static final Logger logger = LogManager.getLogger(FinnoneDumpServiceImpl.class);

	static {
		MANDATORY_COL_MASTER_MAP.put("AL_STATE", "BRANCH MASTER");
		MANDATORY_COL_MASTER_MAP.put("MIS_STATE", "BRANCH MASTER");
		MANDATORY_COL_MASTER_MAP.put("ZONE", "BRANCH MASTER");
		MANDATORY_COL_MASTER_MAP.put("STATE", "GST MASTER");
		MANDATORY_COL_MASTER_MAP.put("SOURCING", "CHANNEL MASTER");
		MANDATORY_COL_MASTER_MAP.put("SOURCING_1", "CHANNEL MASTER");
		MANDATORY_COL_MASTER_MAP.put("I_BOX_ID", "CHANNEL MASTER");
		MANDATORY_COL_MASTER_MAP.put("CIBIL_SCORE", "RCAS DUMP");
		MANDATORY_COL_MASTER_MAP.put("APPLICANT_TYPE", "RCAS DUMP");
		MANDATORY_COL_MASTER_MAP.put("PROCESS_SHOP", "RCAS DUMP");
		MANDATORY_COL_MASTER_MAP.put("SEGMENT", "MODEL MASTER");
		MANDATORY_COL_MASTER_MAP.put("CIBIL_SCORE", "CIBIL_SCORE");
	}

	DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MMM-yyyy",
			Locale.ENGLISH);

	static// ✅ CASE-SENSITIVE HEADER → FIELD MAP
	Map<String, String> HEADER_FIELD_MAP = new TreeMap<>();
	static {
		HEADER_FIELD_MAP.put("AGREEMENTID", "agreementId");
		HEADER_FIELD_MAP.put("AGREEMENTNO", "agreementNo");
		HEADER_FIELD_MAP.put("AGREEMENTDATE", "agreementDate");
		HEADER_FIELD_MAP.put("DISB_DATE", "disbDate");
		HEADER_FIELD_MAP.put("DISBURSALAMOUNT", "disbursalAmount");
		HEADER_FIELD_MAP.put("DISB_AMT", "disbAmt");
		HEADER_FIELD_MAP.put("AMTFIN", "amtFin");
		HEADER_FIELD_MAP.put("PRETAXIRR", "preTaxIrr");
		HEADER_FIELD_MAP.put("LESSEEID", "lesseeId");
		HEADER_FIELD_MAP.put("TENURE", "tenure");
		HEADER_FIELD_MAP.put("EMI", "emi");
		HEADER_FIELD_MAP.put("EFFRATE", "effRate");
		HEADER_FIELD_MAP.put("FILENO", "fileNo");
		HEADER_FIELD_MAP.put("BRANCHNM", "branchNm");
		HEADER_FIELD_MAP.put("MODELNO", "modelNo");
		HEADER_FIELD_MAP.put("MANUFACTURERDESC", "manufacturerDesc");
		HEADER_FIELD_MAP.put("NAME", "name");
		HEADER_FIELD_MAP.put("DEALERNAME", "dealerName");
		HEADER_FIELD_MAP.put("ADVANCEINSTL", "advanceInstl");
		HEADER_FIELD_MAP.put("PROCESSINGFEE", "processingFee");
		HEADER_FIELD_MAP.put("MFR_SUBVENTION_IN", "mfrSubventionIn");
		HEADER_FIELD_MAP.put("MFR_SUBVENTION_PAID", "mfrSubventionPaid");
		HEADER_FIELD_MAP.put("DEALER_SUBVENTION", "dealerSubvention");
		HEADER_FIELD_MAP.put("DMA_SUBVENTION", "dmaSubvention");
		HEADER_FIELD_MAP.put("PROMOTIONDESC", "promotionDesc");
		HEADER_FIELD_MAP.put("MARGINMONEY", "marginMoney");
		HEADER_FIELD_MAP.put("ADVANCE_EMI", "advanceEmi");
		HEADER_FIELD_MAP.put("EMPLOYERNAME", "employerName");
		HEADER_FIELD_MAP.put("STATUS", "status");
		HEADER_FIELD_MAP.put("MAKE", "make");
		HEADER_FIELD_MAP.put("V_ASSET_CATG", "vAssetCatg");
		HEADER_FIELD_MAP.put("PRODUCTFLAG", "productFlag");
		HEADER_FIELD_MAP.put("BRANCH_CODE", "branchCode");
		HEADER_FIELD_MAP.put("DMABROKERCODE", "dmaBrokerCode");
		HEADER_FIELD_MAP.put("SCHEMECODE", "schemeCode");
		HEADER_FIELD_MAP.put("PROMOTIONSCHEME", "promotionScheme");
		HEADER_FIELD_MAP.put("MODELCODE", "modelCode");
		HEADER_FIELD_MAP.put("SUBMODELCODE", "subModelCode");
		HEADER_FIELD_MAP.put("GROSS_LTV", "grossLtv");
		HEADER_FIELD_MAP.put("NET_LTV", "netLtv");
		HEADER_FIELD_MAP.put("FINALSOURCE", "finalSource");
		HEADER_FIELD_MAP.put("FIRSTSOURCE", "firstSource");
		HEADER_FIELD_MAP.put("CUSTCATG", "custCatg");
		HEADER_FIELD_MAP.put("DMA_SUBVENTION_NOT_DED", "dmaSubventionNotDed");
		HEADER_FIELD_MAP.put("EMPTYPE", "empType");
		HEADER_FIELD_MAP.put("CFOC", "cfoc");
		HEADER_FIELD_MAP.put("STATE", "state");
		HEADER_FIELD_MAP.put("CHANNELCODE", "channelCode");
		HEADER_FIELD_MAP.put("MANUFACTURERID", "manufacturerId");
		HEADER_FIELD_MAP.put("EMPLOYERID", "employerId");
		HEADER_FIELD_MAP.put("INFAVOUROF", "inFavourOf");
		HEADER_FIELD_MAP.put("CHEQUESTATUS", "chequeStatus");
		HEADER_FIELD_MAP.put("OSP_CODE", "ospCode");
		HEADER_FIELD_MAP.put("DME_NAME", "dmeName");
		HEADER_FIELD_MAP.put("DUMMY", "dummy");
		HEADER_FIELD_MAP.put("CUSTOMER_NAME", "customerName");
		HEADER_FIELD_MAP.put("CHARGE ID1", "chargeId1");
		HEADER_FIELD_MAP.put("CHARGE DESC1", "chargeDesc1");
		HEADER_FIELD_MAP.put("CHARGE_AMT1", "chargeAmt1");
		HEADER_FIELD_MAP.put("CHARGE ID2", "chargeId2");
		HEADER_FIELD_MAP.put("CHARGE DESC2", "chargeDesc2");
		HEADER_FIELD_MAP.put("CHARGE_AMT2", "chargeAmt2");
		HEADER_FIELD_MAP.put("CHARGE ID3", "chargeId3");
		HEADER_FIELD_MAP.put("CHARGE DESC3", "chargeDesc3");
		HEADER_FIELD_MAP.put("CHARGE_AMT3", "chargeAmt3");
		HEADER_FIELD_MAP.put("CHARGE ID4", "chargeId4");
		HEADER_FIELD_MAP.put("CHARGE DESC4", "chargeDesc4");
		HEADER_FIELD_MAP.put("CHARGE_AMT4", "chargeAmt4");
		HEADER_FIELD_MAP.put("CHARGE ID5", "chargeId5");
		HEADER_FIELD_MAP.put("CHARGE DESC5", "chargeDesc5");
		HEADER_FIELD_MAP.put("CHARGE_AMT5", "chargeAmt5");
		HEADER_FIELD_MAP.put("CHARGE ID6", "chargeId6");
		HEADER_FIELD_MAP.put("CHARGE DESC6", "chargeDesc6");
		HEADER_FIELD_MAP.put("CHARGE_AMT6", "chargeAmt6");
		HEADER_FIELD_MAP.put("CHARGE ID7", "chargeId7");
		HEADER_FIELD_MAP.put("CHARGE DESC7", "chargeDesc7");
		HEADER_FIELD_MAP.put("CHARGE_AMT7", "chargeAmt7");
		HEADER_FIELD_MAP.put("CHARGE ID8", "chargeId8");
		HEADER_FIELD_MAP.put("CHARGE DESC8", "chargeDesc8");
		HEADER_FIELD_MAP.put("CHARGE_AMT8", "chargeAmt8");
		HEADER_FIELD_MAP.put("CHARGE ID9", "chargeId9");
		HEADER_FIELD_MAP.put("CHARGE DESC9", "chargeDesc9");
		HEADER_FIELD_MAP.put("CHARGE_AMT9", "chargeAmt9");
		HEADER_FIELD_MAP.put("CHARGE ID10", "chargeId10");
		HEADER_FIELD_MAP.put("CHARGE DESC10", "chargeDesc10");
		HEADER_FIELD_MAP.put("CHARGE_AMT10", "chargeAmt10");
		HEADER_FIELD_MAP.put("CHARGE ID11", "chargeId11");
		HEADER_FIELD_MAP.put("CHARGE DESC11", "chargeDesc11");
		HEADER_FIELD_MAP.put("CHARGE_AMT11", "chargeAmt11");
		HEADER_FIELD_MAP.put("EMPLOYMENT_TYPE", "employmentType");
		HEADER_FIELD_MAP.put("PSL_FLAG", "pslFlag");
		HEADER_FIELD_MAP.put("INSTRUMENT_TYPE", "instrumentType");
		HEADER_FIELD_MAP.put("BANK", "bank");
		HEADER_FIELD_MAP.put("BANK_BRANCH", "bankBranch");
		HEADER_FIELD_MAP.put("CUSTOMER_AC", "customerAc");
		HEADER_FIELD_MAP.put("MICR", "micr");
		HEADER_FIELD_MAP.put("DEST_BANK_AC_TYPE", "destBankAcType");
		HEADER_FIELD_MAP.put("PROCESS_SHOP", "processShop");
	}

//	@Override
	public String uploadFinnoneExcel(MultipartFile file, String user,
			Date cycleFromDate, Date cycleToDate) {

		if (file == null || file.isEmpty()) {			
			logger.error("Uploaded file is empty");
			throw new RuntimeException("Uploaded file is empty");
		}

		List<FinnoneDump> saveList = new ArrayList<>();
		int skippedRows = 0;

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);

			String sheetName = sheet.getSheetName();
			
			logger.info("Sheet Name::" + sheetName);
			//System.out.println("Sheet Name::" + sheetName);

			int sheetCount = workbook.getNumberOfSheets();

			if (sheetCount != 1) {
				
				logger.error("Excel file should contain only one sheet not more than that");
				return "Excel file should contain only one sheet not more than that";
			}

			if (sheet == null) {
				
				logger.error("Excel sheet not found");
				throw new RuntimeException("Excel sheet not found");
			}

			Row headerRow = sheet.getRow(0);
			// validateHeaders(headerRow);

			if (headerRow == null) {
				
				logger.error("Header row missing");
				throw new RuntimeException("Header row missing");
			}

			Map<Integer, String> columnMap = mapHeaders(headerRow);

			//System.out.println("✔ Header mapping completed. Total mapped columns = {}"+ columnMap.size());
			logger.info("✔ Header mapping completed. Total mapped columns = {}"+ columnMap.size());

			int lastRow = sheet.getLastRowNum();
			
			logger.info("Excel last row index detected = {}" + lastRow + 1);
			//System.out.println("Excel last row index detected = {}" + lastRow+ 1);

			/* for (int i = 1; i <= lastRow; i++) { */

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);

				if (isRowEmpty(row)) {
					skippedRows++;
					break;
				}

				FinnoneDump entity = new FinnoneDump();
				repository.deleteAllInBatch();
				entity.setCreatedBy(user);
				entity.setCreatedDate(new Date());
				entity.setCycleFromDate(cycleFromDate);
				entity.setCycleToDate(cycleToDate);

				setFields(row, columnMap, entity);

				// Mandatory DB validation
				/*
				 * if (entity.getAgreementNo() == null ||
				 * entity.getAgreementNo().trim().isEmpty()) { return
				 * ("AGREEMENTNO is Required");
				 * 
				 * }
				 * 
				 * if (entity.getAgreementId() == null ||
				 * String.valueOf(entity.getAgreementId()).trim().isEmpty()) {
				 * return ("AGREEMENTNO is Required");
				 * 
				 * }
				 * 
				 * if (entity.getAgreementDate() == null) { return
				 * ("AGREEMENTDATE is Required");
				 * 
				 * }
				 */
				/*
				 * if (entity.getFileNo() == null) { return ("FILENO is NULL" +
				 * i + 1);
				 * 
				 * } if (entity.getTenure() == null) { return ("TENURE is NULL"
				 * + i + 1);
				 * 
				 * } if (entity.getModelNo() == null) { return
				 * ("MODELNO is NULL" + i + 1);
				 * 
				 * }
				 * 
				 * if (entity.getDisbDate() == null) { return
				 * ("DISB_DATE is NULL" + i + 1);
				 * 
				 * } if (entity.getDisbursalAmount() == null) { return
				 * ("DISBURSALAMOUNT is NULL" + i + 1);
				 * 
				 * } if (entity.getName() == null) { return ("NAME is NULL" + i
				 * + 1);
				 * 
				 * } if (entity.getManufacturerDesc() == null) { return
				 * ("MANUFACTURERDESC is NULL" + i + 1);
				 * 
				 * } if (entity.getDealerName() == null) { return
				 * ("DEALERNAME is NULL" + i + 1);
				 * 
				 * }
				 */
				saveList.add(entity);
			}

			repository.saveAll(saveList);
			
			logger.info("✅ Upload completed. Total records saved = {}"+ saveList.size());
			//System.out.println("✅ Upload completed. Total records saved = {}"+ saveList.size());
			
			logger.info("Finnone Dump uploaded Successfully!");
			return "Finnone Dump uploaded Successfully!";

		} catch (Exception e) {
			//System.out.println("❌ Upload failed" + e);
			
			logger.info("Wrong File Upload: " + e.getMessage());
			throw new RuntimeException("Wrong File Upload: " + e.getMessage());
		}
	}

	private Map<Integer, String> mapHeaders(Row headerRow) {
		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < headerRow.getLastCellNum(); i++) {

			String raw = readHeader(headerRow.getCell(i));
			if (raw == null)
				continue;

			String normalized = normalizeHeader(raw);

			String field = HEADER_FIELD_MAP.get(normalized); // ✅ ONLY ONCE
			
			
			logger.info("Header raw='" + raw + "', normalized='"+ normalized + "', mappedTo='" + field + "'");
			//System.out.println("Header raw='" + raw + "', normalized='"+ normalized + "', mappedTo='" + field + "'");

			if (field != null) {
				map.put(i, field);
			} else {
				
				logger.info("❌ Unmapped header ignored: " + raw);
				//System.err.println("❌ Unmapped header ignored: " + raw);
			}
		}
		return map;
	}

	private void setFields(Row row, Map<Integer, String> columnMap,
			FinnoneDump entity) {

		for (Map.Entry<Integer, String> entry : columnMap.entrySet()) {

			try {
				Cell cell = row.getCell(entry.getKey());
				String value = getCellValue(cell);

				if (value == null || value.trim().isEmpty())
					continue;

				Field field = FinnoneDump.class.getDeclaredField(entry
						.getValue());
				field.setAccessible(true);
				Class<?> type = field.getType();
				
				logger.info("CELL TYPE::: " + type);
				//System.out.println("CELL TYPE::: " + type);

				if (type == String.class) {
					field.set(entity, value);

				} else if (type == Integer.class) {
					field.set(entity, Integer.valueOf(value));

				} else if (type == Long.class) {
					field.set(entity, new BigDecimal(value).longValue());

				} else if (type == BigDecimal.class) {
					field.set(entity, new BigDecimal(value));

				} else if (type == LocalDate.class) {
					field.set(entity, LocalDate.parse(value));

				} else if (type == Date.class) {
					field.set(entity, parseDate(value));
				}

			} catch (Exception e) {
				//System.out.println("❌ Failed at row {} field {}"+ row.getRowNum() + 1 + entry.getValue() + e);
				logger.info("❌ Failed at row {} field {}"+ row.getRowNum() + 1 + entry.getValue() + e);
			}
		}
	}

	/*
	 * ======================================================= DATE PARSER
	 * (MULTI FORMAT) =======================================================
	 */
	private Date parseDate(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		value = value.trim().toUpperCase(Locale.ENGLISH);

		String[] patterns = { "d MMM yy", // 3 OCT 25
				"dd MMM yy", // 03 OCT 25
				"d-MMM-yy", // 1-Oct-25
				"dd-MMM-yy", // 01-Oct-25
				"dd-MM-yyyy", "dd/MM/yyyy", "yyyy-MM-dd" };

		for (String pattern : patterns) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern,
						Locale.ENGLISH);
				sdf.setLenient(false);
				return sdf.parse(value);
			} catch (ParseException ignored) {
			}
		}
		
		logger.error("Unsupported date format: " + value);
		throw new IllegalArgumentException("Unsupported date format: " + value);
	}

	/*
	 * ======================================================= CELL VALUE
	 * =======================================================
	 */
	private String getCellValue(Cell cell) {

		if (cell == null)
			return null;

		DataFormatter formatter = new DataFormatter();

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();
		case NUMERIC:
		case FORMULA:
			return formatter.formatCellValue(cell).trim();
		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case BLANK:
			return "";
		default:
			return null;
		}
	}

	/*
	 * ======================================================= ROW EMPTY CHECK
	 * =======================================================
	 */
	/*
	 * private boolean isRowEmpty(Row row) { for (int i = row.getFirstCellNum();
	 * i < row.getLastCellNum(); i++) { Cell cell = row.getCell(i); if (cell !=
	 * null && cell.getCellType() != CellType.BLANK) { return false; } } return
	 * true; }
	 */

	/*
	 * ======================================================= HEADER VALIDATION
	 * =======================================================
	 */
	private void validateHeaders(Row headerRow) {

		if (headerRow == null) {
			
			logger.error("Excel file has no header row");
			throw new RuntimeException("Excel file has no header row");
		}

		// Excel headers (normalized)
		Set<String> excelHeaders = new HashSet<>();
		for (Cell cell : headerRow) {
			if (cell != null) {
				excelHeaders
						.add(cell.getStringCellValue().trim().toUpperCase());
			}
		}

		// Expected headers (normalized)
		Set<String> expectedHeaders = new HashSet<>();
		for (String header : HEADER_FIELD_MAP.keySet()) {
			expectedHeaders.add(header.trim().toUpperCase());
		}

		// ❌ Extra columns
		Set<String> extraHeaders = new HashSet<>(excelHeaders);
		extraHeaders.removeAll(expectedHeaders);

		if (!extraHeaders.isEmpty()) {
			
			logger.error("Invalid Excel file. Extra columns found: " + extraHeaders);
			throw new RuntimeException("Invalid Excel file. Extra columns found: " + extraHeaders);
		}

		// ❌ Missing columns
		Set<String> missingHeaders = new HashSet<>(expectedHeaders);
		missingHeaders.removeAll(excelHeaders);

		if (!missingHeaders.isEmpty()) {
			
			logger.error("Invalid Excel file. Missing required columns: "+ missingHeaders);
			throw new RuntimeException("Invalid Excel file. Missing required columns: "+ missingHeaders);
		}
	}

	/*
	 * ======================================================= HEADER
	 * NORMALIZATION =======================================================
	 */

	/*
	 * private String normalizeHeader(String header) { return header == null ?
	 * null : header.trim().replace(" ", " ").replace("-", "_").replace("&",
	 * "_").replaceAll("[^a-zA-Z0-9]", "_") .replaceAll("_+", "_").toUpperCase()
	 * 
	 * ; }
	 */
	private String normalizeHeader(String header) {
		if (header == null)
			return null;

		return header.trim().replaceAll("[^A-Za-z0-9_ ]", "") // ❗ keep SPACE
																// and _
				.replaceAll("\\s+", " ") // normalize multiple spaces → single
											// space
				.toUpperCase();
	}

	private String readHeader(Cell cell) {
		return getCellValue(cell);
	}

	// ==========================================
	// Check Empty Row
	// ==========================================
	private boolean isRowEmpty(Row row) {

		if (row == null)
			return true;

		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {

			Cell cell = row.getCell(c);

			if (cell != null && cell.getCellType() != CellType.BLANK) {

				String value = new DataFormatter().formatCellValue(cell).trim();

				if (!value.isEmpty()) {
					return false; // found data
				}
			}
		}

		return true; // all cells empty
	}

//	@Override
	public byte[] exportToExcel(Date cycleFromDate, Date cycleToDate) {

		/* public byte[] exportToExcel() { */
		
		logger.info("Export the service::: " + cycleFromDate + "cycleToDate " + cycleToDate);
		
		Calendar calFrom = Calendar.getInstance();
		calFrom.setTime(cycleFromDate);
		 
		Calendar calTo = Calendar.getInstance();
		calTo.setTime(cycleToDate);
		 
		// Check same month & year
		/*if (calFrom.get(Calendar.MONTH) != calTo.get(Calendar.MONTH) ||
		    calFrom.get(Calendar.YEAR) != calTo.get(Calendar.YEAR)) {
		 
		    throw new RuntimeException("Kindly select proper Date");
		}*/
		 
		// Check FROM date = 1st day
		/*if (calFrom.get(Calendar.DAY_OF_MONTH) != 1) {
		    throw new RuntimeException("Kindly select proper Date");
		}
		 
		// Check TO date = last day of month
		int lastDay = calTo.getActualMaximum(Calendar.DAY_OF_MONTH);
		 
		if (calTo.get(Calendar.DAY_OF_MONTH) != lastDay) {
		    throw new RuntimeException("To Date must be last day of month");
		}*/
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(cycleToDate);
		cal.add(Calendar.DATE, 1);
		Date nextDate =cal.getTime();
		
		long count = repository.countByStatusAndCycleFromDateBetween("A",cycleFromDate,nextDate);
		
		if(count == 0){
			throw new RuntimeException("No data available for data selection");
		}
		
		/*FinnoneDump entity = new FinnoneDump();
		if(!entity.getCycleFromDate().equals(cycleFromDate) && !entity.getCycleToDate().equals(cycleToDate))
		{
			throw new RuntimeException("No data available for data selection");
		}*/

		try (Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

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
				
			String sql = "WITH FIN_DATA AS ( "
					+ " SELECT F.*, "
					+ " CASE WHEN F.DMABROKERCODE = A.DMA_CODE THEN F.DMABROKERCODE ELSE A.DMA_CODE END AS FINAL_DMA_CODE "
					+ " FROM TM_VHL_FINNONE_DUMP F "
					+ " LEFT JOIN TM_VHL_ALDD_TRANS_DUMP A ON A.LAN = F.AGREEMENTNO "
					+ ") "
					+ " SELECT FD.*, "
					+ " CASE "
					+ "   WHEN SUBSTR(FD.AGREEMENTNO,1,2) = 'LA' THEN 'NEW CAR' "
					+ "   WHEN SUBSTR(FD.AGREEMENTNO,1,2) IN ('LU','SP') THEN 'USED CAR' "
					+ "   ELSE 'UNKNOWN' "
					+ " END AS PRODUCT_CLASSIFICATION, "
					+ " ROUND(FD.PRETAXIRR,2) AS PRETAXIRR_FMT, "
					+ " SUBSTR(FD.AGREEMENTNO,3,3) AS DERIVED_BRANCH_CODE, "
					+ " CASE WHEN UPPER(FD.CUSTOMER_NAME) = UPPER(FD.DEALERNAME) THEN 'Y' ELSE 'N' END AS DEEMED_DEMO, "
					+ " B.AL_STATE, B.ZONE, B.MIS_STATE, "
					+ " C.SOURCING, C.SOURCING_1, C.I_BOX_ID, "
					+ " G.STATE AS GST_STATE, "
					+ " M.BAND AS SEGMENT, "
					+ " R.CIBIL_SCORE, R.APPLICANT_TYPE, "
					+ " S.PROCESS_SHOP "
					+ " FROM FIN_DATA FD "
					+ " LEFT JOIN TM_VHL_BRANCH_MST B ON B.BRANCH_CODE = FD.BRANCH_CODE "
					+ " LEFT JOIN TM_VHL_CHANNEL_MST C ON C.APS_CODE = FD.FINAL_DMA_CODE "
					+ " LEFT JOIN TM_VHL_GST_MST G ON G.APS_CODE = FD.FINAL_DMA_CODE "
					+ " LEFT JOIN TM_VHL_MODEL_MST M ON UPPER(M.MODEL_DESC) = UPPER(FD.MAKE) "
					+ " LEFT JOIN TM_VHL_RCAS_CIBIL_DUMP R ON R.LAN_NO = FD.AGREEMENTNO "
					+ " LEFT JOIN TM_VHL_RCAS_PROCESS_SHOP_DUMP S ON S.LAN_NO = FD.AGREEMENTNO "
					+ " WHERE FD.STATUS = 'A' AND FD.FROM_CYCLE_DATE BETWEEN :cycleFromDate AND :cycleToDate";

			/*
			 * String sql = "WITH FIN_DATA AS ( " + " SELECT F.*, " +
			 * " CASE WHEN F.DMABROKERCODE = A.DMA_CODE THEN F.DMABROKERCODE ELSE A.DMA_CODE END AS FINAL_DMA_CODE "
			 * + " FROM TM_VHL_FINNONE_DUMP F " +
			 * " LEFT JOIN TM_VHL_ALDD_TRANS_DUMP A ON A.LAN = F.AGREEMENTNO " +
			 * ") " + " SELECT FD.*, " + " CASE " +
			 * "   WHEN SUBSTR(FD.AGREEMENTNO,1,2) = 'LA' THEN 'NEW CAR' " +
			 * "   WHEN SUBSTR(FD.AGREEMENTNO,1,2) IN ('LU','SP') THEN 'USED CAR' "
			 * + "   ELSE 'UNKNOWN' " + " END AS PRODUCT_CLASSIFICATION, " +
			 * " ROUND(FD.PRETAXIRR,2) AS PRETAXIRR_FMT, " +
			 * " SUBSTR(FD.AGREEMENTNO,3,3) AS DERIVED_BRANCH_CODE, " +
			 * " CASE WHEN UPPER(FD.CUSTOMER_NAME) = UPPER(FD.DEALERNAME) THEN 'Y' ELSE 'N' END AS DEEMED_DEMO, "
			 * + " B.AL_STATE, B.ZONE, B.MIS_STATE, " +
			 * " C.SOURCING, C.SOURCING_1, C.I_BOX_ID, " +
			 * " G.STATE AS GST_STATE, " + " M.BAND AS SEGMENT, " +
			 * " R.CIBIL_SCORE, R.APPLICANT_TYPE, " + " S.PROCESS_SHOP " +
			 * " FROM FIN_DATA FD " +
			 * " LEFT JOIN TM_VHL_BRANCH_MST B ON B.BRANCH_CODE = FD.BRANCH_CODE "
			 * +
			 * " LEFT JOIN TM_VHL_CHANNEL_MST C ON C.APS_CODE = FD.FINAL_DMA_CODE "
			 * +
			 * " LEFT JOIN TM_VHL_GST_MST G ON G.APS_CODE = FD.FINAL_DMA_CODE "
			 * +
			 * " LEFT JOIN TM_VHL_MODEL_MST M ON UPPER(M.MODEL_DESC) = UPPER(FD.MAKE) "
			 * +
			 * " LEFT JOIN TM_VHL_RCAS_CIBIL_DUMP R ON R.LAN_NO = FD.AGREEMENTNO "
			 * +
			 * " LEFT JOIN TM_VHL_RCAS_PROCESS_SHOP_DUMP S ON S.LAN_NO = FD.AGREEMENTNO "
			 * + " WHERE FD.STATUS = 'A' ";
			 */

			/*
			 * +
			 * " AND FD.FROM_CYCLE_DATE BETWEEN TO_DATE(cycleFromDate,'DD-MON-YYYY') AND TO_DATE(cycleToDate,'DD-MON-YYYY')"
			 * ;
			 */

			/*
			 * Query query = entityManager.createNativeQuery(sql.toString());
			 * query.setParameter("cycleFromDate", cycleFromDate);
			 * query.setParameter("cycleToDate", cycleToDate);
			 */
			
				try (PreparedStatement ps = connection
						.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
								ResultSet.CONCUR_READ_ONLY)) {
					ps.setTimestamp(1,new java.sql.Timestamp(cycleFromDate.getTime()));
					//ps.setDate(1, new java.sql.Date(cycleFromDate.getTime()));

					// ✅ End date (next day)
					Calendar calc = Calendar.getInstance();
					calc.setTime(cycleToDate);
					calc.add(Calendar.DATE, 1);

					ps.setTimestamp(2,new java.sql.Timestamp(calc.getTimeInMillis()));
					//ps.setDate(2, new java.sql.Date(cycleToDate.getTime()));				

					try (ResultSet rs = ps.executeQuery()) {
						while (rs.next()) {
							
							ResultSetMetaData meta = rs.getMetaData();
							int columnCount = meta.getColumnCount();

							// HEADER
							Row headerRow = sheet.createRow(0);
							// System.out.println("headerRow:: " + headerRow);
							for (int i = 1; i <= columnCount; i++) {
								Cell cell = headerRow.createCell(i - 1);
								cell.setCellValue(meta.getColumnName(i));
								cell.setCellStyle(headerStyle);
							}

							// DATA
							int rowNum = 1;
							int excelRowNum = rowNum + 1; // for message
							
							logger.info("Excel Row Num::" + excelRowNum);
							//System.out.println("excelRowNum" + excelRowNum);
							while (rs.next()) {
								
								Row row = sheet.createRow(rowNum++);
								for (int i = 1; i <= columnCount; i++) {

									Object val = rs.getObject(i);
									String colLabel = meta.getColumnLabel(i);
									String normalizedCol = colLabel == null ? ""
											: colLabel.trim().toUpperCase();
									Cell cell = row.createCell(i - 1);
									if (val == null) {
										cell.setCellValue("");
										cell.setCellStyle(dataStyle);
									} else if (val instanceof java.sql.Date) {
										LocalDate ld = ((java.sql.Date) val)
												.toLocalDate();
										cell.setCellValue(ld.format(DATE_FMT));
										cell.setCellStyle(dataStyle);
									} else if (val instanceof java.sql.Timestamp) {
										LocalDate ld = ((java.sql.Timestamp) val)
												.toLocalDateTime()
												.toLocalDate();
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
						}
					}
				}
			});
			
			logger.info("sheet:: " + sheet);
			//System.out.println("sheet:: " + sheet);
			workbook.write(out);
			
			logger.info("Exit in the service");
			//System.out.println("Exit in the service");
			return out.toByteArray();

		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			
			logger.error("Excel export failed", e);
			throw new RuntimeException("Excel export failed", e);
		}
	}
}

 
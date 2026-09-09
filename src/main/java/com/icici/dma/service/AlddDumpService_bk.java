package com.icici.dma.service;

/*import com.jcraft.jsch.Logger;*/
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.model.AlddTransaction;
import com.icici.dma.repository.AlddDumpRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AlddDumpService_bk {
	
	@Autowired
	private AlddDumpRepository repository;

	private FormulaEvaluator evaluator;
	
	private static final Logger logger = LogManager.getLogger(AlddDumpService_bk.class);

	private static final List<SimpleDateFormat> DATE_FORMATS = Arrays.asList(new SimpleDateFormat("dd-MM-yyyy"),
			new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("yyyy-MM-dd"), new SimpleDateFormat("yyyy/MM/dd")

	);

	private Date parseDate(String value) {

		for (SimpleDateFormat sdf : DATE_FORMATS) {
			try {
				return sdf.parse(value);
			} catch (ParseException ignored) {
			}
		}
		throw new RuntimeException("Invalid date format: " + value);
	}

	/* ================= REQUIRED HEADERS ================= */
	private static final Set<String> REQUIRED_HEADERS = new HashSet<>(
			Arrays.asList("APPLICATION_NO", "currAdd1", "fileNo"));

	/* ================= HEADER → ENTITY MAP ================= */
	private static final Map<String, String> HEADER_FIELD_MAP = new HashMap<>();

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
		 * HEADER_FIELD_MAP.put("INSTALLMENT_START_DATE",
		 * "installmentStartDate");
		 */ HEADER_FIELD_MAP.put("INSTL_TYPE", "instlType");
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
		HEADER_FIELD_MAP.put("REFERRAL CODE", "referralCode");
		HEADER_FIELD_MAP.put("INSTALMENT_START_DATE", "installmentStartDate");
		HEADER_FIELD_MAP.put("ECS_ACCTNO", "ecsAccNo");
		HEADER_FIELD_MAP.put("ASSET_CATAGORY", "assetCategory");
		HEADER_FIELD_MAP.put("IS_RBI_DECLARATION_SELECTED", "isrbideclarationselected");

	}

	/* ================= UPLOAD ================= */

	public String uploadExcel(MultipartFile file, String user, Date fromDate, Date toDate) {

		if (file == null || file.isEmpty()) {
			
			logger.info("File is empty");
			throw new RuntimeException("File is empty");
		}

		List<AlddTransaction> saveList = new ArrayList<>();

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {

			// this.evaluator =
			// workbook.getCreationHelper().createFormulaEvaluator();

			Sheet sheet = workbook.getSheetAt(0);
			
		    int sheetCount = workbook.getNumberOfSheets();
		    
		    if (sheetCount != 1) {
		    	
		    	logger.error("Excel file should contain only one sheet");
		        return "Excel file should contain only one sheet";
		    }
			
			String sheetName = sheet.getSheetName();
			System.out.println("Sheet Name::"+sheetName);
			
			if (sheet == null) {
				
				logger.info("Sheet not found");
				throw new RuntimeException("Sheet not found");
			}

			Row headerRow = sheet.getRow(0);
			validateHeaders(headerRow);
			repository.deleteAllInBatch();

			Map<Integer, String> columnMap = mapHeaders(headerRow);

			/*System.out.println("========== DATA READ START ==========");*/
			logger.info("========== DATA READ START ==========");

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				System.out.println("ALDD check ::: " + sheet.getLastRowNum());

				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				AlddTransaction entity = new AlddTransaction();
				setFields(row, columnMap, entity);
				entity.setCreatedDate(new Date());
				entity.setCreatedBy(user);
				entity.setToCycleDate(toDate);
				entity.setFromCycleDate(fromDate);
				// ✅ PK check

				if (isBlank(entity.getApplicationNo())) {
					
					logger.error("❌ Skipped → Application No missing");
					System.out.println("❌ Skipped → Application No missing");
					return "Application No missing ";

				}

				if (isBlank(String.valueOf(entity.getId()))) {
					
					logger.error("❌ Skipped → Id No is missing");
					System.out.println("❌ Skipped → Id No is missing");
					return "Id missing ";
				}

				if (entity.getMonth() == null) {
					
					logger.error("Month is blank in ALDD transcation report");
					return "Month is blank in ALDD transcation report";
				}

				if (entity.getLan() == null) {
					
					logger.error("LAN number is blank in ALDD transcation report");
					return "LAN number is blank in ALDD transcation report";
				}

				// ✅ skip duplicates
				if (repository.existsById(entity.getApplicationNo())) {
					
					logger.error("❌ Duplicate → " + entity.getApplicationNo());
					System.out.println("❌ Duplicate → " + entity.getApplicationNo());
					continue;
				}

				saveList.add(entity);
				
				logger.info("✅ Ready → " + entity.getApplicationNo());
				System.out.println("✅ Ready → " + entity.getApplicationNo());
			}

			repository.saveAll(saveList);
			
			logger.info("========== DATA READ END ==========");
			//System.out.println("========== DATA READ END ==========");

		} catch (RuntimeException e) {
			
			logger.error(e);
			throw e;
		}

		catch (Exception e) {
			e.printStackTrace();
			
			logger.error("Upload Failed : " + e.getMessage());
			throw new RuntimeException("Upload Failed : " + e.getMessage());
		}

		/*return "ALDD transaction Report upload Successfully! Records saved : " + saveList.size();*/
		
		logger.info("ALDD transaction Report upload Successfully!");
		return "ALDD transaction Report upload Successfully!";
	}
	/* ================= HEADER VALIDATION ================= */

	private boolean isBlank(String applicationNo) {
		// TODO Auto-generated method stub
		return false;
	}

	private void validateHeaders(Row headerRow) {

		if (headerRow == null) {
			
			logger.error("Excel file has no header row");
			throw new RuntimeException("Excel file has no header row");
		}

		// Excel headers (normalized)
		Set<String> excelHeaders = new HashSet<>();
		for (Cell cell : headerRow) {
			if (cell != null) {
				excelHeaders.add(cell.getStringCellValue().trim().toUpperCase());
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
			
			logger.error("Invalid Excel file. Missing required columns: " + missingHeaders);
			throw new RuntimeException("Invalid Excel file. Missing required columns: " + missingHeaders);
		}
	}

	/* ================= MAP HEADERS ================= */

	private Map<Integer, String> mapHeaders(Row headerRow) {

		Map<Integer, String> map = new HashMap<>();

		for (int i = 0; i < headerRow.getLastCellNum(); i++) {

			String raw = readHeader(headerRow.getCell(i));
			String normalized = normalizeHeader(raw);
			String field = HEADER_FIELD_MAP.get(normalized);

			if (field == null) {
				
				logger.error("❌ UNMAPPED HEADER: [" + raw + "]");
				//System.err.println("❌ UNMAPPED HEADER: [" + raw + "]");
			} else {
				
				logger.info("✅ MAPPED: " + normalized + " -> " + field);
				//System.out.println("✅ MAPPED: " + normalized + " -> " + field);
			}

			map.put(i, field);
		}
		return map;
	}

	/* ================= SET ENTITY FIELDS ================= */

	private void setFields(Row row, Map<Integer, String> columnMap, AlddTransaction entity) {

		for (Map.Entry<Integer, String> entry : columnMap.entrySet()) {

			String fieldName = entry.getValue();
			if (fieldName == null)
				continue;

			try {
				String value = getCellValue(row.getCell(entry.getKey()));
				if (value == null || value == "")
					continue;

				Field field = AlddTransaction.class.getDeclaredField(fieldName);
				field.setAccessible(true);

				Class<?> type = field.getType();
				System.out.println("Date Type " + type + " value " + value);

				if (type == String.class)
					field.set(entity, value);
				else if (type == Integer.class)
					field.set(entity, Integer.valueOf(value));
				else if (type == Long.class)
					field.set(entity, new BigDecimal(value).longValue());
				else if (type == Double.class)
					field.set(entity, Double.valueOf(value));
				else if (type == BigDecimal.class)
					field.set(entity, new BigDecimal(value));
				else if (type == LocalDate.class)
					field.set(entity, LocalDate.parse(value));
				else if (type == Date.class) {
					Cell cell = row.getCell(entry.getKey());

					Date finalDate = null;

					if (cell != null && cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {

						finalDate = cell.getDateCellValue();
					} else if (value != null && !value.trim().isEmpty()) {

						String[] patterns = { "dd-MM-yyyy", "dd/MM/yyyy", "dd-MMM-yyyy", "yyyy-MM-dd", "MM/dd/yyyy" };

						for (String pattern : patterns) {
							try {
								SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
								sdf.setLenient(false); // 🚫 invalid dates
														// blocked
								finalDate = sdf.parse(value.trim());
								break; // ✅ stop once parsed
							} catch (ParseException ignored) {
							}
						}
					}

					if (finalDate != null) {
						field.set(entity, finalDate);
					} else {
						
						logger.error("❌ Invalid date at row " + (row.getRowNum() + 1) + " field " + fieldName+ " value: " + value);
						//System.err.println("❌ Invalid date at row " + (row.getRowNum() + 1) + " field " + fieldName+ " value: " + value);
					}
				}

			} catch (Exception e) {
				
				logger.error("❌ Failed at row " + (row.getRowNum() + 1) + " field " + fieldName);
				//System.err.println("❌ Failed at row " + (row.getRowNum() + 1) + " field " + fieldName);
			}
		}
	}

	/* ================= CELL VALUE ================= */
	private String getCellValue(Cell cell) {
		if (cell == null)
			return null;

		try {
			switch (cell.getCellType()) {

			case STRING:
				return cell.getStringCellValue().trim();

			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return new DataFormatter().formatCellValue(cell);
				}
				return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();

			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());

			case FORMULA:
				return new DataFormatter().formatCellValue(cell).trim();

			default:
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
	/* ================= HEADER NORMALIZATION ================= */

	private String normalizeHeader(String header) {
		if (header == null)
			return null;
		System.out.println("header:: " + header);
		return header.trim().replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_").toUpperCase();
	}

	private String readHeader(Cell cell) {
		if (cell == null)
			return null;

		DataFormatter formatter = new DataFormatter();
		
		logger.info("Recader Celll ::: " + cell + "values of the cell " + cell.getCellType());
		System.out.println("Recader Celll ::: " + cell + "values of the cell " + cell.getCellType());

		switch (cell.getCellType()) {

		case STRING:
			return cell.getStringCellValue().trim();

		case NUMERIC:
			// IMPORTANT: handles DATE properly
			return formatter.formatCellValue(cell).trim();

		case FORMULA:
			return formatter.formatCellValue(cell).trim();

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		default:
			return null;
		}
	}
}
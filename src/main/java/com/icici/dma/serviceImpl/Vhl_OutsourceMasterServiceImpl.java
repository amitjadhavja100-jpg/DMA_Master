package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.OutsourceMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.OutsourceMaster;
import com.icici.dma.model.OutsourceMasterError;
import com.icici.dma.model.OutsourceMasterTemp;
import com.icici.dma.repository.OutsourceMasterErrorRepository;
import com.icici.dma.repository.OutsourceMasterMainRepository;
import com.icici.dma.repository.OutsourceMasterTempRepository;
import com.icici.dma.service.Vhl_OutsourceMasterService;

@Service
public class Vhl_OutsourceMasterServiceImpl implements Vhl_OutsourceMasterService {

	private static final Logger logger = LogManager.getLogger(Vhl_OutsourceMasterServiceImpl.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public OutsourceMasterTempRepository tempRepo;

	@Autowired
	public OutsourceMasterMainRepository mainRepo;

	@Autowired
	public OutsourceMasterErrorRepository errorRepo;

	private static final Set<String> EXPECTED_HEADERS = new LinkedHashSet<>(
//			Arrays.asList("SR_NO", "EMPCODE",
//			"VSTS_CODE_COUNSELOR_SAP_CODE", "OLD_VSTS_CODE", "EXECUTIVE_NAME", "TOTAL_SALARY", "TOTAL_INCENTIVE",
//			"CONVEYANCE_FOR_THE_MONTH", "ADDITIONAL_COST", "TOTAL_PAYOUT", "MAIN_DESIGNATION", "DESIGNATION",
//			"OLD_DESIGNATION", "PRIMARY_PRODUCT", "AGENCY_NAME", "LOCATION", "OLD_LOCATION", "ZONE", "ED_STATE",
//			"ED_ZONE", "MIS_STATE", "C_STATE", "MONTH", "REMARK", "I_BOX", "IPROCESS_DESIGNATION", "DOJ", "TOP_TIER_II",
//			"OLD_TOP_TIER_II", "NO_OF_DAY", "ARREAR_DAYS", "BAND", "INVOICE_NO", "INVOICE_DATE", "INVOICE_AMT",
//			"INVOICE_RECD_ON", "RESIGNED_DATE", "EPF", "EESI", "ELWF", "ROUNDING_OFF_DUE_TO_EMPLOYER_CONTR_TO_ESIC",
//			"RECOVERY_AMT_RECD", "NOTICE_PAY_DED", "TOTAL", "EX_GRATIA_BONUS", "TOTAL_WITH_INCENTIVE_CON",
//			"SERVICE_CHGS_2_1_ON_TOTAL_COST", "TOTAL_WITH_SC", "GST_18", "TOTAL_BILL_AMT",
//			"TOTAL_SALARY_INCENTIVE_CONV", "ADDITION_COST", "AS_PER_I_PROCESS_LOCATION", "AS_PER_I_PROCESS_STATE",
//			"LOT_NO", "I_PROCESS_GROSS_SALARY", "SOL_ID", "MAIL_FROM", "NO_OF_CASES_NEW", "LOAN_MNS_NEW",
//			"NO_OF_CASES_USED", "LOAN_MNS_USED", "NO_OF_CASES_TOTAL", "LOAN_MNS_TOTAL")

			Arrays.asList("EMPCODE", "VSTS_CODE_COUNSELOR_SAP_CODE", "EXECUTIVE_NAME")

	);

	private static final int BATCH_SIZE = 500;

	private static FormulaEvaluator evaluator;

	private final DataFormatter formatter = new DataFormatter();

	@Transactional
	@Override
	public Map<String, Object> uploadOutsourceMaster(MultipartFile file, String loginUser) {

		logger.info("Outsource upload started by user : {}", loginUser);
		Map<String, Object> response = new LinkedHashMap<>();
		Map<String, Integer> headerMap = new LinkedHashMap<>();
		List<OutsourceMasterTemp> tempBatch = new ArrayList<>(BATCH_SIZE);
		List<OutsourceMasterError> errorBatch = new ArrayList<>(BATCH_SIZE);
		Set<String> duplicateSet = new HashSet<>();
		Set<String> deleteSet = new HashSet<>();

		int totalRecords = 0;
		int successCount = 0;
		int errorCount = 0;
		String uploadId = generateUploadId(file.getOriginalFilename());

		try {

			if (file == null || file.isEmpty()) {
				response.put("status", "FAILED");
				response.put("message", "Uploaded file is empty");
				return response;
			}

			if (!file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
				response.put("status", "FAILED");
				response.put("message", "Only XLSX file allowed");
				return response;
			}
			logger.info("Outsource file validation  complated");
			try (
//					Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096)
//					.open(file.getInputStream())

					Workbook workbook = WorkbookFactory.create(file.getInputStream());

			) {
				evaluator = workbook.getCreationHelper().createFormulaEvaluator();

				Sheet sheet = workbook.getSheetAt(0);
				Row headerRow = sheet.getRow(0);
				headerMap = validateExcelHeaders(headerRow);
				
				int rowNumber = 0;
				
				for (int r = 1; r <= sheet.getLastRowNum(); r++) {
					
					Row row = sheet.getRow(r);
					
					rowNumber++;

					if (isRowEmpty(row)) {
						continue;
					}

					totalRecords++;
					try {

						// Mandatory field validation
						validateMandatoryFields(row, headerMap, formatter);

						// Validate duplicate row in excel
						validateExcelDuplicate(row, headerMap, duplicateSet, formatter);

						// map row data to temp
						OutsourceMasterTemp m = new OutsourceMasterTemp();

//						m.setSrNo(getCellValue(row.getCell(headerMap.get("SR_NO"))) != null
//								? Integer.valueOf(getCellValue(row.getCell(headerMap.get("SR_NO"))))
//								: null);
						
						String empCodeCellValue = getCellValue(row.getCell(headerMap.get("EMPCODE")));

						m.setEmpCode(empCodeCellValue != null
								? empCodeCellValue
								: null);
						
						// delete already pending records from temp 
						deleteSet.add(empCodeCellValue);

						m.setvSTSCodeCounselorSAPCode(
								getCellValue(row.getCell(headerMap.get("VSTS_CODE_COUNSELOR_SAP_CODE"))) != null
										? getCellValue(row.getCell(headerMap.get("VSTS_CODE_COUNSELOR_SAP_CODE")))
										: null);

//						m.setOldVSTSCode(getCellValue(row.getCell(headerMap.get("OLD_VSTS_CODE"))) != null
//								? getCellValue(row.getCell(headerMap.get("OLD_VSTS_CODE")))
//								: null);

						m.setExecutiveName(getCellValue(row.getCell(headerMap.get("EXECUTIVE_NAME"))) != null
								? getCellValue(row.getCell(headerMap.get("EXECUTIVE_NAME")))
								: null);

//						m.setTotalSalary(getCellValue(row.getCell(headerMap.get("TOTAL_SALARY"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("TOTAL_SALARY"))))
//								: null);
//
//						m.setTotalIncentive(getCellValue(row.getCell(headerMap.get("TOTAL_INCENTIVE"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("TOTAL_INCENTIVE"))))
//								: null);
//
//						m.setConveyanceforthemonth(
//								getCellValue(row.getCell(headerMap.get("CONVEYANCE_FOR_THE_MONTH"))) != null
//										? new BigDecimal(
//												getCellValue(row.getCell(headerMap.get("CONVEYANCE_FOR_THE_MONTH"))))
//										: null);
//
//						m.setAdditionalCost(getCellValue(row.getCell(headerMap.get("ADDITIONAL_COST"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("ADDITIONAL_COST"))))
//								: null);
//
//						m.setTotalPayout(getCellValue(row.getCell(headerMap.get("TOTAL_PAYOUT"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("TOTAL_PAYOUT"))))
//								: null);
//
//						m.setMainDesignation(getCellValue(row.getCell(headerMap.get("MAIN_DESIGNATION"))) != null
//								? getCellValue(row.getCell(headerMap.get("MAIN_DESIGNATION")))
//								: null);
//
//						m.setDesignation(getCellValue(row.getCell(headerMap.get("DESIGNATION"))) != null
//								? getCellValue(row.getCell(headerMap.get("DESIGNATION")))
//								: null);
//
//						m.setOldDesignation(getCellValue(row.getCell(headerMap.get("OLD_DESIGNATION"))) != null
//								? getCellValue(row.getCell(headerMap.get("OLD_DESIGNATION")))
//								: null);
//
//						m.setPrimaryProduct(getCellValue(row.getCell(headerMap.get("PRIMARY_PRODUCT"))) != null
//								? getCellValue(row.getCell(headerMap.get("PRIMARY_PRODUCT")))
//								: null);
//
//						m.setAgencyName(getCellValue(row.getCell(headerMap.get("AGENCY_NAME"))) != null
//								? getCellValue(row.getCell(headerMap.get("AGENCY_NAME")))
//								: null);
//
//						m.setLocation(getCellValue(row.getCell(headerMap.get("LOCATION"))) != null
//								? getCellValue(row.getCell(headerMap.get("LOCATION")))
//								: null);
//
//						m.setOldLocation(getCellValue(row.getCell(headerMap.get("OLD_LOCATION"))) != null
//								? getCellValue(row.getCell(headerMap.get("OLD_LOCATION")))
//								: null);
//
//						m.setZone(getCellValue(row.getCell(headerMap.get("ZONE"))) != null
//								? getCellValue(row.getCell(headerMap.get("ZONE")))
//								: null);
//
//						m.seteDState(getCellValue(row.getCell(headerMap.get("ED_STATE"))) != null
//								? getCellValue(row.getCell(headerMap.get("ED_STATE")))
//								: null);
//
//						m.seteDZone(getCellValue(row.getCell(headerMap.get("ED_ZONE"))) != null
//								? getCellValue(row.getCell(headerMap.get("ED_ZONE")))
//								: null);
//
//						m.setmISState(getCellValue(row.getCell(headerMap.get("MIS_STATE"))) != null
//								? getCellValue(row.getCell(headerMap.get("MIS_STATE")))
//								: null);
//
//						m.setcState(getCellValue(row.getCell(headerMap.get("C_STATE"))) != null
//								? getCellValue(row.getCell(headerMap.get("C_STATE")))
//								: null);
//
//						// Month
//						String monthCellValue = getCellValue(row.getCell(headerMap.get("MONTH")));
//						m.setMonth(
//								monthCellValue != null ? DateFormatter.parseToLocalDate(monthCellValue.trim()) : null);
//
//						m.setRemark(getCellValue(row.getCell(headerMap.get("REMARK"))) != null
//								? getCellValue(row.getCell(headerMap.get("REMARK")))
//								: null);
//
//						m.setiBox(getCellValue(row.getCell(headerMap.get("I_BOX"))) != null
//								? getCellValue(row.getCell(headerMap.get("I_BOX")))
//								: null);
//
//						m.setIprocessDesignation(
//								getCellValue(row.getCell(headerMap.get("IPROCESS_DESIGNATION"))) != null
//										? getCellValue(row.getCell(headerMap.get("IPROCESS_DESIGNATION")))
//										: null);
//
//						// Doj
//						String dojCellValue = getCellValue(row.getCell(headerMap.get("DOJ")));
//						m.setdOJ(dojCellValue != null ? DateFormatter.parseToLocalDate(dojCellValue.trim()) : null);
//
//						m.setTopTierII(getCellValue(row.getCell(headerMap.get("TOP_TIER_II"))) != null
//								? getCellValue(row.getCell(headerMap.get("TOP_TIER_II")))
//								: null);
//
//						m.setOldTopTierII(getCellValue(row.getCell(headerMap.get("OLD_TOP_TIER_II"))) != null
//								? getCellValue(row.getCell(headerMap.get("OLD_TOP_TIER_II")))
//								: null);
//
//						m.setNoOfDay(getCellValue(row.getCell(headerMap.get("NO_OF_DAY"))) != null
//								? Integer.valueOf(getCellValue(row.getCell(headerMap.get("NO_OF_DAY"))))
//								: null);
//
//						m.setArrearDays(getCellValue(row.getCell(headerMap.get("ARREAR_DAYS"))) != null
//								? getCellValue(row.getCell(headerMap.get("ARREAR_DAYS")))
//								: null);
//
//						m.setBand(getCellValue(row.getCell(headerMap.get("BAND"))) != null
//								? getCellValue(row.getCell(headerMap.get("BAND")))
//								: null);
//						m.setInvoiceNo(getCellValue(row.getCell(headerMap.get("INVOICE_NO"))) != null
//								? getCellValue(row.getCell(headerMap.get("INVOICE_NO")))
//								: null);
//
//						// InvoiceDate
//						String invoiceDateCellValue = getCellValue(row.getCell(headerMap.get("INVOICE_DATE")));
//						m.setInvoiceDate(invoiceDateCellValue != null
//								? DateFormatter.parseToLocalDate(invoiceDateCellValue.trim())
//								: null);
//
//						m.setInvoiceAmt(new BigDecimal(getCellValue(row.getCell(headerMap.get("INVOICE_AMT")))));
//
//						// InvoiceRecdOn
//						String invoiceRecdOnCellValue = getCellValue(row.getCell(headerMap.get("INVOICE_RECD_ON")));
//						m.setInvoiceRecdOn(invoiceRecdOnCellValue != null
//								? DateFormatter.parseToLocalDate(invoiceRecdOnCellValue.trim())
//								: null);
//
//						// Resigned date
//						String resigneddateCellValue = getCellValue(row.getCell(headerMap.get("INVOICE_RECD_ON")));
//						m.setResigneddate(resigneddateCellValue != null
//								? DateFormatter.parseToLocalDate(resigneddateCellValue.trim())
//								: null);
//
//						m.setePF(getCellValue(row.getCell(headerMap.get("EPF"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("EPF"))))
//								: null);
//
//						m.seteESI(getCellValue(row.getCell(headerMap.get("EESI"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("EESI"))))
//								: null);
//
//						m.seteLWF(getCellValue(row.getCell(headerMap.get("ELWF"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("ELWF"))))
//								: null);
//
//						m.setRoundingOffDueToEmployerContrToESIC(getCellValue(
//								row.getCell(headerMap.get("ROUNDING_OFF_DUE_TO_EMPLOYER_CONTR_TO_ESIC"))) != null
//										? new BigDecimal(getCellValue(row
//												.getCell(headerMap.get("ROUNDING_OFF_DUE_TO_EMPLOYER_CONTR_TO_ESIC"))))
//										: null);
//
//						m.setRecoveryAmtRecd(getCellValue(row.getCell(headerMap.get("RECOVERY_AMT_RECD"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("RECOVERY_AMT_RECD"))))
//								: null);
//
//						m.setNoticePayDed(getCellValue(row.getCell(headerMap.get("NOTICE_PAY_DED"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("NOTICE_PAY_DED"))))
//								: null);
//
//						m.setTotal(getCellValue(row.getCell(headerMap.get("TOTAL"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("TOTAL"))))
//								: null);
//
//						m.setExGratiaBonus(getCellValue(row.getCell(headerMap.get("EX_GRATIA_BONUS"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("EX_GRATIA_BONUS"))))
//								: null);
//
//						m.setTotalWithIncentiveAndCON(
//								getCellValue(row.getCell(headerMap.get("TOTAL_WITH_INCENTIVE_CON"))) != null
//										? new BigDecimal(
//												getCellValue(row.getCell(headerMap.get("TOTAL_WITH_INCENTIVE_CON"))))
//										: null);
//
//						m.setServiceChgsOnTotalCost(
//								getCellValue(row.getCell(headerMap.get("SERVICE_CHGS_2_1_ON_TOTAL_COST"))) != null
//										? new BigDecimal(getCellValue(
//												row.getCell(headerMap.get("SERVICE_CHGS_2_1_ON_TOTAL_COST"))))
//										: null);
//
//						String totalWithSCCellValue = getCellValue(row.getCell(headerMap.get("TOTAL_WITH_SC")));
//						m.setTotalWithSC(totalWithSCCellValue != null ? new BigDecimal(totalWithSCCellValue) : null);
//
//						String gstCellValue = getCellValue(row.getCell(headerMap.get("GST_18")));
//						m.setgST(gstCellValue != null ? new BigDecimal(gstCellValue.trim()) : null);
//
//						m.setTotalBillAmt(getCellValue(row.getCell(headerMap.get("TOTAL_BILL_AMT"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("TOTAL_BILL_AMT"))))
//								: null);
//
//						m.setTotalSalaryAndIncentiveAndConv(
//								getCellValue(row.getCell(headerMap.get("TOTAL_SALARY_INCENTIVE_CONV"))) != null
//										? new BigDecimal(
//												getCellValue(row.getCell(headerMap.get("TOTAL_SALARY_INCENTIVE_CONV"))))
//										: null);
//
//						m.setAdditionalCost(getCellValue(row.getCell(headerMap.get("ADDITION_COST"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("ADDITION_COST"))))
//								: null);
//
//						m.setAsPerIrProcessLocation(
//								getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_LOCATION"))) != null
//										? getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_LOCATION")))
//										: null);
//
//						m.setAsPerIProcessState(
//								getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_STATE"))) != null
//										? getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_STATE")))
//										: null);
//
//						m.setLotNo(getCellValue(row.getCell(headerMap.get("LOT_NO"))) != null
//								? Integer.valueOf(getCellValue(row.getCell(headerMap.get("LOT_NO"))))
//								: null);
//
//						m.setIprocessGrossSalary(
//								getCellValue(row.getCell(headerMap.get("I_PROCESS_GROSS_SALARY"))) != null
//										? Integer.valueOf(
//												getCellValue(row.getCell(headerMap.get("I_PROCESS_GROSS_SALARY"))))
//										: null);
//
//						m.setSolId(getCellValue(row.getCell(headerMap.get("SOL_ID"))) != null
//								? getCellValue(row.getCell(headerMap.get("SOL_ID")))
//								: null);
//
//						m.setMailFrom(getCellValue(row.getCell(headerMap.get("MAIL_FROM"))) != null
//								? getCellValue(row.getCell(headerMap.get("MAIL_FROM")))
//								: null);
//
//						m.setNoOfCasesNew(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_NEW"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_NEW"))))
//								: null);
//
//						m.setLoanMnsNew(getCellValue(row.getCell(headerMap.get("LOAN_MNS_NEW"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("LOAN_MNS_NEW"))))
//								: null);
//
//						m.setNoOfCasesUsed(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_USED"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_USED"))))
//								: null);
//
//						m.setLoanMnsUsed(getCellValue(row.getCell(headerMap.get("LOAN_MNS_USED"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("LOAN_MNS_USED"))))
//								: null);
//
//						m.setNoOfCasesTotal(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_TOTAL"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_TOTAL"))))
//								: null);
//
//						m.setLoanMnsTotal(getCellValue(row.getCell(headerMap.get("LOAN_MNS_TOTAL"))) != null
//								? new BigDecimal(getCellValue(row.getCell(headerMap.get("LOAN_MNS_TOTAL"))))
//								: null);

						m.setStatus("P");
						m.setCreatedBy(loginUser);
						m.setCreatedDate(new Date());
						m.setActionType("I");
						m.setActionUser(loginUser);
						m.setActionDate(new Date());
						m.setUploadId(uploadId);
						m.setFileName(file.getOriginalFilename());

						tempBatch.add(m);
						successCount++;

					} catch (RuntimeException e) {
						String errorMassage = e.getMessage();
						OutsourceMasterError error = createError(row, errorMassage, rowNumber, headerMap, formatter,
								loginUser, uploadId);
						errorBatch.add(error);
						errorCount++;
//						continue;
					}

					if (tempBatch.size() >= BATCH_SIZE) {
						// jdbcTemplate.batchUpdate(...)
						insertTempBatch(tempBatch,deleteSet);
						deleteSet.clear();
						tempBatch.clear();
					}

					if (errorBatch.size() >= BATCH_SIZE) {
						// jdbcTemplate.batchUpdate(...)
						insertErrorBatch(errorBatch);
						errorBatch.clear();
					}

					if (totalRecords % BATCH_SIZE == 0) {
						logger.info("Outsource - Record Read Count : {}", totalRecords);
					}

				}

				// save remaining batch
				if (!tempBatch.isEmpty()) {
					// jdbcTemplate.batchUpdate(...)
					insertTempBatch(tempBatch,deleteSet);
					deleteSet.clear();
					tempBatch.clear();
				}

				if (!errorBatch.isEmpty()) {
					// jdbcTemplate.batchUpdate(...)
					insertErrorBatch(errorBatch);
					errorBatch.clear();
				}
			}

			response.put("status", "SUCCESS");
			response.put("message", "File Uploaded Successfully");
			response.put("uploadId", uploadId);
			response.put("totalRecords", totalRecords);
			response.put("successfulRecords", successCount);
			response.put("ErrorCount", errorCount);

			logger.info("File Uploaded Successfully with success count : {} and error count : {}  , Total count : {}",
					successCount, errorCount, totalRecords);
		} catch (Exception e) {
			logger.error("Upload failed", e);
//			response.put("status", "FAILED");
//			response.put("message", e.getMessage());
//			return response;
			throw new RuntimeException("OutSource Master Upload Failed: " + e.getMessage());
		}
		return response;

	}

	@Transactional
	@Override
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Error Report generation started : Outsource_Master");
		logger.info("Error Report generation started By : {}",user);

		int processedRecords = 0;
		long startTime = System.currentTimeMillis();


//		String sql = " SELECT  " + "EMP_CODE  \"EmpCode\", "
//				+ "VSTSCODE_COUNSLR_SAPCODE AS \"VSTS Code / Counselor SAP Code\", "
//				+ "EXECUTIVE_NAME AS \"Executive Name\" " 
//				+ "ROW_NUMBER AS \"Row Number\", "
//				+ "ERROR_MESSAGE AS \"Error Message\" " 
//				+ "FROM TM_VHL_OUTSOURCE_MST_ERROR " 
//				+ "WHERE UPLOAD_ID = ( "
//				+ "		SELECT UPLOAD_ID  FROM ( " + "			SELECT UPLOAD_ID  "
//				+ "			FROM TM_VHL_OUTSOURCE_MST_ERROR  "
//				+ "			WHERE CREATED_BY = ?  ORDER BY CREATED_DATE DESC  )  " + "WHERE ROWNUM = 1 )";
		
	String sql =	"SELECT    "
		+ "EMP_CODE  \"EmpCode\",   "
		+ "VSTSCODE_COUNSLR_SAPCODE AS \"VSTS Code / Counselor SAP Code\",   "
		+ "EXECUTIVE_NAME AS \"Executive Name\",  "
		+ "ROW_NUMBER AS \"Row Number\",   "
		+ "ERROR_MESSAGE AS \"Error Message\"  "
		+ "FROM TM_VHL_OUTSOURCE_MST_ERROR  "
		+ "WHERE UPLOAD_ID = ( SELECT UPLOAD_ID   "
		+ "					FROM ( SELECT UPLOAD_ID  "
		+ "						   From Tm_Vhl_Outsource_Mst_Error  "
		+ "						   Where Created_By = ?   "
		+ "						)  WHERE ROWNUM = 1 )";

		try (Connection connection = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY)) {

			ps.setString(1, user);

			// ---------------------------------------------------
			// Step 1: Fetch error records from DB
			// ---------------------------------------------------

			try (ResultSet resultSet = ps.executeQuery();
					SXSSFWorkbook workbook = new SXSSFWorkbook(100);
					ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				workbook.setCompressTempFiles(true);
				resultSet.setFetchSize(1000);

				if (!resultSet.next()) {
					throw new RuntimeException("No record found");
				}

				ResultSetMetaData metaData = resultSet.getMetaData();
				int columnCount = metaData.getColumnCount();
				logger.info("Dynamic column count detected : {}", columnCount);
				// Store headers once.
				List<String> headers = new ArrayList<String>();
				for (int column = 1; column <= columnCount; column++) {
					headers.add(metaData.getColumnLabel(column));
				}
				// Header Style.
				CellStyle headerStyle = workbook.createCellStyle();
				Font headerFont = workbook.createFont();
				headerFont.setBold(true);
				headerStyle.setFont(headerFont);
				headerStyle.setAlignment(HorizontalAlignment.CENTER);
				int sheetNo = 1;
				int rowNumber = 0;
				Sheet sheet = workbook.createSheet("Sheet_" + sheetNo);
				sheet.setDefaultColumnWidth(25);
				Row headerRow = sheet.createRow(rowNumber++);
				for (int column = 0; column < headers.size(); column++) {
					Cell cell = headerRow.createCell(column);
					cell.setCellValue(headers.get(column));
					cell.setCellStyle(headerStyle);
				}
				// Stream cursor directly into Excel.
				while (resultSet.next()) {
					if (rowNumber >= 800000) {
						logger.info("Creating new sheet : Sheet_{}", sheetNo + 1);
						sheetNo++;
						sheet = workbook.createSheet("Sheet_" + sheetNo);
						rowNumber = 0;
						Row newHeader = sheet.createRow(rowNumber++);
						for (int column = 0; column < headers.size(); column++) {
							Cell cell = newHeader.createCell(column);
							cell.setCellValue(headers.get(column));
							cell.setCellStyle(headerStyle);
						}
					}
					Row dataRow = sheet.createRow(rowNumber++);
					for (int column = 1; column <= columnCount; column++) {
						Object value = resultSet.getObject(column);
						dataRow.createCell(column - 1).setCellValue(value == null ? "" : String.valueOf(value));
					}
					processedRecords++;
					if (processedRecords % 1000 == 0) {
						logger.info("ErrorLog Report generation in progress. Records processed : {}", processedRecords);
						if (sheet instanceof SXSSFSheet) {
							((SXSSFSheet) sheet).flushRows(100);
						}
					}
				}

				workbook.write(out);
				// Cleanup SXSSF temp files.
				workbook.dispose();

				logger.info("Error Excel export completed successfully ");
				logger.info("ErrorLog Report generated successfully.  RecordsWritten : {}, TimeTaken(ms) : {}",
						processedRecords, System.currentTimeMillis() - startTime);

				return new ByteArrayInputStream(out.toByteArray());
			}

		} catch (DataAccessException e) {
			logger.info("DB error while fetching the Finnone error Records ", e);
			throw new RuntimeException("Database Error occured");
		} catch (IOException ex) {
			logger.info("Excel Genration Failed ", ex);
			throw new RuntimeException("Failed to genrate error file");
		}
	}

	/*
	
	*/

	private Map<String, Integer> validateExcelHeaders(Row headerRow) {

		if (headerRow == null) {
			throw new RuntimeException("Header row is missing in uploaded Excel.");
		}

		Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
		Set<String> actualHeaders = new LinkedHashSet<>();
		Set<String> duplicateHeaders = new LinkedHashSet<>();
		Set<String> extraHeaders = new LinkedHashSet<>();

		for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
			Cell cell = headerRow.getCell(columnIndex);

			if (cell == null) {
				continue;
			}
//				String rawHeader = formatter.formatCellValue(cell).trim();
			String rawHeader = getCellValue(cell);

			if (rawHeader == null || rawHeader.trim().isEmpty()) {
				continue;
			}
			String normalizedHeader = normalizeHeader(rawHeader);

			// Duplicate Header
			if (!actualHeaders.add(normalizedHeader)) {
				duplicateHeaders.add(rawHeader);
				continue;
			}
			// Extra Header
			if (!EXPECTED_HEADERS.contains(normalizedHeader)) {
				extraHeaders.add(rawHeader);
				continue;
			}
			// Header -> Column Index
			headerIndexMap.put(normalizedHeader, columnIndex);
		}

		// Missing Header Validation
		Set<String> missingHeaders = new LinkedHashSet<>(EXPECTED_HEADERS);
		missingHeaders.removeAll(actualHeaders);

		if (!missingHeaders.isEmpty() || !duplicateHeaders.isEmpty() || !extraHeaders.isEmpty()) {
			StringBuilder errorMessage = new StringBuilder();
			if (!missingHeaders.isEmpty()) {
				errorMessage.append("Missing Header(s): ").append(missingHeaders).append(". ");
			}
			if (!duplicateHeaders.isEmpty()) {
				errorMessage.append("Duplicate Header(s): ").append(duplicateHeaders).append(". ");
			}
			if (!extraHeaders.isEmpty()) {
				errorMessage.append("Extra Header(s): ").append(extraHeaders).append(". ");
			}
			throw new RuntimeException(errorMessage.toString().trim());
		}
		return headerIndexMap;
	}

	private String normalizeHeader(String header) {
		if (header == null) {
			return "";
		}
		return header.trim().replace("-", "_").replace("/", "_").replace(" ", "_").replaceAll("[^A-Za-z0-9]", "_")
				.replaceAll("_+", "_").replaceAll("^_|_$", "").toUpperCase();
	}

	private boolean isRowEmpty(Row row) {

		if (row == null)
			return true;

		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();

		if (firstCellNum == -1 || lastCellNum == -1)
			return true;

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

	private String getCellValue(Cell cell) {

		if (cell == null) {
			return null;
		}

		try {

			switch (cell.getCellType()) {

			case STRING:
				return cell.getStringCellValue();

			case NUMERIC:

				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getLocalDateTimeCellValue().toLocalDate().toString();
				}

				return BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();

			case BOOLEAN:
				return String.valueOf(cell.getBooleanCellValue());

			case FORMULA:

				CellValue cellValue = evaluator.evaluate(cell);

				if (cellValue == null) {
					return null;
				}

				switch (cellValue.getCellType()) {

				case STRING:
					return cellValue.getStringValue();

				case NUMERIC:

					if (DateUtil.isCellDateFormatted(cell)) {
//					return formatter.formatCellValue(cell);
						return cell.getLocalDateTimeCellValue().toLocalDate().toString();
					}

					return BigDecimal.valueOf(cellValue.getNumberValue()).stripTrailingZeros().toPlainString();

				case BOOLEAN:
					return String.valueOf(cellValue.getBooleanValue());

				case BLANK:
				case ERROR:
				default:
					return null;
				}

			case BLANK:
			case ERROR:
			case _NONE:
			default:
				return null;
			}
		} catch (Exception ex) {

			ex.printStackTrace();
			return null;

		}

	}

	private void validateMandatoryFields(Row row, Map<String, Integer> headerMap, DataFormatter formatter) {
		validateMandatory(row, headerMap, formatter, "EMPCODE", "Emp code");
//		validateMandatory(row, headerMap, formatter, "VSTS_CODE_COUNSELOR_SAP_CODE", "VSTS Code / Counselor SAP Code");

		// Add remaining mandatory columns here...
	}

	private void validateMandatory(Row row, Map<String, Integer> headerMap, DataFormatter formatter, String headerName,
			String fieldName) {

		Integer columnIndex = headerMap.get(headerName);
		if (columnIndex == null) {
			throw new RuntimeException(fieldName + " column is missing.");
		}
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
//			String value = formatter.formatCellValue(cell);
		String value = getCellValue(cell);

		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
	}

	private void validateExcelDuplicate(Row row, Map<String, Integer> headerMap, Set<String> duplicateSet,
			DataFormatter formatter) {
		String userId = getCellValue(row.getCell(headerMap.get("EMPCODE"))).trim();
		String duplicateKey = userId.toUpperCase();
		if (!duplicateSet.add(duplicateKey)) {
			throw new RuntimeException("Duplicate record found in Excel.");
		}
	}

	private OutsourceMasterError createError(Row row, String errorMassage, Integer rowNumber,
			Map<String, Integer> headerMap, DataFormatter formatter, String loginUser, String uploadId) {

		OutsourceMasterError m = new OutsourceMasterError();

//		m.setSrNo(getCellValue(row.getCell(headerMap.get("SR_NO"))));
		m.setEmpCode(getCellValue(row.getCell(headerMap.get("EMPCODE"))));
		m.setvSTSCodeCounselorSAPCode(getCellValue(row.getCell(headerMap.get("VSTS_CODE_COUNSELOR_SAP_CODE"))));
//		m.setOldVSTSCode(getCellValue(row.getCell(headerMap.get("OLD_VSTS_CODE"))));
		m.setExecutiveName(getCellValue(row.getCell(headerMap.get("EXECUTIVE_NAME"))));
//		m.setTotalSalary(getCellValue(row.getCell(headerMap.get("TOTAL_SALARY"))));
//		m.setTotalIncentive(getCellValue(row.getCell(headerMap.get("TOTAL_INCENTIVE"))));
//		m.setConveyanceforthemonth(getCellValue(row.getCell(headerMap.get("CONVEYANCE_FOR_THE_MONTH"))));
//		m.setAdditionalCost(getCellValue(row.getCell(headerMap.get("ADDITIONAL_COST"))));
//		m.setTotalPayout(getCellValue(row.getCell(headerMap.get("TOTAL_PAYOUT"))));
//		m.setMainDesignation(getCellValue(row.getCell(headerMap.get("MAIN_DESIGNATION"))));
//		m.setDesignation(getCellValue(row.getCell(headerMap.get("DESIGNATION"))));
//		m.setOldDesignation(getCellValue(row.getCell(headerMap.get("OLD_DESIGNATION"))));
//		m.setPrimaryProduct(getCellValue(row.getCell(headerMap.get("PRIMARY_PRODUCT"))));
//		m.setAgencyName(getCellValue(row.getCell(headerMap.get("AGENCY_NAME"))));
//		m.setLocation(getCellValue(row.getCell(headerMap.get("LOCATION"))));
//		m.setOldLocation(getCellValue(row.getCell(headerMap.get("OLD_LOCATION"))));
//		m.setZone(getCellValue(row.getCell(headerMap.get("ZONE"))));
//		m.seteDState(getCellValue(row.getCell(headerMap.get("ED_STATE"))));
//		m.seteDZone(getCellValue(row.getCell(headerMap.get("ED_ZONE"))));
//		m.setmISState(getCellValue(row.getCell(headerMap.get("MIS_STATE"))));
//		m.setcState(getCellValue(row.getCell(headerMap.get("C_STATE"))));
//		m.setMonth(getCellValue(row.getCell(headerMap.get("MONTH"))));
//		m.setRemark(getCellValue(row.getCell(headerMap.get("REMARK"))));
//		m.setiBox(getCellValue(row.getCell(headerMap.get("I_BOX"))));
//		m.setIprocessDesignation(getCellValue(row.getCell(headerMap.get("IPROCESS_DESIGNATION"))));
//		m.setdOJ(getCellValue(row.getCell(headerMap.get("DOJ"))));
//		m.setTopTierII(getCellValue(row.getCell(headerMap.get("TOP_TIER_II"))));
//		m.setOldTopTierII(getCellValue(row.getCell(headerMap.get("OLD_TOP_TIER_II"))));
//		m.setNoOfDay(getCellValue(row.getCell(headerMap.get("NO_OF_DAY"))));
//		m.setArrearDays(getCellValue(row.getCell(headerMap.get("ARREAR_DAYS"))));
//		m.setBand(getCellValue(row.getCell(headerMap.get("BAND"))));
//		m.setInvoiceNo(getCellValue(row.getCell(headerMap.get("INVOICE_NO"))));
//		m.setInvoiceDate(getCellValue(row.getCell(headerMap.get("INVOICE_DATE"))));
//		m.setInvoiceAmt(getCellValue(row.getCell(headerMap.get("INVOICE_AMT"))));
//		m.setInvoiceRecdOn(getCellValue(row.getCell(headerMap.get("INVOICE_RECD_ON"))));
//		m.setResigneddate(getCellValue(row.getCell(headerMap.get("RESIGNED_DATE"))));
//		m.setePF(getCellValue(row.getCell(headerMap.get("EPF"))));
//		m.seteESI(getCellValue(row.getCell(headerMap.get("EESI"))));
//		m.seteLWF(getCellValue(row.getCell(headerMap.get("ELWF"))));
//		m.setRoundingOffDueToEmployerContrToESIC(
//				getCellValue(row.getCell(headerMap.get("ROUNDING_OFF_DUE_TO_EMPLOYER_CONTR_TO_ESIC"))));
//		m.setRecoveryAmtRecd(getCellValue(row.getCell(headerMap.get("RECOVERY_AMT_RECD"))));
//		m.setNoticePayDed(getCellValue(row.getCell(headerMap.get("NOTICE_PAY_DED"))));
//		m.setTotal(getCellValue(row.getCell(headerMap.get("TOTAL"))));
//		m.setExGratiaBonus(getCellValue(row.getCell(headerMap.get("EX_GRATIA_BONUS"))));
//		m.setTotalWithIncentiveAndCON(getCellValue(row.getCell(headerMap.get("TOTAL_WITH_INCENTIVE_CON"))));
//		m.setServiceChgsOnTotalCost(getCellValue(row.getCell(headerMap.get("SERVICE_CHGS_2_1_ON_TOTAL_COST"))));
//		m.setTotalWithSC(getCellValue(row.getCell(headerMap.get("TOTAL_WITH_SC"))));
//		m.setgST(getCellValue(row.getCell(headerMap.get("GST_18"))));
//		m.setTotalBillAmt(getCellValue(row.getCell(headerMap.get("TOTAL_BILL_AMT"))));
//		m.setTotalSalaryAndIncentiveAndConv(getCellValue(row.getCell(headerMap.get("TOTAL_SALARY_INCENTIVE_CONV"))));
//		m.setAdditionalCost(getCellValue(row.getCell(headerMap.get("ADDITION_COST"))));
//		m.setAsPerIrProcessLocation(getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_LOCATION"))));
//		m.setAsPerIProcessState(getCellValue(row.getCell(headerMap.get("AS_PER_I_PROCESS_STATE"))));
//		m.setLotNo(getCellValue(row.getCell(headerMap.get("LOT_NO"))));
//		m.setIprocessGrossSalary(getCellValue(row.getCell(headerMap.get("I_PROCESS_GROSS_SALARY"))));
//		m.setSolId(getCellValue(row.getCell(headerMap.get("SOL_ID"))));
//
//		m.setMailFrom(getCellValue(row.getCell(headerMap.get("MAIL_FROM"))));
//
//		m.setNoOfCasesNew(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_NEW"))));
//		m.setLoanMnsNew(getCellValue(row.getCell(headerMap.get("LOAN_MNS_NEW"))));
//		m.setNoOfCasesUsed(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_USED"))));
//		m.setLoanMnsUsed(getCellValue(row.getCell(headerMap.get("LOAN_MNS_USED"))));
//		m.setNoOfCasesTotal(getCellValue(row.getCell(headerMap.get("NO_OF_CASES_TOTAL"))));
//		m.setLoanMnsTotal(getCellValue(row.getCell(headerMap.get("LOAN_MNS_TOTAL"))));

		m.setRowNumber(rowNumber);
		m.setErrorMessage(errorMassage);
		m.setCreatedBy(loginUser);
		m.setCreatedDate(new Date());
		m.setUploadId(uploadId);

		return m;
	}

	private void insertTempBatch(List<OutsourceMasterTemp> tempBatch,Set<String> deleteSet) {

		if (tempBatch == null || tempBatch.isEmpty()) {
			return;
		}

		try {
			
			if (deleteSet != null && !deleteSet.isEmpty()) {
				tempRepo.deletePendingByPartnerIds(StatusConstant.PENDING, deleteSet);
			}

			tempRepo.saveAll(tempBatch);

		} catch (DataAccessException ex) {

			logger.error("Error while inserting Temp batch.", ex);

			throw new RuntimeException("Unable to save uploaded records into Temp table.", ex);
		}
	}

	private void insertErrorBatch(List<OutsourceMasterError> errorBatch) {

		if (errorBatch == null || errorBatch.isEmpty()) {
			return;
		}

		try {

			errorRepo.saveAll(errorBatch);

		} catch (DataAccessException ex) {

			logger.error("Error while inserting Error batch.", ex);

			throw new RuntimeException("Unable to save error records.", ex);
		}
	}

	private String generateUploadId(String fileName) {

		// remove extension
		String baseName = fileName.replace(".xlsx", "").replace(".xls", "");

		// take first 3 characters
		String prefix = baseName.substring(0, Math.min(3, baseName.length())).toUpperCase();

		// timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		// random number
//			int random = ThreadLocalRandom.current().nextInt(1000, 9999);
		Long sequence = jdbcTemplate.queryForObject("SELECT OUTSOURCE_UPLOAD_ID_MST_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	/*
	
	*/

	@Transactional
	@Override
	public void createOutsourceMasterByMaker(OutsourceMasterDto dto, String username) {

		logger.info("Processing create Outsource master");

		try {

			if (mainRepo.existsByEmpCode(dto.getEmpCode())) {
				logger.warn("Record already approved for user id : {} ", dto.getEmpCode());
				throw new IllegalArgumentException("Record already Approved");
			}

			if (tempRepo.existsByEmpCodeAndStatus(dto.getEmpCode(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for user id : {} ", dto.getEmpCode());
				throw new IllegalArgumentException("Record already Pending ");
			}

			OutsourceMasterTemp temp = this.convertDtoToTemp(dto);
			temp.setStatus(StatusConstant.PENDING);
			temp.setCreatedBy(username);
			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);
			temp.setActionDate(new Date());
			temp.setActionUser(username);

			tempRepo.save(temp);

			logger.info(" {} Record saved in Temp Table with status pending. ", dto.getEmpCode());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the Outsource master Record in Temp Table", e);
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateOutsourceMasterByMaker(OutsourceMasterDto dto, String username) {

		try {

			logger.info("Maker {} Request Edit for Approval record of User Id {}", username, dto.getEmpCode());

			if (tempRepo.existsByEmpCodeAndStatus(username, StatusConstant.PENDING)) {

				logger.warn("User ID {} already Pending for Approval");

				throw new IllegalArgumentException("user Id " + dto.getEmpCode() + " alredy pending for Approval ");
			}

			OutsourceMaster main = mainRepo.findByEmpCode(dto.getEmpCode()).orElseThrow(() -> {

				logger.warn("Approved record not found for User Id: {}", dto.getEmpCode());

				return new ResourceNotFoundException("Approved record not found for User Id :" + dto.getEmpCode());
			});

			// update status in main
			// main.setStatus(StatusConstant.PENDING);
			// mainRepo.save(main);

			logger.info("Outsource Master Main Record status updated to PENDING for user Id : {}", dto.getEmpCode());

			// update or mapping Outsource Temp record
			OutsourceMasterTemp temp = this.convertDtoToTemp(dto);

			temp.setEmpCode(dto.getEmpCode());

			temp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			temp.setCreatedBy(main.getCreatedBy()); // 1st main record createdBy
			temp.setCreatedDate(main.getCreatedDate()); // 1st main record createdDate

			temp.setModifiedBy(username); // modified by whom
			temp.setModifiedDate(new Date()); // current modified date

			temp.setActionType(ActionConstant.UPDATE); // now Action taken
			temp.setActionUser(username); // Action taken by whom
			temp.setActionDate(new Date()); // Action taken Date

			tempRepo.save(temp);

			logger.info("user_Id {} successfully sent for Approval", dto.getEmpCode());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	@Transactional
	@Override
	public List<OutsourceMasterDto> getAllOutsourceMasterByChecker(String user) {

		try {

			logger.info("Fetching All Outsource Master but not created by checker : {}", user.toUpperCase());

			List<OutsourceMasterTemp> tempList = tempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING, user);

			if (tempList == null || tempList.size() < 0 || tempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<OutsourceMasterDto> dtoList = tempList.stream().map(this::convertTempToDto)
					.collect(Collectors.toList());

			logger.info("Returning {} pending record to checker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Transactional
	@Override
	public void updateOutsourceMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info("Processing checker action in Outsource master");

		try {

			if (requestPayload == null) {
				throw new IllegalArgumentException("Request payload is null");
			}

			String decision = requestPayload.getDecision().toUpperCase();
			String remark = requestPayload.getRemark();

			if (decision == null) {
				throw new IllegalArgumentException("Decision is null");
			}

			if (!StatusConstant.APPROVE.equalsIgnoreCase(decision)
					&& !StatusConstant.REJECTE.equalsIgnoreCase(decision)) {

				logger.error("Invalid decision received: {}", decision);
				throw new IllegalArgumentException("Invalid decision type. Use A for APPROVE and R for REJECT");
			}

			List<String> ids = requestPayload.getPrimaryIds();

			if (ids == null || ids.isEmpty()) {
				throw new IllegalArgumentException("Primary IDs list is empty");
			}

			List<String> StringIds = ids.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty())
					.collect(Collectors.toList());

			int batchSize = 900;
			List<String> pendingIds = new ArrayList<>();

			for (int i = 0; i < StringIds.size(); i += batchSize) {

				List<String> batch = StringIds.subList(i, Math.min(i + batchSize, StringIds.size()));

				List<String> result = tempRepo.findAllEmpCodesByEmpCodeInANDStatus(batch, StatusConstant.PENDING);

				pendingIds.addAll(result);
			}

			if (pendingIds.size() != StringIds.size()) {

				List<String> missingIds = new ArrayList<>(StringIds);
				missingIds.removeAll(pendingIds);

				logger.error("Pending record not found for user Id: {}", missingIds);

				throw new ResourceNotFoundException("Pending record not found for user Ids: " + missingIds);
			}

			// ================= DECIDE STATUS =================
			String status = decision.equalsIgnoreCase(StatusConstant.APPROVE) ? StatusConstant.APPROVE
					: StatusConstant.REJECTE;

			int totalUpdated = 0;

			// ================= BATCH OPERATION FOR BULK UPDATE =================

			for (int i = 0; i < pendingIds.size(); i += batchSize) {

				List<String> batch = pendingIds.subList(i, Math.min(i + batchSize, pendingIds.size()));

				int updated = tempRepo.updateStatusAndRemarksByEmpCodeInANDStatus(batch, status, remark,
						StatusConstant.PENDING);

				totalUpdated += updated;
			}

			// ================= =================
			if (StatusConstant.APPROVE.equals(status)) {
				logger.info("All user Id approved successfully. Count: {}", totalUpdated);
			} else {
				logger.info("All user Id Codes rejected successfully. Count: {}", totalUpdated);
			}

			logger.info("Checker update status successfully");

		} catch (DataAccessException e) {
			logger.error("Database error while updating pending Outsource records", e);
			throw e;

		}

	}

	@Transactional
	@Override
	public List<OutsourceMasterDto> getOutsourceMasterByStatus(String statusType) {

		logger.info("Fetching  Outsource Master record for maker");

		List<OutsourceMasterDto> dtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching Outsource Master Approve record ");
				mainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(main -> {

					OutsourceMasterDto dto = this.convertMainToDto(main);
					dtoList.add(dto);
				});
				logger.info("Approved Outsource record Count : {}", dtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching Outsource Master Pending record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					OutsourceMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Peinding Outsource record Count : {}", dtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching Outsource Master Reject record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					OutsourceMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Reject Outsource record Count : {}", dtoList.size());
				break;

			case "All":

				logger.info("Fetching All Outsource Master Approve record ");

				String sql = "SELECT * FROM (	 "
						+ "			SELECT EMP_CODE, VSTSCODE_COUNSLR_SAPCODE, EXECUTIVE_NAME, STATUS,  "
						+ "			GREATEST(	NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_OUTSOURCE_MST WHERE STATUS = 'A' 	 " + "			 "
						+ "			UNION ALL  " + "			 "
						+ "			SELECT EMP_CODE, VSTSCODE_COUNSLR_SAPCODE, EXECUTIVE_NAME, STATUS ,  "
						+ "			GREATEST(   NVL(ACTION_DATE, DATE '1900-01-01'),    "
						+ "						NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_OUTSOURCE_MST_TEMP  WHERE STATUS = 'P'   " + "			)  "
						+ "ORDER BY sort_date DESC";

				List<OutsourceMasterDto> ListOfDto = jdbcTemplate.query(con -> {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(BATCH_SIZE);
					return ps;
				}, (rs, rowNum) -> {

					OutsourceMasterDto m = new OutsourceMasterDto();

					m.setEmpCode(rs.getString("EMP_CODE"));
					m.setvSTSCodeCounselorSAPCode(rs.getString("VSTSCODE_COUNSLR_SAPCODE"));
					m.setExecutiveName(rs.getString("EXECUTIVE_NAME"));

					m.setStatus(rs.getString("STATUS"));

					return m;
				});

				dtoList.addAll(ListOfDto);

				logger.info("All Outsource record Count : {}", dtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (dtoList == null || dtoList.size() < 0 || dtoList.isEmpty()) {
				logger.warn("No Outsource Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All Outsource Master record to maker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;

		}
	}

}

package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
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
import com.icici.dma.dto.ILensChannelMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.DateFormatter;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.ILensChannelMaster;
import com.icici.dma.model.ILensChannelMasterError;
import com.icici.dma.model.ILensChannelMasterTemp;
import com.icici.dma.repository.ILensChannelMasterMainRepository;
import com.icici.dma.repository.ILensChannelMasterTempRepository;
import com.icici.dma.service.Vhl_iLensChannelMasterService;

@Service
public class Vhl_iLensChannelMasterServiceImpl implements Vhl_iLensChannelMasterService {

	private static final Logger logger = LogManager.getLogger(Vhl_iLensChannelMasterServiceImpl.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public ILensChannelMasterTempRepository tempRepo;

	@Autowired
	public ILensChannelMasterMainRepository mainRepo;

	private static final Set<String> EXPECTED_HEADERS = new LinkedHashSet<>(
			Arrays.asList("S_NO", "USER_ID", "USER_NAME", "ID_CREATION_DATE", "STATUS", "EMAIL_ID", "MOBILE_NUMBER",
					"AGENCY_ID", "AGENCY_TYPE", "VPTS_ID", "VPTS_APPROVAL_STATUS", "ANNUAL_REVIEW_DUE_DATE",
					"MSME_REGISTERED", "UDYOG_AADHAR_NUMBER", "AGENCY_PAN_NUMBER", "USER_GROUP", "CHANNEL_NAME",
					"PRODUCT_TYPE", "BASE_CPC_PROCESS_SHOP", "MAPPED_EMPLOYEE_ID", "MAPPED_EMPLOYEE_NAME",
					"INBOUND_OUTBOUND_TYPE", "MAPPED_SOL_IDS", "COUNSELLOR_IDS", "TSM_ID", "TSM_NAME", "CHILD_ALLOWED",
					"OPERATING_LOCATIONS", "ROLE_CODE", "VSTS_ID", "VSTS_NAME", "VSTS_STATUS", "PRODUCT"));

	private static final int BATCH_SIZE = 1000;

	private final DataFormatter formatter = new DataFormatter();

	/*
	
	*/

	@Transactional
	@Override
	public Map<String, Object> uploadILensChannelMaster(MultipartFile file, String loginUser) {

		logger.info("iLens upload started by user : {}", loginUser);
		Map<String, Object> response = new LinkedHashMap<>();
		Map<String, Integer> headerMap = new LinkedHashMap<>();
		List<ILensChannelMasterTemp> tempBatch = new ArrayList<>(BATCH_SIZE);
		List<ILensChannelMasterError> errorBatch = new ArrayList<>(BATCH_SIZE);
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
			logger.info("iLens Channel file validation  complated");
			try (Workbook workbook = WorkbookFactory.create(file.getInputStream());) {

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

						String userId = getCellValue(row.getCell(headerMap.get("USER_ID")));

						// only read those record has User id start with 'SE'
						if (userId == null || !userId.toUpperCase().startsWith("SE")) {
//							continue;User ID
							if (userId == null) {
								throw new RuntimeException("UserId is mandatory");
							}
							throw new RuntimeException("UserId is not Start with \'SE\'");

						}

						// Mandatory field validation
						validateMandatoryFields(row, headerMap, formatter);

						// Validate duplicate row in excel
						validateExcelDuplicate(row, headerMap, duplicateSet, formatter);

						// map row data to temp
						ILensChannelMasterTemp temp = new ILensChannelMasterTemp();

						// user id
						temp.setUserID(userId);

						// delete already pending
						deleteSet.add(userId);

						// sr no
						String srNocellValue = getCellValue(row.getCell(headerMap.get("S_NO")));
						temp.setSrNo(srNocellValue != null ? Long.valueOf(srNocellValue) : null);

						String UserNamecellValue = getCellValue(row.getCell(headerMap.get("USER_NAME")));
						temp.setUserName(UserNamecellValue != null ? UserNamecellValue : null);

						String iDCreationDateString = getCellValue(row.getCell(headerMap.get("ID_CREATION_DATE")));

//						temp.setiD_Creation_Date(iD_Creation_DateString != null ? LocalDateTime.parse(iD_Creation_DateString,
//								DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);
						temp.setiDCreationDate(
								iDCreationDateString != null ? DateFormatter.parseToLocalDateTime(iDCreationDateString)
										: null);

						temp.setStatus(getCellValue(row.getCell(headerMap.get("STATUS"))));
						temp.setEmailID(getCellValue(row.getCell(headerMap.get("EMAIL_ID"))));
						temp.setMobileNumber(getCellValue(row.getCell(headerMap.get("MOBILE_NUMBER"))));
						temp.setAgencyID(getCellValue(row.getCell(headerMap.get("AGENCY_ID"))));
						temp.setAgencyType(getCellValue(row.getCell(headerMap.get("AGENCY_TYPE"))));
						temp.setvPTSID(getCellValue(row.getCell(headerMap.get("VPTS_ID"))));
						temp.setvPTSApprovalStatus(getCellValue(row.getCell(headerMap.get("VPTS_APPROVAL_STATUS"))));
						String AnnualReviewDateString = getCellValue(
								row.getCell(headerMap.get("ANNUAL_REVIEW_DUE_DATE")));

//						temp.setAnnual_Review_Due_Date( 
//								AnnualReviewDateString != null ? 
//								LocalDate.parse(AnnualReviewDateString,DateTimeFormatter.ofPattern("dd MMM yyyy")) 
//								: null);

						temp.setAnnualReviewDueDate(
								AnnualReviewDateString != null ? DateFormatter.parseToLocalDate(AnnualReviewDateString)
										: null);

						temp.setmSMERegistered(getCellValue(row.getCell(headerMap.get("MSME_REGISTERED"))));
						temp.setUdyogAadharNumber(getCellValue(row.getCell(headerMap.get("UDYOG_AADHAR_NUMBER"))));
						temp.setAgencyPanNumber(getCellValue(row.getCell(headerMap.get("AGENCY_PAN_NUMBER"))));
						temp.setUserGroup(getCellValue(row.getCell(headerMap.get("USER_GROUP"))));
						temp.setChannelName(getCellValue(row.getCell(headerMap.get("CHANNEL_NAME"))));
						temp.setProductType(getCellValue(row.getCell(headerMap.get("PRODUCT_TYPE"))));
						temp.setBaseCPCProcessShop(getCellValue(row.getCell(headerMap.get("BASE_CPC_PROCESS_SHOP"))));
						temp.setMappedEmployeeID(getCellValue(row.getCell(headerMap.get("MAPPED_EMPLOYEE_ID"))));
						temp.setMappedEmployeeName(getCellValue(row.getCell(headerMap.get("MAPPED_EMPLOYEE_NAME"))));
						temp.setInboundOutBoundType(getCellValue(row.getCell(headerMap.get("INBOUND_OUTBOUND_TYPE"))));
						temp.setMappedSolIDs(getCellValue(row.getCell(headerMap.get("MAPPED_SOL_IDS"))));
						temp.setCounsellorIDs(getCellValue(row.getCell(headerMap.get("COUNSELLOR_IDS"))));
						temp.settSMID(getCellValue(row.getCell(headerMap.get("TSM_ID"))));
						temp.settSMName(getCellValue(row.getCell(headerMap.get("TSM_NAME"))));
						temp.setChildAllowed(getCellValue(row.getCell(headerMap.get("CHILD_ALLOWED"))));
						temp.setOperatingLocations(getCellValue(row.getCell(headerMap.get("OPERATING_LOCATIONS"))));
						temp.setRoleCode(getCellValue(row.getCell(headerMap.get("ROLE_CODE"))));
						temp.setvSTSID(getCellValue(row.getCell(headerMap.get("VSTS_ID"))));
						temp.setvSTSName(getCellValue(row.getCell(headerMap.get("VSTS_NAME"))));
						temp.setvSTSStatus(getCellValue(row.getCell(headerMap.get("VSTS_STATUS"))));
						temp.setProduct(getCellValue(row.getCell(headerMap.get("PRODUCT"))));

						temp.setStatusA("P");
						temp.setCreatedBy(loginUser);
						temp.setCreatedDate(new Date());
						temp.setActionType("I");
						temp.setActionUser(loginUser);
						temp.setActionDate(new Date());
						temp.setUploadId(uploadId);
						temp.setFileName(file.getOriginalFilename());

						tempBatch.add(temp);
						successCount++;

					} catch (RuntimeException e) {
						String errorMassage = e.getMessage();
						ILensChannelMasterError error = createError(row, errorMassage, rowNumber, headerMap, formatter,
								loginUser, uploadId);
						errorBatch.add(error);
						errorCount++;
//						continue;
					}

					if (tempBatch.size() >= BATCH_SIZE) {
						// jdbcTemplate.batchUpdate(...)
						insertTempBatch(tempBatch, deleteSet);
						deleteSet.clear();
						tempBatch.clear();
					}

					if (errorBatch.size() >= BATCH_SIZE) {
						// jdbcTemplate.batchUpdate(...)
						insertErrorBatch(errorBatch);
						errorBatch.clear();
					}

					if (totalRecords % BATCH_SIZE == 0) {
						logger.info("iLens Channel - Record Read Count : {}", totalRecords);
					}

				}

				// save remaining batch
				if (!tempBatch.isEmpty()) {
					// jdbcTemplate.batchUpdate(...)
					insertTempBatch(tempBatch, deleteSet);
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
			throw new RuntimeException("Ilens Channel Master Upload Failed: " + e.getMessage());
		}
		return response;

	}

	@Transactional
	@Override
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Error Report generation started : Ilens_Channel_Master");
		logger.info("Error Report generation started BY: " + user);

		int processedRecords = 0;
		long startTime = System.currentTimeMillis();

		String sql = " SELECT SR_NO AS \"S.No.\", USER_ID AS \"User ID\", USER_NAME AS \"User Name\", "
				+ "ID_CREATION_DATE AS \"ID Creation Date\", " + "STATUS AS Status, " + "EMAIL_ID AS \"Email ID\", "
				+ "MOBILE_NUMBER AS \"Mobile Number\", " + "AGENCY_ID AS \"Agency ID\", "
				+ "AGENCY_TYPE AS \"Agency Type\", " + "VPTS_ID AS \"VPTS ID\", "
				+ "VPTS_APPROVAL_STATUS AS \"VPTS Approval Status\", "
				+ "ANNUAL_REVIEW_DUE_DATE AS \"Annual Review Due Date\", " + "MSME_REGISTERED AS \"MSME Registered\", "
				+ "UDYOG_AADHAR_NUMBER AS \"Udyog Aadhar Number\", " + "AGENCY_PAN_NUMBER AS \"Agency PAN Number\", "
				+ "USER_GROUP AS \"User Group\", " + "CHANNEL_NAME AS \"Channel Name\", "
				+ "PRODUCT_TYPE AS \"Product Type\", " + "BASE_CPC_PROCESS_SHOP AS \"Base CPC / Process Shop2\", "
				+ "MAPPED_EMPLOYEE_ID AS \"Mapped Employee ID\", "
				+ "MAPPED_EMPLOYEE_NAME AS \"Mapped Employee Name\", "
				+ "INBOUND_OUTBOUND_TYPE AS \"Inbound/Outbound Type\", " + "MAPPED_SOL_IDS AS \"Mapped Sol IDs\", "
				+ "COUNSELLOR_IDS AS \"Counsellor IDs\", " + "TSM_ID AS \"TSM ID\", " + "TSM_NAME AS \"TSM Name\", "
				+ "CHILD_ALLOWED AS \"Child Allowed\", " + "OPERATING_LOCATIONS AS \"Operating Locations\", "
				+ "ROLE_CODE AS \"Role Code\", " + "VSTS_ID AS \"VSTS ID\", " + "VSTS_NAME AS \"VSTS Name\", "
				+ "VSTS_STATUS AS \"VSTS Status\", " + "PRODUCT AS \"Product\", " + "ROW_NUMBER AS \"Row Number\", "
				+ "ERROR_MESSAGE AS \"Error Message\" " + "FROM TM_VHL_ILENS_CHANNEL_MST_ERROR WHERE UPLOAD_ID = ( "
				+ "		SELECT UPLOAD_ID  FROM ( " + "			SELECT UPLOAD_ID  "
				+ "			FROM TM_VHL_ILENS_CHANNEL_MST_ERROR  WHERE CREATED_BY = ?  ORDER BY CREATED_DATE DESC  )  "
				+ "WHERE ROWNUM = 1 )";

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
					if (processedRecords % 5000 == 0) {
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
//			String rawHeader = formatter.formatCellValue(cell).trim();
			String rawHeader = getCellValue(cell).trim();

			if (rawHeader.isEmpty()) {
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

	private void validateMandatoryFields(Row row, Map<String, Integer> headerMap, DataFormatter formatter) {
		validateMandatory(row, headerMap, formatter, "USER_ID", "User ID");
		validateMandatory(row, headerMap, formatter, "AGENCY_ID", "Agency ID");

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
//		String value = formatter.formatCellValue(cell);
		String value = getCellValue(cell);

		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
	}

	private void validateExcelDuplicate(Row row, Map<String, Integer> headerMap, Set<String> duplicateSet,
			DataFormatter formatter) {
		String userId = getCellValue(row.getCell(headerMap.get("USER_ID"))).trim();
		String duplicateKey = userId.toUpperCase();
		if (!duplicateSet.add(duplicateKey)) {
			throw new RuntimeException("Duplicate record found in Excel.");
		}
	}

	private ILensChannelMasterError createError(Row row, String errorMassage, Integer rowNumber,
			Map<String, Integer> headerMap, DataFormatter formatter, String loginUser, String uploadId) {

		ILensChannelMasterError error = new ILensChannelMasterError();

		error.setRowNumber(rowNumber);
		error.setErrorMessage(errorMassage);
		error.setCreatedBy(loginUser);
		error.setCreatedDate(new Date());
		error.setUploadId(uploadId);

		error.setSr_No(getCellValue(row.getCell(headerMap.get("S_NO"))));
		error.setUser_ID(getCellValue(row.getCell(headerMap.get("USER_ID"))));
		error.setUser_Name(getCellValue(row.getCell(headerMap.get("USER_NAME"))));
		error.setiD_Creation_Date(getCellValue(row.getCell(headerMap.get("ID_CREATION_DATE"))));
		error.setStatus(getCellValue(row.getCell(headerMap.get("STATUS"))));
		error.setEmail_ID(getCellValue(row.getCell(headerMap.get("EMAIL_ID"))));
		error.setMobile_Number(getCellValue(row.getCell(headerMap.get("MOBILE_NUMBER"))));
		error.setAgency_ID(getCellValue(row.getCell(headerMap.get("AGENCY_ID"))));
		error.setAgency_Type(getCellValue(row.getCell(headerMap.get("AGENCY_TYPE"))));
		error.setvPTS_ID(getCellValue(row.getCell(headerMap.get("VPTS_ID"))));
		error.setvPTS_Approval_Status(getCellValue(row.getCell(headerMap.get("VPTS_APPROVAL_STATUS"))));
		error.setAnnual_Review_Due_Date(getCellValue(row.getCell(headerMap.get("ANNUAL_REVIEW_DUE_DATE"))));
		error.setmSME_Registered(getCellValue(row.getCell(headerMap.get("MSME_REGISTERED"))));
		error.setUdyog_Aadhar_Number(getCellValue(row.getCell(headerMap.get("UDYOG_AADHAR_NUMBER"))));
		error.setAgency_Pan_Number(getCellValue(row.getCell(headerMap.get("AGENCY_PAN_NUMBER"))));
		error.setUser_Group(getCellValue(row.getCell(headerMap.get("USER_GROUP"))));
		error.setChannel_Name(getCellValue(row.getCell(headerMap.get("CHANNEL_NAME"))));
		error.setProduct_Type(getCellValue(row.getCell(headerMap.get("PRODUCT_TYPE"))));
		error.setBase_CPC_Process_Shop(getCellValue(row.getCell(headerMap.get("BASE_CPC_PROCESS_SHOP"))));
		error.setMapped_Employee_ID(getCellValue(row.getCell(headerMap.get("MAPPED_EMPLOYEE_ID"))));
		error.setMapped_Employee_Name(getCellValue(row.getCell(headerMap.get("MAPPED_EMPLOYEE_NAME"))));
		error.setInbound_OutBound_Type(getCellValue(row.getCell(headerMap.get("INBOUND_OUTBOUND_TYPE"))));
		error.setMapped_Sol_IDs(getCellValue(row.getCell(headerMap.get("MAPPED_SOL_IDS"))));
		error.setCounsellor_IDs(getCellValue(row.getCell(headerMap.get("COUNSELLOR_IDS"))));
		error.settSM_ID(getCellValue(row.getCell(headerMap.get("TSM_ID"))));
		error.settSM_Name(getCellValue(row.getCell(headerMap.get("TSM_NAME"))));
		error.setChild_Allowed(getCellValue(row.getCell(headerMap.get("CHILD_ALLOWED"))));
		error.setOperating_Locations(getCellValue(row.getCell(headerMap.get("OPERATING_LOCATIONS"))));
		error.setRole_Code(getCellValue(row.getCell(headerMap.get("ROLE_CODE"))));
		error.setvSTS_ID(getCellValue(row.getCell(headerMap.get("VSTS_ID"))));
		error.setvSTS_Name(getCellValue(row.getCell(headerMap.get("VSTS_NAME"))));
		error.setvSTS_Status(getCellValue(row.getCell(headerMap.get("VSTS_STATUS"))));
		error.setProduct(getCellValue(row.getCell(headerMap.get("PRODUCT"))));

		return error;
	}

	private void insertTempBatch(List<ILensChannelMasterTemp> tempBatch, Set<String> deleteSet) {

		if (tempBatch == null || tempBatch.isEmpty()) {
			return;
		}

		String sql = "INSERT INTO TM_VHL_ILENS_CHANNEL_MST_TEMP ( TEMP_ID, "
				+ "SR_NO, USER_ID, USER_NAME, ID_CREATION_DATE, STATUS, EMAIL_ID, "
				+ "MOBILE_NUMBER, AGENCY_ID, AGENCY_TYPE, VPTS_ID, VPTS_APPROVAL_STATUS, ANNUAL_REVIEW_DUE_DATE, MSME_REGISTERED, "
				+ "UDYOG_AADHAR_NUMBER, AGENCY_PAN_NUMBER, USER_GROUP, CHANNEL_NAME, "
				+ "PRODUCT_TYPE, BASE_CPC_PROCESS_SHOP, MAPPED_EMPLOYEE_ID, MAPPED_EMPLOYEE_NAME, "
				+ "INBOUND_OUTBOUND_TYPE, MAPPED_SOL_IDS, COUNSELLOR_IDS, TSM_ID, TSM_NAME, "
				+ "CHILD_ALLOWED, OPERATING_LOCATIONS, ROLE_CODE, VSTS_ID, VSTS_NAME, " + "VSTS_STATUS, PRODUCT,"
				+ "STATUS_A, CREATED_BY, CREATED_DATE, ACTION_TYPE, ACTION_USER, ACTION_DATE, UPLOAD_ID, FILE_NAME) "
				+ "VALUES (ILENS_CHANNEL_TEMP_ID_SEQ.NEXTVAL, " + "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , "
				+ "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " + "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , "
				+ "? , ? , ? , ? , ? , ? , ? , ? , ? , ? , " + "? )";

		try {

			if (deleteSet != null && !deleteSet.isEmpty()) {
				tempRepo.deletePendingByPartnerIds(StatusConstant.PENDING, deleteSet);
			}

			jdbcTemplate.batchUpdate(sql, tempBatch, BATCH_SIZE, (ps, entity) -> {

				ps.setLong(1, entity.getSrNo());
				ps.setString(2, entity.getUserID());
				ps.setString(3, entity.getUserName());

//				ps.setTimestamp(4, entity.getiD_Creation_Date());

				if (entity.getiDCreationDate() == null) {
					ps.setNull(4, Types.DATE);
				} else {
					ps.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getiDCreationDate()));
				}

				ps.setString(5, entity.getStatus());
				ps.setString(6, entity.getEmailID());

				ps.setString(7, entity.getMobileNumber());
				ps.setString(8, entity.getAgencyID());
				ps.setString(9, entity.getAgencyType());
				ps.setString(10, entity.getvPTSID());
				ps.setString(11, entity.getvPTSApprovalStatus());
//				ps.setTimestamp(12, entity.getAnnualReviewDueDate());

				if (entity.getAnnualReviewDueDate() == null) {
					ps.setNull(12, Types.DATE);
				} else {
					ps.setDate(12, java.sql.Date.valueOf(entity.getAnnualReviewDueDate()));
				}

				ps.setString(13, entity.getmSMERegistered());
				ps.setString(14, entity.getUdyogAadharNumber());
				ps.setString(15, entity.getAgencyPanNumber());
				ps.setString(16, entity.getUserGroup());
				ps.setString(17, entity.getChannelName());
				ps.setString(18, entity.getProductType());
				ps.setString(19, entity.getBaseCPCProcessShop());
				ps.setString(20, entity.getMappedEmployeeID());
				ps.setString(21, entity.getMappedEmployeeName());
				ps.setString(22, entity.getInboundOutBoundType());
				ps.setString(23, entity.getMappedSolIDs());
				ps.setString(24, entity.getCounsellorIDs());
				ps.setString(25, entity.gettSMID());
				ps.setString(26, entity.gettSMName());
				ps.setString(27, entity.getChildAllowed());
				ps.setString(28, entity.getOperatingLocations());
				ps.setString(29, entity.getRoleCode());
				ps.setString(30, entity.getvSTSID());
				ps.setString(31, entity.getvSTSName());
				ps.setString(32, entity.getvSTSStatus());
				ps.setString(33, entity.getProduct());

				ps.setString(34, entity.getStatusA());
				ps.setString(35, entity.getCreatedBy());
//				ps.setString(36, entity.getCreatedDate());

				if (entity.getCreatedDate() == null) {
					ps.setNull(36, Types.DATE);
				} else {
					ps.setDate(36, new java.sql.Date(entity.getCreatedDate().getTime()));
				}

				ps.setString(37, entity.getActionType());
				ps.setString(38, entity.getActionUser());
//				ps.setString(39, entity.getActionDate());

				if (entity.getActionDate() == null) {
					ps.setNull(39, Types.DATE);
				} else {
					ps.setDate(39, new java.sql.Date(entity.getActionDate().getTime()));
				}

				ps.setString(40, entity.getUploadId());
				ps.setString(41, entity.getFileName());

			});

		} catch (DataAccessException ex) {

			logger.error("Error while inserting Temp batch.", ex);

			throw new RuntimeException("Unable to save uploaded records into Temp table.", ex);
		}
	}

	private void insertErrorBatch(List<ILensChannelMasterError> errorBatch) {

		if (errorBatch == null || errorBatch.isEmpty()) {
			return;
		}

		String sql = "INSERT INTO TM_VHL_ILENS_CHANNEL_MST_ERROR ( ERROR_ID, "
				+ "SR_NO, USER_ID, USER_NAME, ID_CREATION_DATE, STATUS, EMAIL_ID, "
				+ "MOBILE_NUMBER, AGENCY_ID, AGENCY_TYPE, VPTS_ID, VPTS_APPROVAL_STATUS, ANNUAL_REVIEW_DUE_DATE, MSME_REGISTERED, "
				+ "UDYOG_AADHAR_NUMBER, AGENCY_PAN_NUMBER, USER_GROUP, CHANNEL_NAME, "
				+ "PRODUCT_TYPE, BASE_CPC_PROCESS_SHOP, MAPPED_EMPLOYEE_ID, MAPPED_EMPLOYEE_NAME, "
				+ "INBOUND_OUTBOUND_TYPE, MAPPED_SOL_IDS, COUNSELLOR_IDS, TSM_ID, TSM_NAME, "
				+ "CHILD_ALLOWED, OPERATING_LOCATIONS, ROLE_CODE, VSTS_ID, VSTS_NAME, " + "VSTS_STATUS, PRODUCT, "
				+ "ROW_NUMBER, ERROR_MESSAGE, UPLOAD_ID, CREATED_BY, CREATED_DATE ) "
				+ "VALUES (ILENS_CHANNEL_ERROR_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?)";

		try {

			jdbcTemplate.batchUpdate(sql, errorBatch, BATCH_SIZE, (ps, entity) -> {

				ps.setString(1, entity.getSr_No());
				ps.setString(2, entity.getUser_ID());
				ps.setString(3, entity.getUser_Name());
				ps.setString(4, entity.getiD_Creation_Date());
				ps.setString(5, entity.getStatus());
				ps.setString(6, entity.getEmail_ID());

				ps.setString(7, entity.getMobile_Number());
				ps.setString(8, entity.getAgency_ID());
				ps.setString(9, entity.getAgency_Type());
				ps.setString(10, entity.getvPTS_ID());
				ps.setString(11, entity.getvPTS_Approval_Status());
				ps.setString(12, entity.getAnnual_Review_Due_Date());
				ps.setString(13, entity.getmSME_Registered());

				ps.setString(14, entity.getUdyog_Aadhar_Number());
				ps.setString(15, entity.getAgency_Pan_Number());
				ps.setString(16, entity.getUser_Group());
				ps.setString(17, entity.getChannel_Name());

				ps.setString(18, entity.getProduct_Type());
				ps.setString(19, entity.getBase_CPC_Process_Shop());
				ps.setString(20, entity.getMapped_Employee_ID());
				ps.setString(21, entity.getMapped_Employee_Name());

				ps.setString(22, entity.getInbound_OutBound_Type());
				ps.setString(23, entity.getMapped_Sol_IDs());
				ps.setString(24, entity.getCounsellor_IDs());
				ps.setString(25, entity.gettSM_ID());
				ps.setString(26, entity.gettSM_Name());

				ps.setString(27, entity.getChild_Allowed());
				ps.setString(28, entity.getOperating_Locations());
				ps.setString(29, entity.getRole_Code());
				ps.setString(30, entity.getvSTS_ID());
				ps.setString(31, entity.getvSTS_Name());

				ps.setString(32, entity.getvSTS_Status());
				ps.setString(33, entity.getProduct());

				ps.setInt(34, entity.getRowNumber());
				ps.setString(35, entity.getErrorMessage());
				ps.setString(36, entity.getUploadId());

				ps.setString(37, entity.getCreatedBy());

				if (entity.getCreatedDate() == null) {
					ps.setNull(38, Types.DATE);
				} else {
					ps.setDate(38, new java.sql.Date(entity.getCreatedDate().getTime()));
				}

			});

		} catch (DataAccessException ex) {

			logger.error("Error while inserting Error batch.", ex);

			throw new RuntimeException("Unable to save error records.", ex);
		}
	}

	private String getCellValue(Cell cell) {

		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {

		case STRING:
			String stringValue = cell.getStringCellValue();

			return stringValue == null ? null : stringValue.trim();

		case NUMERIC:

			if (DateUtil.isCellDateFormatted(cell)) {
				String formatCellValue = formatter.formatCellValue(cell).trim();
				return formatCellValue;
			}

			return BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		case FORMULA:

			String cellValue = formatter.formatCellValue(cell).trim();
//			throw new RuntimeException("Formula cells are not allowed.");
			return cellValue;

		case ERROR:
//			throw new RuntimeException("Invalid Excel cell.");
			return null;

		case BLANK:
			return null;

		case _NONE:
			return null;
		default:
			return null;
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
//		int random = ThreadLocalRandom.current().nextInt(1000, 9999);
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_ILENS_CHANNEL_MST_SEQ.NEXTVAL FROM DUAL",
				long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	/*
	
	*/

	@Transactional
	@Override
	public void createILensChannelMasterByMaker(ILensChannelMasterDto dto, String username) {

		logger.info("Processing create ilens Channel");

		try {

			if (mainRepo.existsByUserID(dto.getUserID())) {
				logger.warn("Record already approved for user id : {} ", dto.getUserID());
				throw new IllegalArgumentException("Record already Approved");
			}

			if (tempRepo.existsByUserIDAndStatusA(dto.getUserID(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for user id : {} ", dto.getUserID());
				throw new IllegalArgumentException("Record already Pending ");
			}

			ILensChannelMasterTemp temp = this.convertDtoToTemp(dto);
			temp.setStatusA(StatusConstant.PENDING);
			temp.setCreatedBy(username);
			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);
			temp.setActionDate(new Date());
			temp.setActionUser(username);

			tempRepo.save(temp);

			logger.info(" {} Record saved in Temp Table with status pending. ", dto.getUserID());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the iLens channel master Record in Temp Table", e);
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateILensChannelMasterByMaker(ILensChannelMasterDto dto, String username) {

		try {

			logger.info("Maker {} Request Edit for Approval record of User Id {}", username, dto.getUserID());

			if (tempRepo.existsByUserIDAndStatusA(username, StatusConstant.PENDING)) {

				logger.warn("User ID {} already Pending for Approval");

				throw new IllegalArgumentException("user Id " + dto.getUserID() + " alredy pending for Approval ");
			}

			ILensChannelMaster main = mainRepo.findByUserID(dto.getUserID()).orElseThrow(() -> {

				logger.warn("Approved record not found for User Id: {}", dto.getUserID());

				return new ResourceNotFoundException("Approved record not found for User Id :" + dto.getUserID());
			});

			// update status in main
			// main.setStatus(StatusConstant.PENDING);
			// mainRepo.save(main);

			logger.info("iLens Channel Master Main Record status updated to PENDING for user Id : {}", dto.getUserID());

			// update or mapping ilens channel Temp record
			ILensChannelMasterTemp temp = this.convertDtoToTemp(dto);

			temp.setUserID(dto.getUserID());

			temp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			temp.setCreatedBy(main.getCreatedBy()); // 1st main record createdBy
			temp.setCreatedDate(main.getCreatedDate()); // 1st main record createdDate

			temp.setModifiedBy(username); // modified by whom
			temp.setModifiedDate(new Date()); // current modified date

			temp.setActionType(ActionConstant.UPDATE); // now Action taken
			temp.setActionUser(username); // Action taken by whom
			temp.setActionDate(new Date()); // Action taken Date

			tempRepo.save(temp);

			logger.info("user_Id {} successfully sent for Approval", dto.getUserID());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	@Transactional
	@Override
	public List<ILensChannelMasterDto> getAlliLensChannelMasterByChecker(String user) {

		try {

			logger.info("Fetching All iLens channel Master but not created by checker : {}", user.toUpperCase());

			List<ILensChannelMasterTemp> tempList = tempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING,
					user);

			if (tempList == null || tempList.size() < 0 || tempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<ILensChannelMasterDto> dtoList = tempList.stream().map(this::convertTempToDto)
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
	public void updateiLensChannelMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info("Processing checker action in ilens channel master");

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

				List<String> result = tempRepo.findAllUserIDsByUserIDInANDStatusA(batch, StatusConstant.PENDING);

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

				int updated = tempRepo.updateStatusAndRemarksByUserIDInANDStatusA(batch, status, remark,
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
			logger.error("Database error while updating pending iLens channel records", e);
			throw e;

		}

	}

	@Transactional
	@Override
	public List<ILensChannelMasterDto> getiLensChannelMasterByStatus(String statusType) {

		logger.info("Fetching  ilens channel Master record for maker");

		List<ILensChannelMasterDto> dtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching ilens Channel Master Approve record ");
				mainRepo.findAllByStatusA(StatusConstant.APPROVE).forEach(main -> {

					ILensChannelMasterDto dto = this.convertMainToDto(main);
					dtoList.add(dto);
				});
				logger.info("Approved ilens Channel record Count : {}", dtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching ilens Channel Master Pending record ");
				tempRepo.findAllByStatusA(StatusConstant.PENDING).forEach(temp -> {

					ILensChannelMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Peinding ilens Channel record Count : {}", dtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching ilens Channel Master Reject record ");
				tempRepo.findAllByStatusA(StatusConstant.PENDING).forEach(temp -> {

					ILensChannelMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Reject ilens Channel record Count : {}", dtoList.size());
				break;

			case "All":

				logger.info("Fetching All ilens Channel Master Approve record ");

				String sql = "SELECT * FROM (	 "
						+ "			SELECT SR_NO, USER_ID, USER_NAME, ID_CREATION_DATE, STATUS, EMAIL_ID, MOBILE_NUMBER, AGENCY_ID, AGENCY_TYPE, VPTS_ID, VPTS_APPROVAL_STATUS, ANNUAL_REVIEW_DUE_DATE, MSME_REGISTERED, UDYOG_AADHAR_NUMBER, AGENCY_PAN_NUMBER, USER_GROUP, CHANNEL_NAME, PRODUCT_TYPE, BASE_CPC_PROCESS_SHOP, MAPPED_EMPLOYEE_ID, MAPPED_EMPLOYEE_NAME, INBOUND_OUTBOUND_TYPE, MAPPED_SOL_IDS, COUNSELLOR_IDS, TSM_ID, TSM_NAME, CHILD_ALLOWED, OPERATING_LOCATIONS, ROLE_CODE, VSTS_ID, VSTS_NAME,VSTS_STATUS, PRODUCT, STATUS_A,  "
						+ "			GREATEST(	NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_ILENS_CHANNEL_MST WHERE STATUS_A = 'A' 	 " + "			 "
						+ "			UNION ALL  " + "			 "
						+ "			SELECT SR_NO, USER_ID, USER_NAME, ID_CREATION_DATE, STATUS, EMAIL_ID, MOBILE_NUMBER, AGENCY_ID, AGENCY_TYPE, VPTS_ID, VPTS_APPROVAL_STATUS, ANNUAL_REVIEW_DUE_DATE, MSME_REGISTERED, UDYOG_AADHAR_NUMBER, AGENCY_PAN_NUMBER, USER_GROUP, CHANNEL_NAME, PRODUCT_TYPE, BASE_CPC_PROCESS_SHOP, MAPPED_EMPLOYEE_ID, MAPPED_EMPLOYEE_NAME, INBOUND_OUTBOUND_TYPE, MAPPED_SOL_IDS, COUNSELLOR_IDS, TSM_ID, TSM_NAME, CHILD_ALLOWED, OPERATING_LOCATIONS, ROLE_CODE, VSTS_ID, VSTS_NAME,VSTS_STATUS, PRODUCT, STATUS_A,  "
						+ "			GREATEST(   NVL(ACTION_DATE, DATE '1900-01-01'),    "
						+ "						NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_ILENS_CHANNEL_MST_TEMP  WHERE STATUS_A = 'P'   " + "			)  "
						+ "ORDER BY sort_date DESC";

				List<ILensChannelMasterDto> ListOfDto = jdbcTemplate.query(con -> {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(BATCH_SIZE);
					return ps;
				}, (rs, rowNum) -> {

					ILensChannelMasterDto m = new ILensChannelMasterDto();

					m.setSrNo(rs.getLong("SR_NO"));
					m.setUserID(rs.getString("USER_ID"));
					m.setUserName(rs.getString("USER_NAME"));
					m.setiDCreationDate(rs.getTimestamp("ID_CREATION_DATE") != null
							? rs.getTimestamp("ID_CREATION_DATE").toLocalDateTime()
							: null);
					m.setStatus(rs.getString("STATUS"));
					m.setEmailID(rs.getString("EMAIL_ID"));
					m.setMobileNumber(rs.getString("MOBILE_NUMBER"));
					m.setAgencyID(rs.getString("AGENCY_ID"));
					m.setAgencyType(rs.getString("AGENCY_TYPE"));
					m.setvPTSID(rs.getString("VPTS_ID"));
					m.setvPTSApprovalStatus(rs.getString("VPTS_APPROVAL_STATUS"));
					m.setAnnualReviewDueDate(rs.getDate("ANNUAL_REVIEW_DUE_DATE") != null
							? rs.getDate("ANNUAL_REVIEW_DUE_DATE").toLocalDate()
							: null);
					m.setmSMERegistered(rs.getString("MSME_REGISTERED"));
					m.setUdyogAadharNumber(rs.getString("UDYOG_AADHAR_NUMBER"));
					m.setAgencyPanNumber(rs.getString("AGENCY_PAN_NUMBER"));
					m.setUserGroup(rs.getString("USER_GROUP"));
					m.setChannelName(rs.getString("CHANNEL_NAME"));
					m.setProductType(rs.getString("PRODUCT_TYPE"));
					m.setBaseCPCProcessShop(rs.getString("BASE_CPC_PROCESS_SHOP"));
					m.setMappedEmployeeID(rs.getString("MAPPED_EMPLOYEE_ID"));
					m.setMappedEmployeeName(rs.getString("MAPPED_EMPLOYEE_NAME"));
					m.setInboundOutBoundType(rs.getString("INBOUND_OUTBOUND_TYPE"));
					m.setMappedSolIDs(rs.getString("MAPPED_SOL_IDS"));
					m.setCounsellorIDs(rs.getString("COUNSELLOR_IDS"));
					m.settSMID(rs.getString("TSM_ID"));
					m.settSMName(rs.getString("TSM_NAME"));
					m.setChildAllowed(rs.getString("CHILD_ALLOWED"));
					m.setOperatingLocations(rs.getString("OPERATING_LOCATIONS"));
					m.setRoleCode(rs.getString("ROLE_CODE"));
					m.setvSTSID(rs.getString("VSTS_ID"));
					m.setvSTSName(rs.getString("VSTS_NAME"));
					m.setvSTSStatus(rs.getString("VSTS_STATUS"));
					m.setProduct(rs.getString("PRODUCT"));

					m.setStatusA(rs.getString("STATUS_A"));

					return m;
				});

				dtoList.addAll(ListOfDto);

				logger.info("All ilens Channel record Count : {}", dtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (dtoList == null || dtoList.size() < 0 || dtoList.isEmpty()) {
				logger.warn("No ilens Channel Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All ilens Channel Master record to maker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;

		}
	}
}

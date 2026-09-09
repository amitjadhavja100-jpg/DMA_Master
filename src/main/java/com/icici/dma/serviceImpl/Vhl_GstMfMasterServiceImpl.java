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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GstMfMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.GstMfMaster;
import com.icici.dma.model.GstMfMasterError;
import com.icici.dma.model.GstMfMasterTemp;
import com.icici.dma.repository.GstMfMasterErrorRepository;
import com.icici.dma.repository.GstMfMasterMainRepository;
import com.icici.dma.repository.GstMfMasterTempRepository;
import com.icici.dma.service.Vhl_GstMfMasterService;

@Service
public class Vhl_GstMfMasterServiceImpl implements Vhl_GstMfMasterService {

	private static final Logger logger = LogManager.getLogger(Vhl_GstMfMasterServiceImpl.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public GstMfMasterTempRepository tempRepo;

	@Autowired
	public GstMfMasterMainRepository mainRepo;

	@Autowired
	public GstMfMasterErrorRepository errorRepo;

	private static final Set<String> EXPECTED_HEADERS = new LinkedHashSet<>(
//			Arrays.asList("APSCODE", "NAME", "STATE", "LOCATION", "ADDRESS", "DD", "LOCATIONMF")
			Arrays.asList("APSCODE", "NAME", "STATE"));

	private static final int BATCH_SIZE = 500;

	private static FormulaEvaluator evaluator;

	private final DataFormatter formatter = new DataFormatter();

	@Transactional
	@Override
	public Map<String, Object> uploadGstMfMaster(MultipartFile file, String loginUser) {

		logger.info("GST MF upload started by user : {}", loginUser);
		Map<String, Object> response = new LinkedHashMap<>();
		Map<String, Integer> headerMap = new LinkedHashMap<>();
		List<GstMfMasterTemp> tempBatch = new ArrayList<>(BATCH_SIZE);
		List<GstMfMasterError> errorBatch = new ArrayList<>(BATCH_SIZE);
		Set<Long> duplicateSet = new HashSet<>();
		Set<Long> deleteSet = new HashSet<>();

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
			logger.info("GST MF file validation  complated");

			try (Workbook workbook = WorkbookFactory.create(file.getInputStream());) {
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
						GstMfMasterTemp m = new GstMfMasterTemp();

						String aspCode = getCellValue(row, headerMap.get("APSCODE"));
						m.setApsCode(aspCode != null ? Long.valueOf(aspCode) : null);

						// add ids for delete old pending
						deleteSet.add(m.getApsCode());

						String name = getCellValue(row, headerMap.get("NAME"));
						m.setName(name != null ? name : null);

						String state = getCellValue(row, headerMap.get("STATE"));
						m.setState(state != null ? state : null);

//						String location = getCellValue(row, headerMap.get("LOCATION"));
//						m.setLocation(location != null ? location : null);
//
//						String address = getCellValue(row, headerMap.get("ADDRESS"));
//						m.setAddress(address != null ? address : null);
//
//						String dd = getCellValue(row, headerMap.get("DD"));
//						m.setDd(dd != null ? dd : null);
//
//						String locationmf = getCellValue(row, headerMap.get("LOCATIONMF"));
//						m.setLocationMf(locationmf != null ? locationmf : null);

						m.setStatus(StatusConstant.PENDING);
						m.setCreatedBy(loginUser);
						m.setCreatedDate(new Date());
						m.setActionType(ActionConstant.INSERT);
						m.setActionUser(loginUser);
						m.setActionDate(new Date());
						m.setUploadId(uploadId);
						m.setFileName(file.getOriginalFilename());

						tempBatch.add(m);
						successCount++;

					} catch (RuntimeException e) {
						String errorMassage = e.getMessage();
						GstMfMasterError error = createError(row, errorMassage, rowNumber, headerMap, formatter,
								loginUser, uploadId);
						errorBatch.add(error);
						errorCount++;
//						continue;
					}

					// save in batch and delete older pending
					if (tempBatch.size() >= BATCH_SIZE) {

						insertTempBatch(tempBatch, deleteSet);
						deleteSet.clear();
						tempBatch.clear();
					}

					if (errorBatch.size() >= BATCH_SIZE) {

						insertErrorBatch(errorBatch);
						errorBatch.clear();
					}

					if (totalRecords % BATCH_SIZE == 0) {
						logger.info("GST MF - Record Read Count : {}", totalRecords);
					}

				}

				// save remaining batch and delete older pending
				if (!tempBatch.isEmpty()) {

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
			throw new RuntimeException("GST MF Master Upload Failed: " + e.getMessage());
		}
		return response;
	}

	private Map<String, Integer> validateExcelHeaders(Row headerRow) throws Exception {

		if (headerRow == null) {
			throw new RuntimeException("Header row is missing in uploaded Excel.");
		}

		Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
		Set<String> actualHeaders = new LinkedHashSet<>();
		Set<String> duplicateHeaders = new LinkedHashSet<>();
		Set<String> extraHeaders = new LinkedHashSet<>();

		for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {
//			Cell cell = headerRow.getCell(columnIndex);
//
//			if (cell == null) {
//				continue;
//			}

			String rawHeader = getCellValue(headerRow, columnIndex);

			if (rawHeader == null || rawHeader.trim().isEmpty()) {
				continue;
			}
			String normalizedHeader = normalizeHeader(rawHeader);

			logger.info("Normalized Header : {} ", normalizedHeader);

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
			throw new Exception(errorMessage.toString().trim());
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

	private String getCellValue(Row row, Integer index) {

		Cell cell = row.getCell(index);

		if (cell == null) {
			return null;
		}

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

	}

	private void validateMandatoryFields(Row row, Map<String, Integer> headerMap, DataFormatter formatter) {
		validateMandatory(row, headerMap, formatter, "APSCODE", "APScode");

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
		String value = getCellValue(row, headerMap.get(headerName));

		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
	}

	private void validateExcelDuplicate(Row row, Map<String, Integer> headerMap, Set<Long> duplicateSet,
			DataFormatter formatter) {
		Long apsCode = Long.valueOf(getCellValue(row, headerMap.get("APSCODE")).trim());
		Long duplicateKey = apsCode;
		if (!duplicateSet.add(duplicateKey)) {
			throw new RuntimeException("Duplicate record found in Excel.");
		}
	}

	private GstMfMasterError createError(Row row, String errorMassage, Integer rowNumber,
			Map<String, Integer> headerMap, DataFormatter formatter, String loginUser, String uploadId) {

		GstMfMasterError m = new GstMfMasterError();

		m.setApsCode(getCellValue(row, headerMap.get("APSCODE")));
		m.setName(getCellValue(row, headerMap.get("NAME")));
		m.setState(getCellValue(row, headerMap.get("STATE")));
//		m.setLocation(getCellValue(row, headerMap.get("LOCATION")));
//		m.setAddress(getCellValue(row, headerMap.get("ADDRESS")));
//		m.setDd(getCellValue(row, headerMap.get("DD")));
//		m.setLocationMf(getCellValue(row, headerMap.get("LOCATIONMF")));

		m.setRowNumber(rowNumber);
		m.setErrorMessage(errorMassage);
		m.setCreatedBy(loginUser);
		m.setCreatedDate(new Date());
		m.setUploadId(uploadId);

		return m;
	}

	private void insertTempBatch(List<GstMfMasterTemp> tempBatch, Set<Long> deleteSet) {

		if (tempBatch == null || tempBatch.isEmpty()) {
			return;
		}

		try {

			if (deleteSet != null && !deleteSet.isEmpty()) {
				tempRepo.deletePendingByApsCodes(StatusConstant.PENDING, deleteSet);
			}

			tempRepo.saveAll(tempBatch);

		} catch (DataAccessException ex) {

			logger.error("Error while inserting Temp batch.", ex);

			throw new RuntimeException("Unable to save uploaded records into Temp table.", ex);
		}
	}

	private void insertErrorBatch(List<GstMfMasterError> errorBatch) {

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
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_GST_MF_UPLOAD_ID_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	@Transactional
	@Override
	public void createGstMfMasterByMaker(GstMfMasterDto dto, String username) {

		logger.info("Processing create GSt mf master");

		try {

			if (mainRepo.existsByApsCode(dto.getApsCode())) {
				logger.warn("Record already approved for apscode : {} ", dto.getApsCode());
				throw new IllegalArgumentException("Record already Approved");
			}

			if (tempRepo.existsByApsCodeAndStatus(dto.getApsCode(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for apscode : {} ", dto.getApsCode());
				throw new IllegalArgumentException("Record already Pending ");
			}

			GstMfMasterTemp temp = this.convertDtoToTemp(dto);

			temp.setStatus(StatusConstant.PENDING);
			temp.setCreatedBy(username);
			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);
			temp.setActionDate(new Date());
			temp.setActionUser(username);

			tempRepo.save(temp);

			logger.info(" {} Record saved in Temp Table with status pending. ", dto.getApsCode());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the Outsource master Record in Temp Table", e);
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateGstMfMasterByMaker(GstMfMasterDto dto, String username) {

		try {

			logger.info("Maker {} Request Edit for Approval record of Aps Code {}", username, dto.getApsCode());

			if (tempRepo.existsByApsCodeAndStatus(dto.getApsCode(), StatusConstant.PENDING)) {

				logger.warn("ApsCode {} already Pending for Approval");

				throw new IllegalArgumentException("Aps code " + dto.getApsCode() + " alredy pending for Approval ");
			}

			GstMfMaster main = mainRepo.findByApsCode(dto.getApsCode()).orElseThrow(() -> {

				logger.warn("Approved record not found for apscode: {}", dto.getApsCode());

				return new ResourceNotFoundException("Approved record not found for apscode :" + dto.getApsCode());
			});

			// update status in main
			// main.setStatus(StatusConstant.PENDING);
			// mainRepo.save(main);

			logger.info("Outsource Master Main Record status updated to PENDING for apscode : {}", dto.getApsCode());

			// update or mapping Outsource Temp record
			GstMfMasterTemp temp = this.convertDtoToTemp(dto);

			temp.setApsCode(dto.getApsCode());

			temp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			temp.setCreatedBy(main.getCreatedBy()); // 1st main record createdBy
			temp.setCreatedDate(main.getCreatedDate()); // 1st main record createdDate

			temp.setModifiedBy(username); // modified by whom
			temp.setModifiedDate(new Date()); // current modified date

			temp.setActionType(ActionConstant.UPDATE); // now Action taken
			temp.setActionUser(username); // Action taken by whom
			temp.setActionDate(new Date()); // Action taken Date

			tempRepo.save(temp);

			logger.info("user_Id {} successfully sent for Approval", dto.getApsCode());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	@Transactional
	@Override
	public List<GstMfMasterDto> getAllGstMfMasterByChecker(String user) {

		try {

			logger.info("Fetching All Outsource Master but not created by checker : {}", user.toUpperCase());

			List<GstMfMasterTemp> tempList = tempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING, user);

			if (tempList == null || tempList.size() < 0 || tempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<GstMfMasterDto> dtoList = tempList.stream().map(this::convertTempToDto).collect(Collectors.toList());

			logger.info("Returning {} pending record to checker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Transactional
	@Override
	public void updateGstMfMasterByChecker(CheckerDecisionReq requestPayload, String username) {

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

			List<Long> StringIds = ids.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty())
					.map(Long::valueOf).collect(Collectors.toList());

			int batchSize = 900;
			List<Long> pendingIds = new ArrayList<>();

			for (int i = 0; i < StringIds.size(); i += batchSize) {

				List<Long> batch = StringIds.subList(i, Math.min(i + batchSize, StringIds.size()));

				List<Long> result = tempRepo.findAllApsCodesByApsCodeInANDStatus(batch, StatusConstant.PENDING);

				pendingIds.addAll(result);
			}

			if (pendingIds.size() != StringIds.size()) {

				List<Long> missingIds = new ArrayList<>(StringIds);
				missingIds.removeAll(pendingIds);

				logger.error("Pending record not found for apscode: {}", missingIds);

				throw new ResourceNotFoundException("Pending record not found for apscodes: " + missingIds);
			}

			// ================= DECIDE STATUS =================
			String status = decision.equalsIgnoreCase(StatusConstant.APPROVE) ? StatusConstant.APPROVE
					: StatusConstant.REJECTE;

			int totalUpdated = 0;

			// ================= BATCH OPERATION FOR BULK UPDATE =================

			for (int i = 0; i < pendingIds.size(); i += batchSize) {

				List<Long> batch = pendingIds.subList(i, Math.min(i + batchSize, pendingIds.size()));

				int updated = tempRepo.updateStatusAndRemarksByEmpCodeInANDStatus(batch, status, remark,
						StatusConstant.PENDING);

				totalUpdated += updated;
			}

			// ================= =================
			if (StatusConstant.APPROVE.equals(status)) {
				logger.info("All apscode approved successfully. Count: {}", totalUpdated);
			} else {
				logger.info("All apscode Codes rejected successfully. Count: {}", totalUpdated);
			}

			logger.info("Checker update status successfully");

		} catch (DataAccessException e) {
			logger.error("Database error while updating pending Outsource records", e);
			throw e;

		}

	}

	@Transactional
	@Override
	public List<GstMfMasterDto> getGstMfMasterByStatus(String statusType) {

		logger.info("Fetching  Outsource Master record for maker");

		List<GstMfMasterDto> dtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching Outsource Master Approve record ");
				mainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(main -> {

					GstMfMasterDto dto = this.convertMainToDto(main);
					dtoList.add(dto);
				});
				logger.info("Approved Outsource record Count : {}", dtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching Outsource Master Pending record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GstMfMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Peinding Outsource record Count : {}", dtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching Outsource Master Reject record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GstMfMasterDto dto = this.convertTempToDto(temp);
					dtoList.add(dto);
				});
				logger.info("Reject Outsource record Count : {}", dtoList.size());
				break;

			case "All":

				logger.info("Fetching All Outsource Master Approve record ");

				String sql = "SELECT * FROM (	 "
//						+ "			SELECT APSCODE, NAME, STATE, LOCATION, ADDRESS, DD, LOCATIONMF, STATUS,  "
						+ "			SELECT APSCODE, NAME, STATE, STATUS,  "
						+ "			GREATEST(	NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_MF_MST WHERE STATUS = 'A' 	 " + "			 "
						+ "			UNION ALL  " + "			 "
//						+ "			SELECT APSCODE, NAME, STATE, LOCATION, ADDRESS, DD, LOCATIONMF, STATUS,  "
						+ "			SELECT APSCODE, NAME, STATE, STATUS,  "
						+ "			GREATEST(   NVL(ACTION_DATE, DATE '1900-01-01'),    "
						+ "						NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_MF_MST_TEMP  WHERE STATUS = 'P'   " + "			)  "
						+ "ORDER BY sort_date DESC";

				List<GstMfMasterDto> ListOfDto = jdbcTemplate.query(con -> {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(BATCH_SIZE);
					return ps;
				}, (rs, rowNum) -> {

					GstMfMasterDto m = new GstMfMasterDto();

					m.setApsCode(rs.getLong("APSCODE"));
					m.setName(rs.getString("NAME"));
					m.setState(rs.getString("STATE"));
//					m.setLocation(rs.getString("LOCATION"));
//					m.setAddress(rs.getString("ADDRESS"));
//					m.setDd(rs.getString("DD"));
//					m.setLocationMf(rs.getString("LOCATIONMF"));

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

	@Transactional
	@Override
	public ByteArrayInputStream exportErrorExcel(String user) throws Exception {

		logger.info("Error Report generation started : Outsource_Master");

		int processedRecords = 0;
		long startTime = System.currentTimeMillis();

		String sql = " SELECT "
//				+ " APSCODE AS \"APScode\",  NAME AS \"Name\",  STATE AS \"State\",  LOCATION AS \"Location\", ADDRESS AS \"Address\",  DD AS \"dd\",  LOCATIONMF AS \"LocationMF\", "
				+ " APSCODE AS \"APScode\",  NAME AS \"Name\",  STATE AS \"State\",  "
				+ "ROW_NUMBER AS \"Row Number\", ERROR_MESSAGE AS \"Error Message\" "
				+ "FROM TM_VHL_GST_MF_MST_ERROR WHERE UPLOAD_ID = ( SELECT UPLOAD_ID  FROM ( SELECT UPLOAD_ID FROM TM_VHL_GST_MF_MST_ERROR  WHERE CREATED_BY = ?  ORDER BY CREATED_DATE DESC  ) WHERE ROWNUM = 1 )";

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
				resultSet.setFetchSize(100);

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
					if (processedRecords % 50 == 0) {
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

}

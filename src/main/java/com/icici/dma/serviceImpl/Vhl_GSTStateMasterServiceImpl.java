package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
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
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.GSTStateMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.GSTStateMaster;
import com.icici.dma.model.GSTStateMasterError;
import com.icici.dma.model.GSTStateMasterTemp;
import com.icici.dma.repository.GSTStateMasterErrorRepository;
import com.icici.dma.repository.GSTStateMasterMainRepository;
import com.icici.dma.repository.GSTStateMasterTempRepository;
import com.icici.dma.service.Vhl_GSTStateMasterService;

@Service
public class Vhl_GSTStateMasterServiceImpl implements Vhl_GSTStateMasterService {

	private static final Logger logger = LogManager.getLogger(Vhl_GSTStateMasterServiceImpl.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public GSTStateMasterTempRepository tempRepo;

	@Autowired
	public GSTStateMasterMainRepository mainRepo;

	@Autowired
	public GSTStateMasterErrorRepository errorRepo;

	// Normalized expected headers (spaces/special chars become "_")
	private static final Set<String> EXPECTED_HEADERS = new LinkedHashSet<>(
//			Arrays.asList("PARTNER_ID", "PARTNER_NAME", "TOTAL", "ADDRESS1", "ADDRESS2", "ADDRESS3",
//					"CITY", "PINCODE", "STATE", "GST_STATE", "OLD_ADDRESS")
			Arrays.asList("PARTNER_ID", "PARTNER_NAME", "GST_STATE"));

	private static final int BATCH_SIZE = 500;

	private static FormulaEvaluator evaluator;

	private final DataFormatter formatter = new DataFormatter();

	// =====================================================================
	// 1. UPLOAD
	// =====================================================================
	@Transactional
	@Override
	public Map<String, Object> uploadGSTStateMaster(MultipartFile file, String loginUser) {

		logger.info("Partner Master upload started by user : {}", loginUser);
		Map<String, Object> response = new LinkedHashMap<>();
		Map<String, Integer> headerMap = new LinkedHashMap<>();
		List<GSTStateMasterTemp> tempBatch = new ArrayList<>(BATCH_SIZE);
		List<GSTStateMasterError> errorBatch = new ArrayList<>(BATCH_SIZE);
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
			logger.info("Partner Master file validation completed");

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

						// mandatory validation
						validateMandatoryFields(row, headerMap, formatter);

						// duplicate validation
						validateExcelDuplicate(row, headerMap, duplicateSet, formatter);

						GSTStateMasterTemp m = new GSTStateMasterTemp();

						String partnerId = getCellValue(row, headerMap.get("PARTNER_ID"));
						m.setPartnerId(partnerId != null ? Long.valueOf(partnerId) : null);

						// delete set
						deleteSet.add(m.getPartnerId());

						String partnerName = getCellValue(row, headerMap.get("PARTNER_NAME"));
						m.setPartnerName(partnerName != null ? partnerName : null);

//						String total = getCellValue(row, headerMap.get("TOTAL"));
//						m.setTotal(total != null ? Long.valueOf(total) : null);
//
//						String address1 = getCellValue(row, headerMap.get("ADDRESS1"));
//						m.setAddress1(address1 != null ? address1 : null);
//
//						String address2 = getCellValue(row, headerMap.get("ADDRESS2"));
//						m.setAddress2(address2 != null ? address2 : null);
//
//						String address3 = getCellValue(row, headerMap.get("ADDRESS3"));
//						m.setAddress3(address3 != null ? address3 : null);
//
//						String city = getCellValue(row, headerMap.get("CITY"));
//						m.setCity(city != null ? city : null);
//
//						String pincode = getCellValue(row, headerMap.get("PINCODE"));
//						m.setPincode(pincode != null ? pincode : null);
//
//						String state = getCellValue(row, headerMap.get("STATE"));
//						m.setState(state != null ? state : null);

						String gstState = getCellValue(row, headerMap.get("GST_STATE"));
						m.setGstState(gstState != null ? gstState : null);

//						String oldAddress = getCellValue(row, headerMap.get("OLD_ADDRESS"));
//						m.setOldAddress(oldAddress != null ? oldAddress : null);

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
						GSTStateMasterError error = createError(row, errorMassage, rowNumber, headerMap, formatter,
								loginUser, uploadId);
						errorBatch.add(error);
						errorCount++;
					}

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
						logger.info("Partner Master - Record Read Count : {}", totalRecords);
					}
				}

				if (!tempBatch.isEmpty()) {
					insertTempBatch(tempBatch, deleteSet);
					deleteSet.clear();
					tempBatch.clear();
				}

				if (!errorBatch.isEmpty()) {
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

			logger.info("File Uploaded Successfully with success count : {} and error count : {}, Total count : {}",
					successCount, errorCount, totalRecords);
		} catch (Exception e) {
			logger.error("Upload failed", e);
//			response.put("status", "FAILED");
//			response.put("message", e.getMessage());
//			return response;
			throw new RuntimeException("GST State Master Upload Failed: " + e.getMessage());
		}
		return response;
	}

	// =====================================================================
	// 2. ERROR DOWNLOAD
	// =====================================================================
	@Override
	public ByteArrayInputStream exportErrorExcel(String loginUser) {

		logger.info("Export error excel started for user : {}", loginUser);

		List<GSTStateMasterError> errorList = errorRepo.findByCreatedByOrderByCreatedDateDesc(loginUser);

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records found for user : {}", loginUser);
			return new ByteArrayInputStream(new byte[0]);
		}

//		String[] columns = { "PARTNER_ID", "PARTNER_NAME", "TOTAL", "ADDRESS1", "ADDRESS2", "ADDRESS3",
//				"CITY", "PINCODE", "STATE", "GST_STATE", "OLD_ADDRESS", "ROW_NUMBER", "ERROR_MESSAGE",
//				"UPLOAD_ID", "CREATED_BY", "CREATED_DATE" };
		String[] columns = { "PARTNER_ID", "PARTNER_NAME", "GST_STATE", "ROW_NUMBER", "ERROR_MESSAGE" };

		try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = wb.createSheet("Errors");

			// Header row
			Row header = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			// Data rows
			int rowIdx = 1;
			for (GSTStateMasterError e : errorList) {

				Row r = sheet.createRow(rowIdx++);
				r.createCell(0).setCellValue(safe(e.getPartnerId()));
				r.createCell(1).setCellValue(safe(e.getPartnerName()));
//				r.createCell(2).setCellValue(safe(e.getTotal()));
//				r.createCell(3).setCellValue(safe(e.getAddress1()));
//				r.createCell(4).setCellValue(safe(e.getAddress2()));
//				r.createCell(5).setCellValue(safe(e.getAddress3()));
//				r.createCell(6).setCellValue(safe(e.getCity()));
//				r.createCell(7).setCellValue(safe(e.getPincode()));
//				r.createCell(8).setCellValue(safe(e.getState()));
				r.createCell(2).setCellValue(safe(e.getGstState()));
//				r.createCell(10).setCellValue(safe(e.getOldAddress()));
				r.createCell(3).setCellValue(e.getRowNumber() != null ? e.getRowNumber() : 0);
				r.createCell(4).setCellValue(safe(e.getErrorMessage()));
//				r.createCell(13).setCellValue(safe(e.getUploadId()));
//				r.createCell(14).setCellValue(safe(e.getCreatedBy()));
//				r.createCell(15).setCellValue(e.getCreatedDate() != null ? e.getCreatedDate().toString() : "");
			}

			wb.write(out);
			return new ByteArrayInputStream(out.toByteArray());

		} catch (Exception ex) {
			logger.error("Failed to export error excel", ex);
			throw new RuntimeException("Failed to export error excel : " + ex.getMessage(), ex);
		}
	}

	private String safe(String v) {
		return v == null ? "" : v;
	}

	// =====================================================================
	// ENTITY -> DTO CONVERTERS
	// =====================================================================
	private GSTStateMasterDto toDto(GSTStateMaster m) {
		GSTStateMasterDto d = new GSTStateMasterDto();
		d.setPartnerId(m.getPartnerId());
		d.setPartnerName(m.getPartnerName());
//		d.setTotal(m.getTotal());
//		d.setAddress1(m.getAddress1());
//		d.setAddress2(m.getAddress2());
//		d.setAddress3(m.getAddress3());
//		d.setCity(m.getCity());
//		d.setPincode(m.getPincode());
//		d.setState(m.getState());
		d.setGstState(m.getGstState());
//		d.setOldAddress(m.getOldAddress());

		d.setStatus(m.getStatus());
		return d;
	}

	private GSTStateMasterDto toDto(GSTStateMasterTemp t) {
		GSTStateMasterDto d = new GSTStateMasterDto();
		d.setPartnerId(t.getPartnerId());
		d.setPartnerName(t.getPartnerName());
//		d.setTotal(t.getTotal());
//		d.setAddress1(t.getAddress1());
//		d.setAddress2(t.getAddress2());
//		d.setAddress3(t.getAddress3());
//		d.setCity(t.getCity());
//		d.setPincode(t.getPincode());
//		d.setState(t.getState());
		d.setGstState(t.getGstState());
//		d.setOldAddress(t.getOldAddress());

		d.setStatus(t.getStatus());
		return d;
	}

	// =====================================================================
	// EXCEL HELPERS (unchanged)
	// =====================================================================
	private Map<String, Integer> validateExcelHeaders(Row headerRow) {

		if (headerRow == null) {
			throw new RuntimeException("Header row is missing in uploaded Excel.");
		}

		Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
		Set<String> actualHeaders = new LinkedHashSet<>();
		Set<String> duplicateHeaders = new LinkedHashSet<>();
		Set<String> extraHeaders = new LinkedHashSet<>();

		for (int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++) {

			String rawHeader = getCellValue(headerRow, columnIndex);

			if (rawHeader == null || rawHeader.trim().isEmpty()) {
				continue;
			}
			String normalizedHeader = normalizeHeader(rawHeader);

			logger.info("Normalized Header : {} ", normalizedHeader);

			if (!actualHeaders.add(normalizedHeader)) {
				duplicateHeaders.add(rawHeader);
				continue;
			}
			if (!EXPECTED_HEADERS.contains(normalizedHeader)) {
				extraHeaders.add(rawHeader);
				continue;
			}
			headerIndexMap.put(normalizedHeader, columnIndex);
		}

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

	private String getCellValue(Row row, Integer index) {

		Cell cell = row.getCell(index);
		if (cell == null)
			return null;

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
			if (cellValue == null)
				return null;
			switch (cellValue.getCellType()) {
			case STRING:
				return cellValue.getStringValue();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
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
		validateMandatory(row, headerMap, formatter, "PARTNER_ID", "PartnerId");
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
		String value = getCellValue(row, headerMap.get(headerName));

		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
	}

	private void validateExcelDuplicate(Row row, Map<String, Integer> headerMap, Set<Long> duplicateSet,
			DataFormatter formatter) {
		Long partnerId = Long.valueOf(getCellValue(row, headerMap.get("PARTNER_ID")).trim());
		Long duplicateKey = partnerId;
		if (!duplicateSet.add(duplicateKey)) {
			throw new RuntimeException("Duplicate record found in Excel.");
		}
	}

	private GSTStateMasterError createError(Row row, String errorMassage, Integer rowNumber,
			Map<String, Integer> headerMap, DataFormatter formatter, String loginUser, String uploadId) {

		GSTStateMasterError m = new GSTStateMasterError();
		m.setPartnerId(getCellValue(row, headerMap.get("PARTNER_ID")));
		m.setPartnerName(getCellValue(row, headerMap.get("PARTNER_NAME")));
//		m.setTotal(getCellValue(row, headerMap.get("TOTAL")));
//		m.setAddress1(getCellValue(row, headerMap.get("ADDRESS1")));
//		m.setAddress2(getCellValue(row, headerMap.get("ADDRESS2")));
//		m.setAddress3(getCellValue(row, headerMap.get("ADDRESS3")));
//		m.setCity(getCellValue(row, headerMap.get("CITY")));
//		m.setPincode(getCellValue(row, headerMap.get("PINCODE")));
//		m.setState(getCellValue(row, headerMap.get("STATE")));
		m.setGstState(getCellValue(row, headerMap.get("GST_STATE")));
//		m.setOldAddress(getCellValue(row, headerMap.get("OLD_ADDRESS")));

		m.setRowNumber(rowNumber);
		m.setErrorMessage(errorMassage);
		m.setCreatedBy(loginUser);
		m.setCreatedDate(new Date());
		m.setUploadId(uploadId);

		return m;
	}

	private void insertTempBatch(List<GSTStateMasterTemp> tempBatch, Set<Long> deleteSet) {

		if (tempBatch == null || tempBatch.isEmpty())
			return;

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

	private void insertErrorBatch(List<GSTStateMasterError> errorBatch) {

		if (errorBatch == null || errorBatch.isEmpty())
			return;

		try {
			errorRepo.saveAll(errorBatch);
		} catch (DataAccessException ex) {
			logger.error("Error while inserting Error batch.", ex);
			throw new RuntimeException("Unable to save error records.", ex);
		}
	}

	private String generateUploadId(String fileName) {

		String baseName = fileName.replace(".xlsx", "").replace(".xls", "");
		String prefix = baseName.substring(0, Math.min(3, baseName.length())).toUpperCase();
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_GST_STATE_UPLOAD_ID_SEQ.NEXTVAL FROM DUAL",
				long.class);

		return prefix + "_" + timestamp + "_" + sequence;

	}

	@Transactional
	@Override
	public void createGstStateMasterByMaker(GSTStateMasterDto dto, String username) {

		logger.info("Processing create GSt state master");

		try {

			if (mainRepo.existsByPartnerId(dto.getPartnerId())) {
				logger.warn("Record already approved for partner Id : {} ", dto.getPartnerId());
				throw new IllegalArgumentException("Record already Approved");
			}

			if (tempRepo.existsByPartnerIdAndStatus(dto.getPartnerId(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for partnerId : {} ", dto.getPartnerId());
				throw new IllegalArgumentException("Record already Pending ");
			}

			GSTStateMasterTemp temp = new GSTStateMasterTemp();

			temp.setPartnerId(dto.getPartnerId());
			temp.setPartnerName(dto.getPartnerName());
			temp.setGstState(dto.getGstState());

			temp.setStatus(StatusConstant.PENDING);
			temp.setCreatedBy(username);
			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);
			temp.setActionDate(new Date());
			temp.setActionUser(username);

			tempRepo.save(temp);

			logger.info(" {} Record saved in Temp Table with status pending. ", dto.getPartnerId());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the gst state master Record in Temp Table", e);
			throw e;
		}
	}

	@Transactional
	@Override
	public void updateGstStateMasterByMaker(GSTStateMasterDto dto, String username) {

		try {

			logger.info("Maker {} Request Edit for Approval record of Partner Id {}", username, dto.getPartnerId());

			if (tempRepo.existsByPartnerIdAndStatus(dto.getPartnerId(), StatusConstant.PENDING)) {

				logger.warn("ApsCode {} already Pending for Approval");

				throw new IllegalArgumentException("Aps code " + dto.getPartnerId() + " alredy pending for Approval ");
			}

			GSTStateMaster main = mainRepo.findByPartnerId(dto.getPartnerId()).orElseThrow(() -> {

				logger.warn("Approved record not found for partner Id : {}", dto.getPartnerId());

				return new ResourceNotFoundException("Approved record not found for apscode :" + dto.getPartnerId());
			});

			// update status in main
			// main.setStatus(StatusConstant.PENDING);
			// mainRepo.save(main);

			logger.info("GST State Master Main Record status updated to PENDING for Partner Id : {}",
					dto.getPartnerId());

			// update or mapping GST State Temp record
			GSTStateMasterTemp temp = new GSTStateMasterTemp();

			temp.setPartnerId(dto.getPartnerId());
			temp.setPartnerName(dto.getPartnerName());
			temp.setGstState(dto.getGstState());

			temp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			temp.setCreatedBy(main.getCreatedBy()); // 1st main record createdBy
			temp.setCreatedDate(main.getCreatedDate()); // 1st main record createdDate

			temp.setModifiedBy(username); // modified by whom
			temp.setModifiedDate(new Date()); // current modified date

			temp.setActionType(ActionConstant.UPDATE); // now Action taken
			temp.setActionUser(username); // Action taken by whom
			temp.setActionDate(new Date()); // Action taken Date

			tempRepo.save(temp);

			logger.info("user_Id {} successfully sent for Approval", dto.getPartnerId());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	@Transactional
	@Override
	public List<GSTStateMasterDto> getAllGstStateMasterByChecker(String user) {

		try {

			logger.info("Fetching All Gst State Master but not created by checker : {}", user.toUpperCase());

			List<GSTStateMasterTemp> tempList = tempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING, user);

			if (tempList == null || tempList.size() < 0 || tempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<GSTStateMasterDto> dtoList = tempList.stream().map(temp -> {

				GSTStateMasterDto dto = new GSTStateMasterDto();

				dto.setPartnerId(temp.getPartnerId());
				dto.setPartnerName(temp.getPartnerName());
				dto.setGstState(temp.getGstState());
				dto.setStatus(temp.getStatus());

				return dto;
			}).collect(Collectors.toList());

			logger.info("Returning {} pending record to checker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			logger.error("Database error while fetching pending records", e);
			throw e;
		}

	}

	@Transactional
	@Override
	public void updateGstStateMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info("Processing checker action in GST State master");

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

				List<Long> result = tempRepo.findAllPartnerIdByPartnerIdInANDStatus(batch, StatusConstant.PENDING);

				pendingIds.addAll(result);
			}

			if (pendingIds.size() != StringIds.size()) {

				List<Long> missingIds = new ArrayList<>(StringIds);
				missingIds.removeAll(pendingIds);

				logger.error("Pending record not found for partner Ids : {}", missingIds);

				throw new ResourceNotFoundException("Pending record not found for partner Ids : " + missingIds);
			}

			// ================= DECIDE STATUS =================
			String status = decision.equalsIgnoreCase(StatusConstant.APPROVE) ? StatusConstant.APPROVE
					: StatusConstant.REJECTE;

			int totalUpdated = 0;

			// ================= BATCH OPERATION FOR BULK UPDATE =================

			for (int i = 0; i < pendingIds.size(); i += batchSize) {

				List<Long> batch = pendingIds.subList(i, Math.min(i + batchSize, pendingIds.size()));

				int updated = tempRepo.updateStatusAndRemarksByPartnerIdInANDStatus(batch, status, remark,
						StatusConstant.PENDING);

				totalUpdated += updated;
			}

			// ================= =================
			if (StatusConstant.APPROVE.equals(status)) {
				logger.info("All Partner Id approved successfully. Count: {}", totalUpdated);
			} else {
				logger.info("All Partner Id rejected successfully. Count: {}", totalUpdated);
			}

			logger.info("Checker update status successfully");

		} catch (DataAccessException e) {
			logger.error("Database error while updating pending GST State records", e);
			throw e;

		}

	}

	@Transactional
	@Override
	public List<GSTStateMasterDto> getGstStateMasterByStatus(String statusType) {

		logger.info("Fetching  GST State Master record for maker");

		List<GSTStateMasterDto> dtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching GST State Master Approve record ");
				mainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(main -> {

					GSTStateMasterDto dto = new GSTStateMasterDto();
					dto.setPartnerId(main.getPartnerId());
					dto.setPartnerName(main.getPartnerName());
					dto.setGstState(main.getGstState());
					dto.setStatus(main.getStatus());

					dtoList.add(dto);
				});
				logger.info("Approved GST State record Count : {}", dtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching GST State Master Pending record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GSTStateMasterDto dto = new GSTStateMasterDto();
					dto.setPartnerId(temp.getPartnerId());
					dto.setPartnerName(temp.getPartnerName());
					dto.setGstState(temp.getGstState());
					dto.setStatus(temp.getStatus());

					dtoList.add(dto);
				});
				logger.info("Peinding GST State record Count : {}", dtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching GST State Master Reject record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GSTStateMasterDto dto = new GSTStateMasterDto();
					dto.setPartnerId(temp.getPartnerId());
					dto.setPartnerName(temp.getPartnerName());
					dto.setGstState(temp.getGstState());
					dto.setStatus(temp.getStatus());
					dtoList.add(dto);
				});
				logger.info("Reject GST State record Count : {}", dtoList.size());
				break;

			case "All":

				logger.info("Fetching All GST State Master Approve record ");

				String sql = "SELECT * FROM (	 " + "			SELECT PARTNER_ID, PARTNER_NAME, GST_STATE, STATUS,  "
						+ "			GREATEST(	NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_STATE_MST WHERE STATUS = 'A' 	 " + "			 "
						+ "			UNION ALL  " + "			 "
						+ "			SELECT PARTNER_ID, PARTNER_NAME, GST_STATE, STATUS,  "
						+ "			GREATEST(   NVL(ACTION_DATE, DATE '1900-01-01'),    "
						+ "						NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_STATE_MST_TEMP  WHERE STATUS = 'P' )  " + "ORDER BY sort_date DESC";

				List<GSTStateMasterDto> ListOfDto = jdbcTemplate.query(con -> {

					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(BATCH_SIZE);
					return ps;

				}, (rs, rowNum) -> {

					GSTStateMasterDto dto = new GSTStateMasterDto();
					dto.setPartnerId(rs.getLong("PARTNER_ID"));
					dto.setPartnerName(rs.getString("PARTNER_NAME"));
					dto.setGstState(rs.getString("GST_STATE"));

					dto.setStatus(rs.getString("STATUS"));

					return dto;
				});

				dtoList.addAll(ListOfDto);

				logger.info("All GST State record Count : {}", dtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (dtoList == null || dtoList.size() < 0 || dtoList.isEmpty()) {
				logger.warn("No GST State Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All GST State Master record to maker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;

		}
	}

}
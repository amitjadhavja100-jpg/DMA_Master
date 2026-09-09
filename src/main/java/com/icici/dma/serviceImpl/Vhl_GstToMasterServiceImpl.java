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
import com.icici.dma.dto.GstToMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.GstToMaster;
import com.icici.dma.model.GstToMasterError;
import com.icici.dma.model.GstToMasterTemp;
import com.icici.dma.repository.GstToMasterErrorRepository;
import com.icici.dma.repository.GstToMasterMainRepository;
import com.icici.dma.repository.GstToMasterTempRepository;
import com.icici.dma.service.Vhl_GstToMasterService;

@Service
public class Vhl_GstToMasterServiceImpl implements Vhl_GstToMasterService {

	private static final Logger logger = LogManager.getLogger(Vhl_GstToMasterServiceImpl.class);

	@Autowired
	public JdbcTemplate jdbcTemplate;

	@Autowired
	public GstToMasterTempRepository tempRepo;

	@Autowired
	public GstToMasterMainRepository mainRepo;

	@Autowired
	public GstToMasterErrorRepository errorRepo;

	// Normalized expected headers (spaces / special chars become "_")
	private static final Set<String> EXPECTED_HEADERS = new LinkedHashSet<>(
			Arrays.asList("PROCESS_SHOP", "GST_STATE_TO"));

	private static final int BATCH_SIZE = 500;

	private static FormulaEvaluator evaluator;

	private final DataFormatter formatter = new DataFormatter();

	// =====================================================================
	// 1. UPLOAD
	// =====================================================================
	@Transactional
	@Override
	public Map<String, Object> uploadGstToMaster(MultipartFile file, String loginUser) {

		logger.info("GST To Master upload started by user : {}", loginUser);

		Map<String, Object> response = new LinkedHashMap<>();
		Map<String, Integer> headerMap = new LinkedHashMap<>();
		List<GstToMasterTemp> tempBatch = new ArrayList<>(BATCH_SIZE);
		List<GstToMasterError> errorBatch = new ArrayList<>(BATCH_SIZE);

		// PROCESS_SHOP is a String business key, so both sets are Set<String>
		Set<String> duplicateSet = new HashSet<>();
		Set<String> deleteSet = new HashSet<>();

		int totalRecords = 0;
		int successCount = 0;
		int errorCount = 0;

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

			String uploadId = generateUploadId(file.getOriginalFilename());
			logger.info("GST To Master file validation completed. UploadId : {}", uploadId);

			try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

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

						// Mandatory : not null / not empty
						validateMandatoryFields(row, headerMap, formatter);

						// skip 2nd duplicate and add into error
						validateExcelDuplicate(row, headerMap, duplicateSet, formatter);

						// Uniqueness : within the Excel + against DB
//						validateProcessShopUnique(row, headerMap, duplicateSet);

						GstToMasterTemp m = new GstToMasterTemp();

						String processShop = trimOrNull(getCellValue(row, headerMap.get("PROCESS_SHOP")));
						m.setProcessShop(processShop);

						// collect ids so old PENDING rows can be replaced from temp
						deleteSet.add(processShop);

						String gstStateTo = trimOrNull(getCellValue(row, headerMap.get("GST_STATE_TO")));
						m.setGstStateTo(gstStateTo);

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

						String errorMessage = e.getMessage();
						GstToMasterError error = createError(row, errorMessage, rowNumber, headerMap, loginUser,
								uploadId);
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
						logger.info("GST  To Master - Record Read Count : {}", totalRecords);
					}
				}

				// flush remaining
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
			throw new RuntimeException("GST TO Master Upload Failed: " + e.getMessage());
		}

		return response;
	}

	// =====================================================================
	// 2. ERROR DOWNLOAD
	// =====================================================================
	@Override
	public ByteArrayInputStream exportErrorExcel(String loginUser) {

		logger.info("Export error excel started for user : {}", loginUser);

		List<GstToMasterError> errorList = errorRepo.findByCreatedByOrderByCreatedDateDesc(loginUser);

		if (errorList == null || errorList.isEmpty()) {
			logger.info("No error records found for user : {}", loginUser);
			return new ByteArrayInputStream(new byte[0]);
		}

		String[] columns = { "Process Shop", "GST State To", "ROW_NUMBER", "ERROR_MESSAGE" };

		try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = wb.createSheet("Errors");

			Row header = sheet.createRow(0);
			for (int i = 0; i < columns.length; i++) {
				header.createCell(i).setCellValue(columns[i]);
			}

			int rowIdx = 1;
			for (GstToMasterError e : errorList) {

				Row r = sheet.createRow(rowIdx++);
				r.createCell(0).setCellValue(safe(e.getProcessShop()));
				r.createCell(1).setCellValue(safe(e.getGstStateTo()));
				r.createCell(2).setCellValue(e.getRowNumber());
				r.createCell(3).setCellValue(safe(e.getErrorMessage()));
//				r.createCell(4).setCellValue(safe(e.getUploadId()));
//				r.createCell(5).setCellValue(safe(e.getCreatedBy()));
//				r.createCell(6).setCellValue(e.getCreatedDate() != null ? e.getCreatedDate().toString() : "");
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			wb.write(out);
			return new ByteArrayInputStream(out.toByteArray());

		} catch (Exception ex) {
			logger.error("Failed to export error excel", ex);
			throw new RuntimeException("Failed to export error excel : " + ex.getMessage(), ex);
		}
	}

	// =====================================================================
	// 3. MAKER - list all main-table records
	// =====================================================================
	@Override
	public List<GstToMasterDto> getAllGstToMasterMaker() {

		logger.info("Fetching all GST  To Master records for maker");

		List<GstToMaster> list = mainRepo.findAll();
		List<GstToMasterDto> dtoList = new ArrayList<>();

		for (GstToMaster m : list) {
			dtoList.add(toDto(m));
		}
		return dtoList;
	}

	// =====================================================================
	// 4. MAKER - create single record manually
	// =====================================================================
	@Transactional
	@Override
	public void createGstToMasterByMaker(GstToMasterDto dto, String username) {

		logger.info("Processing create GST TO master");

		try {

			if (mainRepo.existsByProcessShop(dto.getProcessShop())) {
				logger.warn("Record already approved for partner Id : {} ", dto.getProcessShop());
				throw new IllegalArgumentException("Record already Approved");
			}

			if (tempRepo.existsByProcessShopAndStatus(dto.getProcessShop(), StatusConstant.PENDING)) {
				logger.warn("Record already pending for partnerId : {} ", dto.getProcessShop());
				throw new IllegalArgumentException("Record already Pending ");
			}

			GstToMasterTemp temp = new GstToMasterTemp();

			temp.setProcessShop(dto.getProcessShop());
			temp.setGstStateTo(dto.getGstStateTo());

			temp.setStatus(StatusConstant.PENDING);
			temp.setCreatedBy(username);
			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);
			temp.setActionDate(new Date());
			temp.setActionUser(username);

			tempRepo.save(temp);

			logger.info(" {} Record saved in Temp Table with status pending. ", dto.getProcessShop());
		} catch (DataAccessException e) {
			logger.error("Database error while saving the gst state master Record in Temp Table", e);
			throw e;
		}
	}

	// =====================================================================
	// 5. MAKER - update rejected / pending record
	// =====================================================================
	@Transactional
	@Override
	public void updateGstToMasterByMaker(GstToMasterDto dto, String username) {

		try {

			logger.info("Maker {} Request Edit for Approval record of Partner Id {}", username, dto.getProcessShop());

			if (tempRepo.existsByProcessShopAndStatus(dto.getProcessShop(), StatusConstant.PENDING)) {

				logger.warn("process shop : {} already Pending for Approval");

				throw new IllegalArgumentException(
						"Process shop " + dto.getProcessShop() + " alredy pending for Approval ");
			}

			GstToMaster main = mainRepo.findByProcessShop(dto.getProcessShop()).orElseThrow(() -> {

				logger.warn("Approved record not found forprocess shop : {}", dto.getProcessShop());

				return new ResourceNotFoundException(
						"Approved record not found for process shop :" + dto.getProcessShop());
			});

			// update status in main
			// main.setStatus(StatusConstant.PENDING);
			// mainRepo.save(main);

			logger.info("GST to Master Main Record status updated to PENDING for process shop : {}",
					dto.getProcessShop());

			// update or mapping GST to Temp record
			GstToMasterTemp temp = new GstToMasterTemp();

			temp.setProcessShop(dto.getProcessShop());
			temp.setGstStateTo(dto.getGstStateTo());

			temp.setStatus(StatusConstant.PENDING); // now current status shifting Approval -- > Pending

			temp.setCreatedBy(main.getCreatedBy()); // 1st main record createdBy
			temp.setCreatedDate(main.getCreatedDate()); // 1st main record createdDate

			temp.setModifiedBy(username); // modified by whom
			temp.setModifiedDate(new Date()); // current modified date

			temp.setActionType(ActionConstant.UPDATE); // now Action taken
			temp.setActionUser(username); // Action taken by whom
			temp.setActionDate(new Date()); // Action taken Date

			tempRepo.save(temp);

			logger.info("process shop {} successfully sent for Approval", dto.getProcessShop());

		} catch (DataAccessException ex) {
			logger.error("Database error while fetching pending records", ex);
			throw ex;
		}
	}

	// =====================================================================
	// 6. CHECKER - list PENDING records not created by self (4-eyes principle)
	// =====================================================================
	@Transactional
	@Override
	public List<GstToMasterDto> getAllGstToMasterByChecker(String user) {

		try {

			logger.info("Fetching All Gst To Master but not created by checker : {}", user.toUpperCase());

			List<GstToMasterTemp> tempList = tempRepo.findAllByStatusAndActionUserNot(StatusConstant.PENDING, user);

			if (tempList == null || tempList.size() < 0 || tempList.isEmpty()) {
				logger.warn("No pending record found for checker");
				throw new ResourceNotFoundException("No Pending record found");
			}

			List<GstToMasterDto> dtoList = tempList.stream().map(temp -> {

				GstToMasterDto dto = new GstToMasterDto();

				dto.setProcessShop(temp.getProcessShop());
				dto.setGstStateTo(temp.getGstStateTo());
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

	// =====================================================================
	// 7. CHECKER - approve / reject
	// =====================================================================
	@Transactional
	@Override
	public void updateGstStateMasterByChecker(CheckerDecisionReq requestPayload, String username) {

		logger.info("Processing checker action in GST to master");

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

				List<String> result = tempRepo.findAllProcessShopByProcessShopInANDStatus(batch,
						StatusConstant.PENDING);

				pendingIds.addAll(result);
			}

			if (pendingIds.size() != StringIds.size()) {

				List<String> missingIds = new ArrayList<>(StringIds);
				missingIds.removeAll(pendingIds);

				logger.error("Pending record not found for process shops : {}", missingIds);

				throw new ResourceNotFoundException("Pending record not found for process shopss : " + missingIds);
			}

			// ================= DECIDE STATUS =================
			String status = decision.equalsIgnoreCase(StatusConstant.APPROVE) ? StatusConstant.APPROVE
					: StatusConstant.REJECTE;

			int totalUpdated = 0;

			// ================= BATCH OPERATION FOR BULK UPDATE =================

			for (int i = 0; i < pendingIds.size(); i += batchSize) {

				List<String> batch = pendingIds.subList(i, Math.min(i + batchSize, pendingIds.size()));

				int updated = tempRepo.updateStatusAndRemarksByProcessShopInANDStatus(batch, status, remark,
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
			logger.error("Database error while updating pending GST to records", e);
			throw e;

		}

	}

	// =====================================================================
	// 8. STATUS-WISE LISTING
	// =====================================================================
	@Transactional
	@Override
	public List<GstToMasterDto> getGstToMasterByStatus(String statusType) {

		logger.info("Fetching  GST To Master record for maker");

		List<GstToMasterDto> dtoList = new ArrayList<>();

		try {

			if (statusType == null || statusType.isEmpty()) {
				throw new IllegalArgumentException("StatusType is inavlid or Null");
			}

			switch (statusType) {

			case StatusConstant.APPROVE:

				logger.info("Fetching GST To Master Approve record ");
				mainRepo.findAllByStatus(StatusConstant.APPROVE).forEach(main -> {

					GstToMasterDto dto = new GstToMasterDto();

					dto.setProcessShop(main.getProcessShop());
					dto.setGstStateTo(main.getGstStateTo());
					dto.setStatus(main.getStatus());

					dtoList.add(dto);
				});
				logger.info("Approved GST To record Count : {}", dtoList.size());
				break;

			case StatusConstant.PENDING:

				logger.info("Fetching GST To Master Pending record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GstToMasterDto dto = new GstToMasterDto();

					dto.setProcessShop(temp.getProcessShop());
					dto.setGstStateTo(temp.getGstStateTo());
					dto.setStatus(temp.getStatus());

					dtoList.add(dto);
				});
				logger.info("Peinding GST To record Count : {}", dtoList.size());
				break;

			case StatusConstant.REJECTE:

				logger.info("Fetching GST To Master Reject record ");
				tempRepo.findAllByStatus(StatusConstant.PENDING).forEach(temp -> {

					GstToMasterDto dto = new GstToMasterDto();

					dto.setProcessShop(temp.getProcessShop());
					dto.setGstStateTo(temp.getGstStateTo());
					dto.setStatus(temp.getStatus());
					dtoList.add(dto);
				});
				logger.info("Reject GST To record Count : {}", dtoList.size());
				break;

			case "All":

				logger.info("Fetching All GST To Master Approve record ");

				String sql = "SELECT * FROM ( SELECT PROCESS_SHOP, GST_STATE_TO, STATUS,  "
						+ "			GREATEST(	NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_TO_MST WHERE STATUS = 'A' " + "			UNION ALL  "
						+ "			 " + "			SELECT PROCESS_SHOP, GST_STATE_TO, STATUS,  "
						+ "			GREATEST(   NVL(ACTION_DATE, DATE '1900-01-01'),    "
						+ "						NVL(CREATED_DATE, DATE '1900-01-01'), 		  "
						+ "						NVL(MODIFIED_DATE, DATE '1900-01-01')) AS sort_date  "
						+ "			FROM TM_VHL_GST_TO_MST_TEMP  WHERE STATUS = 'P' )  " + "ORDER BY sort_date DESC";

				List<GstToMasterDto> ListOfDto = jdbcTemplate.query(con -> {

					PreparedStatement ps = con.prepareStatement(sql);
					ps.setFetchSize(BATCH_SIZE);
					return ps;

				}, (rs, rowNum) -> {

					GstToMasterDto dto = new GstToMasterDto();
					dto.setProcessShop(rs.getString("PROCESS_SHOP"));
					dto.setGstStateTo(rs.getString("GST_STATE_TO"));

					dto.setStatus(rs.getString("STATUS"));

					return dto;
				});

				dtoList.addAll(ListOfDto);

				logger.info("All GST To record Count : {}", dtoList.size());

				break;

			default:
				throw new IllegalArgumentException("Invalid Status");
			}

			if (dtoList == null || dtoList.size() < 0 || dtoList.isEmpty()) {
				logger.warn("No GST to Master record found ");
				throw new ResourceNotFoundException("No record found");
			}

			logger.info("Returning {} All GST To Master record to maker", dtoList.size());
			return dtoList;

		} catch (DataAccessException e) {
			e.printStackTrace();
			logger.error("Database error while fetching pending records", e);
			throw e;

		}
	}

	// =====================================================================
	// ENTITY -> DTO CONVERTERS
	// =====================================================================
	private GstToMasterDto toDto(GstToMaster m) {
		GstToMasterDto d = new GstToMasterDto();
		d.setProcessShop(m.getProcessShop());
		d.setGstStateTo(m.getGstStateTo());
		d.setStatus(m.getStatus());
		return d;
	}

	private GstToMasterDto toDto(GstToMasterTemp t) {
		GstToMasterDto d = new GstToMasterDto();
		d.setProcessShop(t.getProcessShop());
		d.setGstStateTo(t.getGstStateTo());
		d.setStatus(t.getStatus());
		d.setRemarks(t.getRemarks());
		return d;
	}

	// =====================================================================
	// VALIDATIONS
	// =====================================================================
	private void validateMandatoryFields(Row row, Map<String, Integer> headerMap, DataFormatter formatter) {
		validateMandatory(row, headerMap, "PROCESS_SHOP", "Process Shop");
		validateMandatory(row, headerMap, "GST_STATE_TO", "GST State To");
	}

	private void validateMandatory(Row row, Map<String, Integer> headerMap, String headerName, String fieldName) {

		Integer columnIndex = headerMap.get(headerName);
		if (columnIndex == null) {
			throw new RuntimeException(fieldName + " column is missing.");
		}

		String value = getCellValue(row, columnIndex);

		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(fieldName + " is mandatory.");
		}
	}

	private void validateExcelDuplicate(Row row, Map<String, Integer> headerMap, Set<String> duplicateSet,
			DataFormatter formatter) {
		String processShop = getCellValue(row, headerMap.get("PROCESS_SHOP")).trim();
		String duplicateKey = processShop;
		if (!duplicateSet.add(duplicateKey)) {
			throw new RuntimeException("Duplicate record found in Excel.");
		}
	}

	/**
	 * Uniqueness check for PROCESS_SHOP. Case-insensitive so "SHOP1" and "shop1"
	 * are treated as the same key. Checks both within the uploaded file and against
	 * the approved main table.
	 */
	private void validateProcessShopUnique(Row row, Map<String, Integer> headerMap, Set<String> duplicateSet) {

		String processShop = getCellValue(row, headerMap.get("PROCESS_SHOP"));

		if (processShop == null || processShop.trim().isEmpty()) {
			throw new RuntimeException("Process Shop is mandatory.");
		}

		String key = processShop.trim().toUpperCase();

		// duplicate inside the uploaded Excel
		if (!duplicateSet.add(key)) {
			throw new RuntimeException("Duplicate Process Shop found in Excel.");
		}

		// already present in the approved master table
		if (mainRepo.existsByProcessShop(processShop.trim())) {
			throw new RuntimeException("Process Shop already exists in master.");
		}
	}

	// =====================================================================
	// EXCEL HELPERS
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

		if (row == null) {
			return true;
		}

		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();

		if (firstCellNum == -1 || lastCellNum == -1) {
			return true;
		}

		for (int c = firstCellNum; c < lastCellNum; c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != CellType.BLANK) {
				String value = formatter.formatCellValue(cell).trim();
				if (!value.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	private String getCellValue(Row row, Integer index) {

		if (index == null) {
			return null;
		}

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

	private GstToMasterError createError(Row row, String errorMessage, Integer rowNumber,
			Map<String, Integer> headerMap, String loginUser, String uploadId) {

		GstToMasterError m = new GstToMasterError();

		m.setProcessShop(getCellValue(row, headerMap.get("PROCESS_SHOP")));
		m.setGstStateTo(getCellValue(row, headerMap.get("GST_STATE_TO")));

		m.setRowNumber(rowNumber);
		m.setErrorMessage(errorMessage);
		m.setCreatedBy(loginUser);
		m.setCreatedDate(new Date());
		m.setUploadId(uploadId);

		return m;
	}

	private void insertTempBatch(List<GstToMasterTemp> tempBatch, Set<String> deleteSet) {

		if (tempBatch == null || tempBatch.isEmpty()) {
			return;
		}

		try {
			if (deleteSet != null && !deleteSet.isEmpty()) {
				tempRepo.deletePendingByProcessShop(StatusConstant.PENDING, deleteSet);
			}
			tempRepo.saveAll(tempBatch);

		} catch (DataAccessException ex) {
			logger.error("Error while inserting Temp batch.", ex);
			throw new RuntimeException("Unable to save uploaded records into Temp table.", ex);
		}
	}

	private void insertErrorBatch(List<GstToMasterError> errorBatch) {

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

		String baseName = fileName.replace(".xlsx", "").replace(".xls", "");
		String prefix = baseName.substring(0, Math.min(3, baseName.length())).toUpperCase();
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_GST_TO_UPLOAD_ID_SEQ.NEXTVAL FROM DUAL", Long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	private String safe(String v) {
		return v == null ? "" : v;
	}

	private String trimOrNull(String v) {
		return (v == null || v.trim().isEmpty()) ? null : v.trim();
	}

}

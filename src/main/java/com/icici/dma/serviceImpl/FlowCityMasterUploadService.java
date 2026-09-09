package com.icici.dma.serviceImpl;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.FlowCityMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.FlowCityMaster;
import com.icici.dma.model.FlowCityMasterError;
import com.icici.dma.model.FlowCityMasterTemp;
import com.icici.dma.repository.FlowCityMasterErrorRepository;
import com.icici.dma.repository.FlowCityMasterRepository;
import com.icici.dma.repository.FlowCityMasterTempRepository;
import com.icici.dma.service.FlowCityMasterService;
import lombok.AllArgsConstructor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FlowCityMasterUploadService implements FlowCityMasterService {
	private static final Logger log = LogManager.getLogger(FlowCityMasterUploadService.class);

    @Autowired
    private FlowCityMasterErrorRepository flowCityMasterErrorRepository;

    @Autowired
    private FlowCityMasterTempRepository flowCityMasterTempRepository;

    @Autowired
    private FlowCityMasterRepository flowCityMasterRepository;

    private final String[] EXPECTED_HEADERS = {
    		"City Code",	"CITY_NAME",	"Zone",
    		"CAT",	"MAIN_BRANCH_FOR_PAYOUT_CALCULATION"
    };

    @Override
    public Map<String, Object> upload(MultipartFile file, String user) {
        Map<String, Object> response = new HashMap<>();
        String uploadId = generateUploadId();
        Date now = new Date();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();
            validateHeaders(sheet.getRow(0), formatter);
            List<Row> rows = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    rows.add(row);
                }
            }

            int totalRows = rows.size();
            int threadCount = Runtime.getRuntime().availableProcessors();
            // Don't create more threads than rows
            threadCount = Math.min(threadCount, totalRows);
            int chunkSize = (int) Math.ceil((double) totalRows / threadCount);

            log.info("Total Rows : {}", totalRows);
            log.info("Thread Count : {}", threadCount);
            log.info("Chunk Size : {}", chunkSize);

            ExecutorService executor =
                    Executors.newFixedThreadPool(threadCount);
            List<Future<UploadChunkResult>> futures = new ArrayList<>();

            for (int start = 0; start < totalRows; start += chunkSize) {
                int from = start;
                int to = Math.min(start + chunkSize, totalRows);
                futures.add(executor.submit(() -> processChunk(workbook, rows.subList(from, to),
                        formatter, user.toLowerCase(), uploadId, now, file.getOriginalFilename())));
            }
            List<FlowCityMasterTemp> tempList = new ArrayList<>();
            List<FlowCityMasterError> errorList = new ArrayList<>();
            int success = 0;
            int error = 0;

            for (Future<UploadChunkResult> future : futures) {
                UploadChunkResult result = future.get();
                tempList.addAll(result.getTempList());
                errorList.addAll(result.getErrorList());
                success += result.getSuccessCount();
                error += result.getErrorCount();
            }
            executor.shutdown();
            updateActionTypes(tempList, user.toLowerCase(), now);
            if (errorList.isEmpty()) {
            	response.put("status", "SUCCESS");
                flowCityMasterTempRepository.saveAll(tempList);
            }
            if (!errorList.isEmpty()) {
                flowCityMasterErrorRepository.saveAll(errorList);
            }
            response.put("positiveRecords", success);
            response.put("message", "File loaded successfully.");
            response.put("errorCount", error);
            response.put("totalRecords", success + error);
            response.put("uploadId", uploadId);
        } catch (Exception ex) {
            log.error("Upload Failed", ex);
            response.put("status", "FAILED");
            response.put("message", ex.getMessage());
        }
        return response;
    }

    public static class UploadChunkResult {

        private final List<FlowCityMasterTemp> tempList = new ArrayList<>();

        private final List<FlowCityMasterError> errorList = new ArrayList<>();

        private int successCount;

        private int errorCount;

        public List<FlowCityMasterTemp> getTempList() {
            return tempList;
        }

        public List<FlowCityMasterError> getErrorList() {
            return errorList;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public void incrementSuccess() {
            successCount++;
        }

        public int getErrorCount() {
            return errorCount;
        }

        public void incrementError() {
            errorCount++;
        }
    }

    private UploadChunkResult processChunk(Workbook workbook, List<Row> rows, DataFormatter formatter, String user,
                                           String uploadId, Date now, String fileName) {
        UploadChunkResult result = new UploadChunkResult();
        for (Row row : rows) {
            processRow(workbook, row, formatter, user, uploadId, now, fileName, result);
        }
        return result;
    }

    private void processRow(Workbook workbook, Row row, DataFormatter formatter, String user, String uploadId,
                            Date now, String fileName, UploadChunkResult result) {

        List<String> errors = new ArrayList<>();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        // Formula Validation
//		for (int col = 0; col <= 4; col++) {
//			Cell cell = row.getCell(col);
//			if (cell != null && cell.getCellType() == CellType.FORMULA) {
//				try {
//					CellValue cellValue = evaluator.evaluate(cell);
//					CellType cellType = cellValue.getCellType();
//					switch (cellType) {
//					case STRING:
//						cell.setCellValue(String.valueOf(cellValue.getStringValue()));
//						break;
//					case NUMERIC:
//						cell.setCellValue(String.valueOf(cellValue.getNumberValue()));
//						break;
//					case BOOLEAN:
//						cell.setCellValue(String.valueOf(cellValue.getBooleanValue()));
//						break;
//					default:
//						cell.setCellValue("");
//					}
//				} catch (Exception e) {
//					errors.add(EXPECTED_HEADERS[col] + " not contain valid data.");
//				}
//
//			}
//		}

        String cityCode = String.valueOf(validateFormula(row.getCell(0),errors,evaluator,0));
        String cityName = String.valueOf(validateFormula(row.getCell(1),errors,evaluator,1));
        String zone = String.valueOf(validateFormula(row.getCell(2),errors,evaluator,2));
        String cat =String.valueOf(validateFormula(row.getCell(3),errors,evaluator,3));
        String branch = String.valueOf(validateFormula(row.getCell(4),errors,evaluator,4));

        // Mandatory Validation

        if (isNullEquivalent(cityCode))
            errors.add("City Code is mandatory");
        if (isNullEquivalent(cityName))
            errors.add("City Name is mandatory");
        if (isNullEquivalent(zone))
            errors.add("Zone is mandatory");
        if (isNullEquivalent(cat))
            errors.add("Cat is mandatory");
        if (isNullEquivalent(branch))
            errors.add("Branch is mandatory");

        // Error Record
        if (!errors.isEmpty()) {
            FlowCityMasterError error = new FlowCityMasterError();
            error.setCityCode(cityCode);
            error.setCityName(cityName);
            error.setZone(zone);
            error.setCat(cat);
            error.setMainBranchForPayoutCalculation(branch);

            error.setUploadId(uploadId);
            error.setCreatedBy(user);
            error.setCreatedDate(now);

//            error.setFileName(fileName);
            error.setRowNumber(row.getRowNum() + 1);
            error.setErrorMsg(String.join(", ", errors));
            result.getErrorList().add(error);
            result.incrementError();

            return;
        }

        // Temp Record

        FlowCityMasterTemp temp = new FlowCityMasterTemp();
        temp.setCityCode(cityCode);
        temp.setCityName(cityName);
        temp.setZone(zone);
        temp.setCat(cat);
        temp.setMainBranchForPayoutCalculation(branch);
        temp.setStatus(StatusConstant.PENDING);
        temp.setCreatedBy(user);
        temp.setCreatedDate(now);
        temp.setActionUser(user);
        temp.setActionDate(now);
        temp.setUploadId(uploadId);
        temp.setFileName(fileName);
        // Required for ordering later
//        temp.setRowNumber(row.getRowNum() + 1);
        result.getTempList().add(temp);
        result.incrementSuccess();
    }
    
    public Object validateFormula(Cell cell,List<String> errors, FormulaEvaluator evaluator,int col) {
    	
    	if (cell != null && cell.getCellType() == CellType.FORMULA) {
			try {
				CellValue cellValue = evaluator.evaluate(cell);
				CellType cellType = cellValue.getCellType();
				switch (cellType) {
				case STRING:
					return String.valueOf(cellValue.getStringValue());
					
				case NUMERIC:
					return String.valueOf(cellValue.getNumberValue());
					
				case BOOLEAN:
					return String.valueOf(cellValue.getBooleanValue());
					
				default:
					return "";
				}
			} catch (Exception e) {
				errors.add(EXPECTED_HEADERS[col] + " not contain valid data.");
				return cell;
			}

		}
		return cell;
    }

    private void updateActionTypes(List<FlowCityMasterTemp> tempList, String user, Date now) {

        if (tempList == null || tempList.isEmpty()) {
            return;
        }

        // Collect unique city codes
        List<String> cityCodes = tempList.stream()
                .map(FlowCityMasterTemp::getCityCode)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // Existing codes from DB
        Set<String> existingCodes = new HashSet<>();

        // Oracle supports only 1000 values in IN clause
        List<List<String>> partitions = partitionList(cityCodes, 1000);
        for (List<String> batch : partitions) {
            existingCodes.addAll(flowCityMasterTempRepository.findExistingCityCodes(batch));
        }

        // Set Action Type
        for (FlowCityMasterTemp temp : tempList) {
            if (existingCodes.contains(temp.getCityCode())) {
                temp.setActionType(ActionConstant.UPDATE);
                temp.setModifiedBy(user);
                temp.setModifiedDate(now);
            } else {
                temp.setActionType(ActionConstant.INSERT);
            }
        }
    }

    private <T> List<List<T>> partitionList(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }

    private String generateUploadId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "FLOW_CITY_" + timestamp;
    }

    private void validateHeaders(Row headerRow, DataFormatter formatter) {

        List<String> expectedHeaders = Arrays.asList(
        		"City Code",	"CITY_NAME",	"Zone",
        		"CAT",	"MAIN_BRANCH_FOR_PAYOUT_CALCULATION");

        for (int i = 0; i < expectedHeaders.size(); i++) {

            String actual = formatter.formatCellValue(headerRow.getCell(i)).trim();

            if (!expectedHeaders.get(i).equalsIgnoreCase(actual)) {

                throw new RuntimeException(
                        "Invalid Header at column " + (i + 1)
                                + ". Expected: " + expectedHeaders.get(i)
                                + ", Found: " + actual);
            }
        }
    }

    //    Error logic code
    @Override
    public ResponseEntity<ByteArrayResource> downloadErrorFile(String uploadId) throws IOException {
        List<FlowCityMasterError> errorList = flowCityMasterErrorRepository.findByUploadId(uploadId);
        if (errorList.isEmpty()) {
            throw new RuntimeException("No error records found for Upload Id : " + uploadId);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Error Records");

        // Header Style
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Header
        Row header = sheet.createRow(0);
        String[] columns = {
        		"City Code",	"CITY_NAME",	"Zone",
        		"CAT",	"MAIN_BRANCH_FOR_PAYOUT_CALCULATION",
                "ERROR_MESSAGE"
        };

        for (int i = 0; i < columns.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        int rowNum = 1;
        for (FlowCityMasterError error : errorList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(error.getCityCode());
            row.createCell(1).setCellValue(error.getCityName());
            row.createCell(2).setCellValue(error.getZone());
            row.createCell(3).setCellValue(error.getCat());
            row.createCell(4).setCellValue(error.getMainBranchForPayoutCalculation());
            row.createCell(5).setCellValue(error.getErrorMsg());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Flow_City_Master_Error.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    private boolean isNullEquivalent(String value) {

        if (value == null) {
            return true;
        }
        value = value.trim();
        return value.isEmpty()
                || "null".equalsIgnoreCase(value)
                || "na".equalsIgnoreCase(value)
                || "n/a".equalsIgnoreCase(value);
    }

    public List<FlowCityMasterDto> getRecordsByStatus(String statusType) {
        List<FlowCityMasterDto> dtoList = new ArrayList<>();
        if (StatusConstant.ALL.equalsIgnoreCase(statusType)) {
            // Approved records
            flowCityMasterRepository.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e -> dtoList.add(convertFlowCityMasterToDto(e)));
            // Pending records
            flowCityMasterTempRepository.findAllByStatus(StatusConstant.PENDING)
                    .forEach(e -> dtoList.add(convertFlowCityMasterTempToDto(e)));
        } else if (StatusConstant.APPROVE.equals(statusType)) {
            flowCityMasterRepository.findAllByStatus(StatusConstant.APPROVE)
                    .forEach(e -> dtoList.add(convertFlowCityMasterToDto(e)));
        } else if (StatusConstant.PENDING.equals(statusType)) {
            flowCityMasterTempRepository.findAllByStatus(StatusConstant.PENDING)
                    .forEach(e -> dtoList.add(convertFlowCityMasterTempToDto(e)));
        }
        return dtoList;
    }


    FlowCityMasterDto convertFlowCityMasterToDto(FlowCityMaster temp) {

        FlowCityMasterDto master = new FlowCityMasterDto();
      
        master.setCityCode(temp.getCityCode());
        master.setCityName(temp.getCityName());
        master.setZone(temp.getZone());
        master.setCat(temp.getCat());
        master.setCat(temp.getCat());
        master.setMainBranchForPayoutCalculation(temp.getMainBranchForPayoutCalculation());
        master.setCreatedBy(temp.getCreatedBy());
        master.setCreatedDate(temp.getCreatedDate());
        master.setModifiedBy(temp.getModifiedBy());
        master.setModifiedDate(temp.getModifiedDate());
        master.setStatus(temp.getStatus());
        return master;
    }

    FlowCityMasterDto convertFlowCityMasterTempToDto(FlowCityMasterTemp temp) {

        FlowCityMasterDto master = new FlowCityMasterDto();
        master.setId(temp.getId());
        master.setCityCode(temp.getCityCode());
        master.setCityName(temp.getCityName());
        master.setZone(temp.getZone());
        master.setCat(temp.getCat());
        master.setCat(temp.getCat());
        master.setMainBranchForPayoutCalculation(temp.getMainBranchForPayoutCalculation());
        master.setCreatedBy(temp.getCreatedBy());
        master.setCreatedDate(temp.getCreatedDate());
        master.setModifiedBy(temp.getModifiedBy());
        master.setModifiedDate(temp.getModifiedDate());
        master.setStatus(temp.getStatus());
        master.setActionType(temp.getActionType());
        master.setActionDate(temp.getActionDate());
        master.setActionUser(temp.getActionUser());
        return master;
    }


    @Override
    public List<FlowCityMasterDto> getAllFlowCityMasterChecker(String user) {

        List<FlowCityMasterTemp> list = flowCityMasterTempRepository
                .findAllByStatusAndCreatedByNot(StatusConstant.PENDING, user.toLowerCase());

        if (list == null || list.isEmpty()) {
            throw new ResourceNotFoundException("No pending City Flow Master records");
        }
        return list.stream()
                .map(this::convertFlowCityMasterTempToDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateFlowCityMasterByChecker(CheckerDecisionReq requestPayload,
                                              String username) {

        if (requestPayload == null) {
            throw new IllegalArgumentException("Request payload is null");
        }

        String decision = requestPayload.getDecision();
        if (decision == null || decision.trim().isEmpty()) {
            throw new IllegalArgumentException("Decision is mandatory");
        }
        decision = decision.toUpperCase();
        List<String> ids = requestPayload.getPrimaryIds();
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("No Unit Codes selected");
        }

        List<String> pendingIds = new ArrayList<>();
        for (int i = 0; i < ids.size(); i += 500) {
            List<String> batch = ids.subList(i, Math.min(i + 500, ids.size()));
            pendingIds.addAll(flowCityMasterTempRepository.findPendingIds(batch, StatusConstant.PENDING));
        }

        if (pendingIds.isEmpty()) {
            throw new ResourceNotFoundException("No pending records found");
        }

        // APPROVE
        if (StatusConstant.APPROVE.equals(decision)) {
            approveRecords(pendingIds, username);
            for (int i = 0; i < pendingIds.size(); i += 500) {
                List<String> batch = pendingIds.subList(i, Math.min(i + 500, pendingIds.size()));
                flowCityMasterTempRepository.deleteApprovedRecords(batch);
            }
        }
        // REJECT
        else if (StatusConstant.REJECTE.equals(decision)) {
            for (int i = 0; i < pendingIds.size(); i += 500) {
                List<String> batch = pendingIds.subList(i, Math.min(i + 500, pendingIds.size()));
                flowCityMasterTempRepository.bulkUpdateStatus(batch, StatusConstant.REJECTE, requestPayload.getRemark(),
                        StatusConstant.PENDING);
            }
        } else {
            throw new IllegalArgumentException("Invalid decision. Use A or R");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void approveRecords(List<String> pendingIds, String username) {
        Date now = new Date();
        final int BATCH_SIZE = 1000;
        for (int i = 0; i < pendingIds.size(); i += BATCH_SIZE) {
            List<String> batchIds = pendingIds.subList(i, Math.min(i + BATCH_SIZE, pendingIds.size()));

            // Fetch all pending temp records
            List<FlowCityMasterTemp> tempList =
                    flowCityMasterTempRepository.findByCityCodeInAndStatus(batchIds, StatusConstant.PENDING);

            if (tempList.size() != batchIds.size()) {
                throw new ResourceNotFoundException("Some pending records not found.");
            }
            // Fetch existing master records
            List<FlowCityMaster> masterList = flowCityMasterRepository.findByCityCodeIn(batchIds);

            Map<String, FlowCityMaster> masterMap =
                    masterList.stream().collect(Collectors.toMap(FlowCityMaster::getCityCode, Function.identity()));
            List<FlowCityMaster> saveMasterList = new ArrayList<>();
            for (FlowCityMasterTemp temp : tempList) {
                FlowCityMaster master = masterMap.get(temp.getCityCode());
                if (master == null) {
                    // INSERT
                    master = new FlowCityMaster();
                    master.setCreatedBy(temp.getCreatedBy());
                    master.setCreatedDate(temp.getCreatedDate());
                }

                // Copy Temp -> Master
                master.setCityCode(temp.getCityCode());
                master.setCityName(temp.getCityName());
                master.setZone(temp.getZone());
                master.setCat(temp.getCat());
                master.setMainBranchForPayoutCalculation(temp.getMainBranchForPayoutCalculation());
                master.setRemarks(temp.getRemarks());
                master.setStatus(StatusConstant.APPROVE);
                master.setModifiedBy(username);
                master.setModifiedDate(now);
                saveMasterList.add(master);
                // Update Temp
                temp.setStatus(StatusConstant.APPROVE);
                temp.setModifiedBy(username);
                temp.setModifiedDate(now);
            }
            // Batch Save
            flowCityMasterRepository.saveAll(saveMasterList);
            // Batch Update Temp
            flowCityMasterTempRepository.saveAll(tempList);
            // Flush after every batch (recommended)
            flowCityMasterRepository.flush();
            flowCityMasterTempRepository.flush();
        }
    }

    @Override
    public void updateFlowCityMasterByMaker(FlowCityMasterDto row, String user) {
        FlowCityMasterTemp temp = new FlowCityMasterTemp();
        Optional<FlowCityMaster> dto = flowCityMasterRepository.findByCityCode(row.getCityCode());
        temp.setCityCode(row.getCityCode());
        temp.setCityName(row.getCityName());
        temp.setZone(row.getZone());
        temp.setCat(row.getCat());
        temp.setMainBranchForPayoutCalculation(row.getMainBranchForPayoutCalculation());
        temp.setStatus(StatusConstant.PENDING);
        temp.setActionType(ActionConstant.INSERT);
        temp.setCreatedBy(dto.get().getCreatedBy());
        temp.setCreatedDate(dto.get().getCreatedDate());
        temp.setActionUser(user);
        temp.setActionDate(new Date());
        temp.setUploadId(generateUploadId());
        temp.setModifiedBy(user);
        temp.setModifiedDate(new Date());
        flowCityMasterTempRepository.save(temp);
    }
}

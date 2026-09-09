package com.icici.dma.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.HoldCodeMaster;
import com.icici.dma.model.HoldCodeMasterError;
import com.icici.dma.model.HoldCodeMasterTemp;
import com.icici.dma.repository.HoldCodeMasterErrorRepository;
import com.icici.dma.repository.HoldCodeMasterRepository;
import com.icici.dma.repository.HoldCodeMasterTempRepository;

@Service
public class HoldCodeUploadService {

    @Autowired
    private HoldCodeMasterRepository holdCodeRepo;

    @Autowired
    private HoldCodeMasterTempRepository holdCodeTempRepo;

    @Autowired
    private HoldCodeMasterErrorRepository holdCodeErrorRepo;
    
    private static final Logger logger =
            LogManager.getLogger(HoldCodeUploadService.class);
    
    
    private static final Map<String, String> HEADER_MAP =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private static final Map<String, Field> FIELD_CACHE =
            new HashMap<>();

    static {

        HEADER_MAP.put("HOLD_REASON", "holdReason");
        HEADER_MAP.put("CODE", "code");

        for(Field field : HoldCodeMasterTemp.class.getDeclaredFields()){

            field.setAccessible(true);

            FIELD_CACHE.put(
                    field.getName().toUpperCase(),
                    field);
        }

    }

    public Map<String, Object> upload(
            MultipartFile file,
            String username) {

        Map<String, Object> response = new HashMap<>();

        int totalRecords = 0;
        int successfulRecords = 0;
        //int failedRecords = 0;

        String uploadId =
                "HOLDCODE_" + System.currentTimeMillis();

        Map<String, List<Row>> codeMap = new HashMap<>();

        List<Row> validRows = new ArrayList<>();

        List<Row> invalidRows = new ArrayList<>();

        Map<Integer, String> errorReasonMap = new HashMap<>();

        try (Workbook workbook =
                     WorkbookFactory.create(file.getInputStream())) {

        	Sheet sheet = workbook.getSheetAt(0);

        	DataFormatter formatter = new DataFormatter();

        	Row headerRow = sheet.getRow(0);

        	Map<Integer, Field> columnFieldMap =
        	        validateHeaders(headerRow, formatter);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            	Row row = sheet.getRow(i);

            	if (isRowEmpty(row, formatter)) {
            	    continue;
            	}
            	
                totalRecords++;

                HoldCodeMasterTemp entity =
                        mapRowToEntity(
                                row,
                                columnFieldMap,
                                formatter);

                String holdReason = entity.getHoldReason();

                String code = entity.getCode();

                String errorMessage = null;

                if (holdReason == null || holdReason.isEmpty()) {
                    errorMessage = "Hold Reason is mandatory";
                }
                else if (code == null || code.isEmpty()) {
                    errorMessage = "Code is mandatory";
                }
                else if (holdCodeTempRepo.existsByCodeAndStatus(
                        code,
                        StatusConstant.PENDING)) {
                    errorMessage = "Code already pending for approval";
                }

                if (errorMessage != null) {

                    invalidRows.add(row);

                    errorReasonMap.put(
                            row.getRowNum(),
                            errorMessage);

                    continue;
                }

                codeMap.computeIfAbsent(
                        code.toUpperCase(),
                        k -> new ArrayList<>())
                        .add(row);

            }

            for (List<Row> rows : codeMap.values()) {

                if (rows.size() > 1) {

                    for (Row dupRow : rows) {

                        invalidRows.add(dupRow);

                        errorReasonMap.put(
                                dupRow.getRowNum(),
                                "Duplicate CODE found in Excel");
                    }

                } else {

                    validRows.add(rows.get(0));
                }
            }
            
            if (!invalidRows.isEmpty()) {

                for (Row row : invalidRows) {

                    HoldCodeMasterError err =
                            new HoldCodeMasterError();

                    err.setCode(
                            formatter.formatCellValue(
                                    row.getCell(1)).trim());

                    err.setHoldReason(
                            formatter.formatCellValue(
                                    row.getCell(0)).trim());

                    err.setErrorMessage(
                            errorReasonMap.get(row.getRowNum()));

                    err.setUploadId(uploadId);

                    err.setCreatedBy(username);

                    err.setCreatedDate(new Date());

                    err.setRowNumber(row.getRowNum() + 1);

                    holdCodeErrorRepo.save(err);
                }

                response.put("totalRecords", totalRecords);
                response.put("successfulRecords", 0);
                response.put("failedRecords", invalidRows.size());
                response.put("uploadId", uploadId);
                response.put("message",
                        "Upload failed. Error file generated.");

                return response;
            }
            
            for (Row row : validRows) {

            	HoldCodeMasterTemp entity =
            			mapRowToEntity(
            			row,
            			columnFieldMap,
            			formatter);

                HoldCodeMasterTemp temp =
                        new HoldCodeMasterTemp();
                
                String code = entity.getCode();

                String holdReason = entity.getHoldReason();

                temp.setCode(code);
                temp.setHoldReason(holdReason);

                Optional<HoldCodeMaster> masterOpt =
                        holdCodeRepo.findById(code);

                if (masterOpt.isPresent()) {

                    HoldCodeMaster master = masterOpt.get();

                    temp.setCreatedBy(master.getCreatedBy());
                    temp.setCreatedDate(master.getCreatedDate());

                    temp.setActionType(ActionConstant.UPDATE);

                    temp.setModifiedBy(username);
                    temp.setModifiedDate(new Date());

                } else {

                    temp.setCreatedBy(username);
                    temp.setCreatedDate(new Date());

                    temp.setActionType(ActionConstant.INSERT);
                }

                temp.setStatus(StatusConstant.PENDING);

                temp.setActionDate(new Date());
                temp.setActionUser(username);

                temp.setUploadId(uploadId);

                temp.setFileName(file.getOriginalFilename());

                holdCodeTempRepo.save(temp);

                successfulRecords++;
            }
            
            
            response.put("totalRecords", totalRecords);
            response.put("successfulRecords", successfulRecords);
            response.put("failedRecords", 0);
            response.put("uploadId", uploadId);
            response.put("message",  "Upload successful.");

            return response;

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    // Normalize Header
 	private String normalizeHeader(String header) {
 		return header == null ? null
 				: header.trim().replace("-", "_").replace("&", "_").replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "_")
 						.replaceAll("_+", "_").replaceAll("^_|_$", "_").toUpperCase();
 	}
 	
 	private HoldCodeMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

 		HoldCodeMasterTemp entity = new HoldCodeMasterTemp();

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
	// CHECK Cell Null Equivalent
	// ============================================================

	private boolean isNullEquivalent(String value) {
		if (value == null)
			return true;
		String val = value.trim();
		return val.isEmpty() || val.equalsIgnoreCase("N/A") || val.equalsIgnoreCase("#N/A") || val.equalsIgnoreCase("#")
				|| val.equalsIgnoreCase("##") || val.equalsIgnoreCase("###") || val.equalsIgnoreCase("-");
	}
 	
 	private void setFieldValue(Field field, Object entity, String value, Cell cell) throws Exception {

		Class<?> type = field.getType();
		String fieldName = field.getName();

		if (isNullEquivalent(value)) {

			logger.info("Field '{}' received null equivalent value '{}'", fieldName, value);

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
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					field.set(entity, sdf.parse(value));
				}
			}

			else if (type.equals(LocalDate.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue().toLocalDate());
				else
					field.set(entity, LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}

			else if (type.equals(LocalDateTime.class)) {

				if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell))
					field.set(entity, cell.getLocalDateTimeCellValue());
				else
					field.set(entity, LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			}

//			logger.info("Field : {} Successfully set with value : {}", fieldName, value);

		} catch (Exception e) {

			logger.error("Error while setting field : '{}' with value : '{}' , type : {} ", fieldName, value,
					type.getSimpleName(), e);
			throw e;
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

	public byte[] downloadErrorExcel(String uploadId) throws Exception {

		List<HoldCodeMasterError> errors = holdCodeErrorRepo.findByUploadIdOrderByRowNumber(uploadId);

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Hold_Code_Errors");

		// Header Style
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);

		// Error Style (Red)
		CellStyle errorStyle = workbook.createCellStyle();
		Font errorFont = workbook.createFont();
		errorFont.setColor(IndexedColors.RED.getIndex());
		errorStyle.setFont(errorFont);

		Row header = sheet.createRow(0);

		Cell cell0 = header.createCell(0);
		cell0.setCellValue("HOLD_REASON");
		cell0.setCellStyle(headerStyle);

		Cell cell1 = header.createCell(1);
		cell1.setCellValue("CODE");
		cell1.setCellStyle(headerStyle);

		Cell cell2 = header.createCell(2);
		cell2.setCellValue("ERROR_MESSAGE");
		cell2.setCellStyle(headerStyle);

//        Row header = sheet.createRow(0);
//
//        header.createCell(0).setCellValue("HOLD_REASON");
//        header.createCell(1).setCellValue("CODE");
//        header.createCell(2).setCellValue("ERROR_MESSAGE");

		int rowNum = 1;

		for (HoldCodeMasterError err : errors) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(err.getHoldReason());
			row.createCell(1).setCellValue(err.getCode());
//			row.createCell(2).setCellValue(err.getErrorMessage());
			
			Cell errorCell = row.createCell(2);
			errorCell.setCellValue(err.getErrorMessage());
			errorCell.setCellStyle(errorStyle);
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(2);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}

	private boolean isRowEmpty(Row row, DataFormatter formatter) {

		if (row == null) {
			return true;
		}

		for (int c = 0; c < row.getLastCellNum(); c++) {

			Cell cell = row.getCell(c);

			if (cell != null && cell.getCellType() != CellType.BLANK
					&& !formatter.formatCellValue(cell).trim().isEmpty()) {

				return false;
			}
		}

		return true;
	}
    
}
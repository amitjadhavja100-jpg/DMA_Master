package com.icici.dma.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.PaymentCodeMaster;
import com.icici.dma.model.PaymentCodeMasterError;
import com.icici.dma.model.PaymentCodeMasterTemp;
import com.icici.dma.repository.PaymentCodeMasterErrorRepository;
import com.icici.dma.repository.PaymentCodeMasterRepository;
import com.icici.dma.repository.PaymentCodeMasterTempRepository;

@Service
public class PaymentCodeUploadService {

	//snz
	
    @Autowired
    private PaymentCodeMasterRepository paymentCodeRepo;

    @Autowired
    private PaymentCodeMasterTempRepository paymentCodeTempRepo;

    @Autowired
    private PaymentCodeMasterErrorRepository errorRepo;
    
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        HEADER_MAP.put("CODES", "code");
        
        for (Field field : PaymentCodeMasterTemp.class.getDeclaredFields()) {
            field.setAccessible(true);
            FIELD_CACHE.put(field.getName().toUpperCase(), field);
        }

    }

	@Transactional
	public Map<String, Object> upload(MultipartFile file, String user) {

		Map<String, Object> result = new HashMap<>();
		
		String uploadId = "PAYMENT_" + System.currentTimeMillis();
		Map<String, List<Row>> codeMap = new HashMap<>();
		List<Row> validRows = new ArrayList<>();
		List<Row> invalidRows = new ArrayList<>();
		Map<Integer, String> errorReasonMap = new HashMap<>();

		int total = 0;
		int success = 0;
		int failed = 0;

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {

			Sheet sheet = workbook.getSheetAt(0);
			DataFormatter formatter = new DataFormatter();

			//snz
			Row headerRow = sheet.getRow(0);
			Map<Integer, Field> columnFieldMap =
			        validateHeaders(headerRow, formatter);
			
			// Find CODES column only once
			Integer codeColumn = null;

			for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
			    if ("code".equals(entry.getValue().getName())) {
			        codeColumn = entry.getKey();
			        break;
			    }
			}

			if (codeColumn == null) {
			    throw new RuntimeException("CODES column not found.");
			}
			
			for (int r = 1; r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (isRowEmpty(row)) {
					continue;
				}
				total++;
				
				String code = formatter.formatCellValue(row.getCell(codeColumn)).trim();
				
				String errorMessage = null;
				if (code.isEmpty()) {
					errorMessage = "Code is mandatory";

				} else if (paymentCodeTempRepo.existsByCodeAndStatus(code, StatusConstant.PENDING)) {
					errorMessage = "Code already pending for approval";
				}
				if (errorMessage != null) {
					invalidRows.add(row);
					errorReasonMap.put(row.getRowNum(), errorMessage);
					continue;
				}
				codeMap.computeIfAbsent(code.toUpperCase(), k -> new ArrayList<>()).add(row);
			}
			for (List<Row> rows : codeMap.values()) {
				if (rows.size() > 1) {
					for (Row dupRow : rows) {
						invalidRows.add(dupRow);
						errorReasonMap.put(dupRow.getRowNum(), "Duplicate CODE found in Excel");
					}
				} else {
					validRows.add(rows.get(0));
				}
			}

			if (!invalidRows.isEmpty()) {
				for (Row row : invalidRows) {
					PaymentCodeMasterError err = new PaymentCodeMasterError();

					err.setCode(formatter.formatCellValue(row.getCell(codeColumn)).trim());
					err.setErrorMessage(errorReasonMap.get(row.getRowNum()));
					err.setUploadId(uploadId);
					err.setCreatedBy(user);
					err.setCreatedDate(new Date());
					err.setRowNumber(row.getRowNum() + 1);
					errorRepo.save(err);
				}

				result.put("totalRecords", total);
				result.put("successfulRecords", 0);
				result.put("failedRecords", invalidRows.size());
				result.put("uploadId", uploadId);
				result.put("message", "Upload failed. Error file generated.");

				return result;
			}
			for (Row row : validRows) {
				String code = formatter.formatCellValue(row.getCell(codeColumn)).trim();
				PaymentCodeMasterTemp temp = new PaymentCodeMasterTemp();
				temp.setCode(code);
				if (paymentCodeRepo.existsById(code)) {
					PaymentCodeMaster master = paymentCodeRepo.findById(code).get();
					temp.setCreatedBy(master.getCreatedBy());
					temp.setCreatedDate(master.getCreatedDate());
					temp.setActionType(ActionConstant.UPDATE);
					temp.setModifiedBy(user);
					temp.setModifiedDate(new Date());
				} else {
					temp.setCreatedBy(user);
					temp.setCreatedDate(new Date());
					temp.setActionType(ActionConstant.INSERT);
				}
				temp.setStatus(StatusConstant.PENDING);
				temp.setActionDate(new Date());
				temp.setActionUser(user);
				temp.setUploadId(uploadId);
				temp.setFileName(file.getOriginalFilename());
				
				paymentCodeTempRepo.save(temp);
				success++;
			}
			result.put("totalRecords", total);
			result.put("successfulRecords", success);
			result.put("failedRecords", failed);
			result.put("uploadId", uploadId);

			return result;

		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	//snz
	private Map<Integer, Field> validateHeaders(Row headerRow, DataFormatter formatter) {

		if (headerRow == null) {
			throw new RuntimeException("Header row missing in Excel");
		}

		Map<Integer, Field> columnFieldMap = new HashMap<>();

		Set<String> expectedHeaders = HEADER_MAP.keySet();
		Set<String> actualHeaders = new HashSet<>();
		Set<String> duplicateHeaders = new HashSet<>();
		Set<String> extraHeaders = new HashSet<>();

		for (int c = 0; c < headerRow.getLastCellNum(); c++) {

			Cell cell = headerRow.getCell(c);

			if (cell == null) {
				continue;
			}

			String rawHeader = formatter.formatCellValue(cell).trim();

			if (rawHeader.isEmpty()) {
				continue;
			}

			 String header = rawHeader
		                .replace("-", "_")
		                .replace("/", "_")
		                .replace("&", "_")
		                .replace(" ", "_")
		                .replaceAll("[^a-zA-Z0-9_]", "_")
		                .replaceAll("_+", "_")
		                .replaceAll("^_|_$", "")
		                .toUpperCase();
			 
			if (!actualHeaders.add(header)) {
				duplicateHeaders.add(header);
				continue;
			}

			if (!HEADER_MAP.containsKey(header)) {
				extraHeaders.add(header);
				continue;
			}

			String fieldName = HEADER_MAP.get(header);

			Field field = FIELD_CACHE.get(fieldName.toUpperCase());

			if (field == null) {
				throw new RuntimeException("Field mapping not found for : " + header);
			}

			columnFieldMap.put(c, field);
		}

		Set<String> missingHeaders = new HashSet<>(expectedHeaders);
		missingHeaders.removeAll(actualHeaders);

		if (!duplicateHeaders.isEmpty() || !extraHeaders.isEmpty() || !missingHeaders.isEmpty()) {

			StringBuilder error = new StringBuilder("");

			if (!duplicateHeaders.isEmpty()) {
				error.append(" Duplicate Header(s): ").append(String.join(", ", duplicateHeaders)).append(".");
			}

			if (!extraHeaders.isEmpty()) {
				error.append(" Invalid Header(s): ").append(String.join(", ", extraHeaders)).append(".");
			}

			if (!missingHeaders.isEmpty()) {
				error.append("\nMissing Header(s): ").append(String.join(", ", missingHeaders)).append(".");
			}

			throw new RuntimeException(error.toString());
		}

		return columnFieldMap;
	}


	
	public byte[] downloadErrorExcel(String uploadId) throws Exception {

		List<PaymentCodeMasterError> errors = errorRepo.findByUploadIdOrderByRowNumber(uploadId);

//		Workbook workbook = new XSSFWorkbook();
//		Sheet sheet = workbook.createSheet("Payment Code Errors");
//
//		Row header = sheet.createRow(0);
//		header.createCell(0).setCellValue("CODE");
//		header.createCell(1).setCellValue("ERROR_MESSAGE");

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Payment Code Errors");

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
		cell0.setCellValue("CODES");
		cell0.setCellStyle(headerStyle);

		Cell cell1 = header.createCell(1);
		cell1.setCellValue("ERROR_MESSAGE");
		cell1.setCellStyle(headerStyle);
		
		int rowNum = 1;

		for (PaymentCodeMasterError error : errors) {

			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(error.getCode() != null ? error.getCode() : "");
//			row.createCell(1).setCellValue(error.getErrorMessage() != null ? error.getErrorMessage() : "");
			
			Cell errorCell = row.createCell(1);
			errorCell.setCellValue(error.getErrorMessage() != null ? error.getErrorMessage() : "");
			errorCell.setCellStyle(errorStyle);
		}

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		workbook.write(out);
		workbook.close();

		return out.toByteArray();
	}

    private boolean isRowEmpty(Row row) {

        if (row == null) {
            return true;
        }
        for (int c = 0; c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null &&
                    cell.getCellType() != CellType.BLANK &&
                    !new DataFormatter()
                            .formatCellValue(cell)
                            .trim()
                            .isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
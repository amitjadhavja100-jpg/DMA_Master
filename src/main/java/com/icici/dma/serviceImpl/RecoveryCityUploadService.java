package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.RecoveryCityMasterError;
import com.icici.dma.model.RecoveryCityMasterTemp;
import com.icici.dma.repository.RecoveryCityMasterErrorRepository;
import com.icici.dma.repository.RecoveryCityMasterRepository;
import com.icici.dma.repository.RecoveryCityMasterTempRepository;

@Service
@Transactional
public class RecoveryCityUploadService {
	
	//snz

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RecoveryCityMasterErrorRepository errorRepo;
    
    @Autowired
    private RecoveryCityMasterRepository recoveryCityRepo;
    
    @Autowired
    private RecoveryCityMasterTempRepository recoveryCityTempRepo;

    private static final Map<String, String> HEADER_MAP =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private static final Map<String, Field> FIELD_CACHE =
            new HashMap<>();

    private static final Map<String, Field> ERROR_FIELD_CACHE =
            new HashMap<>();

    private static final Set<String> MANDATORY_FIELDS =
            new HashSet<>(Arrays.asList(
                    "city",
                    "branchName",
                    "mainBranch",
                    "zone",
                    "zoneCode"
            ));

    static {

        HEADER_MAP.put("CITY", "city");
        HEADER_MAP.put("BRANCH_NAME", "branchName");
        HEADER_MAP.put("MAIN_BRANCH", "mainBranch");
        HEADER_MAP.put("ZONE", "zone");
        HEADER_MAP.put("360+_EXISTING_CATEGORY", "existingCategory");
        HEADER_MAP.put("CATAEGORY_FOR_181_360", "cataegory181360");
        HEADER_MAP.put("ZONE_CODE", "zoneCode");

		for (Field field : RecoveryCityMasterTemp.class.getDeclaredFields()) {
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}

		for (Field field : RecoveryCityMasterError.class.getDeclaredFields()) {
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
    }

	public Map<String, Object> upload(MultipartFile file, String user) {

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				throw new RuntimeException("Sheet not found");
			}
			DataFormatter formatter = new DataFormatter();
			Row headerRow = sheet.getRow(0);
			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			List<Row> validRows = new ArrayList<>();
			List<Row> invalidRows = new ArrayList<>();

			Map<Integer, String> errorReasonMap = new HashMap<>();
			Map<String, List<Row>> cityMap = new HashMap<>();

			for (int r = 1; r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (isRowEmpty(row)) {
					continue;
				}
				boolean invalid = false;
				for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
					Field field = entry.getValue();
					if (!MANDATORY_FIELDS.contains(field.getName())) {
						continue;
					}
					String value = formatter.formatCellValue(row.getCell(entry.getKey())).trim();

					if (isNullEquivalent(value)) {
						invalidRows.add(row);
						errorReasonMap.put(row.getRowNum(), field.getName() + " is mandatory");
						invalid = true;
						
						break;
					}
				}
				if (invalid) {
					continue;
				}
				cityMap.computeIfAbsent(formatter.formatCellValue(row.getCell(0)).trim(), k -> new ArrayList<>())
						.add(row);
			}
			
			 //Mark duplicate rows
			for (List<Row> rows : cityMap.values()) {
				if (rows.size() > 1) {
					for (Row row : rows) {
						invalidRows.add(row);
						errorReasonMap.put(row.getRowNum(), "Duplicate CITY found in Excel");
					}
				} else {
					validRows.add(rows.get(0));
				}
			}
			String uploadId = "RECOVERY_" + System.currentTimeMillis();
			List<RecoveryCityMasterTemp> tempList = new ArrayList<>();
			for (Row row : validRows) {
				String city = formatter.formatCellValue(row.getCell(0)).trim();

				if (recoveryCityTempRepo.existsById(city)) {

					RecoveryCityMasterTemp temp = recoveryCityTempRepo.findById(city).get();
					invalidRows.add(row);
					errorReasonMap.put(row.getRowNum(),
							StatusConstant.PENDING.equals(temp.getStatus()) ? "Already waiting for approval"
									: "Already Rejected");
					continue;
				}
				RecoveryCityMasterTemp entity = mapRowToEntity(row, columnFieldMap, formatter, user, uploadId,
						file.getOriginalFilename());

				 //Already Approved				 
				if (recoveryCityRepo.existsById(city)) {

					entity.setActionType(ActionConstant.UPDATE);
					entity.setModifiedBy(user);
					entity.setModifiedDate(new Date());

				} else {
					entity.setActionType(ActionConstant.INSERT);
				}
				tempList.add(entity);
			}

			List<RecoveryCityMasterError> errorList = invalidRows.stream().distinct().map(row -> mapToErrorEntity(row,
					columnFieldMap, formatter, errorReasonMap.get(row.getRowNum()), user, uploadId))
					.collect(Collectors.toList());

			if (!errorList.isEmpty()) {
				errorRepo.saveAll(errorList);
				Map<String, Object> response = new HashMap<>();

				response.put("totalRecords", validRows.size() + errorList.size());
				response.put("successfulRecords", 0);
				response.put("failedRecords", errorList.size());
				response.put("uploadId", uploadId);
				response.put("message", "Upload failed. Error file generated.");

				return response;
			}

			batchInsertTemp(tempList);

			Map<String, Object> response = new HashMap<>();

			response.put("totalRecords", tempList.size() + errorList.size());
			response.put("successfulRecords", tempList.size());
			response.put("failedRecords", errorList.size());
			response.put("uploadId", uploadId);
			response.put("message", "Upload successful.");

			return response;

		} catch (Exception e) {
			throw new RuntimeException("Upload failed : " + e.getMessage());
		}
	}

	private RecoveryCityMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter,
			String user, String uploadId, String fileName) {

		RecoveryCityMasterTemp entity = new RecoveryCityMasterTemp();

		entity.setCity(formatter.formatCellValue(row.getCell(0)).trim());
		entity.setBranchName(formatter.formatCellValue(row.getCell(1)).trim());
		entity.setMainBranch(formatter.formatCellValue(row.getCell(2)).trim());
		entity.setZone(formatter.formatCellValue(row.getCell(3)).trim());
		entity.setExistingCategory(formatter.formatCellValue(row.getCell(4)).trim());
		entity.setCataegory181360(formatter.formatCellValue(row.getCell(5)).trim());
		entity.setZoneCode(formatter.formatCellValue(row.getCell(6)).trim());
		
		entity.setCreatedBy(user);
		entity.setCreatedDate(new Date());
		entity.setStatus(StatusConstant.PENDING);
		entity.setActionType(ActionConstant.INSERT);
		entity.setActionDate(new Date());
		entity.setActionUser(user);
		entity.setUploadId(uploadId);
		entity.setFileName(fileName);

		return entity;
	}

	private RecoveryCityMasterError mapToErrorEntity(Row row, Map<Integer, Field> columnFieldMap,
			DataFormatter formatter, String errorMsg, String user, String uploadId) {

		RecoveryCityMasterError error = new RecoveryCityMasterError();

		error.setCity(formatter.formatCellValue(row.getCell(0)));
		error.setBranchName(formatter.formatCellValue(row.getCell(1)));
		error.setMainBranch(formatter.formatCellValue(row.getCell(2)));
		error.setZone(formatter.formatCellValue(row.getCell(3)));
		error.setExistingCategory(formatter.formatCellValue(row.getCell(4)));
		error.setCataegory181360(formatter.formatCellValue(row.getCell(5)));
		error.setZoneCode(formatter.formatCellValue(row.getCell(6)));

		error.setErrorMsg(errorMsg);
		error.setCreatedBy(user);
		error.setCreatedDate(new Date());
		error.setUploadId(uploadId);
		error.setRowNumber(row.getRowNum() + 1);

		return error;
	}

	private boolean isNullEquivalent(String value) {
		return value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value);
	}

	private Map<Integer, Field> validateHeaders(Row headerRow, DataFormatter formatter) {

		Map<Integer, Field> map = new HashMap<>();

		for (int c = 0; c < headerRow.getLastCellNum(); c++) {

//            String header =
//                    formatter.formatCellValue(
//                            headerRow.getCell(c))
//                            .trim()
//                            .toUpperCase();

			String header = formatter.formatCellValue(headerRow.getCell(c)).trim().replace("-", "_").replace(" ", "_")
					.toUpperCase();

			if (!HEADER_MAP.containsKey(header)) {
				throw new RuntimeException("Invalid header : " + header);
			}

			String fieldName = HEADER_MAP.get(header);

			map.put(c, FIELD_CACHE.get(fieldName.toUpperCase()));
		}

		return map;
	}

    private void batchInsertTemp(
            List<RecoveryCityMasterTemp> list) {

    	String sql =
    	        "INSERT INTO TM_CCR_RECOVERY_CITY_MST_TEMP "
    	                + "(CITY,BRANCH_NAME,MAIN_BRANCH,"
    	                + "ZONE,EXISTING_CATEGORY,"
    	                + "CATAEGORY_181_360,ZONE_CODE,"
    	                + "CREATED_BY,CREATED_DATE,"
    	                + "STATUS,ACTION_TYPE,"
    	                + "ACTION_DATE,ACTION_USER,"
    	                + "UPLOAD_ID,FILE_NAME)"
    	                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.batchUpdate(
                sql,
                list,
                list.size(),
                (ps, e) -> {
                    ps.setString(1, e.getCity());
                    ps.setString(2, e.getBranchName());
                    ps.setString(3, e.getMainBranch());
                    ps.setString(4, e.getZone());
                    ps.setString(5, e.getExistingCategory());
                    ps.setString(6, e.getCataegory181360());
                    ps.setString(7, e.getZoneCode());
                    ps.setString(8, e.getCreatedBy());
                    ps.setTimestamp(9,
                        new java.sql.Timestamp(e.getCreatedDate().getTime()));
                    ps.setString(10, e.getStatus());
                    ps.setString(11, e.getActionType());
                    ps.setTimestamp(12,
                        new java.sql.Timestamp(e.getActionDate().getTime()));
                    ps.setString(13, e.getActionUser());
                    ps.setString(14, e.getUploadId());
                    ps.setString(15, e.getFileName());
                });
    }
    
	public ByteArrayInputStream downloadErrorExcel(String uploadId) {

		try {

			List<RecoveryCityMasterError> errors = errorRepo.findByUploadIdOrderByRowNumber(uploadId);

			if (errors == null || errors.isEmpty()) {
				throw new RuntimeException("No error records found");
			}

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Recovery City Errors");

            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);
            
            //snz
            // Error Style (Red)
            CellStyle errorStyle = workbook.createCellStyle();

            Font errorFont = workbook.createFont();
            errorFont.setColor(IndexedColors.RED.getIndex());

            errorStyle.setFont(errorFont);

            //HEADER ROW
            Row header = sheet.createRow(0);

            Cell cell0 = header.createCell(0);
            cell0.setCellValue("CITY");
            cell0.setCellStyle(headerStyle);

            Cell cell1 = header.createCell(1);
            cell1.setCellValue("BRANCH_NAME");
            cell1.setCellStyle(headerStyle);

            Cell cell2 = header.createCell(2);
            cell2.setCellValue("MAIN_BRANCH");
            cell2.setCellStyle(headerStyle);

            Cell cell3 = header.createCell(3);
            cell3.setCellValue("ZONE");
            cell3.setCellStyle(headerStyle);

            Cell cell4 = header.createCell(4);
            cell4.setCellValue("360+ EXISTING CATEGORY");
            cell4.setCellStyle(headerStyle);

            Cell cell5 = header.createCell(5);
            cell5.setCellValue("CATAEGORY FOR 181-360");
            cell5.setCellStyle(headerStyle);

            Cell cell6 = header.createCell(6);
            cell6.setCellValue("ZONE_CODE");
            cell6.setCellStyle(headerStyle);

            Cell cell7 = header.createCell(7);
            cell7.setCellValue("ERROR_MESSAGE");
            cell7.setCellStyle(headerStyle);

            int rowNum = 1;

            for (RecoveryCityMasterError error : errors) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(error.getCity() != null ? error.getCity() : "");
                row.createCell(1).setCellValue(error.getBranchName() != null ? error.getBranchName() : "");
                row.createCell(2).setCellValue( error.getMainBranch() != null ? error.getMainBranch() : "");
                row.createCell(3).setCellValue(error.getZone() != null ? error.getZone() : "");
                row.createCell(4).setCellValue(error.getExistingCategory() != null ? error.getExistingCategory() : "");
                row.createCell(5).setCellValue(error.getCataegory181360() != null ? error.getCataegory181360() : "");
                row.createCell(6).setCellValue(error.getZoneCode() != null ? error.getZoneCode() : "");
                // row.createCell(7).setCellValue(error.getErrorMsg() != null ? error.getErrorMsg() : "");
                
                //snz
                Cell errorCell = row.createCell(7);
                errorCell.setCellValue(
                        error.getErrorMsg() != null ? error.getErrorMsg() : "");
                errorCell.setCellStyle(errorStyle);
            }

            //AUTO SIZE COLUMNS
			for (int i = 0; i < 8; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			workbook.write(out);
			workbook.close();

			return new ByteArrayInputStream(out.toByteArray());

		} catch (Exception e) {
			throw new RuntimeException("Error file generation failed : " + e.getMessage());
		}
	}

    private boolean isRowEmpty(Row row) {

        if (row == null) {
            return true;
        }

        for (int c = 0; c < row.getLastCellNum(); c++) {

            Cell cell = row.getCell(c);

            if (cell != null
                    && cell.getCellType() != CellType.BLANK
                    && !new DataFormatter()
                            .formatCellValue(cell)
                            .trim()
                            .isEmpty()) {

                return false;
            }
        }

        return true;
    }
}
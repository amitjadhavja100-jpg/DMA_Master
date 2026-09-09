package com.icici.dma.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.SAPMasterError;
import com.icici.dma.model.SAPMasterTemp;
import com.icici.dma.repository.SAPMasterErrorRepository;
import com.icici.dma.repository.SAPMasterRepository;
import com.icici.dma.repository.SAPMasterTempRepository;

@Service
@Transactional
public class SapMasterUploadService {
	
	//snz

    private static final Logger logger =
            LogManager.getLogger(SapMasterUploadService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private SAPMasterRepository sapMasterRepo;

    @Autowired
    private SAPMasterErrorRepository sapMasterErrorRepository;
    
    @Autowired
    private SAPMasterTempRepository sapMasterTempRepo;

	// HEADER MAP
	private static final Map<String, String> HEADER_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	private static final Map<String, Field> FIELD_CACHE = new HashMap<>();
	private static final Map<String, Field> ERROR_FIELD_CACHE = new HashMap<>();
    private static final String PRIMARY_KEY = "unitCode";
    private static final Set<String> MANDATORY_FIELDS =
            new HashSet<>(Arrays.asList(
                    "unitCode",
                    "cleanUnitId",
                    "vendorName",
                    "pan",
                    "sapVendorCode",
                    "state",
                    "gstnNo"
            ));

	private static final Pattern PAN_PATTERN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
	private static final Pattern GST_PATTERN = Pattern.compile("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[A-Z0-9]{3}$");

    //STATIC BLOCK
    static {

        HEADER_MAP.put("SR_NO", "srNo");
        HEADER_MAP.put("UNIT_CODE", "unitCode");
        HEADER_MAP.put("CLEAN_UNIT_ID", "cleanUnitId");
        HEADER_MAP.put("VENDOR_NAME", "vendorName");
        HEADER_MAP.put("CCA_CALL_CENTER", "ccaCallCenter");
        HEADER_MAP.put("PAN", "pan");
        HEADER_MAP.put("SAP_VENDOR_CODE", "sapVendorCode");
        HEADER_MAP.put("STATE", "state");
        HEADER_MAP.put("TDS_RATE", "tdsRate");
        HEADER_MAP.put("TAX_CODE", "taxCode");
        HEADER_MAP.put("FINALI_BOXIDS", "finaliBoxids");
        HEADER_MAP.put("STATUS_OF_BLOCKING", "statusOfBlocking");
        HEADER_MAP.put("ACCOUNT_STATUS", "accountStatus");
        HEADER_MAP.put("CRED_INFO_NO", "credInfoNo");
        HEADER_MAP.put("GSTN_NO", "gstnNo");
        HEADER_MAP.put("SAC_CODE", "sacCode");
        HEADER_MAP.put("SERVICE_PROVIDER_ID_STATUS", "serviceProviderIdStatus");
        HEADER_MAP.put("GST_APPLICABLE", "gstApplicable");
        HEADER_MAP.put("HOLD_STATUS", "holdStatus");
        HEADER_MAP.put("PAYMENT_MODE", "paymentMode");

        
		for (Field field : SAPMasterTemp.class.getDeclaredFields()) {
			if (field.getName().equals("id")) {
				continue;
			}
			field.setAccessible(true);
			FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
		
		for (Field field : SAPMasterError.class.getDeclaredFields()) {
			if ("errorId".equals(field.getName())) {
			    continue;
			}
			field.setAccessible(true);
			ERROR_FIELD_CACHE.put(field.getName().toUpperCase(), field);
		}
    }

    // MAIN UPLOAD METHOD
	public Map<String, Object> upload(MultipartFile file, String user) {

		long startTime = System.currentTimeMillis();

		logger.info("SAP MASTER UPLOAD STARTED");

		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				throw new RuntimeException("Sheet not found");
			}

			DataFormatter formatter = new DataFormatter();

			// HEADER VALIDATION
			Row headerRow = sheet.getRow(0);

			Map<Integer, Field> columnFieldMap = validateHeaders(headerRow, formatter);

			// PK COLUMN
			Integer pkColumnIndex = getPrimaryKeyColumnIndex(columnFieldMap);
			Map<String, List<Row>> unitCodeMap = new HashMap<>();
            List<Row> validRowsList = new ArrayList<>();
            List<Row> invalidRowsList = new ArrayList<>();
            Map<Integer, String> errorReasonMap = new HashMap<>();

            //FIRST PASS VALIDATION
			for (int r = 1; r <= sheet.getLastRowNum(); r++) {
				Row row = sheet.getRow(r);
				if (isRowEmpty(row)) {
					continue;
				}
				boolean invalid = false;
				
			    //FORMULA VALIDATION START
			    for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			        Cell cell = row.getCell(entry.getKey(),
			                Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					if (cell.getCellType() == CellType.FORMULA) {
						invalidRowsList.add(row);
						
						String excelColumn = headerRow.getCell(entry.getKey()).getStringCellValue();

						errorReasonMap.put(row.getRowNum(), "Formula is not allowed in column " + excelColumn
								+ ". Please convert the formula to a value.");
						invalid = true;
						break;
					}
				}

			    if (invalid) {
			        continue;
			    }
			    
				// MANDATORY VALIDATION
				for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
					Field field = entry.getValue();
					String fieldName = field.getName();
					if (!MANDATORY_FIELDS.contains(fieldName)) {
						continue;
					}
					Cell cell = row.getCell(entry.getKey());
					String value = formatter.formatCellValue(cell).trim();
					if (isNullEquivalent(value)) {
						invalidRowsList.add(row);
						errorReasonMap.put(row.getRowNum(), fieldName + " is mandatory");
						invalid = true;

						break;
					}
				}
				if (invalid) {
					continue;
				}

				// PRIMARY KEY VALIDATION
				String pkValue = formatter.formatCellValue(row.getCell(pkColumnIndex)).trim();

				if (sapMasterTempRepo.existsByUnitCode(pkValue)) {

					SAPMasterTemp temp = sapMasterTempRepo.findByUnitCode(pkValue).get();
					invalidRowsList.add(row);
					errorReasonMap.put(row.getRowNum(),
							StatusConstant.PENDING.equals(temp.getStatus())
									? "Already waiting for approval"
									: "Already Rejected");
					continue;
				}
				if (isNullEquivalent(pkValue)) {
					invalidRowsList.add(row);
					errorReasonMap.put(row.getRowNum(), "Unit Code is mandatory");
					continue;
				}

				// PAN VALIDATION
				String pan = formatter.formatCellValue(row.getCell(getColumnIndex(columnFieldMap, "pan"))).trim();
				if (!PAN_PATTERN.matcher(pan).matches()) {
					invalidRowsList.add(row);
					errorReasonMap.put(row.getRowNum(), "Invalid PAN Number");
					continue;
				}
				// GST VALIDATION
				String gst = formatter.formatCellValue(row.getCell(getColumnIndex(columnFieldMap, "gstnNo"))).trim();
				if (!GST_PATTERN.matcher(gst).matches()) {

					invalidRowsList.add(row);
					errorReasonMap.put(row.getRowNum(), "Invalid GST Number");
					continue;
				}
				// DUPLICATE CHECK
				unitCodeMap.computeIfAbsent(pkValue.toUpperCase(), k -> new ArrayList<>()).add(row);
			}
			for (List<Row> rows : unitCodeMap.values()) {
				if (rows.size() > 1) {
					for (Row row : rows) {
						invalidRowsList.add(row);
						errorReasonMap.put(row.getRowNum(), "Duplicate UNIT_CODE found in Excel");
					}
				} else {
					validRowsList.add(rows.get(0));
				}
			}
			logger.info("Valid Rows : {}", validRowsList.size());
			logger.info("Invalid Rows : {}", invalidRowsList.size());

			// UPLOAD ID
			String uploadId = generateUploadId(file.getOriginalFilename());

			// VALID ENTITY CREATION
			List<SAPMasterTemp> entityList = validRowsList.parallelStream().map(row -> {

				try {

					SAPMasterTemp entity = mapRowToEntity(row, columnFieldMap, formatter);

					entity.setStatus(StatusConstant.PENDING);
					entity.setCreatedBy(user);
					entity.setCreatedDate(new Date());

					if (sapMasterRepo.existsByUnitCode(entity.getUnitCode())) {

						entity.setActionType(ActionConstant.UPDATE);
						entity.setModifiedBy(user);
						entity.setModifiedDate(new Date());

					} else {
						entity.setActionType(ActionConstant.INSERT);
					}
					entity.setActionDate(new Date());
					entity.setActionUser(user);
					entity.setUploadId(uploadId);
					entity.setFileName(file.getOriginalFilename());

					return entity;

				} catch (Exception e) {
					logger.error("Entity creation failed row {}", row.getRowNum());
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());

            //ERROR ENTITY CREATION
			List<SAPMasterError> errorEntityList = invalidRowsList.parallelStream().map(row -> {
				try {
					SAPMasterError errorEntity = mapToErrorEntity(row, columnFieldMap, formatter);

					errorEntity.setErrorMsg(errorReasonMap.get(row.getRowNum()));
					errorEntity.setCreatedBy(user);
					errorEntity.setCreatedDate(new Date());
					errorEntity.setUploadId(uploadId);
					errorEntity.setRowNumber(row.getRowNum() + 1);

					return errorEntity;

				} catch (Exception e) {
					return null;
				}

			}).filter(Objects::nonNull).collect(Collectors.toList());

			// ALL OR NOTHING LOGIC
			if (!errorEntityList.isEmpty()) {
				batchInsertError(errorEntityList);
				Map<String, Object> response = new HashMap<>();
				
				response.put("totalRecords", entityList.size() + errorEntityList.size());
				response.put("successfulRecords", 0);
				response.put("failedRecords", errorEntityList.size());
				response.put("uploadId", uploadId);
				response.put("errorFileAvailable", true);
				response.put("message", "Upload failed. Error file generated.");

				return response;
			}

			// ALL VALID RECORDS
			batchInsertTemp(entityList);

			Map<String, Object> response = new HashMap<>();
			response.put("totalRecords", entityList.size());
			response.put("successfulRecords", entityList.size());
			response.put("failedRecords", 0);
			response.put("uploadId", uploadId);
			response.put("errorFileAvailable", false);
			response.put("message", "Upload successful.");
			logger.info("SAP Upload Completed in {} ms",(System.currentTimeMillis() - startTime));

			return response;
            
		} catch (Exception e) {
			logger.error("SAP Upload Failed", e);
			throw new RuntimeException("SAP Upload Failed : " + e.getMessage());
		}
    }
    
    //VALIDATE HEADERS
    private Map<Integer, Field> validateHeaders(
            Row headerRow,
            DataFormatter formatter) {

        logger.info("HEADER VALIDATION STARTED");

		if (headerRow == null) {
			throw new RuntimeException("Header row missing in Excel");
		}

        //Column Index -> Entity Field Mapping
        Map<Integer, Field> columnFieldMap = new HashMap<>();

        Set<String> expectedHeaders = HEADER_MAP.keySet();
        Set<String> actualHeaders = new HashSet<>();
        Set<String> duplicateHeaders = new HashSet<>();
        Set<String> extraHeaders = new HashSet<>();

        int expectedHeaderCount = HEADER_MAP.size();
        int matchedHeaderCount = 0;
        logger.info("Expected Header Count : {}",  expectedHeaderCount);


		for (int c = 0; c < headerRow.getLastCellNum(); c++) {

			Cell cell = headerRow.getCell(c);
			if (cell == null) {
				continue;
			}
			String rawHeader = formatter.formatCellValue(cell).trim();
			if (rawHeader.isEmpty()) {
				continue;
			}
			String normalized = normalizeHeader(rawHeader);
			logger.info("Column {} -> Raw Header : {} -> Normalized : {}", c, rawHeader, normalized);

			if (!actualHeaders.add(normalized)) {
				duplicateHeaders.add(normalized);
				continue;
			}
			if (!HEADER_MAP.containsKey(normalized)) {
				extraHeaders.add(normalized);
				continue;
			}
			matchedHeaderCount++;
			String fieldName = HEADER_MAP.get(normalized);
			Field field = FIELD_CACHE.get(fieldName.toUpperCase());
			if (field == null) {

				throw new RuntimeException("Field mapping not found for : " + normalized);
			}
			columnFieldMap.put(c, field);

			if (matchedHeaderCount == expectedHeaderCount) {
				break;
			}
		}

		// MISSING HEADER VALIDATION
		Set<String> missingHeaders = new HashSet<>(expectedHeaders);

		missingHeaders.removeAll(actualHeaders);

		logger.info("Actual Header Count : {}", actualHeaders.size());
		logger.info("Matched Header Count : {}", matchedHeaderCount);
		logger.info("Duplicate Headers : {}", duplicateHeaders.size());
		logger.info("Extra Headers : {}", extraHeaders.size());
		logger.info("Missing Headers : {}", missingHeaders.size());

		if (!duplicateHeaders.isEmpty() || !extraHeaders.isEmpty() || !missingHeaders.isEmpty()) {

			StringBuilder error = new StringBuilder();

			error.append("\nHEADER VALIDATION FAILED\n");

			if (!duplicateHeaders.isEmpty()) {
				error.append("\nDuplicate Headers : ").append(duplicateHeaders);
			}

			if (!extraHeaders.isEmpty()) {
				error.append("\nUnexpected Headers : ").append(extraHeaders);
			}

			if (!missingHeaders.isEmpty()) {
				error.append("\nMissing Headers : ").append(missingHeaders);
			}
			logger.error(error.toString());
			throw new RuntimeException(error.toString());
		}

		logger.info("HEADER VALIDATION SUCCESS");
		logger.info("Total Column Mapping : {}", columnFieldMap.size());

		return columnFieldMap;
    }

    // NORMALIZE HEADER
    private String normalizeHeader(String header) {
        if (header == null) {
            return null;
        }
        return header.trim()
                .replace("-", "_")
                .replace("/", "_")
                .replace("&", "_")
                .replace(" ", "_")
                .replaceAll("[^a-zA-Z0-9_]", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "")
                .toUpperCase();
    }

	// GET PRIMARY KEY COLUMN INDEX
	private Integer getPrimaryKeyColumnIndex(Map<Integer, Field> columnFieldMap) {

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
			if (entry.getValue().getName().equalsIgnoreCase(PRIMARY_KEY)) {
				return entry.getKey();
			}
		}
		throw new RuntimeException("Primary Key Column Not Found");
	}

	// GET COLUMN INDEX USING FIELD NAME
	private Integer getColumnIndex(Map<Integer, Field> columnFieldMap, String fieldName) {

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			if (entry.getValue().getName().equalsIgnoreCase(fieldName)) {

				return entry.getKey();
			}
		}

		return null;
	}
    
   //MAP ROW TO ENTITY
	private SAPMasterTemp mapRowToEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		SAPMasterTemp entity = new SAPMasterTemp();
		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {
			Integer columnIndex = entry.getKey();
			Field field = entry.getValue();
			Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String rawValue = formatter.formatCellValue(cell);
			if (rawValue != null) {
				rawValue = rawValue.trim();
			}
			
			if (isNullEquivalent(rawValue)) {
				rawValue = null;
			}
			rawValue = sanitizeValue(rawValue);
			setFieldValue(entity, field, rawValue, row.getRowNum() + 1);
		}
		validateMandatoryFields(entity);
		validatePan(entity.getPan());
		validateGst(entity.getGstnNo());

		return entity;
	}

    //SANITIZE VALUE - Remove special chars except:. , + - * _
    private String sanitizeValue(String value) {

        if (value == null) {
            return null;
        }
        value = value.trim();
        
        //Remove unwanted special chars
        value = value.replaceAll(
                "[^a-zA-Z0-9.,+\\-* _]",
                "");
        
        //Blank after sanitize
        if (value.trim().isEmpty()) {
            return null;
        }

        return value;
    }

    // NULL CHECK
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

    //VALIDATE MANDATORY FIELDS
	private void validateMandatoryFields(SAPMasterTemp entity) {

		List<String> missingFields = new ArrayList<>();

		if (isNullEquivalent(entity.getUnitCode())) {
			missingFields.add("UNIT_CODE");
		}

		if (isNullEquivalent(entity.getCleanUnitId())) {
			missingFields.add("CLEAN_UNIT_ID");
		}

		if (isNullEquivalent(entity.getVendorName())) {
			missingFields.add("VENDOR_NAME");
		}

		if (isNullEquivalent(entity.getPan())) {
			missingFields.add("PAN");
		}

		if (isNullEquivalent(entity.getSapVendorCode())) {
			missingFields.add("SAP_VENDOR_CODE");
		}

		if (isNullEquivalent(entity.getState())) {
			missingFields.add("STATE");
		}
		if (isNullEquivalent(entity.getGstnNo())) {
			missingFields.add("GSTN_NO");
		}

		if (!missingFields.isEmpty()) {

			throw new RuntimeException("Mandatory fields missing : " + String.join(", ", missingFields));
		}
	}

	// PAN VALIDATION
	private void validatePan(String pan) {
		if (isNullEquivalent(pan)) {
			throw new RuntimeException("PAN is mandatory");
		}
		pan = pan.trim().toUpperCase();
		if (!PAN_PATTERN.matcher(pan).matches()) {
			throw new RuntimeException("Invalid PAN Number : " + pan);
		}
	}

	// GST VALIDATION
	private void validateGst(String gst) {

		if (isNullEquivalent(gst)) {
			throw new RuntimeException("GST Number is mandatory");
		}

		gst = gst.trim().toUpperCase();
		if (!GST_PATTERN.matcher(gst).matches()) {
			throw new RuntimeException("Invalid GST Number : " + gst);
		}
	}
    
	// SET FIELD VALUE
	private void setFieldValue(SAPMasterTemp entity, Field field, String value, Integer rowNumber) throws Exception {
		Class<?> type = field.getType();
		if (value == null) {
			field.set(entity, null);
			return;
		}

		if (type.equals(String.class)) {
			value = value.trim();
			if (value.isEmpty()) {
				field.set(entity, null);
				return;
			}
			field.set(entity, value);
			return;
		}

		if (type.equals(Integer.class) || type.equals(int.class)) {
			try {
				value = value.replace(".0", "");
				Integer intValue = Integer.parseInt(value);
				field.set(entity, intValue);

			} catch (Exception e) {
				logger.error("Invalid Integer at row {} field {} value {}", rowNumber, field.getName(), value);
				field.set(entity, null);
			}
			return;
		}

		if (type.equals(Long.class) || type.equals(long.class)) {
			try {
				value = value.replace(".0", "");
				Long longValue = Long.parseLong(value);
				field.set(entity, longValue);
			} catch (Exception e) {
				logger.error("Invalid Long at row {} field {} value {}", rowNumber, field.getName(), value);
				field.set(entity, null);
			}
			return;
		}

		if (type.equals(BigDecimal.class)) {
			try {
				value = value.replace(",", "");
				BigDecimal decimal = new BigDecimal(value);
				field.set(entity, decimal);
			} catch (Exception e) {
				logger.error("Invalid BigDecimal at row {} field {} value {}", rowNumber, field.getName(), value);
				field.set(entity, null);
			}
			return;
		}

		if (type.equals(Double.class) || type.equals(double.class)) {
			try {
				value = value.replace(",", "");
				Double doubleValue = Double.parseDouble(value);
				field.set(entity, doubleValue);

			} catch (Exception e) {
				logger.error("Invalid Double at row {} field {} value {}", rowNumber, field.getName(), value);
				field.set(entity, null);
			}
			return;
		}

		if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			try {
				Boolean boolValue = value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES")
						|| value.equalsIgnoreCase("TRUE");

				field.set(entity, boolValue);
			} catch (Exception e) {
				logger.error("Invalid Boolean at row {} field {} value {}", rowNumber, field.getName(), value);
				field.set(entity, null);
			}
			return;
		}
        field.set(entity, value);
    }
    
    //BATCH INSERT TEMP TABLE
    private void batchInsertTemp(
            List<SAPMasterTemp> entityList) {

        logger.info("TEMP BATCH INSERT STARTED");

        if (entityList == null || entityList.isEmpty()) {

            logger.info("No valid records found for temp insert");

            return;
        }

        String sql =
        		"INSERT INTO TM_CCR_SAP_MST_TEMP (" +
        		"ID," +
        		"SR_NO," +
        		"UNIT_CODE," +
        		"CLEAN_UNIT_ID," +
        		"VENDOR_NAME," +
        		"CCA_CALL_CENTER," +
        		"PAN," +
        		"SAP_VENDOR_CODE," +
        		"STATE," +
        		"TDS_RATE," +
        		"TAX_CODE," +
        		"FINALI_BOXIDS," +
        		"STATUS_OF_BLOCKING," +
        		"ACCOUNT_STATUS," +
        		"CRED_INFO_NO," +
        		"GSTN_NO," +
        		"SAC_CODE," +
        		"SERVICE_PROVIDER_ID_STATUS," +
        		"GST_APPLICABLE," +
        		"HOLD_STATUS," +
        		"PAYMENT_MODE," +
        		"CREATED_BY," +
        		"CREATED_DATE," +
        		"MODIFIED_BY," +
        		"MODIFIED_DATE," +
        		"REMARKS," +
        		"STATUS," +
        		"ACTION_TYPE," +
        		"ACTION_DATE," +
        		"ACTION_USER," +
        		"UPLOAD_ID," +
        		"FILE_NAME" +
        		") VALUES (" +
        		"TM_CCR_SAP_MST_TEMP_SEQ.NEXTVAL," +
        		"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?" +
        		")";

		int batchSize = 500;

		List<List<SAPMasterTemp>> batches = splitList(entityList, batchSize);

		int totalInserted = 0;

		for (List<SAPMasterTemp> batch : batches) {
			jdbcTemplate.batchUpdate(sql, batch, batch.size(), (PreparedStatement ps, SAPMasterTemp entity) -> {
				if (entity.getSrNo() != null) {
					ps.setInt(1, entity.getSrNo());
				} else {
					ps.setNull(1, Types.INTEGER);
				}
				ps.setString(2, entity.getUnitCode());
				ps.setString(3, entity.getCleanUnitId());
				ps.setString(4, entity.getVendorName());
				ps.setString(5, entity.getCcaCallCenter());
				ps.setString(6, entity.getPan());
				ps.setString(7, entity.getSapVendorCode());
				ps.setString(8, entity.getState());
				if (entity.getTdsRate() != null) {
					ps.setBigDecimal(9, entity.getTdsRate());
				} else {
					ps.setNull(9, Types.DECIMAL);
				}
				ps.setString(10, entity.getTaxCode());
				ps.setString(11, entity.getFinaliBoxids());
				ps.setString(12, entity.getStatusOfBlocking());
				ps.setString(13, entity.getAccountStatus());
				ps.setString(14, entity.getCredInfoNo());
				ps.setString(15, entity.getGstnNo());
				ps.setString(16, entity.getSacCode());
				ps.setString(17, entity.getServiceProviderIdStatus());
				ps.setString(18, entity.getGstApplicable());
				ps.setString(19, entity.getHoldStatus());
				ps.setString(20, entity.getPaymentMode());
				ps.setString(21, entity.getCreatedBy());
				if (entity.getCreatedDate() != null) {
					ps.setTimestamp(22, new Timestamp(entity.getCreatedDate().getTime()));
				} else {
					ps.setNull(22, Types.TIMESTAMP);
				}
				ps.setString(23, entity.getModifiedBy());
				if (entity.getModifiedDate() != null) {
					ps.setTimestamp(24, new Timestamp(entity.getModifiedDate().getTime()));

				} else {
					ps.setNull(24, Types.TIMESTAMP);
				}
				ps.setString(25, entity.getRemarks());
				ps.setString(26, entity.getStatus());
				ps.setString(27, entity.getActionType());
				if (entity.getActionDate() != null) {

					ps.setTimestamp(28, new Timestamp(entity.getActionDate().getTime()));

				} else {

					ps.setNull(28, Types.TIMESTAMP);
				}
				ps.setString(29, entity.getActionUser());
				ps.setString(30, entity.getUploadId());
				ps.setString(31, entity.getFileName());
			});

			totalInserted += batch.size();

			logger.info("Inserted temp batch size : {}", batch.size());
		}

		logger.info("Total Temp Records Inserted : {}", totalInserted);
	}

	// SPLIT LIST
	private <T> List<List<T>> splitList(List<T> list, int batchSize) {
		List<List<T>> batches = new ArrayList<>();
		if (list == null || list.isEmpty()) {
			return batches;
		}
		for (int i = 0; i < list.size(); i += batchSize) {
			batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
		}
		return batches;
	}
    
    // BATCH INSERT ERROR TABLE
	private void batchInsertError(List<SAPMasterError> errorEntityList) {
        logger.info(" ERROR BATCH INSERT STARTED");
		if (errorEntityList == null || errorEntityList.isEmpty()) {
			logger.info("No error records found");
			return;
		}

        String sql =
                "INSERT INTO TM_CCR_SAP_MST_ERROR ("
                        + "ERROR_ID, "
                        + "SR_NO, "
                        + "UNIT_CODE, "
                        + "CLEAN_UNIT_ID, "
                        + "VENDOR_NAME, "
                        + "CCA_CALL_CENTER, "
                        + "PAN, "
                        + "SAP_VENDOR_CODE, "
                        + "STATE, "
                        + "TDS_RATE, "
                        + "TAX_CODE, "
                        + "FINALI_BOXIDS, "
                        + "STATUS_OF_BLOCKING, "
                        + "ACCOUNT_STATUS, "
                        + "CRED_INFO_NO, "
                        + "GSTN_NO, "
                        + "SAC_CODE, "
                        + "SERVICE_PROVIDER_ID_STATUS, "
                        + "GST_APPLICABLE, "
                        + "HOLD_STATUS, "
                        + "PAYMENT_MODE, "
                        + "ERROR_MSG, "
                        + "CREATED_BY, "
                        + "CREATED_DATE, "
                        + "UPLOAD_ID, "
                        + "ROW_NUMBER "
                        + ") VALUES ("
                        + "SAP_ERROR_SEQ.NEXTVAL, "
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?, ?"
                        + ")";

		int batchSize = 500;

		List<List<SAPMasterError>> batches = splitList(errorEntityList, batchSize);

		int totalInserted = 0;

		for (List<SAPMasterError> batch : batches) {

			jdbcTemplate.batchUpdate(sql, batch, batch.size(), (PreparedStatement ps, SAPMasterError entity) -> {

				ps.setString(1, entity.getSrNo());
				ps.setString(2, entity.getUnitCode());
				ps.setString(3, entity.getCleanUnitId());
				ps.setString(4, entity.getVendorName());
				ps.setString(5, entity.getCcaCallCenter());
				ps.setString(6, entity.getPan());
				ps.setString(7, entity.getSapVendorCode());
				ps.setString(8, entity.getState());
				ps.setString(9, entity.getTdsRate());
				ps.setString(10, entity.getTaxCode());
				ps.setString(11, entity.getFinaliBoxids());
				ps.setString(12, entity.getStatusOfBlocking());
				ps.setString(13, entity.getAccountStatus());
				ps.setString(14, entity.getCredInfoNo());
				ps.setString(15, entity.getGstnNo());
				ps.setString(16, entity.getSacCode());
				ps.setString(17, entity.getServiceProviderIdStatus());
				ps.setString(18, entity.getGstApplicable());
				ps.setString(19, entity.getHoldStatus());
				ps.setString(20, entity.getPaymentMode());
				ps.setString(21, entity.getErrorMsg());
				ps.setString(22, entity.getCreatedBy());
				if (entity.getCreatedDate() != null) {
					ps.setTimestamp(23, new Timestamp(entity.getCreatedDate().getTime()));
				} else {

					ps.setNull(23, Types.TIMESTAMP);
				}
				// UPLOAD ID
				ps.setString(24, entity.getUploadId());

				// ROW NUMBER
				if (entity.getRowNumber() != null) {

					ps.setInt(25, entity.getRowNumber());

				} else {

					ps.setNull(25, Types.INTEGER);
				}
			});
			totalInserted += batch.size();
			logger.info("Inserted error batch size : {}", batch.size());
		}
		logger.info("Total Error Records Inserted : {}", totalInserted);
		logger.info("ERROR BATCH INSERT COMPLETED");
	}
    
    
    
    //MAP ROW TO ERROR ENTITY
	private SAPMasterError mapToErrorEntity(Row row, Map<Integer, Field> columnFieldMap, DataFormatter formatter)
			throws Exception {

		SAPMasterError errorEntity = new SAPMasterError();

		for (Map.Entry<Integer, Field> entry : columnFieldMap.entrySet()) {

			Integer columnIndex = entry.getKey();
			Field entityField = entry.getValue();
			Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
			String value = formatter.formatCellValue(cell);
			if (value != null) {
				value = value.trim();
			}

			value = sanitizeValue(value);
			Field errorField = ERROR_FIELD_CACHE.get(entityField.getName().toUpperCase());

			if (errorField == null) {
				continue;
			}
			errorField.set(errorEntity, value);
		}

		return errorEntity;
	}

	private boolean isRowEmpty(Row row) {
		if (row == null) {
			return true;
		}
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell == null) {
				continue;
			}
			if (cell.getCellType() != CellType.BLANK) {
				String value = new DataFormatter().formatCellValue(cell);
				if (value != null && !value.trim().isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	public ByteArrayInputStream exportErrorExcel(String uploadId) {

		logger.info("EXPORT ERROR EXCEL STARTED");
        logger.info("Upload Id : {}", uploadId);

		try {
			// FETCH ERROR RECORDS
			List<SAPMasterError> errorList = sapMasterErrorRepository.findByUploadIdOrderByRowNumber(uploadId);

			if (errorList == null || errorList.isEmpty()) {
				throw new RuntimeException("No error records found for upload id : " + uploadId);
			}

			Workbook workbook = new XSSFWorkbook();

			Sheet sheet = workbook.createSheet("SAP_UPLOAD_ERRORS");
			CellStyle headerStyle = workbook.createCellStyle();

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			CellStyle errorStyle = workbook.createCellStyle();

			Font errorFont = workbook.createFont();
			errorFont.setColor(IndexedColors.RED.getIndex());
			errorStyle.setFont(errorFont);

			String[] headers = {

                    "ROW_NUMBER",
                    "SR_NO",
                    "UNIT_CODE",
                    "CLEAN_UNIT_ID",
                    "VENDOR_NAME",
                    "CCA_CALL_CENTER",
                    "PAN",
                    "SAP_VENDOR_CODE",
                    "STATE",
                    "TDS_RATE",
                    "TAX_CODE",
                    "FINALI_BOXIDS",
                    "STATUS_OF_BLOCKING",
                    "ACCOUNT_STATUS",
                    "CRED_INFO_NO",
                    "GSTN_NO",
                    "SAC_CODE",
                    "SERVICE_PROVIDER_ID_STATUS",
                    "GST_APPLICABLE",
                    "HOLD_STATUS",
                    "PAYMENT_MODE",
                    "ERROR_MESSAGE"
            };

            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

			int rowNum = 1;

			for (SAPMasterError error : errorList) {

				Row row = sheet.createRow(rowNum++);

				int col = 0;
				row.createCell(col++).setCellValue(error.getRowNumber() != null ? error.getRowNumber() : 0);
				row.createCell(col++).setCellValue(safe(error.getSrNo()));
				row.createCell(col++).setCellValue(safe(error.getUnitCode()));
				row.createCell(col++).setCellValue(safe(error.getCleanUnitId()));
				row.createCell(col++).setCellValue(safe(error.getVendorName()));
				row.createCell(col++).setCellValue(safe(error.getCcaCallCenter()));
				row.createCell(col++).setCellValue(safe(error.getPan()));
				row.createCell(col++).setCellValue(safe(error.getSapVendorCode()));
				row.createCell(col++).setCellValue(safe(error.getState()));
				row.createCell(col++).setCellValue(safe(error.getTdsRate()));
				row.createCell(col++).setCellValue(safe(error.getTaxCode()));
				row.createCell(col++).setCellValue(safe(error.getFinaliBoxids()));
				row.createCell(col++).setCellValue(safe(error.getStatusOfBlocking()));
				row.createCell(col++).setCellValue(safe(error.getAccountStatus()));
				row.createCell(col++).setCellValue(safe(error.getCredInfoNo()));
				row.createCell(col++).setCellValue(safe(error.getGstnNo()));
				row.createCell(col++).setCellValue(safe(error.getSacCode()));
				row.createCell(col++).setCellValue(safe(error.getServiceProviderIdStatus()));
				row.createCell(col++).setCellValue(safe(error.getGstApplicable()));
				row.createCell(col++).setCellValue(safe(error.getHoldStatus()));
				row.createCell(col++).setCellValue(safe(error.getPaymentMode()));

				Cell errorCell = row.createCell(col++);
				errorCell.setCellValue(safe(error.getErrorMsg()));
				errorCell.setCellStyle(errorStyle);
			}
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			workbook.close();
			logger.info("EXPORT ERROR EXCEL COMPLETED");
			return new ByteArrayInputStream(out.toByteArray());

		} catch (Exception e) {
			logger.error("Error while exporting SAP Error Excel", e);
			throw new RuntimeException("Failed to export SAP error excel : " + e.getMessage());
		}
    }
    private String safe(String value) {

        return value == null ? "" : value;
    }

	private String generateUploadId(String fileName) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		return "SAP_" + timestamp;
	}
    
}
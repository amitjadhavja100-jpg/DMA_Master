package com.icici.dma.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.globalException.ResourceNotFoundException;
/*import com.jcraft.jsch.Logger;*/
import com.icici.dma.serviceImpl.RcaCibilServiceImpl;
import com.icici.dma.serviceImpl.RcaProcessShopServiceImpl;
import com.icici.dma.serviceImpl.RcasCBCServiceimpl;

@Service
@Transactional
public class RcasService {

	private static final Logger logger = LogManager.getLogger(RcasService.class);


	@Autowired
	private RcaCibilServiceImpl rcaCibilServiceImpl;

	@Autowired
	private RcasCBCServiceimpl rcasCBCServiceimpl;

	@Autowired
	private RcaProcessShopServiceImpl rcaProcessShopServiceImpl;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private String generateUploadId(String fileName) {

		// remove extension
		String baseName = fileName.replace(".xlsx", "").replace(".xls", "");

		// take first 3 characters
		String prefix = baseName.substring(0, Math.min(3, baseName.length())).toUpperCase();

		// timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

		// random number
//		int random = ThreadLocalRandom.current().nextInt(1000, 9999);
		Long sequence = jdbcTemplate.queryForObject("SELECT TM_VHL_RCAS_DUMP_UPL_SEQ.NEXTVAL FROM DUAL", long.class);

		return prefix + "_" + timestamp + "_" + sequence;
	}

	public String uploadExcelRcas(MultipartFile file, String user, Date cycleFromDate, Date cycleToDate) {

		if (file == null || file.isEmpty()) {

			logger.error("Uploaded file is empty");
			throw new RuntimeException("Uploaded file is empty");
		}
		/*
		 * if(file!=null){ throw new RuntimeException(
		 * "File already available.Do you want to replace it?"); }
		 */
		


		String fileName = file.getOriginalFilename();

		String uploadId = generateUploadId(file.getOriginalFilename());

		logger.info("Uploaded File Name" + fileName);
		// System.out.println("Uploaded File Name"+fileName);

		Set<String> validSheets = new HashSet<>();
		validSheets.add("CBC Master");
		validSheets.add("CIBIL&Entity mapping");
		validSheets.add("Process_Shop");

		try {
			logger.info("Enter the serive method");
			// System.out.println("Enter the serive method");
			InputStream inputStream = file.getInputStream();
			Workbook workbook = new XSSFWorkbook(inputStream);
			/* Sheet sheet = workbook.getSheetAt(0); */

			logger.info("Enter the sheet");
			// System.out.println("Enter the sheet");
			/* String sheetName = sheet.getSheetName(); */

			int numberOfSheets = workbook.getNumberOfSheets();
			
			Map<String, Integer> rcasCBCCount = null;
			Map<String, Integer> rcasCibilCount= null;
			Map<String, Integer> rcasProcessShopCount= null;

			for (int i = 0; i < numberOfSheets; i++) {

				Sheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();

				if (!validSheets.contains(sheetName)) {

					logger.error("Invalid excel file sheet Name");
					return "Invalid excel file sheet Name";
				}
				switch (sheetName) {
				case "CBC Master":
					 rcasCBCCount = rcasCBCServiceimpl.uploadRcasCBCExcel(sheet, user, cycleFromDate, cycleToDate, uploadId,fileName);
//					Thread.sleep(30000);
					break;

				case "CIBIL&Entity mapping":
					rcasCibilCount = rcaCibilServiceImpl.saveCibilSheet(sheet, user, cycleFromDate, cycleToDate, uploadId,fileName);
//					Thread.sleep(30000);Map<String, Integer> rcasCibilCount
					break;

				case "Process_Shop":
					 rcasProcessShopCount = rcaProcessShopServiceImpl.saveProcessShopSheet(sheet, user, cycleFromDate, cycleToDate, uploadId,fileName);
//					Thread.sleep(30000);
					break;

				default:

					logger.error("Unknown Sheet: " + sheetName);
					// System.out.println("Unknown Sheet: " + sheetName);
				}
			}

			workbook.close();

			logger.info("uploadId : " +uploadId);
			logger.info("Rcas CBC Record Count : "+rcasCBCCount.toString());
			logger.info("Rcas CIBIL Record Count : "+rcasCibilCount.toString());
			logger.info("Rcas ProcessShop Record Count : "+rcasProcessShopCount.toString());
		
//			String message = "Upload Completed Successfully " + "\n" 
//					+ "Total Records in file : " + (rcasCBCCount.get("TotalRecordInFile") + rcasCibilCount.get("TotalRecordInFile") + rcasProcessShopCount.get("TotalRecordInFile"))+ "\n" 
//					+ "Count of added records : " + (rcasCBCCount.get("insertCount")+rcasCibilCount.get("insertCount") + rcasProcessShopCount.get("insertCount"))+ "\n"
//					+ "Count of error records : " + (rcasCBCCount.get("errorCount")+rcasCibilCount.get("errorCount") + rcasProcessShopCount.get("errorCount"));
			
			String message = "Upload Completed Successfully " + "\n" + "\n"
					+ "Total Records in file RCAS-CBC : " + rcasCBCCount.get("TotalRecordInFile")+ "\n" 
					+ "Count of added records RCAS-CBC : " +rcasCBCCount.get("insertCount")+ "\n" 
					+ "Count of error records : " + rcasCBCCount.get("errorCount")	+ "\n" + "\n"
					
					+ "Total Records in file RCAS-CIBIL : "	+ rcasCibilCount.get("TotalRecordInFile") + "\n" 
					+ "Count of added records RCAS-CIBIL : " +rcasCibilCount.get("insertCount") + "\n"
					+ "Count of error records RCAS-CIBIL : " +rcasCibilCount.get("errorCount")+ "\n" + "\n"
					
					+ "Total Records in file RCAS-PROCESS-SHOP: "+ rcasProcessShopCount.get("TotalRecordInFile")+ "\n" 
					+ "Count of added records RCAS-PROCESS-SHOP: " + rcasProcessShopCount.get("insertCount")+ "\n"
					+ "Count of error records RCAS-PROCESS-SHOP: " + rcasProcessShopCount.get("errorCount");
			
			
			logger.info("Rcas file uploaded Successfully!");
//			return "Rcas file uploaded Successfully!";
			return message;

		} catch (Exception e) {
			e.printStackTrace();

			logger.error("Error: " + e.getMessage());
			return "Error: " + e.getMessage();
		}
	}

	@Transactional
	public ByteArrayInputStream exportRcasErrorExcel(String user) throws Exception {
		logger.info("Starting error export for User: {}", user);

		try {
			// get latest upload Id
			// String cibilUploadId1 =
			// rcasCbcErrorRepo.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);
			// String psUploadId2 =
			// rcasCbcErrorRepo.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);
			// String cbcUploadId3 =
			// rcasCbcErrorRepo.findTopUploadIdByCreatedByOrderByCreatedDateDesc(user);

			String sql = "SELECT final_upload_id FROM ( " + "    SELECT final_upload_id, created_date FROM ( "
					+ "        SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id, created_date "
					+ "        FROM TM_VHL_RCAS_CIBIL_DUMP_ERROR WHERE created_by = ? ORDER BY created_date DESC "
					+ "    ) WHERE ROWNUM = 1 " +

					"    UNION ALL " +

					"    SELECT final_upload_id, created_date FROM ( "
					+ "        SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id, SYSTEM_CREATED_DATE as created_date "
					+ "        FROM TM_VHL_RCAS_PROCESS_SHOP_DUMP_ERROR WHERE created_by = ? ORDER BY created_date DESC "
					+ "    ) WHERE ROWNUM = 1 " +

					"    UNION ALL " +

					"    SELECT final_upload_id, created_date FROM ( "
					+ "        SELECT NVL(upload_id, ora_err_tag$) AS final_upload_id , created_date "
					+ "        FROM TM_VHL_RCAS_CBC_DUMP_ERROR WHERE created_by = ? ORDER BY created_date DESC "
					+ "    ) WHERE ROWNUM = 1 " +

					") ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY";

//		String uploadId = jdbcTemplate.queryForObject(sql, String.class, user, user, user);

			List<String> list = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("final_upload_id"), user, user,
					user);

			String uploadId = list.isEmpty() ? null : list.get(0);

			logger.info("uploadId from RCAS::: " + uploadId);

			// uploadId = "FIN_20260411_112255_3156";

			if (uploadId == null ) {
				throw new ResourceNotFoundException("No Error Record");
			}

			logger.info("Starting error export for uploadId: {}", uploadId);

			// ---------------------------------------------------
			// Step 2: Create Workbook
			// ---------------------------------------------------

			Workbook workbook = new XSSFWorkbook();

			Sheet sheet1 = workbook.createSheet("CIBIL&Entity mapping");
			Sheet sheet2 = workbook.createSheet("Process_Shop");
			Sheet sheet3 = workbook.createSheet("CBC Master");
			// HEADER FONT STYLE

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontName("Mulish");
			headerFont.setFontHeightInPoints((short) 11);

			CellStyle headerStyle = workbook.createCellStyle();
			headerStyle.setFont(headerFont);

			// DATA FONT STYLE
			Font dataFont = workbook.createFont();
			dataFont.setFontName("Mulish");
			dataFont.setFontHeightInPoints((short) 11);

			CellStyle dataStyle = workbook.createCellStyle();
			dataStyle.setFont(dataFont);

			// ---------------------------------------------------
			// Step 1: Fetch error records from DB
			// ---------------------------------------------------

			logger.info("Fetching error from Rcas_cibil_dump with uploadId: {}", uploadId);

			String sql1 = "SELECT * " + "FROM TM_VHL_RCAS_CIBIL_DUMP_ERROR " + "WHERE upload_id = ?"
					+ " OR ora_err_tag$ = ?";

			List<Map<String, Object>> cibilDumpErrors = jdbcTemplate.queryForList(sql1, uploadId, uploadId);

			logger.info("Error from Rcas_cibil_dump with count: {}", cibilDumpErrors.size());

			// ======================================================================================================

			logger.info("Fetching error from Rcas_process_shop_dump with uploadId: {}", uploadId);

			String sql2 = "SELECT * " + "FROM tm_vhl_rcas_process_shop_dump_error " + "WHERE upload_id = ?"
					+ " OR ora_err_tag$ = ?";

			List<Map<String, Object>> PsDumpErrors = jdbcTemplate.queryForList(sql2, uploadId, uploadId);

			logger.info("Error from Rcas_process_shop_dump with count: {}", PsDumpErrors.size());

			// ======================================================================================================

			logger.info("Fetching error from Rcas_cbc_dump with uploadId: {}", uploadId);

			String sql3 = "SELECT * " + "FROM tm_vhl_rcas_cbc_dump_error " + "WHERE upload_id = ?"
					+ " OR ora_err_tag$ = ?";

			List<Map<String, Object>> cbcErrors = jdbcTemplate.queryForList(sql3, uploadId, uploadId);

			logger.info("Error from Rcas_cbc_dump with count: {}", cbcErrors.size());

			// ======================================================================================================

			logger.info("cibilDumpErrors::: " + cibilDumpErrors.size() + "PsDumpErrors::: " + PsDumpErrors.size()
					+ "cbcErrorss::: " + cbcErrors.size());

			/*
			 * if ((cibilDumpErrors == null || cibilDumpErrors.isEmpty()) && (PsDumpErrors
			 * == null || PsDumpErrors.isEmpty()) && (cbcErrorss == null ||
			 * cbcErrorss.isEmpty())) {
			 * 
			 * logger.warn("No error records found for uploadId: {}", uploadId);
			 * 
			 * throw new ResourceNotFoundException("No error records found for uploadId: " +
			 * uploadId); }
			 */

			if (cibilDumpErrors != null && !cibilDumpErrors.isEmpty()) {

				// ---------------------------------------------------
				// Create Header Row
				// ---------------------------------------------------

				logger.info("Enter in the RCA CIBIL sheet 1 " + sheet1);

				logger.info("Start Header Creation RCA CIBIL sheet 1");

				String[] headerList = { "RCAS No", "LAN NO", "DISBURSEMENT DATE", "CIBIL_SCORE", "APPLICANT_TYPE",
						"CUSTOMER_NAME", "CUSTOMER_TYPE", "CREATED_BY", "UPLOAD_DATE", "ERROR_MSG" }; // 5

				Row header = sheet1.createRow(0);
				for (int i = 0; i < headerList.length; i++) {

					Cell cell = header.createCell(i);
					cell.setCellValue(headerList[i]);
					cell.setCellStyle(headerStyle);
				}

				logger.info("END Header Creation RCA CIBIL sheet 1");

				// ---------------------------------------------------
				// Write Data Rows
				// ---------------------------------------------------
				logger.info("Start Error record row Creation RCA CIBIL sheet 1");

				int rowIndex = 1;

				for (Map<String, Object> error : cibilDumpErrors) {

					Row row = sheet1.createRow(rowIndex++);

					Cell cell00 = row.createCell(0);
					cell00.setCellValue(formatValue(error.get("RCAS_NO")));
					cell00.setCellStyle(dataStyle);

					Cell cell01 = row.createCell(1);
					cell01.setCellValue(formatValue(error.get("LAN_NO")));
					cell01.setCellStyle(dataStyle);

					Cell cell02 = row.createCell(2);
					cell02.setCellValue(formatValue(error.get("DISBURSEMENT_DATE")));
					cell02.setCellStyle(dataStyle);

					Cell cell03 = row.createCell(3);
					cell03.setCellValue(formatValue(error.get("CIBIL_SCORE")));
					cell03.setCellStyle(dataStyle);

					Cell cell04 = row.createCell(4);
					cell04.setCellValue(formatValue(error.get("APPLICANT_TYPE")));
					cell04.setCellStyle(dataStyle);

					Cell cell05 = row.createCell(5);
					cell05.setCellValue(formatValue(error.get("CUSTOMER_NAME")));
					cell05.setCellStyle(dataStyle);

					Cell cell06 = row.createCell(6);
					cell06.setCellValue(formatValue(error.get("CUSTOMER_TYPE")));
					cell06.setCellStyle(dataStyle);

					Cell cell07 = row.createCell(7);
					cell07.setCellValue(formatValue(error.get("CREATED_BY")));
					cell07.setCellStyle(dataStyle);

					Cell cell08 = row.createCell(8);
					cell08.setCellValue(formatValue(error.get("CREATED_DATE")));
					cell08.setCellStyle(dataStyle);

					Cell cell09 = row.createCell(9);
					cell09.setCellValue(formatValue(
							error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
					cell09.setCellStyle(dataStyle);

				}

				logger.info("End Error record row Creation RCA CIBIL sheet 1");
				// ---------------------------------------------------
				// Step 5: Auto size columns
				// ---------------------------------------------------

				for (int i = 0; i < 9; i++) {
					sheet1.autoSizeColumn(i);
				}
				logger.info("Exit in the RCA CIBIL sheet 1 ");

			} else {
				logger.info("No error Record In Rcas_cibil_dump for uploadId: {}", uploadId);
			}

			// =============================================================================================================

			if (PsDumpErrors != null && !PsDumpErrors.isEmpty()) {

				// ---------------------------------------------------
				// Step 3: Create Header Row
				// ---------------------------------------------------
				logger.info("Enter in the RCA Process Dump sheet 2 " + sheet2);

				logger.info("Start Header Creation RCA PROCESS SHOP sheet 2");

				String[] headerList = { "RCAS_ID", "CUSTMOER_NAME", "LAN_NO", "LEAD_ID", "CREATED_DATE", "ENTRY_DATE",
						"ACTIVITYNAME", "STATUS", "TRAY", "BROKER_NAME", "DISBURSAL_DATE", "MOBILE", "EMPLOYER_ID",
						"EMPLOYER_NAME", "CASE_STATUS", "RM_NAME", "DME_CODE", "DME_NAME", "HUB_NAME", "LOAN_AMNT",
						"PROCESS_SHOP", "BRANCH_NAME", "SCHEME_NAME", "PROMOTION_CODE", "CHANNEL_CODE",
						"PSL_DESCRIPTION", "PSL_TYPE", "PRECFOC", "LAR_COMMENTS", "CREATED_BY", "UPLOAD_DATE",
						"ERROR_MSG" }; // 5

				Row header = sheet2.createRow(0);
				for (int i = 0; i < headerList.length; i++) {

					Cell cell = header.createCell(i);
					cell.setCellValue(headerList[i]);
					cell.setCellStyle(headerStyle);
				}

				logger.info("END Header Creation RCA PROCESS SHOP sheet 2");

				// ---------------------------------------------------
				// Step 4: Write Data Rows
				// ---------------------------------------------------

				logger.info("START Error record row Creation RCA Process shop sheet 2");

				int rowIndex = 1;

				for (Map<String, Object> error : PsDumpErrors) {

					Row row = sheet2.createRow(rowIndex++);

					Cell cell00 = row.createCell(0);
					cell00.setCellValue(formatValue(error.get("RCAS_ID")));
					cell00.setCellStyle(dataStyle);

					Cell cell01 = row.createCell(1);
					cell01.setCellValue(formatValue(error.get("CUSTOMER_NAME")));
					cell01.setCellStyle(dataStyle);

					Cell cell02 = row.createCell(2);
					cell02.setCellValue(formatValue(error.get("LAN_NO")));
					cell02.setCellStyle(dataStyle);

					Cell cell03 = row.createCell(3);
					cell03.setCellValue(formatValue(error.get("LEAD_ID")));
					cell03.setCellStyle(dataStyle);

					Cell cell04 = row.createCell(4);
					cell04.setCellValue(formatValue(error.get("CREATED_DATE")));
					cell04.setCellStyle(dataStyle);

					Cell cell05 = row.createCell(5);
					cell05.setCellValue(formatValue(error.get("ENTRY_DATE")));
					cell05.setCellStyle(dataStyle);

					Cell cell06 = row.createCell(6);
					cell06.setCellValue(formatValue(error.get("ACTIVITYNAME")));
					cell06.setCellStyle(dataStyle);

					Cell cell07 = row.createCell(7);
					cell07.setCellValue(formatValue(error.get("STATUS")));
					cell07.setCellStyle(dataStyle);

					Cell cell08 = row.createCell(8);
					cell08.setCellValue(formatValue(error.get("TRAY")));
					cell08.setCellStyle(dataStyle);

					Cell cell09 = row.createCell(9);
					cell09.setCellValue(formatValue(error.get("BROKER_NAME")));
					cell09.setCellStyle(dataStyle);

					Cell cell010 = row.createCell(10);
					cell010.setCellValue(formatValue(error.get("DISBURSAL_DATE")));
					cell010.setCellStyle(dataStyle);

					Cell cell011 = row.createCell(11);
					cell011.setCellValue(formatValue(error.get("MOBILE")));
					cell011.setCellStyle(dataStyle);

					Cell cell012 = row.createCell(12);
					cell012.setCellValue(formatValue(error.get("EMPLOYER_ID")));
					cell012.setCellStyle(dataStyle);

					Cell cell013 = row.createCell(13);
					cell013.setCellValue(formatValue(error.get("EMPLOYER_NAME")));
					cell013.setCellStyle(dataStyle);

					Cell cell014 = row.createCell(14);
					cell014.setCellValue(formatValue(error.get("CASE_STATUS")));
					cell014.setCellStyle(dataStyle);

					Cell cell015 = row.createCell(15);
					cell015.setCellValue(formatValue(error.get("RM_NAME")));
					cell015.setCellStyle(dataStyle);

					Cell cell016 = row.createCell(16);
					cell016.setCellValue(formatValue(error.get("DME_CODE")));
					cell016.setCellStyle(dataStyle);

					Cell cell017 = row.createCell(17);
					cell017.setCellValue(formatValue(error.get("DME_NAME")));
					cell017.setCellStyle(dataStyle);

					Cell cell018 = row.createCell(18);
					cell018.setCellValue(formatValue(error.get("HUB_NAME")));
					cell018.setCellStyle(dataStyle);

					Cell cell019 = row.createCell(19);
					cell019.setCellValue(formatValue(error.get("LOAN_AMNT")));
					cell019.setCellStyle(dataStyle);

					Cell cell020 = row.createCell(20);
					cell020.setCellValue(formatValue(error.get("PROCESS_SHOP")));
					cell020.setCellStyle(dataStyle);

					Cell cell021 = row.createCell(21);
					cell021.setCellValue(formatValue(error.get("BRANCH_NAME")));
					cell021.setCellStyle(dataStyle);

					Cell cell022 = row.createCell(22);
					cell022.setCellValue(formatValue(error.get("SCHEME_NAME")));
					cell022.setCellStyle(dataStyle);

					Cell cell023 = row.createCell(23);
					cell023.setCellValue(formatValue(error.get("PROMOTION_CODE")));
					cell023.setCellStyle(dataStyle);

					Cell cell024 = row.createCell(24);
					cell024.setCellValue(formatValue(error.get("CHANNEL_CODE")));
					cell024.setCellStyle(dataStyle);

					Cell cell025 = row.createCell(25);
					cell025.setCellValue(formatValue(error.get("PSL_DESCRIPTION")));
					cell025.setCellStyle(dataStyle);

					Cell cell026 = row.createCell(26);
					cell026.setCellValue(formatValue(error.get("PSL_TYPE")));
					cell026.setCellStyle(dataStyle);

					Cell cell027 = row.createCell(27);
					cell027.setCellValue(formatValue(error.get("PRECFOC")));
					cell027.setCellStyle(dataStyle);

					Cell cell028 = row.createCell(28);
					cell028.setCellValue(formatValue(error.get("LAR_COMMENTS")));
					cell028.setCellStyle(dataStyle);

					Cell cell029 = row.createCell(29);
					cell029.setCellValue(formatValue(error.get("CREATED_BY")));
					cell029.setCellStyle(dataStyle);

					Cell cell030 = row.createCell(30);
					cell030.setCellValue(formatValue(error.get("SYSTEM_CREATED_DATE")));
					cell030.setCellStyle(dataStyle);

					Cell cell031 = row.createCell(31);
					cell031.setCellValue(formatValue(
							error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
					cell031.setCellStyle(dataStyle);

				}

				logger.info("END Error record row Creation RCA Process shop sheet 2");

				// ---------------------------------------------------
				// Step 5: Auto size columns
				// ---------------------------------------------------

				for (int i = 0; i < 31; i++) {
					sheet2.autoSizeColumn(i);
				}
				logger.info("Exit in the RCA Process Dump sheet 2 ");

			} else {
				logger.info("No error Record In Rcas_process_shop_dump for uploadId: {}", uploadId);
			}

			// =============================================================================================================

			if (cbcErrors != null && !cbcErrors.isEmpty()) {

				logger.info("Enter in the RCA CBC Dump sheet 3 " + sheet3);

				logger.info("Start Header Creation RCA CBC  : sheet 3");

				String[] headerList = { "CBC Code", "Process Shop", "CREATED_BY", "UPLOAD_DATE", "ERROR_MSG" }; // 5

				Row header = sheet3.createRow(0);
				for (int i = 0; i < headerList.length; i++) {

					Cell cell = header.createCell(i);
					cell.setCellValue(headerList[i]);
					cell.setCellStyle(headerStyle);
				}

				logger.info("END Header Creation RCA CBC  : sheet 3");

				// ---------------------------------------------------
				// Step 4: Write Data Rows
				// ---------------------------------------------------

				logger.info("START Error record row Creation RCA cbc shop :sheet 3");

				int rowIndex = 1;

				for (Map<String, Object> error : cbcErrors) {

					Row row = sheet3.createRow(rowIndex++);

					Cell cell00 = row.createCell(0);
					cell00.setCellValue(formatValue(error.get("CBC_CODE")));
					cell00.setCellStyle(dataStyle);

					Cell cell01 = row.createCell(1);
					cell01.setCellValue(formatValue(error.get("PROCESS_SHOP")));
					cell01.setCellStyle(dataStyle);

					Cell cell02 = row.createCell(2);
					cell02.setCellValue(formatValue(error.get("CREATED_BY")));
					cell02.setCellStyle(dataStyle);

					Cell cell03 = row.createCell(3);
					cell03.setCellValue(formatValue(error.get("CREATED_DATE")));
					cell03.setCellStyle(dataStyle);

					Cell cell04 = row.createCell(4);
					cell04.setCellValue(formatValue(
							error.get("ERROR_MSG") != null ? error.get("ERROR_MSG") : error.get("ORA_ERR_MESG$")));
					cell04.setCellStyle(dataStyle);

				}

				logger.info("END Error record row Creation RCA cbc shop :sheet 3");
				
		
				// ---------------------------------------------------
				// Step 5: Auto size columns
				// ---------------------------------------------------

				for (int i = 0; i < 4; i++) {
					sheet2.autoSizeColumn(i);
				}
				logger.info("Enter in the RCA CBC Dump sheet 3 " + sheet3);

			} else {
				logger.info("No error Record In Rcas_CBC_dump for uploadId: {}", uploadId);
			}

	// =============================================================================================================
			// ---------------------------------------------------
			// Step 6: Convert Workbook to InputStream
			// ---------------------------------------------------

			ByteArrayOutputStream out = new ByteArrayOutputStream();

			workbook.write(out);
			int size = out.size();

			logger.info("RCAS workbook::: " + size);

			workbook.close();

			logger.info("Excel export completed successfully " + out.toByteArray());

			return new ByteArrayInputStream(out.toByteArray());

		} catch (DataAccessException e) {
			logger.info("DB error while fetching the Finnone error Records ", e);
			throw new RuntimeException("Database Error occured");
		} catch (IOException ex) {
			logger.info("Excel Genration Failed ", ex);
			throw new RuntimeException("Failed to genrate error file");
		}
	}

	private String formatValue(Object value) {

		if (value == null) {
			return "";
		}

		if (value instanceof Date) {

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

			return sdf.format((Date) value);
		}

		if (value instanceof LocalDate) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

			return ((LocalDate) value).format(formatter);
		}

		if (value instanceof LocalDateTime) {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

			return ((LocalDateTime) value).toLocalDate().format(formatter);
		}

		return value.toString();
	}
}

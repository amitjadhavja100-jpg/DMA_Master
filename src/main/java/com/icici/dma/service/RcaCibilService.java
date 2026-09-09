package com.icici.dma.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.model.RCA_CBC;
import com.icici.dma.model.RCA_CIBIL;
import com.icici.dma.model.RCA_ProcessShop;
import com.icici.dma.repository.RcaCbcRepository;
import com.icici.dma.repository.RcaCibilRepository;
import com.icici.dma.repository.RcaProcessShop;

@Service
@Transactional
public class RcaCibilService {
	
	private static final Logger logger = LogManager.getLogger(RcaCibilService.class);

	@Autowired
	private RcaCibilRepository repository;

	@Autowired
	private RcaCbcRepository cbcRepository;

	@Autowired
	private RcaProcessShop processShop;

	public String saveCibil(MultipartFile file, String user,Date cycleFromDate,Date cycleToDate) {
		
		if (file == null || file.isEmpty()) {
			
			logger.error("Uploaded file is empty");
			throw new RuntimeException("Uploaded file is empty");
		}
		/*if(file!=null){
			throw new RuntimeException("File already available.Do you want to replace it?");
		}*/
		
		String fileName = file.getOriginalFilename();
		
		logger.info("Uploaded File Name"+fileName);
		//System.out.println("Uploaded File Name"+fileName);
		
		Set<String> validSheets = new HashSet<>();
		validSheets.add("CBC Master");
		validSheets.add("CIBIL&Entity mapping");
		validSheets.add("Process_Shop");

		try {
			logger.info("Enter the serive method");
			//System.out.println("Enter the serive method");
			InputStream inputStream = file.getInputStream();
			Workbook workbook = new XSSFWorkbook(inputStream);
			/*Sheet sheet = workbook.getSheetAt(0);*/
			
			logger.info("Enter the sheet");
			//System.out.println("Enter the sheet");
			/*String sheetName = sheet.getSheetName();*/

			int numberOfSheets = workbook.getNumberOfSheets();

			for (int i = 0; i < numberOfSheets; i++) {

				Sheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				
				if (!validSheets.contains(sheetName)) {
					
					logger.error("Invalid excel file sheet Name");
					return "Invalid excel file sheet Name";
				}
				
				if (sheet == null) {
					
					logger.error("Excel sheet not found");
					throw new RuntimeException("Excel sheet not found");
				}
				switch (sheetName) {

				case "CBC Master":
					saveCbcSheet(sheet,user,cycleFromDate,cycleToDate);
					break;

				case "CIBIL&Entity mapping":
					saveCibilSheet(sheet,user,cycleFromDate,cycleToDate);
					break;

				case "Process_Shop":
					saveProcessShopSheet(sheet,user,cycleFromDate,cycleToDate);
					break;

				default:
					
					logger.error("Unknown Sheet: " + sheetName);
					//System.out.println("Unknown Sheet: " + sheetName);
				}
			}

			
			workbook.close();
			
			logger.info("Rcas file uploaded Successfully!");
			return "Rcas file uploaded Successfully!";

		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error("Error: " + e.getMessage());
			return "Error: " + e.getMessage();
		}
	}

	private String saveCibilSheet(Sheet sheet,String user,Date cycleFromDate,Date cycleToDate) {

		Iterator<Row> rows = sheet.iterator();

			List<RCA_CIBIL> list = new ArrayList<>();
			
			logger.info("Enter the RCA_CIBIL");
			//System.out.println("Enter the RCA_CIBIL");

		int rowNumber = 0;
		Row headerRow = sheet.getRow(0);
		if (!validateHeaders(headerRow)) {
			
			logger.error("Invalid file format: Header mismatch. File Rejected.");
			throw new RuntimeException("Invalid file format: Header mismatch. File Rejected.");
		}

			while (rows.hasNext()) {

				Row row = rows.next();

				if (rowNumber == 0) { // skip header
					rowNumber++;
					continue;
				}
				RCA_CIBIL entity = new RCA_CIBIL();
				repository.deleteAll();
				
				entity.setRcasNo(getCellValue(row.getCell(0)));
				entity.setLanNo(getCellValue(row.getCell(1)));

				Cell dateCell = row.getCell(2);
				if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC) {
					entity.setDisbursementDate(dateCell.getDateCellValue());
				}

				entity.setCibilScore(getCellValue(row.getCell(3)));
				entity.setApplicantType(getCellValue(row.getCell(4)));
				entity.setCustomerName(getCellValue(row.getCell(5)));
				entity.setCustomerType(getCellValue(row.getCell(6)));
				entity.setCreatedBy(user);
				entity.setFromCycleDate(cycleFromDate);
				entity.setToCycleDate(cycleToDate);
//				entity.setCycleFromDate(cycleFromDate);
//				entity.setCycleToDate(cycleToDate);
				entity.setCreatedDate(new Date());

				
				/*if(entity.getRcasNo() == null || entity.getRcasNo().trim().isEmpty()){
					return "Rcas number is null";
				}
				
				if(entity.getLanNo() == null || entity.getLanNo().trim().isEmpty()){
					return "LAN number is null";
				}
				
				if(entity.getDisbursementDate() == null){
					return "Disbursement Date is null";
				}
				
				if(entity.getCibilScore() == null){
					return "CIBIL SCORE is null";
				}
				
				if(entity.getApplicantType() == null){
					return "Applicant Type is null";
				}
				
				if(entity.getCustomerName() == null){
					return "CUSTOMER NAME is null";
				}
				
				if(entity.getCustomerType() == null){
					return "CUSTOMER TYPE is null";
				}*/

				
				list.add(entity);

				rowNumber++;

				/*if (list.size() == 1000) {
					repository.saveAll(list);
					repository.flush();
					list.clear();
				}*/
			}

		// Save remaining records
		/*if (!list.isEmpty()) {
			repository.saveAll(list);
		}*/
			//repository.deleteAllInBatch();						
			
			repository.saveAll(list);
			
		
		logger.info("Rcas CIBIL and Entity Mapping sheet uploaded successfully");
		return "Rcas CIBIL and Entity Mapping sheet uploaded successfully";
	}

	private String saveCbcSheet(Sheet sheet,String user,Date cycleFromDate,Date cycleToDate) {

		Iterator<Row> rows = sheet.iterator();
		
		List<RCA_CBC> list = new ArrayList<>();
		
		logger.info("Enter the RCA_CBC");
		//System.out.println("Enter the RCA_CBC");

		int rowNumber = 0;
		Row headerRow = sheet.getRow(0);
		while (rows.hasNext()) {

			Row row = rows.next();

			if (rowNumber == 0) { // skip header
				rowNumber++;
				continue;
			}

			RCA_CBC entity = new RCA_CBC();
			//cbcRepository.deleteAllInBatch();
			cbcRepository.deleteAll();

			entity.setCbcCode(getCellValue(row.getCell(0)));
			entity.setProcessShop(getCellValue(row.getCell(1)));
			entity.setCreatedBy(user);
			entity.setCreatedDate(new Date());
			entity.setFromCycleDate(cycleFromDate);
			entity.setToCycleDate(cycleToDate);
			
			if (entity.getCbcCode() == null || entity.getCbcCode().trim().isEmpty()) {
				
				logger.error("CBC COD IS NULL");
				return "CBC COD IS NULL";

				}
			
			if(entity.getProcessShop() == null){
				
				logger.error("PROCESS SHOP IS NULL");
				return "PROCESS SHOP IS NULL";
			}

			list.add(entity);

			rowNumber++;

			/*if (list.size() == 1000) {
				cbcRepository.saveAll(list);
				cbcRepository.flush();
				list.clear();
			}*/
		}

		// Save remaining records
		/*if (!list.isEmpty()) {
			cbcRepository.saveAll(list);
		}*/
		
		cbcRepository.saveAll(list);
		
		logger.info("Rcas CBC sheet uploaded successfully");
		return "Rcas CBC sheet uploaded successfully";

	}

	private String saveProcessShopSheet(Sheet sheet,String user,Date cycleFromDate,Date cycleToDate){
		
		Iterator<Row> rows = sheet.iterator();

		List<RCA_ProcessShop> list = new ArrayList<>();
		
		logger.info("Enter the RCA_ProcessShop");
		//System.out.println("Enter the RCA_ProcessShop");
		
		int rowNumber = 0;
		 Row headerRow = sheet.getRow(0);
		/*if (!validateHeaders(headerRow)) {
			throw new RuntimeException("Invalid file format: Header mismatch. File Rejected.");
		}*/

		while (rows.hasNext()) {

			Row row = rows.next();

			if (rowNumber == 0) { // skip header
				rowNumber++;
				continue;
			}

			RCA_ProcessShop entity = new RCA_ProcessShop();			
			//processShop.deleteAllInBatch();
			processShop.deleteAll();
			
			entity.setRcasId(getCellValue(row.getCell(0)));
			entity.setCustomerName(getCellValue(row.getCell(1)));
			entity.setLanNo(getCellValue(row.getCell(2)));
			entity.setLeadId(getCellValue(row.getCell(3)));
			entity.setCreatedBy(user);
			entity.setCreatedDate(new Date());
			entity.setSystemCreatedDate(new Date());
			entity.setFromCycleDate(cycleFromDate);
			entity.setToCycleDate(cycleToDate);
//			entity.setCycleFromDate(cycleFromDate);
//			entity.setCycleToDate(cycleToDate);
			
			Cell dateCell = row.getCell(4);
			if (dateCell != null && dateCell.getCellType() == CellType.NUMERIC) {
				entity.setCreatedDate(dateCell.getDateCellValue());
			}
			
			Cell dateCell1 = row.getCell(5);
			if (dateCell1 != null && dateCell1.getCellType() == CellType.NUMERIC) {
				entity.setEntryDate(dateCell1.getDateCellValue());
			}
			
			/*entity.setCreatedDate(getCellValue(row.getCell(4)));
			entity.setEntryDate(getCellValue(row.getCell(6)));*/
			
			entity.setActivityName(getCellValue(row.getCell(6)));
			entity.setStatus(getCellValue(row.getCell(7)));
			
			//String status = row.getCell(7).getStringCellValue();
			//String status = getStringCellValue(row,7);
			//Cell statusCell = row.getCell(7);
			
			/*if (status!=null) {
				status=status.trim().toUpperCase();
			}
			entity.setStatus(status);*/
			entity.setTray(getCellValue(row.getCell(8)));
			entity.setBrokerName(getCellValue(row.getCell(9)));
			
			Cell dateCell2 = row.getCell(10);
			if (dateCell2 != null && dateCell2.getCellType() == CellType.NUMERIC) {
				entity.setDisbursalDate(dateCell2.getDateCellValue());
			}
			else{
				entity.setDisbursalDate(null);
			}
			//entity.setDisbursalDate(getCellValue(row.getCell(11)));
			String mobile = getSafeCellValue(row,11);
			
			//List<String> invalidRows = new ArrayList<>();
				
				if (mobile != null) {
					 
				    mobile = mobile.replaceAll("[^0-9]", "");
				    
				    if (mobile.length() < 10) {
				        mobile = String.format("%010d", Long.parseLong(mobile));
				    }
				 
				   /* if (!mobile.matches("^[0-9]{10}$")) {
				        invalidRows.add("Invalid mobile at row: " + row.getRowNum());
				        continue; // skip this row
				    }*/
				}
			
			/*if (mobile != null && !mobile.matches("^[0-9]{10}$")) {
			    throw new RuntimeException("Invalid mobile at row: " + row.getRowNum());
			}*/
			 
			entity.setMobile(mobile);
			entity.setEmployerId(getCellValue(row.getCell(12)));
			entity.setEmployerName(getCellValue(row.getCell(13)));
			entity.setCaseStatus(getCellValue(row.getCell(14)));
			entity.setRmName(getCellValue(row.getCell(15)));
			entity.setDmeCode(getCellValue(row.getCell(16)));
			entity.setDmeName(getCellValue(row.getCell(17)));
			entity.setHubName(getCellValue(row.getCell(18)));
			entity.setLoanAmnt(getCellValue(row.getCell(19)));
			entity.setProcessShop(getCellValue(row.getCell(20)));
			entity.setBranchName(getCellValue(row.getCell(21)));
			entity.setSchemeName(getCellValue(row.getCell(22)));
			entity.setPromotionCode(getCellValue(row.getCell(23)));
			entity.setChannelCode(getCellValue(row.getCell(24)));
			entity.setPslDescription(getCellValue(row.getCell(25)));
			entity.setPslType(getCellValue(row.getCell(26)));
			entity.setPrecfoc(getCellValue(row.getCell(27)));
			entity.setLarComments(getCellValue(row.getCell(28)));
			
			/*if(entity.getRcasID() == null){
				return "Process shop rcas id is null";
			}
			
			if(entity.getCustomerName() == null){
				return "Process shop customer name is null";
			}
			
			if(entity.getCreatedDate() == null){
				return "Process shop created date is null";
			}
			
			if(entity.getEntryDate() == null){
				return "Process shop entry date is null";
			}
			
			if(entity.getActivityName() == null){
				return "Process shop activity name is null";
			}
			
			if(entity.getBrokerName() == null){
				return "Process shop broker name is null";
			}
			
			if(entity.getRmName() == null){
				return "Process shop RM name is null";
			}
			
			if(entity.getDmeCode() == null){
				return "Process shop DME code is null";
			}
			
			if(entity.getDmeName() == null){
				return "Process shop DME name is null";
			}
			
			if(entity.getHubName() == null){
				return "Process shop HUB name is null";
			}
			
			if(entity.getProcessShop() == null){
				return "Process shop name is null";
			}
			
			if(entity.getBranchName() == null){
				return "Process shop branch name is null";
			}*/
			
			list.add(entity);

			rowNumber++;
			

			/*if (list.size() == 1000) {
				processShop.saveAll(list);
				processShop.flush();
				list.clear();
			}*/
		}

		// Save remaining records
		/*if (!list.isEmpty()) {
			processShop.saveAll(list);
		}*/
		
		processShop.saveAll(list);
		
		logger.info("Rcas process shop is uploaded successfully");
		return "Rcas process shop is uploaded successfully";		
	}

	private boolean validateHeaders(Row headerRow) {

		if (headerRow == null)
			return false;

		Set<String> actualHeaders = new HashSet<>();

		for (int i = 0; i < headerRow.getLastCellNum(); i++) {
			Cell cell = headerRow.getCell(i);
			if (cell == null)
				continue;

			cell.setCellType(CellType.STRING);

			String header = cell.getStringCellValue().trim()
					.replaceAll("_", "").replaceAll(" ", "").toUpperCase();

			actualHeaders.add(header);
		}

		for (String expected : EXPECTED_HEADERS) {

			String formattedExpected = expected.replaceAll("_", "")
					.replaceAll(" ", "").toUpperCase();

			if (!actualHeaders.contains(formattedExpected)) {
				return false;
			}
		}

		return true;
	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return null;

		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			return String.valueOf((long) cell.getNumericCellValue());
		default:
			return null;
		}
	}

	private String getStringCellValue(Row row, int index) {

		if (row == null)
			return null;

		Cell cell = row.getCell(index);

		if (cell == null)
			return null;

		if (cell.getCellType() == CellType.STRING) {
			return cell.getStringCellValue().trim();
		}

		if (cell.getCellType() == CellType.NUMERIC) {
			return String.valueOf((long) cell.getNumericCellValue());
		}

		return null;
	}

	private String getSafeCellValue(Row row, int index) {
		if (row == null)
			return null;

		DataFormatter formatter = new DataFormatter();
		Cell cell = row.getCell(index);

		if (cell == null)
			return null;

		String value = formatter.formatCellValue(cell);

		if (value != null) {
			value = value.trim();
			
			logger.info("Rcas value::"+value);
		}

		return value.isEmpty() ? null : value;
	}

	private static final List<String> EXPECTED_HEADERS = Arrays.asList(
			"RCAS No", "LAN NO", "DISBURSEMENT DATE", "CIBIL_SCORE",
			"APPLICANT_TYPE", "CUSTOMER_NAME", "CUSTOMER_TYPE");
}

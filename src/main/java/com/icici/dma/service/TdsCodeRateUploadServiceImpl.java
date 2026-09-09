package com.icici.dma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.TdsCodeRateTemp;
import com.icici.dma.model.VendorMasterTemp;
import com.icici.dma.repository.TdsCodeRateTempRepository;
import com.icici.dma.repository.VendorMasterTempRepository;
import com.monitorjbl.xlsx.StreamingReader;

import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.PreparedStatement;

@Service
@Transactional
public class TdsCodeRateUploadServiceImpl implements TdsCodeRateUploadService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private TdsCodeRateTempRepository tdsCodeRateTempRepository;

	@Override
	public Map<String, Object> upload(MultipartFile file, String user) throws Exception {

		System.out.println("==================VendorMasterUploadServiceImpl=================");
		System.out.println("========== SERVICE START ==========");
		System.out.println("File Received : " + file.getOriginalFilename());

		Map<String, Object> response = new HashMap<>();

		int totalRecords = 0;

		List<TdsCodeRateTemp> batch = new ArrayList<>();

		System.out.println("STEP 1 : about to cretae Workbook");

		/*
		 * XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		 */

		Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file.getInputStream());

		System.out.println("STEP 2 : Workbook Created");

		Sheet sheet = workbook.getSheetAt(0);
		System.out.println("toatal rows : " + sheet.getLastRowNum());

		System.out.println("STEP 3 : Sheet Loaded");

		DataFormatter formatter = new DataFormatter();

		/*
		 * for (int i = 1; i <= sheet.getLastRowNum(); i++) {
		 * 
		 * if(i % 100 == 0){ System.out.println("Processing Row : " + i); }
		 * 
		 * Row row = sheet.getRow(i);
		 * 
		 * if (row == null) { continue; }
		 */

		int rowNum = 0;

		for (Row row : sheet) {

			if (rowNum++ == 0) {
				continue; // skip header row
			}

			if (rowNum % 1000 == 0) {
				System.out.println("Processing Row : " + rowNum);
			}

			TdsCodeRateTemp temp = new TdsCodeRateTemp();


			temp.setWtaxType(formatter.formatCellValue(row.getCell(0)));

			temp.setWtx(formatter.formatCellValue(row.getCell(1)));

			temp.setTdsRate(formatter.formatCellValue(row.getCell(2)));

			temp.setStatus(StatusConstant.PENDING);

			temp.setCreatedBy(user);

			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);

			temp.setActionUser(user);

			temp.setActionDate(new Date());

			temp.setUploadId("Td_" + System.currentTimeMillis());

			temp.setFileName(file.getOriginalFilename());

			if (rowNum % 10000 == 0) {
				System.out.println("Adding to batch : " + rowNum);
			}

			// vendorMasterTempRepository.save(temp);
			batch.add(temp);

			totalRecords++;

			if (batch.size() == 5000) {

				long start = System.currentTimeMillis();

				System.out.println("Before saveAll 5000");

				// vendorMasterTempRepository.saveAll(batch);
				saveBatch(batch);

				System.out.println("After saveAll 5000");

				System.out.println("5000 records saved in " + (System.currentTimeMillis() - start) + " ms");

				batch.clear();

				System.out.println("Saved Records : " + totalRecords);
			}

		}

		if (!batch.isEmpty()) {

			long start = System.currentTimeMillis();

			System.out.println("Before saveAll Remaining");

			saveBatch(batch);

			// vendorMasterTempRepository.saveAll(batch);

			System.out.println("After saveAll Remaining");

			System.out.println("Remaining records saved in " + (System.currentTimeMillis() - start) + " ms");

			batch.clear();
		}

		workbook.close();

		System.out.println("Upload Completed");
		System.out.println("Total Records : " + totalRecords);

		response.put("message", "Vendor Master Upload Successful");

		response.put("totalRecords", totalRecords);

		response.put("successfulRecords", totalRecords);

		response.put("failedRecords", 0);

		return response;

	}

	private void saveBatch(List<TdsCodeRateTemp> batch) {
		
		  
		String sql = "INSERT INTO TDS_CODE_RATE_TEMP "
		        + "(ID,WTAX_TYPE,WTX,TDS_RATE,STATUS,CREATED_BY,CREATED_DATE,"
		        + "MODIFIED_BY,MODIFIED_DATE,ACTION_TYPE,ACTION_USER,ACTION_DATE,"
		        + "REMARKS,UPLOAD_ID,FILE_NAME) "
		        + "VALUES "
		        + "(SEQ_TDS_CODE_RATE_ID.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 

		jdbcTemplate.batchUpdate(sql, batch, 1000, (PreparedStatement ps, TdsCodeRateTemp temp) -> {

			
			  
			
			ps.setString(1, temp.getWtaxType());
			ps.setString(2, temp.getWtx());
			ps.setString(3, temp.getTdsRate());
			ps.setString(4, temp.getStatus());
			ps.setString(5, temp.getCreatedBy());

			ps.setTimestamp(6, new java.sql.Timestamp(temp.getCreatedDate().getTime()));

			ps.setString(7, temp.getModifiedBy());

			ps.setTimestamp(8,
					temp.getModifiedDate() == null ? null : new java.sql.Timestamp(temp.getModifiedDate().getTime()));

			ps.setString(9, temp.getActionType());

			ps.setString(10, temp.getActionUser());

			ps.setTimestamp(11,
					temp.getActionDate() == null ? null : new java.sql.Timestamp(temp.getActionDate().getTime()));

			ps.setString(12, temp.getRemarks());

			ps.setString(13, temp.getUploadId());

			ps.setString(14, temp.getFileName());

		});
	}

	@Override
	public byte[] exportErrorExcel(String uploadId) throws Exception {

		return new byte[0];
	}
}

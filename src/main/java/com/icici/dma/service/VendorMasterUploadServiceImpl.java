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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.VendorMasterTemp;
import com.icici.dma.repository.VendorMasterTempRepository;
import com.monitorjbl.xlsx.StreamingReader;

import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.PreparedStatement;

@Service
@Transactional
public class VendorMasterUploadServiceImpl implements VendorMasterUploadService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private VendorMasterTempRepository vendorMasterTempRepository;

	@Override
	public Map<String, Object> upload(MultipartFile file, String user) throws Exception {

		System.out.println("==================VendorMasterUploadServiceImpl=================");
		System.out.println("========== SERVICE START ==========");
		System.out.println("File Received : " + file.getOriginalFilename());

		Map<String, Object> response = new HashMap<>();

		int totalRecords = 0;

		List<VendorMasterTemp> batch = new ArrayList<>();

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

			VendorMasterTemp temp = new VendorMasterTemp();

			temp.setVendorNo(formatter.formatCellValue(row.getCell(0)));

			temp.setName1(formatter.formatCellValue(row.getCell(1)));

			temp.setName2(formatter.formatCellValue(row.getCell(2)));

			temp.setSearchItm(formatter.formatCellValue(row.getCell(3)));

			temp.setStreetHouse(formatter.formatCellValue(row.getCell(4)));

			temp.setStreet4(formatter.formatCellValue(row.getCell(5)));
			temp.setStreet5(formatter.formatCellValue(row.getCell(6)));
			temp.setPostCode(formatter.formatCellValue(row.getCell(7)));
			temp.setCity(formatter.formatCellValue(row.getCell(8)));
			temp.setCountry(formatter.formatCellValue(row.getCell(9)));
			temp.setRegion(formatter.formatCellValue(row.getCell(10)));
			temp.setStateName(formatter.formatCellValue(row.getCell(11)));
			temp.setTelNo(formatter.formatCellValue(row.getCell(12)));
			temp.setMobileNo(formatter.formatCellValue(row.getCell(13)));
			temp.setFax(formatter.formatCellValue(row.getCell(14)));
			temp.setCtr(formatter.formatCellValue(row.getCell(15)));
			temp.setBankKey(formatter.formatCellValue(row.getCell(16)));
			temp.setBankAccount(formatter.formatCellValue(row.getCell(17)));
			temp.setAccountHolder(formatter.formatCellValue(row.getCell(18)));
			temp.setControlKey(formatter.formatCellValue(row.getCell(19)));
			temp.setBankType(formatter.formatCellValue(row.getCell(20)));
			temp.setReferenceDetails(formatter.formatCellValue(row.getCell(21)));
			temp.setRecAc(formatter.formatCellValue(row.getCell(22)));
			temp.setPaymMethd(formatter.formatCellValue(row.getCell(23)));
			temp.setAlterPay(formatter.formatCellValue(row.getCell(24)));
			temp.setPb(formatter.formatCellValue(row.getCell(25)));
			temp.sethBank(formatter.formatCellValue(row.getCell(26)));
			temp.setExtraTextPanNumber(formatter.formatCellValue(row.getCell(27)));
			temp.setCinPanNumber(formatter.formatCellValue(row.getCell(28)));
			temp.setExciseRegNumber(formatter.formatCellValue(row.getCell(29)));
			temp.setCentralSalesTaxNumber(formatter.formatCellValue(row.getCell(30)));
			temp.setLocalSalesTaxNumber(formatter.formatCellValue(row.getCell(31)));
			temp.setServiceTaxRegisNumber(formatter.formatCellValue(row.getCell(32)));
			temp.setServiceTaxNo(formatter.formatCellValue(row.getCell(33)));
			temp.setSalesTaxNo(formatter.formatCellValue(row.getCell(34)));
			temp.setName3(formatter.formatCellValue(row.getCell(35)));
			temp.setName4(formatter.formatCellValue(row.getCell(36)));
			temp.setBankName(formatter.formatCellValue(row.getCell(37)));
			temp.setBankBranch(formatter.formatCellValue(row.getCell(38)));
			temp.setBranchAddress(formatter.formatCellValue(row.getCell(39)));
			temp.setTaxCode(formatter.formatCellValue(row.getCell(40)));
			temp.setWctCode(formatter.formatCellValue(row.getCell(41)));
			temp.setEmailAddress(formatter.formatCellValue(row.getCell(42)));

			temp.setOutSourcingActivity(formatter.formatCellValue(row.getCell(44)));
			temp.setVptsId(formatter.formatCellValue(row.getCell(45)));
			temp.setActivityNo(formatter.formatCellValue(row.getCell(46)));
			temp.setGstVendorClassification(formatter.formatCellValue(row.getCell(47)));
			temp.setGstVendorClassificationDesc(formatter.formatCellValue(row.getCell(48)));
			temp.setTaxNumber3(formatter.formatCellValue(row.getCell(49)));
			temp.setBlackListingReason(formatter.formatCellValue(row.getCell(50)));
			temp.setSearchTerm2(formatter.formatCellValue(row.getCell(51)));
			temp.setWithholdingTaxType1(formatter.formatCellValue(row.getCell(52)));
			temp.setWithholdingTaxCode1(formatter.formatCellValue(row.getCell(53)));
			temp.setWithholdingTaxType2(formatter.formatCellValue(row.getCell(54)));
			temp.setWithholdingTaxCode2(formatter.formatCellValue(row.getCell(55)));
			temp.setWithholdingTaxType3(formatter.formatCellValue(row.getCell(56)));
			temp.setWithholdingTaxCode3(formatter.formatCellValue(row.getCell(57)));
			temp.setWithholdingTaxType4(formatter.formatCellValue(row.getCell(58)));
			temp.setWithholdingTaxCode4(formatter.formatCellValue(row.getCell(59)));
			temp.setMsmedStatus(formatter.formatCellValue(row.getCell(60)));
			temp.setVendorTagging(formatter.formatCellValue(row.getCell(61)));
			temp.setComments(formatter.formatCellValue(row.getCell(62)));
			temp.setVendorBlock(formatter.formatCellValue(row.getCell(63)));
			temp.setModifiedTime(formatter.formatCellValue(row.getCell(65)));
			temp.setCredInfoNo(formatter.formatCellValue(row.getCell(66)));
			temp.setSpecificPerson206ab206cca(formatter.formatCellValue(row.getCell(67)));
			temp.setAdhaarPanLinked(formatter.formatCellValue(row.getCell(68)));
			temp.setVendorReturnFiling(formatter.formatCellValue(row.getCell(69)));
			temp.setPoBoxNumber(formatter.formatCellValue(row.getCell(70)));
			temp.setTaxNumber1(formatter.formatCellValue(row.getCell(71)));
			temp.setDepartment(formatter.formatCellValue(row.getCell(72)));
			temp.setUdyam(formatter.formatCellValue(row.getCell(73)));
			temp.setExemptionNumber(formatter.formatCellValue(row.getCell(74)));
			temp.setTaxNumber12(formatter.formatCellValue(row.getCell(75)));
			temp.setDepartment2(formatter.formatCellValue(row.getCell(76)));
			temp.setCreateDateStr(formatter.formatCellValue(row.getCell(77)));
			temp.setLastExtReview(formatter.formatCellValue(row.getCell(79)));
			temp.setExemptionFrom(formatter.formatCellValue(row.getCell(80)));
			temp.setExemptionTo(formatter.formatCellValue(row.getCell(81)));
			temp.setExemptionPercentage(formatter.formatCellValue(row.getCell(82)));
			temp.setThresholdAmountExemption(formatter.formatCellValue(row.getCell(83)));
			temp.setLowerTdsRate(formatter.formatCellValue(row.getCell(84)));
			temp.setStandardRateRelatedParty(formatter.formatCellValue(row.getCell(85)));

			temp.setStatus(StatusConstant.PENDING);

			temp.setCreatedBy(user);

			temp.setCreatedDate(new Date());

			temp.setActionType(ActionConstant.INSERT);

			temp.setActionUser(user);

			temp.setActionDate(new Date());

			temp.setUploadId("VEN_" + System.currentTimeMillis());

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

	private void saveBatch(List<VendorMasterTemp> batch) {

		/*
		 * String sql = "INSERT INTO VENDOR_MASTER_TEMP " +
		 * "(VENDOR_NO, NAME1, NAME2, SEARCH_ITM, STREET_HOUSE, STATUS, CREATED_BY, CREATED_DATE, FILE_NAME,"
		 * + "STREET4,STREET5,POST_CODE,CITY,COUNTRY,REGION,STATE_NAME," +
		 * "TEL_NO,MOBILE_NO,FAX,CTR,BANK_KEY,BANK_ACCOUNT,ACCOUNT_HOLDER," +
		 * "CONTROL_KEY,BANK_TYPE,REFERENCE_DETAILS,REC_AC,PAYM_METHD,ALTER_PAY," +
		 * "PB,HBANK,EXTRA_TEXT_PAN_NUMBER,CIN_PAN_NUMBER,EXCISE_REG_NUMBER," +
		 * 
		 * ")" +
		 * "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		 */

		String sql = "INSERT INTO VENDOR_MASTER_TEMP ("
				+ "VENDOR_NO,NAME1,NAME2,SEARCH_ITM,STREET_HOUSE,STREET4,STREET5,POST_CODE,CITY,COUNTRY,"
				+ "REGION,STATE_NAME,TEL_NO,MOBILE_NO,FAX,CTR,BANK_KEY,BANK_ACCOUNT,ACCOUNT_HOLDER,CONTROL_KEY,"
				+ "BANK_TYPE,REFERENCE_DETAILS,REC_AC,PAYM_METHD,ALTER_PAY,PB,HBANK,EXTRA_TEXT_PAN_NUMBER,CIN_PAN_NUMBER,EXCISE_REG_NUMBER,"
				+ "CENTRAL_SALES_TAX_NUMBER,LOCAL_SALES_TAX_NUMBER,SERVICE_TAX_REGIS_NUMBER,SERVICE_TAX_NO,SALES_TAX_NO,NAME3,NAME4,BANK_NAME,BANK_BRANCH,BRANCH_ADDRESS,"
				+ "TAX_CODE,WCT_CODE,EMAIL_ADDRESS,OUT_SOURCING_ACTIVITY,VPTS_ID,ACTIVITY_NO,GST_VENDOR_CLASSIFICATION,GST_VENDOR_CLASSIFICATION_DESC,TAX_NUMBER_3,BLACK_LISTING_REASON,"
				+ "SEARCH_TERM_2,WITHHOLDING_TAX_TYPE_1,WITHHOLDING_TAX_CODE_1,WITHHOLDING_TAX_TYPE_2,WITHHOLDING_TAX_CODE_2,WITHHOLDING_TAX_TYPE_3,WITHHOLDING_TAX_CODE_3,WITHHOLDING_TAX_TYPE_4,WITHHOLDING_TAX_CODE_4,MSMED_STATUS,"
				+ "VENDOR_TAGGING,COMMENTS,VENDOR_BLOCK,MODIFIED_TIME,CRED_INFO_NO,SPECIFIC_PERSON_206AB_206CCA,ADHAAR_PAN_LINKED,VENDOR_RETURN_FILING,PO_BOX_NUMBER,TAX_NUMBER_1,"
				+ "DEPARTMENT,UDYAM,EXEMPTION_NUMBER,TAX_NUMBER_1_2,DEPARTMENT_2,CREATE_DATE_STR,LAST_EXT_REVIEW,EXEMPTION_FROM,EXEMPTION_TO,EXEMPTION_PERCENTAGE,"
				+ "THRESHOLD_AMOUNT_EXEMPTION,LOWER_TDS_RATE,STANDARD_RATE_RELATED_PARTY,STATUS,CREATED_BY,CREATED_DATE,MODIFIED_BY,MODIFIED_DATE,ACTION_TYPE,ACTION_USER,"
				+ "ACTION_DATE,REMARKS,UPLOAD_ID,FILE_NAME" + ") VALUES ("
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
				+ ")";

		jdbcTemplate.batchUpdate(sql, batch, 1000, (PreparedStatement ps, VendorMasterTemp temp) -> {

			/*
			 * ps.setString(1, temp.getVendorNo()); ps.setString(2, temp.getName1());
			 * ps.setString(3, temp.getName2()); ps.setString(4, temp.getSearchItm());
			 * ps.setString(5, temp.getStreetHouse()); ps.setString(6, temp.getStatus());
			 * ps.setString(7, temp.getCreatedBy());
			 * 
			 * ps.setTimestamp( 8, new java.sql.Timestamp(
			 * temp.getCreatedDate().getTime()));
			 * 
			 * ps.setString(9, temp.getFileName());
			 */

			ps.setString(1, temp.getVendorNo());
			ps.setString(2, temp.getName1());
			ps.setString(3, temp.getName2());
			ps.setString(4, temp.getSearchItm());
			ps.setString(5, temp.getStreetHouse());
			 
			ps.setString(6, temp.getStreet4());
			ps.setString(7, temp.getStreet5());
			ps.setString(8, temp.getPostCode());
			ps.setString(9, temp.getCity());
			ps.setString(10, temp.getCountry());
			ps.setString(11, temp.getRegion());
			ps.setString(12, temp.getStateName());
			ps.setString(13, temp.getTelNo());
			ps.setString(14, temp.getMobileNo());
			ps.setString(15, temp.getFax());
			ps.setString(16, temp.getCtr());
			ps.setString(17, temp.getBankKey());
			ps.setString(18, temp.getBankAccount());
			ps.setString(19, temp.getAccountHolder());
			ps.setString(20, temp.getControlKey());
			ps.setString(21, temp.getBankType());
			ps.setString(22, temp.getReferenceDetails());
			ps.setString(23, temp.getRecAc());
			ps.setString(24, temp.getPaymMethd());
			ps.setString(25, temp.getAlterPay());
			ps.setString(26, temp.getPb());
			ps.setString(27, temp.gethBank());
			ps.setString(28, temp.getExtraTextPanNumber());
			ps.setString(29, temp.getCinPanNumber());
			ps.setString(30, temp.getExciseRegNumber());
			ps.setString(31, temp.getCentralSalesTaxNumber());
			ps.setString(32, temp.getLocalSalesTaxNumber());
			ps.setString(33, temp.getServiceTaxRegisNumber());
			ps.setString(34, temp.getServiceTaxNo());
			ps.setString(35, temp.getSalesTaxNo());
			 
			ps.setString(36, temp.getName3());
			ps.setString(37, temp.getName4());
			ps.setString(38, temp.getBankName());
			ps.setString(39, temp.getBankBranch());
			ps.setString(40, temp.getBranchAddress());
			ps.setString(41, temp.getTaxCode());
			ps.setString(42, temp.getWctCode());
			ps.setString(43, temp.getEmailAddress());
			ps.setString(44, temp.getOutSourcingActivity());
			ps.setString(45, temp.getVptsId());
			ps.setString(46, temp.getActivityNo());
			ps.setString(47, temp.getGstVendorClassification());
			ps.setString(48, temp.getGstVendorClassificationDesc());
			ps.setString(49, temp.getTaxNumber3());
			ps.setString(50, temp.getBlackListingReason());
			 
			ps.setString(51, temp.getSearchTerm2());
			ps.setString(52, temp.getWithholdingTaxType1());
			ps.setString(53, temp.getWithholdingTaxCode1());
			ps.setString(54, temp.getWithholdingTaxType2());
			ps.setString(55, temp.getWithholdingTaxCode2());
			ps.setString(56, temp.getWithholdingTaxType3());
			ps.setString(57, temp.getWithholdingTaxCode3());
			ps.setString(58, temp.getWithholdingTaxType4());
			ps.setString(59, temp.getWithholdingTaxCode4());
			ps.setString(60, temp.getMsmedStatus());
			ps.setString(61, temp.getVendorTagging());
			ps.setString(62, temp.getComments());
			ps.setString(63, temp.getVendorBlock());
			ps.setString(64, temp.getModifiedTime());
			ps.setString(65, temp.getCredInfoNo());
			 
			ps.setString(66, temp.getSpecificPerson206ab206cca());
			ps.setString(67, temp.getAdhaarPanLinked());
			ps.setString(68, temp.getVendorReturnFiling());
			ps.setString(69, temp.getPoBoxNumber());
			ps.setString(70, temp.getTaxNumber1());
			ps.setString(71, temp.getDepartment());
			ps.setString(72, temp.getUdyam());
			ps.setString(73, temp.getExemptionNumber());
			ps.setString(74, temp.getTaxNumber12());
			ps.setString(75, temp.getDepartment2());
			ps.setString(76, temp.getCreateDateStr());
			ps.setString(77, temp.getLastExtReview());
			ps.setString(78, temp.getExemptionFrom());
			ps.setString(79, temp.getExemptionTo());
			ps.setString(80, temp.getExemptionPercentage());
			 
			ps.setString(81, temp.getThresholdAmountExemption());
			ps.setString(82, temp.getLowerTdsRate());
			ps.setString(83, temp.getStandardRateRelatedParty());
			 
			ps.setString(84, temp.getStatus());
			ps.setString(85, temp.getCreatedBy());
			 
			ps.setTimestamp(86,
			    temp.getCreatedDate() == null ? null :
			    new java.sql.Timestamp(temp.getCreatedDate().getTime()));
			 
			ps.setString(87, temp.getModifiedBy());
			 
			ps.setTimestamp(88,
			    temp.getModifiedDate() == null ? null :
			    new java.sql.Timestamp(temp.getModifiedDate().getTime()));
			 
			ps.setString(89, temp.getActionType());
			 
			ps.setString(90, temp.getActionUser());
			 
			ps.setTimestamp(91,
			    temp.getActionDate() == null ? null :
			    new java.sql.Timestamp(temp.getActionDate().getTime()));
			 
			ps.setString(92, temp.getRemarks());
			 
			ps.setString(93, temp.getUploadId());
			 
			ps.setString(94, temp.getFileName());
			 
		});
	}

	@Override
	public byte[] exportErrorExcel(String uploadId) throws Exception {

		return new byte[0];
	}
}

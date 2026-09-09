package com.icici.dma.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_FINNONE_DUMP_ERROR")

public class FinnoneDumpError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finnone_error_seq")
	@SequenceGenerator(name = "finnone_error_seq", sequenceName = "my_sequence", allocationSize = 1)
	@Column(name = "ERROR_ID", length = 10)
	private Integer errorId;
	
	// === 01 - 10 === 10
	@Column(name = "AGREEMENT_ID")
	private String agreementId;
	@Column(name = "AGREEMENTNO")
	private String agreementNo;
	@Column(name = "AGREEMENTDATE")
	private String agreementDate;
	@Column(name = "DISB_DATE")
	private String disbDate;
	@Column(name = "DISBURSALAMOUNT")
	private String disbursalAmount;
	@Column(name = "DISB_AMT")
	private String disbAmt;
	@Column(name = "AMTFIN")
	private String amtFin;
	@Column(name = "PRETAXIRR")
	private String preTaxIrr;
	@Column(name = "LESSEEID")
	private String lesseeId;
	@Column(name = "TENURE")
	private String tenure;
	
	//=== 11-20 === 10
	@Column(name = "EMI")
	private String emi;
	@Column(name = "FILENO")
	private String fileNo;
	@Column(name = "BRANCHNM")
	private String branchNm;
	@Column(name = "MODELNO")
	private String modelNo;
	@Column(name = "MANUFACTURERDESC")
	private String manufacturerDesc;
	@Column(name = "NAME")
	private String name;
	@Column(name = "DEALERNAME")
	private String dealerName;
	@Column(name = "ADVANCEINSTL")
	private String advanceInstl;
	@Column(name = "PROCESSINGFEE")
	private String processingFee;
	@Column(name = "MFR_SUBVENTION_IN")
	private String mfrSubventionIn;
	
	//=== 21-30 === 10
	@Column(name = "DEALER_SUBVENTION")
	private String dealerSubvention;
	@Column(name = "MFR_SUBVENTION_PAID")
	private String mfrSubventionPaid;
	@Column(name = "DMA_SUBVENTION")
	private String dmaSubvention;
	@Column(name = "PROMOTIONDESC")
	private String promotionDesc;
	@Column(name = "MARGINMONEY")
	private String marginMoney;
	@Column(name = "ADVANCE_EMI")
	private String advanceEmi;
	@Column(name = "EMPLOYERNAME")
	private String employerName;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "MAKE")
	private String make;
	@Column(name = "V_ASSET_CATEG")
	private String vAssetCatg;
	
	//=== 31-40 === 10
	@Column(name = "PRODUCTFLAG")
	private String productFlag;
	@Column(name = "BRANCH_CODE")
	private String branchCode;
	@Column(name = "DMABROKERCODE")
	private String dmaBrokerCode;
	@Column(name = "SCHEMECODE")
	private String schemeCode;
	@Column(name = "PROMOTIONSCHEME")
	private String promotionScheme;
	@Column(name = "EFFRATE")
	private String effRate;
	@Column(name = "MODELCODE")
	private String modelCode;
	@Column(name = "SUBMODELCODE")
	private String subModelCode;
	@Column(name = "GROSS_LTV")
	private String grossLtv;
	@Column(name = "NET_LTV")
	private String netLtv;
	
	//=== 41-50 ==== 10
	@Column(name = "FINALSOURCE")
	private String finalSource;
	@Column(name = "FIRSTSOURCE")
	private String firstSource;
	@Column(name = "CUSTCATG")
	private String custCatg;
	@Column(name = "DMA_SUBVENTION_NOT_DED")
	private String dmaSubventionNotDed;
	@Column(name = "EMPTYPE")
	private String empType;
	@Column(name = "CFOC")
	private String cfoc;
	@Column(name = "STATE")
	private String state;
	@Column(name = "CHANNELCODE")
	private String channelCode;
	@Column(name = "MANUFACTURERID")
	private String manufacturerId;
//	@Column(name = "BRANCHCREDITID")
//	private String branchCreditId;
	@Column(name = "EMPLOYERID")
	private String employerId;
	
	// === 51-56 === 06
	@Column(name = "INFAVOUROF")
	private String inFavourOf;
	@Column(name = "CHEQUESTATUS")
	private String chequeStatus;
	@Column(name = "OSP_CODE")
	private String ospCode;
	@Column(name = "DME_NAME")
	private String dmeName;
	@Column(name = "DUMMY")
	private String dummy;
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	

	//=== 57-68 ====  12
	@Column(name = "CHARGE_ID1")
	private String chargeId1;
	@Column(name = "CHARGE_DESC1")
	private String chargeDesc1;
	@Column(name = "CHARGE_AMT1")
	private String chargeAmt1;
	@Column(name = "CHARGE_ID2")
	private String chargeId2;
	@Column(name = "CHARGE_DESC2")
	private String chargeDesc2;
	@Column(name = "CHARGE_AMT2")
	private String chargeAmt2;
	@Column(name = "CHARGE_ID4")
	private String chargeId4;
	@Column(name = "CHARGE_DESC4")
	private String chargeDesc4;
	@Column(name = "CHARGE_AMT4")
	private String chargeAmt4;
	@Column(name = "CHARGE_ID5")
	private String chargeId5;
	@Column(name = "CHARGE_DESC5")
	private String chargeDesc5;
	@Column(name = "CHARGE_AMT5")
	private String chargeAmt5;

	// ===== 69 – 78 ===== 10
	@Column(name = "CHARGE_ID6")
	private String chargeId6;
	@Column(name = "CHARGE_DESC6")
	private String chargeDesc6;
	@Column(name = "CHARGE_AMT6")
	private String chargeAmt6;
	@Column(name = "CHARGE_ID8")
	private String chargeId8;
	@Column(name = "CHARGE_DESC8")
	private String chargeDesc8;
	@Column(name = "CHARGE_AMT8")
	private String chargeAmt8;
	@Column(name = "CHARGE_ID9")
	private String chargeId9;
	@Column(name = "CHARGE_DESC9")
	private String chargeDesc9;
	@Column(name = "CHARGE_AMT9")
	private String chargeAmt9;
	@Column(name = "CHARGE_ID10")
	private String chargeId10;
	
	// ===== 79 – 83 ===== 5
	@Column(name = "CHARGE_DESC10")
	private String chargeDesc10;
	@Column(name = "CHARGE_AMT10")
	private String chargeAmt10;
	@Column(name = "CHARGE_ID11")
	private String chargeId11;
	@Column(name = "CHARGE_DESC11")
	private String chargeDesc11;
	@Column(name = "CHARGE_AMT11")
	private String chargeAmt11;
	
	// ===== 84 – 92 ===== 9
	@Column(name = "EMPLOYMENT_TYPE")
	private String employmentType;
	@Column(name = "pslFlag")
	private String pslFlag;
	@Column(name = "INSTRUMENT_TYPE")
	private String instrumentType;
	@Column(name = "BANK")
	private String bank;
	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	@Column(name = "CUSTOMER_AC")
	private String customerAc;
	@Column(name = "MICR")
	private String micr;
	@Column(name = "DEST_BANK_AC_TYPE")
	private String destBankAcType;
	@Column(name = "PROCESS_SHOP")
	private String processShop;

	// ===== 93 – 100 ===== 6 || 8
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
    private Date cycleFromDate;
	
	@Temporal(TemporalType.DATE)
    @Column(name = "TO_CYCLE_DATE")
    private Date cycleToDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	
	@Column(name = "ERROR_MSG")
	private String errorMsg;
	
	@Column(name = "ROW_NUMBER")
	private Integer rowNumber;
	
	@Column(name = "UPLOAD_ID")
	private String uploadId;
	
	@Column(name = "REMARKS")
	private String remarks;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public String getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(String agreementDate) {
		this.agreementDate = agreementDate;
	}

	public String getDisbDate() {
		return disbDate;
	}

	public void setDisbDate(String disbDate) {
		this.disbDate = disbDate;
	}

	public String getDisbursalAmount() {
		return disbursalAmount;
	}

	public void setDisbursalAmount(String disbursalAmount) {
		this.disbursalAmount = disbursalAmount;
	}

	public String getDisbAmt() {
		return disbAmt;
	}

	public void setDisbAmt(String disbAmt) {
		this.disbAmt = disbAmt;
	}

	public String getAmtFin() {
		return amtFin;
	}

	public void setAmtFin(String amtFin) {
		this.amtFin = amtFin;
	}

	public String getPreTaxIrr() {
		return preTaxIrr;
	}

	public void setPreTaxIrr(String preTaxIrr) {
		this.preTaxIrr = preTaxIrr;
	}

	public String getLesseeId() {
		return lesseeId;
	}

	public void setLesseeId(String lesseeId) {
		this.lesseeId = lesseeId;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getEmi() {
		return emi;
	}

	public void setEmi(String emi) {
		this.emi = emi;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getBranchNm() {
		return branchNm;
	}

	public void setBranchNm(String branchNm) {
		this.branchNm = branchNm;
	}

	public String getModelNo() {
		return modelNo;
	}

	public void setModelNo(String modelNo) {
		this.modelNo = modelNo;
	}

	public String getManufacturerDesc() {
		return manufacturerDesc;
	}

	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getAdvanceInstl() {
		return advanceInstl;
	}

	public void setAdvanceInstl(String advanceInstl) {
		this.advanceInstl = advanceInstl;
	}

	public String getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(String processingFee) {
		this.processingFee = processingFee;
	}

	public String getMfrSubventionIn() {
		return mfrSubventionIn;
	}

	public void setMfrSubventionIn(String mfrSubventionIn) {
		this.mfrSubventionIn = mfrSubventionIn;
	}

	public String getDealerSubvention() {
		return dealerSubvention;
	}

	public void setDealerSubvention(String dealerSubvention) {
		this.dealerSubvention = dealerSubvention;
	}

	public String getMfrSubventionPaid() {
		return mfrSubventionPaid;
	}

	public void setMfrSubventionPaid(String mfrSubventionPaid) {
		this.mfrSubventionPaid = mfrSubventionPaid;
	}

	public String getDmaSubvention() {
		return dmaSubvention;
	}

	public void setDmaSubvention(String dmaSubvention) {
		this.dmaSubvention = dmaSubvention;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	public String getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(String marginMoney) {
		this.marginMoney = marginMoney;
	}

	public String getAdvanceEmi() {
		return advanceEmi;
	}

	public void setAdvanceEmi(String advanceEmi) {
		this.advanceEmi = advanceEmi;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getvAssetCatg() {
		return vAssetCatg;
	}

	public void setvAssetCatg(String vAssetCatg) {
		this.vAssetCatg = vAssetCatg;
	}

	public String getProductFlag() {
		return productFlag;
	}

	public void setProductFlag(String productFlag) {
		this.productFlag = productFlag;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getDmaBrokerCode() {
		return dmaBrokerCode;
	}

	public void setDmaBrokerCode(String dmaBrokerCode) {
		this.dmaBrokerCode = dmaBrokerCode;
	}

	public String getSchemeCode() {
		return schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getPromotionScheme() {
		return promotionScheme;
	}

	public void setPromotionScheme(String promotionScheme) {
		this.promotionScheme = promotionScheme;
	}

	public String getEffRate() {
		return effRate;
	}

	public void setEffRate(String effRate) {
		this.effRate = effRate;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getSubModelCode() {
		return subModelCode;
	}

	public void setSubModelCode(String subModelCode) {
		this.subModelCode = subModelCode;
	}

	public String getGrossLtv() {
		return grossLtv;
	}

	public void setGrossLtv(String grossLtv) {
		this.grossLtv = grossLtv;
	}

	public String getNetLtv() {
		return netLtv;
	}

	public void setNetLtv(String netLtv) {
		this.netLtv = netLtv;
	}

	public String getFinalSource() {
		return finalSource;
	}

	public void setFinalSource(String finalSource) {
		this.finalSource = finalSource;
	}

	public String getFirstSource() {
		return firstSource;
	}

	public void setFirstSource(String firstSource) {
		this.firstSource = firstSource;
	}

	public String getCustCatg() {
		return custCatg;
	}

	public void setCustCatg(String custCatg) {
		this.custCatg = custCatg;
	}

	public String getDmaSubventionNotDed() {
		return dmaSubventionNotDed;
	}

	public void setDmaSubventionNotDed(String dmaSubventionNotDed) {
		this.dmaSubventionNotDed = dmaSubventionNotDed;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getCfoc() {
		return cfoc;
	}

	public void setCfoc(String cfoc) {
		this.cfoc = cfoc;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	public String getInFavourOf() {
		return inFavourOf;
	}

	public void setInFavourOf(String inFavourOf) {
		this.inFavourOf = inFavourOf;
	}

	public String getChequeStatus() {
		return chequeStatus;
	}

	public void setChequeStatus(String chequeStatus) {
		this.chequeStatus = chequeStatus;
	}

	public String getOspCode() {
		return ospCode;
	}

	public void setOspCode(String ospCode) {
		this.ospCode = ospCode;
	}

	public String getDmeName() {
		return dmeName;
	}

	public void setDmeName(String dmeName) {
		this.dmeName = dmeName;
	}

	public String getDummy() {
		return dummy;
	}

	public void setDummy(String dummy) {
		this.dummy = dummy;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getChargeId1() {
		return chargeId1;
	}

	public void setChargeId1(String chargeId1) {
		this.chargeId1 = chargeId1;
	}

	public String getChargeDesc1() {
		return chargeDesc1;
	}

	public void setChargeDesc1(String chargeDesc1) {
		this.chargeDesc1 = chargeDesc1;
	}

	public String getChargeAmt1() {
		return chargeAmt1;
	}

	public void setChargeAmt1(String chargeAmt1) {
		this.chargeAmt1 = chargeAmt1;
	}

	public String getChargeId2() {
		return chargeId2;
	}

	public void setChargeId2(String chargeId2) {
		this.chargeId2 = chargeId2;
	}

	public String getChargeDesc2() {
		return chargeDesc2;
	}

	public void setChargeDesc2(String chargeDesc2) {
		this.chargeDesc2 = chargeDesc2;
	}

	public String getChargeAmt2() {
		return chargeAmt2;
	}

	public void setChargeAmt2(String chargeAmt2) {
		this.chargeAmt2 = chargeAmt2;
	}

	public String getChargeId4() {
		return chargeId4;
	}

	public void setChargeId4(String chargeId4) {
		this.chargeId4 = chargeId4;
	}

	public String getChargeDesc4() {
		return chargeDesc4;
	}

	public void setChargeDesc4(String chargeDesc4) {
		this.chargeDesc4 = chargeDesc4;
	}

	public String getChargeAmt4() {
		return chargeAmt4;
	}

	public void setChargeAmt4(String chargeAmt4) {
		this.chargeAmt4 = chargeAmt4;
	}

	public String getChargeId5() {
		return chargeId5;
	}

	public void setChargeId5(String chargeId5) {
		this.chargeId5 = chargeId5;
	}

	public String getChargeDesc5() {
		return chargeDesc5;
	}

	public void setChargeDesc5(String chargeDesc5) {
		this.chargeDesc5 = chargeDesc5;
	}

	public String getChargeAmt5() {
		return chargeAmt5;
	}

	public void setChargeAmt5(String chargeAmt5) {
		this.chargeAmt5 = chargeAmt5;
	}

	public String getChargeId6() {
		return chargeId6;
	}

	public void setChargeId6(String chargeId6) {
		this.chargeId6 = chargeId6;
	}

	public String getChargeDesc6() {
		return chargeDesc6;
	}

	public void setChargeDesc6(String chargeDesc6) {
		this.chargeDesc6 = chargeDesc6;
	}

	public String getChargeAmt6() {
		return chargeAmt6;
	}

	public void setChargeAmt6(String chargeAmt6) {
		this.chargeAmt6 = chargeAmt6;
	}

	public String getChargeId8() {
		return chargeId8;
	}

	public void setChargeId8(String chargeId8) {
		this.chargeId8 = chargeId8;
	}

	public String getChargeDesc8() {
		return chargeDesc8;
	}

	public void setChargeDesc8(String chargeDesc8) {
		this.chargeDesc8 = chargeDesc8;
	}

	public String getChargeAmt8() {
		return chargeAmt8;
	}

	public void setChargeAmt8(String chargeAmt8) {
		this.chargeAmt8 = chargeAmt8;
	}

	public String getChargeId9() {
		return chargeId9;
	}

	public void setChargeId9(String chargeId9) {
		this.chargeId9 = chargeId9;
	}

	public String getChargeDesc9() {
		return chargeDesc9;
	}

	public void setChargeDesc9(String chargeDesc9) {
		this.chargeDesc9 = chargeDesc9;
	}

	public String getChargeAmt9() {
		return chargeAmt9;
	}

	public void setChargeAmt9(String chargeAmt9) {
		this.chargeAmt9 = chargeAmt9;
	}

	public String getChargeId10() {
		return chargeId10;
	}

	public void setChargeId10(String chargeId10) {
		this.chargeId10 = chargeId10;
	}

	public String getChargeDesc10() {
		return chargeDesc10;
	}

	public void setChargeDesc10(String chargeDesc10) {
		this.chargeDesc10 = chargeDesc10;
	}

	public String getChargeAmt10() {
		return chargeAmt10;
	}

	public void setChargeAmt10(String chargeAmt10) {
		this.chargeAmt10 = chargeAmt10;
	}

	public String getChargeId11() {
		return chargeId11;
	}

	public void setChargeId11(String chargeId11) {
		this.chargeId11 = chargeId11;
	}

	public String getChargeDesc11() {
		return chargeDesc11;
	}

	public void setChargeDesc11(String chargeDesc11) {
		this.chargeDesc11 = chargeDesc11;
	}

	public String getChargeAmt11() {
		return chargeAmt11;
	}

	public void setChargeAmt11(String chargeAmt11) {
		this.chargeAmt11 = chargeAmt11;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getPslFlag() {
		return pslFlag;
	}

	public void setPslFlag(String pslFlag) {
		this.pslFlag = pslFlag;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getCustomerAc() {
		return customerAc;
	}

	public void setCustomerAc(String customerAc) {
		this.customerAc = customerAc;
	}

	public String getMicr() {
		return micr;
	}

	public void setMicr(String micr) {
		this.micr = micr;
	}

	public String getDestBankAcType() {
		return destBankAcType;
	}

	public void setDestBankAcType(String destBankAcType) {
		this.destBankAcType = destBankAcType;
	}

	public String getProcessShop() {
		return processShop;
	}

	public void setProcessShop(String processShop) {
		this.processShop = processShop;
	}

	public Date getCycleFromDate() {
		return cycleFromDate;
	}

	public void setCycleFromDate(Date cycleFromDate) {
		this.cycleFromDate = cycleFromDate;
	}

	public Date getCycleToDate() {
		return cycleToDate;
	}

	public void setCycleToDate(Date cycleToDate) {
		this.cycleToDate = cycleToDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public FinnoneDumpError(Integer errorId, String agreementId, String agreementNo, String agreementDate,
			String disbDate, String disbursalAmount, String disbAmt, String amtFin, String preTaxIrr, String lesseeId,
			String tenure, String emi, String fileNo, String branchNm, String modelNo, String manufacturerDesc,
			String name, String dealerName, String advanceInstl, String processingFee, String mfrSubventionIn,
			String dealerSubvention, String mfrSubventionPaid, String dmaSubvention, String promotionDesc,
			String marginMoney, String advanceEmi, String employerName, String status, String make, String vAssetCatg,
			String productFlag, String branchCode, String dmaBrokerCode, String schemeCode, String promotionScheme,
			String effRate, String modelCode, String subModelCode, String grossLtv, String netLtv, String finalSource,
			String firstSource, String custCatg, String dmaSubventionNotDed, String empType, String cfoc, String state,
			String channelCode, String manufacturerId, String employerId, String inFavourOf, String chequeStatus,
			String ospCode, String dmeName, String dummy, String customerName, String chargeId1, String chargeDesc1,
			String chargeAmt1, String chargeId2, String chargeDesc2, String chargeAmt2, String chargeId4,
			String chargeDesc4, String chargeAmt4, String chargeId5, String chargeDesc5, String chargeAmt5,
			String chargeId6, String chargeDesc6, String chargeAmt6, String chargeId8, String chargeDesc8,
			String chargeAmt8, String chargeId9, String chargeDesc9, String chargeAmt9, String chargeId10,
			String chargeDesc10, String chargeAmt10, String chargeId11, String chargeDesc11, String chargeAmt11,
			String employmentType, String pslFlag, String instrumentType, String bank, String bankBranch,
			String customerAc, String micr, String destBankAcType, String processShop, Date cycleFromDate,
			Date cycleToDate, String createdBy, Date createdDate, String errorMsg, Integer rowNumber, String uploadId,
			String remarks) {
		super();
		this.errorId = errorId;
		this.agreementId = agreementId;
		this.agreementNo = agreementNo;
		this.agreementDate = agreementDate;
		this.disbDate = disbDate;
		this.disbursalAmount = disbursalAmount;
		this.disbAmt = disbAmt;
		this.amtFin = amtFin;
		this.preTaxIrr = preTaxIrr;
		this.lesseeId = lesseeId;
		this.tenure = tenure;
		this.emi = emi;
		this.fileNo = fileNo;
		this.branchNm = branchNm;
		this.modelNo = modelNo;
		this.manufacturerDesc = manufacturerDesc;
		this.name = name;
		this.dealerName = dealerName;
		this.advanceInstl = advanceInstl;
		this.processingFee = processingFee;
		this.mfrSubventionIn = mfrSubventionIn;
		this.dealerSubvention = dealerSubvention;
		this.mfrSubventionPaid = mfrSubventionPaid;
		this.dmaSubvention = dmaSubvention;
		this.promotionDesc = promotionDesc;
		this.marginMoney = marginMoney;
		this.advanceEmi = advanceEmi;
		this.employerName = employerName;
		this.status = status;
		this.make = make;
		this.vAssetCatg = vAssetCatg;
		this.productFlag = productFlag;
		this.branchCode = branchCode;
		this.dmaBrokerCode = dmaBrokerCode;
		this.schemeCode = schemeCode;
		this.promotionScheme = promotionScheme;
		this.effRate = effRate;
		this.modelCode = modelCode;
		this.subModelCode = subModelCode;
		this.grossLtv = grossLtv;
		this.netLtv = netLtv;
		this.finalSource = finalSource;
		this.firstSource = firstSource;
		this.custCatg = custCatg;
		this.dmaSubventionNotDed = dmaSubventionNotDed;
		this.empType = empType;
		this.cfoc = cfoc;
		this.state = state;
		this.channelCode = channelCode;
		this.manufacturerId = manufacturerId;
		this.employerId = employerId;
		this.inFavourOf = inFavourOf;
		this.chequeStatus = chequeStatus;
		this.ospCode = ospCode;
		this.dmeName = dmeName;
		this.dummy = dummy;
		this.customerName = customerName;
		this.chargeId1 = chargeId1;
		this.chargeDesc1 = chargeDesc1;
		this.chargeAmt1 = chargeAmt1;
		this.chargeId2 = chargeId2;
		this.chargeDesc2 = chargeDesc2;
		this.chargeAmt2 = chargeAmt2;
		this.chargeId4 = chargeId4;
		this.chargeDesc4 = chargeDesc4;
		this.chargeAmt4 = chargeAmt4;
		this.chargeId5 = chargeId5;
		this.chargeDesc5 = chargeDesc5;
		this.chargeAmt5 = chargeAmt5;
		this.chargeId6 = chargeId6;
		this.chargeDesc6 = chargeDesc6;
		this.chargeAmt6 = chargeAmt6;
		this.chargeId8 = chargeId8;
		this.chargeDesc8 = chargeDesc8;
		this.chargeAmt8 = chargeAmt8;
		this.chargeId9 = chargeId9;
		this.chargeDesc9 = chargeDesc9;
		this.chargeAmt9 = chargeAmt9;
		this.chargeId10 = chargeId10;
		this.chargeDesc10 = chargeDesc10;
		this.chargeAmt10 = chargeAmt10;
		this.chargeId11 = chargeId11;
		this.chargeDesc11 = chargeDesc11;
		this.chargeAmt11 = chargeAmt11;
		this.employmentType = employmentType;
		this.pslFlag = pslFlag;
		this.instrumentType = instrumentType;
		this.bank = bank;
		this.bankBranch = bankBranch;
		this.customerAc = customerAc;
		this.micr = micr;
		this.destBankAcType = destBankAcType;
		this.processShop = processShop;
		this.cycleFromDate = cycleFromDate;
		this.cycleToDate = cycleToDate;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.errorMsg = errorMsg;
		this.rowNumber = rowNumber;
		this.uploadId = uploadId;
		this.remarks = remarks;
	}

	public FinnoneDumpError() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FinnoneDumpError [errorId=" + errorId + ", agreementId=" + agreementId + ", agreementNo=" + agreementNo
				+ ", agreementDate=" + agreementDate + ", disbDate=" + disbDate + ", disbursalAmount=" + disbursalAmount
				+ ", disbAmt=" + disbAmt + ", amtFin=" + amtFin + ", preTaxIrr=" + preTaxIrr + ", lesseeId=" + lesseeId
				+ ", tenure=" + tenure + ", emi=" + emi + ", fileNo=" + fileNo + ", branchNm=" + branchNm + ", modelNo="
				+ modelNo + ", manufacturerDesc=" + manufacturerDesc + ", name=" + name + ", dealerName=" + dealerName
				+ ", advanceInstl=" + advanceInstl + ", processingFee=" + processingFee + ", mfrSubventionIn="
				+ mfrSubventionIn + ", dealerSubvention=" + dealerSubvention + ", mfrSubventionPaid="
				+ mfrSubventionPaid + ", dmaSubvention=" + dmaSubvention + ", promotionDesc=" + promotionDesc
				+ ", marginMoney=" + marginMoney + ", advanceEmi=" + advanceEmi + ", employerName=" + employerName
				+ ", status=" + status + ", make=" + make + ", vAssetCatg=" + vAssetCatg + ", productFlag="
				+ productFlag + ", branchCode=" + branchCode + ", dmaBrokerCode=" + dmaBrokerCode + ", schemeCode="
				+ schemeCode + ", promotionScheme=" + promotionScheme + ", effRate=" + effRate + ", modelCode="
				+ modelCode + ", subModelCode=" + subModelCode + ", grossLtv=" + grossLtv + ", netLtv=" + netLtv
				+ ", finalSource=" + finalSource + ", firstSource=" + firstSource + ", custCatg=" + custCatg
				+ ", dmaSubventionNotDed=" + dmaSubventionNotDed + ", empType=" + empType + ", cfoc=" + cfoc
				+ ", state=" + state + ", channelCode=" + channelCode + ", manufacturerId=" + manufacturerId
				+ ", employerId=" + employerId + ", inFavourOf=" + inFavourOf + ", chequeStatus=" + chequeStatus
				+ ", ospCode=" + ospCode + ", dmeName=" + dmeName + ", dummy=" + dummy + ", customerName="
				+ customerName + ", chargeId1=" + chargeId1 + ", chargeDesc1=" + chargeDesc1 + ", chargeAmt1="
				+ chargeAmt1 + ", chargeId2=" + chargeId2 + ", chargeDesc2=" + chargeDesc2 + ", chargeAmt2="
				+ chargeAmt2 + ", chargeId4=" + chargeId4 + ", chargeDesc4=" + chargeDesc4 + ", chargeAmt4="
				+ chargeAmt4 + ", chargeId5=" + chargeId5 + ", chargeDesc5=" + chargeDesc5 + ", chargeAmt5="
				+ chargeAmt5 + ", chargeId6=" + chargeId6 + ", chargeDesc6=" + chargeDesc6 + ", chargeAmt6="
				+ chargeAmt6 + ", chargeId8=" + chargeId8 + ", chargeDesc8=" + chargeDesc8 + ", chargeAmt8="
				+ chargeAmt8 + ", chargeId9=" + chargeId9 + ", chargeDesc9=" + chargeDesc9 + ", chargeAmt9="
				+ chargeAmt9 + ", chargeId10=" + chargeId10 + ", chargeDesc10=" + chargeDesc10 + ", chargeAmt10="
				+ chargeAmt10 + ", chargeId11=" + chargeId11 + ", chargeDesc11=" + chargeDesc11 + ", chargeAmt11="
				+ chargeAmt11 + ", employmentType=" + employmentType + ", pslFlag=" + pslFlag + ", instrumentType="
				+ instrumentType + ", bank=" + bank + ", bankBranch=" + bankBranch + ", customerAc=" + customerAc
				+ ", micr=" + micr + ", destBankAcType=" + destBankAcType + ", processShop=" + processShop
				+ ", cycleFromDate=" + cycleFromDate + ", cycleToDate=" + cycleToDate + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", errorMsg=" + errorMsg + ", rowNumber=" + rowNumber + ", uploadId="
				+ uploadId + ", remarks=" + remarks + "]";
	}

	
	
}
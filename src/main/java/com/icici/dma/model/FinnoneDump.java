package com.icici.dma.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_FINNONE_DUMP")

public class FinnoneDump {

	// ===== 01 – 10 ===== 10
	
	@Id
	@Column(name = "AGREEMENT_ID")
	private Long agreementId;
	@Column(name = "AGREEMENTNO")
	private String agreementNo;
	@Column(name = "AGREEMENTDATE")
	private Date agreementDate;
	@Column(name = "DISB_DATE")
	private Date disbDate;
	@Column(name = "DISBURSALAMOUNT")
	private BigDecimal disbursalAmount;
	@Column(name = "DISB_AMT")
	private BigDecimal disbAmt;
	@Column(name = "AMTFIN")
	private BigDecimal amtFin;
	@Column(name = "PRETAXIRR")
	private BigDecimal preTaxIrr;
	@Column(name = "LESSEEID")
	private BigDecimal lesseeId;
	@Column(name = "TENURE")
	private BigDecimal tenure;
	
	// ===== 11 – 20 ===== 10
	@Column(name = "EMI")
	private BigDecimal emi;
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
	private BigDecimal advanceInstl;
	@Column(name = "PROCESSINGFEE")
	private BigDecimal processingFee;
	
	@Column(name = "MFR_SUBVENTION_IN")
	private BigDecimal mfrSubventionIn;
	
	// ===== 21 – 30 ===== 10
	@Column(name = "DEALER_SUBVENTION")
	private BigDecimal dealerSubvention;
	@Column(name = "MFR_SUBVENTION_PAID")
	private BigDecimal mfrSubventionPaid;
	@Column(name = "DMA_SUBVENTION")
	private BigDecimal dmaSubvention;
	@Column(name = "PROMOTIONDESC")
	private String promotionDesc;
	@Column(name = "MARGINMONEY")
	private BigDecimal marginMoney;
	@Column(name = "ADVANCE_EMI")
	private BigDecimal advanceEmi;
	@Column(name = "EMPLOYERNAME")
	private String employerName;
	@Column(name = "STATUS")
	private String status;
	@Column(name = "MAKE")
	private String make;
	@Column(name = "V_ASSET_CATEG")
	private String vAssetCatg;
	
	
	// ===== 31 – 40 ===== 10
	@Column(name = "PRODUCTFLAG")
	private String productFlag;
	@Column(name = "BRANCH_CODE")
	private String branchCode;
	@Column(name = "DMABROKERCODE")
	private BigDecimal dmaBrokerCode;
	@Column(name = "SCHEMECODE")
	private String schemeCode;
	@Column(name = "PROMOTIONSCHEME")
	private String promotionScheme;
	@Column(name = "EFFRATE")
	private BigDecimal effRate;
	@Column(name = "MODELCODE")
	private BigDecimal modelCode;
	@Column(name = "SUBMODELCODE")
	private BigDecimal subModelCode;
	@Column(name = "GROSS_LTV")
	private BigDecimal grossLtv;
	@Column(name = "NET_LTV")
	private BigDecimal netLtv;
	
	
	// ===== 41 – 50 ===== 10
	@Column(name = "FINALSOURCE")
	private String finalSource;
	@Column(name = "FIRSTSOURCE")
	private String firstSource;
	@Column(name = "CUSTCATG")
	private String custCatg;
	@Column(name = "DMA_SUBVENTION_NOT_DED")
	private BigDecimal dmaSubventionNot;
	@Column(name = "EMPTYPE")
	private String empType;
	@Column(name = "CFOC")
	private BigDecimal cfoc;
	@Column(name = "STATE")
	private String state;
	@Column(name = "CHANNELCODE")
	private String channelCode;
//	@Column(name = "BRANCHCREDITID")
//	private String branchCreditId;
	@Column(name = "MANUFACTURERID")
	private BigDecimal manufacturerId;
	@Column(name = "EMPLOYERID")
	private BigDecimal employerId;
	
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
	

	// ===== 57 – 68 (CHARGES) ===== 12
	@Column(name = "CHARGE_ID1")
	private BigDecimal chargeId1;
	@Column(name = "CHARGE_DESC1")
	private String chargeDesc1;
	@Column(name = "CHARGE_AMT1")
	private BigDecimal chargeAmt1;
	@Column(name = "CHARGE_ID2")
	private BigDecimal chargeId2;
	@Column(name = "CHARGE_DESC2")
	private String chargeDesc2;
	@Column(name = "CHARGE_AMT2")
	private BigDecimal chargeAmt2;
	@Column(name = "CHARGE_ID4")
	private BigDecimal chargeId4;
	@Column(name = "CHARGE_DESC4")
	private String chargeDesc4;
	@Column(name = "CHARGE_AMT4")
	private BigDecimal chargeAmt4;
	@Column(name = "CHARGE_ID5")
	private BigDecimal chargeId5;
	@Column(name = "CHARGE_DESC5")
	private String chargeDesc5;
	@Column(name = "CHARGE_AMT5")
	private BigDecimal chargeAmt5;

	// ===== 69 – 78 =====10
	@Column(name = "CHARGE_ID6")
	private BigDecimal chargeId6;
	@Column(name = "CHARGE_DESC6")
	private String chargeDesc6;
	@Column(name = "CHARGE_AMT6")
	private BigDecimal chargeAmt6;
	@Column(name = "CHARGE_ID8")
	private BigDecimal chargeId8;
	@Column(name = "CHARGE_DESC8")
	private String chargeDesc8;
	@Column(name = "CHARGE_AMT8")
	private BigDecimal chargeAmt8;
	@Column(name = "CHARGE_ID9")
	private BigDecimal chargeId9;
	@Column(name = "CHARGE_DESC9")
	private String chargeDesc9;
	@Column(name = "CHARGE_AMT9")
	private BigDecimal chargeAmt9;
	@Column(name = "CHARGE_ID10")
	private BigDecimal chargeId10;
	
	// ===== 79 – 83 ===== 5
	@Column(name = "CHARGE_DESC10")
	private String chargeDesc10;
	@Column(name = "CHARGE_AMT10")
	private BigDecimal chargeAmt10;
	@Column(name = "CHARGE_ID11")
	private BigDecimal chargeId11;
	@Column(name = "CHARGE_DESC11")
	private String chargeDesc11;
	@Column(name = "CHARGE_AMT11")
	private BigDecimal chargeAmt11;

	
	// ===== 84 – 92 ===== 9
	@Column(name = "EMPLOYMENT_TYPE")
	private String employmentType;
	@Column(name = "PSL_FLAG")
	private String pslFlag;
	@Column(name = "INSTRUMENT_TYPE")
	private String instrumentType;
	@Column(name = "BANK")
	private String bank;
	@Column(name = "BANK_BRANCH")
	private String bankBranch;
	@Column(name = "CUSTOMER_AC")
	private BigDecimal customerAc;
	@Column(name = "MICR")
	private BigDecimal micr;
	@Column(name = "DEST_BANK_AC_TYPE")
	private String destBankAcType;
	@Column(name = "PROCESS_SHOP")
	private String processShop;
//	@Column(name = "MANUFACTURERID")
//	private BigDecimal manufacturerId;

	
	// ===== 93 – 97 ===== 5
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
    private Date cycleFromDate;
    
	@Temporal(TemporalType.DATE)
    @Column(name = "TO_CYCLE_DATE")
    private Date cycleToDate;

	@Column(name="UPLOAD_ID")
	private String uploadId;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public Long getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(Long agreementId) {
		this.agreementId = agreementId;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public Date getAgreementDate() {
		return agreementDate;
	}

	public void setAgreementDate(Date agreementDate) {
		this.agreementDate = agreementDate;
	}

	public Date getDisbDate() {
		return disbDate;
	}

	public void setDisbDate(Date disbDate) {
		this.disbDate = disbDate;
	}

	public BigDecimal getDisbursalAmount() {
		return disbursalAmount;
	}

	public void setDisbursalAmount(BigDecimal disbursalAmount) {
		this.disbursalAmount = disbursalAmount;
	}

	public BigDecimal getDisbAmt() {
		return disbAmt;
	}

	public void setDisbAmt(BigDecimal disbAmt) {
		this.disbAmt = disbAmt;
	}

	public BigDecimal getAmtFin() {
		return amtFin;
	}

	public void setAmtFin(BigDecimal amtFin) {
		this.amtFin = amtFin;
	}

	public BigDecimal getPreTaxIrr() {
		return preTaxIrr;
	}

	public void setPreTaxIrr(BigDecimal preTaxIrr) {
		this.preTaxIrr = preTaxIrr;
	}

	public BigDecimal getLesseeId() {
		return lesseeId;
	}

	public void setLesseeId(BigDecimal lesseeId) {
		this.lesseeId = lesseeId;
	}

	public BigDecimal getTenure() {
		return tenure;
	}

	public void setTenure(BigDecimal tenure) {
		this.tenure = tenure;
	}

	public BigDecimal getEmi() {
		return emi;
	}

	public void setEmi(BigDecimal emi) {
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

	public BigDecimal getAdvanceInstl() {
		return advanceInstl;
	}

	public void setAdvanceInstl(BigDecimal advanceInstl) {
		this.advanceInstl = advanceInstl;
	}

	public BigDecimal getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(BigDecimal processingFee) {
		this.processingFee = processingFee;
	}

	public BigDecimal getMfrSubventionIn() {
		return mfrSubventionIn;
	}

	public void setMfrSubventionIn(BigDecimal mfrSubventionIn) {
		this.mfrSubventionIn = mfrSubventionIn;
	}

	public BigDecimal getDealerSubvention() {
		return dealerSubvention;
	}

	public void setDealerSubvention(BigDecimal dealerSubvention) {
		this.dealerSubvention = dealerSubvention;
	}

	public BigDecimal getMfrSubventionPaid() {
		return mfrSubventionPaid;
	}

	public void setMfrSubventionPaid(BigDecimal mfrSubventionPaid) {
		this.mfrSubventionPaid = mfrSubventionPaid;
	}

	public BigDecimal getDmaSubvention() {
		return dmaSubvention;
	}

	public void setDmaSubvention(BigDecimal dmaSubvention) {
		this.dmaSubvention = dmaSubvention;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	public BigDecimal getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(BigDecimal marginMoney) {
		this.marginMoney = marginMoney;
	}

	public BigDecimal getAdvanceEmi() {
		return advanceEmi;
	}

	public void setAdvanceEmi(BigDecimal advanceEmi) {
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

	public BigDecimal getDmaBrokerCode() {
		return dmaBrokerCode;
	}

	public void setDmaBrokerCode(BigDecimal dmaBrokerCode) {
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

	public BigDecimal getEffRate() {
		return effRate;
	}

	public void setEffRate(BigDecimal effRate) {
		this.effRate = effRate;
	}

	public BigDecimal getModelCode() {
		return modelCode;
	}

	public void setModelCode(BigDecimal modelCode) {
		this.modelCode = modelCode;
	}

	public BigDecimal getSubModelCode() {
		return subModelCode;
	}

	public void setSubModelCode(BigDecimal subModelCode) {
		this.subModelCode = subModelCode;
	}

	public BigDecimal getGrossLtv() {
		return grossLtv;
	}

	public void setGrossLtv(BigDecimal grossLtv) {
		this.grossLtv = grossLtv;
	}

	public BigDecimal getNetLtv() {
		return netLtv;
	}

	public void setNetLtv(BigDecimal netLtv) {
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

	public BigDecimal getDmaSubventionNot() {
		return dmaSubventionNot;
	}

	public void setDmaSubventionNot(BigDecimal dmaSubventionNot) {
		this.dmaSubventionNot = dmaSubventionNot;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public BigDecimal getCfoc() {
		return cfoc;
	}

	public void setCfoc(BigDecimal cfoc) {
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

	public BigDecimal getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(BigDecimal manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public BigDecimal getEmployerId() {
		return employerId;
	}

	public void setEmployerId(BigDecimal employerId) {
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

	public BigDecimal getChargeId1() {
		return chargeId1;
	}

	public void setChargeId1(BigDecimal chargeId1) {
		this.chargeId1 = chargeId1;
	}

	public String getChargeDesc1() {
		return chargeDesc1;
	}

	public void setChargeDesc1(String chargeDesc1) {
		this.chargeDesc1 = chargeDesc1;
	}

	public BigDecimal getChargeAmt1() {
		return chargeAmt1;
	}

	public void setChargeAmt1(BigDecimal chargeAmt1) {
		this.chargeAmt1 = chargeAmt1;
	}

	public BigDecimal getChargeId2() {
		return chargeId2;
	}

	public void setChargeId2(BigDecimal chargeId2) {
		this.chargeId2 = chargeId2;
	}

	public String getChargeDesc2() {
		return chargeDesc2;
	}

	public void setChargeDesc2(String chargeDesc2) {
		this.chargeDesc2 = chargeDesc2;
	}

	public BigDecimal getChargeAmt2() {
		return chargeAmt2;
	}

	public void setChargeAmt2(BigDecimal chargeAmt2) {
		this.chargeAmt2 = chargeAmt2;
	}

	public BigDecimal getChargeId4() {
		return chargeId4;
	}

	public void setChargeId4(BigDecimal chargeId4) {
		this.chargeId4 = chargeId4;
	}

	public String getChargeDesc4() {
		return chargeDesc4;
	}

	public void setChargeDesc4(String chargeDesc4) {
		this.chargeDesc4 = chargeDesc4;
	}

	public BigDecimal getChargeAmt4() {
		return chargeAmt4;
	}

	public void setChargeAmt4(BigDecimal chargeAmt4) {
		this.chargeAmt4 = chargeAmt4;
	}

	public BigDecimal getChargeId5() {
		return chargeId5;
	}

	public void setChargeId5(BigDecimal chargeId5) {
		this.chargeId5 = chargeId5;
	}

	public String getChargeDesc5() {
		return chargeDesc5;
	}

	public void setChargeDesc5(String chargeDesc5) {
		this.chargeDesc5 = chargeDesc5;
	}

	public BigDecimal getChargeAmt5() {
		return chargeAmt5;
	}

	public void setChargeAmt5(BigDecimal chargeAmt5) {
		this.chargeAmt5 = chargeAmt5;
	}

	public BigDecimal getChargeId6() {
		return chargeId6;
	}

	public void setChargeId6(BigDecimal chargeId6) {
		this.chargeId6 = chargeId6;
	}

	public String getChargeDesc6() {
		return chargeDesc6;
	}

	public void setChargeDesc6(String chargeDesc6) {
		this.chargeDesc6 = chargeDesc6;
	}

	public BigDecimal getChargeAmt6() {
		return chargeAmt6;
	}

	public void setChargeAmt6(BigDecimal chargeAmt6) {
		this.chargeAmt6 = chargeAmt6;
	}

	public BigDecimal getChargeId8() {
		return chargeId8;
	}

	public void setChargeId8(BigDecimal chargeId8) {
		this.chargeId8 = chargeId8;
	}

	public String getChargeDesc8() {
		return chargeDesc8;
	}

	public void setChargeDesc8(String chargeDesc8) {
		this.chargeDesc8 = chargeDesc8;
	}

	public BigDecimal getChargeAmt8() {
		return chargeAmt8;
	}

	public void setChargeAmt8(BigDecimal chargeAmt8) {
		this.chargeAmt8 = chargeAmt8;
	}

	public BigDecimal getChargeId9() {
		return chargeId9;
	}

	public void setChargeId9(BigDecimal chargeId9) {
		this.chargeId9 = chargeId9;
	}

	public String getChargeDesc9() {
		return chargeDesc9;
	}

	public void setChargeDesc9(String chargeDesc9) {
		this.chargeDesc9 = chargeDesc9;
	}

	public BigDecimal getChargeAmt9() {
		return chargeAmt9;
	}

	public void setChargeAmt9(BigDecimal chargeAmt9) {
		this.chargeAmt9 = chargeAmt9;
	}

	public BigDecimal getChargeId10() {
		return chargeId10;
	}

	public void setChargeId10(BigDecimal chargeId10) {
		this.chargeId10 = chargeId10;
	}

	public String getChargeDesc10() {
		return chargeDesc10;
	}

	public void setChargeDesc10(String chargeDesc10) {
		this.chargeDesc10 = chargeDesc10;
	}

	public BigDecimal getChargeAmt10() {
		return chargeAmt10;
	}

	public void setChargeAmt10(BigDecimal chargeAmt10) {
		this.chargeAmt10 = chargeAmt10;
	}

	public BigDecimal getChargeId11() {
		return chargeId11;
	}

	public void setChargeId11(BigDecimal chargeId11) {
		this.chargeId11 = chargeId11;
	}

	public String getChargeDesc11() {
		return chargeDesc11;
	}

	public void setChargeDesc11(String chargeDesc11) {
		this.chargeDesc11 = chargeDesc11;
	}

	public BigDecimal getChargeAmt11() {
		return chargeAmt11;
	}

	public void setChargeAmt11(BigDecimal chargeAmt11) {
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

	public BigDecimal getCustomerAc() {
		return customerAc;
	}

	public void setCustomerAc(BigDecimal customerAc) {
		this.customerAc = customerAc;
	}

	public BigDecimal getMicr() {
		return micr;
	}

	public void setMicr(BigDecimal micr) {
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FinnoneDump(Long agreementId, String agreementNo, Date agreementDate, Date disbDate,
			BigDecimal disbursalAmount, BigDecimal disbAmt, BigDecimal amtFin, BigDecimal preTaxIrr,
			BigDecimal lesseeId, BigDecimal tenure, BigDecimal emi, String fileNo, String branchNm, String modelNo,
			String manufacturerDesc, String name, String dealerName, BigDecimal advanceInstl, BigDecimal processingFee,
			BigDecimal mfrSubventionIn, BigDecimal dealerSubvention, BigDecimal mfrSubventionPaid,
			BigDecimal dmaSubvention, String promotionDesc, BigDecimal marginMoney, BigDecimal advanceEmi,
			String employerName, String status, String make, String vAssetCatg, String productFlag, String branchCode,
			BigDecimal dmaBrokerCode, String schemeCode, String promotionScheme, BigDecimal effRate,
			BigDecimal modelCode, BigDecimal subModelCode, BigDecimal grossLtv, BigDecimal netLtv, String finalSource,
			String firstSource, String custCatg, BigDecimal dmaSubventionNot, String empType, BigDecimal cfoc,
			String state, String channelCode, BigDecimal manufacturerId, BigDecimal employerId, String inFavourOf,
			String chequeStatus, String ospCode, String dmeName, String dummy, String customerName,
			BigDecimal chargeId1, String chargeDesc1, BigDecimal chargeAmt1, BigDecimal chargeId2, String chargeDesc2,
			BigDecimal chargeAmt2, BigDecimal chargeId4, String chargeDesc4, BigDecimal chargeAmt4,
			BigDecimal chargeId5, String chargeDesc5, BigDecimal chargeAmt5, BigDecimal chargeId6, String chargeDesc6,
			BigDecimal chargeAmt6, BigDecimal chargeId8, String chargeDesc8, BigDecimal chargeAmt8,
			BigDecimal chargeId9, String chargeDesc9, BigDecimal chargeAmt9, BigDecimal chargeId10, String chargeDesc10,
			BigDecimal chargeAmt10, BigDecimal chargeId11, String chargeDesc11, BigDecimal chargeAmt11,
			String employmentType, String pslFlag, String instrumentType, String bank, String bankBranch,
			BigDecimal customerAc, BigDecimal micr, String destBankAcType, String processShop, String createdBy,
			Date createdDate, String remarks, Date cycleFromDate, Date cycleToDate, String uploadId, String fileName) {
		super();
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
		this.dmaSubventionNot = dmaSubventionNot;
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
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.remarks = remarks;
		this.cycleFromDate = cycleFromDate;
		this.cycleToDate = cycleToDate;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public FinnoneDump() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "FinnoneDump [agreementId=" + agreementId + ", agreementNo=" + agreementNo + ", agreementDate="
				+ agreementDate + ", disbDate=" + disbDate + ", disbursalAmount=" + disbursalAmount + ", disbAmt="
				+ disbAmt + ", amtFin=" + amtFin + ", preTaxIrr=" + preTaxIrr + ", lesseeId=" + lesseeId + ", tenure="
				+ tenure + ", emi=" + emi + ", fileNo=" + fileNo + ", branchNm=" + branchNm + ", modelNo=" + modelNo
				+ ", manufacturerDesc=" + manufacturerDesc + ", name=" + name + ", dealerName=" + dealerName
				+ ", advanceInstl=" + advanceInstl + ", processingFee=" + processingFee + ", mfrSubventionIn="
				+ mfrSubventionIn + ", dealerSubvention=" + dealerSubvention + ", mfrSubventionPaid="
				+ mfrSubventionPaid + ", dmaSubvention=" + dmaSubvention + ", promotionDesc=" + promotionDesc
				+ ", marginMoney=" + marginMoney + ", advanceEmi=" + advanceEmi + ", employerName=" + employerName
				+ ", status=" + status + ", make=" + make + ", vAssetCatg=" + vAssetCatg + ", productFlag="
				+ productFlag + ", branchCode=" + branchCode + ", dmaBrokerCode=" + dmaBrokerCode + ", schemeCode="
				+ schemeCode + ", promotionScheme=" + promotionScheme + ", effRate=" + effRate + ", modelCode="
				+ modelCode + ", subModelCode=" + subModelCode + ", grossLtv=" + grossLtv + ", netLtv=" + netLtv
				+ ", finalSource=" + finalSource + ", firstSource=" + firstSource + ", custCatg=" + custCatg
				+ ", dmaSubventionNot=" + dmaSubventionNot + ", empType=" + empType + ", cfoc=" + cfoc + ", state="
				+ state + ", channelCode=" + channelCode + ", manufacturerId=" + manufacturerId + ", employerId="
				+ employerId + ", inFavourOf=" + inFavourOf + ", chequeStatus=" + chequeStatus + ", ospCode=" + ospCode
				+ ", dmeName=" + dmeName + ", dummy=" + dummy + ", customerName=" + customerName + ", chargeId1="
				+ chargeId1 + ", chargeDesc1=" + chargeDesc1 + ", chargeAmt1=" + chargeAmt1 + ", chargeId2=" + chargeId2
				+ ", chargeDesc2=" + chargeDesc2 + ", chargeAmt2=" + chargeAmt2 + ", chargeId4=" + chargeId4
				+ ", chargeDesc4=" + chargeDesc4 + ", chargeAmt4=" + chargeAmt4 + ", chargeId5=" + chargeId5
				+ ", chargeDesc5=" + chargeDesc5 + ", chargeAmt5=" + chargeAmt5 + ", chargeId6=" + chargeId6
				+ ", chargeDesc6=" + chargeDesc6 + ", chargeAmt6=" + chargeAmt6 + ", chargeId8=" + chargeId8
				+ ", chargeDesc8=" + chargeDesc8 + ", chargeAmt8=" + chargeAmt8 + ", chargeId9=" + chargeId9
				+ ", chargeDesc9=" + chargeDesc9 + ", chargeAmt9=" + chargeAmt9 + ", chargeId10=" + chargeId10
				+ ", chargeDesc10=" + chargeDesc10 + ", chargeAmt10=" + chargeAmt10 + ", chargeId11=" + chargeId11
				+ ", chargeDesc11=" + chargeDesc11 + ", chargeAmt11=" + chargeAmt11 + ", employmentType="
				+ employmentType + ", pslFlag=" + pslFlag + ", instrumentType=" + instrumentType + ", bank=" + bank
				+ ", bankBranch=" + bankBranch + ", customerAc=" + customerAc + ", micr=" + micr + ", destBankAcType="
				+ destBankAcType + ", processShop=" + processShop + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", remarks=" + remarks + ", cycleFromDate=" + cycleFromDate + ", cycleToDate="
				+ cycleToDate + ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}
	
	
}
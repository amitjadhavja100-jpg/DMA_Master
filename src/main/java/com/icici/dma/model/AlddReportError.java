package com.icici.dma.model;

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
@Table(name = "TM_VHL_ALDD_TRANS_DUMP_ERROR")
public class AlddReportError {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ALDD_error_seq")
	@SequenceGenerator(name = "ALDD_error_seq", sequenceName = "ISEQ$$_138499", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private String errorId;
	
	@Column(name = "APPLICATION_NO")
	private String applicationNo;

	@Column(name = "ID")
	private String id;

	@Column(name = "MONTH")
	private String month;

	@Column(name = "LAN")
	private String lan;

	@Column(name = "FILENO")
	private String fileNo;

	@Column(name = "APPLICATION_NO_BK")
	private String applicationNoBk;

	@Column(name = "BRANCH_CODE")
	private String branchCode;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "SCHEME")
	private String scheme;

	@Column(name = "SCHEME_ID")
	private String schemeId;

	@Column(name = "SOL_ID")
	private String solId;

	@Column(name = "CUST_FNAME")
	private String custFname;

	@Column(name = "CUST_MNAME")
	private String custMname;

	@Column(name = "CUST_LNAME")
	private String custLname;

	/*@Column(name = "CONSTITUTION")
	private String constitution;*/
	
	@Column(name = "CONSTID")
	private String constitution;
	
	//@Temporal(TemporalType.DATE)
	@Column(name = "DOB")
	private String dob;

	@Column(name = "IND_CORP_FLAG")
	private String indCorpFlag;

	@Column(name = "CURR_ADD1")
	private String currAdd1;

	@Column(name = "CURR_ADD2")
	private String currAdd2;

	@Column(name = "CURR_ADD3")
	private String currAdd3;

	@Column(name = "CITY")
	private String city;

	@Column(name = "STATE")
	private String state;

	@Column(name = "ZIPCODE")
	private String zipcode;

	@Column(name = "PHONE1")
	private String phone1;

	@Column(name = "PHONE2")
	private String phone2;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "ASSET_TYPE")
	private String assetType;

	@Column(name = "MAKE")
	private String make;

	@Column(name = "MODEL_BK")
	private String modelBk;

	@Column(name = "SUB_MODEL")
	private String subModel;

	@Column(name = "ASSETCOST")
	private String assetCost;

	@Column(name = "MARGIN_MONEY")
	private String marginMoney;

	@Column(name = "AMOUNTFINANCED")
	private String amountFinanced;

	@Column(name = "TENURE")
	private String tenure;

	@Column(name = "EMI")
	private String emi;

	@Column(name = "IRR")
	private String irr;

	@Column(name = "ADVANCE_EMI")
	private String advanceEmi;
	
	@Column(name = "INSTALMENT_START_DATE")
	private String instalmentStartDate;

	@Column(name = "INSTL_TYPE")
	private String instlType;

	@Column(name = "FREQ")
	private String freq;

	@Column(name = "SUPPLIER")
	private String supplier;

	@Column(name = "BROKER")
	private String broker;

	@Column(name = "REP_MODE")
	private String repMode;

	@Column(name = "ACCT_NUMBER")
	private String acctNumber;

	@Column(name = "BANK_ACC_NO")
	private String bankAccNo;

	@Column(name = "ECS_ACCNO")
	private String ecsAccNo;

	@Column(name = "ECS_MICR")
	private String ecsMicr;

	@Column(name = "INDUSTRYDESC")
	private String industryDesc;

	@Column(name = "PROMOTION_DESC")
	private String promotionDesc;

	@Column(name = "ASSET_CATAGORY")
	private String assetCategory;

	@Column(name = "CHANNELCODE")
	private String channelCode;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "DME")
	private String dme;

	@Column(name = "MKTG_OFFICER")
	private String mktgOfficer;

	@Column(name = "FIRST_SOURCE")
	private String firstSource;

	@Column(name = "FINAL_SOURCE")
	private String finalSource;

	@Column(name = "UN_FIRST_SOURCE")
	private String unFirstSource;

	@Column(name = "UN_FINAL_SOURCE")
	private String unFinalSource;

	@Column(name = "CONNECTOR_NAME")
	private String connectorName;

	@Column(name = "LAN_BK")
	private String lanBk;

	@Column(name = "RELIGION")
	private String religion;

	@Column(name = "PROFESSION")
	private String profession;

	@Column(name = "SC_ST_FLAG")
	private String scStFlag;

	@Column(name = "SEX")
	private String sex;

	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;

	@Column(name = "QUALIFICATION")
	private String qualification;

	@Column(name = "ADDRESSTYPE")
	private String addressType;

	@Column(name = "EMAIL_COMMUNICATION")
	private String emailCommunication;

	@Column(name = "CALL_COMMUNICATION")
	private String callCommunication;

	@Column(name = "PPI_AMT")
	private String ppiAmt;

	@Column(name = "APPLICATION_DATE")
	private String applicationDate;

	@Column(name = "UPLOAD_DISBURSAL_DATE")
	private String uploadDisbursalDate;

	@Column(name = "LOANTYPE")
	private String loanType;

	@Column(name = "EFFRATE")
	private String effRate;

	@Column(name = "CHARGE_CODE1")
	private String chargeCode1;

	@Column(name = "CHARGE_AMOUNT1")
	private String chargeAmount1;

	@Column(name = "CHARGE_CODE2")
	private String chargeCode2;

	@Column(name = "CHARGE_AMOUNT2")
	private String chargeAmount2;

	@Column(name = "ANNUALISEDAPR")
	private String annualisedApr;

	@Column(name = "PSL_FLAG")
	private String pslFlag;

	@Column(name = "PSL_CODE")
	private String pslCode;

	@Column(name = "SME_CODE")
	private String smeCode;

	@Column(name = "HNICODE")
	private String hniCode;

	@Column(name = "OWNED_IRRIGATED")
	private String ownedIrrigated;

	@Column(name = "OWNED_NON_IRRIGATED")
	private String ownedNonIrrigated;

	@Column(name = "LEASED_IN_IRRIGATED")
	private String leasedInIrrigated;

	@Column(name = "LEASED_IN_NON_IRRIGATED")
	private String leasedInNonIrrigated;

	@Column(name = "LEASED_OUT_IRRIGATED")
	private String leasedOutIrrigated;

	@Column(name = "LEASED_OUT_NON_IRRIGATED")
	private String leasedOutNonIrrigated;

	@Column(name = "DISBURSAL_TO")
	private String disbursalTo;

	@Column(name = "CROSSCOLL_WITH")
	private String crossCollWith;

	@Column(name = "UMRN_NUMBER")
	private String umrnNumber;

	@Column(name = "NPCI_MANDATE_UPLD_DATE")
	private String npciMandateUpldDate;

	@Column(name = "REGISTRATION_STATUS")
	private String registrationStatus;

	@Column(name = "NACH_EFFECTIVE_DATE")
	private String nachEffectiveDate;

	@Column(name = "REGISTRATION_AMOUNT")
	private String registrationAmount;

	@Column(name = "UMRN_ACCEPTANCE_DATE")
	private String umrnAcceptanceDate;

	@Column(name = "REJECT_CODE")
	private String rejectCode;

	@Column(name = "FATHER_NAME")
	private String fatherName;

	@Column(name = "SPOUSE_NAME")
	private String spouseName;

	@Column(name = "INDUSTRY")
	private String industry;

	@Column(name = "LOAN_PURPOSE")
	private String loanPurpose;

	@Column(name = "MARGIN_MONEY_CODE")
	private String marginMoneyCode;

	@Column(name = "DMA_CODE")
	private String dmaCode;

	@Column(name = "IS_RBI_DECLARATION_SELECTED")
	private String isRbiDeclarationSelected;

	@Column(name = "ECODE")
	private String ecode;

	@Column(name = "REFERRAL_CODE")
	private String referralCode;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;
/*
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDate modifiedDate;*/

	@Column(name = "REMARKS")
	private String remarks;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
	private Date fromCycleDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TO_CYCLE_DATE")
	private Date toCycleDate;
	
	@Column(name = "ERROR_MSG", length = 4000)
	private String errorMsg;
	
	@Column(name = "ROW_NUMBER", length = 10)
	private Integer rowNumber;
	
	@Column(name = "UPLOAD_ID", length = 30)
	private String uploadId;
	

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

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getApplicationNoBk() {
		return applicationNoBk;
	}

	public void setApplicationNoBk(String applicationNoBk) {
		this.applicationNoBk = applicationNoBk;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(String schemeId) {
		this.schemeId = schemeId;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public String getCustFname() {
		return custFname;
	}

	public void setCustFname(String custFname) {
		this.custFname = custFname;
	}

	public String getCustMname() {
		return custMname;
	}

	public void setCustMname(String custMname) {
		this.custMname = custMname;
	}

	public String getCustLname() {
		return custLname;
	}

	public void setCustLname(String custLname) {
		this.custLname = custLname;
	}

	public String getConstitution() {
		return constitution;
	}

	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getIndCorpFlag() {
		return indCorpFlag;
	}

	public void setIndCorpFlag(String indCorpFlag) {
		this.indCorpFlag = indCorpFlag;
	}

	public String getCurrAdd1() {
		return currAdd1;
	}

	public void setCurrAdd1(String currAdd1) {
		this.currAdd1 = currAdd1;
	}

	public String getCurrAdd2() {
		return currAdd2;
	}

	public void setCurrAdd2(String currAdd2) {
		this.currAdd2 = currAdd2;
	}

	public String getCurrAdd3() {
		return currAdd3;
	}

	public void setCurrAdd3(String currAdd3) {
		this.currAdd3 = currAdd3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModelBk() {
		return modelBk;
	}

	public void setModelBk(String modelBk) {
		this.modelBk = modelBk;
	}

	public String getSubModel() {
		return subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
	}

	public String getAssetCost() {
		return assetCost;
	}

	public void setAssetCost(String assetCost) {
		this.assetCost = assetCost;
	}

	public String getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(String marginMoney) {
		this.marginMoney = marginMoney;
	}

	public String getAmountFinanced() {
		return amountFinanced;
	}

	public void setAmountFinanced(String amountFinanced) {
		this.amountFinanced = amountFinanced;
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

	public String getIrr() {
		return irr;
	}

	public void setIrr(String irr) {
		this.irr = irr;
	}

	public String getAdvanceEmi() {
		return advanceEmi;
	}

	public void setAdvanceEmi(String advanceEmi) {
		this.advanceEmi = advanceEmi;
	}

	public String getInstalmentStartDate() {
		return instalmentStartDate;
	}

	public void setInstallmentStartDate(String instalmentStartDate) {
		this.instalmentStartDate = instalmentStartDate;
	}

	public String getInstlType() {
		return instlType;
	}

	public void setInstlType(String instlType) {
		this.instlType = instlType;
	}

	public String getFreq() {
		return freq;
	}

	public void setFreq(String freq) {
		this.freq = freq;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getRepMode() {
		return repMode;
	}

	public void setRepMode(String repMode) {
		this.repMode = repMode;
	}

	public String getAcctNumber() {
		return acctNumber;
	}

	public void setAcctNumber(String acctNumber) {
		this.acctNumber = acctNumber;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public String getEcsAccNo() {
		return ecsAccNo;
	}

	public void setEcsAccNo(String ecsAccNo) {
		this.ecsAccNo = ecsAccNo;
	}

	public String getEcsMicr() {
		return ecsMicr;
	}

	public void setEcsMicr(String ecsMicr) {
		this.ecsMicr = ecsMicr;
	}

	public String getIndustryDesc() {
		return industryDesc;
	}

	public void setIndustryDesc(String industryDesc) {
		this.industryDesc = industryDesc;
	}

	public String getPromotionDesc() {
		return promotionDesc;
	}

	public void setPromotionDesc(String promotionDesc) {
		this.promotionDesc = promotionDesc;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getDme() {
		return dme;
	}

	public void setDme(String dme) {
		this.dme = dme;
	}

	public String getMktgOfficer() {
		return mktgOfficer;
	}

	public void setMktgOfficer(String mktgOfficer) {
		this.mktgOfficer = mktgOfficer;
	}

	public String getFirstSource() {
		return firstSource;
	}

	public void setFirstSource(String firstSource) {
		this.firstSource = firstSource;
	}

	public String getFinalSource() {
		return finalSource;
	}

	public void setFinalSource(String finalSource) {
		this.finalSource = finalSource;
	}

	public String getUnFirstSource() {
		return unFirstSource;
	}

	public void setUnFirstSource(String unFirstSource) {
		this.unFirstSource = unFirstSource;
	}

	public String getUnFinalSource() {
		return unFinalSource;
	}

	public void setUnFinalSource(String unFinalSource) {
		this.unFinalSource = unFinalSource;
	}

	public String getConnectorName() {
		return connectorName;
	}

	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}

	public String getLanBk() {
		return lanBk;
	}

	public void setLanBk(String lanBk) {
		this.lanBk = lanBk;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getScStFlag() {
		return scStFlag;
	}

	public void setScStFlag(String scStFlag) {
		this.scStFlag = scStFlag;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getEmailCommunication() {
		return emailCommunication;
	}

	public void setEmailCommunication(String emailCommunication) {
		this.emailCommunication = emailCommunication;
	}

	public String getCallCommunication() {
		return callCommunication;
	}

	public void setCallCommunication(String callCommunication) {
		this.callCommunication = callCommunication;
	}

	public String getPpiAmt() {
		return ppiAmt;
	}

	public void setPpiAmt(String ppiAmt) {
		this.ppiAmt = ppiAmt;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getUploadDisbursalDate() {
		return uploadDisbursalDate;
	}

	public void setUploadDisbursalDate(String uploadDisbursalDate) {
		this.uploadDisbursalDate = uploadDisbursalDate;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getEffRate() {
		return effRate;
	}

	public void setEffRate(String effRate) {
		this.effRate = effRate;
	}

	public String getChargeCode1() {
		return chargeCode1;
	}

	public void setChargeCode1(String chargeCode1) {
		this.chargeCode1 = chargeCode1;
	}

	public String getChargeAmount1() {
		return chargeAmount1;
	}

	public void setChargeAmount1(String chargeAmount1) {
		this.chargeAmount1 = chargeAmount1;
	}

	public String getChargeCode2() {
		return chargeCode2;
	}

	public void setChargeCode2(String chargeCode2) {
		this.chargeCode2 = chargeCode2;
	}

	public String getChargeAmount2() {
		return chargeAmount2;
	}

	public void setChargeAmount2(String chargeAmount2) {
		this.chargeAmount2 = chargeAmount2;
	}

	public String getAnnualisedApr() {
		return annualisedApr;
	}

	public void setAnnualisedApr(String annualisedApr) {
		this.annualisedApr = annualisedApr;
	}

	public String getPslFlag() {
		return pslFlag;
	}

	public void setPslFlag(String pslFlag) {
		this.pslFlag = pslFlag;
	}

	public String getPslCode() {
		return pslCode;
	}

	public void setPslCode(String pslCode) {
		this.pslCode = pslCode;
	}

	public String getSmeCode() {
		return smeCode;
	}

	public void setSmeCode(String smeCode) {
		this.smeCode = smeCode;
	}

	public String getHniCode() {
		return hniCode;
	}

	public void setHniCode(String hniCode) {
		this.hniCode = hniCode;
	}

	public String getOwnedIrrigated() {
		return ownedIrrigated;
	}

	public void setOwnedIrrigated(String ownedIrrigated) {
		this.ownedIrrigated = ownedIrrigated;
	}

	public String getOwnedNonIrrigated() {
		return ownedNonIrrigated;
	}

	public void setOwnedNonIrrigated(String ownedNonIrrigated) {
		this.ownedNonIrrigated = ownedNonIrrigated;
	}

	public String getLeasedInIrrigated() {
		return leasedInIrrigated;
	}

	public void setLeasedInIrrigated(String leasedInIrrigated) {
		this.leasedInIrrigated = leasedInIrrigated;
	}

	public String getLeasedInNonIrrigated() {
		return leasedInNonIrrigated;
	}

	public void setLeasedInNonIrrigated(String leasedInNonIrrigated) {
		this.leasedInNonIrrigated = leasedInNonIrrigated;
	}

	public String getLeasedOutIrrigated() {
		return leasedOutIrrigated;
	}

	public void setLeasedOutIrrigated(String leasedOutIrrigated) {
		this.leasedOutIrrigated = leasedOutIrrigated;
	}

	public String getLeasedOutNonIrrigated() {
		return leasedOutNonIrrigated;
	}

	public void setLeasedOutNonIrrigated(String leasedOutNonIrrigated) {
		this.leasedOutNonIrrigated = leasedOutNonIrrigated;
	}

	public String getDisbursalTo() {
		return disbursalTo;
	}

	public void setDisbursalTo(String disbursalTo) {
		this.disbursalTo = disbursalTo;
	}

	public String getCrossCollWith() {
		return crossCollWith;
	}

	public void setCrossCollWith(String crossCollWith) {
		this.crossCollWith = crossCollWith;
	}

	public String getUmrnNumber() {
		return umrnNumber;
	}

	public void setUmrnNumber(String umrnNumber) {
		this.umrnNumber = umrnNumber;
	}

	public String getNpciMandateUpldDate() {
		return npciMandateUpldDate;
	}

	public void setNpciMandateUpldDate(String npciMandateUpldDate) {
		this.npciMandateUpldDate = npciMandateUpldDate;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public String getNachEffectiveDate() {
		return nachEffectiveDate;
	}

	public void setNachEffectiveDate(String nachEffectiveDate) {
		this.nachEffectiveDate = nachEffectiveDate;
	}

	public String getRegistrationAmount() {
		return registrationAmount;
	}

	public void setRegistrationAmount(String registrationAmount) {
		this.registrationAmount = registrationAmount;
	}

	public String getUmrnAcceptanceDate() {
		return umrnAcceptanceDate;
	}

	public void setUmrnAcceptanceDate(String umrnAcceptanceDate) {
		this.umrnAcceptanceDate = umrnAcceptanceDate;
	}

	public String getRejectCode() {
		return rejectCode;
	}

	public void setRejectCode(String rejectCode) {
		this.rejectCode = rejectCode;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(String loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getMarginMoneyCode() {
		return marginMoneyCode;
	}

	public void setMarginMoneyCode(String marginMoneyCode) {
		this.marginMoneyCode = marginMoneyCode;
	}

	public String getDmaCode() {
		return dmaCode;
	}

	public void setDmaCode(String dmaCode) {
		this.dmaCode = dmaCode;
	}

	public String getIsRbiDeclarationSelected() {
		return isRbiDeclarationSelected;
	}

	public void setIsRbiDeclarationSelected(String isRbiDeclarationSelected) {
		this.isRbiDeclarationSelected = isRbiDeclarationSelected;
	}

	public String getEcode() {
		return ecode;
	}

	public void setEcode(String ecode) {
		this.ecode = ecode;
	}

	public String getReferralCode() {
		return referralCode;
	}

	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
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

	/*public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDate getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDate modifiedDate) {
		this.modifiedDate = modifiedDate;
	}*/

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getFromCycleDate() {
		return fromCycleDate;
	}

	public void setFromCycleDate(Date fromCycleDate) {
		this.fromCycleDate = fromCycleDate;
	}

	public Date getToCycleDate() {
		return toCycleDate;
	}

	public void setToCycleDate(Date toCycleDate) {
		this.toCycleDate = toCycleDate;
	}
	
}

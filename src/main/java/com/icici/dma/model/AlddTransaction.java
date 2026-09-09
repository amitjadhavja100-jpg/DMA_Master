package com.icici.dma.model;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "TM_VHL_ALDD_TRANS_DUMP")
public class AlddTransaction {

	@Id
	@Column(name = "APPLICATION_NO")
	private String applicationNo;

	@Column(name = "ID")
	private Long id;

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
	private Long schemeId;

	@Column(name = "SOL_ID")
	private Long solId;

	@Column(name = "CUST_FNAME")
	private String custFname;

	@Column(name = "CUST_MNAME")
	private String custMname;

	@Column(name = "CUST_LNAME")
	private String custLname;

	@Column(name = "CONSTID")
	private Long constitution;

	/*
	 * @Column(name = "CONSTITUTION") private Long constitution;
	 */

	@Column(name = "DOB")
	private Date dob;

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
	private Long zipcode;

	@Column(name = "PHONE1")
	private Long phone1;

	@Column(name = "PHONE2")
	private Long phone2;

	@Column(name = "MOBILE")
	private Long mobile;

	@Column(name = "ASSET_TYPE")
	private String assetType;

	@Column(name = "MAKE")
	private Long make;

	@Column(name = "MODEL_BK")
	private Long modelBk;

	@Column(name = "SUB_MODEL")
	private Long subModel;

	@Column(name = "ASSETCOST")
	private Long assetCost;

	@Column(name = "MARGIN_MONEY")
	private Long marginMoney;

	@Column(name = "AMOUNTFINANCED")
	private Long amountFinanced;

	@Column(name = "TENURE")
	private Long tenure;

	@Column(name = "EMI")
	private Long emi;

	@Column(name = "IRR")
	private Long irr;

	@Column(name = "ADVANCE_EMI")
	private Long advanceEmi;

	@Column(name = "INSTALMENT_START_DATE")
	private Date instalmentStartDate;

	@Column(name = "INSTL_TYPE")
	private String instlType;

	@Column(name = "FREQ")
	private String freq;

	@Column(name = "SUPPLIER")
	private Long supplier;

	@Column(name = "BROKER")
	private String broker;

	@Column(name = "REP_MODE")
	private String repMode;

	@Column(name = "ACCT_NUMBER")
	private Long acctNumber;

	@Column(name = "BANK_ACC_NO")
	private Long bankAccNo;

	@Column(name = "ECS_ACCNO")
	private Long ecsAccNo;

	@Column(name = "ECS_MICR")
	private Long ecsMicr;

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
	private Date applicationDate;

	@Column(name = "UPLOAD_DISBURSAL_DATE")
	private Date uploadDisbursalDate;

	@Column(name = "LOANTYPE")
	private String loanType;

	@Column(name = "EFFRATE")
	private Long effRate;

	@Column(name = "CHARGE_CODE1")
	private Long chargeCode1;

	@Column(name = "CHARGE_AMOUNT1")
	private Long chargeAmount1;

	@Column(name = "CHARGE_CODE2")
	private Long chargeCode2;

	@Column(name = "CHARGE_AMOUNT2")
	private Long chargeAmount2;

	@Column(name = "ANNUALISEDAPR")
	private Long annualisedApr;

	@Column(name = "PSL_FLAG")
	private String pslFlag;

	@Column(name = "PSL_CODE")
	private String pslCode;

	@Column(name = "SME_CODE")
	private String smeCode;

	@Column(name = "HNICODE")
	private String hniCode;

	@Column(name = "OWNED_IRRIGATED")
	private Long ownedIrrigated;

	@Column(name = "OWNED_NON_IRRIGATED")
	private Long ownedNonIrrigated;

	@Column(name = "LEASED_IN_IRRIGATED")
	private Long leasedInIrrigated;

	@Column(name = "LEASED_IN_NON_IRRIGATED")
	private Long leasedInNonIrrigated;

	@Column(name = "LEASED_OUT_IRRIGATED")
	private Long leasedOutIrrigated;

	@Column(name = "LEASED_OUT_NON_IRRIGATED")
	private Long leasedOutNonIrrigated;

	@Column(name = "DISBURSAL_TO")
	private String disbursalTo;

	@Column(name = "CROSSCOLL_WITH")
	private String crossCollWith;

	@Column(name = "UMRN_NUMBER")
	private Long umrnNumber;

	@Column(name = "NPCI_MANDATE_UPLD_DATE")
	private Date npciMandateUpldDate;

	@Column(name = "REGISTRATION_STATUS")
	private String registrationStatus;

	@Column(name = "NACH_EFFECTIVE_DATE")
	private Date nachEffectiveDate;

	@Column(name = "REGISTRATION_AMOUNT")
	private Long registrationAmount;

	@Column(name = "UMRN_ACCEPTANCE_DATE")
	private Date umrnAcceptanceDate;

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
	private Long dmaCode;

	@Column(name = "IS_RBI_DECLARATION_SELECTED")
	private Boolean isRbiDeclarationSelected;

	@Column(name = "ECODE")
	private Long ecode;

	@Column(name = "REFERRAL_CODE")
	private String referralCode;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	/*
	 * @Column(name = "MODIFIED_BY") private String modifiedBy;
	 * 
	 * @Column(name = "MODIFIED_DATE") private LocalDate modifiedDate;
	 */

	@Column(name = "REMARKS")
	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
	private Date fromCycleDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "TO_CYCLE_DATE")
	private Date toCycleDate;

	@Column(name = "UPLOAD_ID")
	private String uploadId;

	@Column(name = "FILE_NAME")
	private String fileName;

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Long getSchemeId() {
		return schemeId;
	}

	public void setSchemeId(Long schemeId) {
		this.schemeId = schemeId;
	}

	public Long getSolId() {
		return solId;
	}

	public void setSolId(Long solId) {
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

	public Long getConstitution() {
		return constitution;
	}

	public void setConstitution(Long constitution) {
		this.constitution = constitution;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
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

	public Long getZipcode() {
		return zipcode;
	}

	public void setZipcode(Long zipcode) {
		this.zipcode = zipcode;
	}

	public Long getPhone1() {
		return phone1;
	}

	public void setPhone1(Long phone1) {
		this.phone1 = phone1;
	}

	public Long getPhone2() {
		return phone2;
	}

	public void setPhone2(Long phone2) {
		this.phone2 = phone2;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Long getMake() {
		return make;
	}

	public void setMake(Long make) {
		this.make = make;
	}

	public Long getModelBk() {
		return modelBk;
	}

	public void setModelBk(Long modelBk) {
		this.modelBk = modelBk;
	}

	public Long getSubModel() {
		return subModel;
	}

	public void setSubModel(Long subModel) {
		this.subModel = subModel;
	}

	public Long getAssetCost() {
		return assetCost;
	}

	public void setAssetCost(Long assetCost) {
		this.assetCost = assetCost;
	}

	public Long getMarginMoney() {
		return marginMoney;
	}

	public void setMarginMoney(Long marginMoney) {
		this.marginMoney = marginMoney;
	}

	public Long getAmountFinanced() {
		return amountFinanced;
	}

	public void setAmountFinanced(Long amountFinanced) {
		this.amountFinanced = amountFinanced;
	}

	public Long getTenure() {
		return tenure;
	}

	public void setTenure(Long tenure) {
		this.tenure = tenure;
	}

	public Long getEmi() {
		return emi;
	}

	public void setEmi(Long emi) {
		this.emi = emi;
	}

	public Long getIrr() {
		return irr;
	}

	public void setIrr(Long irr) {
		this.irr = irr;
	}

	public Long getAdvanceEmi() {
		return advanceEmi;
	}

	public void setAdvanceEmi(Long advanceEmi) {
		this.advanceEmi = advanceEmi;
	}

	public Date getInstalmentStartDate() {
		return instalmentStartDate;
	}

	public void setInstalmentStartDate(Date instalmentStartDate) {
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

	public Long getSupplier() {
		return supplier;
	}

	public void setSupplier(Long supplier) {
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

	public Long getAcctNumber() {
		return acctNumber;
	}

	public void setAcctNumber(Long acctNumber) {
		this.acctNumber = acctNumber;
	}

	public Long getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(Long bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public Long getEcsAccNo() {
		return ecsAccNo;
	}

	public void setEcsAccNo(Long ecsAccNo) {
		this.ecsAccNo = ecsAccNo;
	}

	public Long getEcsMicr() {
		return ecsMicr;
	}

	public void setEcsMicr(Long ecsMicr) {
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

	public Date getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Date applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Date getUploadDisbursalDate() {
		return uploadDisbursalDate;
	}

	public void setUploadDisbursalDate(Date uploadDisbursalDate) {
		this.uploadDisbursalDate = uploadDisbursalDate;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Long getEffRate() {
		return effRate;
	}

	public void setEffRate(Long effRate) {
		this.effRate = effRate;
	}

	public Long getChargeCode1() {
		return chargeCode1;
	}

	public void setChargeCode1(Long chargeCode1) {
		this.chargeCode1 = chargeCode1;
	}

	public Long getChargeAmount1() {
		return chargeAmount1;
	}

	public void setChargeAmount1(Long chargeAmount1) {
		this.chargeAmount1 = chargeAmount1;
	}

	public Long getChargeCode2() {
		return chargeCode2;
	}

	public void setChargeCode2(Long chargeCode2) {
		this.chargeCode2 = chargeCode2;
	}

	public Long getChargeAmount2() {
		return chargeAmount2;
	}

	public void setChargeAmount2(Long chargeAmount2) {
		this.chargeAmount2 = chargeAmount2;
	}

	public Long getAnnualisedApr() {
		return annualisedApr;
	}

	public void setAnnualisedApr(Long annualisedApr) {
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

	public Long getOwnedIrrigated() {
		return ownedIrrigated;
	}

	public void setOwnedIrrigated(Long ownedIrrigated) {
		this.ownedIrrigated = ownedIrrigated;
	}

	public Long getOwnedNonIrrigated() {
		return ownedNonIrrigated;
	}

	public void setOwnedNonIrrigated(Long ownedNonIrrigated) {
		this.ownedNonIrrigated = ownedNonIrrigated;
	}

	public Long getLeasedInIrrigated() {
		return leasedInIrrigated;
	}

	public void setLeasedInIrrigated(Long leasedInIrrigated) {
		this.leasedInIrrigated = leasedInIrrigated;
	}

	public Long getLeasedInNonIrrigated() {
		return leasedInNonIrrigated;
	}

	public void setLeasedInNonIrrigated(Long leasedInNonIrrigated) {
		this.leasedInNonIrrigated = leasedInNonIrrigated;
	}

	public Long getLeasedOutIrrigated() {
		return leasedOutIrrigated;
	}

	public void setLeasedOutIrrigated(Long leasedOutIrrigated) {
		this.leasedOutIrrigated = leasedOutIrrigated;
	}

	public Long getLeasedOutNonIrrigated() {
		return leasedOutNonIrrigated;
	}

	public void setLeasedOutNonIrrigated(Long leasedOutNonIrrigated) {
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

	public Long getUmrnNumber() {
		return umrnNumber;
	}

	public void setUmrnNumber(Long umrnNumber) {
		this.umrnNumber = umrnNumber;
	}

	public Date getNpciMandateUpldDate() {
		return npciMandateUpldDate;
	}

	public void setNpciMandateUpldDate(Date npciMandateUpldDate) {
		this.npciMandateUpldDate = npciMandateUpldDate;
	}

	public String getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	public Date getNachEffectiveDate() {
		return nachEffectiveDate;
	}

	public void setNachEffectiveDate(Date nachEffectiveDate) {
		this.nachEffectiveDate = nachEffectiveDate;
	}

	public Long getRegistrationAmount() {
		return registrationAmount;
	}

	public void setRegistrationAmount(Long registrationAmount) {
		this.registrationAmount = registrationAmount;
	}

	public Date getUmrnAcceptanceDate() {
		return umrnAcceptanceDate;
	}

	public void setUmrnAcceptanceDate(Date umrnAcceptanceDate) {
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

	public Long getDmaCode() {
		return dmaCode;
	}

	public void setDmaCode(Long dmaCode) {
		this.dmaCode = dmaCode;
	}

	public Boolean getIsRbiDeclarationSelected() {
		return isRbiDeclarationSelected;
	}

	public void setIsRbiDeclarationSelected(Boolean isRbiDeclarationSelected) {
		this.isRbiDeclarationSelected = isRbiDeclarationSelected;
	}

	public Long getEcode() {
		return ecode;
	}

	public void setEcode(Long ecode) {
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

	public AlddTransaction(String applicationNo, Long id, String month, String lan, String fileNo,
			String applicationNoBk, String branchCode, String product, String scheme, Long schemeId, Long solId,
			String custFname, String custMname, String custLname, Long constitution, Date dob, String indCorpFlag,
			String currAdd1, String currAdd2, String currAdd3, String city, String state, Long zipcode, Long phone1,
			Long phone2, Long mobile, String assetType, Long make, Long modelBk, Long subModel, Long assetCost,
			Long marginMoney, Long amountFinanced, Long tenure, Long emi, Long irr, Long advanceEmi,
			Date instalmentStartDate, String instlType, String freq, Long supplier, String broker, String repMode,
			Long acctNumber, Long bankAccNo, Long ecsAccNo, Long ecsMicr, String industryDesc, String promotionDesc,
			String assetCategory, String channelCode, String employeeName, String dme, String mktgOfficer,
			String firstSource, String finalSource, String unFirstSource, String unFinalSource, String connectorName,
			String lanBk, String religion, String profession, String scStFlag, String sex, String maritalStatus,
			String qualification, String addressType, String emailCommunication, String callCommunication,
			String ppiAmt, Date applicationDate, Date uploadDisbursalDate, String loanType, Long effRate,
			Long chargeCode1, Long chargeAmount1, Long chargeCode2, Long chargeAmount2, Long annualisedApr,
			String pslFlag, String pslCode, String smeCode, String hniCode, Long ownedIrrigated, Long ownedNonIrrigated,
			Long leasedInIrrigated, Long leasedInNonIrrigated, Long leasedOutIrrigated, Long leasedOutNonIrrigated,
			String disbursalTo, String crossCollWith, Long umrnNumber, Date npciMandateUpldDate,
			String registrationStatus, Date nachEffectiveDate, Long registrationAmount, Date umrnAcceptanceDate,
			String rejectCode, String fatherName, String spouseName, String industry, String loanPurpose,
			String marginMoneyCode, Long dmaCode, Boolean isRbiDeclarationSelected, Long ecode, String referralCode,
			String createdBy, Date createdDate, String remarks, Date fromCycleDate, Date toCycleDate, String uploadId,
			String fileName) {
		super();
		this.applicationNo = applicationNo;
		this.id = id;
		this.month = month;
		this.lan = lan;
		this.fileNo = fileNo;
		this.applicationNoBk = applicationNoBk;
		this.branchCode = branchCode;
		this.product = product;
		this.scheme = scheme;
		this.schemeId = schemeId;
		this.solId = solId;
		this.custFname = custFname;
		this.custMname = custMname;
		this.custLname = custLname;
		this.constitution = constitution;
		this.dob = dob;
		this.indCorpFlag = indCorpFlag;
		this.currAdd1 = currAdd1;
		this.currAdd2 = currAdd2;
		this.currAdd3 = currAdd3;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.mobile = mobile;
		this.assetType = assetType;
		this.make = make;
		this.modelBk = modelBk;
		this.subModel = subModel;
		this.assetCost = assetCost;
		this.marginMoney = marginMoney;
		this.amountFinanced = amountFinanced;
		this.tenure = tenure;
		this.emi = emi;
		this.irr = irr;
		this.advanceEmi = advanceEmi;
		this.instalmentStartDate = instalmentStartDate;
		this.instlType = instlType;
		this.freq = freq;
		this.supplier = supplier;
		this.broker = broker;
		this.repMode = repMode;
		this.acctNumber = acctNumber;
		this.bankAccNo = bankAccNo;
		this.ecsAccNo = ecsAccNo;
		this.ecsMicr = ecsMicr;
		this.industryDesc = industryDesc;
		this.promotionDesc = promotionDesc;
		this.assetCategory = assetCategory;
		this.channelCode = channelCode;
		this.employeeName = employeeName;
		this.dme = dme;
		this.mktgOfficer = mktgOfficer;
		this.firstSource = firstSource;
		this.finalSource = finalSource;
		this.unFirstSource = unFirstSource;
		this.unFinalSource = unFinalSource;
		this.connectorName = connectorName;
		this.lanBk = lanBk;
		this.religion = religion;
		this.profession = profession;
		this.scStFlag = scStFlag;
		this.sex = sex;
		this.maritalStatus = maritalStatus;
		this.qualification = qualification;
		this.addressType = addressType;
		this.emailCommunication = emailCommunication;
		this.callCommunication = callCommunication;
		this.ppiAmt = ppiAmt;
		this.applicationDate = applicationDate;
		this.uploadDisbursalDate = uploadDisbursalDate;
		this.loanType = loanType;
		this.effRate = effRate;
		this.chargeCode1 = chargeCode1;
		this.chargeAmount1 = chargeAmount1;
		this.chargeCode2 = chargeCode2;
		this.chargeAmount2 = chargeAmount2;
		this.annualisedApr = annualisedApr;
		this.pslFlag = pslFlag;
		this.pslCode = pslCode;
		this.smeCode = smeCode;
		this.hniCode = hniCode;
		this.ownedIrrigated = ownedIrrigated;
		this.ownedNonIrrigated = ownedNonIrrigated;
		this.leasedInIrrigated = leasedInIrrigated;
		this.leasedInNonIrrigated = leasedInNonIrrigated;
		this.leasedOutIrrigated = leasedOutIrrigated;
		this.leasedOutNonIrrigated = leasedOutNonIrrigated;
		this.disbursalTo = disbursalTo;
		this.crossCollWith = crossCollWith;
		this.umrnNumber = umrnNumber;
		this.npciMandateUpldDate = npciMandateUpldDate;
		this.registrationStatus = registrationStatus;
		this.nachEffectiveDate = nachEffectiveDate;
		this.registrationAmount = registrationAmount;
		this.umrnAcceptanceDate = umrnAcceptanceDate;
		this.rejectCode = rejectCode;
		this.fatherName = fatherName;
		this.spouseName = spouseName;
		this.industry = industry;
		this.loanPurpose = loanPurpose;
		this.marginMoneyCode = marginMoneyCode;
		this.dmaCode = dmaCode;
		this.isRbiDeclarationSelected = isRbiDeclarationSelected;
		this.ecode = ecode;
		this.referralCode = referralCode;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.remarks = remarks;
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public AlddTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "AlddTransaction [applicationNo=" + applicationNo + ", id=" + id + ", month=" + month + ", lan=" + lan
				+ ", fileNo=" + fileNo + ", applicationNoBk=" + applicationNoBk + ", branchCode=" + branchCode
				+ ", product=" + product + ", scheme=" + scheme + ", schemeId=" + schemeId + ", solId=" + solId
				+ ", custFname=" + custFname + ", custMname=" + custMname + ", custLname=" + custLname
				+ ", constitution=" + constitution + ", dob=" + dob + ", indCorpFlag=" + indCorpFlag + ", currAdd1="
				+ currAdd1 + ", currAdd2=" + currAdd2 + ", currAdd3=" + currAdd3 + ", city=" + city + ", state=" + state
				+ ", zipcode=" + zipcode + ", phone1=" + phone1 + ", phone2=" + phone2 + ", mobile=" + mobile
				+ ", assetType=" + assetType + ", make=" + make + ", modelBk=" + modelBk + ", subModel=" + subModel
				+ ", assetCost=" + assetCost + ", marginMoney=" + marginMoney + ", amountFinanced=" + amountFinanced
				+ ", tenure=" + tenure + ", emi=" + emi + ", irr=" + irr + ", advanceEmi=" + advanceEmi
				+ ", instalmentStartDate=" + instalmentStartDate + ", instlType=" + instlType + ", freq=" + freq
				+ ", supplier=" + supplier + ", broker=" + broker + ", repMode=" + repMode + ", acctNumber="
				+ acctNumber + ", bankAccNo=" + bankAccNo + ", ecsAccNo=" + ecsAccNo + ", ecsMicr=" + ecsMicr
				+ ", industryDesc=" + industryDesc + ", promotionDesc=" + promotionDesc + ", assetCategory="
				+ assetCategory + ", channelCode=" + channelCode + ", employeeName=" + employeeName + ", dme=" + dme
				+ ", mktgOfficer=" + mktgOfficer + ", firstSource=" + firstSource + ", finalSource=" + finalSource
				+ ", unFirstSource=" + unFirstSource + ", unFinalSource=" + unFinalSource + ", connectorName="
				+ connectorName + ", lanBk=" + lanBk + ", religion=" + religion + ", profession=" + profession
				+ ", scStFlag=" + scStFlag + ", sex=" + sex + ", maritalStatus=" + maritalStatus + ", qualification="
				+ qualification + ", addressType=" + addressType + ", emailCommunication=" + emailCommunication
				+ ", callCommunication=" + callCommunication + ", ppiAmt=" + ppiAmt + ", applicationDate="
				+ applicationDate + ", uploadDisbursalDate=" + uploadDisbursalDate + ", loanType=" + loanType
				+ ", effRate=" + effRate + ", chargeCode1=" + chargeCode1 + ", chargeAmount1=" + chargeAmount1
				+ ", chargeCode2=" + chargeCode2 + ", chargeAmount2=" + chargeAmount2 + ", annualisedApr="
				+ annualisedApr + ", pslFlag=" + pslFlag + ", pslCode=" + pslCode + ", smeCode=" + smeCode
				+ ", hniCode=" + hniCode + ", ownedIrrigated=" + ownedIrrigated + ", ownedNonIrrigated="
				+ ownedNonIrrigated + ", leasedInIrrigated=" + leasedInIrrigated + ", leasedInNonIrrigated="
				+ leasedInNonIrrigated + ", leasedOutIrrigated=" + leasedOutIrrigated + ", leasedOutNonIrrigated="
				+ leasedOutNonIrrigated + ", disbursalTo=" + disbursalTo + ", crossCollWith=" + crossCollWith
				+ ", umrnNumber=" + umrnNumber + ", npciMandateUpldDate=" + npciMandateUpldDate
				+ ", registrationStatus=" + registrationStatus + ", nachEffectiveDate=" + nachEffectiveDate
				+ ", registrationAmount=" + registrationAmount + ", umrnAcceptanceDate=" + umrnAcceptanceDate
				+ ", rejectCode=" + rejectCode + ", fatherName=" + fatherName + ", spouseName=" + spouseName
				+ ", industry=" + industry + ", loanPurpose=" + loanPurpose + ", marginMoneyCode=" + marginMoneyCode
				+ ", dmaCode=" + dmaCode + ", isRbiDeclarationSelected=" + isRbiDeclarationSelected + ", ecode=" + ecode
				+ ", referralCode=" + referralCode + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", remarks=" + remarks + ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate
				+ ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}
	
	

}
package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
import lombok.Data;
 
@Data
@Entity
@Table(name = "VENDOR_MASTER")
public class VendorMaster {
 
    @Id
    @Column(name = "VENDOR_NO")
    private String vendorNo;
 
    @Column(name = "NAME1")
    private String name1;
 
    @Column(name = "NAME2")
    private String name2;
 
    @Column(name = "SEARCH_ITM")
    private String searchItm;
 
    @Column(name = "STREET_HOUSE")
    private String streetHouse;
 
    @Column(name = "STATUS")
    private String status;
 
    @Column(name = "CREATED_BY")
    private String createdBy;
 
    @Column(name = "CREATED_DATE")
    private Date createdDate;
 
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
 
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
 
    @Column(name = "ACTION_TYPE")
    private String actionType;
 
    @Column(name = "ACTION_USER")
    private String actionUser;
 
    @Column(name = "ACTION_DATE")
    private Date actionDate;
 
    @Column(name = "REMARKS")
    private String remarks;
 
    @Column(name = "UPLOAD_ID")
    private String uploadId;
 
    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "STREET4")
    private String street4;
     
    @Column(name = "STREET5")
    private String street5;
     
    @Column(name = "POST_CODE")
    private String postCode;
     
    @Column(name = "CITY")
    private String city;
     
    @Column(name = "COUNTRY")
    private String country;
     
    @Column(name = "REGION")
    private String region;
     
    @Column(name = "STATE_NAME")
    private String stateName;
     
    @Column(name = "TEL_NO")
    private String telNo;
     
    @Column(name = "MOBILE_NO")
    private String mobileNo;
     
    @Column(name = "FAX")
    private String fax;
     
    @Column(name = "CTR")
    private String ctr;
     
    @Column(name = "BANK_KEY")
    private String bankKey;
     
    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;
     
    @Column(name = "ACCOUNT_HOLDER")
    private String accountHolder;
     
    @Column(name = "CONTROL_KEY")
    private String controlKey;
     
    @Column(name = "BANK_TYPE")
    private String bankType;
     
    @Column(name = "REFERENCE_DETAILS")
    private String referenceDetails;
     
    @Column(name = "REC_AC")
    private String recAc;
     
    @Column(name = "PAYM_METHD")
    private String paymMethd;
     
    @Column(name = "ALTER_PAY")
    private String alterPay;
     
    @Column(name = "PB")
    private String pb;
     
    @Column(name = "HBANK")
    private String hBank;
     
    @Column(name = "EXTRA_TEXT_PAN_NUMBER")
    private String extraTextPanNumber;
     
    @Column(name = "CIN_PAN_NUMBER")
    private String cinPanNumber;
     
    @Column(name = "EXCISE_REG_NUMBER")
    private String exciseRegNumber;
     
    @Column(name = "CENTRAL_SALES_TAX_NUMBER")
    private String centralSalesTaxNumber;
     
    @Column(name = "LOCAL_SALES_TAX_NUMBER")
    private String localSalesTaxNumber;
     
    @Column(name = "SERVICE_TAX_REGIS_NUMBER")
    private String serviceTaxRegisNumber;
     
    @Column(name = "SERVICE_TAX_NO")
    private String serviceTaxNo;
     
    @Column(name = "SALES_TAX_NO")
    private String salesTaxNo;
     
    @Column(name = "NAME3")
    private String name3;
     
    @Column(name = "NAME4")
    private String name4;
     
    @Column(name = "BANK_NAME")
    private String bankName;
     
    @Column(name = "BANK_BRANCH")
    private String bankBranch;
     
    @Column(name = "BRANCH_ADDRESS")
    private String branchAddress;
     
    @Column(name = "TAX_CODE")
    private String taxCode;
     
    @Column(name = "WCT_CODE")
    private String wctCode;
     
    @Column(name = "EMAIL_ADDRESS")
    private String emailAddress;
     
    @Column(name = "OUT_SOURCING_ACTIVITY")
    private String outSourcingActivity;
     
    @Column(name = "VPTS_ID")
    private String vptsId;
     
    @Column(name = "ACTIVITY_NO")
    private String activityNo;
     
    @Column(name = "GST_VENDOR_CLASSIFICATION")
    private String gstVendorClassification;
     
    @Column(name = "GST_VENDOR_CLASSIFICATION_DESC")
    private String gstVendorClassificationDesc;
     
    @Column(name = "TAX_NUMBER_3")
    private String taxNumber3;
     
    @Column(name = "BLACK_LISTING_REASON")
    private String blackListingReason;
     
    @Column(name = "SEARCH_TERM_2")
    private String searchTerm2;
     
    @Column(name = "WITHHOLDING_TAX_TYPE_1")
    private String withholdingTaxType1;
     
    @Column(name = "WITHHOLDING_TAX_CODE_1")
    private String withholdingTaxCode1;
     
    @Column(name = "WITHHOLDING_TAX_TYPE_2")
    private String withholdingTaxType2;
     
    @Column(name = "WITHHOLDING_TAX_CODE_2")
    private String withholdingTaxCode2;
     
    @Column(name = "WITHHOLDING_TAX_TYPE_3")
    private String withholdingTaxType3;
     
    @Column(name = "WITHHOLDING_TAX_CODE_3")
    private String withholdingTaxCode3;
     
    @Column(name = "WITHHOLDING_TAX_TYPE_4")
    private String withholdingTaxType4;
     
    @Column(name = "WITHHOLDING_TAX_CODE_4")
    private String withholdingTaxCode4;
     
    @Column(name = "MSMED_STATUS")
    private String msmedStatus;
     
    @Column(name = "VENDOR_TAGGING")
    private String vendorTagging;
     
    @Column(name = "COMMENTS")
    private String comments;
     
    @Column(name = "VENDOR_BLOCK")
    private String vendorBlock;
     
    @Column(name = "MODIFIED_TIME")
    private String modifiedTime;
     
    @Column(name = "CRED_INFO_NO")
    private String credInfoNo;
     
    @Column(name = "SPECIFIC_PERSON_206AB_206CCA")
    private String specificPerson206ab206cca;
     
    @Column(name = "ADHAAR_PAN_LINKED")
    private String adhaarPanLinked;
     
    @Column(name = "VENDOR_RETURN_FILING")
    private String vendorReturnFiling;
     
    @Column(name = "PO_BOX_NUMBER")
    private String poBoxNumber;
     
    @Column(name = "TAX_NUMBER_1")
    private String taxNumber1;
     
    @Column(name = "DEPARTMENT")
    private String department;
     
    @Column(name = "UDYAM")
    private String udyam;
     
    @Column(name = "EXEMPTION_NUMBER")
    private String exemptionNumber;
     
    @Column(name = "TAX_NUMBER_1_2")
    private String taxNumber12;
     
    @Column(name = "DEPARTMENT_2")
    private String department2;
     
    @Column(name = "CREATE_DATE_STR")
    private String createDateStr;
     
    @Column(name = "LAST_EXT_REVIEW")
    private String lastExtReview;
     
    @Column(name = "EXEMPTION_FROM")
    private String exemptionFrom;
     
    @Column(name = "EXEMPTION_TO")
    private String exemptionTo;
     
    @Column(name = "EXEMPTION_PERCENTAGE")
    private String exemptionPercentage;
     
    @Column(name = "THRESHOLD_AMOUNT_EXEMPTION")
    private String thresholdAmountExemption;
     
    @Column(name = "LOWER_TDS_RATE")
    private String lowerTdsRate;
     
    @Column(name = "STANDARD_RATE_RELATED_PARTY")
    private String standardRateRelatedParty;
    
    @Column(name = "APPROVED_BY")
    private String approvedBy;
     
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getSearchItm() {
		return searchItm;
	}

	public void setSearchItm(String searchItm) {
		this.searchItm = searchItm;
	}

	public String getStreetHouse() {
		return streetHouse;
	}

	public void setStreetHouse(String streetHouse) {
		this.streetHouse = streetHouse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getStreet4() {
		return street4;
	}

	public void setStreet4(String street4) {
		this.street4 = street4;
	}

	public String getStreet5() {
		return street5;
	}

	public void setStreet5(String street5) {
		this.street5 = street5;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getCtr() {
		return ctr;
	}

	public void setCtr(String ctr) {
		this.ctr = ctr;
	}

	public String getBankKey() {
		return bankKey;
	}

	public void setBankKey(String bankKey) {
		this.bankKey = bankKey;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public String getControlKey() {
		return controlKey;
	}

	public void setControlKey(String controlKey) {
		this.controlKey = controlKey;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getReferenceDetails() {
		return referenceDetails;
	}

	public void setReferenceDetails(String referenceDetails) {
		this.referenceDetails = referenceDetails;
	}

	public String getRecAc() {
		return recAc;
	}

	public void setRecAc(String recAc) {
		this.recAc = recAc;
	}

	public String getPaymMethd() {
		return paymMethd;
	}

	public void setPaymMethd(String paymMethd) {
		this.paymMethd = paymMethd;
	}

	public String getAlterPay() {
		return alterPay;
	}

	public void setAlterPay(String alterPay) {
		this.alterPay = alterPay;
	}

	public String getPb() {
		return pb;
	}

	public void setPb(String pb) {
		this.pb = pb;
	}

	public String gethBank() {
		return hBank;
	}

	public void sethBank(String hBank) {
		this.hBank = hBank;
	}

	public String getExtraTextPanNumber() {
		return extraTextPanNumber;
	}

	public void setExtraTextPanNumber(String extraTextPanNumber) {
		this.extraTextPanNumber = extraTextPanNumber;
	}

	public String getCinPanNumber() {
		return cinPanNumber;
	}

	public void setCinPanNumber(String cinPanNumber) {
		this.cinPanNumber = cinPanNumber;
	}

	public String getExciseRegNumber() {
		return exciseRegNumber;
	}

	public void setExciseRegNumber(String exciseRegNumber) {
		this.exciseRegNumber = exciseRegNumber;
	}

	public String getCentralSalesTaxNumber() {
		return centralSalesTaxNumber;
	}

	public void setCentralSalesTaxNumber(String centralSalesTaxNumber) {
		this.centralSalesTaxNumber = centralSalesTaxNumber;
	}

	public String getLocalSalesTaxNumber() {
		return localSalesTaxNumber;
	}

	public void setLocalSalesTaxNumber(String localSalesTaxNumber) {
		this.localSalesTaxNumber = localSalesTaxNumber;
	}

	public String getServiceTaxRegisNumber() {
		return serviceTaxRegisNumber;
	}

	public void setServiceTaxRegisNumber(String serviceTaxRegisNumber) {
		this.serviceTaxRegisNumber = serviceTaxRegisNumber;
	}

	public String getServiceTaxNo() {
		return serviceTaxNo;
	}

	public void setServiceTaxNo(String serviceTaxNo) {
		this.serviceTaxNo = serviceTaxNo;
	}

	public String getSalesTaxNo() {
		return salesTaxNo;
	}

	public void setSalesTaxNo(String salesTaxNo) {
		this.salesTaxNo = salesTaxNo;
	}

	public String getName3() {
		return name3;
	}

	public void setName3(String name3) {
		this.name3 = name3;
	}

	public String getName4() {
		return name4;
	}

	public void setName4(String name4) {
		this.name4 = name4;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getWctCode() {
		return wctCode;
	}

	public void setWctCode(String wctCode) {
		this.wctCode = wctCode;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getOutSourcingActivity() {
		return outSourcingActivity;
	}

	public void setOutSourcingActivity(String outSourcingActivity) {
		this.outSourcingActivity = outSourcingActivity;
	}

	public String getVptsId() {
		return vptsId;
	}

	public void setVptsId(String vptsId) {
		this.vptsId = vptsId;
	}

	public String getActivityNo() {
		return activityNo;
	}

	public void setActivityNo(String activityNo) {
		this.activityNo = activityNo;
	}

	public String getGstVendorClassification() {
		return gstVendorClassification;
	}

	public void setGstVendorClassification(String gstVendorClassification) {
		this.gstVendorClassification = gstVendorClassification;
	}

	public String getGstVendorClassificationDesc() {
		return gstVendorClassificationDesc;
	}

	public void setGstVendorClassificationDesc(String gstVendorClassificationDesc) {
		this.gstVendorClassificationDesc = gstVendorClassificationDesc;
	}

	public String getTaxNumber3() {
		return taxNumber3;
	}

	public void setTaxNumber3(String taxNumber3) {
		this.taxNumber3 = taxNumber3;
	}

	public String getBlackListingReason() {
		return blackListingReason;
	}

	public void setBlackListingReason(String blackListingReason) {
		this.blackListingReason = blackListingReason;
	}

	public String getSearchTerm2() {
		return searchTerm2;
	}

	public void setSearchTerm2(String searchTerm2) {
		this.searchTerm2 = searchTerm2;
	}

	public String getWithholdingTaxType1() {
		return withholdingTaxType1;
	}

	public void setWithholdingTaxType1(String withholdingTaxType1) {
		this.withholdingTaxType1 = withholdingTaxType1;
	}

	public String getWithholdingTaxCode1() {
		return withholdingTaxCode1;
	}

	public void setWithholdingTaxCode1(String withholdingTaxCode1) {
		this.withholdingTaxCode1 = withholdingTaxCode1;
	}

	public String getWithholdingTaxType2() {
		return withholdingTaxType2;
	}

	public void setWithholdingTaxType2(String withholdingTaxType2) {
		this.withholdingTaxType2 = withholdingTaxType2;
	}

	public String getWithholdingTaxCode2() {
		return withholdingTaxCode2;
	}

	public void setWithholdingTaxCode2(String withholdingTaxCode2) {
		this.withholdingTaxCode2 = withholdingTaxCode2;
	}

	public String getWithholdingTaxType3() {
		return withholdingTaxType3;
	}

	public void setWithholdingTaxType3(String withholdingTaxType3) {
		this.withholdingTaxType3 = withholdingTaxType3;
	}

	public String getWithholdingTaxCode3() {
		return withholdingTaxCode3;
	}

	public void setWithholdingTaxCode3(String withholdingTaxCode3) {
		this.withholdingTaxCode3 = withholdingTaxCode3;
	}

	public String getWithholdingTaxType4() {
		return withholdingTaxType4;
	}

	public void setWithholdingTaxType4(String withholdingTaxType4) {
		this.withholdingTaxType4 = withholdingTaxType4;
	}

	public String getWithholdingTaxCode4() {
		return withholdingTaxCode4;
	}

	public void setWithholdingTaxCode4(String withholdingTaxCode4) {
		this.withholdingTaxCode4 = withholdingTaxCode4;
	}

	public String getMsmedStatus() {
		return msmedStatus;
	}

	public void setMsmedStatus(String msmedStatus) {
		this.msmedStatus = msmedStatus;
	}

	public String getVendorTagging() {
		return vendorTagging;
	}

	public void setVendorTagging(String vendorTagging) {
		this.vendorTagging = vendorTagging;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getVendorBlock() {
		return vendorBlock;
	}

	public void setVendorBlock(String vendorBlock) {
		this.vendorBlock = vendorBlock;
	}

	public String getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getCredInfoNo() {
		return credInfoNo;
	}

	public void setCredInfoNo(String credInfoNo) {
		this.credInfoNo = credInfoNo;
	}

	public String getSpecificPerson206ab206cca() {
		return specificPerson206ab206cca;
	}

	public void setSpecificPerson206ab206cca(String specificPerson206ab206cca) {
		this.specificPerson206ab206cca = specificPerson206ab206cca;
	}

	public String getAdhaarPanLinked() {
		return adhaarPanLinked;
	}

	public void setAdhaarPanLinked(String adhaarPanLinked) {
		this.adhaarPanLinked = adhaarPanLinked;
	}

	public String getVendorReturnFiling() {
		return vendorReturnFiling;
	}

	public void setVendorReturnFiling(String vendorReturnFiling) {
		this.vendorReturnFiling = vendorReturnFiling;
	}

	public String getPoBoxNumber() {
		return poBoxNumber;
	}

	public void setPoBoxNumber(String poBoxNumber) {
		this.poBoxNumber = poBoxNumber;
	}

	public String getTaxNumber1() {
		return taxNumber1;
	}

	public void setTaxNumber1(String taxNumber1) {
		this.taxNumber1 = taxNumber1;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUdyam() {
		return udyam;
	}

	public void setUdyam(String udyam) {
		this.udyam = udyam;
	}

	public String getExemptionNumber() {
		return exemptionNumber;
	}

	public void setExemptionNumber(String exemptionNumber) {
		this.exemptionNumber = exemptionNumber;
	}

	public String getTaxNumber12() {
		return taxNumber12;
	}

	public void setTaxNumber12(String taxNumber12) {
		this.taxNumber12 = taxNumber12;
	}

	public String getDepartment2() {
		return department2;
	}

	public void setDepartment2(String department2) {
		this.department2 = department2;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getLastExtReview() {
		return lastExtReview;
	}

	public void setLastExtReview(String lastExtReview) {
		this.lastExtReview = lastExtReview;
	}

	public String getExemptionFrom() {
		return exemptionFrom;
	}

	public void setExemptionFrom(String exemptionFrom) {
		this.exemptionFrom = exemptionFrom;
	}

	public String getExemptionTo() {
		return exemptionTo;
	}

	public void setExemptionTo(String exemptionTo) {
		this.exemptionTo = exemptionTo;
	}

	public String getExemptionPercentage() {
		return exemptionPercentage;
	}

	public void setExemptionPercentage(String exemptionPercentage) {
		this.exemptionPercentage = exemptionPercentage;
	}

	public String getThresholdAmountExemption() {
		return thresholdAmountExemption;
	}

	public void setThresholdAmountExemption(String thresholdAmountExemption) {
		this.thresholdAmountExemption = thresholdAmountExemption;
	}

	public String getLowerTdsRate() {
		return lowerTdsRate;
	}

	public void setLowerTdsRate(String lowerTdsRate) {
		this.lowerTdsRate = lowerTdsRate;
	}

	public String getStandardRateRelatedParty() {
		return standardRateRelatedParty;
	}

	public void setStandardRateRelatedParty(String standardRateRelatedParty) {
		this.standardRateRelatedParty = standardRateRelatedParty;
	}
     
    
    
    }
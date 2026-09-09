package com.icici.dma.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TM_VHL_ILENS_DUMP")
public class IlensDump {

	@Column(name = "S_NO")
	private Long sNo;

	@Id
	@Column(name = "APPLICATION_NUMBER")
	private String applicationNumber;

	@Column(name = "ILENS_ID")
	private Long ilensId;

	@Column(name = "APPLICANT_NAME")
	private String applicantName;

	@Column(name = "APPLICATION_LOGIN_DATE")
	private LocalDateTime applicationLoginDate;

	@Column(name = "DOCKET_LOGIN_DATE")
	private LocalDateTime docketLoginDate;

	@Column(name = "CRM_ID")
	private String crmId;

	@Column(name = "SOURCING_CPC")
	private String sourcingCpc;

	@Column(name = "PROCESSING_CPC")
	private String processingCpc;

	@Column(name = "CITY")
	private String city;

	@Column(name = "DISBURSEMENT_SEQ")
	private Long disbursementSeq;

	@Column(name = "CHANNEL_TYPE")
	private String channelType;

	@Column(name = "SOL_ID")
	private String solId;

	@Column(name = "CHANNEL_NAME_ID")
	private String channelNameId;

	@Column(name = "BROKER_ID")
	private Long brokerId;

	@Column(name = "SUPPLIER_ID")
	private Long supplierId;

	@Column(name = "SALES_EXECUTIVE_NAME_ID")
	private String salesExecutiveNameId;
	
	@Column(name = "CHANNEL_DEALER_SALES_EXECUTIVE")
	private String channelDealerSalesExecutive;
	
	@Column(name = "PROFILE")
	private String profile;

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "DEALER_NAME")
	private String dealerName;

	@Column(name = "SCHEME")
	private String scheme;

	@Column(name = "VARIANT")
	private String variant;

	@Column(name = "SUB_VARIANT")
	private String subVariant;

	@Column(name = "STAGE")
	private String stage;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CUSTOMER_SEGMENT")
	private String customerSegment;

	@Column(name = "BT_FLAG")
	private String btFlag;

	@Column(name = "BT_TYPE")
	private String btType;

	@Column(name = "PSL_FLAG")
	private String pslFlag;

	@Column(name = "RI_NRI")
	private String riNri;

	@Column(name = "LOAN_AMOUNT")
	private BigDecimal loanAmount;

	@Column(name = "RC_NUMBER")
	private String rcNumber;

	@Column(name = "SANCTION_DATE")
	private LocalDateTime sanctionDate;

	@Column(name = "DISBURSEMENT_TYPE")
	private String disbursementType;

	@Column(name = "DISBURSEMENT_DATE")
	private LocalDateTime disbursementDate;

	@Column(name = "PROCESSING_FEE")
	private BigDecimal processingFee;

	@Column(name = "DISBURSEMENT_AMOUNT")
	private BigDecimal disbursementAmount;

	@Column(name = "MODE_OF_DISBURSEMENT")
	private String modeOfDisbursement;

	@Column(name = "COLLATERAL_STATUS")
	private String collateralStatus;

	@Column(name = "ROI")
	private BigDecimal roi;

	@Column(name = "TENOR")
	private Integer tenor;
	
	@Column(name = "MTD_SPILLOVER")
	private String mtdSpillover;

	@Column(name = "CRO_NAME_ID")
	private String croNameId;

	@Column(name = "MAPPED_BSM_NAME_ID")
	private String mappedBsmNameId;

	@Column(name = "MAPPED_BCM_NAME_ID")
	private String mappedBcmNameId;

	@Column(name = "DISBURSEMENT_INTENDED_DATE")
	private LocalDateTime disbursementIntendedDate;

	@Column(name = "CHEQUE_HANDOVER_DATE")
	private LocalDateTime chequeHandoverDate;

	@Column(name = "SUBSEQUENT_FLAG")
	private String subsequentFlag;

	@Column(name = "SUBSEQUENT_RECEIVED_CHANNEL")
	private String subsequentReceivedChannel;

	@Column(name = "FCPG_STATUS")
	private String fcpgStatus;
	
	@Column(name = "FCPG_RECOMMENDATION")
	private String fcpgRecommendation;

	@Column(name = "HUNTER_STATUS")
	private String hunterStatus;

	@Column(name = "VALUATION_AMOUNT")
	private BigDecimal valuationAmount;
	
	@Column(name = "LAN")
	private String lan;

	@Column(name = "AGREEMENT_ID")
	private String agreementId;
	
	@Column(name = "LTV")
	private BigDecimal ltv;

	@Column(name = "QUERY_CLASSIFICATION")
	private String queryClassification;

	@Column(name = "PROPERTY_TYPE")
	private String propertyType;

	@Column(name = "LAST_STAGE")
	private String lastStage;

	@Column(name = "LAST_STATUS")
	private String lastStatus;

	@Column(name = "TYPE_OF_FACILITY")
	private String typeOfFacility;

	@Column(name = "LAST_STAGE_STATUS_TIMESTAMP")
	private LocalDateTime lastStageStatusTimestamp;

	@Column(name = "CHEQUE_HANDOVER_FLAG")
	private String chequeHandoverFlag;

	@Column(name = "HIGHEST_BUREAU_SCORE")
	private Integer highestBureauScore;
	
	@Column(name = "PROCESS_TYPE")
	private String processType;

	@Column(name = "STP_REMARKS")
	private String stpRemarks;
	
	@Column(name = "ADMISSION_STATUS")
	private String admissionStatus;

	@Column(name = "MORATORIUM_STATUS")
	private String moratoriumStatus;

	@Column(name = "MORATORIUM_TYPE")
	private String moratoriumType;

	@Column(name = "ZONE")
	private String zone;

	@Column(name = "STATE")
	private String state;
	
	@Column(name = "TRANCHE_NUMBER  ")
	private Integer trancheNumber;

	@Column(name = "MODE_OF_REPAYMENT")
	private String modeOfRepayment;
	
	@Column(name = "NEO_APPLICATION_NUMBER  ")
	private String neoApplicationNumber;

	@Column(name = "BENEFICIARY_TYPE")
	private String beneficiaryType;

	@Column(name = "TYPE_OF_REMITTANCE")
	private String typeOfRemittance;

	@Column(name = "REASON_FOR_DISBURSEMENT")
	private String reasonForDisbursement;

	@Column(name = "MORATORIUM_IN_MONTHS")
	private String moratoriumInMonths;

	@Column(name = "MODE_OF_KYC")
	private String modeOfKyc;

	@Column(name = "FINAL_CREDIT_DECISION")
	private String finalCreditDecision;

	@Column(name = "FUND_TRANSFER_METHOD")
	private String fundTransferMethod;

	@Column(name = "DISBURSEMENT_STAGE")
	private String disbursementStage;

	@Column(name = "RHS_USER_NAME_ID")
	private String rhsUserNameId;

	//============================================
	
	@Column(name = "CREATED_BY")
	private String createdby;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "FROM_CYCLE_DATE")
	private Date cycleFromDate;

	@Column(name = "TO_CYCLE_DATE")
	private Date cycleToDate;

	@Column(name = "UPLOAD_ID")
	private String uploadId;

	@Column(name = "FILE_NAME")
	private String fileName;
	
	
	//=======================================

	public Long getsNo() {
		return sNo;
	}

	public void setsNo(Long sNo) {
		this.sNo = sNo;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public Long getIlensId() {
		return ilensId;
	}

	public void setIlensId(Long ilensId) {
		this.ilensId = ilensId;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public LocalDateTime getApplicationLoginDate() {
		return applicationLoginDate;
	}

	public void setApplicationLoginDate(LocalDateTime applicationLoginDate) {
		this.applicationLoginDate = applicationLoginDate;
	}

	public LocalDateTime getDocketLoginDate() {
		return docketLoginDate;
	}

	public void setDocketLoginDate(LocalDateTime docketLoginDate) {
		this.docketLoginDate = docketLoginDate;
	}

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String crmId) {
		this.crmId = crmId;
	}

	public String getSourcingCpc() {
		return sourcingCpc;
	}

	public void setSourcingCpc(String sourcingCpc) {
		this.sourcingCpc = sourcingCpc;
	}

	public String getProcessingCpc() {
		return processingCpc;
	}

	public void setProcessingCpc(String processingCpc) {
		this.processingCpc = processingCpc;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getDisbursementSeq() {
		return disbursementSeq;
	}

	public void setDisbursementSeq(Long disbursementSeq) {
		this.disbursementSeq = disbursementSeq;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getSolId() {
		return solId;
	}

	public void setSolId(String solId) {
		this.solId = solId;
	}

	public String getChannelNameId() {
		return channelNameId;
	}

	public void setChannelNameId(String channelNameId) {
		this.channelNameId = channelNameId;
	}

	public Long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(Long brokerId) {
		this.brokerId = brokerId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSalesExecutiveNameId() {
		return salesExecutiveNameId;
	}

	public void setSalesExecutiveNameId(String salesExecutiveNameId) {
		this.salesExecutiveNameId = salesExecutiveNameId;
	}

	public String getChannelDealerSalesExecutive() {
		return channelDealerSalesExecutive;
	}

	public void setChannelDealerSalesExecutive(String channelDealerSalesExecutive) {
		this.channelDealerSalesExecutive = channelDealerSalesExecutive;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getSubVariant() {
		return subVariant;
	}

	public void setSubVariant(String subVariant) {
		this.subVariant = subVariant;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerSegment() {
		return customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getBtFlag() {
		return btFlag;
	}

	public void setBtFlag(String btFlag) {
		this.btFlag = btFlag;
	}

	public String getBtType() {
		return btType;
	}

	public void setBtType(String btType) {
		this.btType = btType;
	}

	public String getPslFlag() {
		return pslFlag;
	}

	public void setPslFlag(String pslFlag) {
		this.pslFlag = pslFlag;
	}

	public String getRiNri() {
		return riNri;
	}

	public void setRiNri(String riNri) {
		this.riNri = riNri;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getRcNumber() {
		return rcNumber;
	}

	public void setRcNumber(String rcNumber) {
		this.rcNumber = rcNumber;
	}

	public LocalDateTime getSanctionDate() {
		return sanctionDate;
	}

	public void setSanctionDate(LocalDateTime sanctionDate) {
		this.sanctionDate = sanctionDate;
	}

	public String getDisbursementType() {
		return disbursementType;
	}

	public void setDisbursementType(String disbursementType) {
		this.disbursementType = disbursementType;
	}

	public LocalDateTime getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(LocalDateTime disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public BigDecimal getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(BigDecimal processingFee) {
		this.processingFee = processingFee;
	}

	public BigDecimal getDisbursementAmount() {
		return disbursementAmount;
	}

	public void setDisbursementAmount(BigDecimal disbursementAmount) {
		this.disbursementAmount = disbursementAmount;
	}

	public String getModeOfDisbursement() {
		return modeOfDisbursement;
	}

	public void setModeOfDisbursement(String modeOfDisbursement) {
		this.modeOfDisbursement = modeOfDisbursement;
	}

	public String getCollateralStatus() {
		return collateralStatus;
	}

	public void setCollateralStatus(String collateralStatus) {
		this.collateralStatus = collateralStatus;
	}

	public BigDecimal getRoi() {
		return roi;
	}

	public void setRoi(BigDecimal roi) {
		this.roi = roi;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public String getMtdSpillover() {
		return mtdSpillover;
	}

	public void setMtdSpillover(String mtdSpillover) {
		this.mtdSpillover = mtdSpillover;
	}

	public String getCroNameId() {
		return croNameId;
	}

	public void setCroNameId(String croNameId) {
		this.croNameId = croNameId;
	}

	public String getMappedBsmNameId() {
		return mappedBsmNameId;
	}

	public void setMappedBsmNameId(String mappedBsmNameId) {
		this.mappedBsmNameId = mappedBsmNameId;
	}

	public String getMappedBcmNameId() {
		return mappedBcmNameId;
	}

	public void setMappedBcmNameId(String mappedBcmNameId) {
		this.mappedBcmNameId = mappedBcmNameId;
	}

	public LocalDateTime getDisbursementIntendedDate() {
		return disbursementIntendedDate;
	}

	public void setDisbursementIntendedDate(LocalDateTime disbursementIntendedDate) {
		this.disbursementIntendedDate = disbursementIntendedDate;
	}

	public LocalDateTime getChequeHandoverDate() {
		return chequeHandoverDate;
	}

	public void setChequeHandoverDate(LocalDateTime chequeHandoverDate) {
		this.chequeHandoverDate = chequeHandoverDate;
	}

	public String getSubsequentFlag() {
		return subsequentFlag;
	}

	public void setSubsequentFlag(String subsequentFlag) {
		this.subsequentFlag = subsequentFlag;
	}

	public String getSubsequentReceivedChannel() {
		return subsequentReceivedChannel;
	}

	public void setSubsequentReceivedChannel(String subsequentReceivedChannel) {
		this.subsequentReceivedChannel = subsequentReceivedChannel;
	}

	public String getFcpgStatus() {
		return fcpgStatus;
	}

	public void setFcpgStatus(String fcpgStatus) {
		this.fcpgStatus = fcpgStatus;
	}

	public String getFcpgRecommendation() {
		return fcpgRecommendation;
	}

	public void setFcpgRecommendation(String fcpgRecommendation) {
		this.fcpgRecommendation = fcpgRecommendation;
	}

	public String getHunterStatus() {
		return hunterStatus;
	}

	public void setHunterStatus(String hunterStatus) {
		this.hunterStatus = hunterStatus;
	}

	public BigDecimal getValuationAmount() {
		return valuationAmount;
	}

	public void setValuationAmount(BigDecimal valuationAmount) {
		this.valuationAmount = valuationAmount;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getAgreementId() {
		return agreementId;
	}

	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	public BigDecimal getLtv() {
		return ltv;
	}

	public void setLtv(BigDecimal ltv) {
		this.ltv = ltv;
	}

	public String getQueryClassification() {
		return queryClassification;
	}

	public void setQueryClassification(String queryClassification) {
		this.queryClassification = queryClassification;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getLastStage() {
		return lastStage;
	}

	public void setLastStage(String lastStage) {
		this.lastStage = lastStage;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public String getTypeOfFacility() {
		return typeOfFacility;
	}

	public void setTypeOfFacility(String typeOfFacility) {
		this.typeOfFacility = typeOfFacility;
	}

	public LocalDateTime getLastStageStatusTimestamp() {
		return lastStageStatusTimestamp;
	}

	public void setLastStageStatusTimestamp(LocalDateTime lastStageStatusTimestamp) {
		this.lastStageStatusTimestamp = lastStageStatusTimestamp;
	}

	public String getChequeHandoverFlag() {
		return chequeHandoverFlag;
	}

	public void setChequeHandoverFlag(String chequeHandoverFlag) {
		this.chequeHandoverFlag = chequeHandoverFlag;
	}

	public Integer getHighestBureauScore() {
		return highestBureauScore;
	}

	public void setHighestBureauScore(Integer highestBureauScore) {
		this.highestBureauScore = highestBureauScore;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public String getStpRemarks() {
		return stpRemarks;
	}

	public void setStpRemarks(String stpRemarks) {
		this.stpRemarks = stpRemarks;
	}

	public String getAdmissionStatus() {
		return admissionStatus;
	}

	public void setAdmissionStatus(String admissionStatus) {
		this.admissionStatus = admissionStatus;
	}

	public String getMoratoriumStatus() {
		return moratoriumStatus;
	}

	public void setMoratoriumStatus(String moratoriumStatus) {
		this.moratoriumStatus = moratoriumStatus;
	}

	public String getMoratoriumType() {
		return moratoriumType;
	}

	public void setMoratoriumType(String moratoriumType) {
		this.moratoriumType = moratoriumType;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getTrancheNumber() {
		return trancheNumber;
	}

	public void setTrancheNumber(Integer trancheNumber) {
		this.trancheNumber = trancheNumber;
	}

	public String getModeOfRepayment() {
		return modeOfRepayment;
	}

	public void setModeOfRepayment(String modeOfRepayment) {
		this.modeOfRepayment = modeOfRepayment;
	}

	public String getNeoApplicationNumber() {
		return neoApplicationNumber;
	}

	public void setNeoApplicationNumber(String neoApplicationNumber) {
		this.neoApplicationNumber = neoApplicationNumber;
	}

	public String getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(String beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}

	public String getTypeOfRemittance() {
		return typeOfRemittance;
	}

	public void setTypeOfRemittance(String typeOfRemittance) {
		this.typeOfRemittance = typeOfRemittance;
	}

	public String getReasonForDisbursement() {
		return reasonForDisbursement;
	}

	public void setReasonForDisbursement(String reasonForDisbursement) {
		this.reasonForDisbursement = reasonForDisbursement;
	}

	public String getMoratoriumInMonths() {
		return moratoriumInMonths;
	}

	public void setMoratoriumInMonths(String moratoriumInMonths) {
		this.moratoriumInMonths = moratoriumInMonths;
	}

	public String getModeOfKyc() {
		return modeOfKyc;
	}

	public void setModeOfKyc(String modeOfKyc) {
		this.modeOfKyc = modeOfKyc;
	}

	public String getFinalCreditDecision() {
		return finalCreditDecision;
	}

	public void setFinalCreditDecision(String finalCreditDecision) {
		this.finalCreditDecision = finalCreditDecision;
	}

	public String getFundTransferMethod() {
		return fundTransferMethod;
	}

	public void setFundTransferMethod(String fundTransferMethod) {
		this.fundTransferMethod = fundTransferMethod;
	}

	public String getDisbursementStage() {
		return disbursementStage;
	}

	public void setDisbursementStage(String disbursementStage) {
		this.disbursementStage = disbursementStage;
	}

	public String getRhsUserNameId() {
		return rhsUserNameId;
	}

	public void setRhsUserNameId(String rhsUserNameId) {
		this.rhsUserNameId = rhsUserNameId;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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


	


	
	
}
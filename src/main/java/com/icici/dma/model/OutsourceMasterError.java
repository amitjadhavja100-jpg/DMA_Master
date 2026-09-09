package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TM_VHL_OUTSOURCE_MST_ERROR")
public class OutsourceMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTSOURCE_ERROR_ID_SEQ")
	@SequenceGenerator(name = "OUTSOURCE_ERROR_ID_SEQ", sequenceName = "OUTSOURCE_ERROR_ID_SEQ", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Integer errorId;

	@Column(name = "EMP_CODE")
	private String empCode;

	@Column(name = "VSTSCODE_COUNSLR_SAPCODE")
	private String vSTSCodeCounselorSAPCode;

	@Column(name = "EXECUTIVE_NAME")
	private String executiveName;
	
	@Column(name = "ROW_NUMBER")
	private Integer rowNumber;

	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPLOAD_ID")
	private String uploadId;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public String getvSTSCodeCounselorSAPCode() {
		return vSTSCodeCounselorSAPCode;
	}

	public void setvSTSCodeCounselorSAPCode(String vSTSCodeCounselorSAPCode) {
		this.vSTSCodeCounselorSAPCode = vSTSCodeCounselorSAPCode;
	}

	public String getExecutiveName() {
		return executiveName;
	}

	public void setExecutiveName(String executiveName) {
		this.executiveName = executiveName;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	
//	@Column(name = "SR_NO")
//	private String srNo;

//	@Column(name = "OLD_VSTS_CODE")
//	private String oldVSTSCode;
//	
//	@Column(name = "TOTAL_SALARY")
//	private String totalSalary;
//
//	@Column(name = "TOTAL_INCENTIVE")
//	private String totalIncentive;
//
//	@Column(name = "CONVEYANCE_FOR_THE_MONTH")
//	private String conveyanceforthemonth;
//
//	@Column(name = "ADDITIONAL_COST")
//	private String additionalCost;
//
//	@Column(name = "TOTAL_PAYOUT")
//	private String totalPayout;
//
//	@Column(name = "MAIN_DESIGNATION")
//	private String mainDesignation;
//
//	@Column(name = "DESIGNATION")
//	private String designation;
//
//	@Column(name = "OLD_DESIGNATION")
//	private String oldDesignation;
//
//	@Column(name = "PRIMARY_PRODUCT")
//	private String primaryProduct;
//
//	@Column(name = "AGENCY_NAME")
//	private String agencyName;
//
//	@Column(name = "LOCATION")
//	private String location;
//
//	@Column(name = "OLD_LOCATION")
//	private String oldLocation;
//
//	@Column(name = "ZONE")
//	private String zone;
//
//	@Column(name = "ED_STATE")
//	private String eDState;
//
//	@Column(name = "ED_ZONE")
//	private String eDZone;
//
//	@Column(name = "MIS_STATE")
//	private String mISState;
//
//	@Column(name = "C_STATE")
//	private String cState;
//
//	@Column(name = "MONTH")
//	private String month;
//
//	@Column(name = "REMARK")
//	private String remark;
//
//	@Column(name = "I_BOX")
//	private String iBox;
//
//	@Column(name = "IPROCESS_DESIGNATION")
//	private String iprocessDesignation;
//
//	@Column(name = "DOJ")
//	private String dOJ;
//
//	@Column(name = "TOP_TIER_II")
//	private String topTierII;
//
//	@Column(name = "OLD_TOP_TIER_II")
//	private String oldTopTierII;
//
//	@Column(name = "NO_OF_DAY")
//	private String noOfDay;
//
//	@Column(name = "ARREAR_DAYS")
//	private String arrearDays;
//
//	@Column(name = "BAND")
//	private String band;
//
//	@Column(name = "INVOICE_NO")
//	private String invoiceNo;
//
//	@Column(name = "INVOICE_DATE")
//	private String invoiceDate;
//
//	@Column(name = "INVOICE_AMT")
//	private String invoiceAmt;
//
//	@Column(name = "INVOICE_RECD_ON")
//	private String invoiceRecdOn;
//
//	@Column(name = "RESIGNED_DATE")
//	private String resigneddate;
//
//	@Column(name = "EPF")
//	private String ePF;
//
//	@Column(name = "EESI")
//	private String eESI;
//
//	@Column(name = "ELWF")
//	private String eLWF;
//
//	@Column(name = "ROUNDOFF_EMP_CONTRTO_ESIC")
//	private String roundingOffDueToEmployerContrToESIC;
//
//	@Column(name = "RECOVERY_AMT_RECD")
//	private String recoveryAmtRecd;
//
//	@Column(name = "NOTICE_PAY_DED")
//	private String noticePayDed;
//
//	@Column(name = "TOTAL")
//	private String total;
//
//	@Column(name = "EX_GRATIA_BONUS")
//	private String exGratiaBonus;
//
//	@Column(name = "TOTAL_WITH_INCENTIVE_CON")
//	private String totalWithIncentiveAndCON;
//
//	@Column(name = "SERVICE_CHGS_ON_TOT_COST")
//	private String serviceChgsOnTotalCost;
//
//	@Column(name = "TOTAL_WITH_SC")
//	private String totalWithSC;
//
//	@Column(name = "GST")
//	private String gST;
//
//	@Column(name = "TOTAL_BILL_AMT")
//	private String totalBillAmt;
//
//	@Column(name = "TOTAL_SAL_INCENTIVE_CONV")
//	private String totalSalaryAndIncentiveAndConv;
//
//	@Column(name = "ADDITION_COST")
//	private String additionCost;
//
//	@Column(name = "AS_PER_I_PROCESS_LOCATION")
//	private String asPerIrProcessLocation;
//
//	@Column(name = "AS_PER_I_PROCESS_STATE")
//	private String asPerIProcessState;
//
//	// duplicate in excel
////	private String	EmpCode;
//
//	@Column(name = "LOT_NO")
//	private String lotNo;
//
//	@Column(name = "I_PROCESS_GROSS_SALARY")
//	private String iprocessGrossSalary;
//
//	@Column(name = "SOL_ID")
//	private String solId;
//
//	@Column(name = "MAIL_FROM")
//	private String mailFrom;
//
//	@Column(name = "NO_OF_CASES_NEW")
//	private String noOfCasesNew;
//
//	@Column(name = "NO_OF_CASES_USED")
//	private String noOfCasesUsed;
//
//	@Column(name = "NO_OF_CASES_TOTAL")
//	private String noOfCasesTotal;
//
//	@Column(name = "LOAN_MNS_NEW")
//	private String loanMnsNew;
//
//	@Column(name = "LOAN_MNS_USED")
//	private String loanMnsUsed;
//
//	@Column(name = "LOAN_MNS_TOTAL")
//	private String loanMnsTotal;

	/*
	 * 
	*/

	
	
	
}

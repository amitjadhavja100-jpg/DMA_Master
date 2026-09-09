package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TM_VHL_OUTSOURCE_MST")
public class OutsourceMaster {

	@Id
	@Column(name = "EMP_CODE")
	private String empCode;

	@Column(name = "VSTSCODE_COUNSLR_SAPCODE")
	private String vSTSCodeCounselorSAPCode;

	@Column(name = "EXECUTIVE_NAME")
	private String executiveName;
	
	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private String modifiedDate;

	@Column(name = "ACTION_TYPE")
	private String actionType;

	@Column(name = "ACTION_USER")
	private String actionUser;

	@Column(name = "ACTION_DATE")
	private Date actionDate;

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

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
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
	
	
	
	
	
//	@Column(name = "SR_NO")
//	private Integer srNo;
//	
//	@Column(name = "OLD_VSTS_CODE")
//	private String oldVSTSCode;
//
//	@Column(name = "TOTAL_SALARY")
//	private BigDecimal totalSalary;
//
//	@Column(name = "TOTAL_INCENTIVE")
//	private BigDecimal totalIncentive;
//
//	@Column(name = "CONVEYANCE_FOR_THE_MONTH")
//	private BigDecimal conveyanceforthemonth;
//
//	@Column(name = "ADDITIONAL_COST")
//	private BigDecimal additionalCost;
//
//	@Column(name = "TOTAL_PAYOUT")
//	private BigDecimal totalPayout;
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
//	private LocalDate month;
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
//	private LocalDate dOJ;
//
//	@Column(name = "TOP_TIER_II")
//	private String topTierII;
//
//	@Column(name = "OLD_TOP_TIER_II")
//	private String oldTopTierII;
//
//	@Column(name = "NO_OF_DAY")
//	private Integer noOfDay;
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
//	private LocalDate invoiceDate;
//
//	@Column(name = "INVOICE_AMT")
//	private BigDecimal invoiceAmt;
//
//	@Column(name = "INVOICE_RECD_ON")
//	private LocalDate invoiceRecdOn;
//
//	@Column(name = "RESIGNED_DATE")
//	private LocalDate resigneddate;
//
//	@Column(name = "EPF")
//	private BigDecimal ePF;
//
//	@Column(name = "EESI")
//	private BigDecimal eESI;
//
//	@Column(name = "ELWF")
//	private BigDecimal eLWF;
//
//	@Column(name = "ROUNDOFF_EMP_CONTRTO_ESIC")
//	private BigDecimal roundingOffDueToEmployerContrToESIC;
//
//	@Column(name = "RECOVERY_AMT_RECD")
//	private BigDecimal recoveryAmtRecd;
//
//	@Column(name = "NOTICE_PAY_DED")
//	private BigDecimal noticePayDed;
//
//	@Column(name = "TOTAL")
//	private BigDecimal total;
//
//	@Column(name = "EX_GRATIA_BONUS")
//	private BigDecimal exGratiaBonus;
//
//	@Column(name = "TOTAL_WITH_INCENTIVE_CON")
//	private BigDecimal totalWithIncentiveAndCON;
//
//	@Column(name = "SERVICE_CHGS_ON_TOT_COST")
//	private BigDecimal serviceChgsOnTotalCost;
//
//	@Column(name = "TOTAL_WITH_SC")
//	private BigDecimal totalWithSC;
//
//	@Column(name = "GST")
//	private BigDecimal gST;
//
//	@Column(name = "TOTAL_BILL_AMT")
//	private BigDecimal totalBillAmt;
//
//	@Column(name = "TOTAL_SAL_INCENTIVE_CONV")
//	private BigDecimal totalSalaryAndIncentiveAndConv;
//
//	@Column(name = "ADDITION_COST")
//	private BigDecimal additionCost;
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
//	private Integer lotNo;
//
//	@Column(name = "I_PROCESS_GROSS_SALARY")
//	private Integer iprocessGrossSalary;
//
//	@Column(name = "SOL_ID")
//	private String solId;
//
//	@Column(name = "MAIL_FROM")
//	private String mailFrom;
//
//	@Column(name = "NO_OF_CASES_NEW")
//	private BigDecimal noOfCasesNew;
//
//	@Column(name = "NO_OF_CASES_USED")
//	private BigDecimal noOfCasesUsed;
//
//	@Column(name = "NO_OF_CASES_TOTAL")
//	private BigDecimal noOfCasesTotal;
//
//	@Column(name = "LOAN_MNS_NEW")
//	private BigDecimal loanMnsNew;
//
//	@Column(name = "LOAN_MNS_USED")
//	private BigDecimal loanMnsUsed;
//
//	@Column(name = "LOAN_MNS_TOTAL")
//	private BigDecimal loanMnsTotal;

	
}

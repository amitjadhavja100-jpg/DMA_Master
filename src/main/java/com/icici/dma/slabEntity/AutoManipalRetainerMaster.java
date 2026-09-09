package com.icici.dma.slabEntity;
 
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
 
@Entity
@Table(name = "AUTO_MANIPAL_RETAINER_MASTER")
public class AutoManipalRetainerMaster {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
 
    @Column(name = "SLAB", length = 10)
    private String slab;
 
    @Column(name = "CASE_TYPE", length = 20)
    private String caseType;
 
    @Column(name = "CASES_FROM")
    private Long casesFrom;
 
    @Column(name = "CASES_TO")
    private Long casesTo;
 
    @Column(name = "CURRENT_FIXED_SALARY_PERCENTAGE", precision = 5, scale = 2)
    private BigDecimal currentFixedSalaryPerc;
 
    @Column(name = "CURRENT_INCENTIVE_PERCENTAGE", precision = 5, scale = 2)
    private BigDecimal currentIncentivePerc;
 
    @Column(name = "PER_CASE_AMOUNT", precision = 10, scale = 2)
    private BigDecimal perCaseAmount;
 
    @Column(name = "REMARKS", length = 500)
    private String remarks;
 
    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;
 
    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;
 
    @Column(name = "CHECKED_BY", length = 100)
    private String checkedBy;
 
    @Column(name = "CHECKED_DATE")
    private LocalDateTime checkedDate;
 
    @Column(name = "SEQUENCE")
    private Long sequence;
 
    @Column(name = "STATUS", length = 20)
    private String status = "PENDING";
 
    @Column(name = "CYCLE_FROM_DATE", length = 20)
    private String cycleFromDate;
 
    @Column(name = "CYCLE_TO_DATE", length = 20)
    private String cycleToDate;
 
    // --- JPA Lifecycle Callbacks ---
    @PrePersist
    protected void onCreate() {
        if (this.createdDate == null) {
            this.createdDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }
 
    @PreUpdate
    protected void onUpdate() {
        this.checkedDate = LocalDateTime.now();
    }
 
    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public String getSlab() {
        return slab;
    }
 
    public void setSlab(String slab) {
        this.slab = slab;
    }
 
    public String getCaseType() {
        return caseType;
    }
 
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
 
    public Long getCasesFrom() {
        return casesFrom;
    }
 
    public void setCasesFrom(Long casesFrom) {
        this.casesFrom = casesFrom;
    }
 
    public Long getCasesTo() {
        return casesTo;
    }
 
    public void setCasesTo(Long casesTo) {
        this.casesTo = casesTo;
    }
 
    public BigDecimal getCurrentFixedSalaryPerc() {
        return currentFixedSalaryPerc;
    }
 
    public void setCurrentFixedSalaryPerc(BigDecimal currentFixedSalaryPerc) {
        this.currentFixedSalaryPerc = currentFixedSalaryPerc;
    }
 
    public BigDecimal getCurrentIncentivePerc() {
        return currentIncentivePerc;
    }
 
    public void setCurrentIncentivePerc(BigDecimal currentIncentivePerc) {
        this.currentIncentivePerc = currentIncentivePerc;
    }
 
    public BigDecimal getPerCaseAmount() {
        return perCaseAmount;
    }
 
    public void setPerCaseAmount(BigDecimal perCaseAmount) {
        this.perCaseAmount = perCaseAmount;
    }
 
    public String getRemarks() {
        return remarks;
    }
 
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
 
    public String getCreatedBy() {
        return createdBy;
    }
 
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
 
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
 
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
 
    public String getCheckedBy() {
        return checkedBy;
    }
 
    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }
 
    public LocalDateTime getCheckedDate() {
        return checkedDate;
    }
 
    public void setCheckedDate(LocalDateTime checkedDate) {
        this.checkedDate = checkedDate;
    }
 
    public Long getSequence() {
        return sequence;
    }
 
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }
 
    public String getCycleFromDate() {
        return cycleFromDate;
    }
 
    public void setCycleFromDate(String cycleFromDate) {
        this.cycleFromDate = cycleFromDate;
    }
 
    public String getCycleToDate() {
        return cycleToDate;
    }
 
    public void setCycleToDate(String cycleToDate) {
        this.cycleToDate = cycleToDate;
    }
}
package com.icici.dma.dto;
 
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class RetainerPayloadDto {
 
	private String user;
	private String cycleFromDate;
	private String cycleToDate;
	private List<SlabRowDto> tableData;
	 
	// Getters and Setters for Root Payload
	public String getUser() {
	    return user;
	}
	 
	public void setUser(String user) {
	    this.user = user;
	}
	 
	public List<SlabRowDto> getTableData() {
	    return tableData;
	}
	 
	public void setTableData(List<SlabRowDto> tableData) {
	    this.tableData = tableData;
	}
	
	  // Getters and Setters
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
	
	
	// --- Inner Static DTO for Table Rows ---
	public static class SlabRowDto {
	 
	    @JsonProperty("slab")
	    private String SLAB;
	 
	    @JsonProperty("newCarCasesFrom")
	    private Long newCarCasesFrom;
	 
	    @JsonProperty("newCarCasesTo")
	    private Long newCarCasesTo;
	 
	    @JsonProperty("usedCarCasesFrom")
	    private Long usedCarCasesFrom;
	 
	    @JsonProperty("usedCarCasesTo")
	    private Long usedCarCasesTo;
	 
	    @JsonProperty("Current fixed Salary %")
	    private BigDecimal currentFixedSalaryPerc;
	 
	    @JsonProperty("Current Incentive %")
	    private BigDecimal currentIncentivePerc;
	 
	    @JsonProperty("Per case Amount")
	    private BigDecimal perCaseAmount;
	 
	    @JsonProperty("remark")
	    private String remark;
	 
	    @JsonProperty("status")
	    private String status = "P";
	 
	    // Getters and Setters
	    public String getSLAB() {
	        return SLAB;
	    }
	 
	    public void setSLAB(String sLAB) {
	        SLAB = sLAB;
	    }
	 
	    public Long getNewCarCasesFrom() {
	        return newCarCasesFrom;
	    }
	 
	    public void setNewCarCasesFrom(Long newCarCasesFrom) {
	        this.newCarCasesFrom = newCarCasesFrom;
	    }
	 
	    public Long getNewCarCasesTo() {
	        return newCarCasesTo;
	    }
	 
	    public void setNewCarCasesTo(Long newCarCasesTo) {
	        this.newCarCasesTo = newCarCasesTo;
	    }
	 
	    public Long getUsedCarCasesFrom() {
	        return usedCarCasesFrom;
	    }
	 
	    public void setUsedCarCasesFrom(Long usedCarCasesFrom) {
	        this.usedCarCasesFrom = usedCarCasesFrom;
	    }
	 
	    public Long getUsedCarCasesTo() {
	        return usedCarCasesTo;
	    }
	 
	    public void setUsedCarCasesTo(Long usedCarCasesTo) {
	        this.usedCarCasesTo = usedCarCasesTo;
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
	 
	    public String getRemark() {
	        return remark;
	    }
	 
	    public void setRemark(String remark) {
	        this.remark = remark;
	    }
	 
	    public String getStatus() {
	        return status;
	    }
	 
	    public void setStatus(String status) {
	        this.status = status;
	    }
	}
	 
	 
}
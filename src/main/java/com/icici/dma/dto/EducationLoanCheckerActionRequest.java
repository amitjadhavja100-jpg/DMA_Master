package com.icici.dma.dto;

import java.util.Date;
import java.util.List;

public class EducationLoanCheckerActionRequest {
    private List<Long> tempIds;
    private String action; // "APPROVE" or "REJECT"
    private String checkerId;
    private String rejectionReason;
    private Date cycleFromDate;
 
    // Getters and Setters
    public List<Long> getTempIds() {
        return tempIds;
    }
 
    public void setTempIds(List<Long> tempIds) {
        this.tempIds = tempIds;
    }
 
    public String getAction() {
        return action;
    }
 
    public void setAction(String action) {
        this.action = action;
    }
 
    public String getCheckerId() {
        return checkerId;
    }
 
    public void setCheckerId(String checkerId) {
        this.checkerId = checkerId;
    }
 
    public String getRejectionReason() {
        return rejectionReason;
    }
 
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
 
    public Date getCycleFromDate() {
        return cycleFromDate;
    }
 
    public void setCycleFromDate(Date cycleFromDate) {
        this.cycleFromDate = cycleFromDate;
    }
}
 
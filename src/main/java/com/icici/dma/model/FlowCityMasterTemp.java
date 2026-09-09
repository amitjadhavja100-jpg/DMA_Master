package com.icici.dma.model;

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
@Table(name = "TM_CCR_FLOW_CITY_MASTER_TEMP")
public class FlowCityMasterTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flowCityMasterTempSeq")
    @SequenceGenerator(
            name = "flowCityMasterTempSeq",
            sequenceName = "TM_CCR_FLOW_CITY_MASTER_TEMP_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "CITY_CODE")
    private String cityCode;

    @Column(name = "CITY_NAME")
    private String cityName;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "CAT")
    private String cat;

    @Column(name = "MAIN_BRANCH_FOR_PAYOUT_CALCULATION")
    private String mainBranchForPayoutCalculation;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTION_DATE")
    private Date actionDate;

    @Column(name = "ACTION_USER")
    private String actionUser;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "FILE_NAME")
    private String fileName;

    public FlowCityMasterTemp() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getMainBranchForPayoutCalculation() {
        return mainBranchForPayoutCalculation;
    }

    public void setMainBranchForPayoutCalculation(String mainBranchForPayoutCalculation) {
        this.mainBranchForPayoutCalculation = mainBranchForPayoutCalculation;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getActionUser() {
        return actionUser;
    }

    public void setActionUser(String actionUser) {
        this.actionUser = actionUser;
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
package com.icici.dma.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TM_CCR_FLOW_CITY_MASTER_ERROR")
public class FlowCityMasterError {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flowCityMasterErrorSeq")
    @SequenceGenerator(
            name = "flowCityMasterErrorSeq",
            sequenceName = "TM_CCR_FLOW_CITY_MASTER_ERROR_SEQ",
            allocationSize = 1
    )
    @Column(name = "ERROR_ID")
    private Long errorId;

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

    @Column(name = "ERROR_MSG", length = 1000)
    private String errorMsg;

    @Column(name = "ROW_NUMBER")
    private Integer rowNumber;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    public FlowCityMasterError() {
    }

    public Long getErrorId() {
        return errorId;
    }

    public void setErrorId(Long errorId) {
        this.errorId = errorId;
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
}
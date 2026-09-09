package com.icici.dma.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "TM_CCR_RECOVERY_CITY_MST_ERROR")
public class RecoveryCityMasterError {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recovery_city_error_seq")
    @SequenceGenerator(
        name = "recovery_city_error_seq",
        sequenceName = "RECOVERY_CITY_ERROR_SEQ",
        allocationSize = 1
    )
    @Column(name = "ERROR_ID")
    private Integer errorId;

    @Column(name = "ID")
    private String id;

    @Column(name = "CITY")
    private String city;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "MAIN_BRANCH")
    private String mainBranch;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "EXISTING_CATEGORY")
    private String existingCategory;

    @Column(name = "CATAEGORY_181_360")
    private String cataegory181360;

    @Column(name = "ZONE_CODE")
    private String zoneCode;

    @Column(name = "ERROR_MSG")
    private String errorMsg;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "ROW_NUMBER")
    private Integer rowNumber;

    public RecoveryCityMasterError() {
    }

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getMainBranch() {
		return mainBranch;
	}

	public void setMainBranch(String mainBranch) {
		this.mainBranch = mainBranch;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getExistingCategory() {
		return existingCategory;
	}

	public void setExistingCategory(String existingCategory) {
		this.existingCategory = existingCategory;
	}

	public String getCataegory181360() {
		return cataegory181360;
	}

	public void setCataegory181360(String cataegory181360) {
		this.cataegory181360 = cataegory181360;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

    
}
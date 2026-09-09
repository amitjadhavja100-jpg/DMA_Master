package com.icici.dma.slabEntity.osp;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "TC_OSP_DPD_TEMP")
public class OspConfigDpdTemp {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "osp_dpd_temp_seq")
    @SequenceGenerator(
            name = "osp_dpd_temp_seq",
            sequenceName = "TC_OSP_DPD_TEMP_SEQ",
            allocationSize = 1)
    @Column(name = "TEMP_ID")
    private Long tempId;

    @Column(name = "DPD_ID")
    private Long dpdId;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "DPD_NAME")
    private String dpdName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CHECKER_REMARKS")
    private String checkerRemarks;
    
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

    public Long getTempId() {
        return tempId;
    }

	public Long getDpdId() {
		return dpdId;
	}

	public void setDpdId(Long dpdId) {
		this.dpdId = dpdId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getDpdName() {
		return dpdName;
	}

	public void setDpdName(String dpdName) {
		this.dpdName = dpdName;
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

	public String getCheckerRemarks() {
		return checkerRemarks;
	}

	public void setCheckerRemarks(String checkerRemarks) {
		this.checkerRemarks = checkerRemarks;
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

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

}
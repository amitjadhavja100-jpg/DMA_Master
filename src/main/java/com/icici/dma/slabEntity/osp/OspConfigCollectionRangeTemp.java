package com.icici.dma.slabEntity.osp;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TC_OSP_COLL_RANGE_TEMP")
public class OspConfigCollectionRangeTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "osp_coll_range_temp_seq")
    @SequenceGenerator(
            name = "osp_coll_range_temp_seq",
            sequenceName = "TC_OSP_COLL_RANGE_TEMP_SEQ",
            allocationSize = 1)
    @Column(name = "TEMP_ID")
    private Long tempId;

    @Column(name = "RANGE_ID")
    private Long rangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DPD_ID")
    private OspConfigDpdMaster dpd;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "FROM_AMOUNT")
    private BigDecimal fromAmount;

    @Column(name = "TO_AMOUNT")
    private BigDecimal toAmount;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

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
    
    @Column(name="IS_MAX")
    private String isMax;

    public Long getTempId() {
        return tempId;
    }

	public Long getRangeId() {
		return rangeId;
	}

	public void setRangeId(Long rangeId) {
		this.rangeId = rangeId;
	}

	public OspConfigDpdMaster getDpd() {
		return dpd;
	}

	public void setDpd(OspConfigDpdMaster dpd) {
		this.dpd = dpd;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
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

	public String getIsMax() {
		return isMax;
	}

	public void setIsMax(String isMax) {
		this.isMax = isMax;
	}
    
}
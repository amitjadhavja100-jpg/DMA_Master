package com.icici.dma.slabEntity.osp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TC_OSP_COLL_RANGE_HISTORY")
public class OspConfigCollectionRangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "osp_coll_range_history_seq")
    @SequenceGenerator(
            name = "osp_coll_range_history_seq",
            sequenceName = "TC_OSP_COLL_RANGE_HISTORY_SEQ",
            allocationSize = 1)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "RANGE_ID")
    private Long rangeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DPD_ID")
    private OspConfigDpdMaster dpd;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Column(name = "OLD_FROM_AMOUNT")
    private BigDecimal oldFromAmount;

    @Column(name = "OLD_TO_AMOUNT")
    private BigDecimal oldToAmount;

    @Column(name = "NEW_FROM_AMOUNT")
    private BigDecimal newFromAmount;

    @Column(name = "NEW_TO_AMOUNT")
    private BigDecimal newToAmount;

    @Column(name = "OLD_ORDER_NO")
    private Integer oldOrderNo;

    @Column(name = "NEW_ORDER_NO")
    private Integer newOrderNo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "REMARKS")
    private String remarks;
    
    @Column(name="OLD_IS_MAX")
    private String oldIsMax;

    @Column(name="NEW_IS_MAX")
    private String newIsMax;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
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

    public BigDecimal getOldFromAmount() {
        return oldFromAmount;
    }

    public void setOldFromAmount(BigDecimal oldFromAmount) {
        this.oldFromAmount = oldFromAmount;
    }

    public BigDecimal getOldToAmount() {
        return oldToAmount;
    }

    public void setOldToAmount(BigDecimal oldToAmount) {
        this.oldToAmount = oldToAmount;
    }

    public BigDecimal getNewFromAmount() {
        return newFromAmount;
    }

    public void setNewFromAmount(BigDecimal newFromAmount) {
        this.newFromAmount = newFromAmount;
    }

    public BigDecimal getNewToAmount() {
        return newToAmount;
    }

    public void setNewToAmount(BigDecimal newToAmount) {
        this.newToAmount = newToAmount;
    }

    public Integer getOldOrderNo() {
        return oldOrderNo;
    }

    public void setOldOrderNo(Integer oldOrderNo) {
        this.oldOrderNo = oldOrderNo;
    }

    public Integer getNewOrderNo() {
        return newOrderNo;
    }

    public void setNewOrderNo(Integer newOrderNo) {
        this.newOrderNo = newOrderNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

	public String getOldIsMax() {
		return oldIsMax;
	}

	public void setOldIsMax(String oldIsMax) {
		this.oldIsMax = oldIsMax;
	}

	public String getNewIsMax() {
		return newIsMax;
	}

	public void setNewIsMax(String newIsMax) {
		this.newIsMax = newIsMax;
	}
    
    
}
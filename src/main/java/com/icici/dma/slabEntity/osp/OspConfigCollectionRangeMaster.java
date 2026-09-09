package com.icici.dma.slabEntity.osp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TC_OSP_COLL_RANGE_MST")
public class OspConfigCollectionRangeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "osp_coll_range_mst_seq")
    @SequenceGenerator(
            name = "osp_coll_range_mst_seq",
            sequenceName = "TC_OSP_COLL_RANGE_MST_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DPD_ID")
    private OspConfigDpdMaster dpd;

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

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
    
    @Column(name="IS_MAX")
    private String isMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OspConfigDpdMaster getDpd() {
        return dpd;
    }

    public void setDpd(OspConfigDpdMaster dpd) {
        this.dpd = dpd;
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

	public String getIsMax() {
		return isMax;
	}

	public void setIsMax(String isMax) {
		this.isMax = isMax;
	}
}
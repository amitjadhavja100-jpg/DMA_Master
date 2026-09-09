package com.icici.dma.slabEntity.osp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TS_OSP_RECV_COLL_RANGE_MST")
public class OspCollectionRangeMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "osp_coll_range_seq")
    @SequenceGenerator(
            name = "osp_coll_range_seq",
            sequenceName = "TS_OSP_RECV_COLL_RANGE_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DPD")
    private String dpd;

    @Column(name = "FROM_AMOUNT")
    private BigDecimal fromAmount;

    @Column(name = "TO_AMOUNT")
    private BigDecimal toAmount;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDpd() {
		return dpd;
	}

	public void setDpd(String dpd) {
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

    
}
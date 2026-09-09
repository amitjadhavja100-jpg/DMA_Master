package com.icici.dma.slabEntity.osp;

import java.math.BigDecimal;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="TS_OSP_RECV_PAYOUT_DTL")
public class OspPayoutDetail {

    @Id
    @GeneratedValue(
            strategy=GenerationType.SEQUENCE,
            generator="osp_dtl_seq")
    @SequenceGenerator(
            name="osp_dtl_seq",
            sequenceName="TS_OSP_RECV_PAYOUT_DTL_SEQ",
            allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MASTER_ID")
    @JsonIgnore
    private OspPayoutMaster master;

//    @Column(name="DPD_ID")
//    private Long dpdId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DPD_ID")
    private OspConfigDpdMaster dpd;
    
    @Column(name="FROM_AMOUNT")
    private BigDecimal fromAmount;

    @Column(name="TO_AMOUNT")
    private BigDecimal toAmount;

    @Column(name="INCENTIVE_PERCENT")
    private Double incentivePercent;

    @Column(name="ORDER_NO")
    private Integer orderNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OspPayoutMaster getMaster() {
		return master;
	}

	public void setMaster(OspPayoutMaster master) {
		this.master = master;
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

	public Double getIncentivePercent() {
		return incentivePercent;
	}

	public void setIncentivePercent(Double incentivePercent) {
		this.incentivePercent = incentivePercent;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}
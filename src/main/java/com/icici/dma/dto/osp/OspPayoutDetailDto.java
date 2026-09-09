package com.icici.dma.dto.osp;

import java.math.BigDecimal;

public class OspPayoutDetailDto {

    private Long id;

    private Long dpdId;

    private String dpdName;

    private BigDecimal fromAmount;

    private BigDecimal toAmount;

    private Boolean isMax;

    private Double incentivePercent;

    private Integer orderNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDpdId() {
        return dpdId;
    }

    public void setDpdId(Long dpdId) {
        this.dpdId = dpdId;
    }

    public String getDpdName() {
        return dpdName;
    }

    public void setDpdName(String dpdName) {
        this.dpdName = dpdName;
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

    public Boolean getIsMax() {
		return isMax;
	}

	public void setIsMax(Boolean isMax) {
		this.isMax = isMax;
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
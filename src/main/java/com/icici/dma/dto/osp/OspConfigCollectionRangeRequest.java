package com.icici.dma.dto.osp;

import java.math.BigDecimal;

public class OspConfigCollectionRangeRequest {

    private Long id;

    private Long dpdId;

    private BigDecimal fromAmount;

    private BigDecimal toAmount;
    
    private Boolean isMax;

    private Integer orderNo;

    private String createdBy;

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
}
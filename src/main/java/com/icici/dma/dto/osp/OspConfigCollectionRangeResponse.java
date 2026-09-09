package com.icici.dma.dto.osp;

import java.math.BigDecimal;

public class OspConfigCollectionRangeResponse {

    private Long id;

    private Long dpdId;
    
    private Long rangeId;

    private String dpdName;

    private BigDecimal fromAmount;

    private BigDecimal toAmount;

    private Integer orderNo;

    private String status;
    
    private String actionType;
    
    private Boolean isMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public Long getRangeId() {
		return rangeId;
	}

	public void setRangeId(Long rangeId) {
		this.rangeId = rangeId;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Boolean getIsMax() {
		return isMax;
	}

	public void setIsMax(Boolean isMax) {
		this.isMax = isMax;
	}
}
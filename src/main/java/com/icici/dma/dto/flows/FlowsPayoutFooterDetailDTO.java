package com.icici.dma.dto.flows;

public class FlowsPayoutFooterDetailDTO {
	
	private Long id;

    private Long sourceFooterId;

    private Integer bucketOrderId;

    private String footerType;

    private String footerText;

    private Long performanceId;

    private Double footerValue;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSourceFooterId() {
        return sourceFooterId;
    }

    public void setSourceFooterId(Long sourceFooterId) {
        this.sourceFooterId = sourceFooterId;
    }

    public Integer getBucketOrderId() {
		return bucketOrderId;
	}

	public void setBucketOrderId(Integer bucketOrderId) {
		this.bucketOrderId = bucketOrderId;
	}

	public String getFooterType() {
        return footerType;
    }

    public void setFooterType(String footerType) {
        this.footerType = footerType;
    }


    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }


    public Long getPerformanceId() {
        return performanceId;
    }

    public void setPerformanceId(Long performanceId) {
        this.performanceId = performanceId;
    }


    public Double getFooterValue() {
        return footerValue;
    }

    public void setFooterValue(Double footerValue) {
        this.footerValue = footerValue;
    }
}
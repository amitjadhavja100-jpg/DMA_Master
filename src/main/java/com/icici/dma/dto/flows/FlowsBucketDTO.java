package com.icici.dma.dto.flows;

public class FlowsBucketDTO {

    private Long id;
    
    private Long categoryId;
    
    private Long cityId;
    
    private Long bucketTypeId;

    private String bucketName;
    
    private Integer orderId;

    private String tableType;
    
    private String rangeRequired;

    private String status;

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getBucketTypeId() {
		return bucketTypeId;
	}

	public void setBucketTypeId(Long bucketTypeId) {
		this.bucketTypeId = bucketTypeId;
	}

	public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getTableType() {
        return tableType;
    }

    public String getRangeRequired() {
		return rangeRequired;
	}

	public void setRangeRequired(String rangeRequired) {
		this.rangeRequired = rangeRequired;
	}

	public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

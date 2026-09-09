package com.icici.dma.dto.flows;

public class FlowsBucketTypeDTO {

    private Long id;

    private String bucketTypeName;

    private String status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucketTypeName() {
        return bucketTypeName;
    }

    public void setBucketTypeName(String bucketTypeName) {
        this.bucketTypeName = bucketTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
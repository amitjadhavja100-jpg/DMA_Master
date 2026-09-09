package com.icici.dma.dto.osp;

public class OspConfigDpdRequest {

    private Long id;

    private String dpdName;

    private String createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDpdName() {
        return dpdName;
    }

    public void setDpdName(String dpdName) {
        this.dpdName = dpdName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
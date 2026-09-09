package com.icici.dma.dto.osp;

public class OspConfigDpdResponse {

    private Long id;

    private String dpdName;

    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
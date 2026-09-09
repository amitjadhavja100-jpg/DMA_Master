package com.icici.dma.dto.osp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class OspPayoutCompareDto {

    private Long id;

    private Long parentMasterId;

    private String product;

    private String subProduct;

    private Long dpdId;

    private String dpdName;

    private String status;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer version;

    private String createdBy;

    private String approvedBy;

    private Date createdDate;

    private LocalDateTime approvedDate;

    private String remarks;

    private List<OspPayoutDetailDto> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentMasterId() {
        return parentMasterId;
    }

    public void setParentMasterId(Long parentMasterId) {
        this.parentMasterId = parentMasterId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<OspPayoutDetailDto> getDetails() {
        return details;
    }

    public void setDetails(List<OspPayoutDetailDto> details) {
        this.details = details;
    }
}
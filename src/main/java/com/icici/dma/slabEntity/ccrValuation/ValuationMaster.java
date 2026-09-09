package com.icici.dma.slabEntity.ccrValuation;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "TS_CCR_VALUATION_MST")
public class ValuationMaster {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "valuation_master_seq"
    )
    @SequenceGenerator(
        name = "valuation_master_seq",
        sequenceName = "TS_CCR_VALUATION_MST_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "IBOX_ID")
    private String iboxId;

    @Column(name = "VALUER_NAME")
    private String valuerName;

    @Column(name = "PRODUCT_SUB_TYPE")
    private String productSubType;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIboxId() {
		return iboxId;
	}

	public void setIboxId(String iboxId) {
		this.iboxId = iboxId;
	}

	public String getValuerName() {
		return valuerName;
	}

	public void setValuerName(String valuerName) {
		this.valuerName = valuerName;
	}

	public String getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
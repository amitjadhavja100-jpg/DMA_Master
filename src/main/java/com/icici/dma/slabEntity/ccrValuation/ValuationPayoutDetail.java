package com.icici.dma.slabEntity.ccrValuation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TS_CCR_VALUATION_PAYOUT_DTL")
public class ValuationPayoutDetail {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "valuation_slab_dtl_seq"
    )
    @SequenceGenerator(
        name = "valuation_slab_dtl_seq",
        sequenceName = "TS_CCR_VALUATION_PAYOUT_DTL_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID")
    @JsonBackReference
    private ValuationSlabMaster master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VALUATION_MASTER_ID")
    @JsonIgnore
    private ValuationMaster valuationMaster;

    @Column(name = "RATE_UNDER_GST")
    private BigDecimal rateUnderGst;

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

	public ValuationSlabMaster getMaster() {
		return master;
	}

	public void setMaster(ValuationSlabMaster master) {
		this.master = master;
	}

	public ValuationMaster getValuationMaster() {
		return valuationMaster;
	}

	public void setValuationMaster(ValuationMaster valuationMaster) {
		this.valuationMaster = valuationMaster;
	}

	public BigDecimal getRateUnderGst() {
		return rateUnderGst;
	}

	public void setRateUnderGst(BigDecimal rateUnderGst) {
		this.rateUnderGst = rateUnderGst;
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
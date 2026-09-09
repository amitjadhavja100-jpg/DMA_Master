
package com.icici.dma.slabEntity;

import java.math.BigDecimal;

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


@Entity
@Table(name="TS_CCR_SLAB_DETAIL")
public class PayoutDetail {

	/*
	 * @Id @GeneratedValue private Long id;
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slab_detail_seq")
	@SequenceGenerator(
	    name = "slab_detail_seq",
	    sequenceName = "TS_CCR_SLAB_DETAIL_SEQ",
	    allocationSize = 1
	)
	private Long id;

    private String collectionSlab;   // e.g. "8 Lacs"
    private String ceRange;          // e.g. "<=3%"
    private Double payout;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID")   
    @JsonBackReference
    private PayoutMaster master;
    
    @Column(name="COLLECTION_SLAB_FROM")
    private BigDecimal collectionSlabFrom;

    @Column(name="COLLECTION_SLAB_TO")
    private BigDecimal collectionSlabTo;

    @Column(name="CE_RANGE_FROM")
    private BigDecimal ceRangeFrom;

    @Column(name="CE_RANGE_TO")
    private BigDecimal ceRangeTo;
    

	public BigDecimal getCollectionSlabFrom() {
		return collectionSlabFrom;
	}

	public void setCollectionSlabFrom(BigDecimal collectionSlabFrom) {
		this.collectionSlabFrom = collectionSlabFrom;
	}

	public BigDecimal getCollectionSlabTo() {
		return collectionSlabTo;
	}

	public void setCollectionSlabTo(BigDecimal collectionSlabTo) {
		this.collectionSlabTo = collectionSlabTo;
	}

	public BigDecimal getCeRangeFrom() {
		return ceRangeFrom;
	}

	public void setCeRangeFrom(BigDecimal ceRangeFrom) {
		this.ceRangeFrom = ceRangeFrom;
	}

	public BigDecimal getCeRangeTo() {
		return ceRangeTo;
	}

	public void setCeRangeTo(BigDecimal ceRangeTo) {
		this.ceRangeTo = ceRangeTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCollectionSlab() {
		return collectionSlab;
	}

	public void setCollectionSlab(String collectionSlab) {
		this.collectionSlab = collectionSlab;
	}

	public String getCeRange() {
		return ceRange;
	}

	public void setCeRange(String ceRange) {
		this.ceRange = ceRange;
	}

	public Double getPayout() {
		return payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	public PayoutMaster getMaster() {
		return master;
	}

	public void setMaster(PayoutMaster master) {
		this.master = master;
	}
    
    
}

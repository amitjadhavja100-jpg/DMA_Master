package com.icici.dma.slabEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TS_CCR_SLAB_COLL_MST")
public class CollectionSlabMaster {
	
	@Id
	@GeneratedValue(
	        strategy = GenerationType.SEQUENCE,
	        generator = "SLAB_COLL_SEQ")
	@SequenceGenerator(
	        name = "SLAB_COLL_SEQ",
	        sequenceName = "TS_CCR_SLAB_COLL_SEQ",
	        allocationSize = 1)
	@Column(name = "ID")
	private Long id;
    
    
    private String category;
    private String dpd;
   // private String value;
    @Column(name="ORDER_NO")
    private Integer orderNo;
    
    @Column(name="SLAB_VALUE")
    private String slabValue;   
    
    @Column(name="STATUS")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDpd() {
		return dpd;
	}
	public void setDpd(String dpd) {
		this.dpd = dpd;
	}

	/*
	 * public String getValue() { return value; } public void setValue(String value)
	 * { this.value = value; }      
	 */
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getSlabValue() {
		return slabValue;
	}
	public void setSlabValue(String slabValue) {
		this.slabValue = slabValue;
	}
    
    
}

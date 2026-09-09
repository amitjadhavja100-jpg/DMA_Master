package com.icici.dma.slabEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TS_CCR_SLAB_RANGE_MST")
public class CeRangeMaster {
	
	@Id
	@GeneratedValue(
	        strategy = GenerationType.SEQUENCE,
	        generator = "SLAB_RANGE_SEQ")
	@SequenceGenerator(
	        name = "SLAB_RANGE_SEQ",
	        sequenceName = "TS_CCR_SLAB_RANGE_SEQ",
	        allocationSize = 1)
	@Column(name = "ID")
	private Long id;
	
    private String category;
    private String dpd;
    
    @Column(name="RANGE_VALUE")
    private String rangeValue;
    
    @Column(name="ORDER_NO")
    private Integer orderNo;
    
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
	public String getRangeValue() {
		return rangeValue;
	}
	public void setRangeValue(String rangeValue) {
		this.rangeValue = rangeValue;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
    
    
    
}
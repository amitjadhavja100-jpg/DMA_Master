package com.icici.dma.slabEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * @Entity
 * 
 * @Table(name = "TS_CCR_SLAB_CAT_CITY_MST") public class CategoryCityMaster {
 * 
 * @Id
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 */

@Entity
@Table(name = "TS_CCR_SLAB_CAT_CITY_MST")
public class CategoryCityMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "CAT_CITY_SEQ")
    @SequenceGenerator(
            name = "CAT_CITY_SEQ",
            sequenceName = "TS_CCR_SLAB_CAT_CITY_SEQ",
            allocationSize = 1)
    
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY")
    private String category;
    
    @Column(name = "CITY")
    private String city;
    
    @Column(name="STATUS")
    private String status;
    
    @Column(name="DISPLAY_ORDER")
    private Integer displayOrder;
    
    public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}

}
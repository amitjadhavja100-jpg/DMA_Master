package com.icici.dma.slabEntity.flows;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(
    name = "TS_FLOWS_CITY_MST",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "UK_FLOWS_CATEGORY_CITY",
            columnNames = {"CATEGORY_ID", "CITY_NAME"}
        )
    }
)
public class FlowsCityMaster {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "flows_city_seq"
    )
    @SequenceGenerator(
        name = "flows_city_seq",
        sequenceName = "TS_FLOWS_CITY_MST_SEQ",
        allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY_ID", nullable = false)
    private Long categoryId;

    @Column(name = "CITY_NAME", nullable = false)
    private String cityName;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
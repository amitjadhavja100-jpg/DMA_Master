package com.icici.dma.slabEntity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "AUTO_MANIPAL_SPECIAL_CASES_CHENNAI_AND_PUNE")
public class AutoManipalSpecialCasesEntity {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "ID")
	    private Long id;
	 
	    @Column(name = "BROKER_CODE", length = 20)
	    private String brokerCode;
	 
	    @Column(name = "NAME", length = 200)
	    private String name;
	 
	    @Column(name = "MODE_OF_SOURCING", length = 200)
	    private String modeOfSourcing;
	 
	    @Column(name = "VEHICLE_TYPE", length = 20)
	    private String vehicleType;
	 
	    @Column(name = "STATUS")
	    private String status;
	 
	    @Column(name = "CYCLE_FROM_DATE", length = 20)
	    private String cycleFromDate;
	 
	    @Column(name = "CYCLE_TO_DATE", length = 20)
	    private String cycleToDate;
	 
	  
	    @Column(name = "CREATED_DATE")
	    private LocalDate createdDate;
	 
	  
	    @Column(name = "CREATED_BY")
	    private String createdBy;
	 
	  
	    @Column(name = "CHECKED_BY")
	    private String checkedBy;
	 
	  
	    @Column(name = "CHECKED_DATE")
	    private LocalDate checkedDate;
	 
	    @Column(name = "STATE", length = 50)
	    private String state;
	 
	    @Column(name = "SEQUENCE", length = 50)
	    private Integer sequence;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getBrokerCode() {
			return brokerCode;
		}

		public void setBrokerCode(String brokerCode) {
			this.brokerCode = brokerCode;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getModeOfSourcing() {
			return modeOfSourcing;
		}

		public void setModeOfSourcing(String modeOfSourcing) {
			this.modeOfSourcing = modeOfSourcing;
		}

		public String getVehicleType() {
			return vehicleType;
		}

		public void setVehicleType(String vehicleType) {
			this.vehicleType = vehicleType;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCycleFromDate() {
			return cycleFromDate;
		}

		public void setCycleFromDate(String cycleFromDate) {
			this.cycleFromDate = cycleFromDate;
		}

		public String getCycleToDate() {
			return cycleToDate;
		}

		public void setCycleToDate(String cycleToDate) {
			this.cycleToDate = cycleToDate;
		}

		public LocalDate getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(LocalDate createdDate) {
			this.createdDate = createdDate;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public String getCheckedBy() {
			return checkedBy;
		}

		public void setCheckedBy(String checkedBy) {
			this.checkedBy = checkedBy;
		}

		public LocalDate getCheckedDate() {
			return checkedDate;
		}

		public void setCheckedDate(LocalDate checkedDate) {
			this.checkedDate = checkedDate;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public Integer getSequence() {
			return sequence;
		}

		public void setSequence(Integer sequence) {
			this.sequence = sequence;
		}
	    
	    
	    
	    
}

package com.icici.dma.model;

import java.util.Date;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
import lombok.Data;
 
@Data
@Entity
@Table(name = "VENDOR_MASTER_ERROR")
public class VendorMasterError {
 
    @Id
    @Column(name = "ERROR_ID")
    private Long errorId;
 
    @Column(name = "VENDOR_NO")
    private String vendorNo;
 
    @Column(name = "NAME1")
    private String name1;
 
    @Column(name = "NAME2")
    private String name2;
 
    @Column(name = "SEARCH_ITM")
    private String searchItm;
 
    @Column(name = "STREET_HOUSE")
    private String streetHouse;
 
    @Column(name = "ERROR_MSG")
    private String errorMsg;
 
    @Column(name = "UPLOAD_ID")
    private String uploadId;
 
    @Column(name = "ROW_NUMBER")
    private Long rowNumber;
 
    @Column(name = "CREATED_BY")
    private String createdBy;
 
    @Column(name = "CREATED_DATE")
    private Date createdDate;

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getName1() {
		return name1;
	}

	public void setName1(String name1) {
		this.name1 = name1;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getSearchItm() {
		return searchItm;
	}

	public void setSearchItm(String searchItm) {
		this.searchItm = searchItm;
	}

	public String getStreetHouse() {
		return streetHouse;
	}

	public void setStreetHouse(String streetHouse) {
		this.streetHouse = streetHouse;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public Long getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Long rowNumber) {
		this.rowNumber = rowNumber;
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
    
    
    
}
 
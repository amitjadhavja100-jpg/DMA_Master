package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_CHANNEL_MST_ERROR")
public class ChannelMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_error_seq")
	@SequenceGenerator(name = "channel_error_seq", sequenceName = "ISEQ$$_138434", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Integer errorId;
	
	@Column(name = "APS_CODE")
	private String apsCode;

	@Column(name = "I_BOX_ID")
	private String iBoxId;

	@Column(name = "SUPPLIER_ID")
	private String supplierId;

	@Column(name = "DATES")
	private String dates;

	@Column(name = "SUSPENDED")
	private String suspended;

	@Column(name = "NAME_OF_CHANNEL")
	private String nameOfChannel;

	@Column(name = "TYPE_OF_DSA")
	private String typeOfDsa;

	@Column(name = "PAN_NO")
	private String panNo;

	@Column(name = "YY")
	private String yy;

	@Column(name = "SUPPLIER_ID_1")
	private String supplierId1;

	@Column(name = "SUPPLIER_ID_2")
	private String supplierId2;

	@Column(name = "SUPPLIER_ID_3")
	private String supplierId3;

	@Column(name = "SUPPLIER_ID_4")
	private String supplierId4;

	@Column(name = "SUPPLIER_ID_5")
	private String supplierId5;

	@Column(name = "SUPPLIER_ID_6")
	private String supplierId6;

	@Column(name = "SUPPLIER_ID_7")
	private String supplierId7;

	@Column(name = "SUPPLIER_ID_8")
	private String supplierId8;

	@Column(name = "SUPPLIER_ID_9")
	private String supplierId9;

	@Column(name = "SUPPLIER_ID_10")
	private String supplierId10;

	@Column(name = "SUPPLIER_ID_11")
	private String supplierId11;

	@Column(name = "SUPPLIER_ID_12")
	private String supplierId12;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "MIS_STATE")
	private String misState;

	@Column(name = "C_STATE")
	private String cState;

	@Column(name = "ED_STATE")
	private String edState;

	@Column(name = "ED_ZONE")
	private String edZone;

	@Column(name = "SOURCING")
	private String sourcing;

	@Column(name = "SOURCING_1")
	private String sourcing1;

	@Column(name = "MANUFACTU_NAME")
	private String manufactuName;

	@Column(name = "NEW_MANUFACTU_NAME")
	private String newManufactuName;

	@Column(name = "OLD_I_BOX_ID")
	private String oldIBoxId;

	@Column(name = "RC_LIMIT")
	private String rcLimit;

	@Column(name = "SAP_CODE")
	private String sapCode;

	@Column(name = "ACCOUNT_NO")
	private String accountNo;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "I_BANK_YES_NON_I_BANK_NO")
	private String iBankYesNonIBankNo;
	
	@Column(name = "ERROR_MSG", length = 4000)
	private String errorMsg;
	
	@Column(name = "ROW_NUMBER", length = 10)
	private Integer rowNumber;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "UPLOAD_ID", length = 30)
	private String uploadId;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getApsCode() {
		return apsCode;
	}

	public void setApsCode(String apsCode) {
		this.apsCode = apsCode;
	}

	public String getiBoxId() {
		return iBoxId;
	}

	public void setiBoxId(String iBoxId) {
		this.iBoxId = iBoxId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getSuspended() {
		return suspended;
	}

	public void setSuspended(String suspended) {
		this.suspended = suspended;
	}

	public String getNameOfChannel() {
		return nameOfChannel;
	}

	public void setNameOfChannel(String nameOfChannel) {
		this.nameOfChannel = nameOfChannel;
	}

	public String getTypeOfDsa() {
		return typeOfDsa;
	}

	public void setTypeOfDsa(String typeOfDsa) {
		this.typeOfDsa = typeOfDsa;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getYy() {
		return yy;
	}

	public void setYy(String yy) {
		this.yy = yy;
	}

	public String getSupplierId1() {
		return supplierId1;
	}

	public void setSupplierId1(String supplierId1) {
		this.supplierId1 = supplierId1;
	}

	public String getSupplierId2() {
		return supplierId2;
	}

	public void setSupplierId2(String supplierId2) {
		this.supplierId2 = supplierId2;
	}

	public String getSupplierId3() {
		return supplierId3;
	}

	public void setSupplierId3(String supplierId3) {
		this.supplierId3 = supplierId3;
	}

	public String getSupplierId4() {
		return supplierId4;
	}

	public void setSupplierId4(String supplierId4) {
		this.supplierId4 = supplierId4;
	}

	public String getSupplierId5() {
		return supplierId5;
	}

	public void setSupplierId5(String supplierId5) {
		this.supplierId5 = supplierId5;
	}

	public String getSupplierId6() {
		return supplierId6;
	}

	public void setSupplierId6(String supplierId6) {
		this.supplierId6 = supplierId6;
	}

	public String getSupplierId7() {
		return supplierId7;
	}

	public void setSupplierId7(String supplierId7) {
		this.supplierId7 = supplierId7;
	}

	public String getSupplierId8() {
		return supplierId8;
	}

	public void setSupplierId8(String supplierId8) {
		this.supplierId8 = supplierId8;
	}

	public String getSupplierId9() {
		return supplierId9;
	}

	public void setSupplierId9(String supplierId9) {
		this.supplierId9 = supplierId9;
	}

	public String getSupplierId10() {
		return supplierId10;
	}

	public void setSupplierId10(String supplierId10) {
		this.supplierId10 = supplierId10;
	}

	public String getSupplierId11() {
		return supplierId11;
	}

	public void setSupplierId11(String supplierId11) {
		this.supplierId11 = supplierId11;
	}

	public String getSupplierId12() {
		return supplierId12;
	}

	public void setSupplierId12(String supplierId12) {
		this.supplierId12 = supplierId12;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMisState() {
		return misState;
	}

	public void setMisState(String misState) {
		this.misState = misState;
	}

	public String getcState() {
		return cState;
	}

	public void setcState(String cState) {
		this.cState = cState;
	}

	public String getEdState() {
		return edState;
	}

	public void setEdState(String edState) {
		this.edState = edState;
	}

	public String getEdZone() {
		return edZone;
	}

	public void setEdZone(String edZone) {
		this.edZone = edZone;
	}

	public String getSourcing() {
		return sourcing;
	}

	public void setSourcing(String sourcing) {
		this.sourcing = sourcing;
	}

	public String getSourcing1() {
		return sourcing1;
	}

	public void setSourcing1(String sourcing1) {
		this.sourcing1 = sourcing1;
	}

	public String getManufactuName() {
		return manufactuName;
	}

	public void setManufactuName(String manufactuName) {
		this.manufactuName = manufactuName;
	}

	public String getNewManufactuName() {
		return newManufactuName;
	}

	public void setNewManufactuName(String newManufactuName) {
		this.newManufactuName = newManufactuName;
	}

	public String getOldIBoxId() {
		return oldIBoxId;
	}

	public void setOldIBoxId(String oldIBoxId) {
		this.oldIBoxId = oldIBoxId;
	}

	public String getRcLimit() {
		return rcLimit;
	}

	public void setRcLimit(String rcLimit) {
		this.rcLimit = rcLimit;
	}

	public String getSapCode() {
		return sapCode;
	}

	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getiBankYesNonIBankNo() {
		return iBankYesNonIBankNo;
	}

	public void setiBankYesNonIBankNo(String iBankYesNonIBankNo) {
		this.iBankYesNonIBankNo = iBankYesNonIBankNo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public ChannelMasterError(Integer errorId, String apsCode, String iBoxId, String supplierId, String dates,
			String suspended, String nameOfChannel, String typeOfDsa, String panNo, String yy, String supplierId1,
			String supplierId2, String supplierId3, String supplierId4, String supplierId5, String supplierId6,
			String supplierId7, String supplierId8, String supplierId9, String supplierId10, String supplierId11,
			String supplierId12, String remark, String location, String misState, String cState, String edState,
			String edZone, String sourcing, String sourcing1, String manufactuName, String newManufactuName,
			String oldIBoxId, String rcLimit, String sapCode, String accountNo, String ifscCode, String bankName,
			String iBankYesNonIBankNo, String errorMsg, Integer rowNumber, String createdBy, Date createdDate,
			String uploadId) {
		super();
		this.errorId = errorId;
		this.apsCode = apsCode;
		this.iBoxId = iBoxId;
		this.supplierId = supplierId;
		this.dates = dates;
		this.suspended = suspended;
		this.nameOfChannel = nameOfChannel;
		this.typeOfDsa = typeOfDsa;
		this.panNo = panNo;
		this.yy = yy;
		this.supplierId1 = supplierId1;
		this.supplierId2 = supplierId2;
		this.supplierId3 = supplierId3;
		this.supplierId4 = supplierId4;
		this.supplierId5 = supplierId5;
		this.supplierId6 = supplierId6;
		this.supplierId7 = supplierId7;
		this.supplierId8 = supplierId8;
		this.supplierId9 = supplierId9;
		this.supplierId10 = supplierId10;
		this.supplierId11 = supplierId11;
		this.supplierId12 = supplierId12;
		this.remark = remark;
		this.location = location;
		this.misState = misState;
		this.cState = cState;
		this.edState = edState;
		this.edZone = edZone;
		this.sourcing = sourcing;
		this.sourcing1 = sourcing1;
		this.manufactuName = manufactuName;
		this.newManufactuName = newManufactuName;
		this.oldIBoxId = oldIBoxId;
		this.rcLimit = rcLimit;
		this.sapCode = sapCode;
		this.accountNo = accountNo;
		this.ifscCode = ifscCode;
		this.bankName = bankName;
		this.iBankYesNonIBankNo = iBankYesNonIBankNo;
		this.errorMsg = errorMsg;
		this.rowNumber = rowNumber;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.uploadId = uploadId;
	}

	public ChannelMasterError() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ChannelMasterError [errorId=" + errorId + ", apsCode=" + apsCode + ", iBoxId=" + iBoxId
				+ ", supplierId=" + supplierId + ", dates=" + dates + ", suspended=" + suspended + ", nameOfChannel="
				+ nameOfChannel + ", typeOfDsa=" + typeOfDsa + ", panNo=" + panNo + ", yy=" + yy + ", supplierId1="
				+ supplierId1 + ", supplierId2=" + supplierId2 + ", supplierId3=" + supplierId3 + ", supplierId4="
				+ supplierId4 + ", supplierId5=" + supplierId5 + ", supplierId6=" + supplierId6 + ", supplierId7="
				+ supplierId7 + ", supplierId8=" + supplierId8 + ", supplierId9=" + supplierId9 + ", supplierId10="
				+ supplierId10 + ", supplierId11=" + supplierId11 + ", supplierId12=" + supplierId12 + ", remark="
				+ remark + ", location=" + location + ", misState=" + misState + ", cState=" + cState + ", edState="
				+ edState + ", edZone=" + edZone + ", sourcing=" + sourcing + ", sourcing1=" + sourcing1
				+ ", manufactuName=" + manufactuName + ", newManufactuName=" + newManufactuName + ", oldIBoxId="
				+ oldIBoxId + ", rcLimit=" + rcLimit + ", sapCode=" + sapCode + ", accountNo=" + accountNo
				+ ", ifscCode=" + ifscCode + ", bankName=" + bankName + ", iBankYesNonIBankNo=" + iBankYesNonIBankNo
				+ ", errorMsg=" + errorMsg + ", rowNumber=" + rowNumber + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", uploadId=" + uploadId + "]";
	}

	
	
	
	
	

}

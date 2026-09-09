package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "TM_VHL_CHANNEL_MST_TEMP")
public class ChannelMasterTemp {
	@Id
	@Column(name = "APS_CODE")
	private Integer apsCode;

	@Column(name = "I_BOX_ID")
	private String iBoxId;

	@Column(name = "SUPPLIER_ID")
	private Long supplierId;

	
	@Column(name = "DATES")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dates;

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
	private Long supplierId1;

	@Column(name = "SUPPLIER_ID_2")
	private Long supplierId2;

	@Column(name = "SUPPLIER_ID_3")
	private Long supplierId3;

	@Column(name = "SUPPLIER_ID_4")
	private Long supplierId4;

	@Column(name = "SUPPLIER_ID_5")
	private Long supplierId5;

	@Column(name = "SUPPLIER_ID_6")
	private Long supplierId6;

	@Column(name = "SUPPLIER_ID_7")
	private Long supplierId7;

	@Column(name = "SUPPLIER_ID_8")
	private Long supplierId8;

	@Column(name = "SUPPLIER_ID_9")
	private Long supplierId9;

	@Column(name = "SUPPLIER_ID_10")
	private Long supplierId10;

	@Column(name = "SUPPLIER_ID_11")
	private Long supplierId11;

	@Column(name = "SUPPLIER_ID_12")
	private Long supplierId12;

//	@Column(name = "SUPPLIER_ID_13")
//	private Long supplierId13;

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
	private Long accountNo;

	@Column(name = "IFSC_CODE")
	private String ifscCode;

	@Column(name = "BANK_NAME")
	private String bankName;

	@Column(name = "I_BANK_YES_NON_I_BANK_NO")
	private String iBankYesNonIBankNo;

//	@Column(name = "REMARKS_BK")
//	private String remarksBk;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@Column(name = "REMARK_BY_CHK")
	private String remarkByChk;

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ACTION_TYPE")
	private String actionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE")
	private Date actionDate;

	@Column(name = "ACTION_USER")
	private String actionUser;

	@Column(name="UPLOAD_ID")
	private String uploadId;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public Integer getApsCode() {
		return apsCode;
	}

	public void setApsCode(Integer apsCode) {
		this.apsCode = apsCode;
	}

	public String getiBoxId() {
		return iBoxId;
	}

	public void setiBoxId(String iBoxId) {
		this.iBoxId = iBoxId;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Date getDates() {
		return dates;
	}

	public void setDates(Date dates) {
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

	public Long getSupplierId1() {
		return supplierId1;
	}

	public void setSupplierId1(Long supplierId1) {
		this.supplierId1 = supplierId1;
	}

	public Long getSupplierId2() {
		return supplierId2;
	}

	public void setSupplierId2(Long supplierId2) {
		this.supplierId2 = supplierId2;
	}

	public Long getSupplierId3() {
		return supplierId3;
	}

	public void setSupplierId3(Long supplierId3) {
		this.supplierId3 = supplierId3;
	}

	public Long getSupplierId4() {
		return supplierId4;
	}

	public void setSupplierId4(Long supplierId4) {
		this.supplierId4 = supplierId4;
	}

	public Long getSupplierId5() {
		return supplierId5;
	}

	public void setSupplierId5(Long supplierId5) {
		this.supplierId5 = supplierId5;
	}

	public Long getSupplierId6() {
		return supplierId6;
	}

	public void setSupplierId6(Long supplierId6) {
		this.supplierId6 = supplierId6;
	}

	public Long getSupplierId7() {
		return supplierId7;
	}

	public void setSupplierId7(Long supplierId7) {
		this.supplierId7 = supplierId7;
	}

	public Long getSupplierId8() {
		return supplierId8;
	}

	public void setSupplierId8(Long supplierId8) {
		this.supplierId8 = supplierId8;
	}

	public Long getSupplierId9() {
		return supplierId9;
	}

	public void setSupplierId9(Long supplierId9) {
		this.supplierId9 = supplierId9;
	}

	public Long getSupplierId10() {
		return supplierId10;
	}

	public void setSupplierId10(Long supplierId10) {
		this.supplierId10 = supplierId10;
	}

	public Long getSupplierId11() {
		return supplierId11;
	}

	public void setSupplierId11(Long supplierId11) {
		this.supplierId11 = supplierId11;
	}

	public Long getSupplierId12() {
		return supplierId12;
	}

	public void setSupplierId12(Long supplierId12) {
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

	public Long getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(Long accountNo) {
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

	public String getRemarkByChk() {
		return remarkByChk;
	}

	public void setRemarkByChk(String remarkByChk) {
		this.remarkByChk = remarkByChk;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ChannelMasterTemp(Integer apsCode, String iBoxId, Long supplierId, Date dates, String suspended,
			String nameOfChannel, String typeOfDsa, String panNo, String yy, Long supplierId1, Long supplierId2,
			Long supplierId3, Long supplierId4, Long supplierId5, Long supplierId6, Long supplierId7, Long supplierId8,
			Long supplierId9, Long supplierId10, Long supplierId11, Long supplierId12, String remark, String location,
			String misState, String cState, String edState, String edZone, String sourcing, String sourcing1,
			String manufactuName, String newManufactuName, String oldIBoxId, String rcLimit, String sapCode,
			Long accountNo, String ifscCode, String bankName, String iBankYesNonIBankNo, String createdBy,
			Date createdDate, String modifiedBy, Date modifiedDate, String remarkByChk, String status,
			String actionType, Date actionDate, String actionUser, String uploadId, String fileName) {
		super();
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
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remarkByChk = remarkByChk;
		this.status = status;
		this.actionType = actionType;
		this.actionDate = actionDate;
		this.actionUser = actionUser;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public ChannelMasterTemp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ChannelMasterTemp [apsCode=" + apsCode + ", iBoxId=" + iBoxId + ", supplierId=" + supplierId
				+ ", dates=" + dates + ", suspended=" + suspended + ", nameOfChannel=" + nameOfChannel + ", typeOfDsa="
				+ typeOfDsa + ", panNo=" + panNo + ", yy=" + yy + ", supplierId1=" + supplierId1 + ", supplierId2="
				+ supplierId2 + ", supplierId3=" + supplierId3 + ", supplierId4=" + supplierId4 + ", supplierId5="
				+ supplierId5 + ", supplierId6=" + supplierId6 + ", supplierId7=" + supplierId7 + ", supplierId8="
				+ supplierId8 + ", supplierId9=" + supplierId9 + ", supplierId10=" + supplierId10 + ", supplierId11="
				+ supplierId11 + ", supplierId12=" + supplierId12 + ", remark=" + remark + ", location=" + location
				+ ", misState=" + misState + ", cState=" + cState + ", edState=" + edState + ", edZone=" + edZone
				+ ", sourcing=" + sourcing + ", sourcing1=" + sourcing1 + ", manufactuName=" + manufactuName
				+ ", newManufactuName=" + newManufactuName + ", oldIBoxId=" + oldIBoxId + ", rcLimit=" + rcLimit
				+ ", sapCode=" + sapCode + ", accountNo=" + accountNo + ", ifscCode=" + ifscCode + ", bankName="
				+ bankName + ", iBankYesNonIBankNo=" + iBankYesNonIBankNo + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", remarkByChk=" + remarkByChk + ", status=" + status + ", actionType=" + actionType + ", actionDate="
				+ actionDate + ", actionUser=" + actionUser + ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}

	
	
	
}
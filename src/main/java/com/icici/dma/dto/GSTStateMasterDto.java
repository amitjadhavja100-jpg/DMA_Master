package com.icici.dma.dto;

public class GSTStateMasterDto {

	private Long partnerId;
	private String partnerName;
//	private Long total;
//	private String address1;
//	private String address2;
//	private String address3;
//	private String city;
//	private String pincode;
//	private String state;
	private String gstState;
//	private String oldAddress;
	private String status;

	public Long getPartnerId() { return partnerId; }
	public void setPartnerId(Long partnerId) { this.partnerId = partnerId; }

	public String getPartnerName() { return partnerName; }
	public void setPartnerName(String partnerName) { this.partnerName = partnerName; }

	public String getGstState() { return gstState; }
	public void setGstState(String gstState) { this.gstState = gstState; }


	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}

package com.icici.dma.dto;

public class GstMfMasterDto {

	private Long apsCode;
	private String name;
	private String state;
//	private String location;
//	private String address;
//	private String dd;
//	private String locationMf;

	private String status;

	public Long getApsCode() {
		return apsCode;
	}

	public void setApsCode(Long apsCode) {
		this.apsCode = apsCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

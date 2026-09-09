package com.icici.dma.dto;

public class BrokerRowRequest {
	private String brokerId;
	private String brokerName;
	private String newCar;
	private String usedCar;
	private String attachmentIncentive;

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getNewCar() {
		return newCar;
	}

	public void setNewCar(String newCar) {
		this.newCar = newCar;
	}

	public String getUsedCar() {
		return usedCar;
	}

	public void setUsedCar(String usedCar) {
		this.usedCar = usedCar;
	}

	public String getAttachmentIncentive() {
		return attachmentIncentive;
	}

	public void setAttachmentIncentive(String attachmentIncentive) {
		this.attachmentIncentive = attachmentIncentive;
	}
}

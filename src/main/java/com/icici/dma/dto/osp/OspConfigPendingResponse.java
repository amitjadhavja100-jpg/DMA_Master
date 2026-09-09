package com.icici.dma.dto.osp;

public class OspConfigPendingResponse {

    private Long tempId;
    
    private Long EntityId;

    private String type;

    private String dpdName;

    private String key;

    private String oldValue;

    private String newValue;

    private String actionType;

    private String makerId;

    private String status;

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public Long getEntityId() {
		return EntityId;
	}

	public void setEntityId(Long entityId) {
		EntityId = entityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDpdName() {
		return dpdName;
	}

	public void setDpdName(String dpdName) {
		this.dpdName = dpdName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    

}
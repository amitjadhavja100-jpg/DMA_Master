package com.icici.dma.dto;

import java.util.List;

public class CheckerDecisionReq {

	private List<String> primaryIds;
	private String decision;
	private String remark;
	
	public List<String> getPrimaryIds() {
		return primaryIds;
	}
	public void setPrimaryIds(List<String> primaryIds) {
		this.primaryIds = primaryIds;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CheckerDecisionReq(List<String> primaryIds, String decision, String remark) {
		super();
		this.primaryIds = primaryIds;
		this.decision = decision;
		this.remark = remark;
	}
	public CheckerDecisionReq() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "CheckerDecisionReq [primaryIds=" + primaryIds + ", decision=" + decision + ", remark=" + remark + "]";
	}
	


	
	
}

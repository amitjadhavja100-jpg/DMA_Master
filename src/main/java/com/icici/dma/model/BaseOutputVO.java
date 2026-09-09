package com.icici.dma.model;

public class BaseOutputVO {

	private String status;
	 
	private String message;
 
	private String tokenId;
 
	private String session;
 
	private String errorCode;
 
	private long total_rows_count;
 
	private String base64str;
 
 
	public String getErrorCode() {
 
		return errorCode;
 
	}
 
	public void setErrorCode(String errorCode) {
 
		this.errorCode = errorCode;
 
	}
 
	public String getErrorDescription() {
 
		return errorDescription;
 
	}
 
	public void setErrorDescription(String errorDescription) {
 
		this.errorDescription = errorDescription;
 
	}
 
	public String getIeCode() {
 
		return IeCode;
 
	}
 
	public void setIeCode(String ieCode) {
 
		IeCode = ieCode;
 
	}
 
	private String errorDescription;
 
	private String IeCode;
 
	public String getSession() {
 
		return session;
 
	}
 
	public void setSession(String session) {
 
		this.session = session;
 
	}
 
	public String getStatus() {
 
		return status;
 
	}
 
	public void setStatus(String status) {
 
		this.status = status;
 
	}
 
	public String getMessage() {
 
		return message;
 
	}
 
	public void setMessage(String message) {
 
		this.message = message;
 
	}
 
	public String getTokenId() {
 
		return tokenId;
 
	}
 
	public void setTokenId(String tokenId) {
 
		this.tokenId = tokenId;
 
	}
 
	public long getTotal_rows_count() {
 
		return total_rows_count;
 
	}
 
	public void setTotal_rows_count(long total_rows_count) {
 
		this.total_rows_count = total_rows_count;
 
	}
 
	public String getBase64str() {
 
		return base64str;
 
	}
 
	public void setBase64str(String base64str) {
 
		this.base64str = base64str;
 
	}
 
}

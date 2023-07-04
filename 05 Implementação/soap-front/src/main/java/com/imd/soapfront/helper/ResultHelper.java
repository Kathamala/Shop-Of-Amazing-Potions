package com.imd.soapfront.helper;

public class ResultHelper {
	private boolean status;
	private String message;

	public ResultHelper() {
		this.status = false;
		this.message = "";
	}

	public ResultHelper(boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ResultHelper fromJson(StringBuffer json){
		ResultHelper resultHelper = new ResultHelper();
		resultHelper.setStatus(json.substring(0, json.indexOf(",")).contains("true"));
		resultHelper.setMessage(json.substring(json.indexOf(",") + 12, json.length()-2));
		return resultHelper;
	}
}
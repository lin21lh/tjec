package com.jbf.sys.login.service;

public class resultMsg {
	public boolean success;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String info;

	public resultMsg(boolean isSuccess, String info) {
		this.success = isSuccess;
		this.info = info;
	}

	public String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public resultMsg(boolean isSuccess, String info, String message) {
		this.success = isSuccess;
		this.info = info;
		this.message = message;
	}
}

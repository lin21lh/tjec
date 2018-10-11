package com.jbf.sys.login.service;


public class SendMsgException extends Exception {

	private static final long serialVersionUID = -6450649532005537176L;
	
	private String statusCode = null;

	public SendMsgException(String statusCode) {
		super();
		this.statusCode = statusCode;
		// TODO Auto-generated constructor stub
	}

	public SendMsgException(String message, Throwable cause, String statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
		// TODO Auto-generated constructor stub
	}

	public SendMsgException(String message, String statusCode) {
		super(message);
		this.statusCode = statusCode;
		// TODO Auto-generated constructor stub
	}

	public SendMsgException(Throwable cause, String statusCode) {
		super(cause);
		this.statusCode = statusCode;
		// TODO Auto-generated constructor stub
	}

	public String getStatusCode() {
		return statusCode;
	}

}

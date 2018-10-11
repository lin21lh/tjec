package com.freework.base.exception;




public class AppException extends Exception {
	private String code=null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AppException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	public AppException(String message) {
		super(message);
	}
	public AppException() {
		super();
	}
	public AppException(String code,String[]args) {
		
		super(AppErrMessage.getMessage(code,args));
		this.code=code;
	}
	public AppException(String code,String defaultMessage,String[]args) {
		super(AppErrMessage.getMessage(code,defaultMessage,args));
		this.code=code;
	}

	public String getCode() {
		return code;
	}
	


	

	

	

}

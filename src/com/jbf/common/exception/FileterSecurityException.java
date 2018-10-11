/************************************************************
 * 类名：FileterSecurityException.java
 *
 * 类别：异常处理
 * 功能：安全过滤器异常
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.exception;

public class FileterSecurityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -437852744615205479L;
	private View view=null;
	public FileterSecurityException(View view) {
		super();
		this.view=view;
	}
	public FileterSecurityException(String message, Throwable cause,View view) {
		super(message, cause);
		this.view=view;
	}
	public FileterSecurityException(String message,View type) {
		super(message);
		this.view=type;
	}
	public FileterSecurityException(Throwable cause,View type) {
		super(cause);
		this.view=type;
	}
	public View  getView(){
		return view;
	}
}

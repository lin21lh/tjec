/************************************************************
 * 类名：AppRuntimeException.java
 *
 * 类别：异常处理类
 * 功能：运行异常处理
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.exception;

import org.springframework.dao.DataAccessException;

public class AppRuntimeException extends DataAccessException {

	/**
	 * 运行时异常处理
	 * @param msg 错误信息
	 */
	public AppRuntimeException(String msg) {
		super(msg);
	}
	
	/**
	 * 运行时异常处理
	 * @param msg
	 * @param tw
	 */
	public AppRuntimeException(String msg, Throwable tw) {
		super(msg, tw);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}

/************************************************************
 * 类名：AppException.java
 *
 * 类别：通用类
 * 功能：异常处理
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.exception;

import java.util.Locale;

import com.jbf.common.web.JbfContextLoaderListener;

public class AppException extends Exception {

	private static final long serialVersionUID = -3704020984587583951L;

	/**
	 * 获取异常信息
	 * @param code 异常编码
	 */
	public AppException(String code) {
		super(getMessage(code, code, null));
	}

	/**
	 * 获取异常信息
	 * @param code 异常编码
	 * @param args 参数
	 */
	public AppException(String code, String[] args) {
		super(getMessage(code, args));
	}

	/**
	 * 获取异常信息
	 * @param code 异常编码
	 * @param defaultMessage 缺省提示信息
	 * @param args 参数
	 */
	public AppException(String code, String defaultMessage, String[] args) {
		super(getMessage(code, defaultMessage, args));
	}

	public static String getMessage(String code) {
		return getMessage(code, code, null);
	}

	public static String getMessage(String code, String[] args) {
		return getMessage(code, code, args);
	}

	public static String getMessage(String code, String defaultMessage,
			String[] args) {
		String message = JbfContextLoaderListener.applicationContext.getMessage(code, args, defaultMessage, Locale.getDefault());
		return message;
	}

	public AppException(String code, Throwable cause, String[] args) {
		super(getMessage(code, args), cause);

	}
}

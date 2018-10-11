/************************************************************
 * 类名：ResultMsg
 *
 * 类别：vo类
 * 功能：用于在前后台传输业务操作成功标志
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-11  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.web;

import java.util.HashMap;
import java.util.Locale;

import com.jbf.common.exception.AppException;

public class ResultMsg {

	/*
	 * 是否成功
	 */
	boolean success;
	/*
	 * 消息标题
	 */
	String title;
	/*
	 * 消息体
	 */
	HashMap<String, Object> body;

	/**
	 * 构造函数
	 * 
	 * @param succ
	 *            是否成功
	 * @param title
	 *            标题
	 */
	public ResultMsg(boolean succ, String title) {
		this.success = succ;
		this.title = title;
	}

	/**
	 * 构造函数
	 * 
	 * @param succ
	 *            是否成功
	 * @param title
	 *            标题
	 * @param body
	 *            可选消息体
	 */
	public ResultMsg(boolean succ, String title, HashMap<String, Object> body) {
		this.success = succ;
		this.title = title;
		this.body = body;
	}

	/**
	 * 构造函数
	 * 
	 * @param succ
	 *            是否成功
	 * @param code
	 *            编码 - 在properties文件中配置的错误信息编码
	 * @param args
	 *            参数
	 * @throws Exception
	 */
	public ResultMsg(boolean succ, String code, String args[]) {
		this.success = succ;
		this.title = JbfContextLoaderListener.applicationContext.getMessage(
				code, args, "未知的异常编码！", Locale.getDefault());
		this.body = new HashMap<String, Object>();
		this.body.put("ERRORCODE", code);
	}

	/**
	 * 取得成功标志
	 * 
	 * @return 成功标志
	 */
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 取得消息标题
	 * 
	 * @return 消息标题
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 取得消息体
	 * 
	 * @return 消息体
	 */
	public HashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(HashMap<String, Object> body) {
		this.body = body;
	}

	/**
	 * 由Exception 自动构建ResultMsg
	 * 
	 * @param exception
	 *            异常
	 * @param defaultErrMsg
	 *            默认异常信息
	 * @return 构建的ResultMsg
	 */
	public static ResultMsg build(Exception exception, String defaultErrMsg) {
		exception.printStackTrace();
		if (exception instanceof AppException) {
			return new ResultMsg(false, exception.getMessage());
		} else {
			return new ResultMsg(false, defaultErrMsg);
		}
	}

}

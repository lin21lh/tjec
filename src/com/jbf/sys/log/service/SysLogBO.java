/************************************************************
 * 类名：SysLogBO.java
 *
 * 类别：组件
 * 功能：写日志组件
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.service;

import javax.servlet.http.HttpServletRequest;

public interface SysLogBO {

	/**
	 * 写日志
	 * @param request 操作请求
	 * @param opermessage 操作信息
	 * @param opertype 操作类型
	 */
	public void writeLog(HttpServletRequest request, String opermessage, Integer opertype);
	
	/**
	 * 写日志
	 * @param request 操作请求
	 * @param opermessage 操作信息
	 * @param usercode 操作用户编码
	 * @param opertype 操作类型
	 */
	public void writeLog(HttpServletRequest request, String opermessage, String usercode, Integer opertype);
}

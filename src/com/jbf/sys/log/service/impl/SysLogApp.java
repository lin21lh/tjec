/************************************************************
 * 类名：SysLogApp.java
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
package com.jbf.sys.log.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.log.dao.SysLogDao;
import com.jbf.sys.log.service.SysLogBO;

public class SysLogApp {

	static SysLogBO logBO = (SysLogBO) WebContextFactoryUtil.getBean("com.jbf.sys.log.service.impl.SysLogBOImpl");
	SysLogDao logDao;
	
	public static final int LOG_LOGIN = 1; //登录日志
	
	/**
	 * 写入登录日志
	 * @param request
	 * @param opermessage
	 */
	public static void writeLogToLogin(HttpServletRequest request, String opermessage) {
		if (logBO != null) {

			writeLog(request, opermessage, LOG_LOGIN);
		} else {
			System.err.println("SysLogApp日志无法写出");
		}
	}
	
	/**
	 * 写入登录日志
	 * @Title: writeLog 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param opermessage
	 * @param @param opertype 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public static void writeLog(String opermessage, Integer opertype) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 	
		writeLog(request, opermessage, opertype);
	}
	
	/**
	 * 写入日志
	 * @param request HttpServletRequest 请求
	 * @param opermessage 操作信息
	 * @param opertype 操作类型
	 */
	public static void writeLog(HttpServletRequest request, String opermessage, Integer opertype) {
		logBO.writeLog(request, opermessage, opertype);
	}
}

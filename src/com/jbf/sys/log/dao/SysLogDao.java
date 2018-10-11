/************************************************************
 * 类名：SysLogDao.java
 *
 * 类别：DAO接口
 * 功能：日志DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.dao;

import com.jbf.common.dao.IGenericDao;
import com.jbf.sys.log.po.SysLog;

public interface SysLogDao extends IGenericDao<SysLog, Long> {

	/**
	 * 写日志
	 * @param usercode 用户编码
	 * @param ip IP地址
	 * @param opermessage 操作信息
	 * @param useragentinfo 客户端信息
	 * @param opertype 操作类型
	 */
	public void writeLog(String usercode, String ip, String opermessage, String useragentinfo, Integer opertype);
	
	/**
	 * 写日志
	 * @param sysLog 日志PO
	 */
	public void writeLog(SysLog sysLog);
}

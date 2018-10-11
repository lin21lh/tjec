/************************************************************
 * 类名：SysLogDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：日志DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.util.DateUtil;
import com.jbf.sys.log.dao.SysLogDao;
import com.jbf.sys.log.po.SysLog;

@Scope("prototype")
@Repository
public class SysLogDaoImpl extends GenericDao<SysLog, Long> implements SysLogDao {

	@Override
	public void writeLog(String usercode, String ip, String opermessage, String useragentinfo, Integer opertype) {
		
		SysLog sysLog = new SysLog();
		sysLog.setUsercode(usercode);
		sysLog.setIp(ip);
		sysLog.setOpermessage(opermessage);
		sysLog.setUseragentinfo(useragentinfo);
		sysLog.setOpertype(opertype);
		sysLog.setOpertime(DateUtil.getCurrentDateTime());
		
		writeLog(sysLog);
	}
	
	public void writeLog(SysLog sysLog) {
		save(sysLog);
	}
	
	
}

/************************************************************
 * 类名：SysLogServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：日志服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.TableNameConst;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.log.dao.SysLogDao;
import com.jbf.sys.log.po.SysLog;
import com.jbf.sys.log.service.SysLogService;

@Scope("prototype")
@Service
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	SysLogDao logDao;
	
	@Override
	public PaginationSupport query(Integer rows, Integer page, Integer opertype, String starttime, String endtime) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysLog.class);
		if (opertype != null)
			dc.add(Property.forName("opertype").eq(opertype));
		
		if (StringUtil.isNotBlank(starttime))
			dc.add(Property.forName("opertime").ge(starttime));
		
		if (StringUtil.isNotBlank(endtime))
			dc.add(Property.forName("opertime").le(endtime));
		
		dc.addOrder(Order.desc("opertime"));
		return logDao.findByCriteria(dc, rows, page);
	}

	@Override
	public void deleteLog(String logids) {
		
		logDao.deleteBySQL(TableNameConst.SYS_LOG, "logid in(" + logids + ")");
	}
	

}

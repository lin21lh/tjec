/************************************************************
 * 类名：SysExcelImpLogServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：数据导入日志Service实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.excel.dao.SysExcelImpLogDao;
import com.jbf.base.excel.po.SysExcelImpLog;
import com.jbf.base.excel.service.SysExcelImpLogService;
import com.jbf.common.dao.PaginationSupport;

@Scope("prototype")
@Service
public class SysExcelImpLogServiceImpl implements SysExcelImpLogService {
	@Autowired
	SysExcelImpLogDao sysExcelImpLogDao;

	@Override
	public void saveLog(SysExcelImpLog log) {
		sysExcelImpLogDao.save(log);
	}
	
	@Override
	public PaginationSupport queryDataImplLog(Map<String, Object> map) {
	    return sysExcelImpLogDao.queryDataImplLog(map);
	}

}

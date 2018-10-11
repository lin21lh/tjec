/************************************************************
 * 类名：SysExcelImpLogDao
 *
 * 类别：Dao
 * 功能：数据导入日志Dao
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.dao;

import java.util.Map;

import com.jbf.base.excel.po.SysExcelImpLog;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;

public interface SysExcelImpLogDao extends IGenericDao<SysExcelImpLog, Long> {
	/**
	 * 查询导入日志
	 * 
	 * @param param
	 *            查询参数 ，包括rows和page属性
	 * @return
	 */
	public PaginationSupport queryDataImplLog(Map<String, Object> param);

}

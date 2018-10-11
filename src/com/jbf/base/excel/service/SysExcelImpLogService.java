/************************************************************
 * 类名：SysExcelImpLogService
 *
 * 类别：Service
 * 功能：数据导入日志Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service;

import java.util.Map;

import com.jbf.base.excel.po.SysExcelImpLog;
import com.jbf.common.dao.PaginationSupport;

public interface SysExcelImpLogService {

	/**
	 * 保存日志
	 * 
	 * @param log
	 *            日志详情对象
	 */
	public void saveLog(SysExcelImpLog log);

	/**
	 * 查询日志
	 * 
	 * @param map
	 *            参数列表 ,包括 page 和rows
	 * @return 分页后的日志信息
	 */

	public PaginationSupport queryDataImplLog(Map<String, Object> map);

}

/************************************************************
 * 类名：SysLogService.java
 *
 * 类别：Service接口
 * 功能：日志服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.service;

import com.jbf.common.dao.PaginationSupport;

public interface SysLogService {

	/**
	 * 日志查询
	 * @param rows 每次请求数据条数
	 * @param page 当前页
	 * @param opertype 操作类型
	 * @param starttime 起始时间
	 * @param endtime 截止时间
	 * @return 日志列表
	 */
	public PaginationSupport query(Integer rows, Integer page, Integer opertype, String starttime, String endtime);
	
	/**
	 * 日志删除
	 * @param logids 日志ID 格式 'xxx,xxx,xxx,xxx...'
	 */
	public void deleteLog(String logids);
}

/************************************************************
 * 类名：SysAppExceptionService.java
 *
 * 类别：Service接口
 * 功能：自定义异常服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.service;

import com.jbf.common.dao.PaginationSupport;

public interface SysAppExceptionService {

	/**
	 * 自定义异常查询
	 * @param rows 分页条数
	 * @param page 当前页
	 * @return 自定义异常集合
	 */
	PaginationSupport query(Integer rows, Integer page);

}

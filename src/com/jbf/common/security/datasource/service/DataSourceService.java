/************************************************************
 * 类名：DataSourceService.java
 *
 * 类别：Service
 * 功能：数据源服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-18  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.datasource.service;

import java.util.List;
import java.util.Map;

public interface DataSourceService {

	/**
	 * 获取全部数据源
	 * @return
	 */
	public List<Map> getAllDataSource();
	
	/**
	 * 判断是否启用分布式数据源
	 * @return
	 */
	public boolean isEnabledMultiDataSource();
}

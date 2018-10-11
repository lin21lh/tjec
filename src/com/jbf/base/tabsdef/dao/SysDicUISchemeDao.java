/************************************************************
 * 类名：SysDicUISchemeDao.java
 *
 * 类别：DAO接口
 * 功能：界面设计器DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.dao;

import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.common.dao.IGenericDao;

public interface SysDicUISchemeDao extends IGenericDao<SysDicUIScheme, Long> {
	/**
	 * 查询数据表的界面方案
	 * 
	 * @param tablecode
	 *            表名
	 * @return 界面方案详情
	 */
	public SysDicUIScheme getByTablecode(String tablecode);

	/**
	 * 查询数据表的界面方案
	 * 
	 * @param tablecode
	 *            表名
	 * @param schemeid
	 *            界面方案id
	 * @return 界面方案详情
	 */
	public SysDicUIScheme getByTablecode(String tablecode, Long schemeid);

}

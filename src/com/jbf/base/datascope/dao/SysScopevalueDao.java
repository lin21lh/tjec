/************************************************************
 * 类名：SysScopevalueDao.java
 *
 * 类别：DAO接口
 * 功能：数据权限值集DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao;

import java.util.List;

import com.jbf.base.datascope.po.SysScopevalue;
import com.jbf.common.dao.IGenericDao;

public interface SysScopevalueDao extends IGenericDao<SysScopevalue, Long> {

	/**
	 * 查询数据权限值集PO
	 * @param scopeitemid 数据权限条件ID
	 * @return 数据权限值集PO集合
	 */
	public List<SysScopevalue> findScopevalueByScopeitem(Long scopeitemid);
	
	/**
	 * 查询数据权限值集
	 * @param scopeitemid 数据权限条件ID
	 * @return 数据权限值集
	 */
	public List<Long> findScopevaluesByScopeitem(Long scopeitemid);
}

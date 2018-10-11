/************************************************************
 * 类名：SysDatascopeItemDao.java
 *
 * 类别：DAO接口
 * 功能：数据权限条件DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao;

import java.util.List;

import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.common.dao.IGenericDao;

public interface SysDatascopeItemDao extends IGenericDao<SysDatascopeitem, Long> {

	/**
	 * 查询数据权限条件
	 * @param scopesubID 数据权限条件项ID
	 * @return 数据权限条件集合
	 */
	public List<SysDatascopeitem> findDsitemByscopesubID(Long scopesubID);
}

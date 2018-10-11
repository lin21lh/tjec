/************************************************************
 * 类名：SysDatascopeSubDao.java
 *
 * 类别：DAO接口
 * 功能：数据权限条件项DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao;

import java.util.List;

import com.jbf.base.datascope.po.SysDatascopesub;
import com.jbf.common.dao.IGenericDao;

public interface SysDatascopeSubDao extends IGenericDao<SysDatascopesub, Long> {

	/**
	 * 数据权限条件项保存
	 * @param datascopesub 数据权限条件项PO
	 * @return 数据权限条件项ID
	 */
	public Long saveDatascopesub(SysDatascopesub datascopesub);
	
	/**
	 * 查询数据权限条件项
	 * @param scopemainid 数据权限主ID
	 * @return 数据权限条件项集合
	 */
	public List<SysDatascopesub> findscopesubByscopemainID(Long scopemainid);
}

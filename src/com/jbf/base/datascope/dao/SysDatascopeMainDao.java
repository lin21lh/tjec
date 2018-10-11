/************************************************************
 * 类名：SysDatascopeMainDao.java
 *
 * 类别：DAO接口
 * 功能：数据权限主表DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao;

import java.util.List;

import com.jbf.base.datascope.po.SysDatascopemain;
import com.jbf.common.dao.IGenericDao;

public interface SysDatascopeMainDao extends IGenericDao<SysDatascopemain, Long> {

	/**
	 * 数据权限主表保存
	 * @param datascopemain 数据权限主PO
	 * @return 数据权限主ID
	 */
	public Long saveDatascopeMain(SysDatascopemain datascopemain); 
	
	/**
	 * 查询数据权限主表数据
	 * @return 数据权限主数据集合
	 */
	public List findDataScopeMain(); 
}

/************************************************************
 * 类名：SysDicElementViewFilterDao.java
 *
 * 类别：DAO接口
 * 功能：数据项视图过滤条件DAO接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.common.dao.IGenericDao;

public interface SysDicElementViewFilterDao extends IGenericDao<SysDicElementViewFilter, Long> {

	/**
	 * 通过数据项编码查询数据项过滤条件
	 * @param elementcode 数据项编码
	 * @return 数据项过滤条件集合
	 */
	public List<SysDicElementViewFilter> queryByElementcode(String elementcode);
	
	/**
	 * 批量保存数据项过滤条件
	 * @param list 数据项过滤条件集合
	 */
	public void saveBatch(List<SysDicElementViewFilter> list);

	/**
	 * 查询数据项过滤条件
	 * @param dc DetachedCriteria
	 * @return 数据项过滤条件集合
	 */
	public List<SysDicElementViewFilter> getFilters(DetachedCriteria dc);
}

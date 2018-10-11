/************************************************************
 * 类名：SysDicElementViewFilterDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据项视图过滤条件DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.dic.dao.SysDicElementViewFilterDao;
import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysDicElementViewFilterDaoImpl extends GenericDao<SysDicElementViewFilter, Long>
		implements SysDicElementViewFilterDao {

	@Override
	public java.util.List<SysDicElementViewFilter> queryByElementcode(String elementcode) {
	
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElementViewFilter.class);
		dc = dc.add(Property.forName("elementcode").eq(elementcode));
		dc = dc.addOrder(Order.asc("seqno"));
		return (List<SysDicElementViewFilter>) super.findByCriteria(dc);
	}
	
	@Override
	public void saveBatch(List<SysDicElementViewFilter> list) {
		
		for (SysDicElementViewFilter filter : list) {
			save(filter);
		}
	}
	
	@Override
	public List<SysDicElementViewFilter> getFilters(DetachedCriteria dc) {
		
		return (List<SysDicElementViewFilter>) super.findByCriteria(dc);
	}
}

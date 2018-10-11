/************************************************************
 * 类名：SysDatascopeItemDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据权限条件DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.datascope.dao.SysDatascopeItemDao;
import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysDatascopeItemDaoImpl extends GenericDao<SysDatascopeitem, Long> implements SysDatascopeItemDao {

	@Override
	public List<SysDatascopeitem> findDsitemByscopesubID(Long scopesubID) {
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDatascopeitem.class);
		dcCriteria = dcCriteria.add(Property.forName("scopesubid").eq(scopesubID));
		dcCriteria = dcCriteria.addOrder(Order.asc("scopeitemid"));
		
		return (List<SysDatascopeitem>) super.findByCriteria(dcCriteria);
	}

}

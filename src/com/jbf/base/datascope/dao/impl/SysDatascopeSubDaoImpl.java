/************************************************************
 * 类名：SysDatascopeSubDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据权限条件项DAO实现类
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

import com.jbf.base.datascope.dao.SysDatascopeSubDao;
import com.jbf.base.datascope.po.SysDatascopesub;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysDatascopeSubDaoImpl extends GenericDao<SysDatascopesub, Long> implements SysDatascopeSubDao {

	@Override
	public Long saveDatascopesub(SysDatascopesub datascopesub) {
		
		return (Long) super.save(datascopesub);
	}

	@Override
	public List<SysDatascopesub> findscopesubByscopemainID(Long scopemainid) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDatascopesub.class);
		dcCriteria = dcCriteria.add(Property.forName("scopemainid").eq(scopemainid));
		dcCriteria = dcCriteria.addOrder(Order.asc("scopesubid"));
		return (List<SysDatascopesub>) super.findByCriteria(dcCriteria);
	}

}

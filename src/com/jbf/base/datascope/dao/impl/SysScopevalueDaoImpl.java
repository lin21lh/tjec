/************************************************************
 * 类名：SysScopevalueDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据权限值集DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.datascope.dao.SysScopevalueDao;
import com.jbf.base.datascope.po.SysDatascopesub;
import com.jbf.base.datascope.po.SysScopevalue;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysScopevalueDaoImpl extends GenericDao<SysScopevalue, Long> implements SysScopevalueDao {

	public List<SysScopevalue> findScopevalueByScopeitem(Long scopeitemid) {

		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysScopevalue.class);
		dcCriteria = dcCriteria.add(Property.forName("scopeitemid").eq(scopeitemid));
		return (List<SysScopevalue>) super.findByCriteria(dcCriteria);
	}
	
	public List<Long> findScopevaluesByScopeitem(Long scopeitemid) {
		List<SysScopevalue> list = findScopevalueByScopeitem(scopeitemid);
		List<Long> resList = new ArrayList<Long>(list.size());
		for (SysScopevalue sv : list) {
			resList.add(sv.getValueid());
		}
		return resList;
	}
}

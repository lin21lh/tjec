/************************************************************
 * 类名：SysDeptDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：机构DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.cache.CacheKeyName;
import com.jbf.common.cache.CacheProviderManager;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;



@Scope("prototype")
@Repository("jbf.sys.dept.SysDeptDao")
public class SysDeptDaoImpl extends GenericDao<SysDept, Long> implements SysDeptDao {

	@Override
	public List<SysDept> query() {
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDept.class);
		dcCriteria.addOrder(Order.asc("code"));
		return (List<SysDept>) super.findByCriteria(dcCriteria);
	}
	
	public SysDept get(String agencycode) {
		if (StringUtil.isBlank(agencycode))
			return null;
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDept.class);
		dcCriteria.add(Property.forName("code").eq(agencycode));
		
		return ((List<SysDept>) super.findByCriteria(dcCriteria)).get(0);
		
		
		
//		SysDept dept = CacheProviderManager.get(SysDept.class, CacheKeyName.SYS_AGENCY_CACHE, agencycode);
//
//		if (dept == null){
//			loadCache();
//			dept = CacheProviderManager.get(SysDept.class, CacheKeyName.SYS_AGENCY_CACHE , agencycode);
//
//			return dept;
//		} else {
//			return dept;
//		}	
	} 
	
	public void clearCache(){
		CacheProviderManager.clear(CacheKeyName.SYS_AGENCY_CACHE);
	}
	
	public void loadCache(){
		
		List<SysDept> list = list();
		clearCache();
		for (SysDept dept : list) {
			this.setCache(dept);
		}
	}
	
	public void setCache(SysDept dept){
		if (dept == null)
			return ;
		String deptcode = dept.getCode();
		if (StringUtil.isBlank(deptcode))
			return ;
		this.setCache(deptcode, dept);
	}
	
	public void setCache(String deptcode, SysDept dept) {

		if (StringUtil.isBlank(deptcode))
			return;
		
		
		if (deptcode.equals(dept.getCode())) {
			CacheProviderManager.evict(CacheKeyName.SYS_AGENCY_CACHE, deptcode);
			deptcode = dept.getCode();
		}
		CacheProviderManager.set(CacheKeyName.SYS_AGENCY_CACHE, deptcode, dept);
	}
}

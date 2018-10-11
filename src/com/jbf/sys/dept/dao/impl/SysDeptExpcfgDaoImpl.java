/************************************************************
 * 类名：SysDeptExpcfgDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：机构扩展属性配置DAO实现类
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
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.dept.dao.SysDeptExpcfgDao;
import com.jbf.sys.dept.po.SysDeptexpcfg;

@Scope("prototype")
@Repository
public class SysDeptExpcfgDaoImpl extends GenericDao<SysDeptexpcfg, Long>
		implements SysDeptExpcfgDao {
	
	public List<SysDeptexpcfg> findByAgencycat(Long agencycat) {
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDeptexpcfg.class);
		dcCriteria.add(Property.forName("agencycat").eq(agencycat));
		
		return (List<SysDeptexpcfg>) super.findByCriteria(dcCriteria);
	}
}

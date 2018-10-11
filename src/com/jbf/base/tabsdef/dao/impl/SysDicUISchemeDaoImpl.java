/************************************************************
 * 类名：SysDicUISchemeDaoImpl.java
 *
 * 类别：daoimpl
 * 功能：界面设计器daoimpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.tabsdef.dao.SysDicUISchemeDao;
import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysDicUISchemeDaoImpl extends GenericDao<SysDicUIScheme, Long>
		implements SysDicUISchemeDao {

	@Override
	public SysDicUIScheme getByTablecode(String tablecode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicUIScheme.class);
		dc.add(Property.forName("used").eq(Byte.valueOf("1")));
		dc = dc.add(Property.forName("tablecode").eq(tablecode));
		List<SysDicUIScheme> list = (List<SysDicUIScheme>) super
				.findByCriteria(dc);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	public SysDicUIScheme getByTablecode(String tablecode, Long schemeid) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicUIScheme.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		dc = dc.add(Property.forName("schemeid").eq(schemeid));
		List<SysDicUIScheme> list = (List<SysDicUIScheme>) super
				.findByCriteria(dc);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
}

/************************************************************
 * 类名：SysFileManageDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：附件管理DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.filemanage.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.impl.GenericDao;


@Scope("prototype")
@Repository
public class SysFileManageDaoImpl extends GenericDao<SysFileManage, Long> implements SysFileManageDao {

	@Override
	public PaginationSupport query(String keyid, String elementcode, Integer page, Integer rows) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysFileManage.class);
		dcCriteria.add(Property.forName("keyid").eq(keyid));
		dcCriteria.add(Property.forName("elementcode").eq(elementcode));
		
		PaginationSupport ps = super.findByCriteria(dcCriteria, rows, page);
		List<SysFileManage> fileList = ps.getItems();
		List<Map> returnList = new ArrayList<Map>(fileList.size());
		for (SysFileManage fileManage : fileList) {
			Map map = BeanUtils.describe(fileManage);
			map.remove("content");
			returnList.add(map);
		}
		
		ps.setItems(returnList);
		
		return ps;
	}

	public int updateByHql(String hql) {
		Session session = super.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createQuery(hql);
		return query.executeUpdate();
	}
}

/************************************************************
 * 类名：SysDicColumnDaoImpl.java
 *
 * 类别：daoimpl
 * 功能：数据表列daoimpl
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.cache.CacheKeyName;
import com.jbf.common.cache.CacheProviderManager;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Repository
public class SysDicColumnDaoImpl extends GenericDao<SysDicColumn, Long>
		implements SysDicColumnDao {

	@Override
	public PaginationSupport query(String tablecode, String columncode,
			String columnname, Integer pageNumber, Integer pageSize) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		if (StringUtil.isNotBlank(tablecode)) {
			dc = dc.add(Property.forName("tablecode").eq(tablecode));
		}

		if (StringUtil.isNotBlank(columncode)) {
			dc = dc.add(Property.forName("columncode").like(columncode,
					MatchMode.ANYWHERE));
		}

		if (StringUtil.isNotBlank(columnname)) {
			dc = dc.add(Property.forName("columnname").like(columnname,
					MatchMode.ANYWHERE));
		}

		return super.findByCriteria(dc, pageSize, pageNumber);
	}

	@SuppressWarnings("unchecked")
	public List<SysDicColumn> findColumnsByTablecode(String tablecode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode.toUpperCase()));

		return (List<SysDicColumn>) super.findByCriteria(dc);
	}

	@SuppressWarnings({ "rawtypes", "null" })
	public SysDicColumn getColumnBySourceElement(String tableCode,
			String sourceElement) throws AppException {
		String strKey = tableCode + sourceElement;
		SysDicColumn column = CacheProviderManager.get(SysDicColumn.class,
				CacheKeyName.DIC_COLUMN_BY_TABLE_ELEMENT, strKey);
		if (column != null)
			return column;

		List columns = findColumnsByTablecode(tableCode);
		if (columns == null && columns.size() == 0)
			return null;

		SysDicColumn result = null;
		column = null;
		for (int i = 0; i < columns.size(); i++) {
			column = (SysDicColumn) columns.get(i);
			if (column.getSourceelementcode() != null
					&& !column.getSourceelementcode().equals("")
					&& sourceElement.equalsIgnoreCase(column
							.getSourceelementcode()))
				if (result == null)
					result = column;
				else
					throw new AppException("配置错误：一个来源数据元对应到了多个字段");
		}
		CacheProviderManager.set(CacheKeyName.DIC_COLUMN_BY_TABLE_ELEMENT,
				strKey, result);
		return result;
	}

	public SysDicColumn getDiccolumn(String tablecode, String columncode) {
		if (StringUtil.isBlank(tablecode) || StringUtil.isBlank(columncode))
			return null;

		String strKey = tablecode.toLowerCase() + "."
				+ columncode.toLowerCase();
		SysDicColumn column = CacheProviderManager.get(SysDicColumn.class,
				CacheKeyName.TAB_COLUMN_CACHE, strKey);

		if (column == null) {
			loadCache();
			column = CacheProviderManager.get(SysDicColumn.class,
					CacheKeyName.TAB_COLUMN_CACHE, strKey);

			return column;
		} else {
			return column;
		}
	}

	public void clearCache() {
		CacheProviderManager.clear(CacheKeyName.TAB_COLUMN_CACHE);
	}

	public void loadCache() {

		List<SysDicColumn> list = super.list();
		clearCache();
		for (SysDicColumn element : list) {
			this.setCache(element);
		}
	}

	public void setCache(SysDicColumn column) {
		if (column == null)
			return;
		String tablecode = column.getTablecode();
		String columncode = column.getColumncode();
		if (StringUtil.isBlank(tablecode) || StringUtil.isBlank(columncode))
			return;
		this.setCache(tablecode, columncode, column);
	}

	public void setCache(String tablecode, String columncode,
			SysDicColumn column) {

		if (StringUtil.isBlank(tablecode) || StringUtil.isBlank(columncode))
			return;

		String strKey = tablecode.toLowerCase() + "."
				+ columncode.toLowerCase();
		String newStrKey = column.getTablecode().toLowerCase() + "."
				+ column.getColumncode().toLowerCase();
		if (strKey.equals(newStrKey)) {
			CacheProviderManager.evict(CacheKeyName.TAB_COLUMN_CACHE, strKey);
			strKey = newStrKey;
		}
		CacheProviderManager.set(CacheKeyName.TAB_COLUMN_CACHE, strKey, column);
	}
}

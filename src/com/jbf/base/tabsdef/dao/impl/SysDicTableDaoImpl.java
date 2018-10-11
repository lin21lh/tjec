/************************************************************
 * 类名：SysDicTableDaoImpl.java
 *
 * 类别：daoimpl
 * 功能：数据表daoimpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.TableNameConst;
import com.jbf.common.cache.CacheKeyName;
import com.jbf.common.cache.CacheProviderManager;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository("com.base.tabsdef.dao.SysDicTableDao")
public class SysDicTableDaoImpl extends GenericDao<SysDicTable, Long> implements
		SysDicTableDao {

	@SuppressWarnings("rawtypes")
	public List query(String tablename) {
		String sql = "select tableid id,tablename ||'-'||tablecode text from "
				+ TableNameConst.SYS_DICTABLE;

		if (tablename != null && tablename.trim().length() > 0)
			sql += " where tablename like '%" + tablename + "%' ";

		sql += "  order by tablename ";
		return super.findMapBySql(sql);
	}

	public PaginationSupport query(Map<String, Object> params) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysDicTable.class);
		
		//不区分大小写
		Object tablecode =params.get("tablecode");
		if (tablecode != null && !String.valueOf(tablecode).isEmpty()){
			dc = dc.add(Restrictions.sqlRestriction("UPPER(tablecode) like UPPER('" + tablecode + "%')"));
		}

		Object tablename = params.get("tablename");
		if (tablename != null && !String.valueOf(tablename).isEmpty()){
			dc = dc.add(Property.forName("tablename").like(String.valueOf(tablename), MatchMode.ANYWHERE));
		}
		
		Object tabletype = params.get("tabletype");
		if (tabletype != null && !String.valueOf(tabletype).isEmpty()){
			String[] tabletypes = tabletype.toString().split(",");
			Byte[] tts = new Byte[tabletypes.length];
			for (int i=0; i<tabletypes.length; i++) {
				tts[i] = Byte.valueOf(tabletypes[i]);
			}
			dc = dc.add(Property.forName("tabletype").in(tts));
			//dc = dc.add(Restrictions.eq("tabletype", Byte.valueOf(String.valueOf(tabletype))));
		}
		
		//按表类型倒叙和表名正序排列
		dc.addOrder(Order.desc("tabletype"));
		dc.addOrder(Order.asc("tablecode"));
		
		Integer pageNumber = Integer.valueOf(String.valueOf(params.get("page")));
		Integer pageSize = Integer.valueOf(String.valueOf(params.get("rows")));
		return super.findByCriteria(dc, pageSize, pageNumber);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDicTable> searchTables(Map<String, Object> params) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicTable.class);
		
		//不区分大小写
		Object tablecode =params.get("tablecode");
		if (tablecode != null && !String.valueOf(tablecode).isEmpty()){
			dc = dc.add(Restrictions.sqlRestriction("UPPER(tablecode) like UPPER('" + tablecode + "%')"));
		}
		
		Object tablename = params.get("tablename");
		if (tablename != null && !String.valueOf(tablename).isEmpty()){
			dc = dc.add(Property.forName("tablename").like(String.valueOf(tablename), MatchMode.ANYWHERE));
		}
		
		//按表类型倒叙和表名正序排列
		dc.addOrder(Order.desc("tabletype"));
		dc.addOrder(Order.asc("tablecode"));
		
		return (List<SysDicTable>)super.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public SysDicTable getByTablecode(String tablecode) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysDicTable.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		List<SysDicTable> list = (List<SysDicTable>) super.findByCriteria(dc);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public SysDicTable getByElementcode(String elementcode) {

		String sql = "select * from " + TableNameConst.SYS_DICTABLE
				+ " where tablecode=(select tablecode from "
				+ TableNameConst.SYS_DICELEMENT + " where elementcode='"
				+ elementcode + "')";

		List<SysDicTable> list = (List<SysDicTable>) super.findVoBySql(sql,
				SysDicTable.class);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public SysDicTable getDicTable(String tablecode) {
		if (tablecode == null)
			return null;
		SysDicTable dicTable = CacheProviderManager.get(SysDicTable.class,
				CacheKeyName.DIC_TABLE_CACHE, tablecode);

		if (dicTable == null) {
			loadCache();
			dicTable = CacheProviderManager.get(SysDicTable.class,
					CacheKeyName.DIC_TABLE_CACHE, tablecode);
			if (dicTable == null)
				return null;
			else
				return dicTable;
		} else {
			return dicTable;
		}
	}
	
	public void update(SysDicTable dicTable) {
		super.update(dicTable);
		setCache(dicTable);
	}

	public void clearCache() {
		CacheProviderManager.clear(CacheKeyName.DIC_TABLE_CACHE);
	}

	public void loadCache() {

		List<SysDicTable> list = super.list();
		clearCache();
		for (SysDicTable dicTable : list) {
			this.setCache(dicTable);
		}
	}

	public void setCache(SysDicTable dicTable) {
		if (dicTable == null)
			return;
		String tablecode = dicTable.getTablecode();
		if (tablecode == null)
			return;
		this.setCache(tablecode, dicTable);
	}

	public void setCache(String tablecode, SysDicTable dicTable) {
		if (dicTable == null)
			return;
		if (tablecode == null)
			return;

		String newTablecode = dicTable.getTablecode();
		if (tablecode.equals(newTablecode)) {
			CacheProviderManager.evict(CacheKeyName.DIC_TABLE_CACHE, tablecode);
			tablecode = newTablecode;

		}
		CacheProviderManager.set(CacheKeyName.DIC_TABLE_CACHE, tablecode,
				dicTable);
	}
}

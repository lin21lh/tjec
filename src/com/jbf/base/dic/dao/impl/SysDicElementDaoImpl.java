/************************************************************
 * 类名：SysDicElementDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据项DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-08  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.TableNameConst;
import com.jbf.common.cache.CacheKeyName;
import com.jbf.common.cache.CacheProviderManager;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Repository("com.base.dic.dao.SysDicElementDao")
public class SysDicElementDaoImpl extends GenericDao<SysDicElement, Long> implements SysDicElementDao {

	public List<?> queryByElementClass() {
		
		String sql = "select itemid as id, code||'-'||name as text from " + TableNameConst.SYS_DICENUMITEM + " where elementcode='SYS_ELEMENTCLASS'";
		return super.findMapBySql(sql);
	}
	
	public PaginationSupport query(Long elementclass, String elementcode, String elementname, Integer pageNumber, Integer pageSize) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		if (elementclass != null && elementclass != 0)
			dc = dc.add(Property.forName("elementclass").eq(elementclass));
		
		if (StringUtil.isNotBlank(elementcode))
			dc = dc.add(Restrictions.sqlRestriction("UPPER(elementcode) like UPPER('" + elementcode + "%')"));
//			dc = dc.add(Property.forName("elementcode").like(elementcode, MatchMode.START));
		
		if (StringUtil.isNotBlank(elementname))
			dc = dc.add(Property.forName("elementname").like(elementname, MatchMode.ANYWHERE));
		
		dc.addOrder(Order.asc("elementcode"));
		
		return super.findByCriteria(dc, pageSize, pageNumber);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<SysDicElement> searchDicEles(Map<String, Object> params) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		
		//不区分大小写
		Object elementcode =params.get("elementcode");
		if (elementcode != null && !String.valueOf(elementcode).isEmpty()){
			dc = dc.add(Restrictions.sqlRestriction("UPPER(elementcode) like UPPER('" + elementcode + "%')"));
		}
		
		Object elementname = params.get("elementname");
		if (elementname != null && !String.valueOf(elementname).isEmpty()){
			dc = dc.add(Property.forName("elementname").like(String.valueOf(elementname), MatchMode.ANYWHERE));
		}
		
		dc.addOrder(Order.asc("elementcode"));
		
		return (List<SysDicElement>)super.findByCriteria(dc);
	}
	
	@SuppressWarnings("rawtypes")
	public SysDicElement getByElementcode(String elementcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		dc.add(Property.forName("elementcode").eq(elementcode));
		
		List list = super.findByCriteria(dc);
		if (list != null && list.size() > 0)
			return (SysDicElement) list.get(0);
		else
			return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> findDicElementVals(SysDicTable dicTable, String elementcode) {
		
		String sql = "select " + dicTable.getCodecolumn() + " as id, " + dicTable.getCodecolumn() + " ||'-'||" + dicTable.getNamecolumn() + " as text from " + dicTable.getTablecode()
				+ " where elementcode='" + elementcode + "'";

		sql += "  order by " + dicTable.getCodecolumn();
		return (List<Map>) super.findMapBySql(sql);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SysDicElement> findDicElement(List elementid) {
		if (elementid == null)
			return null;
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		dc.add(Property.forName("elementid").in(elementid));
		
		List list= super.findByCriteria(dc);
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map> findDicElementToDataScope() {
		String sql = "select elementcode, elementname, datatype from " + TableNameConst.SYS_DICELEMENT + " where iselements=1 order by elementid";
		return (List<Map>) super.findMapBySql(sql);
	}
	
	public SysDicElement getDicelement(String elementcode) {
		if (elementcode == null)
			return null;
		SysDicElement element = CacheProviderManager.get(SysDicElement.class, CacheKeyName.DIC_ELEMENT_CACHE, elementcode);

		if (element == null){
			loadCache();
			element = CacheProviderManager.get(SysDicElement.class, CacheKeyName.DIC_ELEMENT_CACHE , elementcode);
			if (element == null)
				return null;
			else
				return element;
		}else{
			return element;
		}	
	}
	
	public void clearCache(){
		CacheProviderManager.clear(CacheKeyName.DIC_ELEMENT_CACHE);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void loadCache(){
		
		SysDicElement vo=new SysDicElement();
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		dc.add(Property.forName("status").eq(Byte.valueOf("0")));
		List<SysDicElement> list = (List<SysDicElement>) super.findByCriteria(dc);
		clearCache();
		for (SysDicElement element : list) {
			this.setCache(element);
		}
	}
	
	public void setCache(SysDicElement element){
		if (element == null)
			return ;
		String elementcode = element.getElementcode();
		if (elementcode == null)
			return ;
		this.setCache(elementcode, element);
	}
	
	public void setCache(String elementcode, SysDicElement element) {
		if (element == null)
			return ;
		if (elementcode == null)
			return ;
		
		String newElementcode = element.getElementcode();
		if (elementcode.equals(newElementcode)) {
			CacheProviderManager.evict(CacheKeyName.DIC_ELEMENT_CACHE, elementcode);
			elementcode = newElementcode;

		}
		CacheProviderManager.set(CacheKeyName.DIC_ELEMENT_CACHE, elementcode, element);
	}
}

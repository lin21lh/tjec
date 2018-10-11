/************************************************************
 * 类名：DicElementValSetComponentImpl.java
 *
 * 类别：组件实现类
 * 功能：数据项值集组件实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.component.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.dic.component.DicElementValSetComponent;
import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.cache.CacheKeyName;
import com.jbf.common.cache.CacheProviderManager;
@Scope("prototype")
@Component("base.dic.component.DicElementValSetComponent")
public class DicElementValSetComponentImpl implements DicElementValSetComponent {

	@Autowired
	SysDicTableDao dicTableDao;
	@Autowired
	SysDicElementDao dicDefDao;
	@Autowired
	SysDicElementValSetDao dicElementValSetDao;
	
	public List getElementValList(String elementcode) throws SecurityException, ClassNotFoundException, NoSuchFieldException {
		if (elementcode == null)
			return null;
		List list = (List)CacheProviderManager.get(CacheKeyName.DIC_ELEMENT_VALUE_CACHE, elementcode);

		if (list == null){
			loadCache(elementcode);
			list = (List)CacheProviderManager.get(CacheKeyName.DIC_ELEMENT_VALUE_CACHE , elementcode);
			if (list == null)
				return null;
			else
				return list;
		}else{
			return list;
		}	
	}
	
	public void clearCache(String elementcode){
		CacheProviderManager.evict(CacheKeyName.DIC_ELEMENT_CACHE, elementcode);
	}
	
	public void loadCache(String elementcode) throws SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		clearCache(elementcode);
		setCache(elementcode);
	}
	
	public void setCache(String elementcode) {
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		List list = dicElementValSetDao.query(dicTable.getTablecode(), "1=1");
		setCache(elementcode, list);
	}
	
	public void setCache(String elementcode, List list) {
		if (elementcode == null)
			return ;
		if (list == null)
			return ;

		CacheProviderManager.evict(CacheKeyName.DIC_ELEMENT_VALUE_CACHE, elementcode);
		CacheProviderManager.set(CacheKeyName.DIC_ELEMENT_VALUE_CACHE, elementcode, list);
	}
	
	public String getCodeByID(String elementcode, Long id) {
		try {
			List list = getElementValList(elementcode.toUpperCase());
			SysDicElement dicElement = dicDefDao.getDicelement(elementcode.toUpperCase());
			SysDicTable dicTable = dicTableDao.getDicTable(dicElement.getTablecode());
			for (Object o : list) {
				if (Long.valueOf(getValue(o, dicTable.getKeycolumn())) == id)
					return getValue(o, dicTable.getCodecolumn());
			}
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Long getElementIDByCode(String elementcode, String code) {
		try {
			List list = getElementValList(elementcode.toUpperCase());
			SysDicElement dicElement = dicDefDao.getDicelement(elementcode.toUpperCase());
			SysDicTable dicTable = dicTableDao.getDicTable(dicElement.getTablecode());
			String value = null;
			for (Object o : list) {
				if (String.valueOf(getValue(o, dicTable.getCodecolumn())).equals(code)) {
					value =getValue(o, dicTable.getKeycolumn());
					return value != null ? Long.valueOf(value) : null;
				}
					
			}
			return null;
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public String getValue(Object o, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (o instanceof Map)
			return ((Map) o).get(key).toString();
		else
			return BeanUtils.getProperty(o, key);			
	}
}

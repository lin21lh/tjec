/************************************************************
 * 类名：SysDicElementServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：数据项服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-08  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.dao.SysDicElementViewFilterDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.base.dic.service.SysDicElementService;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.JsonUtil;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Service
@Component
public class SysDicElementServiceImpl implements SysDicElementService {

	@Autowired
	SysDicTableDao dicTableDao; //数据表DAO
	
	@Autowired
	SysDicColumnDao dicColumnDao; //字段属性列DAO
	
	@Autowired
	SysDicElementDao dicElementDao; //数据项DAO
	
	@Autowired
	SysDicElementViewFilterDao dicElementViewFilterDao; //数据项视图过滤条件DAO
	
	@Autowired
	SysDicElementValSetDao dicElementValSetDao; //数据项值集DAO
	
	//@Autowired
	//JbfConditionFilter jbfConditionFilter;
		
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> queryElementClass() {
		
		return dicElementDao.queryByElementClass();
	}
	
	public PaginationSupport queryPage(String elementclass, String elementcode, String elementname, String pageNumber, String pageSize) {
		
		Integer pnumber = StringUtil.isNotBlank(pageNumber) ? Integer.valueOf(pageNumber) : 1;
		Integer psize = StringUtil.isNotBlank(pageSize) ? Integer.valueOf(pageSize) : 0;
		Long elementClass = StringUtil.isNotBlank(elementclass) ? Long.valueOf(elementclass) : null;
//		System.err.println("数据权限测试===========");
//		try {
//			System.err.println(jbfConditionFilter.getConditionFilterHQL(26L, "t_pubagency"));
//		} catch (AppException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return dicElementDao.query(elementClass, elementcode, elementname, pnumber, psize);
	}
	
	@Override
	public List<SysDicElement> searchDicEles(Map<String, Object> params) {
		return dicElementDao.searchDicEles(params);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveDicElement(SysDicElement dicElement) {
		Long dicElementid = dicElement.getElementid();
		dicElement.setElementtype(Byte.valueOf("0"));
		dicElement.setSystempretag(Byte.valueOf("0"));
		if (dicElement.getIsedit() == null)
			dicElement.setIsedit(Byte.valueOf("0"));
		
		if (dicElement.getIselements() == null)
			dicElement.setIselements(Byte.valueOf("0"));
		
		if (dicElementid == null) {
			dicElementDao.save(dicElement);
		} else {
			dicElementDao.update(dicElement);
		}
	}
	
	public List<SysDicElementViewFilter> getFilters(String elementcode) throws NumberFormatException, Exception {
		
		if (StringUtil.isBlank(elementcode))
			return new ArrayList<SysDicElementViewFilter>();
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElementViewFilter.class).add(Property.forName("elementcode").eq(elementcode.toUpperCase()));
		dc = dc.addOrder(Order.asc("seqno"));
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode);
		SysDicTable dicTable = null;
		List<SysDicElementViewFilter> list = dicElementViewFilterDao.getFilters(dc);
		SysDicColumn dicColumn = null;
		DetachedCriteria dCriteria = null;
		List<SysDicElementViewFilter> resList = new ArrayList<SysDicElementViewFilter>();
		for (SysDicElementViewFilter def : list) {
			dCriteria = DetachedCriteria.forClass(SysDicColumn.class);
			dCriteria = dCriteria.add(Property.forName("tablecode").eq(dicElement.getTablecode()));
			dCriteria = dCriteria.add(Property.forName("columncode").eq(def.getColumncode()));
			dicColumn = (SysDicColumn) dicColumnDao.findByCriteria(dCriteria).get(0);
			def.setColumnname(dicColumn.getColumnname());
			def.setColumntype(dicColumn.getColumntype());
			if (StringUtil.isNotBlank(dicColumn.getSourceelementcode())) {
				def.setSourceelementcode(dicColumn.getSourceelementcode());
				dicTable = dicTableDao.getByElementcode(dicColumn.getSourceelementcode());
				if (StringUtil.isNotBlank(def.getMinvalue())) {
					Object minObject = dicElementValSetDao.getByID(dicTable, Long.valueOf(def.getMinvalue()));
					if (minObject != null)
						def.setMinvaluename(BeanUtils.getProperty(minObject, dicTable.getCodecolumn().toLowerCase()) + "-" + BeanUtils.getProperty(minObject, dicTable.getNamecolumn().toLowerCase()));
				}
				if (StringUtil.isNotBlank(def.getMaxvalue())) {
					Object maxObject = dicElementValSetDao.getByID(dicTable, Long.valueOf(def.getMaxvalue()));
					if (maxObject != null)
						def.setMaxvaluename(BeanUtils.getProperty(maxObject, dicTable.getCodecolumn().toLowerCase()) + "-" + BeanUtils.getProperty(maxObject, dicTable.getNamecolumn().toLowerCase()));
				}
			} else {
				def.setMinvaluename(def.getMinvalue());
				def.setMaxvaluename(def.getMaxvalue());
			}
			resList.add(def);	
		}
		return resList;
	}

	@SuppressWarnings("rawtypes")
	@Transactional(propagation=Propagation.REQUIRED , rollbackFor = java.lang.Exception.class)
	public void saveDicElementView(Map elementMap, String elementviewfilters) throws Exception {
		
		SysDicElement dicElement = new SysDicElement();
		try {
			BeanUtils.populate(dicElement, elementMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
		
		dicElement.setElementtype(Byte.valueOf("1"));
		dicElement.setSystempretag(Byte.valueOf("0"));
		//继承源数据项属性
		SysDicElement sourceElement = dicElementDao.getByElementcode(dicElement.getSourceelement());
		dicElement.setTablecode(sourceElement.getTablecode());
		dicElement.setIsedit(sourceElement.getIsedit());
		dicElement.setCodetype(sourceElement.getCodetype());
		dicElement.setDatatype(sourceElement.getDatatype());
		dicElement.setCodeformat(sourceElement.getCodeformat());
		if (dicElement.getIselements() == null)
			dicElement.setIselements(Byte.valueOf("0"));
		
		dicElementDao.saveOrUpdate(dicElement);
		
		List<SysDicElementViewFilter> filters = JsonUtil.createListObjectByJson(elementviewfilters, SysDicElementViewFilter.class);
		List<Long> existFiltersID = new ArrayList<Long>();
		for (SysDicElementViewFilter filter : filters) {
			if (filter.getFilterid() != null)
				existFiltersID.add(filter.getFilterid());
		}
//		DataUtilFactory fac = DataUtilFactory.newInstance();
//		DataFilter dataFilter = fac.eq("elementcode", dicElement.getElementcode());
//		dataFilter = dataFilter.notIn("filterid", existFiltersID);
//		List<DicElementViewFilter> deleteFilters = dicElementViewFilterDao.find(dataFilter);
//		if (deleteFilters.size() > 0)
//			dicElementValSetDao.deleteAll(deleteFilters);

		
		for (SysDicElementViewFilter filter : filters) {
			filter.setElementcode(dicElement.getElementcode());
			dicElementViewFilterDao.saveOrUpdate(filter);
		}

	}
	
	public String delete(String elementids) throws AppException {
		String pretagmsg = "";
		String existValsmsg = "";
		String existElementViewmsg = "";
		String[] elementid = elementids.split(",");
		List<Long> list =new ArrayList<Long>(elementid.length);
		for (String eid : elementid) {
			list.add(Long.valueOf(eid.toLowerCase()));
		}
		
		List<SysDicElement> deList = dicElementDao.findDicElement(list);
		List<SysDicElement> canDeleteList = new ArrayList<SysDicElement>();
		for (SysDicElement dicElement : deList) {
			if (dicElement.getSystempretag().equals(Byte.valueOf("1"))) {
				//系统预设 数据项不允许删除
				if (pretagmsg.length() > 0)
					pretagmsg += "、";
				pretagmsg += dicElement.getElementcode() + "-" + dicElement.getElementname();
				continue;
			}
			//基础数据项 并且 存在值集 数据项 不允许删除
			if (dicElement.getElementtype().equals(Byte.valueOf("0")) && isExistsVals(dicElement.getElementcode(), dicElement.getTablecode())) {
				if (existValsmsg.length() > 0)
					existValsmsg += "、";
				existValsmsg += dicElement.getElementcode() + "-" + dicElement.getElementname();
				continue;
			}
			//存在数据项视图 数据项 不允许删除
			if (isExistsElementView(dicElement.getElementcode())) {
				if (existElementViewmsg.length() > 0)
					existElementViewmsg += "、";
				existElementViewmsg += dicElement.getElementcode() + "-" + dicElement.getElementname();
				continue;
			}
			
			canDeleteList.add(dicElement);
				
		}
		
		dicElementDao.deleteAll(canDeleteList);
		
		if (pretagmsg.length() > 0)
			pretagmsg = "数据项：" + pretagmsg + "为系统预设，不允许删除";
		
		if (existValsmsg.length() > 0) {
			if (pretagmsg.length() > 0)
				pretagmsg += "；";
			pretagmsg += "数据项：" + existValsmsg + "存在值集，不允许删除";
		}
		
		if (existElementViewmsg.length() > 0) {
			if (pretagmsg.length() > 0)
				pretagmsg += "；";
			pretagmsg += "数据项：" + existElementViewmsg + "存在数据项视图，不允许删除";
		}
		
		if (pretagmsg.length() > 0)
			pretagmsg += "！";
			
		return pretagmsg;
	}
	
	@SuppressWarnings("rawtypes")
	Boolean isExistsVals(String elementcode, String tablecode) {
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		List<Map> valsList = dicElementValSetDao.queryDicElementVals(dicTable, elementcode, "");
		return valsList != null && valsList.size() > 0;

	}
	
	Boolean isExistsElementView(String elementcode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		dc.add(Property.forName("sourceelement").eq(elementcode));
		return dicElementDao.findByCriteria(dc).size() > 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> findDicElementToDataScope() {
		return dicElementDao.findDicElementToDataScope();
	}
	
	public SysDicElement getDicElement(String elementcode) {
		
		return dicElementDao.getDicelement(elementcode.toUpperCase());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SysDicElement> getSourceDicElements() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicElement.class);
		dc.add(Property.forName("elementtype").eq(Byte.valueOf("0")));
		return (List<SysDicElement>) dicElementDao.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysDicColumn> getColumnsByElementcode(String elementcode) {
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		DetachedCriteria dCriteria = DetachedCriteria.forClass(SysDicColumn.class);
		dCriteria.add(Property.forName("tablecode").eq(dicTable.getTablecode()));
		List<SysDicColumn> list = (List<SysDicColumn>) dicColumnDao.findByCriteria(dCriteria);
		return list;
	}
}

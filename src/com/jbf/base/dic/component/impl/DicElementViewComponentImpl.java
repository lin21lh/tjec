/************************************************************
 * 类名：DicElementViewComponentImpl.java
 *
 * 类别：组件实现类
 * 功能：数据项视图组件实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-14  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.component.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.dic.component.DicElementViewComponent;
import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dao.SysDicElementViewFilterDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.po.SysDicElementViewFilter;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Component
public class DicElementViewComponentImpl implements DicElementViewComponent {

	@Autowired
	SysDicElementViewFilterDao dicElementViewFilterDao;
	
	@Autowired
	SysDicElementDao dicElementDao;
	
	@Autowired
	SysDicTableDao dicTableDao;
	
	@Autowired
	SysDicColumnDao dicColumnDao;
	
	public String getSqlString(String elementcode) throws AppException {
		SysDicElement dicElement = getDicElement(elementcode);
		if (dicElement.getElementtype().equals(Byte.valueOf("0"))) //基础数据项
			return "";
		
		SysDicElement sourceDicElement = getDicElement(dicElement.getSourceelement());
		SysDicTable mainTable = getDicTableByTablecode(sourceDicElement.getTablecode());
		List<SysDicElementViewFilter> dicElementViewFilters = findElementViewFilter(elementcode);
		StringBuffer filterSB = new StringBuffer(100);
		SysDicTable filterDicTable = null;
		SysDicColumn filterColumn = null;
		if(dicElementViewFilters.size() > 0) {
			for (SysDicElementViewFilter filter : dicElementViewFilters) {
				filterDicTable = getDicTableByElementcode(filter.getElementcode());
				filterColumn = getDicColumn(filterDicTable.getTablecode(), filter.getColumncode());
				if (StringUtil.isNotBlank(filterColumn.getSourceelementcode()))
					filterDicTable = getDicTableByElementcode(filterColumn.getSourceelementcode());
				else
					filterDicTable = null;
				filterSB.append(filter.toString(filterDicTable,  mainTable, "t", true));
			}
	    }
	    return filterSB.toString();
	}
	
    public List<SysDicElementViewFilter> findElementViewFilter(String elementcode) {
    	return dicElementViewFilterDao.queryByElementcode(elementcode);
    }
    
    public SysDicColumn getDicColumn(String tablecode, String columncode) {
    	return dicColumnDao.getDiccolumn(tablecode, columncode);
    }
    
    public SysDicColumn getDicColumnBySourceElement(String tablecode, String sourceElement) throws AppException {
    	return dicColumnDao.getColumnBySourceElement(tablecode, sourceElement);
    }
    
    public SysDicElement getDicElement(String elementcode) {
    	return dicElementDao.getByElementcode(elementcode);
    }
    
    public SysDicTable getDicTableByTablecode(String tablecode) {
    	return dicTableDao.getByTablecode(tablecode);
    }
    
    public SysDicTable getDicTableByElementcode(String elementcode) {
    	return dicTableDao.getByElementcode(elementcode);
    }
}

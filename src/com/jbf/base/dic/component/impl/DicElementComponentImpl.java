/************************************************************
 * 类名：DicElementComponentImpl.java
 *
 * 类别：组件实现类
 * 功能：数据项组件实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-14  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.dic.component.DicElementComponent;
import com.jbf.base.dic.component.DicElementValSetComponent;
import com.jbf.base.dic.component.DicElementViewComponent;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;

@Scope("prototype")
@Component("base.dic.component.DicElementComponent")
public class DicElementComponentImpl implements DicElementComponent {

	@Autowired
	DicElementValSetComponent dicElementValSetCp;
	@Autowired
	DicElementViewComponent dicElementViewCp;
	
    public String getElementCodeSql(String elementCode, String filter) throws AppException {
		String sElement = elementCode.toUpperCase();
		SysDicElement dicElement = findPhysicalElement(sElement);
		if(dicElement.getTablecode() == null || dicElement.getTablecode().length() == 0)
		    throw new AppException("dic.err.notable", new String[] {dicElement.getTablecode()});
		SysDicTable tableDTO = dicElementViewCp.getDicTableByTablecode(dicElement.getTablecode());
		StringBuffer sWhere = new StringBuffer("");
		if(tableDTO.getStatuscolumn() != null && tableDTO.getStatuscolumn().length() != 0) {
		    sWhere.append(tableDTO.getStatuscolumn());
		    sWhere.append(" = ");
		    sWhere.append("'0'");//sWhere.append(dicElementValSetCp.getElementIDByCode("SYS_STATUS", "0"));
		}
		String viewfilter = getFilters(elementCode, dicElement.getTablecode());
		if(viewfilter.length() > 0)
		    if(sWhere.length() != 0) {
		        sWhere.append(" and (");
		        sWhere.append(viewfilter);
		        sWhere.append(")");
		    } else {
		        sWhere.append(viewfilter);
		    }
		if(filter != null && filter.trim().length() != 0)
		{
		    if(sWhere.length() != 0)
		        sWhere.append(" and ");
		    sWhere.append(filter);
		}
		StringBuffer sSql = new StringBuffer("Select * from ");
		StringBuffer enumFilter = new StringBuffer();
		if(tableDTO.getTabletype() == 4)
		{
		    enumFilter.append("(elementCode = '");
		enumFilter.append(dicElement.getElementcode());
		enumFilter.append("')");
		}
		sSql.append(tableDTO.getTablecode());
		if(sWhere.length() > 0) {
		    sSql.append(" where (");
			sSql.append(sWhere);
			sSql.append(")");
			if(enumFilter.length() > 0) {
			    sSql.append(" and ");
			            sSql.append(enumFilter);
			}
		}
		return sSql.toString();
    }
    
    public SysDicElement findPhysicalElement(String elementCode) throws AppException {
        String sElement = elementCode;
        SysDicElement dicElement = null;
        do {
        	dicElement = findElementByCode(sElement);
            if(dicElement == null)
                throw new AppException("dic.err.noelement", new String[] {elementCode});
            if(dicElement.getElementtype() == 0)
                break;
            sElement = findElementByCode(dicElement.getElementcode()).getSourceelement();
        } while (sElement != null);
        return dicElement;
    }

    public SysDicElement findElementByCode(String elementcode) {
        return dicElementViewCp.getDicElement(elementcode);
    }
    
    public String getFilters(String elementcode, String tablecode) throws AppException {
    	SysDicElement dicElement = findElementByCode(elementcode);
		StringBuffer result = new StringBuffer(100);
		if (dicElement == null)
		    throw new AppException("dic.err.noelement", new String[] {elementcode});
		if (dicElement.getElementtype() == 1) {
		    String viewCode = dicElement.getElementcode();
		    result.append("(");
		    String filter = dicElementViewCp.getSqlString(viewCode);
		    if(filter == null || filter.trim().length() == 0)
		        throw new AppException("dicview.err.nofilters", new String[] {viewCode});
		    result.append(filter);
		    result.append(")");
		}
		return result.toString();
	}
    
    public SysDicColumn getDicColumn(String tablecode, String columncode) {
    	return dicElementViewCp.getDicColumn(tablecode, columncode);
    }
    
    public SysDicColumn getDicColumnBySourceElement(String tablecode, String sourceElement) throws AppException {
    	return dicElementViewCp.getDicColumnBySourceElement(tablecode, sourceElement);
    }
    
    public SysDicElement getDicElement(String elementcode) {
    	return dicElementViewCp.getDicElement(elementcode);
    }
    
    public SysDicTable getDicTableByTablecode(String tablecode) {
    	return dicElementViewCp.getDicTableByTablecode(tablecode);
    }
    
    public SysDicTable getDicTableByElementcode(String elementcode) {
    	return dicElementViewCp.getDicTableByElementcode(elementcode);
    }
}

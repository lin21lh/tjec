/************************************************************
 * 类名：DatascopeComponentImpl.java
 *
 * 类别：组件实现类
 * 功能：组装多条件项数据权限组件实现类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.component.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.datascope.component.DatascopeComponent;
import com.jbf.base.datascope.component.ScopeItemParseComponent;
import com.jbf.base.datascope.dao.SysDatascopeItemDao;
import com.jbf.base.datascope.dao.SysDatascopeMainDao;
import com.jbf.base.datascope.dao.SysDatascopeSubDao;
import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.base.datascope.po.SysDatascopemain;
import com.jbf.base.datascope.po.SysDatascopesub;
import com.jbf.common.exception.AppException;

@Scope("prototype")
@Component
public class DatascopeComponentImpl implements DatascopeComponent {

	@Autowired
	SysDatascopeMainDao datascopeMainDao;
	@Autowired
	SysDatascopeSubDao datascopeSubDao;
	@Autowired
	SysDatascopeItemDao datascopeItemDao;
	
	@Autowired
	ScopeItemParseComponent itemParseComponent;
	
    public String getDynamicSQLExpression(Long scopemainid, String tableAlias, String tablecode, Long iUserid, int scopeType) throws AppException {
    	tablecode = tablecode.toUpperCase();
    	SysDatascopemain datascopemain = datascopeMainDao.get(scopemainid);
    	if(datascopemain == null) {
    		throw new AppException("数据权限未找到！");
    	}
    	List<SysDatascopesub> scopesubs = datascopeSubDao.findscopesubByscopemainID(scopemainid);
    	StringBuffer subSQL = new StringBuffer();
    	String sql;
    	for (Iterator iter = scopesubs.iterator(); iter.hasNext();) {
    		SysDatascopesub scopesub = (SysDatascopesub)iter.next();
    		List<SysDatascopeitem> scopeitems = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
    		StringBuffer itemSQL = new StringBuffer();
            for (SysDatascopeitem scopeitem : scopeitems) {
                if(!preCAExceptionHandle(scopeitem, tablecode)) {
                    itemSQL.append(createSQLByScopeItem(scopeitem, tableAlias, tablecode, iUserid, scopeType));
                    itemSQL.append("AND");
                }
            }

            String item = itemSQL.toString();
            if(item != null && !item.equals(""))
            {
                int beginIndex = item.indexOf('(');
                int endIndex = item.lastIndexOf('A');
                String itemSQL1 = item.substring(0, endIndex);
                subSQL.append("(");
                subSQL.append(itemSQL1);
                subSQL.append(")");
                subSQL.append("OR");
            }
        }

    	sql = subSQL.toString();
        if(sql == null || sql.equals("")) {
        	return null;
        } else {
            int beginIndex = sql.indexOf('(');
            int endIndex = sql.lastIndexOf('O');
            String subSQL1 = sql.substring(beginIndex, endIndex);
            return subSQL1;
        }
    }
    
    private boolean preCAExceptionHandle(SysDatascopeitem scopeitem, String tablecode) throws AppException {
        tablecode = tablecode.toUpperCase();
        String columncode = "";
        try {
        	columncode = itemParseComponent.findColumnBySourceElement(tablecode, scopeitem.getElementcode());
        } catch(Exception e) {
        	e.printStackTrace();
            return true;
        }
        return false;
    }
    
    public String createSQLByScopeItem(SysDatascopeitem scopeitem, String tableAlias, String tableCode, Long userid, int scopeType) throws AppException {
        StringBuffer buffer = new StringBuffer();
       // String elementCode = scopeitem.getElementcode().toUpperCase();
       // boolean hasSource = false;
        String sql = itemParseComponent.getSql(scopeitem, tableAlias, tableCode, userid, scopeType);
        buffer.append(sql);
        return buffer.toString();
    }
    
    public String getConditionByElementcode(Long scopemainid, String elementcode, String tableAlias, String tableCode, Long iUserid, int scopeType) throws AppException {
    	
    	SysDatascopemain datascopemain = datascopeMainDao.get(scopemainid);
    	if(datascopemain == null) {
    		throw new AppException("数据权限未找到！");
    	}
    	
    	List<SysDatascopesub> scopesubs = datascopeSubDao.findscopesubByscopemainID(scopemainid);
    	StringBuffer itemSQL = new StringBuffer("");
    	for (Iterator iter = scopesubs.iterator(); iter.hasNext();) {
    		SysDatascopesub scopesub = (SysDatascopesub)iter.next();
    		List<SysDatascopeitem> scopeitems = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
    		
            for (SysDatascopeitem scopeitem : scopeitems) {
            	if (itemSQL.length() > 0)
        			itemSQL.append(" or ");
            	
            	if (scopeitem.getElementcode().equals(elementcode.toUpperCase())) {
            		itemSQL.append(createSQLByScopeItem(scopeitem, tableAlias, tableCode, iUserid, scopeType));
            	} else {
            		itemSQL.append("1=1");
            	}
            }
        }
    	
    	return itemSQL.toString();
    }
    
    public List<SysDatascopeitem> findDatascopeItemList(Long scopemainID) {
    	
    	List<SysDatascopeitem> dsItemList = new ArrayList<SysDatascopeitem>();
    	List<SysDatascopesub> dsSubList = datascopeSubDao.findscopesubByscopemainID(scopemainID);
    	if (dsSubList.isEmpty())
    		return dsItemList;
    	for (SysDatascopesub dsSub : dsSubList) {
    		dsItemList.addAll(datascopeItemDao.findDsitemByscopesubID(dsSub.getScopesubid()));
    	}
    	
    	return dsItemList;
    }
    
    public String getDatascopemainName(Long scopemainId) {
    	SysDatascopemain scopemain = datascopeMainDao.get(scopemainId);
    	return scopemain.getScopemainname();
    }
    
    public class ScopeType {
    	public static final int ID = 1;
    	public static final int CODE = 2;
    }
}

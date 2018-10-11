/************************************************************
 * 类名：SysDicElemenetValSetDaoImpl.java
 *
 * 类别：DAO实现类
 * 功能：数据项值集DAO实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Repository
public class SysDicElemenetValSetDaoImpl extends GenericDao<Object, Long> implements SysDicElementValSetDao {
	
	@Override
	public List<Map> queryDicElementVals(SysDicTable tablePO, String elementcode, String viewFilterSql) {
		
		return queryDicElementVals(tablePO, elementcode, viewFilterSql, null, null, null);
	}
	
	@Override
	public List<Map> queryDicElementVals(SysDicTable tablePO, String elementcode, String viewFilterSql, String customSql, String conditionFilterSql, String relationSql) {
		
		
		
		SqlVO sqlVO = super.getTableInfo(tablePO.getTablecode());
		List<ColVO> cols = sqlVO.getCols();
		boolean pand = false;
		for (ColVO colVO : cols) {
			if (colVO.getName().equals("elementcode")) {
				pand = true;
				break;
			}
				
		}
		
		String where = " 1=1";
		if (pand)
			where = "elementcode='" + elementcode.toUpperCase() + "'";
		
		if (StringUtil.isNotBlank(viewFilterSql))
			where += " and (" + viewFilterSql + ")";
		
		if (StringUtil.isNotBlank(customSql)) {
			where += " and (" + customSql + ")";
		}
		
		if (StringUtil.isNotBlank(conditionFilterSql)) {
			where += " and (" + conditionFilterSql + ")";
		}
		
		if (StringUtil.isNotBlank(relationSql)) {
			where += " and (" + relationSql + ")";
		}
			
		if (StringUtil.isNotBlank(tablePO.getCodecolumn()))
			where += "order by " + tablePO.getCodecolumn().toLowerCase();
		return super.queryBySQL(tablePO.getTablecode(), where);
	}
	
	@Override
	public EasyUITotalResult queryPageDicElementVals(SysDicTable dicTable, String elementcode, Integer page, Integer rows) {
		
		SqlVO sqlVO = super.getTableInfo(dicTable.getTablecode());
		List<ColVO> cols = sqlVO.getCols();
		boolean pand = false;
		for (ColVO colVO : cols) {
			if (colVO.getName().equals("elementcode")) {
				pand = true;
				break;
			}
				
		}
		
		String where = " 1=1";
		if (pand)
			where = "elementcode='" + elementcode.toUpperCase() + "'";
		
		where += " order by " + dicTable.getCodecolumn();
		return EasyUITotalResult.from(super.queryPageBySQL(dicTable.getTablecode(), where, page, rows));
	}
	
	public Map add(Map<String, Object> values, String tablename) {
		return super.addByMap(values, tablename);
	}
	
	public Map update(Map<String, Object> values, String tablename) {
		return super.updateByMap(values, tablename);
	}
	
	public Boolean delete(String tablename, String where) {
		return super.deleteBySQL(tablename, where);
	}

	@Override
	public Object getByID(SysDicTable dicTable, Long id) throws AppException {
		String where = dicTable.getKeycolumn() + "=" + id;
		List list = super.queryBySQL(dicTable.getTablecode(), where);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public Map getByElementAndCode(String tablename, String elementcode, String code) {
		
		SqlVO sqlVO = super.getTableInfo(tablename);
		List<ColVO> cols = sqlVO.getCols();
		boolean pand = false;
		for (ColVO colVO : cols) {
			if (colVO.getName().equals("elementcode")) {
				pand = true;
				break;
			}
		}
		
		String where = "";
		if (pand)
			where = "upper(elementcode)='" + elementcode.toUpperCase() + "'";
		
		if (where.length() > 0)
			where += " and ";
		where += "code='" + code + "'";
		
		List list = super.queryBySQL(tablename, where);
		if (list != null && list.size() > 0)
			return (Map)list.get(0);
		else
			return null;
	}
	
	public List query(String tablename, String where) {
		return super.queryBySQL(tablename, where);
	}

	@Override
	public boolean deleteDicTreeElementVal(SysDicTable dicTable, Long id) throws AppException {
		String where = dicTable.getKeycolumn() + "=" + id;
		
		return super.deleteBySQL(dicTable.getTablecode(), where);
	}

	@Override
	public Long findIDByCodeElement(SysDicTable dicTable, String elementcode, String code) throws AppException {
		// TODO Auto-generated method stub
		boolean pand = false;
		SqlVO sqlVO = super.getTableInfo(dicTable.getTablecode());
		List<ColVO> cols = sqlVO.getCols();
		for (ColVO colVO : cols) {
			if (colVO.getName().equals("elementcode"))
				pand = true;
		}
		List list = super.findMapBySql("select " + dicTable.getKeycolumn() + " from " + dicTable.getTablecode() 
				+ " where " + dicTable.getCodecolumn() + "='" + code + "'" + (pand ? " and elementcode='" + elementcode + "'" : ""));
		Map map = (Map)list.get(0);
		return Long.valueOf(map.get(dicTable.getKeycolumn()).toString());
	}

	public List findByElemenetcode(SysDicElement dicElement, SysDicTable dicTable, String idColumn, String textColumn, String viewFilterSql,
			String customSql, String conditionFilterSql, String relationSql) {
		
		 String where = "";
		 if (dicElement.getElementtype().equals(Byte.valueOf("1")) ) {
			 where = viewFilterSql;
		 } else {
			boolean pand = false;
			SqlVO sqlVO = super.getTableInfo(dicTable.getTablecode());
			List<ColVO> cols = sqlVO.getCols();
			for (ColVO colVO : cols) {
				if (colVO.getName().equals("elementcode"))
					pand = true;
			}
			if (pand)
				where = " upper(elementcode)='" + dicElement.getElementcode().toUpperCase()  + "'";
		 }
		 
			if (StringUtil.isNotBlank(customSql)) {
				where += " and (" + customSql + ")";
			}
			
			if (StringUtil.isNotBlank(conditionFilterSql)) {
				where += " and (" + conditionFilterSql + ")";
			}
			
			if (StringUtil.isNotBlank(relationSql)) {
				where += " and (" + relationSql + ")";
			}
		
		 if (where.length() > 0)
			 where = where + " and ";
		String sql = "select " + idColumn + " as id, " + textColumn + " as text from " + dicElement.getTablecode() 
				+ " t where " + where + dicTable.getStatuscolumn().toLowerCase() + "=0 order by code";

		return super.findMapBySql(sql);
	
	}
}

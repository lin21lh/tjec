/************************************************************
 * 类名：EasyUIDataGridSchemeResolve.java
 *
 * 类别：解析类
 * 功能：DataGrid列表解析类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.ui.resolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jbf.base.dic.dto.Column;
import com.jbf.base.dic.dto.GridFieldDTO;
import com.jbf.base.dic.dto.GridSchemeDTO;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.util.StringUtil;

public class EasyUIDataGridSchemeResolve {
	
	String datagridID = "datagrid_elementVal"; //grid ID
	public String getGridScheme(GridSchemeDTO gridSchemeDTO, String elementcode, String tablecode) {
		
		List<GridFieldDTO> list = gridSchemeDTO.getGridField();
		Map<String, SysDicColumn> dicColumn = gridSchemeDTO.getColumnMap();
		List<Column> columns = new ArrayList<Column>(list.size());
		SysDicColumn column = null;
		String keyColumn = "";
		for (GridFieldDTO gridField : list) {
			column = dicColumn.get(gridField.getName().toLowerCase());
			if (Byte.valueOf("1").equals(column.getIskeycol())) {
				keyColumn = gridField.getName().toLowerCase();
				continue;
			}
			if (StringUtil.isNotBlank(column.getSourceelementcode())) {
				columns.add(new Column(gridField.getName().toLowerCase(), false, "", "0", true));
				columns.add(new Column(gridField.getName().toLowerCase()+"name", false, column.getColumnname(), gridField.getWidth(), false));
			} else
				columns.add(new Column(gridField.getName().toLowerCase(), false, column.getColumnname(), gridField.getWidth(), gridField.getIshidden() == 1));
		}
		
		List<Column> frozenColumns = new ArrayList<Column>();
		if (StringUtil.isNotBlank(keyColumn))
			frozenColumns.add(new Column(keyColumn, true, "", "0", true));
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("elementcode", elementcode);
		queryParams.put("tablecode", tablecode);
		
		StringBuffer loadDataGridFunction = new StringBuffer();
		loadDataGridFunction.append("function loadDataFunction() {");
		loadDataGridFunction.append("$(\"#" + datagridID + "\").datagrid(");
		loadDataGridFunction.append(new DataGrid(true,  frozenColumns, columns, queryParams));
		loadDataGridFunction.append(");}");
		
		System.out.println(loadDataGridFunction);
		return loadDataGridFunction.toString();
	}
	
	public String getGridSchemeVM() {
		
		return "<table id=\"" + datagridID + "\" ></table>";
	}
	
	public class DataGrid {
		Boolean fit = true;
		Boolean fitColumns = false;//默认设置 列自适应
		Boolean stripe = true; //默认false，斑马条纹
		Boolean singleSelect = true;//默认为false，是否选中单行
		Boolean rownumbers = false;//默认为false，是否显示序号列
		Boolean nowrap = true; //默认为true，是否换行
		//Boolean pagination = true; //默认为false，是否显示在底部分页栏
		//Integer pageNumber = 1; //默认页码为第一页
		Integer pageSize =50; //默认分页数为10条
		String view = "scrollview";
		//String pageList ="[10,20,30,40,50]"; //分页条数下拉菜单
		String url = "base/dic/dicElementValSetController/queryDicElementVals.do";
		String loadMsg = "正在加载，请稍候......";
		Map<String, Object> queryParams;
		
		List<Column> columns;
		
		List<Column> frozenColumns;
		
		public DataGrid() {
			
		}
		
		public DataGrid(Boolean singleSelect, List<Column> frozenColumns, List<Column> columns, Map<String, Object> queryParams) {
			this.singleSelect = singleSelect;
			this.frozenColumns = frozenColumns;
			this.columns = columns;
			this.queryParams = queryParams;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("fit:").append(fit).append(", stripe:").append(stripe).append(", singleSelect:").append(singleSelect);//.append(", pagination:").append(pagination);
			sb.append(", view:").append(view).append(", pageSize:").append(pageSize);//.append(", pageList:").append(pageList).append(",");
			sb.append(",url:\"").append(url).append("\", loadMsg:\"").append(loadMsg).append("\"");
			sb.append(", queryParams:{");
			Iterator<String> it = queryParams.keySet().iterator();
			boolean pand = false;
			while (it.hasNext()) {
				if (pand)
					sb.append(",");
				String key = it.next();
				sb.append(key).append(":'").append(queryParams.get(key)).append("'");
				pand = true;
			}
			sb.append("}");
			if (frozenColumns != null || frozenColumns.size() > 0)
				sb.append(", frozenColumns:[").append(frozenColumns.toString()).append("]");
			
			sb.append(", columns:[").append(columns.toString());
			sb.append("]");
			sb.append("}");
			
			return sb.toString();
		}
	}
	
}

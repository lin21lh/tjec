/************************************************************
 * 类名：EasyUIFormResolve.java
 *
 * 类别：解析类
 * 功能：EasyUI表单解析类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-21  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.ui.resolve;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.jbf.base.dic.dto.CodeType;
import com.jbf.base.dic.dto.ComboboxDataRepoints;
import com.jbf.base.dic.dto.EasyUIInputType;
import com.jbf.base.dic.dto.EditFieldDTO;
import com.jbf.base.dic.dto.EditSchemeDTO;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

public class EasyUIFormResolve {

	/**
	 * 比较器
	 */
	final static Comparator<EditFieldDTO> editFieldComp = new Comparator<EditFieldDTO>() {
		public int compare(EditFieldDTO o1, EditFieldDTO o2) {
			if (o1.getRow() > o2.getRow())
				return 1;
			else if(o1.getRow() == o2.getRow()) {
				return o1.getCol() > o2.getCol() ? 1 : -1;
			} else if (o1.getRow() < o2.getRow())
				return -1;
			return 0;
		}
	};
	
	/**
	 * 获取form表单界面方案
	 * @param editSchemeDTO form表单DTO
	 * @param dicElement 数据项PO
	 * @return form表单界面
	 * @throws AppException
	 */
	public String getFormScheme(EditSchemeDTO editSchemeDTO, SysDicElement dicElement) throws AppException {
		
		List<EditFieldDTO> fields = editSchemeDTO.getEditfields();
		SysDicTable dicTable = editSchemeDTO.getDicTable();
		Map<String, SysDicColumn> columnMap = editSchemeDTO.getColumnMap();
		StringBuffer sb = new StringBuffer();
		
		Collections.sort(fields, editFieldComp);
		
		SysDicColumn column = null;
		Integer currentRow = 1;
		for (EditFieldDTO field : fields) {
			if (field.getCol() == 1) {
				if (!field.getRow().equals( currentRow))
					sb.append("</tr>");
				
				sb.append("<tr>");
			}
			column = columnMap.get(field.getName().toLowerCase());
			if (column == null)
				throw new AppException("数据表【" + dicTable.getTablecode() + "】对应列【" + field.getName().toLowerCase() + "】未找到");
			
			if (field.getRow() ==1 && field.getCol() == 1) 
				sb.append("<th style='width:" +editSchemeDTO.getLabelcellwidth() + "px;border-left-width:1px;border-top-width:1px'>");
			else if (field.getRow() ==1 && field.getCol() != 1)
				sb.append("<th style='width:" +editSchemeDTO.getLabelcellwidth() + "px;border-top-width:1px'>");
			else if (field.getRow() !=1 && field.getCol() == 1)
				sb.append("<th style='width:" +editSchemeDTO.getLabelcellwidth() + "px;border-left-width:1px'>");
			else
				sb.append("<th>");
			
			sb.append(column.getColumnname()).append("：</th>");
			if (field.getMergedcols() !=null && field.getMergedcols() != 0) {
				sb.append("<td colspan=\"" + field.getMergedcols() + "\"");
				if (field.getRow() == 1)
					sb.append("style='border-top-width:1px'");
				
				sb.append(">");
			}
			else	{
				sb.append("<td ");
				if (field.getRow() == 1)
					sb.append("style='border-top-width:1px'");
				
				sb.append(">");
			}
			String htmlInput = getHTMLInput(editSchemeDTO.getControlcellwidth(), field, column, dicTable);
		//	CodeType codeType = CodeType.getCodeType(dicElement.getCodetype());
			//if (EasyUIInputType.TEXTBOX.getIndex().equals(field.getControlname()) || EasyUIInputType.TEXTAREA.getIndex().equals(field.getControlname()))
				//htmlInput = htmlInput.replaceAll("/>", "  onChange=\"form_onchange()\"/>");
			
			sb.append(htmlInput).append("</td>");
			
			
			currentRow = field.getRow();
		}
		sb.append("<input id=\"").append( dicTable.getKeycolumn().toLowerCase()).append("\" name=\"").
			append(dicTable.getKeycolumn().toLowerCase()).append("\" hidden=\"true\" />");
		
		if (dicElement != null) {
			sb.append("<input id=\"elementcode\" name=\"elementcode\" value=\"" + dicElement.getElementcode() + "\" hidden=\"true\" />");
			CodeType codeType = CodeType.getCodeType(dicElement.getCodetype());
			if (codeType.equals(CodeType.LayerCode))
				sb.append("<input id=\"superitemid\" name=\"" + dicTable.getSupercolumn().toLowerCase() + "\" hidden=\"true\" />");
		}
		sb.append("</tr>");
		
		System.out.println(sb);
		return sb.toString();
	}
	
	/**
	 * 获取HTML代码
	 * @param colwidth 列宽
	 * @param field form表单字段DTO
	 * @param column 数据列PO
	 * @param dicTable 数据表PO
	 * @return HTML代码字符串
	 * @throws AppException
	 */
	private String getHTMLInput(Integer colwidth, EditFieldDTO field, SysDicColumn column, SysDicTable dicTable) throws AppException {
		
		if (StringUtil.isBlank(field.getControlname()))
			throw  new AppException(field.getName() + "未定义控件类型.");
		
		if ("textbox".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.TEXTBOX.getInputHTML(colwidth, field, column, dicTable, "onChange:form_onchange");
		
		else if ("numberbox".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.NUMBERBOX.getInputHTML(colwidth, field, column, dicTable, "onChange:form_onchange");
		
		else if ("combobox".equalsIgnoreCase(field.getControlname())) {
			String dataoptions = new ComboboxDataRepoints(column.getSourceelementcode()).toString() + ",onSelect:form_onchange";
			return EasyUIInputType.COMBOBOX.getInputHTML(colwidth, field, column, dicTable, dataoptions);
		}
			
		
		else if ("enumbox".equalsIgnoreCase(field.getControlname())) {
			String dataoptions = new ComboboxDataRepoints(column.getSourceelementcode()).toString() + ",onSelect:form_onchange";
			return EasyUIInputType.ENUMBOX.getInputHTML(colwidth, field, column, dicTable, dataoptions);
		}
			
		
		else if ("searchbox".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.SEARCHBOX.getInputHTML(colwidth, field, column, dicTable, "onSelect:form_onchange");
		
		else if ("datebox".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.DATEBOX.getInputHTML(colwidth, field, column, dicTable, "onChange:form_onchange");
		
		else if ("timebox".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.DATETIMEBOX.getInputHTML(colwidth, field, column, dicTable, "onChange:form_onchange");
		
		else if ("textarea".equalsIgnoreCase(field.getControlname()))
			return EasyUIInputType.TEXTAREA.getInputHTML(colwidth, field, column, dicTable, "onChange:form_onchange");
		
		else
			throw new AppException("easyui未定义 【" + field.getControlname() + "】");
	}
}

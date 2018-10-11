/************************************************************
 * 类名：EasyUIInputType.java
 *
 * 类别：枚举类
 * 功能：EasyUI控件类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;


public enum EasyUIInputType {

	TEXTBOX("textbox", "input", "easyui-textbox", "请输入"), //文本框
	NUMBERBOX("numberbox", "input", "easyui-numberbox", "请输入"), //数字框
	COMBOBOX("combobox", "input", "easyui-combobox", "请选择"),//顺序码下拉选择框
	ENUMBOX("enumbox", "input", "easyui-combobox", "请选择"), //枚举下拉框
	SEARCHBOX("searchbox", "input", "easyui-searchbox", "请选择"), //弹出选择框
	DATEBOX("datebox", "input", "easyui-datebox", "请选择"),//日期框
	DATETIMEBOX("timebox", "input", "easyui-datetimebox", "请选择"), //时间框
	TEXTAREA("textarea", "input", "easyui-textbox", "请输入"); //文本域
	
	
	String index; //
	String inputType; //输入框类型
	String cssClass; //easyui class
	String missingMsgPrefix; //提示语前缀
	
	private EasyUIInputType(String index, String inputType, String cssClass, String missingMsgPrefix) {
		this.index = index;
		this.inputType = inputType;
		this.cssClass = cssClass;
		this.missingMsgPrefix = missingMsgPrefix;
	}
	
	public String getInputHTML(Integer colwidth, EditFieldDTO field, SysDicColumn column, SysDicTable dicTable, String datapoints) throws AppException {
		
		String htmlStr = "<" + this.inputType + " id='" + column.getColumncode().toLowerCase()  + "edit' name='" + column.getColumncode().toLowerCase() + "' class='" + getCssClass() + "' " ;
		htmlStr += "data-options=\"";
		
		if (StringUtil.isNotBlank(datapoints))
			htmlStr += datapoints;
		
		if (field.getNotnull() == 1) {
			if (StringUtil.isNotBlank(datapoints))
				htmlStr += ",";
			htmlStr += "required:true,missingMessage:'" + getMissingMsgPrefix() + column.getColumnname() + "'";
		}
		if (this.index.equals("textarea")) {
			if (StringUtil.isNotBlank(datapoints) || field.getNotnull() == 1)
				htmlStr += ",";
			htmlStr += "validType:{length:[0,100]}";
		}
			
		
		htmlStr += "\" ";
		
		if(this.index.equals("textarea")) {
			htmlStr += " prompt='最多可输入100个汉字' multiline='true' ";
		}
		if (field.getIsunique() == 1) {
			if (StringUtil.isBlank(dicTable.getKeycolumn()))
				throw new AppException("datatable.keycolumn.undefined", new String[] {dicTable.getTablecode()});
			
			htmlStr += " validType=\"remoteIsExist['" + dicTable.getTablecode() + "','" + dicTable.getKeycolumn().toLowerCase() + "','elementcode']\"";
		}
			
		
		htmlStr += " style=\"width:" + colwidth +"px;";
		
		if (this.index.equals("textarea"))
			htmlStr += "height:80px;";
		
		htmlStr += "\"";
		htmlStr += " />";
				
		return htmlStr;
	}
	
	public String getIndex() {
		return index;
	}
	
	public String getCssClass() {
		return cssClass;
	}
	
	public String getMissingMsgPrefix() {
		return missingMsgPrefix;
	}

}



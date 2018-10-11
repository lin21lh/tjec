/************************************************************
 * 类名：ComboboxDataRepoints.java
 *
 * 类别：组件类
 * 功能：下拉框DataRepoints类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

public class ComboboxDataRepoints {
	String elementcode = "";
	String url = "base/dic/dicElementValSetController/findEnumDicELementVals.do?elementcode=";
	String method = "POST";
	String valueField = "id";
	String textField = "text";
	String panelHeight = "auto";
	String formatter = "function(row) {var opts = $(this).combobox('options');return row[opts.textField];}";
	
	public ComboboxDataRepoints() {
		
	}
	
	public ComboboxDataRepoints(String elementcode) {
		this.elementcode = elementcode;
	}
	
	public String toString() {
		return "url:'" + url + elementcode + "', method:'" + method + "', valueField:'" + valueField + "', textField:'" + textField + "', panelHeight:'" + panelHeight + "'";//, formatter:" + formatter;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getValueField() {
		return valueField;
	}
	public void setValueField(String valueField) {
		this.valueField = valueField;
	}
	public String getTextField() {
		return textField;
	}
	public void setTextField(String textField) {
		this.textField = textField;
	}
	public String getPanelHeight() {
		return panelHeight;
	}
	public void setPanelHeight(String panelHeight) {
		this.panelHeight = panelHeight;
	}
	public String getFormatter() {
		return formatter;
	}
	public void setFormatter(String formatter) {
		this.formatter = formatter;
	}	
}
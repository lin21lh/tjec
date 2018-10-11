/************************************************************
 * 类名：EditSchemeDTO.java
 *
 * 类别：DTO
 * 功能：编辑界面DTO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

import java.util.List;
import java.util.Map;

import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;

public class EditSchemeDTO {

	Integer colcount = 2; //form 表单展示几列
	Integer labelcellwidth; //label 宽度
	Integer controlcellwidth; //控件宽度
	
	List<EditFieldDTO> editfields; //需维护属性字段
	
	SysDicTable dicTable;
	Map<String, SysDicColumn> columnMap;

	public EditSchemeDTO() {
		
	}
	
	public Integer getColcount() {
		return colcount;
	}

	public void setColcount(Integer colcount) {
		this.colcount = colcount;
	}
	
	public Integer getLabelcellwidth() {
		return labelcellwidth;
	}
	
	public void setLabelcellwidth(Integer labelcellwidth) {
		this.labelcellwidth = labelcellwidth;
	}
	
	public Integer getControlcellwidth() {
		return controlcellwidth;
	}
	
	public void setControlcellwidth(Integer controlcellwidth) {
		this.controlcellwidth = controlcellwidth;
	}
	
	public List<EditFieldDTO> getEditfields() {
		return editfields;
	}
	
	public void setEditfields(List<EditFieldDTO> editfields) {
		this.editfields = editfields;
	}
	
	public SysDicTable getDicTable() {
		return dicTable;
	}
	
	public void setDicTable(SysDicTable dicTable) {
		this.dicTable = dicTable;
	}
	
	public Map<String, SysDicColumn> getColumnMap() {
		return columnMap;
	}
	
	public void setColumnMap(Map<String, SysDicColumn> columnMap) {
		this.columnMap = columnMap;
	}
}

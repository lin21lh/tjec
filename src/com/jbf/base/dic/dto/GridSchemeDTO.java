/************************************************************
 * 类名：GridSchemeDTO.java
 *
 * 类别：DTO
 * 功能： 列表界面DTO
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

public class GridSchemeDTO {

	Integer checked = 0;
	String keyID = "";
	List<GridFieldDTO> gridField;
	
	Map<String, SysDicColumn> columnMap;
	
	public GridSchemeDTO() {
		
	}
	
	public GridSchemeDTO(List<GridFieldDTO> gridField) {
		this.gridField = gridField;
	}
	
	public List<GridFieldDTO> getGridField() {
		return gridField;
	}
	
	public void setGridField(List<GridFieldDTO> gridField) {
		this.gridField = gridField;
	}
	
	public Map<String, SysDicColumn> getColumnMap() {
		return columnMap;
	}
	
	public void setColumnMap(Map<String, SysDicColumn> columnMap) {
		this.columnMap = columnMap;
	}
}

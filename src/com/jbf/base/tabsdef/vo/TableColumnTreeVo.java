/************************************************************
 * 类名：TableColumnTreeVo.java
 *
 * 类别：VO
 * 功能：数据表字段列组装树VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-09-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.vo;

import com.jbf.common.vo.TreeVo;

public class TableColumnTreeVo extends TreeVo {

	String columncode;
	
	public String getColumncode() {
		return columncode;
	}
	
	public void setColumncode(String columncode) {
		this.columncode = columncode;
	}
}

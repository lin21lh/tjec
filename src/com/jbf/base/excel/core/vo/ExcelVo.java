/************************************************************
 * 类名：ExcelVo.java
 *
 * 类别：vo类
 * 功能：excel文件vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.vo;

import com.jbf.base.excel.core.excel.DataExceptionLevel;

public class ExcelVo implements BaseVo {
	String classname = null;// 读入Excel数据后，插入数据库前，对数据的处理全路径类名。
	DataExceptionLevel dataExceptionLevel;// 数据异常的处理方式

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public DataExceptionLevel getDataExceptionLevel() {
		return dataExceptionLevel;
	}

	public void setDataExceptionLevel(DataExceptionLevel dataExceptionLevel) {
		this.dataExceptionLevel = dataExceptionLevel;
	}

}

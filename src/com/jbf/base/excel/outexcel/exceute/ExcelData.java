/************************************************************
 * 类名：ExcelData.java
 *
 * 类别：Excel导出数据接口类
 * 功能：获取需要导出的数据
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.outexcel.exceute;

import java.util.List;
import java.util.Map;

public interface ExcelData {

	public List getExcelData(Map paramMap);
}

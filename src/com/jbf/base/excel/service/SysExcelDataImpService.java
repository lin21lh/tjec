/************************************************************
 * 类名：SysExcelDataImpService
 *
 * 类别：Service
 * 功能：数据导入Service
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service;

import java.io.InputStream;
import java.util.Map;

public interface SysExcelDataImpService {
	/**
	 * 上传保存文件数据
	 * 
	 * @param map
	 *            包含导入模析信息的参数
	 * @param input
	 *            数据输入
	 * @return
	 * @throws Exception
	 */
	String uploadExcelDataFile(Map<String, String> map, InputStream input)
			throws Exception;

}

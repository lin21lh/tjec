/************************************************************
 * 类名：BizDataException.java
 *
 * 类别：异常类
 * 功能： 业务数据异常类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

public class BizDataException extends Exception {
	public BizDataException(String msg) {
		super(msg);
	}
}

/************************************************************
 * 类名：DataExceptionLevel.java
 *
 * 类别：
 * 功能： 业务数据异常级别
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

public enum DataExceptionLevel {
	/**
	 * 继续插入数据，最后提示异常信息。
	 */
	insert,
	/**
	 * 抛出异常
	 */
	throwerr
}

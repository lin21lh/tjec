/************************************************************
 * 类名：EasyUITotalResult.java
 *
 * 类别：通用类
 * 功能：EasyUI分页支持类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   mqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao;

import java.util.List;

public class EasyUITotalResult {

	private int total; // 总数
	private List rows; // 数据列表

	private List footer;
	public EasyUITotalResult() {
	}

	/**
	 * 由PaginationSupport对象生成支持easyui分页的数据对象
	 * 
	 * @param pSupport
	 * @return
	 */
	public static EasyUITotalResult from(PaginationSupport pSupport) {
		return new EasyUITotalResult(pSupport);
	}

	public EasyUITotalResult(PaginationSupport pSupport) {
		this.total = pSupport.getTotalCount();
		this.rows = pSupport.getItems();
		this.footer = pSupport.getSumFooter();
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}

	public void setFooter(List footer) {
		this.footer = footer;
	}

	public List getFooter() {
		return footer;
	}
}

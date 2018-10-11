/************************************************************
 * 类名：SqlVO.java
 *
 * 类别：VO
 * 功能：Sql语句VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import java.util.List;

public class SqlVO {
	private String updateSql = null;
	private String insertSql = null;
	private String sql = null;

	private List<ColVO> cols = null;
	private List<ColVO> keys = null;

	String tablename;

	public Object clone() {
		SqlVO vo = new SqlVO();

		vo.setInsertSql(this.insertSql);
		vo.setUpdateSql(this.updateSql);
		vo.setSql(this.sql);
		vo.setCols(this.cols);
		vo.setKeys(this.keys);
		vo.setTablename(this.tablename);
		return vo;

	}

	public List<ColVO> getKeys() {
		return keys;
	}

	public void setKeys(List<ColVO> keys) {
		this.keys = keys;
	}

	public List<ColVO> getCols() {
		return cols;
	}

	public void setCols(List<ColVO> cols) {
		this.cols = cols;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

}

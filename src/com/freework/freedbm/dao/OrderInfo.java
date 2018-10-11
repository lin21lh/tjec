package com.freework.freedbm.dao;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;

public class OrderInfo {
	private String colName;
	private String sortorder="asc";
	
	public OrderInfo(FieldInfoEnum colName){
		this.colName = colName.getFieldInfo().getColName();
	}

	public OrderInfo(FieldInfoEnum colName, Sort sortorder) {
		super();
		this.colName =colName.getFieldInfo().getColName();
		this.sortorder = sortorder.name();
	}
	public OrderInfo(String colName){
		
		this.colName = colName;
	}
	public OrderInfo(String colName, Sort sortorder) {
		super();
		this.colName = colName;
		this.sortorder = sortorder.name();
	}
	
	public OrderInfo(String colName, String sortorder) {
		super();
		this.colName = colName;
		this.sortorder = sortorder;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getSortorder() {
		return sortorder;
	}
	public void setSortorder(Sort sortorder) {
		this.sortorder = sortorder.name();
	}
	
	
public	enum Sort{
		asc,desc
	}
}

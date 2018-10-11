package com.freework.freedbm.bean;

import java.util.List;

import com.freework.freedbm.dao.jdbcm.Param;

public class WhereResult {
	private List<Param>  params;
	private StringBuilder  where;
	private String order="";
	public WhereResult(List<Param> params, StringBuilder where) {
		super();
		this.params = params;
		this.where = where;
	}

	public WhereResult(){}

	public List<Param> getParams() {
		return params;
	}
	public void setParams(List<Param> params) {
		this.params = params;
	}
	public String getWhere() {
		return where.toString();
	}
	public boolean isWhereBlank(){
		return where==null|| where.length()==0;
	}
	public void setWhere(StringBuilder where) {
		this.where = where;
	}
	public void and(String where){
		this.where=	this.where.append(" and (").append(where).append(")");
		
	}
	public void and(StringBuilder where){
		this.where=	this.where.append(" and (").append(where).append(")");
		
	}
	public void order(String sql){
		order=sql;
		
	}

	public String getOrder() {
		return order;
	}
	
}

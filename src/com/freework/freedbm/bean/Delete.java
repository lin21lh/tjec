package com.freework.freedbm.bean;

import java.util.Collection;
import java.util.LinkedList;

import com.freework.freedbm.dao.jdbcm.Param;

public class Delete extends AbstDBExecute {
	private StringBuilder sql = new StringBuilder();
	private Criteria criteria;
	public Delete(String table) {
		sql.append("delete from  ").append(table);
	}
	public Delete where(String colName, Object value) {
		return this.where(colName, "=", value);
	}
	public Delete and(String colName, Object value){
		return this.and(colName,"=", value);
	}
	public Delete or(String colName, Object value){
		return this.or(colName, "=", value);
	}
	public Delete and(String colName, String than, Object value){
		return this.condition("and", colName, than, value);
	}
	public Delete or(String colName, String than, Object value){
		return this.condition("or", colName, than, value);
	}
	
	
	public Delete and(Criteria criteria){
		return this.criteria("and", criteria);
	}
	public Delete or(Criteria criteria){
		return this.criteria("or", criteria);
	}
	private Delete condition(String relation,String colName, String than, Object value) {
		if(criteria==null){
			criteria=new Criteria(" where ",colName,than,value);
		}else{
			this.criteria.condition(relation, colName, than, value);
		}
		return this;
	}
	public Delete criteria(String relation,Criteria criteria){
		if(criteria==null){
			criteria=new Criteria(" where ",criteria);
		}else{
			this.criteria.criteria(relation, criteria);
		}
		return this;
	}
	public Delete where(String sql) {
		if(criteria==null){
			criteria=new Criteria(sql);
		}else{
			criteria.addSql(sql);
		}
		return this;
	}

	
	public Delete where(String colName, String than, Object value) {
		if(criteria==null){
			criteria=new Criteria("where",colName,than,value);
		}
		return this;
	}

	public String sql() {
		if(criteria != null){
			return sql+" where "+criteria.sql();
		}
		return sql.toString();
	}
	public Collection<? extends Param> getValues() {
		if (criteria == null) {
			return new LinkedList<ParamValue>();
		} else {
			return  criteria.getValues();
		}
	}

}

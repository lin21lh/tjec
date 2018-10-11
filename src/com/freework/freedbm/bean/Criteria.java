package com.freework.freedbm.bean;

import java.util.LinkedList;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.dao.jdbcm.Param;
public class Criteria extends Order{
	
	private StringBuilder sql = new StringBuilder();
	private LinkedList<Param> values = new LinkedList<Param>();
	
	
	public StringBuilder getSql() {
		return sql;
	}

	public String sql(){
		return sql.toString()+this.getOrder();
	}
	public LinkedList<Param> getValues() {
		return values;
	}
	Criteria(String sql){
		this.addSql(sql);
	}
	 Criteria(String relation,String colName, String than, Object value){
		 this.condition(relation, colName, than, value);
		 
	 }
	 public Criteria(String relation,Criteria criteria){
		 this.criteria(relation, criteria);
		 
	 }
	public Criteria(){}
	public Criteria(String colName, Object value){
			this(colName,"=",value);
	}
	public Criteria(FieldInfoEnum colName, Object value){
		this(colName.getFieldInfo().getColName(),"=",value);
}
 	public Criteria(String colName, String than, Object value){
		sql.append(colName).append(than).append("?");
		values.add(new ParamValue(colName, value));
	}
	

	public Criteria addSql(String sqlStr){
		sql.append(sqlStr);
		return this;
	}
	public Criteria and(Criteria criteria){
		return this.criteria("and", criteria);
	}
	public Criteria or(Criteria criteria){
		return this.criteria("or", criteria);
	}
	public Criteria and(String colName, Object value){
		return this.and(colName,"=", value);
	}
	public Criteria or(String colName, Object value){
		return this.or(colName, "=", value);
	}
	public Criteria and(String colName, String than, Object value){
		return this.condition("and", colName, than, value);
	}
	public Criteria or(String colName, String than, Object value){
		return this.condition("or", colName, than, value);
	} 
	
	
	public Criteria and(FieldInfoEnum colName, Object value){
		return this.and(colName,"=", value);
	}
	public Criteria or(FieldInfoEnum colName, Object value){
		return this.or(colName, "=", value);
	}
	public Criteria and(FieldInfoEnum colName, String than, Object value){
		return this.condition("and", colName.getFieldInfo().getColName(), than, value);
	}
	public Criteria or(FieldInfoEnum colName, String than, Object value){
		return this.condition("or", colName.getFieldInfo().getColName(), than, value);
	} 
	
	
	public Criteria criteria(String relation,Criteria criteria){
		if(sql.length()!=0)
		   sql.append(" ").append(relation).append(" ");
		sql.append("(").append(criteria.getSql()).append(")");
		values.addAll(criteria.getValues());
		return this;
	}
	 Criteria condition(String relation,String colName, String than, Object value) {
		 if(sql.length()!=0)
			sql.append(" ").append(relation).append(" ");
		sql.append(colName).append(than).append("?");
		values.add(new ParamValue(colName, value));
		return this;
	}

	public WhereResult toWhereResult(){
		WhereResult whereResult=new WhereResult();
		whereResult.setParams(this.getValues());
		whereResult.setWhere(sql);
		whereResult.order(this.getOrder());
		return whereResult;
		
	}
}

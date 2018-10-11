package com.freework.freedbm.bean;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.freework.freedbm.dao.jdbcm.Param;
public class Update   extends  AbstDBExecute{
	private StringBuilder sql=new StringBuilder();
	private LinkedList<ParamValue> values=new LinkedList<ParamValue>();
	private Criteria criteria;

	public Update(String table){
		sql.append("update  ").append(table).append(" set ");
	}
	

	public Update set(String colName,Object value ){
		if(values.size()!=0){
			ParamValue paramValue=this.get(values, colName);
			if(paramValue!=null){
				paramValue.setValue(value);
				return this;
			}
			sql.append(",");
		}
		
		
		sql.append(colName).append("=").append("?");
		values.add(new ParamValue(colName,value));
		return this;
	}
	public Update where(String colName,Object value ){
		return this.where(colName, "=", value);
	}
	
	
	public Update where(String sql){
		if(criteria==null){
			criteria=new Criteria(sql);
		}else{
			criteria.addSql(sql);
		}
		return this;
	}
	public Update and(String colName, Object value){
		return this.condition("and",colName,"=", value);
	}
	public Update or(String colName, Object value){
		return this.condition("or",colName, "=", value);
	}
	public Update and(String colName, String than, Object value){
		return this.condition("and", colName, than, value);
	}
	public Update or(String colName, String than, Object value){
		return this.condition("or", colName, than, value);
	}
	private Update condition(String relation,String colName, String than, Object value) {
		if(criteria==null){
			criteria=new Criteria(" where ",colName,than,value);
		}else{
			this.criteria.condition(relation, colName, than, value);
		}
		return this;
	}
	public Update and(Criteria criteria){
		return this.criteria("and", criteria);
	}
	public Update or(Criteria criteria){
		return this.criteria("or", criteria);
	}
	public Update criteria(String relation,Criteria criteria){
		if(criteria==null){
			criteria=new Criteria(" where ",criteria);
		}else{
			this.criteria.criteria(relation, criteria);
		}
		return this;
	}
	
	
	public Update where(String colName,String than,Object value ){
		if(criteria==null){
			criteria=new Criteria(" where ",colName,than,value);
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
		 if(criteria==null){
			 return this.values;
		 }else{
			 Iterator<ParamValue> valueParams= this.values.iterator();
			 Iterator<Param> criteriaParams= this.criteria.getValues().iterator();
			return new MergeCollection<Param>(valueParams,criteriaParams,this.values.size()+criteria.getValues().size());
		 }
	}
	
	
	 
	 
}

package com.freework.freedbm.bean;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Insert extends  AbstDBExecute{
	private StringBuilder sql1;
	private StringBuilder sql2;
	private boolean first=true;
	private List<ParamValue> values=new LinkedList<ParamValue>();
	public Insert(String table){
		 sql1=new StringBuilder("insert into ").append(table);
		 sql2=new StringBuilder(" values ");
		
		
	}
	public Insert setSQL(String colName,String value ){
		if(first){
			
			sql1.append(" (");
			sql2.append(" (");
			first=false;
		}else{
			
			sql1.append(" , ");
			sql2.append(" , ");
		}
		sql1.append(colName);
		sql2.append(value);
		return this;
	}
	public Insert add(String colName,Object value ){
		if(first){
			sql1.append(" (");
			sql2.append(" (");
			first=false;
		}else{
			sql1.append(" , ");
			sql2.append(" , ");
		}
		sql1.append(colName);
		sql2.append("?");
		values.add(new ParamValue(colName,value));
		return this;
	}
	
	public Insert set(String colName,Object value ){
		if(first){
			sql1.append(" (");
			sql2.append(" (");
			first=false;
		}else{
			ParamValue paramValue=this.get(values, colName);
			if(paramValue!=null){
				paramValue.setValue(value);
				return this;
			}
			sql1.append(" , ");
			sql2.append(" , ");
		}
		sql1.append(colName);
		sql2.append("?");
		values.add(new ParamValue(colName,value));
		return this;
		
	}
	public Collection<ParamValue> getValues() {
		return values;
	}

	
	public String sql() {
		StringBuilder  sql=new StringBuilder(sql1.length()+sql2.length()+2).append(sql1).append(")").append(sql2).append(")");

		return sql.toString();
	}


	
	
	

}

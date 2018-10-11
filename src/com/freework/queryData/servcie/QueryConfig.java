package com.freework.queryData.servcie;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.queryData.compileSQL.CompileSQL;

public class QueryConfig {
	private CompileSQL sql;
	private ArrayList<TableQuery> querys=new ArrayList<TableQuery>(1);
	private LinkedList<Class> classes=new LinkedList<Class>();
	private Object userData;
	private TableQuery singletonQuery;
	private Set<String> params;
	private QueryService execute;
	public Set<String> getParams() {
		if(params!=null)
			return params;
		else if(sql!=null)
			return sql.getParamNames();
		else
			return null;
	}
	public void setParams(Set<String> params) {
		this.params = params;
	}
	public QueryService getExecute() {
		return execute;
	}
	public void setExecute(QueryService execute) {
		this.execute = execute;
	}
	public Object getUserData() {
		return userData;
	}
	public void setUserData(Object userData) {
		this.userData = userData;
	}
	public void setSingletonQuery(TableQuery singletonQuery) {
		this.singletonQuery = singletonQuery;
	}
	
	
	public CompileSQL getSql() {
		return sql;
	}
	public void setSql(CompileSQL sql) {
		this.sql = sql;
	}

	public  void addTableQuery(Class clazz,TableQuery query){
		synchronized(classes){
			querys.add(query);
			classes.add(clazz);

		}
		
	}
	
	public  TableQuery getQuery(ResultSetMetaData rsmd,Class clazz)
			throws SQLException {
			if(singletonQuery!=null){
				return singletonQuery;
			}
			
			if(clazz==null)
				clazz=Map.class;
			synchronized(classes){
				int index=classes.indexOf(clazz);
				if(index==-1){
					TableQuery query=execute.getQueryInfo(this, rsmd, clazz);
					querys.add(query);
					classes.add(clazz);

					return query;
				
				}else{
					return querys.get(index);
				}
			}
	}

}

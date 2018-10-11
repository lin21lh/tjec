package com.freework.queryData.servcie.tree;

import java.util.Map;

import com.freework.queryData.compileSQL.CompileSQLFactory;
import com.freework.queryData.servcie.QueryConfig;
import com.freework.queryData.servcie.QueryService;
import com.freework.queryData.servcie.tree.TreeParserFactory.TreeInfo;
public  abstract class AbstractTreeParser {

	protected TreeInfo info;
	protected String sql;
	protected CompileSQLFactory sqlFactory;
	public void setSqlFactory(CompileSQLFactory sqlFactory) {
		this.sqlFactory = sqlFactory;
	}
	protected  abstract  String getQueryServiceName();
	public  QueryConfig getConfig(QueryService execute){
		QueryConfig queryConfig= queryConfig();
		queryConfig.setExecute(execute);
		return queryConfig;
		
	};
	
	public  QueryConfig getConfig(Map<String,QueryService> treeService){
		return getConfig(treeService.get(getQueryServiceName()));
		
	};
	protected  abstract  QueryConfig queryConfig();
	
	public String getSQL() {
		return sql;
	}
	public void setSQL(String sql) {
		this.sql = sql;
	}

	public TreeInfo getInfo() {
		return info;
	}
	public void setInfo(TreeInfo info) {
		this.info = info;
	}


	
	
	
	
	
}

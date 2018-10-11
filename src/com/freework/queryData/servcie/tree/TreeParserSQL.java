package com.freework.queryData.servcie.tree;


import com.freework.queryData.servcie.QueryConfig;

public class TreeParserSQL extends AbstractTreeParser  {
	
	@Override
	protected String getQueryServiceName() {
		return "treeSQLService";
	}
	@Override
	protected QueryConfig queryConfig() {
		QueryConfig cfg = new QueryConfig();
		cfg.setSql(sqlFactory.getCompileSQL(sql));
		cfg.setUserData(this.getInfo());
		return cfg;
	}


}

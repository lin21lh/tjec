package com.freework.freedbm.cfg.tablecfg;

import com.freework.freedbm.cfg.id.IdentifierGenerator;

public interface TableDataManager extends TableQuery {
	public  String getInsertsql();
	public  String getInsertSQLKey() ;
	public  String getUpdatesql();
	public String getTableName();
	public IdentifierGenerator getIdentifierGenerator();


}

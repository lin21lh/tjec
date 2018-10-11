package com.freework.freedbm;
import com.freework.freedbm.cfg.dbsqltype.BuilderSQL;
public class DatabaseType {
	public void setDbType(BuilderSQL dbType){
		((BuilderSQLProxy)Cfg.DB_TYPE).setProxyDefault(dbType);
		
	}
}

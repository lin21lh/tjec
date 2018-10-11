package com.freework.freedbm.cfg.annotation;

import java.util.Map;

import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public abstract class TableCfg implements TableDataManager {
	 JdbcForDTO pKey[] =null;
	 JdbcForDTO fields[] = null;
	 Class dtoClass=null;
	 String insertSQLKey=null;
	 String insertsql=null;
	 String tableName=null;
	 String updatesql=null;
	 String querysql=null;
	 Map<String,JdbcForDTO> map=null;
	
	public java.lang.String getInsertSQLKey() {
		return insertSQLKey;
	}

	public java.lang.String getInsertsql() {
		return insertsql;
	}

	public java.lang.String getTableName() {
		return tableName;
	}

	public java.lang.String getUpdatesql() {
		return updatesql;
	}

	public java.lang.String getQuerysql() {
		return querysql;
	}

	public	abstract  Object newInstance();

	public Class getDTOClass() {
		return dtoClass;
	}

	public JdbcForDTO getField(java.lang.String name) {
		return map.get(name);
	}

	public JdbcForDTO[] getFields() {
		return fields;
	}

	public JdbcForDTO[] getPKey() {
		return pKey;
	}
	
	 void setFields(JdbcForDTO[] fields){
		 
		 this.fields=fields;
	 }

}

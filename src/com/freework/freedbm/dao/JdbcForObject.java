package com.freework.freedbm.dao;

import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.Type;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public class JdbcForObject implements JdbcForDTO {
	String colName = null;
	SQLType type;

	
	
	public JdbcForObject(String colName, SQLType type) {
		this.colName=colName;
		this.type=type;

	}

	
	public JdbcForObject(SQLType type) {
		this.colName=colName;
		this.type=type;

	}
	
	public String getColName() {
		return colName;
	}

	public Like getLike() {
		return null;
	}

	public Type getType() {
		return type;
	}

	public Object getValue(Object obj) {
		return obj;
	}

	public boolean isDbCol() {
		return true;
	}

	public boolean isKey() {
		return false;
	}

	public void setValue(Object obj, Object Value) {
	}


	public String getName() {
		return null;
	}


	public Object getDefVal() {
		return null;
	}

}

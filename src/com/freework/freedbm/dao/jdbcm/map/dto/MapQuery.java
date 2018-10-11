package com.freework.freedbm.dao.jdbcm.map.dto;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.unmodifiableMap.KeysIndex;

public class MapQuery extends KeysIndex implements TableQuery {

	String querySQL=null;
	 JdbcForDTO[] fields=null;
	public MapQuery(String querySQL, JdbcForDTO[] fields) {
		this.querySQL = querySQL;
		this.fields = fields;
		String fieldName[]=new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldName[i]=fields[i].getName();
		}
		this.setNames(fieldName);
	}
	public void setQuerysql(String querySQL) {
		this.querySQL=querySQL;
	}
	public String getQuerysql() {
		return querySQL;
	}

	public Object newInstance() {
		return new MapDTO(this);
	}

	public Class getDTOClass() {
		return MapDTO.class;
	}

	public JdbcForDTO getField(java.lang.String name) {
		Integer index=this.getIndex(name);
		if(index==null)
			return null;
		else
			return fields[index];
			
	}
	public JdbcForDTO[] getFields() {
		return fields;
	}

	public JdbcForDTO[] getPKey() {
		return null;
	}

	public java.lang.String getTableName() {
		return null;
	}

}

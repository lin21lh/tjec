package com.freework.freedbm.dao.jdbcm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.tablecfg.CreateObject;
import com.freework.freedbm.cfg.tablecfg.TableCfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.JdbcForObject;
import com.freework.freedbm.dao.jdbcm.Param.p;

/**
 * @author ≥Ã∑Á¡Î
 * @category
 
 */
public class QueryInfo {
	int start;
	int limit;
	String querySelect = null;
	boolean ispage = false;
	Object dto;
	JdbcForDTO[] jdbcForDTOs;
	List<Param> params = null;
	CreateObject newInstanceDTO = null;

	public QueryInfo() {
		super();
	}

	public QueryInfo(CreateObject newInstanceDTO) {
		super();
		this.newInstanceDTO = newInstanceDTO;
	}

	public List<Param> getParams() {
		return params;
	}

	public void setParam(SQLType type, Object value) {
		if (params == null)
			params = new ArrayList<Param>(5);
		else if (!(params instanceof ArrayList)) {
			List tmp = new ArrayList<Param>(params.size() + 5);
			tmp.addAll(params);
			params = tmp;
		}
		params.add(p.param(type, value));
	}

	public JdbcForDTO[] getJdbcForDTOs() {
		return jdbcForDTOs;
	}

	public void setJdbcForDTOs(JdbcForDTO[] jdbcForDTOs) {
		this.jdbcForDTOs = jdbcForDTOs;
	}

	public boolean isIspage() {
		return ispage;
	}

	public String getQuerySelect() {

		return ispage ? TableCfg.DB_TYPE.getLimitString(querySelect, start,
				limit) : querySelect;
	}

	public String getOldQuerySelect() {

		return querySelect;
	}

	public void setQuerySelect(String querySelect) {
		this.querySelect = querySelect;
	}

	void setStartResultSet(ResultSet rset) throws SQLException {
		if (ispage)
			TableCfg.DB_TYPE.setStartResultSet(rset, start);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		ispage = true;
		this.limit = limit;
	}

	public boolean isScrollInsensitive() {
		return ispage ? TableCfg.DB_TYPE.isScrollInsensitive() : false;
	}

	public CreateObject getNewInstanceDTO() {
		return newInstanceDTO;
	}

	public static QueryInfo getQueryInfo(String querySelect, SQLType sqlType,
			List<Param> params) {
		QueryInfo count = new QueryInfo();
		count.setQuerySelect(querySelect);
		count.setJdbcForDTOs(new JdbcForDTO[] { new JdbcForObject(sqlType) });
		if (params != null) {
			count.params = params;
		}
		return count;
	}

	public static QueryInfo getQueryInfo(JdbcForDTO[] jdbcForDTOs,
			String querySQL, TableQuery newInstanceDTO, List<Param> params) {
		QueryInfo qi = new QueryInfo(newInstanceDTO);
		qi.setJdbcForDTOs(jdbcForDTOs);
		qi.setQuerySelect(querySQL);
		if (params != null)
			qi.params = params;
		return qi;
	}

	public void setParams(List<Param> params) {
		this.params = params;
	}
	public static QueryInfo getQueryInfo(TableQuery tablecfg, String where,String order,List<Param> params2) {
		// TODO Auto-generated method stub
		StringBuilder querySQL=new StringBuilder(tablecfg.getQuerysql());
		if(where!=null&&!where.equals("")){
			querySQL.append(" where ").append(where).append(" ").append(order);
		}else{
			querySQL.append(order);
		}
		return getQueryInfo(tablecfg.getFields(), querySQL.toString(), tablecfg, params2);
	}

	public static QueryInfo getQueryInfo(TableQuery tablecfg, String where,
			List<Param> params2) {
		// TODO Auto-generated method stub
		String querySQL = tablecfg.getQuerysql();
		if (where != null && !where.equals("")){
			querySQL += " where " + where;
		}
		return getQueryInfo(tablecfg.getFields(), querySQL, tablecfg, params2);
	}

}

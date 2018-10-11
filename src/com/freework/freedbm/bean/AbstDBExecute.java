package com.freework.freedbm.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public  abstract class   AbstDBExecute   implements DBExecute {
	private JdbcDaoSupport dao;

	
	public void setDao(JdbcDaoSupport dao) {
		this.dao = dao;
	}
	@Override
	public int execute(Connection conn) throws SQLException {
		return DBUtil.execute(conn,sql(), getValues());

	}
	public int execute() {
		
		return  dao.getJdbcTemplate().execute(
				new ConnectionCallback<Integer>() {
					public Integer doInConnection(final Connection conn)
							throws SQLException {
						return AbstDBExecute.this.execute(conn);
					}
				});
	}
	
	public ParamValue get(List<ParamValue> list,String name){
		
		for (ParamValue paramValue : list) {
			if(paramValue.getName().equals(name)){
				return paramValue;
			}
		}
		return null;
		
	}

}

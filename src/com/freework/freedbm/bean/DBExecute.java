package com.freework.freedbm.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import com.freework.freedbm.dao.jdbcm.Param;

public interface DBExecute {
	public String sql();
	public Collection<? extends Param> getValues();
	public int execute(Connection conn) throws SQLException;
}

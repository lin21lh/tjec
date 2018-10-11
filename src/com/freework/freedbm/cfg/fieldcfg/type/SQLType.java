package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public interface SQLType extends Type {

	String objectToSQLString(Object value);
	public int sqlType();
	public void set(PreparedStatement st, Object value, int index)throws SQLException;
	public Object get(ResultSet rs, String name)throws SQLException;
	public Object get(ResultSet rs, int index)throws SQLException;

}

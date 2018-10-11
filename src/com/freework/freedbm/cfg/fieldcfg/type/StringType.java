package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class StringType implements SQLType {

	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getString(name);
	}
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getString(index);
	}
	public String objectToSQLString(Object value) {
		return '\'' + (String) value + '\'';
	}

	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setString(index, (String) value);

	}

	public int sqlType() {
		return Types.VARCHAR;
	}

	public String getName() { return "string"; }


	public Class getReturnedClass() {
		return String.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

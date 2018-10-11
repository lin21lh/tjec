package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NullType extends NullableType {

	@Override
	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setObject(index, null);
	}

	public Object get(ResultSet rs, String name) throws SQLException {
		return null;
	}

	public Object get(ResultSet rs, int index) throws SQLException {
		return null;
	}

	public String objectToSQLString(Object value) {
		return null;
	}

	public int sqlType() {
		return -1;
	}

	public String getName() {
		return "null";
	}

	public Class getReturnedClass() {
		return NullType.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

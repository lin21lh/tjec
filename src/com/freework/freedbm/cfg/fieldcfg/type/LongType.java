package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class LongType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		long l=rs.getLong(name);
		if(rs.wasNull())
			return null;
			else
		return new Long(l);
	}
	public Object get(ResultSet rs, int index) throws SQLException {
		long l=rs.getLong(index);
		if(rs.wasNull())
			return null;
			else
		return new Long(l);
	}

	public String objectToSQLString(Object value) {
		return value.toString();
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setLong( index, ( (Long) value ).longValue() );

	}

	public int sqlType() {
		return Types.BIGINT;
	}

	public String getName() { return "long"; }

	public Class getReturnedClass() {
		// TODO Auto-generated method stub
		return Long.class;
	}

	public boolean isCollectionType() {
		// TODO Auto-generated method stub
		return false;
	}

}

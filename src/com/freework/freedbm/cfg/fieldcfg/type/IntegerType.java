package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class IntegerType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		// TODO Auto-generated method stub
		int i=rs.getInt(name);
		if(rs.wasNull())
			return null;
			else
		return new Integer(i);
	}
	public Object get(ResultSet rs, int index) throws SQLException {
		int i=rs.getInt(index);
		if(rs.wasNull())
			return null;
			else
		return new Integer(i);
	}
	public String objectToSQLString(Object value) {
		// TODO Auto-generated method stub
		return value.toString();
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
	
			st.setInt( index, ( (Integer) value ).intValue() );

	}

	public int sqlType() {
		return Types.INTEGER;
	}

	public String getName() { return "integer"; }

	public Class getReturnedClass() {
		return Integer.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

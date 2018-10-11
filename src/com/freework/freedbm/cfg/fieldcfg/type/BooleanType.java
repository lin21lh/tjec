package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class BooleanType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		// TODO Auto-generated method stub
		boolean b=rs.getBoolean(name);
		if(rs.wasNull())
			return null;
			else
		return new Boolean(b);
	}
	public Object get(ResultSet rs, int index) throws SQLException {
		boolean b=rs.getBoolean(index);
		if(rs.wasNull())
			return null;
			else
		return new Boolean(b);
	}
	public String objectToSQLString(Object value) {
		if (value instanceof Boolean) {
			return value.toString();
		}else
		if (value instanceof String) {
			return Boolean.parseBoolean((String)value)?"true":"false";
		}
		if(value==null)
			return "false";
		else
			return Boolean.parseBoolean(value.toString())?"true":"false";
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
	
			st.setBoolean( index, ( (Boolean) value ).booleanValue() );

	}

	public int sqlType() {
		return Types.BOOLEAN;
	}

	public String getName() { return "boolean"; }

	public Class getReturnedClass() {
		return Boolean.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class ByteType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		byte b=rs.getByte(name);
		
		if(rs.wasNull())
			return null;
			else
		return new Byte(b);
	}

	public Object get(ResultSet rs, int index) throws SQLException {
		byte b=rs.getByte(index);
		if(rs.wasNull())
			return null;
			else
		return new Byte(b);
	}
	
	public String objectToSQLString(Object value) {
		return value.toString();
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
		st.setByte( index, ( (Byte) value ).byteValue() );

	}

	public int sqlType() {
		return Types.TINYINT;
	}

	public String getName() { return "byte"; }


	public Class getReturnedClass() {
		return Byte.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

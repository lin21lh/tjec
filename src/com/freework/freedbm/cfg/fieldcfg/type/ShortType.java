package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class ShortType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		short s=rs.getShort(name);
		if(rs.wasNull())
			return null;
			else
		return new Short(s);
 		}

	public Object get(ResultSet rs, int index) throws SQLException {
		short s=rs.getShort(index);
		if(rs.wasNull())
			return null;
			else
		return new Short(s);
	}

	public String objectToSQLString(Object value) {
		return value.toString();
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {
 		 st.setShort(index,( (Short)value).shortValue());

	}

	public int sqlType() {
		return Types.SMALLINT;
	}

	public String getName() { return "short"; }


	public Class getReturnedClass() {
		return Short.class;
	}

	public boolean isCollectionType() {
		return false;
	}

}

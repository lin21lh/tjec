package com.freework.freedbm.cfg.fieldcfg.type;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.freework.base.util.FormatUtil;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class DoubleType extends NullableType {

	public Object get(ResultSet rs, String name) throws SQLException {
		double d=rs.getDouble(name);
		if(rs.wasNull())
			return null;
			else
		return new Double(d);
	}
	public Object get(ResultSet rs, int index) throws SQLException {
		double d=rs.getDouble(index);
		if(rs.wasNull())
			return null;
			else
		return new Double(d);
		}
	public String objectToSQLString(Object value) {
		// TODO Auto-generated method stub
		return this.toString((Double)value);
	}

	public void setNotNull(PreparedStatement st, Object value, int index)
			throws SQLException {

		st.setDouble(index, ((Double) value).doubleValue() );

	}

	public int sqlType() {
		// TODO Auto-generated method stub
		return Types.DOUBLE;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "double";
	}

	public Class getReturnedClass() {
		// TODO Auto-generated method stub
		return Double.class;
	}

	public boolean isCollectionType() {
		// TODO Auto-generated method stub
		return false;
	}

	public Double stringToObject(String str) {
		// TODO Auto-generated method stub
		return new Double(str);
	}

	public String toString(Double value) {
		// TODO Auto-generated method stub
		 return FormatUtil.getNumber2(value);
	}



}

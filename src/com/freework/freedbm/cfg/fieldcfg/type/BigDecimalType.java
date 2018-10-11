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
public class BigDecimalType implements SQLType, FormatType<BigDecimal> {

	
	
	
	public Object get(ResultSet rs, String name) throws SQLException {
		 		return rs.getBigDecimal(name);
	}
	
	public Object get(ResultSet rs, int index) throws SQLException {
		return rs.getBigDecimal(index);
	}
	public void set(PreparedStatement st, Object value, int index) throws SQLException {
		st.setBigDecimal(index,(BigDecimal) value);
		
	}
	
	public String objectToSQLString(Object value) {
		return value.toString();
	}
	

	public int sqlType() {
		return Types.NUMERIC;
	}

	public String getName() {
		return "big_decimal";
	}
	public Class getReturnedClass() {
		return BigDecimal.class;
	}

	public boolean isCollectionType() {
		return false;
	}

	public BigDecimal stringToObject(String str) {
		return new BigDecimal(str);
	}



	public String toString(BigDecimal value) {
		 return FormatUtil.getNumber2(value);
	}
	

	
}

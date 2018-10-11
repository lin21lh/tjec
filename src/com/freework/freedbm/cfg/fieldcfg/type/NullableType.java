package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public abstract class NullableType implements SQLType {

	public void set(PreparedStatement st, Object value, int index)
	throws SQLException {

		
		if(value!=null)
			this.setNotNull(st, value, index);
		else
			st.setNull(index, this.sqlType());
	
	}
	public abstract void setNotNull(PreparedStatement st, Object value, int index)throws SQLException ;



}

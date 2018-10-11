package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.sql.Date;

import com.freework.base.util.FormatUtil;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class TimestampType implements SQLType, FormatType<java.sql.Timestamp> {

	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getTimestamp(name);
	}
	public Object get(ResultSet rs,int index ) throws SQLException {
		return rs.getTimestamp(index);
	}

	public String objectToSQLString(Object value) {
		return '\'' + new Date( ( (java.sql.Timestamp) value ).getTime() ).toString() + '\'';
	}

	
	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		java.sql.Timestamp sqlDate=null;
		if ( value instanceof java.sql.Timestamp) {
			sqlDate = ( java.sql.Timestamp) value;
		} else if(value instanceof java.util.Date){
			sqlDate = new  java.sql.Timestamp( ( (java.util.Date) value ).getTime() );
		}
		st.setTimestamp(index, sqlDate);
	}

	public int sqlType() {
		return Types.TIMESTAMP;
	}

	// TODO Auto-generated method stub
	public String getName() { return "timestamp"; }


	public Class getReturnedClass() {
		return java.util.Date.class;
	}

	public boolean isCollectionType() {
		return false;
	}

	public java.sql.Timestamp stringToObject(String str) {
			return new java.sql.Timestamp(FormatUtil.dateTime2(str).getTime());
		
	}

	public String toString(java.sql.Timestamp value) {
		return FormatUtil.stringDateTime2(value);
	}


}

package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.Date;

import com.freework.base.util.FormatUtil;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class DateType implements SQLType, FormatType<java.util.Date> {

	public Object get(ResultSet rs, String name) throws SQLException {
		return rs.getDate(name);
	}
	public Object get(ResultSet rs,int index ) throws SQLException {
		return rs.getDate(index);
	}

	public String objectToSQLString(Object value) {
		return '\'' + new Date( ( (java.util.Date) value ).getTime() ).toString() + '\'';
	}

	
	public void set(PreparedStatement st, Object value, int index)
			throws SQLException {
		 java.sql.Date sqlDate=null;
		if ( value instanceof java.sql.Date) {
			sqlDate = ( java.sql.Date) value;
		}
		else if(value!=null){
			sqlDate = new  java.sql.Date( ( (java.util.Date) value ).getTime() );
		}
		st.setDate(index, sqlDate);
	}

	public int sqlType() {
		return Types.DATE;
	}

	// TODO Auto-generated method stub
	public String getName() { return "date"; }


	public Class getReturnedClass() {
		return java.util.Date.class;
	}

	public boolean isCollectionType() {
		return false;
	}

	public java.util.Date stringToObject(String str) {
			return FormatUtil.dateTime2(str);
		
	}

	public String toString(java.util.Date value) {
		return FormatUtil.stringDateTime2((java.util.Date) value);
	}

}

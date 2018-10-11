package com.freework.freedbm.bean;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.dao.jdbcm.Param;


public class ParamValue extends Param{
	
	String name;
	
//	public ParamValue newParam(){
//		ParamValue that=new ParamValue(name,this.getValue());
//		return that;
//	}
	
	public ParamValue(String name,Object value,SQLType type){
		super(type,value);
		this.name=name;
		
	}
	public ParamValue(String name,Object value){
		super(SQLTypeMap.getSQLType(value),value);
		this.name=name;
		
	}
	public void setPreparedStatement(PreparedStatement statement,int index) throws SQLException{
		this.getType().set(statement, this.getValue(), index);
		
	}

	public String getName() {
		return name;
	}
	public void setValue(Object value) {
		super.setValue(value);
		super.setType(SQLTypeMap.getSQLType(value));

	}
	
	
}

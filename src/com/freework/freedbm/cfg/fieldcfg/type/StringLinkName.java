package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.Connection;
import java.sql.SQLException;

import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.type.LinkType.LinkInfo;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.SqlUtil;

public class StringLinkName implements LinkType {

	LinkInfo linkInfo=null;
	boolean isConnection=true;
	public StringLinkName(String linkColName, String linkValueColName,
			String sourceName,
			String tableName) {
		this.linkInfo = new LinkInfo(linkColName,linkValueColName, sourceName, tableName);
	}

	public LinkInfo getLinkInfo() {
		return linkInfo;
	}

	public Object getValue(Object obj,FieldInfo fi) {
		Object  sourceValue=fi.getValue(obj);
		return obj;
	}

	public String getName() {
		return "string";
	}

	public Class getReturnedClass() {
		return String.class;
	}

	public boolean isCollectionType() {
		return false;
	}

	public String getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs) {
		return null;
		
		
	}

	public Object getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs,
			Connection con) {
		String tablename=linkInfo.tableName;
		String code=linkInfo.linkValueColName;
		JdbcForDTO	source =null;
		for (int i = 0; i < jdbcForDTOs.length; i++) {
			if(jdbcForDTOs[i].getColName().equals(linkInfo.sourceName)){
				source=jdbcForDTOs[i];
				break;
			}
		}
		if(source==null){
			System.out.println("cfl:"+linkInfo.sourceName+"不存在数据!!!!!!");
			return null;
		}	
		boolean isString=source.getType().getReturnedClass()==String.class;
		
		Object  sourceValue=source==null?null:source.getValue(dto);
		return SqlUtil.querySingle(con,tablename,code,isString?linkInfo.linkColName+"='"+sourceValue+"'":linkInfo.linkColName+"="+sourceValue);
		
	}

	public boolean isConnection() {
		return isConnection;
	}

	

	




}

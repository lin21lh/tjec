package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.Connection;
import java.sql.SQLException;

import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.type.LinkType.LinkInfo;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.base.util.SqlUtil;

public class StringLinkNameByWhere extends StringLinkName {

	
	private String where;
	/**
	 * 
	 * @param linkValueColName 字典表 取值列名
	 * @param linkColName 字典表 关联列名
	 * @param sourceName 本表数据来源列
	 * @param tableName 字典表名
	 */
	public StringLinkNameByWhere( String linkValueColName,String linkColName,
			String sourceName, String tableName,String where) {
		super(linkColName, linkValueColName, sourceName, tableName);
		this.where=where;
	}

	public JdbcForDTO getJdbcForDTO(JdbcForDTO[] jdbcForDTOs,String sourceName){
		JdbcForDTO	source =null;
		for (int i = 0; i < jdbcForDTOs.length; i++) {
			if(jdbcForDTOs[i].getColName().equals(sourceName)){
				source=jdbcForDTOs[i];
				break;
			}
		}
		
		return source;
	}
	
	public Object getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs,
			Connection con) {
		String tablename=linkInfo.tableName;
		String code=linkInfo.linkValueColName;
		//------------------
		JdbcForDTO	source =getJdbcForDTO(jdbcForDTOs,linkInfo.sourceName);
		if(source==null){
			System.out.println("cfl:"+linkInfo.sourceName+"不存在数据!!!!!!");
		}	
		boolean isString=source.getType().getReturnedClass()==String.class;
		
		Object  sourceValue=source==null?null:source.getValue(dto);
		
	
		//------------------------
		
		
		StringBuilder where=new StringBuilder();
		where.append(linkInfo.linkColName).append("=");
		if(isString){
			where.append("'");
			where.append(sourceValue);
			where.append("'");
		}else{
			where.append(sourceValue);
		}
		
		where.append("  and ").append(this.where);
		return SqlUtil.querySingle(con,tablename,code,where.toString());
		
	}

	

	




}

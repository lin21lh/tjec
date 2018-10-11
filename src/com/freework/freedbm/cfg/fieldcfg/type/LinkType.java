package com.freework.freedbm.cfg.fieldcfg.type;

import java.sql.Connection;

import com.freework.freedbm.dao.jdbcm.JdbcForDTO;

public interface LinkType extends Type {

	boolean isConnection();
	Object getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs);
	Object getDTOValue(Object dto, JdbcForDTO[] jdbcForDTOs,Connection con);
	public class LinkInfo{
		
		public LinkInfo(String linkColName, String linkValueColName,
				String sourceName, String tableName) {
			super();
			this.linkColName = linkColName;
			this.linkValueColName = linkValueColName;
			this.sourceName = sourceName;
			this.tableName = tableName;
		}
		
		public String linkColName="";
		public String linkValueColName="";
		public String sourceName="";
		public String tableName="";
		
		
	}


}

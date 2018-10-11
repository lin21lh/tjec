package com.freework.queryData.dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.freework.freedbm.DTO;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOUtil;
import com.freework.freedbm.dao.jdbcm.map.dto.MapFieldInfo;
import com.freework.freedbm.dao.jdbcm.map.dto.MapQuery;
import com.freework.queryData.util.Query;
import com.freework.queryData.util.QueryDTO;


public class BuilerQuery {

	
	public static TableQuery  getQueryInfo(ResultSetMetaData rsmd,Class clazz) throws SQLException{
		if(clazz==null||Map.class.isAssignableFrom(clazz))
			return getMapQueryInfo(rsmd);
		return getObjectQueryInfo(rsmd, clazz);
	}
	
	
	public static TableQuery  getObjectQueryInfo(ResultSetMetaData rsmd,Class clazz) throws SQLException
	{
		int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
		List<String> colNames=new LinkedList<String>();
		for (int i = 1; i <= columnCount; i++) {
			String colName = rsmd.getColumnName(i);
			String name=MapDTOUtil.getFieldName(colName);
			colNames.add(name);
		}
		TableQuery queryInfo=null;
		if(DTO.class.isAssignableFrom(clazz)){
			 queryInfo=new QueryDTO(clazz,colNames);
		}else{
			 queryInfo=new Query(clazz,colNames);
		}
		return queryInfo;
		
	}


	public static TableQuery getMapQueryInfo(ResultSetMetaData rsmd)
			throws SQLException {
		int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
		MapFieldInfo[] field = new MapFieldInfo[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			//System.out.println(rsmd.getColumnLabel(i));
			String colName =rsmd.getColumnLabel(i);
			if(colName==null||colName.equals(""))
				colName=rsmd.getColumnName(i);
			field[i - 1] = new MapFieldInfo(i - 1,
					MapDTOUtil.getFieldName(colName), colName,
					MapDTOUtil.getSQLType(rsmd, i), true, false, null, "");

		}
		MapQuery queryInfo = new MapQuery(null, field);
		return queryInfo;
	}
	
	

}

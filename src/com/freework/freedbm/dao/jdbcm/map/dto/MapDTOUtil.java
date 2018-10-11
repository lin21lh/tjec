package com.freework.freedbm.dao.jdbcm.map.dto;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.map.dao.MapDaoSupport;
import com.freework.base.util.SqlUtil;
public class MapDTOUtil {

	
	private static Hashtable<String,MapDTOCfg> columnsTableInfo=new Hashtable<String,MapDTOCfg>(50, 0.5f);

	
	private static MapDaoSupport dao=null;
	
	public static void setDaoSupport(MapDaoSupport dao){
		MapDTOUtil.dao=dao;
	}
	
	
	public static MapDTO newMapDTO(Connection con,String tableName){
		return new MapDTO(getTableInfo(con, tableName));
		
	}
	
	public static MapDTO newMapDTO(String tableName){
		return new MapDTO(dao.getMapDTOCfg(tableName));
		
	}
	
	
	
	public static BaseDTO mapToUpdateDto(Connection con,String tableName,Map map){
		
		if(map instanceof MapDTO){
			return (MapDTO)map;
		}
		return new UpdateMapToDTO(getTableInfo(con,tableName),map);
		
	}
	public static BaseDTO mapToDto(Connection con,String tableName,Map map){
		
			if(map instanceof MapDTO){
				MapDTO dto=(MapDTO)map;
				if(((MapDTO) map).managerCfg()==null)
					new MapToDTO(getTableInfo(con,tableName),map);
				else{
					return (MapDTO)map;
				}
			}
			
			return new MapToDTO(getTableInfo(con,tableName),map);
			
		}
	
	public static String getFieldName(String name) {
		  if(name!=null){
	        	String fieldName=name.toLowerCase();
				int index=-1;
				//System.out.println(fieldName);
				while((index=fieldName.indexOf("_"))!=-1){
					if(index==(fieldName.length()-1))
						fieldName=fieldName.substring(0,index);
					else
						fieldName=fieldName.substring(0,index)+fieldName.substring(index+1,index+2).toUpperCase()+fieldName.substring(index+2);
				}
				
				return fieldName;
			
	        }else
	        	return null;
		
	}
	public static MapQuery getQueryInfo(ResultSetMetaData rsmd,String sql) throws SQLException{

		int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
		MapFieldInfo[] field=new MapFieldInfo[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			String colName=rsmd.getColumnName(i);
			
			
			field[i-1]=new MapFieldInfo(i-1, getFieldName(colName), colName, getSQLType(rsmd,i), true,false, null, "");
			
		}
		MapQuery queryInfo=new MapQuery(sql,field);

		 return queryInfo;
	}
	
	public static MapDTOCfg getTableInfo(Connection con,String tableName){
		tableName=tableName.toLowerCase();
		MapDTOCfg cfg=  columnsTableInfo.get(tableName);
		 if(cfg!=null)
			 return cfg;
		 cfg= getTableInfo2(con,tableName);
		 columnsTableInfo.put(tableName, cfg);
		 return cfg;
	}
	public static MapDTOCfg getTableInfo2(Connection con,String tableName){
		tableName=tableName.toLowerCase();
		List<MapFieldInfo> list=getTableInfoColumns(con,tableName);
		if(list.size()==0)
			return null;
		MapDTOCfg  cfg= new MapDTOCfg(tableName, list.toArray(new MapFieldInfo[list.size()]));
		 return cfg;
	}
public static SQLType getSQLType(ResultSetMetaData rsmd,int column) throws SQLException{
		
		int type=rsmd.getColumnType(column);
		if(	rsmd.getColumnTypeName(column).toLowerCase().indexOf("char") != -1)
			return Cfg.String;
		else if(type==java.sql.Types.DATE){
			return Cfg.Date;
			
		}if (type==java.sql.Types.TIMESTAMP){
			return Cfg.Timestamp;
		}else{
			if(rsmd.getScale(column)==0&&type==Cfg.Integer.sqlType())
				return Cfg.Integer;
			else
				return Cfg.BigDecimal;
			
		}
	}
	public static SQLType getSQLType(ResultSet rs) throws SQLException{
		
		int type=rs.getInt("DATA_TYPE");
		if(rs.getString("TYPE_NAME").toLowerCase().indexOf("char") != -1)
			return Cfg.String;
		else if(type==java.sql.Types.DATE||type==java.sql.Types.TIMESTAMP){
			return Cfg.Date;
		}else if(rs.getInt("DECIMAL_DIGITS")==0)
				return Cfg.Integer;
			else
				return Cfg.BigDecimal;
			
		
	}
	
	public static List<MapFieldInfo> getTableInfoColumns(Connection con, String tableName) {

		List<MapFieldInfo> columns = new ArrayList<MapFieldInfo>();
		
	    List<String> keys=SqlUtil.getTableColumns(con, tableName, 2);
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData=	con.getMetaData();
				rs = metaData.getColumns(null,metaData.getUserName().toUpperCase(), tableName.toUpperCase(), null);
				MapFieldInfo field=null;
				int i=0;

			while (rs.next()) {
					if(rs.getInt("DATA_TYPE")!=Types.CLOB){
						String columnName=rs.getString("COLUMN_NAME").toLowerCase();
						field=new MapFieldInfo(i,  getFieldName(columnName), columnName, getSQLType(rs), true, keys.contains(columnName), null, "");
						field.setDefVal(rs.getString("COLUMN_DEF"));
					   columns.add(field);
					i++;
					}
			}

		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

		}
	
	

		return columns;
	}
}

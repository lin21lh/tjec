/************************************************************
 * 类名：DaoUtil.java
 *
 * 类别：公共类
 * 功能：Dao工具类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-05  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jbf.common.util.StringUtil;

@Component
public class DaoUtil {

	/**
	 * 获取数据字段信息
	 * @param con 数据库连接
	 * @param tableName 表名称
	 * @return 数据表字段集合
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getTableInfoColumns(Connection con, String tableName) {

		List columns = new ArrayList();
		ResultSet rs = null;
		ResultSet tableRs = null;
		try {
			DatabaseMetaData metaData =	con.getMetaData();
			String schemaPattern = null;
			
			String getCommentsSql = "";
			//根据当前数据库类型获取数据库用户名
			switch (DatabaseProductName.getDataBaseIndex(metaData.getDatabaseProductName())) {
			case 1: //Oracle
				schemaPattern = metaData.getUserName();
				getCommentsSql = "select comments from user_col_comments where upper(table_name)='" + tableName.toUpperCase() + "' and upper(column_name)='${columncode}'" ;
				break;
			case 2: //sqlserver
				break;
			case 3: //mysql
			default:
				break;
			}
			rs = metaData.getColumns(null, schemaPattern, tableName.toUpperCase(), "%");
			
            Map map=null;
			while (rs.next()) {

				if(rs.getInt("DATA_TYPE")!=Types.CLOB && rs.getInt("DATA_TYPE")!=Types.BLOB){
				    map=new HashMap();
					map.put("columnName", rs.getString("COLUMN_NAME").toLowerCase());
					map.put("isnumber",new Boolean(rs.getString("TYPE_NAME").toLowerCase().indexOf("char")==-1));
					map.put("typename", rs.getString("TYPE_NAME"));
					map.put("datatype", rs.getInt("DATA_TYPE"));
					map.put("nullable", rs.getInt("NULLABLE"));
					map.put("defVal", rs.getString("COLUMN_DEF"));
					map.put("length", rs.getInt("COLUMN_SIZE"));
					map.put("decimalDigits", rs.getInt("DECIMAL_DIGITS"));
					//map.put("remarks", rs.getString("REMARKS"));
					if (StringUtil.isNotBlank(getCommentsSql)) {
						PreparedStatement statement = con.prepareStatement(getCommentsSql.replace("${columncode}", rs.getString("COLUMN_NAME").toUpperCase()));
						 tableRs = statement.executeQuery();
						 String remarks = null;
                        if (tableRs.next()) {
                            remarks = tableRs.getString("comments");
	                    }
						map.put("remarks", remarks);
					}
				
					
					columns.add(map);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (tableRs != null)
					tableRs.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return columns;
	}
	
	/**
	 * 获取数据表字段
	 * @param con 数据库连接
	 * @param tableName 表名
	 * @param isPrimaryKey 是否主键
	 * @return 数据表字段集合
	 */
	public static List getTableColumns(Connection con, String tableName,
			int isPrimaryKey) {
		List columns = new ArrayList();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData =	con.getMetaData();
			
			String schemaPattern = null;
			switch (DatabaseProductName.getDataBaseIndex(metaData.getDatabaseProductName())) {
			case 1: //Oracle
				schemaPattern = metaData.getUserName();
				break;
			case 2: //sqlserver
				break;
			case 3: //mysql
			default:
				break;
			}
			
			if (isPrimaryKey < 2)
				rs = metaData.getColumns(null, schemaPattern, tableName.toUpperCase(), null);
			else if (isPrimaryKey == 2)
				rs = metaData.getPrimaryKeys(null, schemaPattern, tableName.toUpperCase());

			while (rs.next()) {
				if (isPrimaryKey == 1) {
					String isnull = rs.getString("IS_NULLABLE");

					if (isnull != null && isnull.equals("NO"))
						columns.add(rs.getString("COLUMN_NAME").toLowerCase());
				} else
					columns.add(rs.getString("COLUMN_NAME").toLowerCase());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		if (isPrimaryKey > 1 && columns.size() <= 0)
			return getTableColumns(con, tableName, isPrimaryKey - 1);

		return columns;
	}
	
	/**
	 * 将值放入PreparedStatement
	 * 
	 * @param values 值
	 * @param i 索引号
	 * @param prpe PreparedStatement对象
	 * @param columns 列名集合
	 * @return i 索引号
	 * @throws SQLException
	 */
	public int addSetPrepareStatement(Map values, int i, PreparedStatement prpe, List columns) throws SQLException {
		ColVO col = null;
		Object value = null;
		try {
			for (int j = 0; j < columns.size(); j++) {
				col = (ColVO) columns.get(j);

				value = values.get(col.getName());
				if (value != null&&(!col.isIsnumber()||!value.toString().equals(""))) {
					if (col.isIsnumber()) {
						if(value.toString().equals("null")){//避免map中存在值为null的情况。
							prpe.setBigDecimal(i, null);
						}else{
							if (value instanceof java.util.Date)
								prpe.setDate(i, new java.sql.Date(((java.util.Date) value).getTime()));
							else {
							    prpe.setBigDecimal(i, new BigDecimal(value.toString().trim()));
							}
						}
					} else {

						prpe.setString(i, value.toString().trim());

					}
				} else {
					
                    if(col.getDefVal()==null)
					prpe.setObject(i, null);
                    else{
                    	if(col.isIsnumber()){
                    		 if(col.getDefVal()==null||col.getDefVal().equals("null")){
                    			 prpe.setBigDecimal(i, new BigDecimal(0));
                    		 }else{
                    			 prpe.setBigDecimal(i, new BigDecimal(col.getDefVal())); 
                    		 }
                    		 
                    	}else{
                    		 prpe.setString(i, col.getDefVal());
                    	}
                      
                    }
				}

				i++;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();

			throw new SQLException(e.getMessage() + " col " + col.getName());

		}
		return i;
	}
	
	/**
	 * 获取数据库连接
	 * @param tablename 表名
	 * @param conn 数据库连接
	 * @return 序列值
	 */
	public Integer getSequenceNextVal(String tablename, Connection conn) {
		
		return getSequenceNextVal(tablename, "SEQ_", conn);
	}
	
	/**
	 * 获取序列值
	 * @param tablename 表名
	 * @param prefix 前缀
	 * @param conn 数据库连接
	 * @return 序列值
	 */
	public Integer getSequenceNextVal(String tablename, String prefix, Connection conn) {
		
		if (tablename.toUpperCase().startsWith("JOC_"))
			tablename = tablename.toUpperCase().replaceFirst("JOC_", prefix);
		
		if (tablename.toUpperCase().startsWith("SYS_"))
			tablename = tablename.toUpperCase().replaceFirst("SYS_", prefix);
		
		if (tablename.toUpperCase().startsWith("JOB_"))
			tablename = tablename.toUpperCase().replaceFirst("JOB_", prefix);
		
		if (tablename.toUpperCase().startsWith("TEST_"))
			tablename = tablename.toUpperCase().replaceFirst("TEST_", prefix);
		
		if (tablename.toUpperCase().startsWith("FA_"))
			tablename = prefix + tablename;
		if (tablename.toUpperCase().startsWith("PRO_"))
			tablename = prefix + tablename;
		
		String sql = "select " + tablename + ".NEXTVAL from dual";
		Object sequence = getSQLObject(conn, sql);
		return sequence == null ? null : new Integer(sequence.toString());
	}
	
	/**
	 * 查询
	 * @param conn 数据库连接
	 * @param sql SQL语句
	 * @return
	 */
	public Object getSQLObject(Connection conn, String sql) {
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			if (rs.next())
				return rs.getObject(1);
			else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}
	
	/**
	 * 将值放入PreparedStatement
	 * 
	 * @param values 值
	 * @param i 索引号
	 * @param prpe PreparedStatement对象
	 * @param columns 列名集合
	 * @return i 索引号
	 * @throws SQLException
	 */
	public int setPrepareStatement(Map values, int i, PreparedStatement prpe, List columns) throws SQLException {
		ColVO col = null;
		Object value = null;
		try {
			for (int j = 0; j < columns.size(); j++) {
				col = (ColVO) columns.get(j);
				value = values.get(col.getName().toLowerCase());
				if (value != null) {
					if (col.isIsnumber()) {
						if (value instanceof java.util.Date)
							prpe.setDate(i, new java.sql.Date(((java.util.Date) value).getTime()));
						else {
							value = value.toString().trim();
							if (!value.equals("")&&!value.equals("null"))
								prpe.setBigDecimal(i, new BigDecimal((String) value));
							else
								prpe.setObject(i, null);
						}
					} else {

						prpe.setString(i, value.toString().trim());

					}
				} else {

					prpe.setObject(i, null);
				}

				i++;
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage() + " col " + col.getName());
		}
		return i;
	}
	
	/**
	 * 判断是否存在表
	 * @param tablename 表名称
	 * @param connection 数据库连接
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistTable(String tablename, Connection connection) throws SQLException {
		
 		DatabaseMetaData metaData = connection.getMetaData();
		String schemaPattern = null;
		
		//根据当前数据库类型获取数据库用户名
		switch (DatabaseProductName.getDataBaseIndex(metaData.getDatabaseProductName())) {
		case 1: //Oracle
			schemaPattern = metaData.getUserName();
			break;
		case 2: //sqlserver
			break;
		case 3: //mysql
		default:
			break;
		}
		
		ResultSet rs = connection.getMetaData().getTables(null, schemaPattern, tablename.toUpperCase(), null);
		
		boolean isExist = rs.next();
		
		if (rs != null)
			rs.close();
		
		return isExist;
	}
}

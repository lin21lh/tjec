package com.freework.base.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




public class SqlUtil {
/**
 * 生成返回查询的sql语句
 * @param tablename 表名
 * @param columns 列名
 * @param where 过滤条件
 * @return
 */
	public static String getQuerySql(String tablename, List<String> columns,
			String where) {
		StringBuffer sql = new StringBuffer(100);
		sql.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			sql.append(columns.get(i));
			if (i != columns.size() - 1)
				sql.append(",");

		}
		sql.append(" from ").append(tablename).append("  t ");

		if (where != null && !where.equals(""))
			sql.append(" where ").append(where);

		return sql.toString();

	}
	

	/**
	 * 生成返回修改的sql语句
	 * @param tablename 表名
	 * @param columns 列名
	 * @return
	 */
	public  String getUpdateSql(String tableName, Collection columns,
			String whereColumns[]) {
		StringBuffer sql1 = new StringBuffer("update  ").append(tableName)
				.append(" set ");
		int j = 0;
		int length = columns.size();

		Iterator itr = columns.iterator();
		String string = null;
		while (itr.hasNext()) {
			string = (String) itr.next();
			sql1.append(string).append("=").append("?");

			if (j != length - 1) {
				sql1.append(",");

			} else {
				sql1.append(" where ");
			}
			j++;
		}
		for (int i = 0; i < whereColumns.length; i++) {

			sql1.append(whereColumns[i]).append("=").append("?");

			if (i != whereColumns.length - 1) {
				sql1.append(" and ");

			}
		}

		return sql1.toString();
	}

	public static String getInsertSql(String tableName, Collection columns) {
		StringBuffer sql1 = new StringBuffer("insert into ").append(tableName)
				.append(" (");
		StringBuffer sql2 = new StringBuffer("  values ( ");
		int i = 0;
		String string = null;
		Iterator itr = columns.iterator();
		int length = columns.size() - 1;
		while (itr.hasNext()) {
			string = (String) itr.next();
			sql1.append(string);

			sql2.append("?");
			if (i != length) {
				sql1.append(",");
				sql2.append(",");
			} else {
				sql1.append(")");
				sql2.append(")");
			}
			i++;
		}
		return (sql1.append(sql2)).toString();

	}

	public static boolean pandTableName(String tableName, Connection con) {

		ResultSet rs = null;
		try {

			rs = con.getMetaData().getColumns(null, null, tableName, null);

			return rs.next();

		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}

		}

	}

	public static List<String> getTableNames(Connection con) {

		List<String> columns = new LinkedList<String>();
		ResultSet rs = null;
		try {

			DatabaseMetaData metaData = con.getMetaData();
			rs = metaData.getTables(null, metaData.getUserName().toUpperCase(), null, new String[]{"TABLE"});

			while (rs.next()) {
				columns.add(rs.getString("TABLE_NAME"));
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
	
	
	public static List getTableInfoColumns(Connection con, String tableName) {

		List columns = new ArrayList();
		ResultSet rs = null;
		try {

			DatabaseMetaData metaData = con.getMetaData();
			rs = metaData.getColumns(null, metaData.getUserName().toUpperCase(), tableName
					.toUpperCase(), null);

			Map map = null;
			while (rs.next()) {

				map = new HashMap();
				map
						.put("columnName", rs.getString("COLUMN_NAME")
								.toLowerCase());
				map.put("isnumber", new Boolean(rs.getString("TYPE_NAME")
						.toLowerCase().indexOf("char") == -1));
				map.put("defVal", rs.getString("COLUMN_DEF"));
				columns.add(map);

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

	public static List getTableColumns(Connection con, String tableName,
			int isPrimaryKey) {
		List columns = new ArrayList();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData = con.getMetaData();
			if (isPrimaryKey < 2)
				rs = metaData.getColumns(null, metaData.getUserName().toUpperCase(),
						tableName.toUpperCase(), null);
			else if (isPrimaryKey == 2)
				rs = metaData.getPrimaryKeys(null, metaData.getUserName().toUpperCase(),
						tableName.toUpperCase());

			while (rs.next()) {
				if (isPrimaryKey == 1) {
					String isnull = rs.getString("IS_NULLABLE");

					if (isnull != null && isnull.equals("NO"))
						columns.add(rs.getString("COLUMN_NAME").toLowerCase());
				} else
					columns.add(rs.getString("COLUMN_NAME").toLowerCase());
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
	public static List<String> getSqlParameter(String sql, char startStr, char endStr) {
		int beginIndex=sql.indexOf(startStr);
		int endIndex=sql.indexOf(endStr);
		List<String> list = new LinkedList<String>();
		while(beginIndex!=-1&&beginIndex+1<sql.length()&&endIndex!=-1){
			list.add(sql.substring(beginIndex+1, endIndex));
			sql=sql.substring( endIndex+1);
			beginIndex=sql.indexOf(startStr);
			endIndex=sql.indexOf(endStr);

		}
		return list;
	}
	public static List<String> getNewSqlParameter2(String sql, char startStr, char endStr) {
		int beginIndex=sql.indexOf(startStr);
		int endIndex=sql.indexOf(endStr);
		List<String> list = new ArrayList<String>();
		while(beginIndex!=-1&&beginIndex+1<sql.length()&&endIndex!=-1){
			list.add(sql.substring(beginIndex, endIndex+1));
			sql=sql.substring( endIndex+1);
			beginIndex=sql.indexOf(startStr);
			endIndex=sql.indexOf(endStr);

		}
		return list;
	}
	public static List<String> getSqlParameter2(String sql, char startStr,
			char endStr) {
		List<String> list = new LinkedList<String>();
		int j=0;
		int index=-1;
		for (int i = 0; i < sql.length(); i++) {
			if (sql.charAt(i) == startStr) {
				
				if(j==0)
					index=i;
				j++;
			} else if (sql.charAt(i) == endStr) {
				j--;
				if(j==0&&index!=-1)
					list.add(sql.substring(index+1,i));
			}
		}
		return list;
	}
	
	static public String querySingle(Connection conn,String Tablename, String code, String where) {
		final StringBuffer sql = new StringBuffer("select ");
		sql.append(code).append("  from ").append(Tablename).append(" where ")
				.append(where);
		
	
						Statement prpe=null;
						ResultSet rs=null;
						try {
							prpe = conn.createStatement();
						
//						System.out.println("sql::::"+sql);
						 rs = prpe.executeQuery(sql.toString());
						String value = null;
						if (rs.next())
							value = rs.getString(1);
						rs.close();
						prpe.close();

						return value;
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						}finally{
							if(prpe!=null)
								try {
									prpe.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								if(rs!=null)
									try {
										rs.close();
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							
						}
				
	}
	public static String getCountSQL(String sql){
		return CountSQL.getCountSql(sql);
		
	}
	
	/**
	 * 字符串数组转换为sql的in条件
	 * @param arr
	 * @return ('aa','bb')
	 */
	public static String arrToSqlstr(String[] arr) {
		if (arr == null || arr.length == 0) {
			return "()";
		}
		String str = "";
		for (int i = 0; i < arr.length; i++) {
			str += "'" + arr[i] + "'";
			if (i < arr.length - 1) {
				str += ",";
			}
		}
		
		return " ("+str + ")";
	}
	
	/**
	 * 字符串数组转换为sql的in条件
	 * @param arr
	 * @return (123,234)
	 */
	public static String arrToSqlint(String[] arr) {
		if (arr == null || arr.length == 0) {
			return "()";
		}
		String str = "";
		for (int i = 0; i < arr.length; i++) {
			str += arr[i];
			if (i < arr.length - 1) {
				str += ",";
			}
		}
		
		return " ("+str + ")";
	}

}

/************************************************************
 * 类名：SqlUtil.java
 *
 * 类别：工具类
 * 功能：SQL工具类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


public class SqlUtil {
	
	/**
	 * 对象转换为Map
	 * @param object 实体类对象
	 * @return
	 */
	static public Map ObjectToMap(Object object) {
		if (object == null)
			return null;
		
		Class myclass=object.getClass();
		Map map=new HashMap();
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(myclass);
		
			PropertyDescriptor pd[] = info.getPropertyDescriptors();
			String name = null;
			for (int i = 0; i < pd.length; i++) {

				name = pd[i].getName();
				try {
					map.put(name, BeanUtils.getProperty(object, name));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取修改语句
	 * @param tableName 表名
	 * @param columns 修改列集合
	 * @param whereColumns 条件列
	 * @return
	 */
	public static String getUpdateSql(String tableName, Collection columns, String whereColumns[]) {
		StringBuffer sql = new StringBuffer("update  ").append(tableName).append(" set ");
		int j = 0;
		int length = columns.size();

		Iterator itr = columns.iterator();
		String string = null;
		while (itr.hasNext()) {
			string = (String) itr.next();
			sql.append(string).append("=").append("?");

			if (j != length - 1) {
				sql.append(",");

			} else {
				sql.append(" where ");
			}
			j++;
		}
		for (int i = 0; i < whereColumns.length; i++) {

			sql.append(whereColumns[i]).append("=").append("?");

			if (i != whereColumns.length - 1) {
				sql.append(" and ");
			}
		}

		return sql.toString();
	}

	/**
	 * 获得插入语句
	 * @param tableName 表名
	 * @param columns 插入列集合
	 * @return
	 */
	public static String getInsertSql(String tableName, Collection columns) {
		StringBuffer sql1 = new StringBuffer("insert into ").append(tableName)
				.append(" (");
		StringBuffer sql2 = new StringBuffer("  values ( ");
		int i = 0;
		String string = null;
		Iterator itr = columns.iterator();
		int length= columns.size() - 1;
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
	
	/**
	 * 判断是否为表对象
	 * @param tableName 判断的表名
	 * @param con 数据库连接
	 * @return true 是;false 否
	 */
	public static boolean pandTableName(String tableName, Connection con) {

		ResultSet rs = null;
		try {
			rs = con.getMetaData().getColumns(null, null, tableName, null);
			 
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * 获取Where条件
	 * @param map 
	 * @param tabtyname
	 * @param list
	 * @return
	 */
	public static String getWhere(Map map,String tabtyname,List<ColVO> list ){
		Object value=null;
		
		StringBuilder where=new StringBuilder();
		boolean pand=false;
		for (ColVO colVO : list) {
			
			value=map.get(colVO.getName());
			if(value!=null&&!value.equals("")){
				if(pand)
				where.append(" and ");
				else
				pand=true;
				if(colVO.isIsnumber())
				where.append(colVO.getName()).append("=").append(value);
				else
				where.append(colVO.getName()).append("='").append(value).append("' ");
			}
			
		}
		return where.toString();
		
		
		
		
	}
	
	/**
	 * 获取数据表列
	 * @param con 数据库连接
	 * @param tableName 
	 * @return 列集合
	 */
	public static List getTableInfoColumns(Connection con, String tableName) {

		List columns = new ArrayList();
		ResultSet rs = null;
		try {
			
			DatabaseMetaData metaData=	con.getMetaData();
				rs = metaData.getColumns(null,metaData.getUserName(), tableName.toUpperCase(), null);
		
            Map map=null;
			while (rs.next()) {

					if(rs.getInt("DATA_TYPE")!=Types.CLOB){
				    map=new HashMap();
					map.put("columnName", rs.getString("COLUMN_NAME").toLowerCase());
					map.put("isnumber",new Boolean(rs.getString("TYPE_NAME").toLowerCase().indexOf("char")==-1));
					map.put("defVal", rs.getString("COLUMN_DEF"));
					columns.add(map);
					}

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
	
	

		return columns;
	}
	
	/**
	 * 获取数据表列
	 * @param con 数据库连接
	 * @param tableName 表名称
	 * @return ColVO集合
	 */
	public static List<ColVO> getTableInfoColumns2(Connection con, String tableName) {

		List<ColVO> columns = new ArrayList<ColVO>();
		ResultSet rs = null;
		try {
			
			DatabaseMetaData metaData=	con.getMetaData();
				rs = metaData.getColumns(null,metaData.getUserName(), tableName.toUpperCase(), null);
		
				ColVO vo=null;
			while (rs.next()) {

				vo=new ColVO();
				vo.setName( rs.getString("COLUMN_NAME").toLowerCase());
				vo.setIsnumber(new Boolean(rs.getString("TYPE_NAME").toLowerCase().indexOf("char")==-1));
				vo.setDefVal(rs.getString("COLUMN_DEF"));
		
					columns.add(vo);

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
		return columns;
	}
	
	/**
	 * 获取数据表所有列
	 * @param con 数据库连接
	 * @param tableName 表名称
	 * @param isPrimaryKey 是否主键
	 * @return 列集合
	 */
	public static List getTableColumns(Connection con, String tableName,
			int isPrimaryKey) {
		List columns = new ArrayList();
		ResultSet rs = null;
		try {
			DatabaseMetaData metaData=	con.getMetaData();
			if (isPrimaryKey < 2)
				rs = metaData.getColumns(null, metaData.getUserName(), tableName.toUpperCase(), null);
			else if (isPrimaryKey == 2)
				rs = metaData.getPrimaryKeys(null, metaData.getUserName(), tableName.toUpperCase());

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
	 * 获取SQL参数
	 * @param sql SQL语句
	 * @param startStr 前缀
	 * @param endStr 后缀
	 * @return
	 */
	public static List<String> getSqlParameter(String sql, char startStr, char endStr) {
		char[] sqlArray = sql.toCharArray();

		List<String> list = new ArrayList<String>();
		boolean pand = false;
		StringBuilder parameter = new StringBuilder(10);
		for (int i = 0; i < sqlArray.length; i++) {
			if (!pand && sqlArray[i] == startStr) {
           
				pand = true;
			} else if (sqlArray[i] == endStr) {
				list.add(parameter.toString());
				parameter = new StringBuilder(10);
				pand = false;

			} else if (pand) {
				parameter.append(sqlArray[i]);
			}

		}
		return list;

	}
}

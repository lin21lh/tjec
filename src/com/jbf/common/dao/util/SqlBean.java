/************************************************************
 * 类名：SqlBean.java
 *
 * 类别：Bean
 * 功能：SQLBean
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-14  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SqlBean {

	private Hashtable<String, SqlVO> columnsTableInfo = new Hashtable<String, SqlVO>(50, 0.5f);

	public SqlVO getQuerySqlVo(Collection<String> columns, String tablename, String where, Connection db, String jndiName) {
		return getQuerySqlVo(columns, tablename, where, db, jndiName, true);
	}
	
	/**
	 * 获取查询SQLVO
	 * @param columns 列集合
	 * @param tablename 表名称
	 * @param where where条件
	 * @param db 数据库连接
	 * @param jndiName weblogic数据源名称
	 * @param isCache 是否缓存
	 * @return SqlVO
	 */
	public SqlVO getQuerySqlVo(Collection<String> columns, String tablename, String where, Connection db, String jndiName, boolean isCache) {

		SqlVO vo = null;

		if (columns == null) {
			vo = (SqlVO) getTableInfo(tablename, null, db, jndiName, isCache);

			if (where != null && !where.equals("")) {
				vo = (SqlVO) vo.clone();
				vo.setSql(vo.getSql()
						+ (where != null && !where.equals("") ? " where "
								+ where : ""));
			}

		} else {
			vo = new SqlVO();
			List<ColVO> list = new ArrayList<ColVO>();
			ColVO cvo = null;
			for (Iterator<String> iterator = columns.iterator(); iterator.hasNext();) {
				String column = (String) iterator.next();
				cvo = new ColVO();
				cvo.setName(column);
				list.add(cvo);

			}
			vo.setCols(list);
			vo.setTablename(tablename);
			vo.setSql(getQuerySql(columns, tablename, where));
		}
		return vo;

	}
	
	/**
	 * 获取表信息
	 * @param tablename 表名称
	 * @param db 数据库连接
	 * @param values 
	 * @param jndiName weblogic 数据源名称
	 * @param isCache  是否缓存
	 * @return SqlVO
	 */
	public SqlVO getTableInfo(String tablename, Connection db, Map values, String jndiName, boolean isCache) {

		return getTableInfo(tablename, db, values, jndiName, false, isCache);
	}
	
	/**
	 * 获取数据表信息
	 * @param tablename 表名称
	 * @param db 数据库连接
	 * @param values 
	 * @param jndiName weblogic数据源名称
	 * @param isUpdateNull 是否 修改为NULL
	 * @param isCache 是否缓存
	 * @return  SqlVO
	 */
	public SqlVO getTableInfo(String tablename, Connection db, Map values, String jndiName, boolean isUpdateNull,  boolean isCache) {

		SqlVO vo = getTableInfo(tablename, null, db, jndiName, isCache);
		if (values == null)
			return vo;

		List list = vo.getCols();
		List rList = new ArrayList();
		List colname = new ArrayList();
		String key[] = new String[vo.getKeys().size()];
		SqlVO sqlVo = new SqlVO();
		int i = 0;
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ColVO col = (ColVO) iterator.next();
			if (col.isKey()) {
				key[i] = col.getName();
				i++;
				rList.add(col);
				colname.add(col.getName());
			} else if ((values.get(col.getName()) != null && !values.get(col.getName()).toString().equals("")) || (values.containsKey(col.getName().toLowerCase()) && isUpdateNull)) {
				rList.add(col);
				colname.add(col.getName());
			}
		}
		List keys = new ArrayList();
		//keys.addAll(vo.getKeys());//当一个表的主键有多个时，取出的顺序与拼接update sql where条件顺序混乱，故增加一下代码改变字段顺序
		///--增加代码---start----
		List list2  = vo.getKeys();
		for (int j = 0; j < key.length; j++) {
			for (int j2 = 0; j2 < list2.size(); j2++) {
				ColVO colVO = (ColVO) list2.get(j2);
				if (key[j].equals(colVO.getName())) {
					keys.add(colVO);
				}
			}
		}
		///--增加代码---end----
		sqlVo.setTablename(vo.getTablename());
		sqlVo.setCols(rList);
		sqlVo.setKeys(keys);
		sqlVo.setInsertSql(SqlUtil.getInsertSql(tablename, colname));
		sqlVo.setUpdateSql(SqlUtil.getUpdateSql(tablename, colname, key));

		return sqlVo;

	}
	
	/**
	 * 获取数据表信息
	 * @param tablename 表名称
	 * @param db 数据库连接
	 * @param isCache 是否缓存
	 * @return
	 */
	public SqlVO getTableInfo(String tablename, Connection db, boolean isCache) {
		
		return getTableInfo(tablename, null, db, null, isCache);
	}
	
	/**
	 * 获取数据表信息
	 * @param tablename 数据表名称
	 * @param where where条件
	 * @param db 数据库连接
	 * @param jndiName 
	 * @param isCache
	 * @return
	 */
	public SqlVO getTableInfo(String tablename, String where, Connection db, String jndiName, boolean isCache) {
		String dbuser = "";
		if (jndiName != null)
			dbuser = jndiName;
		SqlVO vo = (SqlVO) columnsTableInfo.get(dbuser + "_" + tablename);
		if (vo != null && isCache) {
			if(where != null && where.length() > 0){
				vo = (SqlVO) vo.clone();
				vo.setSql(vo.getSql()+(where!=null&&!where.equals("")?" where "+where:""));
			}
			return vo;
		}
			

		vo = new SqlVO();
		List keyCols1 = DaoUtil.getTableColumns(db, tablename, 2);
		List columns1 = DaoUtil.getTableInfoColumns(db, tablename);

		List<String> columnsStr = new ArrayList<String> ();

		List<ColVO> columns = new ArrayList<ColVO>();
		List<ColVO> keyCols = new ArrayList<ColVO>();
		ColVO col = null;
		for (Iterator iterator = columns1.iterator(); iterator.hasNext();) {
			Map column = (Map) iterator.next();
			col = new ColVO();
			col.setIsnumber(((Boolean) column.get("isnumber")).booleanValue());
			col.setNullable((Integer)column.get("nullable"));
			col.setName(column.get("columnName").toString());
			col.setDefVal((String) column.get("defVal"));
			col.setLength((Integer)column.get("length"));
			col.setRemarks((String)column.get("remarks"));
			col.setDecimalDigits(column.get("decimalDigits") != null ? (Integer)column.get("decimalDigits") : 0);
			col.setTypename((String)column.get("typename"));
			col.setDatatype((Integer)column.get("datatype"));
			columns.add(col);
			columnsStr.add(col.getName());
		}

		for (Iterator iterator = keyCols1.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			for (Iterator iterator2 = columns.iterator(); iterator2.hasNext();) {
				ColVO column = (ColVO) iterator2.next();

				if (key.equals(column.getName())) {
					column.setKey(true);
					keyCols.add(column);

					break;
				}
			}
		}
		String insertSql = SqlUtil.getInsertSql(tablename, columnsStr);

		String updateSql = SqlUtil.getUpdateSql(tablename, columnsStr, (String[]) keyCols1.toArray(new String[keyCols1.size()]));
		vo.setCols(columns);
		vo.setKeys(keyCols);

		vo.setInsertSql(insertSql);
		vo.setUpdateSql(updateSql);
		vo.setSql(getQuerySql(columnsStr, tablename, where));

		columnsTableInfo.put(dbuser + "_" + tablename, vo);

		return vo;
	}

	/**
	 * 获取查询语句
	 * @param columns 列集合
	 * @param tablename 表名称
	 * @param where where条件
	 * @return
	 */
	private String getQuerySql(Collection columns, String tablename, String where) {
		String column;
		StringBuffer sql = new StringBuffer(100);
		sql.append("select ");
		for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			column = (String) iterator.next();
			sql.append(column).append(",");

		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(" from ").append(tablename).append("  t ");

		if (where != null && !where.equals(""))
			sql.append(" where ").append(where);

		return sql.toString();

	}
}

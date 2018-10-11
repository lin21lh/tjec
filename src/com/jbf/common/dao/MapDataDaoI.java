/************************************************************
 * 类名：MapDataDaoI.java
 *
 * 类别：通用类
 * 功能：通用数据操作基类，用于对持久层具体技术特性的抽象与封装(使用SQL)
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-3-31  CFIT-PM  maqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.freework.queryData.compileSQL.SqlAndParam;

public interface MapDataDaoI {

	/**
	 * 添加
	 * @param value 数据列
	 * @param tablename 表名
	 * @return 添加后数据库对应的数据列
	 */
	public Map add(final Map value, final String tablename);
	
	/**
	 * 添加（选择JNDI）
	 * @param value 数据列
	 * @param tablename 表名
	 * @param jndiName JNDI名称
	 * @return 添加后数据库对应的数据列
	 */
	public Map addRemote(final Map value, final String tablename, final String jndiName);
	
	/**
	 * 批量添加
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @return 添加后数据库对应的数据列集合
	 */
	public List addBatch(final List values, final String tablename);
	
	/**
	 * 批量添加（选择JNDI）
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @param jndiName JNDI名称
	 * @return 添加后数据库对应的数据列集合
	 */
	public List addBatchRemote(final List values, final String tablename, final String jndiName);
	
	/**
	 * 修改 默认UpdateNULL为false
	 * @param value 数据列
	 * @param tablename 表名
	 * @return 修改后数据库中对应的数据列
	 */
	public Map update(final Map value, final String tablename);
	
	/**
	 * 修改
	 * @param value 数据列
	 * @param tablename 表名
	 * @param UpdateNull 是否将数据列中没有的值修改为NULL
	 * @return 修改后数据库中对应的数据列
	 */
	public Map update(final Map value, final String tablename, final boolean UpdateNull);
	
	/**
	 * 修改（选择JNDI）默认UpdateNULL为false
	 * @param value 数据列
	 * @param tablename 表名
	 * @param jndiName JNDI名称
	 * @return 修改后数据库中对应的数据列
	 */
	public Map updateRemote(final Map value, final String tablename, final String jndiName);
	
	/**
	 * 修改（选择JNDI）
	 * @param value 数据列集合
	 * @param tablename 表名
	 * @param UpdateNull 是否将数据列中没有的值修改为NULL
	 * @param jndiName JNDI名称
	 * @return 修改后数据库中对应的数据列
	 */
	public Map updateRemote(final Map value, final String tablename, final boolean UpdateNull, final String jndiName);
	
	/**
	 * 批量修改 默认UpdateNULL为false
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @return 修改后数据库中对应的数据列集合
	 */
	public List updateBatch(final List values, final String tablename);
	
	/**
	 * 批量修改
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @param UpdateNULL 是否将数据列中没有的值修改为NULL
	 * @return 修改后数据库中对应的数据列集合
	 */
	public List updateBatch(final List values, final String tablename, final boolean UpdateNULL);
	
	/**
	 * 批量修改（选择JNDI）默认UpdateNULL为false
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @param jndiName JNDI名称
	 * @return 修改后数据库中对应的数据列集合
	 */
	public List updateBatchRemote(final List values, final String tablename, final String jndiName);
	
	/**
	 * 批量修改（选择JNDI）
	 * @param values 数据列集合
	 * @param tablename 表名
	 * @param UpdateNULL 是否将数据列中没有的值修改为NULL
	 * @param jndiName JNDI名称
	 * @return 修改后数据库中对应的数据列集合
	 */
	public List updateBatchRemote(final List values, final String tablename, final boolean UpdateNULL, final String jndiName);
	
	/**
	 * 修改 通过SQL语句
	 * @param sql 修改SQL语句
	 * @return 是否修改成功
	 */
	public int updateTX(final String sql);
	
	/**
	 * 修改 通过SQL语句（选择JNDI）
	 * @param sql 修改SQL语句
	 * @param jndiName JNDI名称
	 * @return 是否修改成功
	 */
	public int updateRemoteTX(final String sql, final String jndiName);
	
	/**
	 * 删除
	 * @param tablename 表名
	 * @param where 删除条件
	 * @return 是否删除成功 true："成功";false："失败"
	 */
	public Boolean delete(String tablename, String where);
	
	/**
	 * 删除（选择JNDI）
	 * @param tablename 表名
	 * @param where 删除条件
	 * @param jndiName JNDI名称
	 * @return 是否删除成功 true："成功";false："失败"
	 */
	public Boolean deleteRemote(String tablename, String where, final String jndiName);
	
	/**
	 * 查询
	 * @param sql 查询SQL语句
	 * @return 查询结果集合
	 */
	public List queryListBySQL(final String sql);
	
	/**
	 * app端公共方法查询
	 * @param sqlParam
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List query4app(final SqlAndParam sqlParam,final Integer pageNum,final Integer pageSize);
	
	/**
	 *  查询（选择JNDI）
	 * @param sql 查询SQL语句
	 * @param jndiName JNDI名称
	 * @return 查询结果集合
	 */
	public List queryListBySQLRemote(final String sql, final String jndiName);
	
	/**
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public List queryListBySQLParam(final String sql, final Object... param);
	
	
	
	/**
	 * 解决in 1000 限制
	 * [select distinct column_value from table(CAST(SEND_SPLIT(?, ',') as mytable]
	 * @param sql
	 * @param jndiName
	 * @param param
	 * @return
	 */
	public List queryListBySQLParamRemote(final String sql, final String jndiName, final Object... param);
	
	/**
	 * 分页查询
	 * @param sql 查询SQL语句
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPageBySQL(final String sql, final int pageIndex, final int pageSize);
	
	/**
	 * 分页查询（选择JNDI）
	 * @param sql 查询SQL语句
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @param jndiName JNDI名称
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPageBySQLRemote(final String sql, final int pageIndex, final int pageSize, final String jndiName);
	
	/**
	 * 查询 默认全部列
	 * @param tablename 表名
	 * @param where 查询（检索）条件
	 * @return 查询结果集合
	 */
	public List queryList(final String tablename, final String where);
	
	/**
	 * 查询
	 * @param tablename 表名
	 * @param columns 查询列集合
	 * @param where 查询（检索集合）条件
	 * @return 查询结果集合
	 */
	public List queryList(final String tablename, final Collection<String> columns, final String where);
	
	/**
	 * 查询（选择JNDI）默认全部列
	 * @param tablename 表名
	 * @param where 查询（检索）条件
	 * @param jndiName JNDI名称
	 * @return 查询结果集合
	 */
	public List queryListRemote(final String tablename, final String where, final String jndiName);
	
	/**
	 * 查询（选择JNDI）
	 * @param tablename 表名
	 * @param columns 查询列集合
	 * @param where 查询（检索）集合
	 * @param jndiName JNDI名称
	 * @return 查询结果集合
	 */
	public List queryListRemote(final String tablename, final Collection<String> columns, final String where, final String jndiName);
	
	/**
	 * 分页查询 默认全部列
	 * @param tablename 表名
	 * @param where 查询（检索）条件
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPage(final String tablename, final String where, final int pageIndex, final int pageSize);
	
	/**
	 * 分页查询
	 * @param tablename 表名
	 * @param columns 查询列集合
	 * @param where 查询（检索）条件
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPage(final String tablename, final Collection<String> columns, final String where, final int pageIndex, final int pageSize);
	
	/**
	 * 分页查询（选择JNDI）默认全部列
	 * @param tablename 表名
	 * @param where 查询（检索）条件
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @param jndiName JNDI名称
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPageRemote(final String tablename, final String where, final int pageIndex, final int pageSize, final String jndiName);
	
	/**
	 * 分页查询（选择JNDI）
	 * @param tablename 表名
	 * @param columns 查询列集合
	 * @param where 查询（检索）条件
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize PaginationSupport
	 * @param jndiName JNDI名称
	 * @return 当前页数据 PaginationSupport
	 */
	public PaginationSupport queryPageRemote(final String tablename, final Collection<String> columns, final String where, final int pageIndex, final int pageSize, final String jndiName);
	
	/**
	 * 获取Blob字段内容
	 * @param blobColumn blob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @return Blob字段内容
	 */
	public InputStream getBlob(final String blobColumn, final String keyColumn, final int keyValue, final String table);
	
	/**
	 * 获取Blob字段内容（选择JNDI）
	 * @param blobColumn blob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @param jndiName JNDI名称
	 * @return Blob字段内容
	 */
	public InputStream getBlobRemote(final String blobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName);
	
	/**
	 * 获取Clob字段值
	 * @param clobColumn clob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @return Clob字段值
	 */
	public String getClob(final String clobColumn, final String keyColumn, final int keyValue, final String table);
	
	/**
	 * 获取Clob字段值（选择JNDI）
	 * @param clobColumn clob列名
	 * 	@param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @param jndiName JNDI名称
	 * @return Clob字段值
	 */
	public String getClobRemote(final String clobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName);
	
	/**
	 * 设置Blob字段
	 * @param in 内容
	 * @param blobColumn blob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 */
	public void setBlob(final InputStream in, final String blobColumn, final String keyColumn, final int keyValue, final String table);
	
	/**
	 * 设置Blob字段（选择JNDI）
	 * @param in 内容
	 * @param blobColumn blob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @param jndiName JNDI名称
	 */
	public void setBlobRemote(final InputStream in, final String blobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName);
	
	/**
	 * 设置Clob字段
	 * @param content 内容
	 * @param clobColumn clob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 */
	public void setClob(final String content, final String clobColumn, final String keyColumn, final int keyValue, final String table);
	
	/**
	 * 设置Clob字段（选择JNDI）
	 * @param content 内容
	 * @param clobColumn clob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @param jndiName JNDI名称
	 */
	public void setClobRemote(final String content, final String clobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName);
	
	/**
	 * 分页查询 默认全部列 将数据查询字段中带_转为后字母大写如（app_id 转换为appId）
	 * @param tablename 表名
	 * @param where 查询（检索）条件
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @return 当前页数据 PaginationSupport
	 * @author XinPeng 2015年4月23日13:55:30
	 */
	public PaginationSupport queryPageForConvert(final String tablename, final String where, final int pageIndex, final int pageSize);
	/**
	 * 分页查询  将数据查询字段中带_转为后字母大写如（app_id 转换为appId）
	 * @param sql 查询SQL语句
	 * @param pageIndex 第几页索引,从1开始
	 * @param pageSize 每页数据条数
	 * @return 当前页数据 PaginationSupport
	 * @author XinPeng 2015年4月23日14:56:46
	 */
	public PaginationSupport queryPageBySQLForConvert(final String sql, final int pageIndex, final int pageSize);
	
	/**
	 * 查询结果 将数据查询字段中带_转为后字母大写如（app_id 转换为appId）
	 * @Title: queryListBySQLForConvert 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return 设定文件 
	 * @author XinPeng 2015年5月20日23:55:19
	 */
	public List queryListBySQLForConvert(final String sql);
	
	
	/**
	 * 获取存取findeditor数据Clob字段值，findeditor控件专用
	 * @param clobColumn clob列名
	 * @param keyColumn 主键列名
	 * @param keyValue 主键值
	 * @param table 表名
	 * @return Clob字段值
	 */
	public String getContentClob(final String clobColumn, final String keyColumn, final int keyValue, final String table);
	
	/**
	 * 查询sql的列名
	 * @Title: querySqlColumns 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sql
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	public List<String> querySqlColumns(final String sql,final String jndiName);
	
}

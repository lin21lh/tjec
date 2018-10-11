/************************************************************
 * 类名：IGenericDao.java
 *
 * 类别：通用类
 * 功能：通用数据操作基类，用于对持久层具体技术特性的抽象与封装
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-11  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.exception.AppException;

public interface IGenericDao<T, ID extends Serializable> {

	/**
	 * 保存对象
	 * 
	 * @param t
	 *            对象PO
	 * @return 对象的物理主键
	 * @throws DataAccessException
	 */
	public Serializable save(T t) throws DataAccessException;

	/**
	 * 存储或更新对象，主键不为空时更新
	 * 
	 * @param t
	 *            对象PO
	 * @throws DataAccessException
	 */
	public void saveOrUpdate(T t) throws DataAccessException;

	/**
	 * 更新对象
	 * 
	 * @param t
	 *            对象PO
	 * @throws DataAccessException
	 */
	public void update(T t) throws DataAccessException;

	/**
	 * 更新对象
	 * 
	 * @param t1
	 *            前端提交对象PO
	 * @param t2
	 *            数据库最新对象PO
	 * @throws DataAccessException
	 * @throws AppException
	 */
	public void update(T t1, T t2) throws DataAccessException, AppException;

	/**
	 * 删除对象
	 * 
	 * @param t
	 *            对象PO
	 * @throws DataAccessException
	 */

	public void delete(T t) throws DataAccessException;

	/**
	 * 删除对象
	 * 
	 * @param id
	 *            对象ID
	 * @throws DataAccessException
	 */
	public void delete(ID id) throws DataAccessException;

	/**
	 * 批量删除对象
	 * 
	 * @param entities
	 *            对象PO列表
	 * @throws DataAccessException
	 */
	public void deleteAll(Collection<T> entities) throws DataAccessException;

	/**
	 * 更新对象缓存
	 * 
	 * @param entity
	 */
	/**
	 * 由ID取得对象
	 * 
	 * @param id
	 *            对象的物理主键
	 * @return 返回对象PO
	 * @throws DataAccessException
	 */
	public T get(ID id) throws DataAccessException;

	/**
	 * 取得所有对象列表
	 * 
	 * @return 所有对象列表
	 * @throws DataAccessException
	 */
	public List<T> list() throws DataAccessException;

	/**
	 * 使用hql进行查询
	 * 
	 * @param hql
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> find(String hql) throws DataAccessException;

	/**
	 * 使用hql进行查询,参数使用?占位
	 * 
	 * @param hql
	 * @param values
	 *            参数列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> find(String hql, Object... values)
			throws DataAccessException;

	/**
	 * 使用hql进行分页查询,参数使用?占位
	 * 
	 * @param hql
	 *            hql语句
	 * @param countHql
	 *            用于确定记录总条数的hql语句
	 * @param pageSize
	 *            每页数据数量
	 * @param pageIndex
	 *            第几页索引,从1开始
	 * @param values
	 *            参数列表
	 * @return 分页的查询结果
	 * @throws DataAccessException
	 */

	public PaginationSupport find(String hql, String countHql, int pageSize,
			int pageIndex, Object... values) throws DataAccessException;

	/**
	 * 使用动态查询
	 * 
	 * @param criteria
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByCriteria(DetachedCriteria criteria)
			throws DataAccessException;

	/**
	 * 使用动态查询
	 * 
	 * @param criteria
	 * @param pageSize
	 *            每页数据数量
	 * @param pageIndex
	 *            第几页索引,从1开始
	 * @return 分页结果
	 * @throws DataAccessException
	 */
	public PaginationSupport findByCriteria(DetachedCriteria criteria,
			int pageSize, int pageIndex) throws DataAccessException;

	/**
	 * 使用entity 样例进行查询
	 * 
	 * @param exampleEntity
	 *            entity 样例
	 * @return
	 * @throws DataAccessException
	 */
	public List<T> findByExample(T exampleEntity) throws DataAccessException;

	/**
	 * 使用entity 样例进行查询
	 * 
	 * @param exampleEntity
	 *            entity 样例
	 * @param pageSize
	 *            每页数据数量
	 * @param pageIndex
	 *            第几页索引,从1开始
	 * @return
	 * @throws DataAccessException
	 */
	public PaginationSupport findByExample(T exampleEntity, int pageSize,
			int pageIndex) throws DataAccessException;

	/**
	 * 按以:开头的命令参数查询
	 * 
	 * @param queryString
	 * @param paramNames
	 *            参数名列表
	 * @param values
	 *            参数值列表
	 * @return
	 * @throws DataAccessException
	 */
	public List<?> findByNamedParam(String queryString, String[] paramNames,
			Object[] values) throws DataAccessException;

	public void refresh(T entity);

	/**
	 * 更新对象缓存
	 * 
	 * @param entity
	 */
	public void refresh(T entity, LockMode lockMode);

	/**
	 * 创建Clob对象
	 * 
	 * @param content
	 * @return
	 */
	public Clob createClob(String content);

	/**
	 * 创建Blob对象
	 * 
	 * @param bytes
	 * @return
	 */
	public Blob createBlob(byte[] bytes);
	
	/**
	 * 创建Blob对象
	 * @Title: createBlob 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param fileStream 文件流
	 * @param @param filesize 文件大小
	 * @param @return 设定文件 
	 * @return Blob 返回类型 
	 * @throws
	 */
	public Blob createBlob(InputStream fileStream, Long filesize);

	/**
	 * 使用sql查询，并映射到指定的vo类
	 * 
	 * @param sql
	 *            sql语句
	 * @param voClazz
	 *            vo类
	 * @return 查询结果，为List<VoClass>
	 */
	List<?> findVoBySql(String sql, Class voClazz);

	/**
	 * 使用sql查询，并返回包含Map 的List列表
	 * 
	 * @param sql
	 *            sql语句
	 * @return 查询结果，为List<Map>
	 */
	List<?> findMapBySql(String sql);

	/**
	 * 新增方法 SQL 并返回新增后数据库数据信息
	 * 
	 * @param values
	 *            数据
	 * @param tablename
	 *            表名
	 * @return
	 */
	public Map addByMap(final Map values, final String tablename);

	/**
	 * 批量添加方法 SQL
	 * 
	 * @param values
	 *            List<Map>
	 * @param tablename
	 * @return
	 */
	public List addBatchByList(final List values, final String tablename);

	/**
	 * 修改方法 SQL
	 * 
	 * @param values
	 * @param tablename
	 * @return
	 */
	public Map updateByMap(final Map values, final String tablename);

	/**
	 * 批量修改方法 SQL
	 * 
	 * @param values
	 * @param tablename
	 * @return
	 */
	public List updateBatchByList(final List values, final String tablename);

	/**
	 * 删除方法 SQL
	 * 
	 * @param tablename
	 *            表名
	 * @param where
	 *            条件
	 * @return 是否成功
	 */
	public Boolean deleteBySQL(String tablename, String where);

	/**
	 * 查询 SQL
	 * 
	 * @param tablename
	 *            查询表
	 * @param where
	 *            查询条件
	 * @return 数据列表
	 */
	public List queryBySQL(final String tablename, final String where);

	/**
	 * 查询 SQL
	 * 
	 * @param tablename
	 *            查询表
	 * @param columns
	 *            查询列集合
	 * @param where
	 *            查询条件
	 * @return 数据列表 List<Map>
	 */
	public List queryBySQL(final String tablename, final Collection columns,
			final String where);

	/**
	 * 分页查询 SQL
	 * 
	 * @param tablename
	 *            查询表
	 * @param where
	 *            查询条件
	 * @param pageIndex
	 *            索引页号
	 * @param pageSize
	 *            当前页条数
	 * @return 数据列表 List<Map>
	 */
	public PaginationSupport queryPageBySQL(final String tablename,
			final String where, final int pageIndex, final int pageSize);

	/**
	 * 分页查询 SQL
	 * 
	 * @param tablename
	 *            查询表
	 * @param columns
	 *            查询列集合
	 * @param where查询条件
	 * @param pageIndex
	 *            索引页号
	 * @param pageSize
	 *            当前页条数
	 * @return 分页的数据列表
	 */
	public PaginationSupport queryPageBySQL(final String tablename,
			final Collection columns, final String where, final int pageIndex,
			final int pageSize);

	/**
	 * 获取表信息
	 * 
	 * @param tablename
	 *            表名称
	 * @return
	 */
	public SqlVO getTableInfo(final String tablename);

	/**
	 * 实时获取表信息
	 * 
	 * @param tablename
	 *            表名称
	 * @return
	 */
	public SqlVO getTableInfoRT(final String tablename);

	/**
	 * 判断表是否存在
	 * 
	 * @param tablename
	 * @return
	 */
	public Boolean isExistTable(final String tablename);

	/**
	 * hibernate事务提交时，需要将所有缓存flush入数据库，<br/>
	 * Session启动一个事务，并按照insert,update,……,delete的顺序提交所有之前登记的操作， <br/>
	 * 如需要控制操作的执行顺序，需使用 flush
	 * 
	 */
	public void flush();
	/**
	 * 执行数据库更新
	 * 
	 * @param sql
	 */
	public Integer updateBySql(String sql);
}
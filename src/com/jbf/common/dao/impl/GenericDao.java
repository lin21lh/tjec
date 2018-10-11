/************************************************************
 * 类名：GenericDao.java
 *
 * 类别：Hibernate dao支持类实现 
 * 功能：通用数据操作基类，用于对持久层具体技术特性的抽象与封装
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-11  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.impl;

import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jbf.base.po.DBVersionPO;
import com.jbf.common.dao.IGenericDao;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.DaoUtil;
import com.jbf.common.dao.util.SqlBean;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.MapUtil;
import com.jbf.common.util.StringUtil;

public class GenericDao<T, ID extends Serializable> extends HibernateDaoSupport
		implements IGenericDao<T, ID> {
	private Log logger = LogFactory.getLog(getClass());

	protected Class<T> entityClass;

	@Autowired
	SqlBean sqlBean;

	@Autowired
	DaoUtil daoUtil;

	public GenericDao() {

	}

	@Autowired
	public void setSuperHibernateTemplate(HibernateTemplate hibernateTemplate) {
		super.setHibernateTemplate(hibernateTemplate);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Class getEntityClass() {
		if (entityClass == null) {
			entityClass = (Class<T>) ((ParameterizedType) getClass()
					.getGenericSuperclass()).getActualTypeArguments()[0];
			logger.debug("T class = " + entityClass.getName());
		}
		return entityClass;
	}

	@Override
	public Serializable save(T t) throws DataAccessException {
		if (t instanceof DBVersionPO) {
			if (((DBVersionPO) t).getDbversion() == null)
				((DBVersionPO) t).setDbversion(0);
		}
		return getHibernateTemplate().save(t);
	}

	@Override
	public void saveOrUpdate(T t) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(t);
	}

	@Override
	public void update(T t) throws DataAccessException {
		try {
			this.update(t, null);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
	public Blob createBlob(InputStream fileStream, Long filesize) {
		Session session = getHibernateTemplate().getSessionFactory().openSession();
		LobHelper lobHelper = session.getLobHelper();
		Blob blob = lobHelper.createBlob(fileStream, filesize);
		session.close();
		return blob;
	}

	@Override
	public void update(T t1, T t2) throws DataAccessException, AppException {
		if (t2 != null && t1 instanceof DBVersionPO) {
			DBVersionPO dbVersionPO1 = (DBVersionPO) t1;
			if (!dbVersionPO1.getDbversion().equals(
					((DBVersionPO) t2).getDbversion()))
				throw new AppException("update.data.conflict");

			dbVersionPO1.setDbversion(dbVersionPO1.getDbversion() + 1);
		}

		getHibernateTemplate().update(t1);
	}

	@Override
	public void delete(T t) throws DataAccessException {
		getHibernateTemplate().delete(t);
	}

	@Override
	public void delete(ID id) throws DataAccessException {
		getHibernateTemplate().delete(get(id));
	}

	@Override
	public void deleteAll(Collection<T> entities) throws DataAccessException {
		getHibernateTemplate().deleteAll(entities);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(ID id) throws DataAccessException {
		return (T) getHibernateTemplate().get(getEntityClass(), id);
	}

	@Override
	public List<T> list() throws DataAccessException {
		return (List<T>) getHibernateTemplate().find(
				" from " + getEntityClass().getName());
	}

	@Override
	public List<?> find(String hql) throws DataAccessException {
		return getHibernateTemplate().find(hql);
	}

	@Override
	public List<?> find(String hql, Object... values)
			throws DataAccessException {
		return getHibernateTemplate().find(hql, values);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginationSupport find(final String hql, final String countHql,
			final int pageSize, final int pageIndex, final Object... values)
			throws DataAccessException {
		return (PaginationSupport) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						int totalCount = Integer.parseInt(session
								.createQuery(countHql).uniqueResult()
								.toString());

						List<?> list = session.createQuery(hql).list();
						PaginationSupport ps = new PaginationSupport(list,
								totalCount, pageSize, (pageIndex - 1)
										* pageSize);
						return ps;
					}
				});
	}

	@Override
	public List<?> findByCriteria(DetachedCriteria criteria)
			throws DataAccessException {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginationSupport findByCriteria(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageIndex) throws DataAccessException {
		return (PaginationSupport) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						Object object = criteria.setProjection(Projections.rowCount()).uniqueResult();
						int totalCount = 0;
						if (StringUtil.isNotNull(object)) {
							totalCount = Integer.valueOf(String.valueOf(object));
						}
						criteria.setProjection(null);

						List items = criteria
								.setFirstResult((pageIndex - 1) * pageSize)
								.setMaxResults(pageSize).list();
						PaginationSupport ps = new PaginationSupport(items,
								totalCount, pageSize, (pageIndex - 1)
										* pageSize);
						return ps;
					}
				});
	}

	@Override
	public List<T> findByExample(T exampleEntity) throws DataAccessException {
		return getHibernateTemplate().findByExample(exampleEntity);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginationSupport findByExample(final T exampleEntity,
			final int pageSize, final int pageIndex) throws DataAccessException {
		return (PaginationSupport) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException {
						Example example = Example.create(exampleEntity);
						Criteria criteria = session.createCriteria(
								getEntityClass()).add(example);
						int totalCount = ((Integer) criteria.setProjection(
								Projections.rowCount()).uniqueResult())
								.intValue();
						criteria.setProjection(null);
						List items = criteria
								.setFirstResult((pageIndex - 1) * pageSize)
								.setMaxResults(pageSize).list();
						PaginationSupport ps = new PaginationSupport(items,
								totalCount, pageSize, (pageIndex - 1)
										* pageSize);
						return ps;
					}
				});
	}

	@Override
	public List<?> findByNamedParam(String queryString, String[] paramNames,
			Object[] values) throws DataAccessException {
		return getHibernateTemplate().findByNamedParam(queryString, paramNames,
				values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> findVoBySql(final String sql, final Class voClazz) {
		return (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						ResultSet rs = null;
						Statement statement = null;
						List items = new ArrayList();

						try {
							statement = conn.createStatement();
							System.out.println(sql);
							rs = statement.executeQuery(sql);
							ResultSetMetaData rsmd = rs.getMetaData();

							int columnCount = rsmd.getColumnCount();
							Map vo = null;
							while (rs.next()) {
								vo = new HashMap();
								for (int i = 1; i <= columnCount; i++) {
									vo.put(rsmd.getColumnName(i).toLowerCase(),
											rs.getObject(i));
								}
								items.add(vo);
							}

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (rs != null)
								rs.close();
							if (statement != null)
								statement.close();
							if (conn != null)
								conn.close();
						}

						if (items != null && items.size() > 0
								&& voClazz != null) {
							List temp = new ArrayList();
							for (Object obj : items) {
								try {
									temp.add(MapUtil.cloneMapToObj((Map) obj,
											voClazz.newInstance()));
								} catch (InstantiationException e) {
									e.printStackTrace();
								} catch (IllegalAccessException e) {
									e.printStackTrace();
								}
							}
							items.clear();
							items.addAll(temp);
						}
						return items;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<?> findMapBySql(final String sql) {
		return (List<?>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						ResultSet rs = null;
						Statement statement = null;
						List items = new ArrayList();
						try {
							statement = conn.createStatement();
							System.out.println(sql);
							rs = statement.executeQuery(sql);
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnCount = rsmd.getColumnCount();
							Map vo = null;
							while (rs.next()) {
								vo = new HashMap();
								for (int i = 1; i <= columnCount; i++) {
									vo.put(rsmd.getColumnName(i).toLowerCase(),
											rs.getObject(i));
								}
								items.add(vo);
							}
						} finally {
							if (rs != null)
								rs.close();
							if (statement != null)
								statement.close();
							if (conn != null)
								conn.close();
						}
						return items;
					}
				});
	}

	@Override
	public void refresh(T entity) {
		getHibernateTemplate().refresh(entity);
	}

	@Override
	public void refresh(T entity, LockMode lockMode) {
		getHibernateTemplate().refresh(entity, lockMode);
	}

	@Override
	public Clob createClob(String content) {
		return Hibernate.createClob(content);
	}

	@Override
	public Blob createBlob(byte[] bytes) {
		return Hibernate.createBlob(bytes);
	}

	public Map addByMap(final Map values, final String tablename) {

		return (Map) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						return addM(values, tablename, conn);
					}
				});
	}

	public Map addM(final Map values, final String tablename, Connection conn)
			throws SQLException {

		PreparedStatement prpe = null;
		try {
			SqlVO vo = sqlBean.getTableInfo(tablename, "", conn, null, true);
			prpe = conn.prepareStatement(vo.getInsertSql());
			int i = 1;
			if (vo.getKeys().size() == 1) { // 判断是否只有一个主键
				ColVO key = (ColVO) vo.getKeys().get(0);
				// 判断主键是否为空
				if ((values.get(key.getName()) == null || Integer
						.parseInt(values.get(key.getName()).toString()) == 0)
						&& key.isIsnumber()) {
					// 并且是数字型的如果是 通过序列生成
					values.put(key.getName(),
							daoUtil.getSequenceNextVal(tablename, conn));
				}
			}

			i = daoUtil.addSetPrepareStatement(values, i, prpe, vo.getCols()); // 将数据放入预存的游标中
			prpe.executeUpdate();
		} finally {
			if (prpe != null)
				prpe.close();
		}

		return values;

	}

	public List addBatchByList(final List values, final String tablename) {
		return (List) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						if (values == null || values.size() <= 0)
							return null;

						Connection conn = session.connection();

						SqlVO vo = sqlBean.getTableInfo(tablename, null, conn,
								null, true);
						PreparedStatement prpe = null;
						prpe = conn.prepareStatement(vo.getInsertSql());
						int i = 1;

						for (Iterator iterator = values.iterator(); iterator
								.hasNext();) {
							i = 1;
							Map object = (Map) iterator.next();

							if (vo.getKeys().size() == 1) { // 判断是否只有一个主键
								ColVO key = (ColVO) vo.getKeys().get(0);
								if (key.isIsnumber()
										&& (object.get(key.getName()) == null || Integer
												.parseInt(object.get(
														key.getName())
														.toString()) == 0)) {
									// 判读主键是否为空 并且是数字型的如果是 通过序列生成
									object.put(key.getName(),
											daoUtil.getSequenceNextVal(
													tablename, conn));
								}
							}
							daoUtil.addSetPrepareStatement(object, i, prpe,
									vo.getCols()); // 将数据放入预存的游标中
							prpe.addBatch();
						}
						prpe.executeBatch();
						prpe.close();
						return values;
					}
				});
	}

	public Map updateByMap(final Map values, final String tablename) {
		return updateByMap(values, tablename, true);
	}

	public Map updateByMap(final Map values, final String tablename,
			final boolean isUpdateNull) {
		return (Map) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						return updateM(values, tablename, conn, isUpdateNull);
					}
				});
	}

	public Map updateM(final Map values, final String tablename,
			Connection conn, final boolean isUpdateNull) throws SQLException {

		SqlVO vo = sqlBean.getTableInfo(tablename, conn, values, null,
				isUpdateNull);
		PreparedStatement prpe = null;
		System.out.println(vo.getUpdateSql());
		prpe = conn.prepareStatement(vo.getUpdateSql());

		int i = 1;
		i = daoUtil.setPrepareStatement(values, i, prpe, vo.getCols());
		daoUtil.setPrepareStatement(values, i, prpe, vo.getKeys());
		prpe.executeUpdate();
		prpe.close();

		return values;

	}

	public List updateBatchByList(final List values, final String tablename) {
		return (List) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						if (values == null || values.size() <= 0)
							return null;
						Connection conn = session.connection();
						SqlVO vo = sqlBean.getTableInfo(tablename, null, conn,
								null, true);
						PreparedStatement prpe = null;

						prpe = conn.prepareStatement(vo.getUpdateSql());
						int i = 1;
						for (Iterator iterator = values.iterator(); iterator
								.hasNext();) {
							i = 1;
							Map object = (Map) iterator.next();
							i = daoUtil.setPrepareStatement(object, i, prpe,
									vo.getCols());
							daoUtil.setPrepareStatement(object, i, prpe,
									vo.getKeys());
							prpe.addBatch();
						}
						prpe.executeBatch();
						prpe.close();

						return values;
					}
				});

	}

	public Boolean deleteBySQL(String tablename, String where) {
		final StringBuffer sql = new StringBuffer("delete ").append("  from ")
				.append(tablename).append(" where ").append(where);
		return (Boolean) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						Connection conn = session.connection();

						Statement statement = null;
						try {
							statement = conn.createStatement();
							statement.executeUpdate(sql.toString());
							return new Boolean(true);
						} catch (SQLException e) {
							e.printStackTrace();
							return new Boolean(false);

						} finally {

							if (statement != null)
								statement.close();

						}

					}
				});
	}

	public List queryBySQL(final String tablename, final String where) {
		return queryBySQL(tablename, null, where);
	}

	public List queryBySQL(final String tablename, final Collection columns,
			final String where) {

		return (List) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						return queryM(tablename, columns, where, conn);
					}
				});
	}

	public List queryM(String tablename, Collection columns, String where,
			Connection conn) throws SQLException {

		SqlVO vo = sqlBean.getQuerySqlVo(columns, tablename, where, conn, null,
				true);

		columns = vo.getCols();
		ResultSet rs = null;
		Statement statement = null;
		try {
			statement = conn.createStatement();
			System.out.println(vo.getSql());
			rs = statement.executeQuery(vo.getSql());
			List list = new ArrayList();
			Map map = null;
			ColVO column = null;
			int i = 1;
			Object anObject = null;
			while (rs.next()) {
				i = 1;
				map = new HashMap();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					column = (ColVO) iterator.next();
					anObject = rs.getObject(i);
					map.put(column.getName(), anObject);
					i++;
				}
				list.add(map);
			}
			return list;
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

	public PaginationSupport queryPageBySQL(final String tablename,
			final String where, final int pageIndex, final int pageSize) {
		return queryPageBySQL(tablename, null, where, pageIndex, pageSize);
	}

	public PaginationSupport queryPageBySQL(final String tablename,
			final Collection columns, final String where, final int pageIndex,
			final int pageSize) {

		return (PaginationSupport) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						SqlVO vo = sqlBean.getQuerySqlVo(columns, tablename,
								where, conn, null, true); // 获得数据库表的结构
						Collection columns = vo.getCols();
						ResultSet rs = null;
						Statement statement = null;
						int start = (pageIndex - 1) * pageSize;
						PaginationSupport ps = null;
						try {
							statement = conn.createStatement();

							rs = statement.executeQuery("select * from("
									+ vo.getSql() + ") where rownum<="
									+ (pageSize + start)); // 执行sql
							System.out.println("select * from(" + vo.getSql()
									+ ") where rownum<=" + (pageSize + start));
							List list = new JSONArray();
							Map map = null;
							ColVO column = null;
							int i = 1;
							int j = 0;
							Object anObject = null;

							while (rs.next()) {
								i = 1;
								if (j >= start) { // 判读从多少行开始接收数据
									map = new JSONObject();
									for (Iterator iterator = columns.iterator(); iterator
											.hasNext();) {
										column = (ColVO) iterator.next();
										anObject = rs.getObject(i);
										if (anObject instanceof java.sql.Date) {
											map.put(column.getName(),
													DateUtil.getDateString(((java.sql.Date) anObject)));
										} else {

											if (!(anObject instanceof java.sql.Clob)
													&& !(anObject instanceof java.sql.Blob)) {// java.sql.Clob
												map.put(column.getName(),
														anObject);
											}
										}

										i++;
									}
									list.add(map);

								}

								j++;
							}
							ps = new PaginationSupport(list, getCount(conn,
									tablename + " t ", where), pageSize, start);
							return ps;
						} finally {
							if (rs != null)
								rs.close();
							if (statement != null)
								statement.close();

						}

					}
				});
	}

	public int getCount(Connection conn, String tablename, String where) {

		String sql = "select count(*) from " + tablename
				+ (where == null || where.equals("") ? "" : " where " + where);
		Object count = getSQLObject(conn, sql);
		return count == null ? 0 : Integer.parseInt(count.toString());

	}

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

	public SqlVO getTableInfo(final String tablename) {
		return (SqlVO) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						return sqlBean.getTableInfo(tablename, "", conn, null,
								true);
					}
				});
	}

	public SqlVO getTableInfoRT(final String tablename) {
		return (SqlVO) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						return sqlBean.getTableInfo(tablename, "", conn, null,
								false);
					}
				});
	}

	public Boolean isExistTable(final String tablename) {

		return (Boolean)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = session.connection();
				return daoUtil.isExistTable(tablename, conn);
			}
		});
	}

	@Override
	public void flush() {
		super.getHibernateTemplate().flush();
	}
	@Override
	public Integer updateBySql(final String sql) {
		System.out.println("UpdateBySql:" + sql);
		return (Integer) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = session.connection();
						Statement statement = null;
						int count = 0;
						try {
							statement = conn.createStatement();
							count = statement.executeUpdate(sql);
						} catch (Exception e) {
							e.printStackTrace();
							count = 0;
						} finally {
							statement.close();
						}
						return count;
					}
				});

	}
}
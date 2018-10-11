/************************************************************
 * 类名：MapDataDaoImpl.java
 *
 * 类别：MapDataDao dao支持类实现(使用SQL)
 * 功能：通用数据操作基类，用于对持久层具体技术特性的抽象与封装
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-3-31  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import oracle.jdbc.driver.OracleConnection;
import oracle.sql.CLOB;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.nativejdbc.C3P0NativeJdbcExtractor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freework.queryData.compileSQL.SqlAndParam;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.DaoUtil;
import com.jbf.common.dao.util.SqlBean;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.dao.util.WeblogicDataSource;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Repository("com.jbf.common.dao.MapDataDao")
public class MapDataDaoImpl extends HibernateDaoSupport implements MapDataDaoI {

	private Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	public SqlBean sqlBean = null;
	
	@Autowired
	private DaoUtil daoUtil = null;
	
	/**
	 * 获取数据库连接
	 * @param jndiName
	 * @param session
	 * @return
	 * @throws AppException
	 */
	protected Connection getConnection(String jndiName, Session session) {
		
		Connection conn = null;
		if (StringUtil.isNotBlank(jndiName)) {
			try {
				conn = WeblogicDataSource.getInstance().getConnection(jndiName);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NamingException e) {
				logger.info(AppException.getMessage("jndiName.deploy.err", new String[] {jndiName}));
				e.printStackTrace();
			}
		} else {
			conn = session.connection();
		}
		
		return conn;
	}
	
	public Map add(final Map value, final String tablename) {
		
		return addRemote(value, tablename, null);
	}
	
	@SuppressWarnings("unchecked")
	public Map addRemote(final Map value, final String tablename, final String jndiName) {
		return (Map)super.getHibernateTemplate().execute(
				new HibernateCallback() {
					
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Connection conn = getConnection(jndiName, session);
						return addM(value, tablename, conn, jndiName);
					} 
				}
		);
	}
	
	protected Map addM(final Map value, final String tablename, Connection conn, final String jndiName) throws SQLException {

		SqlVO vo = sqlBean.getTableInfo(tablename, conn, value, jndiName, true);
		PreparedStatement prpe = null;
		prpe = conn.prepareStatement(vo.getInsertSql());
		int i = 1;
		if (vo.getKeys().size() == 1) { //判断是否只有一个主键
			ColVO key = (ColVO) vo.getKeys().get(0);
			//判断主键是否为空
			if ((value.get(key.getName()) == null || Integer.parseInt(value.get(key.getName()).toString()) == 0) && key.isIsnumber()) {
				//并且是数字型的如果是 通过序列生成
				value.put(key.getName(), daoUtil.getSequenceNextVal(tablename, conn));
			}
		}
		
		i = daoUtil.addSetPrepareStatement(value, i, prpe, vo.getCols()); //将数据放入预存的游标中
		prpe.executeUpdate();
		prpe.close();
		
		return value;
	}
	
	public List addBatch(final List values, final String tablename) {
		return addBatchRemote(values, tablename, null);
	}
	
	@SuppressWarnings("unchecked")
	public List addBatchRemote(final List values, final String tablename, final String jndiName) {
		return (List) super.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				if (values == null || values.size() <= 0)
					return null;
				
				Connection conn = getConnection(jndiName, session);

				SqlVO vo = sqlBean.getTableInfo(tablename, conn, null, jndiName, true);
				PreparedStatement prpe = null;
				prpe = conn.prepareStatement(vo.getInsertSql());
				int i = 1;

				for (Iterator iterator = values.iterator(); iterator.hasNext();) {
					i = 1;
					Map object = (Map) iterator.next();

					if (vo.getKeys().size() == 1) { // 判断是否只有一个主键
						ColVO key = (ColVO) vo.getKeys().get(0);
						if (key.isIsnumber() && (object.get(key.getName()) == null || Integer.parseInt(object.get(key.getName()).toString()) ==0)) { 
							// 判读主键是否为空 并且是数字型的如果是 通过序列生成

							object.put(key.getName(), daoUtil.getSequenceNextVal(tablename, conn));
						}
					}
					daoUtil.addSetPrepareStatement(object, i, prpe, vo.getCols()); // 将数据放入预存的游标中
					prpe.addBatch();
				}
				prpe.executeBatch();
				prpe.close();
				return values;
			}
		});
	}
	
	public Map update(final Map value, final String tablename) {
		return update(value, tablename, false);
	}
	
	public Map update(final Map value, final String tablename, final boolean isUpdateNull) {
		return updateRemote(value, tablename, isUpdateNull, null);
	}
	
	public Map updateRemote(final Map value, final String tablename, final String jndiName) {
		return updateRemote(value, tablename, false, jndiName);
	}
	
	@SuppressWarnings("unchecked")
	public Map updateRemote(final Map value, final String tablename, final boolean isUpdateNull, final String jndiName) {
		return (Map) super.getHibernateTemplate().execute(new HibernateCallback() {
			
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Connection conn = getConnection(jndiName, session);
					return updateM(value, tablename, conn, jndiName, isUpdateNull);
				}
			});
	}
	
	protected Map updateM(final Map value, final String tablename,  Connection conn, final String jndiName, final boolean isUpdateNull) throws SQLException {

		SqlVO vo = sqlBean.getTableInfo(tablename, conn, value, jndiName, isUpdateNull);
		PreparedStatement prpe = null;

		prpe = conn.prepareStatement(vo.getUpdateSql());

		int i = 1;
		i = daoUtil.setPrepareStatement(value, i, prpe, vo.getCols());
		daoUtil.setPrepareStatement(value, i, prpe, vo.getKeys());
		prpe.executeUpdate();
		prpe.close();
		
		if (StringUtil.isNotBlank(jndiName) && conn != null)
			conn.close();

		return value;
	
	}
	
	public List updateBatch(final List values, final String tablename) {
		return updateBatch(values, tablename, false);
	}
	
	public List updateBatch(final List values, final String tablename, final boolean UpdateNULL) {
		return updateBatchRemote(values, tablename, UpdateNULL, null);
	}
	
	public List updateBatchRemote(final List values, final String tablename, final String jndiName) {
		return updateBatchRemote(values, tablename, false, jndiName);
	}
	
	@SuppressWarnings("unchecked")
	public List updateBatchRemote(final List values, final String tablename, final boolean UpdateNULL, final String jndiName) {
		return (List) super.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				if (values == null || values.size() <= 0)
					return null;
				
				Connection conn = getConnection(jndiName, session);
				SqlVO vo = sqlBean.getTableInfo(tablename, conn, null, jndiName, UpdateNULL, true);
				PreparedStatement prpe = null;

				prpe = conn.prepareStatement(vo.getUpdateSql());
				int i = 1;
				for (Iterator iterator = values.iterator(); iterator.hasNext();) {
					i = 1;
					Map object = (Map) iterator.next();
					i = daoUtil.setPrepareStatement(object, i, prpe, vo.getCols());
					daoUtil.setPrepareStatement(object, i, prpe, vo.getKeys());
					prpe.addBatch();
				}
				prpe.executeBatch();
				prpe.close();
				
				if (StringUtil.isNotBlank(jndiName) && conn != null)
					conn.close();

				return values;
			}
		});
	}
	
	public int updateTX(final String sql) {
		return updateRemoteTX(sql, null);
	}
	
	public int updateRemoteTX(final String sql, final String jndiName) {
		return (Integer) super.getHibernateTemplate().execute(
				new HibernateCallback() {

					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Connection conn = getConnection(jndiName, session);

						Statement statement = conn.createStatement();

						int n = statement.executeUpdate(sql);

						if (statement != null)
							statement.close();
						
						if (StringUtil.isNotBlank(jndiName) && conn != null)
							conn.close();

						return n;
					}
				}
		);
	}
	
	public Boolean delete(String tablename, String where) {
		return deleteRemote(tablename, where, null);
	}
	
	@SuppressWarnings("unchecked")
	public Boolean deleteRemote(String tablename, String where, final String jndiName) {
		final StringBuffer sql = new StringBuffer("delete ").append("  from ").append(tablename).append(" where ").append(where);
		return (Boolean) super.getHibernateTemplate().execute(new HibernateCallback() {

				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					Connection conn = getConnection(jndiName, session);

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
						
						if (StringUtil.isNotBlank(jndiName) && conn != null)
							conn.close();

					}

				}
			});
	}
	
	public List queryListBySQL(final String sql) {
		return queryListBySQLRemote(sql, null);
	}
	
	@SuppressWarnings("unchecked")
	public List queryListBySQLRemote(final String sql, final String jndiName) {
		return (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				Statement statement = null;
				List list = new JSONArray();
				PaginationSupport ps = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					int j=0;
					Object anObject = null;
					while (rs.next()) {
						vo = new JSONObject();
						for (int i = 1; i <= columnCount; i++) {
							anObject = rs.getObject(i);
							if (anObject instanceof java.sql.Date) {
								vo.put(rsmd.getColumnName(i).toLowerCase(), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
							}else{
								vo.put(rsmd.getColumnName(i).toLowerCase(),rs.getObject(i));
							}
						}
						list.add(vo);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				return list;
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public List query4app(final SqlAndParam sqlParam,final Integer pageNum,final Integer pageSize) {
		return (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(null, session);
				/*添加分页条件*/
				int start = (pageNum-1)*pageSize + 1;
				int end = pageNum * pageSize;
				String sql = "select * from (select a.*,rownum rn from (" + sqlParam.getSql() + ")a) where rn between " + start + " and " + end ;
				List<Object> params = sqlParam.getParam();
				ResultSet rs = null;
//				Statement statement = null;
				PreparedStatement psm = null;
				List list = new LinkedList<String>();
//				PaginationSupport ps = null;
				try {
//					statement = conn.createStatement();
					psm = conn.prepareStatement(sql);
					for(int i=0;i<params.size();i++){
//						psm.setString(i, params.get(i).toString());
						psm.setObject(i+1, params.get(i));
					}
					rs = psm.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					Object anObject = null;
					while (rs.next()) {
						vo = new HashMap<String, Object>();
						for (int i = 1; i <= columnCount; i++) {
							anObject = rs.getObject(i);
							if(anObject == null){
								vo.put(rsmd.getColumnName(i).toLowerCase(),"");
							}else if (anObject instanceof java.sql.Date) {
								vo.put(rsmd.getColumnName(i).toLowerCase(), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
							} else if (anObject instanceof Clob){
								Clob clob = (Clob) anObject;
//								String reString = "";
//								Reader is = clob.getCharacterStream();// 得到流
//								BufferedReader br = new BufferedReader(is);
//								String s = br.readLine();
//								StringBuffer sb = new StringBuffer();
//								while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer
//									//由StringBuffer转成STRING
//									sb.append(s);
//									s = br.readLine();//存在问题：得到的字符串没有结束符
//								}
//								reString = sb.toString();
//								vo.put(rsmd.getColumnName(i).toLowerCase(),reString);
								String clobStr = clob == null ? null : clob.getSubString(1, (int)clob.length());
								vo.put(rsmd.getColumnName(i).toLowerCase(),clobStr);
								
							} else {
								vo.put(rsmd.getColumnName(i).toLowerCase(),rs.getObject(i).toString());
							}
						}
						list.add(vo);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (psm != null)
						psm.close();
					if (conn != null)
						conn.close();
				}
				return list;
			}
		});
	}
	
	public List queryListBySQLParam(final String sql, final Object... param) {
		return queryListBySQLParamRemote(sql, null, param);
	}
	
	@SuppressWarnings("unchecked")
	public List queryListBySQLParamRemote(final String sql, final String jndiName, final Object... param) {
		return (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				//OraclePreparedStatement statement = null;
				PreparedStatement statement = null;
				OracleConnection connection = null;
				List list = new JSONArray();
				PaginationSupport ps = null;
				try {
					//NewProxyConnection转换为OracleConnection
					C3P0NativeJdbcExtractor cp30NativeJdbcExtractor = new C3P0NativeJdbcExtractor();  
                    connection = (OracleConnection) cp30NativeJdbcExtractor.getNativeConnection(conn);  
					//statement = (OraclePreparedStatement)connection.prepareStatement(sql);
					statement = conn.prepareStatement(sql);
					if (param[0] != null && param[0].toString().length() > 0) {						
						//statement.setStringForClob(1, param[0].toString());
						CLOB clob = CLOB.createTemporary(connection, false, oracle.sql.CLOB.DURATION_SESSION);
						clob.open(CLOB.DURATION_SESSION);
						Writer writer = clob.getCharacterOutputStream();
						writer.write(param[0].toString().toCharArray());
						writer.flush();
						writer.close();
						statement.setClob(1, clob);
					}
					rs = statement.executeQuery();
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					int j=0;
					Object anObject = null;
					while (rs.next()) {
						vo = new JSONObject();
						for (int i = 1; i <= columnCount; i++) {
							anObject = rs.getObject(i);
							if (anObject instanceof java.sql.Date) {
								vo.put(rsmd.getColumnName(i).toLowerCase(), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
							}else{
								vo.put(rsmd.getColumnName(i).toLowerCase(),rs.getObject(i));
							}
						}
						list.add(vo);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && connection != null)
						connection.close();
					
					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				return list;
			}
		});
	}
	
	public PaginationSupport queryPageBySQL(final String sql, final int pageIndex, final int pageSize) {
		return queryPageBySQLRemote(sql, pageIndex, pageSize, null);
	}
	
	@SuppressWarnings("unchecked")
	public PaginationSupport queryPageBySQLRemote(final String sql, final int pageIndex, final int pageSize, final String jndiName) {
		return (PaginationSupport) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				Statement statement = null;
				List list = new JSONArray();
				int start = (pageIndex - 1) * pageSize;
				PaginationSupport ps = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery("select * from("+sql+") where rownum<=" + (pageSize + start));
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					int j=0;
					Object anObject = null;
					while (rs.next()) {
						if (j >= start) { // 判读从多少行开始接收数据
							vo = new JSONObject();
							for (int i = 1; i <= columnCount; i++) {
								anObject = rs.getObject(i);
								if (anObject instanceof java.sql.Date) {
									vo.put(rsmd.getColumnName(i).toLowerCase(), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
								}else{
									vo.put(rsmd.getColumnName(i).toLowerCase(),rs.getObject(i));
								}
							}
							list.add(vo);
						}
						j++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				ps = new PaginationSupport(list, getCount(conn, "(" + sql + ") t ", "1=1"), pageSize, start);
				return ps;
			}
		});
	}
	
	public List queryList(final String tablename, final String where) {
		return queryListRemote(tablename, where, null);
	}
	
	public List queryList(final String tablename, final Collection<String> columns, final String where) {
		return queryListRemote(tablename, columns, where, null);
	}
	
	public List queryListRemote(final String tablename, final String where, final String jndiName) {
		return queryListRemote(tablename, null, where, jndiName);
	}

	@SuppressWarnings("unchecked")
	public List queryListRemote(final String tablename, final Collection<String> columns, final String where, final String jndiName) {
		return (List) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);
				SqlVO vo = sqlBean.getQuerySqlVo(columns, tablename, where, conn, jndiName);
				Collection<ColVO> columns = vo.getCols();
				ResultSet rs = null;
				Statement statement = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery( vo.getSql()); // 执行sql
	
					List list = new JSONArray();
					Map map = null;
					ColVO column = null;
					int i = 1;
					Object anObject = null;
					while (rs.next()) {
						i = 1;
						map = new JSONObject();
						for (Iterator<ColVO> iterator = columns.iterator(); iterator.hasNext();) {
							column = iterator.next();
							anObject = rs.getObject(i);
							map.put(column.getName(), anObject);
							i++;
						}
						list.add(map);
					}
					return list;
				} finally {
					if (rs != null)
						rs.close();
					
					if (statement != null)
						statement.close();
					
					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
			}
		});
	}
	
	public PaginationSupport queryPage(final String tablename, final String where, final int pageIndex, final int pageSize) {
		return queryPageRemote(tablename, where, pageIndex, pageSize, null);
	}
	
	public PaginationSupport queryPage(final String tablename, final Collection<String> columns, final String where, final int pageIndex, final int pageSize) {
		return queryPageRemote(tablename, columns, where, pageIndex, pageSize, null);
	}
	
	public PaginationSupport queryPageRemote(final String tablename, final String where, final int pageIndex, final int pageSize, final String jndiName) {
		return queryPageRemote(tablename, null, where, pageIndex, pageSize, jndiName);
	}
	
	@SuppressWarnings("unchecked")
	public PaginationSupport queryPageRemote(final String tablename, final Collection<String> columns, final String where, final int pageIndex, final int pageSize, final String jndiName) {

		return (PaginationSupport) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Connection conn = getConnection(jndiName, session);
						SqlVO vo = sqlBean.getQuerySqlVo(columns, tablename, where, conn, jndiName); // 获得数据库表的结构
						Collection<ColVO> columns = vo.getCols();
						ResultSet rs = null;
						Statement statement = null;
						int start = (pageIndex - 1) * pageSize;
						PaginationSupport ps = null;
						try {
							statement = conn.createStatement();
							rs = statement.executeQuery("select * from(" + vo.getSql() + ") where rownum<=" + (pageSize + start)); // 执行sql
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
									for (Iterator<ColVO> iterator = columns.iterator(); iterator.hasNext();) {
										column = iterator.next();
										anObject = rs.getObject(i);
										if (anObject instanceof java.sql.Date) {
											map.put(column.getName(), DateUtil.getDateString(((java.sql.Date) anObject)));
										} else {
											if (!(anObject instanceof java.sql.Clob) && !(anObject instanceof java.sql.Blob)) {// java.sql.Clob
												map.put(column.getName(), anObject);
											}
										}
										i++;
									}
									list.add(map);
								}
								j++;
							}
							ps = new PaginationSupport(list, getCount(conn, tablename + " t ", where), pageSize, start);
							return ps;
						} finally {
							if (rs != null)
								rs.close();
							if (statement != null)
								statement.close();
							
							if (StringUtil.isNotBlank(jndiName) && conn != null)
								conn.close();

						}

					}
				});
	}
	
	protected int getCount(Connection conn, String tablename, String where) {

		String sql = "select count(*) from " + tablename + (where == null || where.equals("") ? "" : " where " + where);
		Object count = getSQLObject(conn, sql);
		return count == null ? 0 : Integer.parseInt(count.toString());

	}
	
	protected Object getSQLObject(Connection conn, String sql) {
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
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public InputStream getBlob(final String blobColumn, final String keyColumn, final int keyValue, final String table) {
		return getBlobRemote(blobColumn, keyColumn, keyValue, table, null);
	}
	
	@SuppressWarnings("unchecked")
	public InputStream getBlobRemote(final String blobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName) {
		return (InputStream) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT " + blobColumn + " FROM " + table + " WHERE " + keyColumn + "=? FOR UPDATE";
				Connection conn = getConnection(jndiName, session);
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, keyValue);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					Object obj = rs.getBlob(1);
					if (obj == null)
						return null;
					Class cla = obj.getClass();
					Method method;
					try {
						InputStream in = null;
						method = cla.getMethod("length", new Class[]{});
						long length = (Long) method.invoke(obj, new Object[]{});
						if (length > 0) {
							method = cla.getMethod("getBinaryStream", new Class[]{});
							in = (InputStream) method.invoke(obj, new Object[]{});
						}
						return in;
					} catch (SecurityException e) {
						e.printStackTrace();
						return null;
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
						return null;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return null;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return null;
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						return null;
					} finally {
						
						if (pstmt != null)
							pstmt.close();
						
						if (rs != null)
							rs.close();
						
						if (StringUtil.isNotBlank(jndiName) && conn != null)
							conn.close();
					}
				} else {
					return null;
				}
			}
		});
	}
	
	public String getClob(final String clobColumn, final String keyColumn, final int keyValue, final String table) {
		return getClobRemote(clobColumn, keyColumn, keyValue, table, null);
	}
	
	@SuppressWarnings("unchecked")
	public String getClobRemote(final String clobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName) { 
		return  (String) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT " + clobColumn + " FROM " + table + " WHERE " + keyColumn + "=? FOR UPDATE ";
				Connection conn = getConnection(jndiName, session);
				PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, keyValue);
	            ResultSet rs = stmt.executeQuery();
	            String buffer="";
	            if(rs.next()) {
	                Clob clobtmp =  rs.getClob(1);
					if(clobtmp==null || clobtmp.length()==0) {
	                    buffer = "";
	                } else {
	                    buffer = clobtmp.getSubString((long)1,(int)clobtmp.length());
	                }
	            }
	            if (rs != null)
	            	rs.close();
	            
	            if (stmt != null)
	            	stmt.close();
	            if (StringUtil.isNotBlank(jndiName) && conn != null)
	            	conn.close();
	            return buffer;
			}}
		);
	}
	
	public void setBlob(final InputStream in, final String blobColumn, final String keyColumn, final int keyValue, final String table) {
		setBlobRemote(in, blobColumn, keyColumn, keyValue, table, null);
	}
	
	@SuppressWarnings("unchecked")
	public void setBlobRemote(final InputStream in, final String blobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName) {
		super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Connection conn = getConnection(jndiName, session);
				String sql = "SELECT " + blobColumn + " FROM " + table + " WHERE " + keyColumn + "=? FOR UPDATE";
				PreparedStatement stmt = null;
				ResultSet rs = null;
				Statement st = conn.createStatement();
				st.execute("UPDATE " + table + " SET " + blobColumn + "=EMPTY_BLOB() WHERE " + keyColumn + "=" + keyValue);
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, keyValue);
				rs = stmt.executeQuery();
				try {
					if (rs.next()) {
						Object obj = rs.getBlob(1);
						Class cla = obj.getClass();
						try {
							Method method = cla.getMethod("getBinaryOutputStream", new Class[]{});
							OutputStream out = (OutputStream) method.invoke(obj, new Object[]{});
							byte[] b = new byte[1024];
							int len = 0;
							while ((len = in.read(b)) != -1) {
								out.write(b, 0, len);
							}
							in.close();
							out.flush();
							out.close();
							return true;
						} catch (SecurityException e) {
							e.printStackTrace();
							return false;
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
							return false;
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							return false;
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							return false;
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							return false;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return false;
						}
					} else {
						return false;
					}
				} finally {
					if (st != null) {
						st.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (rs != null) {
						rs.close();
					}
					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
			}
		});
	}
	
	public void setClob(final String content, final String clobColumn, final String keyColumn, final int keyValue, final String table) {
		setClobRemote(content, clobColumn, keyColumn, keyValue, table, null);
	}
	
	@SuppressWarnings("unchecked")
	public void setClobRemote(final String content, final String clobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName) {
		super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Connection conn = getConnection(jndiName, session);
				String sql = "SELECT " + clobColumn + " FROM " + table + " WHERE " + keyColumn + "=? FOR UPDATE ";
			    PreparedStatement stmt = null;
			    ResultSet rs = null;  
			    Statement st=conn.createStatement();
			    st.execute("update "+ table+" set "+clobColumn+"= EMPTY_CLOB()  WHERE " + keyColumn + "="+keyValue);
			    st.close();
			    stmt = conn.prepareStatement(sql);
			    stmt.setInt(1, keyValue);
			    rs = stmt.executeQuery();
			    Clob clobtt = null;
			    if (rs.next()) {
			    	clobtt =  rs.getClob(1);
			    }
			    Writer wr = clobtt.setCharacterStream(0L);
			    try {
			    	wr.write(content);
			    } catch (IOException e) {
			    	// TODO Auto-generated catch block
			    	e.printStackTrace();
			    } finally {
			    	try {
			    		wr.flush();
			    	} catch (IOException e) {
			    		// TODO Auto-generated catch block
			    		e.printStackTrace();
			    	}
			    	try {
			    		wr.close();
			    	} catch (IOException e) {
			    		// TODO Auto-generated catch block
			    		e.printStackTrace();
			    	}
			    }
				rs.close();
				stmt.close();
				if (StringUtil.isNotBlank(jndiName) && conn != null)
					conn.close();
				return true;
			}
		});
	}

	public void setDaoUtil(DaoUtil daoUtil) {
		this.daoUtil = daoUtil;
	}
	
	public DaoUtil getDaoUtil() {
		return daoUtil;
	}
	
	public void setSqlBean(SqlBean sqlBean) {
		this.sqlBean = sqlBean;
	}

	public SqlBean getSqlBean() {
		return sqlBean;
	}
	
	@Autowired
	public void setSuperHibernateTemplate(HibernateTemplate hibernateTemplate) {
		super.setHibernateTemplate(hibernateTemplate);
	}

	@Override
	public PaginationSupport queryPageForConvert(String tablename, String where,
			int pageIndex, int pageSize) {
		return queryPageRemoteForConvert(tablename,null , where, pageIndex, pageSize, null);
	}
	@SuppressWarnings("unchecked")
	public PaginationSupport queryPageRemoteForConvert(final String tablename, final Collection<String> columns, final String where, final int pageIndex, final int pageSize, final String jndiName) {

		return (PaginationSupport) super.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session) throws HibernateException, SQLException {
						Connection conn = getConnection(jndiName, session);
						SqlVO vo = sqlBean.getQuerySqlVo(columns, tablename, where, conn, jndiName); // 获得数据库表的结构
						Collection<ColVO> columns = vo.getCols();
						ResultSet rs = null;
						Statement statement = null;
						int start = (pageIndex - 1) * pageSize;
						PaginationSupport ps = null;
						try {
							statement = conn.createStatement();
							rs = statement.executeQuery("select * from(" + vo.getSql() + ") where rownum<=" + (pageSize + start)); // 执行sql
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
									for (Iterator<ColVO> iterator = columns.iterator(); iterator.hasNext();) {
										column = iterator.next();
										anObject = rs.getObject(i);
										if (anObject instanceof java.sql.Date) {
											map.put(StringChangeToUpper(column.getName().toLowerCase()), DateUtil.getDateString(((java.sql.Date) anObject)));
										} else {
											if (!(anObject instanceof java.sql.Clob) && !(anObject instanceof java.sql.Blob)) {// java.sql.Clob
												map.put(StringChangeToUpper(column.getName().toLowerCase()), anObject);
											}
										}
										i++;
									}
									list.add(map);
								}
								j++;
							}
							ps = new PaginationSupport(list, getCount(conn, tablename + " t ", where), pageSize, start);
							return ps;
						} finally {
							if (rs != null)
								rs.close();
							if (statement != null)
								statement.close();
							
							if (StringUtil.isNotBlank(jndiName) && conn != null)
								conn.close();

						}

					}
				});
	}

	@Override
	public PaginationSupport queryPageBySQLForConvert(String sql,
			int pageIndex, int pageSize) {
		// TODO Auto-generated method stub
		return queryPageBySQLForConvert(sql, pageIndex, pageSize, null);
	}

	@SuppressWarnings("unchecked")
	public PaginationSupport queryPageBySQLForConvert(final String sql, final int pageIndex, final int pageSize, final String jndiName) {
		return (PaginationSupport) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				Statement statement = null;
				List list = new JSONArray();
				int start = (pageIndex - 1) * pageSize;
				PaginationSupport ps = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery("select * from("+sql+") where rownum<=" + (pageSize + start));
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					int j=0;
					Object anObject = null;
					while (rs.next()) {
						if (j >= start) { // 判读从多少行开始接收数据
							vo = new JSONObject();
							for (int i = 1; i <= columnCount; i++) {
								
								anObject = rs.getObject(i);
								if (anObject instanceof java.sql.Date) {
									vo.put(StringChangeToUpper(rsmd.getColumnName(i).toLowerCase()), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
								}else{
									vo.put(StringChangeToUpper(rsmd.getColumnName(i).toLowerCase()),rs.getObject(i));
								}
							}
							list.add(vo);
						}
						j++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				ps = new PaginationSupport(list, getCount(conn, "(" + sql + ") t ", "1=1"), pageSize, start);
				return ps;
			}
		});
	}
	
	@Override
	public String getContentClob(String clobColumn, String keyColumn,
			int keyValue, String table) {
		return getClobContent(clobColumn,keyColumn,keyValue,table,"");
	}

	@SuppressWarnings("unchecked")
	public String getClobContent(final String clobColumn, final String keyColumn, final int keyValue, final String table, final String jndiName) { 
		return  (String) super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "SELECT " + clobColumn + " FROM " + table + " WHERE " + keyColumn + "=?";
				Connection conn = getConnection(jndiName, session);
				PreparedStatement stmt = conn.prepareStatement(sql);
	            stmt.setInt(1, keyValue);
	            ResultSet rs = stmt.executeQuery();
	            String buffer="";
	            if(rs.next()) {
	                Clob clobtmp =  rs.getClob(1);
	                String clobStr = clobtmp == null ? null : clobtmp.getSubString(1L, (int)clobtmp.length());
					if(StringUtil.isBlank(clobStr)) {
	                    buffer = "";
	                } else {
	                    buffer = clobStr.replaceAll("\r\n", "").replaceAll("\t", "");
	                }
	            }
	            if (rs != null)
	            	rs.close();
	            
	            if (stmt != null)
	            	stmt.close();
	            if (StringUtil.isNotBlank(jndiName) && conn != null)
	            	conn.close();
	            return buffer;
			}}
		);
	}	
	
	/**
	 * 
	 * @Title: StringChangeToUpper 
	 * @Description: TODO将传入的字符串中的_a转换为A 如（old_account_type 转换为oldAccountType）
	 * @param @param str
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String StringChangeToUpper(String str){
		String returnStr ="";
		if(str.indexOf("_")>=0){
			int start =str.indexOf("_");
			int end = str.indexOf("_")+2;
			end = end >=str.length()?str.length():end;
			String aString = str.substring(start,end);
			start = start+1>=str.length()?str.length():start+1;
			String rStr = str.substring(start,end);
			returnStr = str.replaceAll(aString, rStr.toUpperCase());
			returnStr = StringChangeToUpper(returnStr);
		}else{
			returnStr = str;
		}
		return returnStr;
	}

	@Override
	public List queryListBySQLForConvert(String sql) {
		// TODO Auto-generated method stub
		return queryListBySQLRemoteForConvert(sql, null);
	}
	@SuppressWarnings("unchecked")
	public List queryListBySQLRemoteForConvert(final String sql, final String jndiName) {
		return (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				Statement statement = null;
				List list = new JSONArray();
				PaginationSupport ps = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					Map vo = null;
					int j=0;
					Object anObject = null;
					while (rs.next()) {
						vo = new JSONObject();
						for (int i = 1; i <= columnCount; i++) {
							anObject = rs.getObject(i);
							if (anObject instanceof java.sql.Date) {
								vo.put(StringChangeToUpper(rsmd.getColumnName(i).toLowerCase()), DateUtil.getDateTimeString(((java.sql.Date)anObject)));
							}else{
								vo.put(StringChangeToUpper(rsmd.getColumnName(i).toLowerCase()),rs.getObject(i));
							}
						}
						list.add(vo);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				return list;
			}
		});
	}

	@Override
	public List<String> querySqlColumns(final String sql,final String jndiName) {
		// TODO Auto-generated method stub
		
		return (List)super.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws SQLException {
				Connection conn = getConnection(jndiName, session);

				ResultSet rs = null;
				Statement statement = null;
				List<String> list = new ArrayList<String>();
				PaginationSupport ps = null;
				try {
					statement = conn.createStatement();
					rs = statement.executeQuery(sql);
					ResultSetMetaData rsmd = rs.getMetaData(); // 获得结果集的模型（列的相关信息）

					int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
					for(int i=1;i<=columnCount;i++){
						list.add(rsmd.getColumnName(i).toUpperCase());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();

					if (StringUtil.isNotBlank(jndiName) && conn != null)
						conn.close();
				}
				return list;
			}
		});
	}
}

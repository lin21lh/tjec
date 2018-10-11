package com.freework.queryData.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.QueryManager;
import com.freework.freedbm.util.TotalResult;
import com.freework.queryData.compileSQL.CompileSQL;
import com.freework.queryData.compileSQL.SqlAndParam;
import com.freework.queryData.servcie.QueryConfig;

public class QueryPageDao extends NamedParameterJdbcDaoSupport {
	private static final Log log=LogFactory.getLog(QueryPageDao.class); 

	protected QueryManager jdbcManager = QueryManager.getJdbcManager();
	@Resource()
	public void setSuperDataSource(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public PreparedStatement getPreparedStatement(Connection conn,SqlAndParam sqlParam, Object values,final int start, final int limit) throws SQLException {
		//
		
		String pageSql=Cfg.DB_TYPE.getLimitString(sqlParam.getSql(), start, limit);
		
		System.out.println(sqlParam.getSql());
		PreparedStatement preparedStatement =jdbcManager.getQueryPreparedStatement(pageSql,Cfg.DB_TYPE.isScrollInsensitive(), conn);
		List<Object>paramArray=sqlParam.getParam();
		if (paramArray.size()!=0) {
			Param params2[]=new Param[paramArray.size()];
			int i=0;
			for (Object value : paramArray) {
				params2[i]=Param.p.param(value);
				i++;
			}
			jdbcManager.setPreparedStatement(Arrays.asList(params2), preparedStatement);
		}


		return preparedStatement;

	}

	public TotalResult<Map> query(final Object value, final QueryConfig compileSQL,final int start, final int limit) {
		return this.query(value, compileSQL, Map.class,start,limit);
	}

	public <T> TotalResult<T> query(final Object value, final QueryConfig cfg,
			final Class<T> clazz,final int start, final int limit) {
		return super.getJdbcTemplate().execute(
				new ConnectionCallback<TotalResult<T>>() {
					public TotalResult<T> doInConnection(Connection conn)
							throws SQLException {
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						List<T> list = new ArrayList<T>(limit);
						TotalResult<T> tolalResult=new TotalResult<T>();
						try {
							CompileSQL compileSQL=cfg.getSql();
							SqlAndParam sqlParam = compileSQL.getSQL(value==null?new TreeMap():value);

							preparedStatement = getPreparedStatement(conn,sqlParam, value,start,limit);
							resultSet = preparedStatement.executeQuery();
							Cfg.DB_TYPE.setStartResultSet(resultSet, start);
							Object rdto = null;
							TableQuery newdto = cfg.getQuery(resultSet.getMetaData(), clazz);
							while (resultSet.next()) {
								rdto = newdto.newInstance();
								jdbcManager.setResultSetValue(Cfg.DB_TYPE.pagetResultStart(),newdto.getFields(), rdto, resultSet);
								list.add((T) rdto);
							}
							List<Param> params=Arrays.asList(Param.p.params(sqlParam.getParamArray()));
							tolalResult.setResults(jdbcManager.getCount(conn, sqlParam.getSql(),params));
							tolalResult.setItems(list);
						} catch (SQLException e) {
							throw e;
						} finally {
							jdbcManager.close(resultSet, preparedStatement);
						}
						return tolalResult;
					}
				});

	}
	
	public <T> TotalResult<T> queryForGeneral(final Object value, final QueryConfig cfg,
			final Class<T> clazz,final int start, final int limit) {
		return super.getJdbcTemplate().execute(
				new ConnectionCallback<TotalResult<T>>() {
					public TotalResult<T> doInConnection(Connection conn)
							throws SQLException {
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						List<T> list = new ArrayList<T>(limit);
						TotalResult<T> tolalResult=new TotalResult<T>();
						try {
							CompileSQL compileSQL=cfg.getSql();
							SqlAndParam sqlParam = compileSQL.getSQL(value==null?new TreeMap():value);

							preparedStatement = getPreparedStatement(conn,sqlParam, value,start,limit);
							resultSet = preparedStatement.executeQuery();
							Cfg.DB_TYPE.setStartResultSet(resultSet, start);
							Object rdto = null;
							TableQuery newdto = cfg.getQuery(resultSet.getMetaData(), clazz);
							while (resultSet.next()) {
								rdto = newdto.newInstance();
								jdbcManager.setResultSetValue(Cfg.DB_TYPE.pagetResultStart(),newdto.getFields(), rdto, resultSet);
								list.add((T) rdto);
							}
							List<Param> params=Arrays.asList(Param.p.params(sqlParam.getParamArray()));
//							tolalResult.setResults(jdbcManager.getCount(conn, sqlParam.getSql(),params));
							tolalResult.setItems(list);
						} catch (SQLException e) {
							throw e;
						} finally {
							jdbcManager.close(resultSet, preparedStatement);
						}
						return tolalResult;
					}
				});

	}

}

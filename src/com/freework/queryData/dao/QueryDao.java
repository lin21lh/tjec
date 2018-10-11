package com.freework.queryData.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.QueryManager;
import com.freework.queryData.compileSQL.CompileSQL;
import com.freework.queryData.compileSQL.SqlAndParam;
import com.freework.queryData.servcie.QueryConfig;

public class QueryDao extends NamedParameterJdbcDaoSupport {

	protected QueryManager jdbcManager = QueryManager.getJdbcManager();

	private static final Log log=LogFactory.getLog(QueryDao.class); 
	@Resource()
	public void setSuperDataSource(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public PreparedStatement getPreparedStatement(Connection conn,
			CompileSQL compileSQL, Object values) throws SQLException {
		SqlAndParam sqlParam = compileSQL.getSQL(values==null?new TreeMap():values);
		log.debug(sqlParam.getSql());
		System.out.println(sqlParam.getSql());
		PreparedStatement preparedStatement = jdbcManager.getQueryPreparedStatement(sqlParam.getSql(), false, conn);
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

	public List<Map> query(final Object value, final QueryConfig compileSQL) {
		
		
			return this.query(value, compileSQL, Map.class);
		
	}
	
	public <T> List<T> query(final Object value, final QueryConfig cfg,
			final Class<T> clazz) {
		
		
		return super.getJdbcTemplate().execute(
				new ConnectionCallback<List<T>>() {
					public List<T> doInConnection(Connection conn)
							throws SQLException {
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						List<T> list = new LinkedList<T>();
						try {
							CompileSQL compileSQL=cfg.getSql();
							preparedStatement = getPreparedStatement(conn,compileSQL, value);
							resultSet = preparedStatement.executeQuery();
							Object rdto = null;

							TableQuery newdto = cfg.getQuery(resultSet.getMetaData(), clazz);
							while (resultSet.next()) {
								rdto = newdto.newInstance();
								jdbcManager.setResultSetValue(1,newdto.getFields(), rdto, resultSet);
								list.add((T) rdto);
							}
						} catch (SQLException e) {
							throw e;
						} finally {
							jdbcManager.close(resultSet, preparedStatement);
						}
						return list;
					}
				});

	}

}

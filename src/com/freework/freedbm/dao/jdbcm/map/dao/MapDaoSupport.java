package com.freework.freedbm.dao.jdbcm.map.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.freework.freedbm.BaseDTO;
import com.freework.freedbm.bean.Criteria;
import com.freework.freedbm.bean.DBExecute;
import com.freework.freedbm.bean.Delete;
import com.freework.freedbm.bean.Insert;
import com.freework.freedbm.bean.Update;
import com.freework.freedbm.cfg.dbsqltype.BuilderSQL;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.DefaultProtoQueryDAO;
import com.freework.freedbm.dao.DefaultProtoUpdateDAO;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.QueryInfo;
import com.freework.freedbm.dao.jdbcm.QueryManager;
import com.freework.freedbm.dao.jdbcm.Param.p;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTO;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOCfg;
import com.freework.freedbm.dao.jdbcm.map.dto.MapDTOUtil;
import com.freework.freedbm.util.DTOByCfg;
import com.freework.freedbm.util.TotalResult;

@Service
public class MapDaoSupport extends NamedParameterJdbcDaoSupport  {

	@Resource()
	public void setSuperDataSource(DataSource dataSource) {
		this.setDataSource(dataSource);
		MapDTOUtil.setDaoSupport(this);
	}
	protected DefaultProtoQueryDAO qdao = DefaultProtoQueryDAO.getDefaultQueryDAO();

	protected DefaultProtoUpdateDAO udao = DefaultProtoUpdateDAO.getDefaultUpdateDAO();
	protected QueryManager jdbcManager=QueryManager.getJdbcManager();
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<Map> query(final String tableName,final  Criteria where) {
		return (List<Map>) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						MapDTOCfg tablecfg=	MapDTOUtil.getTableInfo(conn, tableName);
						QueryInfo qi = QueryInfo.getQueryInfo(tablecfg, where.sql(),where.getValues());
						return qdao.query(conn, qi);
					}
				});
	}
	public List<Map> querySQL(final String sql,final Object... paramsArgs) {
		return query(sql,p.params(paramsArgs));
	}
	
	public TotalResult<Map> queryPageSQL(Connection conn,final int start, final int limit,final String sql,final BuilderSQL dbType,final Object... paramsArgs) throws SQLException {

						
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						TotalResult<Map> tolalResult=new TotalResult<Map>();
						List<Map> list = new ArrayList<Map>(limit);
						try {
						String pageSql=dbType.getLimitString(sql, start, limit);
							preparedStatement =jdbcManager.getQueryPreparedStatement(pageSql,dbType.isScrollInsensitive(), conn);
							List<Param> params=null;
							if(paramsArgs.length!=0){
								params=Arrays.asList(p.params(paramsArgs));
									jdbcManager.setPreparedStatement(params,preparedStatement);
							}
							resultSet = preparedStatement.executeQuery();
							dbType.setStartResultSet(resultSet, start);
							Map rdto = null;
							TableQuery newdto=MapDTOUtil.getQueryInfo(resultSet.getMetaData(), pageSql);
							while (resultSet.next()) {
								rdto =  (Map) newdto.newInstance();
								jdbcManager.setResultSetValue(1,newdto.getFields(), rdto, resultSet);
								list.add(rdto);
							}
							tolalResult.setResults(jdbcManager.getCount(conn, sql, params));
							tolalResult.setItems(list);
						} catch (SQLException e) {
							throw e;
						} finally {
							jdbcManager.close(resultSet, preparedStatement);
						}
						return tolalResult;
					}
				

	public TotalResult<Map> queryPageSQL(final int start, final int limit,final String sql,final BuilderSQL dbType,final Object... paramsArgs) {
		return (TotalResult<Map>) super.getJdbcTemplate().execute(
				new ConnectionCallback<TotalResult<Map>>() {
					public TotalResult<Map> doInConnection(Connection conn)
							throws SQLException {
						try {
					
						return	queryPageSQL(conn, start, limit, sql, dbType, paramsArgs);
						} catch (SQLException e) {
							throw e;
						} 
					}
				});
	}

	public List<Map> query(final String sql,final Param... paramsArgs) {
		return (List<Map>) super.getJdbcTemplate().execute(
				new ConnectionCallback<List<Map>>() {
					public List<Map> doInConnection(Connection conn)
							throws SQLException {
						
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						List list = new ArrayList();
						try {
							preparedStatement =jdbcManager.getQueryPreparedStatement(sql,false, conn);
							if(paramsArgs.length!=0){
								List<Param> params=Arrays.asList(paramsArgs);
	
								if(params!=null)
									jdbcManager.setPreparedStatement(params,preparedStatement);
							}
							resultSet = preparedStatement.executeQuery();
							Object rdto = null;
							TableQuery newdto=MapDTOUtil.getQueryInfo(resultSet.getMetaData(), sql);
							while (resultSet.next()) {
								rdto =  newdto.newInstance();
								jdbcManager.setResultSetValue(1,newdto.getFields(), rdto, resultSet);
								list.add(rdto);
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
	
	
	
	
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<Map> queryPage(final int start, final int limit,final String tableName, final Criteria where) {
		return (List<Map>) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						MapDTOCfg tablecfg=	MapDTOUtil.getTableInfo(conn, tableName);
						QueryInfo qi = QueryInfo.getQueryInfo(tablecfg, where.sql(),where.getValues());
						qi.setStart(start);
						qi.setLimit(limit);
						return qdao.query(conn, qi);
					}
				});
	}
	
	public MapDTOCfg  getMapDTOCfg(String tableName){
		Connection conn=null;
		try {
			conn=getDataSource().getConnection();
			 MapDTOCfg tablecfg=MapDTOUtil.getTableInfo(conn, tableName);
			 return tablecfg;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			if(conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	public int insertByKey(final MapDTO dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						return udao.insertByKey(dto, conn);
					}
				});
	} 

	public int insertByKey(final Map map,final String tableName) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						BaseDTO dto=MapDTOUtil.mapToDto(conn, tableName,map);
						return udao.insertByKey(dto, conn);
					}
				});
	} 

	public int insert(final Map map,final String tableName) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						BaseDTO dto=MapDTOUtil.mapToDto(conn, tableName,map);
						return udao.insert(dto, conn);
					}
				});
	} 
	public int insert(final MapDTO dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						return udao.insert(dto, conn);
					}
				});
	} 
	public int updateAll(final MapDTO dto) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						return udao.updateAll(dto, conn);
					}
				});
	} 
	public int updateAll(final Map map,final String tableName) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						BaseDTO dto=MapDTOUtil.mapToDto(conn, tableName,map);

						return udao.updateAll(dto, conn);
					}
				});
	} 
	public int update(final Map map,final String tableName) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {
						BaseDTO dto=MapDTOUtil.mapToUpdateDto(conn, tableName,map);
						if(DTOByCfg.getUpdateField(dto,false).length!=0)
							return udao.update(dto, conn);
						else
							return 0;
					}
				});
	} 
	
	
	
	
	/**
	 * 根据sql语句进行修改
	 * @author SYL
	 * @param sql
	 * @param 字段值
	 * @return
	 */
	

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public int updateSql(final String sql,final Param... paramsArgs) {
		return (Integer) super.getJdbcTemplate().execute(
				new ConnectionCallback() {
					public Object doInConnection(Connection conn)
							throws SQLException {		
						int n = 0;
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						try {
							preparedStatement =jdbcManager. getQueryPreparedStatement(sql,false, conn);
							if(paramsArgs.length!=0){
								List<Param> params=Arrays.asList(paramsArgs);	
								if(params!=null)
									jdbcManager.setPreparedStatement(params,preparedStatement);
							}						
							n = preparedStatement.executeUpdate();					      
						} catch (SQLException e) {
							e.printStackTrace();
							throw e;
						} finally {
							jdbcManager.close(resultSet, preparedStatement);
						}
						return n;
					}
				});
	}
	 /**
		 * 根据sql语句进行修改
		 * @author SYL
		 * @param sql
		 * @param 字段值
		 * @return
		 */
	public Integer updateBySql(final String sql,final Object... paramsArgs) {
		return updateSql(sql,p.params(paramsArgs));
	}
	public  Update update(String tableName){
		Update update=new Update(tableName);
		update.setDao(this);
		return update;
	}
	public  Insert insert(String tableName){
		Insert insert=new Insert(tableName);
		insert.setDao(this);
		return insert;
	}
	
	public  Delete delete(String tableName){
		Delete delete=new Delete(tableName);
		delete.setDao(this);
		return delete;

	}

	
}

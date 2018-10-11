package com.freework.freedbm.dao;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.DTO;
import com.freework.freedbm.ProtoQueryDAO;
import com.freework.freedbm.bean.Criteria;
import com.freework.freedbm.bean.WhereResult;
import com.freework.freedbm.cfg.fieldcfg.FieldInfo;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.cfg.tablecfg.CreateObject;
import com.freework.freedbm.cfg.tablecfg.TableCfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
import com.freework.freedbm.dao.jdbcm.Param;
import com.freework.freedbm.dao.jdbcm.QueryInfo;
import com.freework.freedbm.dao.jdbcm.QueryManager;
import com.freework.freedbm.util.DTOByCfg;
import com.freework.freedbm.util.TotalResult;
import com.freework.base.util.FieldUtil;
import com.freework.base.util.SqlUtil;

public class DefaultProtoQueryDAO extends AbstractDAO implements
		ProtoQueryDAO {
	protected QueryManager jm = QueryManager.getJdbcManager();
	private final static DefaultProtoQueryDAO dfaultQueryDAO = new DefaultProtoQueryDAO();

	private DefaultProtoQueryDAO() {

	}

	public static DefaultProtoQueryDAO getDefaultQueryDAO() {
		return dfaultQueryDAO;
	}

	public  JdbcForDTO[] getJdbcForDTO(ResultSetMetaData rsmd,Class clazz) throws SQLException{
		int columnCount = rsmd.getColumnCount(); // 获得一共有多少类
		JdbcForDTO fieldArray[]=new JdbcForDTO[columnCount];
		for (int i = 1; i <= columnCount; i++) {
			String colName=rsmd.getColumnName(i);
			PropertyDescriptor p=FieldUtil.getPropertyDescriptor(clazz,colName);
			FieldInfo f=new FieldInfo(colName,colName, SQLTypeMap.getSQLType(p.getPropertyType()));
			f.setWriteMethod(p.getWriteMethod());
			fieldArray[i-1]=f;
		}
		 return fieldArray;
	}
	public <T> List<T> query(Connection con,String sql,Class<T> clazz,List<Object> params) throws SQLException{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> list = new LinkedList<T>();
		try {
			preparedStatement = jm.getQueryPreparedStatement(sql,false, con);
			if(params!=null){
				List<Param> params2=new ArrayList<Param>(params.size());
				for (Object obj : params) {
					params2.add(Param.p.param(obj));
				}
				jm.setPreparedStatement(params2,preparedStatement);
			}
			resultSet = preparedStatement.executeQuery();
			T rdto = null;
			JdbcForDTO[] dbjdbcForDTOs=getJdbcForDTO(resultSet.getMetaData(),clazz);
			while (resultSet.next()) {
				try {
					rdto = clazz.newInstance();
				} catch (InstantiationException e) {
					throw new SQLException(e);
				} catch (IllegalAccessException e) {
					throw new SQLException(e);

				}
				jm.setResultSetValue(1,dbjdbcForDTOs, rdto, resultSet);
				list.add(rdto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			jm.close(resultSet, preparedStatement);
		}
		return list;
		
	}
	
	public DTO getObject(Connection con, DTO key)
			throws SQLException {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(key);
		//JdbcForDTO[] ufieldInfos = tablecfg.getPKey();
		JdbcForDTO[] ufieldInfos = DTOByCfg.getUpdateField(key,false);

		WhereResult whereInfo= getWhere(ufieldInfos,key);
		QueryInfo qi = QueryInfo.getQueryInfo(tablecfg, whereInfo.getWhere(),whereInfo.getParams());

		return (DTO) jm.getDTOObject(con, qi);
	}

	public DTO getObject(Connection con, DTO key, JdbcForDTO[] e)
			throws SQLException {
		TableQuery tablecfg =  DTOByCfg.getTableDataManager(key);
		//JdbcForDTO[] ufieldInfos = tablecfg.getPKey();
		JdbcForDTO[] ufieldInfos = DTOByCfg.getUpdateField(key,false);

		WhereResult whereInfo= getWhere(ufieldInfos,key);
		String querySelect = Cfg.DB_TYPE.getQuerySql(tablecfg.getTableName(), e,whereInfo.getWhere());
		QueryInfo qi = QueryInfo.getQueryInfo(e,querySelect,tablecfg, whereInfo.getParams());
		return (DTO) jm.getDTOObject(con, qi);
	}

	

	
	public int getCount(Connection con,String sql,List<Param> ps) throws SQLException{
		QueryInfo count = new QueryInfo();
		count.setJdbcForDTOs(new JdbcForDTO[] { new JdbcForObject(Cfg.Integer) });
		count.setQuerySelect(SqlUtil.getCountSQL(sql));
		count.setParams(ps);
		return ((Integer) jm.getObject(con, count)).intValue();
	}
	
	public TotalResult<DTO> queryPage(Connection con, int start,
			int limit,TableQuery tablecfg,String where,List<Param> ps,String order) throws SQLException {
		TotalResult<DTO> tr = new TotalResult<DTO>();
		QueryInfo qi = QueryInfo.getQueryInfo(tablecfg,where,order,ps);
		qi.setJdbcForDTOs(tablecfg.getFields());
		qi.setStart(start);
		qi.setLimit(limit);
		tr.setItems(query(con, qi));
		tr.setResults(getCount(con,qi.getOldQuerySelect(),ps));
		return tr;
	}
	
	public TotalResult<DTO> queryPage(Connection con, int start,
			int limit,TableQuery tablecfg,String where,List<Param> ps, JdbcForDTO[] e,String order) throws SQLException {
		TotalResult<DTO> tr = new TotalResult<DTO>();
		String querySelect = Cfg.DB_TYPE.getQuerySql(tablecfg.getTableName(), e,where,order);
		QueryInfo qi = QueryInfo.getQueryInfo(e,querySelect,tablecfg, ps);
		qi.setStart(start);
		qi.setLimit(limit);
		tr.setItems(query(con, qi));
		tr.setResults(getCount(con,qi.getOldQuerySelect(),ps));

		return tr;
	}


	public List<DTO> query(Connection con, QueryInfo qi)
			throws SQLException {
		return jm.query(con, qi);
	}

	public DTO getDTOObject(Connection con, QueryInfo qi)
			throws SQLException {

		return (DTO) jm.getDTOObject(con, qi);
	}

	public Object getObject(Connection con, QueryInfo qi) throws SQLException {

		return jm.getObject(con, qi);
	}

	public Object getList(Connection con, QueryInfo qi) throws SQLException {

		return jm.getList(con, qi);
	}

	@Override
	public List<DTO> query(Connection con, TableQuery tablecfg, String where, List<Param> ps, JdbcForDTO[] e,String order)
			throws SQLException {
		String querySelect = Cfg.DB_TYPE.getQuerySql(tablecfg.getTableName(), e,where,order);
		QueryInfo qi = QueryInfo.getQueryInfo(e,querySelect,tablecfg, ps);
		return query(con, qi);
	}

	@Override
	public List<DTO> query(Connection con, TableQuery tablecfg, String where, List<Param> ps,String order) throws SQLException {
		QueryInfo qi = QueryInfo.getQueryInfo(tablecfg,where,order, ps);
		qi.setJdbcForDTOs(tablecfg.getFields());
		return query(con, qi);
	}

	


	
	
}

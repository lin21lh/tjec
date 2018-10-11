package com.freework.freedbm.dao.jdbcm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.Like;
import com.freework.freedbm.cfg.fieldcfg.type.LinkType;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.cfg.tablecfg.CreateObject;
import com.freework.freedbm.cfg.tablecfg.TableCfg;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.jdbcm.Param.p;
import com.freework.base.util.SqlUtil;

public class QueryManager  extends JdbcManager{
	
	
	private final static  QueryManager jdbcManager=new QueryManager();

	public static  QueryManager getJdbcManager(){
		return jdbcManager;
	}
	/**
	 * 根据对应关系从Result向dto里赋值
	 * @param JdbcForDTOs对应关系
	 * @param dto
	 * @param resultSet
	 * @throws SQLException
	 */
	
	public void setResultSetValue(int rsStart,JdbcForDTO[] JdbcForDTOs,Object dto,ResultSet resultSet) throws SQLException{
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
				SQLType type = ((SQLType) jdbcForDTO.getType());
				jdbcForDTO.setValue(dto, type.get(resultSet, rsStart));
				rsStart++;

		}
	}
	public void setResultSetValue(int rsStart,List<JdbcForDTO> JdbcForDTOs,Object dto,ResultSet resultSet) throws SQLException{
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
				SQLType type = ((SQLType) jdbcForDTO.getType());
				jdbcForDTO.setValue(dto, type.get(resultSet, rsStart));
				rsStart++;
		}
	}
	public void setLinkValue(List<JdbcForDTO> links,JdbcForDTO[] JdbcForDTOs,Object dto,Connection con) throws SQLException{
		if(links==null||dto==null)
			return ;
		for (JdbcForDTO link : links) {
				LinkType type = (LinkType) link.getType();
				Object value=type.isConnection()?type.getDTOValue(dto, JdbcForDTOs,con):type.getDTOValue(dto, JdbcForDTOs);
				link.setValue(dto, value);
		}
	}
	/***
	 * 
	 * @param q
	 * @return
	 * @throws SQLException 
	 */
	public Object getDTOObject(Connection con,QueryInfo q) throws SQLException{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Object rdto = q.getNewInstanceDTO().newInstance();
		try {
			
			System.out.println(q.getQuerySelect());
			preparedStatement = getQueryPreparedStatement(q.getQuerySelect(),false, con);
			List<Param> params=q.getParams();
			if(params!=null)
				setPreparedStatement(params,preparedStatement);
			resultSet = preparedStatement.executeQuery();
			List<JdbcForDTO> fields[]=getDBJdbcForDTOs(q.getJdbcForDTOs());
			if (resultSet.next()) {
				setResultSetValue(1,fields[0], rdto, resultSet);
				setLinkValue(fields[1], q.getJdbcForDTOs(), rdto,con);
			}else{
				return null;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			this.close(resultSet, preparedStatement);
		}
		return rdto;
	
		
	}	
	/***
	 * 
	 * @param q
	 * @return
	 * @throws SQLException 
	 */
	public   List getList(Connection con,QueryInfo q) throws SQLException{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List list=new ArrayList();
		try {

			preparedStatement = getQueryPreparedStatement(q.getQuerySelect(),false, con);
			List<Param> params=q.getParams();
			if(params!=null)
				setPreparedStatement(params,preparedStatement);
			resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				SQLType sql=(SQLType) q.getJdbcForDTOs()[0].getType();
				list.add( sql.get(resultSet, 1));;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			this.close(resultSet, preparedStatement);
		}
	
		return list;
	}	
	/***
	 * 
	 * @param q
	 * @return
	 * @throws SQLException 
	 */
	public Object getObject(Connection con,QueryInfo q) throws SQLException{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			

			preparedStatement = getQueryPreparedStatement(q.getQuerySelect(),false, con);
			List<Param> params=q.getParams();
			if(params!=null)
				setPreparedStatement(params,preparedStatement);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				SQLType sql=(SQLType) q.getJdbcForDTOs()[0].getType();
				return  sql.get(resultSet, 1);
			}else{
				return null;
			}
			
		} catch (SQLException e) {
			throw e;
		} finally {
			this.close(resultSet, preparedStatement);
		}
	
		
	}	
	
	
	

	public List<JdbcForDTO>[] getDBJdbcForDTOs(JdbcForDTO[] jdbcForDTOs){
		List<JdbcForDTO> dbList= new ArrayList<JdbcForDTO>(jdbcForDTOs.length);
		List<JdbcForDTO> linkList= new LinkedList<JdbcForDTO>();
		for (int i = 0; i < jdbcForDTOs.length; i++) {
			JdbcForDTO field=jdbcForDTOs[i];
			if(field.isDbCol()){
				dbList.add(field);
			}else if(field.getType() instanceof LinkType){
				linkList.add(field);
			}
		}
		return new List[]{dbList,linkList.size()==0?null:linkList};
	}
//	public List<JdbcForDTO> getLinkJdbcForDTOs(JdbcForDTO[] jdbcForDTOs){
//		
//		
//		List<JdbcForDTO> dbList= new LinkedList<JdbcForDTO>();
//
//		for (int i = 0; i < jdbcForDTOs.length; i++) {
//			JdbcForDTO field=jdbcForDTOs[i];
//			if(!field.isDbCol()&&field.getType() instanceof LinkType){
//				dbList.add(jdbcForDTOs[i]);
//			}
//		}
//		if(dbList.size()==0)
//			return null;
//		return dbList;
//	}
	
	
	public Integer getCount(Connection con,String sql,List<Param> params) throws SQLException{
		
		Integer count=0;
		PreparedStatement preparedStatementCount = null;
		ResultSet resultSetCount = null;
		try {
		
		preparedStatementCount =getQueryPreparedStatement(SqlUtil.getCountSQL(sql),false, con);
		if(params!=null)
				setPreparedStatement(params,preparedStatementCount);
		resultSetCount=preparedStatementCount.executeQuery();
		if(resultSetCount.next())
			count=resultSetCount.getInt(1);
	
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.close(resultSetCount, preparedStatementCount);
		}
		return count;
		
	}
	
	
	public List queryPage(Connection con,QueryInfo q)
			throws SQLException {
		List<Object> list=new LinkedList<Object>();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try {
		 stmt = getQueryPreparedStatement(q.getQuerySelect(),true, con);
		 List<Param> params=q.getParams();
			if(params!=null)
				setPreparedStatement(params,stmt);
		 rset = stmt.executeQuery();
		
		if (q.getStart() == 1 || q.getStart()  == 0)// 分页的结果集
			rset.beforeFirst();
		else
			rset.absolute(q.getStart());
		

		CreateObject newdto=q.getNewInstanceDTO();
		JdbcForDTO[] jdbcForDTOs= q.getJdbcForDTOs();
		Object rdto = null;
		List<JdbcForDTO> dbjdbcForDTOs[]=getDBJdbcForDTOs(jdbcForDTOs);
	
		for (int i = 0; rset.next() && i <q.limit; i++) {
			rdto = newdto.newInstance();
			setResultSetValue(1,dbjdbcForDTOs[0], rdto, rset);
			setLinkValue(dbjdbcForDTOs[1], jdbcForDTOs, rdto,con);
			list.add(rdto);
		}
		
		return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.close(rset, stmt);
		}
	}
	/**
	 * 
	 * @param q
	 * @return
	 * @throws SQLException 
	 */
	public List query(Connection con,QueryInfo q) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List list = new ArrayList(20);
		try {
			//System.out.println(q.getQuerySelect());
			System.out.println(q.getQuerySelect());

			preparedStatement = getQueryPreparedStatement(q.getQuerySelect(),q.isScrollInsensitive(), con);
			List<Param> params=q.getParams();
			if(params!=null)
				setPreparedStatement(params,preparedStatement);

			
			resultSet = preparedStatement.executeQuery();
			//if(q.isIspage())
			q.setStartResultSet(resultSet);
			
			Object rdto = null;
			CreateObject newdto=q.getNewInstanceDTO();
			JdbcForDTO[] jdbcForDTOs= q.getJdbcForDTOs();
			
			List<JdbcForDTO> dbjdbcForDTOs[]=getDBJdbcForDTOs(jdbcForDTOs);
			while (resultSet.next()) {
				rdto = newdto.newInstance();
				setResultSetValue(1,dbjdbcForDTOs[0], rdto, resultSet);
				setLinkValue(dbjdbcForDTOs[1], jdbcForDTOs, rdto,con);
				list.add(rdto);
	
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.close(resultSet, preparedStatement);
		}
		return list;
	}
	/**
	 * 
	 * @param dto
	 * @param JdbcForDTOs
	 * @param statement
	 * @throws SQLException
	 */
	public void setPreparedStatement(List<Param> list,PreparedStatement statement) throws SQLException {
		int i = 1;
		for (Param param : list) {
			Like like=param.getLike();
			if(like==null)
			param.getType().set(statement, param.getValue(), i);
			else{
	    		switch (like.getLocation()){
	    		case all:
	    			param.getType().set(statement, new StringBuilder().append(like.getWildcard()).append(param.getValue()).append(like.getWildcard()).toString(), i);
	    			break;
	    		case right:
	    			param.getType().set(statement, new StringBuilder().append(param.getValue()).append(like.getWildcard()).toString(), i);
	    			break;
	    		case left:
	    			param.getType().set(statement, new StringBuilder().append(like.getWildcard()).append(param.getValue()).toString(), i);
	    			break;
	    		
	    		}
			}
			i++;
		}
	}
	/**
	 * 关闭连接
	 * @param resultSet
	 * @param statement
	 * @param con
	 */
	public void close(ResultSet resultSet,Statement statement){
		  if (resultSet != null) {
              try {  resultSet.close(); }
              catch (SQLException e) { e.printStackTrace(); }
          }
          if (statement != null) {
              try {  statement.close(); }
              catch (SQLException e) { e.printStackTrace(); }
          }
         
	}
	
	
}

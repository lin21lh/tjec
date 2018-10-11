package com.freework.freedbm.dao.jdbcm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.Cfg;
import com.freework.freedbm.cfg.dbsqltype.BuilderSQL;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.base.util.NumberUtil;

/**
 * @author 程风岭
 * @category
 */
public class JdbcManager {

	private final static  JdbcManager jdbcManager=new JdbcManager();
//	private  JdbcManager(){
//		 
//	 }
	public static  JdbcManager getJdbcManager(){
		return jdbcManager;
	}
	
	/**
	 * 返回查询用的PreparedStatement
	 * @param sql
	 * @param scrollable 结果集是否可以滚动
	 * @param dbConn
	 * @return
	 * @throws SQLException
	 */
	 public   PreparedStatement getQueryPreparedStatement(
		        String sql,
		        boolean scrollable,
		         Connection  dbConn) throws SQLException {
		
			return getPreparedStatement(sql,scrollable,false,false,dbConn);

		}
	 /**
		 * 返回修改或添加用的PreparedStatement
		 * @param sql
		 * @param useGetGeneratedKeys 游标是否可以获得主键
		 * @param dbConn
		 * @return
		 * @throws SQLException
		 */
	 public   PreparedStatement getUpdatePreparedStatement(
		        String sql,
		        boolean useGetGeneratedKeys,
		         Connection  dbConn) throws SQLException {
			return getPreparedStatement(sql,false,useGetGeneratedKeys,false,dbConn);

		}
	 /**
		 * 返回PreparedStatement
		 * @param sql
		 * @param useGetGeneratedKeys 游标是否可以获得主键
		 * @param scrollable 结果集是否可以滚动
		 * @param dbConn
		 * @return
		 * @throws SQLException
		 */

	public  PreparedStatement getPreparedStatement(
		        String sql,
		        boolean scrollable,
		         boolean useGetGeneratedKeys,
		         boolean callable,
		         Connection  dbConn) throws SQLException {
			PreparedStatement result;
			if ( scrollable ) {
				if ( callable ) {
					result = dbConn.prepareCall( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				}
				else {
					result = dbConn.prepareStatement( sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				}
			}
			else if ( useGetGeneratedKeys ) {
				result = dbConn.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS );
			}
			
			else {
				if ( callable ) {
					result = dbConn.prepareCall( sql );
				}
				else {
					result = dbConn.prepareStatement( sql );
				}
			}

		
			return result;

		}
	

	/**
	 * 
	 * @param dto
	 * @param JdbcForDTOs
	 * @param statement
	 * @throws SQLException
	 */
	protected void setPreparedStatement(Object dto, JdbcForDTO[] JdbcForDTOs,
			PreparedStatement statement) throws SQLException {
		int i = 1;
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
			if (jdbcForDTO.isDbCol()) {
				Object value = jdbcForDTO.getValue(dto);
				((SQLType) jdbcForDTO.getType()).set(statement, value, i);
				i++;
			}
		}
	}
	protected void setPreparedStatementInsert(Object dto, JdbcForDTO[] JdbcForDTOs,
			PreparedStatement statement) throws SQLException {
		int i = 1;
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
			if (jdbcForDTO.isDbCol()) {
				Object value = jdbcForDTO.getValue(dto);
				((SQLType) jdbcForDTO.getType()).set(statement, value!=null?value:jdbcForDTO.getDefVal(), i);
				i++;
			}
		}
	}
	/**
	 * 
	 * @param dto
	 * @param JdbcForDTOs
	 * @param statement
	 * @param iskey
	 * @throws SQLException
	 */
	public void setPreparedStatementNotKeyInsert(Object dto,
			JdbcForDTO[] JdbcForDTOs, PreparedStatement statement)
			throws SQLException {
		int i = 1;
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
			if (jdbcForDTO.isDbCol()) {
				if (!jdbcForDTO.isKey()) {
					Object value = jdbcForDTO.getValue(dto);
					((SQLType) jdbcForDTO.getType()).set(statement, value!=null?value:jdbcForDTO.getDefVal(), i);
					i++;
				} 
			}
		}
	}
	/**
	 * 
	 * @param dto
	 * @param JdbcForDTOs
	 * @param statement
	 * @param iskey
	 * @throws SQLException
	 */
	public void setPreparedStatementLastKey(Object dto,JdbcForDTO[] JdbcForDTOs, PreparedStatement statement)
			throws SQLException {
		int i = 1;
		List<JdbcForDTO> key =new  ArrayList<JdbcForDTO>(1);
		for (JdbcForDTO jdbcForDTO : JdbcForDTOs) {
			if (jdbcForDTO.isDbCol()) {
				if (!jdbcForDTO.isKey()) {
					Object value = jdbcForDTO.getValue(dto);
					((SQLType) jdbcForDTO.getType()).set(statement, value, i);
					i++;
				}else{
					key.add(jdbcForDTO);
				}
			}
		}
		for (JdbcForDTO jdbcForDTO : key) {
			((SQLType) jdbcForDTO.getType()).set(statement, jdbcForDTO.getValue(dto), i);
			i++;
		}

	}
	public void setPreparedStatement(SQLType[] types, Object[] values,
			PreparedStatement statement) throws SQLException {
		int i = 1;
		for (int j = 0; j < values.length; j++) {
			types[j].set(statement, values[j], i);
			i++;
		}
	}
	public void setPreparedStatement(SQLType type, Object[] values,
			PreparedStatement statement) throws SQLException {
		for (int j = 0; j < values.length; j++) {
			type.set(statement, values[j], j+1);
		}
	}
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection con, String sql, Object[] values,
			SQLType[] types) throws SQLException {

		PreparedStatement statement = null;
		try {
			statement = getUpdatePreparedStatement(sql, false, con);
			
			System.out.println(sql);
			this.setPreparedStatement(types, values, statement);
			return statement.executeUpdate();
		} catch (Exception e) {
			throw new SQLException(e.toString());
		} finally {
			if (statement != null)
				statement.close();
		}

	}
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection con, String sql, Object[] values,
			SQLType types) throws SQLException {

		PreparedStatement statement = null;
		try {
			statement = getUpdatePreparedStatement(sql, false, con);
			this.setPreparedStatement(types, values, statement);
			return statement.executeUpdate();
		} catch (Exception e) {
			throw new SQLException(e.toString());
		} finally {
			if (statement != null)
				statement.close();
		}

	}
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int updateBatch(Connection con, String sql,
			List<Object[]> values, SQLType[] types) throws SQLException {

		PreparedStatement statement = null;
		try {
			statement = getUpdatePreparedStatement(sql, false, con);
			for (Object[] value : values) {
				this.setPreparedStatement(types, value, statement);
				statement.addBatch();
			}

			return statement.executeUpdate();
		} catch (Exception e) {
			throw new SQLException(e.toString());
		} finally {
			if (statement != null)
				statement.close();
		}

	}
	protected void setKey(PreparedStatement statement,String tablename, JdbcForDTO key,Object dto)
	throws SQLException {
		Object id = Cfg.DB_TYPE.findId((SQLType)key.getType(),tablename, statement);
		key.setValue(dto, id);
	}
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int insertBatchByKey(Connection con,List dtos, JdbcForDTO[] JdbcForDTOs,
			 String  sql,String tablename) throws SQLException {
		PreparedStatement statement=null;
		
		try {
			 statement = getUpdatePreparedStatement(sql,false, con);
				for (Object dto : dtos) {
					setPreparedStatementInsert(dto, JdbcForDTOs, statement);
					statement.addBatch();
				}
				
				int ri[] = statement.executeBatch();
				
				return NumberUtil.sumIntArray(ri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if (statement != null){
				
				statement.clearParameters();
				statement.clearBatch();
				statement.close();
			}
		}
		

	}
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int insertBatch(Connection con,List dtos,JdbcForDTO key, JdbcForDTO[] JdbcForDTOs,
			 String  sql,String tablename) throws SQLException {
		BuilderSQL bq = Cfg.DB_TYPE;
		boolean beforeExecutionFindId = bq.beforeExecutionFindId();
		boolean useGeneratedKeys = bq.useGeneratedKeys();
		PreparedStatement statement=null;
		
		try {
			 statement = getUpdatePreparedStatement(sql,useGeneratedKeys, con);
				
			 if (beforeExecutionFindId){
				 	for (Object dto : dtos) {
					 	setKey(statement,tablename, key,dto);
					 	setPreparedStatementInsert(dto, JdbcForDTOs, statement);
						statement.addBatch();
					}
			}else { 
					for (Object dto : dtos) {
						setPreparedStatementNotKeyInsert(dto, JdbcForDTOs, statement);
						statement.addBatch();
					}
			}
				int ri[] = statement.executeBatch();
				if(!beforeExecutionFindId){
				List list=bq.findIds((SQLType)key.getType(), tablename, statement, dtos.size());
					for (int i = 0; i < list.size(); i++) {
						key.setValue(dtos.get(i), list.get(i));
					}
				}
				

				return NumberUtil.sumIntArray(ri);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if (statement != null)
				statement.close();
		}
		

	}
	
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int insert(Connection con,Object dto,JdbcForDTO key, JdbcForDTO[] JdbcForDTOs,
			 String  sql,String tablename) throws SQLException {
		BuilderSQL bq = Cfg.DB_TYPE;
		boolean beforeExecutionFindId = bq.beforeExecutionFindId();
		PreparedStatement statement=null;
		
		try {
				statement = getUpdatePreparedStatement(sql,bq.useGeneratedKeys(), con);
				if (beforeExecutionFindId) setKey(statement,tablename, key,dto);
				if(beforeExecutionFindId)
					setPreparedStatement(dto, JdbcForDTOs, statement);
				else
					setPreparedStatementNotKeyInsert(dto, JdbcForDTOs, statement);
					 	
				int ri = statement.executeUpdate();
				if (ri > 0 && !beforeExecutionFindId) setKey(statement,tablename,key, dto);
				return ri;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		} finally {
			if (statement != null){
				statement.clearWarnings();
				statement.clearParameters();
				statement.clearBatch();
				statement.close();
				
			}
		}
		

	}
	/**
	 * 
	 * @param dto
	 * @param con
	 * @param sql
	 * @param JdbcForDTOs
	 * @param keyJdbcForDTOs
	 * @return
	 * @throws SQLException
	 */
	public int update(Object dto, JdbcForDTO[] JdbcForDTOs, PreparedStatement statement,boolean isLastkey) throws SQLException {
		Class dtoClass = dto.getClass();

		if (isLastkey)
			setPreparedStatementLastKey(dto, JdbcForDTOs, statement);
		else
			setPreparedStatement(dto, JdbcForDTOs, statement);
		return statement.executeUpdate();

	}
	/**
	 * 
	 * @param dto
	 * @param sql
	 * @param jdbcForDTOs
	 * @param con
	 * @param iskeyLast
	 * @return
	 * @throws SQLException
	 */
	public int update(Object dto,  String sql,JdbcForDTO[] jdbcForDTOs,Connection con, boolean iskeyLast) throws SQLException {
		PreparedStatement statement = null;
		try {
			System.out.println(sql);
			statement = getUpdatePreparedStatement(sql, false, con);
			return update(dto, jdbcForDTOs,  statement,iskeyLast);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.toString());
		} finally {
			if (statement != null)
				statement.close();
		}
	}
	/**
	 * 
	 * @param dto
	 * @param sql
	 * @param jdbcForDTOs
	 * @param con
	 * @param iskeyLast
	 * @return
	 * @throws SQLException
	 */
	public int insert(Object dto,  String sql,JdbcForDTO[] jdbcForDTOs,Connection con, boolean iskeyLast) throws SQLException {
		PreparedStatement statement = null;
		try {
			System.out.println(sql);
			statement = getUpdatePreparedStatement(sql, false, con);
			setPreparedStatementInsert(dto, jdbcForDTOs, statement);

			return statement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(e.toString());
		} finally {
			if (statement != null)
				statement.close();
		}
	}
	
	
	/**
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int update(Connection conn,String sql) throws SQLException{
		 Statement statement = null;
		try {
			statement =conn.createStatement();
			return statement.executeUpdate(sql);
		} catch (SQLException e) {
				throw e;
		} finally {
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	
		}
	 }
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class BatchUpdate{
		PreparedStatement preparedStatement=null;
		SQLType[] paramtypes=null;
		 public BatchUpdate(boolean callable,String sql,Connection conn,SQLType ...paramtypes) throws SQLException{
			this.preparedStatement=	JdbcManager.this.getPreparedStatement(sql, false, false, callable, conn);
			this.paramtypes=paramtypes;
		}
		
		public void addParam(Object ...param) throws SQLException{
			JdbcManager.this.setPreparedStatement(paramtypes, param, preparedStatement);
			preparedStatement.addBatch();
		}
		
		public int[]  executeBatch() throws SQLException{
			
			return preparedStatement.executeBatch();

		}
		
	}
	

}

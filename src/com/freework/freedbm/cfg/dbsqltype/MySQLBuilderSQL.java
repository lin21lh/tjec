package com.freework.freedbm.cfg.dbsqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ³Ì·çÁë
 * @category
 */
public class MySQLBuilderSQL extends AbstractBuilderSQL {

	
	
	public String getInsertSql(String tableName, JdbcForDTO[] columns) {
		return this.getInsertSql(tableName, columns, false);
	}

	
	public boolean useGeneratedKeys(){
		return true;
	}

	
	
	public String getLimitString(String sql, int start, int limit) {
		 boolean hasOffset=start > 0;
		return new StringBuffer( sql.length() + 20 )
				.append( sql )
				.append( hasOffset ? " limit "+start+", "+limit : " limit "+limit )
				.toString();
	}


	


	public Object findId(SQLType sqltype,String tablename, PreparedStatement pstmt)
			throws SQLException {
		
		
     //   String SELECT = "select insert_id() as id from " +tablename + " limit 1";
		  ResultSet  rs = pstmt.getGeneratedKeys();
	        if(rs==null)
	        	return null;
		 Object value=null;
			if(rs.next())
				value=sqltype.get(rs, 1);
		       rs.close();
			 
			return value;
	}






	public List findIds(SQLType sqltype,String tablename, PreparedStatement pstmt,int size)
			throws SQLException {
		if(size<=0)
			return null;
	
		// String SELECT = "select last_insert_id() as id from " +tablename ;

	        ResultSet  rs = pstmt.getGeneratedKeys();
	        if(rs==null)
	        	return null;
			 Object value=null;
			 List list=new ArrayList();
				while(rs.next()){
					value=sqltype.get(rs, 1);
					list.add(value);
				}
			       rs.close();
				 
				return list;
	}






	public String dbname() {
		// TODO Auto-generated method stub
		return "mysql";
	}
	

}

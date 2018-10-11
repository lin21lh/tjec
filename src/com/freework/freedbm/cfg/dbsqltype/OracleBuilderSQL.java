package com.freework.freedbm.cfg.dbsqltype;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class OracleBuilderSQL extends AbstractBuilderSQL {

	
	public boolean useGeneratedKeyNames(){
		return true;
	}
	public  String getInsertSql(String tableName, JdbcForDTO[] columns) {
		StringBuffer sql1 = new StringBuffer("insert into ").append(tableName)
				.append(" (");
		StringBuffer sql2 = new StringBuffer("  values ( ");
		String string = null;
	boolean isStartAppend=false;
	for (int j = 0; j < columns.length; j++) {

			if(columns[j].isDbCol()){
				string = columns[j].getColName();
				
				if (isStartAppend) {
					sql1.append(",");
					sql2.append(",");
				} else {
					isStartAppend=true;
				}
				sql1.append(string);
				sql2.append("?");
			}

		}
		sql1.append(")");
		sql2.append(")");
		return (sql1.append(sql2)).toString();
	}
	
	public String getLimitString(String sql, int start, int limit) {
		sql = sql.trim();
		boolean isForUpdate = false;
		
		boolean hasOffset=start > 0;
		if ( sql.toLowerCase().endsWith(" for update") ) {
			sql = sql.substring( 0, sql.length()-11 );
			isForUpdate = true;
		}

		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 );
		if (hasOffset) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		
		pagingSelect.append(sql);
		if (hasOffset) {
			pagingSelect.append(" ) row_ where rownum <= ").append(limit + start ).append(") where rownum_ > ").append(start);
		}
		else {
			pagingSelect.append(" ) where rownum <= " ).append(limit + start);
		}

		if ( isForUpdate ) {
			pagingSelect.append( " for update" );
		}

		return pagingSelect.toString();
	}






	public Object findId(SQLType sqltype,String tablename, PreparedStatement pre)
			throws SQLException {
//	
//			ResultSet rs =   pre.getGeneratedKeys(); 
//	        if(rs==null)
//	        	return null;
//	        Object value=null;
//			if(rs.next())
//				value=sqltype.get(rs, 1);
//		       rs.close();
//		       return null;
		//long starttime=System.currentTimeMillis();
		 String sql="SELECT s_" + tablename + ".nextval FROM dual";
		 Connection con=null;
		 Statement statement=null;
		 ResultSet rs =null;
		 try{
		
		 con= pre.getConnection();

		 statement= con.createStatement();
		 rs = statement.executeQuery(sql);
		 Object value=null;
		 
		if(rs.next())
			value=sqltype.get(rs, 1);
		return value;
		

		 }catch(SQLException e){
	
			 throw e;
			
		 }finally{
			 if(rs!=null)
				 try{rs.close();}catch(SQLException e1){};
			 if(statement!=null)
				 try{statement.close();}catch(SQLException e1){};

		 }
			
	      
	}


	public List findIds(SQLType sqltype,String tablename, PreparedStatement pstmt,int size)
			throws SQLException {
		if(size<=0)
			return null;
		
		List list=new ArrayList(size);
		for (int i = 0; i < size; i++) {
			list.add(this.findId(sqltype, tablename, pstmt));

		}
		return list;
	}




	public String dbname() {
		// TODO Auto-generated method stub
		return "oracle";
	}
	
	public boolean beforeExecutionFindId() {
		// TODO Auto-generated method stub
		return true;
	}
	

}

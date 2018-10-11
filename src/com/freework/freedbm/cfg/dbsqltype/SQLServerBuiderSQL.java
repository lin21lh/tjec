package com.freework.freedbm.cfg.dbsqltype;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public class SQLServerBuiderSQL extends AbstractBuilderSQL{
	

	public String getInsertSql(String tableName, JdbcForDTO[] columns) {
		return this.getInsertSql(tableName, columns, false);
	}

	
	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}
	public String getLimitString(String querySelect,int start, int limit) {

		return new StringBuffer( querySelect.length() + 8 )
				.append( querySelect )
				.insert( getAfterSelectInsertPoint( querySelect ), " top " + limit )
				.toString();
	}

	public boolean isScrollInsensitive(){
		return true;
	}
	public void setStartResultSet(ResultSet rset, int start) throws SQLException {
		if (start == 0 || start == 1)
			rset.beforeFirst();
		else
			rset.absolute(start);
	}



	public Object findId(SQLType sqltype,String tablename, PreparedStatement pre) throws SQLException {
		ResultSet rs=pre.getGeneratedKeys();

		 Object value=null;
		 
			while(rs.next())
				value=sqltype.get(rs, 1);
		     rs.close();
			return value;
	}
	public boolean useGeneratedKeys(){
		return true;
	}

	
	
	public List findIds(SQLType sqltype,String tablename, PreparedStatement pstmt,int size)
			throws SQLException {
		if(size<=0)
			return null;
		ResultSet rs=pstmt.getGeneratedKeys();

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
		return "mssql";
	}



}

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
public class DB2BuiderSQL extends AbstractBuilderSQL{
	

	public String getInsertSql(String tableName, JdbcForDTO[] columns) {
		return this.getInsertSql(tableName, columns, false);
	}


	private String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50)
			.append("rownumber() over(");

		int orderByIndex = sql.toLowerCase().indexOf("order by");

		if ( orderByIndex>0 && !hasDistinct(sql) ) {
			rownumber.append( sql.substring(orderByIndex) );
		}

		rownumber.append(") as rownumber_,");

		return rownumber.toString();
	}
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}

	public String getLimitString(String querySelect,int start, int limit) {

		int startOfSelect = querySelect.toLowerCase().indexOf("select");
		boolean hasOffset=start > 0;
		StringBuffer pagingSelect = new StringBuffer( querySelect.length()+100 )
				.append( querySelect.substring(0, startOfSelect) )	// add the comment
				.append("select * from ( select ") 			// nest the main query in an outer select
				.append( getRowNumber(querySelect) ); 				// add the rownnumber bit into the outer query select list

		if ( hasDistinct(querySelect) ) {
			pagingSelect.append(" row_.* from ( ")			// add another (inner) nested select
					.append( querySelect.substring(startOfSelect) ) // add the main query
					.append(" ) as row_"); 					// close off the inner nested select
		}
		else {
			pagingSelect.append( querySelect.substring( startOfSelect + 6 ) ); // add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");

		//add the restriction to the outer select
		if (hasOffset) {
			pagingSelect.append("between ").append(start+1).append(" and ").append(limit + start);
		}
		else {
			pagingSelect.append("<= ").append(limit + start);
		}

		return pagingSelect.toString();
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
		return "db2";
	}

	public int pagetResultStart(){
		return 2;
	}

}

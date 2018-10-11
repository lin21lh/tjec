package com.freework.freedbm.cfg.dbsqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public interface  BuilderSQL {
	public boolean useGeneratedKeyNames();
	public String dbname();
	
	public boolean isScrollInsensitive();

	public int pagetResultStart();
	public Object findId(SQLType sqltype,String tablename,PreparedStatement pre)throws SQLException;
	public List findIds(SQLType sqltype,String tablename,PreparedStatement pstmt,int size)throws SQLException;
	public String getQuerySql(String tablename, JdbcForDTO[] columns,String where);
	public String getQuerySql(String tablename, JdbcForDTO[] columns,String where,String order);
	public String getQuerySql(String tablename, FieldInfoEnum[] columns,String where,String order);
	public String getLimitString(String querySelect,int start, int limit);
	public void setStartResultSet(ResultSet rset, int start) throws SQLException;
	
	
	public String getUpdateSql(String tableName, JdbcForDTO[] columns);

	
	
	public String getInsertSql(String tableName, JdbcForDTO[] columns);

	public String getInsertSqlKey(String tableName, JdbcForDTO[] columns);
	public boolean useGeneratedKeys();
	public boolean beforeExecutionFindId();
}


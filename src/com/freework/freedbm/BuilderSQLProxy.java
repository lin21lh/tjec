package com.freework.freedbm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import com.freework.freedbm.cfg.dbsqltype.BuilderSQL;
import com.freework.freedbm.cfg.dbsqltype.OracleBuilderSQL;
import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.cfg.fieldcfg.type.SQLType;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
public class BuilderSQLProxy    implements BuilderSQL {
	
	private final static ThreadLocal<BuilderSQL> threadLocal = new ThreadLocal<BuilderSQL>();

	public static void dbTyle(BuilderSQL type){
		threadLocal.set(type);
	}
	public static void clearDbTyle(){
		threadLocal.remove();
	}
	public BuilderSQL proxy(){
		BuilderSQL builderSQL=threadLocal.get();
		return builderSQL==null?proxyDefault:builderSQL;
		
	}
	private BuilderSQL proxyDefault=new OracleBuilderSQL();
	public void setProxyDefault(BuilderSQL proxy) {
		this.proxyDefault = proxy;
	}
	
	
	public boolean useGeneratedKeyNames() {
		return proxy().useGeneratedKeyNames();
	}
	public String dbname() {
		return proxy().dbname();
	}

	public boolean isScrollInsensitive() {
		return proxy().isScrollInsensitive();
	}

	public int pagetResultStart() {
		return proxy().pagetResultStart();
	}

	public Object findId(SQLType sqltype, String tablename,
			PreparedStatement pre) throws SQLException {
		return proxy().findId(sqltype, tablename, pre);
	}

	public List findIds(SQLType sqltype, String tablename,
			PreparedStatement pstmt, int size) throws SQLException {
		return proxy().findIds(sqltype, tablename, pstmt, size);
	}

	public String getQuerySql(String tablename, JdbcForDTO[] columns,
			String where  ,String order) {
		return proxy().getQuerySql(tablename, columns, where,order);
	}

	public String getQuerySql(String tablename, FieldInfoEnum[] columns,
			String where,String order) {
		return proxy().getQuerySql(tablename, columns, where,order);
	}

	public String getLimitString(String querySelect, int start, int limit) {
		return proxy().getLimitString(querySelect, start, limit);
	}

	public void setStartResultSet(ResultSet rset, int start)
			throws SQLException {
		proxy().setStartResultSet(rset, start);
	}

	public String getUpdateSql(String tableName, JdbcForDTO[] columns) {
		return proxy().getUpdateSql(tableName, columns);
	}

	public String getInsertSql(String tableName, JdbcForDTO[] columns) {
		return proxy().getInsertSql(tableName, columns);
	}

	public String getInsertSqlKey(String tableName, JdbcForDTO[] columns) {
		return proxy().getInsertSqlKey(tableName, columns);
	}

	public boolean useGeneratedKeys() {
		return proxy().useGeneratedKeys();
	}

	public boolean beforeExecutionFindId() {
		return proxy().beforeExecutionFindId();
	}
	@Override
	public String getQuerySql(String tablename, JdbcForDTO[] columns,
			String where) {
		// TODO Auto-generated method stub
		return proxy().getQuerySql(tablename, columns, where);
	}


}

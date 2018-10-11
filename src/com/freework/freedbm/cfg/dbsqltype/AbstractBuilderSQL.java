package com.freework.freedbm.cfg.dbsqltype;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;
/**
 * @author 程风岭
 * @category
 */
public abstract class AbstractBuilderSQL implements BuilderSQL {
	
	
	/**
	 * 生成返回修改的sql语句
	 * @param tablename 表名
	 * @param columns 列名
	 * @return
	 */
	public  String getUpdateSql(String tableName, JdbcForDTO[] columns) {
		StringBuffer sql = new StringBuffer("update  ").append(tableName)
				.append(" set ");
		StringBuilder where = new StringBuilder();
		boolean isStartAppend = false;
		boolean isWhereAnd = false;
		for (int i = 0; i < columns.length; i++) {
			JdbcForDTO info = columns[i];
			if(info.isDbCol()){
				if (info.isKey()){
					where.append(isWhereAnd?" and ":" ").append(info.getColName()).append("=?");
					isWhereAnd=true;
				}else  {
					if (isStartAppend)
						sql.append(",");
					else
						isStartAppend = true;
					sql.append(" ").append(info.getColName()).append("=?");
				
				}
			}

		}

		return sql.append(" where ").append(where).toString();
	}
	
	
	/**
	 * 生成返回包括主键id的添加sql语句 
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public  String getInsertSqlKey(String tableName, JdbcForDTO[] columns) {
		return this.getInsertSql(tableName, columns, true);
	}
	/**
	 * 生成返回添加的sql语句 
	 * @param tableName
	 * @param columns
	 * @param addkey 是否包括主键id
	 * @return
	 */
	public  String getInsertSql(String tableName, JdbcForDTO[] columns,boolean addkey) {
		StringBuffer sql1 = new StringBuffer("insert into ").append(tableName)
				.append(" (");
		StringBuffer sql2 = new StringBuffer("  values ( ");
		String string = null;
	boolean isStartAppend=false;
	for (int j = 0; j < columns.length; j++) {

			if(columns[j].isDbCol()&&(addkey||!columns[j].isKey())){
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
	
	/**
	 * 生成返回查询的sql语句
	 * @param tablename 表名
	 * @param columns 列名
	 * @param where 过滤条件
	 * @return
	 */
	public  String getQuerySql(String tablename, FieldInfoEnum[] columns,
			String where,String order) {
		String column;
		StringBuffer sql = new StringBuffer(100);
		sql.append("select ");
		boolean isStartAppend=false;

		for (int i = 0; i < columns.length; i++) {
			column = columns[i].getFieldInfo().getColName();
			if(columns[i].getFieldInfo().isDbCol()){
				if (isStartAppend) {
					sql.append(",");
				} else {
					isStartAppend=true;
				}
				
				sql.append(column);
			}
		}
		sql.append(" from ").append(tablename).append("  t ");
		if (where != null && !where.equals(""))
			sql.append(" where ").append(where);
		if (order != null && !order.equals(""))
			sql.append(" ").append(order);
		return sql.toString();

	}
	/**
	 * 生成返回查询的sql语句
	 * @param tablename 表名
	 * @param columns 列名
	 * @param where 过滤条件
	 * @return
	 */
	public  String getQuerySql(String tablename, JdbcForDTO[] columns,String where){
		return this.getQuerySql(tablename, columns, where, null);
	}
	/**
	 * 生成返回查询的sql语句
	 * @param tablename 表名
	 * @param columns 列名
	 * @param where 过滤条件
	 * @return
	 */
	public  String getQuerySql(String tablename, JdbcForDTO[] columns,String where,String order) {
		String column;
		StringBuffer sql = new StringBuffer(100);
		sql.append("select ");
		boolean isStartAppend=false;

		for (int i = 0; i < columns.length; i++) {
			column = columns[i].getColName();
			if(columns[i].isDbCol()){
				if (isStartAppend) {
					sql.append(",");
				} else {
					isStartAppend=true;
				}
				
				sql.append(column);
			}
		}
		sql.append(" from ").append(tablename).append("  t ");

		if (where != null && !where.equals(""))
			sql.append(" where ").append(where);
		if (order != null && !order.equals(""))
			sql.append(" ").append(order);
		return sql.toString();

	}
	
	public boolean useGeneratedKeyNames(){
		return false;
	}
	
	
	public boolean isScrollInsensitive(){
		return false;
	}
	
	public void setStartResultSet(ResultSet rset, int start) throws SQLException{
		
	}
	public boolean beforeExecutionFindId() {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean useGeneratedKeys(){
		return false;
	}
	public int pagetResultStart(){
		return 1;
	}
}

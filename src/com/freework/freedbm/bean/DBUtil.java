package com.freework.freedbm.bean;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import com.freework.freedbm.cfg.fieldcfg.type.SQLTypeMap;
import com.freework.freedbm.dao.jdbcm.Param;

public abstract class DBUtil {
	
	
	public static Connection connection(String driver,String url,String user,String password){
		try {
			Class.forName(driver);
			return DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	
	public static int execute(Connection conn,DBExecute execute) throws SQLException{
		PreparedStatement preparedStatement = null;
		try {
			String sql=execute.sql();
			Collection<? extends Param> values=execute.getValues();
			preparedStatement = conn.prepareStatement(sql);
			int index=1;
			for (Param value : values) {
				value.getType().set(preparedStatement, value.getValue(), index);
				index++;
			}
		return	preparedStatement.executeUpdate();
		}finally{
			close(preparedStatement);
		}
	}
	public static int execute(Connection conn,String sql , Collection<? extends Param> values) throws SQLException{
		System.out.println(sql);
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(sql);
			int index=1;
			for (Param value : values) {
				value.getType().set(preparedStatement, value.getValue(), index);
				index++;
			}
		return	preparedStatement.executeUpdate();
		}finally{
			close(preparedStatement);
		}
	}
	public static void execute(Connection conn,String sql,Object... params) throws SQLException{
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = conn.prepareStatement(sql);
			set(preparedStatement, params);
			preparedStatement.executeUpdate();
		} finally{
			close(preparedStatement);
		}
	}
	
	
	
	
	
	
	public static void set(PreparedStatement preparedStatement,Object... params) throws SQLException{
		
		int index=1;
		for (int i = 0; i < params.length; i++) {
			Object param=params[i];
			SQLTypeMap.getSQLType(param).set(preparedStatement,param, index);
			index++;
		}
		
	}
	
		
	public static void  closeAll(Object... resources) {
		for (int i = 0; i < resources.length; i++) {
			close(resources[i]);
		}
	}
	public static void  close(Object resource) {
		if (resource != null)
			try {
				if (resource instanceof Connection) {
					((Connection) resource).close();
				} else if (resource instanceof Statement) {
					((Statement) resource).close();
				} else if (resource instanceof ResultSet) {
					((ResultSet) resource).close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	
}

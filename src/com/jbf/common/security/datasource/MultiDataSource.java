/************************************************************
 * 类名：MultiDataSource.java
 *
 * 类别：DataSource
 * 功能：多数据源类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.util.Assert;

import com.jbf.common.exception.AppRuntimeException;
import com.jbf.common.security.SecureUtil;

public class MultiDataSource implements DataSource {

	
	private static String dsDefault; // 默认数据源标识符
	private static ThreadLocal contextHolder = new InheritableThreadLocal();
	private static Map dataSources; //数据源集合
	private static Map dsAlias;
	 
	
	
	public MultiDataSource() {
	}
	
	/**
	 * 获取当前数据源
	 * @return
	 */
	public DataSource getDataSourceByYear() {
		UserDataSource userDS = getUserDataSource();
		DataSource ds = getDataSourceByYear(userDS.getLoginDS());
		if (ds != null)
			return ds;
		else
			return getDefaultDataSource();
	}
	
	/**
	 * 获取默认数据源
	 * @return
	 */
	public DataSource getDefaultDataSource() {
		if (dsDefault == null || !(dsDefault instanceof String))
			throw new AppRuntimeException("无法获取数据库连接!");
		else {
			DataSource defaultDataSource = getDataSourceByYear(dsDefault);
			if (defaultDataSource == null)
				throw new AppRuntimeException("请检查多数据源配置文件中的默认数据源[" + dsDefault + "]是否正确!");
			else
				return defaultDataSource;
		}
	}
	
	public DataSource getDataSourceByYear(String year) {
		return (DataSource) dataSources.get(year);
	}

	public static UserDataSource getUserDataSource() {
		UserDataSource userDS = (UserDataSource) contextHolder.get();
		if (userDS == null) {
			userDS = SecureUtil.getDataSource();
			if (userDS == null || userDS.getLoginDS() == null) {
				userDS = new UserDataSource();
				userDS.setLoginDS(dsDefault);
			}
			contextHolder.set(userDS);
		}
		return userDS;
	} 
	
    public static String findDS(String year) {
        if(year == null)
            return null;
        if(dsAlias != null)
        {
            String newyear = (String)dsAlias.get(year);
            if(newyear != null)
                year = newyear;
        }
        if(dataSources.get(year) == null)
            return null;
        else
            return year;
    }
    
    /**
     * 设置登录年度
     * @param ds
     */
    public static void setLoginDS(String ds)  {
        UserDataSource user = getUserDataSource();
        String newds = findDS(ds);
        if(newds != null) {
            user.setLoginDS(newds);
            contextHolder.set(user);
        }
    }
	
    /**
     * 获取所有数据源
     * @return
     */
    public static Map getDatasources() {
        Assert.notNull(dataSources, "数据源未初始化，请配置datasource.xml!");
        return dataSources;
    }
    
    public Map getDataSources() {
    	return dataSources;
    }
	
    public void setDataSources(Map dataSources){
    	this.dataSources = dataSources;
   }
	
	public void setDsDefault(String dsDefaultNew) {
		dsDefault = dsDefaultNew;
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		throw new AppRuntimeException("不支持的方法");
	}
	
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	public void setLoginTimeout(int seconds) throws SQLException {
		
	}
	
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public Connection getConnection() throws SQLException {
		DataSource targetDS = getDataSourceByYear();
		return targetDS.getConnection();
	}

	public Connection getConnection(String username, String password) throws SQLException {
		throw new AppRuntimeException("不支持的方法");
	}
}

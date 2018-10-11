/************************************************************
 * 类名：WeblogicDataSource.java
 *
 * 类别：工具类
 * 功能：获取Weblogic数据源
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dao.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.jbf.common.security.datasource.MultiDataSource;


public class WeblogicDataSource {

	private static WeblogicDataSource instance = null;
	
	private static Context ctx = null;
	
	private WeblogicDataSource() throws NamingException {
		
		/**
		 * 当跨不同weblogic服务时,需在此配置对应weblogic服务端IP与端口号.
		 *
		 *	Hashtable<String, String> ht = new Hashtable<String, String>();
		 *	ht.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		 *  ht.put(Context.PROVIDER_URL, "t3://10.28.110.61:7001");
		 *  ctx = new InitialContext(ht);
		 */
		ctx = new InitialContext();
	}
	
	public  DataSource getDataSource(String jndiName) throws NamingException {
		
		DataSource ds = null;
		ds = (DataSource) ctx.lookup(jndiName);
		
		MultiDataSource.getDatasources();
		
		return ds;
	}
	
	private Connection connection(String jndiName) throws SQLException, NamingException {
		
		Connection con = getDataSource(jndiName).getConnection();
		
		return con;
	}
	
	public static WeblogicDataSource getInstance() throws NamingException {
		
		if (instance == null) {
			instance = new WeblogicDataSource();
		}
		
		return instance;
	}
	
	public Connection getConnection(String jndiName) throws SQLException, NamingException {
		
		return connection(jndiName);
	}
}
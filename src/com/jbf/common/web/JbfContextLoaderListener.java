/************************************************************
 * 类名：JbfContextLoaderListener
 *
 * 类别：监听器
 * 功能：在spring初始化时，将环境的引用放到全局变量中，以便在任意时间可以用它取得bean
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-11  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.web;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JbfContextLoaderListener extends ContextLoaderListener {

	public static ApplicationContext applicationContext;

	/**
	 * bean环境初始化
	 */
	public void contextInitialized(ServletContextEvent event) {

		try {
			super.contextInitialized(event);
			applicationContext = (ApplicationContext) WebApplicationContextUtils
					.getWebApplicationContext(event.getServletContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 环境销毁
	 */
	public void contextDestroyed(ServletContextEvent event) {
		super.contextDestroyed(event);

	}

}

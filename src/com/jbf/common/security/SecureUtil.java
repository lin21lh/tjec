/************************************************************
 * 类名：SecureUtil.java
 *
 * 类别：工具类
 * 功能：获取登录用户信息及数据源工具类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-17  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jbf.common.security.datasource.UserDataSource;
import com.jbf.sys.user.po.SysUser;

public class SecureUtil {

	/**
	 * 获取当前登录数据源
	 * @return
	 */
	public static UserDataSource getDataSource() {
		UserInfo authuser = getAuthUser();
		if (authuser == null)
			return null;
		else
			return authuser.getDs();
	}
	
	/**
	 * 获取当前登录用户
	 * @return
	 */
    public static synchronized SysUser getCurrentUser() {
        UserInfo authUser = getAuthUser();
        if(authUser == null) {
            return null;
        } else {
            return authUser.getUser();
        }
    }
	
    /**
     * 获取当前登录用户信息
     * @return
     */
	public static UserInfo getAuthUser() {
		Authentication currentauthAuthentication = getCurrentAuthentication();
		if (currentauthAuthentication == null)
			return null;
		Object user = currentauthAuthentication.getPrincipal();
		if (user == null || !(user instanceof UserInfo))
			return null;
		else
			return (UserInfo) user;
	}
	
	/**
	 * 获取安全认证管理
	 * @return
	 */
	private static Authentication getCurrentAuthentication() {
		SecurityContext context;
		try {
			context = SecurityContextHolder.getContext();
		} catch (Exception e) {
			context = null;
		}
		if (context == null || context.getAuthentication() == null)
			return null;
		else
			return context.getAuthentication();
	}
}

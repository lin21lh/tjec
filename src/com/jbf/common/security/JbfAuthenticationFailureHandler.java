/************************************************************
 * 类名：JbfAuthenticationFailureHandler.java
 *
 * 类别：处理类
 * 功能：登录验证失败处理
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-16  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class JbfAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	/**
	 * 登录失败之后处理
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,HttpServletResponse response, AuthenticationException exception)throws IOException, ServletException {
		request.setAttribute("errorMsg", exception.getMessage());
		// SysLogApp.writeLogToLogin(request, "登录系统失败，失败原因：" + exception.getMessage());
		super.setUseForward(true); //重定向 导致request放置的属性丢失
		super.onAuthenticationFailure(request, response, exception);
	}
}

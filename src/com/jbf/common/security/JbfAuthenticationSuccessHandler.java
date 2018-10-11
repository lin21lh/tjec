/************************************************************
 * 类名：JbfAuthenticationSuccessHandler.java
 *
 * 类别：处理类
 * 功能：验证成功处理
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
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.jbf.sys.log.service.impl.SysLogApp;

public class JbfAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	/**
	 * 登录成功之后处理 记录操作日志
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		SysLogApp.writeLogToLogin(request, "登录系统");
		HttpSession  session = request.getSession();
		session.setAttribute("fromIECore",  request.getParameter("fromIECore"));
		super.onAuthenticationSuccess(request, response, authentication);
		
	}
}

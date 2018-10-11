package com.jbf.common.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.jbf.sys.log.service.impl.SysLogApp;

public class JbfLogoutHandler implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) {
		
		SysLogApp.writeLogToLogin(request, "退出系统");
	}

}

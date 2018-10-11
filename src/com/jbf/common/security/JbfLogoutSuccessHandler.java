package com.jbf.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.jbf.sys.log.service.impl.SysLogApp;


public class JbfLogoutSuccessHandler implements LogoutSuccessHandler {

	private String logout_success_url = "";
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		SysLogApp.writeLogToLogin(request, "退出系统");
		response.sendRedirect(this.logout_success_url);
	}
	
	public String getLogout_success_url() {
		return logout_success_url;
	}
	
	public void setLogout_success_url(String logout_success_url) {
		this.logout_success_url = logout_success_url;
	}

}

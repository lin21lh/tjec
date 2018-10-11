/************************************************************
 * 类名：JbfSecurityFilter.java
 *
 * 类别：过滤器
 * 功能：安全验证过滤器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-16  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.web.servlet.support.JstlUtils;


public class JbfSecurityFilter extends AbstractSecurityInterceptor implements Filter {
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	 private MessageSource messageSource;

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}
	
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
        this.messageSource=messageSource;
    }

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}
	
	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		// object为FilterInvocation对象
                  //super.beforeInvocation(fi);源码
		//1.获取请求资源的权限
		//执行Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(object);
		//2.是否拥有权限
		//this.accessDecisionManager.decide(authenticated, object, attributes);
		
		InterceptorStatusToken token = super.beforeInvocation(fi);
		if(token==null){
			
			if(isAnonymous()){
				throw new AccessDeniedException(" 系统不允许匿名用户访问！ ");
			}
		}
        try {  
       	HttpServletRequest request=fi.getRequest();
        	
        	HttpSession session=request.getSession(false);
        	 if(session==null){
   			 throw new AccessDeniedException(" 系统不允许匿名用户访问！ ");
   	     }
       	this.localizationContext(request);
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());  
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            super.afterInvocation(token, null);  
        }  
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}
	
	/**
	 * 判断是否匿名访问
	 * @return
	 */
	private boolean isAnonymous() {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken){
			return true;
		}else{
			return false;
		}
	}
	
	public void localizationContext(HttpServletRequest request){
		String uri= request.getRequestURI();
		int index= uri.lastIndexOf('.');
		if(index!=-1){
			if(index!=uri.length()-1)
				uri=uri.substring(index+1);
			else
				return;
		}else{
			return;
		}
		if(uri.toLowerCase().equals("jsp"))
    	  JstlUtils.exposeLocalizationContext(request,messageSource );

	 }

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		//下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
		return FilterInvocation.class;
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}
}
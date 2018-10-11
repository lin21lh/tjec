/************************************************************
 * 类名：JbfLoginAuthenticationFilter.java
 *
 * 类别：过滤器
 * 功能：登录验证过滤器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-16  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.filter;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.datasource.service.DataSourceService;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.PTConst;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.systemConfiguration.SystemCfg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;

public class JbfLoginAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	public static final String FINACEYEAR = "finaceYear"; // 财政年度
	public static final String USERNAME = "username"; // 用户名
	public static final String PASSWORD = "password"; // 密码
	public static final String VERIFY_CODE = "verifyCode"; // 密码

	private DataSourceService dataSourceService;

	public void setDataSourceService(DataSourceService dataSourceService) {
		this.dataSourceService = dataSourceService;
	}

	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {

		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("登录方式不支持: " + request.getMethod());
		}
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//用户service
		SysUserService userService = (SysUserService)WebContextFactoryUtil.getBean("sys.user.service.impl.SysUserServiceImpl");
		//系统参数方法
		ParamCfgComponent pcfg = (ParamCfgComponent)WebContextFactoryUtil.getBean("sys.paramCfg.component.ParamCfgComponentImpl");
		String username = obtainUsername(request);
		//增加忽略用户名大小写
		//获取是否区分大小写的标志
		String flag = pcfg.findGeneralParamValue("SYSTEM", "CASE_INSENSITIVE");
		if("N".equals(flag)){//不区分大小写
			String usernameUpper = username.toUpperCase();
			SysUser userUpper = userService.getUserByUsercode(usernameUpper);
			if(userUpper!=null){
				username = usernameUpper;
			}
		}
		String password = obtainPassword(request);
		/*
		 * if (dataSourceService.isEnabledMultiDataSource()) { // 判断是否启用分布式数据源
		 * String year = request.getParameter(FINACEYEAR);
		 * 
		 * if (year != null && MultiDataSource.findDS(year.trim()) == null)
		 * throw new AuthenticationServiceException("财政年度不存在,请重新登陆![year=" +
		 * year + "]");
		 * 
		 * MultiDataSource.setLoginDS(year); }
		 * 
		 * String username = obtainUsername(request); String password =
		 * obtainPassword(request); SysUserService userService =
		 * (SysUserService) WebContextFactoryUtil
		 * .getBean("sys.user.service.impl.SysUserServiceImpl"); //
		 * 验证用户账号与密码是否对应 username = username.trim();
		 * 
		 * SysUser user = userService.getUserByUsercode(username);
		 * 
		 * if (user == null) { // 用户不存在 throw new
		 * AuthenticationServiceException(
		 * AppException.getMessage("user.login.user.not.exists")); } else { //
		 * 验证MD5加密的密码 Md5PasswordEncoder enc = new Md5PasswordEncoder(); if
		 * (!enc.isPasswordValid(user.getUserpswd(), password, username)) { //
		 * 密码错误 throw new AuthenticationServiceException( AppException
		 * .getMessage("user.login.password.incorrect")); } }
		 */
		// 判断request验证码与session验证码是否一致
		HttpSession session = request.getSession();
		
		/*if (SystemCfg.VerificationcodeEnabled()) {
			String verifyCode = request.getParameter(VERIFY_CODE);
			if (verifyCode == null) {
				throw new AuthenticationServiceException(AppException.getMessage("user.login.verificationcode.null"));
			} else {
				String okVerifyCode = String.valueOf(session.getAttribute(VERIFY_CODE));
				if (!verifyCode.equalsIgnoreCase(okVerifyCode)) {
					throw new AuthenticationServiceException(AppException.getMessage("user.login.verificationcode.incorrect"));
				}
			}
		}*/
		String calogin = request.getParameter("calogin");
		String fromportal = request.getParameter("fromportal");//单点登录：1
		calogin = calogin==null?"":calogin;
		if("1".equals(fromportal)){//单点登录
			PTConst.userSuperPWD = true;
			password = PTConst.superPassWord;
		}else {
			if (PTConst.STARTCA) {//启用ca
				if("true".equals(calogin)){//ukey登录
					PTConst.userSuperPWD = true;
					password = PTConst.superPassWord;
				}else {
					PTConst.userSuperPWD = false;
				}
			}else {
				PTConst.userSuperPWD = false;
			}
		}

		// UsernamePasswordAuthenticationToken实现 Authentication
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

		// 允许子类设置详细属性
		setDetails(request, authRequest);
		// 运行UserDetailsService的loadUserByUsername 再次封装Authentication
		Authentication authen = null;
		try {
			authen = this.getAuthenticationManager().authenticate(authRequest);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			if (e instanceof BadCredentialsException) {
				throw new BadCredentialsException("用户名或密码错误！");
			} else{
				throw new BadCredentialsException("登录过程发生异常！");
			}
		}
		
		SysUser user = userService.getUserByUsercode(username);
		
		if (user.getStatus().equals(Byte.valueOf("9"))) {
			throw new BadCredentialsException(AppException.getMessage("user.login.user.overdue"));
		}
			
		if (StringUtil.isNotBlank(user.getOverduedate())) {
			Date overdue = DateUtil.parseDate(user.getOverduedate(), "yyyy-MM-dd");
			if (overdue.before(new Date()))
				throw new BadCredentialsException(AppException.getMessage("user.login.user.overdue"));
		}
		//用户密码3个月的有效期
//		SysUserService userService = (SysUserService) WebContextFactoryUtil
//				  .getBean("sys.user.service.impl.SysUserServiceImpl");
//		
//		SysUser user = userService.getUserByUsercode(username);
//		Date modDate = DateUtil.parseDate(user.getModifytime(), "yyyy-MM-dd");
//		if(modDate != null){
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(modDate);
//			calendar.add(Calendar.MONTH, 3);
//			
//			Calendar now = Calendar.getInstance();
//			if(calendar.before(now)){
//				throw new BadCredentialsException("密码超过3个月的有效期，请联系管理员修改密码！");
//			}
//		}
			
		
		return authen;
	}

	@Override
	protected String obtainUsername(HttpServletRequest request) {
		Object obj = request.getParameter(USERNAME);
		return null == obj ? "" : obj.toString();
	}

	@Override
	protected String obtainPassword(HttpServletRequest request) {
		Object obj = request.getParameter(PASSWORD);
		return null == obj ? "" : obj.toString();
	}

}

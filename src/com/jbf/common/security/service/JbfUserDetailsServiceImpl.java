/************************************************************
 * 类名：JbfUserDetailsServiceImpl.java
 *
 * 类别：Service
 * 功能：用户登录服务
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-18  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jbf.common.security.UserInfo;
import com.jbf.common.security.datasource.MultiDataSource;
import com.jbf.common.security.datasource.UserDataSource;
import com.jbf.common.util.PTConst;
import com.jbf.common.util.WebContextFactoryUtil;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.systemConfiguration.SystemCfg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;

public class JbfUserDetailsServiceImpl implements UserDetailsService {

	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		SysUserService userService = (SysUserService) WebContextFactoryUtil.getBean("sys.user.service.impl.SysUserServiceImpl");
		SysUser user = userService.getUserByUsercode(username);
		if (user == null) {

			throw new UsernameNotFoundException(username);
		}

		Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user);
		if(SystemCfg.MultiDataSourceEnabled()) {
			UserDataSource uds = MultiDataSource.getUserDataSource();
			System.err.println(uds.getLoginDS());
		}
		
		if (PTConst.userSuperPWD) { //启用CA不用输入密码 为保证登录成功 加入超级默认密码
			Md5PasswordEncoder enc = new Md5PasswordEncoder();
			user.setUserpswd(enc.encodePassword(PTConst.superPassWord, username));
		}
		
		UserInfo userdetail = new UserInfo(user, null, 0, null, null, grantedAuths);
		return userdetail;
	}

	/**
	 * 取得用戶角色
	 * 
	 * @param user
	 * @return
	 */
	private Set<GrantedAuthority> obtionGrantedAuthorities(SysUser user) {
		List<SysResource> resources = new ArrayList<SysResource>();
		Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
		for (SysResource resource : resources) {

			authSet.add(new SimpleGrantedAuthority("ROLE_"
					+ resource.getResourceid()));
		}
		return authSet;
	}

}

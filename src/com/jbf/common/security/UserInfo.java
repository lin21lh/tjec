/************************************************************
 * 类名：UserInfo.java
 *
 * 类别：UserDetails
 * 功能：当前登录用户信息
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-17  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jbf.common.security.datasource.UserDataSource;
import com.jbf.sys.user.po.SysUser;

public class UserInfo implements UserDetails {

	private static final long serialVersionUID = 1L;
	private SysUser user; // 用户信息
	private String selectYear; // 选择年度
	private Integer bdgyear; //年度
	private String admincode; //行政区码
	private UserDataSource ds; // 用户连接数据源
	private long userid; // 用户ID
	private Collection<? extends GrantedAuthority> roles; // 角色

	public UserInfo() {

	}

	public UserInfo(SysUser user) {
		this.user = user;
		this.userid = user.getUserid();
	}

	public UserInfo(SysUser user, long userid) {
		this.user = user;
		this.userid = userid;
	}

	public UserInfo(SysUser user, String selectYear, Integer bdgyear, String admincode, UserDataSource ds,
			Collection<? extends GrantedAuthority> roles) {

		this.user = user;
		this.userid = user.getUserid();
		this.selectYear = selectYear;
		this.ds = ds;
		this.roles = roles;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public String getSelectYear() {
		return selectYear;
	}

	public void setSelectYear(String selectYear) {
		this.selectYear = selectYear;
	}
	
	public Integer getBdgyear() {
		return bdgyear;
	}
	
	public void setBdgyear(Integer bdgyear) {
		this.bdgyear = bdgyear;
	}
	
	public String getAdmincode() {
		return admincode;
	}
	
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	public UserDataSource getDs() {
		return ds;
	}

	public void setDs(UserDataSource ds) {
		this.ds = ds;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	public void setRoles(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return user.getUserpswd();
	}

	public String getUsername() {
		return user.getUsercode();
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}
}

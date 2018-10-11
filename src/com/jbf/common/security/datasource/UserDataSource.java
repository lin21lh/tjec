/************************************************************
 * 类名：UserDataSource.java
 *
 * 类别：UserDataSource
 * 功能：用户使用年度及行政区码
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-18  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.datasource;

public class UserDataSource {

	private String loginDS; //登录数据源标识符
	private String currentDS; //当前数据源标识符
	private int loginYear; //登录年度
	private int currentYear; //当前年度
	private String loginAdmincode; //登录行政区码
	private String currentAdmincode; //当前行政区码
	
	public UserDataSource() {
		
	}
	
	public void reset() {
		currentDS = loginDS;
		currentYear = loginYear;
	}
	
	public String getLoginDS() {
		return loginDS;
	}
	
	public void setLoginDS(String loginDS) {
		this.loginDS = loginDS;
		currentDS = loginDS;
		String s[] = loginDS.split(";");
		loginYear = Integer.valueOf(s[0]);
		currentYear = loginYear;
	}
	
   public String getCurrentDS() {
        return currentDS;
    }

    public void setCurrentDS(String currentDS) {
        this.currentDS = currentDS;
        String s[] = currentDS.split(";");
        currentYear = Integer.valueOf(s[0]);
    }

    public int getLoginYear() {
        return loginYear;
    }

    public void setLoginYear(int loginYear) {
        loginDS = loginDS.replaceAll(String.valueOf(this.loginYear), String.valueOf(loginYear));
        this.loginYear = loginYear;
        currentYear = this.loginYear;
    }

    public int getCurrentYear() {
        return currentYear;
    }

	public void setCurrentYear(int currentYear) {
	    currentDS = currentDS.replaceAll(String.valueOf(this.currentYear), String.valueOf(currentYear));
	    this.currentYear = currentYear;
	}
	
	public String getLoginAdmincode() {
		return loginAdmincode;
	}
	
	public void setLoginAdmincode(String loginAdmincode) {
		this.loginAdmincode = loginAdmincode;
	}
	
	public String getCurrentAdmincode() {
		return currentAdmincode;
	}
	
	public void setCurrentAdmincode(String currentAdmincode) {
		this.currentAdmincode = currentAdmincode;
	}
}

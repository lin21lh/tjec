/************************************************************
 * 类名：RoleTreeVo.java
 *
 * 类别：VO
 * 功能：角色资源对应操作VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.vo;

import com.jbf.common.vo.TreeVo;

public class RoleTreeVo extends TreeVo {

	String rolecode;
	String managerid;
	Integer status;

	public String getRolecode() {
		return rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer b) {
		this.status = b;
	}

}

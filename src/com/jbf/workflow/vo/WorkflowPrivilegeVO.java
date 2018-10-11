/************************************************************
 * 类名：WorkflowPrivilegeVO
 * 
 * 类别：vo
 * 功能：工作流权限vo
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.vo;

public class WorkflowPrivilegeVO {
	String rolename;
	Long roleid;
	String activityname;

	public WorkflowPrivilegeVO(Long roleid, String rolename, String activityname) {

		this.rolename = rolename;
		this.roleid = roleid;
		this.activityname = activityname;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

}

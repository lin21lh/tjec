/************************************************************
 * 类名：UserTodoListVo
 *
 * 类别：vo
 * 功能：用户待办任务vo，包括流程实例id，任务id，业务数据主键
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.vo;

public class UserTodoListVo {
	// 工作流流程id
	String wfid;

	// 业务主键id
	String objid;

	// 是否是可选任务
	int iscandidate;

	String backflag;

	public String getWfid() {
		return wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	public String getObjid() {
		return objid;
	}

	public void setObjid(String objid) {
		this.objid = objid;
	}

	public int getIscandidate() {
		return iscandidate;
	}

	public void setIscandidate(int iscandidate) {
		this.iscandidate = iscandidate;
	}

	public String getBackflag() {
		return backflag;
	}

	public void setBackflag(String backflag) {
		this.backflag = backflag;
	}

}

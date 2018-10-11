/************************************************************
 * 类名：SysAppException.java
 *
 * 类别：PO
 * 功能：自定义异常PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.appexception.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysAppException entity. @author mqs
 */
@Entity
@Table(name = "SYS_APPEXCEPTION")
public class SysAppException {

	private String excode;
	private String exmsg;
	private String extype;
	private String createtime;
	private String remark;
	
	@Id
	@Column(name = "EXCODE", nullable = false, length = 20)
	public String getExcode() {
		return excode;
	}
	
	public void setExcode(String excode) {
		this.excode = excode;
	}
	
	@Column(name = "EXMSG", nullable = false, length = 100)
	public String getExmsg() {
		return exmsg;
	}
	
	public void setExmsg(String exmsg) {
		this.exmsg = exmsg;
	}
	
	@Column(name = "EXTYPE", nullable = false, length = 20)
	public String getExtype() {
		return extype;
	}
	
	public void setExtype(String extype) {
		this.extype = extype;
	}
	
	@Column(name = "CREATETIME", length = 20)
	public String getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "REMARK", length = 255)
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}

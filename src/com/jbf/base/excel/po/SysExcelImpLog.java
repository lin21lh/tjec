/************************************************************
 * 类名：SysExcelImpLog
 *
 * 类别：PO
 * 功能：数据导入日志PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * TImpexpImplog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_EXCEL_IMPLOG")
public class SysExcelImpLog implements java.io.Serializable {

	// Fields
	private Long id;
	private Long cfgid;
	private Boolean succ;
	private String message;
	private String batch;
	private String impdate;
	private String usercode;
	private String username;
	private String ipaddr;

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CFGID", scale = 0)
	public Long getCfgid() {
		return this.cfgid;
	}

	public void setCfgid(Long cfgid) {
		this.cfgid = cfgid;
	}

	@Column(name = "SUCC", precision = 1, scale = 0)
	public Boolean getSucc() {
		return this.succ;
	}

	public void setSucc(Boolean succ) {
		this.succ = succ;
	}

	@Column(name = "MESSAGE", length = 600)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "BATCH", length = 50)
	public String getBatch() {
		return this.batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	@Column(name = "IMPDATE", nullable = false, length = 19)
	public String getImpdate() {
		return this.impdate;
	}

	public void setImpdate(String impdate) {
		this.impdate = impdate;
	}

	@Column(name = "USERCODE", length = 50)
	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	@Column(name = "USERNAME", length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "IPADDR", length = 15)
	public String getIpaddr() {
		return this.ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}

}
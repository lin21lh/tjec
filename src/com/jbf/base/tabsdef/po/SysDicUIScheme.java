/************************************************************
 * 类名：SysDicUIScheme.java
 *
 * 类别：PO
 * 功能：界面方案PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SysDicUIScheme entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DICUISCHEME")
public class SysDicUIScheme implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private Long schemeid;
	private String tablecode;
	private String schemename;
	private String listscheme;
	private String formscheme;
	private String treescheme;
	private Byte used;
	private String remark;

	// Constructors

	/** default constructor */
	public SysDicUIScheme() {
	}

	/** minimal constructor */
	public SysDicUIScheme(String tablecode, String schemename) {
		this.tablecode = tablecode;
		this.schemename = schemename;
	}

	/** full constructor */
	public SysDicUIScheme(String tablecode, String schemename,
			String listscheme, String formscheme, String treescheme, Byte used,
			String remark) {
		this.tablecode = tablecode;
		this.schemename = schemename;
		this.listscheme = listscheme;
		this.formscheme = formscheme;
		this.treescheme = treescheme;
		this.used = used;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_DICUISCHEME")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SCHEMEID", nullable = false, scale = 0)
	public Long getSchemeid() {
		return this.schemeid;
	}

	public void setSchemeid(Long schemeid) {
		this.schemeid = schemeid;
	}

	@Column(name = "TABLECODE", nullable = false, length = 50)
	public String getTablecode() {
		return this.tablecode;
	}

	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}

	@Column(name = "SCHEMENAME", nullable = false, length = 50)
	public String getSchemename() {
		return this.schemename;
	}

	public void setSchemename(String schemename) {
		this.schemename = schemename;
	}

	@Column(name = "LISTSCHEME", length = 4000)
	public String getListscheme() {
		return this.listscheme;
	}

	public void setListscheme(String listscheme) {
		this.listscheme = listscheme;
	}

	@Column(name = "FORMSCHEME", length = 4000)
	public String getFormscheme() {
		return this.formscheme;
	}

	public void setFormscheme(String formscheme) {
		this.formscheme = formscheme;
	}

	@Column(name = "TREESCHEME", length = 4000)
	public String getTreescheme() {
		return this.treescheme;
	}

	public void setTreescheme(String treescheme) {
		this.treescheme = treescheme;
	}

	@Column(name = "USED", precision = 2, scale = 0)
	public Byte getUsed() {
		return this.used;
	}

	public void setUsed(Byte used) {
		this.used = used;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
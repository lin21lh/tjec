/************************************************************
 * 类名：SysExcelImpCfg
 *
 * 类别：PO
 * 功能：数据导入模板配置PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.po;

import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * TImpexpImpcfg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_EXCEL_IMPCFG")
public class SysExcelImpCfg implements java.io.Serializable {

	// Fields

	private Long id;
	private String cfgcategory;
	private String cfgname;
	private String cfgfile;
	private String status;
	private Clob cfgxml;
	private String remark;
	private String dataexception;
	private String classname;

	// Constructors

	/** default constructor */
	public SysExcelImpCfg() {
	}

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

	@Column(name = "CFGCATEGORY", length = 50)
	public String getCfgcategory() {
		return this.cfgcategory;
	}

	public void setCfgcategory(String cfgcategory) {
		this.cfgcategory = cfgcategory;
	}

	@Column(name = "CFGNAME", length = 50)
	public String getCfgname() {
		return this.cfgname;
	}

	public void setCfgname(String cfgname) {
		this.cfgname = cfgname;
	}

	@Column(name = "CFGFILE", length = 100)
	public String getCfgfile() {
		return this.cfgfile;
	}

	public void setCfgfile(String cfgfile) {
		this.cfgfile = cfgfile;
	}

	@Column(name = "STATUS", length = 2)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CFGXML")
	public Clob getCfgxml() {
		return this.cfgxml;
	}

	public void setCfgxml(Clob cfgxml) {
		this.cfgxml = cfgxml;
	}

	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "DATAEXCEPTION", length = 100)
	public String getDataexception() {
		return dataexception;
	}

	public void setDataexception(String dataexception) {
		this.dataexception = dataexception;
	}

	@Column(name = "CLASSNAME", length = 100)
	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

}
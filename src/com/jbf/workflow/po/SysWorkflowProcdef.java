/************************************************************
 * 类名：SysWorkflowProcdef
 *
 * 类别：PO
 * 功能：工作流流程定义PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * SysWorkflowProcdef entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_WORKFLOW_PROCDEF")
public class SysWorkflowProcdef implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_PROCDEF")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	private Long id;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "STARTDATE", length = 7)
	private Date startdate;

	@JSONField(format = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name = "ENDDATE", length = 7)
	private Date enddate;

	@Column(name = "TYPE", nullable = false, length = 10)
	private String type;

	@Column(name = "REMARK", length = 100)
	private String remark;

	@Column(name = "KEY")
	private String key;

	@Column(name = "TABID")
	private Long tabid;

	@Formula("(select t.tablename from sys_dictable t where t.tableid=tabid)")
	private String tabname;

	@Column(name = "DEFAULTUI")
	private Byte defaultui;
	
	@Column(name = "STATUSBEAN")
	private String statusbean;

	// Constructors

	/** default constructor */
	public SysWorkflowProcdef() {
	}

	/** minimal constructor */
	public SysWorkflowProcdef(Long id, String name, String type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	/** full constructor */
	public SysWorkflowProcdef(Long id, String name, Date startdate,
			Date enddate, String type, String remark) {
		this.id = id;
		this.name = name;
		this.startdate = startdate;
		this.enddate = enddate;
		this.type = type;
		this.remark = remark;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return this.enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Long getTabid() {
		return tabid;
	}

	public void setTabid(Long tabid) {
		this.tabid = tabid;
	}

	public String getTabname() {
		return tabname;
	}

	public void setTabname(String tabname) {
		this.tabname = tabname;
	}

	public Byte getDefaultui() {
		return defaultui;
	}

	public void setDefaultui(Byte defaultui) {
		this.defaultui = defaultui;
	}

	public String getStatusbean() {
		return statusbean;
	}

	public void setStatusbean(String statusbean) {
		this.statusbean = statusbean;
	}

}
/************************************************************
 * 类名：SysWorkflowProcversion
 *
 * 类别：PO
 * 功能：工作流版本PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

/**
 * SysWorkflowProcversion entity. 保存流程的具体版本
 */
@Entity
@Table(name = "SYS_WORKFLOW_PROCVERSION")
public class SysWorkflowProcversion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_PROCVERSION")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	private Long id;

	@Column(name = "KEY", length = 50)
	private String key;

	@Column(name = "VERSION", precision = 6, scale = 0)
	private Integer version;

	@Column(name = "STATUS", length = 2)
	private String status;

	@Column(name = "REMARK", length = 50)
	private String remark;

	@Column(name = "DEPLOYMENTID", length = 50)
	private String deploymentid;

	@Column(name = "STARTDATE", length = 20)
	private String startdate;

	@Column(name = "ENDDATE", length = 20)
	private String enddate;

	@Formula("( select dic.name from sys_dicenumitem dic where dic.code= status and dic.elementcode='SYS_TRUE_FALSE')")
	private String cnstatus;

	@Column(name = "FIRSTNODE", length = 20)
	private String firstnode;

	// Constructors

	/** default constructor */
	public SysWorkflowProcversion() {
	}

	/** full constructor */
	public SysWorkflowProcversion(String key, Integer version, String status,
			String remark) {
		this.key = key;
		this.version = version;
		this.status = status;
		this.remark = remark;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDeploymentid() {
		return deploymentid;
	}

	public void setDeploymentid(String deploymentid) {
		this.deploymentid = deploymentid;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getCnstatus() {
		return cnstatus;
	}

	public void setCnstatus(String cnstatus) {
		this.cnstatus = cnstatus;
	}

	public String getFirstnode() {
		return firstnode;
	}

	public void setFirstnode(String firstnode) {
		this.firstnode = firstnode;
	}

}
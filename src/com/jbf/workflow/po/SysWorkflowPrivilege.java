/************************************************************
 * 类名：SysWorkflowPrivilege
 *
 * 类别：PO
 * 功能：工作流权限PO
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

/**
 * SysWorkflowPrivilege entity. @author MyEclipse Persistence Tools
 * 保存流程具体版本的活动节点对应的角色权限
 */
@Entity
@Table(name = "SYS_WORKFLOW_PRIVILEGE")
public class SysWorkflowPrivilege implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private Long id;
	private String deploymentid;
	private String activityname;
	private Long roleid;
	private String rule;
	private String ruleparam;
	private String key;
	private Integer version;

	// Constructors

	/** default constructor */
	public SysWorkflowPrivilege() {
	}

	/** full constructor */
	public SysWorkflowPrivilege(String deploymentid, String activityname,
			Long roleid, String rule, String ruleparam) {
		this.deploymentid = deploymentid;
		this.activityname = activityname;
		this.roleid = roleid;
		this.rule = rule;
		this.ruleparam = ruleparam;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_PRIVILEGE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "DEPLOYMENTID", length = 50)
	public String getDeploymentid() {
		return this.deploymentid;
	}

	public void setDeploymentid(String deploymentid) {
		this.deploymentid = deploymentid;
	}

	@Column(name = "ACTIVITYNAME", length = 50)
	public String getActivityname() {
		return this.activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	@Column(name = "ROLEID", scale = 0)
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	@Column(name = "RULE", length = 50)
	public String getRule() {
		return this.rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Column(name = "RULEPARAM", length = 50)
	public String getRuleparam() {
		return this.ruleparam;
	}

	public void setRuleparam(String ruleparam) {
		this.ruleparam = ruleparam;
	}
	@Column(name = "KEY", length = 50)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	@Column(name = "VERSION")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
}
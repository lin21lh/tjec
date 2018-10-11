/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：PO
 * 功能：工作流退回撤回属性PO
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
 * SysWorkflowBackattr entity. 
 * 保存流程的任务节点是否可退回，是否可撤回属性
 */
@Entity
@Table(name = "SYS_WORKFLOW_BACKATTR")
public class SysWorkflowBackattr implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deploymentid;
	private String activityname;
	private Boolean returnable;
	private Boolean withdrawable;
	private String key;
	private Integer version;

	// Constructors

	/** default constructor */
	public SysWorkflowBackattr() {
	}

	/** full constructor */
	public SysWorkflowBackattr(String deploymentid, String activityname,
			Boolean returnable, Boolean withdrawable) {
		this.deploymentid = deploymentid;
		this.activityname = activityname;
		this.returnable = returnable;
		this.withdrawable = withdrawable;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_WORKFLOW_BACKATTR")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
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

	@Column(name = "RETURNABLE", precision = 1, scale = 0)
	public Boolean getReturnable() {
		return this.returnable;
	}

	public void setReturnable(Boolean returnable) {
		this.returnable = returnable;
	}

	@Column(name = "WITHDRAWABLE", precision = 1, scale = 0)
	public Boolean getWithdrawable() {
		return this.withdrawable;
	}

	public void setWithdrawable(Boolean withdrawable) {
		this.withdrawable = withdrawable;
	}

	@Column(name = "KEY")
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
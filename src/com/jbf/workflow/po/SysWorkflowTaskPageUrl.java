/************************************************************
 * 类名：SysWorkflowTaskPageUrl
 *
 * 类别：PO
 * 功能：工作流节点对应处理页面PO
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
 * SysWorkflowTaskPageUrl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_WORKFLOW_TASK_PAGE_URL")
public class SysWorkflowTaskPageUrl implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private Long id;
	private String deploymentid;
	private String key;
	private Integer version;
	private String activityname;
	private String pageurl;

	// Constructors

	/** default constructor */
	public SysWorkflowTaskPageUrl() {
	}

	/** full constructor */
	public SysWorkflowTaskPageUrl(String deploymentid, String key,
			Integer version, String activityname, String pageurl) {
		this.deploymentid = deploymentid;
		this.key = key;
		this.version = version;
		this.activityname = activityname;
		this.pageurl = pageurl;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_TASK_PAGE_URL")
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

	@Column(name = "KEY", length = 50)
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VERSION", precision = 6, scale = 0)
	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "ACTIVITYNAME", length = 50)
	public String getActivityname() {
		return this.activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	@Column(name = "PAGEURL", length = 100)
	public String getPageurl() {
		return this.pageurl;
	}

	public void setPageurl(String pageurl) {
		this.pageurl = pageurl;
	}

}
/************************************************************
 * 类名：SysWorkflowBacklineRec
 *
 * 类别：PO
 * 功能：工作流退回撤回线路PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SysWorkflowBacklineRec entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_WORKFLOW_BACKLINE_REC")
public class SysWorkflowBacklineRec implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private BigDecimal id;
	private String key;
	private Integer version;
	private String deploymentid;
	private String srcacti;
	private String tgtacti;
	private String transname;
	private String type;

	// Constructors

	/** default constructor */
	public SysWorkflowBacklineRec() {
	}

	/** full constructor */
	public SysWorkflowBacklineRec(String key, Integer version,
			String deploymentid, String srcacti, String tgtacti,
			String transname) {
		this.key = key;
		this.version = version;
		this.deploymentid = deploymentid;
		this.srcacti = srcacti;
		this.tgtacti = tgtacti;
		this.transname = transname;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_BACKLINE_REC")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
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

	@Column(name = "DEPLOYMENTID", length = 50)
	public String getDeploymentid() {
		return this.deploymentid;
	}

	public void setDeploymentid(String deploymentid) {
		this.deploymentid = deploymentid;
	}

	@Column(name = "SRCACTI", length = 50)
	public String getSrcacti() {
		return this.srcacti;
	}

	public void setSrcacti(String srcacti) {
		this.srcacti = srcacti;
	}

	@Column(name = "TGTACTI", length = 50)
	public String getTgtacti() {
		return this.tgtacti;
	}

	public void setTgtacti(String tgtacti) {
		this.tgtacti = tgtacti;
	}

	@Column(name = "TRANSNAME", length = 50)
	public String getTransname() {
		return this.transname;
	}

	public void setTransname(String transname) {
		this.transname = transname;
	}

	@Column(name = "TYPE", length = 2)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
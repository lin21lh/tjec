/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：PO
 * 功能：工作流退回撤回属性PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     maqs         初版
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
 * SysWorkflowDecConds entity. 
 * 工作流decision 条件配置
 */
@Entity
@Table(name = "SYS_WORKFLOW_DECCONDS")
public class SysWorkflowDecConds implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long deccondid;
	private String wfkey;
	private Integer wfversion;
	private String decisionname;
	private String taskname;
	private Long scopemainid;

	// Constructors

	/** default constructor */
	public SysWorkflowDecConds() {
	}

	/** full constructor */
	public SysWorkflowDecConds(Long deccondid, String wfkey, Integer wfversion, String decisionname, String taskname
			, Long scopemainid) {
		this.deccondid = deccondid;
		this.wfkey = wfkey;
		this.wfversion = wfversion;
		this.decisionname = decisionname;
		this.taskname = taskname;
		this.scopemainid = scopemainid;
	}
	
	public SysWorkflowDecConds(Long deccondid, String wfkey, Integer wfversion, String decisionname, String taskname) {
		this.deccondid = deccondid;
		this.wfkey = wfkey;
		this.wfversion = wfversion;
		this.decisionname = decisionname;
		this.taskname = taskname;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_WORKFLOW_DECCONDS")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "DECCONDID", unique = true, nullable = false, scale = 0)
	public Long getDeccondid() {
		return deccondid;
	}
	
	public void setDeccondid(Long deccondid) {
		this.deccondid = deccondid;
	}

	@Column(name = "WFKEY", length = 50)
	public String getWfkey() {
		return wfkey;
	}
	
	public void setWfkey(String wfkey) {
		this.wfkey = wfkey;
	}

	@Column(name = "WFVERSION", scale = 0)
	public void setWfversion(Integer wfversion) {
		this.wfversion = wfversion;
	}
	
	public Integer getWfversion() {
		return wfversion;
	}

	@Column(name = "DECISIONNAME", length = 50)
	public String getDecisionname() {
		return decisionname;
	}
	
	public void setDecisionname(String decisionname) {
		this.decisionname = decisionname;
	}

	@Column(name = "TASKNAME", length = 50)
	public String getTaskname() {
		return taskname;
	}
	
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	@Column(name = "scopemainid", scale = 0)
	public Long getScopemainid() {
		return scopemainid;
	}

	public void setScopemainid(Long scopemainid) {
		this.scopemainid = scopemainid;
	}
}
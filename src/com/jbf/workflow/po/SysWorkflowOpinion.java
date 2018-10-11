/************************************************************
 * 类名：SysWorkflowOpinion
 *
 * 类别：PO
 * 功能：工作流审核意见记录PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-04-13  CFIT-PG     HYF         初版
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

@Entity
@Table(name = "SYS_WORKFLOW_OPINION")
public class SysWorkflowOpinion {

	Long id;
	String execid;
	String srcacti;
	String transation;
	String tgtacti;
	String author;
	String opinion;
	String crdate;
	Long htask;

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_OPINION")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "EXECID", length = 50)
	public String getExecid() {
		return execid;
	}

	public void setExecid(String execid) {
		this.execid = execid;
	}

	@Column(name = "SRCACTI", length = 50)
	public String getSrcacti() {
		return srcacti;
	}

	public void setSrcacti(String srcacti) {
		this.srcacti = srcacti;
	}

	@Column(name = "TRANSATION", length = 50)
	public String getTransation() {
		return transation;
	}

	public void setTransation(String transation) {
		this.transation = transation;
	}

	@Column(name = "TGTACTI", length = 50)
	public String getTgtacti() {
		return tgtacti;
	}

	public void setTgtacti(String tgtacti) {
		this.tgtacti = tgtacti;
	}

	@Column(name = "AUTHOR", length = 50)
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "OPINION", length = 50)
	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	@Column(name = "CRDATE", length = 50)
	public String getCrdate() {
		return crdate;
	}

	public void setCrdate(String crdate) {
		this.crdate = crdate;
	}

	@Column(name = "HTASK")
	public Long getHtask() {
		return htask;
	}

	public void setHtask(Long htask) {
		this.htask = htask;
	}

}

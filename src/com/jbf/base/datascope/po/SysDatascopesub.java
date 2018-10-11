/************************************************************
 * 类名：SysDatascopesub.java
 *
 * 类别：PO
 * 功能：数据权限条件项PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SysDatascopesub entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DATASCOPESUB")
public class SysDatascopesub implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long scopesubid;
	private Long scopemainid;
	private String scopesubname;
	private Integer seqno;
	private String createdate;
	private String remark;

	// Constructors

	/** default constructor */
	public SysDatascopesub() {
	}
	
	/** minimal constructor */
	public SysDatascopesub(Long scopemainid,
			String scopesubname, Integer seqno, String createdate) {
		this.scopemainid = scopemainid;
		this.scopesubname = scopesubname;
		this.seqno = seqno;
		this.createdate = createdate;
	}

	/** minimal constructor */
	public SysDatascopesub(Long scopesubid, Long scopemainid,
			String scopesubname, Integer seqno) {
		this.scopesubid = scopesubid;
		this.scopemainid = scopemainid;
		this.scopesubname = scopesubname;
		this.seqno = seqno;
	}

	/** full constructor */
	public SysDatascopesub(Long scopesubid, Long scopemainid,
			String scopesubname, Integer seqno, String createdate, String remark) {
		this.scopesubid = scopesubid;
		this.scopemainid = scopemainid;
		this.scopesubname = scopesubname;
		this.seqno = seqno;
		this.createdate = createdate;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DATASCOPESUB")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SCOPESUBID", nullable = false, scale = 0)
	public Long getScopesubid() {
		return this.scopesubid;
	}

	public void setScopesubid(Long scopesubid) {
		this.scopesubid = scopesubid;
	}

	@Column(name = "SCOPEMAINID", nullable = false, scale = 0)
	public Long getScopemainid() {
		return this.scopemainid;
	}

	public void setScopemainid(Long scopemainid) {
		this.scopemainid = scopemainid;
	}

	@Column(name = "SCOPESUBNAME", nullable = false, length = 100)
	public String getScopesubname() {
		return this.scopesubname;
	}

	public void setScopesubname(String scopesubname) {
		this.scopesubname = scopesubname;
	}

	@Column(name = "SEQNO", nullable = false, precision = 9, scale = 0)
	public Integer getSeqno() {
		return this.seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

	@Column(name = "CREATEDATE", length = 19)
	public String getCreatedate() {
		return this.createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
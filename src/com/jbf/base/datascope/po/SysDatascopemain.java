/************************************************************
 * 类名：SysDatascopemain.java
 *
 * 类别：PO
 * 功能：数据权限主PO
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
 * SysDatascopemain entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DATASCOPEMAIN")
public class SysDatascopemain implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long scopemainid;
	private String scopemainname;
	private Long grpscopemain;
	private String type;
	private String createdate;
	private String remark;

	// Constructors

	/** default constructor */
	public SysDatascopemain() {
	}

	/** minimal constructor */
	public SysDatascopemain(Long scopemainid, String scopemainname) {
		this.scopemainid = scopemainid;
		this.scopemainname = scopemainname;
	}
	
	/** minimal constructor */
	public SysDatascopemain(String scopemainname, String type, String createdate) {
		this.scopemainname = scopemainname;
		this.type = type;
		this.createdate = createdate;
	}

	/** full constructor */
	public SysDatascopemain(Long scopemainid, String scopemainname,
			Long grpscopemain, String type, String createdate, String remark) {
		this.scopemainid = scopemainid;
		this.scopemainname = scopemainname;
		this.grpscopemain = grpscopemain;
		this.type = type;
		this.createdate = createdate;
		this.remark = remark;
	}

	@Id    
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DATASCOPEMAIN")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SCOPEMAINID", nullable = false, scale = 0)
	public Long getScopemainid() {
		return this.scopemainid;
	}

	public void setScopemainid(Long scopemainid) {
		this.scopemainid = scopemainid;
	}

	@Column(name = "SCOPEMAINNAME", nullable = false, length = 100)
	public String getScopemainname() {
		return this.scopemainname;
	}

	public void setScopemainname(String scopemainname) {
		this.scopemainname = scopemainname;
	}

	@Column(name = "GRPSCOPEMAIN", scale = 0)
	public Long getGrpscopemain() {
		return this.grpscopemain;
	}

	public void setGrpscopemain(Long grpscopemain) {
		this.grpscopemain = grpscopemain;
	}
	
	@Column(name = "TYPE", nullable = false, length = 20)
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
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
/************************************************************
 * 类名：SysUserRole.java
 *
 * 类别：PO
 * 功能：用户角色PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.user.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SysUserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USER_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"USERID", "ROLEID" }))
public class SysUserRole implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private Long relationid;
	private Long userid;
	private Long roleid;

	// Constructors

	/** default constructor */
	public SysUserRole() {
	}

	/** full constructor */
	public SysUserRole(Long userid, Long roleid) {
		this.userid = userid;
		this.roleid = roleid;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_USER_ROLE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "RELATIONID", nullable = false, scale = 0)
	public Long getRelationid() {
		return this.relationid;
	}

	public void setRelationid(Long relationid) {
		this.relationid = relationid;
	}

	@Column(name = "USERID", nullable = false, length = 19)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "ROLEID", nullable = false, length = 19)
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

}
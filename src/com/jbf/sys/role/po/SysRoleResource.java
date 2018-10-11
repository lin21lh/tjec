/************************************************************
 * 类名：SysRoleResource.java
 *
 * 类别：PO
 * 功能：角色对应的资源PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.role.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SysRMRelation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE_RESOURCE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ROLEID", "RESOURCEID" }))
public class SysRoleResource implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 3421092209028816666L;
	private Long relationid;
	private Long roleid;
	private Long resourceid;

	// Constructors

	/** default constructor */
	public SysRoleResource() {
	}

	/** full constructor */
	public SysRoleResource(Long roleid, Long resourceid) {
		this.roleid = roleid;
		this.resourceid = resourceid;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_ROLE_RESOURCE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "RELATIONID", nullable = false, scale = 0)
	public Long getRelationid() {
		return this.relationid;
	}

	public void setRelationid(Long relationid) {
		this.relationid = relationid;
	}

	@Column(name = "ROLEID", nullable = false, length = 19, scale = 0)
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	@Column(name = "RESOURCEID", nullable = false, length = 19, scale = 0)
	public Long getResourceid() {
		return this.resourceid;
	}

	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}

}
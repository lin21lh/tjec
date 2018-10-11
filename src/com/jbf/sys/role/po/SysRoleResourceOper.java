/************************************************************
 * 类名：SysRoleResourceOper.java
 *
 * 类别：PO
 * 功能：角色资源对应操作PO
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

import org.hibernate.annotations.Formula;

/**
 * SysRoleResourceOper entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE_RESOURCE_OPER")
public class SysRoleResourceOper implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -205562133702107427L;
	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_ROLE_RESOURCE_OPER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	private Long id;
	@Column(name = "ROLEID", scale = 0)
	private Long roleid;
	@Column(name = "RESOURCEID", scale = 0)
	private Long resourceid;
	@Column(name = "RESOPERID", scale = 0)
	private Long resoperid;

	@Formula("(select op.name from sys_resource_oper op where op.id =resoperid)")
	private String opername;

	// Constructors

	/** default constructor */
	public SysRoleResourceOper() {
	}

	/** full constructor */
	public SysRoleResourceOper(Long roleid, Long resourceid, Long resoperid) {
		this.roleid = roleid;
		this.resourceid = resourceid;
		this.resoperid = resoperid;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public Long getResourceid() {
		return this.resourceid;
	}

	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}

	public Long getResoperid() {
		return resoperid;
	}

	public void setResoperid(Long resoperid) {
		this.resoperid = resoperid;
	}

	public String getOpername() {
		return opername;
	}

	public void setOpername(String opername) {
		this.opername = opername;
	}

}
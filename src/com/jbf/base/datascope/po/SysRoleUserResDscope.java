/************************************************************
 * 类名：SysRoleUserResDscope.java
 *
 * 类别：PO
 * 功能：角色、用户、功能菜单和数据权限关联关系PO
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

import org.hibernate.annotations.Formula;

/**
 * SysRoleUserResDscope entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE_USER_RES_DSCOPE")
public class SysRoleUserResDscope implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long relationid;
	private Long roleid;
	private Long userid;
	private Long resourceid;
	private Long scopemainid;
	private Byte isalluser;
	private Byte isallmenu;
	private String createdate;
	private String remark;
	private String scopemainname;

	// Constructors

	/** default constructor */
	public SysRoleUserResDscope() {
	}

	/** minimal constructor */
	public SysRoleUserResDscope(Long relationid, Long roleid,
			Long scopemainid, Byte isalluser, Byte isallmenu) {
		this.relationid = relationid;
		this.roleid = roleid;
		this.scopemainid = scopemainid;
		this.isalluser = isalluser;
		this.isallmenu = isallmenu;
	}

	/** full constructor */
	public SysRoleUserResDscope(Long relationid, Long roleid,
			Long userid, Long resourceid, Long scopemainid,
			Byte isalluser, Byte isallmenu, String createdate, String remark) {
		this.relationid = relationid;
		this.roleid = roleid;
		this.userid = userid;
		this.resourceid = resourceid;
		this.scopemainid = scopemainid;
		this.isalluser = isalluser;
		this.isallmenu = isallmenu;
		this.createdate = createdate;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_ROLE_USER_RES_DSCOPE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "RELATIONID", nullable = false, scale = 0)
	public Long getRelationid() {
		return this.relationid;
	}

	public void setRelationid(Long relationid) {
		this.relationid = relationid;
	}

	@Column(name = "ROLEID", nullable = false, scale = 0)
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	@Column(name = "USERID", scale = 0)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "resourceid", scale = 0)
	public Long getResourceid() {
		return this.resourceid;
	}

	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}

	@Column(name = "SCOPEMAINID", nullable = false, scale = 0)
	public Long getScopemainid() {
		return this.scopemainid;
	}

	public void setScopemainid(Long scopemainid) {
		this.scopemainid = scopemainid;
	}

	@Column(name = "ISALLUSER", nullable = false, precision = 2, scale = 0)
	public Byte getIsalluser() {
		return this.isalluser;
	}

	public void setIsalluser(Byte isalluser) {
		this.isalluser = isalluser;
	}

	@Column(name = "ISALLMENU", nullable = false, precision = 2, scale = 0)
	public Byte getIsallmenu() {
		return this.isallmenu;
	}

	public void setIsallmenu(Byte isallmenu) {
		this.isallmenu = isallmenu;
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
	
	@Formula("(SELECT T.SCOPEMAINNAME FROM SYS_DATASCOPEMAIN T WHERE T.SCOPEMAINID=scopemainid)")
	public String getScopemainname() {
		return scopemainname;
	}
	
	public void setScopemainname(String scopemainname) {
		this.scopemainname = scopemainname;
	}

}
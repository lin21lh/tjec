/************************************************************
 * 类名：SysRole.java
 *
 * 类别：PO
 * 功能：角色PO
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

import org.hibernate.annotations.Formula;

/**
 * SysRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = "ROLECODE"))
public class SysRole implements java.io.Serializable {

	
	private static final long serialVersionUID = 1327808353097979829L;
	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_ROLE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ROLEID", nullable = false, scale = 0)
	private Long roleid;

	@Column(name = "ROLECODE", unique = true, nullable = false, length = 50)
	private String rolecode;
	@Column(name = "ROLENAME", nullable = false, length = 50)
	private String rolename;

	@Column(name = "GRPCODE", nullable = true, length = 50)
	private String grpcode;

	@Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
	private Integer status;

	@Column(name = "STARTDATE", length = 20)
	private String startdate;

	@Column(name = "ENDDATE", length = 20)
	private String enddate;

	@Column(name = "REMARK", length = 100)
	private String remark;

	@Column(name = "PARENTROLEID", nullable = false, scale = 0)
	private Long parentroleid;

	@Column(name = "LEVELNO", nullable = false, precision = 2, scale = 0)
	private Integer levelno;

	@Column(name = "ISLEAF", nullable = true, precision = 2, scale = 0)
	private Integer isleaf;

	@Column(name = "CREATER", length = 20)
	private String creater;

	@Formula("( select u.username from sys_user u where u.usercode= creater )")
	private String cncreater;

	// Constructors

	/** default constructor */
	public SysRole() {
	}

	/** minimal constructor */
	public SysRole(String rolecode, String rolename, String grpcode,
			Integer status, String startdate) {
		this.rolecode = rolecode;
		this.rolename = rolename;
		this.grpcode = grpcode;
		this.status = status;
		this.startdate = startdate;
	}

	/** full constructor */
	public SysRole(String rolecode, String rolename, String grpcode,
			Integer status, String startdate, String enddate, String remark) {
		this.rolecode = rolecode;
		this.rolename = rolename;
		this.grpcode = grpcode;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.remark = remark;
	}

	// Property accessors

	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public String getRolecode() {
		return this.rolecode;
	}

	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getGrpcode() {
		return this.grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getParentroleid() {
		return parentroleid;
	}

	public void setParentroleid(Long parentroleid) {
		this.parentroleid = parentroleid;
	}

	public Integer getLevelno() {
		return levelno;
	}

	public void setLevelno(Integer levelno) {
		this.levelno = levelno;
	}

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCncreater() {
		return cncreater;
	}

	public void setCncreater(String cncreater) {
		this.cncreater = cncreater;
	}

}
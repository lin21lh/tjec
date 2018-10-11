/************************************************************
 * 类名：SysUser.java
 *
 * 类别：PO
 * 功能：用户PO
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

import org.hibernate.annotations.Formula;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USER", uniqueConstraints = @UniqueConstraint(columnNames = "USERCODE"))
public class SysUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6992085147133726281L;
	private Long userid;
	private String usercode;
	private String username;
	private String userpswd;
	private Byte usertype;
	private String orgcode;
	private String grpcode;
	private Byte status = 0;
	private String createtime;
	private String overduedate;
	private Byte isca = 0;
	private String remark;
	private String createuser;
	private String updatetime;
	private String modifyuser;
	private String modifytime;

	private String orgname;
	private String cnstatus;
	private String area;//区域
	private String areamc;//区域名称
	@Formula("(select c.name from sys_dicenumitem c where c.elementcode='SYS_AREA' and c.code=(select a.area from sys_dept a where a.code = orgcode))")
	public String getAreamc() {
		return areamc;
	}
	public void setAreamc(String areamc) {
		this.areamc = areamc;
	}
	@Formula("(select a.area from sys_dept a where a.code = orgcode)")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_USER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "USERID", nullable = false, scale = 0)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "USERCODE", unique = true, nullable = false, length = 50)
	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	@Column(name = "USERNAME", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "USERPSWD", nullable = false, length = 100)
	public String getUserpswd() {
		return this.userpswd;
	}

	public void setUserpswd(String userpswd) {
		this.userpswd = userpswd;
	}

	@Column(name = "USERTYPE", nullable = false, precision = 2, scale = 0)
	public Byte getUsertype() {
		return this.usertype;
	}

	public void setUsertype(Byte usertype) {
		this.usertype = usertype;
	}

	@Column(name = "ORGCODE", length = 50)
	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	@Column(name = "GRPCODE", length = 50)
	public String getGrpcode() {
		return this.grpcode;
	}

	public void setGrpcode(String grpcode) {
		this.grpcode = grpcode;
	}

	@Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "CREATETIME", nullable = false, length = 19)
	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Column(name = "OVERDUEDATE", length = 19)
	public String getOverduedate() {
		return this.overduedate;
	}

	public void setOverduedate(String overduedate) {
		this.overduedate = overduedate;
	}

	@Column(name = "ISCA", nullable = false, precision = 2, scale = 0)
	public Byte getIsca() {
		return this.isca;
	}

	public void setIsca(Byte isca) {
		this.isca = isca;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATEUSER")
	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Column(name = "UPDATETIME", length = 19)
	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "MODIFYUSER")
	public String getModifyuser() {
		return modifyuser;
	}

	public void setModifyuser(String modifyuser) {
		this.modifyuser = modifyuser;
	}

	@Column(name = "MODIFYTIME", length = 19)
	public String getModifytime() {
		return modifytime;
	}

	public void setModifytime(String modifytime) {
		this.modifytime = modifytime;
	}

	@Formula("(select t.name from SYS_DEPT t  where t.code=orgcode)")
	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='SYS_STATUS' and t.status=0  and t.code=status)")
	public String getCnstatus() {
		return cnstatus;
	}

	public void setCnstatus(String cnstatus) {
		this.cnstatus = cnstatus;
	}

}
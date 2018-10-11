/************************************************************
 * 类名：SysResourceOperController.java
 *
 * 类别：PO
 * 功能：资源PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * SysResource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_RESOURCE")
@SequenceGenerator(name="SEQ_RESOURCE",sequenceName="SEQ_RESOURCE")
@GenericGenerator(name = "SEQ_RESOURCE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_RESOURCE") })
public class SysResource implements java.io.Serializable {

	// Fields

	private Long resourceid;
	private Long parentresid;
	private String name;
	private String webpath;
	private Long vchtypeid;
	private Byte stepdescid;
	private Byte isleaf;
	private Byte levelno;
	private Byte status;
	private String remark;
	private String createtime;
	private Integer remindtype;
	private Byte datascopemode;
	private String iconCls;
	private Byte restype;
	private String wfkey;
	private String wfkeyname;
	private Integer wfversion;
	private String activityid;
	private String activityname;
	private Integer wftasknode;
	private String toRemindWhere;

	private Integer resorder;

	// Constructors

	/** default constructor */
	public SysResource() {
	}

	/** minimal constructor */
	public SysResource(Long resourceid, Long parentresid, String name,
			String webpath, Byte isleaf, Byte levelno, Byte status,
			String createtime, Integer remindtype, String wfkey, String wfkeyname,
			Integer wfversion, String activityid, Integer wftasknode, String toRemindWhere) {
		this.resourceid = resourceid;
		this.parentresid = parentresid;
		this.name = name;
		this.webpath = webpath;
		this.isleaf = isleaf;
		this.levelno = levelno;
		this.status = status;
		this.createtime = createtime;
		this.remindtype = remindtype;
		this.wfkey = wfkey;
		this.wfkeyname = wfkeyname;
		this.wfversion = wfversion;
		this.activityid = activityid;
		this.wftasknode = wftasknode;
		this.toRemindWhere = toRemindWhere;
	}

	// Property accessors
	@Id
	@Column(name = "RESOURCEID", nullable = false, scale = 0)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_RESOURCE")
	public Long getResourceid() {
		return this.resourceid;
	}

	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}

	@Column(name = "PARENTRESID", nullable = false, scale = 0)
	public Long getParentresid() {
		return this.parentresid;
	}

	public void setParentresid(Long parentresid) {
		this.parentresid = parentresid;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "WEBPATH", nullable = true, length = 100)
	public String getWebpath() {
		return this.webpath;
	}

	public void setWebpath(String webpath) {
		this.webpath = webpath;
	}

	@Column(name = "VCHTYPEID", scale = 0)
	public Long getVchtypeid() {
		return this.vchtypeid;
	}

	public void setVchtypeid(Long vchtypeid) {
		this.vchtypeid = vchtypeid;
	}

	@Column(name = "STEPDESCID", precision = 2, scale = 0)
	public Byte getStepdescid() {
		return this.stepdescid;
	}

	public void setStepdescid(Byte stepdescid) {
		this.stepdescid = stepdescid;
	}

	@Column(name = "ISLEAF", nullable = true, precision = 2, scale = 0)
	public Byte getIsleaf() {
		return this.isleaf;
	}

	public void setIsleaf(Byte isleaf) {
		this.isleaf = isleaf;
	}

	@Column(name = "LEVELNO", nullable = true, precision = 2, scale = 0)
	public Byte getLevelno() {
		return this.levelno;
	}

	public void setLevelno(Byte levelno) {
		this.levelno = levelno;
	}

	@Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATETIME", nullable = false, length = 10)
	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Column(name = "REMINDTYPE", precision = 2, scale = 0)
	public Integer getRemindtype() {
		return this.remindtype;
	}

	public void setRemindtype(Integer remindtype) {
		this.remindtype = remindtype;
	}

	@Column(name = "DATASCOPEMODE", nullable = true, precision = 2, scale = 0)
	public Byte getDatascopemode() {
		return datascopemode;
	}

	public void setDatascopemode(Byte datascopemode) {
		this.datascopemode = datascopemode;
	}

	@Column(name = "ICONCLS", nullable = true)
	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column(name = "RESORDER", nullable = false)
	public Integer getResorder() {
		return resorder;
	}

	public void setResorder(Integer resorder) {
		this.resorder = resorder;
	}

	@Column(name = "RESTYPE", nullable = false)
	public Byte getRestype() {
		return restype;
	}

	public void setRestype(Byte restype) {
		this.restype = restype;
	}

	@Column(name = "WFKEY", nullable = true)
	public String getWfkey() {
		return wfkey;
	}

	public void setWfkey(String wfkey) {
		this.wfkey = wfkey;
	}
	
	@Formula("(select t.name from sys_workflow_procdef t where t.key=wfkey)")
	public String getWfkeyname() {
		return wfkeyname;
	}
	
	public void setWfkeyname(String wfkeyname) {
		this.wfkeyname = wfkeyname;
	}
	
	@Column(name = "WFVERSION", nullable = true)
	public Integer getWfversion() {
		return wfversion;
	}
	
	public void setWfversion(Integer wfversion) {
		this.wfversion = wfversion;
	}
	
	@Column(name = "ACTIVITYID", nullable = true, length=50)
	public String getActivityid() {
		return activityid;
	}
	
	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}
	
	@Transient
	public String getActivityname() {
		return activityname;
	}
	
	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}
	
	@Column(name = "WFTASKNODE", nullable = true)
	public Integer getWftasknode() {
		return wftasknode;
	}
	
	public void setWftasknode(Integer wftasknode) {
		this.wftasknode = wftasknode;
	}
	
	@Column(name = "TOREMINDWHERE", nullable = true, length=200)
	public String getToRemindWhere() {
		return toRemindWhere;
	}
	
	public void setToRemindWhere(String toRemindWhere) {
		this.toRemindWhere = toRemindWhere;
	}
}
/************************************************************
 * 类名：SysResourceOperController.java
 *
 * 类别：PO
 * 功能：资源操作PO
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
import org.hibernate.annotations.Formula;

/**
 * SysMenuOper entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_RESOURCE_OPER")
public class SysResourceOper implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5928689129073426199L;

	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_RESOURCE_OPER")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	private Long id;

	@Column(name = "RESOURCEID", scale = 0)
	private Long resourceid;
	@Column(name = "NAME", length = 50)
	private String name;

	@Column(name = "CODE", length = 50)
	private String code;

	@Column(name = "OPERMODE")
	private Byte opermode;

	@Formula("( select d.name from sys_dicenumitem d where d.code=opermode and d.elementcode='SYS_DATASCOPE_PRIORITY_MODE' )")
	private String cnopermode;

	@Column(name = "ICONCLS", length = 20)
	private String iconcls;

	@Column(name = "REMARK", length = 50)
	private String remark;

	@Column(name = "POSITION", length = 50)
	private String position;

	// Constructors

	/** default constructor */
	public SysResourceOper() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceid() {
		return resourceid;
	}

	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Byte getOpermode() {
		return opermode;
	}

	public void setOpermode(Byte opermode) {
		this.opermode = opermode;
	}

	public String getIconcls() {
		return iconcls;
	}

	public void setIconcls(String iconcls) {
		this.iconcls = iconcls;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCnopermode() {
		return cnopermode;
	}

	public void setCnopermode(String cnopermode) {
		this.cnopermode = cnopermode;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
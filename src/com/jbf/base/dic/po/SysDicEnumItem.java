/************************************************************
 * 类名：SysDicEnumItem.java
 *
 * 类别：PO
 * 功能：系统预设枚举项PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SysDicEnumItem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DicEnumItem", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ELEMENTCODE", "CODE" }))
public class SysDicEnumItem implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8323003452756597670L;
	
	// Fields
	private Long itemid;
	private String code;
	private String name;
	private String elementcode;
	private String shortname;
	private String wholename;
	private Byte status;
	private String startdate;
	private String enddate;
	private String remark;

	// Constructors

	/** default constructor */
	public SysDicEnumItem() {
	}

	/** minimal constructor */
	public SysDicEnumItem(String code, String name, String elementcode,
			String shortname, String wholename, Byte status, String startdate) {
		this.code = code;
		this.name = name;
		this.elementcode = elementcode;
		this.shortname = shortname;
		this.wholename = wholename;
		this.status = status;
		this.startdate = startdate;
	}

	/** full constructor */
	public SysDicEnumItem(String code, String name, String elementcode,
			String shortname, String wholename, Byte status, String startdate,
			String enddate, String remark) {
		this.code = code;
		this.name = name;
		this.elementcode = elementcode;
		this.shortname = shortname;
		this.wholename = wholename;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DICENUMITEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ITEMID", nullable = false, scale = 0)
	public Long getItemid() {
		return this.itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ELEMENTCODE", nullable = false, length = 50)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}

	@Column(name = "SHORTNAME", nullable = true, length = 50)
	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@Column(name = "WHOLENAME", nullable = true, length = 100)
	public String getWholename() {
		return this.wholename;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

	@Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "STARTDATE", nullable = false, length = 19)
	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	@Column(name = "ENDDATE", length = 19)
	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
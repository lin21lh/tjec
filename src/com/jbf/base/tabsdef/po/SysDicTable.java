/************************************************************
 * 类名：SysDicTable.java
 *
 * 类别：PO
 * 功能：数据表 PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   zcz         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

import com.jbf.base.po.DBVersionPO;

/**
 * SysDicTable entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DICTABLE", uniqueConstraints = @UniqueConstraint(columnNames = "TABLECODE"))
public class SysDicTable extends DBVersionPO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_DICTABLE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "TABLEID", nullable = false, scale = 0)
	private Long tableid;

	@Column(name = "TABLECODE", unique = true, nullable = false, length = 50)
	private String tablecode;

	@Column(name = "TABLENAME", nullable = false, length = 50)
	private String tablename;

	@Column(name = "TABLETYPE", nullable = false, precision = 2, scale = 0)
	private Byte tabletype;

	@Formula("(select d.name from sys_dicenumitem d where d.elementcode='SYS_TABLE_TYPE' and d.code= tabletype)")
	private String cntabletype;

	@Column(name = "ISCACHE", nullable = false, precision = 2, scale = 0)
	private Byte iscache = 0;

	@Column(name = "KEYCOLUMN", length = 50)
	private String keycolumn;

	@Column(name = "CODECOLUMN", length = 50)
	private String codecolumn;

	@Column(name = "NAMECOLUMN", length = 50)
	private String namecolumn;

	@Column(name = "SUPERCOLUMN", length = 50)
	private String supercolumn;

	@Column(name = "LEVELNOCOLUMN", length = 50)
	private String levelnocolumn;

	@Column(name = "ISLEAFCOLUMN", length = 50)
	private String isleafcolumn;

	@Column(name = "STATUSCOLUMN", length = 50)
	private String statuscolumn;

	@Column(name = "CREATEUSER", length = 50)
	private String createuser;

	@Column(name = "CREATETIME", length = 7)
	private String createtime;

	@Column(name = "REMARK", length = 20)
	private String remark;

	@Column(name = "SYSTEMPRETAG")
	private Byte systempretag = 0;

	// Constructors

	/** default constructor */
	public SysDicTable() {
	}

	public Long getTableid() {
		return this.tableid;
	}

	public void setTableid(Long tableid) {
		this.tableid = tableid;
	}

	public String getTablecode() {
		return this.tablecode;
	}

	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}

	public String getTablename() {
		return this.tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public Byte getTabletype() {
		return this.tabletype;
	}

	public void setTabletype(Byte tabletype) {
		this.tabletype = tabletype;
	}

	public Byte getIscache() {
		return this.iscache;
	}

	public void setIscache(Byte iscache) {
		this.iscache = iscache;
	}

	public String getKeycolumn() {
		return this.keycolumn;
	}

	public void setKeycolumn(String keycolumn) {
		this.keycolumn = keycolumn;
	}

	public String getCodecolumn() {
		return this.codecolumn;
	}

	public void setCodecolumn(String codecolumn) {
		this.codecolumn = codecolumn;
	}

	public String getNamecolumn() {
		return this.namecolumn;
	}

	public void setNamecolumn(String namecolumn) {
		this.namecolumn = namecolumn;
	}

	public String getSupercolumn() {
		return this.supercolumn;
	}

	public void setSupercolumn(String supercolumn) {
		this.supercolumn = supercolumn;
	}

	public String getLevelnocolumn() {
		return this.levelnocolumn;
	}

	public void setLevelnocolumn(String levelnocolumn) {
		this.levelnocolumn = levelnocolumn;
	}

	public String getIsleafcolumn() {
		return this.isleafcolumn;
	}

	public void setIsleafcolumn(String isleafcolumn) {
		this.isleafcolumn = isleafcolumn;
	}

	public String getStatuscolumn() {
		return this.statuscolumn;
	}

	public void setStatuscolumn(String statuscolumn) {
		this.statuscolumn = statuscolumn;
	}

	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCntabletype() {
		return cntabletype;
	}

	public void setCntabletype(String cntabletype) {
		this.cntabletype = cntabletype;
	}

	public Byte getSystempretag() {
		return systempretag;
	}

	public void setSystempretag(Byte systempretag) {
		this.systempretag = systempretag;
	}

}
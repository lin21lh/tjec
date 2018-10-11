/************************************************************
 * 类名：SysDicColumn.java
 *
 * 类别：PO
 * 功能：数据表列PO
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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;

/**
 * SysDiccolumn entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DICCOLUMN", uniqueConstraints = @UniqueConstraint(columnNames = {
		"TABLECODE", "COLUMNCODE" }))
public class SysDicColumn implements java.io.Serializable {

	// Fields
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_DICCOLUMN")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "COLUMNID", nullable = false, scale = 0)
	private Long columnid;

	@Column(name = "TABLECODE", nullable = false, length = 50)
	private String tablecode;

	@Column(name = "COLUMNCODE", nullable = false, length = 50)
	private String columncode;

	@Column(name = "COLUMNNAME", nullable = false, length = 50)
	private String columnname;

	@Column(name = "COLUMNTYPE", nullable = false, length = 2)
	private String columntype;

	@Column(name = "COLUMNLENGTH", precision = 3, scale = 0)
	private Short columnlength;

	@Column(name = "SCALELENGTH", precision = 2, scale = 0)
	private Byte scalelength;

	@Column(name = "SOURCEELEMENTCODE", length = 50)
	private String sourceelementcode;

	@Formula("(select de.elementname from sys_dicelement de where de.elementcode=sourceelementcode )")
	private String cnsourceelementcode;

	@Column(name = "SOURCETABLE", length = 50)
	private String sourcetable;

	@Column(name = "NULLABLE", nullable = false, precision = 2, scale = 0)
	private Byte nullable;

	@Column(name = "STARTMARK", nullable = false, precision = 2, scale = 0)
	private Byte startmark;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "SYSTEMPRETAG", precision = 2, scale = 0)
	private Byte systempretag;

	@Column(name = "ISNAMECOL", precision = 2, scale = 0)
	private Byte isnamecol;

	@Column(name = "ISKEYCOL", precision = 2, scale = 0)
	private Byte iskeycol;

	@Column(name = "ISPKEYCOL", precision = 2, scale = 0)
	private Byte ispkeycol;

	@Column(name = "ISLEVELNOCOL", precision = 2, scale = 0)
	private Byte islevelnocol;

	@Column(name = "ISLEAFCOL", precision = 2, scale = 0)
	private Byte isleafcol;

	@Column(name = "ISUNQCOL", precision = 2, scale = 0)
	private Byte isunqcol;

	@Column(name = "ISCODECOL", precision = 2, scale = 0)
	private Byte iscodecol;

	// 是否配置与数据表表字段不一致
	@Transient
	private Byte disagree;

	// Constructors

	/** default constructor */
	public SysDicColumn() {
	}

	/** minimal constructor */
	public SysDicColumn(Long columnid, String tablecode, String columncode,
			String columnname, String columntype, Byte nullable, Byte startmark) {
		this.columnid = columnid;
		this.tablecode = tablecode;
		this.columncode = columncode;
		this.columnname = columnname;
		this.columntype = columntype;
		this.nullable = nullable;
		this.startmark = startmark;
	}

	/** full constructor */
	public SysDicColumn(Long columnid, String tablecode, String columncode,
			String columnname, String columntype, Short columnlength,
			Byte scalelength, String sourceelementcode, String sourcetable,
			Byte nullable, Byte startmark, String remark, Byte systempretag,
			Byte isnamecol, Byte iskeycol, Byte ispkeycol, Byte islevelnocol,
			Byte isleafcol, Byte isunqcol) {
		this.columnid = columnid;
		this.tablecode = tablecode;
		this.columncode = columncode;
		this.columnname = columnname;
		this.columntype = columntype;
		this.columnlength = columnlength;
		this.scalelength = scalelength;
		this.sourceelementcode = sourceelementcode;
		this.sourcetable = sourcetable;
		this.nullable = nullable;
		this.startmark = startmark;
		this.remark = remark;
		this.systempretag = systempretag;
		this.isnamecol = isnamecol;
		this.iskeycol = iskeycol;
		this.ispkeycol = ispkeycol;
		this.islevelnocol = islevelnocol;
		this.isleafcol = isleafcol;
		this.isunqcol = isunqcol;
	}

	// Property accessors

	public Long getColumnid() {
		return this.columnid;
	}

	public void setColumnid(Long columnid) {
		this.columnid = columnid;
	}

	public String getTablecode() {
		return this.tablecode;
	}

	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}

	public String getColumncode() {
		return this.columncode;
	}

	public void setColumncode(String columncode) {
		this.columncode = columncode;
	}

	public String getColumnname() {
		return this.columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumntype() {
		return this.columntype;
	}

	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}

	public Short getColumnlength() {
		return this.columnlength;
	}

	public void setColumnlength(Short columnlength) {
		this.columnlength = columnlength;
	}

	public Byte getScalelength() {
		return this.scalelength;
	}

	public void setScalelength(Byte scalelength) {
		this.scalelength = scalelength;
	}

	public String getSourceelementcode() {
		return this.sourceelementcode;
	}

	public void setSourceelementcode(String sourceelementcode) {
		this.sourceelementcode = sourceelementcode;
	}

	public String getSourcetable() {
		return this.sourcetable;
	}

	public void setSourcetable(String sourcetable) {
		this.sourcetable = sourcetable;
	}

	public Byte getNullable() {
		return this.nullable;
	}

	public void setNullable(Byte nullable) {
		this.nullable = nullable;
	}

	public Byte getStartmark() {
		return this.startmark;
	}

	public void setStartmark(Byte startmark) {
		this.startmark = startmark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getSystempretag() {
		return this.systempretag;
	}

	public void setSystempretag(Byte systempretag) {
		this.systempretag = systempretag;
	}

	public Byte getIsnamecol() {
		return this.isnamecol;
	}

	public void setIsnamecol(Byte isnamecol) {
		this.isnamecol = isnamecol;
	}

	public Byte getIskeycol() {
		return this.iskeycol;
	}

	public void setIskeycol(Byte iskeycol) {
		this.iskeycol = iskeycol;
	}

	public Byte getIspkeycol() {
		return this.ispkeycol;
	}

	public void setIspkeycol(Byte ispkeycol) {
		this.ispkeycol = ispkeycol;
	}

	public Byte getIslevelnocol() {
		return islevelnocol;
	}

	public void setIslevelnocol(Byte islevelnocol) {
		this.islevelnocol = islevelnocol;
	}

	public Byte getIsleafcol() {
		return this.isleafcol;
	}

	public void setIsleafcol(Byte isleafcol) {
		this.isleafcol = isleafcol;
	}

	public Byte getIsunqcol() {
		return this.isunqcol;
	}

	public void setIsunqcol(Byte isunqcol) {
		this.isunqcol = isunqcol;
	}

	public Byte getDisagree() {
		return disagree;
	}

	public void setDisagree(Byte disagree) {
		this.disagree = disagree;
	}

	public String getCnsourceelementcode() {
		return cnsourceelementcode;
	}

	public void setCnsourceelementcode(String cnsourceelementcode) {
		this.cnsourceelementcode = cnsourceelementcode;
	}

	public Byte getIscodecol() {
		return iscodecol;
	}

	public void setIscodecol(Byte iscodecol) {
		this.iscodecol = iscodecol;
	}

}
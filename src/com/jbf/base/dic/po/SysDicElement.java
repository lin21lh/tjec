/************************************************************
 * 类名：SysDicElement.java
 *
 * 类别：PO
 * 功能：数据项PO
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

import org.hibernate.annotations.Formula;

/**
 * SysDicElement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DicElement", uniqueConstraints = @UniqueConstraint(columnNames = "ELEMENTCODE"))
public class SysDicElement implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5941643934165851227L;
	// Fields
	private Long elementid;
	private String elementcode;
	private String elementname;
	private String tablecode;
	private Byte elementtype;
	private String sourceelement;
	private Long elementclass;
	private Byte codetype;
	private String codeformat;
	private Byte iselements;
	private Byte systempretag;
	private String datatype;
	private Byte isedit;
	private Byte status;
	private String startdate;
	private String enddate;
	private String remark;
	
	private String cnelementclass;
	
	private String tablename;

	// Constructors

	/** default constructor */
	public SysDicElement() {
	}

	/** minimal constructor */
	public SysDicElement(String elementcode, String elementname, String tablecode,
			Byte elementtype) {
		this.elementcode = elementcode;
		this.elementname = elementname;
		this.tablecode = tablecode;
		this.elementtype = elementtype;
	}

	/** full constructor */
	public SysDicElement(String elementcode, String elementname, String tablecode,
			Byte elementtype, String sourceelement, Long elementclass,
			Byte codetype, String codeformat, Byte iselements,
			Byte systempretag, String datatype, Byte isedit, Byte status,
			String startdate, String enddate, String remark) {
		this.elementcode = elementcode;
		this.elementname = elementname;
		this.tablecode = tablecode;
		this.elementtype = elementtype;
		this.sourceelement = sourceelement;
		this.elementclass = elementclass;
		this.codetype = codetype;
		this.codeformat = codeformat;
		this.iselements = iselements;
		this.systempretag = systempretag;
		this.datatype = datatype;
		this.isedit = isedit;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DICELEMENT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ELEMENTID", nullable = false, scale = 0)
	public Long getElementid() {
		return this.elementid;
	}

	public void setElementid(Long elementid) {
		this.elementid = elementid;
	}

	@Column(name = "ELEMENTCODE", unique = true, nullable = false, length = 50)
	public String getElementcode() {
		return this.elementcode != null ? this.elementcode.toUpperCase() : this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode != null ? elementcode.toUpperCase() : elementcode;
	}

	@Column(name = "ELEMENTNAME", nullable = false, length = 50)
	public String getElementname() {
		return this.elementname;
	}

	public void setElementname(String elementname) {
		this.elementname = elementname;
	}

	@Column(name = "TABLECODE", nullable = true, length = 50)
	public String getTablecode() {
		return this.tablecode;
	}

	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}

	@Column(name = "ELEMENTTYPE", nullable = false, precision = 2, scale = 0)
	public Byte getElementtype() {
		return this.elementtype;
	}

	public void setElementtype(Byte elementtype) {
		this.elementtype = elementtype;
	}

	@Column(name = "SOURCEELEMENT", length = 50)
	public String getSourceelement() {
		return this.sourceelement != null ? this.sourceelement.toUpperCase() : this.sourceelement;
	}

	public void setSourceelement(String sourceelement) {
		this.sourceelement = (sourceelement != null ? sourceelement.toUpperCase() : sourceelement);
	}

	@Column(name = "ELEMENTCLASS", scale = 0)
	public Long getElementclass() {
		return this.elementclass;
	}

	public void setElementclass(Long elementclass) {
		this.elementclass = elementclass;
	}

	@Column(name = "CODETYPE", precision = 2, scale = 0)
	public Byte getCodetype() {
		return this.codetype;
	}

	public void setCodetype(Byte codetype) {
		this.codetype = codetype;
	}

	@Column(name = "CODEFORMAT", length = 50)
	public String getCodeformat() {
		return this.codeformat;
	}

	public void setCodeformat(String codeformat) {
		this.codeformat = codeformat;
	}

	@Column(name = "ISELEMENTS", precision = 2, scale = 0)
	public Byte getIselements() {
		return this.iselements;
	}

	public void setIselements(Byte iselements) {
		this.iselements = iselements;
	}

	@Column(name = "SYSTEMPRETAG", precision = 2, scale = 0)
	public Byte getSystempretag() {
		return this.systempretag;
	}

	public void setSystempretag(Byte systempretag) {
		this.systempretag = systempretag;
	}

	@Column(name = "DATATYPE", length = 2)
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "ISEDIT", precision = 2, scale = 0)
	public Byte getIsedit() {
		return this.isedit;
	}

	public void setIsedit(Byte isedit) {
		this.isedit = isedit;
	}

	@Column(name = "STATUS", precision = 2, scale = 0)
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "STARTDATE", length = 19)
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

	@Formula("(select dic.name from SYS_DICENUMITEM dic  where dic.elementcode='SYS_ELEMENTCLASS'  and dic.code=elementclass)")
	public String getCnelementclass() {
		return cnelementclass;
	}
	
	public void setCnelementclass(String cnelementclass) {
		this.cnelementclass = cnelementclass;
	}
	
	@Formula("(select t.tablename from SYS_DICTABLE t  where t.tablecode=tablecode)")
	public String getTablename() {
		return tablename;
	}
	
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
}
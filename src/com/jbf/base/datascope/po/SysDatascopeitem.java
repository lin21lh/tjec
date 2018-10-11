/************************************************************
 * 类名：SysDatascopeitem.java
 *
 * 类别：PO
 * 功能：数据权限条件PO
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
 * SysDatascopeitem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DATASCOPEITEM")
public class SysDatascopeitem implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long scopeitemid;
	private Long scopesubid;
	private String elementcode;
	private String elementname;
	private String datatype;
	private byte isinclude;
	private byte matchtype;
	private String scopevalue;
	private String createdate;
	private String remark;

	// Constructors

	/** default constructor */
	public SysDatascopeitem() {
	}

	/** minimal constructor */
	public SysDatascopeitem(Long scopeitemid, Long scopesubid,
			String elementcode, byte isinclude, byte matchtype,
			String scopevalue) {
		this.scopeitemid = scopeitemid;
		this.scopesubid = scopesubid;
		this.elementcode = elementcode;
		this.isinclude = isinclude;
		this.matchtype = matchtype;
		this.scopevalue = scopevalue;
	}

	/** full constructor */
	public SysDatascopeitem(Long scopeitemid, Long scopesubid,
			String elementcode, byte isinclude, byte matchtype,
			String scopevalue, String createdate, String remark) {
		this.scopeitemid = scopeitemid;
		this.scopesubid = scopesubid;
		this.elementcode = elementcode;
		this.isinclude = isinclude;
		this.matchtype = matchtype;
		this.scopevalue = scopevalue;
		this.createdate = createdate;
		this.remark = remark;
	}

	@Id
    @SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DATASCOPEITEM")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SCOPEITEMID", nullable = false, scale = 0)
	public Long getScopeitemid() {
		return this.scopeitemid;
	}

	public void setScopeitemid(Long scopeitemid) {
		this.scopeitemid = scopeitemid;
	}

	@Column(name = "SCOPESUBID", nullable = false, scale = 0)
	public Long getScopesubid() {
		return this.scopesubid;
	}

	public void setScopesubid(Long scopesubid) {
		this.scopesubid = scopesubid;
	}

	@Column(name = "ELEMENTCODE", nullable = false, length = 50)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}
	
	@Formula("(select t.elementname from SYS_DICELEMENT t  where t.elementcode=elementcode)")
	public String getElementname() {
		return elementname;
	}
	
	public void setElementname(String elementname) {
		this.elementname = elementname;
	}
	
	@Formula("(select t.datatype from SYS_DICELEMENT t where t.elementcode=elementcode)")
	public String getDatatype() {
		return datatype;
	}
	
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "ISINCLUDE", nullable = false, precision = 2, scale = 0)
	public byte getIsinclude() {
		return this.isinclude;
	}

	public void setIsinclude(byte isinclude) {
		this.isinclude = isinclude;
	}

	@Column(name = "MATCHTYPE", nullable = false, precision = 2, scale = 0)
	public byte getMatchtype() {
		return this.matchtype;
	}

	public void setMatchtype(byte matchtype) {
		this.matchtype = matchtype;
	}

	@Column(name = "SCOPEVALUE", nullable = true, length = 100)
	public String getscopevalue() {
		return this.scopevalue;
	}

	public void setscopevalue(String scopevalue) {
		this.scopevalue = scopevalue;
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

}
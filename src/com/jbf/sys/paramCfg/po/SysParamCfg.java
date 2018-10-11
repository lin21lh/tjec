/************************************************************
 * 类名：SysParamCfg.java
 *
 * 类别：PO
 * 功能：系统参数PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-5-7  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.paramCfg.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.jbf.common.util.StringUtil;

/**
 * SysParamCfg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_APPCFG")
public class SysParamCfg implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6992085147133726281L;
	private Long paramid;
	private Integer admivcode;
	private String scenecode;
	private String scenename;
	private String paramcode;
	private String paramname;
	private String paramvalue;
	private String paramdesc;
	private Integer status;
	private String remark;
	
	public SysParamCfg() {
		
	}
	
	public SysParamCfg(Integer admivcode, String scenecode, String paramcode, String paramname, String paramvalue, String paramdesc, Integer status, String remark) {
		this.admivcode = admivcode;
		this.scenecode = scenecode;
		this.paramcode = paramcode;
		this.paramname = paramname;
		this.paramvalue = paramvalue;
		this.paramdesc = paramdesc;
		this.status = status;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_APPCFG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "PARAMID", nullable = false, scale = 0)
	public Long getParamid() {
		return paramid;
	}
	
	public void setParamid(Long paramid) {
		this.paramid = paramid;
	}

	@Column(name = "ADMIVCODE", nullable = false, scale = 0)
	public Integer getAdmivcode() {
		return admivcode;
	}
	
	public void setAdmivcode(Integer admivcode) {
		this.admivcode = admivcode;
	}

	@Column(name = "SCENECODE", nullable = false, length = 42)
	public String getScenecode() {
		return scenecode;
	}
	
	public void setScenecode(String scenecode) {
		if (StringUtil.isNotBlank(scenecode))
			scenecode = scenecode.toUpperCase();
		this.scenecode = scenecode;
	}
	
	@Formula("(select t.code || '-' || t.name from SYS_DICENUMITEM t where upper(t.elementcode)='SYS_SCENE' and t.status=0  and t.code=scenecode)")
	public String getScenename() {
		return scenename;
	}
	
	public void setScenename(String scenename) {
		this.scenename = scenename;
	}

	@Column(name = "PARAMCODE", nullable = false, length = 42)
	public String getParamcode() {
		return paramcode;
	}
	
	public void setParamcode(String paramcode) {
		if (StringUtil.isNotBlank(paramcode))
			paramcode = paramcode.toUpperCase();
		this.paramcode = paramcode;
	}

	@Column(name = "PARAMNAME", nullable = false, length = 60)
	public String getParamname() {
		return paramname;
	}
	
	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	@Column(name = "PARAMVALUE", nullable = false, length = 2000)
	public String getParamvalue() {
		return paramvalue;
	}
	
	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}

	@Column(name = "PARAMDESC", nullable = false, length = 200)
	public String getParamdesc() {
		return paramdesc;
	}
	
	public void setParamdesc(String paramdesc) {
		this.paramdesc = paramdesc;
	}

	@Column(name = "STATUS", nullable = false, precision = 1, scale = 0)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

/************************************************************
 * 类名：SysScopevalue.java
 *
 * 类别：PO
 * 功能：数据权限值集PO
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

@Entity
@Table(name = "SYS_SCOPEVALUE")
public class SysScopevalue {

	private Long scopevalueid;
	private Long valueid;
	private String valuecode;
	private Long scopeitemid;

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_SCOPEVALUE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SCOPEVALUEID", nullable = false, scale = 0)
	public Long getScopevalueid() {
		return scopevalueid;
	}

	public void setScopevalueid(Long scopevalueid) {
		this.scopevalueid = scopevalueid;
	}

	@Column(name = "VALUEID", nullable = false, scale = 0)
	public Long getValueid() {
		return valueid;
	}
	
	public void setValueid(Long valueid) {
		this.valueid = valueid;
	}
	
	@Column(name = "VALUECODE", nullable = false, length=50)
	public String getValuecode() {
		return valuecode;
	}
	
	public void setValuecode(String valuecode) {
		this.valuecode = valuecode;
	}
	
	@Column(name = "SCOPEITEMID", nullable = false, scale = 0)
	public Long getScopeitemid() {
		return scopeitemid;
	}
	
	public void setScopeitemid(Long scopeitemid) {
		this.scopeitemid = scopeitemid;
	}
}

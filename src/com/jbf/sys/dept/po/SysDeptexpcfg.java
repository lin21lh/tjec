/************************************************************
 * 类名：SysDeptexpcfg.java
 *
 * 类别：PO
 * 功能：机构扩展属性配置PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_DEPTEXPCFG")
public class SysDeptexpcfg {

	private Long itemid;
	private Long agencycat;
	private String expandcolumn;
	private Byte isnotnull;
	private String remark;
	
	public SysDeptexpcfg() {
		
	}
	
	public SysDeptexpcfg(Long agencycat, String expandcolumn,
			Byte isnotnull) {
		this.agencycat = agencycat;
		this.expandcolumn = expandcolumn;
		this.isnotnull = isnotnull;
	}
	
	public SysDeptexpcfg(Long agencycat, String expandcolumn,
			Byte isnotnull, String remark) {
		this.agencycat = agencycat;
		this.expandcolumn = expandcolumn;
		this.isnotnull = isnotnull;
		this.remark = remark;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DEPTEXPCFG")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ITEMID", nullable = false, scale = 0)
	public Long getItemid() {
		return itemid;
	}
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	@Column(name = "AGENCYCAT", scale = 0)
	public Long getAgencycat() {
		return agencycat;
	}
	public void setAgencycat(Long agencycat) {
		this.agencycat = agencycat;
	}
	@Column(name = "EXPANDCOLUMN", nullable = false, length = 100)
	public String getExpandcolumn() {
		return expandcolumn;
	}
	public void setExpandcolumn(String expandcolumn) {
		this.expandcolumn = expandcolumn;
	}
	@Column(name = "ISNOTNULL", nullable = false, precision = 2, scale = 0)
	public Byte getIsnotnull() {
		return isnotnull;
	}
	public void setIsnotnull(Byte isnotnull) {
		this.isnotnull = isnotnull;
	}
	@Column(name = "REMARK", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

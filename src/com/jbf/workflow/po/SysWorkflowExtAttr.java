/************************************************************
 * 类名：SysWorkflowExtAttr
 *
 * 类别：PO
 * 功能：工作流扩展属性配置表PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * SysWorkflowOpdef entity. @author MyEclipse Persistence Tools 保存流程所使用的操作
 */
@Entity
@Table(name = "SYS_WORKFLOW_EXT_ATTR")
public class SysWorkflowExtAttr implements java.io.Serializable {

	Long id;
	String key;
	Integer version;
	String deploymentid;
	String category;
	String srcacti;
	String transition;
	String tgtacti;
	String attrvalue1;
	String attrvalue2;
	String attrvalue3;
	String attrvalue4;
	Long attrnum1;
	Long attrnum2;
	String remark;

	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_WORKFLOW_EXT_ATTR")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ID", unique = true, nullable = false, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "KEY", unique = false, nullable = false)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name = "VERSION", unique = false, nullable = true)
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name = "DEPLOYMENTID", unique = false, nullable = true)
	public String getDeploymentid() {
		return deploymentid;
	}

	public void setDeploymentid(String deploymentid) {
		this.deploymentid = deploymentid;
	}

	@Column(name = "CATEGORY", unique = false, nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "SRCACTI", unique = false, nullable = true)
	public String getSrcacti() {
		return srcacti;
	}

	public void setSrcacti(String srcacti) {
		this.srcacti = srcacti;
	}

	@Column(name = "TRANSITION", unique = false, nullable = true)
	public String getTransition() {
		return transition;
	}

	public void setTransition(String transition) {
		this.transition = transition;
	}

	@Column(name = "TGTACTI", unique = false, nullable = true)
	public String getTgtacti() {
		return tgtacti;
	}

	public void setTgtacti(String tgtacti) {
		this.tgtacti = tgtacti;
	}

	@Column(name = "ATTRVALUE1", unique = false, nullable = true)
	public String getAttrvalue1() {
		return attrvalue1;
	}

	public void setAttrvalue1(String attrvalue1) {
		this.attrvalue1 = attrvalue1;
	}

	@Column(name = "ATTRVALUE2", unique = false, nullable = true)
	public String getAttrvalue2() {
		return attrvalue2;
	}

	public void setAttrvalue2(String attrvalue2) {
		this.attrvalue2 = attrvalue2;
	}

	@Column(name = "ATTRVALUE3", unique = false, nullable = true)
	public String getAttrvalue3() {
		return attrvalue3;
	}

	public void setAttrvalue3(String attrvalue3) {
		this.attrvalue3 = attrvalue3;
	}

	@Column(name = "ATTRVALUE4", unique = false, nullable = true)
	public String getAttrvalue4() {
		return attrvalue4;
	}

	public void setAttrvalue4(String attrvalue4) {
		this.attrvalue4 = attrvalue4;
	}

	@Column(name = "ATTRNUM1", unique = false, nullable = true)
	public Long getAttrnum1() {
		return attrnum1;
	}

	public void setAttrnum1(Long attrnum1) {
		this.attrnum1 = attrnum1;
	}

	@Column(name = "ATTRNUM2", unique = false, nullable = true)
	public Long getAttrnum2() {
		return attrnum2;
	}

	public void setAttrnum2(Long attrnum2) {
		this.attrnum2 = attrnum2;
	}

	@Column(name = "REMARK", unique = false, nullable = true)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
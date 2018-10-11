/************************************************************
 * 类名：SysHelpDoc.java
 *
 * 类别：PO
 * 功能：帮助文档PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-12-6  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.helpdoc.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/**
 * SysHelpDoc entity. @author mqs
 */
@Entity
@Table(name = "SYS_HELPDOC")
public class SysHelpDoc {

	private Long hcid;
	private String hccode;
	private Long superihcid;
	
	private String hctitle;
	private String hccontent;
	private String hctype;
	private Integer levelno;
	private Integer isleaf;
	private Integer readcount;
	private Integer usecount;
	private String createtime;
	private String updatetime;
	private String remark;
	
	public SysHelpDoc() {
		
	}
	
	public SysHelpDoc(String hccode, String hctitle, String hccontent, String hctype, String createtime, String updatetime, String remark) {
		this.hccode = hccode;
		this.hctitle = hctitle;
		this.hccontent = hccontent;
		this.hctype = hctype;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.remark = remark;
	}
	
	public SysHelpDoc(Long hcid, String hccode, Long superihcid, String hctitle, String hccontent, String hctype, Integer levelno, Integer isleaf, Integer readcount,
			Integer usecount, String createtime, String updatetime, String remark) {
		this.hcid = hcid;
		this.hccode = hccode;
		this.superihcid = superihcid;
		this.hctitle = hctitle;
		this.hccontent = hccontent;
		this.hctype = hctype;
		this.levelno = levelno;
		this.isleaf = isleaf;
		this.readcount = readcount;
		this.usecount = usecount;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.remark = remark;
	}
	
	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_HELPDOC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "HCID", precision = 0)
	public Long getHcid() {
		return hcid;
	}
	public void setHcid(Long hcid) {
		this.hcid = hcid;
	}
	
	@Column(name = "HCCODE", nullable = false, length = 20)
	public String getHccode() {
		return hccode;
	}
	public void setHccode(String hccode) {
		this.hccode = hccode;
	}
	
	@Column(name = "SUPERIHCID", precision = 0)
	public Long getSuperihcid() {
		return superihcid;
	}
	public void setSuperihcid(Long superihcid) {
		this.superihcid = superihcid;
	}
	
	@Column(name = "HCTITLE", nullable = false, length = 100)
	public String getHctitle() {
		return hctitle;
	}
	public void setHctitle(String hctitle) {
		this.hctitle = hctitle;
	}
	
	@Lob
	@Type(type = "org.springframework.orm.hibernate3.support.ClobStringType")
	@Column(name = "HCCONTENT", nullable = false)
	public String getHccontent() {
		return hccontent;
	}
	public void setHccontent(String hccontent) {
		this.hccontent = hccontent;
	}
	
	@Column(name = "HCTYPE", nullable = false, length = 20)
	public String getHctype() {
		return hctype;
	}
	public void setHctype(String hctype) {
		this.hctype = hctype;
	}
	
	@Column(name = "LEVELNO", nullable = false, precision = 0)
	public Integer getLevelno() {
		return levelno;
	}
	public void setLevelno(Integer levelno) {
		this.levelno = levelno;
	}
	
	@Column(name = "ISLEAF", nullable = false, precision = 0)
	public Integer getIsleaf() {
		return isleaf;
	}
	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}
	
	@Column(name = "READCOUNT", nullable = false, precision = 0)
	public Integer getReadcount() {
		return readcount;
	}
	public void setReadcount(Integer readcount) {
		this.readcount = readcount;
	}
	
	@Column(name = "USECOUNT", nullable = false, precision = 0)
	public Integer getUsecount() {
		return usecount;
	}
	public void setUsecount(Integer usecount) {
		this.usecount = usecount;
	}
	
	@Column(name = "CREATETIME", length = 20)
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	@Column(name = "UPDATETIME", length = 20)
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	@Column(name = "REMARK", length = 255)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

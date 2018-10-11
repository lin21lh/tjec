/************************************************************
 * 类名：SysDept.java
 *
 * 类别：PO
 * 功能：机构PO
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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Formula;



/**
 * SysDept entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DEPT", uniqueConstraints = @UniqueConstraint(columnNames = "CODE"))
public class SysDept implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3189312011089948748L;
	private Long itemid;
	private String code;
	private String name;
	private String isbncode;
	private String shortname;
	private String wholename;
	private Long superitemid;
	private Integer levelno;
	private Integer isleaf;
	private Integer status;
	private String startdate;
	private String enddate;
	private Long agencycat;
	private String agencycatname;
	private String remark;
	private String area;


	// Constructors
	@Column(name = "AREA", nullable = true, length = 10)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/** default constructor */
	public SysDept() {
	}

	/** full constructor */
	public SysDept(String code, String name, String isbncode,
			String shortname, String wholename, Long superitemid, Integer levelno,
			Integer isleaf, Integer status, String startdate, String enddate, Long agencycat,
			String remark) {
		this.code = code;
		this.name = name;
		this.isbncode = isbncode;
		this.shortname = shortname;
		this.wholename = wholename;
		this.superitemid = superitemid;
		this.levelno = levelno;
		this.isleaf = isleaf;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.agencycat = agencycat;
		this.remark = remark;
	}	

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DEPT")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ITEMID", nullable = false, scale = 0)
	public Long getItemid() {
		return itemid;
	}

	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}

	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "ISBNCODE", nullable = true, length = 50)
	public String getIsbncode() {
		return isbncode;
	}
	
	public void setIsbncode(String isbncode) {
		this.isbncode = isbncode;
	}

	@Column(name = "SHORTNAME", nullable = true, length = 100)
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@Column(name = "WHOLENAME", nullable = true, length = 100)
	public String getWholename() {
		return wholename;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

	@Column(name = "SUPERITEMID", nullable = true, scale = 0)
	public Long getSuperitemid() {
		return superitemid;
	}

	public void setSuperitemid(Long superitemid) {
		this.superitemid = superitemid;
	}

	@Column(name = "LEVELNO", nullable = false, precision = 2)
	public Integer getLevelno() {
		return levelno;
	}

	public void setLevelno(Integer levelno) {
		this.levelno = levelno;
	}

	@Column(name = "ISLEAF", nullable = false, precision = 2)
	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	@Column(name = "STATUS", nullable = false, precision = 2)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

    @Column(name = "STARTDATE", nullable = false, length = 19)
	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

    @Column(name = "ENDDATE", nullable = true, length = 19)
	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	@Column(name = "AGENCYCAT", nullable = false, scale = 0)
	public Long getAgencycat() {
		return agencycat;
	}

	public void setAgencycat(Long agencycat) {
		this.agencycat = agencycat;
	}
	
	@Formula("(select t.name from sys_dicenumitem t where t.elementcode='AGENCYCAT' and t.code=agencycat)")
	public String getAgencycatname() {
		return agencycatname;
	}
	
	public void setAgencycatname(String agencycatname) {
		this.agencycatname = agencycatname;
	}

	@Column(name = "REMARK", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
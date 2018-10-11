package com.jbf.base.dic.po;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * SysYwDiccodeitem entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_YW_DICCODEITEM", uniqueConstraints = @UniqueConstraint(columnNames = {
		"ELEMENTCODE", "CODE" }))
public class SysYwDiccodeitem implements java.io.Serializable {

	// Fields

	private BigDecimal itemid;
	private String code;
	private String name;
	private String elementcode;
	private String shortname;
	private String wholename;
	private Byte isleaf;
	private BigDecimal superitemid;
	private Byte levelno;
	private Byte status;
	private String startdate;
	private String enddate;
	private String remark;

	// Constructors

	/** default constructor */
	public SysYwDiccodeitem() {
	}

	/** minimal constructor */
	public SysYwDiccodeitem(BigDecimal itemid, String code, String name,
			String elementcode, Byte isleaf, BigDecimal superitemid,
			Byte levelno, Byte status, String startdate) {
		this.itemid = itemid;
		this.code = code;
		this.name = name;
		this.elementcode = elementcode;
		this.isleaf = isleaf;
		this.superitemid = superitemid;
		this.levelno = levelno;
		this.status = status;
		this.startdate = startdate;
	}

	/** full constructor */
	public SysYwDiccodeitem(BigDecimal itemid, String code, String name,
			String elementcode, String shortname, String wholename,
			Byte isleaf, BigDecimal superitemid, Byte levelno, Byte status,
			String startdate, String enddate, String remark) {
		this.itemid = itemid;
		this.code = code;
		this.name = name;
		this.elementcode = elementcode;
		this.shortname = shortname;
		this.wholename = wholename;
		this.isleaf = isleaf;
		this.superitemid = superitemid;
		this.levelno = levelno;
		this.status = status;
		this.startdate = startdate;
		this.enddate = enddate;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@Column(name = "ITEMID", unique = true, nullable = false, scale = 0)
	public BigDecimal getItemid() {
		return this.itemid;
	}

	public void setItemid(BigDecimal itemid) {
		this.itemid = itemid;
	}

	@Column(name = "CODE", nullable = false, length = 50)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ELEMENTCODE", nullable = false, length = 50)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}

	@Column(name = "SHORTNAME", length = 50)
	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@Column(name = "WHOLENAME", length = 100)
	public String getWholename() {
		return this.wholename;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

	@Column(name = "ISLEAF", nullable = false, precision = 2, scale = 0)
	public Byte getIsleaf() {
		return this.isleaf;
	}

	public void setIsleaf(Byte isleaf) {
		this.isleaf = isleaf;
	}

	@Column(name = "SUPERITEMID", nullable = false, scale = 0)
	public BigDecimal getSuperitemid() {
		return this.superitemid;
	}

	public void setSuperitemid(BigDecimal superitemid) {
		this.superitemid = superitemid;
	}

	@Column(name = "LEVELNO", nullable = false, precision = 2, scale = 0)
	public Byte getLevelno() {
		return this.levelno;
	}

	public void setLevelno(Byte levelno) {
		this.levelno = levelno;
	}

	@Column(name = "STATUS", nullable = false, precision = 2, scale = 0)
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Column(name = "STARTDATE", nullable = false, length = 19)
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

}
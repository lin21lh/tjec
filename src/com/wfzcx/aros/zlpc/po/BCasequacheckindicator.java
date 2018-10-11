package com.wfzcx.aros.zlpc.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * BCasequacheckindicator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASEQUACHECKINDICATOR")
public class BCasequacheckindicator implements java.io.Serializable {

	// Fields

	private String standid;
	private String standardname;
	private String stagetype;
	private String stagetypeMc;
	private String inditype;
	private String inditypeMc;
	private Integer score;
	private Integer seqno;

	// Constructors

	/** default constructor */
	public BCasequacheckindicator() {
	}

	/** minimal constructor */
	public BCasequacheckindicator(String standid) {
		this.standid = standid;
	}

	/** full constructor */
	public BCasequacheckindicator(String standid, String standardname, String stagetype, String inditype,
			Integer score, Integer seqno) {
		this.standid = standid;
		this.standardname = standardname;
		this.stagetype = stagetype;
		this.inditype = inditype;
		this.score = score;
		this.seqno = seqno;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "STANDID", unique = true, nullable = false, length = 32)
	public String getStandid() {
		return this.standid;
	}

	public void setStandid(String standid) {
		this.standid = standid;
	}

	@Column(name = "STANDARDNAME", length = 1000)
	public String getStandardname() {
		return this.standardname;
	}

	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}

	@Column(name = "STAGETYPE", length = 2)
	public String getStagetype() {
		return this.stagetype;
	}

	public void setStagetype(String stagetype) {
		this.stagetype = stagetype;
	}

	@Column(name = "INDITYPE", length = 2)
	public String getInditype() {
		return this.inditype;
	}

	public void setInditype(String inditype) {
		this.inditype = inditype;
	}

	@Column(name = "SCORE", precision = 22, scale = 0)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "SEQNO", precision = 22, scale = 0)
	public Integer getSeqno() {
		return this.seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_STAGETYPE' and a.status=0  and a.code=stagetype)")
	public String getStagetypeMc() {
		return stagetypeMc;
	}

	public void setStagetypeMc(String stagetypeMc) {
		this.stagetypeMc = stagetypeMc;
	}

	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ZB_INDITYPE' and a.status=0  and a.code=inditype)")
	public String getInditypeMc() {
		return inditypeMc;
	}

	public void setInditypeMc(String inditypeMc) {
		this.inditypeMc = inditypeMc;
	}

	
}
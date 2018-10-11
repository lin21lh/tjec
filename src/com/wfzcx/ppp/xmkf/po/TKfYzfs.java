package com.wfzcx.ppp.xmkf.po;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * TKfYzfs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_KF_YZFS")
@SequenceGenerator(name="SEQ_T_KF_YZFS",sequenceName="SEQ_T_KF_YZFS")
@GenericGenerator(name = "SEQ_T_KF_YZFS", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_KF_YZFS") })
public class TKfYzfs implements java.io.Serializable {

	// Fields

	private Integer yzfsid;
	private Integer xmid;
	private String sftyyzfs;
	private Date pzrq;
	private String kfnd;
	private String bz;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;
	private String wfid;
	private String dqzt;
	private String cgfs;
	private String spyj;

	// Constructors

	/** default constructor */
	public TKfYzfs() {
	}

	/** minimal constructor */
	public TKfYzfs(Integer yzfsid, Integer xmid, String sftyyzfs, Date pzrq, String kfnd, String cjr,
			Date cjsj, String cgfs) {
		this.yzfsid = yzfsid;
		this.xmid = xmid;
		this.sftyyzfs = sftyyzfs;
		this.pzrq = pzrq;
		this.kfnd = kfnd;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.cgfs = cgfs;
	}

	/** full constructor */
	public TKfYzfs(Integer yzfsid, Integer xmid, String sftyyzfs, Date pzrq, String kfnd, String bz,
			String cjr, Date cjsj, String xgr, Date xgsj, String wfid, String dqzt, String cgfs) {
		this.yzfsid = yzfsid;
		this.xmid = xmid;
		this.sftyyzfs = sftyyzfs;
		this.pzrq = pzrq;
		this.kfnd = kfnd;
		this.bz = bz;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.xgr = xgr;
		this.xgsj = xgsj;
		this.wfid = wfid;
		this.dqzt = dqzt;
		this.cgfs = cgfs;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_KF_YZFS")
	@Column(name = "YZFSID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getYzfsid() {
		return this.yzfsid;
	}

	public void setYzfsid(Integer yzfsid) {
		this.yzfsid = yzfsid;
	}

	@Column(name = "XMID", nullable = false, precision = 20, scale = 0)
	public Integer getXmid() {
		return this.xmid;
	}

	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}

	@Column(name = "SFTYYZFS", nullable = false, length = 1)
	public String getSftyyzfs() {
		return this.sftyyzfs;
	}

	public void setSftyyzfs(String sftyyzfs) {
		this.sftyyzfs = sftyyzfs;
	}

	@Column(name = "PZRQ", nullable = false, length = 7)
	public Date getPzrq() {
		return this.pzrq;
	}

	public void setPzrq(Date pzrq) {
		this.pzrq = pzrq;
	}

	@Column(name = "KFND", nullable = false, length = 7)
	public String getKfnd() {
		return this.kfnd;
	}

	public void setKfnd(String kfnd) {
		this.kfnd = kfnd;
	}

	@Column(name = "BZ", length = 1000)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "CJR", nullable = false, length = 20)
	public String getCjr() {
		return this.cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	@Column(name = "CJSJ", nullable = false, length = 7)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}

	@Column(name = "XGR", length = 20)
	public String getXgr() {
		return this.xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	@Column(name = "XGSJ", length = 7)
	public Date getXgsj() {
		return this.xgsj;
	}

	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "DQZT", length = 2)
	public String getDqzt() {
		return this.dqzt;
	}

	public void setDqzt(String dqzt) {
		this.dqzt = dqzt;
	}

	@Column(name = "CGFS", nullable = false, length = 2)
	public String getCgfs() {
		return this.cgfs;
	}

	public void setCgfs(String cgfs) {
		this.cgfs = cgfs;
	}

	@Column(name = "SPYJ", nullable = false, length = 200)
	public String getSpyj() {
		return spyj;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

}
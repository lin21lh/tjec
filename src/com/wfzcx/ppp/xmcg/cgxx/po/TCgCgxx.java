package com.wfzcx.ppp.xmcg.cgxx.po;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * TCgCgxx entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CG_CGXX")
@SequenceGenerator(name="SEQ_T_CG_CGXX",sequenceName="SEQ_T_CG_CGXX")
@GenericGenerator(name = "SEQ_T_CG_CGXX", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_CG_CGXX") })
public class TCgCgxx implements java.io.Serializable {

	// Fields

	private Integer cgxxid;
	private Integer xmid;
	private String cglxr;
	private String lxrdh;
	private Double ysje;
	private String cgfs;
	private String dljgid;
	private String zgbmyj;
	private String pppyj;
	private String zfcgglkyj;
	private String bz;
	private String wfid;
	private String dqzt;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;

	// Constructors

	/** default constructor */
	public TCgCgxx() {
	}

	/** minimal constructor */
	public TCgCgxx(Integer cgxxid, Integer xmid, String cglxr,
			String lxrdh, String cgfs, String dljgid, String zgbmyj,
			String pppyj, String zfcgglkyj, String cjr, Date cjsj) {
		this.cgxxid = cgxxid;
		this.xmid = xmid;
		this.cglxr = cglxr;
		this.lxrdh = lxrdh;
		this.cgfs = cgfs;
		this.dljgid = dljgid;
		this.zgbmyj = zgbmyj;
		this.pppyj = pppyj;
		this.zfcgglkyj = zfcgglkyj;
		this.cjr = cjr;
		this.cjsj = cjsj;
	}

	/** full constructor */
	public TCgCgxx(Integer cgxxid, Integer xmid, String cglxr,
			String lxrdh, Double ysje, String cgfs, String dljgid,
			String zgbmyj, String pppyj, String zfcgglkyj, String bz,
			String wfid, String dqzt, String cjr, Date cjsj, String xgr,
			Date xgsj) {
		this.cgxxid = cgxxid;
		this.xmid = xmid;
		this.cglxr = cglxr;
		this.lxrdh = lxrdh;
		this.ysje = ysje;
		this.cgfs = cgfs;
		this.dljgid = dljgid;
		this.zgbmyj = zgbmyj;
		this.pppyj = pppyj;
		this.zfcgglkyj = zfcgglkyj;
		this.bz = bz;
		this.wfid = wfid;
		this.dqzt = dqzt;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.xgr = xgr;
		this.xgsj = xgsj;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CG_CGXX")
	@Column(name = "CGXXID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getCgxxid() {
		return this.cgxxid;
	}

	public void setCgxxid(Integer cgxxid) {
		this.cgxxid = cgxxid;
	}

	@Column(name = "XMID", nullable = false, precision = 20, scale = 0)
	public Integer getXmid() {
		return this.xmid;
	}

	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}

	@Column(name = "CGLXR", nullable = false, length = 100)
	public String getCglxr() {
		return this.cglxr;
	}

	public void setCglxr(String cglxr) {
		this.cglxr = cglxr;
	}

	@Column(name = "LXRDH", nullable = false, length = 100)
	public String getLxrdh() {
		return this.lxrdh;
	}

	public void setLxrdh(String lxrdh) {
		this.lxrdh = lxrdh;
	}

	@Column(name = "YSJE", precision = 20)
	public Double getYsje() {
		return this.ysje;
	}

	public void setYsje(Double ysje) {
		this.ysje = ysje;
	}

	@Column(name = "CGFS", nullable = false, length = 1)
	public String getCgfs() {
		return this.cgfs;
	}

	public void setCgfs(String cgfs) {
		this.cgfs = cgfs;
	}

	@Column(name = "DLJGID", nullable = false, length = 20)
	public String getDljgid() {
		return this.dljgid;
	}

	public void setDljgid(String dljgid) {
		this.dljgid = dljgid;
	}

	@Column(name = "ZGBMYJ", nullable = false, length = 100)
	public String getZgbmyj() {
		return this.zgbmyj;
	}

	public void setZgbmyj(String zgbmyj) {
		this.zgbmyj = zgbmyj;
	}

	@Column(name = "PPPYJ", nullable = false, length = 100)
	public String getPppyj() {
		return this.pppyj;
	}

	public void setPppyj(String pppyj) {
		this.pppyj = pppyj;
	}

	@Column(name = "ZFCGGLKYJ", nullable = false, length = 100)
	public String getZfcgglkyj() {
		return this.zfcgglkyj;
	}

	public void setZfcgglkyj(String zfcgglkyj) {
		this.zfcgglkyj = zfcgglkyj;
	}

	@Column(name = "BZ", length = 1000)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "DQZT", length = 20)
	public String getDqzt() {
		return this.dqzt;
	}

	public void setDqzt(String dqzt) {
		this.dqzt = dqzt;
	}

	@Column(name = "CJR", nullable = false, length = 20)
	public String getCjr() {
		return this.cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	@Temporal(TemporalType.DATE)
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

	@Temporal(TemporalType.DATE)
	@Column(name = "XGSJ", length = 7)
	public Date getXgsj() {
		return this.xgsj;
	}

	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

}
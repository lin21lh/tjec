package com.wfzcx.ppp.xmcb.po;

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
 * TXmxx entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_XMXX")
@SequenceGenerator(name="SEQ_T_XMXX",sequenceName="SEQ_T_XMXX")
@GenericGenerator(name = "SEQ_T_XMXX", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_XMXX") })
public class TXmxx implements java.io.Serializable {

	// Fields

	private Integer xmid;
	private String sjlx;
	private String xmmc;
	private String xmlx;
	private Double xmzje;
	private String hznx;
	private String sshy;
	private String yzfs;
	private String hbjz;
	private String fqsj;
	private String xmdqhj;
	private String wfid;
	private String orgcode;
	private String xmcbk;
	private String xmkfk;
	private String fqlx;
	private String fqrmc;
	private String xmgk;
	private String xmlxr;
	private String lxrdh;
	private String cbssfa;
	private String ssfafj;
	private String spjg;
	private String sftjxm;
	private String sfxm;
	private String sfsqbt;
	private Double btje;
	private String spyj;
	private String xmzt;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;
	private Integer sjxmxh;
	private String ghsjfafj;
	private String zfzyzc;
	private String ssxq;
	private String dqzt;

	// Constructors

	/** default constructor */
	public TXmxx() {
	}

	/** minimal constructor */
	public TXmxx(Integer xmid, String sjlx, String xmmc, String xmlx,
			Double xmzje, String hznx, String sshy, String yzfs, String hbjz,
			String fqsj, String orgcode, String fqlx, String fqrmc,
			String xmgk, String xmlxr, String lxrdh, String cbssfa,
			String ssfafj, String cjr, Date cjsj) {
		this.xmid = xmid;
		this.sjlx = sjlx;
		this.xmmc = xmmc;
		this.xmlx = xmlx;
		this.xmzje = xmzje;
		this.hznx = hznx;
		this.sshy = sshy;
		this.yzfs = yzfs;
		this.hbjz = hbjz;
		this.fqsj = fqsj;
		this.orgcode = orgcode;
		this.fqlx = fqlx;
		this.fqrmc = fqrmc;
		this.xmgk = xmgk;
		this.xmlxr = xmlxr;
		this.lxrdh = lxrdh;
		this.cbssfa = cbssfa;
		this.ssfafj = ssfafj;
		this.cjr = cjr;
		this.cjsj = cjsj;
	}

	/** full constructor */
	public TXmxx(Integer xmid, String sjlx, String xmmc, String xmlx,
			Double xmzje, String hznx, String sshy, String yzfs, String hbjz,
			String fqsj, String xmdqhj, String wfid, String orgcode,
			String xmcbk, String xmkfk, String fqlx, String fqrmc, String xmgk,
			String xmlxr, String lxrdh, String cbssfa, String ssfafj,
			String spjg, String sftjxm, String sfxm, String sfsqbt,
			Double btje, String spyj, String xmzt, String cjr, Date cjsj,
			String xgr, Date xgsj, Integer sjxmxh, String ghsjfafj,
			String zzfyzc, String ssxq) {
		this.xmid = xmid;
		this.sjlx = sjlx;
		this.xmmc = xmmc;
		this.xmlx = xmlx;
		this.xmzje = xmzje;
		this.hznx = hznx;
		this.sshy = sshy;
		this.yzfs = yzfs;
		this.hbjz = hbjz;
		this.fqsj = fqsj;
		this.xmdqhj = xmdqhj;
		this.wfid = wfid;
		this.orgcode = orgcode;
		this.xmcbk = xmcbk;
		this.xmkfk = xmkfk;
		this.fqlx = fqlx;
		this.fqrmc = fqrmc;
		this.xmgk = xmgk;
		this.xmlxr = xmlxr;
		this.lxrdh = lxrdh;
		this.cbssfa = cbssfa;
		this.ssfafj = ssfafj;
		this.spjg = spjg;
		this.sftjxm = sftjxm;
		this.sfxm = sfxm;
		this.sfsqbt = sfsqbt;
		this.btje = btje;
		this.spyj = spyj;
		this.xmzt = xmzt;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.xgr = xgr;
		this.xgsj = xgsj;
		this.sjxmxh = sjxmxh;
		this.ghsjfafj = ghsjfafj;
		this.zfzyzc = zzfyzc;
		this.ssxq = ssxq;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_XMXX")
	@Column(name = "XMID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getXmid() {
		return this.xmid;
	}

	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}

	@Column(name = "SJLX", nullable = false, length = 2)
	public String getSjlx() {
		return this.sjlx;
	}

	public void setSjlx(String sjlx) {
		this.sjlx = sjlx;
	}

	@Column(name = "XMMC", nullable = false, length = 100)
	public String getXmmc() {
		return this.xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	@Column(name = "XMLX", nullable = false, length = 1)
	public String getXmlx() {
		return this.xmlx;
	}

	public void setXmlx(String xmlx) {
		this.xmlx = xmlx;
	}

	@Column(name = "XMZJE", nullable = false, precision = 16)
	public Double getXmzje() {
		return this.xmzje;
	}

	public void setXmzje(Double xmzje) {
		this.xmzje = xmzje;
	}

	@Column(name = "HZNX", nullable = false, precision = 2, scale = 0)
	public String getHznx() {
		return this.hznx;
	}

	public void setHznx(String hznx) {
		this.hznx = hznx;
	}

	@Column(name = "SSHY", nullable = false, length = 6)
	public String getSshy() {
		return this.sshy;
	}

	public void setSshy(String sshy) {
		this.sshy = sshy;
	}

	@Column(name = "YZFS", nullable = false, length = 10)
	public String getYzfs() {
		return this.yzfs;
	}

	public void setYzfs(String yzfs) {
		this.yzfs = yzfs;
	}

	@Column(name = "HBJZ", nullable = false, length = 1)
	public String getHbjz() {
		return this.hbjz;
	}

	public void setHbjz(String hbjz) {
		this.hbjz = hbjz;
	}

	@Column(name = "FQSJ", nullable = false, length = 20)
	public String getFqsj() {
		return this.fqsj;
	}

	public void setFqsj(String fqsj) {
		this.fqsj = fqsj;
	}

	@Column(name = "XMDQHJ", length = 2)
	public String getXmdqhj() {
		return this.xmdqhj;
	}

	public void setXmdqhj(String xmdqhj) {
		this.xmdqhj = xmdqhj;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "ORGCODE", nullable = false, length = 50)
	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	@Column(name = "XMCBK", length = 1)
	public String getXmcbk() {
		return this.xmcbk;
	}

	public void setXmcbk(String xmcbk) {
		this.xmcbk = xmcbk;
	}

	@Column(name = "XMKFK", length = 1)
	public String getXmkfk() {
		return this.xmkfk;
	}

	public void setXmkfk(String xmkfk) {
		this.xmkfk = xmkfk;
	}

	@Column(name = "FQLX", nullable = false, length = 1)
	public String getFqlx() {
		return this.fqlx;
	}

	public void setFqlx(String fqlx) {
		this.fqlx = fqlx;
	}

	@Column(name = "FQRMC", nullable = false, length = 100)
	public String getFqrmc() {
		return this.fqrmc;
	}

	public void setFqrmc(String fqrmc) {
		this.fqrmc = fqrmc;
	}

	@Column(name = "XMGK", nullable = false, length = 1000)
	public String getXmgk() {
		return this.xmgk;
	}

	public void setXmgk(String xmgk) {
		this.xmgk = xmgk;
	}

	@Column(name = "XMLXR", nullable = false, length = 50)
	public String getXmlxr() {
		return this.xmlxr;
	}

	public void setXmlxr(String xmlxr) {
		this.xmlxr = xmlxr;
	}

	@Column(name = "LXRDH", nullable = false, length = 100)
	public String getLxrdh() {
		return this.lxrdh;
	}

	public void setLxrdh(String lxrdh) {
		this.lxrdh = lxrdh;
	}

	@Column(name = "CBSSFA", nullable = false, length = 1000)
	public String getCbssfa() {
		return this.cbssfa;
	}

	public void setCbssfa(String cbssfa) {
		this.cbssfa = cbssfa;
	}

	public void setSsfafj(String ssfafj) {
		this.ssfafj = ssfafj;
	}

	@Column(name = "SPJG", length = 2)
	public String getSpjg() {
		return this.spjg;
	}

	public void setSpjg(String spjg) {
		this.spjg = spjg;
	}

	@Column(name = "SFTJXM", length = 1)
	public String getSftjxm() {
		return this.sftjxm;
	}

	public void setSftjxm(String sftjxm) {
		this.sftjxm = sftjxm;
	}

	@Column(name = "SFXM", length = 1)
	public String getSfxm() {
		return this.sfxm;
	}

	public void setSfxm(String sfxm) {
		this.sfxm = sfxm;
	}

	@Column(name = "SFSQBT", length = 1)
	public String getSfsqbt() {
		return this.sfsqbt;
	}

	public void setSfsqbt(String sfsqbt) {
		this.sfsqbt = sfsqbt;
	}

	@Column(name = "BTJE", precision = 16)
	public Double getBtje() {
		return this.btje;
	}

	public void setBtje(Double btje) {
		this.btje = btje;
	}

	@Column(name = "SPYJ", length = 1000)
	public String getSpyj() {
		return this.spyj;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

	@Column(name = "XMZT", length = 10)
	public String getXmzt() {
		return this.xmzt;
	}

	public void setXmzt(String xmzt) {
		this.xmzt = xmzt;
	}

	@Column(name = "CJR", nullable = false, length = 20)
	public String getCjr() {
		return this.cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "XGSJ", length = 7)
	public Date getXgsj() {
		return this.xgsj;
	}

	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

	@Column(name = "SJXMXH", precision = 20, scale = 0)
	public Integer getSjxmxh() {
		return this.sjxmxh;
	}

	public void setSjxmxh(Integer sjxmxh) {
		this.sjxmxh = sjxmxh;
	}

	@Column(name = "GHSJFAFJ", length = 100)
	public String getGhsjfafj() {
		return this.ghsjfafj;
	}

	public void setGhsjfafj(String ghsjfafj) {
		this.ghsjfafj = ghsjfafj;
	}

	@Column(name = "ZFZYZC", length = 1)
	public String getZfzyzc() {
		return this.zfzyzc;
	}

	public void setZfzyzc(String zfzyzc) {
		this.zfzyzc = zfzyzc;
	}

	@Column(name = "SSXQ", length = 10)
	public String getSsxq() {
		return this.ssxq;
	}

	public void setSsxq(String ssxq) {
		this.ssxq = ssxq;
	}
	@Column(name = "DQZT", length = 1)
	public String getDqzt() {
		return dqzt;
	}

	public void setDqzt(String dqzt) {
		this.dqzt = dqzt;
	}

}
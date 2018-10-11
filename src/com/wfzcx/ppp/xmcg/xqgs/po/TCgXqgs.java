package com.wfzcx.ppp.xmcg.xqgs.po;

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
 * TCgXqgs entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_CG_XQGS")
@SequenceGenerator(name="SEQ_T_CG_XQGS",sequenceName="SEQ_T_CG_XQGS")
@GenericGenerator(name = "SEQ_T_CG_XQGS", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_CG_XQGS") })
public class TCgXqgs implements java.io.Serializable {

	// Fields

	private Integer xqgsid;
	private Integer xmid;
	private String cgr;
	private String cgrdz;
	private String cglxr;
	private String cgrlxfs;
	private String cgxmmc;
	private String cgpmdm;
	private String cgpmmc;
	private Date cgksrq;
	private Date cgjsrq;
	private Integer gsts;
	private String bz;
	private String wfid;
	private String dqzt;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;

	// Constructors

	/** default constructor */
	public TCgXqgs() {
	}

	/** minimal constructor */
	public TCgXqgs(Integer xqgsid, Integer xmid, String cgr,
			String cgrdz, String cglxr, String cgrlxfs, String cgxmmc,
			String cgpmdm, String cgpmmc, Date cgksrq, Date cgjsrq, String cjr,
			Date cjsj) {
		this.xqgsid = xqgsid;
		this.xmid = xmid;
		this.cgr = cgr;
		this.cgrdz = cgrdz;
		this.cglxr = cglxr;
		this.cgrlxfs = cgrlxfs;
		this.cgxmmc = cgxmmc;
		this.cgpmdm = cgpmdm;
		this.cgpmmc = cgpmmc;
		this.cgksrq = cgksrq;
		this.cgjsrq = cgjsrq;
		this.cjr = cjr;
		this.cjsj = cjsj;
	}

	/** full constructor */
	public TCgXqgs(Integer xqgsid, Integer xmid, String cgr,
			String cgrdz, String cglxr, String cgrlxfs, String cgxmmc,
			String cgpmdm, String cgpmmc, Date cgksrq, Date cgjsrq,
			Integer gsts, String bz, String wfid, String dqzt, String cjr,
			Date cjsj, String xgr, Date xgsj) {
		this.xqgsid = xqgsid;
		this.xmid = xmid;
		this.cgr = cgr;
		this.cgrdz = cgrdz;
		this.cglxr = cglxr;
		this.cgrlxfs = cgrlxfs;
		this.cgxmmc = cgxmmc;
		this.cgpmdm = cgpmdm;
		this.cgpmmc = cgpmmc;
		this.cgksrq = cgksrq;
		this.cgjsrq = cgjsrq;
		this.gsts = gsts;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_CG_XQGS")
	@Column(name = "XQGSID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getXqgsid() {
		return this.xqgsid;
	}

	public void setXqgsid(Integer xqgsid) {
		this.xqgsid = xqgsid;
	}

	@Column(name = "XMID", nullable = false, precision = 20, scale = 0)
	public Integer getXmid() {
		return this.xmid;
	}

	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}

	@Column(name = "CGR", nullable = false, length = 100)
	public String getCgr() {
		return this.cgr;
	}

	public void setCgr(String cgr) {
		this.cgr = cgr;
	}

	@Column(name = "CGRDZ", nullable = false, length = 100)
	public String getCgrdz() {
		return this.cgrdz;
	}

	public void setCgrdz(String cgrdz) {
		this.cgrdz = cgrdz;
	}

	@Column(name = "CGLXR", nullable = false, length = 50)
	public String getCglxr() {
		return this.cglxr;
	}

	public void setCglxr(String cglxr) {
		this.cglxr = cglxr;
	}

	@Column(name = "CGRLXFS", nullable = false, length = 50)
	public String getCgrlxfs() {
		return this.cgrlxfs;
	}

	public void setCgrlxfs(String cgrlxfs) {
		this.cgrlxfs = cgrlxfs;
	}

	@Column(name = "CGXMMC", nullable = false, length = 100)
	public String getCgxmmc() {
		return this.cgxmmc;
	}

	public void setCgxmmc(String cgxmmc) {
		this.cgxmmc = cgxmmc;
	}

	@Column(name = "CGPMDM", nullable = false, length = 50)
	public String getCgpmdm() {
		return this.cgpmdm;
	}

	public void setCgpmdm(String cgpmdm) {
		this.cgpmdm = cgpmdm;
	}

	@Column(name = "CGPMMC", nullable = false, length = 100)
	public String getCgpmmc() {
		return this.cgpmmc;
	}

	public void setCgpmmc(String cgpmmc) {
		this.cgpmmc = cgpmmc;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CGKSRQ", nullable = false, length = 7)
	public Date getCgksrq() {
		return this.cgksrq;
	}

	public void setCgksrq(Date cgksrq) {
		this.cgksrq = cgksrq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CGJSRQ", nullable = false, length = 7)
	public Date getCgjsrq() {
		return this.cgjsrq;
	}

	public void setCgjsrq(Date cgjsrq) {
		this.cgjsrq = cgjsrq;
	}

	@Column(name = "GSTS", precision = 22, scale = 0)
	public Integer getGsts() {
		return this.gsts;
	}

	public void setGsts(Integer gsts) {
		this.gsts = gsts;
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

	@Column(name = "DQZT", length = 10)
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
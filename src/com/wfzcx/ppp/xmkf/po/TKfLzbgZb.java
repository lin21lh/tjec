package com.wfzcx.ppp.xmkf.po;

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
 * TKfLzbgZb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_KF_LZBG_ZB")
@SequenceGenerator(name="SEQ_T_KF_LZBG_ZB",sequenceName="SEQ_T_KF_LZBG_ZB")
@GenericGenerator(name = "SEQ_T_KF_LZBG_ZB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_KF_LZBG_ZB") })
public class TKfLzbgZb implements java.io.Serializable {

	// Fields

	private Integer lzbgid;
	private Integer xmid;
	private String dxpgJl;
	private String dxpgZjdfxq;
	private Date wyszyzWcsj;
	private Double dlpgYywhjcb;
	private Double dlpgJzxzltzz;
	private Double dlpgXmqbfxcb;
	private Double dlpgXj;
	private Double dlpgPpp;
	private Double dlpgVfm;
	private String dxpgJg;
	private String dlpgJg;
	private String czcsnlYzjl;
	private Date czcsnlWcsj;
	private String czcsnlYzjg;
	private String bz;
	private String wfid;
	private String dqzt;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;
	private String zjjgdm;

	// Constructors

	/** default constructor */
	public TKfLzbgZb() {
	}

	/** minimal constructor */
	public TKfLzbgZb(Integer lzbgid, Integer xmid, String dxpgJl,
			String dxpgZjdfxq, String dxpgJg, String czcsnlYzjl,
			Date czcsnlWcsj, String czcsnlYzjg, String cjr, Date cjsj,
			String zjjgdm) {
		this.lzbgid = lzbgid;
		this.xmid = xmid;
		this.dxpgJl = dxpgJl;
		this.dxpgZjdfxq = dxpgZjdfxq;
		this.dxpgJg = dxpgJg;
		this.czcsnlYzjl = czcsnlYzjl;
		this.czcsnlWcsj = czcsnlWcsj;
		this.czcsnlYzjg = czcsnlYzjg;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.zjjgdm = zjjgdm;
	}

	/** full constructor */
	public TKfLzbgZb(Integer lzbgid, Integer xmid, String dxpgJl,
			String dxpgZjdfxq, Date wyszyzWcsj, Double dlpgYywhjcb,
			Double dlpgJzxzltzz, Double dlpgXmqbfxcb, Double dlpgXj,
			Double dlpgPpp, Double dlpgVfm, String dxpgJg, String dlpgJg,
			String czcsnlYzjl, Date czcsnlWcsj, String czcsnlYzjg, String bz,
			String wfid, String dqzt, String cjr, Date cjsj, String xgr,
			Date xgsj, String zjjgdm) {
		this.lzbgid = lzbgid;
		this.xmid = xmid;
		this.dxpgJl = dxpgJl;
		this.dxpgZjdfxq = dxpgZjdfxq;
		this.wyszyzWcsj = wyszyzWcsj;
		this.dlpgYywhjcb = dlpgYywhjcb;
		this.dlpgJzxzltzz = dlpgJzxzltzz;
		this.dlpgXmqbfxcb = dlpgXmqbfxcb;
		this.dlpgXj = dlpgXj;
		this.dlpgPpp = dlpgPpp;
		this.dlpgVfm = dlpgVfm;
		this.dxpgJg = dxpgJg;
		this.dlpgJg = dlpgJg;
		this.czcsnlYzjl = czcsnlYzjl;
		this.czcsnlWcsj = czcsnlWcsj;
		this.czcsnlYzjg = czcsnlYzjg;
		this.bz = bz;
		this.wfid = wfid;
		this.dqzt = dqzt;
		this.cjr = cjr;
		this.cjsj = cjsj;
		this.xgr = xgr;
		this.xgsj = xgsj;
		this.zjjgdm = zjjgdm;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_KF_LZBG_ZB")
	@Column(name = "LZBGID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getLzbgid() {
		return this.lzbgid;
	}

	public void setLzbgid(Integer lzbgid) {
		this.lzbgid = lzbgid;
	}

	@Column(name = "XMID", nullable = false, precision = 20, scale = 0)
	public Integer getXmid() {
		return this.xmid;
	}

	public void setXmid(Integer xmid) {
		this.xmid = xmid;
	}

	@Column(name = "DXPG_JL", nullable = false, length = 1000)
	public String getDxpgJl() {
		return this.dxpgJl;
	}

	public void setDxpgJl(String dxpgJl) {
		this.dxpgJl = dxpgJl;
	}

	@Column(name = "DXPG_ZJDFXQ", nullable = false, length = 1000)
	public String getDxpgZjdfxq() {
		return this.dxpgZjdfxq;
	}

	public void setDxpgZjdfxq(String dxpgZjdfxq) {
		this.dxpgZjdfxq = dxpgZjdfxq;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WYSZYZ_WCSJ", length = 7)
	public Date getWyszyzWcsj() {
		return this.wyszyzWcsj;
	}

	public void setWyszyzWcsj(Date wyszyzWcsj) {
		this.wyszyzWcsj = wyszyzWcsj;
	}

	@Column(name = "DLPG_YYWHJCB", precision = 20, scale = 4)
	public Double getDlpgYywhjcb() {
		return this.dlpgYywhjcb;
	}

	public void setDlpgYywhjcb(Double dlpgYywhjcb) {
		this.dlpgYywhjcb = dlpgYywhjcb;
	}

	@Column(name = "DLPG_JZXZLTZZ", precision = 20, scale = 4)
	public Double getDlpgJzxzltzz() {
		return this.dlpgJzxzltzz;
	}

	public void setDlpgJzxzltzz(Double dlpgJzxzltzz) {
		this.dlpgJzxzltzz = dlpgJzxzltzz;
	}

	@Column(name = "DLPG_XMQBFXCB", precision = 20, scale = 4)
	public Double getDlpgXmqbfxcb() {
		return this.dlpgXmqbfxcb;
	}

	public void setDlpgXmqbfxcb(Double dlpgXmqbfxcb) {
		this.dlpgXmqbfxcb = dlpgXmqbfxcb;
	}

	@Column(name = "DLPG_XJ", precision = 20, scale = 4)
	public Double getDlpgXj() {
		return this.dlpgXj;
	}

	public void setDlpgXj(Double dlpgXj) {
		this.dlpgXj = dlpgXj;
	}

	@Column(name = "DLPG_PPP", precision = 20, scale = 4)
	public Double getDlpgPpp() {
		return this.dlpgPpp;
	}

	public void setDlpgPpp(Double dlpgPpp) {
		this.dlpgPpp = dlpgPpp;
	}

	@Column(name = "DLPG_VFM", precision = 20, scale = 4)
	public Double getDlpgVfm() {
		return this.dlpgVfm;
	}

	public void setDlpgVfm(Double dlpgVfm) {
		this.dlpgVfm = dlpgVfm;
	}

	@Column(name = "DXPG_JG", nullable = false, length = 1)
	public String getDxpgJg() {
		return this.dxpgJg;
	}

	public void setDxpgJg(String dxpgJg) {
		this.dxpgJg = dxpgJg;
	}

	@Column(name = "DLPG_JG", length = 1)
	public String getDlpgJg() {
		return this.dlpgJg;
	}

	public void setDlpgJg(String dlpgJg) {
		this.dlpgJg = dlpgJg;
	}

	@Column(name = "CZCSNL_YZJL", nullable = false, length = 1000)
	public String getCzcsnlYzjl() {
		return this.czcsnlYzjl;
	}

	public void setCzcsnlYzjl(String czcsnlYzjl) {
		this.czcsnlYzjl = czcsnlYzjl;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "CZCSNL_WCSJ", nullable = false, length = 7)
	public Date getCzcsnlWcsj() {
		return this.czcsnlWcsj;
	}

	public void setCzcsnlWcsj(Date czcsnlWcsj) {
		this.czcsnlWcsj = czcsnlWcsj;
	}

	@Column(name = "CZCSNL_YZJG", nullable = false, length = 1)
	public String getCzcsnlYzjg() {
		return this.czcsnlYzjg;
	}

	public void setCzcsnlYzjg(String czcsnlYzjg) {
		this.czcsnlYzjg = czcsnlYzjg;
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

	@Column(name = "DQZT", length = 2)
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

	@Column(name = "ZJJGDM", nullable = false, length = 20)
	public String getZjjgdm() {
		return this.zjjgdm;
	}

	public void setZjjgdm(String zjjgdm) {
		this.zjjgdm = zjjgdm;
	}

}
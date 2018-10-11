package com.wfzcx.ppp.xmkf.po;

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
 * TKfLzbgZjlb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_KF_LZBG_ZJLB")
@SequenceGenerator(name="SEQ_T_KF_LZBG_ZJLB",sequenceName="SEQ_T_KF_LZBG_ZJLB")
@GenericGenerator(name = "SEQ_T_KF_LZBG_ZJLB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_KF_LZBG_ZJLB") })
public class TKfLzbgZjlb implements java.io.Serializable {

	// Fields

	private Integer zjlbid;
	private Integer lzbgid;
	private String zjmc;
	private String zjlx;
	private String pbzy;
	private String scly;
	private String zjlxfs;
	private String bz;
	private Integer zjkid;

	// Constructors

	/** default constructor */
	public TKfLzbgZjlb() {
	}

	/** minimal constructor */
	public TKfLzbgZjlb(Integer zjlbid, Integer lzbgid, String zjmc,
			String zjlx, String pbzy) {
		this.zjlbid = zjlbid;
		this.lzbgid = lzbgid;
		this.zjmc = zjmc;
		this.zjlx = zjlx;
		this.pbzy = pbzy;
	}

	/** full constructor */
	public TKfLzbgZjlb(Integer zjlbid, Integer lzbgid, String zjmc,
			String zjlx, String pbzy, String scly, String zjlxfs, String bz,
			Integer zjkid) {
		this.zjlbid = zjlbid;
		this.lzbgid = lzbgid;
		this.zjmc = zjmc;
		this.zjlx = zjlx;
		this.pbzy = pbzy;
		this.scly = scly;
		this.zjlxfs = zjlxfs;
		this.bz = bz;
		this.zjkid = zjkid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_KF_LZBG_ZJLB")
	@Column(name = "ZJLBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getZjlbid() {
		return this.zjlbid;
	}

	public void setZjlbid(Integer zjlbid) {
		this.zjlbid = zjlbid;
	}

	@Column(name = "LZBGID", nullable = false, precision = 20, scale = 0)
	public Integer getLzbgid() {
		return this.lzbgid;
	}

	public void setLzbgid(Integer lzbgid) {
		this.lzbgid = lzbgid;
	}

	@Column(name = "ZJMC", nullable = false, length = 100)
	public String getZjmc() {
		return this.zjmc;
	}

	public void setZjmc(String zjmc) {
		this.zjmc = zjmc;
	}

	@Column(name = "ZJLX", nullable = false, length = 1)
	public String getZjlx() {
		return this.zjlx;
	}

	public void setZjlx(String zjlx) {
		this.zjlx = zjlx;
	}

	@Column(name = "PBZY", nullable = false, length = 100)
	public String getPbzy() {
		return this.pbzy;
	}

	public void setPbzy(String pbzy) {
		this.pbzy = pbzy;
	}

	@Column(name = "SCLY", length = 100)
	public String getScly() {
		return this.scly;
	}

	public void setScly(String scly) {
		this.scly = scly;
	}

	@Column(name = "ZJLXFS", length = 100)
	public String getZjlxfs() {
		return this.zjlxfs;
	}

	public void setZjlxfs(String zjlxfs) {
		this.zjlxfs = zjlxfs;
	}

	@Column(name = "BZ", length = 1000)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "ZJKID", precision = 20, scale = 0)
	public Integer getZjkid() {
		return this.zjkid;
	}

	public void setZjkid(Integer zjkid) {
		this.zjkid = zjkid;
	}

}
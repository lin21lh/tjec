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
 * TKfLzbgPjzb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_KF_LZBG_PJZB")
@SequenceGenerator(name="SEQ_T_KF_LZBG_PJZB",sequenceName="SEQ_T_KF_LZBG_PJZB")
@GenericGenerator(name = "SEQ_T_KF_LZBG_PJZB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_KF_LZBG_PJZB") })
public class TKfLzbgPjzb implements java.io.Serializable {

	// Fields

	private Integer pjzbid;
	private Integer zbkid;
	private String zbmc;
	private Byte qz;
	private String bz;
	private String zblb;
	private Integer lzbgid;

	// Constructors

	/** default constructor */
	public TKfLzbgPjzb() {
	}

	/** minimal constructor */
	public TKfLzbgPjzb(Integer pjzbid, Integer zbkid, String zbmc,
			Byte qz, String zblb, Integer lzbgid) {
		this.pjzbid = pjzbid;
		this.zbkid = zbkid;
		this.zbmc = zbmc;
		this.qz = qz;
		this.zblb = zblb;
		this.lzbgid = lzbgid;
	}

	/** full constructor */
	public TKfLzbgPjzb(Integer pjzbid, Integer zbkid, String zbmc,
			Byte qz, String bz, String zblb, Integer lzbgid) {
		this.pjzbid = pjzbid;
		this.zbkid = zbkid;
		this.zbmc = zbmc;
		this.qz = qz;
		this.bz = bz;
		this.zblb = zblb;
		this.lzbgid = lzbgid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_KF_LZBG_PJZB")
	@Column(name = "PJZBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getPjzbid() {
		return this.pjzbid;
	}

	public void setPjzbid(Integer pjzbid) {
		this.pjzbid = pjzbid;
	}

	@Column(name = "ZBKID", nullable = false, precision = 20, scale = 0)
	public Integer getZbkid() {
		return this.zbkid;
	}

	public void setZbkid(Integer zbkid) {
		this.zbkid = zbkid;
	}

	@Column(name = "ZBMC", nullable = false, length = 100)
	public String getZbmc() {
		return this.zbmc;
	}

	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}

	@Column(name = "QZ", nullable = false, precision = 2, scale = 0)
	public Byte getQz() {
		return this.qz;
	}

	public void setQz(Byte qz) {
		this.qz = qz;
	}

	@Column(name = "BZ", length = 1000)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "ZBLB", nullable = false, length = 1)
	public String getZblb() {
		return this.zblb;
	}

	public void setZblb(String zblb) {
		this.zblb = zblb;
	}

	@Column(name = "LZBGID", nullable = false, precision = 20, scale = 0)
	public Integer getLzbgid() {
		return this.lzbgid;
	}

	public void setLzbgid(Integer lzbgid) {
		this.lzbgid = lzbgid;
	}

}
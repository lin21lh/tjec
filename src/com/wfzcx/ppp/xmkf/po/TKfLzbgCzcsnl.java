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
 * TKfLzbgCzcsnl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_KF_LZBG_CZCSNL")
@SequenceGenerator(name="SEQ_T_KF_LZBG_CZCSNL",sequenceName="SEQ_T_KF_LZBG_CZCSNL")
@GenericGenerator(name = "SEQ_T_KF_LZBG_CZCSNL", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_T_KF_LZBG_CZCSNL") })
public class TKfLzbgCzcsnl implements java.io.Serializable {

	// Fields

	private Integer czcsnlid;
	private Integer lzbgid;
	private Double zcnd;
	private Double zcje;
	private Double gqtz;
	private Double yybt;
	private Double fxcd;
	private Double pttr;
	private Double tzhZcje;

	// Constructors

	/** default constructor */
	public TKfLzbgCzcsnl() {
	}

	/** minimal constructor */
	public TKfLzbgCzcsnl(Integer czcsnlid, Integer lzbgid, Double zcnd,
			Double zcje) {
		this.czcsnlid = czcsnlid;
		this.lzbgid = lzbgid;
		this.zcnd = zcnd;
		this.zcje = zcje;
	}

	/** full constructor */
	public TKfLzbgCzcsnl(Integer czcsnlid, Integer lzbgid, Double zcnd,
			Double zcje, Double gqtz, Double yybt, Double fxcd, Double pttr,
			Double tzhZcje) {
		this.czcsnlid = czcsnlid;
		this.lzbgid = lzbgid;
		this.zcnd = zcnd;
		this.zcje = zcje;
		this.gqtz = gqtz;
		this.yybt = yybt;
		this.fxcd = fxcd;
		this.pttr = pttr;
		this.tzhZcje = tzhZcje;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_KF_LZBG_CZCSNL")
	@Column(name = "CZCSNLID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getCzcsnlid() {
		return this.czcsnlid;
	}

	public void setCzcsnlid(Integer czcsnlid) {
		this.czcsnlid = czcsnlid;
	}

	@Column(name = "LZBGID", nullable = false, precision = 20, scale = 0)
	public Integer getLzbgid() {
		return this.lzbgid;
	}

	public void setLzbgid(Integer lzbgid) {
		this.lzbgid = lzbgid;
	}

	@Column(name = "ZCND", nullable = false, precision = 20, scale = 4)
	public Double getZcnd() {
		return this.zcnd;
	}

	public void setZcnd(Double zcnd) {
		this.zcnd = zcnd;
	}

	@Column(name = "ZCJE", nullable = false, precision = 20, scale = 4)
	public Double getZcje() {
		return this.zcje;
	}

	public void setZcje(Double zcje) {
		this.zcje = zcje;
	}

	@Column(name = "GQTZ", precision = 20, scale = 4)
	public Double getGqtz() {
		return this.gqtz;
	}

	public void setGqtz(Double gqtz) {
		this.gqtz = gqtz;
	}

	@Column(name = "YYBT", precision = 20, scale = 4)
	public Double getYybt() {
		return this.yybt;
	}

	public void setYybt(Double yybt) {
		this.yybt = yybt;
	}

	@Column(name = "FXCD", precision = 20, scale = 4)
	public Double getFxcd() {
		return this.fxcd;
	}

	public void setFxcd(Double fxcd) {
		this.fxcd = fxcd;
	}

	@Column(name = "PTTR", precision = 20, scale = 4)
	public Double getPttr() {
		return this.pttr;
	}

	public void setPttr(Double pttr) {
		this.pttr = pttr;
	}

	@Column(name = "TZH_ZCJE", precision = 20, scale = 4)
	public Double getTzhZcje() {
		return this.tzhZcje;
	}

	public void setTzhZcje(Double tzhZcje) {
		this.tzhZcje = tzhZcje;
	}

}
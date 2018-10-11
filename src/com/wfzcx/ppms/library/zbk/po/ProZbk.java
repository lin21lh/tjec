package com.wfzcx.ppms.library.zbk.po;

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
 * ProZbk entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_ZBK")
@SequenceGenerator(name="SEQ_PRO_ZBK",sequenceName="SEQ_PRO_ZBK")
@GenericGenerator(name = "SEQ_PRO_ZBK", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_ZBK") })
public class ProZbk implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer zbkid;
	private String zbmc;
	private String zbms;
	private String sfyx;
	private String zblb;

	// Constructors

	/** default constructor */
	public ProZbk() {
	}

	/** minimal constructor */
	public ProZbk(Integer zbkid) {
		this.zbkid = zbkid;
	}

	/** full constructor */
	public ProZbk(Integer zbkid, String zbmc, String zbms, String sfyx, String zblb) {
		this.zbkid = zbkid;
		this.zbmc = zbmc;
		this.zbms = zbms;
		this.sfyx = sfyx;
		this.zblb = zblb;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_ZBK")
	@Column(name = "ZBKID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getZbkid() {
		return this.zbkid;
	}

	public void setZbkid(Integer zbkid) {
		this.zbkid = zbkid;
	}

	@Column(name = "ZBMC", length = 100)
	public String getZbmc() {
		return this.zbmc;
	}

	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}

	@Column(name = "ZBMS", length = 1000)
	public String getZbms() {
		return this.zbms;
	}

	public void setZbms(String zbms) {
		this.zbms = zbms;
	}

	@Column(name = "SFYX", length = 1)
	public String getSfyx() {
		return this.sfyx;
	}

	public void setSfyx(String sfyx) {
		this.sfyx = sfyx;
	}

	@Column(name = "ZBLB", length = 1)
	public String getZblb() {
		return this.zblb;
	}

	public void setZblb(String zblb) {
		this.zblb = zblb;
	}

}
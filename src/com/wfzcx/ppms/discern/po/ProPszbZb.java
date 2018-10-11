package com.wfzcx.ppms.discern.po;

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
 * ProPszbZb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PSZB_ZB")
@SequenceGenerator(name="SEQ_PRO_PSZB_ZB",sequenceName="SEQ_PRO_PSZB_ZB")
@GenericGenerator(name = "SEQ_PRO_PSZB_ZB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PSZB_ZB") })
public class ProPszbZb implements java.io.Serializable {

	// Fields

	private Integer zbid;
	private Integer pszbid;
	private Integer zbkid;
	private Double qz;

	// Constructors

	/** default constructor */
	public ProPszbZb() {
	}

	/** full constructor */
	public ProPszbZb(Integer zbid, Integer pszbid, Integer zbkid,
			Double qz) {
		this.zbid = zbid;
		this.pszbid = pszbid;
		this.zbkid = zbkid;
		this.qz = qz;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PSZB_ZB")
	@Column(name = "ZBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getZbid() {
		return this.zbid;
	}

	public void setZbid(Integer zbid) {
		this.zbid = zbid;
	}

	@Column(name = "PSZBID", nullable = false, precision = 20, scale = 0)
	public Integer getPszbid() {
		return this.pszbid;
	}

	public void setPszbid(Integer pszbid) {
		this.pszbid = pszbid;
	}

	@Column(name = "ZBKID", nullable = false, precision = 20, scale = 0)
	public Integer getZbkid() {
		return this.zbkid;
	}

	public void setZbkid(Integer zbkid) {
		this.zbkid = zbkid;
	}

	@Column(name = "QZ", nullable = false, precision = 5)
	public Double getQz() {
		return this.qz;
	}

	public void setQz(Double qz) {
		this.qz = qz;
	}

}
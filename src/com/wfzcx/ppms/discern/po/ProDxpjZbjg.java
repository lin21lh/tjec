package com.wfzcx.ppms.discern.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProDxpjZbjg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DXPJ_ZBJG", uniqueConstraints = @UniqueConstraint(columnNames = {
		"QUALEXPERTID", "ZBID", "PSZBID" }))
@SequenceGenerator(name="SEQ_PRO_DXPJ_ZBJG",sequenceName="SEQ_PRO_DXPJ_ZBJG")
@GenericGenerator(name = "SEQ_PRO_DXPJ_ZBJG", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DXPJ_ZBJG") })
public class ProDxpjZbjg implements java.io.Serializable {

	// Fields

	private Integer zbjgid;
	private Integer zbid;
	private Integer qualexpertid;
	private Integer pszbid;
	private Double df;
	private Integer dxpjid;

	// Constructors

	/** default constructor */
	public ProDxpjZbjg() {
	}

	/** full constructor */
	public ProDxpjZbjg(Integer zbjgid, Integer zbid, Integer qualexpertid,
			Integer pszbid, Double df, Integer dxpjid) {
		this.zbjgid = zbjgid;
		this.zbid = zbid;
		this.qualexpertid = qualexpertid;
		this.pszbid = pszbid;
		this.df = df;
		this.dxpjid = dxpjid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DXPJ_ZBJG")
	@Column(name = "ZBJGID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getZbjgid() {
		return this.zbjgid;
	}

	public void setZbjgid(Integer zbjgid) {
		this.zbjgid = zbjgid;
	}

	@Column(name = "ZBID", nullable = false, precision = 20, scale = 0)
	public Integer getZbid() {
		return this.zbid;
	}

	public void setZbid(Integer zbid) {
		this.zbid = zbid;
	}

	@Column(name = "QUALEXPERTID", nullable = false, precision = 18, scale = 0)
	public Integer getQualexpertid() {
		return this.qualexpertid;
	}

	public void setQualexpertid(Integer qualexpertid) {
		this.qualexpertid = qualexpertid;
	}

	@Column(name = "PSZBID", nullable = false, precision = 20, scale = 0)
	public Integer getPszbid() {
		return this.pszbid;
	}

	public void setPszbid(Integer pszbid) {
		this.pszbid = pszbid;
	}

	@Column(name = "DF", nullable = false, precision = 5)
	public Double getDf() {
		return this.df;
	}

	public void setDf(Double df) {
		this.df = df;
	}

	@Column(name = "DXPJID", nullable = false, precision = 20, scale = 0)
	public Integer getDxpjid() {
		return this.dxpjid;
	}

	public void setDxpjid(Integer dxpjid) {
		this.dxpjid = dxpjid;
	}

}
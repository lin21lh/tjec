package com.wfzcx.ppms.wdjg.po;

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
 * ProWdjg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_WDJG")
@SequenceGenerator(name="SEQ_PRO_WDJG",sequenceName="SEQ_PRO_WDJG")
@GenericGenerator(name = "SEQ_PRO_WDJG", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_WDJG") })
public class ProWdjg implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer wdjgid;
	private String xmhj;
	private String hjfl;
	private String wdmc;
	private String glbm;
	private String glzd;
	private String bz;
	private Integer plsx;

	// Constructors

	/** default constructor */
	public ProWdjg() {
	}

	/** minimal constructor */
	public ProWdjg(Integer wdjgid, String xmhj, String hjfl, String wdmc, String glbm, String glzd) {
		this.wdjgid = wdjgid;
		this.xmhj = xmhj;
		this.hjfl = hjfl;
		this.wdmc = wdmc;
		this.glbm = glbm;
		this.glzd = glzd;
	}

	/** full constructor */
	public ProWdjg(Integer wdjgid, String xmhj, String hjfl, String wdmc, String glbm, String glzd, String bz,
			Integer plsx) {
		this.wdjgid = wdjgid;
		this.xmhj = xmhj;
		this.hjfl = hjfl;
		this.wdmc = wdmc;
		this.glbm = glbm;
		this.glzd = glzd;
		this.bz = bz;
		this.plsx = plsx;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_WDJG")
	@Column(name = "WDJGID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getWdjgid() {
		return this.wdjgid;
	}

	public void setWdjgid(Integer wdjgid) {
		this.wdjgid = wdjgid;
	}

	@Column(name = "XMHJ", nullable = false, length = 1)
	public String getXmhj() {
		return this.xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

	@Column(name = "HJFL", nullable = false, length = 2)
	public String getHjfl() {
		return this.hjfl;
	}

	public void setHjfl(String hjfl) {
		this.hjfl = hjfl;
	}

	@Column(name = "WDMC", nullable = false, length = 100)
	public String getWdmc() {
		return this.wdmc;
	}

	public void setWdmc(String wdmc) {
		this.wdmc = wdmc;
	}

	@Column(name = "GLBM", nullable = false, length = 50)
	public String getGlbm() {
		return this.glbm;
	}

	public void setGlbm(String glbm) {
		this.glbm = glbm;
	}

	@Column(name = "GLZD", nullable = false, length = 50)
	public String getGlzd() {
		return this.glzd;
	}

	public void setGlzd(String glzd) {
		this.glzd = glzd;
	}

	@Column(name = "BZ", length = 1000)
	public String getBz() {
		return this.bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	@Column(name = "PLSX", precision = 22, scale = 0)
	public Integer getPlsx() {
		return this.plsx;
	}

	public void setPlsx(Integer plsx) {
		this.plsx = plsx;
	}

}
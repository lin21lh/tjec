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
 * ProPszb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PSZB")
@SequenceGenerator(name="SEQ_PRO_PSZB",sequenceName="SEQ_PRO_PSZB")
@GenericGenerator(name = "SEQ_PRO_PSZB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PSZB") })
public class ProPszb implements java.io.Serializable {

	// Fields

	private Integer pszbid;
	private Integer projectid;
	private String status;
	private String vfmDlpj;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String xmhj;

	// Constructors

	/** default constructor */
	public ProPszb() {
	}

	/** minimal constructor */
	public ProPszb(Integer pszbid, Integer projectid, String status,
			String vfmDlpj, String xmhj) {
		this.pszbid = pszbid;
		this.projectid = projectid;
		this.status = status;
		this.vfmDlpj = vfmDlpj;
		this.xmhj = xmhj;
	}

	/** full constructor */
	public ProPszb(Integer pszbid, Integer projectid, String status,
			String vfmDlpj, String createuser, String createtime,
			String updateuser, String updatetime, String xmhj) {
		this.pszbid = pszbid;
		this.projectid = projectid;
		this.status = status;
		this.vfmDlpj = vfmDlpj;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.xmhj = xmhj;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PSZB")
	@Column(name = "PSZBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getPszbid() {
		return this.pszbid;
	}

	public void setPszbid(Integer pszbid) {
		this.pszbid = pszbid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "VFM_DLPJ", nullable = false, length = 1)
	public String getVfmDlpj() {
		return this.vfmDlpj;
	}

	public void setVfmDlpj(String vfmDlpj) {
		this.vfmDlpj = vfmDlpj;
	}

	@Column(name = "CREATEUSER", length = 20)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Column(name = "CREATETIME", length = 20)
	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Column(name = "UPDATEUSER", length = 20)
	public String getUpdateuser() {
		return this.updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	@Column(name = "UPDATETIME", length = 20)
	public String getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "XMHJ", nullable = false, length = 1)
	public String getXmhj() {
		return this.xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

}
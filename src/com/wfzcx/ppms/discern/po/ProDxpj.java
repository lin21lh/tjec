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
 * ProDxpj entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DXPJ",uniqueConstraints = @UniqueConstraint(columnNames = {
		"PROJECTID", "XMHJ" }))
@SequenceGenerator(name="SEQ_PRO_DXPJ",sequenceName="SEQ_PRO_DXPJ")
@GenericGenerator(name = "SEQ_PRO_DXPJ", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DXPJ") })
public class ProDxpj implements java.io.Serializable {

	// Fields

	private Integer dxpjid;
	private Integer projectid;
	private String status;
	private String xmhj;
	private String qualResult;
	private String qualPath;
	private String qualConclusion;
	private String qualVerifytime;
	private String vfmDlpj;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String qualDf;

	// Constructors

	/** default constructor */
	public ProDxpj() {
	}

	/** minimal constructor */
	public ProDxpj(Integer dxpjid, Integer projectid, String status,
			String xmhj, String qualResult, String qualVerifytime,
			String vfmDlpj) {
		this.dxpjid = dxpjid;
		this.projectid = projectid;
		this.status = status;
		this.xmhj = xmhj;
		this.qualResult = qualResult;
		this.qualVerifytime = qualVerifytime;
		this.vfmDlpj = vfmDlpj;
	}

	/** full constructor */
	public ProDxpj(Integer dxpjid, Integer projectid, String status,
			String xmhj, String qualResult, String qualPath,
			String qualConclusion, String qualVerifytime, String vfmDlpj,
			String createuser, String createtime, String updateuser,
			String updatetime, String qualDf) {
		this.dxpjid = dxpjid;
		this.projectid = projectid;
		this.status = status;
		this.xmhj = xmhj;
		this.qualResult = qualResult;
		this.qualPath = qualPath;
		this.qualConclusion = qualConclusion;
		this.qualVerifytime = qualVerifytime;
		this.vfmDlpj = vfmDlpj;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.qualDf = qualDf;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DXPJ")
	@Column(name = "DXPJID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getDxpjid() {
		return this.dxpjid;
	}

	public void setDxpjid(Integer dxpjid) {
		this.dxpjid = dxpjid;
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

	@Column(name = "XMHJ", nullable = false, length = 1)
	public String getXmhj() {
		return this.xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

	@Column(name = "QUAL_RESULT", nullable = false, length = 20)
	public String getQualResult() {
		return this.qualResult;
	}

	public void setQualResult(String qualResult) {
		this.qualResult = qualResult;
	}

	@Column(name = "QUAL_PATH", length = 100)
	public String getQualPath() {
		return this.qualPath;
	}

	public void setQualPath(String qualPath) {
		this.qualPath = qualPath;
	}

	@Column(name = "QUAL_CONCLUSION", length = 1000)
	public String getQualConclusion() {
		return this.qualConclusion;
	}

	public void setQualConclusion(String qualConclusion) {
		this.qualConclusion = qualConclusion;
	}

	@Column(name = "QUAL_VERIFYTIME", nullable = false, length = 20)
	public String getQualVerifytime() {
		return this.qualVerifytime;
	}

	public void setQualVerifytime(String qualVerifytime) {
		this.qualVerifytime = qualVerifytime;
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

	@Column(name = "QUAL_DF", precision = 5)
	public String getQualDf() {
		return this.qualDf;
	}

	public void setQualDf(String qualDf) {
		this.qualDf = qualDf;
	}

}
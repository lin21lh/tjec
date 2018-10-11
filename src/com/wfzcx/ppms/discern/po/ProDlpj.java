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
 * ProDlpj entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DLPJ",uniqueConstraints = @UniqueConstraint(columnNames = {
		"DXPJID", "XMHJ", "PROJECTID" }))
@SequenceGenerator(name="SEQ_PRO_DLPJ",sequenceName="SEQ_PRO_DLPJ")
@GenericGenerator(name = "SEQ_PRO_DLPJ", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DLPJ") })
public class ProDlpj implements java.io.Serializable {

	// Fields

	private Integer dlpjid;
	private Integer dxpjid;
	private String vomConclusion;
	private Double vomNetcost;
	private Double vomAdjust;
	private Double vomRiskcost;
	private Double vomPsc;
	private Double vomPpp;
	private Double vomVfm;
	private String vomResult;
	private String vomEvaluate;
	private String vomVerifytime;
	private String vomAttachment;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String xmhj;
	private Integer projectid;

	// Constructors

	/** default constructor */
	public ProDlpj() {
	}

	/** minimal constructor */
	public ProDlpj(Integer dlpjid, Integer dxpjid, String status,
			String createuser, String createtime, String xmhj,
			Integer projectid) {
		this.dlpjid = dlpjid;
		this.dxpjid = dxpjid;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.xmhj = xmhj;
		this.projectid = projectid;
	}

	/** full constructor */
	public ProDlpj(Integer dlpjid, Integer dxpjid, String vomConclusion,
			Double vomNetcost, Double vomAdjust, Double vomRiskcost,
			Double vomPsc, Double vomPpp, Double vomVfm, String vomResult,
			String vomEvaluate, String vomVerifytime, String vomAttachment,
			String status, String createuser, String createtime,
			String updateuser, String updatetime, String xmhj,
			Integer projectid) {
		this.dlpjid = dlpjid;
		this.dxpjid = dxpjid;
		this.vomConclusion = vomConclusion;
		this.vomNetcost = vomNetcost;
		this.vomAdjust = vomAdjust;
		this.vomRiskcost = vomRiskcost;
		this.vomPsc = vomPsc;
		this.vomPpp = vomPpp;
		this.vomVfm = vomVfm;
		this.vomResult = vomResult;
		this.vomEvaluate = vomEvaluate;
		this.vomVerifytime = vomVerifytime;
		this.vomAttachment = vomAttachment;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.xmhj = xmhj;
		this.projectid = projectid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DLPJ")
	@Column(name = "DLPJID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getDlpjid() {
		return this.dlpjid;
	}

	public void setDlpjid(Integer dlpjid) {
		this.dlpjid = dlpjid;
	}

	@Column(name = "DXPJID", nullable = false, precision = 20, scale = 0)
	public Integer getDxpjid() {
		return this.dxpjid;
	}

	public void setDxpjid(Integer dxpjid) {
		this.dxpjid = dxpjid;
	}

	@Column(name = "VOM_CONCLUSION", length = 1000)
	public String getVomConclusion() {
		return this.vomConclusion;
	}

	public void setVomConclusion(String vomConclusion) {
		this.vomConclusion = vomConclusion;
	}

	@Column(name = "VOM_NETCOST", precision = 16)
	public Double getVomNetcost() {
		return this.vomNetcost;
	}

	public void setVomNetcost(Double vomNetcost) {
		this.vomNetcost = vomNetcost;
	}

	@Column(name = "VOM_ADJUST", precision = 16)
	public Double getVomAdjust() {
		return this.vomAdjust;
	}

	public void setVomAdjust(Double vomAdjust) {
		this.vomAdjust = vomAdjust;
	}

	@Column(name = "VOM_RISKCOST", precision = 16)
	public Double getVomRiskcost() {
		return this.vomRiskcost;
	}

	public void setVomRiskcost(Double vomRiskcost) {
		this.vomRiskcost = vomRiskcost;
	}

	@Column(name = "VOM_PSC", precision = 16)
	public Double getVomPsc() {
		return this.vomPsc;
	}

	public void setVomPsc(Double vomPsc) {
		this.vomPsc = vomPsc;
	}

	@Column(name = "VOM_PPP", precision = 16)
	public Double getVomPpp() {
		return this.vomPpp;
	}

	public void setVomPpp(Double vomPpp) {
		this.vomPpp = vomPpp;
	}

	@Column(name = "VOM_VFM", precision = 16)
	public Double getVomVfm() {
		return this.vomVfm;
	}

	public void setVomVfm(Double vomVfm) {
		this.vomVfm = vomVfm;
	}

	@Column(name = "VOM_RESULT", length = 1)
	public String getVomResult() {
		return this.vomResult;
	}

	public void setVomResult(String vomResult) {
		this.vomResult = vomResult;
	}

	@Column(name = "VOM_EVALUATE", length = 1000)
	public String getVomEvaluate() {
		return this.vomEvaluate;
	}

	public void setVomEvaluate(String vomEvaluate) {
		this.vomEvaluate = vomEvaluate;
	}

	@Column(name = "VOM_VERIFYTIME", length = 20)
	public String getVomVerifytime() {
		return this.vomVerifytime;
	}

	public void setVomVerifytime(String vomVerifytime) {
		this.vomVerifytime = vomVerifytime;
	}

	@Column(name = "VOM_ATTACHMENT", length = 100)
	public String getVomAttachment() {
		return this.vomAttachment;
	}

	public void setVomAttachment(String vomAttachment) {
		this.vomAttachment = vomAttachment;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATEUSER", nullable = false, length = 20)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Column(name = "CREATETIME", nullable = false, length = 20)
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

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

}
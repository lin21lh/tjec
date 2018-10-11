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
 * ProApprove entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_APPROVE")
@SequenceGenerator(name="SEQ_PRO_APPROVE",sequenceName="SEQ_PRO_APPROVE")
@GenericGenerator(name = "SEQ_PRO_APPROVE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_APPROVE") })
public class ProApprove implements java.io.Serializable {

	// Fields

	private Integer vomid;
	private Integer projectid;
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
	private String fcConclusion;
	private String fcResult;
	private String fcEvaluate;
	private String fcVerifytime;
	private String fcAttachment;
	private String zfVerifytime;
	private String zfAttachment;
	private String zfOpinion;
	private String czResult;
	private String czOpinion;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String qualResult;
	private String qualPath;
	private String qualConclusion;
	private String qualVerifytime;

	// Constructors
	/** default constructor */
	public ProApprove() {
	}

	/** minimal constructor */
	public ProApprove(Integer vomid, Integer projectid,
			String vomConclusion, Double vomNetcost, Double vomAdjust,
			Double vomRiskcost, Double vomPsc, Double vomPpp, Double vomVfm,
			String vomResult, String vomEvaluate, String vomVerifytime,
			String fcConclusion, String fcResult, String fcEvaluate,
			String fcVerifytime, String zfVerifytime, String zfAttachment,
			String zfOpinion, String czResult, String czOpinion,
			String createuser, String createtime) {
		this.vomid = vomid;
		this.projectid = projectid;
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
		this.fcConclusion = fcConclusion;
		this.fcResult = fcResult;
		this.fcEvaluate = fcEvaluate;
		this.fcVerifytime = fcVerifytime;
		this.zfVerifytime = zfVerifytime;
		this.zfAttachment = zfAttachment;
		this.zfOpinion = zfOpinion;
		this.czResult = czResult;
		this.czOpinion = czOpinion;
		this.createuser = createuser;
		this.createtime = createtime;
	}

	/** full constructor */
	public ProApprove(Integer vomid, Integer projectid,
			String vomConclusion, Double vomNetcost, Double vomAdjust,
			Double vomRiskcost, Double vomPsc, Double vomPpp, Double vomVfm,
			String vomResult, String vomEvaluate, String vomVerifytime,
			String vomAttachment, String fcConclusion, String fcResult,
			String fcEvaluate, String fcVerifytime, String fcAttachment,
			String zfVerifytime, String zfAttachment, String zfOpinion,
			String czResult, String czOpinion, String createuser,
			String createtime, String updateuser, String updatetime) {
		this.vomid = vomid;
		this.projectid = projectid;
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
		this.fcConclusion = fcConclusion;
		this.fcResult = fcResult;
		this.fcEvaluate = fcEvaluate;
		this.fcVerifytime = fcVerifytime;
		this.fcAttachment = fcAttachment;
		this.zfVerifytime = zfVerifytime;
		this.zfAttachment = zfAttachment;
		this.zfOpinion = zfOpinion;
		this.czResult = czResult;
		this.czOpinion = czOpinion;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_APPROVE")
	@Column(name = "VOMID", unique = true, precision = 20,nullable = false,  scale = 0)
	public Integer getVomid() {
		return this.vomid;
	}

	public void setVomid(Integer vomid) {
		this.vomid = vomid;
	}

	@Column(name = "PROJECTID", precision = 20,nullable = false, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "VOM_CONCLUSION",length = 1000)
	public String getVomConclusion() {
		return this.vomConclusion;
	}

	public void setVomConclusion(String vomConclusion) {
		this.vomConclusion = vomConclusion;
	}

	@Column(name = "VOM_NETCOST",  precision = 16)
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

	@Column(name = "VOM_PSC",  precision = 16)
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

	@Column(name = "FC_CONCLUSION", length = 1000)
	public String getFcConclusion() {
		return this.fcConclusion;
	}

	public void setFcConclusion(String fcConclusion) {
		this.fcConclusion = fcConclusion;
	}

	@Column(name = "FC_RESULT", length = 1)
	public String getFcResult() {
		return this.fcResult;
	}

	public void setFcResult(String fcResult) {
		this.fcResult = fcResult;
	}

	@Column(name = "FC_EVALUATE", length = 1000)
	public String getFcEvaluate() {
		return this.fcEvaluate;
	}

	public void setFcEvaluate(String fcEvaluate) {
		this.fcEvaluate = fcEvaluate;
	}

	@Column(name = "FC_VERIFYTIME", length = 20)
	public String getFcVerifytime() {
		return this.fcVerifytime;
	}

	public void setFcVerifytime(String fcVerifytime) {
		this.fcVerifytime = fcVerifytime;
	}

	@Column(name = "FC_ATTACHMENT", length = 100)
	public String getFcAttachment() {
		return this.fcAttachment;
	}

	public void setFcAttachment(String fcAttachment) {
		this.fcAttachment = fcAttachment;
	}

	@Column(name = "ZF_VERIFYTIME", length = 20)
	public String getZfVerifytime() {
		return this.zfVerifytime;
	}

	public void setZfVerifytime(String zfVerifytime) {
		this.zfVerifytime = zfVerifytime;
	}

	@Column(name = "ZF_ATTACHMENT", length = 100)
	public String getZfAttachment() {
		return this.zfAttachment;
	}

	public void setZfAttachment(String zfAttachment) {
		this.zfAttachment = zfAttachment;
	}

	@Column(name = "ZF_OPINION", length = 1000)
	public String getZfOpinion() {
		return this.zfOpinion;
	}

	public void setZfOpinion(String zfOpinion) {
		this.zfOpinion = zfOpinion;
	}

	@Column(name = "CZ_RESULT", length = 1)
	public String getCzResult() {
		return this.czResult;
	}

	public void setCzResult(String czResult) {
		this.czResult = czResult;
	}

	@Column(name = "CZ_OPINION", length = 1000)
	public String getCzOpinion() {
		return this.czOpinion;
	}

	public void setCzOpinion(String czOpinion) {
		this.czOpinion = czOpinion;
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
	
	@Column(name = "QUAL_RESULT", length = 20)
	public String getQualResult() {
		return qualResult;
	}

	public void setQualResult(String qualResult) {
		this.qualResult = qualResult;
	}

	@Column(name = "QUAL_PATH", length = 100)
	public String getQualPath() {
		return qualPath;
	}

	public void setQualPath(String qualPath) {
		this.qualPath = qualPath;
	}

	@Column(name = "QUAL_CONCLUSION", length = 1000)
	public String getQualConclusion() {
		return qualConclusion;
	}

	public void setQualConclusion(String qualConclusion) {
		this.qualConclusion = qualConclusion;
	}

	@Column(name = "QUAL_VERIFYTIME", length = 20)
	public String getQualVerifytime() {
		return qualVerifytime;
	}

	public void setQualVerifytime(String qualVerifytime) {
		this.qualVerifytime = qualVerifytime;
	}


}
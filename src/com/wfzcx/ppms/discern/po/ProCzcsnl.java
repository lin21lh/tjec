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
 * ProCzcsnl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_CZCSNL",uniqueConstraints = @UniqueConstraint(columnNames = {
		"PROJECTID", "XMHJ" }))
@SequenceGenerator(name="SEQ_PRO_CZCSNL",sequenceName="SEQ_PRO_CZCSNL")
@GenericGenerator(name = "SEQ_PRO_CZCSNL", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_CZCSNL") })
public class ProCzcsnl implements java.io.Serializable {

	// Fields

	private Integer czcsnlid;
	private Integer projectid;
	private Integer dlpjid;
	private String xmhj;
	private String fcConclusion;
	private String fcResult;
	private String fcEvaluate;
	private String fcVerifytime;
	private String fcAttachment;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String status;
	private Integer dxpjid;

	// Constructors

	/** default constructor */
	public ProCzcsnl() {
	}

	/** minimal constructor */
	public ProCzcsnl(Integer czcsnlid, Integer projectid,
			Integer dlpjid, String xmhj, String fcResult, String createuser,
			String createtime, String status, Integer dxpjid) {
		this.czcsnlid = czcsnlid;
		this.projectid = projectid;
		this.dlpjid = dlpjid;
		this.xmhj = xmhj;
		this.fcResult = fcResult;
		this.createuser = createuser;
		this.createtime = createtime;
		this.status = status;
		this.dxpjid = dxpjid;
	}

	/** full constructor */
	public ProCzcsnl(Integer czcsnlid, Integer projectid,
			Integer dlpjid, String xmhj, String fcConclusion,
			String fcResult, String fcEvaluate, String fcVerifytime,
			String fcAttachment, String createuser, String createtime,
			String updateuser, String updatetime, String status,
			Integer dxpjid) {
		this.czcsnlid = czcsnlid;
		this.projectid = projectid;
		this.dlpjid = dlpjid;
		this.xmhj = xmhj;
		this.fcConclusion = fcConclusion;
		this.fcResult = fcResult;
		this.fcEvaluate = fcEvaluate;
		this.fcVerifytime = fcVerifytime;
		this.fcAttachment = fcAttachment;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.status = status;
		this.dxpjid = dxpjid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_CZCSNL")
	@Column(name = "CZCSNLID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getCzcsnlid() {
		return this.czcsnlid;
	}

	public void setCzcsnlid(Integer czcsnlid) {
		this.czcsnlid = czcsnlid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "DLPJID", nullable = false, precision = 20, scale = 0)
	public Integer getDlpjid() {
		return this.dlpjid;
	}

	public void setDlpjid(Integer dlpjid) {
		this.dlpjid = dlpjid;
	}

	@Column(name = "XMHJ", nullable = false, length = 1)
	public String getXmhj() {
		return this.xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

	@Column(name = "FC_CONCLUSION", length = 1000)
	public String getFcConclusion() {
		return this.fcConclusion;
	}

	public void setFcConclusion(String fcConclusion) {
		this.fcConclusion = fcConclusion;
	}

	@Column(name = "FC_RESULT", nullable = false, length = 1)
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

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "DXPJID", nullable = false, precision = 20, scale = 0)
	public Integer getDxpjid() {
		return this.dxpjid;
	}

	public void setDxpjid(Integer dxpjid) {
		this.dxpjid = dxpjid;
	}

}
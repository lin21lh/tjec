package com.wfzcx.ppms.procurement.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProPurchaseResult entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PURCHASE_RESULT")
@GenericGenerator(name = "SEQ_PRO_PURCHASE_RESULT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PURCHASE_RESULT") })
public class ProPurchaseResult implements java.io.Serializable {

	// Fields

	private int purchaseid;
	private int projectid;
	private String purchaseNoticeTime;
	private String purchaseNoticeMedia;
	private String fileCommitTime;
	private String fileJudgeTime;
	private String negotiateTime;
	private String govVerifyTime;
	private String contractTime;
	private String contractPublishTime;
	private String contractPublishMedia;
	private String contractPath;
	private String remark;
	private String wfid;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String datatype;
	private String orgcode;

	// Constructors

	/** default constructor */
	public ProPurchaseResult() {
	}

	/** minimal constructor */
	public ProPurchaseResult(int purchaseid, int projectid,
			String purchaseNoticeTime, String purchaseNoticeMedia,
			String fileCommitTime, String fileJudgeTime, String negotiateTime,
			String govVerifyTime, String contractTime,
			String contractPublishTime, String contractPublishMedia,
			String status, String datatype) {
		this.purchaseid = purchaseid;
		this.projectid = projectid;
		this.purchaseNoticeTime = purchaseNoticeTime;
		this.purchaseNoticeMedia = purchaseNoticeMedia;
		this.fileCommitTime = fileCommitTime;
		this.fileJudgeTime = fileJudgeTime;
		this.negotiateTime = negotiateTime;
		this.govVerifyTime = govVerifyTime;
		this.contractTime = contractTime;
		this.contractPublishTime = contractPublishTime;
		this.contractPublishMedia = contractPublishMedia;
		this.status = status;
		this.datatype = datatype;
	}

	/** full constructor */
	public ProPurchaseResult(int purchaseid, int projectid,
			String purchaseNoticeTime, String purchaseNoticeMedia,
			String fileCommitTime, String fileJudgeTime, String negotiateTime,
			String govVerifyTime, String contractTime,
			String contractPublishTime, String contractPublishMedia,
			String contractPath, String remark, String wfid, String status,
			String createuser, String createtime, String updateuser,
			String updatetime, String datatype) {
		this.purchaseid = purchaseid;
		this.projectid = projectid;
		this.purchaseNoticeTime = purchaseNoticeTime;
		this.purchaseNoticeMedia = purchaseNoticeMedia;
		this.fileCommitTime = fileCommitTime;
		this.fileJudgeTime = fileJudgeTime;
		this.negotiateTime = negotiateTime;
		this.govVerifyTime = govVerifyTime;
		this.contractTime = contractTime;
		this.contractPublishTime = contractPublishTime;
		this.contractPublishMedia = contractPublishMedia;
		this.contractPath = contractPath;
		this.remark = remark;
		this.wfid = wfid;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.datatype = datatype;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PURCHASE_RESULT")
	@Column(name = "PURCHASEID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getPurchaseid() {
		return this.purchaseid;
	}

	public void setPurchaseid(int purchaseid) {
		this.purchaseid = purchaseid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	@Column(name = "PURCHASE_NOTICE_TIME", nullable = false, length = 20)
	public String getPurchaseNoticeTime() {
		return this.purchaseNoticeTime;
	}

	public void setPurchaseNoticeTime(String purchaseNoticeTime) {
		this.purchaseNoticeTime = purchaseNoticeTime;
	}

	@Column(name = "PURCHASE_NOTICE_MEDIA", nullable = false, length = 200)
	public String getPurchaseNoticeMedia() {
		return this.purchaseNoticeMedia;
	}

	public void setPurchaseNoticeMedia(String purchaseNoticeMedia) {
		this.purchaseNoticeMedia = purchaseNoticeMedia;
	}

	@Column(name = "FILE_COMMIT_TIME", nullable = false, length = 20)
	public String getFileCommitTime() {
		return this.fileCommitTime;
	}

	public void setFileCommitTime(String fileCommitTime) {
		this.fileCommitTime = fileCommitTime;
	}

	@Column(name = "FILE_JUDGE_TIME", nullable = false, length = 20)
	public String getFileJudgeTime() {
		return this.fileJudgeTime;
	}

	public void setFileJudgeTime(String fileJudgeTime) {
		this.fileJudgeTime = fileJudgeTime;
	}

	@Column(name = "NEGOTIATE_TIME", nullable = false, length = 20)
	public String getNegotiateTime() {
		return this.negotiateTime;
	}

	public void setNegotiateTime(String negotiateTime) {
		this.negotiateTime = negotiateTime;
	}

	@Column(name = "GOV_VERIFY_TIME", nullable = false, length = 20)
	public String getGovVerifyTime() {
		return this.govVerifyTime;
	}

	public void setGovVerifyTime(String govVerifyTime) {
		this.govVerifyTime = govVerifyTime;
	}

	@Column(name = "CONTRACT_TIME", nullable = false, length = 20)
	public String getContractTime() {
		return this.contractTime;
	}

	public void setContractTime(String contractTime) {
		this.contractTime = contractTime;
	}

	@Column(name = "CONTRACT_PUBLISH_TIME", nullable = false, length = 20)
	public String getContractPublishTime() {
		return this.contractPublishTime;
	}

	public void setContractPublishTime(String contractPublishTime) {
		this.contractPublishTime = contractPublishTime;
	}

	@Column(name = "CONTRACT_PUBLISH_MEDIA", nullable = false, length = 200)
	public String getContractPublishMedia() {
		return this.contractPublishMedia;
	}

	public void setContractPublishMedia(String contractPublishMedia) {
		this.contractPublishMedia = contractPublishMedia;
	}

	@Column(name = "CONTRACT_PATH", length = 200)
	public String getContractPath() {
		return this.contractPath;
	}

	public void setContractPath(String contractPath) {
		this.contractPath = contractPath;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "STATUS", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Column(name = "DATATYPE", nullable = false, length = 2)
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	
	@Column(name = "ORGCODE", length = 50)
	public String getOrgcode() {
		return orgcode;
	}

}
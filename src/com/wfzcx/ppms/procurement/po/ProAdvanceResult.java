package com.wfzcx.ppms.procurement.po;

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
 * ProAdvanceResult entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_ADVANCE_RESULT")
@SequenceGenerator(name="SEQ_PRO_ADVANCE_RESULT",sequenceName="SEQ_PRO_ADVANCE_RESULT")
@GenericGenerator(name = "SEQ_PRO_ADVANCE_RESULT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_ADVANCE_RESULT") })
public class ProAdvanceResult implements java.io.Serializable {

	// Fields

	private Integer advanceid;
	private Integer projectid;
	private String noticeTime;
	private String publishMedia;
	private String inquiryTime;
	private String inquiryResult;
	private String inquiryDeclare;
	private String inquiryDeclarePath;
	private String wfid;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String datatype;
	private String orgcode;

	// Constructors
	@Column(name = "ORGCODE")
	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	/** default constructor */
	public ProAdvanceResult() {
	}

	/** minimal constructor */
	public ProAdvanceResult(Integer advanceid, Integer projectid,
			String noticeTime, String publishMedia, String inquiryTime,
			String inquiryResult, String inquiryDeclare,
			String inquiryDeclarePath, String status, String datatype) {
		this.advanceid = advanceid;
		this.projectid = projectid;
		this.noticeTime = noticeTime;
		this.publishMedia = publishMedia;
		this.inquiryTime = inquiryTime;
		this.inquiryResult = inquiryResult;
		this.inquiryDeclare = inquiryDeclare;
		this.inquiryDeclarePath = inquiryDeclarePath;
		this.status = status;
		this.datatype = datatype;
	}

	/** full constructor */
	public ProAdvanceResult(Integer advanceid, Integer projectid,
			String noticeTime, String publishMedia, String inquiryTime,
			String inquiryResult, String inquiryDeclare,
			String inquiryDeclarePath, String wfid, String status,
			String createuser, String createtime, String updateuser,
			String updatetime, String datatype) {
		this.advanceid = advanceid;
		this.projectid = projectid;
		this.noticeTime = noticeTime;
		this.publishMedia = publishMedia;
		this.inquiryTime = inquiryTime;
		this.inquiryResult = inquiryResult;
		this.inquiryDeclare = inquiryDeclare;
		this.inquiryDeclarePath = inquiryDeclarePath;
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
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_ADVANCE_RESULT")
	@Column(name = "ADVANCEID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getAdvanceid() {
		return this.advanceid;
	}

	public void setAdvanceid(Integer advanceid) {
		this.advanceid = advanceid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "NOTICE_TIME", nullable = false, length = 20)
	public String getNoticeTime() {
		return this.noticeTime;
	}

	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	@Column(name = "PUBLISH_MEDIA", nullable = false, length = 200)
	public String getPublishMedia() {
		return this.publishMedia;
	}

	public void setPublishMedia(String publishMedia) {
		this.publishMedia = publishMedia;
	}

	@Column(name = "INQUIRY_TIME", nullable = false, length = 20)
	public String getInquiryTime() {
		return this.inquiryTime;
	}

	public void setInquiryTime(String inquiryTime) {
		this.inquiryTime = inquiryTime;
	}

	@Column(name = "INQUIRY_RESULT", nullable = false, length = 1)
	public String getInquiryResult() {
		return this.inquiryResult;
	}

	public void setInquiryResult(String inquiryResult) {
		this.inquiryResult = inquiryResult;
	}

	@Column(name = "INQUIRY_DECLARE", nullable = false, length = 1000)
	public String getInquiryDeclare() {
		return this.inquiryDeclare;
	}

	public void setInquiryDeclare(String inquiryDeclare) {
		this.inquiryDeclare = inquiryDeclare;
	}

	@Column(name = "INQUIRY_DECLARE_PATH",length = 200)
	public String getInquiryDeclarePath() {
		return this.inquiryDeclarePath;
	}

	public void setInquiryDeclarePath(String inquiryDeclarePath) {
		this.inquiryDeclarePath = inquiryDeclarePath;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "STATUS", nullable = false, length = 10)
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

}
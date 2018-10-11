package com.wfzcx.ppms.transfer.po;

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
 * ProTransfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_TRANSFER")
@SequenceGenerator(name="SEQ_PRO_TRANSFER",sequenceName="SEQ_PRO_TRANSFER")
@GenericGenerator(name = "SEQ_PRO_TRANSFER", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_TRANSFER") })
public class ProTransfer implements java.io.Serializable {

	// Fields

	private Integer transferid;
	private Integer projectid;
	private String transferStartTime;
	private String transferEndTime;
	private String assessTime;
	private String transferProperty;
	private String projectPerformance;
	private String assetAssessPath;
	private String transferPropertyPath;
	private String projectPerformancePath;
	private String remark;
	private String wfid;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String assetAssess;
	private String orgcode;
	
	@Column(name = "ORGCODE")
	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	// Constructors
	@Column(name = "ASSET_ASSESS")
	public String getAssetAssess() {
		return assetAssess;
	}

	public void setAssetAssess(String assetAssess) {
		this.assetAssess = assetAssess;
	}

	/** default constructor */
	public ProTransfer() {
	}

	/** minimal constructor */
	public ProTransfer(Integer transferid, Integer projectid,
			String transferStartTime, String transferEndTime,
			String assessTime, String transferProperty,
			String projectPerformance, String status, String createtime,
			String updateuser) {
		this.transferid = transferid;
		this.projectid = projectid;
		this.transferStartTime = transferStartTime;
		this.transferEndTime = transferEndTime;
		this.assessTime = assessTime;
		this.transferProperty = transferProperty;
		this.projectPerformance = projectPerformance;
		this.status = status;
		this.createtime = createtime;
		this.updateuser = updateuser;
	}

	/** full constructor */
	public ProTransfer(Integer transferid, Integer projectid,
			String transferStartTime, String transferEndTime,
			String assessTime, String transferProperty,
			String projectPerformance, String assetAssessPath,
			String transferPropertyPath, String projectPerformancePath,
			String remark, String wfid, String status, String createuser,
			String createtime, String updateuser, String updatetime) {
		this.transferid = transferid;
		this.projectid = projectid;
		this.transferStartTime = transferStartTime;
		this.transferEndTime = transferEndTime;
		this.assessTime = assessTime;
		this.transferProperty = transferProperty;
		this.projectPerformance = projectPerformance;
		this.assetAssessPath = assetAssessPath;
		this.transferPropertyPath = transferPropertyPath;
		this.projectPerformancePath = projectPerformancePath;
		this.remark = remark;
		this.wfid = wfid;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_TRANSFER")
	@Column(name = "TRANSFERID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getTransferid() {
		return this.transferid;
	}

	public void setTransferid(Integer transferid) {
		this.transferid = transferid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "TRANSFER_START_TIME", nullable = false, length = 20)
	public String getTransferStartTime() {
		return this.transferStartTime;
	}

	public void setTransferStartTime(String transferStartTime) {
		this.transferStartTime = transferStartTime;
	}

	@Column(name = "TRANSFER_END_TIME", nullable = false, length = 20)
	public String getTransferEndTime() {
		return this.transferEndTime;
	}

	public void setTransferEndTime(String transferEndTime) {
		this.transferEndTime = transferEndTime;
	}

	@Column(name = "ASSESS_TIME", nullable = false, length = 20)
	public String getAssessTime() {
		return this.assessTime;
	}

	public void setAssessTime(String assessTime) {
		this.assessTime = assessTime;
	}

	@Column(name = "TRANSFER_PROPERTY", nullable = false, length = 1000)
	public String getTransferProperty() {
		return this.transferProperty;
	}

	public void setTransferProperty(String transferProperty) {
		this.transferProperty = transferProperty;
	}

	@Column(name = "PROJECT_PERFORMANCE", nullable = false, length = 1000)
	public String getProjectPerformance() {
		return this.projectPerformance;
	}

	public void setProjectPerformance(String projectPerformance) {
		this.projectPerformance = projectPerformance;
	}

	@Column(name = "ASSET_ASSESS_PATH", length = 1000)
	public String getAssetAssessPath() {
		return this.assetAssessPath;
	}

	public void setAssetAssessPath(String assetAssessPath) {
		this.assetAssessPath = assetAssessPath;
	}

	@Column(name = "TRANSFER_PROPERTY_PATH", length = 200)
	public String getTransferPropertyPath() {
		return this.transferPropertyPath;
	}

	public void setTransferPropertyPath(String transferPropertyPath) {
		this.transferPropertyPath = transferPropertyPath;
	}

	@Column(name = "PROJECT_PERFORMANCE_PATH", length = 200)
	public String getProjectPerformancePath() {
		return this.projectPerformancePath;
	}

	public void setProjectPerformancePath(String projectPerformancePath) {
		this.projectPerformancePath = projectPerformancePath;
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

}
package com.wfzcx.ppms.prepare.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProQualitativeExpert entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_QUALITATIVE_EXPERT")
@GenericGenerator(name = "SEQ_PRO_QUALITATIVE_EXPERT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_QUALITATIVE_EXPERT") })
public class ProQualitativeExpert implements java.io.Serializable {

	// Fields

	private Integer qualexpertid;
	private String expertName;
	private String expertType;
	private Integer expertId;
	private String expertPhone;
	private String bidmajor;
	private String responsibleArea;
	private String remark;
	private Integer projectid;
	private String xmhj;
	private Integer pszbid;
	@Column(name = "PSZBID")
	public Integer getPszbid() {
		return pszbid;
	}

	public void setPszbid(Integer pszbid) {
		this.pszbid = pszbid;
	}

	// Constructors
	@Column(name = "XMHJ")
	public String getXmhj() {
		return xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

	/** default constructor */
	public ProQualitativeExpert() {
	}

	/** minimal constructor */
	public ProQualitativeExpert(Integer qualexpertid) {
		this.qualexpertid = qualexpertid;
	}

	/** full constructor */
	public ProQualitativeExpert(Integer qualexpertid, String expertName,
			String expertType, Integer expertId, String expertPhone,
			String bidmajor, String responsibleArea, String remark,
			Integer projectid) {
		this.qualexpertid = qualexpertid;
		this.expertName = expertName;
		this.expertType = expertType;
		this.expertId = expertId;
		this.expertPhone = expertPhone;
		this.bidmajor = bidmajor;
		this.responsibleArea = responsibleArea;
		this.remark = remark;
		this.projectid = projectid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_QUALITATIVE_EXPERT")
	@Column(name = "QUALEXPERTID", unique = true, nullable = false, precision = 18, scale = 0)
	public Integer getQualexpertid() {
		return this.qualexpertid;
	}

	public void setQualexpertid(Integer qualexpertid) {
		this.qualexpertid = qualexpertid;
	}

	@Column(name = "EXPERT_NAME", length = 100)
	public String getExpertName() {
		return this.expertName;
	}

	public void setExpertName(String expertName) {
		this.expertName = expertName;
	}

	@Column(name = "EXPERT_TYPE", length = 100)
	public String getExpertType() {
		return this.expertType;
	}

	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}

	@Column(name = "EXPERT_ID", precision = 9, scale = 0)
	public Integer getExpertId() {
		return this.expertId;
	}

	public void setExpertId(Integer expertId) {
		this.expertId = expertId;
	}

	@Column(name = "EXPERT_PHONE", length = 30)
	public String getExpertPhone() {
		return this.expertPhone;
	}

	public void setExpertPhone(String expertPhone) {
		this.expertPhone = expertPhone;
	}

	@Column(name = "BIDMAJOR", length = 400)
	public String getBidmajor() {
		return this.bidmajor;
	}

	public void setBidmajor(String bidmajor) {
		this.bidmajor = bidmajor;
	}

	@Column(name = "RESPONSIBLE_AREA", length = 100)
	public String getResponsibleArea() {
		return this.responsibleArea;
	}

	public void setResponsibleArea(String responsibleArea) {
		this.responsibleArea = responsibleArea;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "PROJECTID", precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

}
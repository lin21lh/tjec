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
 * ProProcurementExpert entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PROCUREMENT_EXPERT")
@GenericGenerator(name = "SEQ_PRO_PROCUREMENT_EXPERT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PROCUREMENT_EXPERT") })
public class ProProcurementExpert implements java.io.Serializable {

	// Fields

	private Integer purexpertid;
	private Integer expertid;
	private String expertName;
	private String expertType;
	private String expertPhone;
	private String bidmajor;
	private String responsibleArea;
	private String remark;
	private Integer advanceid;
	private String type;

	// Constructors

	/** default constructor */
	public ProProcurementExpert() {
	}

	/** minimal constructor */
	public ProProcurementExpert(Integer purexpertid) {
		this.purexpertid = purexpertid;
	}

	/** full constructor */
	public ProProcurementExpert(Integer purexpertid, Integer expertid,
			String expertName, String expertType, String expertPhone,
			String bidmajor, String responsibleArea, String remark,
			Integer advanceid, String type) {
		this.purexpertid = purexpertid;
		this.expertid = expertid;
		this.expertName = expertName;
		this.expertType = expertType;
		this.expertPhone = expertPhone;
		this.bidmajor = bidmajor;
		this.responsibleArea = responsibleArea;
		this.remark = remark;
		this.advanceid = advanceid;
		this.type = type;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PROCUREMENT_EXPERT")
	@Column(name = "PUREXPERTID", unique = true, nullable = false, precision = 18, scale = 0)
	public Integer getPurexpertid() {
		return this.purexpertid;
	}

	public void setPurexpertid(Integer purexpertid) {
		this.purexpertid = purexpertid;
	}

	@Column(name = "EXPERTID", precision = 9, scale = 0)
	public Integer getExpertid() {
		return this.expertid;
	}

	public void setExpertid(Integer expertid) {
		this.expertid = expertid;
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

	@Column(name = "ADVANCEID", precision = 20, scale = 0)
	public Integer getAdvanceid() {
		return this.advanceid;
	}

	public void setAdvanceid(Integer advanceid) {
		this.advanceid = advanceid;
	}

	@Column(name = "TYPE", length = 20)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
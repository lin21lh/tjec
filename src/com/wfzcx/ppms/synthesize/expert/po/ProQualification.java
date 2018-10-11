package com.wfzcx.ppms.synthesize.expert.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProQualification entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_QUALIFICATION")
@GenericGenerator(name = "SEQ_PRO_QUALIFICATION", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_QUALIFICATION") })
public class ProQualification implements java.io.Serializable {

	// Fields

	private Integer qualificationid;
	private String enumCode;
	private String filepath;
	private String qualificationCode;
	private String unit;
	private String startTime;
	private String endTime;
	private Integer expertid;

	// Constructors

	/** default constructor */
	public ProQualification() {
	}

	/** minimal constructor */
	public ProQualification(Integer qualificationid, Integer expertid) {
		this.qualificationid = qualificationid;
		this.expertid = expertid;
	}

	/** full constructor */
	public ProQualification(Integer qualificationid, String enumCode,
			String filepath, String qualificationCode, String unit,
			String startTime, String endTime, Integer expertid) {
		this.qualificationid = qualificationid;
		this.enumCode = enumCode;
		this.filepath = filepath;
		this.qualificationCode = qualificationCode;
		this.unit = unit;
		this.startTime = startTime;
		this.endTime = endTime;
		this.expertid = expertid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_QUALIFICATION")
	@Column(name = "QUALIFICATIONID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getQualificationid() {
		return this.qualificationid;
	}

	public void setQualificationid(Integer qualificationid) {
		this.qualificationid = qualificationid;
	}

	@Column(name = "ENUM_CODE", length = 50)
	public String getEnumCode() {
		return this.enumCode;
	}

	public void setEnumCode(String enumCode) {
		this.enumCode = enumCode;
	}

	@Column(name = "FILEPATH", length = 100)
	public String getFilepath() {
		return this.filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	@Column(name = "QUALIFICATION_CODE", length = 50)
	public String getQualificationCode() {
		return this.qualificationCode;
	}

	public void setQualificationCode(String qualificationCode) {
		this.qualificationCode = qualificationCode;
	}

	@Column(name = "UNIT", length = 120)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "START_TIME", length = 50)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 50)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "EXPERTID", nullable = false, precision = 18, scale = 0)
	public Integer getExpertid() {
		return this.expertid;
	}

	public void setExpertid(Integer expertid) {
		this.expertid = expertid;
	}

}
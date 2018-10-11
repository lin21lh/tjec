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
 * ProQualitativeUnit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_QUALITATIVE_UNIT")
@GenericGenerator(name = "SEQ_PRO_QUALITATIVE_UNIT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_QUALITATIVE_UNIT") })
public class ProQualitativeUnit implements java.io.Serializable {

	// Fields

	private Integer qualunitid;
	private String unitName;
	private String unitPerson;
	private String unitTelphone;
	private String remark;
	private Integer projectid;

	// Constructors

	/** default constructor */
	public ProQualitativeUnit() {
	}

	/** minimal constructor */
	public ProQualitativeUnit(Integer qualunitid) {
		this.qualunitid = qualunitid;
	}

	/** full constructor */
	public ProQualitativeUnit(Integer qualunitid, String unitName,
			String unitPerson, String unitTelphone, String remark,
			Integer projectid) {
		this.qualunitid = qualunitid;
		this.unitName = unitName;
		this.unitPerson = unitPerson;
		this.unitTelphone = unitTelphone;
		this.remark = remark;
		this.projectid = projectid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_QUALITATIVE_UNIT")
	@Column(name = "QUALUNITID", unique = true, nullable = false, precision = 18, scale = 0)
	public Integer getQualunitid() {
		return this.qualunitid;
	}

	public void setQualunitid(Integer qualunitid) {
		this.qualunitid = qualunitid;
	}

	@Column(name = "UNIT_NAME", length = 100)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "UNIT_PERSON", length = 100)
	public String getUnitPerson() {
		return this.unitPerson;
	}

	public void setUnitPerson(String unitPerson) {
		this.unitPerson = unitPerson;
	}

	@Column(name = "UNIT_TELPHONE", length = 100)
	public String getUnitTelphone() {
		return this.unitTelphone;
	}

	public void setUnitTelphone(String unitTelphone) {
		this.unitTelphone = unitTelphone;
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
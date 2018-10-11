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
 * ProFinanceUnit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_FINANCE_UNIT")
@GenericGenerator(name = "SEQ_PRO_FINANCE_UNIT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_FINANCE_UNIT") })
public class ProFinanceUnit implements java.io.Serializable {

	// Fields

	private Integer finunitid;
	private String unitName;
	private String unitPerson;
	private String unitTelphone;
	private Integer projectid;
	private String remark;

	// Constructors

	/** default constructor */
	public ProFinanceUnit() {
	}

	/** minimal constructor */
	public ProFinanceUnit(Integer finunitid) {
		this.finunitid = finunitid;
	}

	/** full constructor */
	public ProFinanceUnit(Integer finunitid, String unitName, String unitPerson,
			String unitTelphone, Integer projectid, String remark) {
		this.finunitid = finunitid;
		this.unitName = unitName;
		this.unitPerson = unitPerson;
		this.unitTelphone = unitTelphone;
		this.projectid = projectid;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_FINANCE_UNIT")
	@Column(name = "FINUNITID", unique = true, nullable = false, precision = 18, scale = 0)
	public Integer getFinunitid() {
		return this.finunitid;
	}

	public void setFinunitid(Integer finunitid) {
		this.finunitid = finunitid;
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

	@Column(name = "PROJECTID", precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
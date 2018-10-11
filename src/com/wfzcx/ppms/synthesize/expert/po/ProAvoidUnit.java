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
 * ProAvoidUnit entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_AVOID_UNIT")
@GenericGenerator(name = "SEQ_PRO_AVOID_UNIT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_AVOID_UNIT") })
public class ProAvoidUnit implements java.io.Serializable {

	// Fields

	private Integer unitid;
	private Integer expertid;
	private String name;
	private Integer isWork;
	private String avoidTime;
	private String remark;

	// Constructors

	/** default constructor */
	public ProAvoidUnit() {
	}

	/** minimal constructor */
	public ProAvoidUnit(Integer unitid, Integer expertid, String name) {
		this.unitid = unitid;
		this.expertid = expertid;
		this.name = name;
	}

	/** full constructor */
	public ProAvoidUnit(Integer unitid, Integer expertid, String name,
			Integer isWork, String avoidTime, String remark) {
		this.unitid = unitid;
		this.expertid = expertid;
		this.name = name;
		this.isWork = isWork;
		this.avoidTime = avoidTime;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_AVOID_UNIT")
	@Column(name = "UNITID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getUnitid() {
		return this.unitid;
	}

	public void setUnitid(Integer unitid) {
		this.unitid = unitid;
	}

	@Column(name = "EXPERTID", nullable = false, precision = 9, scale = 0)
	public Integer getExpertid() {
		return this.expertid;
	}

	public void setExpertid(Integer expertid) {
		this.expertid = expertid;
	}

	@Column(name = "NAME", nullable = false, length = 80)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "IS_WORK", precision = 1, scale = 0)
	public Integer getIsWork() {
		return this.isWork;
	}

	public void setIsWork(Integer isWork) {
		this.isWork = isWork;
	}

	@Column(name = "AVOID_TIME", length = 32)
	public String getAvoidTime() {
		return this.avoidTime;
	}

	public void setAvoidTime(String avoidTime) {
		this.avoidTime = avoidTime;
	}

	@Column(name = "REMARK", length = 400)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
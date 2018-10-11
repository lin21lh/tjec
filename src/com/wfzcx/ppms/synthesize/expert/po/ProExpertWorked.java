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
 * ProExpertWorked entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_EXPERT_WORKED")
@GenericGenerator(name = "SEQ_PRO_EXPERT_WORKED", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_EXPERT_WORKED") })
public class ProExpertWorked implements java.io.Serializable {

	// Fields

	private Integer workedid;
	private String name;
	private String time;
	private String duty;
	private String certifier;
	private String certifierPhone;
	private Integer expertid;

	// Constructors

	/** default constructor */
	public ProExpertWorked() {
	}

	/** minimal constructor */
	public ProExpertWorked(Integer workedid, String name, String time,
			String duty, Integer expertid) {
		this.workedid = workedid;
		this.name = name;
		this.time = time;
		this.duty = duty;
		this.expertid = expertid;
	}

	/** full constructor */
	public ProExpertWorked(Integer workedid, String name, String time,
			String duty, String certifier, String certifierPhone,
			Integer expertid) {
		this.workedid = workedid;
		this.name = name;
		this.time = time;
		this.duty = duty;
		this.certifier = certifier;
		this.certifierPhone = certifierPhone;
		this.expertid = expertid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_EXPERT_WORKED")
	@Column(name = "WORKEDID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getWorkedid() {
		return this.workedid;
	}

	public void setWorkedid(Integer workedid) {
		this.workedid = workedid;
	}

	@Column(name = "NAME", nullable = false, length = 80)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TIME", nullable = false, length = 60)
	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "DUTY", nullable = false, length = 30)
	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	@Column(name = "CERTIFIER", length = 30)
	public String getCertifier() {
		return this.certifier;
	}

	public void setCertifier(String certifier) {
		this.certifier = certifier;
	}

	@Column(name = "CERTIFIER_PHONE", length = 30)
	public String getCertifierPhone() {
		return this.certifierPhone;
	}

	public void setCertifierPhone(String certifierPhone) {
		this.certifierPhone = certifierPhone;
	}

	@Column(name = "EXPERTID", nullable = false, precision = 9, scale = 0)
	public Integer getExpertid() {
		return this.expertid;
	}

	public void setExpertid(Integer expertid) {
		this.expertid = expertid;
	}

}
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
 * ProNegotiationGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_NEGOTIATION_GROUP")
@GenericGenerator(name = "SEQ_PRO_NEGOTIATION_GROUP", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_NEGOTIATION_GROUP") })
public class ProNegotiationGroup implements java.io.Serializable {

	// Fields

	private Integer negotiationid;
	private Integer purchaseid;
	private String name;
	private String phone;
	private String remark;
	private String duty;

	// Constructors

	/** default constructor */
	public ProNegotiationGroup() {
	}

	/** minimal constructor */
	public ProNegotiationGroup(Integer negotiationid, Integer purchaseid) {
		this.negotiationid = negotiationid;
		this.purchaseid = purchaseid;
	}

	/** full constructor */
	public ProNegotiationGroup(Integer negotiationid, Integer purchaseid,
			String name, String phone, String remark, String duty) {
		this.negotiationid = negotiationid;
		this.purchaseid = purchaseid;
		this.name = name;
		this.phone = phone;
		this.remark = remark;
		this.duty = duty;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_NEGOTIATION_GROUP")
	@Column(name = "NEGOTIATIONID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getNegotiationid() {
		return this.negotiationid;
	}

	public void setNegotiationid(Integer negotiationid) {
		this.negotiationid = negotiationid;
	}

	@Column(name = "PURCHASEID", nullable = false, precision = 20, scale = 0)
	public Integer getPurchaseid() {
		return this.purchaseid;
	}

	public void setPurchaseid(Integer purchaseid) {
		this.purchaseid = purchaseid;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PHONE", length = 100)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "DUTY", length = 100)
	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

}
package com.wfzcx.aros.ajpj.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BCaseestbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASEESTBASEINFO")
public class BCaseestbaseinfo implements java.io.Serializable {

	// Fields

	private String id;
	private String caseid;
	private String quatype;
	private String remark;
	private String operator;
	private String opttime;

	// Constructors

	/** default constructor */
	public BCaseestbaseinfo() {
	}

	/** minimal constructor */
	public BCaseestbaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BCaseestbaseinfo(String id, String caseid, String quatype, String remark, String operator, String opttime) {
		this.id = id;
		this.caseid = caseid;
		this.quatype = quatype;
		this.remark = remark;
		this.operator = operator;
		this.opttime = opttime;
	}

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "QUATYPE", length = 2)
	public String getQuatype() {
		return this.quatype;
	}

	public void setQuatype(String quatype) {
		this.quatype = quatype;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 30)
	public String getOpttime() {
		return this.opttime;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

}
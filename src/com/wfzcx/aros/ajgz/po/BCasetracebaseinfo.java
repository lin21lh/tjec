package com.wfzcx.aros.ajgz.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BCasetracebaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASETRACEBASEINFO")
public class BCasetracebaseinfo implements java.io.Serializable {

	// Fields

	private String id;
	private String caseid;
	private String exectype;
	private String remark;
	private String operator;
	private String opttime;

	// Constructors

	/** default constructor */
	public BCasetracebaseinfo() {
	}

	/** minimal constructor */
	public BCasetracebaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BCasetracebaseinfo(String id, String caseid, String exectype, String remark, String operator, String opttime) {
		this.id = id;
		this.caseid = caseid;
		this.exectype = exectype;
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

	@Column(name = "EXECTYPE", length = 2)
	public String getExectype() {
		return this.exectype;
	}

	public void setExectype(String exectype) {
		this.exectype = exectype;
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
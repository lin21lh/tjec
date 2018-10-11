package com.wfzcx.aros.zjgl.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BGroupbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_GROUPBASEINFO")
public class BGroupbaseinfo implements java.io.Serializable {

	// Fields

	private String groupid;
	private String groupname;
	private String casedesc;
	private String question;
	private String ifcheck;
	private String operator;
	private String opttime;

	// Constructors

	/** default constructor */
	public BGroupbaseinfo() {
	}

	/** minimal constructor */
	public BGroupbaseinfo(String groupid) {
		this.groupid = groupid;
	}

	/** full constructor */
	public BGroupbaseinfo(String groupid, String groupname, String casedesc, String question, String ifcheck, String operator, String opttime, BigDecimal caseid) {
		this.groupid = groupid;
		this.groupname = groupname;
		this.casedesc = casedesc;
		this.question = question;
		this.ifcheck = ifcheck;
		this.operator = operator;
		this.opttime = opttime;
	}

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "GROUPID")
	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Column(name = "GROUPNAME", length = 80)
	public String getGroupname() {
		return this.groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@Column(name = "CASEDESC", length = 1000)
	public String getCasedesc() {
		return this.casedesc;
	}

	public void setCasedesc(String casedesc) {
		this.casedesc = casedesc;
	}

	@Column(name = "QUESTION", length = 1000)
	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name = "IFCHECK", length = 1)
	public String getIfcheck() {
		return this.ifcheck;
	}

	public void setIfcheck(String ifcheck) {
		this.ifcheck = ifcheck;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 20)
	public String getOpttime() {
		return this.opttime;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

}
package com.wfzcx.aros.wtcl.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BWebquesbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_WEBQUESBASEINFO")
public class BWebquesbaseinfo implements java.io.Serializable {

	// Fields

	private String quesid;
	private String quetype;
	private String caseid;
	private String askername;
	private String asktime;
	private String phone;
	private String quesdesc;
	private String answer;
	private String operator;
	private String opttime;
	private String ifanswer;

	// Constructors

	/** default constructor */
	public BWebquesbaseinfo() {
	}

	/** minimal constructor */
	public BWebquesbaseinfo(String quesid) {
		this.quesid = quesid;
	}

	/** full constructor */
	public BWebquesbaseinfo(String quesid, String quetype, String caseid, String askername, String asktime, String phone, String quesdesc, String answer, String operator, String opttime,
			String ifanswer) {
		this.quesid = quesid;
		this.quetype = quetype;
		this.caseid = caseid;
		this.askername = askername;
		this.asktime = asktime;
		this.phone = phone;
		this.quesdesc = quesdesc;
		this.answer = answer;
		this.operator = operator;
		this.opttime = opttime;
		this.ifanswer = ifanswer;
	}

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "QUESID")
	public String getQuesid() {
		return this.quesid;
	}

	public void setQuesid(String quesid) {
		this.quesid = quesid;
	}

	@Column(name = "QUETYPE", length = 2)
	public String getQuetype() {
		return this.quetype;
	}

	public void setQuetype(String quetype) {
		this.quetype = quetype;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "ASKERNAME", length = 80)
	public String getAskername() {
		return this.askername;
	}

	public void setAskername(String askername) {
		this.askername = askername;
	}

	@Column(name = "ASKTIME", length = 30)
	public String getAsktime() {
		return this.asktime;
	}

	public void setAsktime(String asktime) {
		this.asktime = asktime;
	}

	@Column(name = "PHONE", length = 30)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "QUESDESC", length = 500)
	public String getQuesdesc() {
		return this.quesdesc;
	}

	public void setQuesdesc(String quesdesc) {
		this.quesdesc = quesdesc;
	}

	@Column(name = "ANSWER", length = 2000)
	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name = "OPERATOR", length = 30)
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

	@Column(name = "IFANSWER", length = 1)
	public String getIfanswer() {
		return this.ifanswer;
	}

	public void setIfanswer(String ifanswer) {
		this.ifanswer = ifanswer;
	}

}
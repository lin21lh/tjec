package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "B_CALLBACKINVOICES" )
public class BCallbackInvoicesInfo {

	private String  ciid;             //  varchar2(32)                    not null,
	private String  caseid;           //  varchar2(32),
	private String  isattitude;       //  varchar2(2),
	private String  islegal;          //  varchar2(2),
	private String  iscorruption;     //  varchar2(2),
	private String  isfavoritism;     //  varchar2(2),
	private String  issatisfaction;   //  varchar2(2),
	private String  remark;           //  varchar2(2000),
	private String  interviewee;      //  varchar2(60),
	private String  contactway;       //  varchar2(200),
	private String  interviewtime;    //  varchar2(10),
	private Long  userid;           //  number(19),
	private String  operator;         //  varchar2(20),
	private String  opttime;       //  varchar2(20),
	
	public BCallbackInvoicesInfo(String ciid, String caseid, String isattitude, String islegal, String iscorruption,
			String isfavoritism, String issatisfaction, String remark, String interviewee, String contactway,
			String interviewtime, Long userid, String operator, String opttime) {
		super();
		this.ciid = ciid;
		this.caseid = caseid;
		this.isattitude = isattitude;
		this.islegal = islegal;
		this.iscorruption = iscorruption;
		this.isfavoritism = isfavoritism;
		this.issatisfaction = issatisfaction;
		this.remark = remark;
		this.interviewee = interviewee;
		this.contactway = contactway;
		this.interviewtime = interviewtime;
		this.userid = userid;
		this.operator = operator;
		this.opttime = opttime;
	}
	
	public BCallbackInvoicesInfo() {
		super();
	}
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "CIID", unique = true, nullable = false, length = 32)
	public String getCiid() {
		return ciid;
	}
	
	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return caseid;
	}
	
	@Column(name = "ISATTITUDE", length = 2)
	public String getIsattitude() {
		return isattitude;
	}
	@Column(name = "ISLEGAL", length = 2)
	public String getIslegal() {
		return islegal;
	}
	@Column(name = "ISCORRUPTION", length = 2)
	public String getIscorruption() {
		return iscorruption;
	}
	@Column(name = "ISFAVORITISM", length = 2)
	public String getIsfavoritism() {
		return isfavoritism;
	}
	@Column(name = "ISSATISFACTION", length = 2)
	public String getIssatisfaction() {
		return issatisfaction;
	}
	
	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return remark;
	}
	
	@Column(name = "INTERVIEWEE", length = 60)
	public String getInterviewee() {
		return interviewee;
	}
	
	@Column(name = "CONTACTWAY", length = 200)
	public String getContactway() {
		return contactway;
	}
	
	@Column(name = "INTERVIEWTIME", length = 10)
	public String getInterviewtime() {
		return interviewtime;
	}
	
	@Column(name = "USERID", length = 19)
	public Long getUserid() {
		return userid;
	}
	
	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return operator;
	}
	
	@Column(name = "OPTTIME", length = 20)
	public String getOpttime() {
		return opttime;
	}
	public void setCiid(String ciid) {
		this.ciid = ciid;
	}
	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	public void setIsattitude(String isattitude) {
		this.isattitude = isattitude;
	}
	public void setIslegal(String islegal) {
		this.islegal = islegal;
	}
	public void setIscorruption(String iscorruption) {
		this.iscorruption = iscorruption;
	}
	public void setIsfavoritism(String isfavoritism) {
		this.isfavoritism = isfavoritism;
	}
	public void setIssatisfaction(String issatisfaction) {
		this.issatisfaction = issatisfaction;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}
	public void setContactway(String contactway) {
		this.contactway = contactway;
	}
	public void setInterviewtime(String interviewtime) {
		this.interviewtime = interviewtime;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}          //  varchar2(20),
	
}

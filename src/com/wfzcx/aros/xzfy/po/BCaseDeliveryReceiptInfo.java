package com.wfzcx.aros.xzfy.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "B_CASEDELIVERYRECEIPT" )
public class BCaseDeliveryReceiptInfo {

	private String cdrid;            // varchar2(32)                    not null,
	private String caseid;            // varchar2(32),
	private String docname;           // varchar2(600),
	private String doccode;            // varchar2(60),
	private String receiver;          // varchar2(200),
	private String deliverydate;      // varchar2(10),
	private String deliverysite;      // varchar2(200),
	private String deliveryway;       // varchar2(60),
	private String processagent;      // varchar2(60),
	private String processserver;     // varchar2(60),
	private String remark;            // varchar2(2000),
	private Long userid;            // number(19),
	private String operator;          // varchar2(20),
	private String opttime;          // varchar2(20),
	
	public BCaseDeliveryReceiptInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BCaseDeliveryReceiptInfo(String cdrid, String caseid, String docname, String receiver, String deliverydate,
			String deliverysite, String deliveryway, String processagent, String processserver, String remark,
			Long userid, String operator, String opttime, String doccode) {
		super();
		this.cdrid = cdrid;
		this.caseid = caseid;
		this.docname = docname;
		this.receiver = receiver;
		this.deliverydate = deliverydate;
		this.deliverysite = deliverysite;
		this.deliveryway = deliveryway;
		this.processagent = processagent;
		this.processserver = processserver;
		this.remark = remark;
		this.userid = userid;
		this.operator = operator;
		this.opttime = opttime;
		this.doccode= doccode;
	}

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "CDRID", unique = true, nullable = false, length = 32)
	public String getCdrid() {
		return cdrid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return caseid;
	}

	@Column(name = "DOCNAME", length = 32)
	public String getDocname() {
		return docname;
	}

	@Column(name = "RECEIVER", length = 600)
	public String getReceiver() {
		return receiver;
	}

	@Column(name = "DELIVERYDATE", length = 10)
	public String getDeliverydate() {
		return deliverydate;
	}

	@Column(name = "DELIVERYSITE", length = 200)
	public String getDeliverysite() {
		return deliverysite;
	}

	@Column(name = "DELIVERYWAY", length = 60)
	public String getDeliveryway() {
		return deliveryway;
	}

	@Column(name = "PROCESSAGENT", length =60)
	public String getProcessagent() {
		return processagent;
	}

	@Column(name = "PROCESSSERVER", length = 60)
	public String getProcessserver() {
		return processserver;
	}

	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return remark;
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
    @Column(name = "DOCCODE", length = 60) 
	public  String getDoccode() {
		return doccode;
	}

	public void setDoccode(String doccode) {
		this.doccode = doccode;
	}

	public void setCdrid(String cdrid) {
		this.cdrid = cdrid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setDeliverydate(String deliverydate) {
		this.deliverydate = deliverydate;
	}

	public void setDeliverysite(String deliverysite) {
		this.deliverysite = deliverysite;
	}

	public void setDeliveryway(String deliveryway) {
		this.deliveryway = deliveryway;
	}

	public void setProcessagent(String processagent) {
		this.processagent = processagent;
	}

	public void setProcessserver(String processserver) {
		this.processserver = processserver;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	  
}

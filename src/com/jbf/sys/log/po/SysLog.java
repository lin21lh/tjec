/************************************************************
 * 类名：SysLog.java
 *
 * 类别：PO
 * 功能：资源PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.log.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.jbf.common.util.StringUtil;

import eu.bitwalker.useragentutils.UserAgent;



/**
 * SysLog entity. @author mqs
 */
@Entity
@Table(name = "SYS_LOG")
@SequenceGenerator(name="SEQ_LOG",sequenceName="SEQ_LOG")
@GenericGenerator(name = "SEQ_LOG", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_LOG") }) 
public class SysLog implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3189312011089948748L;
	private Long logid;
	private String usercode;
	private String username;
	private String ip;
	private String useragentinfo;
	private String opersystem;
	private String operbrower;
	private Integer opertype;
	private String opertypename;
	private String opermessage;
	private String opertime;
	private String remark;


	// Constructors

	/** default constructor */
	public SysLog() {
	}

	/** full constructor */
	public SysLog(Long logid, String usercode, String ip,
			String useragentinfo, Integer opertype, String opermessage, String opertime,
			String remark) {
		this.logid = logid;
		this.usercode = usercode;
		this.ip = ip;
		this.useragentinfo = useragentinfo;
		this.opertype = opertype;
		this.opermessage = opermessage;
		this.opertime = opertime;
		this.remark = remark;
	}	

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_LOG")
	@Column(name = "LOGID", nullable = false, scale = 0)
	public Long getLogid() {
		return logid;
	}
	
	public void setLogid(Long logid) {
		this.logid = logid;
	}
	
	@Column(name = "USERCODE", nullable = false, length = 50)
	public String getUsercode() {
		return usercode;
	}
	
	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}
	
	@Formula("(select t.username from SYS_USER t  where t.usercode=usercode)")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "IP", length = 50)
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "USERAGENTINFO", length = 255)
	public String getUseragentinfo() {
		return useragentinfo;
	}
	
	public void setUseragentinfo(String useragentinfo) {
		this.useragentinfo = useragentinfo;
	}
	
	@Transient
	public String getOpersystem() {
		if (StringUtil.isNotBlank(useragentinfo))
			this.setOpersystem(useragentinfo);
		return opersystem;
	}
	
	public void setOpersystem(String useragentinfo) {
		this.opersystem = UserAgent.parseUserAgentString(useragentinfo).getOperatingSystem().getName();
	}
	
	@Transient
	public String getOperbrower() {
		this.setOperbrower(useragentinfo);
		return operbrower;
	}
	
	public void setOperbrower(String useragentinfo) {
		this.operbrower = UserAgent.parseUserAgentString(useragentinfo).getBrowser().name();
	}
	
	@Column(name = "OPERTYPE", nullable = false, precision = 2)
	public Integer getOpertype() {
		return opertype;
	}
	
	public void setOpertype(Integer opertype) {
		this.opertype = opertype;
	}
	
	@Formula("(select t.name from SYS_DICENUMITEM t where t.elementcode='SYS_OPERTYPE' and t.code=opertype)")
	public String getOpertypename() {
		return opertypename;
	}
	
	public void setOpertypename(String opertypename) {
		this.opertypename = opertypename;
	}
	
	@Column(name = "OPERMESSAGE",  nullable = false, length = 4000)
	public String getOpermessage() {
		return opermessage;
	}
	
	public void setOpermessage(String opermessage) {
		this.opermessage = opermessage;
	}
	
	@Column(name = "OPERTIME", nullable = false, length = 20)
	public String getOpertime() {
		return opertime;
	}
	
	public void setOpertime(String opertime) {
		this.opertime = opertime;
	}

	@Column(name = "REMARK", nullable = true, length = 255)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
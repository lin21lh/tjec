package com.wfzcx.aros.bxzfy.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Rcasebaseinfo
 * @Description: 被复议案件基本信息表实体Bean
 * @author  MyEclipse Persistence Tools
 * @date 2016年9月20日 下午4:07:04
 * @version V1.0
 */
@Entity
@Table(name = "B_RCASEBASEINFO")
public class Rcasebaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String rcaseid;
	private String lcasecode;
	private String rcasecode;
	private String rdeptname;
	private String casereason;
	private String appname;
	private String thirdname;
	private String receiver;
	private String rectime;
	private String annex;
	private String undertaker;
	private String undertakedep;
	private String assessor;
	private String lawyer;
	private String noticeid;
	private String operator;
	private String opttime;
	private String region;
	private String registerdate;
	private String solveddate;
	private String state;
	private String lasttime;
	private String opttype;
	private BigDecimal nodeid;
	private Long userid;
	private Long undertakeid;
	private Long auditmanid;

	// Constructors

	/** default constructor */
	public Rcasebaseinfo() {
	}

	/** minimal constructor */
	public Rcasebaseinfo(String rcaseid) {
		this.rcaseid = rcaseid;
	}

	/** full constructor */
	public Rcasebaseinfo(String rcaseid, String lcasecode, String rcasecode,
			String rdeptname, String casereason, String appname,
			String thirdname, String receiver, String rectime, String annex,
			String undertaker, String undertakedep, String assessor,
			String lawyer, String noticeid, String operator, String opttime,
			String region, String registerdate, String solveddate,
			String state, String lasttime, String opttype, BigDecimal nodeid,
			Long userid, Long undertakeid, Long auditmanid) {
		this.rcaseid = rcaseid;
		this.lcasecode = lcasecode;
		this.rcasecode = rcasecode;
		this.rdeptname = rdeptname;
		this.casereason = casereason;
		this.appname = appname;
		this.thirdname = thirdname;
		this.receiver = receiver;
		this.rectime = rectime;
		this.annex = annex;
		this.undertaker = undertaker;
		this.undertakedep = undertakedep;
		this.assessor = assessor;
		this.lawyer = lawyer;
		this.noticeid = noticeid;
		this.operator = operator;
		this.opttime = opttime;
		this.region = region;
		this.registerdate = registerdate;
		this.solveddate = solveddate;
		this.state = state;
		this.lasttime = lasttime;
		this.opttype = opttype;
		this.nodeid = nodeid;
		this.userid = userid;
		this.undertakeid = undertakeid;
		this.auditmanid = auditmanid;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "RCASEID", unique = true, nullable = false, length = 32)
	public String getRcaseid() {
		return this.rcaseid;
	}

	public void setRcaseid(String rcaseid) {
		this.rcaseid = rcaseid;
	}

	@Column(name = "LCASECODE", length = 100)
	public String getLcasecode() {
		return this.lcasecode;
	}

	public void setLcasecode(String lcasecode) {
		this.lcasecode = lcasecode;
	}

	@Column(name = "RCASECODE", length = 100)
	public String getRcasecode() {
		return this.rcasecode;
	}

	public void setRcasecode(String rcasecode) {
		this.rcasecode = rcasecode;
	}

	@Column(name = "RDEPTNAME", length = 100)
	public String getRdeptname() {
		return this.rdeptname;
	}

	public void setRdeptname(String rdeptname) {
		this.rdeptname = rdeptname;
	}

	@Column(name = "CASEREASON", length = 1000)
	public String getCasereason() {
		return this.casereason;
	}

	public void setCasereason(String casereason) {
		this.casereason = casereason;
	}

	@Column(name = "APPNAME", length = 100)
	public String getAppname() {
		return this.appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	@Column(name = "THIRDNAME", length = 100)
	public String getThirdname() {
		return this.thirdname;
	}

	public void setThirdname(String thirdname) {
		this.thirdname = thirdname;
	}

	@Column(name = "RECEIVER", length = 100)
	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "RECTIME", length = 30)
	public String getRectime() {
		return this.rectime;
	}

	public void setRectime(String rectime) {
		this.rectime = rectime;
	}

	@Column(name = "ANNEX", length = 100)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "UNDERTAKER", length = 30)
	public String getUndertaker() {
		return this.undertaker;
	}

	public void setUndertaker(String undertaker) {
		this.undertaker = undertaker;
	}

	@Column(name = "UNDERTAKEDEP", length = 10)
	public String getUndertakedep() {
		return this.undertakedep;
	}

	public void setUndertakedep(String undertakedep) {
		this.undertakedep = undertakedep;
	}

	@Column(name = "ASSESSOR", length = 30)
	public String getAssessor() {
		return this.assessor;
	}

	public void setAssessor(String assessor) {
		this.assessor = assessor;
	}

	@Column(name = "LAWYER", length = 30)
	public String getLawyer() {
		return this.lawyer;
	}

	public void setLawyer(String lawyer) {
		this.lawyer = lawyer;
	}

	@Column(name = "NOTICEID", length = 32)
	public String getNoticeid() {
		return this.noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
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

	@Column(name = "REGION", length = 6)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "REGISTERDATE", length = 10)
	public String getRegisterdate() {
		return this.registerdate;
	}

	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}

	@Column(name = "SOLVEDDATE", length = 10)
	public String getSolveddate() {
		return this.solveddate;
	}

	public void setSolveddate(String solveddate) {
		this.solveddate = solveddate;
	}

	@Column(name = "STATE", length = 2)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "LASTTIME", length = 19)
	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	@Column(name = "OPTTYPE", length = 2)
	public String getOpttype() {
		return this.opttype;
	}

	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}

	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return this.nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}
	
	@Column(name = "USERID", length = 19)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "UNDERTAKEID", length = 19)
	public Long getUndertakeid() {
		return undertakeid;
	}

	public void setUndertakeid(Long undertakeid) {
		this.undertakeid = undertakeid;
	}

	@Column(name = "AUDITMANID", length = 19)
	public Long getAuditmanid() {
		return auditmanid;
	}

	public void setAuditmanid(Long auditmanid) {
		this.auditmanid = auditmanid;
	}
}
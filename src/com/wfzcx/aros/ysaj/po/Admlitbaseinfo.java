package com.wfzcx.aros.ysaj.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Admlitbaseinfo
 * @Description: 应诉案件基本信息表实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年9月22日 下午1:54:53
 * @version V1.0
 */
@Entity
@Table(name = "B_ADMLITBASEINFO")
public class Admlitbaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String acaseid;
	private String relaacaseid;
	private String stage;
	private String lcasecode;
	private String rcasecode;
	private String rdeptname;
	private String casereason;
	private String partytype;
	private String plaintiff;
	private String defendant;
	private String thirdname;
	private String rectype;
	private String receiver;
	private String rectime;
	private String heartime;
	private String hearplace;
	private String annex;
	private String undertaker;
	private String assessor;
	private String lawyer;
	private String noticeid;
	private String operator;
	private String opttime;
	private String courthead;
	private String ifappeal;
	private String appealtime;
	private String remark;
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
	private String headpost;
	private String ifincourt;

	// Constructors

	/** default constructor */
	public Admlitbaseinfo() {
	}

	/** minimal constructor */
	public Admlitbaseinfo(String acaseid) {
		this.acaseid = acaseid;
	}

	/** full constructor */
	public Admlitbaseinfo(String acaseid, String relaacaseid, String stage,
			String lcasecode, String rcasecode, String rdeptname,
			String casereason, String partytype, String plaintiff,
			String defendant, String thirdname, String rectype,
			String receiver, String rectime, String heartime, String hearplace,
			String annex, String undertaker, String assessor, String lawyer,
			String noticeid, String operator, String opttime, String courthead,
			String ifappeal, String appealtime, String remark, String region,
			String registerdate, String solveddate, String state,
			String lasttime, String opttype, BigDecimal nodeid,
			Long userid, Long undertakeid, Long auditmanid) {
		this.acaseid = acaseid;
		this.relaacaseid = relaacaseid;
		this.stage = stage;
		this.lcasecode = lcasecode;
		this.rcasecode = rcasecode;
		this.rdeptname = rdeptname;
		this.casereason = casereason;
		this.partytype = partytype;
		this.plaintiff = plaintiff;
		this.defendant = defendant;
		this.thirdname = thirdname;
		this.rectype = rectype;
		this.receiver = receiver;
		this.rectime = rectime;
		this.heartime = heartime;
		this.hearplace = hearplace;
		this.annex = annex;
		this.undertaker = undertaker;
		this.assessor = assessor;
		this.lawyer = lawyer;
		this.noticeid = noticeid;
		this.operator = operator;
		this.opttime = opttime;
		this.courthead = courthead;
		this.ifappeal = ifappeal;
		this.appealtime = appealtime;
		this.remark = remark;
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
	@Column(name = "ACASEID", unique = true, nullable = false, length = 32)
	public String getAcaseid() {
		return this.acaseid;
	}

	public void setAcaseid(String acaseid) {
		this.acaseid = acaseid;
	}

	@Column(name = "RELAACASEID", length = 32)
	public String getRelaacaseid() {
		return this.relaacaseid;
	}

	public void setRelaacaseid(String relaacaseid) {
		this.relaacaseid = relaacaseid;
	}

	@Column(name = "STAGE", length = 2)
	public String getStage() {
		return this.stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
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

	@Column(name = "PARTYTYPE", length = 2)
	public String getPartytype() {
		return this.partytype;
	}

	public void setPartytype(String partytype) {
		this.partytype = partytype;
	}

	@Column(name = "PLAINTIFF", length = 100)
	public String getPlaintiff() {
		return this.plaintiff;
	}

	public void setPlaintiff(String plaintiff) {
		this.plaintiff = plaintiff;
	}

	@Column(name = "DEFENDANT", length = 100)
	public String getDefendant() {
		return this.defendant;
	}

	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}

	@Column(name = "THIRDNAME", length = 100)
	public String getThirdname() {
		return this.thirdname;
	}

	public void setThirdname(String thirdname) {
		this.thirdname = thirdname;
	}

	@Column(name = "RECTYPE", length = 2)
	public String getRectype() {
		return this.rectype;
	}

	public void setRectype(String rectype) {
		this.rectype = rectype;
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

	@Column(name = "HEARTIME", length = 30)
	public String getHeartime() {
		return this.heartime;
	}

	public void setHeartime(String heartime) {
		this.heartime = heartime;
	}

	@Column(name = "HEARPLACE", length = 200)
	public String getHearplace() {
		return this.hearplace;
	}

	public void setHearplace(String hearplace) {
		this.hearplace = hearplace;
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

	@Column(name = "COURTHEAD", length = 100)
	public String getCourthead() {
		return this.courthead;
	}

	public void setCourthead(String courthead) {
		this.courthead = courthead;
	}

	@Column(name = "IFAPPEAL", length = 2)
	public String getIfappeal() {
		return this.ifappeal;
	}

	public void setIfappeal(String ifappeal) {
		this.ifappeal = ifappeal;
	}

	@Column(name = "APPEALTIME", length = 30)
	public String getAppealtime() {
		return this.appealtime;
	}

	public void setAppealtime(String appealtime) {
		this.appealtime = appealtime;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	@Column(name = "HEADPOST", length = 100)
	public String getHeadpost() {
		return headpost;
	}

	public void setHeadpost(String headpost) {
		this.headpost = headpost;
	}

	@Column(name = "ifincourt", length = 1)
	public String getIfincourt() {
		return ifincourt;
	}

	public void setIfincourt(String ifincourt) {
		this.ifincourt = ifincourt;
	}
}
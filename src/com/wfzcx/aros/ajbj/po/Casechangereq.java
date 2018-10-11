package com.wfzcx.aros.ajbj.po;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BCasechangereq entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASECHANGEREQ")
public class Casechangereq implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 458426804537845129L;
	
	// Fields
	private String ccrid;
	private String caseid;
	private String csaecode;
	private String intro;
	private String protype;
	private String appname;
	private String defname;
	private String suspendreason;
	private String suspenddate;
	private String restorereason;
	private String delayreason;
	private String delaydate;
	private String withdrawreason;
	private String reqmanrequest;
	private String reqmansay;
	private String reqedmansay;
	private String thirdsay;
	private String ascertain;
	private String settlement;
	private BigDecimal nodeid;
	private String opttype;
	private String state;
	private String remark;
	private Long userid;
	private String operator;
	private String opttime;
	private String region;
	private String lasttime;
	private String result;
	private String endreason;

	// Constructors

	/** default constructor */
	public Casechangereq() {
	}

	/** minimal constructor */
	public Casechangereq(String ccrid, String csaecode) {
		this.ccrid = ccrid;
		this.csaecode = csaecode;
	}

	/** full constructor */
	public Casechangereq(String ccrid, String caseid, String csaecode,
			String intro, String protype, String appname, String defname,
			String suspendreason, String suspenddate, String restorereason,
			String delayreason, String delaydate, String withdrawreason,
			String reqmanrequest, String reqmansay, String reqedmansay,
			String thirdsay, String ascertain, String settlement,
			BigDecimal nodeid, String opttype, String state, String remark,
			Long userid, String operator, String opttime, String region,
			String lasttime, String result, String endreason) {
		this.ccrid = ccrid;
		this.caseid = caseid;
		this.csaecode = csaecode;
		this.intro = intro;
		this.protype = protype;
		this.appname = appname;
		this.defname = defname;
		this.suspendreason = suspendreason;
		this.suspenddate = suspenddate;
		this.restorereason = restorereason;
		this.delayreason = delayreason;
		this.delaydate = delaydate;
		this.withdrawreason = withdrawreason;
		this.reqmanrequest = reqmanrequest;
		this.reqmansay = reqmansay;
		this.reqedmansay = reqedmansay;
		this.thirdsay = thirdsay;
		this.ascertain = ascertain;
		this.settlement = settlement;
		this.nodeid = nodeid;
		this.opttype = opttype;
		this.state = state;
		this.remark = remark;
		this.userid = userid;
		this.operator = operator;
		this.opttime = opttime;
		this.region = region;
		this.lasttime = lasttime;
		this.result = result;
		this.endreason = endreason;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "CCRID")
	public String getCcrid() {
		return this.ccrid;
	}

	public void setCcrid(String ccrid) {
		this.ccrid = ccrid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "CSAECODE", length = 100)
	public String getCsaecode() {
		return this.csaecode;
	}

	public void setCsaecode(String csaecode) {
		this.csaecode = csaecode;
	}

	@Column(name = "INTRO", length = 300)
	public String getIntro() {
		return this.intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	@Column(name = "PROTYPE", length = 2)
	public String getProtype() {
		return this.protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	@Column(name = "APPNAME", length = 300)
	public String getAppname() {
		return this.appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	@Column(name = "DEFNAME", length = 200)
	public String getDefname() {
		return this.defname;
	}

	public void setDefname(String defname) {
		this.defname = defname;
	}

	@Column(name = "SUSPENDREASON", length = 2)
	public String getSuspendreason() {
		return this.suspendreason;
	}

	public void setSuspendreason(String suspendreason) {
		this.suspendreason = suspendreason;
	}

	@Column(name = "SUSPENDDATE", length = 10)
	public String getSuspenddate() {
		return this.suspenddate;
	}

	public void setSuspenddate(String suspenddate) {
		this.suspenddate = suspenddate;
	}

	@Column(name = "RESTOREREASON", length = 1000)
	public String getRestorereason() {
		return this.restorereason;
	}

	public void setRestorereason(String restorereason) {
		this.restorereason = restorereason;
	}

	@Column(name = "DELAYREASON", length = 1000)
	public String getDelayreason() {
		return this.delayreason;
	}

	public void setDelayreason(String delayreason) {
		this.delayreason = delayreason;
	}

	@Column(name = "DELAYDATE", length = 10)
	public String getDelaydate() {
		return this.delaydate;
	}

	public void setDelaydate(String delaydate) {
		this.delaydate = delaydate;
	}

	@Column(name = "WITHDRAWREASON", length = 1000)
	public String getWithdrawreason() {
		return this.withdrawreason;
	}

	public void setWithdrawreason(String withdrawreason) {
		this.withdrawreason = withdrawreason;
	}

	@Column(name = "REQMANREQUEST", length = 2000)
	public String getReqmanrequest() {
		return this.reqmanrequest;
	}

	public void setReqmanrequest(String reqmanrequest) {
		this.reqmanrequest = reqmanrequest;
	}

	@Column(name = "REQMANSAY", length = 2000)
	public String getReqmansay() {
		return this.reqmansay;
	}

	public void setReqmansay(String reqmansay) {
		this.reqmansay = reqmansay;
	}

	@Column(name = "REQEDMANSAY", length = 2000)
	public String getReqedmansay() {
		return this.reqedmansay;
	}

	public void setReqedmansay(String reqedmansay) {
		this.reqedmansay = reqedmansay;
	}

	@Column(name = "THIRDSAY", length = 2000)
	public String getThirdsay() {
		return this.thirdsay;
	}

	public void setThirdsay(String thirdsay) {
		this.thirdsay = thirdsay;
	}

	@Column(name = "ASCERTAIN", length = 2000)
	public String getAscertain() {
		return this.ascertain;
	}

	public void setAscertain(String ascertain) {
		this.ascertain = ascertain;
	}

	@Column(name = "SETTLEMENT", length = 2000)
	public String getSettlement() {
		return this.settlement;
	}

	public void setSettlement(String settlement) {
		this.settlement = settlement;
	}

	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return this.nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	@Column(name = "OPTTYPE", length = 2)
	public String getOpttype() {
		return this.opttype;
	}

	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}

	@Column(name = "STATE", length = 2)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "USERID", scale = 0)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
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

	@Column(name = "REGION", length = 6)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "LASTTIME", length = 19)
	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	@Column(name = "RESULT", length = 2)
	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "ENDREASON", length = 2)
	public String getEndreason() {
		return endreason;
	}

	public void setEndreason(String endreason) {
		this.endreason = endreason;
	}
}
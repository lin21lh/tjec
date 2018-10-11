package com.wfzcx.aros.xzys.po;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * BRespbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_RESPBASEINFO")
public class BRespbaseinfo implements java.io.Serializable {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String id;
	private String caseid;
	private String casecode;
	private String carriedstatus;
	private String regtype;
	private String recdate;
	private String suetype;
	private String caseseqno;
	private String lawid;
	private String caseinnerid;
	private String jurilaw;
	private String expresscom;
	private String expressid;
	private String resdept;
	private String deptlevel;
	private String deptname;
	private String adminlevel;
	private String adminbehavior;
	private String regiontype;
	private String fringereq;
	private String suerequest;
	private String evidence;
	private String suecontents;
	private String lawcontents;
	private String constatus;
	private String ifincourt;
	private String recreport;
	private String recmanage;
	private String operator;
	private String opttime;
	private String ifappeal;
	private String appealtime;
	private String remark;
	private String annex;
	private BigDecimal nodeid;
	private String processid;
	private String protype;
	
	
	private String regtypename;
	private String jurilawname;
	private String suetypename;
	private String glahlawid;
	
	

	// Constructors

	/** default constructor */
	public BRespbaseinfo() {
	}

	/** minimal constructor */
	public BRespbaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BRespbaseinfo(String id, String caseid, String casecode, String carriedstatus, String regtype, String recdate, String suetype, String caseseqno, String lawid, String caseinnerid, String jurilaw, String expresscom,
			String expressid, String resdept, String deptlevel, String deptname, String adminlevel, String adminbehavior, String regiontype, String fringereq, String suerequest, String evidence,
			String suecontents, String lawcontents, String constatus, String ifincourt, String recreport, String recmanage, String operator, String opttime,String ifappeal,String appealtime,String remark,String annex,
			String processid,String protype,BigDecimal nodeid) {
		this.id = id;
		this.caseid = caseid;
		this.casecode = casecode;
		this.carriedstatus = carriedstatus;
		this.regtype = regtype;
		this.recdate = recdate;
		this.suetype = suetype;
		this.caseseqno = caseseqno;
		this.lawid = lawid;
		this.caseinnerid = caseinnerid;
		this.jurilaw = jurilaw;
		this.expresscom = expresscom;
		this.expressid = expressid;
		this.resdept = resdept;
		this.deptlevel = deptlevel;
		this.deptname = deptname;
		this.adminlevel = adminlevel;
		this.adminbehavior = adminbehavior;
		this.regiontype = regiontype;
		this.fringereq = fringereq;
		this.suerequest = suerequest;
		this.evidence = evidence;
		this.suecontents = suecontents;
		this.lawcontents = lawcontents;
		this.constatus = constatus;
		this.ifincourt = ifincourt;
		this.recreport = recreport;
		this.recmanage = recmanage;
		this.operator = operator;
		this.opttime = opttime;
		this.ifappeal=ifappeal;
		this.appealtime=appealtime;
		this.remark=remark;
		this.annex=annex;
		this.processid=processid;
		this.protype=protype;
		this.nodeid=nodeid;
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
	
	@Column(name = "CASEID")
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}
	
	@Column(name = "CASECODE")
	public String getCasecode() {
		return this.casecode;
	}

	public void setCasecode(String casecode) {
		this.casecode = casecode;
	}

	@Column(name = "CARRIEDSTATUS", length = 2)
	public String getCarriedstatus() {
		return this.carriedstatus;
	}

	public void setCarriedstatus(String carriedstatus) {
		this.carriedstatus = carriedstatus;
	}

	@Column(name = "REGTYPE", length = 2)
	public String getRegtype() {
		return this.regtype;
	}

	public void setRegtype(String regtype) {
		this.regtype = regtype;
	}

	@Column(name = "RECDATE", length = 10)
	public String getRecdate() {
		return this.recdate;
	}

	public void setRecdate(String recdate) {
		this.recdate = recdate;
	}

	@Column(name = "SUETYPE", length = 2)
	public String getSuetype() {
		return this.suetype;
	}

	public void setSuetype(String suetype) {
		this.suetype = suetype;
	}

	@Column(name = "CASESEQNO", length = 50)
	public String getCaseseqno() {
		return this.caseseqno;
	}

	public void setCaseseqno(String caseseqno) {
		this.caseseqno = caseseqno;
	}

	@Column(name = "LAWID", length = 50)
	public String getLawid() {
		return this.lawid;
	}

	public void setLawid(String lawid) {
		this.lawid = lawid;
	}

	@Column(name = "CASEINNERID", length = 50)
	public String getCaseinnerid() {
		return this.caseinnerid;
	}

	public void setCaseinnerid(String caseinnerid) {
		this.caseinnerid = caseinnerid;
	}

	@Column(name = "JURILAW", length = 2)
	public String getJurilaw() {
		return this.jurilaw;
	}

	public void setJurilaw(String jurilaw) {
		this.jurilaw = jurilaw;
	}

	@Column(name = "EXPRESSCOM", length = 100)
	public String getExpresscom() {
		return this.expresscom;
	}

	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}

	@Column(name = "EXPRESSID", length = 50)
	public String getExpressid() {
		return this.expressid;
	}

	public void setExpressid(String expressid) {
		this.expressid = expressid;
	}

	@Column(name = "RESDEPT", length = 2)
	public String getResdept() {
		return this.resdept;
	}

	public void setResdept(String resdept) {
		this.resdept = resdept;
	}

	@Column(name = "DEPTLEVEL", length = 2)
	public String getDeptlevel() {
		return this.deptlevel;
	}

	public void setDeptlevel(String deptlevel) {
		this.deptlevel = deptlevel;
	}

	@Column(name = "DEPTNAME", length = 100)
	public String getDeptname() {
		return this.deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	@Column(name = "ADMINLEVEL", length = 2)
	public String getAdminlevel() {
		return this.adminlevel;
	}

	public void setAdminlevel(String adminlevel) {
		this.adminlevel = adminlevel;
	}

	@Column(name = "ADMINBEHAVIOR", length = 2)
	public String getAdminbehavior() {
		return this.adminbehavior;
	}

	public void setAdminbehavior(String adminbehavior) {
		this.adminbehavior = adminbehavior;
	}

	@Column(name = "REGIONTYPE", length = 2)
	public String getRegiontype() {
		return this.regiontype;
	}

	public void setRegiontype(String regiontype) {
		this.regiontype = regiontype;
	}

	@Column(name = "FRINGEREQ", length = 500)
	public String getFringereq() {
		return this.fringereq;
	}

	public void setFringereq(String fringereq) {
		this.fringereq = fringereq;
	}

	@Column(name = "SUEREQUEST", length = 500)
	public String getSuerequest() {
		return this.suerequest;
	}

	public void setSuerequest(String suerequest) {
		this.suerequest = suerequest;
	}

	@Column(name = "EVIDENCE", length = 50)
	public String getEvidence() {
		return this.evidence;
	}

	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}

	@Column(name = "SUECONTENTS", length = 50)
	public String getSuecontents() {
		return this.suecontents;
	}

	public void setSuecontents(String suecontents) {
		this.suecontents = suecontents;
	}

	@Column(name = "LAWCONTENTS", length = 50)
	public String getLawcontents() {
		return this.lawcontents;
	}

	public void setLawcontents(String lawcontents) {
		this.lawcontents = lawcontents;
	}

	@Column(name = "CONSTATUS", length = 2)
	public String getConstatus() {
		return this.constatus;
	}

	public void setConstatus(String constatus) {
		this.constatus = constatus;
	}

	@Column(name = "IFINCOURT", length = 1)
	public String getIfincourt() {
		return this.ifincourt;
	}

	public void setIfincourt(String ifincourt) {
		this.ifincourt = ifincourt;
	}

	@Column(name = "RECREPORT", length = 50)
	public String getRecreport() {
		return this.recreport;
	}

	public void setRecreport(String recreport) {
		this.recreport = recreport;
	}

	@Column(name = "RECMANAGE", length = 2)
	public String getRecmanage() {
		return this.recmanage;
	}

	public void setRecmanage(String recmanage) {
		this.recmanage = recmanage;
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
	
	@Column(name = "IFAPPEAL", length = 1)
	public String getIfappeal() {
		return ifappeal;
	}

	public void setIfappeal(String ifappeal) {
		this.ifappeal = ifappeal;
	}
	
	@Column(name = "APPEALTIME", length = 30)
	public String getAppealtime() {
		return appealtime;
	}

	public void setAppealtime(String appealtime) {
		this.appealtime = appealtime;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "ANNEX", length = 100)
	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}
	
	
	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	@Column(name = "PROCESSID", length = 32)
	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	
	@Column(name = "PROTYPE", length = 2)
	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	
	 
	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='REGTYPE' and a.status=0  and a.code=regtype) ")
	public String getRegtypename() {
		return regtypename;
	}

	public void setRegtypename(String regtypename) {
		this.regtypename = regtypename;
	}

	
	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JURILAW' and a.status=0  and a.code=jurilaw) ")
	public String getJurilawname() {
		return jurilawname;
	}

	public void setJurilawname(String jurilawname) {
		this.jurilawname = jurilawname;
	}

	
	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SUETYPE' and a.status=0  and a.code=suetype) ")
	public String getSuetypename() {
		return suetypename;
	}

	public void setSuetypename(String suetypename) {
		this.suetypename = suetypename;
	}
	@Formula("(select a.lawid from B_RESPBASEINFO a where a.id= caseid) ")
	public String getGlahlawid() {
		return glahlawid;
	}

	public void setGlahlawid(String glahlawid) {
		this.glahlawid = glahlawid;
	}

	
}
package com.wfzcx.aros.xzys.po;

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
@Table(name = "B_RESPTRIALINFO")
public class Resptrialinfo {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String lawid;
	private String trailresult;
	private String trialhead;
	private String ifsite;
	private String trialdate;
	private String trialplace;
	private String remark;
	private String operator;
	private String opttime;
	
	
	private String trailresultname;
	private String ifsitename;
	
	
	
	
	public  Resptrialinfo() {
		
	}
	
	
    public  Resptrialinfo(String id,String lawid,String trailresult,String trialhead,String ifsite,String trialdate
    		,String trialplace,String remark,String operator,String opttime) {
    	this.id=id;
    	this.lawid=lawid;
    	this.trailresult=trailresult;
    	this.trialhead=trialhead;
    	this.ifsite=ifsite;
    	this.trialdate=trialdate;
    	this.trialplace=trialplace;
    	this.remark=remark;
    	this.operator=operator;
    	this.opttime=opttime;
	}


    
    @Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID",length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "LAWID", length = 32)
	public String getLawid() {
		return lawid;
	}


	public void setLawid(String lawid) {
		this.lawid = lawid;
	}

	@Column(name = "TRAILRESULT", length = 1000)
	public String getTrailresult() {
		return trailresult;
	}


	public void setTrailresult(String trailresult) {
		this.trailresult = trailresult;
	}

	@Column(name = "TRIALHEAD", length = 1000)
	public String getTrialhead() {
		return trialhead;
	}


	public void setTrialhead(String trialhead) {
		this.trialhead = trialhead;
	}

	@Column(name = "IFSITE", length = 1000)
	public String getIfsite() {
		return ifsite;
	}


	public void setIfsite(String ifsite) {
		this.ifsite = ifsite;
	}

	@Column(name = "TRIALDATE", length = 32)
	public String getTrialdate() {
		return trialdate;
	}


	public void setTrialdate(String trialdate) {
		this.trialdate = trialdate;
	}

	@Column(name = "TRIALPLACE", length = 32)
	public String getTrialplace() {
		return trialplace;
	}


	public void setTrialplace(String trialplace) {
		this.trialplace = trialplace;
	}

	@Column(name = "REMARK", length = 32)
	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "OPERATOR", length = 30)
	public String getOperator() {
		return operator;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 30)
	public String getOpttime() {
		return opttime;
	}


	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='TRAILRESULT' and a.status=0  and a.code=trailresult)")
	public String getTrailresultname() {
		return trailresultname;
	}


	public void setTrailresultname(String trailresultname) {
		this.trailresultname = trailresultname;
	}

	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=ifsite)")
	public String getIfsitename() {
		return ifsitename;
	}


	public void setIfsitename(String ifsitename) {
		this.ifsitename = ifsitename;
	}
	
	
	
	
	

}

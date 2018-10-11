package com.wfzcx.aros.xzys.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BRespbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_RESPREVIEWINFO")
public class Respreviewinfo {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String lawid;
	private String caseact;
	private String casefocus;
	private String remark;
	private String operator;
	private String opttime;
	
	public Respreviewinfo() {
		
	}
	public Respreviewinfo(String id,String lawid,String caseact,String casefocus,String remark,
			String operator,String opttime) {
		this.id = id;
		this.lawid = lawid;
		this.caseact = caseact;
		this.casefocus = casefocus;
		this.remark = remark;
		this.operator = operator;
		this.opttime = opttime;
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
	
	@Column(name = "CASEACT", length = 1000)
	public String getCaseact() {
		return caseact;
	}
	public void setCaseact(String caseact) {
		this.caseact = caseact;
	}
	
	@Column(name = "CASEFOCUS", length = 1000)
	public String getCasefocus() {
		return casefocus;
	}
	public void setCasefocus(String casefocus) {
		this.casefocus = casefocus;
	}
	
	@Column(name = "REMARK", length = 1000)
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

}

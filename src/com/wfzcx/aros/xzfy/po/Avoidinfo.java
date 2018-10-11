package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: BAvoidinfo
 * @Description: 回避信息表实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年8月26日 下午5:33:07
 * @version V1.0
 */
@Entity
@Table(name = "B_AVOIDINFO" )
public class Avoidinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String aid;
	private String caseid;
	private String appname;
	private String defname;
	private String factreason;
	private String operator;
	private String opttime;
	private String lasttime;

	// Constructors

	/** default constructor */
	public Avoidinfo() {
	}

	/** minimal constructor */
	public Avoidinfo(String aid) {
		this.aid = aid;
	}

	/** full constructor */
	public Avoidinfo(String aid, String caseid, String appname,
			String defname, String factreason, String operator, String opttime,
			String lasttime) {
		this.aid = aid;
		this.caseid = caseid;
		this.appname = appname;
		this.defname = defname;
		this.factreason = factreason;
		this.operator = operator;
		this.opttime = opttime;
		this.lasttime = lasttime;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "AID", unique = true, nullable = false, length = 32)
	public String getAid() {
		return this.aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "APPNAME", length = 80)
	public String getAppname() {
		return this.appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	@Column(name = "DEFNAME", length = 80)
	public String getDefname() {
		return this.defname;
	}

	public void setDefname(String defname) {
		this.defname = defname;
	}

	@Column(name = "FACTREASON", length = 1000)
	public String getFactreason() {
		return this.factreason;
	}

	public void setFactreason(String factreason) {
		this.factreason = factreason;
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

	@Column(name = "LASTTIME", length = 19)
	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
}
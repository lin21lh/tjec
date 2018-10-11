package com.wfzcx.aros.zlpc.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BCasequacheckinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASEQUACHECKINFO")
public class BCasequacheckinfo implements java.io.Serializable {

	// Fields

	private String id;
	private String caseid;
	private String standid;
	private String standardname;
	private String stagetype;
	private String inditype;
	private Integer score;
	private Integer actscore;
	private String operator;
	private String optdate;

	// Constructors

	/** default constructor */
	public BCasequacheckinfo() {
	}

	/** minimal constructor */
	public BCasequacheckinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BCasequacheckinfo(String id, String caseid, String standid, String standardname, String stagetype,
			String inditype, Integer score, Integer actscore, String operator, String optdate) {
		this.id = id;
		this.caseid = caseid;
		this.standid = standid;
		this.standardname = standardname;
		this.stagetype = stagetype;
		this.inditype = inditype;
		this.score = score;
		this.actscore = actscore;
		this.operator = operator;
		this.optdate = optdate;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "STANDID", length = 32)
	public String getStandid() {
		return this.standid;
	}

	public void setStandid(String standid) {
		this.standid = standid;
	}

	@Column(name = "STANDARDNAME", length = 1000)
	public String getStandardname() {
		return this.standardname;
	}

	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}

	@Column(name = "STAGETYPE", length = 2)
	public String getStagetype() {
		return this.stagetype;
	}

	public void setStagetype(String stagetype) {
		this.stagetype = stagetype;
	}

	@Column(name = "INDITYPE", length = 2)
	public String getInditype() {
		return this.inditype;
	}

	public void setInditype(String inditype) {
		this.inditype = inditype;
	}

	@Column(name = "SCORE", precision = 22, scale = 0)
	public Integer getScore() {
		return this.score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "ACTSCORE", precision = 22, scale = 0)
	public Integer getActscore() {
		return this.actscore;
	}

	public void setActscore(Integer actscore) {
		this.actscore = actscore;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTDATE", length = 10)
	public String getOptdate() {
		return this.optdate;
	}

	public void setOptdate(String optdate) {
		this.optdate = optdate;
	}

}
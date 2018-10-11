package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BSurveyrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_SURVEYRECORD")
public class BSurveyrecord implements java.io.Serializable {

	// Fields

	private String srid;
	private String caseid;
	private String investtime;
	private String place;
	private String invester;
	private String recorder;
	private String efinvester;
	private String sex;
	private String phone;
	private String item;
	private String obligrights;
	private String remark;
	private String lasttime;

	// Constructors

	/** default constructor */
	public BSurveyrecord() {
	}

	/** minimal constructor */
	public BSurveyrecord(String srid) {
		this.srid = srid;
	}

	/** full constructor */
	public BSurveyrecord(String srid, String caseid, String investtime,
			String place, String invester, String recorder, String efinvester,
			String sex, String phone, String item, String obligrights,
			String remark, String lasttime) {
		this.srid = srid;
		this.caseid = caseid;
		this.investtime = investtime;
		this.place = place;
		this.invester = invester;
		this.recorder = recorder;
		this.efinvester = efinvester;
		this.sex = sex;
		this.phone = phone;
		this.item = item;
		this.obligrights = obligrights;
		this.remark = remark;
		this.lasttime = lasttime;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "SRID")
	public String getSrid() {
		return this.srid;
	}

	public void setSrid(String srid) {
		this.srid = srid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "INVESTTIME", length = 19)
	public String getInvesttime() {
		return this.investtime;
	}

	public void setInvesttime(String investtime) {
		this.investtime = investtime;
	}

	@Column(name = "PLACE", length = 100)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "INVESTER", length = 80)
	public String getInvester() {
		return this.invester;
	}

	public void setInvester(String invester) {
		this.invester = invester;
	}

	@Column(name = "RECORDER", length = 80)
	public String getRecorder() {
		return this.recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	@Column(name = "EFINVESTER", length = 80)
	public String getEfinvester() {
		return this.efinvester;
	}

	public void setEfinvester(String efinvester) {
		this.efinvester = efinvester;
	}

	@Column(name = "SEX", length = 2)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "PHONE", length = 13)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "ITEM", length = 500)
	public String getItem() {
		return this.item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	@Column(name = "OBLIGRIGHTS", length = 500)
	public String getObligrights() {
		return this.obligrights;
	}

	public void setObligrights(String obligrights) {
		this.obligrights = obligrights;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "LASTTIME", length = 19)
	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

}
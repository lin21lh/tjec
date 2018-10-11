package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BTrialrecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_TRIALRECORD")
public class BTrialrecord implements java.io.Serializable {

	// Fields

	private String trid;
	private String caseid;
	private String casename;
	private String casttime;
	private String place;
	private String courter;
	private String moderator;
	private String recorder;
	private String participants;
	private String remark;
	private String lasttime;

	// Constructors

	/** default constructor */
	public BTrialrecord() {
	}

	/** minimal constructor */
	public BTrialrecord(String trid) {
		this.trid = trid;
	}

	/** full constructor */
	public BTrialrecord(String trid, String caseid, String casename,
			String casttime, String place, String courter, String moderator,
			String recorder, String participants, String remark, String lasttime) {
		this.trid = trid;
		this.caseid = caseid;
		this.casename = casename;
		this.casttime = casttime;
		this.place = place;
		this.courter = courter;
		this.moderator = moderator;
		this.recorder = recorder;
		this.participants = participants;
		this.remark = remark;
		this.lasttime = lasttime;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "TRID")
	public String getTrid() {
		return this.trid;
	}

	public void setTrid(String trid) {
		this.trid = trid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "CASENAME", length = 200)
	public String getCasename() {
		return this.casename;
	}

	public void setCasename(String casename) {
		this.casename = casename;
	}

	@Column(name = "CASTTIME", length = 19)
	public String getCasttime() {
		return this.casttime;
	}

	public void setCasttime(String casttime) {
		this.casttime = casttime;
	}

	@Column(name = "PLACE", length = 200)
	public String getPlace() {
		return this.place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "COURTER", length = 200)
	public String getCourter() {
		return this.courter;
	}

	public void setCourter(String courter) {
		this.courter = courter;
	}

	@Column(name = "MODERATOR", length = 80)
	public String getModerator() {
		return this.moderator;
	}

	public void setModerator(String moderator) {
		this.moderator = moderator;
	}

	@Column(name = "RECORDER", length = 80)
	public String getRecorder() {
		return this.recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	@Column(name = "PARTICIPANTS", length = 500)
	public String getParticipants() {
		return this.participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	@Column(name = "REMARK", length = 500)
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
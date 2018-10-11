package com.wfzcx.ppms.execute.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProAssess entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_ASSESS")
@GenericGenerator(name = "SEQ_PRO_ASSESS", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_ASSESS") })
public class ProAssess implements java.io.Serializable {

	// Fields

	private int assessid;
	private int projectid;
	private String assessTime;
	private String assessContent;
	private String assessPath;

	// Constructors

	/** default constructor */
	public ProAssess() {
	}

	/** minimal constructor */
	public ProAssess(int assessid, int projectid,
			String assessTime, String assessContent) {
		this.assessid = assessid;
		this.projectid = projectid;
		this.assessTime = assessTime;
		this.assessContent = assessContent;
	}

	/** full constructor */
	public ProAssess(int assessid, int projectid,
			String assessTime, String assessContent, String assessPath) {
		this.assessid = assessid;
		this.projectid = projectid;
		this.assessTime = assessTime;
		this.assessContent = assessContent;
		this.assessPath = assessPath;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_ASSESS")
	@Column(name = "ASSESSID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getAssessid() {
		return this.assessid;
	}

	public void setAssessid(int assessid) {
		this.assessid = assessid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	@Column(name = "ASSESS_TIME", nullable = false, length = 20)
	public String getAssessTime() {
		return this.assessTime;
	}

	public void setAssessTime(String assessTime) {
		this.assessTime = assessTime;
	}

	@Column(name = "ASSESS_CONTENT", nullable = false, length = 1000)
	public String getAssessContent() {
		return this.assessContent;
	}

	public void setAssessContent(String assessContent) {
		this.assessContent = assessContent;
	}

	@Column(name = "ASSESS_PATH", length = 200)
	public String getAssessPath() {
		return this.assessPath;
	}

	public void setAssessPath(String assessPath) {
		this.assessPath = assessPath;
	}

}
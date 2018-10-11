package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BCasesperelabaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_CASESPERELABASEINFO")
public class BCasesperelabaseinfo implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String groupid;
	private String caseid;
	private String processid;

	// Constructors

	/** default constructor */
	public BCasesperelabaseinfo() {
	}

	/** minimal constructor */
	public BCasesperelabaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BCasesperelabaseinfo(String id, String groupid, String caseid, String processid) {
		this.id = id;
		this.groupid = groupid;
		this.caseid = caseid;
		this.processid = processid;
	}

	// Property accessors
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

	@Column(name = "GROUPID", length = 32)
	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

}
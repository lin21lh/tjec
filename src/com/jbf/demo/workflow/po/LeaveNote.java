package com.jbf.demo.workflow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "DEMO_LEAVE_NOTE", uniqueConstraints = @UniqueConstraint(columnNames = "UUID"))
public class LeaveNote implements java.io.Serializable {

	@Id
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "APPLYER", length = 50)
	private String applyer;

	@Column(name = "CONTENT", length = 50)
	private String content;

	@Column(name = "WFID", length = 50)
	private String wfid;

	@Column(name = "WFNODE", length = 50)
	private String wfnode;

	@Column(name = "CRDATE", length = 50)
	private String crdate;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getApplyer() {
		return applyer;
	}

	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWfid() {
		return wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	public String getWfnode() {
		return wfnode;
	}

	public void setWfnode(String wfnode) {
		this.wfnode = wfnode;
	}

	public String getCrdate() {
		return crdate;
	}

	public void setCrdate(String crdate) {
		this.crdate = crdate;
	}

}

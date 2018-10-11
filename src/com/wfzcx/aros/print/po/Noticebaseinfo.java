/**
 * @Description: 通知书基本信息
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.print.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BNoticebaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_NOTICEBASEINFO")
public class Noticebaseinfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String noticeid;
	private String caseid;
	private String doctype;
	private String buildtime;
	private String contents;
	private String operator;
	private String opttime;

	// Constructors

	/** default constructor */
	public Noticebaseinfo() {
	}

	/** minimal constructor */
	public Noticebaseinfo(String noticeid) {
		this.noticeid = noticeid;
	}

	/** full constructor */
	public Noticebaseinfo(String noticeid, String caseid, String doctype,
			String buildtime, String contents, String operator, String opttime)
	{
		this.noticeid = noticeid;
		this.caseid = caseid;
		this.doctype = doctype;
		this.buildtime = buildtime;
		this.contents = contents;
		this.operator = operator;
		this.opttime = opttime;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "NOTICEID", unique = true, nullable = false, length = 32)
	public String getNoticeid() {
		return this.noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "DOCTYPE", length = 2)
	public String getDoctype() {
		return this.doctype;
	}

	public void setDoctype(String doctype) {
		this.doctype = doctype;
	}

	@Column(name = "BUILDTIME", length = 20)
	public String getBuildtime() {
		return this.buildtime;
	}

	public void setBuildtime(String buildtime) {
		this.buildtime = buildtime;
	}

	@Lob
	@Column(name = "CONTENTS")
	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
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

}
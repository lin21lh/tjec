package com.jbf.sys.regist.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProSocialRegist entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_SOCIAL_REGIST")
@GenericGenerator(name = "SEQ_PRO_SOCIAL_REGIST", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_SOCIAL_REGIST") })
public class ProSocialRegist implements java.io.Serializable {

	// Fields

	private int socialid;
	private String usercode;
	private String username;
	private String userpswd;
	private String linkperson;
	private String linkphone;
	private String orgcode;
	private String orgname;
	private String iscombo;
	private String status;
	private String applicationTime;
	private String auditTime;
	private String remark;
	private String auditUser;
	private String categoryCode;
	private String categoryName;
	private String preferencesCode;
	private String preferencesName;

	// Constructors

	@Column(name = "PREFERENCES_CODE", length = 18)
	public String getPreferencesCode() {
		return preferencesCode;
	}

	public void setPreferencesCode(String preferencesCode) {
		this.preferencesCode = preferencesCode;
	}

	@Column(name = "PREFERENCES_NAME", length = 60)
	public String getPreferencesName() {
		return preferencesName;
	}

	public void setPreferencesName(String preferencesName) {
		this.preferencesName = preferencesName;
	}

	/** default constructor */
	public ProSocialRegist() {
	}

	/** minimal constructor */
	public ProSocialRegist(int socialid, String usercode,
			String username, String userpswd, String linkperson,
			String linkphone, String orgcode, String orgname, String status) {
		this.socialid = socialid;
		this.usercode = usercode;
		this.username = username;
		this.userpswd = userpswd;
		this.linkperson = linkperson;
		this.linkphone = linkphone;
		this.orgcode = orgcode;
		this.orgname = orgname;
		this.status = status;
	}

	/** full constructor */
	public ProSocialRegist(int socialid, String usercode,
			String username, String userpswd, String linkperson,
			String linkphone, String orgcode, String orgname, String iscombo,
			String status, String applicationTime, String auditTime, String remark) {
		this.socialid = socialid;
		this.usercode = usercode;
		this.username = username;
		this.userpswd = userpswd;
		this.linkperson = linkperson;
		this.linkphone = linkphone;
		this.orgcode = orgcode;
		this.orgname = orgname;
		this.iscombo = iscombo;
		this.status = status;
		this.applicationTime = applicationTime;
		this.auditTime = auditTime;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_SOCIAL_REGIST")
	@Column(name = "SOCIALID", unique = true, nullable = false, scale = 0)
	public int getSocialid() {
		return this.socialid;
	}

	public void setSocialid(int socialid) {
		this.socialid = socialid;
	}

	@Column(name = "USERCODE", nullable = false, length = 50)
	public String getUsercode() {
		return this.usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	@Column(name = "USERNAME", nullable = false, length = 50)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "USERPSWD", nullable = false, length = 100)
	public String getUserpswd() {
		return this.userpswd;
	}

	public void setUserpswd(String userpswd) {
		this.userpswd = userpswd;
	}

	@Column(name = "LINKPERSON", nullable = false, length = 50)
	public String getLinkperson() {
		return this.linkperson;
	}

	public void setLinkperson(String linkperson) {
		this.linkperson = linkperson;
	}

	@Column(name = "LINKPHONE", nullable = false, length = 50)
	public String getLinkphone() {
		return this.linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}

	@Column(name = "ORGCODE", nullable = false, length = 50)
	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	@Column(name = "ORGNAME", nullable = false, length = 100)
	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	@Column(name = "ISCOMBO", length = 10)
	public String getIscombo() {
		return this.iscombo;
	}

	public void setIscombo(String iscombo) {
		this.iscombo = iscombo;
	}

	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "APPLICATION_TIME", length = 19)
	public String getApplicationTime() {
		return this.applicationTime;
	}

	public void setApplicationTime(String applicationTime) {
		this.applicationTime = applicationTime;
	}

	@Column(name = "AUDIT_TIME", length = 19)
	public String getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	@Column(name = "REMARK", length = 400)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	
	@Column(name = "AUDIT_USER", length = 19)
	public String getAuditUser() {
		return auditUser;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	@Column(name = "CATEGORY_CODE", length = 9)
	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	@Column(name = "CATEGORY_NAME", length = 60)
	public String getCategoryName() {
		return categoryName;
	}


}
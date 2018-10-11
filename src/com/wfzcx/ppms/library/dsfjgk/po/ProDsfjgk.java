package com.wfzcx.ppms.library.dsfjgk.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProDsfjgk entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DSFJGK")
@SequenceGenerator(name="SEQ_PRO_DSFJGK",sequenceName="SEQ_PRO_DSFJGK")
@GenericGenerator(name = "SEQ_PRO_DSFJGK", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DSFJGK") })
public class ProDsfjgk implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer dsfjgid;
	private String organCode;
	private String organName;
	private String consignor;
	private String projectManager;
	private String phone;
	private String mobile;
	private String content;
	private String weixin;
	private String sfyx;

	// Constructors

	/** default constructor */
	public ProDsfjgk() {
	}

	/** minimal constructor */
	public ProDsfjgk(Integer dsfjgid) {
		this.dsfjgid = dsfjgid;
	}

	/** full constructor */
	public ProDsfjgk(Integer dsfjgid, String organCode, String organName, String consignor, String projectManager,
			String phone, String mobile, String content, String weixin,String sfyx) {
		this.dsfjgid = dsfjgid;
		this.organCode = organCode;
		this.organName = organName;
		this.consignor = consignor;
		this.projectManager = projectManager;
		this.phone = phone;
		this.mobile = mobile;
		this.content = content;
		this.weixin = weixin;
		this.sfyx = sfyx;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DSFJGK")
	@Column(name = "DSFJGID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getDsfjgid() {
		return this.dsfjgid;
	}

	public void setDsfjgid(Integer dsfjgid) {
		this.dsfjgid = dsfjgid;
	}

	@Column(name = "ORGAN_CODE", length = 50)
	public String getOrganCode() {
		return this.organCode;
	}

	public void setOrganCode(String organCode) {
		this.organCode = organCode;
	}

	@Column(name = "ORGAN_NAME", length = 100)
	public String getOrganName() {
		return this.organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	@Column(name = "CONSIGNOR", length = 100)
	public String getConsignor() {
		return this.consignor;
	}

	public void setConsignor(String consignor) {
		this.consignor = consignor;
	}

	@Column(name = "PROJECT_MANAGER", length = 100)
	public String getProjectManager() {
		return this.projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	@Column(name = "PHONE", length = 50)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "MOBILE", length = 50)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "CONTENT", length = 1000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "WEIXIN", length = 50)
	public String getWeixin() {
		return this.weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	
	@Column(name = "SFYX", length = 1)
	public String getSfyx() {
		return sfyx;
	}

	public void setSfyx(String sfyx) {
		this.sfyx = sfyx;
	}

}
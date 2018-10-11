package com.wfzcx.ppms.discern.po;

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
 * ProDlpjCb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DLPJ_CB")
@SequenceGenerator(name="SEQ_PRO_DLPJ_CB",sequenceName="SEQ_PRO_DLPJ_CB")
@GenericGenerator(name = "SEQ_PRO_DLPJ_CB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DLPJ_CB") })
public class ProDlpjCb implements java.io.Serializable {

	// Fields

	private Integer dlpjcbid;
	private Integer dlpjid;
	private Integer dsfjgid;
	private String organCode;
	private String organName;
	private String consignor;
	private String projectManager;
	private String phone;
	private String mobile;
	private String content;
	private String entrustTime;

	// Constructors

	/** default constructor */
	public ProDlpjCb() {
	}

	/** minimal constructor */
	public ProDlpjCb(Integer dlpjid, Integer dsfjgid) {
		this.dlpjid = dlpjid;
		this.dsfjgid = dsfjgid;
	}

	/** full constructor */
	public ProDlpjCb(Integer dlpjid, Integer dsfjgid, String organCode,
			String organName, String consignor, String projectManager,
			String phone, String mobile, String content, String entrustTime) {
		this.dlpjid = dlpjid;
		this.dsfjgid = dsfjgid;
		this.organCode = organCode;
		this.organName = organName;
		this.consignor = consignor;
		this.projectManager = projectManager;
		this.phone = phone;
		this.mobile = mobile;
		this.content = content;
		this.entrustTime = entrustTime;
	}

	// Property accessors
	@SequenceGenerator(name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DLPJ_CB")
	@Column(name = "DLPJCBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getDlpjcbid() {
		return this.dlpjcbid;
	}

	public void setDlpjcbid(Integer dlpjcbid) {
		this.dlpjcbid = dlpjcbid;
	}

	@Column(name = "DLPJID", nullable = false, precision = 20, scale = 0)
	public Integer getDlpjid() {
		return this.dlpjid;
	}

	public void setDlpjid(Integer dlpjid) {
		this.dlpjid = dlpjid;
	}

	@Column(name = "DSFJGID", nullable = false, precision = 20, scale = 0)
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

	@Column(name = "ENTRUST_TIME", length = 20)
	public String getEntrustTime() {
		return this.entrustTime;
	}

	public void setEntrustTime(String entrustTime) {
		this.entrustTime = entrustTime;
	}

}
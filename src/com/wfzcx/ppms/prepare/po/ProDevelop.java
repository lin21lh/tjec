package com.wfzcx.ppms.prepare.po;

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
 * ProDevelop entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_DEVELOP")
@SequenceGenerator(name="SEQ_PRO_DEVELOP",sequenceName="SEQ_PRO_DEVELOP")
@GenericGenerator(name = "SEQ_PRO_DEVELOP", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_DEVELOP") })
public class ProDevelop implements java.io.Serializable {

	// Fields

	private Integer deveid;
	private Integer projectid;
	private int deveYear;
	private String implementOrgan;
	private String implementPerson;
	private String implementPhone;
	private String purchaseType;
	private String remark;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String devPath;

	/** default constructor */
	public ProDevelop() {
	}

	/** minimal constructor */
	public ProDevelop(int deveid, int projectid, Short deveYear,
			String implementOrgan, String implementPerson,
			String implementPhone, String purchaseType, String createuser,
			String createtime) {
		this.deveid = deveid;
		this.projectid = projectid;
		this.deveYear = deveYear;
		this.implementOrgan = implementOrgan;
		this.implementPerson = implementPerson;
		this.implementPhone = implementPhone;
		this.purchaseType = purchaseType;
		this.createuser = createuser;
		this.createtime = createtime;
	}

	/** full constructor */
	public ProDevelop(int deveid, int projectid, Short deveYear,
			String implementOrgan, String implementPerson,
			String implementPhone, String purchaseType, String remark,
			String createuser, String createtime, String updateuser,
			String updatetime) {
		this.deveid = deveid;
		this.projectid = projectid;
		this.deveYear = deveYear;
		this.implementOrgan = implementOrgan;
		this.implementPerson = implementPerson;
		this.implementPhone = implementPhone;
		this.purchaseType = purchaseType;
		this.remark = remark;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_DEVELOP")
	@Column(name = "DEVEID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getDeveid() {
		return this.deveid;
	}

	public void setDeveid(Integer deveid) {
		this.deveid = deveid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	@Column(name = "DEVE_YEAR", nullable = false, precision = 4, scale = 0)
	public int getDeveYear() {
		return this.deveYear;
	}

	public void setDeveYear(int deveYear) {
		this.deveYear = deveYear;
	}

	@Column(name = "IMPLEMENT_ORGAN", nullable = true, length = 100)
	public String getImplementOrgan() {
		return this.implementOrgan;
	}

	public void setImplementOrgan(String implementOrgan) {
		this.implementOrgan = implementOrgan;
	}

	@Column(name = "IMPLEMENT_PERSON", nullable = true, length = 100)
	public String getImplementPerson() {
		return this.implementPerson;
	}

	public void setImplementPerson(String implementPerson) {
		this.implementPerson = implementPerson;
	}

	@Column(name = "IMPLEMENT_PHONE", nullable = true, length = 100)
	public String getImplementPhone() {
		return this.implementPhone;
	}

	public void setImplementPhone(String implementPhone) {
		this.implementPhone = implementPhone;
	}

	@Column(name = "PURCHASE_TYPE", nullable = false, length = 1)
	public String getPurchaseType() {
		return this.purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATEUSER", nullable = false, length = 20)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Column(name = "CREATETIME", nullable = false, length = 20)
	public String getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Column(name = "UPDATEUSER", length = 20)
	public String getUpdateuser() {
		return this.updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	@Column(name = "UPDATETIME", length = 20)
	public String getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	@Column(name = "DEV_PATH", length = 100)
	public String getDevPath() {
		return devPath;
	}

	public void setDevPath(String devPath) {
		this.devPath = devPath;
	}

}
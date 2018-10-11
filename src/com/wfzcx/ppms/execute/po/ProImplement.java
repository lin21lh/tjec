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
 * ProImplement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_IMPLEMENT")
@GenericGenerator(name = "SEQ_PRO_IMPLEMENT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_IMPLEMENT") })
public class ProImplement implements java.io.Serializable {

	// Fields

	private int implementid;
	private String projectCompany;
	private String establishTime;
	private String safeguardMeasure;
	private String safeguardMeasurePath;
	private String financeTime;
	private String propertyTime;
	private String startTime;
	private String endTime;
	private String remark;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String datatype;
	private int projectid;

	// Constructors

	/** default constructor */
	public ProImplement() {
	}

	/** minimal constructor */
	public ProImplement(int implementid, String projectCompany,
			String establishTime, String safeguardMeasure, String financeTime,
			String propertyTime, String startTime, String endTime,
			String datatype, int projectid) {
		this.implementid = implementid;
		this.projectCompany = projectCompany;
		this.establishTime = establishTime;
		this.safeguardMeasure = safeguardMeasure;
		this.financeTime = financeTime;
		this.propertyTime = propertyTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.datatype = datatype;
		this.projectid = projectid;
	}

	/** full constructor */
	public ProImplement(int implementid, String projectCompany,
			String establishTime, String safeguardMeasure,
			String safeguardMeasurePath, String financeTime,
			String propertyTime, String startTime, String endTime,
			String remark, String status, String createuser, String createtime,
			String updateuser, String updatetime, String datatype,
			int projectid) {
		this.implementid = implementid;
		this.projectCompany = projectCompany;
		this.establishTime = establishTime;
		this.safeguardMeasure = safeguardMeasure;
		this.safeguardMeasurePath = safeguardMeasurePath;
		this.financeTime = financeTime;
		this.propertyTime = propertyTime;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remark = remark;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.datatype = datatype;
		this.projectid = projectid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_IMPLEMENT")
	@Column(name = "IMPLEMENTID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getImplementid() {
		return this.implementid;
	}

	public void setImplementid(int implementid) {
		this.implementid = implementid;
	}

	@Column(name = "PROJECT_COMPANY", nullable = false, length = 100)
	public String getProjectCompany() {
		return this.projectCompany;
	}

	public void setProjectCompany(String projectCompany) {
		this.projectCompany = projectCompany;
	}

	@Column(name = "ESTABLISH_TIME", nullable = false, length = 20)
	public String getEstablishTime() {
		return this.establishTime;
	}

	public void setEstablishTime(String establishTime) {
		this.establishTime = establishTime;
	}

	@Column(name = "SAFEGUARD_MEASURE", nullable = false, length = 1000)
	public String getSafeguardMeasure() {
		return this.safeguardMeasure;
	}

	public void setSafeguardMeasure(String safeguardMeasure) {
		this.safeguardMeasure = safeguardMeasure;
	}

	@Column(name = "SAFEGUARD_MEASURE_PATH", length = 200)
	public String getSafeguardMeasurePath() {
		return this.safeguardMeasurePath;
	}

	public void setSafeguardMeasurePath(String safeguardMeasurePath) {
		this.safeguardMeasurePath = safeguardMeasurePath;
	}

	@Column(name = "FINANCE_TIME", nullable = false, length = 20)
	public String getFinanceTime() {
		return this.financeTime;
	}

	public void setFinanceTime(String financeTime) {
		this.financeTime = financeTime;
	}

	@Column(name = "PROPERTY_TIME", nullable = false, length = 20)
	public String getPropertyTime() {
		return this.propertyTime;
	}

	public void setPropertyTime(String propertyTime) {
		this.propertyTime = propertyTime;
	}

	@Column(name = "START_TIME", nullable = false, length = 20)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", nullable = false, length = 20)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATEUSER", length = 20)
	public String getCreateuser() {
		return this.createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	@Column(name = "CREATETIME", length = 20)
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

	@Column(name = "DATATYPE", nullable = false, length = 2)
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

}
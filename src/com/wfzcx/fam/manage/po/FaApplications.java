package com.wfzcx.fam.manage.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * FaApplications entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FA_APPLICATIONS")
@SequenceGenerator(name="SEQ_FA_APPLICATIONS",sequenceName="SEQ_FA_APPLICATIONS")
@GenericGenerator(name = "SEQ_FA_APPLICATIONS", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_FA_APPLICATIONS") })
public class FaApplications implements java.io.Serializable {

	// Fields

	/**
	 * 申请序号
	 */
	private Integer applicationId;
	/**
	 * 预算单位code
	 */
	private String bdgagencycode;
	/**
	 * 备案类型，1：新开立，2：变更，3：撤销
	 */
	private Integer type;
	/**
	 * 单位性质
	 */
	private String deptNature;
	/**
	 * 主管部门
	 */
	private String supervisorDept;
	/**
	 * 单位地址
	 */
	private String deptAddress;
	/**
	 * 理由
	 */
	private String applyReason;
	/**
	 * 工作流id
	 */
	private String wfid;
	/**
	 * 工作流状态
	 */
	private String wfstatus;
	/**
	 * 工作流是否退回
	 */
	private Integer wfisback;
	/**
	 * 预算单位全称
	 */
	private String bdgagencyname;
	/**
	 * 联系方式（多个用,隔开）
	 */
	private String phonenumber;
	/**
	 * 是否开户 0：未开户，1：已开户，已开户必须将银行账户信息填入，以后不需再次备案
	 */
	private String isopen;
	/**
	 * 是否备案 0：未备案，1：已备案
	 */
	private String isregister;
	/**
	 * 状态（预留）
	 */
	private String status;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改人
	 */
	private String updateUser;
	/**
	 * 修改时间
	 */
	private String updateTime;
	/**
	 * 联系人
	 */
	private String linkman;
	/**
	 * 变更事项/注销事项
	 */
	private String changetype;
	@Column(name = "CHANGETYPE", length = 1)
	public String getChangetype() {
		return changetype;
	}

	public void setChangetype(String changetype) {
		this.changetype = changetype;
	}

	// Constructors
	private String bdgagencycn;
	@Formula("bdgagencycode ||'-'|| bdgagencyname")
	public String getBdgagencycn() {
		return bdgagencycn;
	}

	public void setBdgagencycn(String bdgagencycn) {
		this.bdgagencycn = bdgagencycn;
	}
	/** default constructor */
	public FaApplications() {
	}

	/** minimal constructor */
	public FaApplications(Integer applicationId, String bdgagencycode,
			Integer type, String deptNature, String applyReason,
			String wfstatus, String phonenumber, String isopen,
			String isregister, String createUser, String createTime) {
		this.applicationId = applicationId;
		this.bdgagencycode = bdgagencycode;
		this.type = type;
		this.deptNature = deptNature;
		this.applyReason = applyReason;
		this.wfstatus = wfstatus;
		this.phonenumber = phonenumber;
		this.isopen = isopen;
		this.isregister = isregister;
		this.createUser = createUser;
		this.createTime = createTime;
	}

	/** full constructor */
	public FaApplications(Integer applicationId, String bdgagencycode,
			Integer type, String deptNature, String supervisorDept,
			String deptAddress, String applyReason, String wfid,
			String wfstatus, Integer wfisback, String bdgagencyname,
			String phonenumber, String isopen, String isregister,
			String status, String remark, String createUser, String createTime,
			String updateUser, String updateTime, String linkman) {
		this.applicationId = applicationId;
		this.bdgagencycode = bdgagencycode;
		this.type = type;
		this.deptNature = deptNature;
		this.supervisorDept = supervisorDept;
		this.deptAddress = deptAddress;
		this.applyReason = applyReason;
		this.wfid = wfid;
		this.wfstatus = wfstatus;
		this.wfisback = wfisback;
		this.bdgagencyname = bdgagencyname;
		this.phonenumber = phonenumber;
		this.isopen = isopen;
		this.isregister = isregister;
		this.status = status;
		this.remark = remark;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.linkman = linkman;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_FA_APPLICATIONS")
	@Column(name = "APPLICATION_ID", nullable = false, precision = 9, scale = 0)
	public Integer getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "BDGAGENCYCODE", nullable = false, length = 50)
	public String getBdgagencycode() {
		return this.bdgagencycode;
	}

	public void setBdgagencycode(String bdgagencycode) {
		this.bdgagencycode = bdgagencycode;
	}

	@Column(name = "TYPE", nullable = false, precision = 1, scale = 0)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "DEPT_NATURE", nullable = false, length = 2)
	public String getDeptNature() {
		return this.deptNature;
	}

	public void setDeptNature(String deptNature) {
		this.deptNature = deptNature;
	}

	@Column(name = "SUPERVISOR_DEPT", length = 50)
	public String getSupervisorDept() {
		return this.supervisorDept;
	}

	public void setSupervisorDept(String supervisorDept) {
		this.supervisorDept = supervisorDept;
	}

	@Column(name = "DEPT_ADDRESS", length = 500)
	public String getDeptAddress() {
		return this.deptAddress;
	}

	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}

	@Column(name = "APPLY_REASON", nullable = false, length = 500)
	public String getApplyReason() {
		return this.applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	@Column(name = "WFID", length = 50)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "WFSTATUS", nullable = false, length = 50)
	public String getWfstatus() {
		return this.wfstatus;
	}

	public void setWfstatus(String wfstatus) {
		this.wfstatus = wfstatus;
	}

	@Column(name = "WFISBACK", precision = 1, scale = 0)
	public Integer getWfisback() {
		return this.wfisback;
	}

	public void setWfisback(Integer wfisback) {
		this.wfisback = wfisback;
	}

	@Column(name = "BDGAGENCYNAME", length = 100)
	public String getBdgagencyname() {
		return this.bdgagencyname;
	}

	public void setBdgagencyname(String bdgagencyname) {
		this.bdgagencyname = bdgagencyname;
	}

	@Column(name = "PHONENUMBER", nullable = false, length = 50)
	public String getPhonenumber() {
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	@Column(name = "ISOPEN", length = 1)
	public String getIsopen() {
		return this.isopen;
	}

	public void setIsopen(String isopen) {
		this.isopen = isopen;
	}

	@Column(name = "ISREGISTER", length = 1)
	public String getIsregister() {
		return this.isregister;
	}

	public void setIsregister(String isregister) {
		this.isregister = isregister;
	}

	@Column(name = "STATUS", length = 50)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_USER", nullable = false, length = 50)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", nullable = false, length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_USER", length = 50)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "UPDATE_TIME", length = 20)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "LINKMAN", length = 50)
	public String getLinkman() {
		return linkman;
	}
	
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	
}
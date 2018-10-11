package com.wfzcx.ppms.prepare.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProSolution entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_SOLUTION")
@GenericGenerator(name = "SEQ_PRO_SOLUTION", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_SOLUTION") })
public class ProSolution implements java.io.Serializable {

	// Fields

	private int solutionid;
	private int projectid;
	private String datatype;
	private String riskAllocation;
	private String projectFinance;
	private String repayMechanism;
	private String suitedPlan;
	private String contractSystem;
	private String contractCoreContent;
	private String supervisoryRegime;
	private String advancePublishTime;
	private String purchaseNoticeTime;
	private String implementationPlanPath;
	private String wfid;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String orgcode;

	// Constructors

	/** default constructor */
	public ProSolution() {
	}

	/** minimal constructor */
	public ProSolution(int solutionid, int projectid,
			String datatype, String riskAllocation, String projectFinance,
			String repayMechanism, String suitedPlan, String contractSystem,
			String contractCoreContent, String supervisoryRegime,
			String advancePublishTime, String purchaseNoticeTime) {
		this.solutionid = solutionid;
		this.projectid = projectid;
		this.datatype = datatype;
		this.riskAllocation = riskAllocation;
		this.projectFinance = projectFinance;
		this.repayMechanism = repayMechanism;
		this.suitedPlan = suitedPlan;
		this.contractSystem = contractSystem;
		this.contractCoreContent = contractCoreContent;
		this.supervisoryRegime = supervisoryRegime;
		this.advancePublishTime = advancePublishTime;
		this.purchaseNoticeTime = purchaseNoticeTime;
	}

	/** full constructor */
	public ProSolution(int solutionid, int projectid,
			String datatype, String riskAllocation, String projectFinance,
			String repayMechanism, String suitedPlan, String contractSystem,
			String contractCoreContent, String supervisoryRegime,
			String advancePublishTime, String purchaseNoticeTime,
			String implementationPlanPath, String wfid, String status,
			String createuser, String createtime, String updateuser,
			String updatetime) {
		this.solutionid = solutionid;
		this.projectid = projectid;
		this.datatype = datatype;
		this.riskAllocation = riskAllocation;
		this.projectFinance = projectFinance;
		this.repayMechanism = repayMechanism;
		this.suitedPlan = suitedPlan;
		this.contractSystem = contractSystem;
		this.contractCoreContent = contractCoreContent;
		this.supervisoryRegime = supervisoryRegime;
		this.advancePublishTime = advancePublishTime;
		this.purchaseNoticeTime = purchaseNoticeTime;
		this.implementationPlanPath = implementationPlanPath;
		this.wfid = wfid;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_SOLUTION")
	@Column(name = "SOLUTIONID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getSolutionid() {
		return this.solutionid;
	}

	public void setSolutionid(int solutionid) {
		this.solutionid = solutionid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	@Column(name = "DATATYPE", nullable = false, length = 2)
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "RISK_ALLOCATION", nullable = false, length = 1000)
	public String getRiskAllocation() {
		return this.riskAllocation;
	}

	public void setRiskAllocation(String riskAllocation) {
		this.riskAllocation = riskAllocation;
	}

	@Column(name = "PROJECT_FINANCE", nullable = false, length = 1000)
	public String getProjectFinance() {
		return this.projectFinance;
	}

	public void setProjectFinance(String projectFinance) {
		this.projectFinance = projectFinance;
	}

	@Column(name = "REPAY_MECHANISM", nullable = false, length = 1000)
	public String getRepayMechanism() {
		return this.repayMechanism;
	}

	public void setRepayMechanism(String repayMechanism) {
		this.repayMechanism = repayMechanism;
	}

	@Column(name = "SUITED_PLAN", nullable = false, length = 1000)
	public String getSuitedPlan() {
		return this.suitedPlan;
	}

	public void setSuitedPlan(String suitedPlan) {
		this.suitedPlan = suitedPlan;
	}

	@Column(name = "CONTRACT_SYSTEM", nullable = false, length = 1000)
	public String getContractSystem() {
		return this.contractSystem;
	}

	public void setContractSystem(String contractSystem) {
		this.contractSystem = contractSystem;
	}

	@Column(name = "CONTRACT_CORE_CONTENT", nullable = false, length = 1000)
	public String getContractCoreContent() {
		return this.contractCoreContent;
	}

	public void setContractCoreContent(String contractCoreContent) {
		this.contractCoreContent = contractCoreContent;
	}

	@Column(name = "SUPERVISORY_REGIME", nullable = false, length = 1000)
	public String getSupervisoryRegime() {
		return this.supervisoryRegime;
	}

	public void setSupervisoryRegime(String supervisoryRegime) {
		this.supervisoryRegime = supervisoryRegime;
	}

	@Column(name = "ADVANCE_PUBLISH_TIME", nullable = false, length = 20)
	public String getAdvancePublishTime() {
		return this.advancePublishTime;
	}

	public void setAdvancePublishTime(String advancePublishTime) {
		this.advancePublishTime = advancePublishTime;
	}

	@Column(name = "PURCHASE_NOTICE_TIME", nullable = false, length = 20)
	public String getPurchaseNoticeTime() {
		return this.purchaseNoticeTime;
	}

	public void setPurchaseNoticeTime(String purchaseNoticeTime) {
		this.purchaseNoticeTime = purchaseNoticeTime;
	}

	@Column(name = "IMPLEMENTATION_PLAN_PATH", length = 200)
	public String getImplementationPlanPath() {
		return this.implementationPlanPath;
	}

	public void setImplementationPlanPath(String implementationPlanPath) {
		this.implementationPlanPath = implementationPlanPath;
	}

	@Column(name = "WFID", length = 20)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "STATUS", length = 10)
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

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}
	@Column(name = "ORGCODE", length = 50)
	public String getOrgcode() {
		return orgcode;
	}

}
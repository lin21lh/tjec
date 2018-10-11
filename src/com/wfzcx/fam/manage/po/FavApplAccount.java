package com.wfzcx.fam.manage.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.jbf.common.util.StringUtil;

/**
 * FavApplAccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FAV_APPL_ACCOUNT")
public class FavApplAccount implements java.io.Serializable {

	// Fields
	
	private Integer applicationId;
	/**
	 * 预算单位code
	 */
	private String bdgagencycode;
	/**
	 * 备案类型
	 */
	private Integer type;
	/**
	 * 单位性质
	 */
	private String deptNature;
	/**
	 * 上级部门
	 */
	private String supervisorDept;
	/**
	 * 单位地址
	 */
	private String deptAddress;
	/**
	 * 开立、变更、撤销理由
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
	 * 预算单位名称
	 */
	private String bdgagencyname;
	/**
	 * 单位联系人电话
	 */
	private String applPhonenumber;
	/**
	 *单位联系人
	 */
	private String linkman;
	/**
	 *创建时间
	 */
	private String createUser;
	/**
	 *创建人
	 */
	private String createTime;
	/**
	 *修改人
	 */
	private String updateUser;
	/**
	 *修改时间
	 */
	private String updateTime;
	/**
	 *是否备案
	 */
	private String isregister;
	/**
	 *账户id
	 */
	private Integer accountId;
	/**
	 *账户名称
	 */
	private String accountName;
	/**
	 *账户类型
	 */
	private String accountType;
	/**
	 *账号
	 */
	private String accountNumber;
	/**
	 *户号
	 */
	private String accountNum;
	/**
	 *开户行
	 */
	private String bankCode;
	/**
	 *开户时间
	 */
	private String openTime;
	/**
	 *核算内容
	 */
	private String accountContent;
	/**
	 *原账户名称
	 */
	private String oldAccountName;
	/**
	 *原账户开户行
	 */
	private String oldBankCode;
	/**
	 *原账户号
	 */
	private String oldAccountNumber;
	/**
	 *经办人
	 */
	private String responsiblePerson;
	/**
	 *账户联系电话
	 */
	private String acctPhonenumber;
	/**
	 *是否零余额账户
	 */
	private Integer iszero;
	/**
	 *备注
	 */
	private String remark;
	/**
	 *账户性质
	 */
	private String type02;
	/**
	 *原账户性质
	 */
	private String oldType02;
	/**
	 *预算单位名称 翻译后
	 */
	private String bdgagencycn;
	/**
	 *原账户是否零余额账户
	 */
	private String oldIszero;
	
	/**
	 * 原有账户类型
	 */
	private String oldAccountType;
	
	private String oldType02Name;
	
	private String acctWfid;
	private String type02Name;
	private String typeName;
	
	
	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=TYPE02)")
	public String getType02Name() {
		return type02Name;
	}

	public void setType02Name(String type02Name) {
		this.type02Name = type02Name;
	}
	private String acctWfstatus;
	private String legalPerson;//企业法人
	private String idcardno;//身份证号
	private String financialOfficer;//财务负责人
	private String oldLegalPerson;//企业法人
	private String oldIdcardno;//身份证号
	private String oldFinancialOfficer;//财务负责人
	private String oldAccountContent;//核算内容
	@Column(name = "OLD_LEGAL_PERSON", length = 50)
	public String getOldLegalPerson() {
		return oldLegalPerson;
	}

	public void setOldLegalPerson(String oldLegalPerson) {
		this.oldLegalPerson = oldLegalPerson;
	}
	@Column(name = "OLD_IDCARDNO", length = 50)
	public String getOldIdcardno() {
		return oldIdcardno;
	}

	public void setOldIdcardno(String oldIdcardno) {
		this.oldIdcardno = oldIdcardno;
	}
	@Column(name = "OLD_FINANCIAL_OFFICER", length = 50)
	public String getOldFinancialOfficer() {
		return oldFinancialOfficer;
	}

	public void setOldFinancialOfficer(String oldFinancialOfficer) {
		this.oldFinancialOfficer = oldFinancialOfficer;
	}
	@Column(name = "OLD_ACCOUNT_CONTENT", length = 50)
	public String getOldAccountContent() {
		return oldAccountContent;
	}

	public void setOldAccountContent(String oldAccountContent) {
		this.oldAccountContent = oldAccountContent;
	}

	@Column(name = "LEGAL_PERSON", length = 50)
	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	@Column(name = "IDCARDNO", length = 50)
	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}
	@Column(name = "FINANCIAL_OFFICER", length = 50)
	public String getFinancialOfficer() {
		return financialOfficer;
	}

	public void setFinancialOfficer(String financialOfficer) {
		this.financialOfficer = financialOfficer;
	}
	
	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=OLD_TYPE02)")
	public String getOldType02Name() {
		return oldType02Name;
	}

	public void setOldType02Name(String oldType02Name) {
		this.oldType02Name = oldType02Name;
	}

	@Column(name = "OLD_ACCOUNT_TYPE", length = 50)
	public String getOldAccountType() {
		return oldAccountType;
	}

	public void setOldAccountType(String oldAccountType) {
		this.oldAccountType = oldAccountType;
	}
	public String bankName;
	@Formula("(select a.NAME from fav_bank a where a.CODE=bank_code)")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String bankNameCn;
	@Formula("(select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=bank_code)")
	public String getBankNameCn() {
		return bankNameCn;
	}

	public void setBankNameCn(String bankNameCn) {
		this.bankNameCn = bankNameCn;
	}
	/**
	 * 原账户类型名称
	 */
	private String oldAccountTypeName;
	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=OLD_ACCOUNT_TYPE)")
	public String getOldAccountTypeName() {
		return oldAccountTypeName;
	}

	public void setOldAccountTypeName(String oldAccountTypeName) {
		this.oldAccountTypeName = oldAccountTypeName;
	}
	@Column(name = "OLD_ISZERO", precision = 1, scale = 0)
	public String getOldIszero() {
		return oldIszero;
	}

	public void setOldIszero(String oldIszero) {
		this.oldIszero = oldIszero;
	}

	// Constructors
	@Formula("bdgagencycode ||'-'|| bdgagencyname")
	public String getBdgagencycn() {
		return bdgagencycn;
	}

	public void setBdgagencycn(String bdgagencycn) {
		this.bdgagencycn = bdgagencycn;
	}
	/**
	 * 原银行名称
	 */
	private String oldBankName;
	/**
	 *原银行名称 翻译后
	 */
	private String oldBankNameCn;
	
	@Formula("(select a.NAME from fav_bank a where a.CODE=old_bank_code)")
	public String getOldBankName() {
		return oldBankName;
	}

	public void setOldBankName(String oldBankName) {
		this.oldBankName = oldBankName;
	}
	@Formula("(select a.CODE||'-'||a.NAME from fav_bank a where a.CODE=old_bank_code)")
	public String getOldBankNameCn() {
		return oldBankNameCn;
	}

	public void setOldBankNameCn(String oldBankNameCn) {
		this.oldBankNameCn = oldBankNameCn;
	}
	/**
	 * 工作流状态名称
	 */
	private String wfstatusName;
	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='WFSTATUS' and t.status=0  and t.code=WFSTATUS)")
	public String getWfstatusName() {
		return wfstatusName;
	}

	public void setWfstatusName(String wfstatusName) {
		this.wfstatusName = wfstatusName;
	}
	/**
	 * 账户工作流状态名称
	 */
	private String acctWfstatusName;
	@Formula("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='WFSTATUS' and t.status=0  and t.code=ACCT_WFSTATUS)")
	public String getAcctWfstatusName() {
		return acctWfstatusName;
	}
	
	public void setAcctWfstatusName(String acctWfstatusName) {
		this.acctWfstatusName = acctWfstatusName;
	}
	/**
	 * 单位性质
	 */
	private String deptNatureName;

	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='AGENCYTYPE' and t.status=0  and t.code=DEPT_NATURE)")
	public String getDeptNatureName() {
		return deptNatureName;
	}

	public void setDeptNatureName(String deptNatureName) {
		this.deptNatureName = deptNatureName;
	}
	private String iszeroName;
	private String oldIszeroName;
	@Formula("(case when iszero =0 then '否' when  iszero =1 then '是' end )")
	public String getIszeroName() {
		return iszeroName;
	}
	public void setIszeroName(String iszeroName) {
		this.iszeroName = iszeroName;
	}
	@Formula("(case when old_iszero =0 then '否' when  old_iszero =1 then '是' end )")
	public String getOldIszeroName() {
		return oldIszeroName;
	}

	public void setOldIszeroName(String oldIszeroName) {
		this.oldIszeroName = oldIszeroName;
	}
	/** default constructor */
	public FavApplAccount() {
	}

	/** minimal constructor */
	public FavApplAccount(Integer applicationId, String bdgagencycode,
			Integer type, String deptNature, String applyReason,
			String wfstatus, String applPhonenumber, String createUser,
			String createTime, String isregister, Integer accountId,
			String accountName, String accountType, String accountNumber,
			String accountNum, String bankCode, Integer iszero) {
		this.applicationId = applicationId;
		this.bdgagencycode = bdgagencycode;
		this.type = type;
		this.deptNature = deptNature;
		this.applyReason = applyReason;
		this.wfstatus = wfstatus;
		this.applPhonenumber = applPhonenumber;
		this.createUser = createUser;
		this.createTime = createTime;
		this.isregister = isregister;
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.accountNum = accountNum;
		this.bankCode = bankCode;
		this.iszero = iszero;
	}

	/** full constructor */
	public FavApplAccount(Integer applicationId, String bdgagencycode,
			Integer type, String deptNature, String supervisorDept,
			String deptAddress, String applyReason, String wfid,
			String wfstatus, Integer wfisback, String bdgagencyname,
			String applPhonenumber, String linkman, String createUser,
			String createTime, String updateUser, String updateTime,
			String isregister, Integer accountId, String accountName,
			String accountType, String accountNumber, String accountNum,
			String bankCode, String openTime, String accountContent,
			String oldAccountName, String oldBankCode, String oldAccountNumber,
			String responsiblePerson, String acctPhonenumber, Integer iszero,
			String remark) {
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
		this.applPhonenumber = applPhonenumber;
		this.linkman = linkman;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.isregister = isregister;
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.accountNum = accountNum;
		this.bankCode = bankCode;
		this.openTime = openTime;
		this.accountContent = accountContent;
		this.oldAccountName = oldAccountName;
		this.oldBankCode = oldBankCode;
		this.oldAccountNumber = oldAccountNumber;
		this.responsiblePerson = responsiblePerson;
		this.acctPhonenumber = acctPhonenumber;
		this.iszero = iszero;
		this.remark = remark;
	}

	// Property accessors
	@Id
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

	@Column(name = "APPL_PHONENUMBER", nullable = false, length = 50)
	public String getApplPhonenumber() {
		return this.applPhonenumber;
	}

	public void setApplPhonenumber(String applPhonenumber) {
		this.applPhonenumber = applPhonenumber;
	}

	@Column(name = "LINKMAN", length = 50)
	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
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

	@Column(name = "ISREGISTER", nullable = false, length = 1)
	public String getIsregister() {
		return this.isregister;
	}

	public void setIsregister(String isregister) {
		this.isregister = isregister;
	}

	@Column(name = "ACCOUNT_ID", nullable = false, precision = 9, scale = 0)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "ACCOUNT_NAME", length = 500)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNT_TYPE", length = 50)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "ACCOUNT_NUMBER",length = 50)
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "ACCOUNT_NUM",length = 50)
	public String getAccountNum() {
		return this.accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	@Column(name = "BANK_CODE", length = 50)
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "OPEN_TIME", length = 20)
	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	@Column(name = "ACCOUNT_CONTENT", length = 500)
	public String getAccountContent() {
		return this.accountContent;
	}

	public void setAccountContent(String accountContent) {
		this.accountContent = accountContent;
	}

	@Column(name = "OLD_ACCOUNT_NAME", length = 500)
	public String getOldAccountName() {
		return this.oldAccountName;
	}

	public void setOldAccountName(String oldAccountName) {
		this.oldAccountName = oldAccountName;
	}

	@Column(name = "OLD_BANK_CODE", length = 50)
	public String getOldBankCode() {
		return this.oldBankCode;
	}

	public void setOldBankCode(String oldBankCode) {
		this.oldBankCode = oldBankCode;
	}

	@Column(name = "OLD_ACCOUNT_NUMBER", length = 50)
	public String getOldAccountNumber() {
		return this.oldAccountNumber;
	}

	public void setOldAccountNumber(String oldAccountNumber) {
		this.oldAccountNumber = oldAccountNumber;
	}

	@Column(name = "RESPONSIBLE_PERSON", length = 50)
	public String getResponsiblePerson() {
		return this.responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	@Column(name = "ACCT_PHONENUMBER", length = 50)
	public String getAcctPhonenumber() {
		return this.acctPhonenumber;
	}

	public void setAcctPhonenumber(String acctPhonenumber) {
		this.acctPhonenumber = acctPhonenumber;
	}

	@Column(name = "ISZERO", precision = 1, scale = 0)
	public Integer getIszero() {
		return this.iszero;
	}

	public void setIszero(Integer iszero) {
		this.iszero = iszero;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "TYPE02", length = 50)
	public String getType02() {
		return type02;
	}

	public void setType02(String type02) {
		this.type02 = type02;
	}
	@Column(name = "OLD_TYPE02", length = 50)
	public String getOldType02() {
		return oldType02;
	}

	public void setOldType02(String oldType02) {
		this.oldType02 = oldType02;
	}
	
	
	public void setAcctWfid(String acctWfid) {
		this.acctWfid = acctWfid;
	}
	
	@Column(name = "ACCT_WFID", length = 50)
	public String getAcctWfid() {
		return acctWfid;
	}

	public void setAcctWfstatus(String acctWfstatus) {
		this.acctWfstatus = acctWfstatus;
	}
	
	@Column(name = "ACCT_WFSTATUS", nullable = false, length = 50)
	public String getAcctWfstatus() {
		return acctWfstatus;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='REGISTERTYPE' and t.status=0  and t.code=TYPE)")
	public String getTypeName() {
		return typeName;
	}

}
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
 * FaAccounts entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FA_ACCOUNTS")
@SequenceGenerator(name="SEQ_FA_ACCOUNTS",sequenceName="SEQ_FA_ACCOUNTS")
@GenericGenerator(name = "SEQ_FA_ACCOUNTS", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_FA_ACCOUNTS") })
public class FaAccounts implements java.io.Serializable {

	// Fields
	/**
	 * 账户序号
	 */
	private Integer accountId;
	/**
	 * 申请序号
	 */
	private Integer applicationId;
	/**
	 * 银行账户名称
	 */
	private String accountName;
	/**
	 * 账户类型
	 */
	private String accountType;
	/**
	 * 银行账号
	 */
	private String accountNumber;
	/**
	 * 户号
	 */
	private String accountNum;
	/**
	 * 开户行代码
	 */
	private String bankCode;
	/**
	 * 开户日期
	 */
	private String openTime;
	/**
	 * 核算内容
	 */
	private String accountContent;
	/**
	 * 原有银行账户名称
	 */
	private String oldAccountName;
	/**
	 * 原有开户行代码
	 */
	private String oldBankCode;
	/**
	 * 原有银行账号
	 */
	private String oldAccountNumber;
	/**
	 * 原有账户类型
	 */
	private String oldAccountType;
	/**
	 * 经办人
	 */
	private String responsiblePerson;
	/**
	 * 联系电话
	 */
	private String phonenumber;
	/**
	 * 修改人
	 */
	private String modifyUser;
	/**
	 * 修改时间
	 */
	private String modifyTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 备案wfid
	 */
	private String wfid;
	/**
	 * 是否零余额账户
	 */
	private Integer iszero;
	/**
	 * 工作流状态
	 */
	private String wfstatus;
	/**
	 * 工作流是否退回
	 */
	private Integer wfisback;
	/**
	 * 账户性质
	 */
	private String type02;
	
	/**
	 * 原有账户性质
	 */
	private String oldType02;
	
	
	/**
	 *原账户是否零余额账户
	 */
	private Integer oldIszero;
	
	private String legalPerson;//企业法人
	private String idcardno;//身份证号
	private String financialOfficer;//财务负责人
	
	private String oldLegalPerson;//企业法人
	private String oldBankName;
	@Formula("(select a.NAME from fav_bank a where a.CODE=old_bank_code)")
	public String getOldBankName() {
		return oldBankName;
	}

	public void setOldBankName(String oldBankName) {
		this.oldBankName = oldBankName;
	}
	@Formula("(select a.NAME from fav_bank a where a.CODE=bank_code)")
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	private String bankName;
	private String oldType02Name;
	
	@Column(name = "OLD_LEGAL_PERSON", length = 50)
	public String getOldLegalPerson() {
		return oldLegalPerson;
	}

	public void setOldLegalPerson(String oldLegalPerson) {
		this.oldLegalPerson = oldLegalPerson;
	}

	@Column(name = "OLD_FINANCIAL_OFFICER", length = 50)
	public String getOldFinancialOfficer() {
		return oldFinancialOfficer;
	}

	public void setOldFinancialOfficer(String oldFinancialOfficer) {
		this.oldFinancialOfficer = oldFinancialOfficer;
	}

	private String oldIdcardno;//身份证号
	@Column(name = "OLD_IDCARDNO", length = 50)
	public String getOldIdcardno() {
		return oldIdcardno;
	}

	public void setOldIdcardno(String oldIdcardno) {
		this.oldIdcardno = oldIdcardno;
	}

	private String oldFinancialOfficer;//财务负责人
	private String oldAccountContent;//核算内容
	
	// Constructors
	@Column(name = "OLD_ACCOUNT_CONTENT", length = 500)
	public String getOldAccountContent() {
		return oldAccountContent;
	}

	public void setOldAccountContent(String oldAccountContent) {
		this.oldAccountContent = oldAccountContent;
	}

	/** default constructor */
	public FaAccounts() {
	}

	/** minimal constructor */
	public FaAccounts(Integer accountId, Integer applicationId,
			String accountName, String accountType, String accountNumber,
			String accountNum, String bankCode, Integer iszero,Integer oldIszero) {
		this.accountId = accountId;
		this.applicationId = applicationId;
		this.accountName = accountName;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.accountNum = accountNum;
		this.bankCode = bankCode;
		this.iszero = iszero;
		this.oldIszero = oldIszero;
	}

	/** full constructor */
	public FaAccounts(Integer accountId, Integer applicationId,
			String accountName, String accountType, String accountNumber,
			String accountNum, String bankCode, String openTime,
			String accountContent, String oldAccountName, String oldBankCode,
			String oldAccountNumber, String oldAccountType,
			String responsiblePerson, String phonenumber, String modifyUser,
			String modifyTime, String remark, String wfid, Integer iszero,
			String wfstatus, Integer wfisback,String legalPerson,String idcardno,String financialOfficer) {
		this.accountId = accountId;
		this.applicationId = applicationId;
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
		this.oldAccountType = oldAccountType;
		this.responsiblePerson = responsiblePerson;
		this.phonenumber = phonenumber;
		this.modifyUser = modifyUser;
		this.modifyTime = modifyTime;
		this.remark = remark;
		this.wfid = wfid;
		this.iszero = iszero;
		this.wfstatus = wfstatus;
		this.wfisback = wfisback;
		this.legalPerson = legalPerson;
		this.idcardno = idcardno;
		this.financialOfficer = financialOfficer;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FA_ACCOUNTS")
	@Column(name = "ACCOUNT_ID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	@Column(name = "APPLICATION_ID", nullable = false, precision = 9, scale = 0)
	public Integer getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "ACCOUNT_NAME", nullable = true, length = 500)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNT_TYPE", nullable = true, length = 50)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "ACCOUNT_NUMBER", nullable = true, length = 50)
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "ACCOUNT_NUM", nullable = true, length = 50)
	public String getAccountNum() {
		return this.accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	@Column(name = "BANK_CODE", nullable = true, length = 50)
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

	@Column(name = "OLD_ACCOUNT_TYPE", length = 50)
	public String getOldAccountType() {
		return this.oldAccountType;
	}

	public void setOldAccountType(String oldAccountType) {
		this.oldAccountType = oldAccountType;
	}

	@Column(name = "RESPONSIBLE_PERSON", length = 50)
	public String getResponsiblePerson() {
		return this.responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	@Column(name = "PHONENUMBER", length = 50)
	public String getPhonenumber() {
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	@Column(name = "MODIFY_USER", length = 50)
	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "WFID", length = 50)
	public String getWfid() {
		return this.wfid;
	}

	public void setWfid(String wfid) {
		this.wfid = wfid;
	}

	@Column(name = "ISZERO")
	public Integer getIszero() {
		return this.iszero;
	}

	public void setIszero(Integer iszero) {
		this.iszero = iszero;
	}

	@Column(name = "WFSTATUS", length = 50)
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
	@Column(name = "OLD_ISZERO")
	public Integer getOldIszero() {
		return oldIszero;
	}

	public void setOldIszero(Integer oldIszero) {
		this.oldIszero = oldIszero;
	}
	@Column(name = "LEGAL_PERSON", length = 50)
	public String getLegalPerson() {
		return this.legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	@Column(name = "IDCARDNO", length = 50)
	public String getIdcardno() {
		return this.idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	@Column(name = "FINANCIAL_OFFICER", length = 50)
	public String getFinancialOfficer() {
		return this.financialOfficer;
	}

	public void setFinancialOfficer(String financialOfficer) {
		this.financialOfficer = financialOfficer;
	}
}
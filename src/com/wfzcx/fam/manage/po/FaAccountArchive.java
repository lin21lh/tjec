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
 * FaAccountArchive entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FA_ACCOUNT_ARCHIVE")
@SequenceGenerator(name="SEQ_FA_ACCOUNT_ARCHIVE",sequenceName="SEQ_FA_ACCOUNT_ARCHIVE")
@GenericGenerator(name = "SEQ_FA_ACCOUNT_ARCHIVE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_FA_ACCOUNT_ARCHIVE") })
public class FaAccountArchive implements java.io.Serializable {

	// Fields

	private Integer itemid;
	private String elementcode;
	private String startdate;
	private String enddate;
	private String accountName;
	private String accountNumber;
	private Integer bankid;
	private String bankCode;
	private String bankName;
	private String accountType;
	private Integer type;
	private String type01;
	private String type02;
	private Integer status;
	private Integer bdgagency;
	private String bdgagencycode;
	private String bdgagencyname;
	private String remark;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
	private Integer ischange;
	private Integer applicationId;
	private String deptAddress;
	private String phonenumber;
	private String linkman;
	private String deptNature;
	private Integer iszero;
	private String iszeroName;
	private String statusName;
	private String createUserName;
	private String updateUserName;
	private String legalPerson;//企业法人
	private String idcardno;//身份证号
	private String financialOfficer;//财务负责人
	private String accountContent;
	@Column(name = "ACCOUNT_CONTENT", length = 500)
	public String getAccountContent() {
		return accountContent;
	}

	public void setAccountContent(String accountContent) {
		this.accountContent = accountContent;
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

	@Formula("( case status when 1 then '正常'when 9 then '撤销' end  )")
	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	@Formula("( case iszero when 1 then '是'when 0 then '否' end  )")
	public String getIszeroName() {
		return iszeroName;
	}

	public void setIszeroName(String iszeroName) {
		this.iszeroName = iszeroName;
	}
	/**
	 * 预算单位名称 显示为‘bdgagencycode ||'-'|| bdgagencyname’
	 */
	private String bdgagencycn;
	/**
	 * 账户性质名称
	 */
	private String type02Name;
	/**
	 * 账户类型名称
	 */
	private String accountTypeName;

	/**
	 * 银行名称
	 */
	private String bankNameCn;
	@Formula("bank_code ||'-'|| bank_name")
	public String getBankNameCn() {
		return bankNameCn;
	}

	public void setBankNameCn(String bankNameCn) {
		this.bankNameCn = bankNameCn;
	}

	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTNATURE' and t.status=0  and t.code=type02)")
	public String getType02Name() {
		return type02Name;
	}

	public void setType02Name(String type02Name) {
		this.type02Name = type02Name;
	}
	@Formula("(select t.name from SYS_FA_DICENUMITEM t where upper(t.elementcode)='ACCTTYPE' and t.status=0  and t.code=ACCOUNT_TYPE)")
	public String getAccountTypeName() {
		return accountTypeName;
	}

	public void setAccountTypeName(String accountTypeName) {
		this.accountTypeName = accountTypeName;
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

	/** default constructor */
	public FaAccountArchive() {
	}

	/** minimal constructor */
	public FaAccountArchive(Integer itemid, String accountNumber,
			String bankCode, String accountType, String bdgagencycode,
			Integer ischange) {
		this.itemid = itemid;
		this.accountNumber = accountNumber;
		this.bankCode = bankCode;
		this.accountType = accountType;
		this.bdgagencycode = bdgagencycode;
		this.ischange = ischange;
	}

	/** full constructor */
	public FaAccountArchive(Integer itemid, String elementcode,
			String startdate, String enddate, String accountName,
			String accountNumber, Integer bankid, String bankCode,
			String bankName, String accountType, Integer type, String type01,
			String type02, Integer status, Integer bdgagency,
			String bdgagencycode, String bdgagencyname, String remark,
			String createTime, String createUser, String updateTime,
			String updateUser, Integer ischange, Integer applicationId,
			String deptAddress, String phonenumber, String linkman,
			String deptNature, Integer iszero) {
		this.itemid = itemid;
		this.elementcode = elementcode;
		this.startdate = startdate;
		this.enddate = enddate;
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.bankid = bankid;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.accountType = accountType;
		this.type = type;
		this.type01 = type01;
		this.type02 = type02;
		this.status = status;
		this.bdgagency = bdgagency;
		this.bdgagencycode = bdgagencycode;
		this.bdgagencyname = bdgagencyname;
		this.remark = remark;
		this.createTime = createTime;
		this.createUser = createUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
		this.ischange = ischange;
		this.applicationId = applicationId;
		this.deptAddress = deptAddress;
		this.phonenumber = phonenumber;
		this.linkman = linkman;
		this.deptNature = deptNature;
		this.iszero = iszero;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FA_ACCOUNT_ARCHIVE")
	@Column(name = "ITEMID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getItemid() {
		return this.itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	@Column(name = "ELEMENTCODE", length = 30)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}

	@Column(name = "STARTDATE", length = 20)
	public String getStartdate() {
		return this.startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	@Column(name = "ENDDATE", length = 20)
	public String getEnddate() {
		return this.enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	@Column(name = "ACCOUNT_NAME", length = 100)
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "ACCOUNT_NUMBER", nullable = false, length = 50)
	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Column(name = "BANKID", precision = 9, scale = 0)
	public Integer getBankid() {
		return this.bankid;
	}

	public void setBankid(Integer bankid) {
		this.bankid = bankid;
	}

	@Column(name = "BANK_CODE", nullable = false, length = 50)
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "BANK_NAME", length = 50)
	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Column(name = "ACCOUNT_TYPE", nullable = false, length = 50)
	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	@Column(name = "TYPE", precision = 1, scale = 0)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "TYPE01", length = 50)
	public String getType01() {
		return this.type01;
	}

	public void setType01(String type01) {
		this.type01 = type01;
	}

	@Column(name = "TYPE02", length = 50)
	public String getType02() {
		return this.type02;
	}

	public void setType02(String type02) {
		this.type02 = type02;
	}

	@Column(name = "STATUS", precision = 9, scale = 0)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "BDGAGENCY", precision = 9, scale = 0)
	public Integer getBdgagency() {
		return this.bdgagency;
	}

	public void setBdgagency(Integer bdgagency) {
		this.bdgagency = bdgagency;
	}

	@Column(name = "BDGAGENCYCODE", nullable = false, length = 50)
	public String getBdgagencycode() {
		return this.bdgagencycode;
	}

	public void setBdgagencycode(String bdgagencycode) {
		this.bdgagencycode = bdgagencycode;
	}

	@Column(name = "BDGAGENCYNAME", length = 100)
	public String getBdgagencyname() {
		return this.bdgagencyname;
	}

	public void setBdgagencyname(String bdgagencyname) {
		this.bdgagencyname = bdgagencyname;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER", length = 20)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "UPDATE_TIME", length = 20)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "UPDATE_USER", length = 20)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "ISCHANGE", nullable = false, precision = 1, scale = 0)
	public Integer getIschange() {
		return this.ischange;
	}

	public void setIschange(Integer ischange) {
		this.ischange = ischange;
	}

	@Column(name = "APPLICATION_ID", precision = 9, scale = 0)
	public Integer getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "DEPT_ADDRESS", length = 500)
	public String getDeptAddress() {
		return this.deptAddress;
	}

	public void setDeptAddress(String deptAddress) {
		this.deptAddress = deptAddress;
	}

	@Column(name = "PHONENUMBER", length = 50)
	public String getPhonenumber() {
		return this.phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	@Column(name = "LINKMAN", length = 50)
	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	@Column(name = "DEPT_NATURE", length = 2)
	public String getDeptNature() {
		return this.deptNature;
	}

	public void setDeptNature(String deptNature) {
		this.deptNature = deptNature;
	}

	@Column(name = "ISZERO", precision = 1, scale = 0)
	public Integer getIszero() {
		return this.iszero;
	}

	public void setIszero(Integer iszero) {
		this.iszero = iszero;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	@Formula("(select a.username from sys_user a where  a.userid=CREATE_USER)")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}
	@Formula("(select a.username from sys_user a where  a.userid=UPDATE_USER)")
	public String getUpdateUserName() {
		return updateUserName;
	}

}
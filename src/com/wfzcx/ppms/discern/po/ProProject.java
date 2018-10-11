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
 * ProProject entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PROJECT")
@SequenceGenerator(name="SEQ_PRO_PROJECT",sequenceName="SEQ_PRO_PROJECT")
@GenericGenerator(name = "SEQ_PRO_PROJECT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PROJECT") })
public class ProProject implements java.io.Serializable {

	// Fields

	private Integer projectid;
	private String datatype;//数据类型
	private String proName;//项目名称
	private String proType;//项目类型
	private Double amount;//项目总投资
	private String proYear;//合作年限
	private String proTrade;//所属行业
	private String proPerate;//运作方式
	private String proReturn;//回报机制
	private String proSendtime;//项目发起时间
	private String proSendtype;//项目发起类型
	private String proSendperson;//项目发起人名称
	private String proSituation;//项目概况
	private String proPerson;//项目联系人
	private String proPhone;//联系人电话
	private String proScheme;//初步实施方案内容
	private String proSchemepath;
	private String proReportpath;
	private String proConditionpath;
	private String proArticle;//项目产出物说明
	private String proArticlepath;
	private String wfid;
	private String status;
	private String createuser;
	private String createtime;
	private String updateuser;
	private String updatetime;
	private String orgcode;
	private String implementOrgan;
	private String implementPerson;
	private String implementPhone;
	private String governmentPath;
	private String czResult;
	//
	private String tjxm;
	private String sfxm;
	private String sqbt;
	private String btje;
	//
	private String vfmPjhj;
	private String opinion;
	//
	private String xmdqhj;
	
	@Column(name = "XMDQHJ")
	public String getXmdqhj() {
		return xmdqhj;
	}

	public void setXmdqhj(String xmdqhj) {
		this.xmdqhj = xmdqhj;
	}

	@Column(name = "OPINION")
	public String getOpinion() {
		return opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	@Column(name = "VFM_PJHJ")
	public String getVfmPjhj() {
		return vfmPjhj;
	}

	public void setVfmPjhj(String vfmPjhj) {
		this.vfmPjhj = vfmPjhj;
	}

	// Constructors
	@Column(name = "ORGCODE")
	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	/** default constructor */
	public ProProject() {
	}

	/** minimal constructor */
	public ProProject(Integer projectid, String datatype, String proName,
			String proType, Double amount, String proYear, String proTrade,
			String proPerate, String proReturn, String proSendtime,
			String proSendtype, String proSendperson, String proSituation,
			String proPerson, String proPhone, String proScheme,
			String proArticle, String wfid) {
		this.projectid = projectid;
		this.datatype = datatype;
		this.proName = proName;
		this.proType = proType;
		this.amount = amount;
		this.proYear = proYear;
		this.proTrade = proTrade;
		this.proPerate = proPerate;
		this.proReturn = proReturn;
		this.proSendtime = proSendtime;
		this.proSendtype = proSendtype;
		this.proSendperson = proSendperson;
		this.proSituation = proSituation;
		this.proPerson = proPerson;
		this.proPhone = proPhone;
		this.proScheme = proScheme;
		this.proArticle = proArticle;
		this.wfid = wfid;
	}

	

	/** 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param projectid
	 * @param datatype
	 * @param proName
	 * @param proType
	 * @param amount
	 * @param proYear
	 * @param proTrade
	 * @param proPerate
	 * @param proReturn
	 * @param proSendtime
	 * @param proSendtype
	 * @param proSendperson
	 * @param proSituation
	 * @param proPerson
	 * @param proPhone
	 * @param proScheme
	 * @param proSchemepath
	 * @param proReportpath
	 * @param proConditionpath
	 * @param proArticle
	 * @param proArticlepath
	 * @param wfid
	 * @param status
	 * @param createuser
	 * @param createtime
	 * @param updateuser
	 * @param updatetime
	 * @param orgcode
	 * @param implementOrgan
	 * @param implementPerson
	 * @param implementPhone
	 * @param governmentPath 
	 */ 
	public ProProject(Integer projectid, String datatype, String proName,
			String proType, Double amount, String proYear, String proTrade,
			String proPerate, String proReturn, String proSendtime,
			String proSendtype, String proSendperson, String proSituation,
			String proPerson, String proPhone, String proScheme,
			String proSchemepath, String proReportpath,
			String proConditionpath, String proArticle, String proArticlepath,
			String wfid, String status, String createuser, String createtime,
			String updateuser, String updatetime, String orgcode,
			String implementOrgan, String implementPerson,
			String implementPhone, String governmentPath) {
		super();
		this.projectid = projectid;
		this.datatype = datatype;
		this.proName = proName;
		this.proType = proType;
		this.amount = amount;
		this.proYear = proYear;
		this.proTrade = proTrade;
		this.proPerate = proPerate;
		this.proReturn = proReturn;
		this.proSendtime = proSendtime;
		this.proSendtype = proSendtype;
		this.proSendperson = proSendperson;
		this.proSituation = proSituation;
		this.proPerson = proPerson;
		this.proPhone = proPhone;
		this.proScheme = proScheme;
		this.proSchemepath = proSchemepath;
		this.proReportpath = proReportpath;
		this.proConditionpath = proConditionpath;
		this.proArticle = proArticle;
		this.proArticlepath = proArticlepath;
		this.wfid = wfid;
		this.status = status;
		this.createuser = createuser;
		this.createtime = createtime;
		this.updateuser = updateuser;
		this.updatetime = updatetime;
		this.orgcode = orgcode;
		this.implementOrgan = implementOrgan;
		this.implementPerson = implementPerson;
		this.implementPhone = implementPhone;
		this.governmentPath = governmentPath;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PROJECT")
	@Column(name = "PROJECTID", unique = true, nullable = false, length = 20)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "DATATYPE", nullable = false, length = 2)
	public String getDatatype() {
		return this.datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	@Column(name = "PRO_NAME", nullable = false, length = 100)
	public String getProName() {
		return this.proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	@Column(name = "PRO_TYPE", nullable = false, length = 1)
	public String getProType() {
		return this.proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	@Column(name = "AMOUNT", nullable = false, precision = 16)
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Column(name = "PRO_YEAR", nullable = false, precision = 2, scale = 0)
	public String getProYear() {
		return this.proYear;
	}

	public void setProYear(String proYear) {
		this.proYear = proYear;
	}

	@Column(name = "PRO_TRADE", nullable = false, length = 6)
	public String getProTrade() {
		return this.proTrade;
	}

	public void setProTrade(String proTrade) {
		this.proTrade = proTrade;
	}

	@Column(name = "PRO_PERATE", nullable = false, length = 10)
	public String getProPerate() {
		return this.proPerate;
	}

	public void setProPerate(String proPerate) {
		this.proPerate = proPerate;
	}

	@Column(name = "PRO_RETURN", nullable = false, length = 1)
	public String getProReturn() {
		return this.proReturn;
	}

	public void setProReturn(String proReturn) {
		this.proReturn = proReturn;
	}

	@Column(name = "PRO_SENDTIME", nullable = false, length = 20)
	public String getProSendtime() {
		return this.proSendtime;
	}

	public void setProSendtime(String proSendtime) {
		this.proSendtime = proSendtime;
	}

	@Column(name = "PRO_SENDTYPE", nullable = false, length = 1)
	public String getProSendtype() {
		return this.proSendtype;
	}

	public void setProSendtype(String proSendtype) {
		this.proSendtype = proSendtype;
	}

	@Column(name = "PRO_SENDPERSON", nullable = false, length = 100)
	public String getProSendperson() {
		return this.proSendperson;
	}

	public void setProSendperson(String proSendperson) {
		this.proSendperson = proSendperson;
	}

	@Column(name = "PRO_SITUATION", nullable = false, length = 1000)
	public String getProSituation() {
		return this.proSituation;
	}

	public void setProSituation(String proSituation) {
		this.proSituation = proSituation;
	}

	@Column(name = "PRO_PERSON", nullable = false, length = 50)
	public String getProPerson() {
		return this.proPerson;
	}

	public void setProPerson(String proPerson) {
		this.proPerson = proPerson;
	}

	@Column(name = "PRO_PHONE", nullable = false, length = 100)
	public String getProPhone() {
		return this.proPhone;
	}

	public void setProPhone(String proPhone) {
		this.proPhone = proPhone;
	}

	@Column(name = "PRO_SCHEME", nullable = false, length = 1000)
	public String getProScheme() {
		return this.proScheme;
	}

	public void setProScheme(String proScheme) {
		this.proScheme = proScheme;
	}

	@Column(name = "PRO_SCHEMEPATH", length = 100)
	public String getProSchemepath() {
		return this.proSchemepath;
	}

	public void setProSchemepath(String proSchemepath) {
		this.proSchemepath = proSchemepath;
	}

	@Column(name = "PRO_REPORTPATH", length = 100)
	public String getProReportpath() {
		return this.proReportpath;
	}

	public void setProReportpath(String proReportpath) {
		this.proReportpath = proReportpath;
	}

	@Column(name = "PRO_CONDITIONPATH", length = 100)
	public String getProConditionpath() {
		return this.proConditionpath;
	}

	public void setProConditionpath(String proConditionpath) {
		this.proConditionpath = proConditionpath;
	}

	@Column(name = "PRO_ARTICLE", nullable = false, length = 1000)
	public String getProArticle() {
		return this.proArticle;
	}

	public void setProArticle(String proArticle) {
		this.proArticle = proArticle;
	}

	@Column(name = "PRO_ARTICLEPATH", length = 100)
	public String getProArticlepath() {
		return this.proArticlepath;
	}

	public void setProArticlepath(String proArticlepath) {
		this.proArticlepath = proArticlepath;
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

	@Column(name = "CREATETIME", length = 7)
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

	@Column(name = "UPDATETIME", length = 7)
	public String getUpdatetime() {
		return this.updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	@Column(name = "IMPLEMENT_ORGAN", nullable = true, length = 100)
	public String getImplementOrgan() {
		return implementOrgan;
	}

	public void setImplementOrgan(String implementOrgan) {
		this.implementOrgan = implementOrgan;
	}
	@Column(name = "IMPLEMENT_PERSON", nullable = true, length = 100)
	public String getImplementPerson() {
		return implementPerson;
	}

	public void setImplementPerson(String implementPerson) {
		this.implementPerson = implementPerson;
	}
	@Column(name = "IMPLEMENT_PHONE", nullable = true, length = 100)
	public String getImplementPhone() {
		return implementPhone;
	}

	public void setImplementPhone(String implementPhone) {
		this.implementPhone = implementPhone;
	}

	public void setGovernmentPath(String governmentPath) {
		this.governmentPath = governmentPath;
	}
	@Column(name = "GOVERNMENT_PATH", nullable = true, length = 100)
	public String getGovernmentPath() {
		return governmentPath;
	}

	public void setCzResult(String czResult) {
		this.czResult = czResult;
	}
	@Column(name = "CZ_RESULT", nullable = true, length = 2)
	public String getCzResult() {
		return czResult;
	}
	@Column(name = "TJXM")
	public String getTjxm() {
		return tjxm;
	}

	public void setTjxm(String tjxm) {
		this.tjxm = tjxm;
	}
	@Column(name = "SFXM")
	public String getSfxm() {
		return sfxm;
	}

	public void setSfxm(String sfxm) {
		this.sfxm = sfxm;
	}
	@Column(name = "SQBT")
	public String getSqbt() {
		return sqbt;
	}

	public void setSqbt(String sqbt) {
		this.sqbt = sqbt;
	}
	@Column(name = "BTJE")
	public String getBtje() {
		return btje;
	}

	public void setBtje(String btje) {
		this.btje = btje;
	}


}
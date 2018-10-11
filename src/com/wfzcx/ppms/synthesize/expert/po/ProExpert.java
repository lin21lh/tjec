package com.wfzcx.ppms.synthesize.expert.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProExpert entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_EXPERT")
@GenericGenerator(name = "SEQ_PRO_EXPERT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_EXPERT") })
public class ProExpert implements java.io.Serializable {

	// Fields

	private Integer expertid;
	private String code;
	private String name;
	private Integer sex;
	private String birthday;
	private Integer idtype;
	private String idcard;
	private String region;
	private String politicsStatus;
	private Integer isEmergency;
	private String expertType;
	private String highestDegree;
	private String major;
	private String graduateSchool;
	private String workingCondition;
	private String majorType;
	private Short majorYear;
	private String duty;
	private String professionalTitle;
	private String titleNumber;
	private String unitName;
	private String unitAddress;
	private String avoidUnit;
	private String phoneNumber;
	private String wechat;
	private String qq;
	private String email;
	private String homeTelephone;
	private String homeAddress;
	private String homePostcode;
	private String photo;
	private String bidMajor;
	private String bidArea;
	private String qualification;
	private String createUser;
	private String createTime;
	private String updateUser;
	private String updateTime;
	private Integer isTrain;
	private String nation;
	private String highestOffering;
	private String industry;
	private String research;
	private String hasBidProject;
	private String isUse;

	// Constructors

	/** default constructor */
	public ProExpert() {
	}

	/** minimal constructor */
	public ProExpert(Integer expertid, String name, Integer sex,
			String birthday, Integer idtype, String idcard, String region,
			String politicsStatus, Integer isEmergency, String expertType,
			String highestDegree, String major, String graduateSchool,
			String workingCondition, String majorType, Short majorYear,
			String duty, String phoneNumber, String homeTelephone,
			String homeAddress, String bidMajor) {
		this.expertid = expertid;
		this.name = name;
		this.sex = sex;
		this.birthday = birthday;
		this.idtype = idtype;
		this.idcard = idcard;
		this.region = region;
		this.politicsStatus = politicsStatus;
		this.isEmergency = isEmergency;
		this.expertType = expertType;
		this.highestDegree = highestDegree;
		this.major = major;
		this.graduateSchool = graduateSchool;
		this.workingCondition = workingCondition;
		this.majorType = majorType;
		this.majorYear = majorYear;
		this.duty = duty;
		this.phoneNumber = phoneNumber;
		this.homeTelephone = homeTelephone;
		this.homeAddress = homeAddress;
		this.bidMajor = bidMajor;
	}

	/** full constructor */
	public ProExpert(Integer expertid, String code, String name, Integer sex,
			String birthday, Integer idtype, String idcard, String region,
			String politicsStatus, Integer isEmergency, String expertType,
			String highestDegree, String major, String graduateSchool,
			String workingCondition, String majorType, Short majorYear,
			String duty, String professionalTitle, String titleNumber,
			String unitName, String unitAddress, String avoidUnit,
			String phoneNumber, String wechat, String qq, String email,
			String homeTelephone, String homeAddress, String homePostcode,
			String photo, String bidMajor, String bidArea,
			String qualification, String createUser, String createTime,
			String updateUser, String updateTime, Integer isTrain,
			String nation, String highestOffering, String industry,
			String research, String hasBidProject) {
		this.expertid = expertid;
		this.code = code;
		this.name = name;
		this.sex = sex;
		this.birthday = birthday;
		this.idtype = idtype;
		this.idcard = idcard;
		this.region = region;
		this.politicsStatus = politicsStatus;
		this.isEmergency = isEmergency;
		this.expertType = expertType;
		this.highestDegree = highestDegree;
		this.major = major;
		this.graduateSchool = graduateSchool;
		this.workingCondition = workingCondition;
		this.majorType = majorType;
		this.majorYear = majorYear;
		this.duty = duty;
		this.professionalTitle = professionalTitle;
		this.titleNumber = titleNumber;
		this.unitName = unitName;
		this.unitAddress = unitAddress;
		this.avoidUnit = avoidUnit;
		this.phoneNumber = phoneNumber;
		this.wechat = wechat;
		this.qq = qq;
		this.email = email;
		this.homeTelephone = homeTelephone;
		this.homeAddress = homeAddress;
		this.homePostcode = homePostcode;
		this.photo = photo;
		this.bidMajor = bidMajor;
		this.bidArea = bidArea;
		this.qualification = qualification;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
		this.isTrain = isTrain;
		this.nation = nation;
		this.highestOffering = highestOffering;
		this.industry = industry;
		this.research = research;
		this.hasBidProject = hasBidProject;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_EXPERT")
	@Column(name = "EXPERTID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getExpertid() {
		return this.expertid;
	}

	public void setExpertid(Integer expertid) {
		this.expertid = expertid;
	}

	@Column(name = "CODE", length = 18)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX", nullable = false, precision = 1, scale = 0)
	public Integer getSex() {
		return this.sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "BIRTHDAY", nullable = false, length = 20)
	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	@Column(name = "IDTYPE", nullable = false, precision = 3, scale = 0)
	public Integer getIdtype() {
		return this.idtype;
	}

	public void setIdtype(Integer idtype) {
		this.idtype = idtype;
	}

	@Column(name = "IDCARD", nullable = false, length = 32)
	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Column(name = "REGION", nullable = false, length = 120)
	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "POLITICS_STATUS", nullable = false, length = 60)
	public String getPoliticsStatus() {
		return this.politicsStatus;
	}

	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}

	@Column(name = "IS_EMERGENCY", nullable = false, precision = 1, scale = 0)
	public Integer getIsEmergency() {
		return this.isEmergency;
	}

	public void setIsEmergency(Integer isEmergency) {
		this.isEmergency = isEmergency;
	}

	@Column(name = "EXPERT_TYPE", nullable = false, length = 18)
	public String getExpertType() {
		return this.expertType;
	}

	public void setExpertType(String expertType) {
		this.expertType = expertType;
	}

	@Column(name = "HIGHEST_DEGREE", nullable = false, length = 18)
	public String getHighestDegree() {
		return this.highestDegree;
	}

	public void setHighestDegree(String highestDegree) {
		this.highestDegree = highestDegree;
	}

	@Column(name = "MAJOR", nullable = false, length = 18)
	public String getMajor() {
		return this.major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Column(name = "GRADUATE_SCHOOL", nullable = false, length = 30)
	public String getGraduateSchool() {
		return this.graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	@Column(name = "WORKING_CONDITION", nullable = false, length = 30)
	public String getWorkingCondition() {
		return this.workingCondition;
	}

	public void setWorkingCondition(String workingCondition) {
		this.workingCondition = workingCondition;
	}

	@Column(name = "MAJOR_TYPE", nullable = false, length = 9)
	public String getMajorType() {
		return this.majorType;
	}

	public void setMajorType(String majorType) {
		this.majorType = majorType;
	}

	@Column(name = "MAJOR_YEAR", nullable = false, precision = 3, scale = 0)
	public Short getMajorYear() {
		return this.majorYear;
	}

	public void setMajorYear(Short majorYear) {
		this.majorYear = majorYear;
	}

	@Column(name = "DUTY", nullable = false, length = 20)
	public String getDuty() {
		return this.duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	@Column(name = "PROFESSIONAL_TITLE", length = 20)
	public String getProfessionalTitle() {
		return this.professionalTitle;
	}

	public void setProfessionalTitle(String professionalTitle) {
		this.professionalTitle = professionalTitle;
	}

	@Column(name = "TITLE_NUMBER", length = 20)
	public String getTitleNumber() {
		return this.titleNumber;
	}

	public void setTitleNumber(String titleNumber) {
		this.titleNumber = titleNumber;
	}

	@Column(name = "UNIT_NAME", length = 60)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "UNIT_ADDRESS", length = 80)
	public String getUnitAddress() {
		return this.unitAddress;
	}

	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}

	@Column(name = "AVOID_UNIT", length = 400)
	public String getAvoidUnit() {
		return this.avoidUnit;
	}

	public void setAvoidUnit(String avoidUnit) {
		this.avoidUnit = avoidUnit;
	}

	@Column(name = "PHONE_NUMBER", nullable = false, length = 30)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "WECHAT", length = 30)
	public String getWechat() {
		return this.wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	@Column(name = "QQ", length = 30)
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "EMAIL", length = 80)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "HOME_TELEPHONE", nullable = false, length = 30)
	public String getHomeTelephone() {
		return this.homeTelephone;
	}

	public void setHomeTelephone(String homeTelephone) {
		this.homeTelephone = homeTelephone;
	}

	@Column(name = "HOME_ADDRESS", nullable = false, length = 80)
	public String getHomeAddress() {
		return this.homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	@Column(name = "HOME_POSTCODE", length = 12)
	public String getHomePostcode() {
		return this.homePostcode;
	}

	public void setHomePostcode(String homePostcode) {
		this.homePostcode = homePostcode;
	}

	@Column(name = "PHOTO", length = 100)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "BID_MAJOR", nullable = false, length = 100)
	public String getBidMajor() {
		return this.bidMajor;
	}

	public void setBidMajor(String bidMajor) {
		this.bidMajor = bidMajor;
	}

	@Column(name = "BID_AREA", length = 40)
	public String getBidArea() {
		return this.bidArea;
	}

	public void setBidArea(String bidArea) {
		this.bidArea = bidArea;
	}

	@Column(name = "QUALIFICATION", length = 80)
	public String getQualification() {
		return this.qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	@Column(name = "CREATE_USER", length = 40)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length = 40)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "UPDATE_USER", length = 40)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Column(name = "UPDATE_TIME", length = 40)
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "IS_TRAIN", precision = 1, scale = 0)
	public Integer getIsTrain() {
		return this.isTrain;
	}

	public void setIsTrain(Integer isTrain) {
		this.isTrain = isTrain;
	}

	@Column(name = "NATION", length = 20)
	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	@Column(name = "HIGHEST_OFFERING", length = 18)
	public String getHighestOffering() {
		return this.highestOffering;
	}

	public void setHighestOffering(String highestOffering) {
		this.highestOffering = highestOffering;
	}

	@Column(name = "INDUSTRY", length = 20)
	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Column(name = "RESEARCH", length = 1000)
	public String getResearch() {
		return this.research;
	}

	public void setResearch(String research) {
		this.research = research;
	}

	@Column(name = "HAS_BID_PROJECT", length = 1000)
	public String getHasBidProject() {
		return this.hasBidProject;
	}

	public void setHasBidProject(String hasBidProject) {
		this.hasBidProject = hasBidProject;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	@Column(name = "IS_USE", length = 2)
	public String getIsUse() {
		return isUse;
	}

}
package com.wfzcx.aros.print.po;

public class NoticeContentInfo
{
	private String noticeid;
	private String code; // 文书编码
	private String appName; // 申请人
	private String appSex; // 申请人性别
	private String appBirthday; // 申请人生日
	private String appAddress; // 申请人通讯地址
/*	private String appCorName; // 法人或其他组织名称(申请人)
	private String appCorAddress; // 法人或其他组织(申请人)通讯地址
*/	private String appCorporation; // 法人代表(申请人)
	private String appCorJob; // 法人职务(申请人)
	private String defName; // 被申请人
	private String defAddress; // 被申请人通讯地址
	private String defCorJob; // 法人职务(被申请人)
	private String defCorporation; // 法人代表(被申请人)
	private String defCorAddress;
	private String agentName; // 委托代理人
	private String agentAddress; // 委托代理人通讯地址
	private String defReply; // 被申请人答复
	private String appYear; // 申请日期-年
	private String appMonth; // 申请日期-月
	private String appDay; // 申请日期-日
	private String agencyName; // 复议机关
	private String otherName; // 抄送人
	private String thName; // 第三人
	private String thAddress; // 第三人通讯地址
	private String appCase; // 申请事项
	private String reason; // 中止或终止事由
	private String overruleCause; // 不予受理原因
	private String num; // 法律条例
	private String item; // 法律法文项
	private String OtherCode; // 其他通知书编码
	private String agreementContent; // 和解协议
	private String agreementNum; // 和解协议数
	private String year;
	private String month;
	private String day;
	private String systemYear;
	private String systemMonth;
	private String systemDay;
	
	public String getNoticeid() {
		return noticeid;
	}
	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppSex() {
		return appSex;
	}
	public void setAppSex(String appSex) {
		this.appSex = appSex;
	}
	public String getAppBirthday() {
		return appBirthday;
	}
	public void setAppBirthday(String appBirthday) {
		this.appBirthday = appBirthday;
	}
	public String getAppAddress() {
		return appAddress;
	}
	public void setAppAddress(String appAddress) {
		this.appAddress = appAddress;
	}
	/*public String getAppCorName() {
		return appCorName;
	}
	public void setAppCorName(String appCorName) {
		this.appCorName = appCorName;
	}
	public String getAppCorAddress() {
		return appCorAddress;
	}
	public void setAppCorAddress(String appCorAddress) {
		this.appCorAddress = appCorAddress;
	}*/
	public String getAppCorporation() {
		return appCorporation;
	}
	public void setAppCorporation(String appCorporation) {
		this.appCorporation = appCorporation;
	}
	public String getAppCorJob() {
		return appCorJob;
	}
	public void setAppCorJob(String appCorJob) {
		this.appCorJob = appCorJob;
	}
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}
	public String getDefAddress() {
		return defAddress;
	}
	public void setDefAddress(String defAddress) {
		this.defAddress = defAddress;
	}
	public String getDefCorJob() {
		return defCorJob;
	}
	public void setDefCorJob(String defCorJob) {
		this.defCorJob = defCorJob;
	}
	public String getDefCorporation() {
		return defCorporation;
	}
	public void setDefCorporation(String defCorporation) {
		this.defCorporation = defCorporation;
	}
	public String getDefCorAddress() {
		return defCorAddress;
	}
	public void setDefCorAddress(String defCorAddress) {
		this.defCorAddress = defCorAddress;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentAddress() {
		return agentAddress;
	}
	public void setAgentAddress(String agentAddress) {
		this.agentAddress = agentAddress;
	}
	public String getDefReply() {
		return defReply;
	}
	public void setDefReply(String defReply) {
		this.defReply = defReply;
	}
	public String getAppYear() {
		return appYear;
	}
	public void setAppYear(String appYear) {
		this.appYear = appYear;
	}
	public String getAppMonth() {
		return appMonth;
	}
	public void setAppMonth(String appMonth) {
		this.appMonth = appMonth;
	}
	public String getAppDay() {
		return appDay;
	}
	public void setAppDay(String appDay) {
		this.appDay = appDay;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getOtherName() {
		return otherName;
	}
	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	public String getThName() {
		return thName;
	}
	public void setThName(String thName) {
		this.thName = thName;
	}
	public String getThAddress() {
		return thAddress;
	}
	public void setThAddress(String thAddress) {
		this.thAddress = thAddress;
	}
	public String getAppCase() {
		return appCase;
	}
	public void setAppCase(String appCase) {
		this.appCase = appCase;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getOverruleCause() {
		return overruleCause;
	}
	public void setOverruleCause(String overruleCause) {
		this.overruleCause = overruleCause;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getOtherCode() {
		return OtherCode;
	}
	public void setOtherCode(String otherCode) {
		OtherCode = otherCode;
	}
	public String getAgreementContent() {
		return agreementContent;
	}
	public void setAgreementContent(String agreementContent) {
		this.agreementContent = agreementContent;
	}
	public String getAgreementNum() {
		return agreementNum;
	}
	public void setAgreementNum(String agreementNum) {
		this.agreementNum = agreementNum;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getSystemYear() {
		return systemYear;
	}
	public void setSystemYear(String systemYear) {
		this.systemYear = systemYear;
	}
	public String getSystemMonth() {
		return systemMonth;
	}
	public void setSystemMonth(String systemMonth) {
		this.systemMonth = systemMonth;
	}
	public String getSystemDay() {
		return systemDay;
	}
	public void setSystemDay(String systemDay) {
		this.systemDay = systemDay;
	}
	
}

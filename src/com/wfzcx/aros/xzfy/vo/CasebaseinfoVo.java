package com.wfzcx.aros.xzfy.vo;

import java.math.BigDecimal;

/**
 * @ClassName: Casebaseinfo
 * @Description: 行政复议案件基本信息Vo
 * @author MyEclipse Persistence Tools
 * @date 2016年8月12日 上午9:19:10
 * @version V1.0
 */
public class CasebaseinfoVo  {

	// Fields
	private String caseid; // 案件ID
	private String csaecode; // 案件编号
	private String appname; // 申请人
	private String apptype; // 申请人类型
	private String idtype; // 证件类型
	private String idcode; // 证件号码
	private String sex; // 性别
	private String age; // 年龄
	private String birth; // 生日
	private String legalperson; // 法定代表人
	private String position; // 职务
	private String proxyman; // 委托代理人
	private String proxyphone; // 委托人电话
	private String deflegalman; // 法定代表人
	private String deflegalduty; // 法定代表人职务
	private String appliedman; // 被申请委托代理人
	private String appliedphone; // 被申请委托代理人电话
	private String mobile; // 联系手机
	private String phone; // 联系电话
	private String address; // 通讯地址
	private String mailaddress; // 邮寄地址
	private String email; // 邮箱
	private String postcode; // 邮政编码
	private String deftype; // 被申请人类型
	private String defname; // 被申请人
	private String depttype; // 被申请人机构类型
	private String defidtype; // 被申请人证件类型
	private String defidcode; // 被申请人证件号码
	private String defmobile; // 被申请人联系手机
	private String defphone; // 被申请人联系电话
	private String defmailaddress; // 被申请人邮寄地址
	private String defaddress; // 被申请人通讯地址
	private String defemail; // 被申请人邮箱
	private String defpostcode; // 被申请人邮政编码
	private String noticeddate; // 接受告知日期
	private String actnoticeddate; // 实际接受告知日期
	private String admtype; // 行政管理类型
	private String casetype; // 申请复议事项类型
	private String ifcompensation; // 是否附带行政赔偿
	private BigDecimal amount; // 赔偿金额
	private String appcase; // 申请事项
	private String factreason; // 事实和理由
	private String annex; // 附件
	private String workunits; // 附件
	private String appdate; // 申请日期
	private String operator; // 操作人
	private String optdate; // 操作日期
	private String protype; // 流程类型
	private String opttype; // 处理标志
	private BigDecimal nodeid; // 节点编号
	private String lasttime; // 数据最后更新时间
	private Long userid; // 用户ID
	private String oldprotype; // 原流程类型
	private String receivedate; // 案件收文日期
	private String receiveway; // 收文方式
	private String expresscom; // 递送公司
	private String couriernum; // 递送单号
	private String datasource; // 数据来源
	private String delaydays;	//延期天数
	private String key;		//查询码
	private String region;	//行政区划
	private String state;	//状态
	private String isgreat;	//是否重大案件备案
	private String intro;	//案件名称
	private String type;	//案件由分类
	private String administratif;	//具体行政行为
	
	//额外字段
	private String thname;
	private String typename;	//案件由分类
	private String sexname;
	private String apptypename;
	private String idtypename;
	private String deftypename;
	private String admtypename;
	private String casetypename;
	private String ifcompensationname;
	private String regionname;		//行政区
	private String amountstr;		//赔偿金额	
	private String receivewayname;	//收文方式
	private String reqmansum;

	// Constructors

	/** default constructor */
	public CasebaseinfoVo() {
	}

	/** minimal constructor */
	public CasebaseinfoVo(String caseid, String csaecode) {
		this.caseid = caseid;
		this.csaecode = csaecode;
	}

	/** full constructor */
	public CasebaseinfoVo(String caseid, String csaecode, String appname, String apptype, String idtype, String idcode,
			String phone, String address, String postcode, String deftype, String defname, String depttype,
			String defidtype, String defidcode, String defphone, String defaddress, String defpostcode,
			String noticeddate, String actnoticeddate, String admtype, String casetype, String ifcompensation,
			BigDecimal amount, String appcase, String factreason, String annex, String appdate, String operator,
			String optdate, String protype, String opttype, BigDecimal nodeid, String lasttime, Long userid,
			String oldprotype, String mailaddress, String key, String region,String reqmansum) {
		this.caseid = caseid;
		this.csaecode = csaecode;
		this.appname = appname;
		this.apptype = apptype;
		this.idtype = idtype;
		this.idcode = idcode;
		this.phone = phone;
		this.address = address;
		this.postcode = postcode;
		this.deftype = deftype;
		this.defname = defname;
		this.depttype = depttype;
		this.defidtype = defidtype;
		this.defidcode = defidcode;
		this.defphone = defphone;
		this.defaddress = defaddress;
		this.defpostcode = defpostcode;
		this.noticeddate = noticeddate;
		this.actnoticeddate = actnoticeddate;
		this.admtype = admtype;
		this.casetype = casetype;
		this.ifcompensation = ifcompensation;
		this.amount = amount;
		this.appcase = appcase;
		this.factreason = factreason;
		this.annex = annex;
		this.appdate = appdate;
		this.operator = operator;
		this.optdate = optdate;
		this.protype = protype;
		this.opttype = opttype;
		this.nodeid = nodeid;
		this.lasttime = lasttime;
		this.userid = userid;
		this.oldprotype = oldprotype;
		this.mailaddress = mailaddress;
		this.key = key;
		this.region = region;
		this.reqmansum=reqmansum;
	}

	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	public String getCsaecode() {
		return csaecode;
	}

	public void setCsaecode(String csaecode) {
		this.csaecode = csaecode;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApptype() {
		return apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdcode() {
		return idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getDeftype() {
		return deftype;
	}

	public void setDeftype(String deftype) {
		this.deftype = deftype;
	}

	public String getDefname() {
		return defname;
	}

	public void setDefname(String defname) {
		this.defname = defname;
	}

	public String getDepttype() {
		return depttype;
	}

	public void setDepttype(String depttype) {
		this.depttype = depttype;
	}

	public String getDefidtype() {
		return defidtype;
	}

	public void setDefidtype(String defidtype) {
		this.defidtype = defidtype;
	}

	public String getDefidcode() {
		return defidcode;
	}

	public void setDefidcode(String defidcode) {
		this.defidcode = defidcode;
	}

	public String getDefmobile() {
		return defmobile;
	}

	public void setDefmobile(String defmobile) {
		this.defmobile = defmobile;
	}

	public String getDefphone() {
		return defphone;
	}

	public void setDefphone(String defphone) {
		this.defphone = defphone;
	}

	public String getDefmailaddress() {
		return defmailaddress;
	}

	public void setDefmailaddress(String defmailaddress) {
		this.defmailaddress = defmailaddress;
	}

	public String getDefaddress() {
		return defaddress;
	}

	public void setDefaddress(String defaddress) {
		this.defaddress = defaddress;
	}

	public String getDefemail() {
		return defemail;
	}

	public void setDefemail(String defemail) {
		this.defemail = defemail;
	}

	public String getDefpostcode() {
		return defpostcode;
	}

	public void setDefpostcode(String defpostcode) {
		this.defpostcode = defpostcode;
	}

	public String getNoticeddate() {
		return noticeddate;
	}

	public void setNoticeddate(String noticeddate) {
		this.noticeddate = noticeddate;
	}

	public String getActnoticeddate() {
		return actnoticeddate;
	}

	public void setActnoticeddate(String actnoticeddate) {
		this.actnoticeddate = actnoticeddate;
	}

	public String getAdmtype() {
		return admtype;
	}

	public void setAdmtype(String admtype) {
		this.admtype = admtype;
	}

	public String getCasetype() {
		return casetype;
	}

	public void setCasetype(String casetype) {
		this.casetype = casetype;
	}

	public String getIfcompensation() {
		return ifcompensation;
	}

	public void setIfcompensation(String ifcompensation) {
		this.ifcompensation = ifcompensation;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAppcase() {
		return appcase;
	}

	public void setAppcase(String appcase) {
		this.appcase = appcase;
	}

	public String getFactreason() {
		return factreason;
	}

	public void setFactreason(String factreason) {
		this.factreason = factreason;
	}

	public String getAnnex() {
		return annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	public String getAppdate() {
		return appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOptdate() {
		return optdate;
	}

	public void setOptdate(String optdate) {
		this.optdate = optdate;
	}

	public String getProtype() {
		return protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	public String getOpttype() {
		return opttype;
	}

	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}

	public BigDecimal getNodeid() {
		return nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getOldprotype() {
		return oldprotype;
	}

	public void setOldprotype(String oldprotype) {
		this.oldprotype = oldprotype;
	}

	public String getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(String receivedate) {
		this.receivedate = receivedate;
	}

	public String getReceiveway() {
		return receiveway;
	}

	public void setReceiveway(String receiveway) {
		this.receiveway = receiveway;
	}

	public String getExpresscom() {
		return expresscom;
	}

	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}

	public String getCouriernum() {
		return couriernum;
	}

	public void setCouriernum(String couriernum) {
		this.couriernum = couriernum;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getDelaydays() {
		return delaydays;
	}

	public void setDelaydays(String delaydays) {
		this.delaydays = delaydays;
	}

	public String getThname() {
		return thname;
	}

	public void setThname(String thname) {
		this.thname = thname;
	}

	public String getApptypename() {
		return apptypename;
	}

	public void setApptypename(String apptypename) {
		this.apptypename = apptypename;
	}

	public String getIdtypename() {
		return idtypename;
	}

	public void setIdtypename(String idtypename) {
		this.idtypename = idtypename;
	}

	public String getDeftypename() {
		return deftypename;
	}

	public void setDeftypename(String deftypename) {
		this.deftypename = deftypename;
	}

	public String getAdmtypename() {
		return admtypename;
	}

	public void setAdmtypename(String admtypename) {
		this.admtypename = admtypename;
	}

	public String getCasetypename() {
		return casetypename;
	}

	public void setCasetypename(String casetypename) {
		this.casetypename = casetypename;
	}

	public String getIfcompensationname() {
		return ifcompensationname;
	}

	public void setIfcompensationname(String ifcompensationname) {
		this.ifcompensationname = ifcompensationname;
	}

	public String getRegionname() {
		return regionname;
	}

	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAmountstr() {
		return amountstr;
	}

	public void setAmountstr(String amountstr) {
		this.amountstr = amountstr;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getReceivewayname() {
		return receivewayname;
	}

	public void setReceivewayname(String receivewayname) {
		this.receivewayname = receivewayname;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIsgreat() {
		return isgreat;
	}

	public void setIsgreat(String isgreat) {
		this.isgreat = isgreat;
	}

	public String getReqmansum() {
		return reqmansum;
	}

	public void setReqmansum(String reqmansum) {
		this.reqmansum = reqmansum;
	}

	public String getSex() {
		return sex;
	}

	public  String getSexname() {
		return sexname;
	}

	public  void setSex(String sex) {
		this.sex = sex;
	}

	public  void setSexname(String sexname) {
		this.sexname = sexname;
	}

	public String getAge() {
		return age;
	}

	public String getBirth() {
		return birth;
	}

	public String getLegalperson() {
		return legalperson;
	}

	public String getPosition() {
		return position;
	}

	public String getProxyman() {
		return proxyman;
	}

	public String getProxyphone() {
		return proxyphone;
	}

	public String getDeflegalman() {
		return deflegalman;
	}

	public String getDeflegalduty() {
		return deflegalduty;
	}

	public String getAppliedman() {
		return appliedman;
	}

	public String getAppliedphone() {
		return appliedphone;
	}

	public String getWorkunits() {
		return workunits;
	}

	public String getIntro() {
		return intro;
	}

	public String getType() {
		return type;
	}

	public String getAdministratif() {
		return administratif;
	}

	public String getTypename() {
		return typename;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public void setLegalperson(String legalperson) {
		this.legalperson = legalperson;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setProxyman(String proxyman) {
		this.proxyman = proxyman;
	}

	public void setProxyphone(String proxyphone) {
		this.proxyphone = proxyphone;
	}

	public void setDeflegalman(String deflegalman) {
		this.deflegalman = deflegalman;
	}

	public void setDeflegalduty(String deflegalduty) {
		this.deflegalduty = deflegalduty;
	}

	public void setAppliedman(String appliedman) {
		this.appliedman = appliedman;
	}

	public void setAppliedphone(String appliedphone) {
		this.appliedphone = appliedphone;
	}

	public void setWorkunits(String workunits) {
		this.workunits = workunits;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAdministratif(String administratif) {
		this.administratif = administratif;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}
	
}
package com.wfzcx.aros.xzfy.po;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Casebaseinfo
 * @Description: 行政复议案件基本信息实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年8月12日 上午9:19:10
 * @version V1.0
 */
@Entity
@Table(name = "B_CASEBASEINFO" )
public class Casebaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	private String  caseid; //案件ID                      	VARCHAR2(32)
	private String  csaecode; //案件编号                   		 	VARCHAR2(100)
	private String  intro; //案件名称                    				VARCHAR2(300)
	private String  receivedate; //收到申请日期                		VARCHAR2(10)
	private String  receiveway; //收到申请方式                		VARCHAR2(2)
	private String  type; //案由分类                    				VARCHAR2(2)
	private String  admtype; //行政管理类型               			VARCHAR2(2)
	private String  casetype; //行政行为类型                		VARCHAR2(2)
	private String  administratif; //具体行政行为                	VARCHAR2(300)
	private String  apptype; //申请人类型                 			VARCHAR2(2)
	private String  reqmansum; //申请人数                   		 	VARCHAR2(2)
	private String  appname; //申请人                      			VARCHAR2(300)
	private String  workunits; //工作单位                    			VARCHAR2(200)
	private String  idtype; //证件类型                    			VARCHAR2(2)
	private String  idcode; //证件号码                    			VARCHAR2(20)
	private String  mobile; //联系手机                    			VARCHAR2(20)
	private String  phone; //联系电话                    				VARCHAR2(20)
	private String  address; //通讯地址                    			VARCHAR2(300)
	private String  mailaddress; //邮寄地址                    		VARCHAR2(300)
	private String  email; //邮箱                        				VARCHAR2(200)
	private String  postcode; //邮政编码                    			VARCHAR2(6)
	private String  sex; //性别                        				VARCHAR2(2)
	private String  age; //年龄                        				VARCHAR2(3)
	private String  birth; //生日                        				VARCHAR2(10)
	private String  legalperson; //法定代表人                 	 	VARCHAR2(60)
	private String  position; //职务                        			VARCHAR2(60)
	private String  proxyman; //申请人委托代理人            		VARCHAR2(60)
	private String  proxyphone; //申请人委托代理人电话        	VARCHAR2(15)
	private String  deftype; //被申请人类型                			VARCHAR2(2)
	private String  defname; //被申请人名称                			VARCHAR2(200)
	private String  depttype; //被申请人机构类型            		VARCHAR2(2)
	private String  defidtype; //被申请人证件类型            		VARCHAR2(2)
	private String  defidcode; //被申请人证件号码            		VARCHAR2(20)
	private String  defmobile; //被申请人联系手机            		VARCHAR2(20)
	private String  defphone; //被申请人联系电话            		VARCHAR2(20)
	private String  defmailaddress; //被申请人邮寄地址            VARCHAR2(300)
	private String  defaddress; //被申请人通讯地址            		VARCHAR2(300)
	private String  defemail; //被申请人邮箱                		VARCHAR2(200)
	private String  defpostcode; //被申请人邮政编码            	VARCHAR2(6)
	private String  deflegalman; //被申请人法定代表人          	VARCHAR2(60)
	private String  deflegalduty; //被申请人法定代表人职务      VARCHAR2(60)
	private String  appliedman; //被申请人委托代理人          	VARCHAR2(60)
	private String  appliedphone; //被申请人委托代理人电话      VARCHAR2(15)
	private String  noticeddate; //接受告知日期                		VARCHAR2(10)
	private String  actnoticeddate; //实际接受告知日期            VARCHAR2(10)
	private String  ifcompensation; //是否附带行政赔偿            VARCHAR2(1)
	private BigDecimal  amount; //赔偿金额                    		DECIMAL(16, 2)
	private String  indemnity; //赔偿                        			VARCHAR2(2)
	private String  isprefix; //复议前置                    			VARCHAR2(2)
	private String  appcase; //行政复议请求                			VARCHAR2(2000)
	private String  factreason; //事实和理由                  		VARCHAR2(2000)
	private String  appdate; //申请日期                   			VARCHAR2(10)
	private Long    userid; //操作人ID                    		NUMBER(19)
	private String  operator; //操作人                      			VARCHAR2(20)
	private String  optdate; //操作日期                    			VARCHAR2(10)
	private String  protype; //流程类型                    			VARCHAR2(2)
	private String  opttype; //处理标志                   	 		VARCHAR2(2)
	private String  phases; //行政复议阶段                			VARCHAR2(2)
	private BigDecimal  nodeid; //节点编号                    		INTEGER
	private String  oldprotype; //原流程类型                  		VARCHAR2(2)
	private String  expresscom; //递送公司                    		VARCHAR2(100)
	private String  couriernum; //递送单号                    		VARCHAR2(100)
	private String  delaydays; //延期天数                    			VARCHAR2(3)
	private String  key; //秘钥                        				VARCHAR2(20)
	private String  isplaceonfile; //是否归档                    		VARCHAR2(1)
	private String  registerresult; //来件处理                    	VARCHAR2(2)
	private String  casesort; //案件程序                    			VARCHAR2(2)
	private String  slcasesort; //审理案件程序                    	    VARCHAR2(2)
	private String  isreview; //是否委员审议                		VARCHAR2(2)
	private String  isdiscuss; //是否集体讨论                		VARCHAR2(2)
	private String  state; //状态                        				VARCHAR2(2)
	private String  isgreat; //是否重大案件备案           		VARCHAR2(2)
	private String  annex; //附件                        				VARCHAR2(100)
	private String  datasource; //数据来源                   		VARCHAR2(2)
	private String  region; //行政区                     				VARCHAR2(6)
	private String  lasttime; //数据最后更新时间            		VARCHAR2(19)
	private Long  laundertakerid; //立案承办人ID           INTEGER
	private String  laundertaker; //立案承办人                  		VARCHAR2(60)
	private Long  slundertakerid; //审理承办人ID           INTEGER
	private String  slundertaker; //审理承办人                 		VARCHAR2(60)
	private String  slcoorganiser; //审理协办人                 		VARCHAR2(60)
	private String  remark; //备注                        				VARCHAR2(2000)
	private String  casesortname; //备注                        				VARCHAR2(2000)
	private String  isdiscussname; //备注                        				VARCHAR2(2000)
	private String  isreviewname; //备注                        				VARCHAR2(2000)

	// Constructors

	/** default constructor */
	public Casebaseinfo() {
	}

	/** minimal constructor */
	public Casebaseinfo(String caseid, String csaecode) {
		this.caseid = caseid;
		this.csaecode = csaecode;
	}

	public Casebaseinfo(String caseid, String csaecode, String intro, String receivedate, String receiveway,
			String type, String admtype, String casetype, String administratif, String apptype, String reqmansum,
			String appname, String workunits, String idtype, String idcode, String mobile, String phone, String address,
			String mailaddress, String email, String postcode, String sex, String age, String birth, String legalperson,
			String position, String proxyman, String proxyphone, String deftype, String defname, String depttype,
			String defidtype, String defidcode, String defmobile, String defphone, String defmailaddress,
			String defaddress, String defemail, String defpostcode, String deflegalman, String deflegalduty,
			String appliedman, String appliedphone, String noticeddate, String actnoticeddate, String ifcompensation,
			BigDecimal amount, String indemnity, String isprefix, String appcase, String factreason, String appdate,
			Long userid, String operator, String optdate, String protype, String opttype, String phases,
			BigDecimal nodeid, String oldprotype, String expresscom, String couriernum, String delaydays, String key,
			String isplaceonfile, String registerresult, String casesort, String isreview, String isdiscuss,
			String state, String isgreat, String annex, String datasource, String region, String lasttime,
Long laundertakerid, String laundertaker, Long slundertakerid, String slundertaker,
			String slcoorganiser, String remark, String slcasesort) {
		super();
		this.caseid = caseid;
		this.csaecode = csaecode;
		this.intro = intro;
		this.receivedate = receivedate;
		this.receiveway = receiveway;
		this.type = type;
		this.admtype = admtype;
		this.casetype = casetype;
		this.administratif = administratif;
		this.apptype = apptype;
		this.reqmansum = reqmansum;
		this.appname = appname;
		this.workunits = workunits;
		this.idtype = idtype;
		this.idcode = idcode;
		this.mobile = mobile;
		this.phone = phone;
		this.address = address;
		this.mailaddress = mailaddress;
		this.email = email;
		this.postcode = postcode;
		this.sex = sex;
		this.age = age;
		this.birth = birth;
		this.legalperson = legalperson;
		this.position = position;
		this.proxyman = proxyman;
		this.proxyphone = proxyphone;
		this.deftype = deftype;
		this.defname = defname;
		this.depttype = depttype;
		this.defidtype = defidtype;
		this.defidcode = defidcode;
		this.defmobile = defmobile;
		this.defphone = defphone;
		this.defmailaddress = defmailaddress;
		this.defaddress = defaddress;
		this.defemail = defemail;
		this.defpostcode = defpostcode;
		this.deflegalman = deflegalman;
		this.deflegalduty = deflegalduty;
		this.appliedman = appliedman;
		this.appliedphone = appliedphone;
		this.noticeddate = noticeddate;
		this.actnoticeddate = actnoticeddate;
		this.ifcompensation = ifcompensation;
		this.amount = amount;
		this.indemnity = indemnity;
		this.isprefix = isprefix;
		this.appcase = appcase;
		this.factreason = factreason;
		this.appdate = appdate;
		this.userid = userid;
		this.operator = operator;
		this.optdate = optdate;
		this.protype = protype;
		this.opttype = opttype;
		this.phases = phases;
		this.nodeid = nodeid;
		this.oldprotype = oldprotype;
		this.expresscom = expresscom;
		this.couriernum = couriernum;
		this.delaydays = delaydays;
		this.key = key;
		this.isplaceonfile = isplaceonfile;
		this.registerresult = registerresult;
		this.casesort = casesort;
		this.isreview = isreview;
		this.isdiscuss = isdiscuss;
		this.state = state;
		this.isgreat = isgreat;
		this.annex = annex;
		this.datasource = datasource;
		this.region = region;
		this.lasttime = lasttime;
		this.laundertakerid = laundertakerid;
		this.laundertaker = laundertaker;
		this.slundertakerid = slundertakerid;
		this.slundertaker = slundertaker;
		this.slcoorganiser = slcoorganiser;
		this.remark = remark;
		this.slcasesort = slcasesort;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "CASEID", unique = true, nullable = false, length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "CSAECODE", length = 100)
	public String getCsaecode() {
		return this.csaecode;
	}

	public void setCsaecode(String csaecode) {
		this.csaecode = csaecode;
	}

	@Column(name = "APPNAME", length = 300)
	public String getAppname() {
		return this.appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	@Column(name = "APPTYPE", length = 2)
	public String getApptype() {
		return this.apptype;
	}

	public void setApptype(String apptype) {
		this.apptype = apptype;
	}

	@Column(name = "IDTYPE", length = 2)
	public String getIdtype() {
		return this.idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	@Column(name = "IDCODE", length = 20)
	public String getIdcode() {
		return this.idcode;
	}

	public void setIdcode(String idcode) {
		this.idcode = idcode;
	}

	@Column(name = "PHONE", length = 11)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "ADDRESS", length = 300)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "POSTCODE", length = 6)
	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "DEFTYPE", length = 2)
	public String getDeftype() {
		return this.deftype;
	}

	public void setDeftype(String deftype) {
		this.deftype = deftype;
	}

	@Column(name = "DEFNAME", length = 200)
	public String getDefname() {
		return this.defname;
	}

	public void setDefname(String defname) {
		this.defname = defname;
	}

	@Column(name = "DEPTTYPE", length = 2)
	public String getDepttype() {
		return this.depttype;
	}

	public void setDepttype(String depttype) {
		this.depttype = depttype;
	}

	@Column(name = "DEFIDTYPE", length = 2)
	public String getDefidtype() {
		return this.defidtype;
	}

	public void setDefidtype(String defidtype) {
		this.defidtype = defidtype;
	}

	@Column(name = "DEFIDCODE", length = 20)
	public String getDefidcode() {
		return this.defidcode;
	}

	public void setDefidcode(String defidcode) {
		this.defidcode = defidcode;
	}

	@Column(name = "DEFPHONE", length = 11)
	public String getDefphone() {
		return this.defphone;
	}

	public void setDefphone(String defphone) {
		this.defphone = defphone;
	}

	@Column(name = "DEFADDRESS", length = 300)
	public String getDefaddress() {
		return this.defaddress;
	}

	public void setDefaddress(String defaddress) {
		this.defaddress = defaddress;
	}

	@Column(name = "DEFPOSTCODE", length = 6)
	public String getDefpostcode() {
		return this.defpostcode;
	}

	public void setDefpostcode(String defpostcode) {
		this.defpostcode = defpostcode;
	}

	@Column(name = "NOTICEDDATE", length = 10)
	public String getNoticeddate() {
		return this.noticeddate;
	}

	public void setNoticeddate(String noticeddate) {
		this.noticeddate = noticeddate;
	}

	@Column(name = "ACTNOTICEDDATE", length = 10)
	public String getActnoticeddate() {
		return this.actnoticeddate;
	}

	public void setActnoticeddate(String actnoticeddate) {
		this.actnoticeddate = actnoticeddate;
	}

	@Column(name = "ADMTYPE", length = 2)
	public String getAdmtype() {
		return this.admtype;
	}

	public void setAdmtype(String admtype) {
		this.admtype = admtype;
	}

	@Column(name = "CASETYPE", length = 2)
	public String getCasetype() {
		return this.casetype;
	}

	public void setCasetype(String casetype) {
		this.casetype = casetype;
	}

	@Column(name = "IFCOMPENSATION", length = 1)
	public String getIfcompensation() {
		return this.ifcompensation;
	}

	public void setIfcompensation(String ifcompensation) {
		this.ifcompensation = ifcompensation;
	}

	@Column(name = "AMOUNT", precision = 16)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "APPCASE", length = 2000)
	public String getAppcase() {
		return this.appcase;
	}

	public void setAppcase(String appcase) {
		this.appcase = appcase;
	}

	@Column(name = "FACTREASON", length = 2000)
	public String getFactreason() {
		return this.factreason;
	}

	public void setFactreason(String factreason) {
		this.factreason = factreason;
	}

	@Column(name = "ANNEX", length = 100)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "APPDATE", length = 10)
	public String getAppdate() {
		return this.appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTDATE", length = 10)
	public String getOptdate() {
		return this.optdate;
	}

	public void setOptdate(String optdate) {
		this.optdate = optdate;
	}

	@Column(name = "PROTYPE", length = 2)
	public String getProtype() {
		return this.protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	@Column(name = "OPTTYPE", length = 2)
	public String getOpttype() {
		return this.opttype;
	}

	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}

	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return this.nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	@Column(name = "LASTTIME", length = 19)
	public String getLasttime() {
		return this.lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	@Column(name = "USERID", length = 19)
	public Long getUserid() {
		return this.userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "OLDPROTYPE", length = 2)
	public String getOldprotype() {
		return oldprotype;
	}

	public void setOldprotype(String oldprotype) {
		this.oldprotype = oldprotype;
	}

	@Column(name = "MOBILE", length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL", length = 100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "DEFMOBILE", length = 20)
	public String getDefmobile() {
		return defmobile;
	}

	public void setDefmobile(String defmobile) {
		this.defmobile = defmobile;
	}

	@Column(name = "DEFMAILADDRESS", length = 300)
	public String getDefmailaddress() {
		return defmailaddress;
	}

	public void setDefmailaddress(String defmailaddress) {
		this.defmailaddress = defmailaddress;
	}

	@Column(name = "DEFEMAIL", length = 200)
	public String getDefemail() {
		return defemail;
	}

	public void setDefemail(String defemail) {
		this.defemail = defemail;
	}

	@Column(name = "RECEIVEDATE", length = 10)
	public String getReceivedate() {
		return receivedate;
	}

	public void setReceivedate(String receivedate) {
		this.receivedate = receivedate;
	}

	@Column(name = "RECEIVEWAY", length = 2)
	public String getReceiveway() {
		return receiveway;
	}

	public void setReceiveway(String receiveway) {
		this.receiveway = receiveway;
	}

	@Column(name = "EXPRESSCOM", length = 100)
	public String getExpresscom() {
		return expresscom;
	}

	public void setExpresscom(String expresscom) {
		this.expresscom = expresscom;
	}

	@Column(name = "COURIERNUM", length = 100)
	public String getCouriernum() {
		return couriernum;
	}

	public void setCouriernum(String couriernum) {
		this.couriernum = couriernum;
	}

	@Column(name = "DATASOURCE", length = 2)
	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	@Column(name = "MAILADDRESS", length = 100)
	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}

	@Column(name = "DELAYDAYS", length = 3)
	public String getDelaydays() {
		return delaydays;
	}

	public void setDelaydays(String delaydays) {
		this.delaydays = delaydays;
	}

	@Column(name = "REGION", length = 6)
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Column(name = "INTRO", length = 300)
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	@Column(name = "KEY", length = 20)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	@Column(name = "STATE", length = 2)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "ISGREAT", length = 2)
	public String getIsgreat() {
		return isgreat;
	}
	public void setIsgreat(String isgreat) {
		this.isgreat = isgreat;
	}
	@Column(name = "TYPE", length = 2)
	public String getType() {
		return type;
	}
	@Column(name = "ADMINISTRATIF", length = 300)
	public String getAdministratif() {
		return administratif;
	}
	@Column(name = "REQMANSUM", length = 2)
	public String getReqmansum() {
		return reqmansum;
	}
	@Column(name = "WORKUNITS", length = 200)
	public String getWorkunits() {
		return workunits;
	}
	@Column(name = "SEX", length = 2)
	public String getSex() {
		return sex;
	}
	@Column(name = "AGE", length = 3)
	public String getAge() {
		return age;
	}
	@Column(name = "BIRTH", length = 10)
	public String getBirth() {
		return birth;
	}
	@Column(name = "legalperson", length = 60)
	public String getLegalperson() {
		return legalperson;
	}
	@Column(name = "POSITION", length = 60)
	public String getPosition() {
		return position;
	}
	@Column(name = "PROXYMAN", length = 60)
	public String getProxyman() {
		return proxyman;
	}
	@Column(name = "PROXYPHONE", length = 15)
	public String getProxyphone() {
		return proxyphone;
	}
	@Column(name = "DEFLEGALMAN", length = 60)
	public String getDeflegalman() {
		return deflegalman;
	}
	@Column(name = "DEFLEGALDUTY", length = 60)
	public String getDeflegalduty() {
		return deflegalduty;
	}
	@Column(name = "APPLIEDMAN", length = 60)
	public String getAppliedman() {
		return appliedman;
	}
	@Column(name = "APPLIEDPHONE", length = 15)
	public String getAppliedphone() {
		return appliedphone;
	}
	@Column(name = "INDEMNITY", length = 2)
	public String getIndemnity() {
		return indemnity;
	}
	@Column(name = "ISPREFIX", length = 2)
	public String getIsprefix() {
		return isprefix;
	}
	@Column(name = "PHASES", length = 2)
	public String getPhases() {
		return phases;
	}
	@Column(name = "ISPLACEONFILE", length = 1)
	public String getIsplaceonfile() {
		return isplaceonfile;
	}
	@Column(name = "REGISTERRESULT", length = 2)
	public String getRegisterresult() {
		return registerresult;
	}
	@Column(name = "CASESORT", length = 2)
	public String getCasesort() {
		return casesort;
	}
	@Column(name = "ISREVIEW", length = 2)
	public String getIsreview() {
		return isreview;
	}
	@Column(name = "ISDISCUSS", length = 2)
	public String getIsdiscuss() {
		return isdiscuss;
	}
	@Column(name = "LAUNDERTAKERID")
	public Long getLaundertakerid() {
		return laundertakerid;
	}
	@Column(name = "LAUNDERTAKER", length = 60)
	public String getLaundertaker() {
		return laundertaker;
	}
	@Column(name = "SLUNDERTAKERID")
	public Long getSlundertakerid() {
		return slundertakerid;
	}
	@Column(name = "SLUNDERTAKER", length = 60)
	public String getSlundertaker() {
		return slundertaker;
	}
	@Column(name = "SLCOORGANISER", length = 60)
	public String getSlcoorganiser() {
		return slcoorganiser;
	}
	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return remark;
	}

	@Column(name = "SLCASESORT", length = 2)
	public String getSlcasesort() {
		return slcasesort;
	}

	public void setSlcasesort(String slcasesort) {
		this.slcasesort = slcasesort;
	}

	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CASESORT' and a.status=0  and a.code=casesort)")
	public String getCasesortname() {
		return casesortname;
	}
	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=isdiscuss)")
	public String getIsdiscussname() {
		return isdiscussname;
	}
	@Formula("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=isreview)")
	public String getIsreviewname() {
		return isreviewname;
	}

	public void setCasesortname(String casesortname) {
		this.casesortname = casesortname;
	}

	public void setIsdiscussname(String isdiscussname) {
		this.isdiscussname = isdiscussname;
	}

	public void setIsreviewname(String isreviewname) {
		this.isreviewname = isreviewname;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAdministratif(String administratif) {
		this.administratif = administratif;
	}

	public void setReqmansum(String reqmansum) {
		this.reqmansum = reqmansum;
	}

	public void setWorkunits(String workunits) {
		this.workunits = workunits;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public void setIndemnity(String indemnity) {
		this.indemnity = indemnity;
	}

	public void setIsprefix(String isprefix) {
		this.isprefix = isprefix;
	}

	public void setPhases(String phases) {
		this.phases = phases;
	}

	public void setIsplaceonfile(String isplaceonfile) {
		this.isplaceonfile = isplaceonfile;
	}

	public void setRegisterresult(String registerresult) {
		this.registerresult = registerresult;
	}

	public void setCasesort(String casesort) {
		this.casesort = casesort;
	}

	public void setIsreview(String isreview) {
		this.isreview = isreview;
	}

	public void setIsdiscuss(String isdiscuss) {
		this.isdiscuss = isdiscuss;
	}

	public void setLaundertakerid(Long laundertakerid) {
		this.laundertakerid = laundertakerid;
	}

	public void setLaundertaker(String laundertaker) {
		this.laundertaker = laundertaker;
	}

	public void setSlundertakerid(Long slundertakerid) {
		this.slundertakerid = slundertakerid;
	}

	public void setSlundertaker(String slundertaker) {
		this.slundertaker = slundertaker;
	}

	public void setSlcoorganiser(String slcoorganiser) {
		this.slcoorganiser = slcoorganiser;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
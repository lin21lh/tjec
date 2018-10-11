package com.wfzcx.aros.sqbl.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 申请笔录信息
 * @author zhaoxd
 *
 */
@Entity
@Table(name = "B_APPLYRECORD")
public class ApplyRecordInfo {
	
	private String arid;             // VARCHAR2(32)    申请笔录ID
	private String caseid;          // VARCHAR2(32)     案件ID
	private String reqtime;          // VARCHAR2(19)    
	private String place;            // VARCHAR2(200)   地点
	private String inquirer;         // VARCHAR2(60)    调查人
	private String noter;            // VARCHAR2(60)    记录人
	private String workunits;        // VARCHAR2(200)   工作单位
	private String appname;          // VARCHAR2(300)   申请人姓名
	private String sex;              // VARCHAR2(2)     性别 0女1男 对应数据字典中的SEX 申请人为公民时维护此项
	private String age;              // VARCHAR2(3)     年龄 申请人为公民时维护此项
	private String address;          // VARCHAR2(300)   通讯地址
	private String phone;            // VARCHAR2(20)    联系电话
	private String issue;            // VARCHAR2(2000)  问
	private String answer;           // VARCHAR2(2000)  回答
	private String appdate;          // VARCHAR2(10)    申请行政复议日期
	private String opttype;          // VARCHAR2(2)     处理标志 0：已提交1：已接受2：已处理9：已退回
	private String issolve;          // VARCHAR2(2)     是否案前化解'
	private String remark;           // VARCHAR2(2000)  
	private String userid;           // NUMBER(19)      操作人ID
	private String operator;         // VARCHAR2(20)    操作人
	private String optdate;          // VARCHAR2(10)    操作日期'
	
	public ApplyRecordInfo() {
		super();
	}

	public ApplyRecordInfo(String arid, String caseid, String reqtime, String place, String inquirer, String noter,
			String workunits, String appname, String sex, String age, String address, String phone, String issue,
			String answer, String appdate, String opttype, String issolve, String remark, String userid,
			String operator, String optdate) {
		super();
		this.arid = arid;
		this.caseid = caseid;
		this.reqtime = reqtime;
		this.place = place;
		this.inquirer = inquirer;
		this.noter = noter;
		this.workunits = workunits;
		this.appname = appname;
		this.sex = sex;
		this.age = age;
		this.address = address;
		this.phone = phone;
		this.issue = issue;
		this.answer = answer;
		this.appdate = appdate;
		this.opttype = opttype;
		this.issolve = issolve;
		this.remark = remark;
		this.userid = userid;
		this.operator = operator;
		this.optdate = optdate;
	}

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ARID", unique = true, nullable = false, length = 32)
	public String getArid() {
		return arid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return caseid;
	}

	@Column(name = "REQTIME", length = 19)
	public String getReqtime() {
		return reqtime;
	}

	@Column(name = "PLACE", length = 200)
	public String getPlace() {
		return place;
	}

	@Column(name = "INQUIRER", length = 60)
	public String getInquirer() {
		return inquirer;
	}

	@Column(name = "NOTER", length = 60)
	public String getNoter() {
		return noter;
	}

	@Column(name = "WORKUNITS", length = 200)
	public String getWorkunits() {
		return workunits;
	}

	@Column(name = "APPNAME", length = 300)
	public String getAppname() {
		return appname;
	}

	@Column(name = "SEX", length = 2)
	public String getSex() {
		return sex;
	}

	@Column(name = "AGE", length = 3)
	public String getAge() {
		return age;
	}

	@Column(name = "ADDRESS", length = 300)
	public String getAddress() {
		return address;
	}

	@Column(name = "PHONE", length = 20)
	public String getPhone() {
		return phone;
	}

	@Column(name = "ISSUE", length = 2000)
	public String getIssue() {
		return issue;
	}

	@Column(name = "ANSWER", length = 2000)
	public String getAnswer() {
		return answer;
	}

	@Column(name = "APPDATE", length = 10)
	public String getAppdate() {
		return appdate;
	}

	@Column(name = "OPTTYPE", length = 2)
	public String getOpttype() {
		return opttype;
	}

	@Column(name = "ISSOLVE", length = 2)
	public String getIssolve() {
		return issolve;
	}

	@Column(name = "REMARK", length = 2000)
	public String getRemark() {
		return remark;
	}

	@Column(name = "USERID", length = 19)
	public String getUserid() {
		return userid;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return operator;
	}

	@Column(name = "OPTDATE", length = 10)
	public String getOptdate() {
		return optdate;
	}

	public void setArid(String arid) {
		this.arid = arid;
	}


	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}


	public void setReqtime(String reqtime) {
		this.reqtime = reqtime;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public void setInquirer(String inquirer) {
		this.inquirer = inquirer;
	}


	public void setNoter(String noter) {
		this.noter = noter;
	}


	public void setWorkunits(String workunits) {
		this.workunits = workunits;
	}


	public void setAppname(String appname) {
		this.appname = appname;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}


	public void setAge(String age) {
		this.age = age;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public void setIssue(String issue) {
		this.issue = issue;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}


	public void setOpttype(String opttype) {
		this.opttype = opttype;
	}


	public void setIssolve(String issolve) {
		this.issolve = issolve;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public void setOperator(String operator) {
		this.operator = operator;
	}


	public void setOptdate(String optdate) {
		this.optdate = optdate;
	}

	@Override
	public String toString() {
		return "ApplyRecordInfo [arid=" + arid + ", caseid=" + caseid + ", reqtime=" + reqtime + ", place=" + place
				+ ", inquirer=" + inquirer + ", noter=" + noter + ", workunits=" + workunits + ", appname=" + appname
				+ ", sex=" + sex + ", age=" + age + ", address=" + address + ", phone=" + phone + ", issue=" + issue
				+ ", answer=" + answer + ", appdate=" + appdate + ", opttype=" + opttype + ", issolve=" + issolve
				+ ", remark=" + remark + ", userid=" + userid + ", operator=" + operator + ", optdate=" + optdate + "]";
	}
	
}
	
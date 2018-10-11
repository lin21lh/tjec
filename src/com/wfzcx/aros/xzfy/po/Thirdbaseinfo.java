package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Thirdbaseinfo
 * @Description: 第三人基本信息实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年8月12日 上午9:26:52
 * @version V1.0
 */
@Entity
@Table(name = "B_THIRDBASEINFO" )
public class Thirdbaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String thid;			//第三人ID
	private String thname;			//第三人名称
	private String thtype;			//第三人类型
	private String thidtype;		//第三人证件类型
	private String thidcode;		//第三人证件号码
	private String thphone;			//第三人联系电话
	private String thaddress;		//第三人通讯地址
	private String thpostcode;		//第三人邮政编码
	private String caseid;			//案件ID
	private String thidproxyman;    //第三人委托代理人
	private String thidproxyphone;  //第三人委托代理人电话
	private String thidproxyaddress;//第三人委托代理人地址

	// Constructors

	/** default constructor */
	public Thirdbaseinfo() {
	}

	/** minimal constructor */
	public Thirdbaseinfo(String thid) {
		this.thid = thid;
	}

	

	public Thirdbaseinfo(String thid, String thname, String thtype, String thidtype, String thidcode, String thphone,
			String thaddress, String thpostcode, String caseid, String thidproxyman, String thidproxyphone,
			String thidproxyaddress) {
		super();
		this.thid = thid;
		this.thname = thname;
		this.thtype = thtype;
		this.thidtype = thidtype;
		this.thidcode = thidcode;
		this.thphone = thphone;
		this.thaddress = thaddress;
		this.thpostcode = thpostcode;
		this.caseid = caseid;
		this.thidproxyman = thidproxyman;
		this.thidproxyphone = thidproxyphone;
		this.thidproxyaddress = thidproxyaddress;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "THID", unique = true, nullable = false, length = 32)
	public String getThid() {
		return this.thid;
	}

	public void setThid(String thid) {
		this.thid = thid;
	}

	@Column(name = "THNAME", length = 300)
	public String getThname() {
		return this.thname;
	}

	public void setThname(String thname) {
		this.thname = thname;
	}

	@Column(name = "THTYPE", length = 2)
	public String getThtype() {
		return this.thtype;
	}

	public void setThtype(String thtype) {
		this.thtype = thtype;
	}

	@Column(name = "THIDTYPE", length = 2)
	public String getThidtype() {
		return this.thidtype;
	}

	public void setThidtype(String thidtype) {
		this.thidtype = thidtype;
	}

	@Column(name = "THIDCODE", length = 20)
	public String getThidcode() {
		return this.thidcode;
	}

	public void setThidcode(String thidcode) {
		this.thidcode = thidcode;
	}

	@Column(name = "THPHONE", length = 11)
	public String getThphone() {
		return this.thphone;
	}

	public void setThphone(String thphone) {
		this.thphone = thphone;
	}

	@Column(name = "THADDRESS", length = 300)
	public String getThaddress() {
		return this.thaddress;
	}

	public void setThaddress(String thaddress) {
		this.thaddress = thaddress;
	}

	@Column(name = "THPOSTCODE", length = 6)
	public String getThpostcode() {
		return this.thpostcode;
	}

	public void setThpostcode(String thpostcode) {
		this.thpostcode = thpostcode;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "THIDPROXYMAN", length = 60)
	public String getThidproxyman() {
		return thidproxyman;
	}
	@Column(name = "THIDPROXYPHONE", length = 15)
	public String getThidproxyphone() {
		return thidproxyphone;
	}

	@Column(name = "THIDPROXYADDRESS", length = 300)
	public String getThidproxyaddress() {
		return thidproxyaddress;
	}

	
	public void setThidproxyman(String thidproxyman) {
		this.thidproxyman = thidproxyman;
	}

	public void setThidproxyphone(String thidproxyphone) {
		this.thidproxyphone = thidproxyphone;
	}

	public void setThidproxyaddress(String thidproxyaddress) {
		this.thidproxyaddress = thidproxyaddress;
	}
	
	
}
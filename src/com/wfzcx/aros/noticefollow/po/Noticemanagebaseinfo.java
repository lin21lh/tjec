/**
 * @Description: 通知书跟踪bean
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.noticefollow.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BNoticemanagebaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_NOTICEMANAGEBASEINFO")
public class Noticemanagebaseinfo implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	// Fields
	private String id;
	private String noticeid;
	private String noticetype;
	private String noticename;
	private String deliverytype;
	private String deliverydate;
	private String orgperson;
	private String sender;
	private String place;
	private String remark;
	private String operator;
	private String opttime;
	private String receiver;
	private String couriernum;

	// Constructors

	/** default constructor */
	public Noticemanagebaseinfo() {
	}

	/** minimal constructor */
	public Noticemanagebaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public Noticemanagebaseinfo(String id, String noticeid, String noticetype,
			String noticename, String deliverytype, String deliverydate,
			String orgperson, String sender, String place, String remark,
			String operator, String opttime, String receiver, String couriernum) {
		this.id = id;
		this.noticeid = noticeid;
		this.noticetype = noticetype;
		this.noticename = noticename;
		this.deliverytype = deliverytype;
		this.deliverydate = deliverydate;
		this.orgperson = orgperson;
		this.sender = sender;
		this.place = place;
		this.remark = remark;
		this.operator = operator;
		this.opttime = opttime;
		this.receiver = receiver;
		this.couriernum = couriernum;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NOTICEID", length = 32)
	public String getNoticeid() {
		return this.noticeid;
	}

	public void setNoticeid(String noticeid) {
		this.noticeid = noticeid;
	}

	@Column(name = "NOTICETYPE", length = 1)
	public String getNoticetype() {
		return this.noticetype;
	}

	public void setNoticetype(String noticetype) {
		this.noticetype = noticetype;
	}

	@Column(name = "NOTICENAME", length = 100)
	public String getNoticename() {
		return noticename;
	}

	public void setNoticename(String noticename) {
		this.noticename = noticename;
	}

	@Column(name = "DELIVERYTYPE", length = 2)
	public String getDeliverytype() {
		return this.deliverytype;
	}

	public void setDeliverytype(String deliverytype) {
		this.deliverytype = deliverytype;
	}

	@Column(name = "DELIVERYDATE", length = 10)
	public String getDeliverydate() {
		return this.deliverydate;
	}

	public void setDeliverydate(String deliverydate) {
		this.deliverydate = deliverydate;
	}

	@Column(name = "ORGPERSON", length = 100)
	public String getOrgperson() {
		return orgperson;
	}

	public void setOrgperson(String orgperson) {
		this.orgperson = orgperson;
	}

	@Column(name = "SENDER", length = 100)
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	@Column(name = "PLACE", length = 200)
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "OPERATOR", length = 20)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 20)
	public String getOpttime() {
		return this.opttime;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

	@Column(name = "RECEIVER", length = 100)
	public String getReceiver() {
		return this.receiver;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "COURIERNUM", length = 100)
	public String getCouriernum() {
		return couriernum;
	}

	public void setCouriernum(String couriernum) {
		this.couriernum = couriernum;
	}
	
}
package com.wfzcx.ppms.mesNotification.po;

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
 * ProMessageZb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_MESSAGE_ZB")
@SequenceGenerator(name="SEQ_PRO_MESSAGE_ZB",sequenceName="SEQ_PRO_MESSAGE_ZB")
@GenericGenerator(name = "SEQ_PRO_MESSAGE_ZB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_MESSAGE_ZB") })
public class ProMessageZb implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer messageid;
	private String messageType;
	private String messageContext;
	private String status;
	private String remark;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;

	// Constructors

	/** default constructor */
	public ProMessageZb() {
	}

	/** minimal constructor */
	public ProMessageZb(Integer messageid, String messageType, String messageContext, String status,
			String createTime, String createUser) {
		this.messageid = messageid;
		this.messageType = messageType;
		this.messageContext = messageContext;
		this.status = status;
		this.createTime = createTime;
		this.createUser = createUser;
	}

	/** full constructor */
	public ProMessageZb(Integer messageid, String messageType, String messageContext, String status, String remark,
			String createTime, String createUser, String updateTime, String updateUser) {
		this.messageid = messageid;
		this.messageType = messageType;
		this.messageContext = messageContext;
		this.status = status;
		this.remark = remark;
		this.createTime = createTime;
		this.createUser = createUser;
		this.updateTime = updateTime;
		this.updateUser = updateUser;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_MESSAGE_ZB")
	@Column(name = "MESSAGEID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getMessageid() {
		return this.messageid;
	}

	public void setMessageid(Integer messageid) {
		this.messageid = messageid;
	}

	@Column(name = "MESSAGE_TYPE", nullable = false, length = 1)
	public String getMessageType() {
		return this.messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Column(name = "MESSAGE_CONTEXT", nullable = false, length = 200)
	public String getMessageContext() {
		return this.messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	@Column(name = "STATUS", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", nullable = false, length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER", nullable = false, length = 20)
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

}
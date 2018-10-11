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
 * ProMessageCb entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_MESSAGE_CB")
@SequenceGenerator(name="SEQ_PRO_MESSAGE_CB",sequenceName="SEQ_PRO_MESSAGE_CB")
@GenericGenerator(name = "SEQ_PRO_MESSAGE_CB", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_MESSAGE_CB") })
public class ProMessageCb implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private Integer msgcbid;
	private Integer messageid;
	private String receiveName;
	private String receiveLb;
	private String receivePhone;
	private String receiveFlag;
	private Integer receiveId;

	// Constructors

	/** default constructor */
	public ProMessageCb() {
	}

	/** minimal constructor */
	public ProMessageCb(Integer msgcbid, Integer messageid, String receiveName, String receiveLb,
			String receivePhone, Integer receiveId) {
		this.msgcbid = msgcbid;
		this.messageid = messageid;
		this.receiveName = receiveName;
		this.receiveLb = receiveLb;
		this.receivePhone = receivePhone;
		this.receiveId = receiveId;
	}

	/** full constructor */
	public ProMessageCb(Integer msgcbid, Integer messageid, String receiveName, String receiveLb,
			String receivePhone, String receiveFlag, Integer receiveId) {
		this.msgcbid = msgcbid;
		this.messageid = messageid;
		this.receiveName = receiveName;
		this.receiveLb = receiveLb;
		this.receivePhone = receivePhone;
		this.receiveFlag = receiveFlag;
		this.receiveId = receiveId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_MESSAGE_CB")
	@Column(name = "MSGCBID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getMsgcbid() {
		return this.msgcbid;
	}

	public void setMsgcbid(Integer msgcbid) {
		this.msgcbid = msgcbid;
	}

	@Column(name = "MESSAGEID", nullable = false, precision = 20, scale = 0)
	public Integer getMessageid() {
		return this.messageid;
	}

	public void setMessageid(Integer messageid) {
		this.messageid = messageid;
	}

	@Column(name = "RECEIVE_NAME", nullable = false, length = 100)
	public String getReceiveName() {
		return this.receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	@Column(name = "RECEIVE_LB", nullable = false, length = 1)
	public String getReceiveLb() {
		return this.receiveLb;
	}

	public void setReceiveLb(String receiveLb) {
		this.receiveLb = receiveLb;
	}

	@Column(name = "RECEIVE_PHONE", nullable = false, length = 100)
	public String getReceivePhone() {
		return this.receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	@Column(name = "RECEIVE_FLAG", length = 1)
	public String getReceiveFlag() {
		return this.receiveFlag;
	}

	public void setReceiveFlag(String receiveFlag) {
		this.receiveFlag = receiveFlag;
	}
	
	@Column(name = "RECEIVE_ID", nullable = false, precision = 20, scale = 0)
	public Integer getReceiveId() {
		return this.receiveId;
	}

	public void setReceiveId(Integer receiveId) {
		this.receiveId = receiveId;
	}

}
package com.wfzcx.fam.manage.po;

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
 * FaMessage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "FA_MESSAGE")
@SequenceGenerator(name="SEQ_FA_MESSAGE",sequenceName="SEQ_FA_MESSAGE")
@GenericGenerator(name = "SEQ_FA_MESSAGE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_FA_MESSAGE") })
public class FaMessage implements java.io.Serializable {

	// Fields

	private Integer itemid;
	private Integer applicationId;
	private String message;
	private String phone;
	private String sendflag;
	private String backflag;
	private String createUser;
	private String createTime;
	private String activityId;

	// Constructors

	/** default constructor */
	public FaMessage() {
	}

	/** minimal constructor */
	public FaMessage(Integer itemid, Integer applicationId, String message,
			String phone, String sendflag, String backflag) {
		this.itemid = itemid;
		this.applicationId = applicationId;
		this.message = message;
		this.phone = phone;
		this.sendflag = sendflag;
		this.backflag = backflag;
	}

	/** full constructor */
	public FaMessage(Integer itemid, Integer applicationId, String message,
			String phone, String sendflag, String backflag, String createUser,
			String createTime,String activityId) {
		this.itemid = itemid;
		this.applicationId = applicationId;
		this.message = message;
		this.phone = phone;
		this.sendflag = sendflag;
		this.backflag = backflag;
		this.createUser = createUser;
		this.createTime = createTime;
		this.activityId = activityId;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FA_MESSAGE")
	@Column(name = "ITEMID", unique = true, nullable = false, precision = 9, scale = 0)
	public Integer getItemid() {
		return this.itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	@Column(name = "APPLICATION_ID", nullable = false, precision = 9, scale = 0)
	public Integer getApplicationId() {
		return this.applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	@Column(name = "MESSAGE", nullable = false, length = 4000)
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name = "PHONE", nullable = false, length = 100)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "SENDFLAG", nullable = false, length = 1)
	public String getSendflag() {
		return this.sendflag;
	}

	public void setSendflag(String sendflag) {
		this.sendflag = sendflag;
	}

	@Column(name = "BACKFLAG",length = 1)
	public String getBackflag() {
		return this.backflag;
	}

	public void setBackflag(String backflag) {
		this.backflag = backflag;
	}

	@Column(name = "CREATE_USER", length = 50)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "ACTIVITY_ID", length = 20)
	public String getActivityId() {
		return this.activityId;
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

}
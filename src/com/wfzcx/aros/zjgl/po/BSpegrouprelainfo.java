package com.wfzcx.aros.zjgl.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BSpegrouprelainfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_SPEGROUPRELAINFO")
public class BSpegrouprelainfo implements java.io.Serializable {

	// Fields

	private String id;
	private String groupid;
	private String speid;
	private String remark;

	// Constructors

	/** default constructor */
	public BSpegrouprelainfo() {
	}

	/** minimal constructor */
	public BSpegrouprelainfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public BSpegrouprelainfo(String id, String groupid, String speid,
			String remark) {
		this.id = id;
		this.groupid = groupid;
		this.speid = speid;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "GROUPID", length = 32)
	public String getGroupid() {
		return this.groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Column(name = "SPEID", length = 32)
	public String getSpeid() {
		return this.speid;
	}

	public void setSpeid(String speid) {
		this.speid = speid;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
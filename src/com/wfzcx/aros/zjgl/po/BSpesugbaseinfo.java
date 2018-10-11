package com.wfzcx.aros.zjgl.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

/**
 * 专家意见PO
 * BSpesugbaseinfo entity.
 * @author zhaoXD
 * 
 */
@Entity
@Table(name = "b_Spesugbaseinfo")
public class BSpesugbaseinfo implements java.io.Serializable {

	// Fields
 	private String id;
 	private String processid;
 	private String caseid;
 	private String speid;
 	private String spename;
 	private String remark;   
 	private String operator; 
 	private String opttime;  
 	private String groupid;

 	// Constructors

 	/** default constructor */
 	public BSpesugbaseinfo() {
 	}

 	/** minimal constructor */
 	public BSpesugbaseinfo(String id) {
 		this.id = id;
 	}

 	/** full constructor */

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
	
	@Column(name = "PROCESSID", length = 32)
	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "SPEID", length = 32)
	public String getSpeid() {
		return speid;
	}

	public void setSpeid(String speid) {
		this.speid = speid;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "OPERATOR", length = 30)
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OPTTIME", length = 30)
	public String getOpttime() {
		return opttime;
	}

	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}
	
	@Column(name = "GROUPID", length = 32)
	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Formula("(SELECT T.SPENAME FROM B_SPECIALISTBASEINFO T WHERE T.SPEID=speid)")
	public String getSpename() {
		return spename;
	}

	public void setSpename(String spename) {
		this.spename = spename;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BSpesugbaseinfo [id=");
		builder.append(id);
		builder.append(", processid=");
		builder.append(processid);
		builder.append(", caseid=");
		builder.append(caseid);
		builder.append(", speid=");
		builder.append(speid);
		builder.append(", spename=");
		builder.append(spename);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", operator=");
		builder.append(operator);
		builder.append(", opttime=");
		builder.append(opttime);
		builder.append(", groupid=");
		builder.append(groupid);
		builder.append("]");
		return builder.toString();
	}
	
 }

package com.wfzcx.demo.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * TXmxx entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DEMO")
@SequenceGenerator(name="SEQ_DEMO",sequenceName="SEQ_DEMO")
@GenericGenerator(name = "SEQ_DEMO", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_DEMO") })
public class Demo implements java.io.Serializable {

	// Fields

	private Integer id;
	
	private String name;
	private String sex;
	private String remark;
	private String ssjg;
	private String cjr;
	private Date cjsj;
	private String xgr;
	private Date xgsj;

	// Constructors
	@Column(name = "SSJG",  length = 50)
	public String getSsjg() {
		return ssjg;
	}

	public void setSsjg(String ssjg) {
		this.ssjg = ssjg;
	}

	/** default constructor */
	public Demo() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEMO")
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "NAME",  length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "SEX",  length = 1)
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@Column(name = "REMARK",  length = 1000)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "CJR", nullable = false, length = 20)
	public String getCjr() {
		return this.cjr;
	}

	public void setCjr(String cjr) {
		this.cjr = cjr;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CJSJ", nullable = false, length = 7)
	public Date getCjsj() {
		return this.cjsj;
	}

	public void setCjsj(Date cjsj) {
		this.cjsj = cjsj;
	}
	@Column(name = "XGR", length = 20)
	public String getXgr() {
		return this.xgr;
	}

	public void setXgr(String xgr) {
		this.xgr = xgr;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "XGSJ", length = 7)
	public Date getXgsj() {
		return this.xgsj;
	}
	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}

}
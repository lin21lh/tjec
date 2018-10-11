package com.wfzcx.ppms.discern.po;

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
 * ProProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PRODUCT")
@SequenceGenerator(name="SEQ_PRO_PRODUCT",sequenceName="SEQ_PRO_PRODUCT")
@GenericGenerator(name = "SEQ_PRO_PRODUCT", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PRODUCT") })
public class ProProduct implements java.io.Serializable {

	// Fields

	private Integer productid;
	private Integer projectid;
	private String year;
	private String output;
	private String unit;
	private String amount;
	private String remark;

	// Constructors

	/** default constructor */
	public ProProduct() {
	}

	/** minimal constructor */
	public ProProduct(Integer productid, Integer projectid, String year,
			String output, String unit, String amount) {
		this.productid = productid;
		this.projectid = projectid;
		this.year = year;
		this.output = output;
		this.unit = unit;
		this.amount = amount;
	}

	/** full constructor */
	public ProProduct(Integer productid, Integer projectid,  String year,
			String output, String unit, String amount, String remark) {
		this.productid = productid;
		this.projectid = projectid;
		this.year = year;
		this.output = output;
		this.unit = unit;
		this.amount = amount;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PRODUCT")
	@Column(name = "PRODUCTID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getProductid() {
		return this.productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "YEAR", nullable = false, precision = 4, scale = 0)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "OUTPUT", nullable = false, length = 1000)
	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Column(name = "UNIT", nullable = false, length = 20)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "AMOUNT", nullable = false, precision = 10, scale = 0)
	public String getAmount() {
		return this.amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
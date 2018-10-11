package com.wfzcx.ppms.execute.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProPerformance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_PERFORMANCE")
@GenericGenerator(name = "SEQ_PRO_PERFORMANCE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_PERFORMANCE") })
public class ProPerformance implements java.io.Serializable {

	// Fields

	private int performanceid;
	private int projectid;
	private String year;
	private String productElement;
	private String unit;
	private Integer amount;
	private String attachPath;
	private String remark;

	// Constructors

	/** default constructor */
	public ProPerformance() {
	}

	/** minimal constructor */
	public ProPerformance(int performanceid, int projectid,
			String year, String unit, Integer amount) {
		this.performanceid = performanceid;
		this.projectid = projectid;
		this.year = year;
		this.unit = unit;
		this.amount = amount;
	}

	/** full constructor */
	public ProPerformance(int performanceid, int projectid,
			String year, String productElement, String unit, Integer amount,
			String attachPath, String remark) {
		this.performanceid = performanceid;
		this.projectid = projectid;
		this.year = year;
		this.productElement = productElement;
		this.unit = unit;
		this.amount = amount;
		this.attachPath = attachPath;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_PERFORMANCE")
	@Column(name = "PERFORMANCEID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getPerformanceid() {
		return this.performanceid;
	}

	public void setPerformanceid(int performanceid) {
		this.performanceid = performanceid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	@Column(name = "YEAR", nullable = false, length = 4)
	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Column(name = "PRODUCT_ELEMENT", length = 1000)
	public String getProductElement() {
		return this.productElement;
	}

	public void setProductElement(String productElement) {
		this.productElement = productElement;
	}

	@Column(name = "UNIT", nullable = false, length = 100)
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Column(name = "AMOUNT", nullable = false, precision = 9, scale = 0)
	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(name = "ATTACH_PATH", length = 200)
	public String getAttachPath() {
		return this.attachPath;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
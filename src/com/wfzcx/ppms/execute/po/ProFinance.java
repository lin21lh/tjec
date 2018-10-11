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
 * ProFinance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_FINANCE")
@GenericGenerator(name = "SEQ_PRO_FINANCE", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_FINANCE") })
public class ProFinance implements java.io.Serializable {

	// Fields

	private int financeid;
	private String projectid;
	private String financeCode;
	private String financeName;
	private Double amount;
	private String remark;

	// Constructors

	/** default constructor */
	public ProFinance() {
	}

	/** minimal constructor */
	public ProFinance(int financeid, String projectid,
			String financeCode, String financeName) {
		this.financeid = financeid;
		this.projectid = projectid;
		this.financeCode = financeCode;
		this.financeName = financeName;
	}

	/** full constructor */
	public ProFinance(int financeid, String projectid,
			String financeCode, String financeName, Double amount, String remark) {
		this.financeid = financeid;
		this.projectid = projectid;
		this.financeCode = financeCode;
		this.financeName = financeName;
		this.amount = amount;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_FINANCE")
	@Column(name = "FINANCEID", unique = true, nullable = false, precision = 20, scale = 0)
	public int getFinanceid() {
		return this.financeid;
	}

	public void setFinanceid(int financeid) {
		this.financeid = financeid;
	}

	@Column(name = "PROJECTID", nullable = false, length = 20)
	public String getProjectid() {
		return this.projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	@Column(name = "FINANCE_CODE", nullable = false, length = 50)
	public String getFinanceCode() {
		return this.financeCode;
	}

	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode;
	}

	@Column(name = "FINANCE_NAME", nullable = false, length = 100)
	public String getFinanceName() {
		return this.financeName;
	}

	public void setFinanceName(String financeName) {
		this.financeName = financeName;
	}

	@Column(name = "AMOUNT", precision = 16)
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
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
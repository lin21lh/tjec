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
 * ProBudget entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_BUDGET")
@SequenceGenerator(name="SEQ_PRO_BUDGET",sequenceName="SEQ_PRO_BUDGET")
@GenericGenerator(name = "SEQ_PRO_BUDGET", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_BUDGET") })
public class ProBudget implements java.io.Serializable {

	// Fields

	private Integer budgetid;
	private Integer projectid;
	private String budgetType;
	private String budgetYear;
	private Double budgetGqtz;
	private Double budgetYybt;
	private Double budgetFxcd;
	private Double budgetPttr;
	private Double total;
	private Double budgetYbggys;
	private String xmhj;
	@Column(name = "XMHJ")
	public String getXmhj() {
		return xmhj;
	}

	public void setXmhj(String xmhj) {
		this.xmhj = xmhj;
	}

	// Constructors
	@Column(name = "BUDGET_YBGGYS")
	public Double getBudgetYbggys() {
		return budgetYbggys;
	}

	public void setBudgetYbggys(Double budgetYbggys) {
		this.budgetYbggys = budgetYbggys;
	}

	/** default constructor */
	public ProBudget() {
	}

	/** minimal constructor */
	public ProBudget(Integer budgetid, Integer projectid,
			String budgetYear, Double budgetGqtz, Double budgetYybt,
			Double budgetFxcd, Double budgetPttr, Double total) {
		this.budgetid = budgetid;
		this.projectid = projectid;
		this.budgetYear = budgetYear;
		this.budgetGqtz = budgetGqtz;
		this.budgetYybt = budgetYybt;
		this.budgetFxcd = budgetFxcd;
		this.budgetPttr = budgetPttr;
		this.total = total;
	}

	/** full constructor */
	public ProBudget(Integer budgetid, Integer projectid,
			String budgetType, String budgetYear, Double budgetGqtz,
			Double budgetYybt, Double budgetFxcd, Double budgetPttr,
			Double total) {
		this.budgetid = budgetid;
		this.projectid = projectid;
		this.budgetType = budgetType;
		this.budgetYear = budgetYear;
		this.budgetGqtz = budgetGqtz;
		this.budgetYybt = budgetYybt;
		this.budgetFxcd = budgetFxcd;
		this.budgetPttr = budgetPttr;
		this.total = total;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_BUDGET")
	@Column(name = "BUDGETID", unique = true, nullable = false, precision = 20, scale = 0)
	public Integer getBudgetid() {
		return this.budgetid;
	}

	public void setBudgetid(Integer budgetid) {
		this.budgetid = budgetid;
	}

	@Column(name = "PROJECTID", nullable = false, precision = 20, scale = 0)
	public Integer getProjectid() {
		return this.projectid;
	}

	public void setProjectid(Integer projectid) {
		this.projectid = projectid;
	}

	@Column(name = "BUDGET_TYPE", length = 1)
	public String getBudgetType() {
		return this.budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	@Column(name = "BUDGET_YEAR")
	public String getBudgetYear() {
		return this.budgetYear;
	}

	public void setBudgetYear(String budgetYear) {
		this.budgetYear = budgetYear;
	}

	@Column(name = "BUDGET_GQTZ", nullable = false, precision = 16)
	public Double getBudgetGqtz() {
		return this.budgetGqtz;
	}

	public void setBudgetGqtz(Double budgetGqtz) {
		this.budgetGqtz = budgetGqtz;
	}

	@Column(name = "BUDGET_YYBT", nullable = false, precision = 16)
	public Double getBudgetYybt() {
		return this.budgetYybt;
	}

	public void setBudgetYybt(Double budgetYybt) {
		this.budgetYybt = budgetYybt;
	}

	@Column(name = "BUDGET_FXCD", nullable = false, precision = 16)
	public Double getBudgetFxcd() {
		return this.budgetFxcd;
	}

	public void setBudgetFxcd(Double budgetFxcd) {
		this.budgetFxcd = budgetFxcd;
	}

	@Column(name = "BUDGET_PTTR", nullable = false, precision = 16)
	public Double getBudgetPttr() {
		return this.budgetPttr;
	}

	public void setBudgetPttr(Double budgetPttr) {
		this.budgetPttr = budgetPttr;
	}

	@Column(name = "TOTAL", nullable = false, precision = 16)
	public Double getTotal() {
		return this.total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

}
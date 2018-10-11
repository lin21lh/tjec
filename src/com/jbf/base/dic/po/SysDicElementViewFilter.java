/************************************************************
 * 类名：SysDicElementViewFilter.java
 *
 * 类别：PO
 * 功能：数据项过滤条件PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.po;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.util.StringUtil;



/**
 * SysDicElementViewFilter entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_DicElementViewFilter")
public class SysDicElementViewFilter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long filterid;
	private String elementcode;
	private Integer seqno;
	private String leftbracket;
	private String columncode;
	private String columnname;
	private Byte notsymbol;
	private String operator;
	private String minvalue;
	private String minvaluename;
	private String maxvalue;
	private String maxvaluename;
	private String logicalsymbol;
	private String rightbracket;
	private String remark;
	
	private String  sourceelementcode;
	private String columntype;

	// Constructors

	/** default constructor */
	public SysDicElementViewFilter() {
	}

	/** minimal constructor */
	public SysDicElementViewFilter(String elementcode, Integer seqno,
			String columncode, Byte notsymbol, String operator,
			String minvalue, String logicalsymbol) {
		this.elementcode = elementcode;
		this.seqno = seqno;
		this.columncode = columncode;
		this.notsymbol = notsymbol;
		this.operator = operator;
		this.minvalue = minvalue;
		this.logicalsymbol = logicalsymbol;
	}

	/** full constructor */
	public SysDicElementViewFilter(String elementcode, Integer seqno,
			String leftbracket, String columncode, Byte notsymbol,
			String operator, String minvalue, String maxvalue,
			String logicalsymbol, String rightbracket, String remark) {
		this.elementcode = elementcode;
		this.seqno = seqno;
		this.leftbracket = leftbracket;
		this.columncode = columncode;
		this.notsymbol = notsymbol;
		this.operator = operator;
		this.minvalue = minvalue;
		this.maxvalue = maxvalue;
		this.logicalsymbol = logicalsymbol;
		this.rightbracket = rightbracket;
		this.remark = remark;
	}
	
	public String toString(SysDicTable filterTable, SysDicTable mainTable, String mainAlias, boolean isElementColumn) {
		StringBuilder where = new StringBuilder();
		if (StringUtil.isNotBlank(this.leftbracket) && StringUtil.isNotBlank(this.rightbracket))
			where.append(this.leftbracket);
		
		if (this.notsymbol.equals(Byte.valueOf("0")))
			where.append(" exists ");
		else
			where.append(" not exists ");
		if (filterTable != null) {
			where.append("(select ").append(filterTable.getCodecolumn()).append(" from ").append(filterTable.getTablecode()).append(" dic where dic.");
//			if (isElementColumn)
//				where.append("elementcode='").append(this.elementcode).append("' and dic.");
			// where.append(filterTable.getCodecolumn()).append("=").append(StringUtil.isNotBlank(mainAlias) ? mainAlias : "t").append(".").append(this.columncode);
			where.append(filterTable.getCodecolumn()).append("=").append(this.columncode);
			where.append(" and dic.");
			where.append(filterTable.getKeycolumn());
			where.append(getOperatorSql("dic", filterTable.getKeycolumn()));
			
			where.append(")");
		} else {
			where.append("(select ").append(this.columncode).append(" from ").append(mainTable.getTablecode()).append(" t1 where t1.").append(mainTable.getCodecolumn());
			// where.append("=").append(StringUtil.isNotBlank(mainAlias) ? mainAlias : "t").append(".").append(mainTable.getCodecolumn()).append(" and ");
			where.append("=").append(mainTable.getCodecolumn()).append(" and ");
			where.append("t1.").append(this.columncode);
			where.append(getOperatorSql("t1", this.columncode));
			
			where.append(")");
		}
		if (StringUtil.isNotBlank(this.leftbracket) && StringUtil.isNotBlank(this.rightbracket))
			where.append(this.rightbracket);
		if (StringUtil.isNotBlank(this.logicalsymbol))
			where.append(" ").append(this.logicalsymbol);
		return where.toString();
	}
	
	private String getOperatorSql(String alias, String columncode) {
		String operSql = "";
		List<String> list1 = Arrays.asList("=", "<>", ">", ">=", "<", ",<=");
		List<String> list2 = Arrays.asList("betten", "within");
		List<String> list3 = Arrays.asList("likebegin", "likeend", "likeall");
		if (list1.contains(this.operator)) {
			return this.operator + "'" + this.minvalue + "'";
		}
		if (list2.contains(this.operator)) {
			if (this.operator.equals("betten"))
				operSql = ">='" + this.minvalue + "' and " + alias + "." + columncode + "<='" + this.maxvalue + "'";
			else if (this.operator.equals("within"))
				operSql = ">'" + this.minvalue + "' and " + alias + "." + columncode + "<'" + this.maxvalue + "'";
			
			return operSql;
		}
		
		if (list3.contains(this.operator)) {
			operSql = " like ";
			if (this.operator.equals("likebegin"))
				operSql += "'%" + this.minvalue + "'";
			else if (this.operator.equals("likeend"))
				operSql += "'" + this.minvalue + "%'";
			else if (this.operator.equals("likeend"))
				operSql += "'%" + this.minvalue + "%'";
			
			return operSql;
		} 
		return operSql;
	}

	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DICELEMENTVIEWFILTER")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "FILTERID", nullable = false, scale = 0)
	public Long getFilterid() {
		return this.filterid;
	}

	public void setFilterid(Long filterid) {
		this.filterid = filterid;
	}

	@Column(name = "ELEMENTCODE", nullable = false, length = 50)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}

	@Column(name = "SEQNO", nullable = false, precision = 9, scale = 0)
	public Integer getSeqno() {
		return this.seqno;
	}

	public void setSeqno(Integer seqno) {
		this.seqno = seqno;
	}

	@Column(name = "LEFTBRACKET", length = 50)
	public String getLeftbracket() {
		return this.leftbracket;
	}

	public void setLeftbracket(String leftbracket) {
		this.leftbracket = leftbracket;
	}

	@Column(name = "COLUMNCODE", nullable = false, length = 50)
	public String getColumncode() {
		return this.columncode;
	}

	public void setColumncode(String columncode) {
		this.columncode = columncode;
	}
	
	@Transient
	public String getColumnname() {
		return columnname;
	}
	
	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	@Column(name = "NOTSYMBOL", nullable = false, precision = 2, scale = 0)
	public Byte getNotsymbol() {
		return this.notsymbol;
	}

	public void setNotsymbol(Byte notsymbol) {
		this.notsymbol = notsymbol;
	}

	@Column(name = "OPERATOR", nullable = false, length = 50)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "MINVALUE", nullable = false, length = 4000)
	public String getMinvalue() {
		return this.minvalue;
	}

	public void setMinvalue(String minvalue) {
		this.minvalue = minvalue;
	}
	
	@Transient
	public String getMinvaluename() {
		return minvaluename;
	}
	
	public void setMinvaluename(String minvaluename) {
		this.minvaluename = minvaluename;
	}

	@Column(name = "MAXVALUE", length = 4000)
	public String getMaxvalue() {
		return this.maxvalue;
	}

	public void setMaxvalue(String maxvalue) {
		this.maxvalue = maxvalue;
	}

	@Transient
	public String getMaxvaluename() {
		return maxvaluename;
	}
	
	public void setMaxvaluename(String maxvaluename) {
		this.maxvaluename = maxvaluename;
	}
	
	@Column(name = "LOGICALSYMBOL", nullable = true, length = 10)
	public String getLogicalsymbol() {
		return this.logicalsymbol;
	}

	public void setLogicalsymbol(String logicalsymbol) {
		this.logicalsymbol = logicalsymbol;
	}

	@Column(name = "RIGHTBRACKET", length = 50)
	public String getRightbracket() {
		return this.rightbracket;
	}

	public void setRightbracket(String rightbracket) {
		this.rightbracket = rightbracket;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Transient
	public String getColumntype() {
		return columntype;
	}
	
	public void setColumntype(String columntype) {
		this.columntype = columntype;
	}
	
	@Transient
	public String getSourceelementcode() {
		return sourceelementcode;
	}
	
	public void setSourceelementcode(String sourceelementcode) {
		this.sourceelementcode = sourceelementcode;
	}

}
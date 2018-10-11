package com.jbf.sys.formFieldAttrSet.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "SYS_FORMFIELDATTRSET")
@SequenceGenerator(name="SEQ_FORMFIELDATTRSET",sequenceName="SEQ_FORMFIELDATTRSET")
@GenericGenerator(name = "SEQ_FORMFIELDATTRSET", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_FORMFIELDATTRSET") }) 
public class SysFormFieldAttrSet {

	private Long attrsetid;
	private String wfkey;
	private Integer wfversion;
	private String wfnode;
	private String fieldcode;
	private Integer isedit = 0;
	private Integer notnull = 0;
	private Integer defvaltype = 0;
	private String defvalue;
	private Integer ordermodel;
	private Integer ordercontrol;
	private String remark;
	
	public SysFormFieldAttrSet() {
		
	}
	
	public SysFormFieldAttrSet(String wfkey, Integer wfversion, String wfnode, String fieldnode,
			Integer isedit, Integer notnull, Integer defvaltype, String defvalue, Integer ordermodel,
			Integer ordercontrol, String remark) {
		this.wfkey = wfkey;
		this.wfversion = wfversion;
		this.wfnode = wfnode;
		this.fieldcode = fieldnode;
		this.isedit = isedit;
		this.notnull = notnull;
		this.defvaltype = defvaltype;
		this.defvalue = defvalue;
		this.ordermodel = ordermodel;
		this.ordercontrol = ordercontrol;
		this.remark = remark;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_FORMFIELDATTRSET")
	@Column(name = "ATTRSETID", nullable = false, scale = 0)
	public Long getAttrsetid() {
		return attrsetid;
	}
	
	public void setAttrsetid(Long attrsetid) {
		this.attrsetid = attrsetid;
	}
	
	@Column(name = "WFKEY", nullable = false, length = 42)
	public String getWfkey() {
		return wfkey;
	}
	
	public void setWfkey(String wfkey) {
		this.wfkey = wfkey;
	}
	
	@Column(name = "WFVERSION")
	public Integer getWfversion() {
		return wfversion;
	}
	
	public void setWfversion(Integer wfversion) {
		this.wfversion = wfversion;
	}
	
	@Column(name = "WFNODE", length = 42)
	public String getWfnode() {
		return wfnode;
	}
	
	public void setWfnode(String wfnode) {
		this.wfnode = wfnode;
	}
	
	@Column(name = "FIELDCODE", nullable = false, length = 50)
	public String getFieldcode() {
		return fieldcode;
	}
	
	public void setFieldcode(String fieldcode) {
		this.fieldcode = fieldcode;
	}
	
	@Column(name = "ISEDIT", nullable = false)
	public Integer getIsedit() {
		return isedit;
	}
	
	public void setIsedit(Integer isedit) {
		this.isedit = isedit;
	}
	
	@Column(name = "NOTNULL", nullable = false)
	public Integer getNotnull() {
		return notnull;
	}
	
	public void setNotnull(Integer notnull) {
		this.notnull = notnull;
	}
	
	@Column(name = "DEFVALTYPE", nullable = false)
	public Integer getDefvaltype() {
		return defvaltype;
	}
	
	public void setDefvaltype(Integer defvaltype) {
		this.defvaltype = defvaltype;
	}
	
	@Column(name = "DEFVALUE", length = 50)
	public String getDefvalue() {
		return defvalue;
	}
	
	public void setDefvalue(String defvalue) {
		this.defvalue = defvalue;
	}
	
	@Column(name = "ORDERMODEL")
	public Integer getOrdermodel() {
		return ordermodel;
	}
	
	public void setOrdermodel(Integer ordermodel) {
		this.ordermodel = ordermodel;
	}
	
	@Column(name = "ORDERCONTROL")
	public Integer getOrdercontrol() {
		return ordercontrol;
	}
	
	public void setOrdercontrol(Integer ordercontrol) {
		this.ordercontrol = ordercontrol;
	}
	
	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

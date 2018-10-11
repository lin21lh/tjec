/************************************************************
 * 类名：SysDeptExp.java
 *
 * 类别：PO
 * 功能：机构扩展属性PO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_DEPTEXP")
public class SysDeptExp {

	private Long itemid;
	private Long agencycat;
	private Long mofdepmanager;
	private Long mofdepmanager1;
	private Long mofdepmanager2;
	private Long mofdepmanager3;
	private Long mofdepmanager4;
	private Long iscentralizedpay;
	private Long isvirtual;
	private Long bdgduty;
	private Long accountlength;
	private String bankaddress;
	
	public SysDeptExp() {
		
	}
	
	public SysDeptExp(Long itemid, Long agencycat, Long mofdepmanager,
			Long mofdepmanager1, Long mofdepmanager2, Long mofdepmanager3,
			Long mofdepmanager4, Long iscentralizedpay, Long isvirtual,
			Long bdgduty, Long accountlength, String bankaddress) {
		this.itemid = itemid;
		this.agencycat = agencycat;
		this.mofdepmanager = mofdepmanager;
		this.mofdepmanager1 = mofdepmanager1;
		this.mofdepmanager2 = mofdepmanager2;
		this.mofdepmanager3 = mofdepmanager3;
		this.mofdepmanager4 = mofdepmanager4;
		this.iscentralizedpay = iscentralizedpay;
		this.isvirtual = isvirtual;
		this.bdgduty = bdgduty;
		this.accountlength = accountlength;
		this.bankaddress = bankaddress;
	}
	@Id
	@SequenceGenerator(name="oracle_seq", sequenceName="SEQ_DEPTEXP")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "ITEMID", nullable = false, scale = 0)
	public Long getItemid() {
		return itemid;
	}
	public void setItemid(Long itemid) {
		this.itemid = itemid;
	}
	@Column(name = "AGENCYCAT", nullable = false, scale = 0)
	public Long getAgencycat() {
		return agencycat;
	}
	public void setAgencycat(Long agencycat) {
		this.agencycat = agencycat;
	}
	@Column(name = "MOFDEPMANAGER", scale = 0)
	public Long getMofdepmanager() {
		return mofdepmanager;
	}
	public void setMofdepmanager(Long mofdepmanager) {
		this.mofdepmanager = mofdepmanager;
	}
	@Column(name = "MOFDEPMANAGER1", scale = 0)
	public Long getMofdepmanager1() {
		return mofdepmanager1;
	}
	public void setMofdepmanager1(Long mofdepmanager1) {
		this.mofdepmanager1 = mofdepmanager1;
	}
	@Column(name = "MOFDEPMANAGER2", scale = 0)
	public Long getMofdepmanager2() {
		return mofdepmanager2;
	}
	public void setMofdepmanager2(Long mofdepmanager2) {
		this.mofdepmanager2 = mofdepmanager2;
	}
	@Column(name = "MOFDEPMANAGER3", scale = 0)
	public Long getMofdepmanager3() {
		return mofdepmanager3;
	}
	public void setMofdepmanager3(Long mofdepmanager3) {
		this.mofdepmanager3 = mofdepmanager3;
	}
	@Column(name = "MOFDEPMANAGER4", scale = 0)
	public Long getMofdepmanager4() {
		return mofdepmanager4;
	}
	public void setMofdepmanager4(Long mofdepmanager4) {
		this.mofdepmanager4 = mofdepmanager4;
	}
	@Column(name = "ISCENTRALIZEDPAY", scale = 0)
	public Long getIscentralizedpay() {
		return iscentralizedpay;
	}
	public void setIscentralizedpay(Long iscentralizedpay) {
		this.iscentralizedpay = iscentralizedpay;
	}
	@Column(name = "ISVIRTUAL", scale = 0)
	public Long getIsvirtual() {
		return isvirtual;
	}
	public void setIsvirtual(Long isvirtual) {
		this.isvirtual = isvirtual;
	}
	@Column(name = "BDGDUTY", scale = 0)
	public Long getBdgduty() {
		return bdgduty;
	}
	public void setBdgduty(Long bdgduty) {
		this.bdgduty = bdgduty;
	}
	@Column(name = "ACCOUNTLENGTH", scale = 0)
	public Long getAccountlength() {
		return accountlength;
	}
	public void setAccountlength(Long accountlength) {
		this.accountlength = accountlength;
	}
	@Column(name = "BANKADDRESS", length = 255)
	public String getBankaddress() {
		return bankaddress;
	}
	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}
	
	
}

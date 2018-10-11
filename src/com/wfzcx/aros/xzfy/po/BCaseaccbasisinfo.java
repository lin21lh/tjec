package com.wfzcx.aros.xzfy.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: BCaseaccbasisinfo
 * @Description: 行政复议案件受理依据
 * @date 2016年11月2日
 * @version V1.0
 */
@Entity
@Table(name = "B_CASEACCBASISINFO" )
public class BCaseaccbasisinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	private String basisid; // 案件ID
	private String caseid; // 案件ID
	private String processid; // 流程ID
	private String b1; 
	private String b2; 
	private String b3; 
	private String b4; 
	private String b5; 
	private String b6; 
	private String b7; 
	private String b8; 
	private String b9; 
	private String b10; 
	private String b11; 
	private String b12; 
	private String b13; 
	private String b14; 
	private String b15; 

	// Constructors

	/** default constructor */
	public BCaseaccbasisinfo() {
	}

	/** minimal constructor */
	public BCaseaccbasisinfo(String basisid) {
		this.basisid = basisid;
	}

	/** full constructor */
	public BCaseaccbasisinfo(String basisid, String caseid, String processid, String b1, String b2, String b3,
			String b4, String b5, String b6, String b7, String b8, String b9, String b10, String b11, String b12,
			String b13, String b14, String b15) {
		super();
		this.basisid = basisid;
		this.caseid = caseid;
		this.processid = processid;
		this.b1 = b1;
		this.b2 = b2;
		this.b3 = b3;
		this.b4 = b4;
		this.b5 = b5;
		this.b6 = b6;
		this.b7 = b7;
		this.b8 = b8;
		this.b9 = b9;
		this.b10 = b10;
		this.b11 = b11;
		this.b12 = b12;
		this.b13 = b13;
		this.b14 = b14;
		this.b15 = b15;
	}

	// Property accessors
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "BASISID", unique = true, nullable = false, length = 32)
	public String getBasisid() {
		return this.basisid;
	}
	
	public void setBasisid(String basisid) {
		this.basisid = basisid;
	}
	
	@Column(name = "CASEID", length = 32)
	public String getCaseid() {
		return this.caseid;
	}

	public void setCaseid(String caseid) {
		this.caseid = caseid;
	}

	@Column(name = "PROCESSID", length = 32)
	public String getProcessid() {
		return processid;
	}

	public void setProcessid(String processid) {
		this.processid = processid;
	}

	@Column(name = "B1", length = 2)
	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}

	@Column(name = "B2", length = 2)
	public String getB2() {
		return b2;
	}

	public void setB2(String b2) {
		this.b2 = b2;
	}

	@Column(name = "B3", length = 2)
	public String getB3() {
		return b3;
	}

	public void setB3(String b3) {
		this.b3 = b3;
	}

	@Column(name = "B4", length = 2)
	public String getB4() {
		return b4;
	}

	public void setB4(String b4) {
		this.b4 = b4;
	}

	@Column(name = "B5", length = 2)
	public String getB5() {
		return b5;
	}

	public void setB5(String b5) {
		this.b5 = b5;
	}

	@Column(name = "B6", length = 2)
	public String getB6() {
		return b6;
	}

	public void setB6(String b6) {
		this.b6 = b6;
	}

	@Column(name = "B7", length = 2)
	public String getB7() {
		return b7;
	}

	public void setB7(String b7) {
		this.b7 = b7;
	}

	@Column(name = "B8", length = 2)
	public String getB8() {
		return b8;
	}

	public void setB8(String b8) {
		this.b8 = b8;
	}

	@Column(name = "B9", length = 2)
	public String getB9() {
		return b9;
	}

	public void setB9(String b9) {
		this.b9 = b9;
	}

	@Column(name = "B10", length = 2)
	public String getB10() {
		return b10;
	}

	public void setB10(String b10) {
		this.b10 = b10;
	}

	@Column(name = "B11", length = 2)
	public String getB11() {
		return b11;
	}

	public void setB11(String b11) {
		this.b11 = b11;
	}

	@Column(name = "B12", length = 2)
	public String getB12() {
		return b12;
	}

	public void setB12(String b12) {
		this.b12 = b12;
	}

	@Column(name = "B13", length = 2)
	public String getB13() {
		return b13;
	}

	public void setB13(String b13) {
		this.b13 = b13;
	}

	@Column(name = "B14", length = 2)
	public String getB14() {
		return b14;
	}

	public void setB14(String b14) {
		this.b14 = b14;
	}

	@Column(name = "B15", length = 2)
	public String getB15() {
		return b15;
	}

	public void setB15(String b15) {
		this.b15 = b15;
	}
}
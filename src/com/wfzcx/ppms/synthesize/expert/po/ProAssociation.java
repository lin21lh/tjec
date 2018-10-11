package com.wfzcx.ppms.synthesize.expert.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ProAssociation entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRO_ASSOCIATION")
@GenericGenerator(name = "SEQ_PRO_ASSOCIATION", strategy = "native",parameters={@Parameter(name = "sequence", value = "SEQ_PRO_ASSOCIATION") })
public class ProAssociation implements java.io.Serializable {

	// Fields

	private Long associationid;
	private String majorkey;
	private String foreignkey;
	private String elementcode;
	private String remark;

	// Constructors

	/** default constructor */
	public ProAssociation() {
	}

	/** minimal constructor */
	public ProAssociation(Long associationid, String majorkey,
			String foreignkey, String elementcode) {
		this.associationid = associationid;
		this.majorkey = majorkey;
		this.foreignkey = foreignkey;
		this.elementcode = elementcode;
	}

	/** full constructor */
	public ProAssociation(Long associationid, String majorkey,
			String foreignkey, String elementcode, String remark) {
		this.associationid = associationid;
		this.majorkey = majorkey;
		this.foreignkey = foreignkey;
		this.elementcode = elementcode;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRO_ASSOCIATION")
	@Column(name = "ASSOCIATIONID", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getAssociationid() {
		return this.associationid;
	}

	public void setAssociationid(Long associationid) {
		this.associationid = associationid;
	}

	@Column(name = "MAJORKEY", nullable = false, length = 40)
	public String getMajorkey() {
		return this.majorkey;
	}

	public void setMajorkey(String majorkey) {
		this.majorkey = majorkey;
	}

	@Column(name = "FOREIGNKEY", nullable = false, length = 40)
	public String getForeignkey() {
		return this.foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	@Column(name = "ELEMENTCODE", nullable = false, length = 20)
	public String getElementcode() {
		return this.elementcode;
	}

	public void setElementcode(String elementcode) {
		this.elementcode = elementcode;
	}

	@Column(name = "REMARK", length = 40)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
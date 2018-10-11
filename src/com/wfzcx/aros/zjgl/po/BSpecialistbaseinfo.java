package com.wfzcx.aros.zjgl.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * BSpecialistbaseinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "B_SPECIALISTBASEINFO")
public class BSpecialistbaseinfo implements java.io.Serializable {

	// Fields

	private String speid;
	private String spename;
	private String title;
	private String post;
	private String degree;
	private String phone;
	private String address;
	private String postcode;
	private String workunits;
	private String spedesc;
	private String intro;
	private Long userid;
	private String membertype;
	

	/** default constructor */
	public BSpecialistbaseinfo() {
	}

	/** minimal constructor */
	public BSpecialistbaseinfo(String speid) {
		this.speid = speid;
	}

	/** full constructor */
	public BSpecialistbaseinfo(String speid, String spename, String title,
			String post, String degree, String phone, String address,
			String postcode, String workunits, String spedesc, String intro, Long userid,String membertype) {
		this.speid = speid;
		this.spename = spename;
		this.title = title;
		this.post = post;
		this.degree = degree;
		this.phone = phone;
		this.address = address;
		this.postcode = postcode;
		this.workunits = workunits;
		this.spedesc = spedesc;
		this.intro = intro;
		this.userid = userid;
		this.membertype=membertype;
	}

	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "SPEID")
	public String getSpeid() {
		return this.speid;
	}

	public void setSpeid(String speid) {
		this.speid = speid;
	}

	@Column(name = "SPENAME", length = 80)
	public String getSpename() {
		return this.spename;
	}

	public void setSpename(String spename) {
		this.spename = spename;
	}

	@Column(name = "TITLE", length = 2)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "POST", length = 2)
	public String getPost() {
		return this.post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	@Column(name = "DEGREE", length = 2)
	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	@Column(name = "PHONE", length = 11)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "ADDRESS", length = 100)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "POSTCODE", length = 6)
	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "WORKUNITS", length = 200)
	public String getWorkunits() {
		return this.workunits;
	}

	public void setWorkunits(String workunits) {
		this.workunits = workunits;
	}

	@Column(name = "SPEDESC", length = 1000)
	public String getSpedesc() {
		return this.spedesc;
	}

	public void setSpedesc(String spedesc) {
		this.spedesc = spedesc;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	@Column(name = "MEMBERTYPE", length = 30)
	public String getMembertype() {
		return membertype;
	}

	public void setMembertype(String membertype) {
		this.membertype = membertype;
	}

	
	
}
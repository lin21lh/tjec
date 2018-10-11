package com.wfzcx.aros.lxfs.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "B_CONTACTBASEINFO")
public class LxfsInfo {
	private String id;
	private String deptname;
	private String address;
	private String person;
	private String phone;
	private String xcoor;
	private String ycoor;
	
	/** default constructor */
	public LxfsInfo(){
		
	}
	/** minimal constructor */
    public LxfsInfo(String id ){
		this.id=id;
	}
    /** full constructor */
    public LxfsInfo(String id,String deptname,String address,String person,String phone,String xcoor,String ycoor){
    	this.id=id;
    	this.deptname=deptname;
    	this.address=address;
    	this.person=person;
    	this.phone=phone;
    	this.xcoor=xcoor;
    	this.ycoor=ycoor;
    }
    
    
    @Id
   	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "DEPTNAME",length=200)
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	
	@Column(name = "ADDRESS",length=200)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "PERSON",length=100)
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	
	@Column(name = "PHONE",length=30)
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "XCOOR",length=16)
	public String getXcoor() {
		return xcoor;
	}
	public void setXcoor(String xcoor) {
		this.xcoor = xcoor;
	}
	
	@Column(name = "YCOOR",length=16)
	public String getYcoor() {
		return ycoor;
	}
	public void setYcoor(String ycoor) {
		this.ycoor = ycoor;
	}
    
	
	
}
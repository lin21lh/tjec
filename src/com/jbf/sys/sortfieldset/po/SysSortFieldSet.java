package com.jbf.sys.sortfieldset.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "SYS_SORTFIELDSET")
public class SysSortFieldSet {

	private Long sortid;
	private Long resourceid;
	private String resourcename;
	private Long roleid;
	private String rolename;
	private Long userid;
	private String username;
	private Integer scrareano;
	private Integer orderno;
	private String sortname;
	private String sortstr;
	private Integer status;
	private String remark;
	
	@Id
	@SequenceGenerator(name = "oracle_seq", sequenceName = "SEQ_SORTFIELDSET")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oracle_seq")
	@Column(name = "SORTID", nullable = false, scale = 0)
	public Long getSortid() {
		return sortid;
	}
	
	public void setSortid(Long sortid) {
		this.sortid = sortid;
	}
	
	@Column(name = "RESOURCEID", nullable = false, scale = 0)
	public Long getResourceid() {
		return resourceid;
	}
	
	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}
	
	@Formula("(select t.name from sys_resource t where t.resourceid=resourceid)")
	public String getResourcename() {
		return resourcename;
	}
	
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	
	@Column(name = "ROLEID", nullable = true, scale = 0)
	public Long getRoleid() {
		return roleid;
	}
	
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}
	
	@Formula("(select t.rolecode||'-'||t.rolename from sys_role t where t.roleid=roleid)")
	public String getRolename() {
		return rolename;
	}
	
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	
	@Column(name = "USERID", nullable = true, scale = 0)
	public Long getUserid() {
		return userid;
	}
	
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	
	@Formula("(select t.usercode||'-'||t.username from sys_user t where t.userid=userid)")
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "SCRAREANO", nullable = false, scale = 0)
	public Integer getScrareano() {
		return scrareano;
	}
	
	public void setScrareano(Integer scrareano) {
		this.scrareano = scrareano;
	}
	
	@Column(name = "ORDERNO", nullable = false, scale = 0)
	public Integer getOrderno() {
		return orderno;
	}
	
	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}
	
	@Column(name = "SORTNAME", nullable = false, length = 200)
	public String getSortname() {
		return sortname;
	}
	
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}
	
	@Column(name = "SORTSTR", nullable = false, length = 200)
	public String getSortstr() {
		return sortstr;
	}
	
	public void setSortstr(String sortstr) {
		this.sortstr = sortstr;
	}
	
	@Column(name = "STATUS", nullable = false, scale = 0)
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "REMARK", nullable = false, length = 100)
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
}

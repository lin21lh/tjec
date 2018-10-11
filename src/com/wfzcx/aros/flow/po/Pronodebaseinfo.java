package com.wfzcx.aros.flow.po;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @ClassName: Pronodebaseinfo
 * @Description: 流程节点配置信息实体bean
 * @author MyEclipse Persistence Tools
 * @date 2016年8月11日 下午3:13:39
 * @version V1.0
 */
@Entity
@Table(name = "PUB_PRONODEBASEINFO" )
public class Pronodebaseinfo implements java.io.Serializable {

	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	
	// Fields
	
	private String id;				//序号
	private String proname;			//流程名称
	private String protype;			//流程类型
	private String nodename;		//节点名称
	private BigDecimal nodeid;		//节点编号
	private BigDecimal roleid;		//角色id
	private String menuurl;			//菜单路径
	private String rolename;		//角色名称
	private String optional;		//是否可选

	// Constructors

	/** default constructor */
	public Pronodebaseinfo() {
	}

	/** minimal constructor */
	public Pronodebaseinfo(String id) {
		this.id = id;
	}

	/** full constructor */
	public Pronodebaseinfo(String id, String proname, String protype,
			String nodename, BigDecimal nodeid, BigDecimal roleid, String menuurl,
			String rolename, String optional) {
		this.id = id;
		this.proname = proname;
		this.protype = protype;
		this.nodename = nodename;
		this.nodeid = nodeid;
		this.roleid = roleid;
		this.menuurl = menuurl;
		this.rolename = rolename;
		this.optional = optional;
	}

	// Property accessors
	@Id
	@GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PRONAME", length = 100)
	public String getProname() {
		return this.proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	@Column(name = "PROTYPE", length = 2)
	public String getProtype() {
		return this.protype;
	}

	public void setProtype(String protype) {
		this.protype = protype;
	}

	@Column(name = "NODENAME", length = 100)
	public String getNodename() {
		return this.nodename;
	}

	public void setNodename(String nodename) {
		this.nodename = nodename;
	}

	@Column(name = "NODEID", precision = 22, scale = 0)
	public BigDecimal getNodeid() {
		return this.nodeid;
	}

	public void setNodeid(BigDecimal nodeid) {
		this.nodeid = nodeid;
	}

	@Column(name = "ROLEID", precision = 22, scale = 0)
	public BigDecimal getRoleid() {
		return this.roleid;
	}

	public void setRoleid(BigDecimal roleid) {
		this.roleid = roleid;
	}

	@Column(name = "MENUURL", length = 200)
	public String getMenuurl() {
		return menuurl;
	}

	public void setMenuurl(String menuurl) {
		this.menuurl = menuurl;
	}

	@Column(name = "ROLENAME", length = 100)
	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Column(name = "OPTIONAL", length = 2)
	public String getOptional() {
		return optional;
	}

	public void setOptional(String optional) {
		this.optional = optional;
	}

}
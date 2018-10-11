package com.wfzcx.aros.dxal.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
@Entity
@Table(name = "B_CLASSICCASEBASEINFO")
public class Dxalinfo implements java.io.Serializable{
	private String id;
	private String casetitle;
	private String casedesc;
	private String ifpublish;
	private String startdate;
	private String enddate;
	private String operator;
	private String opttime;
	/** default constructor */
	public Dxalinfo(){
		
	}
	/** minimal constructor */
    public Dxalinfo(String id ){
		this.id=id;
	}
    /** full constructor */
    public Dxalinfo(String id,String casetitle,String casedesc,String ifpublish,String startdate,String enddate,String operator,String opttime){
    	this.id=id;
    	this.casetitle=casetitle;
    	this.casedesc=casedesc;
    	this.ifpublish=ifpublish;
    	this.startdate=startdate;
    	this.enddate=enddate;
    	this.operator=operator;
    	this.opttime=opttime;
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
	@Column(name = "CASETITLE",length=200)
	public String getCasetitle() {
		return casetitle;
	}
	public void setCasetitle(String casetitle) {
		this.casetitle = casetitle;
	}
	
	@Column(name = "CASEDESC",length=2000)
	public String getCasedesc() {
		return casedesc;
	}
	public void setCasedesc(String casedesc) {
		this.casedesc = casedesc;
	}
	
	@Column(name = "IFPUBLISH",length=1)
	public String getIfpublish() {
		return ifpublish;
	}
	public void setIfpublish(String ifpublish) {
		this.ifpublish = ifpublish;
	}
	
	@Column(name = "STARTDATE",length=30)
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	
	@Column(name = "ENDDATE",length=30)
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	
	@Column(name = "OPERATOR",length=30)
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	@Column(name = "OPTTIME",length=30)
	public String getOpttime() {
		return opttime;
	}
	public void setOpttime(String opttime) {
		this.opttime = opttime;
	}

}

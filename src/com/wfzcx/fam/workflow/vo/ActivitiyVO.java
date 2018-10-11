package com.wfzcx.fam.workflow.vo;

public class ActivitiyVO {

	private String activitiyid;
	private String activitiyname;
	private String type;
	private Integer version;
	private String keyname;
	
	public ActivitiyVO() {
		
	}
	
	public String getActivitiyid() {
		return activitiyid;
	}
	
	public void setActivitiyid(String activitiyid) {
		this.activitiyid = activitiyid;
	}
	
	public String getActivitiyname() {
		return activitiyname;
	}
	
	public void setActivitiyname(String activitiyname) {
		this.activitiyname = activitiyname;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getVersion() {
		return version;
	}
	
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public String getKeyname() {
		return keyname;
	}
	
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
}

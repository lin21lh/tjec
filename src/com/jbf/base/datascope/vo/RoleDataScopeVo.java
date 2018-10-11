package com.jbf.base.datascope.vo;

import com.jbf.common.vo.TreeVo;

public class RoleDataScopeVo extends TreeVo {
	
	private Long scopemainid;

	private String scopemainname;
	
	public Long getScopemainid() {
		return scopemainid;
	}
	
	public void setScopemainid(Long scopemainid) {
		this.scopemainid = scopemainid;
	}
	
	public String getScopemainname() {
		return scopemainname;
	}
	
	public void setScopemainname(String scopemainname) {
		this.scopemainname = scopemainname;
	}
}

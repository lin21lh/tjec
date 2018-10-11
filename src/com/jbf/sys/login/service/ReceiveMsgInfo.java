package com.jbf.sys.login.service;

import java.util.Map;

public class ReceiveMsgInfo {

	private Boolean isSuccess=true;
	private String errCode="";
	private String errDesc="";
	private Map attributeNodeMap=null;
	public Boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrDesc() {
		return errDesc;
	}
	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
	public Map getAttributeNodeMap() {
		return attributeNodeMap;
	}
	public void setAttributeNodeMap(Map attributeNodeMap) {
		this.attributeNodeMap = attributeNodeMap;
	}
	
	
	
}

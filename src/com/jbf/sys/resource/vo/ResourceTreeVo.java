/************************************************************
 * 类名：ResourceTreeVo.java
 *
 * 类别：VO
 * 功能：资源树vo
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.resource.vo;

import com.jbf.common.vo.TreeVo;

public class ResourceTreeVo extends TreeVo {

	private String code;
	private String webpath;
	private String iconCls;
	private String state;
	private Integer resorder;
	private byte status;
	private String wfkey;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWebpath() {
		return webpath;
	}

	public void setWebpath(String webpath) {
		this.webpath = webpath;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getResorder() {
		return resorder;
	}

	public void setResorder(Integer resorder) {
		this.resorder = resorder;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	public String getWfkey() {
		return wfkey;
	}
	
	public void setWfkey(String wfkey) {
		this.wfkey = wfkey;
	}
}

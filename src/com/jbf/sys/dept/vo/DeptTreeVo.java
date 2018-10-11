
package com.jbf.sys.dept.vo;

import com.jbf.common.vo.TreeVo;

/************************************************************
 * 类名：DeptTreeVo.java
 *
 * 类别：VO
 * 功能：机构树VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-09-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
public class DeptTreeVo extends TreeVo{

	private String code;
	
	private String status;
	
	public DeptTreeVo() {}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}

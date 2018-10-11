/************************************************************
 * 类名：DicTreeVo.java
 *
 * 类别：VO
 * 功能：数据项值集TreeVO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

import com.jbf.common.vo.TreeVo;

public class DicTreeVo extends TreeVo {

	private String code;
	
	private String name;
	
	private String isChecked; //是否选中 选中1；未选中0
	
	public DicTreeVo() {
		
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIsChecked() {
		return isChecked;
	}
	
	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}
}

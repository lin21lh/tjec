/************************************************************
 * 类名：TreeVo.java
 *
 * 类别：VO
 * 功能：树VO
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-09-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.vo;

import java.util.List;

public class TreeVo {
	private String id;
	private String text;
	private String pid;
	private Boolean checked;
	private Integer levelno;
	private Boolean isLeaf;
	private String state;

	private List<? extends TreeVo> children;

	public TreeVo() {

	}

	public TreeVo(String id, String text, String pid) {
		this.id = id;
		this.text = text;
		this.pid = pid;
	}

	public TreeVo(String id, String text, String pid, Boolean checked) {
		this.id = id;
		this.text = text;
		this.pid = pid;
		this.checked = checked;
	}

	public TreeVo(String id, String text, String pid, Boolean checked,
			Integer levelno, Boolean isLeaf) {
		this.id = id;
		this.text = text;
		this.pid = pid;
		this.checked = checked;
		this.levelno = levelno;
		this.isLeaf = isLeaf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public Integer getLevelno() {
		return levelno;
	}

	public void setLevelno(Integer levelno) {
		this.levelno = levelno;
	}

	public Boolean getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public List<? extends TreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<? extends TreeVo> children) {
		this.children = children;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	

}

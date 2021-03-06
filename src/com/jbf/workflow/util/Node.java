/************************************************************
 * 类名：Node
 * 
 * 类别：模型类
 * 功能：后台由jbdl配置文件生成流程图图片的节点模型类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.util;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class Node {
	private String name;
	private String type;
	private Rectangle rectangle;
	private List<Transition> transitions = new ArrayList<Transition>();
	private String description;
	public Node(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Node(String name, String type, int x, int y, int w, int h) {
		this.name = name;
		this.type = type;
		this.rectangle = new Rectangle(x, y, w, h);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addTransition(Transition transition) {
		transitions.add(transition);
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public int getX() {
		return rectangle.x;
	}

	public int getY() {
		return rectangle.y;
	}

	public int getCenterX() {
		return (int) rectangle.getCenterX();
	}

	public int getCenterY() {
		return (int) rectangle.getCenterY();
	}

	public int getWitdth() {
		return rectangle.width;
	}

	public int getHeight() {
		return rectangle.height;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
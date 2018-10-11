/************************************************************
 * 类名：Transition
 * 
 * 类别：模型类
 * 功能：后台由jbdl配置文件生成流程图图片的迁移线模型类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Transition {
	private Point labelPosition;
	private List<Point> lineTrace = new ArrayList<Point>();
	private String label;
	private String to;

	public Transition(String label, String to) {
		this.label = label;
		this.to = to;
	}

	public Point getLabelPosition() {
		return labelPosition;
	}

	public void setLabelPosition(Point labelPosition) {
		this.labelPosition = labelPosition;
	}

	public List<Point> getLineTrace() {
		return lineTrace;
	}

	public void setLineTrace(List<Point> lineTrace) {
		this.lineTrace = lineTrace;
	}

	public void addLineTrace(Point lineTrace) {
		if (lineTrace != null) {
			this.lineTrace.add(lineTrace);
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}

/************************************************************
 * 类名：JpdlModel
 * 
 * 类别：模型类
 * 功能：后台由jbdl配置文件生成流程图图片的模型类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.util;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class JpdlModel {
	private Map<String, Node> nodes = new LinkedHashMap<String, Node>();
	public static final int RECT_OFFSET_X = -7;
	public static final int RECT_OFFSET_Y = -8;
	public static final int DEFAULT_PIC_SIZE = 48;

	private final static Map<String, Object> nodeInfos = new HashMap<String, Object>();
	static {
		nodeInfos.put("start", "start_event_empty.png");
		nodeInfos.put("end", "end_event_terminate.png");
		nodeInfos.put("end-cancel", "end_event_cancel.png");
		nodeInfos.put("end-error", "end_event_error.png");
		nodeInfos.put("decision", "gateway_exclusive.png");
		nodeInfos.put("fork", "gateway_parallel.png");
		nodeInfos.put("join", "gateway_parallel.png");
		nodeInfos.put("state", null);
		nodeInfos.put("hql", null);
		nodeInfos.put("sql", null);
		nodeInfos.put("java", null);
		nodeInfos.put("script", null);
		nodeInfos.put("task", null);
		nodeInfos.put("sub-process", null);
		nodeInfos.put("custom", null);
	}

	public JpdlModel(InputStream is) throws Exception {
		this(new SAXReader().read(is).getRootElement());

	}

	public JpdlModel(Reader reader) throws Exception {
		this(new SAXReader().read(reader).getRootElement());

	}

	@SuppressWarnings("unchecked")
	private JpdlModel(Element rootEl) throws Exception {
		for (Element el : (List<Element>) rootEl.elements()) {
			String type = el.getQName().getName();
			if (!nodeInfos.containsKey(type)) { // 不是可展示的节点
				continue;
			}
			String name = null;
			if (el.attribute("name") != null) {
				name = el.attributeValue("name");
			}
			String[] location = el.attributeValue("g").split(",");
			int x = Integer.parseInt(location[0]);
			int y = Integer.parseInt(location[1]);
			int w = Integer.parseInt(location[2]);
			int h = Integer.parseInt(location[3]);

			if (nodeInfos.get(type) != null) {
				w = DEFAULT_PIC_SIZE;
				h = DEFAULT_PIC_SIZE;
			} else {
				x -= RECT_OFFSET_X;
				y -= RECT_OFFSET_Y;
				w += (RECT_OFFSET_X + RECT_OFFSET_X);
				h += (RECT_OFFSET_Y + RECT_OFFSET_Y);
			}
			Node node = new Node(name, type, x, y, w, h);

			// 写入描述
			String des = "未知";
			Element description = el.element("description");
			if (description != null && description.getText() != null) {
				des = description.getText();
			} else if (el.attribute("name") != null) {
				des = el.attributeValue("name");
			}
			node.setDescription(des);
			
			parserTransition(node, el);
			nodes.put(name, node);
		}
	}

	@SuppressWarnings("unchecked")
	private void parserTransition(Node node, Element nodeEl) {
		for (Element el : (List<Element>) nodeEl.elements("transition")) {
			String label = el.attributeValue("name");
			String to = el.attributeValue("to");
			Transition transition = new Transition(label, to);
			String g = el.attributeValue("g");
			if (g != null && g.length() > 0) {
				if (g.indexOf(":") < 0) {
					transition.setLabelPosition(getPoint(g));
				} else {
					String[] p = g.split(":");
					transition.setLabelPosition(getPoint(p[1]));
					String[] lines = p[0].split(";");
					for (String line : lines) {
						transition.addLineTrace(getPoint(line));
					}
				}
			}
			node.addTransition(transition);
		}
	}

	private Point getPoint(String exp) {
		if (exp == null || exp.length() == 0) {
			return null;
		}
		String[] p = exp.split(",");
		return new Point(Integer.valueOf(p[0]), Integer.valueOf(p[1]));
	}

	public Map<String, Node> getNodes() {
		return nodes;
	}

	public static Map<String, Object> getNodeInfos() {
		return nodeInfos;
	}

	/**
	 * 由xml文件创建图片文件
	 * 
	 * @param FilePath
	 *            输出路径
	 * @param xml
	 *            jpdl文件內容
	 * @throws Exception
	 */
	public static void createPngFile(String FilePath, String xml)
			throws Exception {
		File output = new File(FilePath);
		StringReader reader = new StringReader(xml);
		JpdlModel jpdlModel = new JpdlModel(reader);
		ImageIO.write(new JpdlModelDrawer().draw(jpdlModel), "png", output);
	}

}

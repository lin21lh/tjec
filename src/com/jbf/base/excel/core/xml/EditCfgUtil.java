/************************************************************
 * 类名：EditCfgUtil.java
 *
 * 类别：工具类
 * 功能：导入配置文件修改维护工具
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.XMLUtil;

public class EditCfgUtil {
	/**
	 * 创建配置文件，输出到文件路径
	 * 
	 * @param map
	 *            配置属性map
	 * @param filename
	 *            文件路径
	 * @throws IOException
	 */
	public static void createCfgFile(Map map, String filename)
			throws IOException {
		Document document = DocumentHelper.createDocument();
		Element excel = document.addElement("Excel");
		excel.addAttribute("classname", getMapValue(map, "classname"));
		excel.addAttribute("dataexception", getMapValue(map, "dataexception"));

		XMLUtil.outputDocumentToFile(document, filename);
	}

	/**
	 * 修改配置文件内容
	 * 
	 * @param map
	 *            配置属性map
	 * @param reader
	 *            源配置内容reader
	 * @param writer
	 *            目标配置内容writer
	 * @throws Exception
	 */
	public static void editExcelVo(Map map, Reader reader, Writer writer)
			throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element excel = document.getRootElement();
		excel.attribute("classname").setValue(getMapValue(map, "classname"));
		excel.attribute("dataexception").setValue(
				getMapValue(map, "dataexception"));

		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 取得map的value属性值
	 * 
	 * @param map
	 *            map对象
	 * @param key
	 *            key值
	 * @return
	 */
	public static String getMapValue(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

	/**
	 * 修改工作表配置
	 * 
	 * @param vo 工作表详情
	 * @param reader 输入
	 * @param writer 输出
	 * @param editflag 修改模式
	 * @param oldid 原工作表id
	 * @throws Exception
	 */
	public static void editSheetVo(SheetVo vo, Reader reader, Writer writer,
			String editflag, String oldid) throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();
		Element sheet = null;
		if (editflag == null || editflag.equals("add")) {
			sheet = root.addElement("Sheet");
			sheet.addAttribute("id", vo.getId());
			sheet.addAttribute("name", vo.getName());
		} else {
			sheet = XMLUtil.getElementByID(root, "Sheet", oldid);
			sheet.attribute("id").setValue(vo.getId());
			sheet.attribute("name").setValue(vo.getName());
		}
		// XMLUtil.outputDocumentToFile(document, "D:\\dell\\aa.xml","UTF-8");
		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 删除sheet
	 * 
	 * @param ids  工作表id列表
	 * @param reader 输入
	 * @param writer 输出
	 * @throws Exception
	 */
	public static void deleteSheet(String[] ids, Reader reader, Writer writer)
			throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();

		for (String sheetId : ids) {
			Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
			if (sheet != null) {
				root.remove(sheet);
			}
		}
		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 修改列表
	 * 
	 * @param sheetid
	 *            工作表id
	 * @param vo
	 *            列表详情
	 * @param reader
	 *            输入
	 * @param writer
	 *            输出
	 * @throws Exception
	 */
	public static void editType(String sheetid, ListVo vo, Reader reader,
			Writer writer) throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetid);
		Element type = null;
		if (vo.getId() == null || vo.getId().equals("")) {
			if (vo.getTypecode().equals("3")) {
				type = sheet.addElement("Form");
			} else {
				type = sheet.addElement("List");
			}

			type.addAttribute("id", CommonUtil.getUUID());
			type.addAttribute("name", vo.getName());
			if (vo.getTypecode().equals("1")) {
				type.addAttribute("startrow", vo.getStartrow());
				type.addAttribute("endrow", vo.getEndrow());
			} else if (vo.getTypecode().equals("2")) {
				type.addAttribute("startcol", vo.getStartcol());
				type.addAttribute("endcol", vo.getEndcol());
			}
			type.addAttribute("tablename", vo.getTablename());
			type.addAttribute("uuidfield", vo.getUuidfield());
			type.addAttribute("poclassname", vo.getPoclassname());
			type.addAttribute("rule", vo.getRule());
			type.addAttribute("ruleinfo", vo.getRuleinfo());
			type.addAttribute("checkrule", vo.getCheckrule());
		} else {
			if (vo.getTypecode().equals("3")) {
				type = XMLUtil.getElementByID(sheet, "Form", vo.getId());
			} else {
				type = XMLUtil.getElementByID(sheet, "List", vo.getId());
			}
			type.attribute("name").setValue(vo.getName());
			if (vo.getTypecode().equals("1")) {
				type.attribute("startrow").setValue(vo.getStartrow());
				type.attribute("endrow").setValue(vo.getEndrow());
			} else if (vo.getTypecode().equals("2")) {
				type.attribute("startcol").setValue(vo.getStartcol());
				type.attribute("endcol").setValue(vo.getEndcol());
			}
			type.attribute("tablename").setValue(vo.getTablename());
			type.attribute("uuidfield").setValue(vo.getUuidfield());

			type.attribute("poclassname").setValue(vo.getPoclassname());
			// type.attribute("rule").setValue(vo.getRule());
			// type.attribute("ruleinfo").setValue(vo.getRuleinfo());
			// type.attribute("checkrule").setValue(vo.getCheckrule());
		}

		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 删除列表
	 * 
	 * @param sheetid
	 *            工作表id
	 * @param ids
	 *            列表id列表
	 * @param reader
	 *            输入
	 * @param writer
	 *            输出
	 * @throws Exception
	 */
	public static void deleteTypes(String sheetid, String[] ids, Reader reader,
			Writer writer) throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetid);

		for (String id : ids) {
			Element type = XMLUtil.getElementByID(sheet, null, id);
			if (type != null) {
				sheet.remove(type);
			}
		}

		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 修改列或单元格配置
	 * 
	 * @param ownersheetid
	 *            工作表id
	 * @param typeid
	 *            列表id
	 * @param vo
	 *            cellvo详情
	 * @param reader
	 *            输入
	 * @param writer
	 *            输出
	 * @param flag
	 *            修改模式
	 * @throws Exception
	 */
	public static void editCell(String ownersheetid, String typeid, CellVo vo,
			Reader reader, Writer writer, String flag) throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", ownersheetid);
		Element lf = XMLUtil.getElementByID(sheet, null, typeid);
		Element type = null;
		if (flag == null || flag.equals("add")) {
			if (vo.getTypecode().equals("3")) {
				type = lf.addElement("Field");
			} else {
				type = lf.addElement("Col");
			}

			type.addElement("sheetid").setText(vo.getSheetid());
			type.addElement("rownum").setText(vo.getRownum());
			type.addElement("colnum").setText(vo.getColnum());
			type.addElement("fieldname").setText(vo.getFieldname());
			type.addElement("text").setText(vo.getText());
			type.addElement("datatype").setText(vo.getDatatype());
			type.addElement("allownull").setText(vo.getAllownull());
		} else {
			List<Element> list = lf.elements();
			String fieldname = "";
			for (Element cell : list) {
				fieldname = cell.elementTextTrim("fieldname");
				if (fieldname == null || !fieldname.equals(vo.getFieldname())) {
					continue;
				}

				setMyElementText(cell, "sheetid", vo.getSheetid());
				setMyElementText(cell, "rownum", vo.getRownum());
				setMyElementText(cell, "colnum", vo.getColnum());
				setMyElementText(cell, "fieldname", vo.getFieldname());
				setMyElementText(cell, "text", vo.getText());
				setMyElementText(cell, "datatype", vo.getDatatype());
				setMyElementText(cell, "allownull", vo.getAllownull());
			}
		}

		XMLUtil.outputDocumentToWriter(document, writer);
	}

	/**
	 * 设置配置文件中元素的文本值
	 * 
	 * @param e
	 *            元素
	 * @param name
	 *            元素的属性名
	 * @param val
	 *            元素的属性值
	 */
	private static void setMyElementText(Element e, String name, String val) {
		if (e.element(name) != null) {
			e.element(name).setText(val);
		}

	}

	/**
	 * 删除单元格
	 * 
	 * @param sheetid
	 * @param typeid
	 *            列表id
	 * @param typecode
	 *            列表code
	 * @param ids
	 *            列id或单元格id
	 * @param reader
	 *            源输入
	 * @param writer
	 *            目标输出
	 * @throws Exception
	 */
	public static void deleteCells(String sheetid, String typeid,
			String typecode, String[] ids, Reader reader, Writer writer)
			throws Exception {
		Document document = XMLUtil.readDocumentFromReader(reader);
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetid);
		Element type = XMLUtil.getElementByID(sheet, null, typeid);

		for (String id : ids) {
			List<Element> list = type.elements();
			String fieldname = "";
			for (Element cell : list) {
				fieldname = cell.elementTextTrim("fieldname");
				if (fieldname != null && fieldname.equals(id)) {
					type.remove(cell);
				}

			}
		}

		XMLUtil.outputDocumentToWriter(document, writer);
	}
}

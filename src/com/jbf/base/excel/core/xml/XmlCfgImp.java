/************************************************************
 * 类名：XmlCfgImp.java
 *
 * 类别：配置接口实现
 * 功能：读取xml配置文件得到配置信息
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.xml;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jbf.base.excel.core.excel.DataExceptionLevel;
import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ExcelVo;
import com.jbf.base.excel.core.vo.FormVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.common.util.XMLUtil;

public class XmlCfgImp implements XmlCfgI {
	Document document = null;

	public XmlCfgImp(String filename) throws Exception {
		File fileCfg = new File(filename);
		if (fileCfg == null) {
			throw new Exception("是空文件");
		}
		document = XMLUtil.readDocumentFromFile(fileCfg, "UTF-8");
	}

	public XmlCfgImp(InputStream is, String encoding) throws Exception {
		document = XMLUtil.readDocumentFromInputStream(is, encoding);
	}

	public XmlCfgImp(Reader reader) throws Exception {
		document = XMLUtil.readDocumentFromReader(reader);
	}

	@Override
	public Map<Integer, CellVo> getListCols(String sheetId) throws Exception {
		return getListCols(sheetId, null);
	}

	@Override
	public Map<Integer, String> getListColFieldMap(String sheetId)
			throws Exception {
		return getListColFieldMap(sheetId, null);
	}

	@Override
	public List<CellVo> getFormFields(String sheetId) {
		return getFormFields(sheetId, null);
	}

	@Override
	public List<CellVo> getFormFields(String sheetId, String formId) {
		List<CellVo> ret = new ArrayList<CellVo>();
		Element eform = getEForm(sheetId, formId);
		if (eform == null) {
			return ret;
		}

		List<Element> fields = eform.elements();
		if (fields == null || fields.isEmpty()) {
			return ret;
		}

		CellVo cell = null;
		String colnum = null;
		for (Element field : fields) {
			cell = new CellVo();
			cell.setSheetid(field.elementTextTrim("sheetid"));
			cell.setRownum(field.elementTextTrim("rownum"));
			colnum = field.elementTextTrim("colnum");
			cell.setColnum(colnum);
			cell.setFieldname(field.elementTextTrim("fieldname"));
			cell.setText(field.elementTextTrim("text"));
			cell.setDatatype(field.elementTextTrim("datatype"));
			cell.setKey(field.elementTextTrim("key"));
			cell.setAllownull(field.elementTextTrim("allownull"));
			cell.setDefaulttype(field.elementTextTrim("defaulttype"));
			cell.setDefaultvalue(field.elementTextTrim("defaultvalue"));

			ret.add(cell);
		}

		return ret;
	}

	/**
	 * 得到元素"Form"
	 * 
	 * @param sheetId
	 * @param formId
	 * @return
	 */
	private Element getEForm(String sheetId, String formId) {
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		Element eform = null;
		if (formId == null || formId.equals("")) {
			eform = sheet.element("Form");
		} else {
			eform = XMLUtil.getElementByID(sheet, "Form", formId);
		}

		return eform;
	}

	@Override
	public Map<Integer, CellVo> getListCols(String sheetId, String listId)
			throws Exception {
		Map<Integer, CellVo> ret = new TreeMap<Integer, CellVo>();
		Element elist = getEList(sheetId, listId);
		if (elist == null) {
			return ret;
		}

		List<Element> cols = elist.elements();
		if (cols == null || cols.isEmpty()) {
			return ret;
		}

		ListVo listVo = getListVo(sheetId, listId);
		CellVo cell = null;
		String sheetid = null;
		String colnum = elist.attributeValue("startcol");
		String rownum = elist.attributeValue("startrow");
		for (Element col : cols) {
			sheetid = col.elementTextTrim("sheetid");
			if (sheetid != null && !sheetid.equals("")) {
				continue;
			}

			cell = new CellVo();
			cell.setFieldname(col.elementTextTrim("fieldname"));
			cell.setText(col.elementTextTrim("text"));
			cell.setDatatype(col.elementTextTrim("datatype"));
			cell.setKey(col.elementTextTrim("key"));
			cell.setAllownull(col.elementTextTrim("allownull"));
			cell.setDefaulttype(col.elementTextTrim("defaulttype"));
			cell.setDefaultvalue(col.elementTextTrim("defaultvalue"));
			if (listVo.isVertlist()) {
				cell.setRownum(col.elementTextTrim("rownum"));
				cell.setColnum(colnum);
				ret.put(Integer.parseInt(col.elementTextTrim("rownum")), cell);
			} else {
				cell.setRownum(rownum);
				cell.setColnum(col.elementTextTrim("colnum"));
				ret.put(Integer.parseInt(col.elementTextTrim("colnum")), cell);
			}

		}

		return ret;
	}

	@Override
	public List<CellVo> getListColsElse(String sheetId, String listId) {
		List<CellVo> ret = new ArrayList<CellVo>();
		Element elist = getEList(sheetId, listId);
		if (elist == null) {
			return ret;
		}

		List<Element> cols = elist.elements();
		if (cols == null || cols.isEmpty()) {
			return ret;
		}

		CellVo cell = null;
		String sheetid = null;
		for (Element col : cols) {
			sheetid = col.elementTextTrim("sheetid");
			if (sheetid == null || sheetid.equals("")) {
				continue;
			}

			cell = new CellVo();
			cell.setSheetid(sheetid);
			cell.setRownum(col.elementTextTrim("rownum"));
			cell.setColnum(col.elementTextTrim("colnum"));
			cell.setFieldname(col.elementTextTrim("fieldname"));
			cell.setText(col.elementTextTrim("text"));
			cell.setDatatype(col.elementTextTrim("datatype"));
			cell.setKey(col.elementTextTrim("key"));
			cell.setAllownull(col.elementTextTrim("allownull"));
			cell.setDefaulttype(col.elementTextTrim("defaulttype"));
			cell.setDefaultvalue(col.elementTextTrim("defaultvalue"));

			ret.add(cell);
		}

		return ret;
	}

	/**
	 * 得到元素"List"
	 * 
	 * @param sheetId
	 * @param listId
	 * @return
	 */
	private Element getEList(String sheetId, String listId) {
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		Element elist = null;
		if (listId == null || listId.equals("")) {
			elist = sheet.element("List");
		} else {
			elist = XMLUtil.getElementByID(sheet, "List", listId);
		}

		return elist;
	}

	@Override
	public Map<Integer, String> getListColFieldMap(String sheetId, String listId)
			throws Exception {
		Map<Integer, String> ret = new TreeMap<Integer, String>();
		Element elist = getEList(sheetId, listId);
		if (elist == null) {
			return ret;
		}
		List<Element> cols = elist.elements();
		if (cols == null || cols.isEmpty()) {
			return ret;
		}

		String colnum = null;
		String sheetid = null;
		for (Element col : cols) {
			sheetid = col.elementTextTrim("sheetid");
			if (sheetid == null || sheetid.equals("")) {
				colnum = col.elementTextTrim("colnum");
				if (colnum == null || colnum.equals("")) {
					throw new Exception("colnum列号为空！");
				}
				if (col.elementTextTrim("fieldname") == null
						|| col.elementTextTrim("fieldname").equals("")) {
					throw new Exception("fieldname字段名为空！");
				}
				ret.put(Integer.parseInt(colnum) - 1,
						col.elementTextTrim("fieldname"));
			}
		}

		return ret;
	}

	@Override
	public ListVo getListVo(String sheetId, String listId) throws Exception {
		ListVo vo = new ListVo();
		Element elist = getEList(sheetId, listId);
		if (elist == null) {
			return vo;
		}

		vo.setId(elist.attributeValue("id"));
		vo.setName(elist.attributeValue("name"));
		vo.setStartrow(elist.attributeValue("startrow"));
		vo.setEndrow(elist.attributeValue("endrow"));
		vo.setStartcol(elist.attributeValue("startcol"));
		vo.setEndcol(elist.attributeValue("endcol"));
		vo.setTablename(elist.attributeValue("tablename"));
		vo.setUuidfield(elist.attributeValue("uuidfield"));
		vo.setRule(elist.attributeValue("rule"));
		vo.setRuleinfo(elist.attributeValue("ruleinfo"));
		vo.setCheckrule(elist.attributeValue("checkrule"));
		vo.setPoclassname(elist.attributeValue("poclassname"));

		if (vo.getStartrow() != null && !vo.getStartrow().equals("")
				&& vo.getStartcol() != null && !vo.getStartcol().equals("")) {
			throw new Exception("startrow、startcol不能同时有值!");
		}

		if ((vo.getStartrow() == null || vo.getStartrow().equals(""))
				&& (vo.getStartcol() == null || vo.getStartcol().equals(""))) {
			throw new Exception("startrow、startcol不能同时为空!");
		}

		if (vo.getStartcol() != null && !vo.getStartcol().equals("")) {
			vo.setVertlist(true);
		}

		return vo;
	}

	@Override
	public FormVo getFormVo(String sheetId, String formId) {
		FormVo vo = new FormVo();
		Element eform = getEForm(sheetId, formId);
		if (eform == null) {
			return vo;
		}

		vo.setId(eform.attributeValue("id"));
		vo.setName(eform.attributeValue("name"));
		vo.setTablename(eform.attributeValue("tablename"));
		vo.setUuidfield(eform.attributeValue("uuidfield"));
		vo.setRule(eform.attributeValue("rule"));
		vo.setRuleinfo(eform.attributeValue("ruleinfo"));
		vo.setCheckrule(eform.attributeValue("checkrule"));

		return vo;
	}

	@Override
	public Map<Integer, String> getListRowFieldMap(String sheetId, String listId) {
		Map<Integer, String> ret = new TreeMap<Integer, String>();
		Element elist = getEList(sheetId, listId);
		if (elist == null) {
			return ret;
		}
		List<Element> cols = elist.elements();
		if (cols == null || cols.isEmpty()) {
			return ret;
		}

		String rownum = null;
		for (Element col : cols) {
			rownum = col.elementTextTrim("rownum");
			ret.put(Integer.parseInt(rownum) - 1,
					col.elementTextTrim("fieldname"));
		}

		return ret;
	}

	@Override
	public Map<Integer, String> getListRowFieldMap(String sheetId) {
		return getListRowFieldMap(sheetId, null);
	}

	@Override
	public List<ListVo> getListVos(String sheetId) {
		List<ListVo> ret = new ArrayList<ListVo>();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		List<Element> list = sheet.elements();
		ListVo vo = null;
		for (Element element : list) {
			if (!element.getName().equals("List")) {
				continue;
			}

			vo = new ListVo();
			vo.setId(element.attributeValue("id"));
			vo.setName(element.attributeValue("name"));
			vo.setStartrow(element.attributeValue("startrow"));
			vo.setEndrow(element.attributeValue("endrow"));
			vo.setStartcol(element.attributeValue("startcol"));
			vo.setEndcol(element.attributeValue("endcol"));
			vo.setTablename(element.attributeValue("tablename"));
			vo.setUuidfield(element.attributeValue("uuidfield"));
			vo.setRule(element.attributeValue("rule"));
			vo.setRuleinfo(element.attributeValue("ruleinfo"));
			vo.setCheckrule(element.attributeValue("checkrule"));
			vo.setPoclassname(element.attributeValue("poclassname"));
			ret.add(vo);
		}

		return ret;
	}

	@Override
	public List<FormVo> getFormVos(String sheetId) {
		List<FormVo> ret = new ArrayList<FormVo>();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		List<Element> list = sheet.elements();
		FormVo vo = null;
		for (Element element : list) {
			if (!element.getName().equals("Form")) {
				continue;
			}

			vo = new FormVo();
			if (element.getName().equals("Form")) {
				vo.setId(element.attributeValue("id"));
				vo.setName(element.attributeValue("name"));
				vo.setTablename(element.attributeValue("tablename"));
				vo.setUuidfield(element.attributeValue("uuidfield"));
				vo.setRule(element.attributeValue("rule"));
				vo.setRuleinfo(element.attributeValue("ruleinfo"));
				vo.setCheckrule(element.attributeValue("checkrule"));
				vo.setPoclassname(element.attributeValue("poclassname"));
			}

			ret.add(vo);
		}

		return ret;
	}

	@Override
	public List<SheetVo> getSheetVos() {
		List<SheetVo> ret = new ArrayList<SheetVo>();
		Element root = document.getRootElement();
		List<Element> list = root.elements();
		SheetVo vo = null;
		for (Element element : list) {
			vo = new SheetVo();
			vo.setId(element.attributeValue("id"));
			vo.setName(element.attributeValue("name"));

			ret.add(vo);
		}

		return ret;
	}

	@Override
	public ExcelVo getExcelVo() {
		ExcelVo ret = new ExcelVo();
		Element root = document.getRootElement();
		ret.setClassname(root.attributeValue("classname"));
		String dataexception = root.attributeValue("dataexception");
		if (dataexception == null || dataexception.equals("")) {
			ret.setDataExceptionLevel(DataExceptionLevel.throwerr);
		} else {
			ret.setDataExceptionLevel(DataExceptionLevel.valueOf(dataexception));
		}

		return ret;
	}

	@Override
	public SheetVo getSheetVo(String sheetId) {
		SheetVo vo = new SheetVo();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		if (sheet != null) {
			vo.setId(sheet.attributeValue("id"));
			vo.setName(sheet.attributeValue("name"));
		}

		return vo;
	}

	@Override
	public List<Map<String, Object>> getSheetChildren(String sheetId) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		List<Element> list = sheet.elements();
		Map<String, Object> map = null;
		for (Element element : list) {
			map = new HashMap<String, Object>();

			map.put("id", element.attributeValue("id"));
			map.put("name", element.attributeValue("name"));
			map.put("startrow", element.attributeValue("startrow"));
			map.put("endrow", element.attributeValue("endrow"));
			map.put("startcol", element.attributeValue("startcol"));
			map.put("endcol", element.attributeValue("endcol"));
			map.put("tablename", element.attributeValue("tablename"));
			map.put("uuidfield", element.attributeValue("uuidfield"));
			map.put("rule", element.elementTextTrim("rule"));
			map.put("ruleinfo", element.elementTextTrim("ruleinfo"));
			map.put("checkrule", element.elementTextTrim("checkrule"));
			map.put("poclassname", element.attributeValue("poclassname"));

			if (map.get("startrow") != null && !map.get("startrow").equals("")) {
				map.put("typecode", "1");
				map.put("typename", "横向列表");
			} else if (map.get("startcol") != null
					&& !map.get("startcol").equals("")) {
				map.put("typecode", "2");
				map.put("typename", "竖向列表");
			} else {// 表单
				map.put("typecode", "3");
				map.put("typename", "表单");
			}

			ret.add(map);
		}

		return ret;
	}

	@Override
	public List<Map<String, Object>> getListFormChildren(String sheetId,
			String typeid, String typecode) {
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		String elementname = "List";// 默认横向列表
		if (typecode != null && typecode.equals("3")) {
			elementname = "Form";// 表单
		}
		Element lf = XMLUtil.getElementByID(sheet, elementname, typeid);
		List<Element> list = lf.elements();
		Map<String, Object> map = null;
		for (Element element : list) {
			map = new HashMap<String, Object>();

			map.put("sheetid", element.elementTextTrim("sheetid"));
			map.put("rownum", element.elementTextTrim("rownum"));
			map.put("colnum", element.elementTextTrim("colnum"));
			map.put("fieldname", element.elementTextTrim("fieldname"));
			map.put("text", element.elementTextTrim("text"));
			map.put("datatype", element.elementTextTrim("datatype"));
			map.put("allownull", element.elementTextTrim("allownull"));
			map.put("rule", element.elementTextTrim("rule"));
			map.put("ruleinfo", element.elementTextTrim("ruleinfo"));
			map.put("checkrule", element.elementTextTrim("checkrule"));

			if (map.get("datatype") == null || map.get("datatype").equals("")
					|| map.get("datatype").equals("string")) {
				map.put("datatypename", "字符型");
			} else {
				map.put("datatypename", "数字型");
			}
			if (map.get("allownull") == null || map.get("allownull").equals("")
					|| map.get("allownull").equals("true")) {
				map.put("allownullname", "允许");
			} else {
				map.put("allownullname", "不允许");
			}
			if (map.get("checkrule") == null || map.get("checkrule").equals("")
					|| map.get("checkrule").equals("true")) {
				map.put("checkrulename", "检查");
			} else {
				map.put("checkrulename", "不检查");
			}

			ret.add(map);
		}

		return ret;
	}

	@Override
	public Map<String, String> getSheetsMap() {
		Map<String, String> map = new HashMap<String, String>();
		Element root = document.getRootElement();
		List<Element> list = root.elements();
		for (Element element : list) {
			map.put(element.attributeValue("id"),
					element.attributeValue("name"));

		}

		return map;
	}

	@Override
	public CellVo getCellVo(String sheetId, String typeId, String fieldname) {
		CellVo cell = new CellVo();
		Element root = document.getRootElement();
		Element sheet = XMLUtil.getElementByID(root, "Sheet", sheetId);
		Element lf = XMLUtil.getElementByID(sheet, null, typeId);

		if (lf == null) {
			return cell;
		}

		List<Element> fields = lf.elements();
		if (fields == null || fields.isEmpty()) {
			return cell;
		}

		String colnum = null;
		for (Element field : fields) {
			String myfieldname = field.elementTextTrim("fieldname");
			if (myfieldname == null || !myfieldname.equals(fieldname)) {
				continue;
			}

			cell.setSheetid(field.elementTextTrim("sheetid"));
			cell.setRownum(field.elementTextTrim("rownum"));
			colnum = field.elementTextTrim("colnum");
			cell.setColnum(colnum);
			cell.setFieldname(myfieldname);
			cell.setText(field.elementTextTrim("text"));
			cell.setDatatype(field.elementTextTrim("datatype"));
			cell.setKey(field.elementTextTrim("key"));
			cell.setAllownull(field.elementTextTrim("allownull"));
			cell.setDefaulttype(field.elementTextTrim("defaulttype"));
			cell.setDefaultvalue(field.elementTextTrim("defaultvalue"));
		}

		return cell;
	}
}

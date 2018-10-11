/************************************************************
 * 类名：DicUICSchemeResolve.java
 *
 * 类别：解析类
 * 功能： XML 解析成DTO对象解析类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.ui.resolve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jbf.base.dic.dto.EditFieldDTO;
import com.jbf.base.dic.dto.EditSchemeDTO;
import com.jbf.base.dic.dto.GridFieldDTO;
import com.jbf.base.dic.dto.GridSchemeDTO;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.XMLUtil;


public class DicUICSchemeResolve {

	/**
	 * text 转成 Document文档
	 * @param text 字符串
	 * @return Document 文档
	 * @throws IOException
	 */
	public Document readDocumentFromStr(String text) throws IOException {
		return XMLUtil.readDocumentFromFileStr(text);
	}

	/**
	 * 获取form表单界面
	 * @param editscheme 数据库form表单字符串
	 * @return EditSchemeDTO
	 * @throws IOException
	 */
	public EditSchemeDTO getEditScheme(String editscheme) throws IOException {
		Document doc = readDocumentFromStr(editscheme);
		Element element = doc.getRootElement().element("layout");

		EditSchemeDTO editSchemeDTO = new EditSchemeDTO();
		editSchemeDTO.setColcount(Integer.valueOf(element.attributeValue("col-count")));
		editSchemeDTO.setLabelcellwidth(Integer.valueOf(element.attributeValue("label-cell-width")));
		editSchemeDTO.setControlcellwidth(Integer.valueOf(element.attributeValue("control-cell-width")));
		Element fieldsElement = element.element("fields");
		editSchemeDTO.setEditfields(getEditFields(fieldsElement));

		return editSchemeDTO;
	}

	/**
	 * 获取form表单维护字段
	 * @param fieldsElement form表单FieldsElement
	 * @return EditFieldDTO集合
	 */
	public List<EditFieldDTO> getEditFields(Element fieldsElement) {

		List<Element> fieldElements = fieldsElement.elements("field");
		List<EditFieldDTO> list = new ArrayList<EditFieldDTO>(
				fieldElements.size());
		EditFieldDTO efDto = null;
		for (Element fieldElement : fieldElements) {
			efDto = new EditFieldDTO();
			efDto.setName(fieldElement.attributeValue("name"));
			String mergedcols = fieldElement.attributeValue("merged-cols");
			if (StringUtil.isNotBlank(mergedcols))
				efDto.setMergedcols(Integer.valueOf(mergedcols));

			String row = fieldElement.attributeValue("row");
			if (StringUtil.isNotBlank(row))
				efDto.setRow(Integer.valueOf(row));

			String col = fieldElement.attributeValue("col");
			if (StringUtil.isNotBlank(col))
				efDto.setCol(Integer.valueOf(col));

			String notnull = fieldElement.attributeValue("not-null");
			if (StringUtil.isNotBlank(notnull))
				efDto.setNotnull(Integer.valueOf(notnull));

			String isunique = fieldElement.attributeValue("is-unique");
			if (StringUtil.isNotBlank(isunique))
				efDto.setIsunique(Integer.valueOf(isunique));

			Element controlElement = fieldElement.element("control");
			efDto.setControlname(controlElement.attributeValue("name"));
			efDto.setDefaultValue(controlElement
					.attributeValue("default-value"));

			list.add(efDto);
		}

		return list;
	}

	/**
	 * 获取grid界面
	 * @param listscheme grid界面字符串
	 * @return GridSchemeDTO
	 * @throws IOException
	 */
	public GridSchemeDTO getGridScheme(String listscheme) throws IOException {

		Document doc = readDocumentFromStr(listscheme);
		Element element = doc.getRootElement().element("grid");
		List<Element> colElements = element.elements("col");
		List<GridFieldDTO> list = new ArrayList<GridFieldDTO>(
				colElements.size());
		GridFieldDTO gridFieldDto = null;
		for (Element colElement : colElements) {
			gridFieldDto = new GridFieldDTO();
			gridFieldDto.setName(colElement.attributeValue("name"));
			gridFieldDto.setWidth(colElement.attributeValue("width"));
			String ishidden = colElement.attributeValue("is-hidden");
			if (StringUtil.isNotBlank(ishidden))
				gridFieldDto.setIshidden(Integer.valueOf(ishidden));

			list.add(gridFieldDto);
		}

		return new GridSchemeDTO(list);
	}
}

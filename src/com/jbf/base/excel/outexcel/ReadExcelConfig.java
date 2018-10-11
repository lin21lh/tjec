/************************************************************
 * 类名：ReadExcelConfig.java
 *
 * 类别：读取XML配置类
 * 功能：读取所配置导出Excel的XML文件
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.outexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jbf.base.excel.core.vo.ExcelCfg;
import com.jbf.base.excel.core.vo.ExcelHeadVo;
import com.jbf.base.excel.core.vo.ExcelSheetVo;
import com.jbf.base.excel.core.vo.ExcelVersion;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;

public class ReadExcelConfig {
	
	SAXReader saxReader = new SAXReader();
	
	/**
	 * 读取XML配置
	 * @param filename
	 * @return
	 */
	public Document readXML(String filename) {
		String path = ReadExcelConfig.class.getResource("").getPath().replaceAll("%20", " ")+ "/cfg/" + filename + ".xml";
		FileInputStream fis = null;
		Document FileDocument = null;
		try {
			fis = new FileInputStream(new File(path));
			FileDocument = saxReader.read(fis);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return FileDocument;
	}
	
	public ExcelCfg readXMLToExcelCfg(String filename, String eversion){
		Document doc=readXML(filename);
		Element excel = doc.getRootElement().element("excel");
		String excelName = excel.attributeValue("excelName");
		if (StringUtil.isBlank(excelName))
			excelName = DateUtil.getCurrentDate();
			
		String excelTemplateFile = excel.attributeValue("excelTemplateFile");
		ExcelVersion excelVersion = ExcelVersion.Excel2003;
		if (StringUtil.isNotBlank(excelTemplateFile)) {
			if (excelTemplateFile.toLowerCase().endsWith(ExcelVersion.Excel2007.getSuffix()))
				excelVersion = ExcelVersion.Excel2007;
		} else {
			if (StringUtil.isNotBlank(eversion) && ExcelVersion.Excel2007.getIndex() == Integer.valueOf(eversion))
				excelVersion = ExcelVersion.Excel2007;
		}

		return new ExcelCfg(excelName, excelVersion, excelTemplateFile, getExcelSheetList(excel));
	}
	
	public List<ExcelSheetVo> getExcelSheetList(Element excel) {
		
		List<Element> sheets = excel.element("sheets").elements();
		List<ExcelSheetVo> excelSheetList = new ArrayList<ExcelSheetVo>(sheets.size());
		ExcelSheetVo esv = null;
		
		for (Element sheet : sheets) {
			
			String sheetName = sheet.attributeValue("sheetName");
			String startrow = sheet.attributeValue("startrow");
			Element bean = sheet.element("bean");
			String beanname =  bean.attributeValue("beanname");
			String method = bean.attributeValue("method");
			Element sql = sheet.element("sql");
			String table = "";
			String scenecode ="";
			String elementcode = "";
			String sqlText = "";
			if (sql != null) {
				table = sql.attributeValue("table");
				scenecode = sql.attributeValue("scenecode");
				elementcode = sql.attributeValue("elementcode");
				sqlText = sql.getText();
			}
			
			
			esv = new ExcelSheetVo(sheetName, scenecode, elementcode, table, getExcelHeadList(sheet), sqlText, beanname, method, StringUtil.isNotBlank(startrow) ? Integer.valueOf(startrow) : 0);
			excelSheetList.add(esv);
		}
		return excelSheetList;
	}
	
	public  List<ExcelHeadVo>  getExcelHeadList(Element sheet){
		List<Element> headers = sheet.element("headers").elements();
		
		ExcelHeadVo vo=null;
		
		List<ExcelHeadVo> excelHeadList=new ArrayList<ExcelHeadVo>(headers.size());
	
		for (Element header : headers) {
			String colname =header.attributeValue("colname");
			String title = header.attributeValue("title");
			String width =header.attributeValue("width");
			String isNumber =header.attributeValue("isNumber");
			String isHidden = header.attributeValue("isHidden");
			excelHeadList.add(new ExcelHeadVo(colname, title, Boolean.valueOf(isNumber), Boolean.valueOf(isHidden), Integer.valueOf(width).intValue()));
		}
		return excelHeadList;
		
	}
}

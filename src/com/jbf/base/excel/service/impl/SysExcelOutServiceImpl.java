/************************************************************
 * 类名：SysExcelOutServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：导出Excel
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.excel.core.excel.WriteExcel2003Data;
import com.jbf.base.excel.core.excel.WriteExcel2007Data;
import com.jbf.base.excel.core.vo.ExcelCfg;
import com.jbf.base.excel.core.vo.ExcelHeadVo;
import com.jbf.base.excel.core.vo.ExcelSheetVo;
import com.jbf.base.excel.core.vo.ExcelVersion;
import com.jbf.base.excel.dao.SysExcelImpLogDao;
import com.jbf.base.excel.outexcel.ReadExcelConfig;
import com.jbf.base.excel.outexcel.exceute.ExcelData;
import com.jbf.base.excel.service.SysExcelOutService;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.JsonUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;

@Scope("prototype")
@Service
public class SysExcelOutServiceImpl implements SysExcelOutService {
	
	@Autowired
	SysExcelImpLogDao excelDao;
	
	WriteExcel2003Data writeExcel2003Data = new WriteExcel2003Data();
	WriteExcel2007Data writeExcel2007Data = new WriteExcel2007Data();
	ReadExcelConfig readCfg = new ReadExcelConfig();
	
	Map<String, ExcelCfg> map = new Hashtable<String, ExcelCfg>();
	
	public Workbook outExcel(String filename, ExcelCfg excelCfg,Map paramMap) throws Exception {
		 List<ExcelSheetVo> excelSheetList = excelCfg.getExcelSheetVoList();
		 Workbook wb = null;
		 
		 Map<String, List> excelDataMap = getExcelData(excelSheetList, paramMap);
		 if (StringUtil.isNotBlank(excelCfg.getExcelTemplateFile())) { //有模板
			 wb = getWorkbookTemplate(filename, excelCfg.getExcelTemplateFile());
			 if (excelCfg.getExcelTemplateFile().toLowerCase().endsWith(ExcelVersion.Excel2003.getSuffix()))
				 writeExcel2003Data.writeExcel2003DataByTemplate((HSSFWorkbook)wb, excelSheetList, excelDataMap);
			 else if (excelCfg.getExcelTemplateFile().toLowerCase().endsWith(ExcelVersion.Excel2007.getSuffix()))
				 writeExcel2007Data.writeExcel2007DataByTemplate((XSSFWorkbook)wb, excelSheetList, excelDataMap);
		 }  else { // 无模板
			 String excelVersion = (String) paramMap.get("excelVersion");
			 if (StringUtil.isBlank(excelVersion))
				 excelVersion = "2003";
			 
			 wb = createWorkbook(filename, Integer.valueOf(excelVersion).intValue());
			 
			 if (Integer.valueOf(excelVersion).intValue() == ExcelVersion.Excel2003.getIndex())
				 writeExcel2003Data.writeExcel2003Data((HSSFWorkbook)wb, excelSheetList, excelDataMap);
			 else
				 writeExcel2007Data.writeExcel2007Data((XSSFWorkbook)wb, excelSheetList, excelDataMap);
		 }
		
		 return wb;
	}
	
	public ExcelCfg getExcelCfg(String filename, String excelVersion) {
		
		ExcelCfg excelCfg = map.get(filename);
		if (excelCfg == null)
			excelCfg = readCfg.readXMLToExcelCfg(filename, excelVersion);
		
		return excelCfg;
	}
	
	public Map<String, List> getExcelData(List<ExcelSheetVo> excelSheetList, Map paramMap) {
		Map excelDataMap = new HashMap();
		for (ExcelSheetVo esv : excelSheetList) {
			ExcelData excelData = null;
			if (StringUtil.isNotBlank(esv.getBeanname())) {
				excelData = (ExcelData)WebContextFactoryUtil.getBean(esv.getBeanname());
				excelDataMap.put(esv.getSheetName(), excelData.getExcelData(paramMap));
			} else if (StringUtil.isNotBlank(esv.getSql())) {
				excelDataMap.put(esv.getSheetName(), excelDao.findMapBySql(esv.getSql()));
			}
			
		}
		
		return excelDataMap;
	}
	
	public Workbook getWorkbookTemplate(String filename, String excelTemplateFile) throws IOException, AppException{
		File file=new File(ReadExcelConfig.class.getResource("").getPath().replaceAll("%20", " ") + "/cfg/" + excelTemplateFile);
		if(!file.exists()){
			throw new IOException(excelTemplateFile+"文件不存在！");
		}
		Workbook wb = null;
		if (file.getName().toLowerCase().endsWith(".xls")) { //Excel 2003
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
			wb = new HSSFWorkbook(fs);
		} else if (file.getName().toLowerCase().endsWith(".xlsx")) { // Excel 2007
			wb = new XSSFWorkbook(new FileInputStream(file));
		} else {
			throw new AppException("outexcel.template.invalid", new String[] {filename, file.getName()});
		}
			
		return wb;
	}
	
	public Workbook createWorkbook(String filename, int excelVersion) throws AppException {
		Workbook wb = null;
		if (ExcelVersion.Excel2003.getIndex() == excelVersion)
			wb = new HSSFWorkbook();
		
		else if (ExcelVersion.Excel2007.getIndex() == excelVersion)
			wb = new XSSFWorkbook();
		else
			throw new AppException("", new String[] {String.valueOf(excelVersion)});
		
		return wb;
	}

	public Workbook outExcelCurrentPage(String title, ExcelVersion excelVersion, String headers, String datas,boolean includeHidden) throws AppException {
		List<ExcelHeadVo> excelHeadList = JsonUtil.createListObjectByJson(headers, ExcelHeadVo.class);
		List<Map> dataList = JsonUtil.createListObjectByJson(datas, Map.class);
		
		List<ExcelSheetVo> excelSheetList = new ArrayList<ExcelSheetVo>();
		ExcelSheetVo excelSheetVo = new ExcelSheetVo();
		
		List<ExcelHeadVo> newExcelHeadList = new ArrayList<ExcelHeadVo>();
		for (ExcelHeadVo excelHeadVo : excelHeadList) {
			if (!includeHidden && excelHeadVo.isHidden()) 
				continue;
			excelHeadVo.setWidth(excelHeadVo.getWidth()/5);
			newExcelHeadList.add(excelHeadVo);
		}
		
		excelSheetVo.setExcelHeadList(newExcelHeadList);
		excelSheetVo.setSheetName(title);
		excelSheetList.add(excelSheetVo);
		Map<String, List> excelDataMap = new HashMap<String, List>();
		excelDataMap.put(title, dataList);
		
		Workbook wb = createWorkbook(title, excelVersion.getIndex());
		
		switch (excelVersion) {
		case Excel2007:
			writeExcel2007Data.writeExcel2007Data((XSSFWorkbook)wb, excelSheetList, excelDataMap);
			break;
		case Excel2003:
			 writeExcel2003Data.writeExcel2003Data((HSSFWorkbook)wb, excelSheetList, excelDataMap);
		default:
			break;
		}
		
		return wb;
	}
}

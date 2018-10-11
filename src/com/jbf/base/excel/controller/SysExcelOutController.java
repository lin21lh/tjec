/************************************************************
 * 类名：SysExcelOutController.java
 *
 * 类别：Controller
 * 功能：提供数据导出功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-1-22  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbf.base.excel.core.vo.ExcelCfg;
import com.jbf.base.excel.core.vo.ExcelVersion;
import com.jbf.base.excel.service.SysExcelOutService;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;

@Controller
@RequestMapping("/base/excel/SysExcelOutController")
public class SysExcelOutController {

	@Autowired
	SysExcelOutService excelOutService;
	
	/**
	 * 导出Excel
	 * @param request
	 * @param response
	 */
	@RequestMapping("/outExcel.do")
	public void outExcel(HttpServletRequest request, HttpServletResponse response) {
		String filename=request.getParameter("filename");
		String excelVersion = request.getParameter("excelVersion");
		ExcelCfg excelCfg = excelOutService.getExcelCfg(filename, excelVersion);
		String excelFile = excelCfg.getExcelName() + "." + excelCfg.getExcelVersion().getSuffix();
		try {
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment;filename=\"" + excelFile + "\"");
			Map paramMap = ControllerUtil.getRequestParameterMap(request);
			Workbook wb = excelOutService.outExcel(filename, excelCfg, paramMap);
	        OutputStream ouputStream = response.getOutputStream(); 
	        wb.write(ouputStream);    
	        ouputStream.flush();
	        ouputStream.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页导出Excel
	 * @param request
	 * @param response
	 */
	@RequestMapping("/outExcelCurrentPage.do")
	public void outExcelCurrentPage(HttpServletRequest request, HttpServletResponse response) {
		String headers = request.getParameter("headers");
		String datas = request.getParameter("datas");
		String includeHidden = request.getParameter("includeHidden");
		String title = request.getParameter("title");
		String excelVersion = request.getParameter("excelVersion");
		
		if (StringUtil.isBlank(excelVersion))
			excelVersion = "2003";
		
		String excelFile = title + "." + ExcelVersion.getByIndex(Integer.valueOf(excelVersion).intValue()).getSuffix();
		try {
			//response.setContentType("application/vnd.ms-excel");
			//response.addHeader("Content-Disposition", "attachment;filename=\"" + excelFile + "\"");
			
			response.setCharacterEncoding("GBK");
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment;  filename=" + new String(excelFile.getBytes(), "ISO8859-1"));
			
			Map paramMap = ControllerUtil.getRequestParameterMap(request);
			Workbook wb = excelOutService.outExcelCurrentPage(title, ExcelVersion.getByIndex(Integer.valueOf(excelVersion).intValue()), headers, datas, Boolean.valueOf(includeHidden));
	        OutputStream ouputStream = response.getOutputStream(); 
	        wb.write(ouputStream);    
	        ouputStream.flush();
	        ouputStream.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

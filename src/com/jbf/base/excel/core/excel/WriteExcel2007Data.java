/************************************************************
 * 类名：WriteExcel2007Data.java
 *
 * 类别：工具类
 * 功能：提供Excel数据写出，针对2007及以后版本(.xlsx)
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-1-22  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jbf.base.excel.core.vo.ExcelHeadVo;
import com.jbf.base.excel.core.vo.ExcelSheetVo;

public class WriteExcel2007Data {

	/**
	 * 写出数据到Excel2007工作簿
	 * @param wb 工作簿
	 * @param excelSheetList 配置sheet集合
	 * @param excelDataMap 需导出的数据
	 */
	public void writeExcel2007Data(XSSFWorkbook wb, List<ExcelSheetVo> excelSheetList, Map<String, List> excelDataMap) {
		XSSFSheet sheet = null;
		List excelDataList = null;
		for (ExcelSheetVo esv : excelSheetList) {
			sheet = wb.createSheet(esv.getSheetName());

			createSheetHead(esv.getExcelHeadList(), wb, sheet);
			excelDataList = excelDataMap.get(esv.getSheetName());
			try {
				fillSheetData(wb, esv, sheet, excelDataList);
	
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 写出数据到Excel2007工作簿模板中
	 * @param wb 工作簿
	 * @param excelSheetList 配置sheet集合
	 * @param excelDataMap 需导出的数据
	 */
	public void writeExcel2007DataByTemplate(XSSFWorkbook wb, List<ExcelSheetVo> excelSheetList, Map<String, List> excelDataMap) {
		XSSFSheet sheet = null;
		List excelDataList = null;
		for (ExcelSheetVo esv : excelSheetList) {
			sheet = wb.getSheet(esv.getSheetName());
			excelDataList = excelDataMap.get(esv.getSheetName());
			try {
				fillSheetDataByTemplate(wb, esv, sheet, excelDataList);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	/**
	 * 填充数据到模板
	 * @param wb 工作簿
	 * @param esv
	 * @param sheet
	 * @param excelDataList
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void fillSheetDataByTemplate(XSSFWorkbook wb, ExcelSheetVo esv, XSSFSheet sheet, List excelDataList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<ExcelHeadVo> excelHeadList = esv.getExcelHeadList();
		if (excelDataList == null || excelDataList.size() == 0)
			return ;
		Object obj = excelDataList.get(0);
		boolean isMap = false;
		if (obj instanceof Map)
			isMap = true;
		
		Map map =  null;
		XSSFRow row = null;
		
		ExcelHeadVo excelHeadVo = null;
		for (int i=0; i<excelDataList.size(); i++) {
			obj = excelDataList.get(i);
			if (isMap)
				map = (Map)obj;
			
			row = sheet.getRow(esv.getStartrow() + i);
			if (row == null) {
				row = sheet.createRow(esv.getStartrow() + i);
				copyRow(wb, sheet.getRow(esv.getStartrow()), row);
			}
				
			XSSFCell cell = null;
			Object value = null;
			for (int j=0; j<excelHeadList.size(); j++) {
				excelHeadVo = excelHeadList.get(j);
				cell = row.getCell(j); //创建单元格
				if (cell == null)
					cell = row.createCell(j);
				if (isMap) 
					value = map.get(excelHeadVo.getColname());
				else 
					value = BeanUtils.getProperty(obj, excelHeadVo.getColname());
				
				if (excelHeadVo.isNumber()) {
					cell.setCellValue(value != null ? new BigDecimal(value.toString()).doubleValue() : (double)0);
					
					cell.setCellFormula("#,##0.00");
				} else {
					cell.setCellValue(value != null && value.toString().length() > 0 ? value.toString() : "");
				}
			}
		}
	}
	
	/**
	 * 填充数据
	 * @param wb
	 * @param esv
	 * @param sheet
	 * @param excelDataList
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void fillSheetData(XSSFWorkbook wb, ExcelSheetVo esv, XSSFSheet sheet, List excelDataList) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<ExcelHeadVo> excelHeadList = esv.getExcelHeadList();
		if (excelDataList == null || excelDataList.size() == 0)
			return ;
		Object obj = excelDataList.get(0);
		boolean isMap = false;
		if (obj instanceof Map)
			isMap = true;
		
		Map map =  null;
		XSSFRow row = null;
		
		XSSFCellStyle style = wb.createCellStyle();
		
		style.setBorderBottom(BorderFormatting.BORDER_THIN);
		style.setBorderLeft(BorderFormatting.BORDER_THIN);
		style.setBorderRight(BorderFormatting.BORDER_THIN);
		style.setBorderTop(BorderFormatting.BORDER_THIN);
		
		ExcelHeadVo excelHeadVo = null;
		for (int i=0; i<excelDataList.size(); i++) {
			obj = excelDataList.get(i);
			if (isMap)
				map = (Map)obj;
			
			row = sheet.createRow(i+1);
			XSSFCell cell = null;
			Object value = null;
			for (int j=0; j<excelHeadList.size(); j++) {
				excelHeadVo = excelHeadList.get(j);
				cell = row.createCell(j); //创建单元格
				if (isMap) 
					value = map.get(excelHeadVo.getColname());
				else 
					value = BeanUtils.getProperty(obj, excelHeadVo.getColname());
				
				if (excelHeadVo.isNumber()) {
					cell.setCellValue(value != null ? new BigDecimal(value.toString()).doubleValue() : (double)0);
					
					cell.setCellFormula("#,##0.00");
				} else {
					cell.setCellValue(value != null && value.toString().length() > 0 ? value.toString() : "");
				}
					
				cell.setCellStyle(style); //设置列的样式
			}
		}
	}
	
	/**
	 * 创建单元格
	 * @param excelHeadList
	 * @param wb
	 * @param sheet
	 */
	public void createSheetHead(List<ExcelHeadVo> excelHeadList, XSSFWorkbook wb, XSSFSheet sheet) {
		
		XSSFRow row = sheet.createRow(0);
		XSSFCellStyle style = wb.createCellStyle();
		
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
		style.setBorderTop(BorderFormatting.BORDER_THIN);
		style.setBorderBottom(BorderFormatting.BORDER_THIN);
		style.setBorderLeft(BorderFormatting.BORDER_THIN);
		style.setBorderRight(BorderFormatting.BORDER_THIN);
		
		XSSFFont font = wb.createFont();
		font.setFontName("黑体");
		font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		//font.setFontHeightInPoints((short) 16);//设置字体大小
		
		style.setFont(font);
		
		XSSFCell cell = null;
		ExcelHeadVo ehv = null;
		for (int i = 0; i < excelHeadList.size(); i++) {
			ehv = excelHeadList.get(i);
			
			cell = row.createCell(i); //创建单元格
			cell.setCellValue(ehv.getTitle()); //设置表头名称
			cell.setCellStyle(style); //设置列的样式
			
			sheet.setColumnHidden(i, ehv.isHidden());
			if (ehv.isHidden())
				sheet.setColumnWidth(i, 0);
			else
				sheet.setColumnWidth(i, excelHeadList.get(i).getWidth()*200); //设置列宽
		}
	}
	
	/**
	 * 复制行
	 * @param wb 当前工作簿
	 * @param fromRow 模型行
	 * @param toRow 目标行
	 */
	public void copyRow(XSSFWorkbook wb, XSSFRow fromRow, XSSFRow toRow) {
		for (Iterator cellIt = fromRow.cellIterator(); cellIt.hasNext();) {
			XSSFCell tmpCell = (XSSFCell)cellIt.next();
			XSSFCell newCell = toRow.createCell(tmpCell.getColumnIndex());
			copyCell(wb, tmpCell, newCell);
		}
	}
    
    /** 
     * 复制单元格 
     *  
     * @param srcCell 模型单元格
     * @param distCell 目标单元格
     */  
    public void copyCell(XSSFWorkbook wb,XSSFCell srcCell, XSSFCell distCell) {
    	XSSFCellStyle newstyle=wb.createCellStyle();  
    	copyCellStyle(srcCell.getCellStyle(), newstyle);
        //样式  
        distCell.setCellStyle(newstyle);  
        //评论  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        // 不同数据类型处理  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
    }
    
    /** 
     * 复制一个单元格样式到目的单元格样式 
     * @param fromStyle 
     * @param toStyle 
     */  
    public void copyCellStyle(XSSFCellStyle fromStyle, XSSFCellStyle toStyle) {
    	toStyle.setAlignment(fromStyle.getAlignment());  
        //边框和边框颜色  
		toStyle.setBorderBottom(fromStyle.getBorderBottom());  
		toStyle.setBorderLeft(fromStyle.getBorderLeft());  
		toStyle.setBorderRight(fromStyle.getBorderRight());  
		toStyle.setBorderTop(fromStyle.getBorderTop());  
		toStyle.setTopBorderColor(fromStyle.getTopBorderColor());  
		toStyle.setBottomBorderColor(fromStyle.getBottomBorderColor());  
		toStyle.setRightBorderColor(fromStyle.getRightBorderColor());  
		toStyle.setLeftBorderColor(fromStyle.getLeftBorderColor());  
          
        //背景和前景  
        toStyle.setFillBackgroundColor(fromStyle.getFillBackgroundColor());  
        toStyle.setFillForegroundColor(fromStyle.getFillForegroundColor());  
          
        toStyle.setDataFormat(fromStyle.getDataFormat());  
        toStyle.setFillPattern(fromStyle.getFillPattern());  
//      toStyle.setFont(fromStyle.getFont(null));  
        toStyle.setHidden(fromStyle.getHidden());  
        toStyle.setIndention(fromStyle.getIndention());//首行缩进  
        toStyle.setLocked(fromStyle.getLocked());  
        toStyle.setRotation(fromStyle.getRotation());//旋转  
        toStyle.setVerticalAlignment(fromStyle.getVerticalAlignment());  
        toStyle.setWrapText(fromStyle.getWrapText());           
    }
}

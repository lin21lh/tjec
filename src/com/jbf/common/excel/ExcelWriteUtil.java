/************************************************************
 * 类名：ExcelWriteUtil
 *
 * 类别：Excel写出工具类
 * 功能：将map或object对象列表写出到excel文件中，支持2003、2007版本 
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-11  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jbf.common.exception.AppException;
import com.jbf.common.util.BeanUtil;

public class ExcelWriteUtil {

	/**
	 * 创建空的Workbook
	 * 
	 * @param type
	 *            取值为xlsx或xls,分别对应2007版和2003版的Excel文档
	 * @return
	 */
	public Workbook createEmptyWorkbook(String type) {
		if (type.equalsIgnoreCase("xlsx")) {
			return new XSSFWorkbook();
		}
		return new HSSFWorkbook();
	}

	/**
	 * 输出workbook到输出流
	 * 
	 * @param workbook
	 * @param outputStream
	 * @throws IOException
	 */
	public void writeWorkbook(Workbook workbook, OutputStream outputStream)
			throws IOException {
		workbook.write(outputStream);
	}

	/**
	 * 由workbook及数据创建sheet
	 * 
	 * @param workbook
	 *            非空的Workbook对象
	 * @param sheetName
	 *            sheet页面名
	 * @param dataList
	 *            数据列表
	 * @param columnNames
	 *            数据库列名列表
	 * @param cnColumnNames
	 *            数据库列名中文名列表
	 * 
	 * @param markFirstRowInFirstCol
	 *            是否使用第一列标记第一行数据的位置
	 * @param showColumnNames
	 *            是否导出数据库列表
	 * @return
	 * @throws AppException
	 */
	public Sheet createSheetFromObjectList(Workbook workbook, String sheetName,
			List<Object> dataList, List<String> columnNames,
			List<String> cnColumnNames, Boolean markFirstRowInFirstCol,
			Boolean showColumnNames) throws Exception {
		List<Map<String, Object>> maplist = new ArrayList<Map<String, Object>>();
		// 转换成为map
		for (Object o : dataList) {
			maplist.add(BeanUtil.beanToMap(o));
		}
		return createSheetFromMapList(workbook, sheetName, maplist,
				cnColumnNames, cnColumnNames, showColumnNames, showColumnNames);

	}

	/**
	 * 由workbook及数据创建sheet
	 * 
	 * @param workbook
	 *            非空的Workbook对象
	 * @param sheetName
	 *            sheet页面名
	 * @param dataList
	 *            数据列表
	 * @param columnNames
	 *            数据库列名列表
	 * @param cnColumnNames
	 *            数据库列名中文名列表
	 * 
	 * @param markFirstRowInFirstCol
	 *            是否使用第一列标记第一行数据的位置
	 * @param showColumnNames
	 *            是否导出数据库列表
	 * @return
	 * @throws AppException
	 */
	public Sheet createSheetFromMapList(Workbook workbook, String sheetName,
			List<Map<String, Object>> dataList, List<String> columnNames,
			List<String> cnColumnNames, Boolean markFirstRowInFirstCol,
			Boolean showColumnNames) throws AppException {
		if (markFirstRowInFirstCol == null) {
			markFirstRowInFirstCol = false;
		}

		if (showColumnNames == null) {
			showColumnNames = false;
		}
		int firstColMargin = 0;
		if (markFirstRowInFirstCol) {
			firstColMargin = 1;
		}
		Sheet sheet = workbook.createSheet(sheetName);
		// 写入数据库列名
		int rowIndex = 0;
		if (showColumnNames) {
			Row row = sheet.createRow(rowIndex);
			for (int i = 0; i < columnNames.size(); i++) {
				Cell c = row.createCell(i + firstColMargin,
						Cell.CELL_TYPE_STRING);
				c.setCellValue(columnNames.get(i));
			}
			rowIndex++;
		}

		// 写入中文列名
		Row row = sheet.createRow(rowIndex);
		for (int i = 0; i < cnColumnNames.size(); i++) {
			Cell c = row.createCell(i + firstColMargin, Cell.CELL_TYPE_STRING);
			c.setCellValue(cnColumnNames.get(i));
		}
		rowIndex++;

		// 写入数据

		for (Map<String, Object> map : dataList) {
			Row newrow = sheet.createRow(rowIndex);
			Set<String> keys = map.keySet();
			for (String key : keys) {
				int i = columnNames.indexOf(key);
				if (i == -1) {
					throw new AppException("excel.export.column.not.exists",
							new String[] { key });
				}
				Cell c = newrow.createCell(i + firstColMargin);
				Object o = map.get(key);
				if (o == null) {
					continue;
				}
				if (o instanceof String) {
					c.setCellValue((String) o);
				} else if (o instanceof Boolean) {
					c.setCellValue((Boolean) o);
				} else if (o instanceof Integer || o instanceof Double
						|| o instanceof Float) {
					Double value = Double.valueOf(o.toString());
					c.setCellValue(value);
				} else {
					c.setCellValue(o.toString());
				}
			}
		}
		return sheet;
	}
}

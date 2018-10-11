/************************************************************
 * 类名：ExcelReadUtil
 *
 * 类别：Excel读取工具类
 * 功能：由xls或xlsx文件读取数据，解析为List对象，可以配合使用MapUtil的cloneMapToObj方法使用保存对象
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-11  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.excel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReadUtil {

	/**
	 * 由inpustream创建workbook
	 * 
	 * @param in
	 * @param fileExtension
	 *            ，取值为xls或xlsx
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkBook(InputStream in, String fileExtension)
			throws Exception {
		if (fileExtension.equalsIgnoreCase("xls")) {
			return new HSSFWorkbook(in);
		} else if (fileExtension.equalsIgnoreCase("xlsx")) {
			return new XSSFWorkbook(in);
		} else {
			return null;
		}
	}

	/**
	 * 由文件路径创建workbook
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkBook(String path) throws Exception {
		if (path.toLowerCase().endsWith(".xls")) {
			FileInputStream fis = new FileInputStream(path);
			return new HSSFWorkbook(fis);
		} else if (path.toLowerCase().endsWith(".xlsx")) {
			return new XSSFWorkbook(path);
		} else {
			return null;
		}
	}

	/**
	 * 取得sheet的数量
	 * 
	 * @param workbook
	 * @return
	 */
	public static int getNumberOfSheets(Workbook workbook) {
		return workbook.getNumberOfSheets();
	}

	/**
	 * 按sheet名取得sheet
	 * 
	 * @param workbook
	 * @param sheetName
	 * @return
	 */
	public static Sheet getSheet(Workbook workbook, String sheetName) {
		return workbook.getSheet(sheetName);
	}

	/**
	 * 按索引取得sheet
	 * 
	 * @param workbook
	 * @param sheetIndex
	 * @return
	 */
	public static Sheet getSheetAt(Workbook workbook, int sheetIndex) {
		return workbook.getSheetAt(sheetIndex);
	}

	/**
	 * 取得sheet的列名 模板中的第一列(即A列)需要包含首行数据的指示标志，即在第一行数据的A列填"1"作为标志
	 * 
	 * @param sheet
	 * @return
	 */
	public static Map<String, Integer> getColumnNamesAndIndexes(Sheet sheet) {
		int firstRow = sheet.getFirstRowNum();

		Row row = sheet.getRow(firstRow);

		int firstCellNum = row.getFirstCellNum();
		int lastCellNum = row.getLastCellNum();

		Map<String, Integer> columnNames = new HashMap<String, Integer>();
		for (int index = firstCellNum; index < lastCellNum; index++) {
			Cell cell = row.getCell(index);
			if (cell == null) {
				continue;
			}
			columnNames.put(cell.getStringCellValue(), index);
		}
		return columnNames;
	}

	public static List<Map<String, Object>> getDataList(Sheet sheet) {
		return getDataList(sheet, -1);
	}

	/**
	 * 取得数据列表 如果dataRowIndex指定为-1，则模板中的第一列(即A列)需要包含首行数据的指示标志，即在第一行数据的A列填"1"作为标志
	 * 如果指定了dataRowIndex，则使用指定的dataRowIndex，从1开始
	 * 
	 * @param sheet
	 * @param dataRowIndex
	 *            数据行的开始行数，从1开始
	 * @return
	 */
	public static List<Map<String, Object>> getDataList(Sheet sheet,
			int dataRowIndex) {
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();

		if (dataRowIndex == -1) {
			// 分析第一列的值为“1”的单元格作为数据行的第一行
			for (int i = firstRow; i < lastRow; i++) {
				Row row = sheet.getRow(i);
				Cell c = row.getCell(0);
				if (c == null) {
					continue;
				}
				if (Cell.CELL_TYPE_STRING == c.getCellType()
						&& "1".equals(c.getStringCellValue())) {
					dataRowIndex = i;
					break;
				} else if (Cell.CELL_TYPE_NUMERIC == c.getCellType()
						&& 1d == c.getNumericCellValue()) {
					dataRowIndex = i;
					break;
				}
			}
		} else {
			dataRowIndex = dataRowIndex - 1;
		}
		// 取得各列名及索引位置
		Map<String, Integer> columnNamesAndIndexes = getColumnNamesAndIndexes(sheet);
		// 取得列名
		Set<String> columnNames = columnNamesAndIndexes.keySet();

		// 解析数据

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (int rowIndex = dataRowIndex; rowIndex < lastRow; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (String key : columnNames) {
				int index = columnNamesAndIndexes.get(key);
				Cell cell = row.getCell(index);
				if (cell == null) {
					continue;
				}
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					map.put(key, cell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
				case Cell.CELL_TYPE_FORMULA:
					map.put(key, cell.getNumericCellValue());
					break;

				case Cell.CELL_TYPE_BOOLEAN:
					map.put(key, cell.getBooleanCellValue());
				case Cell.CELL_TYPE_BLANK:
				case Cell.CELL_TYPE_ERROR:
					break;
				}
			}
			dataList.add(map);
		}
		return dataList;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String filePath = "d:/test.xlsx";
		Workbook wb = ExcelReadUtil.getWorkBook(filePath);
		Sheet sheet = wb.getSheet("Sheet1");
		List<Map<String, Object>> list = ExcelReadUtil.getDataList(sheet);
		System.out.println(list);
	}
}

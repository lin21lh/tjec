/************************************************************
 * 类名：ExcelUtil.java
 *
 * 类别：工具类
 * 功能： excel导入处理类(poi)
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelUtil {

	private static final Pattern date_ptrn1 = Pattern
			.compile("^\\[\\$\\-.*?\\]");
	private static final Pattern date_ptrn2 = Pattern
			.compile("^\\[[a-zA-Z]+\\]");
	private static final Pattern date_ptrn3 = Pattern
			.compile("^[yYmMdDhHsS\\-/,. :\"\\\\]+0?[ampAMP/]*$");

	/**
	 * 得到一个sheet页
	 * 
	 * @param in
	 * @param sheetIndex
	 * @return
	 * @throws Exception
	 */
	public static Sheet getOneSheet(InputStream in, int sheetIndex)
			throws Exception {
		if (sheetIndex < 0) {
			sheetIndex = 0;
		}

		Workbook wb = WorkbookFactory.create(in);

		if (sheetIndex >= wb.getNumberOfSheets()) {
			throw new Exception("找不到sheet页" + sheetIndex + "！提示：序号应从0开始。");
		}

		return wb.getSheetAt(sheetIndex);

	}

	public static Sheet getOneSheet(File file, int sheetIndex) throws Exception {
		InputStream in = new FileInputStream(file);
		return getOneSheet(in, sheetIndex);
	}

	public static Sheet getOneSheet(File file, String sheetIndex)
			throws Exception {
		return getOneSheet(file, Integer.parseInt(sheetIndex));
	}

	/**
	 * 得到excel指定页签、行、列单元格的数据
	 * 
	 * @param in
	 * @param sheetIndex
	 * @param colnum
	 *            列号
	 * @param rownum
	 *            行号
	 * @return
	 * @throws Exception
	 */
	public static Object getCellData(InputStream in, int sheetIndex,
			int colnum, int rownum) throws Exception {
		Sheet sheet = getOneSheet(in, sheetIndex);
		return getCellData(sheet, colnum, rownum);
	}

	/**
	 * 得到指定sheet、行、列的单元格的数据
	 * 
	 * @param sheet
	 * @param colnum
	 *            列号
	 * @param rownum
	 *            行号
	 * @return
	 * @throws Exception
	 */
	public static Object getCellData(Sheet sheet, int colnum, int rownum)
			throws Exception {
		if (rownum > sheet.getLastRowNum()) {
			throw new Exception("指定的行数超出范围！");
		}
		Row row = sheet.getRow(rownum);
		if (colnum > row.getLastCellNum()) {
			throw new Exception("指定的列数超出范围！");
		}

		Cell cell = row.getCell(colnum);
		if (cell == null) {
			return null;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			double d = cell.getNumericCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = DateUtil.getJavaDate(d);
				return com.jbf.common.util.DateUtil.dateToString(date,
						com.jbf.common.util.DateUtil.DATE_FORMAT);
			}
			return cell.getNumericCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			return cell.getCellFormula();
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return "";
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			return cell.getBooleanCellValue();
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			return "";
		}

		return null;
	}

	public static Object getCellData(Sheet sheet, String colnum, String rownum)
			throws Exception {
		return getCellData(sheet, Integer.parseInt(colnum),
				Integer.parseInt(rownum));
	}

	/**
	 * 获取单元格数据（行列从1计始）
	 * 
	 * @param sheet
	 * @param colnum
	 * @param rownum
	 * @return
	 * @throws Exception
	 */
	public static Object getCellData1(Sheet sheet, String colnum, String rownum)
			throws Exception {
		return getCellData1(sheet, Integer.parseInt(colnum),
				Integer.parseInt(rownum));
	}

	/**
	 * 获取单元格数据（行列从1计始）
	 * 
	 * @param sheet
	 * @param colnum
	 *            列号
	 * @param rownum
	 *            行号
	 * @return
	 * @throws Exception
	 */
	public static Object getCellData1(Sheet sheet, int colnum, int rownum)
			throws Exception {
		return getCellData(sheet, colnum - 1, rownum - 1);
	}

	/**
	 * 从sheet页中取列表数据
	 * 
	 * @param sheet
	 * @param startrow
	 *            数据起始行号：默认从第2行始
	 * @param endrow
	 *            结束行：如为负值，表示总行数+endrow，用于有表尾的情况。
	 * @param fields
	 *            Map<列号,字段名>
	 * @return
	 */
	public static List<Map> getListData(Sheet sheet, String startrow,
			String endrow, Map<Integer, String> fields) {
		Row row = null;
		Cell cell = null;
		List<Map> list = new ArrayList<Map>();
		int rows = sheet.getLastRowNum();

		int istartrow = 1;
		if (startrow != null && !startrow.equals("")) {// 数据起始行
			istartrow = Integer.parseInt(startrow) - 1;// 从0开始的
		}

		int iendrow = 1;
		if (endrow != null && !endrow.equals("")) {// 数据结束行
			iendrow = Integer.parseInt(endrow);
			if (iendrow < 0) {
				iendrow = rows;
			} else {
				iendrow -= 1;// 从0开始的
			}
		} else {
			iendrow = rows;
		}

		Map map = null;
		String value = "";
		boolean isBlankRow = true;// 是否是空行
		for (int i = istartrow; i <= iendrow; i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			map = new TreeMap();
			isBlankRow = true;
			for (Entry<Integer, String> field : fields.entrySet()) {
				cell = row.getCell(field.getKey());

				boolean isBlankCell = setMapValue(map, field.getValue(), cell);
				if (!isBlankCell && isBlankRow) {
					isBlankRow = false;
				}
			}

			if (!isBlankRow) {
				map.put("rownum", i + 1);
				list.add(map);
			}
		}

		return list;
	}

	/**
	 * 从sheet页中取列表数据(竖形列表)
	 * 
	 * @param sheet
	 * @param startcol
	 *            数据起始列号：默认从第2行始
	 * @param endcol
	 *            结束列：如为负值，表示总行数+endcol，用于有表尾的情况。
	 * @param fields
	 *            Map<行号,字段名>
	 * @return
	 */
	public static List<Map> getListData_vert(Sheet sheet, String startcol,
			String endcol, Map<Integer, String> fields) {
		int istartrow = (Integer) (fields.keySet().toArray()[0]);// 数据起始行
		Row row = sheet.getRow(istartrow);
		int cols = row.getLastCellNum();// 列数
		Cell cell = null;
		List<Map> list = new ArrayList<Map>();

		int istartcol = 1;
		if (startcol != null && !startcol.equals("")) {// 数据起始列
			istartcol = Integer.parseInt(startcol) - 1;// 从0开始的
		}

		int iendcol = 1;
		if (endcol != null && !endcol.equals("")) {// 数据结束列
			iendcol = Integer.parseInt(endcol);
			if (iendcol < 0) {
				iendcol += cols;
			} else {
				iendcol -= 1;// 从0开始的
			}
		} else {
			iendcol = cols - 1;
		}

		Map map = null;
		boolean isBlankCol = true;// 是否是空列
		for (int i = istartcol; i <= iendcol; i++) {
			map = new TreeMap();
			isBlankCol = true;
			for (Entry<Integer, String> field : fields.entrySet()) {
				row = sheet.getRow(field.getKey());
				if (row == null) {
					continue;
				}

				cell = row.getCell(i);

				boolean isBlankCell = setMapValue(map, field.getValue(), cell);
				if (!isBlankCell && isBlankCol) {
					isBlankCol = false;
				}
			}

			if (!isBlankCol) {
				list.add(map);
			}
		}

		return list;
	}

	public boolean isCellDateFormatted(Cell cell) {
		if (cell == null)
			return false;
		boolean bDate = false;

		double d = cell.getNumericCellValue();
		if (isValidExcelDate(d)) {
			CellStyle style = cell.getCellStyle();
			if (style == null)
				return false;
			int i = style.getDataFormat();
			String f = style.getDataFormatString();
			bDate = isADateFormat(i, f);
		}
		return bDate;
	}

	private boolean isADateFormat(int formatIndex, String formatString) {
		if (isInternalDateFormat(formatIndex)) {
			return true;
		}

		if ((formatString == null) || (formatString.length() == 0)) {
			return false;
		}

		String fs = formatString;

		StringBuilder sb = new StringBuilder(fs.length());
		for (int i = 0; i < fs.length(); i++) {
			char c = fs.charAt(i);
			if (i < fs.length() - 1) {
				char nc = fs.charAt(i + 1);
				if (c == '\\')
					switch (nc) {
					case ' ':
					case ',':
					case '-':
					case '.':
					case '\\':
						break;
					default:
						break;
					}
				if ((c == ';') && (nc == '@')) {
					i++;

					continue;
				}
			}
			sb.append(c);
		}
		fs = sb.toString().replaceAll("[年|月|日|时|分|秒|毫秒|微秒|;]", "");// added by
																	// mqy
																	// 20140307

		fs = date_ptrn1.matcher(fs).replaceAll("");

		fs = date_ptrn2.matcher(fs).replaceAll("");

		if ((fs.indexOf(';') > 0) && (fs.indexOf(';') < fs.length() - 1)) {
			fs = fs.substring(0, fs.indexOf(';'));
		}

		return date_ptrn3.matcher(fs).matches();
	}

	private boolean isInternalDateFormat(int format) {
		switch (format) {
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 45:
		case 46:
		case 47:
			return true;
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
		}
		return false;
	}

	private boolean isValidExcelDate(double value) {
		return value > -4.940656458412465E-324D;
	}

	private static boolean setMapValue(Map map, String fieldname, Cell cell) {
		boolean isBlank = true;// 是否是空行或空列
		if (cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			double d = cell.getNumericCellValue();
			if (DateUtil.isCellDateFormatted(cell)) {
				Date date = DateUtil.getJavaDate(d);
				String strdate = com.jbf.common.util.DateUtil.dateToString(
						date, com.jbf.common.util.DateUtil.DATE_FORMAT);
				map.put(fieldname, strdate);
			} else {
				map.put(fieldname, cell.getNumericCellValue());
			}

			isBlank = false;
		} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			String value = cell.getStringCellValue();
			if (value != null && !value.equals("")) {
				value = value.replace("　", "");// 该空格为全角空格
			}
			map.put(fieldname, value);
			isBlank = false;
		} else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
			map.put(fieldname, cell.getCellFormula());
		} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			map.put(fieldname, "");
		} else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
			map.put(fieldname, cell.getBooleanCellValue());
			isBlank = false;
		} else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
			map.put(fieldname, "");
		}

		return isBlank;
	}

}

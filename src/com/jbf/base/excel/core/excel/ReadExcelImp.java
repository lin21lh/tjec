/************************************************************
 * 类名：ReadExcelI.java
 *
 * 类别：工具类接口实现
 * 功能：通过xml配置文件读取excel文件数据
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   mqy         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.excel;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ExcelVo;
import com.jbf.base.excel.core.vo.FormVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.base.excel.core.xml.XmlCfgI;
import com.jbf.base.excel.core.xml.XmlCfgImp;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.NumberUtil;

public class ReadExcelImp implements ReadExcelI {
	XmlCfgI cfg = null;
	File file = null;
	String cfgFilename = "";
	ExcelVo excelVo = null;

	/**
	 * 数据异常信息（dataexception=insert时才可能有值）
	 */
	StringBuilder dataExceptionMsg = new StringBuilder();

	/**
	 * 本次导入的UUID
	 */
	String uuid = "";

	/**
	 * 读取配置
	 * 
	 * @param cfgFilename
	 *            Excel的xml配置文件名
	 * @throws Exception
	 */
	public ReadExcelImp(String cfgFilename) throws Exception {
		this.cfgFilename = cfgFilename;
		this.cfg = new XmlCfgImp(cfgFilename);
		this.excelVo = this.cfg.getExcelVo();
		this.uuid = CommonUtil.getUUID();
	}

	public ReadExcelImp(InputStream is, String encoding) throws Exception {
		this.cfg = new XmlCfgImp(is, encoding);
		this.excelVo = this.cfg.getExcelVo();
		this.uuid = CommonUtil.getUUID();
	}

	public ReadExcelImp(Reader reader) throws Exception {
		this.cfg = new XmlCfgImp(reader);
		this.excelVo = this.cfg.getExcelVo();
		this.uuid = CommonUtil.getUUID();
	}

	@Override
	public List<Map> getListData(File file, String sheetId) throws Exception {
		return getListData(file, sheetId, null);
	}

	@Override
	public Map getFormData(File file, String sheetId) throws Exception {
		return getFormData(file, sheetId, null);
	}

	@Override
	public List<Map> getListData(File file, String sheetId, String listId)
			throws Exception {
		List<Map> list = null;
		this.file = file;
		ListVo listVo = cfg.getListVo(sheetId, listId);
		Sheet sheet = ExcelUtil.getOneSheet(file, Integer.parseInt(sheetId));
		validSheetName(sheet, sheetId);

		if (!listVo.isVertlist()) {// 行列表
			Map<Integer, String> mapColField = cfg.getListColFieldMap(sheetId,
					listId);
			list = ExcelUtil.getListData(sheet, listVo.getStartrow(),
					listVo.getEndrow(), mapColField);
		} else {// 竖形列表
			Map<Integer, String> mapRowField = cfg.getListRowFieldMap(sheetId,
					listId);
			list = ExcelUtil.getListData_vert(sheet, listVo.getStartcol(),
					listVo.getEndcol(), mapRowField);
		}

		setColValueElse(list, sheetId, listId);
		validData(list, sheetId, listId, listVo);
		setListUUID(list, sheetId, listId);
		return list;
	}

	/**
	 * 设置列的其他引用值
	 * 
	 * @param list
	 * @param sheetId
	 * @param listId
	 * @throws Exception
	 */
	private void setColValueElse(List<Map> list, String sheetId, String listId)
			throws Exception {
		List<CellVo> colsElse = cfg.getListColsElse(sheetId, listId);
		if (colsElse == null || colsElse.isEmpty()) {
			return;
		}
		if (list == null || list.isEmpty()) {
			return;
		}

		for (CellVo vo : colsElse) {
			Sheet sheet = ExcelUtil.getOneSheet(file, vo.getSheetid());
			Object obj = ExcelUtil.getCellData1(sheet, vo.getColnum(),
					vo.getRownum());
			for (Map map : list) {
				map.put(vo.getFieldname(), obj);
			}
		}
	}

	private void validData(List<Map> list, String sheetId, String listId,
			ListVo listVo) throws Exception {
		if (list == null || list.isEmpty()) {
			return;
		}

		Map<Integer, CellVo> mapCols = cfg.getListCols(sheetId, listId);
		for (CellVo vo : mapCols.values()) {
			if (vo.getAllownull() != null && vo.getAllownull().equals("false")) {// 不允许空值
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					Object obj = map.get(vo.getFieldname());
					if (obj == null || obj.toString().equals("")) {
						String msg = "sheet页号：" + sheetId + "--'"
								+ vo.getText() + "'列、第" + map.get("rownum")
								+ "行内容为空！";
						list.remove(map);
						i--;
						handleDataException(msg);
					}
				}
			}

			if (vo.getDatatype() != null && vo.getDatatype().equals("decimal")) {// 数字类型
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					Object obj = map.get(vo.getFieldname());
					if (obj == null || !NumberUtil.isNumber(obj.toString())) {
						String msg = "sheet页号：" + sheetId + "--'"
								+ vo.getText() + "'列、第" + map.get("rownum")
								+ "行内容不是数值！";
						list.remove(map);
						i--;
						handleDataException(msg);
					}
				}
			}
		}

	}

	private void validSheetName(Sheet sheet, String sheetId) throws Exception {
		SheetVo sheetVo = cfg.getSheetVo(sheetId);
		String sheetname = sheetVo.getName();
		if (sheetname != null && !sheetname.equals("")
				&& !sheetname.equals(sheet.getSheetName())) {
			throw new AppException("sheetname不对应，请选择正确的文件或配置正确的sheetname。");
		}
	}

	/**
	 * 处理业务数据异常
	 * 
	 * @param msg
	 * @throws BizDataException
	 * @throws Exception
	 */
	public void handleDataException(String msg) throws Exception {
		if (msg == null || msg.equals("")) {
			return;
		}

		if (this.excelVo.getDataExceptionLevel() == DataExceptionLevel.insert) {
			dataExceptionMsg.append(msg).append("<br>");
		} else {
			throw new AppException(msg);
		}

	}

	@Override
	public Map getFormData(File file, String sheetId, String formId)
			throws Exception {
		Map ret = new LinkedHashMap();
		this.file = file;
		List<CellVo> list = cfg.getFormFields(sheetId, formId);
		Sheet sheet = ExcelUtil.getOneSheet(file, sheetId);
		validSheetName(sheet, sheetId);

		for (CellVo vo : list) {
			Object obj = ExcelUtil.getCellData1(sheet, vo.getColnum(),
					vo.getRownum());
			ret.put(vo.getFieldname(), obj);
		}

		setFormUUID(ret, sheetId, formId);
		return ret;
	}

	public XmlCfgI getCfg() {
		return cfg;
	}

	@Override
	public String getDataExceptionMsg() {
		if (dataExceptionMsg.length() > 0) {
			return "【" + this.file.getName() + "】"
					+ dataExceptionMsg.toString();
		}
		return "";
	}

	private void setListUUID(List<Map> list, String sheetId, String listId)
			throws Exception {
		if (list == null || list.isEmpty()) {
			return;
		}

		ListVo vo = cfg.getListVo(sheetId, listId);
		String uuidfield = vo.getUuidfield();
		if (uuidfield == null || uuidfield.equals("")) {
			return;
		}

		for (Map map : list) {
			map.put(uuidfield, uuid);
		}
	}

	private void setFormUUID(Map map, String sheetId, String formId)
			throws Exception {
		if (map == null || map.isEmpty()) {
			return;
		}

		FormVo vo = cfg.getFormVo(sheetId, formId);
		String uuidfield = vo.getUuidfield();
		if (uuidfield == null || uuidfield.equals("")) {
			return;
		}

		map.put(uuidfield, uuid);
	}

	@Override
	public String getUUID() {
		return this.uuid;
	}

}

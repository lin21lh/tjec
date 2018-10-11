/************************************************************
 * 类名：SysExcelDataImpServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：数据导入Service实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service.impl;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.excel.service.SysExcelDataImpService;

import com.jbf.base.excel.core.excel.ReadExcelI;
import com.jbf.base.excel.core.factory.ReadExcelFactory;
import com.jbf.base.excel.core.vo.FormVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.base.excel.core.xml.XmlCfgI;
import com.jbf.base.excel.dao.SysExcelImpCfgDao;
import com.jbf.base.excel.dao.SysExcelImpLogDao;
import com.jbf.base.excel.po.SysExcelImpCfg;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.FileUtil;

@Scope("prototype")
@Service
public class SysExcelDataImpServiceImpl implements SysExcelDataImpService {

	@Autowired
	SysExcelImpCfgDao sysExcelImpCfgDao;
	@Autowired
	SysExcelImpLogDao sysExcelImpLogDao;

	public SysExcelImpCfgDao getSysExcelImpCfgDao() {
		return sysExcelImpCfgDao;
	}

	public void setSysExcelImpCfgDao(SysExcelImpCfgDao sysExcelImpCfgDao) {
		this.sysExcelImpCfgDao = sysExcelImpCfgDao;
	}

	public SysExcelImpLogDao getSysExcelImpLogDao() {
		return sysExcelImpLogDao;
	}

	public void setSysExcelImpLogDao(SysExcelImpLogDao sysExcelImpLogDao) {
		this.sysExcelImpLogDao = sysExcelImpLogDao;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String uploadExcelDataFile(Map<String, String> map, InputStream input)
			throws Exception {
		String cfgId = map.get("cfgid");
		Long id = Long.parseLong(cfgId);
		SysExcelImpCfg impexpImpCfg = sysExcelImpCfgDao.get(id);
		if (impexpImpCfg == null) {
			throw new AppException("excel.imp.fail.cfg.not.exists");
		}
		Clob xml = impexpImpCfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		// 构建读取
		ReadExcelI readExcel = ReadExcelFactory.createByCfgReader(reader);

		XmlCfgI cfg = readExcel.getCfg();

		// 缓存文件
		Properties props = System.getProperties();
		String tmpdir = (String) props.get("java.io.tmpdir");
		String fileName = map.get("_FILE_NAME_");
		int lastIndex = fileName.lastIndexOf('.');
		String extension = fileName.substring(lastIndex);
		String tmpFileName = CommonUtil.getUUID() + extension;
		File tmpFile = new File(tmpdir, tmpFileName);
		FileUtil.writeToFile(input, tmpFile);

		// System.out.println("Debug:临时文件写入到:" + tmpFile.getAbsolutePath());

		List<SheetVo> sheets = cfg.getSheetVos();

		if (sheets == null || sheets.size() == 0) {
			tmpFile.delete();
			throw new AppException("excel.imp.fail.cfg.sheetvo.not.exists"); // sheetvo不存在
		}

		int count = 0;
		for (SheetVo sheetVo : sheets) {
			List<ListVo> lists = cfg.getListVos(sheetVo.getId());
			for (ListVo listVo : lists) {
				List<Map> list = readExcel.getListData(tmpFile,
						sheetVo.getId(), listVo.getId());
				saveData(listVo.getTablename(), list);
				System.out.println(list.size() + " 个对象保存到"
						+ listVo.getTablename() + "中！");
				count++;
			}

			List<FormVo> forms = cfg.getFormVos(sheetVo.getId());

			for (FormVo formVo : forms) {
				Map formmap = readExcel.getFormData(tmpFile, sheetVo.getId(),
						formVo.getId());
				sysExcelImpLogDao.addByMap(formmap, formVo.getTablename());
				count++;
			}
		}
		if (count == 0) {
			tmpFile.delete();
			throw new AppException("excel.imp.fail.cfg.no.list.or.form.exists"); // 配置中没有任何的表单或表单存在！
		}
		tmpFile.delete();
		return readExcel.getUUID();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveData(String tableName, List<Map> list) throws Exception {
		if (tableName == null || tableName.trim().length() == 0) {
			throw new AppException("配置文件中未指定保存的表名，保存失败!");
		}
		sysExcelImpLogDao.addBatchByList(list, tableName);
	}
}

/************************************************************
 * 类名：SysExcelImpCfgServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：数据导入配置Service实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.service.impl;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.excel.core.factory.ReadExcelFactory;
import com.jbf.base.excel.core.vo.CellVo;
import com.jbf.base.excel.core.vo.ListVo;
import com.jbf.base.excel.core.vo.SheetVo;
import com.jbf.base.excel.core.xml.EditCfgUtil;
import com.jbf.base.excel.core.xml.XmlCfgI;
import com.jbf.base.excel.dao.SysExcelImpCfgDao;
import com.jbf.base.excel.po.SysExcelImpCfg;
import com.jbf.base.excel.service.SysExcelImpCfgService;
import com.jbf.common.exception.AppException;

@Scope("prototype")
@Service
public class SysExcelImpCfgServiceImpl implements SysExcelImpCfgService {

	@Autowired
	SysExcelImpCfgDao sysExcelImpCfgDao;

	@Override
	public List queryDataImpCfg(String cfgcategory, String cfgname) {
		// DataUtilFactory fac = DataUtilFactory.newInstance();
		// DataFilter df = fac.empty();

		DetachedCriteria c = DetachedCriteria.forClass(SysExcelImpCfg.class);
		if (cfgcategory != null && cfgcategory.trim().length() > 0) {

			// df = df.eq("cfgcategory", cfgcategory.trim());
			c.add(Property.forName("cfgcategory").eq(cfgcategory.trim()));
		}

		if (cfgname != null && cfgname.trim().length() > 0) {
			// df = df.like("cfgname", cfgname.trim());
//			c.add(Property.forName("cfgname").eq(cfgname.trim()));
			c.add(Restrictions.sqlRestriction("cfgname like '%" + cfgname.trim() + "%'"));
		}

		List<SysExcelImpCfg> list = (List<SysExcelImpCfg>) sysExcelImpCfgDao
				.findByCriteria(c);
		for (SysExcelImpCfg o : list) {
			o.setCfgxml(null);
		}
		return list;
	}

	@Override
	public SysExcelImpCfg loadDataImpCfgForForm(Long id) {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(id);
		cfg.setCfgxml(null);
		return cfg;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delDataImpCfg(Long id) throws Exception {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(id);
		sysExcelImpCfgDao.delete(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editDataImpCfg(SysExcelImpCfg cfg) throws Exception {
		SysExcelImpCfg dbobj = sysExcelImpCfgDao.get(cfg.getId());
		sysExcelImpCfgDao.refresh(dbobj, LockMode.UPGRADE);

		dbobj.setCfgcategory(cfg.getCfgcategory());
		dbobj.setCfgname(cfg.getCfgname());
		dbobj.setStatus(cfg.getStatus());
		dbobj.setDataexception(cfg.getDataexception());
		dbobj.setClassname(cfg.getClassname());
		dbobj.setRemark(cfg.getRemark());

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("dataexception", cfg.getDataexception());
		map.put("classname", cfg.getClassname());

		Clob xml = dbobj.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editExcelVo(map, reader, writer);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(dbobj);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addDataImpCfg(SysExcelImpCfg cfg) throws Exception {
		String dataexception = cfg.getDataexception();
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!--excel 导入配置模板 --><Excel dataexception=\""
				+ dataexception
				+ "\" classname=\""
				+ (null == cfg.getClassname() ? "" : cfg.getClassname())
				+ "\"></Excel>";
		Clob clob = sysExcelImpCfgDao.createClob(xml);
		cfg.setCfgxml(clob);
		sysExcelImpCfgDao.save(cfg);
	}

	@Override
	public List<SheetVo> querySheets(Long cfgid) {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(cfgid);

		Clob xml = cfg.getCfgxml();
		try {
			Reader reader = xml.getCharacterStream();
			XmlCfgI xmlcfg = ReadExcelFactory.createCfgByReader(reader);
			List<SheetVo> list = xmlcfg.getSheetVos();
			for (SheetVo sheetVo : list) {
				sheetVo.setCfgname(cfg.getCfgname());
				sheetVo.setCfgid(cfgid.toString());
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addSheet(SheetVo vo) throws Exception {
		String cfgid = vo.getCfgid();
		Long id = Long.parseLong(cfgid);

		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(id);
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editSheetVo(vo, reader, writer, "add", null);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateSheet(SheetVo vo, String oldid) throws Exception {
		String cfgid = vo.getCfgid();
		Long id = Long.parseLong(cfgid);

		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(id);
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editSheetVo(vo, reader, writer, "edit", oldid);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delSheet(String id, Long cfgid) throws Exception {

		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(cfgid);
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.deleteSheet(id.split(","), reader, writer);

		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addSheetList(String cfgid, String sheetid, ListVo vo)
			throws Exception {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editType(sheetid, vo, reader, writer);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editSheetList(String cfgid, String sheetid, ListVo vo)
			throws Exception {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editType(sheetid, vo, reader, writer);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delSheetList(String cfgid, String sheetid, String id)
			throws Exception {
		if (id == null || id.trim().length() == 0) {
			throw new AppException("工作表列表ID参数异常！");
		}
		String[] ids = id.split(",");
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.deleteTypes(sheetid, ids, reader, writer);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);

	}

	@Override
	public List querySheetList(String cfgid, String sheetid) {
		SysExcelImpCfg cfg1 = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		Clob xml = cfg1.getCfgxml();
		try {
			Reader reader = xml.getCharacterStream();

			XmlCfgI cfg = ReadExcelFactory.createCfgByReader(reader);
			return cfg.getSheetChildren(sheetid);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editSheetCol(String cfgid, String ownersheetid, String typeid,
			CellVo vo, String flag) throws Exception {

		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.editCell(ownersheetid, typeid, vo, reader, writer, flag);
		writer.close();

		updateClob(xml, writer);

		sysExcelImpCfgDao.update(cfg);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List querySheetCol(String cfgid, String sheetid, String typeid,
			String typecode, CellVo vo) {
		SysExcelImpCfg cfg1 = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		Clob xml = cfg1.getCfgxml();
		try {
			Reader reader = xml.getCharacterStream();

			XmlCfgI cfg = ReadExcelFactory.createCfgByReader(reader);
			List<Map<String, Object>> list = cfg.getListFormChildren(sheetid,
					typeid, typecode);

			Map<String, String> mapSheets = cfg.getSheetsMap();
			if (vo.getText() != null && !vo.getText().equals("")) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					if (map.get("text") == null
							|| !map.get("text").equals(vo.getText())) {
						list.remove(map);
						i--;
					}
				}
			}
			if (vo.getFieldname() != null && !vo.getFieldname().equals("")) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					if (map.get("fieldname") == null
							|| !map.get("fieldname").equals(vo.getFieldname())) {
						list.remove(map);
						i--;
					}
				}
			}
			for (Map<String, Object> map : list) {
				map.put("sheetname", mapSheets.get(sheetid));
				map.put("sheetid", sheetid);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delSheetCol(String cfgid, String sheetid, String typeid,
			String typecode, String fieldname) throws Exception {
		SysExcelImpCfg cfg = sysExcelImpCfgDao.get(Long.parseLong(cfgid));
		sysExcelImpCfgDao.refresh(cfg, LockMode.UPGRADE);

		Clob xml = cfg.getCfgxml();
		Reader reader = xml.getCharacterStream();

		StringWriter writer = new StringWriter();
		EditCfgUtil.deleteCells(sheetid, typeid, typecode,
				fieldname.split(","), reader, writer);
		writer.close();
		updateClob(xml, writer);
		sysExcelImpCfgDao.update(cfg);
	}

	/**
	 * 将writer中的内存写入到clob中
	 * 
	 * @param clob
	 * @param writer
	 * @throws Exception
	 */
	private void updateClob(Clob clob, Writer writer) throws Exception {
		clob.truncate(0);
		Writer xmlWriter = clob.setCharacterStream(0);
		xmlWriter.write(writer.toString());
		xmlWriter.flush();
		xmlWriter.close();
	}

}

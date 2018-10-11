
package com.jbf.base.tabsdef.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.dic.dto.EditSchemeDTO;
import com.jbf.base.dic.ui.resolve.DicUICSchemeResolve;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicUISchemeDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicUIScheme;
import com.jbf.base.tabsdef.service.SysDicUIDesignService;
import com.jbf.base.tabsdef.vo.TableColumnFormDesignVo;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.XMLUtil;

@Service
public class SysDicUIDesignServiceImpl implements SysDicUIDesignService {

	public static final HashMap<String, String> TYPES = new HashMap<String, String>();

	static {
		TYPES.put("D", "datebox");
		TYPES.put("N", "numberbox");
		TYPES.put("S", "textbox");
		TYPES.put("C", "combobox");
	}
	@Autowired
	SysDicUISchemeDao sysDicUISchemeDao;
	
	@Autowired
	SysDicColumnDao dicColumnDao;

	public SysDicUIScheme get(Long schemeid) {
		SysDicUIScheme s = sysDicUISchemeDao.get(schemeid);
		return s;
	}
	
	public String getDicUIFormScheme(Long schemeid) throws IOException {
		
		SysDicUIScheme dicUIScheme = get(schemeid);
		if (dicUIScheme != null && StringUtil.isNotBlank(dicUIScheme.getFormscheme())) {
			List<SysDicColumn> columns = dicColumnDao.findColumnsByTablecode(dicUIScheme.getTablecode());
			Map<String, String> cnColMap = new HashMap<String, String>();
			for (SysDicColumn column : columns) {
				cnColMap.put(column.getColumncode().toLowerCase(), column.getColumnname());
			}
			if (dicUIScheme != null && dicUIScheme.getFormscheme() != null) {
				Document doc = XMLUtil.readDocumentFromFileStr(dicUIScheme.getFormscheme());
				Element element = doc.getRootElement().element("layout");
				Element fieldsElement = element.element("fields");
				List<Element> fieldElements = fieldsElement.elements("field");
				
				for (Element fieldElement : fieldElements) {
					fieldElement.addAttribute("label", cnColMap.get(fieldElement.attributeValue("name").toLowerCase()));
				}
				return doc.asXML();
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param id
	 * @param tablecode
	 * @param name
	 * @param content
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(String id, String tablecode, String name, String content)
			throws Exception {
		SysDicUIScheme s = sysDicUISchemeDao.get(Long.parseLong(id));
		if (s != null) {

			s.setFormscheme(content);
			sysDicUISchemeDao.update(s);
		} else {
			s = new SysDicUIScheme();
			s.setSchemename(name);
			s.setTablecode(tablecode);
			s.setUsed((byte) 0);
			s.setFormscheme(content);
			sysDicUISchemeDao.save(s);
		}
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public List queryTableColumns(String tablecode) {
		if (tablecode == null) {
			return new ArrayList();
		}
		String tbc = tablecode.toUpperCase();
		String sql = "select columncode name,columnname label,columntype type,isunqcol isunique,nullable notnull from sys_diccolumn  where tablecode='"
				+ tbc + "' order by name ";
		List<TableColumnFormDesignVo> list = (List<TableColumnFormDesignVo>) sysDicUISchemeDao
				.findVoBySql(sql, TableColumnFormDesignVo.class);

		for (TableColumnFormDesignVo vo : list) {
			vo.setType(TYPES.get(vo.getType()));
			if (vo.getNotNull() == null) {
				vo.setNotNull((byte) 0);
			} else {
				vo.setNotNull(vo.getNotNull() == 1 ? (byte) 0 : (byte) 1);
			}
		}
		return list;
	}
	
	public String saveDicUIScheme(SysDicUIScheme dicUIScheme) {
	
		if (dicUIScheme.getUsed() == null)
			dicUIScheme.setUsed(Byte.valueOf("0"));
		
		if (dicUIScheme.getUsed().equals(Byte.valueOf("1"))) {
			DetachedCriteria dc = DetachedCriteria.forClass(SysDicUIScheme.class);
			dc.add(Property.forName("tablecode").eq(dicUIScheme.getTablecode()));
			dc = dc.add(Property.forName("used").eq(Byte.valueOf("1")));
			List<SysDicUIScheme> list = (List<SysDicUIScheme>)sysDicUISchemeDao.findByCriteria(dc);
			if (list.size() > 0) {
				SysDicUIScheme uiScheme = list.get(0);
				uiScheme.setUsed(Byte.valueOf("0"));
				sysDicUISchemeDao.update(uiScheme);
			}
		}
		sysDicUISchemeDao.saveOrUpdate(dicUIScheme);
		return "保存成功！";
	}
	
	public String deleteDicUIScheme(Long schemeid) {
		
		SysDicUIScheme sysDicUIScheme = sysDicUISchemeDao.get(schemeid);
		if (sysDicUIScheme.getUsed() != null && sysDicUIScheme.getUsed().equals(Byte.valueOf("1"))) 
			return "当前界面方案正在启用状态，不允许删除！";
		
		sysDicUISchemeDao.delete(schemeid);
		return "";
	}
	
	public HashMap<String, Object> isExistUI(String tablecode) throws IOException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicUIScheme.class);
		dc.add(Property.forName("tablecode").eq(tablecode.toUpperCase()));
		dc = dc.add(Property.forName("used").eq(Byte.valueOf("1")));
		
		List<SysDicUIScheme> uiList = (List<SysDicUIScheme>)sysDicUISchemeDao.findByCriteria(dc);
		boolean isExist = uiList != null && uiList.size() > 0;
		map.put("isExist", isExist);
		if (isExist) {
			map.put("width", getWidth(uiList.get(0).getFormscheme()));
		}
		return map;
	}
	
    Integer getWidth(String formScheme) throws IOException {
    	EditSchemeDTO editSchemeDTO = new DicUICSchemeResolve().getEditScheme(formScheme);
    	return Integer.valueOf(editSchemeDTO.getLabelcellwidth() + editSchemeDTO.getControlcellwidth()) * editSchemeDTO.getColcount();
    }
}

/************************************************************
 * 类名：DicElementValSetServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：数据项值集服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.datascope.component.impl.DatascopeComponentImpl.ScopeType;
import com.jbf.base.datascope.dao.SysScopevalueDao;
import com.jbf.base.dic.component.DicElementViewComponent;
import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.dto.DicTreeVo;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.service.DicElementValSetService;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.datascope.ConditionFilter;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.sys.log.service.impl.SysLogApp;

@Scope("prototype")
@Service
public class DicElementValSetServiceImpl implements DicElementValSetService{

	@Autowired
	SysDicElementValSetDao dicElementValSetDao;
	@Autowired
	SysDicTableDao dicTableDao;
	@Autowired
	SysDicColumnDao dicColumnDao;
	@Autowired
	SysDicElementDao dicElementDao;
	@Autowired
	SysScopevalueDao scopevalueDao;
	@Autowired
	DicElementViewComponent dicElementViewCp;
	
	@Override
	public EasyUITotalResult queryPageDicElementVals(Map<String, Object> paramMap) throws SecurityException, ClassNotFoundException, NoSuchFieldException {
		
		SysDicTable tablePO = dicTableDao.getByTablecode(paramMap.get("tablecode").toString());
		Integer page = StringUtil.isNotBlank((String)paramMap.get("page")) ? Integer.valueOf((String)paramMap.get("page")) : 1;
		Integer rows = StringUtil.isNotBlank((String)paramMap.get("rows")) ? Integer.valueOf((String)paramMap.get("rows")) : 50;
		EasyUITotalResult totalResult = dicElementValSetDao.queryPageDicElementVals(tablePO, (String)paramMap.get("elementcode"), page, rows);
		List list = totalResult.getRows();
		List<SysDicColumn> colList = dicColumnDao.findColumnsByTablecode(tablePO.getTablecode());
		Map value = null;
		SysDicTable dicTable = null;
		Map dicMap = null;
		String codeVal = null;
		for (SysDicColumn column : colList) {
			if (StringUtil.isNotBlank(column.getSourceelementcode())) {
				dicTable = dicTableDao.getByElementcode(column.getSourceelementcode());
				for (Object obj : list) {
					value = (Map) obj;
					codeVal = value.get(column.getColumncode().toLowerCase()) != null ? value.get(column.getColumncode().toLowerCase()).toString() : "";
					if (StringUtil.isNotBlank(codeVal)) {
						dicMap = dicElementValSetDao.getByElementAndCode(dicTable.getTablecode(), column.getSourceelementcode(), codeVal);
						if (dicMap != null)
							value.put(column.getColumncode().toLowerCase() + "name", dicMap.get(dicTable.getNamecolumn().toLowerCase()));
						else
							value.put(column.getColumncode().toLowerCase() + "name", "");
					} else
						value.put(column.getColumncode().toLowerCase() + "name", "");

				}
			}
		}
		return totalResult;
	}

	@Override
	public void saveDicElementVal(Map value) throws Exception {
		
		String elementcode = (String)value.get("elementcode");
		String entryname = (String)value.get("entryname");
		value.remove("entryname");
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		if (value.get(dicTable.getKeycolumn().toLowerCase()) == null)
			dicElementValSetDao.add(value, dicTable.getTablecode());
		else
			dicElementValSetDao.update(value, dicTable.getTablecode());
		
	}
	
	public void saveDicElementTreeVal(Map value) throws NumberFormatException, Exception {
		String elementcode = (String)value.get("elementcode");
		String tablecode = (String)value.get("tablecode");
		value.remove("tablecode");
		
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		String parentid = (String)value.get(dicTable.getSupercolumn());
		Object keyid = value.get(dicTable.getKeycolumn().toLowerCase());
		if (StringUtil.isNotBlank((String)keyid)) { //update
			dicElementValSetDao.update(value, dicTable.getTablecode());
		}  else {
			if (StringUtil.isNotBlank(parentid) && !parentid.equals("0")) { //新增下级
				Object parentObj = dicElementValSetDao.getByID(dicTable, Long.valueOf(parentid));
				Integer parentLevel = 1;
				if (parentObj == null)
					throw new AppException("上级ID：" + parentid + "未查询到！");
				
				parentLevel = Integer.valueOf(BeanUtils.getProperty(parentObj, dicTable.getLevelnocolumn().toLowerCase()).toString());
				value.put(dicTable.getSupercolumn().toLowerCase(), parentid);
				value.put(dicTable.getLevelnocolumn().toLowerCase(), parentLevel+1);
				value.put(dicTable.getIsleafcolumn().toLowerCase(), "1");

				dicElementValSetDao.add(value, dicTable.getTablecode());
				Map<String, Object> parentMap = null;
				if (parentObj instanceof Map)
					parentMap = (Map)parentObj;
				else
					parentMap = BeanUtils.describe(parentObj);
				parentMap.put(dicTable.getIsleafcolumn(), 0);
				dicElementValSetDao.update(parentMap, dicTable.getTablecode());
					
			} else { //新增
				value.put(dicTable.getSupercolumn(), 0);
				value.put(dicTable.getLevelnocolumn(), "1");
				value.put(dicTable.getIsleafcolumn(), "1");
				
				dicElementValSetDao.add(value, dicTable.getTablecode());
			}
		}

		

	}

	@Override
	public List<Map> findDicElementVals(String elementcode) {
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		return dicElementDao.findDicElementVals(dicTable, elementcode);
	}

	public Object getDetailByID(String tablecode, Long id) {
		SysDicTable dicTable = dicTableDao.getDicTable(tablecode);
		List list = dicElementValSetDao.query(tablecode, dicTable.getKeycolumn() + "=" + id);
		return list.get(0);
	}
	
	public List queryDicTreeElementVals(String elementcode, String byElementcode, String valueModel, String textModel, String menuid, String customSql, boolean isRelation,  boolean isCFilter) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException {
		
		SysDicTable tablePO = dicTableDao.getByElementcode(elementcode);
		//校验表 对应字段是否已定义
		String[] args = {tablePO.getTablecode()+"-"+tablePO.getTablename()};
		if (StringUtil.isBlank(tablePO.getKeycolumn()))
			throw new AppException("datatable.keycolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getCodecolumn()))
			throw new AppException("datatable.codecolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getNamecolumn()))
			throw new AppException("datatable.namecolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getSupercolumn()))
			throw new AppException("datatable.supercolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getLevelnocolumn()))
			throw new AppException("datatable.levelnocolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getIsleafcolumn()))
			throw new AppException("datatable.isleafcolumn.undefined", args);
		
		String viewFilterSql = dicElementViewCp.getSqlString(elementcode);
		
		String conditionFilterSql = null;
		if (isCFilter) {
			int scopeType = ScopeType.ID;
			if ("code".equalsIgnoreCase(valueModel))
				scopeType = ScopeType.CODE;
			conditionFilterSql = ConditionFilter.calculateDataRightByElementcode(Long.valueOf(menuid), elementcode, "t", tablePO.getTablecode(), scopeType);
		}
		
		List<Map> dteVals = dicElementValSetDao.queryDicElementVals(tablePO, elementcode, viewFilterSql, customSql, conditionFilterSql, null);
		List<DicTreeVo> treeList = new ArrayList<DicTreeVo>(dteVals.size());
		DicTreeVo vo = null;
		String namecolumn = tablePO.getNamecolumn().toLowerCase();
		if (StringUtil.isNotBlank(textModel)) {
			if (textModel.toLowerCase().indexOf("shortname") != -1) {
				namecolumn = "shortname";
			} else if (textModel.toLowerCase().indexOf("wholename") != -1) {
				namecolumn = "wholename";
			}
		}
		for (Map obj : dteVals) {
			vo = new DicTreeVo();
			vo.setId(obj.get(tablePO.getKeycolumn().toLowerCase()).toString());
			Object pid = obj.get(tablePO.getSupercolumn().toLowerCase());
			if (pid!=null) {
				vo.setPid(obj.get(tablePO.getSupercolumn().toLowerCase()).toString());
			}
			vo.setCode((String)obj.get(tablePO.getCodecolumn().toLowerCase()));
			vo.setName((String)obj.get(tablePO.getNamecolumn().toLowerCase()));
			if (StringUtil.isNotBlank(textModel)) {
				if (textModel.startsWith("code")) {
					vo.setText(obj.get(tablePO.getCodecolumn().toLowerCase()) + "-" + obj.get(namecolumn));
				} else {
					vo.setText((String)obj.get(namecolumn));
				}
			}else {				
				vo.setText(obj.get(tablePO.getCodecolumn().toLowerCase()) + "-" + obj.get(tablePO.getNamecolumn().toLowerCase()));
			}
			vo.setLevelno(Integer.valueOf(obj.get(tablePO.getLevelnocolumn().toLowerCase()).toString()));
			vo.setIsLeaf(obj.get(tablePO.getIsleafcolumn().toLowerCase()).toString().equals("1") ? true : false);
			treeList.add(vo);
		}
		
		return TreeVoUtil.toBornTree2(treeList, "0", true);
	}
	
	public Map<String, Object> queryDicValsToDataScope(String elementcode, Long scopeitemid, String currenttdid) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException {
		SysDicTable tablePO = dicTableDao.getByElementcode(elementcode);
		//校验表 对应字段是否已定义
		String[] args = {tablePO.getTablecode()+"-"+tablePO.getTablename()};
		if (StringUtil.isBlank(tablePO.getKeycolumn()))
			throw new AppException("datatable.keycolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getCodecolumn()))
			throw new AppException("datatable.codecolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getNamecolumn()))
			throw new AppException("datatable.namecolumn.undefined", args);
		
		String viewFilterSql = dicElementViewCp.getSqlString(elementcode);
		List<Map> dteVals = dicElementValSetDao.queryDicElementVals(tablePO, elementcode, viewFilterSql, null, null, null);

		List<Long> scopeValList = scopevalueDao.findScopevaluesByScopeitem(scopeitemid);
		DicTreeVo tree = null;
		List<DicTreeVo> treeList = new ArrayList<DicTreeVo>(scopeValList.size() + 1);
		
		for (Map value : dteVals) {
			tree = new DicTreeVo();
			tree.setId(value.get(tablePO.getKeycolumn().toLowerCase()).toString());
			tree.setCode(value.get(tablePO.getCodecolumn().toLowerCase()).toString());
			tree.setText(value.get(tablePO.getCodecolumn().toLowerCase()) + "-" + value.get(tablePO.getNamecolumn().toLowerCase()));
			
			if (tablePO.getSupercolumn() != null)
				tree.setPid(value.get(tablePO.getSupercolumn().toLowerCase())!=null ? value.get(tablePO.getSupercolumn().toLowerCase()).toString() : "0");
			if (tablePO.getIsleafcolumn() != null)
				tree.setIsLeaf("1".equals(value.get(tablePO.getIsleafcolumn().toLowerCase()).toString()));
			if (tablePO.getLevelnocolumn() != null)
				tree.setLevelno(Integer.valueOf(value.get(tablePO.getLevelnocolumn().toLowerCase()).toString()));
			if (scopeValList != null && scopeValList.size() > 0 && scopeValList.contains(Long.valueOf((value.get(tablePO.getKeycolumn().toLowerCase()).toString())))) {
				tree.setChecked(true);
				tree.setIsChecked("1");
			} else {
				tree.setChecked(false);
				tree.setIsChecked("0");
			}
				
			
			treeList.add(tree);
		}
		treeList = (List<DicTreeVo>) TreeVoUtil.toBornTree2(treeList, "0", true);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("currenttdid", currenttdid);
		resultMap.put("treeData", treeList);
		return resultMap;
	}
	
	public String deleteDicElementVal(String ids, String tablecode) {
		
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode.toUpperCase());
		List<Map> list = dicElementValSetDao.query(tablecode, dicTable.getKeycolumn() + " in(" + ids + ")");
		StringBuffer sb = new StringBuffer();
		for (Map value : list) {
			if (value.get("systempretag") != null && value.get("systempretag").toString().equals("1")) {
				if (sb.length() > 0)
					sb.append("、");
				sb.append(value.get(dicTable.getCodecolumn().toLowerCase()) + "-" + value.get(dicTable.getNamecolumn().toLowerCase()));
			}
		}
		
		if (sb.length() > 0) {
			sb.append(AppException.getMessage("dicval.del.isst"));
			return sb.toString();
		}
		return dicElementValSetDao.delete(tablecode, dicTable.getKeycolumn() + " in(" + ids + ")") ? "" : AppException.getMessage("crud.delerr");
	}
	
	@Override
	public String deleteDicTreeElementVal(HttpServletRequest request, Long id, String elementcode) throws Exception {
		SysDicTable dicTable = dicTableDao.getByElementcode(elementcode);
		Object obj = dicElementValSetDao.getByID(dicTable, id);
		Object isleaf = null;
		Object parentid = null;
		Object systempretag = null;
		String text = null;
		if (obj instanceof Map) {
			isleaf = ((Map)obj).get(dicTable.getIsleafcolumn().toLowerCase());
			parentid = ((Map)obj).get(dicTable.getSupercolumn().toLowerCase());
			systempretag = ((Map)obj).get("systempretag");
			text = ((Map)obj).get(dicTable.getCodecolumn().toLowerCase()) + "-" + ((Map)obj).get(dicTable.getNamecolumn().toLowerCase());
		} else {
			isleaf = BeanUtils.getProperty(obj, dicTable.getIsleafcolumn().toLowerCase());
			parentid = BeanUtils.getProperty(obj, dicTable.getSupercolumn().toLowerCase());
			systempretag = BeanUtils.getProperty(obj, "systempretag");
			text = BeanUtils.getProperty(obj, dicTable.getCodecolumn().toLowerCase()) + "-" + BeanUtils.getProperty(obj, dicTable.getNamecolumn().toLowerCase());
		}
		if (systempretag != null && systempretag.toString().equals("1"))
			return text + "是系统预设值集，不允许删除！";
		
		if ("0".equals(isleaf.toString()))
			return "非叶子节点，不允许删除！";
		else {
			if (parentid != null && parentid.toString().length() > 0 && !"0".equals(parentid.toString())) {
				String where = dicTable.getSupercolumn() + "=" + parentid + " and " + dicTable.getKeycolumn() + "<>" + id;
				List list = dicElementValSetDao.query(dicTable.getTablecode(), where);
				if (list == null || list.size() == 0) {
					Object parentObj = dicElementValSetDao.getByID(dicTable, Long.valueOf(parentid.toString()));
					Map parentMap = (Map)parentObj;
					parentMap.put(dicTable.getIsleafcolumn(), 1);
					dicElementValSetDao.update(parentMap, dicTable.getTablecode());
				}
			}
			dicElementValSetDao.deleteDicTreeElementVal(dicTable, id);
			SysLogApp.writeLog(request, "数据项编码：" + elementcode + ",值集为：" + text + "被删除。", 2);
		}
		return "";
	}

	@Override
	public Long findIDByCodeElement(String elementcode, String code) throws AppException {
		SysDicTable table = dicTableDao.getByElementcode(elementcode.toUpperCase());
		return dicElementValSetDao.findIDByCodeElement(table, elementcode, code);
	}
	
	public List queryByElementcode(String menuid, String elementcode, String byElementcode, String idColumn, String textColumn, String customSql, boolean isCFilter, boolean isRelation) throws AppException {
		
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode);
		
		if (dicElement == null)
			throw new AppException("数据项：" + elementcode + "未定义");
		
		
		SysDicTable dicTable = dicTableDao.getByTablecode(dicElement.getTablecode());
		
		if (dicTable == null)
			throw new AppException("数据项：" + elementcode + "对应的表" + dicElement.getTablecode() + "未定义");
		
		if (idColumn == null || idColumn.trim().length() == 0)
			idColumn = dicTable.getKeycolumn();
		
		if (textColumn == null || textColumn.trim().length() == 0)
			textColumn = dicTable.getCodecolumn() + "||'-'||" + dicTable.getNamecolumn();
		else {
			String[] textCols =textColumn.split(",");
			StringBuffer tCol = new StringBuffer(50);
			for (String textCol : textCols) {
				if (tCol.length() > 0)
					tCol.append("||'-'||");
				
				tCol.append(textCol);
			}
			textColumn = tCol.toString();
		}
		
		String conditionFilterSql = null;
		if (isCFilter) {
			int scopeType = ScopeType.ID;
			if (idColumn.equals(dicTable.getCodecolumn()))
				scopeType = ScopeType.CODE;
			conditionFilterSql = ConditionFilter.calculateDataRightByElementcode(Long.valueOf(menuid), elementcode, "t", dicTable.getTablecode(), scopeType);
		}
		
		String relationSql = null;
		if  (isRelation) {
			relationSql = "";
		}
		
		String viewFilterSql = dicElementViewCp.getSqlString(elementcode); 
		
		return dicElementValSetDao.findByElemenetcode(dicElement, dicTable, idColumn, textColumn, viewFilterSql, customSql, conditionFilterSql, relationSql);
	}
}

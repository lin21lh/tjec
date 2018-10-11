/************************************************************
 * 类名：SysDeptServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：机构管理服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.dept.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.TableNameConst;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.common.vo.TreeVo;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.dao.SysDeptExpcfgDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.dept.po.SysDeptexpcfg;
import com.jbf.sys.dept.service.SysDeptService;
import com.jbf.sys.dept.vo.DeptTreeVo;

@Scope("prototype")
@Service
public class SysDeptServiceImpl implements SysDeptService {
	
	@Autowired
	SysDeptDao deptDao;
	@Autowired
	SysDeptExpcfgDao deptExpcfgDao;
	@Autowired
	SysDicTableDao dicTableDao;
	@Autowired
	SysDicColumnDao dicColumnDao;
	@Autowired
	SysDicElementValSetDao dicElementValSetDao;
	
	public List<DeptTreeVo> queryDeptTree() {
		
		List<SysDept> deptList = deptDao.query();
		DeptTreeVo vo = null;
		List<DeptTreeVo> treeList = new ArrayList<DeptTreeVo>();
		for (SysDept dept : deptList) {
			vo = new DeptTreeVo();
			vo.setId(dept.getItemid().toString());
			vo.setPid(dept.getSuperitemid() != null ? dept.getSuperitemid().toString() : "0");
			vo.setIsLeaf(dept.getIsleaf() == 1 ? true : false);
			vo.setLevelno(dept.getLevelno());
			vo.setCode(dept.getCode());
			vo.setText(dept.getCode() + "-" + dept.getName());
			vo.setStatus(dept.getStatus().toString());
			Date endDate = DateUtil.parseDate(dept.getEnddate(), DateUtil.DATE_FORMAT);
			if (endDate != null && endDate.before(new Date())) {
				//System.out.println(dept.getEnddate());
				vo.setStatus("99");
			}
				
			treeList.add(vo);
		}
		
		treeList = (List<DeptTreeVo>) TreeVoUtil.toBornTree(treeList, "0", true);
		recurseCollapseTreeNodes(treeList);
		
		return treeList;
	}
	
	private void recurseCollapseTreeNodes(List<? extends TreeVo> tree){
		for (TreeVo vo : tree) {
			if(vo.getChildren() == null || vo.getChildren().isEmpty()){
				vo.setState("open");
				continue;
			}
			
			vo.setState("closed");
			recurseCollapseTreeNodes(vo.getChildren());
		}
	}
	
	
	public Object get(Long itemid) {
		SysDept agency = deptDao.get(itemid);
		Map value = null;
		try {
			value = BeanUtils.describe(agency);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		SysDicTable deptExpTable = dicTableDao.getByTablecode(TableNameConst.SYS_DEPTEXP.toUpperCase());
		if (deptExpTable != null && deptExpTable.getTablecode() != null) {
			try {
				Object o = dicElementValSetDao.getByID(deptExpTable, itemid);
				Map expColMap = null;
				if (o instanceof Map)
					expColMap = (Map)o;
				else
				 expColMap = BeanUtils.describe(o);
				value.putAll(expColMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	public void save(Map value) {
		SysDept dept = new SysDept();
		try {
			BeanUtils.populate(dept, value);
			
			Integer levelno = 1;
			if (dept.getSuperitemid() != null && dept.getSuperitemid().longValue() > 0) {
				SysDept superDept = deptDao.get(dept.getSuperitemid());
				if (superDept.getIsleaf().intValue() == 1) { //更新上级是否末级节点
					superDept.setIsleaf(0);
					deptDao.update(superDept);
				}
				levelno = superDept.getLevelno().intValue() + levelno;
			}
			
			dept.setIsleaf(1);
			dept.setLevelno(levelno);
			
			SysDicTable deptExpTable = dicTableDao.getByTablecode(TableNameConst.SYS_DEPTEXP.toUpperCase());
			Long itemid = dept.getItemid();
			Map expValue = new HashMap();
			List<SysDeptexpcfg> expCols = deptExpcfgDao.findByAgencycat(dept.getAgencycat());
			for (SysDeptexpcfg expCol : expCols) {
				expValue.put(expCol.getExpandcolumn().toLowerCase(), value.get(expCol.getExpandcolumn().toLowerCase()));
			}
			expValue.put("agencycat", dept.getAgencycat());
			if (itemid == null) {
				itemid = (Long) deptDao.save(dept);
				value.put("itemid", itemid);
				

				expValue.put("itemid", itemid);
				expValue.put("agencycat", dept.getAgencycat());
				dicElementValSetDao.add(expValue, deptExpTable.getTablecode());
			} else {
				deptDao.update(dept);
				expValue.put("itemid", value.get("itemid"));
				
				dicElementValSetDao.update(value, deptExpTable.getTablecode());
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List queryUnselectedExp(Long agencycat) {
		List<SysDicColumn> expCols = dicColumnDao.findColumnsByTablecode(TableNameConst.SYS_DEPTEXP);
		List<String> selectAgencyExpCols = getAgencyExpColumns(agencycat);
		selectAgencyExpCols.add("itemid");
		selectAgencyExpCols.add("agencycat");
		List<SysDicColumn> removeList = new ArrayList<SysDicColumn>();
		if (expCols != null && expCols.size() > 0) {
			for (SysDicColumn expColumn : expCols) {
				if (selectAgencyExpCols.contains(expColumn.getColumncode().toLowerCase()))
					removeList.add(expColumn);
			}
			expCols.removeAll(removeList);
		}
		return expCols;
	}

	@Override
	public List querySelectedExp(Long agencycat) {
		List<SysDicColumn> expCols = dicColumnDao.findColumnsByTablecode(TableNameConst.SYS_DEPTEXP);
		List<String> selectAgencyExpCols = getAgencyExpColumns(agencycat);
		if (selectAgencyExpCols.size() > 0) {
			selectAgencyExpCols.remove("itemid");
			selectAgencyExpCols.remove("agencycat");
			List<SysDicColumn> removeList = new ArrayList<SysDicColumn>();
			if (expCols != null && expCols.size() > 0) {
				for (SysDicColumn expColumn : expCols) {
					if (!selectAgencyExpCols.contains(expColumn.getColumncode().toLowerCase()))
						removeList.add(expColumn);
				}
				expCols.removeAll(removeList);
			}
			return expCols;
		} else
			return new ArrayList();
	}
	
	public List<String> getAgencyExpColumns(Long agencycat) {
		
		List<SysDeptexpcfg> expcfgCols = deptExpcfgDao.findByAgencycat(agencycat);
		List<String> list = new ArrayList<String>();
		for (SysDeptexpcfg expcfgcol : expcfgCols) {
			list.add(expcfgcol.getExpandcolumn().toLowerCase());
		}
		
		return list;
	}

	@Override
	public void saveDeptExpCfg(String str) {
		String[] ss = str.split("`");
		Long agencycat = Long.valueOf(ss[0]);
		String[] expColumns = ss[1].split("~");
		DetachedCriteria dCriteria = DetachedCriteria.forClass(SysDicColumn.class);
		dCriteria = dCriteria.add(Property.forName("tablecode").eq(TableNameConst.SYS_DEPTEXP.toUpperCase()));
		dCriteria = dCriteria.add(Property.forName("columncode").in(expColumns));
		List<SysDicColumn> expcfgCols = (List<SysDicColumn>) dicColumnDao.findByCriteria(dCriteria);
		SysDeptexpcfg deptexpcfg = null;
		deptExpcfgDao.deleteAll(deptExpcfgDao.findByAgencycat(agencycat));
		for (SysDicColumn expColumn : expcfgCols) {
			Byte isnotnull = expColumn.getNullable().toString().equals("1") ? Byte.valueOf("0") : Byte.valueOf("1");
			deptexpcfg = new SysDeptexpcfg(agencycat, expColumn.getColumncode().toLowerCase(), isnotnull);
			deptExpcfgDao.save(deptexpcfg);
		}
		 
	}
	
	public ResultMsg delete(Long itemid) {
		
		SysDept dept = deptDao.get(itemid);
		if (dept.getIsleaf() == 0) {
			return new ResultMsg(false, "删除失败,非末级机构不允许删除！");
		}
		deptDao.delete(dept);
		SysDicTable expTable = dicTableDao.getByTablecode(TableNameConst.SYS_DEPTEXPCFG);
		try {
			dicElementValSetDao.delete(expTable.getTablecode(), expTable.getKeycolumn() + "=" + itemid);
			if (dept.getSuperitemid() != null) {
				SysDept superDept = deptDao.get(dept.getSuperitemid());
				DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysDept.class);
				dcCriteria = dcCriteria.add(Property.forName("superitemid").eq(superDept.getItemid()));
				dcCriteria = dcCriteria.add(Property.forName("itemid").ne(dept.getItemid()));
				List<SysDept> list = (List<SysDept>) deptDao.findByCriteria(dcCriteria);
				if (list.size() == 0) {
					superDept.setIsleaf(1);
					deptDao.update(superDept);
				}
			}
			return new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "删除失败，失败原因：" + e.getMessage());
		}
	}
	
	public String getExpColumnsHTML(Long agencycat, Long itemid) throws AppException {
		
		if (agencycat == null)
			agencycat = deptDao.get(itemid).getAgencycat();
		List<SysDeptexpcfg> expColDTOs = deptExpcfgDao.findByAgencycat(agencycat);
		if (expColDTOs == null || expColDTOs.size() == 0)
			return "";
		
		List<String> expCols = new ArrayList<String>();
		for (SysDeptexpcfg expColDTO : expColDTOs) {
			expCols.add(expColDTO.getExpandcolumn().toUpperCase());
		}
		
		DetachedCriteria dCriteria = DetachedCriteria.forClass(SysDicColumn.class);
		dCriteria = dCriteria.add(Property.forName("tablecode").eq(TableNameConst.SYS_DEPTEXP.toUpperCase()));
		dCriteria = dCriteria.add(Property.forName("columncode").in(expCols));
		
		List<SysDicColumn> expcfgCols = (List<SysDicColumn>) dicColumnDao.findByCriteria(dCriteria);
		
		if (expCols.size() > 0 && (expcfgCols ==null || expcfgCols.size() == 0))
			throw new AppException("扩展属性[" + expCols + "]列未在数据表" + TableNameConst.SYS_DEPTEXP + "中定义");
		
		StringBuilder sb = new StringBuilder();
		StringBuilder expcolids = new StringBuilder();
		StringBuilder expCombo = new StringBuilder();
		int n = 0;
		for (SysDicColumn column : expcfgCols) {
			if (n%2 == 0)
				sb.append("<tr>");
			sb.append("<th>").append(column.getColumnname()).append("：</th>");
			sb.append("<td><input id=\"").append(column.getColumncode().toLowerCase());
			sb.append("\" name=\"").append(column.getColumncode().toLowerCase());
			String inputClass = "easyui-textbox";
			String dataoptions = "onchange=\"form_onchange()\"";
			if (StringUtil.isNotBlank(column.getSourceelementcode())) {
				inputClass = "easyui-combobox";
				dataoptions = " data-options=\"onSelect:form_onchange\" ";
			}
				
			sb.append("\" class='" + inputClass + "' " + dataoptions + " style=\"width:200px;\" ")
			.append(" /></td>");
			if (n%2 == 1)
				sb.append("</tr>");
			
			if (n > 0) {
				expcolids.append("~");
			}
			expcolids.append(column.getColumncode().toLowerCase());
			if (StringUtil.isNotBlank(column.getSourceelementcode())) {
				expCombo.append(column.getColumncode().toLowerCase()).append(",").append(column.getSourceelementcode());
				expCombo.append("~");
			}
			n++;
		}
		sb.append("`").append(expcolids.toString());
		if (expCombo.length() > 0)
			sb.append("`").append(expCombo.substring(0, expCombo.length() - 1).toString());
		return sb.toString();
	}
}

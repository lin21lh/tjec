/************************************************************
 * 类名：CalculateDatascopeBO
 *
 * 类别：组件类
 * 功能：数据库脚本管理，提供访问保存在resource/query下在数据库脚本的通用接口,供业务开发人员使用
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-4-01  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.datascope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.datascope.component.impl.DatascopeComponentImpl.ScopeType;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;



@Scope("prototype")
@Component
public class JbfConditionFilterImpl implements JbfConditionFilter {

	@Autowired
	SysDicColumnDao dicColumnDao;
	@Autowired
	SysResourceDao resourceDao;
	@Autowired
	SysDeptDao deptDao;
	
	@Override
	public String getConditionFilterById(Long resourceid, String tablecode) throws AppException {
		// TODO Auto-generated method stub
		return getConditionFilter(resourceid, tablecode, "t", ScopeType.ID);
	}
	
	@Override
	public String getConditionFilterByCode(Long resourceid, String tablecode) throws AppException {
		// TODO Auto-generated method stub
		return getConditionFilter(resourceid, tablecode, "t", ScopeType.CODE);
	}
	
	@Override
	public String getConditionFilterById(Long resourceid, String tablecode, String tableAlias) throws AppException {
		// TODO Auto-generated method stub
		return getConditionFilter(resourceid, tablecode, tableAlias, ScopeType.ID);
	}
	
	@Override
	public String getConditionFilterByCode(Long resourceid, String tablecode, String tableAlias) throws AppException {
		// TODO Auto-generated method stub
		return getConditionFilter(resourceid, tablecode, tableAlias, ScopeType.CODE);
	}
	
	


	private String getConditionFilter(Long resourceid, String tablecode, String tableAlias, int scopeType) throws AppException {
		
		SysResource menu = resourceDao.get(resourceid);
		String conditionFilters = "";
		SysUser user = SecureUtil.getCurrentUser();
		switch (menu.getDatascopemode()) {
			case 0: //无数据权限
				conditionFilters = "(1=1)";
				break;
	
			case 1: //可配置数据权限 优先级大于 默认数据权限
				
				String agencyCFilter = getAgencyCondFilter(tablecode, user.getOrgcode());
				
				String condFilter = ConditionFilter.getDataScope(resourceid, tablecode, tableAlias, scopeType);
				if (condFilter.length() > 0 && !condFilter.equals("(1=1)") && !condFilter.equals("(1=0)"))
					conditionFilters = condFilter;
				else
					conditionFilters = agencyCFilter;
				break;
			case 2:
				agencyCFilter = getAgencyCondFilter(tablecode, user.getOrgcode());
				
				condFilter = ConditionFilter.getDataScope(resourceid, tablecode, tableAlias, scopeType);
				conditionFilters = agencyCFilter;
				if (conditionFilters.length() > 0)
					conditionFilters += " and ";
				
				conditionFilters += condFilter;
				break;
			default:
				break;
		}
		
		return conditionFilters;
	}
	
	private String getAgencyCondFilter(String tablecode, String agencycode) throws AppException {
		SysDept dept = deptDao.get(agencycode);
		String agencyCondFilter = "";
		Integer agencycatcode = 0;
		switch (agencycatcode) {
		case 1: //预算单位
			String agencyKey = "bdgagencycode";
			SysDicColumn column = dicColumnDao.getColumnBySourceElement(tablecode, "BDGAGECY");
			if (column != null)
				agencyKey = column.getColumncode();
			
			agencyCondFilter = agencyKey + " like '" + agencycode + "%'";
			break;
		case 2: //本机业务科室
			break;
		case 3: //业务科室
			break;
		case 4: //下级财政部门
			break;
		default:
			break;
		}
		
		return agencyCondFilter;
	}
	
}

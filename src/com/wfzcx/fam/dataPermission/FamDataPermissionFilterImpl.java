package com.wfzcx.fam.dataPermission;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.base.datascope.component.impl.DatascopeComponentImpl.ScopeType;
import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.datascope.ConditionFilter;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.DeptComponent;

@Scope("prototype")
@Component
public class FamDataPermissionFilterImpl implements FamDataPermissionFilter {
	@Autowired
	SysDicColumnDao dicColumnDao;
	@Autowired
	SysResourceDao resourceDao;
	@Autowired
	SysDeptDao deptDao;
	
	@Override
	public String getConditionFilter(Long resourceid, String tablecode,String tableAlias) throws AppException {
		return getConditionFilter(resourceid, tablecode, tableAlias, true);
	}
	
	private String getConditionFilter(Long resourceid, String tablecode, String tableAlias, boolean isSQL) throws AppException {
		//获取菜单相关信息
		SysResource menu = resourceDao.get(resourceid);
		String conditionFilters = "";
		SysUser user = SecureUtil.getCurrentUser();
		//数据权限优先模式
		/**
		 * 数据权限优先模式
		 * 如果配置数据权限，则数据权限优先，
		 * 如果没有配置，则以默认权限，
		 * 如果默认权限没有，则以菜单设置的禁止优先或允许优先为准
		 */
		//0禁止优先：若未配置数据权限或没有通用数据权限，不能查询数据。
		//1允许优先：若未配置数据权限或没有通用数据权限，则能查询全部数据。
		//数据权限
		String condFilter = ConditionFilter.getDataScope(resourceid, tablecode, tableAlias, ScopeType.CODE);
		//默认权限
		String mrFilter =getAgencyCondFilterForPpms(tablecode); 
		//默认权限暂时为空
//		String mrFilter ="1=1"; 
		switch (menu.getDatascopemode()) {
			case 0: //禁止优先
				if(!"".equals(condFilter)) {//如果数据权限不为空，则追加条件
					conditionFilters = condFilter;
				}else if (!"".equals(mrFilter)) {//如果数据权限为空，则查询其默认权限
					conditionFilters = mrFilter;
				}else {//
					conditionFilters ="(1<>1)";
				}
				break;
			case 1: //允许优先
				if(!"".equals(condFilter)) {//如果数据权限不为空，则追加条件
					conditionFilters = condFilter;
				}else if (!"".equals(mrFilter)) {//如果数据权限为空，则查询其默认权限
					conditionFilters = mrFilter;
				}else {//
					conditionFilters ="(1=1)";
				}
				break;
			case 2: //数据权限+默认权限 and关系
				if(!"".equals(condFilter)) {
					conditionFilters ="("+condFilter +" and " +mrFilter+")";
				}else {
					conditionFilters = mrFilter;
				}
				break;
			default:
				break;
		}
		return conditionFilters;
	}
	/**
	 * 返回默认数据权限，预算单位为本级及以下，如果是业务科室则返回全部1=1，如果是银行或者其他，则返回1<>1，预算单位则返回本级及以下
	 * @Title: getAgencyCondFilter 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param tablecode
	 * @return
	 * @throws AppException 设定文件
	 */
	private String getAgencyCondFilter(String tablecode) throws AppException {
		String agencyCondFilter = "";
		String agencyKey = "bdgagencycode";
		SysDicColumn column = dicColumnDao.getColumnBySourceElement(tablecode, "BDGAGECY");
		if (column != null){
			agencyKey = column.getColumncode();
		}
		agencyCondFilter = DeptComponent.getCurAndLowerCodeForString(agencyKey);
		
		return agencyCondFilter;
	}
	/**
	 * ppp项目管理权限
	 * @Title: getAgencyCondFilterForPpms 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param tablecode
	 * @return
	 * @throws AppException 设定文件
	 */
	private String getAgencyCondFilterForPpms(String tablecode) throws AppException {
		String agencyCondFilter = "";
		String agencyKey = "orgcode";
		agencyCondFilter = DeptComponent.getCurAndLowerCodeForPpms(agencyKey);
		return agencyCondFilter;
	}
	
	public String getDefaultDataRightDetails(Long menuid) throws AppException {
		String defaultAgencyFilter = getAgencyCondFilter("SYS_DEPT");
		//获取菜单相关信息
		SysResource menu = resourceDao.get(menuid);
		String defaultFilter = "";
		switch (menu.getDatascopemode()) {
			case 0: //禁止优先
				if (StringUtil.isNotBlank(defaultAgencyFilter))
					defaultFilter = defaultAgencyFilter;
				else
					defaultFilter = "(1<>1)";
				break;
			case 1: //允许优先
				if (StringUtil.isNotBlank(defaultAgencyFilter))
					defaultFilter = defaultAgencyFilter;
				else
					defaultAgencyFilter = "(1=1)";
				break;
			default:
				break;
		}
		
		return defaultFilter;
	}
	
	public SysDept getDept(String deptcode) {
		if (StringUtil.isNotBlank(deptcode))
			return deptDao.get(deptcode);
		
		return null;
	}

}

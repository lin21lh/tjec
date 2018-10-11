package com.jbf.base.datascope.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.datascope.component.CalculateDatascopeComponent;
import com.jbf.base.datascope.component.DatascopeComponent;
import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.base.datascope.service.DataRightDetailsService;
import com.jbf.base.datascope.vo.RoleDataScopeVo;
import com.jbf.base.dic.component.DicElementViewComponent;
import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.dto.DicTreeVo;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;

@Scope("prototype")
@Service
public class DataRightDetailsServiceImpl implements DataRightDetailsService {

	@Autowired
	CalculateDatascopeComponent cDatascopeComponent;
	
	@Autowired
	DatascopeComponent datascopeComponent;
	
	@Autowired
	FamDataPermissionFilter famPermissionFilter;
	
	@Autowired
	SysDicTableDao dicTableDao;
	
	@Autowired
	DicElementViewComponent dicElementViewCp;
	
	@Autowired
	SysDicElementValSetDao dicElementValSetDao;
	
	
	public List<RoleDataScopeVo> findRoleDataRightList(Long resourceid) {
		
		SysUser user = SecureUtil.getCurrentUser();
		List<SysRoleUserResDscope> list = cDatascopeComponent.findRoleUserResDscopeList(resourceid, user.getUserid());
		List<RoleDataScopeVo> rdlist = new ArrayList<RoleDataScopeVo>(list.size());
		SysRole role = null;
		for (SysRoleUserResDscope rurd : list) {
			RoleDataScopeVo rdsVo = new RoleDataScopeVo();
			role = cDatascopeComponent.getRoleByRoleID(rurd.getRoleid());
			rdsVo.setId(role.getRoleid().toString());
			rdsVo.setPid(role.getParentroleid() != null ? role.getParentroleid().toString() : "0");
			rdsVo.setText(role.getRolecode() + "-" + role.getRolename());
			rdsVo.setLevelno(role.getLevelno());
			rdsVo.setScopemainid(rurd.getScopemainid());
			rdsVo.setScopemainname(datascopeComponent.getDatascopemainName(rurd.getScopemainid()));
			
			rdlist.add(rdsVo);
		}
		
		return toBornTreeRoleVo(rdlist, "0");
	}
	
	@Override
	public HashMap<String, Object> getDefDataRightDetails(Long resourceid) throws AppException {
		// TODO Auto-generated method stub
		String defaultFilter = famPermissionFilter.getDefaultDataRightDetails(resourceid);
		SysUser user = SecureUtil.getCurrentUser();
		SysDept dept = famPermissionFilter.getDept(user.getOrgcode());
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String filterMsg = "";
		if ("(1<>1)".equals(defaultFilter)) {
			filterMsg = "当前登录用户:[<font style='color:red'>" + user.getUsercode() + "-" + user.getUsername() + "</font>]为[<font style='color:red'>" + dept.getAgencycatname() + "</font>]人员,没有权限查看单位数据。";
			resultMap.put("isTree", false);
		} else if ("(1=1)".equals(defaultFilter)) {
			filterMsg = "当前登录用户:[<font style='color:red'>" + user.getUsercode() + "-" + user.getUsername() + "</font>]为[<font style='color:red'>" + dept.getAgencycatname() + "</font>]人员,拥有权限查看所有单位的数据。";
			resultMap.put("isTree", false);
		} else {
			List<DicTreeVo> treeList = getDefaultAgencyFilter(defaultFilter);
			resultMap.put("treeList", treeList);
			resultMap.put("isTree", true);
		}
		resultMap.put("filterMsg", filterMsg);
		return resultMap;
	}
	
	public List<DicTreeVo> getDefaultAgencyFilter(String defaultAgencyFilter) throws AppException {

		SysDicTable tablePO = dicTableDao.getByTablecode("SYS_DEPT");
		//校验表 对应字段是否已定义
		String[] args = {tablePO.getTablecode()+"-"+tablePO.getTablename()};
		if (StringUtil.isBlank(tablePO.getKeycolumn()))
			throw new AppException("datatable.keycolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getCodecolumn()))
			throw new AppException("datatable.codecolumn.undefined", args);
		
		if (StringUtil.isBlank(tablePO.getNamecolumn()))
			throw new AppException("datatable.namecolumn.undefined", args);
		
		String viewFilterSql = dicElementViewCp.getSqlString("BDGAGENCY");
		if (StringUtil.isNotBlank(viewFilterSql))
			viewFilterSql += " and ";
		viewFilterSql += defaultAgencyFilter.replaceAll("bdgagencycode", tablePO.getCodecolumn());
		List<Map> dteVals = dicElementValSetDao.queryDicElementVals(tablePO, "", viewFilterSql, null, null, null);

		DicTreeVo tree = null;
		List<DicTreeVo> treeList = new ArrayList<DicTreeVo>();
		
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
			
			tree.setChecked(true);
			tree.setIsChecked("1");
				
			
			treeList.add(tree);
		}
		treeList = (List<DicTreeVo>) TreeVoUtil.toBornTree2(treeList, "0", true);
		return treeList;
	
	}
	
	
	/**
	 * 如果没有上级 则展示
	 * @param list
	 * @param rootid
	 * @param isnoExists
	 * @return
	 */
	public static List<RoleDataScopeVo> toBornTreeRoleVo(List<RoleDataScopeVo> list, String rootid) {
		List<RoleDataScopeVo> rootList = new ArrayList<RoleDataScopeVo>();
		RoleDataScopeVo treedata = null;
		
		Map<String, List<RoleDataScopeVo>> branchsMap = new HashMap<String, List<RoleDataScopeVo>>(60, 0.5f);
		for (RoleDataScopeVo treeData : list) {
			List<RoleDataScopeVo> list2 = branchsMap.get(treeData.getPid());
			if(list2 == null){
				list2 = new ArrayList<RoleDataScopeVo>();
				branchsMap.put(treeData.getPid(), list2);
			}
			list2.add(treeData);
		}
		for (RoleDataScopeVo treeData : list) {
			  
			List<RoleDataScopeVo> trees = branchsMap.get(treeData.getId());
			  if(trees != null && trees.size() > 0){				  
				  treeData.setIsLeaf(false);
				  treeData.setState("closed");
				  treeData.setChildren(trees);
				  branchsMap.remove(treeData.getId());
			  } else {
				  treeData.setIsLeaf(true);
				  treeData.setState("open");
			  }
			
		}
		
		Collection<List<RoleDataScopeVo>> root=branchsMap.values();
		
		for (List<RoleDataScopeVo> key : root) {
			rootList.addAll(key);
		}
		// Collections.sort(rootList, treeCode);
		return rootList;
	}
	
//	final static Comparator<RoleDataScopeVo> treeCode=new Comparator<RoleDataScopeVo>(){
//		public int compare(RoleDataScopeVo o1, RoleDataScopeVo o2) {
//			return o1.getCode().compareTo(o2.getCode());
//		}
//	};

}

/************************************************************
 * 类名：DatascopeServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：数据权限服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.datascope.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.datascope.dao.SysDatascopeItemDao;
import com.jbf.base.datascope.dao.SysDatascopeMainDao;
import com.jbf.base.datascope.dao.SysDatascopeSubDao;
import com.jbf.base.datascope.dao.SysRoleUserResDscopeDao;
import com.jbf.base.datascope.dao.SysScopevalueDao;
import com.jbf.base.datascope.po.SysDatascopeitem;
import com.jbf.base.datascope.po.SysDatascopemain;
import com.jbf.base.datascope.po.SysDatascopesub;
import com.jbf.base.datascope.po.SysRoleUserResDscope;
import com.jbf.base.datascope.po.SysScopevalue;
import com.jbf.base.datascope.service.DatascopeService;
import com.jbf.base.dic.util.DicFind;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.JsonUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.role.dao.SysRoleDao;
import com.jbf.sys.role.po.SysRole;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.dao.SysWorkflowDeccondsDao;
import com.jbf.workflow.po.SysWorkflowDecConds;



@Scope("prototype")
@Service
public class DatascopeServiceImpl implements DatascopeService {

	@Autowired
	SysDatascopeMainDao datascopeMainDao;
	@Autowired
	SysDatascopeSubDao datascopeSubDao;
	@Autowired
	SysDatascopeItemDao datascopeItemDao;
	@Autowired
	SysScopevalueDao scopevalueDao;
	@Autowired
	SysRoleUserResDscopeDao roleUserResDscopeDao;
	@Autowired
	SysRoleDao roleDao;
	@Autowired
	SysUserDao userDao;
	@Autowired
	SysResourceDao resourceDao;
	@Autowired
	SysWorkflowDeccondsDao wfDeccondsDao;
	
	@Override
	public void save(SysRoleUserResDscope roleUserResDscope) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria = dcCriteria.add(Property.forName("roleid").eq(roleUserResDscope.getRoleid()));
		if (Byte.valueOf("0").equals(roleUserResDscope.getIsallmenu()))
			dcCriteria = dcCriteria.add(Property.forName("resourceid").eq(roleUserResDscope.getResourceid()));
		
		if (Byte.valueOf("0").equals(roleUserResDscope.getUserid()))
			dcCriteria = dcCriteria.add(Property.forName("userid").eq(roleUserResDscope.getUserid()));
		
		List<SysRoleUserResDscope> list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		if (list.size() > 0) {
			roleUserResDscope.setCreatedate(list.get(0).getCreatedate());
			roleUserResDscope.setRelationid(list.get(0).getRelationid());
			roleUserResDscopeDao.update(roleUserResDscope);
		} else {
			roleUserResDscope.setCreatedate(DateUtil.getCurrentDate());
			roleUserResDscopeDao.save(roleUserResDscope);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveDatascope(String text) throws Exception {
		Map value = (Map) JsonUtil.createObjectByJsonString(text, Map.class);
		String scopemainname = (String)value.get("scopemainname");
		String scopemainidStr = (String)value.get("scopemainid");
		SysDatascopemain datascopemain = new SysDatascopemain(scopemainname, "DATASCOPE", DateUtil.getCurrentDate());
		Long scopemainid = null;
		if (scopemainidStr != null && scopemainidStr.length() > 0) {
			scopemainid = Long.valueOf(scopemainidStr);
			datascopemain.setScopemainid(scopemainid);
			datascopeMainDao.update(datascopemain);
			
			List<SysDatascopesub> oldSubList = datascopeSubDao.findscopesubByscopemainID(scopemainid);
			for (SysDatascopesub scopesub : oldSubList) {
				List<SysDatascopeitem> olditemList = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
				for (SysDatascopeitem scopeitem : olditemList) {
					List<SysScopevalue> oldscopeValList = scopevalueDao.findScopevalueByScopeitem(scopeitem.getScopeitemid());
					scopevalueDao.deleteAll(oldscopeValList);
				}
				datascopeItemDao.deleteAll(olditemList);
			}
			datascopeSubDao.deleteAll(oldSubList);
		} else {
			scopemainid = datascopeMainDao.saveDatascopeMain(datascopemain);
		}
		List<Map> subList = (List<Map>)value.get("subs");
		int seqno = 1;
		SysDatascopesub datascopesub = null;
		for (Map subMap : subList) {
			String scopesubname= (String)subMap.get("scopesubname");
			datascopesub = new SysDatascopesub(scopemainid, scopesubname, seqno, DateUtil.getCurrentDate());
			Long scopesubid = datascopeSubDao.saveDatascopesub(datascopesub);
			List<Map> itemList = (List<Map>)subMap.get("items");
			SysDatascopeitem datascopeitem = null;
			for (Map itemMap : itemList) {
				datascopeitem = new SysDatascopeitem();
				BeanUtils.populate(datascopeitem, itemMap);
				datascopeitem.setCreatedate(DateUtil.getCurrentDate());
				datascopeitem.setScopesubid(scopesubid);
				Long scopeitemid = (Long)datascopeItemDao.save(datascopeitem);
				String scopevalues = (String)itemMap.get("scopevalues");
				datascopeitem.getElementcode();
				if (StringUtil.isNotBlank(scopevalues)) {
					String[] scopeVals = scopevalues.split(",");
					SysScopevalue scopevalue = null;
					for (String scopeVal : scopeVals) {
						scopevalue = new SysScopevalue();
						scopevalue.setValueid(Long.valueOf(scopeVal));
						scopevalue.setValuecode(DicFind.findDicCodeByIdElementcode(datascopeitem.getElementcode(), scopevalue.getValueid()));
						scopevalue.setScopeitemid(scopeitemid);
						scopevalueDao.save(scopevalue);
					}
				}

			}
			seqno ++;
		}
	}

	@Override
	public List findDataScopeMain() {
		
		return datascopeMainDao.findDataScopeMain();
	}

	@Override
	public HashMap<String, Object> getDataScopeDetailByID(Long scopemainid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		SysDatascopemain dsmain = datascopeMainDao.get(scopemainid);
		List<SysDatascopesub> scopesubList = datascopeSubDao.findscopesubByscopemainID(scopemainid);
		
		List<Map> ssubList = new ArrayList<Map>(scopesubList.size());
		Map scopesubMap = null;
		List<Map> sitemList = null;
		Map scopeitemMap = null;
		for (SysDatascopesub scopesub : scopesubList) {
			scopesubMap = BeanUtils.describe(scopesub);
			List<SysDatascopeitem> itemList = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
			sitemList = new ArrayList<Map>(itemList.size());
			if (itemList != null && itemList.size() > 0) {
				for (SysDatascopeitem datascopeitem : itemList) {
					scopeitemMap = BeanUtils.describe(datascopeitem);
					sitemList.add(scopeitemMap);
				}
			}
			scopesubMap.put("itemList", sitemList);
			ssubList.add(scopesubMap);
		}
		
		HashMap<String, Object> dsmainMap = (HashMap<String, Object>) BeanUtils.describe(dsmain);
		dsmainMap.put("subList", ssubList);
		
		return dsmainMap;
	}
	
	public HashMap<String, Object> getDataScopeDetail(Long roleid, Integer isallmenu, Long resourceid, Integer isalluser, Long userid) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria = dcCriteria.add(Property.forName("roleid").eq(roleid));
		if (Integer.valueOf("0").equals(isallmenu))
			dcCriteria = dcCriteria.add(Property.forName("resourceid").eq(resourceid));
		if (Integer.valueOf("0").equals(isalluser))
			dcCriteria = dcCriteria.add(Property.forName("userid").eq(userid));
		
		List<SysRoleUserResDscope> list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (list != null && list.size() > 0) {
			SysRoleUserResDscope roleUserResDscope = list.get(0);
			resultMap = getDataScopeDetailByID(roleUserResDscope.getScopemainid());
			resultMap.put("escopemainid", roleUserResDscope.getScopemainid());
		}
		return resultMap;
	}
	
	public SysRoleUserResDscope getByRole(Long roleid) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria = dcCriteria.add(Property.forName("roleid").eq(roleid));
		dcCriteria = dcCriteria.addOrder(Order.desc("relationid"));
		List<SysRoleUserResDscope> list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public String deleteDataScope(Long scopemainid) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria.add(Property.forName("scopemainid").eq(scopemainid));
		List<SysRoleUserResDscope> dlist = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		if (dlist != null && dlist.size() > 0) 
			return AppException.getMessage("datascope.del.fail.fp");
		
		SysDatascopemain scopemain = datascopeMainDao.get(scopemainid);
		List<SysDatascopesub> subList = datascopeSubDao.findscopesubByscopemainID(scopemainid);
		for (SysDatascopesub scopesub : subList) {
			List<SysDatascopeitem> itemList = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
			datascopeItemDao.deleteAll(itemList);
		}
		datascopeSubDao.deleteAll(subList);
		datascopeMainDao.delete(scopemain);
		
		return "";
	}

	public HashMap<String, Object> deleteDataScopeRelation(Long roleid, Integer isallmenu, Long resourceid, Integer isalluser, Long userid) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria = dcCriteria.add(Property.forName("roleid").eq(roleid));

		if (isallmenu == 0)
			dcCriteria = dcCriteria.add(Property.forName("resourceid").eq(resourceid));
		
		if (isalluser == 0)
			dcCriteria = dcCriteria.add(Property.forName("userid").eq(userid));
		List<SysRoleUserResDscope> list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		if (list.size() > 0)
			roleUserResDscopeDao.delete(list.get(0));
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dc = dc.add(Property.forName("roleid").eq(roleid));
		 list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dc);
		 HashMap<String, Object> body = new HashMap<String, Object>();
		 if (list.size() > 0)
			 body.put("isExistDscope", 1);
		 else
			 body.put("isExistDscope", 0);
		 
		 return body;
	}
	
	public List findRelationByScopemainID(Long scopemainid) {
		
		DetachedCriteria dcCriteria = DetachedCriteria.forClass(SysRoleUserResDscope.class);
		dcCriteria.add(Property.forName("scopemainid").eq(scopemainid));
		dcCriteria.addOrder(Order.asc("relationid"));
		List<SysRoleUserResDscope> list = (List<SysRoleUserResDscope>)roleUserResDscopeDao.findByCriteria(dcCriteria);
		Map<String, String> values = null;
		List resList = new ArrayList(list.size());
		SysRole role = null;
		SysUser user = null;
		SysResource resource = null;
		for (SysRoleUserResDscope rur : list) {
			values = new HashMap<String, String>();
			role = roleDao.get(rur.getRoleid());
			values.put("rolecode", role.getRolecode() + "-" + role.getRolename());
			if (rur.getIsalluser().equals(Byte.valueOf("0"))) {
				user = userDao.get(rur.getUserid());
				if (user != null)
					values.put("usercode", user.getUsercode() + "-" + user.getUsername());
				else
					values.put("usercode", "");
			} else
				values.put("usercode", "");
			
			if (rur.getIsallmenu().equals(Byte.valueOf("0"))) {
				resource = resourceDao.get(rur.getResourceid());
				if (resource != null)
					values.put("resourcecode", resource.getName());
				else
					values.put("resourcecode", "");
			} else
				values.put("resourcecode", "");
			
			resList.add(values);
		}
		
		return resList;
	}
	
	public void saveWFScope(String text) throws Exception {
		Map value = (Map) JsonUtil.createObjectByJsonString(text, Map.class);
		String wfkey = (String)value.get("wfkey");
		String wfversion = (String)value.get("wfversion");
		String decisionname = (String)value.get("decisionname");
		String taskname = (String)value.get("taskname");
		String scopemainidStr = (String)value.get("scopemainid");
		SysDatascopemain datascopemain = new SysDatascopemain("工作流key：" + wfkey + "，版本：" + wfversion + "执行条件", "WFSCOPE", DateUtil.getCurrentDate());
		Long scopemainid = null;
		if (scopemainidStr != null && scopemainidStr.length() > 0) {
			scopemainid = Long.valueOf(scopemainidStr);
			datascopemain.setScopemainid(scopemainid);
			datascopeMainDao.update(datascopemain);
			
			List<SysDatascopesub> oldSubList = datascopeSubDao.findscopesubByscopemainID(scopemainid);
			for (SysDatascopesub scopesub : oldSubList) {
				List<SysDatascopeitem> olditemList = datascopeItemDao.findDsitemByscopesubID(scopesub.getScopesubid());
				for (SysDatascopeitem scopeitem : olditemList) {
					List<SysScopevalue> oldscopeValList = scopevalueDao.findScopevalueByScopeitem(scopeitem.getScopeitemid());
					scopevalueDao.deleteAll(oldscopeValList);
				}
				datascopeItemDao.deleteAll(olditemList);
			}
			datascopeSubDao.deleteAll(oldSubList);
		} else {
			scopemainid = datascopeMainDao.saveDatascopeMain(datascopemain);
			
			SysWorkflowDecConds wfDecConds = new SysWorkflowDecConds();
			wfDecConds.setWfkey(wfkey);
			wfDecConds.setWfversion(Integer.valueOf(wfversion));
			wfDecConds.setDecisionname(decisionname);
			wfDecConds.setTaskname(taskname);
			wfDecConds.setScopemainid(scopemainid);
			
			wfDeccondsDao.save(wfDecConds);
		}
		List<Map> subList = (List<Map>)value.get("subs");
		int seqno = 1;
		SysDatascopesub datascopesub = null;
		for (Map subMap : subList) {
			String scopesubname= (String)subMap.get("scopesubname");
			datascopesub = new SysDatascopesub(scopemainid, scopesubname, seqno, DateUtil.getCurrentDate());
			Long scopesubid = datascopeSubDao.saveDatascopesub(datascopesub);
			List<Map> itemList = (List<Map>)subMap.get("items");
			SysDatascopeitem datascopeitem = null;
			for (Map itemMap : itemList) {
				datascopeitem = new SysDatascopeitem();
				BeanUtils.populate(datascopeitem, itemMap);
				datascopeitem.setCreatedate(DateUtil.getCurrentDate());
				datascopeitem.setScopesubid(scopesubid);
				Long scopeitemid = (Long)datascopeItemDao.save(datascopeitem);
				String scopevalues = (String)itemMap.get("scopevalues");
				String[] scopeVals = scopevalues.split(",");
				SysScopevalue scopevalue = null;
				for (String scopeVal : scopeVals) {
					scopevalue = new SysScopevalue();
					scopevalue.setValueid(Long.valueOf(scopeVal));
					scopevalue.setScopeitemid(scopeitemid);
					scopevalue.setValuecode(DicFind.findDicCodeByIdElementcode(datascopeitem.getElementcode(), scopevalue.getValueid()));
					scopevalueDao.save(scopevalue);
				}
			}
			seqno ++;
		}
	}
	
	public HashMap<String, Object> getWFDataScope(String wfkey, Integer wfversion, String decisionname, String taskname) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		SysWorkflowDecConds wfDecConds = wfDeccondsDao.getWfDecCond(wfkey, wfversion, decisionname, taskname);
		if (wfDecConds == null)
			return resultMap;
		
		resultMap = getDataScopeDetailByID(wfDecConds.getScopemainid());
		resultMap.put("escopemainid", wfDecConds.getScopemainid());
		
		return resultMap;
	}
}

/************************************************************
 * 类名：SysWorkflowDeccondsServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
//import com.jbf.common.datascope.DatascopeBO;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.dao.SysWorkflowDeccondsDao;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.po.SysWorkflowDecConds;
import com.jbf.workflow.service.SysWorkflowDeccondsService;

@Scope("prototype")
@Service("com.jbf.workflow.service.impl.SysWorkflowDeccondsServiceImpl")
public class SysWorkflowDeccondsServiceImpl implements
		SysWorkflowDeccondsService {

	@Autowired
	SysWorkflowDeccondsDao wfDeccondsDao;

	@Autowired
	SysWorkflowProcdefDao wfProcdefDao;

	@Autowired
	SysDicTableDao dicTableDao;

	// @Autowired
	// DatascopeBO datascopeBO;

	public Map<String, Object> getWfTask(String wfkey, Integer wfversion,
			String decisionname, Long billid) throws AppException {

		SysUser user = SecureUtil.getCurrentUser();
		List<SysWorkflowDecConds> list = wfDeccondsDao.finWfDecConds(wfkey,
				wfversion, decisionname);
		String tablecode = wfProcdefDao.getTabcodeByKey(wfkey);
		List<String> taskList = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (SysWorkflowDecConds wfDecCond : list) {
			taskList.add(wfDecCond.getTaskname());
			if (toTaskWf(wfDecCond.getScopemainid(), tablecode,
					user.getUserid(), billid)) {
				map.put("totask", wfDecCond.getTaskname());
				return map;
			}
		}

		map.put("alltask", taskList);

		return map;
	}

	boolean toTaskWf(Long scopemainid, String tablecode, Long userid,
			Long billid) throws AppException {

		// String sql = datascopeBO.getDynamicSQLExpression(scopemainid, "t",
		// tablecode, userid, true);
		//
		// SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		// if (dicTable == null)
		// throw new AppException("datatable.undefined", new String[]
		// {tablecode});
		//
		// if (StringUtil.isBlank(dicTable.getKeycolumn()))
		// throw new AppException("datatable.keycolumn.undefined", new String[]
		// {tablecode});
		//
		// String where = dicTable.getKeycolumn() + "=" + billid;
		// if (StringUtil.isNotBlank(sql))
		// where += " and " + sql;
		//
		// List dataList = wfDeccondsDao.queryBySQL(tablecode, where);
		//
		// return dataList != null && dataList.size() > 0;
		return false;
	}
}

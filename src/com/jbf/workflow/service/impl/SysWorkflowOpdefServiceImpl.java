/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：ServiceImpl
 * 功能：工作流操作定义服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.exception.AppException;
import com.jbf.workflow.dao.SysWorkflowOpdefDao;
import com.jbf.workflow.po.SysWorkflowOpdef;
import com.jbf.workflow.service.SysWorkflowOpdefService;

@Scope("prototype")
@Service
public class SysWorkflowOpdefServiceImpl implements SysWorkflowOpdefService {
	@Autowired
	SysWorkflowOpdefDao sysWorkflowOpdefDao;

	@Override
	public List<SysWorkflowOpdef> query(String key) {

		return (List<SysWorkflowOpdef>) sysWorkflowOpdefDao.find(
				" from SysWorkflowOpdef where key= ?", key);
	}

	@Override
	public SysWorkflowOpdef get(Long id) {

		return sysWorkflowOpdefDao.get(id);

	}

	public void del(Long id) throws Exception {
		SysWorkflowOpdef o = sysWorkflowOpdefDao.get(id);
		if (o != null) {
			sysWorkflowOpdefDao.delete(o);
		} else {
			throw new AppException("crud.delerr");
		}
	}

	@Override
	public void save(SysWorkflowOpdef workflowOpdef) {
		if (workflowOpdef.getId() == null) {
			sysWorkflowOpdefDao.save(workflowOpdef);
		} else {
			sysWorkflowOpdefDao.update(workflowOpdef);
		}
	}

}

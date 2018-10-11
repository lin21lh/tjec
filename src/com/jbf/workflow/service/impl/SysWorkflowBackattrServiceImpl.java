/************************************************************
 * 类名：SysWorkflowBackattrServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：回退属性服务实现 
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

import com.jbf.workflow.dao.SysWorkflowBackattrDao;
import com.jbf.workflow.service.SysWorkflowBackattrService;

@Scope("prototype")
@Service
public class SysWorkflowBackattrServiceImpl implements
		SysWorkflowBackattrService {

	@Autowired
	SysWorkflowBackattrDao sysWorkflowBackattrDao;

	@Override
	public List queryBackAttr(String key, Integer version) {
		return sysWorkflowBackattrDao.find(
				" from SysWorkflowBackattr where key= ? and version = ?", key,
				version);
	}

}

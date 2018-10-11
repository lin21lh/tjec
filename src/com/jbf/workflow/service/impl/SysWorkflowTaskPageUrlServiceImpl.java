/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：ServiceImpl
 * 功能：工作流任务节点对应页面的服务实现
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
import org.springframework.stereotype.Service;

import com.jbf.workflow.dao.SysWorkflowTaskPageUrlDao;
import com.jbf.workflow.service.SysWorkflowTaskPageUrlService;

@Service
public class SysWorkflowTaskPageUrlServiceImpl implements
		SysWorkflowTaskPageUrlService {

	@Autowired
	SysWorkflowTaskPageUrlDao sysWorkflowTaskPageUrlDao;

	@Override
	public List queryTaskPageUrl(String key, Integer version) {
		return sysWorkflowTaskPageUrlDao.find(
				" from SysWorkflowTaskPageUrl where key= ? and version= ? ",
				key, version);
	}

}

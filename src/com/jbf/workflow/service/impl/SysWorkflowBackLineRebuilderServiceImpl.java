/************************************************************
 * 类名：SysWorkflowBackLineRebuilderServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：工作流退回撤回路线重建服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.util.List;

import org.jbpm.api.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.workflow.common.WfLineRebuildCmd;
import com.jbf.workflow.dao.SysWorkflowBacklineRecDao;
import com.jbf.workflow.po.SysWorkflowBacklineRec;
import com.jbf.workflow.service.SysWorkflowBackLineRebuilderService;

@Service
public class SysWorkflowBackLineRebuilderServiceImpl implements
		SysWorkflowBackLineRebuilderService {

	@Autowired
	SysWorkflowBacklineRecDao sysWorkflowBacklineRecDao;
	@Autowired
	ProcessEngine processEngine;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void build() {
		// 查询需要建立回退的配置

		List<SysWorkflowBacklineRec> lines = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec order by key ,version");
		System.out.println("信息：工作流中已注册的退回路径条数:" + lines.size() + "!");
		WfLineRebuildCmd command = new WfLineRebuildCmd(lines);
		processEngine.execute(command);
	}

}

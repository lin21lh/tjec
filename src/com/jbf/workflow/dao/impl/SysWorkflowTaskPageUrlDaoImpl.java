/************************************************************
 * 类名：WorkflowTaskPageUrl
 *
 * 类别：DaoImpl
 * 功能：任务对应的处理页面DaoImpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-23  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/

package com.jbf.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowTaskPageUrlDao;
import com.jbf.workflow.po.SysWorkflowTaskPageUrl;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowTaskPageUrlDaoImpl")
class SysWorkflowTaskPageUrlDaoImpl extends
		GenericDao<SysWorkflowTaskPageUrl, Long> implements
		SysWorkflowTaskPageUrlDao {

}

/************************************************************
 * 类名：WorkflowBacklineRec
 *
 * 类别：DaoImpl
 * 功能：工作流回退线路记录DaoImpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-24  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/

package com.jbf.workflow.dao.impl;

import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.workflow.dao.SysWorkflowBacklineRecDao;
import com.jbf.workflow.po.SysWorkflowBacklineRec;

@Repository("com.jbf.workflow.dao.impl.SysWorkflowBacklineRecDaoImpl")
public class SysWorkflowBacklineRecDaoImpl extends
		GenericDao<SysWorkflowBacklineRec, Long> implements
		SysWorkflowBacklineRecDao {

}

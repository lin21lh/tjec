/************************************************************
 * 类名：WfDecisionBusinessHandler
 *
 * 类别：decision业务处理器接口
 * 功能：实现分支裁定功能(业务处理器实现该接口)
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-05-08  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import java.util.Map;

import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.vo.ProcessDefinitionVO;

public interface WfDecisionBusinessHandler {
	
	/**
	 * 流出路径裁定
	 * 
	 * @param execId
	 *            流程实例ID
	 * @param actiId
	 *            当前节点ID
	 * @param variables
	 *            环境变量
	 * @param user
	 *            处理用户
	 * @param def
	 *            流程定义信息
	 * @return 流出路径名
	 */
	public String decide(String execId, String actiId, Map variables,
			SysUser user, ProcessDefinitionVO def);
}

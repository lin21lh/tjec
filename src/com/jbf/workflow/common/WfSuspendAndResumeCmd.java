/************************************************************
 * 类名：WfSuspendAndResumeCmd
 *
 * 类别：jbpm命令
 * 功能：jbpm工作流流程挂起、解挂命令
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-05-04  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.ExecutionImpl;

import com.jbf.common.web.ResultMsg;

public class WfSuspendAndResumeCmd implements Command<ResultMsg> {

	// 挂起操作
	public static final int SUSPEND = 0;

	// 解挂操作
	public static final int RESUME = 1;

	// 工作流实例ID
	String execId;
	// 操作类型
	int operatation;

	public WfSuspendAndResumeCmd(String execId, int operatation) {
		this.execId = execId;
		this.operatation = operatation;
	}

	@Override
	public ResultMsg execute(Environment environment) throws Exception {
		if (operatation == SUSPEND) {
			return suspend(environment);
		} else if (operatation == RESUME) {
			return resume(environment);
		} else {
			return new ResultMsg(false, "错误的挂起或解挂类型:" + operatation);
		}
	}

	private ResultMsg suspend(Environment environment) {
		ExecutionService es = environment.get(ExecutionService.class);
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();

		if (pi == null) {
			return new ResultMsg(false, "流程不存在，或能由于流程已结束！");
		}
		if (pi.isSuspended()) {
			return new ResultMsg(false, "流程实例已处于挂起状态！");
		}
		try {
			((ExecutionImpl) pi).suspend();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "流程挂起操作过程中发生异常！");
		}
		return new ResultMsg(true, "流程挂起操作成功！");
	}

	private ResultMsg resume(Environment environment) {
		ExecutionService es = environment.get(ExecutionService.class);
		ProcessInstance pi = es.createProcessInstanceQuery()
				.processInstanceId(execId).uniqueResult();
		if (pi == null) {
			return new ResultMsg(false, "流程不存在，或能由于流程已结束！");
		}

		if (pi.isSuspended()) {
			try {
				((ExecutionImpl) pi).resume();
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultMsg(false, "流程挂起操作过程中发生异常！");
			}
		} else {
			return new ResultMsg(false, "流程实例未处于挂起状态，操作失败！");
		}
		return new ResultMsg(true, "流程解挂操作成功！");

	}

	public String getExecId() {
		return execId;
	}

	public void setExecId(String execId) {
		this.execId = execId;
	}

	public int getOperatation() {
		return operatation;
	}

	public void setOperatation(int operatation) {
		this.operatation = operatation;
	}

}

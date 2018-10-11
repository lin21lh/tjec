/************************************************************
 * 类名：WfLineRebuildCmd
 *
 * 类别：jbpm命令
 * 功能：实现应用服务器启动时，将保存的动态线路重建
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import java.util.ArrayList;
import java.util.List;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;

import com.jbf.workflow.po.SysWorkflowBacklineRec;

public class WfLineRebuildCmd implements Command {

	List<SysWorkflowBacklineRec> lines;
	Integer pointer = 0;

	public WfLineRebuildCmd(List<SysWorkflowBacklineRec> lines) {
		this.lines = lines;
	}

	/**
	 * 命令执行
	 */
	@Override
	public Object execute(Environment enviroment) throws Exception {
		RepositoryService rs = enviroment.get(RepositoryService.class);
		List<SysWorkflowBacklineRec> rec;
		while ((rec = next()) != null) {
			createLines(rec, rs);
		}
		return null;
	}

	/**
	 * 创建回退路径
	 * 
	 * @param rec
	 *            回退路径
	 * @param rs
	 *            RepositoryService
	 */
	private void createLines(List<SysWorkflowBacklineRec> rec,
			RepositoryService rs) {
		// 查询流程定义
		ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
		pdq.processDefinitionKey(rec.get(0).getKey());
		List<ProcessDefinition> pdlist = pdq.list();

		Integer version = rec.get(0).getVersion();
		ProcessDefinition tgtPd = null;
		for (ProcessDefinition pd : pdlist) {
			if (pd.getVersion() == version) {
				tgtPd = pd;
				break;
			}
		}
		ProcessDefinitionImpl pi = (ProcessDefinitionImpl) tgtPd;
		for (SysWorkflowBacklineRec r : rec) {
			System.out.println("正在动态建立由" + r.getSrcacti() + "至"
					+ r.getTgtacti() + "的路径，路径名："+r.getTransname()+"，KEY:" + tgtPd.getKey()
					+ "，Version:" + tgtPd.getVersion());

			ActivityImpl from = pi.findActivity(r.getSrcacti());
			ActivityImpl to = pi.findActivity(r.getTgtacti());

			if (from == null) {
				System.out.println("错误：Activity'" + r.getSrcacti() + "'不存在!");
				continue;
			}

			if (to == null) {
				System.out.println("错误：Activity'" + r.getTgtacti() + "'不存在!");
				continue;
			}
			TransitionImpl t = from.createOutgoingTransition();
			t.setDestination(to);
			t.setName(r.getTransname());

		}

	}

	/**
	 * 取得下一组回退线，以key,version进行分组<br>
	 * lines结果集已使用key,version进行排序，相同的key,version的记录是相邻的，是同一组
	 * 
	 * @return 具有相同的key,version的一组回退线
	 */
	private List<SysWorkflowBacklineRec> next() {
		String key = null;
		Integer version = null;
		List<SysWorkflowBacklineRec> locrec = new ArrayList<SysWorkflowBacklineRec>();
		for (int i = pointer; i < lines.size(); i++) {
			if (key == null && version == null) {
				key = lines.get(i).getKey();
				version = lines.get(i).getVersion();
				locrec.add(lines.get(i));
				pointer = new Integer(i + 1);
			} else {
				if (!key.equals(lines.get(i).getKey())) {
					pointer = new Integer(i);
					break;
				}
				if (!version.equals(lines.get(i).getVersion())) {
					pointer = new Integer(i);
					break;
				}
				locrec.add(lines.get(i));
			}
		}
		if (locrec.size() == 0) {
			return null;
		} else {
			return locrec;
		}
	}

}

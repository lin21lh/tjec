/************************************************************
 * 类名：WfAssignmentHandler
 *
 * 类别：受托分配器
 * 功能：jbpm工作流受托人分配器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.api.task.Assignable;
import org.jbpm.api.task.AssignmentHandler;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.springframework.beans.factory.BeanFactory;

import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.po.SysWorkflowExtAttr;

public class WfAssignmentHandler implements AssignmentHandler {

	private static final long serialVersionUID = 1L;

	// @Override
	// public void assign(Assignable assignable, OpenExecution execution)
	// throws Exception {
	// // 取得Spring的BeanFactory 以便使用任意的 spring组件
	// BeanFactory bf = BeanFactoryHelper.getBeanFactory();
	// SysUserRoleDao sysUserRoleDao = (SysUserRoleDao) bf
	// .getBean("sys.user.dao.sysUserRoleDao");
	// SysWorkflowPrivilegeDao sysWorkflowPrivilegeDao =
	// (SysWorkflowPrivilegeDao) bf
	// .getBean("com.jbf.workflow.dao.impl.SysWorkflowPrivaligeDaoImpl");
	//
	// String procDefId = execution.getProcessDefinitionId();
	//
	// RepositoryService repositoryService = (RepositoryService) EnvironmentImpl
	// .getFromCurrent(RepositoryService.class);
	//
	// ProcessDefinitionQuery pdq = repositoryService
	// .createProcessDefinitionQuery();
	//
	// pdq.processDefinitionId(procDefId);
	// ProcessDefinition pd = pdq.uniqueResult();
	// String deploymentId = pd.getDeploymentId();
	//
	// String activityName = execution.getActivity().getName();
	//
	// // 取得流程活动节点对应的role
	// Long roleId = getRoleId(deploymentId, activityName,
	// sysWorkflowPrivilegeDao);
	//
	// // 取得对应的user
	// List<String> userNames = getUserNames(roleId, sysUserRoleDao);
	//
	// if (userNames == null || userNames.size() == 0) {
	// throw new Exception("无法找到角色对应的用户!");
	// }
	// if (userNames.size() == 1) {
	// // 如果角色里面只有一个人，则直接交由这个人处理
	// assignable.setAssignee((String) userNames.get(0));
	// } else {
	// // 如果角色里有多个人，则这些人作为任务候选者
	// for (String user : userNames) {
	// assignable.addCandidateUser(user);
	// }
	// }
	// }

	@Override
	public void assign(Assignable assignable, OpenExecution execution)
			throws Exception {
		// 取得Spring的BeanFactory 以便使用任意的 spring组件
		BeanFactory bf = BeanFactoryHelper.getBeanFactory();

		// 查询得到流程的所在节

		String procDefId = execution.getProcessDefinitionId();

		RepositoryService repositoryService = (RepositoryService) EnvironmentImpl
				.getFromCurrent(RepositoryService.class);

		ProcessDefinitionQuery pdq = repositoryService
				.createProcessDefinitionQuery();

		pdq.processDefinitionId(procDefId);
		ProcessDefinition pd = pdq.uniqueResult();

		String key = pd.getKey();
		int version = pd.getVersion();
		String actiId = execution.getActivity().getName();

		SysWorkflowExtAttrDao sysWorkflowExtAttrDao = (SysWorkflowExtAttrDao) bf
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl");

		// 查询分配器定义
		List<SysWorkflowExtAttr> attrlist = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from  SysWorkflowExtAttr where key= ? and version = ? and category='TASK_ASSIGN' and srcacti= ?",
						key,  version, actiId);
		if (attrlist.size() == 0) {
			throw new Exception("取得人工任务节点" + execution.getActivity().getName()
					+ "的受托分配器配置时发生异常！");
		}
		// String deploymentId = pd.getDeploymentId();

		Set<String> userNames = getQualfiedUsernames(attrlist.get(0)
				.getAttrvalue1(), attrlist.get(0).getAttrvalue4(), bf,
				execution);

		if (userNames == null || userNames.size() == 0) {
			throw new Exception("无法找到合适的用户!");
		}
		if (userNames.size() == 1) {
			// 如果角色里面只有一个人，则直接交由这个人处理
			assignable.setAssignee((String) userNames.iterator().next());
		} else {
			// 如果角色里有多个人，则这些人作为任务候选者
			for (String user : userNames) {
				assignable.addCandidateUser(user);
			}
		}
	}

	/**
	 * 取得合格的候选用户
	 * 
	 * @param assignmentType
	 *            受托分配器类型
	 * @param args
	 *            参数
	 * @param bf
	 *            SpringBeanFactory
	 * @param exec
	 *            流程实例
	 * @return 合格的用户列表
	 */
	private Set<String> getQualfiedUsernames(String assignmentType,
			String args, BeanFactory bf, OpenExecution exec) {
		Set<String> userNames = new HashSet<String>();
		if ("角色分配器".equals(assignmentType)) {
			// 参数格式为 角色名称1,角色名称2,...##角色编码1,角色编码2,...
			int index = args.indexOf("##");
			String roleIds = args.substring(index + 2);
			String[] roles = roleIds.split(",");
			// 由角色表取出合格用户
			userNames = getUsersByRoles(roles, bf);

		} else if ("流程发起人分配器".equals(assignmentType)) {
			// 不需要参数
			SysUser user = (SysUser) exec.getVariable("startUser");
			userNames.add(user.getUsercode());

		} else if ("固定用户分配器".equals(assignmentType)) {
			// 参数格式为 用户名称1,用户名称2,...##用户编码1,用户编码2,...
			int index = args.indexOf("##");
			String names = args.substring(index + 2);
			String[] nameList = names.split(",");
			for (String name : nameList) {
				if (name.trim().length() > 0)
					userNames.add(name.trim());
			}

		} else if ("环境变量分配器".equals(assignmentType)) {
			// 参数格式为 参数说明##参数key
			String variableValue = (String) exec.getVariable(args.trim());
			String[] nameList = variableValue.split(",");
			for (String name : nameList) {
				if (name.trim().length() > 0)
					userNames.add(name.trim());
			}
		}
		return userNames;
	}

	// /**
	// * 取得合适的用户名列表
	// *
	// * @param roleId
	// * 角色id
	// * @param sysUserRoleDao
	// * @return 用户名列表
	// */
	// private List getUserNames(Long roleId, SysUserRoleDao sysUserRoleDao) {
	//
	// return sysUserRoleDao.getUsernameByRoleID(roleId);
	// }

	// /**
	// * 取得流程版本的节配置的角色
	// *
	// * @param deploymentId
	// * 流程版本部署id
	// * @param activityName
	// * 活动节点名称
	// * @param sysWorkflowPrivilegeDao
	// * @return 配置的角色ID
	// */
	// private Long getRoleId(String deploymentId, String activityName,
	// SysWorkflowPrivilegeDao sysWorkflowPrivilegeDao) {
	//
	// String hql =
	// " from SysWorkflowPrivilege where deploymentid =? and activityname =?";
	//
	// List<SysWorkflowPrivilege> list = (List<SysWorkflowPrivilege>)
	// sysWorkflowPrivilegeDao
	// .find(hql, deploymentId, activityName);
	// if (list.size() > 0) {
	// return list.get(0).getRoleid();
	// }
	// return null;
	// }
	/**
	 * 按用户角色列表取得所有角色授权的用户列表
	 * 
	 * @param roleIds
	 *            角色列表
	 * @param bf
	 *            BeanFactory
	 * @return 所有角色授权的用户列表
	 */
	@SuppressWarnings("unchecked")
	private Set<String> getUsersByRoles(String[] roleIds, BeanFactory bf) {
		SysUserDao sysUserDao = (SysUserDao) bf
				.getBean("com.jbf.sys.user.dao.impl.SysUserDaoImpl");
		StringBuilder builder = new StringBuilder();
		for (String id : roleIds) {
			builder.append(id).append(",");
		}
		builder.delete(builder.length() - 1, builder.length());
		List<SysUser> users = (List<SysUser>) sysUserDao
				.find(" select u from SysUserRole ur,SysUser u where u.userid=ur.userid and ur.roleid in ("
						+ builder.toString() + ")");
		Set<String> usernames = new HashSet<String>();
		for (SysUser u : users) {
			usernames.add(u.getUsercode());
		}
		return usernames;

	}

}

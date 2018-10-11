/************************************************************
 * 类名：TableNameConst.java
 *
 * 类别：公共类
 * 功能：表名定义公共类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common;

public class TableNameConst {
	
	public static String SYS_FILEMANAGE = "sys_filemanage"; //附件表
	public static String SYS_DATASCOPEITEM = "sys_datascopeitem"; //数据权限明细表
	public static String SYS_DATASCOPEMAIN = "sys_datascopemain"; //数据权限主表
	public static String SYS_DATASCOPESUB = "sys_datascopesub"; //数据权限规则表
	public static String SYS_DEPT = "sys_dept"; //机构表
	public static String SYS_DEPTEXP = "sys_deptexp"; //机构扩展表
	public static String SYS_DEPTEXPCFG = "sys_deptexpcfg"; //机构类别对照扩展属性表
	public static String SYS_DICCOLUMN = "sys_diccolumn"; //
	public static String SYS_DICELEMENT = "sys_dicelement"; //数据字典表（数据项）
	public static String SYS_DICELEMENTVIEWFILTER = "sys_dicelementviewfilter"; //
	public static String SYS_DICTABLE = "sys_dictable"; //
	public static String SYS_DICUISCHEME = "sys_dicuischeme"; //
	public static String SYS_IMPEXP_IMPCFG = "sys_impexp_impcfg"; //导入设置
	public static String SYS_IMPEXP_IMPLOG = "sys_impexp_implog"; //数据导入日志
	public static String SYS_MENU_OPER = "sys_menu_oper"; //
	public static String SYS_RESOURCE = "sys_resource"; //
	public static String SYS_ROLE_RESOURCE = "sys_role_resource"; //
	public static String SYS_ROLE = "sys_role"; //角色表
	public static String SYS_ROLE_RESOURCE_OPER = "sys_role_resource_oper"; //
	public static String SYS_ROLE_USER_RES_DSCOPE = "sys_role_user_res_dscope"; //角色（用户、功能菜单）与数据权限关联表
	public static String SYS_SCOPEVALUE = "sys_scopevalue"; //数据权限明细值表
	public static String SYS_USER_ROLE = "sys_user_role"; //
	public static String SYS_USER = "sys_user"; //用户表
	public static String SYS_WORKFLOW_BACKATTR = "sys_workflow_backattr"; //工作流退回定义
	public static String SYS_WORKFLOW_BACKLINE_REC = "sys_workflow_backline_rec"; //
	public static String SYS_WORKFLOW_OPDEF = "sys_workflow_opdef"; //操作类型定义
	public static String SYS_WORKFLOW_PRIVILEGE = "sys_workflow_privilege"; //工作流权限表
	public static String SYS_WORKFLOW_PROCDEF = "sys_workflow_procdef"; //工作流流程基本属性表
	public static String SYS_WORKFLOW_PROCVERSION = "sys_workflow_procversion"; //工作流版本
	public static String SYS_WORKFLOW_TASK_PAGE_URL = "sys_workflow_task_page_url"; //工作流任务节点url
	public static String SYS_DICCODEITEM = "sys_diccodeitem"; // 系统预设层码枚举表
	public static String SYS_DICENUMITEM = "sys_dicenumitem"; // 系统预设枚举表
	public static String SYS_LOG = "sys_log"; //日志表
	public static String PRO_PROJECT = "PRO_PROJECT";//项目信息主表

}

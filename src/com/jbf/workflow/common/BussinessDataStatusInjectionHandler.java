/***
 * 业务状态回填接口，由二次开发实现
 */
package com.jbf.workflow.common;

public interface BussinessDataStatusInjectionHandler {

	/**
	 * 根据流程图配置注入业务数据状态
	 * 
	 * @param key
	 *            工作流key
	 * @param verison
	 *            工作流版本
	 * @param actiId
	 *            工作流节点id
	 * @param transition
	 *            流经路径
	 * @param execId
	 *            流程实例id
	 * @param status
	 *            流程图上配置的待注入状态
	 * @param handleUserName
	 *            流程处理人
	 */
	public void inject(String key, String verison, String actiId,
			String transition, String execId, String status,
			String handleUserName) throws Exception;
}

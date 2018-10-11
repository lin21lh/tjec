/**
 * 迁移线事件处理器
 */
package com.jbf.workflow.listener;

import java.util.List;

import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.listener.EventListenerExecution;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.springframework.beans.factory.BeanFactory;

import com.jbf.common.security.SecureUtil;
import com.jbf.workflow.common.BeanFactoryHelper;
import com.jbf.workflow.common.BussinessDataStatusInjectionHandler;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.po.SysWorkflowProcdef;

public class WfTransitionEventListener extends WfEventListener {
	/**
	 * 事件处理
	 */
	@Override
	public void notify(EventListenerExecution execution) throws Exception {

		BeanFactory factory = BeanFactoryHelper.getBeanFactory();
		super.processEvent(factory, execution);
		this.injectBussinessDataStatus(factory, execution);
	}

	/**
	 * 执行状态回填
	 * 
	 * @param factory
	 *            Spring Bean 工厂
	 * @param execution
	 *            工作流实例参数
	 * @throws Exception
	 */
	protected void injectBussinessDataStatus(BeanFactory factory,
			EventListenerExecution execution) throws Exception {

		String execId = execution.getId();
		String defId = execution.getProcessDefinitionId();

		// 查询流程定义的key和version
		ProcessEngine pe = (ProcessEngine) factory.getBean("processEngine");
		RepositoryService rs = pe.getRepositoryService();
		ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
		pdq.processDefinitionId(defId);
		ProcessDefinition pd = pdq.uniqueResult();
		String key = pd.getKey();
		int version = pd.getVersion();
		ExecutionImpl ei = (ExecutionImpl) execution;
		TransitionImpl ti = (TransitionImpl) ei.getEventSource();

		// 取得当前的transition名称和源活动节点
		String actiId = ti.getSource().getName();
		String transition = ti.getName();

		// 查询状态回填Bean
		SysWorkflowProcdefDao sysWorkflowProcdefDao = (SysWorkflowProcdefDao) factory
				.getBean("com.jbf.workflow.dao.impl.SysWorkflowProcdefDaoImpl");
		List<SysWorkflowProcdef> oplist = (List<SysWorkflowProcdef>) sysWorkflowProcdefDao
				.find(" from SysWorkflowProcdef where key=? ", key);

		if (oplist.size() == 0) {
			throw new Exception("无法找到KEY为" + key + "的流程定义！");
		}

		String beanName = oplist.get(0).getStatusbean();

		String handleUserName = SecureUtil.getCurrentUser().getUsercode();
		// 如果定义了回填bean
		if (null != beanName && beanName.trim().length() > 0) {
			// 查询扩展属性定义表中的定义取得定义的状态

			SysWorkflowExtAttrDao sysWorkflowExtAttrDao = (SysWorkflowExtAttrDao) factory
					.getBean("com.jbf.workflow.dao.impl.SysWorkflowExtAttrDaoImpl");
			List<SysWorkflowExtAttr> attrCfgList = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
					.find(" from SysWorkflowExtAttr where key = ? and version = ? and category = 'TRANS_BDATA_STATUS' and srcacti = ?",
							key, version, actiId);
			// 如果定义了该节点的回填状态
			if (attrCfgList.size() > 0) {

				String status = attrCfgList.get(0).getAttrvalue1();
				if (status != null && status.trim().length() > 0) {

					// 取得Bean并回填状态
					Object obean = factory.getBean(beanName);
					if (obean != null) {
						BussinessDataStatusInjectionHandler handler = (BussinessDataStatusInjectionHandler) obean;
						handler.inject(key, "" + version, actiId, transition,
								execId, status, handleUserName);
					} else {
						throw new Exception("工作流流程图" + key + "-" + version
								+ "配置的状态写入接口实现类无效！");
					}
				}

			}
		}

	}
}

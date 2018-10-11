package com.wfzcx.ppp.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.workflow.common.BussinessDataStatusInjectionHandler;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
/**
 * 更改工作流线的状态
 * @ClassName: WFStatusInjectionHandler 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年8月3日18:24:14
 */
@Component("com.wfzcx.ppp.common.WFdqztInjectionHandler")
public class WFdqztInjectionHandler implements BussinessDataStatusInjectionHandler {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	SysWorkflowProcdefDao wfProcdefDao;
	@Override
	public void inject(String key, String verison, String actiId,
			String transition, String execId, String status,
			String handleUserName) throws Exception {
		//取出工作流对应的表名
		String tableCodeString = wfProcdefDao.getTabcodeByKey(key);
		//更改业务数据状态
		String sql ="update "+tableCodeString+" set dqzt='"+status+"' where wfid='"+execId+"'";
		mapDataDao.updateTX(sql);
	}

}

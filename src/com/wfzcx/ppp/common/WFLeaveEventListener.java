package com.wfzcx.ppp.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.StringUtil;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.listener.WfEventListener;
import com.jbf.workflow.vo.EventSourceVO;

/**
 * 工作流节点修改业务数据退回撤回状态
 * @ClassName: WFLeaveEventListener 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年10月10日 上午8:48:17
 */
@Component("com.wfzcx.ppp.common.WFLeaveEventListener")
public class WFLeaveEventListener {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	SysWorkflowProcdefDao wfProcdefDao;
	/**
	 * 节点完成后的处理（更改退回和撤回的状态）
	 * @Title: nodeProcess 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param wfid 工作流id
	 * @param pamam 节点设置参数
	 * @param variables 工作流韩靖变量
	 * @param eventSource 事件的源节点或源迁称路径的信息
	 * @throws Exception 设定文件
	 */
	public void nodeProcess(String wfid, Map pamam, Map variables,EventSourceVO eventSource) throws Exception {
		String backFlag = StringUtil.stringConvert(variables.get(WfEventListener.WF_BACK_FLAG));
		if (!"".equals(wfid)) {
			String wfKey = wfid.substring(0,wfid.indexOf("."));
			//取出工作流对应的表名
			String tableCodeString = wfProcdefDao.getTabcodeByKey(wfKey);
			if("".equals(backFlag)){
				System.err.println("工作流回调WFLeaveEventListener--nodeProcess---backFlag标志为空，工作流id为"+wfid);
			}else if ("NORMAL".equals(backFlag)) {//正常 不做处理
			}else if ("RETURN".equals(backFlag)) {//退回
				String sql ="update "+tableCodeString+" set dqzt='90' where wfid='"+wfid+"'";
				mapDataDao.updateTX(sql);
			}else if ("WITHDRAW".equals(backFlag)) {//撤回
				String sql ="update "+tableCodeString+" set dqzt='99' where wfid='"+wfid+"'";
				mapDataDao.updateTX(sql);
			}
		}
	}
}

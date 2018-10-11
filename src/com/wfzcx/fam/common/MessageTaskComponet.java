package com.wfzcx.fam.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.quartz.dto.TaskDTO;
import com.jbf.sys.quartz.service.ModulesTaskService;
import com.jbf.sys.quartz.trigger.TriggerUtil;
/**
 * 
 * @ClassName: MessageTaskComponet 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年4月26日 下午2:38:41
 */
@Scope("prototype")
@Service("com.wfzcx.fam.common.MessageTaskComponet")
public class MessageTaskComponet implements ModulesTaskService {
	@Autowired
	ParamCfgComponent pcfg;
	@Autowired
	MessageComponent mComponent;
	@Override
	public List<TaskDTO> getTask() {
		/**
		 * 当前模块的定时任务业务逻辑
		 * getTask 没必要传入参数 因为此方法只是在系统启动是执行
		 * 
		 */
		//判断消息是否启用，如果启动，则启动长连接并发送待发送消息
		String msgIsUse = pcfg.findGeneralParamValue("SYSTEM", "MESSAGEISUSE");
		if ("1".equals(msgIsUse)) {
			MessageLongConnection.init();
			Map paramMap = new HashMap();
			Configuration cg = mComponent.getConfiguration();
			//定时执行频率
			int freance  = Integer.parseInt(cg.getValue("freance"));
			Trigger trigger = TriggerUtil.getSimpleTrigger("账户管理消息服务", "groupMessage", freance, 0);
			TaskDTO taskDto = new TaskDTO("账户管理消息服务", "groupMessage", "", MessageJob.class, paramMap, trigger);
			
			return Arrays.asList(taskDto);
			//消息服务接收消息长连接
		}else {
			return null;
		}
		
	}

}

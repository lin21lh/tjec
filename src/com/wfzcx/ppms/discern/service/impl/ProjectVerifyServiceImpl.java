package com.wfzcx.ppms.discern.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.util.EasyUISumRowUtil;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.ppms.discern.dao.ProApproveDao;
import com.wfzcx.ppms.discern.dao.ProProjectDao;
import com.wfzcx.ppms.discern.po.ProProject;
import com.wfzcx.ppms.discern.service.ProjectVerifyService;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

/**
 * 项目审核service实现类
 * @ClassName: ProjectVerifyServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-11-24 上午10:28:23
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.discern.service.impl.ProjectVerifyServiceImpl")
public class ProjectVerifyServiceImpl implements ProjectVerifyService {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProProjectDao proProjectDao;
	@Autowired
	ProApproveDao proApproveDao;
	@Autowired
	ProjectWorkFlowComponent pwfc;
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;
	@Autowired
	EasyUISumRowUtil easyUISumRowUtil;
	
	@Override
	public SysResource getResourceById(String menuid) throws Exception {
		return pwfc.getResourceById(menuid);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String sendWorkFlow(Map map) throws Exception {
		String msg = "";
		String menuid = map.get("menuid") == null ? "":map.get("menuid").toString();
		String projectid = map.get("projectid") == null ? "":map.get("projectid").toString();
		String activityId = map.get("activityId") == null ? "":map.get("activityId").toString();
		String opinion = map.get("opinion") == null ? "":map.get("opinion").toString();
		String implementOrgan = map.get("implementOrgan") == null ? "":map.get("implementOrgan").toString();
		String implementPerson = map.get("implementPerson") == null ? "":map.get("implementPerson").toString();
		String implementPhone = map.get("implementPhone") == null ? "":map.get("implementPhone").toString();
		String governmentPath = map.get("governmentPath") == null ? "":map.get("governmentPath").toString();
		String czResult = map.get("czResult") == null ? "":map.get("czResult").toString();
		String vfmPjhj = map.get("vfmPjhj") == null ? "":map.get("vfmPjhj").toString();
		String wfid = map.get("wfid") == null ? "":map.get("wfid").toString();
		ResultMsg rsgMsg = null;
		String[] projectids = projectid.split(",");
		String[] wfids = wfid.split(",");
		for(int j=0;j<projectids.length;j++){
			SysUser user = SecureUtil.getCurrentUser();
			String usercode = user.getUsercode();
			ProProject pro = proProjectDao.get(Integer.parseInt(projectid));
			pro.setImplementOrgan(implementOrgan);
			pro.setImplementPerson(implementPerson);
			pro.setImplementPhone(implementPhone);
			pro.setGovernmentPath(governmentPath);
			pro.setVfmPjhj(vfmPjhj);
			pro.setCzResult(czResult);
			pro.setOpinion(opinion);//审批意见
			proProjectDao.update(pro);
			rsgMsg =workflowManageComponent.completeTask(wfids[j], activityId, new HashMap(), usercode, opinion);
			if(rsgMsg!=null&&!rsgMsg.isSuccess()){//处理失败
				msg = rsgMsg.getTitle();
				throw new Exception(msg);
			}
		}
		
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String backWorkFlow(Map<String, Object> param) throws Exception {
		// TODO Auto-generated method stub
		String returnMsg = "";
		ResultMsg rsMsg = null;
		String wfid = StringUtil.stringConvert(param.get("wfid"));
		String activityId = StringUtil.stringConvert(param.get("activityId"));
		String opinion = StringUtil.stringConvert(param.get("opinion"));
		SysUser user = SecureUtil.getCurrentUser();
		String usercode = user.getUsercode();
		String[] wfids = wfid.split(",");
		if(wfids.length>1){
			for(int i=0;i<wfids.length;i++){
				rsMsg = workflowManageComponent.sendBackWorkflow(wfids[i], activityId, new HashMap(), usercode, opinion);
				if(!rsMsg.isSuccess()){
					returnMsg = rsMsg.getTitle();
					throw new Exception(returnMsg);
				}
			}
		}else{
			rsMsg = workflowManageComponent.sendBackWorkflow(wfid, activityId, new HashMap(), usercode, opinion);
			 if(!rsMsg.isSuccess()){//处理失败
				 returnMsg = rsMsg.getTitle();
				 throw new Exception(returnMsg);
			}
		}
		 return returnMsg;
	}
}

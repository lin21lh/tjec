package com.jbf.sys.toRemind.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.toRemind.service.ToRemindService;
import com.jbf.sys.toRemind.vo.ToRemindVo;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.wfzcx.fam.dataPermission.FamDataPermissionFilter;
import com.wfzcx.fam.workflow.BussinessWorkFlowComponent;
import com.wfzcx.ppms.workflow.component.ProjectWorkFlowComponent;

/**
 * 待办提醒
 * @ClassName: ToRemindServiceImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月18日 上午9:33:36
 */
@Scope("prototype")
@Service
public class ToRemindServiceImpl implements ToRemindService {

	@Autowired
	SysUserDao userDao;
	
	@Autowired
	SysResourceDao resourceDao;
	
	@Autowired
	SysWorkflowProcdefDao wfProcdefDao;
	
	@Autowired
	BussinessWorkFlowComponent busWorkFlowComponent; //工作流组件
	@Autowired
	ProjectWorkFlowComponent pwfc;//PPP项目管理工作流
	
	@Autowired
	FamDataPermissionFilter dataPermissionFilter; //数据权限组件
	
	public List<ToRemindVo> findRemindResourceList() throws AppException {
		
		SysUser user = SecureUtil.getCurrentUser();
		
		List<SysResource> resourceList = userDao.getMenusByUser(user);
		List<ToRemindVo> toRemindList =  new ArrayList<ToRemindVo>();
		for (SysResource resource : resourceList) {
			if (resource.getRemindtype() == null ||resource.getRemindtype() == 0 || StringUtil.isBlank(resource.getWebpath()))
				continue;
			
			ToRemindVo toRemindVo = getRemindVo(resource);
			if (toRemindVo.getCount() > 0) {
				toRemindList.add(toRemindVo);
			}
		}
		
		return toRemindList;
	}
	public List<ToRemindVo> findRemindResourceListByUser(SysUser user) throws AppException {
		
		List<SysResource> resourceList = userDao.getMenusByUser(user);
		List<ToRemindVo> toRemindList =  new ArrayList<ToRemindVo>();
		for (SysResource resource : resourceList) {
			if (resource.getRemindtype() == null ||resource.getRemindtype() == 0 || StringUtil.isBlank(resource.getWebpath()))
				continue;
			
			ToRemindVo toRemindVo = getRemindVo(resource);
			if (toRemindVo.getCount() > 0) {
				toRemindList.add(toRemindVo);
			}
		}
		
		return toRemindList;
	}
	public ToRemindVo getRemindVo(SysResource resource) throws AppException {
		String wholename = resource.getName();
		Integer count = getCount(resource);
		ToRemindVo toRemindVo = new ToRemindVo();
		toRemindVo.setResourceid(resource.getResourceid());
		toRemindVo.setTitle(resource.getName());
		toRemindVo.setWebpath(resource.getWebpath());
		setWholename(resource, wholename, toRemindVo);
		toRemindVo.setCount(count);
		return toRemindVo;
	}
	
	public void setWholename(SysResource resource, String wholename, ToRemindVo toRemindVo) {
		
		if (resource.getLevelno() != 1) {
			SysResource parentResource = (SysResource) resourceDao.get(resource.getParentresid());
			wholename = parentResource.getName() + "->" + wholename;
			setWholename(parentResource, wholename, toRemindVo);
		} else {
			toRemindVo.setWholename(wholename);
		}
	}
	
	public Integer getCount(SysResource resource) throws AppException {
		
		boolean firstNode = false;
		boolean lastNode = false;
		
		if(null==resource.getWftasknode()){
			return 0;
		}else{
			switch (resource.getWftasknode()) {
			case 0:
				firstNode = true;
				break;
			case 1:
				
				break;
			case 2:
				lastNode = true;
				break;

			default:
				break;
			}
			
			//String wfWhere = busWorkFlowComponent.findCurrentWfids(resource.getResourceid().toString(), resource.getActivityid(), "1", "", firstNode, lastNode);
			String wfWhere = pwfc.findCurrentWfids(resource.getResourceid().toString(), resource.getActivityid(), "1", firstNode, lastNode);
			
			String tablecode = wfProcdefDao.getTabcodeByKey(resource.getWfkey());
			
			String condFilterWhere = dataPermissionFilter.getConditionFilter(resource.getResourceid(), tablecode, "t");
			String where = "(" + wfWhere + ")";
			if (StringUtil.isNotBlank(condFilterWhere)) {
				where += " and (" + condFilterWhere + ")";
			}
			
			if (StringUtil.isNotBlank(resource.getToRemindWhere()))
				where += " and (" + resource.getToRemindWhere() + ")";
			
			List list = userDao.queryBySQL(tablecode, where);
			
			return list.isEmpty() ? 0 : list.size();
		}
		
	}
	
//	public Map<String, Object> getParams(String webpath) {
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		if (webpath.indexOf("?") == -1)
//			return paramMap;
//		String paramStr = webpath.split("\\?")[1];
//		String[] params = paramStr.split("\\&");
//		for (String param : params) {
//			String[] kv = param.split("=");
//			paramMap.put(kv[0].toLowerCase(), kv[1]);
//		}
//		
//		return paramMap;
//	}
}

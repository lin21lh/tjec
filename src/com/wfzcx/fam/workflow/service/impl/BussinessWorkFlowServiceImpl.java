package com.wfzcx.fam.workflow.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.po.SysWorkflowProcdef;
import com.wfzcx.fam.workflow.service.BussinessWorkFlowService;
import com.wfzcx.fam.workflow.vo.ActivitiyVO;

@Scope("prototype")
@Service
public class BussinessWorkFlowServiceImpl implements BussinessWorkFlowService {
	
	@Autowired
	SysWorkflowManageComponent wfManageComponent;
	
	@Autowired
	SysWorkflowProcdefDao workflowProcdefDao;
	
	@Autowired
	SysDicColumnDao dicColumnDao;
	
	public List<ActivitiyVO> findActivitiesByWfKey(String key, Integer wfversion, String activitiyid, String activitiyname) throws AppException {
		
		if (wfversion == null) {
			wfversion = wfManageComponent.getValidVersionByDate(key, new Date());
			if (wfversion == -1)
				throw new AppException("当前工作流找不到合法的版本");
		}
		
		List<HashMap> list = wfManageComponent.getWorkflowDefinitionActivities(key, wfversion);
		
		if (list == null)
			return new ArrayList<ActivitiyVO>();
		
		ActivitiyVO activitiyVO = null;
		List<ActivitiyVO> actList = new ArrayList<ActivitiyVO>();
		boolean ispand = false;
		for (HashMap value : list) {
			
			if (!TASK.equals((String)value.get("type")))
					continue;
			
			if (StringUtil.isNotBlank(activitiyid)) {
				if (((String)value.get("actiId")).toUpperCase().startsWith(activitiyid.toUpperCase()))
					ispand = true;
			}
			
			if (StringUtil.isNotBlank(activitiyname)) {
				if (((String)value.get("actiName")).indexOf(activitiyname) != -1)
					ispand = true;
			}
			
			if (StringUtil.isBlank(activitiyid) && StringUtil.isBlank(activitiyname))
				ispand = true;
			
			if (!ispand)
				continue;
			
			activitiyVO = new ActivitiyVO();
			activitiyVO.setActivitiyid((String)value.get("actiId"));
			activitiyVO.setActivitiyname((String)value.get("actiName"));
			activitiyVO.setVersion(wfversion);
			activitiyVO.setType((String)value.get("type"));
			
			actList.add(activitiyVO);
			
			ispand = false;
		}
		
		return actList;
	}
	
	@Override
	public List<Map<String, Object>> findVersionListByKey(String key) throws AppException {
		// TODO Auto-generated method stub
		List<Integer> list = wfManageComponent.getWorkflowVersions(key);
		
		if (list.isEmpty())
			throw new AppException("当前工作流未定义流程版本！");
		
		Collections.sort(list, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return o1 < o2 ? 1 : -1;
			}
		});
		List<Map<String, Object>> newList = new ArrayList<Map<String,Object>>(list.size());
		Map<String, Object> map = null;
		for (Integer version : list) {
			map = new HashMap<String, Object>();
			map.put("text", version);
			map.put("value", version);
			newList.add(map);
		}
		return newList;
	}

	@Override
	public Integer getCurrentVersion(String key) {
		// TODO Auto-generated method stub
		return wfManageComponent.getValidVersionByDate(key, new Date());
	}
	
	public final static String TASK = "task"; //节点类型为task

	@Override
	public List<SysDicColumn> findColumnsByWfKey(String wfkey, Boolean sourceElementFlag) throws AppException {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(wfkey))
			return new ArrayList<SysDicColumn>();
		
		String tablecode = workflowProcdefDao.getTabcodeByKey(wfkey);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		if (sourceElementFlag)
			dc.add(Property.forName("sourceelementcode").isNotNull());
		List<SysDicColumn> columnList = (List<SysDicColumn>) dicColumnDao.findByCriteria(dc);
		
		return columnList;
	}


}

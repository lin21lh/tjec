/************************************************************
 * 类名：SysWorkflowBackattr
 *
 * 类别：ServiceImpl
 * 功能：工作流定义服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.jbpm.api.DeploymentQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.TableNameConst;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.BeanUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.common.vo.TreeVo;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.dao.SysWorkflowProcversionDao;
import com.jbf.workflow.po.SysWorkflowProcdef;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.service.SysWorkflowProcdefService;
import com.jbf.workflow.vo.WorkflowTreeVo;

@Scope("prototype")
@Service
public class SysWorkflowProcdefServiceImpl implements SysWorkflowProcdefService {
	@Autowired
	SysWorkflowProcdefDao sysWorkflowProcdefDao;
	@Autowired
	SysWorkflowProcversionDao sysWorkflowProcversionDao;

	@Autowired
	ProcessEngine processEngine;

	@Override
	public List<? extends TreeVo> queryWorkflowTree(String name,
			String showVersion) {

		List<String> types = getTypes();
		List<WorkflowTreeVo> tree = new ArrayList<WorkflowTreeVo>();
		// 加入分类节点
		HashMap<String, TreeVo> typeMap = new HashMap<String, TreeVo>();
		for (int i = 0; i < types.size(); i++) {
			WorkflowTreeVo vo = new WorkflowTreeVo();
			vo.setId("" + (990000000L + i));
			vo.setText(types.get(i));
			vo.setType("category");
			vo.setIconCls("icon-folder");
			vo.setPid("ROOT");
			tree.add(vo);
			typeMap.put(types.get(i), vo);
		}
		// 加入工作流节点

		HashMap<String, TreeVo> keyMap = new HashMap<String, TreeVo>();

		List<SysWorkflowProcdef> list = null;
		if (name != null && name.trim().length() > 0) {
			try {
				name = URLDecoder.decode(name, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// list = sysWorkflowProcdefDao.find(fac.like("name", name), fac
			// .createDataOrder().asc("name"));

			list = (List<SysWorkflowProcdef>) sysWorkflowProcdefDao
					.find(" from SysWorkflowProcdef where name like ?  order by name ");
		} else {
			list = (List<SysWorkflowProcdef>) sysWorkflowProcdefDao
					.find(" from SysWorkflowProcdef order by name");
		}
		for (SysWorkflowProcdef o : list) {
			WorkflowTreeVo workflow = new WorkflowTreeVo();
			workflow.setType("workflow");
			workflow.setText(o.getName());
			workflow.setCode(o.getKey());
			workflow.setIconCls("icon-wfnode");
			TreeVo parent = typeMap.get(o.getType());
			if (parent != null) {
				// if (parent.getChildren() == null) {
				// parent.setChildren(new ArrayList());
				// }
				// parent.getChildren().add((TreeVo) workflow);
				workflow.setPid(parent.getId());
			}
			workflow.setId("" + (900000000L + o.getId()));

			workflow.setKey(o.getKey());
			keyMap.put(o.getKey(), workflow);
			tree.add(workflow);
		}

		List<SysWorkflowProcversion> verlist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.find(" from SysWorkflowProcversion order by version ");

		if (!"false".equals(showVersion)) {
			// 加入工作流的版本
			for (SysWorkflowProcversion v : verlist) {
				WorkflowTreeVo workflow = new WorkflowTreeVo();
				workflow.setId("" + v.getId());
				workflow.setType("leaf");
				workflow.setText("" + v.getVersion());
				workflow.setIconCls("icon-version");
				TreeVo parent = keyMap.get(v.getKey());
				if (parent != null) {
					// if (parent.getChildren() == null) {
					// parent.setChildren(new ArrayList());
					// }
					// parent.getChildren().add(workflow);
					workflow.setPid(parent.getId());
				}
				tree.add(workflow);
			}
		}
		return TreeVoUtil.toBornTree(tree, "ROOT", false);
	}

	private List<String> getTypes() {

		List list = sysWorkflowProcdefDao.findMapBySql(" select name from "
				+ TableNameConst.SYS_DICENUMITEM
				+ " where elementcode='SYS_WORKFLOW_CATEGORY'");

		List<String> types = new ArrayList<String>();
		for (Object o : list) {
			Map map = (Map) o;
			types.add((String) map.get("name"));
		}
		return types;
	}

	@Override
	public SysWorkflowProcdef get(Long id) {
		return sysWorkflowProcdefDao.get(id);
	}

	@Override
	public void delete(Long id) {
		SysWorkflowProcdef wf = sysWorkflowProcdefDao.get(id);
		if (wf != null) {
			sysWorkflowProcdefDao.delete(wf);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveWorkflowProcdef(SysWorkflowProcdef def) throws AppException {
		Long id = def.getId();
		if (def.getDefaultui() == null) {
			def.setDefaultui((byte) 0);
		}
		if (id != null) {
			// 查询是否已存在流程版本
			// List list = sysWorkflowProcversionDao.find(
			// " from SysWorkflowProcversion where key= ? ", def.getKey());
			// if (list != null && list.size() > 0) {
			// throw new AppException("流程定义已存在流程版本，不能修改!");
			// }

			// // 取得原来的id
			// Long orgId = def.getId() - 990990000L;
			// def.setId(orgId);
			sysWorkflowProcdefDao.update(def);
		} else {
			sysWorkflowProcdefDao.save(def);
		}
	}

	// 查询部署的xml内容
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map getDeployedJpdlContent(String procVersionId) {
		SysWorkflowProcversion def = sysWorkflowProcversionDao.get(Long
				.parseLong(procVersionId));
		if (def == null) {
			return null;
		}
		String deploymentId = def.getDeploymentid();
		if (deploymentId == null) {
			// 返回一个空文档
			Map<String, String> map = new HashMap<String, String>();
			String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><process name=\"NewProcess\" xmlns=\"http://jbpm.org/4.4/jpdl\"></process>";
			map.put("xml", xml);
			return map;
		}
		Map<String, String> map = new HashMap<String, String>();
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		DeploymentQuery dq = repositoryService.createDeploymentQuery();
		dq.deploymentId(deploymentId);
		List list = dq.list();
		DeploymentImpl impl = (DeploymentImpl) list.get(0);
		// 取得所有的资源名称，从中找到xml
		Set<String> names = impl.getResourceNames();
		String name = null;
		for (String s : names) {
			if (s.endsWith(".xml")) {
				name = s;
				break;
			}
		}
		byte[] bytes = impl.getBytes(name);
		try {
			String xml = new String(bytes, "UTF-8");
			map.put("xml", xml);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public List queryWorkflowVersionsByKey(String key) {

		return sysWorkflowProcversionDao.find(
				" from SysWorkflowProcversion where key= ? order by version",
				key);

	}

	@Override
	public String getWorkflowTablecodeByKey(String key) throws AppException {

		String tablecode = sysWorkflowProcdefDao.getTabcodeByKey(key);
		return tablecode;
	}

	public List<? extends TreeVo> queryWorkflowConfigTree() {

		List<String> types = getTypes();
		List<WorkflowTreeVo> tree = new ArrayList<WorkflowTreeVo>();
		// 加入分类节点
		HashMap<String, TreeVo> typeMap = new HashMap<String, TreeVo>();
		for (int i = 0; i < types.size(); i++) {
			WorkflowTreeVo vo = new WorkflowTreeVo();
			vo.setId("" + (990000000L + i));
			vo.setText(types.get(i));
			vo.setType("category");
			vo.setIconCls("icon-folder");
			vo.setPid("ROOT");
			tree.add(vo);
			typeMap.put(types.get(i), vo);
		}
		// 加入工作流节点

		HashMap<String, TreeVo> keyMap = new HashMap<String, TreeVo>();

		List<SysWorkflowProcdef> list = null;

		list = (List<SysWorkflowProcdef>) sysWorkflowProcdefDao
				.find(" from SysWorkflowProcdef order by name");

		for (SysWorkflowProcdef o : list) {
			WorkflowTreeVo workflow = new WorkflowTreeVo();
			workflow.setType("workflow");
			workflow.setText(o.getName());
			workflow.setCode(o.getKey());
			workflow.setIconCls("icon-wfnode");
			TreeVo parent = typeMap.get(o.getType());
			if (parent != null) {
				// if (parent.getChildren() == null) {
				// parent.setChildren(new ArrayList());
				// }
				// parent.getChildren().add(workflow);
				workflow.setPid(parent.getId());
			}
			workflow.setId("" + o.getId());

			workflow.setKey(o.getKey());
			// keyMap.put(o.getKey(), workflow);

			tree.add(workflow);
		}

		return TreeVoUtil.toBornTree(tree, "ROOT", false);
	}

	@Override
	public List<SysWorkflowProcversion> queryWorkflowVersion(String key) {
		List list = sysWorkflowProcversionDao.find(
				" from SysWorkflowProcversion where key=? order by version",
				key);
		return list;
	}

	public ResultMsg wfcopyopt(Long sourceWfID, String sourceWfName,
			String objectWfKey, String objectWfName) throws AppException {

		SysWorkflowProcversion sourceWfversion = sysWorkflowProcversionDao
				.get(sourceWfID);

		DetachedCriteria dc = DetachedCriteria
				.forClass(SysWorkflowProcversion.class);
		dc.add(Property.forName("key").eq(objectWfKey));

		DetachedCriteria dcCriteria = DetachedCriteria
				.forClass(SysWorkflowProcdef.class);
		dcCriteria.add(Property.forName("key").eq(objectWfKey));
		List<SysWorkflowProcdef> wfList = (List<SysWorkflowProcdef>) sysWorkflowProcdefDao
				.findByCriteria(dcCriteria);
		if (wfList == null || wfList.size() == 0)
			throw new AppException("未查找到目标工作流！");

		SysWorkflowProcdef wfDef = wfList.get(0);
		SysWorkflowProcversion objectWfversion = sourceWfversion;
		objectWfversion.setId(null);
		objectWfversion.setKey(objectWfKey);
		objectWfversion.setDeploymentid(null);
		List<SysWorkflowProcversion> list = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
				.findByCriteria(dc);
		// Long id = null;
		// Map resultMap = new HashMap();
		if (list != null && list.size() > 0) {
			Integer version = 1;
			Date endDate = new Date();
			for (SysWorkflowProcversion wfversion : list) {
				if (wfversion.getVersion() > version)
					version = wfversion.getVersion();

				if (DateUtil.parseDate(wfversion.getEnddate(),
						DateUtil.DATE_FORMAT).compareTo(endDate) > 0)
					endDate = DateUtil.parseDate(wfversion.getEnddate(),
							DateUtil.DATE_FORMAT);
			}
			objectWfversion.setVersion(version + 1);
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			objectWfversion.setStartdate(DateUtil.getDateString(cal.getTime()));
			cal.add(Calendar.MONTH, 1);
			objectWfversion.setEnddate(DateUtil.getDateString(cal.getTime()));
			// id = (Long)sysWorkflowProcversionDao.save(objectWfversion);
		} else {
			objectWfversion.setVersion(1);
			objectWfversion.setKey(objectWfKey);

			objectWfversion.setStartdate(DateUtil.getCurrentDate());
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, 1);
			objectWfversion.setEnddate(DateUtil.getDateString(cal.getTime()));

			// id = (Long)sysWorkflowProcversionDao.save(objectWfversion);
		}
		// resultMap.put("id", id);
		HashMap<String, Object> resultMap = (HashMap<String, Object>) BeanUtil
				.beanToMap(objectWfversion);
		resultMap.put("objectWfKey", objectWfKey);
		resultMap.put("objectWfID", 900000000L + wfDef.getId());
		resultMap.put("objectWfName", objectWfName);
		return new ResultMsg(true, "复制成功", resultMap);
	}

	public void deleteWfVersion(Long id) throws AppException {
		SysWorkflowProcversion wfVersion = sysWorkflowProcversionDao.get(id);
		if (wfVersion == null)
			throw new AppException("未查找到对应的工作流版本！");

		Date nowDate = new Date();
		Date startDate = DateUtil.parseDate(wfVersion.getStartdate(),
				DateUtil.DATE_FORMAT);
		Date endDate = DateUtil.parseDate(wfVersion.getEnddate(),
				DateUtil.DATE_FORMAT);
		if (startDate.before(nowDate) && endDate.after(nowDate))
			throw new AppException("当前工作流版本正处于启用状态，不允许删除！");

		sysWorkflowProcversionDao.delete(wfVersion);

	}

}

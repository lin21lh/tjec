/************************************************************
 * 类名：SysWorkflowDeploymentServiceImpl
 *
 * 类别：ServiceImpl
 * 功能：工作流部署管理服务实现
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.service.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.jbpm.api.Deployment;
import org.jbpm.api.DeploymentQuery;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.HistoryService;
import org.jbpm.api.NewDeployment;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessDefinitionQuery;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.history.HistoryProcessInstance;
import org.jbpm.api.history.HistoryProcessInstanceQuery;
import org.jbpm.api.model.ActivityCoordinates;
import org.jbpm.api.model.Transition;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.repository.DeploymentImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.jbf.workflow.dao.SysWorkflowBacklineRecDao;
import com.jbf.workflow.dao.SysWorkflowExtAttrDao;
import com.jbf.workflow.dao.SysWorkflowProcdefDao;
import com.jbf.workflow.dao.SysWorkflowProcversionDao;
import com.jbf.workflow.po.SysWorkflowBacklineRec;
import com.jbf.workflow.po.SysWorkflowExtAttr;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.service.SysWorkflowDeploymentService;
import com.jbf.workflow.util.JpdlModel;

@Service
public class SysWorkflowDeploymentServiceImpl implements
		SysWorkflowDeploymentService {

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	SysWorkflowProcdefDao sysWorkflowProcdefDao;

	@Autowired
	SysWorkflowProcversionDao sysWorkflowProcversionDao;

	@Autowired
	SysWorkflowExtAttrDao sysWorkflowExtAttrDao;

	@Autowired
	SysWorkflowBacklineRecDao sysWorkflowBacklineRecDao;

	@Autowired
	SysWorkflowManageComponent sysWorkflowManageComponent;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deployWfDef(String xml, String resourceName, String key,
			Integer version, String privilege) throws Exception {

		if (xml == null) {
			throw new AppException("param.invalid");
		}
		xml = URLDecoder.decode(xml, "UTF-8");
		resourceName = URLDecoder.decode(resourceName, "UTF-8");
		privilege = URLDecoder.decode(privilege, "UTF-8");

		if (key == null || key.trim().length() == 0) {
			throw new AppException("param.invalid");
		}
		try {
			// 1生成图片文件
			String tmpDir = System.getProperty("java.io.tmpdir");
			File wfPngFile = new File(tmpDir, "flowchart.png");

			JpdlModel.createPngFile(wfPngFile.getPath(), xml);
			// 2部署xml与图片文件

			NewDeployment deployment = processEngine.getRepositoryService()
					.createDeployment();

			deployment.addResourceFromString(resourceName, xml);
			deployment.addResourceFromFile(wfPngFile);
			String deploymentId = deployment.deploy();
			if (deploymentId == null) {
				System.out.println("部署异常，没有返回正确的部署标识!");
				throw new AppException("crud.saveerr");
			}
			// 3 更新工作流定义
			// WorkflowProcdef def = workflowProcdefDao.get(Long
			// .parseLong(wfDefId));
			// def.setDeploymentid(deploymentId);
			// workflowProcdefDao.update(def);

			// 4 保存工作流权限定义
			// if (privilege != null && privilege.trim().length() > 0) {
			// String[] pairs = privilege.split(";");
			// for (String s : pairs) {
			// String[] pair = s.split(":");
			// SysWorkflowPrivilege p = new SysWorkflowPrivilege();
			// p.setActivityname(pair[0]);
			// p.setRoleid(Long.parseLong(pair[1]));
			// p.setDeploymentid(deploymentId);
			// sysWorkflowPrivilegeDao.save(p);
			// }
			// }

		} catch (Exception e) {
			if (e instanceof AppException) {
				throw e;
			} else {

				String msg = e.getMessage();
				Pattern p = Pattern
						.compile("process '(.*)-(.*)' already exists");
				Matcher m = p.matcher(msg);
				boolean result = m.find();
				if (result) {
					String res = m.group(1);
					System.out.println("标识为 '" + res + "'且版本为'" + m.group(2)
							+ "'的流程已经存在!");

					throw new AppException("crud.saveerr");
				} else {
					e.printStackTrace();
					System.out.println("流程部署时发生异常!");
					throw new AppException("crud.saveerr");
				}
			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public RenderedImage getImageByWfInstanceId(String instId) throws Exception {
		// 取得当前执行的流程节点的图片上的位置
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		ExecutionService executionService = processEngine.getExecutionService();

		HistoryService historyService = processEngine.getHistoryService();

		boolean procIsOver = false;
		// 取得流程实例
		ProcessInstance processInstance = executionService
				.findProcessInstanceById(instId);
		ProcessDefinition pd = null;
		String deploymentId = null;

		if (processInstance != null) {
			// 流程未结束，取得流程定义
			String processDefinitionId = processInstance
					.getProcessDefinitionId();
			ProcessDefinitionQuery pdq = repositoryService
					.createProcessDefinitionQuery();
			pdq.processDefinitionId(processDefinitionId);
			pd = pdq.uniqueResult();

		} else {
			// 流程已结束
			HistoryProcessInstanceQuery q = historyService
					.createHistoryProcessInstanceQuery();
			HistoryProcessInstance hpi = q.processInstanceId(instId)
					.uniqueResult();
			ProcessDefinitionQuery pdq = repositoryService
					.createProcessDefinitionQuery();
			pdq.processDefinitionId(hpi.getProcessDefinitionId());
			pd = pdq.uniqueResult();
			procIsOver = true;
		}
		if (pd == null) {
			return getErrorImage(400, 300, "无法取得流程实例'" + instId + "'的流程定义！");
		}
		// 取得deploymentId
		deploymentId = pd.getDeploymentId();
		// 取得流和定义id

		DeploymentQuery dq = repositoryService.createDeploymentQuery();
		dq.deploymentId(deploymentId);
		Deployment dp = dq.uniqueResult();
		DeploymentImpl impl = (DeploymentImpl) dp;
		// 取得图片文件
		Set<String> names = impl.getResourceNames();
		String name = null;
		for (String s : names) {
			if (s.endsWith(".png")) {
				name = s;
				break;
			}
		}

		// 是否被挂起
		Boolean isSuspended = false;
		if (!procIsOver) {
			// 判断是否已挂起
			ResultMsg rm = sysWorkflowManageComponent.isSuspended(instId);
			if (!rm.isSuccess()) {
				return getErrorImage(400, 300, "判断流程实例'" + instId
						+ "'的挂起状态时发生异常！");
			}
			isSuspended = (Boolean) rm.getBody().get("isSuspended");
		}

		byte[] bytes = impl.getBytes(name);

		BufferedImage img = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

		img = ImageIO.read(bis);
		bis.close();

		String actiName = null;

		// 如果流程没有结束在图上绘制当前节点
		if (!procIsOver) {
			ExecutionImpl ei = (ExecutionImpl) processInstance;
			actiName = ei.getActivityName();

			// 在图上绘制
			Graphics gg = img.getGraphics();
			Graphics2D g = (Graphics2D) gg;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(Color.red);
			g.setStroke(new BasicStroke(2f));

			g.setFont(new Font("微软雅黑", Font.PLAIN, 12));

			if (actiName != null) {
				ActivityCoordinates ac = repositoryService
						.getActivityCoordinates(
								processInstance.getProcessDefinitionId(),
								actiName);
				g.drawRoundRect(ac.getX(), ac.getY(), ac.getWidth(),
						ac.getHeight(), 10, 10);
			}
			g.setColor(Color.black);
			g.drawString("流程版本：" + pd.getVersion(), 10f, 20f);
			g.setColor(Color.red);

			if (isSuspended) {
				g.drawString("流程状态：挂起", 10f, 40f);
			}

			g.dispose();
		} else {
			Graphics gg = img.getGraphics();
			Graphics2D g = (Graphics2D) gg;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g.setStroke(new BasicStroke(2f));
			g.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			g.setColor(Color.black);
			g.drawString("流程版本：" + pd.getVersion(), 10f, 20f);
			g.setColor(Color.red);
			g.drawString("流程状态：结束", 10f, 40f);

			g.dispose();
		}

		// Set<String> activityName = processInstance.findActiveActivityNames();
		// if (activityName.size() > 0) {
		// ActivityCoordinates ac = repositoryService.getActivityCoordinates(
		// processInstance.getProcessDefinitionId(), activityName
		// .iterator().next());
		//
		// // 在图上绘制
		//
		// Graphics gg = img.getGraphics();
		//
		// Graphics2D g = (Graphics2D) gg;
		//
		// g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g.setColor(Color.red);
		// g.setStroke(new BasicStroke(2f));
		// g.drawRoundRect(ac.getX(), ac.getY(), ac.getWidth(),
		// ac.getHeight(), 10, 10);
		// g.dispose();
		// }
		return img;
	}

	// 出错时返回出错信息
	private RenderedImage getErrorImage(int w, int h, String msg) {

		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics gg = img.getGraphics();

		Graphics2D g = (Graphics2D) gg;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(2f));
		g.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		g.drawString("异常信息：", 10, h / 2 - 25);
		g.drawString(msg, 10, h / 2);
		g.dispose();
		return img;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deployWfDef(String xml, String resourceName, String name,
			String key, String version, String firstnode, String startdate,
			String enddate, String extAttrCache) throws Exception {
		if (xml == null) {
			System.out.println("流程定义xml参数异常!");
			throw new AppException("param.invalid");
		}
		xml = URLDecoder.decode(xml, "UTF-8");
		resourceName = URLDecoder.decode(resourceName, "UTF-8");
		if (key == null || key.trim().length() == 0) {
			System.out.println("流程定义KEY参数异常!");
			throw new AppException("param.invalid");
		}

		try {
			Integer.parseInt(version);
		} catch (Exception e) {
			System.out.println("流程定义版本参数异常!");
			throw new AppException("param.invalid");
		}
		try {
			// 1生成图片文件
			String tmpDir = System.getProperty("java.io.tmpdir");
			File wfPngFile = new File(tmpDir, "flowchart.png");

			JpdlModel.createPngFile(wfPngFile.getPath(), xml);
			// 2部署xml与图片文件

			NewDeployment deployment = processEngine.getRepositoryService()
					.createDeployment();

			deployment.addResourceFromString(resourceName, xml);
			deployment.addResourceFromFile(wfPngFile);
			String deploymentId = deployment.deploy();

			if (deploymentId == null) {
				throw new AppException("crud.saveerr");
			}

			// 4保存存工作流版本定义

			List l = sysWorkflowProcversionDao
					.find(" from SysWorkflowProcversion where key = ? and version = ?",
							key, Integer.parseInt(version));
			if (l != null && l.size() > 0) {
				System.out.println("版本号" + version + "已存在，保存失败，请修改版本号！");
				throw new AppException("crud.saveerr");
			}
			SysWorkflowProcversion newver = new SysWorkflowProcversion();
			newver.setDeploymentid(deploymentId);
			newver.setKey(key);
			newver.setVersion(Integer.parseInt(version));
			newver.setStatus("0");
			newver.setStartdate(startdate);
			newver.setEnddate(enddate);
			newver.setFirstnode(firstnode);
			sysWorkflowProcversionDao.save(newver);

			// 暂存需要创建退回首节点路径的节点
			List<String> backToFirstNodeActis = new ArrayList<String>();

			if (extAttrCache != null && extAttrCache.trim().length() > 0) {
				JSONArray ja = JSONArray.parseArray(extAttrCache);
				Object[] objs = ja.toArray();
				for (Object o : objs) {
					JSONObject oo = (JSONObject) o;
					SysWorkflowExtAttr attr = JSONObject.toJavaObject(oo,
							SysWorkflowExtAttr.class);
					sysWorkflowExtAttrDao.save(attr);
					if ("TASK_BACKABLE".equals(attr.getCategory())) {
						if ("true".equals(attr.getAttrvalue3())) {
							backToFirstNodeActis.add(attr.getSrcacti());
						}
					}
				}
			}

			// 5 对部署的工作流创建退回首节点路径

			RepositoryService rs = processEngine.getRepositoryService();
			ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
			pdq.deploymentId(deploymentId);
			ProcessDefinition pd = pdq.uniqueResult();

			createBackLineToFirstNode(pd, backToFirstNodeActis, firstnode, key,
					Integer.parseInt(version));
		} catch (Exception e) {
			if (e instanceof AppException) {
				throw e;
			} else {

				String msg = e.getMessage();
				if (msg == null) {
					e.printStackTrace();
					System.out.println("流程部署时发生异常!");
					throw new AppException("crud.saveerr");
				}
				Pattern p = Pattern
						.compile("process '(.*)-(.*)' already exists");
				Matcher m = p.matcher(msg);
				boolean result = m.find();
				if (result) {
					String res = m.group(1);
					System.out.println("标识为 '" + res + "'且版本为'" + m.group(2)
							+ "'的流程已经存在!");
					throw new AppException("crud.saveerr");
				} else {
					e.printStackTrace();
					System.out.println("流程部署时发生异常!");
					throw new AppException("crud.saveerr");
				}
			}
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editWorkflowProcVersion(String key, String version,
			String startdate, String enddate) throws Exception {

		if (startdate == null || enddate == null) {
			throw new AppException("param.invalid");
		}
		SysWorkflowProcversion newver = new SysWorkflowProcversion();
		newver.setVersion(Integer.parseInt(version));
		newver.setKey(key);

		List<SysWorkflowProcversion> verlist = sysWorkflowProcversionDao
				.findByExample(newver);

		if (verlist.size() == 0) {
			return;
		}
		if (verlist.size() > 1) {
			throw new AppException("crud.obj.not.unique");
		}

		SysWorkflowProcversion ver = verlist.get(0);

		ver.setStartdate(startdate);
		ver.setEnddate(enddate);
		sysWorkflowProcversionDao.update(ver);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public SysWorkflowProcversion getWorkflowProcversion(String key,
			String version) {

		SysWorkflowProcversion newver = new SysWorkflowProcversion();
		newver.setVersion(Integer.parseInt(version));
		newver.setKey(key);

		List<SysWorkflowProcversion> verlist = sysWorkflowProcversionDao
				.findByExample(newver);

		if (verlist.size() > 0) {
			return verlist.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getExtendedAttributes(String key, Integer version) {
		List<SysWorkflowExtAttr> list = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(" from SysWorkflowExtAttr where key= ? and version = ? order by category",
						key, version);
		return list;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void workflowExtAttrUpdate(String key, Integer version,
			String category, String actiName, String transName,
			String fieldName, String fieldValue) throws Exception {
		List paramList = new ArrayList();
		String hql = " from SysWorkflowExtAttr where key= ? and version = ? and category = ? and srcacti = ? ";
		paramList.add(key);
		paramList.add(version);
		paramList.add(category);
		paramList.add(actiName);

		if (transName != null && transName.trim().length() > 0) {
			hql += " and transition = ?";
			paramList.add(transName);
		}
		List<SysWorkflowExtAttr> list = (List<SysWorkflowExtAttr>) sysWorkflowExtAttrDao
				.find(hql, paramList.toArray());

		if (list.size() == 0) {
			throw new Exception("没有找到可更新的属性数据！");
		}

		if (list.size() == 0) {
			throw new Exception("数据重复，请检查数据！");
		}

		SysWorkflowExtAttr attr = list.get(0);

		if (fieldValue == null) {
			fieldValue = "";
		}
		if (fieldName.equals("attrvalue1")) {
			attr.setAttrvalue1(fieldValue);
		} else if (fieldName.equals("attrvalue2")) {
			attr.setAttrvalue2(fieldValue);
		} else if (fieldName.equals("attrvalue3")) {
			attr.setAttrvalue3(fieldValue);
		} else if (fieldName.equals("attrvalue4")) {
			attr.setAttrvalue4(fieldValue);
		} else if (fieldName.equals("attrnum1")) {
			Long lval = Long.parseLong(fieldValue);
			attr.setAttrnum1(lval);
		} else if (fieldName.equals("attrnum2")) {
			Long lval = Long.parseLong(fieldValue);
			attr.setAttrnum2(lval);
		}

		sysWorkflowExtAttrDao.update(attr);

		// 如果更新的是退回首节点属性

		if ("TASK_BACKABLE".equals(category) && "attrvalue3".equals(fieldName)) {

			RepositoryService rs = processEngine.getRepositoryService();
			ProcessDefinitionQuery pdq = rs.createProcessDefinitionQuery();
			pdq.processDefinitionKey(key);

			ProcessDefinition pd = null;

			List<ProcessDefinition> pdlist = pdq.list();
			for (ProcessDefinition p : pdlist) {
				if (p.getVersion() == version) {
					pd = p;
					break;
				}
			}

			// 取得首节点定义

			List<SysWorkflowProcversion> proclist = (List<SysWorkflowProcversion>) sysWorkflowProcversionDao
					.find(" from SysWorkflowProcversion where key = ? and version  = ?",
							key, version.intValue());
			SysWorkflowProcversion pr = proclist.get(0);
			String firstnode = pr.getFirstnode();

			if ("true".equals(fieldValue)) {
				// 增加路线
				List<String> l = new ArrayList<String>();
				l.add(actiName);
				createBackLineToFirstNode(pd, l, firstnode, key, version);
			} else if ("false".equals(fieldValue)) {
				// 删除路线
				removeBackLineToFirstNode(pd, actiName, firstnode, key, version);
			} else {
				throw new Exception("非法的参数值:" + fieldValue + "！");
			}
		}

	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public HashMap isWorkflowTaskFormEditable(String key, Integer version,
			String actiId) {
		HashMap<String, String> map = new HashMap<String, String>();

		Boolean bl;
		try {
			bl = sysWorkflowManageComponent.isWorkflowTaskFormEditable(key,
					version, actiId);
			map.put("success", "true");
			map.put("editable", bl.toString());
		} catch (Exception e) {
			map.put("success", "false");
			map.put("editable", "");
		}
		return map;
	}

	/**
	 * 创建到首节点的退回路线
	 * 
	 * @param pd
	 *            流程定义
	 * @param actiList
	 *            退回路径源节点ID列表
	 * @param firstNodeId
	 *            首节点ID
	 * @param key
	 * @param version
	 * @throws Exception
	 */
	public void createBackLineToFirstNode(ProcessDefinition pd,
			List<String> actiList, String firstNodeId, String key, Integer version)
			throws Exception {

		if (firstNodeId == null) {
			throw new Exception("首节点参数据异常！");
		}

		if (pd == null) {
			throw new Exception("取得部署结果时发生异常！");
		}
		ProcessDefinitionImpl pdi = (ProcessDefinitionImpl) pd;
		ActivityImpl firstNode = pdi.getActivity(firstNodeId);
		if (firstNode == null) {
			throw new Exception("首节点参数据异常，该节点不存在！");
		}
		if (!"task".equals(firstNode.getType())) {
			throw new Exception("首节点参数据异常，该节点类型不是人工任务！");
		}
		for (String acti : actiList) {
			if (acti.equals(firstNodeId)) {
				continue;
			}
			ActivityImpl ai = pdi.getActivity(acti);
			if (ai != null && "task".equals(ai.getType())) {
				// 注册动态路由
				List<TransitionImpl> tilist = (List<TransitionImpl>) ai
						.getOutgoingTransitions();
				int count = 0;
				for (TransitionImpl ti_ : tilist) {
					if (firstNodeId.equals(ti_.getDestination().getName())) {
						count = 1;
						break;
					}
				}
				// 如果没有发现路径则创建路径
				if (count == 0) {
					TransitionImpl ti = ai.createOutgoingTransition();
					ti.setDestination(firstNode);
					ti.setName("退回至" + firstNode.getDescription());
				}

				List list = sysWorkflowBacklineRecDao
						.find(" from SysWorkflowBacklineRec where key = ? and version = ? and srcacti= ? and tgtacti = ?",
								key, version.intValue(), ai.getName(),
								firstNodeId);
				if (list.size() == 0) {
					// 持久化到数据库中
					SysWorkflowBacklineRec rec = new SysWorkflowBacklineRec();
					rec.setKey(pd.getKey());
					rec.setVersion(pd.getVersion());
					rec.setSrcacti(ai.getName());
					rec.setTgtacti(firstNode.getName());
					rec.setTransname("退回至" + firstNode.getDescription());
					rec.setType("3");
					sysWorkflowBacklineRecDao.save(rec);
				}
			}
		}
	}

	/**
	 * 删除至首节点的退回路线
	 * 
	 * @param pd
	 *            流程定义
	 * @param srcActiId
	 *            退回路径源节点ID
	 * @param firstNodeId
	 *            首节点id
	 * @param key
	 * @param version
	 * @throws Exception
	 */
	public void removeBackLineToFirstNode(ProcessDefinition pd,
			String srcActiId, String firstNodeId, String key, Integer version)
			throws Exception {

		// 从jbpm缓存中删除路线
		ProcessDefinitionImpl pdi = (ProcessDefinitionImpl) pd;
		ActivityImpl ai = pdi.getActivity(srcActiId);
		for (Transition ti : ai.getOutgoingTransitions()) {
			TransitionImpl tii = (TransitionImpl) ti;
			if (firstNodeId.equals(tii.getDestination().getName())) {
				ai.removeOutgoingTransition(tii);
				break;
			}
		}

		// 删除记录的路径
		List<SysWorkflowBacklineRec> list = (List<SysWorkflowBacklineRec>) sysWorkflowBacklineRecDao
				.find(" from SysWorkflowBacklineRec where key = ? and version = ? and srcacti= ? and tgtacti = ? and type = 3",
						key, version.intValue(), srcActiId, firstNodeId);
		sysWorkflowBacklineRecDao.deleteAll(list);

	}
}

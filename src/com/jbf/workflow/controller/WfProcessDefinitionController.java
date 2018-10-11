/************************************************************
 * 类名：WfProcessDefinitionController
 *
 * 类别：Controller
 * 功能：工作流流程定义控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.workflow.controller;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.common.vo.TreeVo;
import com.jbf.common.web.ResultMsg;
import com.jbf.workflow.po.SysWorkflowOpdef;
import com.jbf.workflow.po.SysWorkflowProcdef;
import com.jbf.workflow.po.SysWorkflowProcversion;
import com.jbf.workflow.service.SysWorkflowDeploymentService;
import com.jbf.workflow.service.SysWorkflowOpdefService;
import com.jbf.workflow.service.SysWorkflowProcdefService;
import com.jbf.workflow.vo.WorkflowTreeVo;

@Scope("prototype")
@Controller
@RequestMapping("/workflow/WfProcessDefinitionController")
public class WfProcessDefinitionController {

	@Autowired
	SysWorkflowDeploymentService sysWorkflowDeploymentService;

	@Autowired
	SysWorkflowProcdefService sysWorkflowProcdefService;

	@Autowired
	SysWorkflowOpdefService sysWorkflowOpdefService;

	/**
	 * 工作流定义页面入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfDefEntry.do" })
	public ModelAndView entry() {
		return new ModelAndView("/workflow/wfDefEntry");
	}

	/**
	 * 工作流版本定义页面入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfconfigEntry.do" })
	public ModelAndView wfconfigEntry() {
		return new ModelAndView("/workflow/wfconfigEntry");
	}

	/**
	 * 工作流版本管理页面入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfmanageEntry.do" })
	public ModelAndView wfmanageEntry() {
		return new ModelAndView("/workflow/wfmanageEntry");
	}

	/**
	 * 工作流节点权限选择对话框页面入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfPrivilegeDialog.do" })
	public ModelAndView wfPrivilegeDialog() {
		return new ModelAndView("/workflow/wfPrivilegeDialog");
	}

	/**
	 * 工作流节点操作类型选择对话框入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfOpSelDialog.do" })
	public ModelAndView wfOpSelDialog() {
		return new ModelAndView("/workflow/wfOpSelDialog");
	}

	/**
	 * 工作流表单定义入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfFormEntry.do" })
	public ModelAndView formEntry() {
		return new ModelAndView("/workflow/wfFormEntry");
	}

	/**
	 * 工作流操作表单入口
	 * 
	 * @return
	 */
	@RequestMapping({ "/wfOperationForm.do" })
	public ModelAndView wfOperationForm() {
		return new ModelAndView("/workflow/wfOperationForm");
	}

	@RequestMapping({ "/wfAssignBoxSelector.do" })
	public ModelAndView wfAssignBoxSelector() {
		return new ModelAndView("/workflow/wfAssignBoxSelector");
	}

	/**
	 * 查询工作流树
	 * 
	 * @param name
	 *            工作流名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/queryWorkflowTree.do" })
	public List<? extends TreeVo> queryWorkflowTree(String name,
			String showVersion) {
		return sysWorkflowProcdefService.queryWorkflowTree(name, showVersion);
	}

	/**
	 * 查询工作流树,用于工作流设置菜单
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/queryWorkflowConfigTree.do" })
	public List<? extends TreeVo> queryWorkflowConfigTree() {
		return sysWorkflowProcdefService.queryWorkflowConfigTree();
	}

	/**
	 * 查询工作流的版本
	 * 
	 * @param key
	 * @return 工作流的版本列表
	 */
	@ResponseBody
	@RequestMapping({ "/queryWorkflowVersion.do" })
	public List<SysWorkflowProcversion> queryWorkflowVersion(String key) {
		return sysWorkflowProcdefService.queryWorkflowVersion(key);
	}

	/**
	 * 查询工作流定义明细
	 * 
	 * @param id
	 * @return 工作流定义明细列表
	 */
	@ResponseBody
	@RequestMapping({ "/getWorkflowProcdefDetail.do" })
	public SysWorkflowProcdef getWorkflowProcdefDetail(Long id) {
		return sysWorkflowProcdefService.get(id);
	}

	/**
	 * 保存工作流定义
	 * 
	 * @param def
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/saveWorkflowProcdef.do")
	public ResultMsg saveWorkflowProcdef(@ModelAttribute SysWorkflowProcdef def) {
		try {
			sysWorkflowProcdefService.saveWorkflowProcdef(def);
			return new ResultMsg(true, "保存成功!");
		} catch (Exception e) {
			return ResultMsg.build(e, "保存失败!");
		}
	}

	/**
	 * 删除工作流定义
	 * 
	 * @param id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/delWorkflowProcdef.do")
	public ResultMsg delWorkflowProcdef(Long id) {
		try {
			sysWorkflowProcdefService.delete(id);
			return new ResultMsg(true, "删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				return new ResultMsg(false, e.getMessage());
			} else {
				return new ResultMsg(false, "删除失败，请查看异常日志!");
			}

		}
	}

	/**
	 * 删除工作流版本
	 * 
	 * @param id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/deleteWfVersion.do" })
	public ResultMsg deleteWfVersion(Long id) {
		try {
			sysWorkflowProcdefService.deleteWfVersion(id);
			return new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			return new ResultMsg(false, e.getMessage());
		}
	}

	/**
	 * 查询部署的xml内容
	 */
	@ResponseBody
	@RequestMapping({ "/getDeployedJpdlContent.do" })
	public void getDeployedJpdlContent(HttpServletRequest req,
			HttpServletResponse response) {
		response.setContentType("text/html; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String procVersionId = req.getParameter("procVersionId");
		Map map = sysWorkflowProcdefService
				.getDeployedJpdlContent(procVersionId);
		try {
			ServletOutputStream op = response.getOutputStream();
			byte[] b = ((String) map.get("xml")).getBytes("UTF-8");
			op.write(b);
			op.flush();
			op.close();
			response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发布流程定义
	 * 
	 * @param map
	 *            map结构为 { xml:xml文件内容, resourceName:jpdl文件名,需要以*.jpdl.xml结尾
	 *            wfDefId 流程定义表的id }
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/deployWorkflowDef.do" })
	public ResultMsg deployWorkflowDef(String xml, String resourceName,
			String name, String key, String version, String firstnode,
			String startdate, String enddate, String extAttrCache) {
		ResultMsg msg = null;
		try {
			sysWorkflowDeploymentService.deployWfDef(xml, resourceName, name,
					key, version, firstnode, startdate, enddate, extAttrCache);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			return ResultMsg.build(e, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}

	/**
	 * 修改流程版本定义的启用日期和停用日期
	 * 
	 * @param key
	 *            流程key
	 * @param version
	 *            流程版本
	 * @param startdate
	 *            启用日期
	 * @param enddate
	 *            停用日期
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/editWorkflowProcVersion.do" })
	public ResultMsg editWorkflowProcVersion(String key, String version,
			String startdate, String enddate) {
		try {
			sysWorkflowDeploymentService.editWorkflowProcVersion(key, version,
					startdate, enddate);
			return new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			return ResultMsg.build(e, AppException.getMessage("crud.saveerr"));
		}
	}

	/**
	 * 取得版本定义信息
	 * 
	 * @param key
	 * @param version
	 * @return 版本定义
	 */
	@ResponseBody
	@RequestMapping({ "/getWorkflowProcversion.do" })
	public SysWorkflowProcversion getWorkflowProcversion(String key,
			String version) {
		return sysWorkflowDeploymentService
				.getWorkflowProcversion(key, version);
	}

	/**
	 * 取得工作流实时流程图片
	 * 
	 * @param req
	 * @param response
	 */
	@RequestMapping({ "/getWorkflowImage.do" })
	public void getWorkflowImage(HttpServletRequest req,
			HttpServletResponse response) {
		try {
			response.setContentType("image/jpeg");
			String instId = req.getParameter("instanceid");
			RenderedImage img = sysWorkflowDeploymentService
					.getImageByWfInstanceId(instId);
			ServletOutputStream op = response.getOutputStream();
			ImageIO.write(img, "png", op);
			op.close();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询工作流定义的操作类型
	 * 
	 * @param wfDefId
	 *            工作流定义id
	 * @return 工作流定义的操作类型
	 */
	@ResponseBody
	@RequestMapping({ "/findWorkflowOpdef.do" })
	public List findWorkflowOpdef(String key) {
		return sysWorkflowOpdefService.query(key);
	}

	/**
	 * 取得操作类型详情
	 * 
	 * @param id
	 *            操作定义id
	 * @return 取得操作类型详情
	 */
	@ResponseBody
	@RequestMapping({ "/getWorkflowOpdefDetail.do" })
	public SysWorkflowOpdef getWorkflowOpdefDetail(Long id) {
		return sysWorkflowOpdefService.get(id);
	}

	/**
	 * 保存工作流操作定义
	 * 
	 * @param WorkflowOpdef
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/saveWorkflowOpdef.do" })
	public ResultMsg saveWorkflowOpdef(
			@ModelAttribute SysWorkflowOpdef WorkflowOpdef) {
		try {
			sysWorkflowOpdefService.save(WorkflowOpdef);
			return new ResultMsg(true, "保存成功!");
		} catch (Exception e) {
			return ResultMsg.build(e, "保存失败!");
		}
	}

	/**
	 * 删除工作流操作定义
	 * 
	 * @param id
	 *            操作定义id
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/delWorkflowOpdef.do" })
	public ResultMsg saveWorkflowOpdef(Long id) {
		try {
			sysWorkflowOpdefService.del(id);
			return new ResultMsg(true, "删除成功!");
		} catch (Exception e) {
			return ResultMsg.build(e, "删除失败!");
		}
	}

	/**
	 * 查询工作流版本
	 * 
	 * @param key
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/queryWorkflowVersionsByKey.do" })
	public List queryWorkflowVersionsByKey(String key) {
		return sysWorkflowProcdefService.queryWorkflowVersionsByKey(key);
	}

	@ResponseBody
	@RequestMapping({ "/getExtendedAttributes.do" })
	public List getExtendedAttributes(String key, Integer version) {
		return sysWorkflowDeploymentService.getExtendedAttributes(key, version);
	}

	/**
	 * 工作流复制界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping({ "/wfcopyEntry.do" })
	public ModelAndView wfcopyEntry() {

		return new ModelAndView("/workflow/wfCopySet");
	}

	/**
	 * 工作流复制
	 * 
	 * @param sourceWfID
	 * @param sourceWfName
	 * @param objectWfKey
	 * @param objectWfName
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping({ "/wfcopyopt.do" })
	public ResultMsg wfcopyopt(Long sourceWfID, String sourceWfName,
			String objectWfKey, String objectWfName) {

		ResultMsg resultMsg;
		try {
			resultMsg = sysWorkflowProcdefService.wfcopyopt(sourceWfID,
					sourceWfName, objectWfKey, objectWfName);
		} catch (AppException e) {
			resultMsg = new ResultMsg(false, "复制失败，失败原因：" + e.getMessage());
			e.printStackTrace();
		}
		return resultMsg;
	}

	/**
	 * 工作流设计器内部调用 更新工作流的单个扩展属性，用于工作流扩展属性修改功能
	 * 
	 * @param key
	 * @param version
	 * @param category
	 *            类别
	 * @param actiName
	 *            活动名称
	 * @param transName
	 *            迁移线名称
	 * @param fieldName
	 *            属性字段名
	 * @param fieldValue
	 *            属性值
	 * @return
	 */
	@ResponseBody
	@RequestMapping({ "/workflowExtAttrUpdate.do" })
	public ResultMsg workflowExtAttrUpdate(String key, Integer version,
			String category, String actiName, String transName,
			String fieldName, String fieldValue) {

		ResultMsg resultMsg = new ResultMsg(true, "保存成功！");
		try {
			sysWorkflowDeploymentService.workflowExtAttrUpdate(key, version,
					category, actiName, transName, fieldName, fieldValue);
		} catch (Exception e) {
			resultMsg = new ResultMsg(false, "保存失败！");
			e.printStackTrace();
		}
		return resultMsg;
	}

	/**
	 * 工作流节点表单是否可编辑
	 * 
	 * @param key
	 * @param version
	 * @param actiId
	 * @return
	 */
	public HashMap isWorkflowTaskFormEditable(String key, Integer version,
			String actiId) {
		return sysWorkflowDeploymentService.isWorkflowTaskFormEditable(key,
				version, actiId);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping({ "/wfDataScopeEntry.do" })
	public ModelAndView wfDataScopeEntry(HttpServletRequest request)
			throws UnsupportedEncodingException {

		String tasknames = request.getParameter("transition_names");
		String wfkey = request.getParameter("wfkey");
		String wfversion = request.getParameter("wfversion");
		String decisionname = request.getParameter("decisionname");
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("tasknames", tasknames);
		modelMap.put("wfkey", wfkey);
		modelMap.put("wfversion", wfversion);
		modelMap.put("decisionname", URLDecoder.decode(decisionname, "UTF-8"));

		return new ModelAndView("/workflow/wfDataScopeEntry", "modelMap",
				modelMap);
	}

}

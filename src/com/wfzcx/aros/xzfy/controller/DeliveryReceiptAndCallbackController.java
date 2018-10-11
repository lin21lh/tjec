package com.wfzcx.aros.xzfy.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.wfzcx.aros.xzfy.service.DeliveryReceiptAndCallbackService;

/**
 * 行政复议案件文书送达和廉政回访
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/xzfy/controller/DeliveryReceiptAndCallbackController")
public class DeliveryReceiptAndCallbackController {
	
	@Autowired
	DeliveryReceiptAndCallbackService service;
	@Autowired
	ParamCfgComponent pcfg;
	
	/**
	 *查询文书送达列表
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryReceiptGrid.do")
	@ResponseBody
	public EasyUITotalResult queryReceiptGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryReceiptGrid(map);
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 删除文书送达信息
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toDeleteReceipt.do")
	@ResponseBody
	public ResultMsg toDeleteReceipt(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			String id = (String) map.get("cdrid");
			service.deleteReceiptById(id);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
	
	/**
	 * 调查笔录保存
	 * @Title: toSaveReceipt 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toSaveReceipt.do")
	@ResponseBody
	public ResultMsg toSaveReceipt(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.saveReceipt(map);
			msg = new ResultMsg(true, AppException.getMessage("保存成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("保存失败"));
		}
		return msg;
	}
	
	/**
	 *查询文书送达列表
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryCallBackGrid.do")
	@ResponseBody
	public EasyUITotalResult queryCallBackGrid(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryCallBackGrid(map);
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 删除文书送达信息
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toDeleteCallBack.do")
	@ResponseBody
	public ResultMsg toDeleteCallBack(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			String id = (String) map.get("ciid");
			service.deleteCallBackById(id);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
	}
	
	/**
	 * 廉政回访保存
	 * @Title: toSaveReceipt 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param request
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/toSaveCallBack.do")
	@ResponseBody
	public ResultMsg toSaveCallBack(HttpServletRequest request) throws AppException {
		ResultMsg msg = null;
		try {
			Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
			service.saveCallBack(map);
			msg = new ResultMsg(true, AppException.getMessage("保存成功"));
		}catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("保存失败"));
		}
		return msg;
	}
	
}

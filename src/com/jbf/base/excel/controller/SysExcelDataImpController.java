/************************************************************
 * 类名：SysExcelDataImpController.java
 *
 * 类别：Controller
 * 功能：提供数据导入功能页面入口和数据导入功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.controller;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jbf.base.excel.po.SysExcelImpLog;
import com.jbf.base.excel.service.SysExcelDataImpService;
import com.jbf.base.excel.service.SysExcelImpLogService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.web.ResultMsg;

@Controller
@RequestMapping("/base/excel/SysExcelDataImpController")
public class SysExcelDataImpController {

	@Autowired
	SysExcelDataImpService sysExcelDataImpService;

	@Autowired
	SysExcelImpLogService sysExcelImpLogService;

	/**
	 * 数据导入入口
	 * 
	 * @return 入口页面
	 */
	@RequestMapping("/dataImpEntry.do")
	public String entry() {
		return "base/excel/dataImpEntry";
	}

	/**
	 * 上传文件并保存数据
	 * 
	 * @param request
	 * @return 成功标志
	 */
	@ResponseBody
	@RequestMapping("/uploadExcelDataFile.do")
	public ResultMsg uploadExcelDataFile(HttpServletRequest request) {
		Map<String, String> map = ControllerUtil
				.getRequestParameterMap(request);

		SysExcelImpLog log = new SysExcelImpLog();

		log.setCfgid(Long.parseLong(map.get("cfgid")));
		log.setImpdate(DateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
		log.setIpaddr(getIpAddr(request));
		log.setUsercode(SecureUtil.getCurrentUser().getUsercode());
		log.setUsername(SecureUtil.getCurrentUser().getUsername());
		String fileName = null;
		try {

			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			// 获得文件
			MultipartFile file = multipartRequest.getFile("execlfile");
			// 获得输入流
			InputStream input = file.getInputStream();
			map.put("_FILE_NAME_", file.getOriginalFilename());
			fileName = file.getOriginalFilename();
			log.setMessage(AppException.getMessage("excel.imp.succ.filename",
					new String[] { fileName }));
			String batchno = sysExcelDataImpService.uploadExcelDataFile(map,
					input);
			log.setBatch(batchno);
			log.setSucc(true);

			sysExcelImpLogService.saveLog(log);
			return new ResultMsg(true,
					AppException.getMessage("excel.imp.succ"));
		} catch (Exception e) {
			log.setSucc(false);
			String errmsg = AppException.getMessage("excel.imp.fail.filename",
					new String[] { fileName });
			if (e instanceof AppException) {
				errmsg += e.getMessage();
			}
			log.setMessage(errmsg);
			sysExcelImpLogService.saveLog(log);
			return ResultMsg.build(e,
					AppException.getMessage("excel.imp.failed"));
		}
	}

	/**
	 * 取得请求的ip地址
	 * 
	 * @param request
	 * @return ip地址
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 查询导入日志
	 * 
	 * @param request
	 * @return 导入日志信息
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDataImplLog.do")
	@ResponseBody
	public EasyUITotalResult queryDataImplLog(HttpServletRequest request) {

		Map<String, Object> map = ControllerUtil
				.getRequestParameterMap(request);
		PaginationSupport ps = sysExcelImpLogService.queryDataImplLog(map);
		return EasyUITotalResult.from(ps);
	}

}

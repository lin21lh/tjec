/************************************************************
 * 类名：ValidRuleController.java
 *
 * 类别：Controller
 * 功能：提供校验规则功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.base.dic.service.ValidRuleService;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping({"/base/dic/validRuleController"})
public class ValidRuleController {

	@Autowired
	ValidRuleService validRuleService;
	
	/**
	 * 判断是否已存在
	 * @param request tablecode 表名 value 值 id id值 elementcode 数据项编码
	 * @return Integer 1=已存在 0=未存在
	 * @throws SecurityException
	 * @throws AppException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	@ResponseBody
	@RequestMapping({ "/validIsExist.do" })
	public Integer validIsExist(HttpServletRequest request) throws SecurityException, AppException, ClassNotFoundException, NoSuchFieldException   {
		
		String tablecode = request.getParameter("tablecode");
		String value = request.getParameter("value");
		String id = request.getParameter("id");
		String elementcode = request.getParameter("elementcode");
		
		return validRuleService.validIsExist(tablecode, id, value, elementcode);
	}
	
	/**
	 * 验证是否能新增下级
	 * @param elementcode 数据项编码
	 * @param levelno 当前级次
	 * @return 校验结果
	 */
	@ResponseBody
	@RequestMapping({ "/validLevelElement.do" })
	public ResultMsg validLevelElement(String elementcode, Integer levelno) {
		ResultMsg resultMsg = null;
		String msg = validRuleService.validLevelElement(elementcode, levelno);
		if (msg.length() > 0)
			resultMsg = new ResultMsg(false, msg);
		else
			resultMsg = new ResultMsg(true, "");
		return resultMsg;
	}
}

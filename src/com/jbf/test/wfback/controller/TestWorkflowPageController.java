/************************************************************
 * 类名：TestWorkflowPageController
 *
 * 类别：Controller
 * 功能：工作流测试控制器
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-16  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.test.wfback.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Scope("prototype")
@Controller
@RequestMapping("/test/TestWorkflowPageController")
public class TestWorkflowPageController {
	/**
	 * 测试页面入口 
	 * @return
	 */
	@RequestMapping("/entry.do")
	public String entry() {
		return "/test/wfbacktest";
	}
	
	
}

/************************************************************
 * 类名：View.java
 *
 * 类别：异常处理
 * 功能：Http请求处理异常
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   maqs         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface View {

	
	void view(HttpServletRequest request, HttpServletResponse response,FileterSecurityException e);
}

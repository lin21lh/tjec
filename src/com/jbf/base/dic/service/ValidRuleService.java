/************************************************************
 * 类名：ValidRuleService.java
 *
 * 类别：Service接口
 * 功能：规则校验服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service;

import com.jbf.common.exception.AppException;

public interface ValidRuleService {

	/**
	 * 校验数据已被使用
	 * @param tablecode 表名
	 * @param value 须判断值
	 * @param elementcode 数据项编码
	 * @return 1=已使用 0=未使用
	 */
	public Integer validIsExist(String tablecode, String id, String value, String elementcode) throws AppException, SecurityException, ClassNotFoundException, NoSuchFieldException;
	
	/**
	 * 校验层码数据项编码
	 * @param elementcode
	 * @param currentlevel
	 * @return
	 */
	public String validLevelElement(String elementcode, Integer currentlevel);
}

/************************************************************
 * 类名：DicElementValSetComponent.java
 *
 * 类别：组件接口
 * 功能：数据项值集组件接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface DicElementValSetComponent {
	
	/**
	 * 获取数据项值集
	 * @param elementcode 数据项编码
	 * @return 数据项值集集合
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	public List getElementValList(String elementcode) throws SecurityException, ClassNotFoundException, NoSuchFieldException;
	
	/**
	 * 清除缓存
	 * @param elementcode 数据项编码
	 */
	public void clearCache(String elementcode);
	
	/**
	 * 加载缓存
	 * @param elementcode 数据项编码
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * @throws NoSuchFieldException
	 */
	public void loadCache(String elementcode) throws SecurityException, ClassNotFoundException, NoSuchFieldException;
	
	/**
	 * 设置缓存
	 * @param elementcode 数据项编码
	 * @param list 数据项值集
	 */
	public void setCache(String elementcode, List list);
	
	/**
	 * 获取数据项值集编码
	 * @param elementcode 数据项编码
	 * @param id 数据项值集ID
	 * @return 数据项值集编码
	 */
	public String getCodeByID(String elementcode, Long id);
	
	/**
	 * 获取数据项值集ID
	 * @param elementcode 数据项编码
	 * @param code 数据项值集编码
	 * @return 数据项值集ID
	 */
	public Long getElementIDByCode(String elementcode, String code);
	
	/**
	 * 获取数据项值
	 * @param o 数据项Object
	 * @param key key名称
	 * @return 值
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public String getValue(Object o, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
}

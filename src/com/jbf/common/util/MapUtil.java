/************************************************************
 * 类名：MapUtils
 *
 * 类别：工具类
 * 功能：map相关操作类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-14  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class MapUtil {
	/**
	 * 将map对象转为po对象
	 * 
	 * @param source
	 *            map对象
	 * @param target
	 *            po对象
	 * @return po对象
	 */
	public static Object cloneMapToObj(Map source, Object target) {
		Field[] fields = target.getClass().getDeclaredFields();
		for (Field field : fields) {

			if (source.get(field.getName().toLowerCase()) == null)
				continue;
			try {
				Method method2 = target.getClass()
						.getDeclaredMethod(
								"set"
										+ field.getName().substring(0, 1)
												.toUpperCase()
										+ field.getName().substring(1),
								field.getType());

				if (field.getType().toString().equals(String.class.toString())) {
					method2.invoke(target,
							source.get(field.getName().toLowerCase())
									.toString());
				} else if (field.getType().toString().equals(int.class.toString())
						|| field.getType().toString()
								.equals(Integer.class.toString())) {
					method2.invoke(
							target,
							Integer.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				} else if (field.getType().toString().equals(long.class.toString())
						|| field.getType().toString()
								.equals(Long.class.toString())) {
					method2.invoke(
							target,
							Long.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				} else if (field.getType().toString().equals(double.class.toString())
						|| field.getType().toString()
								.equals(Double.class.toString())) {
					method2.invoke(
							target,
							Double.valueOf(source.get(
									field.getName().toLowerCase()).toString()));
				} else if (field.getType().toString().equals(Date.class.toString())) {
					try {
						method2.invoke(target, new SimpleDateFormat(
								"yyyy-MM-dd").parse(source.get(
								field.getName().toLowerCase()).toString()));
					} catch (ParseException e) {

						e.printStackTrace();
					}
				} else if (field.getType().toString().equals(boolean.class.toString())) {
					method2.invoke(
							target,
							Boolean.parseBoolean(source.get(
									field.getName().toLowerCase()).toString()));
				} else if (field.getType().toString().equals(Byte.class.toString())) {
					method2.invoke(target, Byte.parseByte(source.get(field.getName().toLowerCase()).toString()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return target;
	}
	/**
	 * 由HttpServletRequest取得Map参数
	 * @param req
	 * @return
	 */
	public static Map<String, String> getMapFromRequest(HttpServletRequest req) {
		Map<String, String[]> orgMap = req.getParameterMap();

		Map<String, String> tgtMap = new HashMap<String, String>();
		for (String key : orgMap.keySet()) {
			String[] array = orgMap.get(key);
			if (array != null) {
				tgtMap.put(key, array[0]);
			}
		}
		return tgtMap;
	}
}

/************************************************************
 * 类名：UserOutExcel.java
 *
 * 类别：用户列表导出Excel类
 * 功能：导出用户列表数据到Excel
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2015-01-21  CFIT-PM   mqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.outexcel.exceute;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;

@Scope("prototype")
@Component("com.jbf.base.excel.outexcel.exceute.UserOutExcel")
public class UserOutExcel implements ExcelData {

	@Autowired
	SysUserDao userDao;
	
	public List getExcelData(Map paramMap) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysUser.class);
		String usercode = (String)paramMap.get("usercode");
		String username = (String)paramMap.get("username");
		if (StringUtil.isNotBlank(usercode))
			dc.add(Property.forName("usercode").like(usercode, MatchMode.START));

		if (StringUtil.isNotBlank(username)) 
			dc.add(Property.forName("username").like(username, MatchMode.ANYWHERE));

		// 非超级管理用户只能查看自己当前创建的用户
		SysUser cUser = SecureUtil.getCurrentUser();
		if (cUser.getUsertype() != 2) {
			dc.add(Restrictions.eq("createuser", cUser.getUsername()));
		}

		Object sortobj = paramMap.get("sort");
		if (sortobj != null && StringUtil.isNotBlank(String.valueOf(sortobj))) {
			String sorts = String.valueOf(paramMap.get("sort"));
			String orders = String.valueOf(paramMap.get("order"));
			for (int i = 0; i < sorts.split(",").length; i++) {
				String sort = sorts.split(",")[i];
				String order = orders.split(",")[i];
				if ("asc".equals(order)) {
					dc.addOrder(Order.asc(sort));
				} else {
					dc.addOrder(Order.desc(sort));
				}
			}
		}

		return userDao.findByCriteria(dc);
	}

}

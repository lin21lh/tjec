/************************************************************
 * 类名：JbfSecurityMetadataSource.java
 *
 * 类别：SpringSecurity
 * 功能：加载资源与权限的对应关系
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-11-25  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.security.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.resource.po.SysResource;

/**
 * 加载资源与权限的对应关系
 * 
 * @author maqs
 * 
 */
public class JbfSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private SysResourceDao resourceDao;

	public void setResourceDao(SysResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		List<SysResource> reslist = new ArrayList<SysResource>();// resourceDao.getAllMenus();
		List<ConfigAttribute> list = new ArrayList<ConfigAttribute>();
		for (SysResource res : reslist) {
			list.add(new SecurityConfig("ROLE_" + res.getResourceid()));
		}
		return list;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	/**
	 * 返回所请求资源所需要的权限
	 */
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		List<SysResource> resources = null;
		String code = null;
		resources = null;// pubmenuDao.findResourcesByUrl(getUrlNotParam(requestUrl));

		if (resources == null) {
			return null;
		}

		if (resources.size() == 1) {
			code = resources.get(0).getResourceid().toString();
		} else {
			// code = getRsCode(menu, requestUrl);
		}

		ConfigAttribute configAttribute = new SecurityConfig("ROLE_" + code);
		return Arrays.asList(configAttribute);
	}

	public String getUrlNotParam(String requestUrl) {
		int index = requestUrl.indexOf("?");
		return index > -1 ? requestUrl.substring(0, index) : requestUrl;
	}
}
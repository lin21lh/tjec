package com.jbf.common.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerUtil {

	/**
	 * 获取页面参数值并将之转换成map对象
	 * 
	 * @param request
	 * @return 包含参数的Map
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static public Map getRequestParameterMap(HttpServletRequest request) {
		Enumeration parameterNames = request.getParameterNames();
		HashMap map = new HashMap();
		Object value = null;
		while (parameterNames.hasMoreElements()) {
			String key = (String) parameterNames.nextElement();
			value = request.getParameter(key);
			if (value != null)
				map.put(key, value);
		}
		return map;
	}

	/**
	 * Description:回写页面
	 * 
	 * @param msg
	 * 
	 * @return
	 */
	static public void responseWriter(String msg, HttpServletResponse response) {
		try {
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setContentType("text/javascript;charset=utf-8");
			Writer out = response.getWriter();
			out.write(msg != null ? msg : "");
			response.flushBuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

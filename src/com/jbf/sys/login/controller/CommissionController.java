package com.jbf.sys.login.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.jbf.sys.toRemind.service.ToRemindService;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
@Controller
public class CommissionController {
	@Autowired
	SysUserDao sysUserDao;
	@Autowired
	ToRemindService toRemindService;
	@RequestMapping("/getWaitWorkInfo.do")
	public void getWaitWorkInfo(HttpServletRequest request,	HttpServletResponse response) {
		boolean success = true;
		String reason = "";
		List list = new ArrayList();
		
		//多用户循环模拟登录，获取待办任务
		String usernames = request.getParameter("j_username");
		if (usernames == null || usernames.equals("")) {
			success = false;
			reason = "需要传入用户名";
			writeToPage(request, response, list, success, reason);
			return;
		}
		
		String[] arrUsername = usernames.split(",");
		for (String username : arrUsername) {
			try {
				List<SysUser> list1 = (List<SysUser>) sysUserDao.find("from SysUser where usercode ='"+username+"'");
				if (list1.isEmpty()) {
					continue;
				}
				SysUser user = list1.get(0);
				//获取待办任务
				List list2 = toRemindService.findRemindResourceListByUser(user);
				
				//加入用户名信息
				addUsernameInfo(list2, username);
				
				list.addAll(list2);
			} catch (Exception e) {
				success = false;
				reason = e.getMessage();
				e.printStackTrace();
			}
		}
	
		//把待办信息写回页面
		writeToPage(request, response, list, success, reason);
	}

	/**
	 * 把待办信息写回页面
	 * @param request
	 * @param response
	 * @param list
	 * @param success
	 * @param reason
	 */
	private void writeToPage(HttpServletRequest request,
			HttpServletResponse response, List list, boolean success,
			String reason) {
		JSONObject obj = new JSONObject();
		obj.put("items", list);
		obj.put("results", list.size());
		obj.put("success", success);
		obj.put("reason", reason);

		String jsoncallback = request.getParameter("jsoncallback");
		String msg = jsoncallback + "(" + obj.toString() + ")";
		
		try {
			response.setHeader("Pragma","no-cache");
			response.setHeader("Cache-Control","no-cache");
			response.setDateHeader("Expires",0);
			response.setContentType("text/javascript;charset=GBK");
			Writer	out = response.getWriter();
			out.write(msg);
			response.flushBuffer();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * 加入用户名信息
	 * @param list
	 * @param username
	 */
	private void addUsernameInfo(List<Map> list, String username){
		if (list == null || list.size() == 0) {
			return;
		}
		
		for (Map map : list) {
			map.put("username", username);
		}
	}
}

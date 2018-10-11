package com.jbf.sys.user.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserInfoService;

/**
 * 个人信息维护
 * @author songxiaojie
 *
 */
@Controller
@RequestMapping({ "/sys/SysUserInfoController" })
public class SysUserInfoController {

	@Autowired
	SysUserInfoService sysUserInfoService;
	
	/**
	 * 展示个人信息
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showPersonalInfo.do")
	@ResponseBody
	public Map showPersonalInfo(HttpServletRequest request) throws AppException
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		SysUser user = SecureUtil.getCurrentUser();
		Long userId=user.getUserid();
		map.put("userid", userId);
		
		//获取用户扩展表记录
		int count = sysUserInfoService.getCountUserExtByUserId(userId);
		map.put("countExt", count);
		Map<String,Object> userInfoMap = (Map<String, Object>) sysUserInfoService.getUserInfo(map);
		userInfoMap.put("countExt", count);
		
		return userInfoMap;
	}
	
	/**
	 * 修改个人信息
	 * @param username
	 * @param pswdold
	 * @param pswdnew
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/editPersonalInfo.do")
	public ResultMsg editPersonalInfo(SysUser sysUser,String phone,String qq,String email,String weixin){
		ResultMsg msg = null;
		try {
			SysUser user = SecureUtil.getCurrentUser();
			Long userId=user.getUserid();
			sysUser.setUserid(userId);
			//将信息封装在map
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phone", phone);
			map.put("email", email);
			map.put("weixin", weixin);
			map.put("qq", qq);
			
			//处理用户信息
			sysUserInfoService.editPersonalInfo(sysUser, map);
			msg = new ResultMsg(true, AppException.getMessage("crud.editok"));
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof AppException) {
				msg = new ResultMsg(false, e.getMessage());
			} else {
				msg = new ResultMsg(false,
						AppException.getMessage("crud.editerr"));
			}
		}
		return msg;
	}
}

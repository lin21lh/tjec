package com.jbf.sys.toRemind.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.exception.AppException;
import com.jbf.sys.toRemind.service.ToRemindService;
import com.jbf.sys.toRemind.vo.ToRemindVo;
import com.jbf.sys.user.po.SysUser;

/**
 * 待办提醒
 * @ClassName: ToRemindController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月18日 上午9:25:51
 */
@Scope("prototype")
@Controller
@RequestMapping({"/sys/toRemind/ToRemindController"})
public class ToRemindController {
	
	@Autowired
	ToRemindService toRemindService;
	
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		
		return new ModelAndView("login/welcomeIndex");
	}

	@RequestMapping("/findWaitWorkInfo.do")
	@ResponseBody
	public List<ToRemindVo> findWaitWorkInfo() throws AppException {
		
		List<ToRemindVo> list = toRemindService.findRemindResourceList();
		return list;
	}
	/**
	 * 
	 * @Title: findWaitWorkInfoByUser 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param user
	 * @return
	 * @throws AppException 设定文件
	 */
	@RequestMapping("/findWaitWorkInfoByUser.do")
	@ResponseBody
	public List<ToRemindVo> findWaitWorkInfoByUser(SysUser user) throws AppException {
		
		List<ToRemindVo> list = toRemindService.findRemindResourceListByUser(user);
		return list;
	}
	
}

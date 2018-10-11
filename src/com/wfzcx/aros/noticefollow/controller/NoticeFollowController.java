/**
 * @Description: 通知书跟踪controller
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.noticefollow.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.noticefollow.po.Noticemanagebaseinfo;
import com.wfzcx.aros.noticefollow.service.NoticeFollowService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/noticefollow/controller/NoticeFollowController")
public class NoticeFollowController
{
	@Autowired
	private NoticeFollowService noticeFollowService;
	
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/noticefollow/noticeFollow_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNotice.do")
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) throws ServletException
	{ 
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = noticeFollowService.queryNotice(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/sendManage.do")
	public ModelAndView sendManage(HttpServletRequest request) throws ServletException
	{ 
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/noticefollow/noticeFollow_sendform");
		String id = request.getParameter("id");
		Noticemanagebaseinfo info =  noticeFollowService.queryNoticeInfo(id);
		
		if (null != info)
		{
			mav.addObject("info", info);
		}
		
		return mav;
	}
	
	@RequestMapping("/receiveManage.do")
	public ModelAndView receiveManage(HttpServletRequest request) throws ServletException
	{ 
		// 返回页面路径
		ModelAndView mav = new ModelAndView("aros/noticefollow/noticeFollow_receiveform");
		String id = request.getParameter("id");
		Noticemanagebaseinfo info =  noticeFollowService.queryNoticeInfo(id);
		
		if (null != info)
		{
			mav.addObject("info", info);
		}
		
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/save.do")
	public ResultMsg saveNotice(HttpServletRequest request) throws ServletException
	{ 
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Noticemanagebaseinfo info = noticeFollowService.queryNoticeInfo((String)param.get("id"));
		
		try
		{
			// 更新数据
			if (null != info)
			{
				BeanUtils.populate(info, param);
				noticeFollowService.update(info);
				msg = new ResultMsg(true, "更新成功");
			}
			else
			{
				info = new Noticemanagebaseinfo();
				BeanUtils.populate(info, param);
				// 获取当前登录用户信息
				SysUser user = SecureUtil.getCurrentUser();
				info.setOperator(user.getUsername());
				info.setOpttime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				
				String noticeid = noticeFollowService.save(info);
				if (StringUtils.isNotBlank(noticeid))
				{
					msg = new ResultMsg(true, "保存成功");
				}
				else
				{
					msg = new ResultMsg(false, "保存失败");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
}

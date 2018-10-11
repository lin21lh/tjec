/**
 * @Description: 通知书打印controller
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.print.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.print.po.NoticeContentInfo;
import com.wfzcx.aros.print.service.NoticeService;
import com.wfzcx.aros.util.UtilTool;

@Scope("prototype")
@Controller
@RequestMapping("/aros/print/controller/NoticeController")
public class NoticeController
{
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request)
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/print/noticeManage_init");
		//菜单ID
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryXzfyList.do")
	@ResponseBody
	public EasyUITotalResult queryXzfyList(HttpServletRequest request)
	{ 
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = noticeService.queryXzfyList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/generateNotice.do")
	public ModelAndView generateNotice(HttpServletRequest request)
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/print/generateNotice_form");
		return mav;
	}
	
	@RequestMapping("/noticeContent.do")
	public ModelAndView noticeContent(HttpServletRequest request)
	{ 
		// 通知书类型
		String doctype  = request.getParameter("doctype");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		ModelAndView mav = new ModelAndView();
		// 通知书配置
		List<Map<String, Object>> noticeConfig = noticeService.queryNoticeConfig(doctype);
		if (!noticeConfig.isEmpty())
		{
			Map<String, Object> configMap = noticeConfig.get(0);
			mav.setViewName((String)configMap.get("url"));
			NoticeContentInfo info = new NoticeContentInfo();
			// 通知书内容信息
			String xml = noticeService.queryNoticeContent(caseid, doctype).replace(" ", "");
			if (StringUtils.isNotBlank(xml))
			{
				info = (NoticeContentInfo)UtilTool.xmlToObj(xml);
			}
			else
			{
				// 流程类型
				String protype = (String)configMap.get("protype");
				// 字典编码
				String elementcode = (String)configMap.get("elementcode");
				info = noticeService.queryNoticeContentInfo(caseid, protype, elementcode);
			}
			mav.addObject("info", info);
		}
		
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/saveNotice.do")
	@ResponseBody
	public ResultMsg saveNotice(HttpServletRequest request)
	{ 
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		boolean flag = noticeService.saveNotice(param);
		if (flag)
		{
			msg = new ResultMsg(true, "保存成功");
		}
		else
		{
			msg = new ResultMsg(false, "保存失败");
		}
		
		return msg;
	}
	
	/**
	 * 根据流程类型、节点ID、处理结果查询通知书类型
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryDoctype.do")
	@ResponseBody
	public List<Map<String, Object>> queryDoctype(HttpServletRequest request)
	{ 
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		List<Map<String, Object>> list = noticeService.queryDoctype(param);
		return list;
	}
}

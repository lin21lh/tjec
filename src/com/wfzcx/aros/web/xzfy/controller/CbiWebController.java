package com.wfzcx.aros.web.xzfy.controller;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.HashMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.jzgl.service.CaseFileManageService;
import com.wfzcx.aros.print.po.Noticebaseinfo;
import com.wfzcx.aros.flow.service.ProbaseinfoService;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.web.xzfy.service.CbiWebService;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @ClassName: CbiWebController
 * @Description: 行政复议外网部分，实现外网行政复议查询申请等功能
 * @author ybb
 * @date 2016年9月8日 上午10:26:00
 * @version V1.0
 */
@Scope("prototype")
@Controller
public class CbiWebController {

	@Autowired
	private CbiWebService cbiWebService;
	@Autowired
	private ProbaseinfoService probaseinfoService;
	
	@Autowired
	private CaseFileManageService caseFileManageService;
	
	/**
	 * @Title: CbiWebController_init
	 * @Description: 外网案件查询-返回列表页面
	 * @author ybb
	 * @date 2016年9月8日 上午11:08:24
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/CbiWebController_init.do"})
	public ModelAndView initCbiList(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfylist_init");
		
		return mav;
	}
	
	/**
	 * @Title: queryCbiList
	 * @Description: 外网案件查询-根据条件分页查询行政复议申请列表
	 * @author ybb
	 * @date 2016年9月8日 上午11:37:04
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/CbiWebController_queryList.do")
	@ResponseBody
	public EasyUITotalResult queryCbiList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = cbiWebService.queryCbiList(param);
		
		return EasyUITotalResult.from(ps);
	}

	
	/**
	 * @Description: 外网案件查询-通知书
	 * @author zhangtiantian
	 * @date 2016-09-09
	 */
	@RequestMapping({ "/CbiWebController_noticeInit.do" })
	public ModelAndView noticeInit(HttpServletRequest request)
	{
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/noticelist_init");
		return mav;
	}
	
	/**
	 * @Description: 外网案件查询-获取通知书列表
	 * @author zhangtiantian
	 * @date 2016-09-09
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/CbiWebController_queryNoticeList.do" })
	@ResponseBody
	public EasyUITotalResult queryNoticeList(HttpServletRequest request)
	{
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = cbiWebService.queryNoticeList(param);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Description: 外网案件查询-下载doc版通知书
	 * @author zhangtiantian
	 * @date 2016-09-09
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({ "/CbiWebController_downLoadFile.do" })
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response)
	{
		Writer out = null;
		try
		{
			Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
			String noticeId = (String) param.get("noticeid");
			
			if(StringUtils.isEmpty(noticeId))
			{
				return;
			}
			Noticebaseinfo noticebaseinfo = caseFileManageService.queryNoticeInfo(noticeId);
			Map<String, String> docMap = new HashMap<String, String>();
			String content = noticebaseinfo.getContents();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(new InputSource(new StringReader(content)));   
	        Element root = doc.getDocumentElement();
	        NodeList nodes = root.getChildNodes();
			for (int i = 0; i<nodes.getLength(); i++ )
			{
				Node item = nodes.item(i);
				docMap.put(item.getNodeName(), item.getTextContent());
			}
			String noticeTypeName = caseFileManageService.getNoticeTypeName(noticebaseinfo.getDoctype());
			String expFileName = URLEncoder.encode(noticeTypeName + ".doc", "UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; filename=" + expFileName);
			
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			configuration.setClassForTemplateLoading(this.getClass(), "/com/wfzcx/aros/jzgl/template");
			String temFileName = noticeTypeName + "_模板";
			Template t = configuration.getTemplate(temFileName + ".ftl");
			out = new OutputStreamWriter(response.getOutputStream());
			t.process(docMap, out);
			out.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != out)
			{
				try
				{
					out.close();
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * @Title: addNoticeInit
	 * @Description: 外网行政复议申请-返回申请须知页面
	 * @author ybb
	 * @date 2016年9月8日 下午3:46:54
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/CbiWebController_addNoticeInit.do"})
	public ModelAndView addNoticeInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfynotice_form");
		
		return mav;
	}
	
	/**
	 * @Title: addInit
	 * @Description: 外网行政复议申请-返回申请页面
	 * @author ybb
	 * @date 2016年9月8日 下午3:20:19
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/CbiWebController_addInit.do"})
	public ModelAndView addInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_form");
		//附件ID
		UUID uuid = UUID.randomUUID();
		
		//新增时产生临时的uuid放在附件的keyid中
		mav.addObject("fjkeyid", uuid);
		
		return mav;
	}
	
	/**
	 * @Title: queryCbByElementcode
	 * @Description: 返回公共字典列表
	 * @author ybb
	 * @date 2016年9月8日 下午5:47:51
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@ResponseBody
	@RequestMapping("/queryCbByElementcode.do")	
	public List queryCbByElementcode(HttpServletRequest request) throws AppException {
		
		String elementcode = request.getParameter("elementcode");
		
		return cbiWebService.queryDicByElemenetcode(elementcode);
	}
	
	/**
	 * @Title: addXzfyReq
	 * @Description: 外网行政复议申请-新增行政复议申请
	 * @author ybb
	 * @date 2016年9月9日 下午2:15:01
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/CbiWebController_add.do")
	@ResponseBody
	public ResultMsg addXzfyReq(HttpServletRequest request) {

		ResultMsg msg = null;

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);

		try {
			String caseid = cbiWebService.addXzfyReq(param);
			if (!StringUtils.isBlank(caseid)) {
				msg = new ResultMsg(true, caseid);
			} else {
				msg = new ResultMsg(false, "申请失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}

		return msg;
	}
	
	/**
	 * @Title: xzfyReqDetail
	 * @Description: 外网行政复议申请：提交成功后返回此页面
	 * @author ybb
	 * @date 2016年9月9日 下午5:29:15
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException 
	 */
	@RequestMapping({"/CbiWebController_detail.do"})
	public ModelAndView xzfyReqDetail(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_detail");
		
		//案件ID
		String caseid  = request.getParameter("caseid");
		mav.addObject("caseid", caseid);
				
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = cbiWebService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: xzfyReqView
	 * @Description: 案件查询（外网）- 复议案件详情
	 * @author ybb
	 * @date 2016年9月12日 下午1:17:20
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException
	 */
	@RequestMapping({"/CbiWebController_view.do"})
	public ModelAndView xzfyReqView(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_view");
		
		//菜单ID
		String caseid  = request.getParameter("caseid");
				
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = cbiWebService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
		
		return mav;
	}
	
	/**
	 * @Title: xzfyReqFlow
	 * @Description: 案件查询（外网）- 进度信息
	 * @author ybb
	 * @date 2016年9月12日 下午1:57:36
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping({"/CbiWebController_flow.do"})
	public ModelAndView xzfyReqFlow(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_flow");
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		List<JSONObject> list = probaseinfoService.queryProbaseinfoListByParam(request.getParameter("caseid"),
				GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = probaseinfoService.queryNodeidByCaseid(request.getParameter("caseid"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: updateIframeInit
	 * @Description: 案件查询（外网）- 返回补正页面
	 * @author ybb
	 * @date 2016年9月12日 下午2:36:33
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/CbiWebController_updateIframeInit.do"})
	public ModelAndView updateIframeInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_updateInit");
		
		//案件ID
		mav.addObject("caseid", request.getParameter("caseid"));
		
		return mav;
	}
	
	/**
	 * @Title: xzfyUpdate
	 * @Description: 案件查询（外网）- 补正
	 * @author ybb
	 * @date 2016年9月12日 下午3:36:59
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/CbiWebController_update.do")
	@ResponseBody
	public ResultMsg xzfyUpdate(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String key = cbiWebService.updateXzfyReqByCaseid(param);
			msg = new ResultMsg(true, key);
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: cancelInit
	 * @Description: 案件查询（外网）- 返回撤销页面
	 * @author ybb
	 * @date 2016年9月12日 下午4:33:42
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/CbiWebController_cancelInit.do"})
	public ModelAndView cancelInit(HttpServletRequest request) throws ServletException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_cancel");
		
		return mav;
	}
	
	/**
	 * @Title: xzfyCancel
	 * @Description: 案件查询（外网）- 撤销
	 * @author ybb
	 * @date 2016年9月12日 下午4:50:51
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/CbiWebController_cancel.do")
	@ResponseBody
	public ResultMsg xzfyCancel(HttpServletRequest request) {
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			cbiWebService.addXzfyCancelReq(param);
			msg = new ResultMsg(true, "撤销成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: updateInit
	 * @Description: 案件查询（外网）- 返回补正页面
	 * @author ybb
	 * @date 2016年9月14日 上午10:27:28
	 * @param request
	 * @return
	 * @throws ServletException
	 * @throws AppException 
	 */
	@RequestMapping({"/CbiWebController_updateInit.do"})
	public ModelAndView updateInit(HttpServletRequest request) throws ServletException, AppException{ 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xzfy/xzfyreq_update");
		
		//菜单ID
		String caseid  = request.getParameter("caseid");
				
		//根据案件ID查询案件信息
		CasebaseinfoVo casebaseinfo = cbiWebService.queryXzfyreqByCaseid(caseid);
		mav.addObject("casebaseinfo", casebaseinfo);
				
		return mav;
	}
}

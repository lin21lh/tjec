package com.wfzcx.aros.xzys.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.jbf.base.filemanage.service.impl.SysFileManageServiceImpl;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzys.po.BRespbaseinfo;
import com.wfzcx.aros.xzys.po.Respreviewinfo;
import com.wfzcx.aros.xzys.po.Resptrialinfo;
import com.wfzcx.aros.xzys.service.BRespbaseinfoService;

/**
 * 
 * 
 * @author czp
 * @date 2016年8月13日 下午9:58:35
 */
@Scope("prototype")
@Controller
@RequestMapping("/aros/xzys/controller")
public class BRespbaseinfoController {
	@Autowired
	BRespbaseinfoService service;
	@Autowired
	SysFileManageServiceImpl sysfileservice;
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/xzys/brespbaseinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		String key = request.getParameter("key");
		mv.addObject("key", key);
		
		//判断是否是第一节点，判断grid页面按钮显示
		int flag = service.queryPronodebaseinfoByUserid();
		mv.addObject("flag", flag);
		
		return mv;
	}
	
	
	
	/**
	 * 进入行政应诉案件信息列表
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryList.do")
	@ResponseBody
	public EasyUITotalResult queryList(HttpServletRequest request) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = service.queryList(map);
		
		return EasyUITotalResult.from(ps);
	}

	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/add.do" })
	public ModelAndView add(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/xzys/brespbaseinfo_form");
		String nodeid = request.getParameter("nodeid");
		String id = request.getParameter("id");
		BRespbaseinfo bRespbaseinfo = null;
		if (StringUtil.isNotBlank(id)) {
			 bRespbaseinfo = service.queryCasebaseinfoByID(id);
		}
		if (null != bRespbaseinfo) {
			mv.addObject("bRespbaseinfo", bRespbaseinfo);
		} else {
			mv.addObject("bRespbaseinfo", "");
		}
		
		mv.addObject("nodeid", nodeid);
		mv.addObject("id", id);
		return mv;
	}

	/**
	 * @Title: relatednum
	 * @Description: 关联案号查询页面
	 * @author czp
	 * @date 2017年2月7日 下午2:28:52
	 * @param request
	 * @throws ServletException
	 */
	@RequestMapping({ "/relatednum.do" })
	public ModelAndView relatednum(HttpServletRequest request) throws ServletException{
		
		ModelAndView mav = new ModelAndView("aros/xzys/relatednum_select");
		
		return mav;
	}
	/**
	 * @Title: relatedListQuery
	 * @Description: 查询关联案号列表
	 * @author czp
	 * @date 2017年2月8日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/relatedListQuery.do")
	@ResponseBody
	public EasyUITotalResult relatedListQuery(HttpServletRequest request) throws AppException {
		
		//获取页面参数
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		PaginationSupport ps = service.relatedListQuery(map);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * @Title: save
	 * @Description: 行政应诉案件收案登记：保存
	 * @author czp
	 * @date 2017年3月23日
	 * @param id
	 * @return
	 */
	@RequestMapping("/save.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResultMsg save(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.save(param);
			if (!StringUtils.isBlank(id)) {
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("id", id);
				msg = new ResultMsg(true, "保存成功",body);
			} else {
				msg = new ResultMsg(false, "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	
	/**
	 * @Title: xzfyReqReceiveFlow
	 * @Description: 行政应诉案件收案登记：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/send.do")
	@ResponseBody
	public ResultMsg xzfyReqReceiveFlow(String id) {
		
		ResultMsg msg = null;

		try {
			service.xzfyReceiveFlow(id);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * 
	 * @param menuid
	 * @param speid
	 * @param request
	 * @return
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(String menuid, String id, HttpServletRequest request) {
		ResultMsg msg = null;
		try {
			if (id == null || "null".equals(id) || "".equals(id.trim())) {
				msg = new ResultMsg(false, AppException.getMessage("操作失败,前台传送id为null！"));
			} else {
				String msgString = service.delete(id);
				if ("".equals(msgString)) {
					msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
				} else {
					msg = new ResultMsg(false, AppException.getMessage("删除失败！" + msgString));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}

	
	
	/**
	 * @Title: xzysReturn
	 * @Description: 应诉案件回退：回退受理信息
	 * @author czp
	 * @date 2016年11月14日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzysReturn.do")
	@ResponseBody
	public ResultMsg xzysReturn(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.updateXzysReturnByLawid(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping({"/backFlowPage.do"})
	public ModelAndView backFlowPage(HttpServletRequest request){
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/backFlowPage");
		
		return mav;
	}
	
	
	
	/**
	 * @Title: xzysReview
	 * @Description: 行政应诉案件审查：返回案件审查页面
	 * @author czp
	 * @date 2017年3月23日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzysReview.do"})
	public ModelAndView xzysReview(HttpServletRequest request) { 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/casereview_form");
		String lawid = request.getParameter("id");
		String nodeid = request.getParameter("nodeid");
		Respreviewinfo respreviewinfo = null;
		if (StringUtil.isNotBlank(lawid)) {
			respreviewinfo = service.queryCaseReviewinfoById(lawid);
		}
		if (null != respreviewinfo) {
			mav.addObject("respreviewinfo", respreviewinfo);
		} else {
			mav.addObject("respreviewinfo", "");
		}
		mav.addObject("nodeid", nodeid);
		mav.addObject("lawid", lawid);
		
		return mav;
	}
	
	/**
	 * @Title: queryFileList
	 * @Description: 附件列表
	 * @author ztt
	 * @date 2016年11月8日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryFileList.do")
	@ResponseBody
	public EasyUITotalResult queryFileList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = service.queryFileList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	
	/**
	 * @Title: xzysExamSave
	 * @Description: 行政应诉案件审查：保存
	 * @author czp
	 * @date 2017年3月23日
	 * @param id
	 * @return
	 */
	@RequestMapping("/xzysExamSave.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResultMsg xzysExamSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.xzysExamSave(param);
			if (!StringUtils.isBlank(id)) {
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("id", id);
				msg = new ResultMsg(true, "保存成功", body);
			} else {
				msg = new ResultMsg(false, "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}

	/**
	 * @Title: xzysExamSend
	 * @Description: 行政应诉案件审查：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzysExamSend.do")
	@ResponseBody
	public ResultMsg xzysExamSend(String id) {
		
		ResultMsg msg = null;

		try {
			service.xzysExamSend(id);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzysAppeart
	 * @Description: 行政应诉出庭应诉：返回出庭应诉页面
	 * @author czp
	 * @date 2017年3月23日
	 * @param request
	 * @return
	 */
	@RequestMapping({"/xzysAppeart.do"})
	public ModelAndView xzysAppeart(HttpServletRequest request) { 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/appearcourt_form");
		String lawid = request.getParameter("id");
		String nodeid = request.getParameter("nodeid");
		Resptrialinfo resptrialinfo = null;
		if (StringUtil.isNotBlank(lawid)) {
			resptrialinfo = service.queryCaseAppeartinfoById(lawid);
		}
		if (null != resptrialinfo) {
			mav.addObject("resptrialinfo", resptrialinfo);
		} else {
			mav.addObject("resptrialinfo", "");
		}
		mav.addObject("nodeid", nodeid);
		mav.addObject("lawid", lawid);
		
		return mav;
	}
	
	/**
	 * @Title: xzysAppeartSave
	 * @Description: 行政应诉案件出庭：保存
	 * @author czp
	 * @date 2017年3月23日
	 * @param id
	 * @return
	 */
	@RequestMapping("/xzysAppeartSave.do")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public ResultMsg xzysAppeartSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = service.xzysAppeartSave(param);
			if (!StringUtils.isBlank(id)) {
				HashMap<String, Object> body = new HashMap<String, Object>();
				body.put("id", id);
				msg = new ResultMsg(true, "保存成功", body);
			} else {
				msg = new ResultMsg(false, "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: xzysAppeartSend
	 * @Description: 行政应诉案件出庭：发送
	 * @author czp
	 * @date 2017年3月24日
	 * @param caseid
	 * @return
	 */
	@RequestMapping("/xzysAppeartSend.do")
	@ResponseBody
	public ResultMsg xzysAppeartSend(String id) {
		
		ResultMsg msg = null;

		try {
			service.xzysAppeartSend(id);
			msg = new ResultMsg(true, AppException.getMessage("发送成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("发送失败：") + e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * 立案归档
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/xzysFiling.do" })
	public ModelAndView xzysFiling(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("aros/xzys/casefiling_init");
		// 案件ID
		String id  = request.getParameter("id");
		mav.addObject("caseid", id);
		// 流程类型
		String protype  = request.getParameter("protype");
		mav.addObject("protype", protype);
		// 流程节点ID
		String nodeid  = request.getParameter("nodeid");
		mav.addObject("nodeid", nodeid);
		return mav;
	}
	
	/**
	 * @Title: queryNoticeList
	 * @Description: 行政复议文书制作：查询已有文书列表
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNoticeList.do")
	@ResponseBody
	public EasyUITotalResult queryNoticeList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = service.queryNoticeList(param);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * @Title: queryNoticeTmpList
	 * @Description: 行政复议文书制作：查询文书模板列表
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException	
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNoticeTmpList.do")
	@ResponseBody
	public EasyUITotalResult queryNoticeTmpList(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		PaginationSupport ps = service.queryNoticeTmpList(param);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * @Title: noticeInfoAdd
	 * @Description: 行政复议文书制作：新增
	 * @author ztt
	 * @date 2016年11月15日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@RequestMapping("/noticeInfoAdd.do")
	public ModelAndView noticeInfoAdd(HttpServletRequest request) throws AppException {
		//返回页面路径
		//获取页面传递查询参数
		ModelAndView mav = new ModelAndView("aros/xzfy/textproduction_add");
		return mav;
	}
	
	/**
	 * @Title: getClobContentVal
	 * @Description: 行政复议文书制作：获取文书、文书送达内容
	 * @author ztt
	 * @date 2016年11月17日
	 * @return
	 */
	@RequestMapping("getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String tempid, String id, String tableFlag)
	{  
		String contentStr = service.getClobContentVal(tempid, id, tableFlag);
		return contentStr;
	}
	/**
	 * @Title: noticeDownload
	 * @Description: 行政复议文书制作：已有文书下载
	 * @author ztt
	 * @date 2016年11月21日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeDownload.do")
	@ResponseBody
	public void noticeDownload(HttpServletRequest request, HttpServletResponse response) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String, Object> rMap = service.noticeDownload(param);
		File tmpFile= (File)rMap.get("file");
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts= null;
		try {
			ins = new FileInputStream(tmpFile);//构造一个读取文件的IO流对象
			String filename = (String)rMap.get("fileName") + ".doc";
            bins=new BufferedInputStream(ins);//放到缓冲流里面
            response.setContentType("application/octet-stream");
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
         	 filename = java.net.URLEncoder.encode(filename, "UTF-8");    
                 filename = StringUtils.replace(filename, "+", "%20");//替换空格    
            } else {  
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");  
            } 
            
            response.setHeader("Content-Disposition", "attachment; filename=" +filename);
            outs=response.getOutputStream();//获取文件输出IO流
            bouts=new BufferedOutputStream(outs);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {
                bouts.write(buffer, 0, bytesRead);
            }
            bouts.flush();//这里一定要调用flush()方法
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(ins!=null){
						ins.close();
				}
				if(bins!=null){
					bins.close();
				}
				if(outs!=null){
					outs.close();
				}
				if(bouts!=null){
					
					bouts.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			tmpFile.delete();
		}
	}
	
	/**
	 * @Title: noticeInfoSave
	 * @Description: 行政复议文书制作：保存
	 * @author ztt
	 * @date 2016年11月17日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeInfoSave.do")
	@ResponseBody
	public ResultMsg noticeInfoSave(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.updateNoticeInfo(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: noticeInfoDelete
	 * @Description: 行政复议文书制作：删除
	 * @author ztt
	 * @date 2016年11月17日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeInfoDelete.do")
	@ResponseBody
	public ResultMsg noticeInfoDelete(HttpServletRequest request){
		
		ResultMsg msg = null;
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.deleteNoticeInfoByCaseid(param);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	/**
	 * @Title: xzfyReturn
	 * @Description: 复议回退：回退受理信息
	 * @author ztt
	 * @date 2016年11月14日
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/xzfyReturn.do")
	@ResponseBody
	public ResultMsg xzfyReturn(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			service.updateXzysReturnByLawid(param);
			msg = new ResultMsg(true, "回退成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Description: 结案归档
	 * @param id
	 * @return
	 */
	@RequestMapping("/xzfyCaseEndNoticePlaceFlow.do")
	@ResponseBody
	public ResultMsg xzfyCaseEndNoticePlaceFlow(String id) {
		ResultMsg msg = null;
		try {
			service.xzfyCaseEndNoticePlaceFlow(id);
			msg = new ResultMsg(true, AppException.getMessage("结案归档成功"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("结案归档失败：") + e.getMessage());
		}
		return msg;
	}
	
	
	/**
	 * @Title: deleteFileInfo
	 * @Description: 行政应诉审查：删除附件信息
	 * @author czp
	 * @date 2017年3月28日 下午4:00:35
	 * @param request
	 * @return
	 * @throws ServletException   SysFileManage
	 */
	@RequestMapping({"/deleteFileInfo.do"})
	@ResponseBody
	public ResultMsg deleteFileInfo(HttpServletRequest request) throws ServletException{ 
		ResultMsg msg = null;
		String id  = request.getParameter("id");
		
		try {
			sysfileservice.delete(id);
			msg = new ResultMsg(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		
		return msg;
	}
	
	
	
	/**
	 * @Title: queryNodeidsByCase
	 * @Description: 根据案件 caseID ,protype获取 经过流程节点nodeid的 list
	 * 
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryNodeidsByCase.do")
	@ResponseBody
	public List  queryNodeidsByCase(HttpServletRequest request) throws AppException {
		
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		
		//执行查询
		List list = service.queryNodeidsByCase(param);
		
		return list;
	}
	
	
	/**
	 * @Title: xzysReceiveDetail
	 * @Description: 行政应诉案件流程：详情查看_案件登记
	 * @author czp
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzysReceiveDetail.do"})
	public ModelAndView xzfyReqDetail(HttpServletRequest request) throws AppException { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/receregistration_detail");
		// 案件ID
		String id = request.getParameter("id");
		BRespbaseinfo brespbaseinfo = service.queryCasebaseinfoByID(id);
		mav.addObject("brespbaseinfo", brespbaseinfo);
		
		return mav;
	}
	
	
	/**
	 * @Title: xzfyCaseReview
	 * @Description: 行政应诉案件流程：详情查看_案件审查
	 * @author czp
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzfyCaseReview.do"})
	public ModelAndView xzfyCaseReview(HttpServletRequest request) throws AppException { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/xzfycasereview_detail");
		// 案件ID
		String id = request.getParameter("id");
		String nodeid = request.getParameter("nodeid");
		Respreviewinfo respreviewinfo = service.queryCaseReviewinfoById(id);
		
		mav.addObject("respreviewinfo", respreviewinfo);
		mav.addObject("id", id);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	/**
	 * @Title: xzysCtysDetail
	 * @Description: 行政应诉案件流程：详情查看_出庭应诉
	 * @author czp
	 * @date 2016年11月2日
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/xzysCtysDetail.do"})
	public ModelAndView xzysCtysDetail(HttpServletRequest request) throws AppException { 
		
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/xzys/xzfyappearview_detail");
		// 案件ID
		String id = request.getParameter("id");
		String nodeid = request.getParameter("nodeid");
		Resptrialinfo resptrialinfo = service.queryCaseAppeartinfoById(id);
		
		mav.addObject("resptrialinfo", resptrialinfo);
		mav.addObject("id", id);
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	
	
	
	
	
	/**
	 * @Title: queryFlowForReq
	 * @Description:行政应诉：查看流程信息
	 * @author ybb
	 * @date 2016年8月15日 下午3:14:31
	 * @param request
	 * @return
	 * @throws AppException 
	 */
	@RequestMapping({"/queryFlowForReq.do"})
	public ModelAndView queryFlowForReq(HttpServletRequest request) throws AppException {
		
		//返回页面路径 
		ModelAndView mav = new ModelAndView("aros/flow/flow_message");
		//案件ID
		mav.addObject("id", request.getParameter("id"));
		
		List<JSONObject> list = service.queryProbaseinfoListByParam(request.getParameter("id"),
				GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		mav.addObject("wfList", list);
		
		//查询流程节点
		BigDecimal nodeid = service.queryNodeidById(request.getParameter("id"));
		mav.addObject("nodeid", nodeid);
		
		return mav;
	}
	
	
}

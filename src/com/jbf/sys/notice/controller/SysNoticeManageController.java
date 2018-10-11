package com.jbf.sys.notice.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.notice.po.SysNotice;
import com.jbf.sys.notice.service.ISysNoticeManageService;
import com.jbf.sys.user.po.SysUser;

/**
 * 留言公告管理Controller
 * @ClassName: AccountRegisterController 
 * @Description: (留言公告管理处理类) 
 * @author songxiaojie
 * @date 2015年5月8日 
 */
@Controller
@RequestMapping({ "/sys/notice/SysNoticeManageController" })
public class SysNoticeManageController {
	
	@Autowired
	ISysNoticeManageService noticeMangeService;
	
	/**
	 * 公告留言管理列表页入口
	 * @Title: manageEntry 
	 * @Description:  公告留言管理列表页入口
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws ServletException
	 */
	@RequestMapping("manageEntry.do")
	public ModelAndView manageEntry(String status,String messageType) throws ServletException {
		ModelAndView mav = new ModelAndView("sys/notice/noticeMaginit");
		mav.addObject("messageType",messageType);
		return mav;
	}

	/**
	 * 根据类型查询公告留言列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/qrySendAllNotice.do")
	public EasyUITotalResult qrySendAllNotice(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qrySendAllNotice(map);
		
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 添加留言公告入口
	 * @param 留言公告类型：1公告 2留言
	 * @return
	 */
	@RequestMapping("addNoticeMsgInit.do")
	public ModelAndView addNoticeMsgInit(HttpServletRequest request)
	{
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("sys/notice/noticeMsgForm");
		mav.addObject("messageType",request.getParameter("messageType"));
		mav.addObject("sessionId", session);
		return mav;
	}
	
	/**
	 * 查询clob字段内容
	 * @param messageType
	 * @return
	 */
	@RequestMapping("getClobContentVal.do")
	@ResponseBody
	public String getClobContentVal(String noticeid)
	{  
		String contentStr = noticeMangeService.getClobContentVal(noticeid);
		return contentStr;
	}
	
	
	/**
	 * 添加留言公告
	 * @param messageType
	 * @return
	 */
	@RequestMapping("addNoticeMsg.do")
	@ResponseBody
	public ResultMsg addNoticeMsg(SysNotice sysNotice,String status,String messageType,
			String priorLevel,String itemids,String opertype)
	{
		ResultMsg msg = null;
		try {
			
			//如果添加留言设置为空，如果是公告则设置选择的级别值
			if("undefined".equals(priorLevel)|| "null".equals(priorLevel))
			{
				sysNotice.setPriorlevel("");
			}
			else
			{
				sysNotice.setPriorlevel(priorLevel);
			}
			
			
			//设置留言公告类型
			sysNotice.setMessagetype(messageType);
			sysNotice.setStatus(status);
			//新增留言公告信息
			noticeMangeService.addNoticeMsg(sysNotice,itemids);
			try{
				//发送信息
				noticeMangeService.sendMessage(sysNotice.getNoticeid().toString(),opertype,sysNotice.getMessageflag(),messageType);
			}
			catch(Exception e)
			{
				System.err.println("添加留言发送短信出现异常，异常信息为："+e.toString());
			}
			
			if("save".equals(opertype))
			{
				msg = new ResultMsg(true, AppException.getMessage("保存成功！"));
			}
			else
			{
				msg = new ResultMsg(true, AppException.getMessage("发布成功！"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("save".equals(opertype))
			{
				msg = new ResultMsg(true, AppException.getMessage("保存失败！"));
			}
			else
			{
				msg = new ResultMsg(true, AppException.getMessage("发布失败！"));
			}
		}
		return msg;
	}
	
	/**
	 * 公告留言详情入口
	 * @param request
	 * @return
	 */
	@RequestMapping("/showDetailMsgInit.do")
	public ModelAndView showDetailMsgInit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/sys/notice/noticeMsgForm");
		mav.addObject("messageType", request.getParameter("messageType"));
		return mav;
	}
	
	/**
	 * 公告留言详情展示
	 * @return
	 */
	@RequestMapping("/showDetailMsg.do")
	public ModelAndView showDetailMsg(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("/sys/notice/noticeDetailForm");
		mav.addObject("messageType", request.getParameter("messageType"));
		mav.addObject("operType", request.getParameter("operType"));
		String noticeId = request.getParameter("noticeId");
		//判断是否上传附件
		int getCount = noticeMangeService.selectFileCount(noticeId);
		mav.addObject("fileCount", getCount);
		
		//判断发送留言时，此留言我是否是管理者
		int showPersonFlag = noticeMangeService.getMsgCountByMyself(noticeId);
		mav.addObject("showPersonFlag", showPersonFlag);
		return mav;
		
	}
	
	/**
	 * 查询基本信息
	 * @return
	 */
	@RequestMapping("/showBaseDetail.do")
	public ModelAndView showBaseDetail(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("/sys/notice/showNoticeHis");
		mav.addObject("messageType", request.getParameter("messageType"));
		String noticeId = request.getParameter("noticeId");
		//判断是否上传附件
		int getCount = noticeMangeService.selectFileCount(noticeId);
		mav.addObject("fileCount", getCount);
		mav.addObject("flag", request.getParameter("flag"));
		
		//判断发送留言是够是自己是管理者
		int showPersonFlag = noticeMangeService.getMsgCountByMyself(noticeId);
		mav.addObject("showPersonFlag", showPersonFlag);
		
		//更新评论表的阅读标示为已阅读
		noticeMangeService.updateReadFlagByNoticeid(noticeId);
		
		return mav;
	}
	
	
	/**
	 * 发布留言公告
	 * @return
	 */
	@RequestMapping("sendNoticeMsg.do")
	@ResponseBody
	public ResultMsg sendNoticeMsg(HttpServletRequest request)
	{
		ResultMsg msg = null;
		try {
			//获取发送的留言公告编码noticeIds
			String noticeIds = request.getParameter("noticeIds");
			noticeMangeService.sendNoticeMsgById(noticeIds);
			
			//发送短信标示
			String msgFlags = request.getParameter("messageFlags");
			
			//公告留言类型
			String messageType = request.getParameter("messageType");
			try{
				//发送短信
				noticeMangeService.sendMessage(noticeIds,"redo",msgFlags,messageType);
				
			}
			catch(Exception e)
			{
				System.err.println("发布留言时，短信发送出现异常，异常信息为:"+e.toString());
			}
			
			
			msg = new ResultMsg(true, "发布成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "发布失败！");
		}
		return msg;
	}

	/**
	 * 删除留言公告
	 * @return
	 */
	@RequestMapping("delNoticeMsg.do")
	@ResponseBody
	public ResultMsg delNoticeMsg(HttpServletRequest request)
	{
		ResultMsg msg = null;
		try {
			//获取删除的留言公告编码noticeIds
			String delIds = request.getParameter("delIds");
			noticeMangeService.delNoticeMsgById(delIds);
			msg = new ResultMsg(true, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "删除失败！");
		}
		return msg;
	}
	
	
	/**
	 * 修改公告留言信息
	 * @param sysnotice
	 * @param noticeReceive
	 * @return
	 */
	@RequestMapping("/aditNoticeMsg.do")
	@ResponseBody
	public ResultMsg aditNoticeMsg(SysNotice sysnotice,String status,String priorLevel,String itemids,String opertype,String messageType)
	{
		ResultMsg msg = null;
		try {
			//如果修改留言设置重要级别为”“
			if("null".equals(priorLevel))
			{
				sysnotice.setPriorlevel("");
			}
			else
			{
				sysnotice.setPriorlevel(priorLevel);
			}
			
			//修改逻辑
			noticeMangeService.editNoticeMsg(sysnotice, status, itemids);
			
			try
			{
				//发送短信
				noticeMangeService.sendMessage(sysnotice.getNoticeid().toString(),opertype,sysnotice.getMessageflag(),messageType);
			}
			catch(Exception e)
			{
				System.err.println("发布留言时，短信发送出现异常，异常信息为:"+e.toString());
			}
			
			if("save".equals(opertype))
			{
				msg = new ResultMsg(true, AppException.getMessage("保存成功！"));
			}
			else
			{
				msg = new ResultMsg(true, AppException.getMessage("发布成功！"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			if("save".equals(opertype))
			{
				msg = new ResultMsg(true, AppException.getMessage("保存失败！"));
			}
			else
			{
				msg = new ResultMsg(true, AppException.getMessage("发布失败！"));
			}
		}
		return msg;
	}
	
	
	/**
	 * 留言公告撤回处理
	 * @return
	 */
	@RequestMapping("/backNoticeMsg.do")
	@ResponseBody
	public ResultMsg backNoticeMsg(HttpServletRequest request)
	{
		ResultMsg msg = null;
		try {
			//撤回公告留言编码
			String backIds = request.getParameter("backIds");
			//处理撤回留言公告
			String returnMsg = noticeMangeService.dealBackNoticeMsg(backIds);
			
			msg = new ResultMsg(true, returnMsg);
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, "撤回失败！");
		}
		return msg;
	}

	
	/**
	 * 留言阅读入口
	 * @param request
	 * @return
	 */
	@RequestMapping("/readMsgEntry.do")
	public ModelAndView readMsgEntry(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("sys/notice/readNoticeInit");
		mav.addObject("messageType",request.getParameter("messageType"));
		return mav; 
	}
	
	/**
	 * 查询留言阅读列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qryReadAllMessage.do")
	@ResponseBody
	public EasyUITotalResult qryReadAllMessage(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qryReadAllMessage(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 首页查询留言列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qryReadAllMessageIndex.do")
	@ResponseBody
	public EasyUITotalResult qryReadAllMessageIndex(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qryReadAllMessageIndex(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询公告阅读列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qryReadAllNotice.do")
	@ResponseBody
	public EasyUITotalResult qryReadAllNotice(HttpServletRequest request) throws AppException{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String realPath = request.getContextPath();
		map.put("path", realPath);
		PaginationSupport ps = noticeMangeService.qryReadAllNotice(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 首页查询公告阅读列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qryReadAllNoticeIndex.do")
	@ResponseBody
	public EasyUITotalResult qryReadAllNoticeIndex(HttpServletRequest request) throws AppException{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String realPath = request.getContextPath();
		map.put("path", realPath);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qryReadAllNoticeIndex(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 首页查询留言和公告阅读列表
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/qryReadAllMessageAndNoticeIndex.do")
	@ResponseBody
	public EasyUITotalResult qryReadAllMessageAndNoticeIndex(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String realPath = request.getContextPath();
		map.put("path", realPath);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qryReadAllMessageAndNoticeIndex(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 反馈页面初始化
	 * @return
	 */
	@RequestMapping("/feedbackNoticeInit.do")
	public ModelAndView feedbackNoticeInit(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("sys/notice/readNoticeForm");
		mav.addObject("messageType",request.getParameter("messageType"));
		//编码
		String noticeId = request.getParameter("noticeId");
		//判断是够上传附件
		int getCount = noticeMangeService.selectFileCount(noticeId);
		mav.addObject("fileCount", getCount);
		//只要打开页面，就是阅读了此公告留言，需要记录阅读记录
		//阅读标示
		String readflag = request.getParameter("readflag");
		noticeMangeService.dealReceiveNotice(readflag, noticeId);
		
		return mav; 
	}
	
	
	/**
	 * 对公告留言进行反馈
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/feedbackNotice.do")
	@ResponseBody
	public ResultMsg feedbackNotice(HttpServletRequest request)
	{
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			//公告留言评论处理
			noticeMangeService.dealReadNotice(map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	/**
	 * 公告留言管理列表页入口
	 * @Title: manageEntry 
	 * @Description:  公告留言管理列表页入口
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws ServletException
	 */
	@RequestMapping("/showCommonListInit.do")
	public ModelAndView showCommonListInit(String messageType,String noticeId) throws ServletException {
		ModelAndView mav = new ModelAndView("sys/notice/readCommonList");
		mav.addObject("messageType",messageType);
		return mav;
	}
	
	
	/**
	 * 查询留言评论列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showCommonList.do")
	@ResponseBody
	public EasyUITotalResult showCommonList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.qryCommonList(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询自己评论别人的公告列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showCommonBySelf.do")
	@ResponseBody
	public EasyUITotalResult showCommonBySelf(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		PaginationSupport ps = noticeMangeService.showCommonBySelf(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询公告评论列表
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/noticeCommonList.do")
	@ResponseBody
	public EasyUITotalResult noticeCommonList(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = noticeMangeService.qryNoticeCommonList(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 查询公告留言阅读历史
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/showHisList.do")
	@ResponseBody
	public EasyUITotalResult showHisList(HttpServletRequest request) throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = noticeMangeService.qryhisNoticeList(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 展示接收人页面
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/showPersonList.do")
	public ModelAndView showPersonList(String noticeid) throws ServletException
	{
		ModelAndView mav = new ModelAndView("sys/notice/choiseReceivePerson");
		return mav;
	}
	
	/**
	 * 查询未选中所有接收人
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryUnselectedUser.do")
	@ResponseBody
	public List queryUnselectedUser(HttpServletRequest request) throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		
		return noticeMangeService.queryUnselectedUser(map);
	}
	
	
	/**
	 * 查询已选中的接收人
	 * @Title: queryChangeAccount 
	 * @Description:  ServletException
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/querySelectedUser.do")
	@ResponseBody
	public List querySelectedUser(HttpServletRequest request)throws AppException 
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return noticeMangeService.querySelectedUser(map);
	}

	/**
	 * 展示反馈信息页面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/showCommonDialog.do")
	public ModelAndView showCommonDialog(HttpServletRequest request)throws ServletException
	{
		ModelAndView mav = new ModelAndView("sys/notice/commonContextForm");
		return mav;
	}
	
	/**
	 * 获取发布者没有查看反馈列表
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getCommonUnreadList.do")
	@ResponseBody
	public String getCommonUnreadList(HttpServletRequest request)throws AppException
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		map.put("usercode", user.getUsercode());
		return noticeMangeService.getCommonUnreadList(map);
	}
	
	/**
	 * 获取公告留言的详情
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/getNoticeInfo.do")
	@ResponseBody
	public Map getNoticeInfo(HttpServletRequest request)throws AppException
	{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		return noticeMangeService.getNoticeInfo(map);
	}
}

package com.jbf.sys.regist.controller;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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
import com.jbf.sys.regist.service.SocialRegistAuditServiceI;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.user.service.SysUserService;
/**
 * 社会资本注册审批
 * @ClassName: SocialRegistAuditController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-10-12 下午03:56:33
 */
@Scope("prototype")
@Controller
@RequestMapping("/regist/controller/SocialRegistAuditController")
public class SocialRegistAuditController {
	
	@Autowired
	SocialRegistAuditServiceI SocialRegistAuditService;
	@Autowired
    SysUserService userService;
	
	/**
	 * 
	 * @Title: init 
	 * @Description: TODO(初始页面) 
	 * @param @return String 跳转页面
	 * @param @throws ServletException 设定文件 
	 * @return String 返回类型 
	 */
	@RequestMapping({ "/init.do" })
	public ModelAndView init(String menuid) throws ServletException {
		ModelAndView mav = new ModelAndView("sys/regist/socaud_init");
		mav.addObject("menuid",menuid);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({ "/qrySocAud.do" })
	public EasyUITotalResult qrySocAud(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = SocialRegistAuditService.qrySocAud(map);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 *页面跳转
	 * @Title: optSocAudView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/optSocAudView.do"})
	public ModelAndView optSocAudView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("sys/regist/socaud_form");
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("sys/regist/socaud_view");
			}else {
				throw new Exception("没找到视图！");
			}
		}
		return mav;
	}
	
	 /**
     * 同意
     * @Title: approveSocAud 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param request
     * @param @return 设定文件 
     * @return ResultMsg 返回类型 
     * @throws
     */
    @RequestMapping({"/approveSocAud.do"})
    @ResponseBody
    public ResultMsg approveSocAud(HttpServletRequest request){
    	ResultMsg res = null;
    	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
    	try {
    		SocialRegistAuditService.approveSocAud(map);
			res = new ResultMsg(true, "操作成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = new ResultMsg(false, "操作失败！");
		}
    	return res;
    }
    /**
     * 拒绝
     * @Title: refuseSocAud 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param request
     * @param @return 设定文件 
     * @return ResultMsg 返回类型 
     * @throws
     */
    @RequestMapping({"/refuseSocAud.do"})
    @ResponseBody
    public ResultMsg refuseSocAud(HttpServletRequest request){
    	ResultMsg res = null;
    	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
    	try {
    		SocialRegistAuditService.refuseSocAud(map);
			res = new ResultMsg(true, "操作成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = new ResultMsg(false, "操作失败！");
		}
    	return res;
    }
    
    /**
     * 作废
     * @Title: delSocAud 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param request
     * @param @return 设定文件 
     * @return ResultMsg 返回类型 
     * @throws
     */
    @RequestMapping({"/delSocAud.do"})
    @ResponseBody
    public ResultMsg delSocAud(HttpServletRequest request){
    	ResultMsg res = null;
    	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
    	try {
    		SocialRegistAuditService.delSocAud(map);
			res = new ResultMsg(true, "操作成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = new ResultMsg(false, "操作失败！");
		}
    	return res;
    }
}

package com.wfzcx.aros.lxfs.controller;

import java.util.Map;

import javax.servlet.ServletException;
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
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.lxfs.service.LxfsService;

@Scope("prototype")
@Controller
@RequestMapping("/aros/lxfs/controller/LxfsController")
public class LxfsController {
	@Autowired
	LxfsService lxfsService;
	/**
	 * 进入联系方式list主页面。
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/LxfsController_init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/lxfs/lxfsinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	/**
	 * @Title: lxfsqueryList
	 * @Description: 行政复议联系方式：查询典型案例（grid列表页面查询）
	 * @author czp
	 * @date 2016年8月12日 上午11:34:09
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/lxfsqueryList.do")
	@ResponseBody
	public EasyUITotalResult lxfsqueryList(HttpServletRequest request) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = lxfsService.queryLxfsList(param);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * @Title: lxfsAdd
	 * @Description: 行政复议典型案例：返回典型案例新增页面
	 * @author czp
	 * @date 2016年8月12日 下午2:25:29
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/lxfsAdd.do"})
	public ModelAndView lxfsAdd(HttpServletRequest request) throws ServletException{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/lxfs/lxfsinfo_form");
		return mav;
	}
	/**
	 * @Title: lxfsSave
	 * @Description: 行政复议申请：保存典型案例
	 * @author czp
	 * @date 2016年8月12日 下午3:51:15
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/lxfsSave.do")
	@ResponseBody
	public ResultMsg lxfsSave(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = lxfsService.save(param);
			if (!StringUtils.isBlank(id)) {
				msg = new ResultMsg(true, "保存成功");
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
	 * @Title: lxfsEdit
	 * @Description: 行政复议申请：联系方式修改页面
	 * @author czp
	 * @date 2016年8月12日 下午3:52:37
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/lxfsEdit.do"})
	public ModelAndView lxfsEdit(HttpServletRequest request) throws ServletException{
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/lxfs/lxfsinfo_form");
		return mav;
	}
	/**
	 * @Title: lxfsReqEdit
	 * @Description: 行政复议：修改联系方式
	 * @author czp
	 * @date 2016年8月12日 下午3:54:35
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/lxfsReqEdit.do")
	@ResponseBody  
	public ResultMsg dxalReqEdit(HttpServletRequest request) {
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			lxfsService.updateLxfsById(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
		
	}
	/**
	 * @Title: lxfsDelete
	 * @Description: 行政复议申请：删除联系方式
	 * @author czp
	 * @date 2016年8月12日 下午3:57:47
	 * @param id
	 * @return
	 */
	@RequestMapping("/lxfsDelete.do")
	@ResponseBody
	public ResultMsg dxalDelete(String id) {
		ResultMsg msg = null;
		try {
			lxfsService.deleteLxfsById(id);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
		
	}
	/**
	 * @Title: lxfsDetail
	 * @Description: 行政复议申请：联系方式详情页面
	 * @author czp
	 * @date 2016年8月12日 下午3:59:50
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/lxfsDetail.do"})
	public ModelAndView lxfsDetail(HttpServletRequest request) throws ServletException{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/lxfs/lxfsinfo_detail_form");
		return mav;	
	}
	
}

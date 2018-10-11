package com.wfzcx.aros.dxal.controller;

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
import com.wfzcx.aros.dxal.service.DxalService;


@Scope("prototype")
@Controller
@RequestMapping("/aros/dxal/controller/DxalController")
public class DxalController {
	@Autowired
	DxalService dxalservice;
	/**
	 * 
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({ "/DxalController_init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mv = new ModelAndView("aros/dxal/dxalinfo_init");
		String menuid = request.getParameter("menuid");
		mv.addObject("menuid", menuid);
		return mv;
	}
	/**
	 * @Title: dxalqueryList
	 * @Description: 行政复议典型案例：查询典型案例（grid列表页面查询）
	 * @author czp
	 * @date 2016年8月12日 上午11:34:09
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/dxalqueryList.do")
	@ResponseBody
	public EasyUITotalResult dxalqueryList(HttpServletRequest request) throws AppException {
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = dxalservice.queryDxalList(param);
		
		return EasyUITotalResult.from(ps);
	}
	/**
	 * @Title: dxalAdd
	 * @Description: 行政复议典型案例：返回典型案例新增页面
	 * @author czp
	 * @date 2016年8月12日 下午2:25:29
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/dxalAdd.do"})
    public ModelAndView dxalAdd(HttpServletRequest request) throws ServletException{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/dxal/dxalinfo_form");
		return mav;
	}
	/**
	 * @Title: dxalSave
	 * @Description: 行政复议申请：保存典型案例
	 * @author czp
	 * @date 2016年8月12日 下午3:51:15
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/dxalSave.do")
	@ResponseBody
	public ResultMsg dxalSave(HttpServletRequest request) {
		ResultMsg msg = null;

		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			String id = dxalservice.save(param);
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
	 * @Title: dxalEdit
	 * @Description: 行政复议申请：返回典型案例修改页面
	 * @author ybb
	 * @date 2016年8月12日 下午3:52:37
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/dxalEdit.do"})
	public ModelAndView xzfyEdit(HttpServletRequest request) throws ServletException{
		//返回页面路径
				ModelAndView mav = new ModelAndView("aros/dxal/dxalinfo_form");
				return mav;
	}
	/**
	 * @Title: dxalReqEdit
	 * @Description: 行政复议申请：修改典型案例
	 * @author czp
	 * @date 2016年8月12日 下午3:54:35
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/dxalReqEdit.do")
	@ResponseBody  
	public ResultMsg dxalReqEdit(HttpServletRequest request) {
		
		ResultMsg msg = null;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		try {
			dxalservice.updateDxalById(param);
			msg = new ResultMsg(true, "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}
		return msg;
	}
	
	/**
	 * @Title: dxalDelete
	 * @Description: 行政复议申请：删除行政复议申请
	 * @author czp
	 * @date 2016年8月12日 下午3:57:47
	 * @param id
	 * @return
	 */
	@RequestMapping("/dxalDelete.do")
	@ResponseBody
	public ResultMsg dxalDelete(String id) {
		ResultMsg msg = null;
		try {
			dxalservice.deleteDxalById(id);
			msg = new ResultMsg(true, AppException.getMessage("删除成功"));
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败"));
		}
		return msg;
		
	}
	/**
	 * @Title: dxalDetail
	 * @Description: 行政复议申请：典型案例详情页面
	 * @author czp
	 * @date 2016年8月12日 下午3:59:50
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping({"/dxalDetail.do"})
	public ModelAndView dxalDetail(HttpServletRequest request) throws ServletException{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/dxal/dxalinfo_detail_form");
		return mav;
	}
}

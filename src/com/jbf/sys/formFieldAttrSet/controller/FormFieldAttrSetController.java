package com.jbf.sys.formFieldAttrSet.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.formFieldAttrSet.po.SysFormFieldAttrSet;
import com.jbf.sys.formFieldAttrSet.service.FormFieldAttrSetService;

/**
 * 
 * @ClassName: FormFieldAttrSetController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月29日 下午4:06:40
 */
@Scope("prototype")
@Controller
@RequestMapping({"/sys/formFieldAttrSet/FormFieldAttrSetController"})
public class FormFieldAttrSetController {

	@Autowired
	FormFieldAttrSetService formFieldAttrSetService;
	/**
	 * 
	 * @Title: entry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/entry.do")
	public ModelAndView entry() {
		String viewname = "/sys/formFieldAttrSet/formFieldAttrSet";
		Map<String, Object> modelMap = new HashMap<String, Object>();
		return new ModelAndView(viewname, "modelMap", modelMap);
	}
	
	/**
	 * 
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/add.do")
	@ResponseBody
	public ResultMsg add(SysFormFieldAttrSet formFieldAttrSet) {
		ResultMsg resultMsg = null;
		try {
			formFieldAttrSetService.add(formFieldAttrSet);
			resultMsg = new ResultMsg(true, "新增成功");
		} catch (Exception e) {
			// TODO: handle exception
			resultMsg = new ResultMsg(false, "新增失败，失败原因：" + e.getMessage());
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultMsg edit(SysFormFieldAttrSet formFieldAttrSet) {
		ResultMsg resultMsg = null;
		try {
			formFieldAttrSetService.edit(formFieldAttrSet);
			resultMsg = new ResultMsg(true, "修改成功");
		} catch (Exception e) {
			// TODO: handle exception
			resultMsg = new ResultMsg(false, "修改失败，失败原因：" + e.getMessage());
		}
		return resultMsg;
	}
	
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ResultMsg 返回类型 
	 * @throws
	 */
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultMsg delete(HttpServletRequest request) {
		ResultMsg resultMsg = null;
		
		return resultMsg;
	}
	
	@RequestMapping("/getDetail.do")
	@ResponseBody
	public SysFormFieldAttrSet getDetail(HttpServletRequest request) {
		String wfkey = request.getParameter("wfkey");
		String wfversionStr = request.getParameter("wfversion");
		String wfnode = request.getParameter("wfnode");
		String fieldcode = request.getParameter("fieldcode");
		Integer wfversion = null;
		if (StringUtil.isNotBlank(wfversionStr))
			wfversion = Integer.valueOf(wfversionStr);
		
		return formFieldAttrSetService.getDetail(wfkey, wfversion, wfnode, fieldcode);
	}
}

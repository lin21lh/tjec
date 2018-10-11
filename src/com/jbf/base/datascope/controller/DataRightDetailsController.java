package com.jbf.base.datascope.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.datascope.service.DataRightDetailsService;
import com.jbf.base.datascope.vo.RoleDataScopeVo;
import com.jbf.common.exception.AppException;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping({"/base/datascope/DataRightDetailsController"})
public class DataRightDetailsController {

	@Autowired
	DataRightDetailsService drDetailsService;
	
	/**
	 * 数据权限查看界面(业务菜单功能)
	 * @Title: detailsEntry 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/detailsEntry.do")
	public ModelAndView detailsEntry(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String resourceid = request.getParameter("resourceid");
		modelMap.put("resourceid", resourceid);
		return new ModelAndView("base/datascope/dataRightDetailsEntry", "modelMap", modelMap);
	}
	
	
	/**
	 * 查找数据权限
	 * @Title: findRoleDSList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param resourceid
	 * @param @return 设定文件 
	 * @return List<RoleDataScopeVo> 返回类型 
	 * @throws
	 */
	@RequestMapping("/findRoleDSList.do")
	@ResponseBody
	public List<RoleDataScopeVo> findRoleDSList(Long resourceid) {
		return drDetailsService.findRoleDataRightList(resourceid);
	}
	
	/**
	 * @throws AppException 
	 * 查找系统默认数据权限
	 * @Title: defaultDataRightDetails 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return Object 返回类型 
	 * @throws
	 */
	@RequestMapping("/defaultDataRightDetails.do")
	@ResponseBody
	public ResultMsg defaultDataRightDetails(Long resourceid) {
		
		try {
			return new ResultMsg(true, "", drDetailsService.getDefDataRightDetails(resourceid));
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResultMsg(false, "查询数据权限详情失败！失败原因：" + e.getMessage());
		}

	}
}

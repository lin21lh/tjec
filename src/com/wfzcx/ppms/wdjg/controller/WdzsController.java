package com.wfzcx.ppms.wdjg.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.SysFileManageService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.ppms.discern.po.ProProject;
import com.wfzcx.ppms.wdjg.service.WdzsService;

/**
 * 文档展示Controller类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Controller
@RequestMapping("/ppms/wdjg/WdzsController")
public class WdzsController {

	@Autowired
	WdzsService service;
	@Autowired 
	SysFileManageService fileManageService;
	
	/**
	 * 初始化加载
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/init.do")
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/wdjg/wdzs_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	/**
	 * 项目查询
	 * @param request
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryProject.do")
	@ResponseBody
	public EasyUITotalResult queryProject(HttpServletRequest request) throws AppException {
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = service.queryProject(map);
		return EasyUITotalResult.from(ps);
	}
	
	/**
	 * 进入文档展示页面
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	@RequestMapping("/queryWdzsInit.do")
	public ModelAndView queryWdzsInit(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/wdjg/wdzs_detail");
		String projectid = request.getParameter("projectid");
		mav.addObject("wd_projectid", projectid);
		
		return mav;
	}
	
	/**
	 * 页面table构造
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryWdzs.do")
	@ResponseBody
	public List queryWdzs(HttpServletRequest request) {
		List list = new ArrayList();
		String projectid = request.getParameter("wd_projectid");
		list = service.queryWdzs(projectid);
		return list;
	}

	/**
	 * 下载全部文档
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/downloadAll.do")
	public void downloadAll(HttpServletRequest request,HttpServletResponse response) throws IOException {

		String projectid = request.getParameter("projectid");
		ProProject proProject = service.getProject(projectid);
		String fname = proProject.getProName() + "_项目文档.zip";
		//文件名中文乱码处理
		if (request.getHeader("User-Agent").toUpperCase().indexOf("TRIDENT") > 0) {//IE浏览器
			fname = java.net.URLEncoder.encode(fname, "UTF-8");
			fname = StringUtils.replace(fname, "+", "%20");// 替换空格
//			fname = fname.replaceAll("\\+", "%20");
		} else {
			fname = new String(fname.getBytes("UTF-8"), "iso-8859-1");
		}
		response.setHeader("Content-disposition", "attachment; filename="+fname);
		response.setContentType("application/octet-stream");
		
		//输出流包装
		ZipOutputStream zipout = new ZipOutputStream(response.getOutputStream());
		String items = request.getParameter("items");
		String[] itemids = items.split(",");
		for(int i=0;i<itemids.length;i++){
			SysFileManage fm = fileManageService.get(Long.valueOf(itemids[i]));
			String filename = fm.getFilename();
			zipout.putNextEntry(new ZipEntry(filename));
			InputStream ins = fileManageService.getFile(Integer.valueOf(itemids[i]));
			byte[] data = new byte[2048];
			int size = 0;
            while((size = ins.read(data)) != -1) {
            	zipout.write(data,0,size);
            }
            ins.close();
            zipout.closeEntry();
		}
		
		zipout.flush();
		zipout.close();
		
	}
}

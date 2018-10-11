package com.wfzcx.fam.manage.outExcel.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jbf.common.util.ControllerUtil;
import com.wfzcx.fam.manage.outExcel.service.OutApplicationFormService;

/**
 * 导出申请表操作类
 * @ClassName: OutRegisterAppFormController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-5-27 上午08:50:59
 */
@Controller
@RequestMapping({ "/manage/outExcel/controller/OutApplicationFormController" })
public class OutApplicationFormController {
	
	@Autowired
	OutApplicationFormService applicationFormService;
	
	/**
	 * 导出开立(变更)申请表
	 * @Title: outRegisterAppicationForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/outRegisterAppicationForm.do")
	@ResponseBody
	public String outRegisterAppicationForm(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String path = request.getSession().getServletContext().getRealPath("/")+"templates/fam/manage/excel/市级预算单位银行账户开立（变更、注销）申请表.xls";
		param.put("path", path);
		String type = (String)param.get("type");
		try {
			
			HSSFWorkbook work = applicationFormService.outRegisterAppicationForm(param);
			
			OutputStream os = response.getOutputStream();// 取得输出流  
			String name="市级预算单位银行账户开立（变更、注销）申请表.xls";
			if("1".equals(type)){
				name="市级预算单位银行账户开立申请表.xls";
			}else if("2".equals(type)){
				name="市级预算单位银行账户变更申请表.xls";
			}else{
				name="市级预算单位银行账户注销申请表.xls";
			}
			String fileName =  URLEncoder.encode(name,"UTF8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-disposition",  
			"attachment;filename="+fileName);  
			work.write(os);  
			os.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	@RequestMapping("/outRecordAppicationForm.do")
	@ResponseBody
	public String outRecordAppicationForm(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		String path = request.getSession().getServletContext().getRealPath("/")+"templates/fam/manage/excel/市级预算单位银行账户备案表.xls";
		param.put("path", path);
		try {
			
			HSSFWorkbook work = applicationFormService.outRecordAppicationForm(param);
			
			OutputStream os = response.getOutputStream();// 取得输出流  
			String name="市级预算单位银行账户备案表.xls";
			String fileName =  URLEncoder.encode(name,"UTF8");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");  
			response.setHeader("Content-disposition",  
			"attachment;filename="+fileName);  
			work.write(os);  
			os.close();  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
}

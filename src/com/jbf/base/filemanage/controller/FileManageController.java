/************************************************************
 * 类名：FileManageController.java
 *
 * 类别：Controller
 * 功能：提供附件管理的页面入口和增删查功能
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-20  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.filemanage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.SysFileManageService;
import com.jbf.base.filemanage.service.impl.UpLoadFile;
import com.jbf.base.filemanage.service.impl.UpLoadFile.UpLoadFileDTO;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.exception.AppException;
import com.jbf.common.ronline.DocConverter;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Controller
@RequestMapping({"/base/filemanage/fileManageController"})
public class FileManageController {
	
	@Autowired 
	SysFileManageService fileManageService;

	/**
	 * 附件管理界面
	 * @param request sessionId sessionID keyid 业务数据ID elementcode 业务模块编码
	 * @return ModelAndView
	 */
	@RequestMapping({"/entry.do"})
	public ModelAndView entry(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("sessionId", request.getSession().getId());
		modelMap.put("keyid", request.getParameter("keyid"));
		modelMap.put("elementcode", request.getParameter("elementcode"));
		return new ModelAndView("/base/fileManage/filemanage", "modelMap", modelMap);
	}
	
	/**
	 * 添加附件
	 * @Title: addFile 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping({"/addFile.do"})
	public ModelAndView addFile(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("sessionId", request.getSession().getId());
		modelMap.put("keyid", request.getParameter("keyid"));
		modelMap.put("elementcode", request.getParameter("elementcode"));
		return new ModelAndView("/base/fileManage/fileManageAdd", "modelMap", modelMap);
	}
	/**
	 * 附件查询
	 * @param keyid 业务数据ID
	 * @param elementcode 业务编码
	 * @param page 当前页
	 * @param rows 每页条数
	 * @return EasyUITotalResult
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@ResponseBody
	@RequestMapping({"/query.do"})
	public EasyUITotalResult query(String keyid, String elementcode, Integer page, Integer rows) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		return fileManageService.query(keyid, elementcode, page, rows);
	}
	
	/**
	 * 附件查询
	 * @Title: queryFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({"/queryFiles.do"})
	public List<Map<String, Object>> queryFiles(HttpServletRequest request) {
		
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		String stepid = request.getParameter("stepid");
		String showFileLength = request.getParameter("showFileLength");

		return fileManageService.queryFiles(elementcode, keyid, stepid,showFileLength);
	} 
	
	/**
	 * 附件查询
	 * @Title: queryFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping({"/queryFilesByItemid.do"})
	public List<Map<String, Object>> queryFilesByItemid(HttpServletRequest request) {
		String elementcode = request.getParameter("itemid");
		String showFileLength = request.getParameter("showFileLength");
		return fileManageService.queryFilesByItemid(elementcode,showFileLength);
	} 
	
	/**
	 * 附件添加
	 * @param request 附件属性参数
	 * @param response
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping({"/add.do"})
	public Map<String, Object> add(HttpServletRequest request, HttpServletResponse response) throws IOException { 
				     
		List<UpLoadFileDTO> filedata = UpLoadFile.getUpLoadFile().readRequestInputStreams(request, 3 * 1024 * 1024 * 1024L);
		
		Map<String, Object> fileInfo = fileManageService.add(filedata);
		return fileInfo;
	}
	
	/**
	 * 附件下载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping({"/downLoadFile.do"})
	@ResponseBody
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response) {
		
		String itemid = request.getParameter("itemid");
		SysFileManage fm = fileManageService.get(Long.valueOf(itemid));
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs =null;
		BufferedOutputStream bouts=null;;
		try {
			if (fm.getSavemode() == 0) {
				 // path是指欲下载的文件的路径。
		        File file = new File(fm.getFilepath());
		        if (!file.exists() || !file.isFile()) {
						//Map modelMap = new HashMap();
						//modelMap.put("errmsg", "文件" + fm.getFilename() + "未找到！");
						throw new IOException("文件" + fm.getFilename() + "未找到！");
						//return new ModelAndView("/base/fileManage/fileDownLoadError", "modelMap", modelMap);		
		        }
		        ins=new FileInputStream(new File(fm.getFilepath()));//构造一个读取文件的IO流对象
			} else if(fm.getSavemode() == 1){
				ins = fileManageService.getFile(Integer.valueOf(itemid));
			}
			String filename = fm.getFilename();
            bins=new BufferedInputStream(ins);//放到缓冲流里面
            response.setContentType("application/octet-stream");
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
         	 filename = java.net.URLEncoder.encode(filename, "UTF-8");    
                 filename = StringUtils.replace(filename, "+", "%20");//替换空格    
                 //filename = filename.replaceAll(regex, replacement);
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
           
            //return new ModelAndView();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			//Map modelMap = new HashMap();
			//modelMap.put("errmsg", "下载失败！失败原因：" + e.getMessage());
			//return new ModelAndView("/base/fileManage/fileDownLoadError", "modelMap", modelMap);		
		//	return new ResultMsg(true, "下载失败！失败原因：" + e.getMessage());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 附件删除
	 * @param itemids 附件ID
	 * @return ResultMsg
	 */
	@ResponseBody
	@RequestMapping({"/delete.do"})
	public ResultMsg deleteFiles(String itemids) {
		ResultMsg resultMsg = null;
		fileManageService.delete(itemids);
		resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		return resultMsg;
	}
	
	/**
	 * 附件预览
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping({"/showFile.do"})
	public ModelAndView showFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
		String propath = request.getSession().getServletContext().getRealPath("/")+"component"+File.separator+"ronline"+File.separator+"upload"+File.separator;
		String uuid = CommonUtil.getUUID32();
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		String itemid = request.getParameter("itemid");
		SysFileManage fm = fileManageService.get(Long.valueOf(itemid));
		
		//从数据库下载的文件路径
		String fileSuffix = fm.getFilename().substring(fm.getFilename().lastIndexOf("."));
		if(".txt".equals(fileSuffix)){//如果是text文本，则将格式改为odt以解决中文内容乱码问题
			fileSuffix = ".odt";
		}
		String swfPath=propath+uuid+fileSuffix;
		
		//最终生成的swf
		String pdfAndSwfPath= propath+uuid+".swf";
		modelMap.put("swfPath", "component/ronline/upload/"+uuid+".swf");
		
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs =null;
		BufferedOutputStream bouts=null;
		DocConverter dcv = null;
		File targetFile =null;
		
		if(fileSuffix.equals(".txt") || fileSuffix.equals(".doc") 
				|| fileSuffix.equals(".docx") || fileSuffix.equals(".xls") || fileSuffix.equals(".xlsx") 
				|| fileSuffix.equals(".odt") || fileSuffix.equals(".pdf") || fileSuffix.equals(".ppt")){
			modelMap.put("flag", true);
			try {
				if (fm.getSavemode() == 0) {
					 // path是指欲下载的文件的路径。
			        File file = new File(fm.getFilepath());
			        if (!file.exists() || !file.isFile()) {
							throw new IOException("文件" + fm.getFilename() + "未找到！");
			        }
			        dcv =  new DocConverter(fm.getFilepath(),pdfAndSwfPath);
				} else {
					targetFile = new File(swfPath);
					ins = fileManageService.getFile(Integer.valueOf(itemid));
					bins=new BufferedInputStream(ins);//放到缓冲流里面
					bouts = new BufferedOutputStream(new FileOutputStream(targetFile));
					int r;
					while((r = bins.read()) != -1){
	            	   bouts.write(( byte)r);
					}
					dcv =  new DocConverter(swfPath,pdfAndSwfPath);
				}
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(dcv!=null){
				dcv.conver();
			}
			//删除临时下载的文件
			if(targetFile!=null){
				targetFile.delete();
			}
		}else{
			modelMap.put("flag", false);
		}
		
		return new ModelAndView("/base/fileManage/fileReadOnline", "modelMap", modelMap);
	}
	
	/**
	 * 删除swf文件
	 * @Title: delSwfFile 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @param response 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	@RequestMapping({"/delSwfFile.do"})
	public void delSwfFile(HttpServletRequest request, HttpServletResponse response){
		String propath = request.getSession().getServletContext().getRealPath("/");
		String delswf = request.getParameter("delswf");
		if(delswf!=null && !"".equals(delswf)){
			File swfFile = new File(propath+delswf);
			if(swfFile.exists()){
				swfFile.delete();
				System.out.println("【在线预览】删除文件成功");
			}
		}else{
			System.out.println("【在线预览】删除文件失败");
		}
	}
	/**
	 * 
	 * @Title: showUploadifyView 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return ModelAndView 返回类型 
	 * @throws
	 */
	@RequestMapping("/uploadify.do")
	public ModelAndView showUploadifyView(HttpServletRequest request){
		
		String fileItemmids = request.getParameter("fileItemmids");
		String tdId = request.getParameter("tdId");
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		if(fileItemmids==null || fileItemmids.equals("") || fileItemmids.equals("undefined")){
			fileItemmids="itemids";
		}
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("fam/manage/common/uploadify");
		mav.addObject("fileItemmids",fileItemmids);//隐藏域
		mav.addObject("sessionId", session.getId());//session
		mav.addObject("tdId", tdId);//附件展示框
		mav.addObject("elementcode", elementcode);//业务类型
		mav.addObject("keyid", keyid);//业务主键
		return mav;
	}
	
	/**
	 * @Title: photosifyInit
	 * @Description: 拍照页面初始化
	 * @author ybb
	 * @date 2017年3月29日 下午7:21:00
	 * @param request
	 * @return
	 */
	@RequestMapping("/photosifyInit.do")
	public ModelAndView photosifyInit(HttpServletRequest request){
		
		String fileItemmids = request.getParameter("fileItemmids");
		String tdId = request.getParameter("tdId");
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		if(fileItemmids == null || fileItemmids.equals("") || fileItemmids.equals("undefined")){
			fileItemmids = "itemids";
		}
		
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("fam/manage/common/photosify");
		mav.addObject("fileItemmids",fileItemmids);//隐藏域
		mav.addObject("sessionId", session.getId());//session
		mav.addObject("tdId", tdId);//附件展示框
		mav.addObject("elementcode", elementcode);//业务类型
		mav.addObject("keyid", keyid);//业务主键
		
		return mav;
	}
	
	/**
	 * @Title: photosify
	 * @Description: 拍照上传
	 * @author ybb
	 * @date 2017年3月29日 下午5:30:40
	 * @param request
	 * @return
	 */
	@RequestMapping("/photosify.do")
	public ModelAndView photosify(HttpServletRequest request){
		
		String fileItemmids = request.getParameter("fileItemmids");
		String tdId = request.getParameter("tdId");
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		if(fileItemmids == null || fileItemmids.equals("") || fileItemmids.equals("undefined")){
			fileItemmids = "itemids";
		}
		
		//获取session用于附件上传
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView("fam/manage/common/photos");
		mav.addObject("fileItemmids",fileItemmids);//隐藏域
		mav.addObject("sessionId", session.getId());//session
		mav.addObject("tdId", tdId);//附件展示框
		mav.addObject("elementcode", elementcode);//业务类型
		mav.addObject("keyid", keyid);//业务主键
		
		return mav;
	}
	
	/**
	 * @Title: addPhotos
	 * @Description: 拍照上传文件
	 * @author ybb
	 * @date 2017年3月30日 上午11:52:07
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws AppException 
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping({"/addPhotos.do"})
	public ResultMsg addPhotos(HttpServletRequest request, HttpServletResponse response){ 
		
		ResultMsg msg = null;
		
		try {
			
			List<UpLoadFileDTO> filedata = UpLoadFile.getUpLoadFile().readRequestInputStreamsForPhotos(request, 3 * 1024 * 1024 * 1024L);
			
			Map<String, Object> fileInfo = fileManageService.addPotos(filedata);
			
			if (fileInfo != null && !fileInfo.isEmpty()) {
				msg = new ResultMsg(true, "上传成功");
			} else {
				msg = new ResultMsg(false, "保存失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, e.getMessage());
		}

		return msg;
	}
}

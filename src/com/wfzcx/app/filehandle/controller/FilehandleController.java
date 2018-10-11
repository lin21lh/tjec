package com.wfzcx.app.filehandle.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freework.freedbm.util.GeneralTotalResult;
import com.freework.freedbm.util.TotalResult;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.SysFileManageService;
import com.jbf.base.filemanage.service.impl.UpLoadFile;
import com.jbf.base.filemanage.service.impl.UpLoadFile.UpLoadFileDTO;

/**
 * 业务处理参照：FileManageController.java
 * 
 * @author wang_yliang
 * @date 2016-9-23
 */
@Scope("prototype")
@Controller
@RequestMapping("/app/filehandle/FilehandleController")
public class FilehandleController {

	@Autowired 
	SysFileManageService fileManageService;
	
	@RequestMapping({"/picDownload.do"})
	@ResponseBody
	public void picDownload(HttpServletRequest request, HttpServletResponse response) {
		
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
	
	@RequestMapping("/picUpload.do")
	@ResponseBody
	public GeneralTotalResult picUpload(HttpServletRequest request, HttpServletResponse response){ 
		try {
			List<UpLoadFileDTO> filedata = UpLoadFile.getUpLoadFile().readRequestInputStreams(request, 3 * 1024 * 1024 * 1024L);
			Map<String, Object> fileInfo = fileManageService.add(filedata);
			LinkedList<Map> list = new LinkedList<Map>();
			list.add(fileInfo);
			return new GeneralTotalResult<Map>(new TotalResult<Map>(list,list.size()), true, "");
		} catch (Exception e) {
			return new GeneralTotalResult<Map>(new TotalResult<Map>(new LinkedList<Map>(),0), false,e.getMessage());
		}	     
	}
	
	
}

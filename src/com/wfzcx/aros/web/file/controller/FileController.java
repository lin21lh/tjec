package com.wfzcx.aros.web.file.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.impl.UpLoadFile;
import com.jbf.base.filemanage.service.impl.UpLoadFile.UpLoadFileDTO;
import com.jbf.common.exception.AppException;
import com.jbf.common.ronline.DocConverter;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.web.ResultMsg;
import com.wfzcx.aros.web.file.service.FileService;

/**
 * @ClassName: FileController
 * @Description: 外网文件上传操作
 * @author ybb
 * @date 2016年9月13日 下午2:09:27
 * @version V1.0
 */
@Scope("prototype")
@Controller
public class FileController {

	@Autowired 
	private FileService fileService;
	
	/**
	 * @Title: showUploadView
	 * @Description: 外网附件上传（不验证登录）-返回上传页面
	 * @author ybb
	 * @date 2016年9月13日 下午1:26:11
	 * @param request
	 * @return
	 */
	@RequestMapping("/FileManageController_uploadify.do")
	public ModelAndView showUploadView(HttpServletRequest request){
		
		String fileItemmids = request.getParameter("fileItemmids");
		String tdId = request.getParameter("tdId");
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		if( org.apache.commons.lang.StringUtils.isBlank(fileItemmids) ){
			fileItemmids = "itemids";
		}
		String flag = request.getParameter("flag");
		
		//获取session用于附件上传
		HttpSession session = request.getSession();
		
		ModelAndView mav = null;
		if (!org.apache.commons.lang.StringUtils.isBlank(flag)) {	
			mav = new ModelAndView("aros/web/file/uploadfile");	//弹出窗口返回此页面
		} else {
			mav = new ModelAndView("aros/web/file/uploadify");	//非弹出窗口返回此页面
		}
		
		mav.addObject("fileItemmids",fileItemmids);	//隐藏域
		mav.addObject("sessionId", session.getId());//session
		mav.addObject("tdId", tdId);				//附件展示框
		mav.addObject("elementcode", elementcode);	//业务类型
		mav.addObject("keyid", keyid);				//业务主键
		
		return mav;
	}
	
	/**
	 * 
	 * @Title: fileUpload
	 * @Description: 外网附件上传（不验证登录）-文件上传
	 * @author ybb
	 * @date 2016年9月13日 下午2:34:05
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping({"/FileManageController_add.do"})
	public Map<String, Object> fileUpload(HttpServletRequest request, HttpServletResponse response) throws IOException { 
				     
		List<UpLoadFileDTO> filedata = UpLoadFile.getUpLoadFile().readRequestInputStreams(request, 3 * 1024 * 1024 * 1024L);
		
		Map<String, Object> fileInfo = fileService.fileUpload(filedata);
		
		return fileInfo;
	}
	
	/**
	 * @Title: fileDownload
	 * @Description: 外网附件上传（不验证登录）-文件下载
	 * @author ybb
	 * @date 2016年9月13日 下午3:16:49
	 * @param request
	 * @param response
	 */
	@RequestMapping({"/FileManageController_download.do"})
	public void fileDownload(HttpServletRequest request, HttpServletResponse response) {
		
		String itemid = request.getParameter("itemid");
		SysFileManage fm = fileService.getSysFileManage(Long.valueOf(itemid));
		
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		
		try {
			if (fm.getSavemode() == 0) {
				
				//path是指欲下载的文件的路径。
		        File file = new File(fm.getFilepath());
		        
		        if (!file.exists() || !file.isFile()) {
					//Map modelMap = new HashMap();
					//modelMap.put("errmsg", "文件" + fm.getFilename() + "未找到！");
					throw new IOException("文件" + fm.getFilename() + "未找到");
					//return new ModelAndView("/base/fileManage/fileDownLoadError", "modelMap", modelMap);		
		        }
		        
		        ins = new FileInputStream(new File(fm.getFilepath())); //构造一个读取文件的IO流对象
		        
			} else if(fm.getSavemode() == 1){
				ins = fileService.getFile(Integer.valueOf(itemid));
			}
			
			String filename = fm.getFilename();
			
            bins = new BufferedInputStream(ins);//放到缓冲流里面
            response.setContentType("application/octet-stream");
            
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
            	
            	filename = java.net.URLEncoder.encode(filename, "UTF-8");    
                filename = StringUtils.replace(filename, "+", "%20");//替换空格    
                //filename = filename.replaceAll(regex, replacement);
                
            } else {  
            	
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");  
            } 
            
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            outs = response.getOutputStream();//获取文件输出IO流
            bouts = new BufferedOutputStream(outs);
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
			//return new ResultMsg(true, "下载失败！失败原因：" + e.getMessage());
		} finally {
			try {
				if(ins != null){
					ins.close();
				}
				if(bins != null){
					bins.close();
				}
				if(outs!=null){
					outs.close();
				}
				if(bouts!=null){
					bouts.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Title: deleteFiles
	 * @Description: 外网附件上传（不验证登录）- 文件删除
	 * @author ybb
	 * @date 2016年9月13日 下午3:26:22
	 * @param itemids
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/FileManageController_delete.do"})
	public ResultMsg deleteFiles(String itemids) {
		
		ResultMsg resultMsg = null;
		
		fileService.deleteSysFileManage(itemids);
		
		resultMsg = new ResultMsg(true, AppException.getMessage("crud.delok"));
		
		return resultMsg;
	}
	
	/**
	 * @Title: showFile
	 * @Description: 外网附件上传（不验证登录）-附件预览
	 * @author ybb
	 * @date 2016年9月13日 下午4:18:50
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping({"/FileManageController_showFile.do"})
	public ModelAndView showFile(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
		String propath = request.getSession().getServletContext().getRealPath("/")+"component"+File.separator+"ronline"+File.separator+"upload"+File.separator;
		String uuid = CommonUtil.getUUID32();
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
	
		String itemid = request.getParameter("itemid");
		SysFileManage fm = fileService.getSysFileManage(Long.valueOf(itemid));
		
		//从数据库下载的文件路径
		String fileSuffix = fm.getFilename().substring(fm.getFilename().lastIndexOf("."));
		if(".txt".equals(fileSuffix)){//如果是text文本，则将格式改为odt以解决中文内容乱码问题
			fileSuffix = ".odt";
		}
		String swfPath = propath + uuid + fileSuffix;
		
		//最终生成的swf
		String pdfAndSwfPath = propath + uuid + ".swf";
		modelMap.put("swfPath", "component/ronline/upload/" + uuid + ".swf");
		
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		DocConverter dcv = null;
		File targetFile = null;
		
		if(fileSuffix.equals(".txt") || fileSuffix.equals(".doc") 
				|| fileSuffix.equals(".docx") || fileSuffix.equals(".xls") || fileSuffix.equals(".xlsx") 
				|| fileSuffix.equals(".odt") || fileSuffix.equals(".pdf") || fileSuffix.equals(".ppt")){
			
			modelMap.put("flag", true);
			
			try {
				if (fm.getSavemode() == 0) {
					
					 // path是指欲下载的文件的路径。
			        File file = new File(fm.getFilepath());
			        if (!file.exists() || !file.isFile()) {
							throw new IOException("文件" + fm.getFilename() + "未找到 ");
			        }
			        dcv =  new DocConverter(fm.getFilepath(),pdfAndSwfPath);
			        
				} else {
					
					targetFile = new File(swfPath);
					ins = fileService.getFile(Integer.valueOf(itemid));
					bins = new BufferedInputStream(ins);//放到缓冲流里面
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
					e.printStackTrace();
				}
			}
			
			if(dcv != null){
				dcv.conver();
			}
			//删除临时下载的文件
			if(targetFile != null){
				targetFile.delete();
			}
		}else{
			modelMap.put("flag", false);
		}
		
		return new ModelAndView("/base/fileManage/fileReadOnline", "modelMap", modelMap);
	}
	
	/**
	 * @Title: queryFiles
	 * @Description: 外网附件上传（不验证登录）- 附件查看
	 * @author ybb
	 * @date 2016年9月13日 下午4:45:18
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping({"/FileManageController_queryFiles.do"})
	public List<Map<String, Object>> queryFiles(HttpServletRequest request) {
		
		String elementcode = request.getParameter("elementcode");
		String keyid = request.getParameter("keyid");
		String stepid = request.getParameter("stepid");
		String showFileLength = request.getParameter("showFileLength");

		return fileService.queryFiles(elementcode, keyid, stepid, showFileLength);
	}
}

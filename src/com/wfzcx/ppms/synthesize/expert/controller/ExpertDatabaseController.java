package com.wfzcx.ppms.synthesize.expert.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.systemConfiguration.SystemCfg;
import com.wfzcx.ppms.synthesize.expert.service.ExpertDatabaseServiceI;


@Scope("prototype")
@Controller
@RequestMapping("/synthesize/expert/controller/ExpertDatabaseController")
public class ExpertDatabaseController {
	
	@Autowired
	ExpertDatabaseServiceI expertDatabaseService;
	@Autowired
	ParamCfgComponent pcfg;
	
	@RequestMapping({ "/init.do" })
	public ModelAndView init(HttpServletRequest request) throws ServletException {
		ModelAndView mav = new ModelAndView("ppms/synthesize/expert/expert_init");
		HttpSession session = request.getSession();
		mav.addObject("sessionId", session.getId());
		String menuid  = request.getParameter("menuid");
		mav.addObject("menuid", menuid);
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({ "/qryExpert.do" })
	public EasyUITotalResult qryExpert(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = expertDatabaseService.qryExpert(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	@RequestMapping({ "/optExpertView.do" })
	public ModelAndView optExpertView(HttpServletRequest request) throws Exception{
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		String optFlag = map.get("optFlag")==null?"":map.get("optFlag").toString();
		String uuid = CommonUtil.getUUID32();
		ModelAndView mav = null;
		if("".equals(optFlag)){
			throw new Exception("没找到视图！");
		}else{
			if("add".equals(optFlag) || "edit".equals(optFlag)){
				mav = new ModelAndView("ppms/synthesize/expert/expert_form");
				mav.addObject("uuid",uuid);
			}else if("view".equals(optFlag)){
				mav = new ModelAndView("ppms/synthesize/expert/expert_view");
				mav.addObject("uuid",uuid);
			}else {
				throw new Exception("没找到视图！");
			}
		}
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping({ "/qryExpertWorked.do" })
	public EasyUITotalResult qryExpertWorked(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = expertDatabaseService.qryExpertWorked(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	@ResponseBody
	@RequestMapping({ "/qryQualification.do" })
	public EasyUITotalResult qryQualification(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = expertDatabaseService.qryQualification(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	@ResponseBody
	@RequestMapping({ "/qryAvoidUnitGrid.do" })
	public EasyUITotalResult qryAvoidUnitGrid(HttpServletRequest request){
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		PaginationSupport ps = expertDatabaseService.qryAvoidUnitGrid(map);
		
		return EasyUITotalResult.from(ps);
		
	}
	
	@RequestMapping("/saveExpert.do")
	@ResponseBody
	public ResultMsg saveExpert(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			expertDatabaseService.saveExpert(map);
			msg = new ResultMsg(true, AppException.getMessage("crud.saveok"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("crud.saveerr"));
		}
		return msg;
	}
	
	@RequestMapping("/delExpert.do")
	@ResponseBody
	public ResultMsg delExpert(HttpServletRequest request){
		ResultMsg msg = null;
		Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		try {
			expertDatabaseService.delExpert(map);
			msg = new ResultMsg(true, AppException.getMessage("删除成功！"));
		} catch (Exception e) {
			e.printStackTrace();
			msg = new ResultMsg(false, AppException.getMessage("删除失败！"));
		}
		return msg;
	}
	
	@RequestMapping({ "/uploadView.do" })
	public ModelAndView uploadView(HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("ppms/synthesize/expert/easyuifileupload");;
		return mav;
	}
	
	@RequestMapping("/uploadFile.do")
	@ResponseBody
	 public ResultMsg uploadFile(HttpServletRequest request,  
	            HttpServletResponse response) throws ServletException, IOException {  
		ResultMsg msg = null;
		 	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
		 
	        //获取并解析文件类型和支持最大值  
	        String saveFileName = map.get("saveFileName")==null?"":map.get("saveFileName").toString();  
	        String fileType = map.get("fileType")==null?"":map.get("fileType").toString();  
	        String maxSize = map.get("maxSize")==null?"":map.get("maxSize").toString(); 
	        String photoPath = pcfg.findGeneralParamValue("SYSTEM", "EXPERTPHOTOPATH");
	        //临时目录名  
	        String tempPath = request.getSession().getServletContext().getRealPath("/")+"uploadfile"+File.separator+"temp"+File.separator;  
	        //真实目录名  
	        String filePath = request.getSession().getServletContext().getRealPath("/")+"uploadfile"+File.separator+"headphoto"+File.separator;;  
	        System.out.println(tempPath+"|||"+filePath);
	        DiskFileItemFactory factory = new DiskFileItemFactory();  
	        //最大缓存  
	        factory.setSizeThreshold(5*1024);  
	        //设置临时文件目录  
	        File temFile = new File(tempPath);
	        
	        if (!temFile.exists()) {
	        	temFile.mkdirs();
            } 
	        factory.setRepository(new File(tempPath));  
	        ServletFileUpload upload = new ServletFileUpload(factory);  
	        if(maxSize!=null && !"".equals(maxSize.trim())){  
	            //文件最大上限  
	            upload.setSizeMax(Integer.valueOf(maxSize)*1024*1024);  
	        }  
	        FileOutputStream out = null;
	        try {  
	            //获取所有文件列表  
	            List<FileItem> items = upload.parseRequest(request);  
	            for (FileItem item : items) {
	                if(!item.isFormField()){  
	                    //文件名  
	                    String fileName = item.getName();  
	                      
	                    //检查文件后缀格式  
	                    String fileEnd = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase(); 
	                    boolean flag = true;
	                    if(fileType!=null && !"".equals(fileType.trim())){ 
	                    	boolean isRealType = false; 
	                        String[] arrType = fileType.split(",");  
	                        for (String str : arrType) {  
	                            if(fileEnd.equals(str.toLowerCase())){  
	                                isRealType = true;  
	                                break;  
	                            }  
	                        }  
	                        if(!isRealType){  
	                            //提示错误信息:文件格式不正确  
	                        	msg = new ResultMsg(false, AppException.getMessage("文件格式不正确！"));
	                        	flag = false;
	                        } 
	                    }  
	                    
	                    if(flag){
	                    	//真实上传路径  
		                    StringBuffer sbRealPath = new StringBuffer();
		                     if("".equals(saveFileName)){
		                    	//创建文件唯一名称  
		 	                    String uuid = CommonUtil.getUUID32();  
		 	                    sbRealPath.append(filePath).append(uuid).append(".").append(fileEnd);  
		                     }else{
		                    	 sbRealPath.append(filePath).append(saveFileName).append(".").append(fileEnd); 
		                     }
		                    //写入文件  
		                    File file = new File(sbRealPath.toString());  
		                    //创建目录
		                    if (!file.exists()) {
		                        File rootDirectoryFile = new File(file.getParent());
		                        //创建目录
		                        if (!rootDirectoryFile.exists()) {
		                            boolean ifSuccess = rootDirectoryFile.mkdirs();
		                            if (ifSuccess) {
		                            	System.out.println("文件夹创建成功!");
		                            } else {
		                            	System.out.println("文件夹创建失败!");
		                            }
		                        }
		                        //创建文件
		                        try {
		                            file.createNewFile();
		                        } catch (IOException e) {
		                            e.printStackTrace();
		                        }
		                    } 
		                    item.write(file);  
		                    msg = new ResultMsg(true, AppException.getMessage("上传成功！"));
	                    }
	                    
	                }
	            }
	              
	        }catch (Exception e) {  
	        	e.printStackTrace();
	        	msg = new ResultMsg(false, AppException.getMessage("上传头像失败！"));
	        }finally{
	        	if(out!=null){
	        		out.close();
	        	}
	        }  
	        return msg;  
	    } 
	
}

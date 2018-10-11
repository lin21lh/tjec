/**
 * @Description: 文件下载controller
 * @author 张田田
 * @date 2016-09-8 
 */
package com.wfzcx.aros.web.wjxz.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.SysFileManageService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.aros.web.wjxz.service.FileDownService;

@Scope("prototype")
@Controller
public class FileDownController
{
	@Autowired
	FileDownService fileDownService;
	
	@Autowired 
	SysFileManageService fileManageService;
	
	@RequestMapping("/FileDownController_init.do")
	public ModelAndView init(HttpServletRequest request)
	{
		ModelAndView mav = new ModelAndView("aros/web/wjxz/filedown_init");
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/FileDownController_queryDocList.do")
	@ResponseBody
	public EasyUITotalResult queryDoc(HttpServletRequest request)
	{
		//获取页面传递查询参数
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//执行查询
		PaginationSupport ps = fileDownService.queryDoc(param);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/FileDownController_download.do")
	@ResponseBody
	public void downLoadFile(HttpServletRequest request, HttpServletResponse response)
	{
		String itemid = request.getParameter("itemid");
		SysFileManage fm = fileManageService.get(Long.valueOf(itemid));
		InputStream ins = null;
		BufferedInputStream bins = null;
		OutputStream outs = null;
		BufferedOutputStream bouts = null;
		
		try
		{
			if (fm.getSavemode() == 0)
			{
				// path是指欲下载的文件的路径。
		        File file = new File(fm.getFilepath());
		        if (!file.exists() || !file.isFile())
		        {
					throw new IOException("文件" + fm.getFilename() + "未找到！");
		        }
		        
		        //构造一个读取文件的IO流对象
		        ins = new FileInputStream(new File(fm.getFilepath()));
			}
			else if (fm.getSavemode() == 1)
			{
				ins = fileManageService.getFile(Integer.valueOf(itemid));
			}
			String filename = fm.getFilename();
			//放到缓冲流里面
            bins = new BufferedInputStream(ins);
            response.setContentType("application/octet-stream");
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
            {  
            	filename = java.net.URLEncoder.encode(filename, "UTF-8");
            	//替换空格
                filename = StringUtils.replace(filename, "+", "%20");
            }
            else
            {  
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");  
            } 
            
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            
            //获取文件输出IO流
            outs = response.getOutputStream();
            bouts = new BufferedOutputStream(outs);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            //开始向网络传输文件流
            while ((bytesRead = bins.read(buffer, 0, 8192)) != -1)
            {
                bouts.write(buffer, 0, bytesRead);
            }
            //这里一定要调用flush()方法
            bouts.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != ins)
				{
					ins.close();
				}
				if (null != bins)
				{
					bins.close();
				}
				if (null != outs)
				{
					outs.close();
				}
				if (null != bouts)
				{
					bouts.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}

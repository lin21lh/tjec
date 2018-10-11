/**
 * @Description: 信息公开controller
 * @author 张田田
 * @date 2016-09-8 
 */
package com.wfzcx.aros.web.xxgk.controller;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jbf.base.filemanage.service.SysFileManageService;
import com.jbf.common.dao.EasyUITotalResult;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.ronline.DocConverter;
import com.jbf.common.util.CommonUtil;
import com.jbf.common.util.ControllerUtil;
import com.wfzcx.aros.jzgl.service.CaseFileManageService;
import com.wfzcx.aros.print.po.NoticeContentInfo;
import com.wfzcx.aros.print.po.Noticebaseinfo;
import com.wfzcx.aros.print.service.NoticeService;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.web.xxgk.service.CasePublishService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Scope("prototype")
@Controller
public class CasePublishController
{
	@Autowired
	CasePublishService service;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired 
	SysFileManageService fileManageService;
	
	@Autowired
	private CaseFileManageService caseFileManageService;
	
	@RequestMapping({"/CasePublishController_init.do"})
	public ModelAndView init(HttpServletRequest request) throws ServletException
	{ 
		//返回页面路径
		ModelAndView mav = new ModelAndView("aros/web/xxgk/casePublish_init");
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping({"/CasePublishController_query.do"})
	@ResponseBody
	public EasyUITotalResult query(HttpServletRequest request) throws ServletException
	{ 
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		//返回页面路径
		PaginationSupport ps = service.query(param);
		return EasyUITotalResult.from(ps);
	}
	
	@RequestMapping("/CasePublishController_noticeContent.do")
	public ModelAndView noticeContent(HttpServletRequest request) throws ServletException
	{ 
		// 通知书类型
		String doctype  = request.getParameter("doctype");
		// 案件ID
		String caseid  = request.getParameter("caseid");
		ModelAndView mav = new ModelAndView();
		// 通知书配置
		List<Map<String, Object>> noticeConfig = noticeService.queryNoticeConfig(doctype);
		if (!noticeConfig.isEmpty())
		{
			Map<String, Object> configMap = noticeConfig.get(0);
			mav.setViewName((String)configMap.get("url"));
			NoticeContentInfo info = new NoticeContentInfo();
			// 通知书内容信息
			String xml = noticeService.queryNoticeContent(caseid, doctype).replace(" ", "");
			if (StringUtils.isNotBlank(xml))
			{
				info = (NoticeContentInfo)UtilTool.xmlToObj(xml);
			}
			else
			{
				// 流程类型
				String protype = (String)configMap.get("protype");
				// 字典编码
				String elementcode = (String)configMap.get("elementcode");
				info = noticeService.queryNoticeContentInfo(caseid, protype, elementcode);
			}
			mav.addObject("info", info);
		}
		
		return mav;
	}
	
	/**
	 * 通知书预览
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping({"/CasePublishController_showFile.do"})
	public ModelAndView showFile(HttpServletRequest request, HttpServletResponse response)
	{
		String propath = request.getSession().getServletContext().getRealPath("/")+"component"+File.separator+"ronline"+File.separator+"upload"+File.separator;
		Map<String, Object> param = ControllerUtil.getRequestParameterMap(request);
		Map<String, Object> modelMap = changToSwf(param, propath);
		return new ModelAndView("/base/fileManage/fileReadOnline", "modelMap", modelMap);
	}

	/**
	 * 转成swf格式文件
	 */
	private Map<String, Object> changToSwf(Map<String, Object> param, String propath)
	{
		Writer out = null;
		BufferedInputStream bins = null;
		DocConverter dcv = null;
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String uuid = CommonUtil.getUUID32();
		//最终生成的swf
		String pdfAndSwfPath = propath + uuid + ".swf";
		File targetFile = new File(propath + "tmp.doc");
		modelMap.put("swfPath", "component/ronline/upload/" + uuid + ".swf");
		modelMap.put("flag", true);
		
		try
		{
			String noticeId = (String) param.get("noticeid");
			download(noticeId, propath);
			
			// path是指欲下载的文件的路径。
	        if (!targetFile.exists() || !targetFile.isFile())
	        {
				throw new IOException("文件" + targetFile.getName() + "未找到！");
	        }
	        dcv =  new DocConverter(targetFile.getPath(), pdfAndSwfPath);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != out)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			if (null != bins)
			{
				try
				{
					bins.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		if (null != dcv)
		{
			dcv.conver();
		}
		
		return modelMap;
	}
	
	/**
	 * 通知书下载到本地
	 */
	private void download(String noticeId, String propath)
	{
		Writer out = null;
		Map<String, String> docMap = new HashMap<String, String>();
		
		try
		{
			Noticebaseinfo noticebaseinfo = caseFileManageService.queryNoticeInfo(noticeId);
			String content = noticebaseinfo.getContents();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(new InputSource(new StringReader(content)));   
	        Element root = doc.getDocumentElement();
	        NodeList nodes = root.getChildNodes();
			for (int i = 0; i<nodes.getLength(); i++ )
			{
				Node item = nodes.item(i);
				docMap.put(item.getNodeName(), item.getTextContent());
			}
			
			String noticeTypeName = caseFileManageService.getNoticeTypeName(noticebaseinfo.getDoctype());
			
			Configuration configuration = new Configuration();
			configuration.setDefaultEncoding("utf-8");
			configuration.setClassForTemplateLoading(this.getClass(), "/com/wfzcx/aros/jzgl/template");
			String temFileName = noticeTypeName + "_模板";
			Template t = configuration.getTemplate(temFileName + ".ftl");
			
			File outFile = new File(propath + "tmp.doc");
		    if (!outFile.exists())
		    {
			   outFile.createNewFile();
            }
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile))); 
			t.process(docMap, out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != out)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}
/************************************************************
 * 类名：LoginController.java
 *
 * 类别：Controller
 * 功能：提供系统登录的页面入口和成功登录后的界面入口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-10  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.sys.login.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jbf.base.dic.service.impl.DicElementValSetServiceImpl;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.security.datasource.service.DataSourceService;
import com.jbf.common.util.PTConst;
import com.jbf.common.util.VerifyCodeUtil;
import com.jbf.sys.login.service.CaUserIdentityService;
import com.jbf.sys.login.service.resultMsg;
import com.jbf.sys.resource.service.SysResourceService;
import com.jbf.sys.systemConfiguration.SystemCfg;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;
import com.wfzcx.aros.flow.service.PronodebaseinfoService;
import com.wfzcx.aros.xzfy.service.CasebaseinfoService;
import com.wfzcx.fam.common.Configuration;


@Controller
public class LoginController {

    @Autowired
    SysUserService userService;
    @Autowired
    DataSourceService dataSourceService;
    @Autowired
    private PronodebaseinfoService pronodebaseinfoService;
    @Autowired
    private SysResourceService sysResourceService;
    @Autowired
    private CasebaseinfoService casebaseinfoService;
    
    private boolean startCA = false; // 启用CA标识
	 Configuration  prop =getProperties();
	 static Configuration getProperties() {
		try {
			Configuration rc;
			rc = new Configuration("startCA.properties");
			return rc;
		} catch (Exception e) {
		}
		return null;
	 }
    
    /**
     * 登录界面
     * @param request
     * @param response
     * @return
     * @throws FileNotFoundException 
     */
    @RequestMapping({"/login.do"})
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
        String errorMsg = (String) request.getAttribute("errorMsg");
        // String errorMsg = response.get
        String page = "login/login";
        
        try {
        	//读取是否启用CA标志
			startCA = Boolean.parseBoolean(prop.getValue("startCA"));   
			PTConst.STARTCA = startCA;
		} catch (Exception e) {
		}
        System.out.println((startCA ? "已" : "未") + "启用CA认证.");
        if (startCA) {
        	page = "login/ca/CheckloginCA";
		}

        Map viewMap = new HashMap();
        viewMap.put("errorMsg", errorMsg);
        viewMap.put("isEnabledMultiDS", dataSourceService.isEnabledMultiDataSource());
        viewMap.put("verificationcodeEnabled", SystemCfg.VerificationcodeEnabled());
        if (dataSourceService.isEnabledMultiDataSource()) {
            List<Map> finaceYear = dataSourceService.getAllDataSource();
            viewMap.put("finaceYear", finaceYear);
        }
    	String 	iecoreurl = request.getScheme() + "://" + request.getServerName() + ":"	+ request.getServerPort() + request.getContextPath();
    	viewMap.put("iecoreurl", iecoreurl);
    	String g = CALoginAgent(request);
    	viewMap.put("g", g);
    	String user_Agent = request.getHeader("User-Agent");
        return new ModelAndView(page, "viewMap", viewMap);
    }
    
    /**
     * 超时后跳转
     * @return
     */
    @RequestMapping("/timeout.do")
    public ModelAndView timeout() {
    	
    	return new ModelAndView("login/timeout");
    }
    /**
     * 跳转CA登录界面
     * @Title: loginCA 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @return 设定文件
     */
    @RequestMapping("/loginCA.do")
    public ModelAndView loginCA(HttpServletRequest request, HttpServletResponse response) {
    	String errorMsg = (String) request.getParameter("errorMsg");
    	String fromIECore = (String) request.getParameter("fromIECore");
    	Map viewMap = new HashMap();
        viewMap.put("errorMsg", errorMsg);
        viewMap.put("fromIECore", fromIECore);
        String page = "login/ca/loginCA";
        return new ModelAndView(page, "viewMap", viewMap);
    }

    /**
     * 成功登录
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping({"/successLogin.do"})
    public ModelAndView successLogin(HttpServletRequest request) {
    	
        /*ModelAndView view = new ModelAndView("login/index");
        String showFlag = "all";
        SysUser user = SecureUtil.getCurrentUser();
        Long curUserid = user.getUserid();
        // 委员角色ID
        Long wyRoleid = Long.valueOf(userService.getNameByCode("ROLEID", "wy"));
        List<Map<String, Object>> users = userService.getUserByRoleID(wyRoleid);
        for (Map<String, Object> sysUser : users) {
        	BigDecimal sysUserid = (BigDecimal)sysUser.get("userid");
			if (curUserid == sysUserid.longValue()) {
				showFlag = "wy";
			}
		}
        view.addObject("waitTask", "18");//首页显示待办任务数量
        view.addObject("showFlag", showFlag);
        return view;*/
    	
    	//封装返回页面对象
    	ModelAndView view = new ModelAndView("login/index");
        //获取当前登录用户
        SysUser user = SecureUtil.getCurrentUser();
        
        //获取用户对应的操作菜单项
        List<JSONObject> sysResourceList = sysResourceService.queryResource(user.getUserid());
        view.addObject("sysResourceList", sysResourceList);//首页显示菜单块
        view.addObject("waitTask", casebaseinfoService.queryTodoCaseNum());//首页显示待办任务数量
        
        return view;
    }
    
    /*public ModelAndView successLogin(HttpServletRequest request) {
        // 取得登录用户
        SysUser user = SecureUtil.getCurrentUser();
        String all_menu_json =null; 
        String menu_json = null;
        List list = null;
        List menuList=null;
        if (user != null) {
            Long userid = user.getUserid();
            // 取得菜单
            try {
                list = userService.getResourceTree(user);
                menuList = userService.getResourceTree();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            menu_json = JSON.toJSONString(list);
            all_menu_json = JSON.toJSONString(all_menu_json);
        }
        //以下增加是为CA登录
       // String page = "login/layout";
        
        String page ="";
        HttpSession session = request.getSession();
        String fromIECore = (String) session.getAttribute("fromIECore");
        String isHome = request.getParameter("isHome")==null?"1":(String)request.getParameter("isHome");
        String menuid = request.getParameter("menuid")==null?"":(String)request.getParameter("menuid");
        不显示首页登录页面  wangyl 2016-8-23
//        if("1".equals(isHome)){
//        	page = "login/welcome";
//        }else{
        	page = "login/index";
//        }
        if (startCA && "1".equals(fromIECore)) {//如果是CA界面（包括输入用户名和CA）登录并且是从谷歌浏览器中的IE内核提交而来
        	page = "login/layoutCA";
		}
        ModelAndView view = new ModelAndView(page);
        view.addObject("username", user.getUsername());
        view.addObject("deptname", user.getOrgname());
        view.addObject("usercode", user.getUsercode());
        view.addObject("menu_json", menu_json);
        view.addObject("menus", list);
        view.addObject("menuid", menuid);
        view.addObject("menuList",menuList);
        view.addObject("all_menu_json",all_menu_json);
        view.addObject("weatherEnable",SystemCfg.WeatherEnable());
        return view;
    }*/
    
    /**
     * 成功登录行政复议
     * @param request
     * @return
     */
    @RequestMapping({"/arosLogin.do"})
    public ModelAndView arosLogin(HttpServletRequest request) {
        // 取得登录用户
        SysUser user = SecureUtil.getCurrentUser();
        String all_menu_json =null; 
        String menu_json = null;
        List list = null;
        List menuList=null;
        String menuid = request.getParameter("menuid")==null?"":(String)request.getParameter("menuid");
        String page = "login/layout";
        String key = request.getParameter("resourceid");
        if ("LA".equals(key)){
        	List<String> rolesid = casebaseinfoService.getRoleidByuserid(user.getUserid());
        	// 接待人员 入口 
        	if(rolesid.contains("10")){
        		key = "SQBL";
        	}
        	if(rolesid.contains("200")){
        		key = "WY";
        	}
        }
        Long resourceid = Long.valueOf(userService.getNameByCode("RESOURCEID", key));
        List pronodebaseinfos = null;
        if (user != null) {
            if (!("TJ".equals(key) || "XTGL".equals(key) )) {
				page = "aros/xzfy/casebaseinfo_index";
				menuid = String.valueOf(resourceid);
			} else {
	            // 取得菜单
	            try {
	                list = userService.getResourceTree(user, resourceid);
//	                list = userService.getResourceTree(user);
	                menuList = userService.getResourceTree();
	            }
	            catch (Exception e) {
	                e.printStackTrace();
	            }
	            menu_json = JSON.toJSONString(list);
	            all_menu_json = JSON.toJSONString(all_menu_json);
			}
            
            //请求为立案时，返回角色对应的案件操作列表
            if("LA".equals(key)) {
            	pronodebaseinfos = pronodebaseinfoService.queryPronodebaseinfos(user.getUserid());
            }
        }
        
        
        
        HttpSession session = request.getSession();
        String fromIECore = (String) session.getAttribute("fromIECore");
        if (startCA && "1".equals(fromIECore)) {//如果是CA界面（包括输入用户名和CA）登录并且是从谷歌浏览器中的IE内核提交而来
        	page = "login/layoutCA";
		}
        
        ModelAndView view = new ModelAndView(page);
        view.addObject("username", user.getUsername());
        view.addObject("deptname", user.getOrgname());
        view.addObject("usercode", user.getUsercode());
        view.addObject("menu_json", menu_json);
        view.addObject("menus", list);
        view.addObject("menuid", menuid);
        view.addObject("menuList",menuList);
        view.addObject("all_menu_json",all_menu_json);
        view.addObject("weatherEnable",SystemCfg.WeatherEnable());
        view.addObject("key", key);
        view.addObject("pronodebaseinfos", pronodebaseinfos);
        return view;
    }
    
    /**
     * 成功登录
     * @param request
     * @return
     */
    @RequestMapping({"/caLogin.do"})
    public ModelAndView caLogin(HttpServletRequest request) {
        // 取得登录用户
        SysUser user = SecureUtil.getCurrentUser();

        String menu_json = null;
        List list = null;
        if (user != null) {
            Long userid = user.getUserid();
            // 取得菜单
            try {
                list = userService.getResourceTree(user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            menu_json = JSON.toJSONString(list);
        }
        String page = "login/layout";
        ModelAndView view = new ModelAndView(page);
        view.addObject("username", user.getUsername());
        view.addObject("usercode", user.getUsercode());
        view.addObject("menu_json", menu_json);
        view.addObject("menus", list);
        return view;
    }
    
    /**
     * 验证码
     * @param request
     * @param response
     */
    @RequestMapping("/exclude/verfiyCode.do")
    public void genVerfiyCodeImage(HttpServletRequest request, HttpServletResponse response){
        
        try{
            
            HttpSession session = request.getSession();
            // 生成验证码，写入用户session
            String verifyCode = VerifyCodeUtil.generateTextCode(VerifyCodeUtil.TYPE_ALL_MIXED, 4, "0oOilJI1");
            session.setAttribute("verifyCode", verifyCode);
    
            // 输出验证码给客户端
            response.setContentType("image/jpeg");
            BufferedImage bim = VerifyCodeUtil.generateImageCode(verifyCode, 80,
                    30, 2, true, Color.WHITE, Color.BLACK, null);
            ImageIO.write(bim, "JPEG", response.getOutputStream());
        
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    @RequestMapping("/caLoginValidation.do")
	@ResponseBody
	public resultMsg caLoginValidation(HttpServletRequest request, HttpServletResponse response) {
    	String signed_data = (String) request.getParameter("signeddata");
    	String original_data =  (String) request.getParameter("originaldata");;
    	String certcn = (String) request.getParameter("certcn");
    	HttpSession session = request.getSession();
		if (certcn != null) {
			try {
				certcn = URLDecoder.decode(certcn, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		String clientIP = request.getRemoteAddr();
		// 登录失败目标页面
		// String error_destination = getErrorDestination(req, session);
		Enumeration e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String sessionName = (String) e.nextElement();
			session.removeAttribute(sessionName);
		}
		resultMsg msg = null;
		try {
			 CaUserIdentityService caUserIdentityService = new CaUserIdentityService();
			String username = caUserIdentityService.caLogin(clientIP,signed_data, original_data);
			//String username = "admin";
			username = username==null?"":username.toString();
			if (!"".equals(username)) {
				// 验证通过，写一个标记到session中
				String caMark = UUID.randomUUID().toString();
				session.setAttribute("CAMark", caMark);
				session.setAttribute("CAUserName", username);
				msg = new resultMsg(true, caMark);
				msg.setMessage(username);// 传送用户名
			} else {
				msg = new resultMsg(false, "CA验证失败!");
			}

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println("------认证出现异常---");
			System.out.println("message:" + e2.getMessage());
			msg = new resultMsg(false, "CA验证出现异常!");

		}
		return msg;
	}
    public String CALoginAgent(HttpServletRequest request) {
		javax.servlet.http.Cookie h[] = request.getCookies();
		String g = "";
		if (h != null && h.length > 0) {
			for (int f = 0; f < h.length; f++) {

				if (f != 0) {
					g = g + "{n}";
				}
				// 如果是客户端发来的JSESSIONID，要验证与服务端是否一致
				if ("JSESSIONID".equals(h[f].getName())) {

					// 返回服务端的SessionID
					g = h[f].getName() + "=" + request.getSession().getId()
							+ "; path=" + request.getContextPath()
							+ "; domain=" + request.getServerName();

				} else {
					// 处理其他Cookies
					g = g + h[f].getName() + "=" + h[f].getValue() + "; path="
							+ request.getContextPath() + "; domain="
							+ request.getServerName();
				}

				//System.out.println("ga===" + g);
			}
		} else {
			String JSESSIONID = request.getSession().getId();
			g = "JSESSIONID=" + JSESSIONID + "; path="
					+ request.getContextPath() + "; domain="
					+ request.getServerName();
			//System.out.println("gb===" + g);
		}

		return g;
	}
}

package com.jbf.sys.regist.controller;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jbf.base.dic.po.SysYwDiccodeitem;
import com.jbf.base.dic.po.SysYwDicenumitem;
import com.jbf.common.util.ControllerUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.dept.dao.SysDeptDao;
import com.jbf.sys.dept.po.SysDept;
import com.jbf.sys.regist.po.ProSocialRegist;
import com.jbf.sys.regist.service.SocialCapitalRegistServiceI;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;

@Controller
public class SocialCapitalRegistController {
	
	@Autowired
	SocialCapitalRegistServiceI SocialCapitalRegistService;
	@Autowired
    SysUserService userService;
	@Autowired
	SysDeptDao sysDeptDao;
	
	 /**
     * 登录界面
     * @param request
     * @param response
     * @return
     * @throws FileNotFoundException 
     */
    @RequestMapping({"/regist.do"})
    public ModelAndView regist(HttpServletRequest request, HttpServletResponse response){

        String page = "login/socialcapitalregist";
        
        List<SysYwDiccodeitem> prefList = SocialCapitalRegistService.getPreferencesList();
        List<SysYwDicenumitem> cateList = SocialCapitalRegistService.getCategoryList();
        Map viewMap = new HashMap();
        viewMap.put("prefList", prefList);
        viewMap.put("cateList", cateList);
        return new ModelAndView(page, "viewMap", viewMap);
    }
    
    /**
     * 验证唯一性
     * @Title: VerifyUniqueness 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param request
     * @param @param response 设定文件 
     * @return void 返回类型 
     * @throws
     */
    @RequestMapping({"/VerifyUniqueness.do"})
    @ResponseBody
    public ResultMsg VerifyUniqueness(HttpServletRequest request, HttpServletResponse response){
    	
    	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
    	
    	String orgname = map.get("orgname")==null?"":map.get("orgname").toString();
    	String orgcode = map.get("orgcode")==null?"":map.get("orgcode").toString();
    	String username = map.get("username")==null?"":map.get("username").toString();
    	String usercode = map.get("usercode")==null?"":map.get("usercode").toString();
    	String type = map.get("type")==null?"":map.get("type").toString();
    	
    	ResultMsg res = null;
    	/*检查系统表中的用户code*/
    	List<SysUser> users = userService.findAllUserList();
    	boolean flag = false;
    	for(int i=0;i<users.size();i++){
    		
    		if(!"".equals(usercode)){
    			if(usercode.equalsIgnoreCase(users.get(i).getUsercode())){
        			flag=true;
        			res = new ResultMsg(false, "该用户code已被使用！");
        			break;
        		}
    		}
    		if(!"".equals(username)){
    			if(username.equalsIgnoreCase(users.get(i).getUsername())){
        			flag=true;
        			res = new ResultMsg(false, "该用户名已被使用！");
        			break;
        		}
    		}
    		
    	}
    	/*检查pro_social_regist*/
    	List<ProSocialRegist> registUserList = null;
    	if(!"".equals(usercode)){
    		registUserList = SocialCapitalRegistService.getRegistEnableUserList();
    		for(int i=0;i<registUserList.size();i++){
    			if(usercode.equalsIgnoreCase(registUserList.get(i).getUsercode())){
        			flag=true;
        			res = new ResultMsg(false, "该用户code已被使用！");
        			break;
        		}
    		}
    	}
    	
    	if(!"".equals(orgname) || !"".equals(orgcode)){
    		List<SysDept> deptList = sysDeptDao.query();
    		for(int i=0;i<deptList.size();i++){
    			if(!"".equalsIgnoreCase(orgcode)){
					if(orgname.equals(deptList.get(i).getWholename())){
		    			flag=true;
		    			res = new ResultMsg(false, "该社会资本名称已被使用！");
		    			break;
		    		}
    			}
    			if(!"".equalsIgnoreCase(orgcode)){
    				if(orgcode.equals(deptList.get(i).getIsbncode())){
    	    			flag=true;
    	    			res = new ResultMsg(false, "该组织机构代码已被使用！");
    	    			break;
    	    		}
    			}
    		}
			
		}
		
    	if(flag==false){
    		res = new ResultMsg(true, "可以使用！");
    	}
    	return res;
    }
    
    /**
     * 提交信息
     * @Title: subRegistInfo 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param @param request
     * @param @return 设定文件 
     * @return ResultMsg 返回类型 
     * @throws
     */
    @RequestMapping({"/subRegistInfo.do"})
    @ResponseBody
    public ResultMsg subRegistInfo(HttpServletRequest request) {
    	ResultMsg res = null;
    	Map<String, Object> map = ControllerUtil.getRequestParameterMap(request);
    	String verfiyCode = map.get("verifyCode")==null?"":map.get("verifyCode").toString();
    	String vcode = (String)request.getSession().getAttribute("verifyCode");
    	try {
    		if(!verfiyCode.equalsIgnoreCase(vcode)){
    			res = new ResultMsg(false, "验证码不正确！");
    		}else{
    			SocialCapitalRegistService.subRegistInfo(map);
    			res = new ResultMsg(true, "提交申请成功！");
    		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			res = new ResultMsg(false, "提交申请失败！");
		}
    	return res;
    }
    
}

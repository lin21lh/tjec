package com.jbf.sys.regist.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.dic.po.SysYwDiccodeitem;
import com.jbf.base.dic.po.SysYwDicenumitem;
import com.jbf.sys.regist.dao.ProSocialRegistDaoI;
import com.jbf.sys.regist.po.ProSocialRegist;
import com.jbf.sys.regist.service.SocialCapitalRegistServiceI;

@Scope("prototype")
@Service("/sys/regist/impl/SocialCapitalRegistServiceImpl")
public class SocialCapitalRegistServiceImpl implements
		SocialCapitalRegistServiceI {

	@Autowired
	ProSocialRegistDaoI ProSocialRegist;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void subRegistInfo(Map map) throws Exception{
		// TODO Auto-generated method stub
		String orgname = map.get("ORGNAME")==null?"":map.get("ORGNAME").toString();
    	String orgcode = map.get("ORGCODE")==null?"":map.get("ORGCODE").toString();
    	String nameflag = map.get("nameflag")==null?"":map.get("nameflag").toString();
    	String usercode = map.get("codes")==null?"":map.get("codes").toString();
    	String namecode = map.get("namecode")==null?"":map.get("namecode").toString();
    	String psw = map.get("pwds")==null?"":map.get("pwds").toString();
    	String psw2 = map.get("pwds2")==null?"":map.get("pwds2").toString();
    	String remark = map.get("REMARK")==null?"":map.get("REMARK").toString();
    	String iscombo = map.get("ISCOMBO")==null?"":map.get("ISCOMBO").toString();
    	String linkperson = map.get("linkperson")==null?"":map.get("linkperson").toString();
    	String linkphone = map.get("ORGTEL")==null?"":map.get("ORGTEL").toString();
    	String categoryCode = map.get("category")==null?"":map.get("category").toString();
    	String categoryName = map.get("categoryName")==null?"":map.get("categoryName").toString();
    	String preferencesName = map.get("preferencesName")==null?"":map.get("preferencesName").toString();
    	String preferencesCode = map.get("preferences")==null?"":map.get("preferences").toString();
    	
    	ProSocialRegist t = new ProSocialRegist();
    	t.setPreferencesCode(preferencesCode);
    	t.setPreferencesName(preferencesName);
    	t.setCategoryCode(categoryCode);
    	t.setCategoryName(categoryName);
    	t.setOrgcode(orgcode);
    	t.setOrgname(orgname);
    	t.setUsercode(namecode);
    	t.setUsername(orgname);
    	t.setUserpswd(psw);
    	t.setRemark(remark);
    	t.setIscombo(iscombo);
    	t.setLinkperson(linkperson);
    	t.setLinkphone(linkphone);
    	t.setStatus("0");
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String time = df.format(new Date());
    	t.setApplicationTime(time);
    	
    	ProSocialRegist.save(t);
	}

	@Override
	public List<ProSocialRegist> getRegistEnableUserList() {
		// TODO Auto-generated method stub
		String sql = "select * from pro_social_regist t where t.STATUS = '0' or t.status = '1'";
		List<ProSocialRegist> list = (List<ProSocialRegist>)ProSocialRegist.findVoBySql(sql, ProSocialRegist.class);
		return list;
	}

	@Override
	public List<SysYwDiccodeitem> getPreferencesList() {
		// TODO Auto-generated method stub
		
		String sql = "select * from sys_yw_diccodeitem t where t.elementcode='PROTRADE' and t.status = 0 and t.levelno=1 order by t.code";
		List<SysYwDiccodeitem> list = (List<SysYwDiccodeitem>)ProSocialRegist.findVoBySql(sql, SysYwDiccodeitem.class);
		return list;
	}

	@Override
	public List<SysYwDicenumitem> getCategoryList() {
		// TODO Auto-generated method stub
		String sql = "select * from sys_yw_dicenumitem t where t.elementcode='CATEGORY' and t.status=0 order by t.code";
		List<SysYwDicenumitem> list = (List<SysYwDicenumitem>)ProSocialRegist.findVoBySql(sql, SysYwDicenumitem.class);
		return list;
	}

}

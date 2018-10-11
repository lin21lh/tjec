package com.jbf.sys.regist.service;

import java.util.List;
import java.util.Map;

import com.jbf.base.dic.po.SysYwDiccodeitem;
import com.jbf.base.dic.po.SysYwDicenumitem;
import com.jbf.sys.regist.po.ProSocialRegist;

public interface SocialCapitalRegistServiceI {
	public void subRegistInfo(Map map) throws Exception;
	/**
	 * 查询已注册跟已通过列表
	 * @Title: getRegistEnableUserList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param  设定文件 
	 * @return List<ProSocialRegist> 返回类型 
	 * @throws
	 */
	public List<ProSocialRegist> getRegistEnableUserList() ;
	
	/**
	 * 
	 * 查询投资偏好的列表
	 * @Title: getPreferencesList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return List 返回类型 
	 * @throws
	 */
	public List<SysYwDiccodeitem> getPreferencesList();
	
	/**
	 * 查询社会资本所属行业
	 * @Title: getCategoryList 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return 设定文件 
	 * @return List<SysYwDicenumitem> 返回类型 
	 * @throws
	 */
	public List<SysYwDicenumitem> getCategoryList();
}

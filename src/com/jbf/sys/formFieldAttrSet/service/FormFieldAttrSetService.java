package com.jbf.sys.formFieldAttrSet.service;

import com.jbf.sys.formFieldAttrSet.po.SysFormFieldAttrSet;

public interface FormFieldAttrSetService {
	
	/**
	 * 获取表单某个字段属性的设置对象
	 * @Title: getDetail 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param wfkey 工作流标识 必须传入条件
	 * @param @param wfversion 工作流版本 如果为空 则查询当前工作流默认的设置
	 * @param @param wfnode 工作流节点 如果为空 则查询当前工作流版本默认的设置
	 * @param @param fieldcode 字段编码
	 * @param @return 设定文件 
	 * @return SysFormFieldAttrSet 返回类型 
	 * @throws
	 */
	public SysFormFieldAttrSet getDetail(String wfkey, Integer wfversion, String wfnode, String fieldcode);

	/**
	 * 
	 * @Title: add 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param formFieldAttrSet 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void add(SysFormFieldAttrSet formFieldAttrSet);
	
	/**
	 * 
	 * @Title: edit 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param formFieldAttrSet 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void edit(SysFormFieldAttrSet formFieldAttrSet);
	
	/**
	 * 
	 * @Title: delete 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param wfkey
	 * @param @param wfversion
	 * @param @param wfnode 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void delete(String wfkey, Integer wfversion, String wfnode);
}

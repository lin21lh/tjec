/************************************************************
 * 类名：SysFileManageService.java
 *
 * 类别：Service接口
 * 功能：附件管理服务接口
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-23  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.filemanage.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.base.filemanage.service.impl.UpLoadFile.UpLoadFileDTO;
import com.jbf.common.dao.EasyUITotalResult;


public interface SysFileManageService {

	/**
	 * 附件查询列表 分页
	 * @param keyid 业务数据ID
	 * @param elementcode 业务模块编码
	 * @param page 当前页
	 * @param rows 每页条数
	 * @return
	 */
	public EasyUITotalResult query(String keyid, String elementcode, Integer page, Integer rows) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	/**
	 *  查询附件
	 * @Title: queryFiles 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode
	 * @param @param keyid
	 * @param @param stepid
	 * @param @param showFileLength 要显示的文件名称长度
	 * @param @return 设定文件 
	 * @return List<SysFileManage> 返回类型 
	 */
	public List<Map<String, Object>> queryFiles(String elementcode, String keyid, String stepid,String showFileLength);
	/**
	 * 通过itemid查询附件
	 * @Title: queryFilesByItemid 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param itemid
	 * @param showFileLength
	 * @return 设定文件
	 */
	public List<Map<String, Object>> queryFilesByItemid(String itemid,String showFileLength);
	
	/**
	 * 获取附件详细信息
	 * @param itemid 附件ID
	 * @return 附件PO
	 */
	public SysFileManage get(Long itemid);
	
	/**
	 * 附件上传
	 * @param upLoadFiles 附件DTO
	 * @return 上传结果
	 */
	public Map<String, Object> add(List<UpLoadFileDTO> upLoadFiles);
	
	/**
	 * 附件删除
	 * @param itemidstr 附件ID
	 */
	public void delete(String itemidstr);
	
	/**
	 * 获取附件
	 * @Title: getFile 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param itemid
	 * @param @return 设定文件 
	 * @return InputStream 返回类型 
	 * @throws
	 */
	public InputStream getFile(Integer itemid);
	
	/**
	 * 拍照上传
	 * @param upLoadFiles 附件DTO
	 * @return 上传结果
	 */
	public Map<String, Object> addPotos(List<UpLoadFileDTO> upLoadFiles);
}

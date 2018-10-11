package com.wfzcx.ppms.discern.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
/**
 * 财政承受能力验证service
 * @ClassName: ProjectPrepareCzcsnlService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月21日 下午3:33:23
 */

public interface ProjectPrepareCzcsnlService {
	/**
	 * 项目查询
	 * @Title: queryProject 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport queryProject(Map<String, Object> map)  throws AppException;
	/**
	 * 查询是否已经录入了财政承受能力验证
	 * @Title: queryIsExistCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param dxpjid
	 * @param xmhj
	 * @param projectid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryIsExistCzcsnl(String dxpjid,String xmhj,String projectid) throws Exception;
	/**
	 * 财政承受能力保存提交
	 * @Title: czcsnlSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void czcsnlSave(Map<String, Object> param) throws Exception;
	/**
	 * 查询财政承受能力评价信息
	 * @Title: queryCzcsnlForm 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param czcsnlid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryCzcsnlForm(String czcsnlid) throws Exception;
	
	/**
	 * 财政预算情况查询
	 * @Title: financeQuery 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport financeQuery(Map map) throws AppException;
	/**
	 * 提交
	 * @Title: sendCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param czcsnlid
	 * @throws Exception 设定文件
	 */
	public void sendCzcsnl(String projectid,String czcsnlid)throws Exception;
	/**
	 * 撤回
	 * @Title: revokeCzcsnl 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param czcsnlid
	 * @throws Exception 设定文件
	 */
	public void revokeCzcsnl(String projectid,String czcsnlid)throws Exception;
}

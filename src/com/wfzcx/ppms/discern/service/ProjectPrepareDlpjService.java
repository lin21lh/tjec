package com.wfzcx.ppms.discern.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

/**
 * 定量评价service
 * @ClassName: ProjectPrepareDlpjService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2016年3月15日 上午9:40:50
 */

public interface ProjectPrepareDlpjService {
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
	 * 查询是否已经新增了定量分析
	 * @Title: queryIsExistDxpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List queryIsExistDlpj(String projectid,String xmhj) throws Exception;
	
	/**
	 * 查询第三方机构
	 * @Title: queryDsfJg 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return 设定文件
	 */
	public PaginationSupport queryDsfJg(Map map);
	/**
	 * 定量评价保存
	 * @Title: dlpjSave 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param param
	 * @throws Exception 设定文件
	 */
	public void dlpjSave(Map<String, Object> param) throws Exception;
	/**
	 * 从第三方机构库查询
	 * @Title: querythirdOrgan 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param dlpjid
	 * @return
	 * @throws Exception 设定文件
	 */
	public List querythirdOrgan(String dlpjid) throws Exception;
	
	/**
	 * 查询已录入的第三方机构
	 * @Title: thirdOrgQuery 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param map
	 * @return
	 * @throws AppException 设定文件
	 */
	public PaginationSupport thirdOrgQuery(Map map) throws AppException;
	/**
	 * 提交
	 * @Title: sendDlpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @throws Exception 设定文件
	 */
	public void sendDlpj(String projectid,String dlpjid)throws Exception;
	/**
	 * 撤回
	 * @Title: revokeDlpj 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param projectid
	 * @param dlpjid
	 * @throws Exception 设定文件
	 */
	public void revokeDlpj(String projectid,String dlpjid)throws Exception;
}

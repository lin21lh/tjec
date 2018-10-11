package com.jbf.sys.paramCfg.service;

import javax.servlet.http.HttpServletRequest;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.sys.paramCfg.po.SysParamCfg;

/**
 * 系统 参数配置管理Service接口
 * @ClassName: SysParamCfgService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:31:19
 */
public interface SysParamCfgService {

	/**
	 * 系统参数配置一览表
	 * @Title: query 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param admivcode 地区编码
	 * @param @param scenecode 子系统或模块编码
	 * @param @param paramcode 参数编码
	 * @param @param paramname 参数名称
	 * @param @param status 状态
	 * @param @param pageSize
	 * @param @param pageIndex
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport query(String admivcode, String scenecode, String paramcode, String paramname, String status, Integer pageSize, Integer pageIndex);
	
	/**
	 * 新增系统配置参数
	 * @Title: addParam 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @param sysParamCfg
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean addParam(HttpServletRequest request, SysParamCfg sysParamCfg);
	
	/**
	 * 修改系统配置参数
	 * @Title: editParam 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @param sysParamCfg
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean editParam(HttpServletRequest request, SysParamCfg sysParamCfg);
	
	/**
	 * 删除系统配置参数
	 * @Title: deleteParam 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param request
	 * @param @param paramid
	 * @param @return 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean deleteParam(HttpServletRequest request, Long paramid);
}

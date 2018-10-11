package com.wfzcx.fam.dataPermission;

import com.jbf.common.exception.AppException;
import com.jbf.sys.dept.po.SysDept;

/**
 * 账户管理数据权限过滤
 * @ClassName: FamDataPermissionFilter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年5月15日 上午9:36:03
 */
public interface FamDataPermissionFilter {
	/**
	 * 获取数据权限
	 * 数据权限优先模式
	 * 如果配置数据权限，则数据权限优先，
	 * 如果没有配置，则以默认权限，
	 * 如果默认权限没有，则以菜单设置的禁止优先或允许优先为准
	 * @Title: getConditionFilter 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param resourceid 菜单id
	 * @param tablecode 表名
	 * @param tableAlias 别名
	 * @return
	 * @throws AppException 设定文件
	 */
	public String getConditionFilter(Long resourceid, String tablecode, String tableAlias) throws AppException;
	
	/**
	 * 获取系统缺省数据权限
	 * @Title: getDefaultDataRightDetails 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param menuid
	 * @param @return
	 * @param @throws AppException 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String getDefaultDataRightDetails(Long menuid) throws AppException;
	
	/**
	 * 获取机构对象
	 * @Title: getDept 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param deptcode 机构编码
	 * @param @return 设定文件 
	 * @return SysDept 返回类型 
	 * @throws
	 */
	public SysDept getDept(String deptcode);
}

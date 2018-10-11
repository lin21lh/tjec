package com.wfzcx.fam.manage.dao;

import com.jbf.common.dao.IGenericDao;
import com.jbf.common.exception.AppException;
import com.wfzcx.fam.manage.po.FaAccountArchive;

/**
 * 
 * @ClassName: AccountRevokeDao 
 * @Description: TODO(档案表数据操作类) 
 * @author LiuJunBo
 * @date 2015-4-14 上午11:00:37
 */
public interface FaAccountArchiveDao extends IGenericDao<FaAccountArchive, Integer>{
	/**
	 *  add by XinPeng 2015年4月20日14:42:32
	 * @Title: updateFaAccountArchiveByItemid 
	 * @Description: TODO更新账户档案表，账户变更、账户撤销新增时使用
	 * @param @param itemid
	 * @param @param ischange
	 * @param @param application_id 设定文件 
	 * @return  返回类型 
	 * @throws
	 */
	public void updateFaAccountArchiveByItemid(int itemid,int ischange,int applicationId) throws AppException;

	/**
	 *  add by XinPeng 2015年4月20日14:42:32
	 * @Title: updateFaAccountArchiveByApplicationId 
	 * @Description: TODO更新档案表，账户变更、账户撤销删除时使用
	 * @param @param applicationId
	 * @param @throws AppException 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateFaAccountArchiveByApplicationId(String applicationIds) throws AppException;
}

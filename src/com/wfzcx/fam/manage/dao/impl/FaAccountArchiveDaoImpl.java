/**  
 * @Title: AccountRevokeDaoImpl.java  
 * @Package com.wfzcx.fam.manage.revoke.dao.impl  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-14 上午11:00:25  
 * @version V1.0  
 */ 
 
package com.wfzcx.fam.manage.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.manage.dao.FaAccountArchiveDao;
import com.wfzcx.fam.manage.po.FaAccountArchive;
import com.wfzcx.fam.manage.po.FaApplications;

/** 
 * @ClassName: AccountRevokeDaoImpl 
 * @Description: TODO(档案表数据操作类) 
 * @author LiuJunBo
 * @date 2015-4-14 上午11:00:25  
 */
@Scope("prototype")
@Repository
public class FaAccountArchiveDaoImpl extends GenericDao<FaAccountArchive, Integer> implements FaAccountArchiveDao{

	/**
	 * 
	 * @Title: updateFaAccountArchiveByItemid 
	 * @Description: TODO更新账户档案表，撤销时application_id不需要填写
	 * @param @param itemid
	 * @param @param ischange
	 * @param @param application_id 设定文件 
	 * @return  返回类型 
	 * @throws
	 */
	@Override
	public void updateFaAccountArchiveByItemid(int itemid, int ischange,int applicationId) throws AppException {
		//获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		DetachedCriteria dc = DetachedCriteria.forClass(FaAccountArchive.class);
		dc.add(Property.forName("itemid").eq(itemid));
		List<?> faAccountArchives = this.findByCriteria(dc);
		if(faAccountArchives.isEmpty()){
			throw new AppException("在FA_ACCOUNT_ARCHIVE表中未找到itemid为"+itemid+"的数据！");
		}
		FaAccountArchive fa = null;
		if(faAccountArchives.size()==1){
			fa = (FaAccountArchive)faAccountArchives.get(0);
			Integer ischangeOld = fa.getIschange();
			if (ischangeOld==1) {
				throw new AppException("该账户信息已被变更或注销，不能再次变更！");
			}
			fa.setIschange(ischange);
			fa.setApplicationId(applicationId);
			fa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			fa.setUpdateUser(user.getUserid().toString());
		}else{
			throw new AppException("更新FA_ACCOUNT_ARCHIVE表时出错！");
		}
		this.update(fa);
	}

	@Override
	public void updateFaAccountArchiveByApplicationId(String applicationId) throws AppException {
		//获取当前用户
		SysUser user = SecureUtil.getCurrentUser();
		String hql =" from FaAccountArchive where applicationId in("+applicationId+")";
		List<FaAccountArchive> list = (List<FaAccountArchive>) this.find(hql);
		for (int i = 0; i < list.size(); i++) {
			FaAccountArchive fa = list.get(i);
			fa.setIschange(0);
			fa.setApplicationId(null);
			fa.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			fa.setUpdateUser(user.getUserid().toString());
			this.update(fa);
		}
	}
	
}

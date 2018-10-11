/**  
 * @Title: AccountRevokeDaoImpl.java  
 * @Package com.wfzcx.fam.manage.revoke.dao.impl  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-14 上午11:00:25  
 * @version V1.0  
 */ 
 
package com.wfzcx.ppms.discern.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.discern.dao.PrepareReviewZbDao;
import com.wfzcx.ppms.discern.po.ProPszb;

@Scope("prototype")
@Repository
public class PrepareReviewZbDaoImpl extends GenericDao<ProPszb, Integer> implements PrepareReviewZbDao{

	
}

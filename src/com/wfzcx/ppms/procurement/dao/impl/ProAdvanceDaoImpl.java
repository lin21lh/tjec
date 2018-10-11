/**  
 * @Title: AccountRevokeDaoImpl.java  
 * @Package com.wfzcx.fam.manage.revoke.dao.impl  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-14 上午11:00:25  
 * @version V1.0  
 */ 
 
package com.wfzcx.ppms.procurement.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.procurement.dao.ProAdvanceDao;
import com.wfzcx.ppms.procurement.po.ProAdvanceResult;

/**
 * 
 * @ClassName: ProAdvanceDaoImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月21日 上午8:15:37
 */
@Scope("prototype")
@Repository
public class ProAdvanceDaoImpl extends GenericDao<ProAdvanceResult, Integer> implements ProAdvanceDao{

	
}

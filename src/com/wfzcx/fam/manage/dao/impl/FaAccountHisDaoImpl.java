/**  
 * @Title: FaAccountHisDaoImpl.java  
 * @Package com.wfzcx.fam.query.account.dao.impl  
 * @Description: TODO(用一句话描述该文件做什么)  
 * @author LiuJunBo  
 * @date 2015-4-23 下午02:48:55  
 * @version V1.0  
 */ 
 
 
package com.wfzcx.fam.manage.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.fam.manage.dao.FaAccountHisDao;
import com.wfzcx.fam.manage.po.FaAccountHis;

/** 
 * @ClassName: FaAccountHisDaoImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author LiuJunBo
 * @date 2015-4-23 下午02:48:55  
 */
@Scope("prototype")
@Repository
public class FaAccountHisDaoImpl extends GenericDao<FaAccountHis, Integer> implements FaAccountHisDao {

}

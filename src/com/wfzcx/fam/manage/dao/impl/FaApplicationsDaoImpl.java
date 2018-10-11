package com.wfzcx.fam.manage.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.fam.manage.dao.FaApplicationsDao;
import com.wfzcx.fam.manage.po.FaApplications;

/**
 * 账户申请、变更、撤销信息Dao实现类
 * @ClassName: AccountRegisterDaoImpl 
 * @Description: TODO(账户申请、变更、撤销信息 增删改操作) 
 * @author MaQingShuang
 * @date 2015年4月14日 上午11:42:15
 */
@Scope("prototype")
@Repository
public class FaApplicationsDaoImpl extends GenericDao<FaApplications, Integer> implements FaApplicationsDao {

}

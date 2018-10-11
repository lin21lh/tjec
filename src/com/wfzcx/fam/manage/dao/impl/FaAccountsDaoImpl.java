package com.wfzcx.fam.manage.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.fam.manage.dao.FaAccountsDao;
import com.wfzcx.fam.manage.po.FaAccounts;

/**
 * 账户信息Dao实现类
 * @ClassName: FaAccountsDaoImpl 
 * @Description: TODO(账户信息增删改操作) 
 * @author MaQingShuang
 * @date 2015年4月15日 下午3:57:35
 */
@Scope("prototype")
@Repository
public class FaAccountsDaoImpl extends GenericDao<FaAccounts, Integer> implements FaAccountsDao {
	
}

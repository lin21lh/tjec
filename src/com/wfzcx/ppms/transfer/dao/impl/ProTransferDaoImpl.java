package com.wfzcx.ppms.transfer.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.ppms.transfer.dao.ProTransferDao;
import com.wfzcx.ppms.transfer.po.ProTransfer;

/**
 * 
 * @ClassName: ProTransferDaoImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author XinPeng
 * @date 2015年9月24日16:24:21
 */
@Scope("prototype")
@Repository
public class ProTransferDaoImpl extends GenericDao<ProTransfer, Integer> implements ProTransferDao{

	
}

package com.wfzcx.aros.sqbl.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.wfzcx.aros.sqbl.po.ApplyRecordInfo;

/**
 * 申请笔录Dao
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Repository
public class ApplyRecordDao extends GenericDao<ApplyRecordInfo, String>{

}

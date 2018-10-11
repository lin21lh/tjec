/************************************************************
 * 类名：SysExcelImpLogDaoImpl
 *
 * 类别：DaoImpl
 * 功能：数据导入日志DaoImpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/

package com.jbf.base.excel.dao.impl;

import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.excel.dao.SysExcelImpLogDao;
import com.jbf.base.excel.po.SysExcelImpLog;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.impl.GenericDao;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Repository
public class SysExcelImpLogDaoImpl extends GenericDao<SysExcelImpLog, Long>
		implements SysExcelImpLogDao {

    @Override
    public PaginationSupport queryDataImplLog(Map<String, Object> param) {
        
        Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer
               .valueOf(param.get("rows").toString())
               : PaginationSupport.PAGESIZE;
       Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer
               .valueOf(param.get("page").toString()) : 1;
    
       DetachedCriteria dc = DetachedCriteria.forClass(SysExcelImpLog.class);
       dc.addOrder(Order.desc("impdate"));
        
       return this.findByCriteria(dc, pageSize, pageIndex);
    }
    
}

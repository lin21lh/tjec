/************************************************************
 * 类名：SysExcelImpCfgDaoImpl
 *
 * 类别：DaoImpl
 * 功能：数据导入配置DaoImpl
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-07-29  CFIT-PG     HYF         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/

package com.jbf.base.excel.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.base.excel.dao.SysExcelImpCfgDao;
import com.jbf.base.excel.po.SysExcelImpCfg;
import com.jbf.common.dao.impl.GenericDao;

@Scope("prototype")
@Repository
public class SysExcelImpCfgDaoImpl extends GenericDao<SysExcelImpCfg, Long>
		implements SysExcelImpCfgDao {

}

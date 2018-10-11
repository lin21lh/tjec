package com.jbf.sys.paramCfg.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.paramCfg.dao.SysParamCfgDao;
import com.jbf.sys.paramCfg.po.SysParamCfg;

/**
 * 系统参数配置管理Dao实现类
 * @ClassName: SysParamCfgDaoImpl 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月13日 下午2:30:51
 */
@Scope("prototype")
@Repository
public class SysParamCfgDaoImpl extends GenericDao<SysParamCfg, Long> implements SysParamCfgDao {


}

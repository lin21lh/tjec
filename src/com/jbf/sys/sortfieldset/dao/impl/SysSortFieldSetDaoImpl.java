package com.jbf.sys.sortfieldset.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.sortfieldset.dao.SysSortFieldSetDao;
import com.jbf.sys.sortfieldset.po.SysSortFieldSet;

@Scope("prototype")
@Repository
public class SysSortFieldSetDaoImpl extends GenericDao<SysSortFieldSet, Long> implements SysSortFieldSetDao {

}

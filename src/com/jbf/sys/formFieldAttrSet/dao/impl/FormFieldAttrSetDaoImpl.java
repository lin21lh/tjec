package com.jbf.sys.formFieldAttrSet.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jbf.common.dao.impl.GenericDao;
import com.jbf.sys.formFieldAttrSet.dao.FormFieldAttrSetDao;
import com.jbf.sys.formFieldAttrSet.po.SysFormFieldAttrSet;

@Scope("prototype")
@Repository
public class FormFieldAttrSetDaoImpl extends GenericDao<SysFormFieldAttrSet, Long>
		implements FormFieldAttrSetDao {
	
}

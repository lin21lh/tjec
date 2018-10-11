package com.jbf.sys.formFieldAttrSet.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.util.StringUtil;
import com.jbf.sys.formFieldAttrSet.dao.FormFieldAttrSetDao;
import com.jbf.sys.formFieldAttrSet.po.SysFormFieldAttrSet;
import com.jbf.sys.formFieldAttrSet.service.FormFieldAttrSetService;

@Scope("prototype")
@Service
public class FormFieldAttrSetServiceImpl implements FormFieldAttrSetService {
	
	@Autowired
	FormFieldAttrSetDao formfieldAttrSetDao;

	@Override
	public SysFormFieldAttrSet getDetail(String wfkey, Integer wfversion,
			String wfnode, String fieldcode) {
		// TODO Auto-generated method stub
		
		DetachedCriteria dc = DetachedCriteria.forClass(SysFormFieldAttrSet.class);
		dc.add(Property.forName("wfkey").eq(wfkey));
		if (wfversion != null) {			
			dc.add(Property.forName("wfversion").eq(wfversion));
		} else {
			dc.add(Property.forName("wfversion").isNull());
		}
		
		if (StringUtil.isNotBlank(wfnode)) {			
			dc.add(Property.forName("wfnode").eq(wfnode));
		} else {
			dc.add(Property.forName("wfnode").isNull());
		}
		
		dc.add(Property.forName("fieldcode").eq(fieldcode));
		List<SysFormFieldAttrSet> list = (List<SysFormFieldAttrSet>) formfieldAttrSetDao.findByCriteria(dc);
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void add(SysFormFieldAttrSet formFieldAttrSet) {
		// TODO Auto-generated method stub
		
		formfieldAttrSetDao.save(formFieldAttrSet);
	}

	@Override
	public void edit(SysFormFieldAttrSet formFieldAttrSet) {
		// TODO Auto-generated method stub
		formfieldAttrSetDao.update(formFieldAttrSet);
	}

	@Override
	public void delete(String wfkey, Integer wfversion, String wfnode) {
		// TODO Auto-generated method stub
		
	}

}

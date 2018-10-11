package com.jbf.sys.sortfieldset.component.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.jbf.common.util.StringUtil;
import com.jbf.sys.sortfieldset.component.SortFieldSetComponent;
import com.jbf.sys.sortfieldset.dao.SysSortFieldSetDao;
import com.jbf.sys.sortfieldset.po.SysSortFieldSet;

@Scope("prototype")
@Component
public class SortFieldSetComponentImpl implements SortFieldSetComponent {

	@Autowired
	SysSortFieldSetDao sortFieldSetDao;
	
	public String getSortSql(Long sortid) {
		SysSortFieldSet sortFieldSet = sortFieldSetDao.get(sortid);
		if (sortFieldSet == null || StringUtil.isBlank(sortFieldSet.getSortstr()))
			return "";
		
		String sortStr = replaceSortSql(sortFieldSet.getSortstr());
		
		return " order by " + sortStr;
	}
	
	public String replaceSortSql(String sortSql) {
		
		if (StringUtil.isBlank(sortSql))
			return "";
		
		return sortSql.replaceAll("*", " ").replaceAll("#", ", ");
	}
}

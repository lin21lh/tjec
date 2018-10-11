/************************************************************
 * 类名：ValidRuleServiceImpl.java
 *
 * 类别：Service实现类
 * 功能：规则校验服务实现类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.dao.SysDicElementValSetDao;
import com.jbf.base.dic.dto.CodeType;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.dic.service.ValidRuleService;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;

@Scope("prototype")
@Service
public class ValidRuleServiceImpl implements ValidRuleService {

	@Autowired
	SysDicElementValSetDao dao;
	@Autowired
	SysDicTableDao dicTableDao;
	@Autowired
	SysDicElementDao dicElementDao;
	
	public Integer validIsExist(String tablecode, String id, String value, String elementcode) throws SecurityException, ClassNotFoundException, NoSuchFieldException, AppException {
		
		SysDicTable dicTable = dicTableDao.getDicTable(tablecode.toUpperCase());
		String[] args = {tablecode};
		if (dicTable == null)
			throw new AppException("datatable.undefined", args);
		
		if (StringUtil.isBlank(dicTable.getKeycolumn()))
			throw new AppException("datatable.keycolumn.undefined", args);
		
		if (StringUtil.isBlank(dicTable.getCodecolumn()))
			throw new AppException("datatable.codecolumn.undefined", args);

		
		String where = dicTable.getCodecolumn() + "='" + value + "'";
		
		SqlVO sqlVO = dicElementDao.getTableInfo(tablecode);
		boolean pand = false;
		for (ColVO colVO : (List<ColVO>)sqlVO.getCols()) {
			if (colVO.getName().toLowerCase().equals("elementcode")) {
				pand = true;
				break;
			}
		}
		if (elementcode != null && elementcode.length() > 0 && pand) 
			where += " and elementcode='" + elementcode + "'";
		
		if (id != null && id.length() > 0)
			where += " and  " + dicTable.getKeycolumn() + "<>" + id;
			
		List<Object> list = dao.query(tablecode, where);
		
		if (list != null && list.size() > 0)
			return 1;
		else
			return 0;
	}
	public String validLevelElement(String elementcode, Integer currentlevel) {
		
		String msg = "";
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode);
		CodeType codeType = CodeType.getCodeType(dicElement.getCodetype());
		switch (codeType) {
		case NoneCode:
			break;
		case OrderCode:
			break;
		case LayerCode:
			String[] codeformat = dicElement.getCodeformat().split("-");
			if (currentlevel >= codeformat.length)
				msg = "数据项【" + dicElement.getElementcode() + "-" + dicElement.getElementname() + "】编码格式为：" + dicElement.getCodeformat() + "，不允许添加下级！";
			break;
		}
		return msg;
	}
	
	public ResultMsg validLayerCode(String value, Integer levelno, String elementcode) {
		
		SysDicElement dicElement = dicElementDao.getDicelement(elementcode.toUpperCase());
		String codeformat = dicElement.getCodeformat();
		
		CodeFormat cFormat = new CodeFormat(codeformat);
		
		return new ResultMsg(false, "");
	}
	
	public class CodeFormat {
		private Integer size;
		private List<Integer> list;
		
		public CodeFormat() {
			
		}
		
		public CodeFormat(String codeformat) {
			if (StringUtil.isBlank(codeformat)) {
				this.size = 0;
				this.list = null;
			}
			
			String [] formats = codeformat.split("-");
			this.size = formats.length;
			List<Integer> cfList = new ArrayList<Integer>(formats.length);
			for (Integer cfSize : cfList) {
				cfList.add(cfSize);
			}
			this.list = cfList;
		}
		
		public List<Integer> getList() {
			return list;
		}
		public void setList(List<Integer> list) {
			this.list = list;
		}
		
		public Integer getSize() {
			return size;
		}
		public void setSize(Integer size) {
			this.size = size;
		}
	}
}



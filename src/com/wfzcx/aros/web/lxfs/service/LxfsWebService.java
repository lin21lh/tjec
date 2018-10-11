package com.wfzcx.aros.web.lxfs.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Service("com.wfzcx.aros.web.lxfs.service.LxfsWebService")
public class LxfsWebService {
	@Autowired
	MapDataDaoI mapDataDao;
	/**
	 * 查询联系方式返回gridlist
	 * @param param
	 * @return
	 */
	public PaginationSupport queryLxfsList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.id,t.deptname,t.address,t.person,t.phone,t.xcoor,t.ycoor from b_contactbaseinfo t where 1=1 ");
		sql.append("  order by deptname desc ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	
}

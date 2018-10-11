/**
 * @Description: 文件下载service
 * @author 张田田
 * @date 2016-09-8 
 */
package com.wfzcx.aros.web.wjxz.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Service("com.wfzcx.aros.web.wjxz.service.FileDownService")
public class FileDownService
{
	@Autowired
	private MapDataDaoI mapDataDao;
	
	public PaginationSupport queryDoc(Map<String, Object> param)
	{
		String filename = (String)param.get("filename");
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuilder sql = new StringBuilder();
		sql.append("select t.itemid, t.filename, t.createtime from sys_filemanage t where t.elementcode = 'WJXZ'");
		if (StringUtils.isNotBlank(filename))
		{
			sql.append(" and filename like '%" + filename + "%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
}

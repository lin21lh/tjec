package com.wfzcx.ppms.query.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.ppms.query.service.ProjectCountService;

@Scope("prototype")
@Service("/query/service/impl/ProjectCountServiceImpl")
public class ProjectCountServiceImpl implements ProjectCountService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Override
	public PaginationSupport qryProCount(Map map) {
		
		Integer pageSize = StringUtil.isNotNull(map.get("rows")) ? Integer.valueOf(map.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(map.get("page")) ? Integer.valueOf(map.get("page").toString()) : 1;
		String name = map.get("name")==null?"":map.get("name").toString();
		String code = map.get("code")==null?"":map.get("code").toString();
		String startTime = map.get("startTime")==null?"":map.get("startTime").toString();
		String endTime = map.get("endTime")==null?"":map.get("endTime").toString();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T1.CODE,");
		sql.append("       T1.NAME,");
		sql.append("       COUNT(1) ZSL,");
		sql.append("       COUNT((SELECT 1");
		sql.append("               FROM PRO_PROJECT A");
		sql.append("              WHERE T.PROJECTID = A.PROJECTID");
		sql.append("                AND NOT EXISTS");
		sql.append("              (SELECT 1");
		sql.append("                       FROM PRO_DEVELOP B");
		sql.append("                      WHERE A.PROJECTID = B.PROJECTID))) XMSB,");
		sql.append("       COUNT((SELECT 1");
		sql.append("               FROM PRO_DEVELOP A");
		sql.append("              WHERE T.PROJECTID = A.PROJECTID");
		sql.append("                AND NOT EXISTS");
		sql.append("              (SELECT 1");
		sql.append("                       FROM PRO_ADVANCE_RESULT B");
		sql.append("                      WHERE A.PROJECTID = B.PROJECTID))) XMZB,");
		sql.append("       COUNT((SELECT 1");
		sql.append("               FROM PRO_ADVANCE_RESULT A");
		sql.append("              WHERE T.PROJECTID = A.PROJECTID");
		sql.append("                AND NOT EXISTS");
		sql.append("              (SELECT 1");
		sql.append("                       FROM PRO_IMPLEMENT B");
		sql.append("                      WHERE A.PROJECTID = B.PROJECTID))) XMCG,");
		sql.append("       COUNT((SELECT 1");
		sql.append("               FROM PRO_IMPLEMENT A");
		sql.append("              WHERE T.PROJECTID = A.PROJECTID");
		sql.append("                AND NOT EXISTS");
		sql.append("              (SELECT 1");
		sql.append("                       FROM PRO_TRANSFER B");
		sql.append("                      WHERE A.PROJECTID = B.PROJECTID))) XMZX,");
		sql.append("       COUNT((SELECT 1 FROM PRO_TRANSFER A WHERE T.PROJECTID = A.PROJECTID)) XMYJ");
		sql.append("  FROM PRO_PROJECT T, SYS_DEPT T1");
		sql.append(" WHERE T.ORGCODE = T1.CODE");
		SysUser user = SecureUtil.getCurrentUser();
		if(null!=user.getOrgcode()){
			if(!"100".equals(user.getOrgcode())){//如果为ppp中心则查询全部
				sql.append(" and t1.code = '"+user.getOrgcode()+"'");
			}
		}
		if(!"".equals(name)){
			sql.append(" and t1.name like '%"+name+"%'");
		}
		if(!"".equals(code)){
			sql.append(" and t1.code in ('"+code.replaceAll(",", "','")+"')");
		}
		if(!"".equals(startTime)){
			sql.append(" and t.createtime >= '"+startTime+"'");
		}
		if(!"".equals(endTime)){
			sql.append(" and t.createtime <= '"+endTime+"'");
		}
		sql.append(" GROUP BY T1.CODE, T1.NAME");

		System.out.println("【综合查询-项目查询sql：】"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

}

package com.wfzcx.aros.bztx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.common.web.ResultMsg;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.bztx.dao.StandBaseInfoDao;
import com.wfzcx.aros.bztx.po.StandBaseInfo;

/**
 * 标准体系service
 * @author zhaoxd
 *
 */
@Scope("prototype")
@Service("com.wfzcx.aros.bztx.service.StandBaseInfoService")
public class StandBaseInfoService {
	
	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private StandBaseInfoDao standBaseInfoDao;
	
	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryStandbaseInfo(Map<String, Object> param) {
		String standardName = (String) param.get("standardName");
		String stageType = (String) param.get("stageType");
		String sysType = (String) param.get("sysType");
		String starttime = (String) param.get("starttime");
		String endtime = (String) param.get("endtime");
		// b_standbaseinfo
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.STANDID, t.STANDARDNAME, t.STAGETYPE, t.SYSTYPE, t.REMARK, t.OPTTIME,");
		sql.append(" (select a.username from sys_user a where a.usercode = t.operator) operator ,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='BZTX_JDLX' and a.status=0  and a.code=t.STAGETYPE) stagetypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='BZTX_TXLB' and a.status=0  and a.code=t.SYSTYPE) systypename");
		sql.append(" from B_STANDBASEINFO t ");
		
		sql.append(" where 1=1 ");
		if(!StringUtils.isEmpty(standardName)){
			sql.append(" and standardName like '%").append(standardName).append("%'");
		}
		if(!StringUtils.isEmpty(stageType)){
			sql.append(" and stageType ='").append(stageType).append("'");
		}
		if(!StringUtils.isEmpty(sysType)){
			sql.append(" and sysType ='").append(sysType).append("'");
		}
		if(!StringUtils.isEmpty(starttime)){
			sql.append(" and opttime >= '").append(starttime).append("'");
		}
		if(!StringUtils.isEmpty(endtime)){
			sql.append(" and opttime <= '").append(endtime).append("'");
		}
		sql.append(" order by t.opttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	/**
	 * 保存信息
	 * @param param
	 */
	public ResultMsg save(Map<String, Object> param) {
		ResultMsg result = new ResultMsg(true, "");
		String standId = (String) param.get("standid");
		String standardName = (String) param.get("standardname");
		String stageType = (String) param.get("stagetype");
		String sysType = (String) param.get("systype");
		String remark = (String) param.get("remark");
		StandBaseInfo info = new StandBaseInfo();
		if(!StringUtils.isEmpty(standId)){
			info = standBaseInfoDao.get(standId);
		}
		info.setRemark(remark);
		info.setStageType(stageType);
		info.setSysType(sysType);
		info.setStandardName(standardName);
		if(StringUtils.isEmpty(standId)){
			SysUser user = SecureUtil.getCurrentUser();
			info.setOperator(user.getUsercode());
			info.setOpttime(DateUtil.getCurrentDateTime());
			standBaseInfoDao.save(info);
			result.setTitle("新增成功");
		}else{
			info.setStandId(standId);
			standBaseInfoDao.update(info);
			result.setTitle("修改成功");
		}
		return result;
		
	}

	/**
	 * 
	 * @param param
	 */
	public void delete(Map<String, Object> param) {
		String standId = (String) param.get("id");
		standBaseInfoDao.delete(standId);
	}

	public Map<String,String> detail(Map<String, Object> param) {
		//获取StringBuffer对象，用来拼接sql语句
		Map<String,String> map = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.STANDID, t.STANDARDNAME, t.STAGETYPE, t.SYSTYPE, t.REMARK, t.OPERATOR, t.OPTTIME,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='BZTX_JDLX' and a.status=0  and a.code=t.STAGETYPE) stagetypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='BZTX_TXLB' and a.status=0  and a.code=t.SYSTYPE) systypename");
		sql.append(" from B_STANDBASEINFO t ");
		String id = (String) param.get("standid");
		sql.append(" where standid='").append(id).append("'");
		List<Map<String,String>> result = mapDataDao.queryListBySQL(sql.toString());
		if(!CollectionUtils.isEmpty(result)){
			map = result.get(0);
		}
		return map;
	}

}

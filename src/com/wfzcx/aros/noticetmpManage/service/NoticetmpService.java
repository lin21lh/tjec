package com.wfzcx.aros.noticetmpManage.service;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.xzfy.dao.BCasenoticetempinfoDao;
import com.wfzcx.aros.xzfy.po.BCasenoticetempinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.noticetmpManage.service.NoticetmpService")
public class NoticetmpService
{
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	BCasenoticetempinfoDao noticetmpDao;
	
	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryNoticetmp(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.tempid, t.noticename, t.type, t.protype, t.nodeid, t.opttime, t.operator,")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'NOTICETMPTYPE' and a.status=0  and a.code = t.type) typemc, ")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'B_CASEBASEINFO_PROTYPE' and a.status=0  and a.code = t.protype) protypemc, ")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PRONODEBASEINFO_NODENAME' and a.status=0  and a.code = to_char(t.nodeid)) nodeidmc ")
			.append(" from b_casenoticetempinfo t where 1 = 1");
		
		// 文书模板名称
		String noticename = (String)param.get("noticename");
		if (StringUtils.isNotBlank(noticename))
		{
			sql.append(" and t.noticename like '%").append(noticename).append("%'");
		}
		
		// 流程类型
		String protype = (String)param.get("protype");
		if (StringUtils.isNotBlank(protype))
		{
			sql.append(" and t.protype = '").append(protype).append("'");
		}
		
		//开始时间
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		if (StringUtil.isNotBlank(starttime))
		{
			sql.append(" and substr(t.opttime,0,10) >= '").append(starttime).append("'");
		}
		
		//结束时间
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if (StringUtil.isNotBlank(endtime))
		{
			sql.append(" and substr(t.opttime,0,10) <= '").append(endtime).append("'");
		}
		
		sql.append(" order by opttime desc");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 保存
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveNoticetmp(Map<String, Object> param)
	{
		try {
			BCasenoticetempinfo noticetmp = new BCasenoticetempinfo();
			String tempid = (String)param.get("tempid");
			if (StringUtils.isBlank(tempid)) {
				// 创建人
				SysUser user = SecureUtil.getCurrentUser();
				noticetmp.setOperator(user.getUsername());
				// 创建时间
				noticetmp.setBuildtime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				param.remove("tempid");
			}
			else {
				noticetmp = noticetmpDao.get(tempid);
			}
			
			String nodeid = (String)param.get("nodeid");
			if (StringUtils.isBlank(nodeid)) {
				param.put("nodeid", BigDecimal.ZERO);
				BeanUtils.populate(noticetmp, param);
				noticetmp.setNodeid(null);
			}
			else {
				BeanUtils.populate(noticetmp, param);
			}
			// 操作时间
			noticetmp.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			
			noticetmpDao.saveOrUpdate(noticetmp);
			// 插入clob内容
			insertClobVal(noticetmp.getContents(), tempid);
		}
		catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentVal(String tempid)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select contents from b_casenoticetempinfo where tempid='").append(tempid).append("'");
		
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			Clob content = (Clob)list.get(0).get("contents");
			if (null != content) {
				xml = UtilTool.clobToString(content);
			}
		}
		return xml;
	}
	
	/**
	 * 删除
	 * 
	 * @param tempid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delNoticetmp(String tempid)
	{
		noticetmpDao.delete(tempid);
	}
	
	/**
	 * 插入内容
	 * 
	 * @param content
	 * @param conid
	 */
	private void insertClobVal(String content, String tempid)
	{
		if (StringUtil.isBlank(content)) {
			return;
		}
		// 将string字符串转成clob对象
		Clob clob = noticetmpDao.createClob(content);
		
		// 将clob对象更新到表中
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("contents", clob);
		temp.put("tempid", tempid);
		noticetmpDao.updateByMap(temp, "b_casenoticetempinfo");
	}
	
}

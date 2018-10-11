/**
 * @Description: 通知书打印service
 * @author 张田田
 * @date 2016-08-26 
 */
package com.wfzcx.aros.print.service;

import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.print.dao.NoticeDao;
import com.wfzcx.aros.print.po.NoticeContentInfo;
import com.wfzcx.aros.print.po.Noticebaseinfo;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.print.service.NoticeService")
public class NoticeService
{
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private CasebaseinfoDao casebaseinfoDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	
	/**
	 * 查询案件列表
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport queryXzfyList(Map<String, Object> param)
	{
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.caseid, t.csaecode, t.appname, t.defname, t.appdate, ")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype ")
			.append(" from B_CASEBASEINFO t")
			.append(" where 1=1");

		// 案件编号
		String casecode = StringUtil.stringConvert(param.get("casecode"));
		if (StringUtils.isNotBlank(casecode))
		{
			sql.append(" and t.csaecode like '%").append(casecode).append("%'");
		}
		
		// 申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (StringUtils.isNotBlank(appname))
		{
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		// 被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (StringUtils.isNotBlank(defname))
		{
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 查询通知书配置信息
	 * 
	 * @param doctype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryNoticeConfig(String doctype)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select t.protype, t.url, t.elementcode from pub_noticeconfig t where t.doctype ='").append(doctype).append("'");
		
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * 获取通知书预置信息
	 * 
	 * @param caseid
	 * @param property
	 * @param elementcode
	 * @return
	 */
	public NoticeContentInfo queryNoticeContentInfo(String caseid, String property, String elementcode)
	{
		// 查询案件信息
		Casebaseinfo casebaseinfo = queryCaseInfo(caseid);
		
		// 查询中止/终止事由
		Map<String, Object> cause = null;
		if (StringUtils.isNotBlank(elementcode))
		{
			cause = queryCause(caseid, elementcode, property);
		}
		
		// 第三方信息
		Map<String, Object> thirdinfo = queryThirdInfo(caseid);
		
		NoticeContentInfo info = getNoticeContentInfoBean(casebaseinfo, cause, thirdinfo);
		return info;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveNotice(Map<String, Object> param)
	{
		boolean flag = false;
		// 通知书类型
		String doctype  = (String) param.get("doctype");
		// 案件ID
		String caseid = (String) param.get("caseid");
		
		try
		{
			NoticeContentInfo content = new NoticeContentInfo();
			// mapToBean
			BeanUtils.populate(content, param);
			// 通知书内容
			String contents = UtilTool.objToXml(content);
			String noticeid = (String)param.get("noticeid");
			
			Noticebaseinfo info = queryNoticeInfo(noticeid);
			if (null != info)
			{
				// 通知书内容
				info.setContents(contents);
				// 更新通知书
				noticeDao.update(info);
				flag = true;
			}
			else
			{
				info = new Noticebaseinfo();
				// 案件ID
				info.setCaseid(caseid);
				// 通知书类型
				info.setDoctype(doctype);
				String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				// 生成时间
				info.setBuildtime(date);
				// 操作时间
				info.setOpttime(date);
				// 获取当前登录用户信息
				SysUser user = SecureUtil.getCurrentUser();
				info.setOperator(user.getUsername());
				
				// 通知书ID
				String noticeidTmp = (String) noticeDao.save(info);
				if (StringUtils.isNotBlank(noticeidTmp))
				{
					// 插入noticeid
					StringBuffer sBuffer = new StringBuffer(contents);
					int index = contents.indexOf("</noticeid>");
					sBuffer.insert(index, noticeidTmp);
					// 通知书内容
					info.setContents(sBuffer.toString());
					noticeDao.update(info);
					
					flag = true;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * 查询通知书类型名称
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDoctype(Map<String, Object> param)
	{
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.doctype, a.name")
			.append(" from PUB_NOTICECONFIG t, SYS_YW_DICENUMITEM a")
			.append(" where upper(a.elementcode) = 'DOCTYPE'")
			.append(" and t.doctype = a.code");
		
		// 流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		if (StringUtils.isNotBlank(protype))
		{
			sql.append(" and t.protype = '").append(protype).append("'");
		}
		
		// 节点编号
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		if (StringUtils.isNotBlank(nodeid))
		{
			sql.append(" and t.nodeid = '").append(nodeid).append("'");
		}
		
		// 处理结果
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isNotBlank(result))
		{
			sql.append(" and t.result = '").append(result).append("'");
		}
		
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * 查询通知书内容
	 * 
	 * @param caseid
	 * @param doctype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryNoticeContent(String caseid, String doctype)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select contents from b_noticebaseinfo where caseid='")
			.append(caseid) .append("' and doctype = '") .append(doctype) .append("'");
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			xml = UtilTool.clobToString((Clob)list.get(0).get("contents"));
		}
		return xml;
	}
	
	/**
	 * 查询案件基本信息
	 * 
	 * @param caseid
	 * @return
	 */
	private Casebaseinfo queryCaseInfo(String caseid)
	{
		return casebaseinfoDao.get(caseid);
	}
	
	/**
	 * 查询第三人信息
	 * 
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> queryThirdInfo(String caseid)
	{
		// 查询第三人姓名和地址
		StringBuffer sql = new StringBuffer();
		sql.append("select thname, thaddress from b_thirdbaseinfo where caseid='").append(caseid).append("'");
		
		Map<String, Object> map = null;
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * 查询中止或终止事由
	 * 
	 * @param caseid
	 * @param dataCode
	 * @param protype
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> queryCause(String caseid, String elementCode, String protype)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='")
			.append(elementCode).append("' and a.status=0 and a.code = t.reason) reason")
			.append(" from pub_probaseinfo t")
			.append(" where t.caseid = '")
			.append(caseid).append("'")
			.append(" and t.protype = '")
			.append(protype)
			.append("'")
			.append(" and t.opttype='0'");
		
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		Map<String, Object> map = null;
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			map = list.get(0);
		}
		return map;
	}
	
	/**
	 * 组装bean
	 * 
	 * @param casebaseinfo
	 * @param cause
	 * @param third
	 * @return
	 */
	private NoticeContentInfo getNoticeContentInfoBean(Casebaseinfo casebaseinfo, Map<String, Object> cause, Map<String, Object> third)
	{
		NoticeContentInfo info = new NoticeContentInfo();
		// 申请人
		info.setAppName(casebaseinfo.getAppname());
		// 申请人通讯地址
		info.setAppAddress(casebaseinfo.getMailaddress());
		
		// 01：公民 02：法人 03：其他组织
/*		String apptype = casebaseinfo.getApptype();
 		if ("01".equals(apptype))
		{
			
		}
		else
		{
			// 法人或其他组织
			info.setAppCorName(casebaseinfo.getAppname());
			// 法人或其他组织通讯地址
			info.setAppCorAddress(casebaseinfo.getAddress());
		}*/
		// 被申请人
		info.setDefName(casebaseinfo.getDefname());
		// 被申请人通讯地址
		info.setDefAddress(casebaseinfo.getDefmailaddress());
		// 申请事项
		info.setAppCase(casebaseinfo.getAppcase());
		// 申请日期
		String appdate = casebaseinfo.getAppdate().replace("-", "");
		info.setAppYear(appdate.substring(0,4));
		info.setAppMonth(appdate.substring(4,6));
		info.setAppDay(appdate.substring(6));
		if (null != third && !third.isEmpty())
		{
			// 第三人
			Object thname = third.get("thname");
			info.setThName(null == thname ? "" : (String)thname);
			// 第三人通讯地址
			Object thaddress = third.get("thaddress");
			info.setThAddress(null == thaddress ? "" :(String)thaddress);
		}
		
		if (null != cause && !cause.isEmpty())
		{
			// 中止或终止事由
			Object reason = cause.get("reason");
			info.setReason(null == reason ? "" :(String)cause.get("reason"));
		}
		
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		info.setSystemYear(date.substring(0,4));
		info.setSystemMonth(date.substring(4,6));
		info.setSystemDay(date.substring(6));
		
		return info;
	}
	
	/**
	 * 查询通知书基本信息
	 * 
	 * @param noticeid
	 * @return
	 */
	private Noticebaseinfo queryNoticeInfo(String noticeid)
	{
		return noticeDao.get(noticeid);
	}
}

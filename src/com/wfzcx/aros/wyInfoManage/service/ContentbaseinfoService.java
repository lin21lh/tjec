package com.wfzcx.aros.wyInfoManage.service;

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
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.wyInfoManage.dao.ContentbaseinfoDao;
import com.wfzcx.aros.wyInfoManage.dao.ContentuserrelainfoDao;
import com.wfzcx.aros.wyInfoManage.po.BContentbaseinfo;
import com.wfzcx.aros.wyInfoManage.po.BContentuserrelainfo;

@Scope("prototype")
@Service("aros.wyhzd.service.ContentbaseinfoService")
public class ContentbaseinfoService
{
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	ContentbaseinfoDao infoDao;
	
	@Autowired
	ContentuserrelainfoDao relaInfoDao;
	
	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryContent(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		// 类型
		String contype = (String)param.get("contype");
		StringBuffer sql = new StringBuffer();
		sql.append("select t.conid, t.title, t.opttime, t.operator, t.status, t.contype, t.type ,")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'STATUS' and a.status=0  and a.code = t.status) statusname, ")
			.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'CONTYPE' and a.status=0  and a.code = t.contype) contypename, ")
			.append(" (select replace(WMSYS.WM_CONCAT(u.username),',',',') from sys_user u, b_contentuserrelainfo b where u.userid=b.userid and b.conid = t.conid) receiveusername,")
			.append(" (select replace(WMSYS.WM_CONCAT(u.userid),',',',') from sys_user u, b_contentuserrelainfo b where u.userid=b.userid and b.conid = t.conid) receiveuserid ");
		if("02".equals(contype)){
			sql.append(",(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'FLFG_TYPE' and a.status=0  and a.code = t.type) typedesc");
		}else if("03".equals(contype)){
			sql.append(" ,(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'DXAL_TYPE' and a.status=0  and a.code = t.type) typedesc");
		}
		sql.append(" from b_contentbaseinfo t where t.contype = '").append(contype).append("'");
		
		// 状态
		String status = (String)param.get("status");
		if (StringUtils.isNotBlank(status)){
			sql.append(" and t.status = '").append(status).append("'");
		}
		
		// 内部类型
		String type = (String)param.get("type");
		if (StringUtils.isNotBlank(type)){
			sql.append(" and t.type = '").append(type).append("'");
		}
		
		// 标题
		String title = (String)param.get("title");
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
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
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 查询列表
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryByCurrentUser(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		// 当前用户
		SysUser user = SecureUtil.getCurrentUser();
		Long userid = user.getUserid();
		// 类型 01:新闻，02：法律法规，03：典型案例，04：委员会审议制度， 05：委员会工作守则
		String contype = (String)param.get("contype");
		String title = (String)param.get("title");
		
		sql.append("select t.conid, t.title, t.opttime, t.operator, t.contype")
			.append(" from b_contentbaseinfo t")
			.append(" where exists (select r.conid from b_contentuserrelainfo r")
			.append(" where r.userid = '").append(userid).append("')")
			.append(" and t.status = '02'")
			.append(" and t.contype = '").append(contype).append("'");
		// 标题
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
		}
		sql.append(" union all");
		sql.append(" select t.conid, t.title, t.opttime, t.operator, t.contype")
			.append(" from b_contentbaseinfo t")
			.append(" where not exists (select r.conid from b_contentuserrelainfo r)")
			.append(" and t.status = '02'")
			.append(" and t.contype = '").append(contype).append("'");
		// 标题
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 查询未选中接收人的所有列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryUnselectedUser(Map<String, Object> param) throws AppException
	{
		// 已经选择的人员
		String userid = (String)param.get("userid");
		StringBuffer sql = new StringBuffer();
		sql.append("select userid,")
			.append("       username,")
			.append("       usercode,")
			.append("       orgcode,")
			.append("       (select d.name from sys_dept d where d.code = u.orgcode) orgname")
			.append("  from sys_user u where u.status = 0 ");
		// 获取查询条件
		// 按编码/名字查询标识
	    String type = (String)param.get("type");
	    // 已选接收人
	    String selVal =(String)param.get("selVal");
		if (StringUtil.isNotBlank(type) && StringUtil.isNotBlank(selVal))
		{
			if("usercode".equals(type))
			{
				sql.append(" and upper(u.usercode) like upper('").append(selVal).append("%')");
			}
			else if("username".equals(type))
			{
				sql.append(" and u.username like '%").append(selVal).append("%'");
			}
		}
		//字符串 元素个数是否小于1000
		if (StringUtil.isNotBlank(userid))
		{
			userid = this.dealuserid(userid);
			sql.append(" and u.userid not in (").append(userid).append(")");
		}
		sql.append(" order by u.username");
		
		return mapDataDao.queryListBySQL(sql.toString());

	}
	
	/**
	 * 查询已选中的接收人列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySelectedUser(Map<String, Object> param)
			throws AppException
	{
		// 已经选择的人
		String userid = (String)param.get("userid");
		StringBuffer sql = new StringBuffer();
		sql.append("select userid,")
			.append("       username,")
			.append("       usercode,")
			.append("       orgcode,")
			.append("       (select d.name from sys_dept d where d.code =u.orgcode) orgname")
			.append("  from sys_user u where u.status = 0 ");
		if (StringUtil.isNotBlank(userid))
		{
			userid = this.dealuserid(userid);
			sql.append(" and u.userid in (").append(userid).append(")");
		}
		else
		{
			sql.append(" and 1 = 0");
		}
		sql.append(" order by u.username");
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * 保存
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveContent(Map<String, Object> param)
	{
		String flag = (String)param.get("flag");
		try
		{
			BContentbaseinfo contentInfo = new BContentbaseinfo();
			BeanUtils.populate(contentInfo, param);
			
			// 发布状态 01:保存，02：发布，03：撤消，04：作废
			String status = contentInfo.getStatus();
			// 如果是新增默认保存状态，更新保存原来的状态
			if (StringUtils.isBlank(status))
			{
				status = "01";
			}
			
			// 如果是发布状态是02
			if ("send".equals(flag))
			{
				status = "02";
			}
			
			// 状态赋值
			contentInfo.setStatus(status);
			String conid = contentInfo.getConid();
			if (StringUtils.isBlank(conid))
			{
				// 创建人
				SysUser user = SecureUtil.getCurrentUser();
				contentInfo.setOperator(user.getUsername());
				// 创建时间
				contentInfo.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				
				conid = (String)infoDao.save(contentInfo);
			}
			else
			{
				infoDao.update(contentInfo);
				// 先删除原来接收人对应关系数据，后新增接收人对应关系数据
				deleteUserRelaInfo(conid);
			}
			
			// 插入clob内容
			insertClobVal(contentInfo.getContent(), conid);
			
			// 新增接收人
			String userIds = contentInfo.getReceiveuserid();
			if (StringUtils.isNotBlank(userIds))
			{
				//根据接收者的人数插入对应的接收人记录
				String[] receives = userIds.split(",");
				for (int i=0; i<receives.length; i++)
				{
					saveReceive(conid, receives[i]);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentVal(String conid)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select content from b_contentbaseinfo where conid='").append(conid).append("'");
		
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			xml = UtilTool.clobToString((Clob)list.get(0).get("content"));
		}
		return xml;
	}
	
	/**
	 * 删除
	 * 
	 * @param conid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delContent(String conid)
	{
		infoDao.delete(conid);
		// 同时删除关系表数据
		deleteUserRelaInfo(conid);
	}
	
	/**
	 * 发布或撤销
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void changeStatus(String conid, String status)
	{
		BContentbaseinfo info = infoDao.get(conid);
		info.setStatus(status);
		infoDao.update(info);
	}
	
	/**
	 * 查询类型字典
	 */
	@SuppressWarnings("unchecked")
	public String queryContypeName(String contype)
	{
		StringBuilder sql = new StringBuilder();
		sql.append("select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'CONTYPE' and a.status=0  and a.code = '")
			.append(contype).append("'");
		List<Map<String, Object>> result = mapDataDao.queryListBySQL(sql.toString());
		String name = "";
		if (null != result && !result.isEmpty())
		{
			name = (String)result.get(0).get("name");
		}
		return name;
	}
	
	/**
	 * 保存接收人
	 * 
	 * @param conid
	 * @param userid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void saveReceive(String conid,String userid)
	{
		BContentuserrelainfo receiveBean = new BContentuserrelainfo();
		//conid
		receiveBean.setConid(conid);
		//接收人id
		receiveBean.setUserid(new BigDecimal(userid));
		//保存
		relaInfoDao.save(receiveBean);
	}
	
	/**
	 * 拼装接收人
	 * 
	 * @param olduserid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private String dealuserid(String userids)
	{
		StringBuilder newUserids = new StringBuilder();
		if (StringUtil.isNotBlank(userids))
		{
			String[] useridArr = userids.split(",");
			int length = useridArr.length;
			
			for (int i=0; i<length; i++)
			{
				String userid = useridArr[i];
				//拼接接收人
				if (i==0)
				{
					newUserids.append("'").append(userid);
				}
				else
				{
					newUserids.append("','").append(userid);
				}
				if (length-1 == i)
				{
					newUserids.append("'");
				}
				
			}
		}
		return newUserids.toString();
	}
	
	/**
	 * 插入内容
	 * 
	 * @param content
	 * @param conid
	 */
	private void insertClobVal(String content, String conid)
	{
		// 将string字符串转成clob对象
		Clob clob = infoDao.createClob(content);
		
		// 将clob对象更新到表中
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("content", clob);
		temp.put("conid", conid);
		infoDao.updateByMap(temp, "b_contentbaseinfo");
	}
		
	/**
	 * 删除原接收人
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void deleteUserRelaInfo(String conid)
	{
		relaInfoDao.deleteBySQL("b_contentuserrelainfo", " conid='" + conid +"'");
	}
	
	/**
	 * 查询内容基本信息
	 * @return
	 */
	public BContentbaseinfo queryInfo(String conid)
	{
		return infoDao.get(conid);
	}
	
	public String getTypeDic(String contype, String type){
		String result = "";
		if("02".equals(contype)){
			List<Map<String, Object>> list = mapDataDao.queryListBySQL("select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'FLFG_TYPE' and a.status=0  and a.code = '"+type+"'");
			if(!CollectionUtils.isEmpty(list)){
				result = (String) list.get(0).get("name");
			}
		} else if("03".equals(contype)){
			List<Map<String, Object>> list = mapDataDao.queryListBySQL("select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'DXAL_TYPE' and a.status=0  and a.code = '"+type+"'");
			if(!CollectionUtils.isEmpty(list)){
				result = (String) list.get(0).get("name");
			}
		}
		return result;
	}
}

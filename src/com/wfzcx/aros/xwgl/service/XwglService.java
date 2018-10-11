package com.wfzcx.aros.xwgl.service;

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

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.wyInfoManage.dao.ContentbaseinfoDao;
import com.wfzcx.aros.wyInfoManage.po.BContentbaseinfo;

@Scope("prototype")
@Service("aros.xwgl.service.XwglService")
public class XwglService {
	@Autowired
	MapDataDaoI mapDataDao;
	
	@Autowired
	ContentbaseinfoDao infoDao;
	
	
	
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryContent(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.conid, t.title, t.opttime, t.operator, t.status, t.contype,t.releasetime,t.newssources,t.newsauthor,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'STATUS' and a.status=0  and a.code = t.status) statusname, ");
		sql.append(" (select replace(WMSYS.WM_CONCAT(u.username),',',',') from sys_user u, b_contentuserrelainfo b where u.userid=b.userid and b.conid = t.conid) receiveusername,");
		sql.append(" (select replace(WMSYS.WM_CONCAT(u.userid),',',',') from sys_user u, b_contentuserrelainfo b where u.userid=b.userid and b.conid = t.conid) receiveusercid");
		
		// 类型
		String contype = (String)param.get("contype");
		sql.append(" from b_contentbaseinfo t where t.contype = '").append(contype).append("'");
		
		// 状态
		String status = (String)param.get("status");
		if (StringUtils.isNotBlank(status))
		{
			sql.append(" and t.status = '").append(status).append("'");
		}
		
		// 标题
		String title = (String)param.get("title");
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
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
		// 类型
		String contype = (String)param.get("contype");
		
		sql.append("select t.conid, t.title, t.opttime, t.operator, t.contype,t.releasetime,t.newssources,t.newsauthor,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'CONTYPE' and a.status=0  and a.code = t.contype) contypename");
		sql.append(" from b_contentbaseinfo t, b_contentuserrelainfo r");
		sql.append(" where t.conid = r.conid");
		sql.append(" and t.status = '02'");
		sql.append(" and contype = '").append(contype).append("'");// contype 01:新闻，02：法律法规，03：典型案例，04：委员会审议制度， 05：委员会工作守则
		sql.append(" and r.userid = '").append(userid).append("'");
		
		// 标题
		String title = (String)param.get("title");
		if (StringUtils.isNotBlank(title))
		{
			sql.append(" and t.title like '%").append(title).append("%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
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
				//判断是否上传了附件
				//上传，将案件ID更新至附件表的keyid中
				//未上传，不做处理
				String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
				String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
				List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
				if (fileList != null && !fileList.isEmpty())
				{
					String updFjKeySql = "update sys_filemanage t set t.keyid = '" + conid + "' where t.keyid = '" + fjkeyid + "'";
					infoDao.updateBySql(updFjKeySql);
				}
			
			}
			else
			{
				infoDao.update(contentInfo);

			}
			// 插入clob内容
			insertClobVal(contentInfo.getContent(), conid);
			
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
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String getClobContentVal(String conid){
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
	public void delContent(String conid){
		infoDao.delete(conid);
	}
	
	/**
	 * 发布或撤销
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void changeStatus(String conid, String status){
		BContentbaseinfo info = infoDao.get(conid);
		info.setStatus(status);
		infoDao.update(info);
	}
	
	
	
	/**
	 * 插入内容
	 * 
	 * @param content
	 * @param conid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void insertClobVal(String content, String conid){
		// 将string字符串转成clob对象
		Clob clob = infoDao.createClob(content);
		
		// 将clob对象更新到表中
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("content", clob);
		temp.put("conid", conid);
		infoDao.updateByMap(temp, "b_contentbaseinfo");
	}
	
	/**
	 * 查询内容基本信息
	 * @return
	 */
	public BContentbaseinfo queryInfo(String conid){
		return infoDao.get(conid);
	}

}

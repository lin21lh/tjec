package com.jbf.sys.notice.service.impl;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.notice.dao.ISysNoticeCommentDao;
import com.jbf.sys.notice.dao.ISysNoticeDao;
import com.jbf.sys.notice.dao.ISysNoticeReadDao;
import com.jbf.sys.notice.dao.ISysNoticeReceiveDao;
import com.jbf.sys.notice.po.SysNotice;
import com.jbf.sys.notice.po.SysNoticeComment;
import com.jbf.sys.notice.po.SysNoticeRead;
import com.jbf.sys.notice.po.SysNoticeReceive;
import com.jbf.sys.notice.service.ISysNoticeManageService;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.MessageComponent;

/**
 * 留言公告管理service处理类
 * @ClassName: ISysNoticeManageService 
 * @Description: (留言公告管理service处理类) 
 * @author songxiaojie
 * @date 2015年5月8日 
 */
@Scope("prototype")
@Service("com.jbf.sys.notice.service.impl.SysNoticeManageServiceImpl")
public class SysNoticeManageServiceImpl implements ISysNoticeManageService {
	
	@Autowired
	MapDataDaoI mapDataDao;//分页查询dao
	
	@Autowired
	ISysNoticeDao noticeDaoImpl;//留言公告dao
	
	@Autowired
	ISysNoticeReceiveDao receiveDao;//留言公告接收人dao
	
	@Autowired
	ISysNoticeReadDao readNoticeDao;//公告留言阅读处理dao
	
	@Autowired
	ISysNoticeCommentDao commentDao;//公告留言评论dao
	
	@Autowired
	FileManageComponent fileManageComponent;//上传附件

	@Autowired
	MessageComponent messageComponent;//发送短信
	/**
	 * 查询公告留言信息列表
	 */
	public PaginationSupport qrySendAllNotice(Map<String, Object> param)
			throws AppException {
		
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select noticeid,");
		sql.append("       title,");
		sql.append("       status,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESTATUS' and t.status=0  and t.code=n.status) statusname,");
		sql.append("(case when to_date(n.validtime, 'yyyy-mm-dd') >= trunc(sysdate) then (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status)");
		sql.append(" else (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = '4') end ) noticestatus,");
		sql.append("       releasetime,");
		sql.append("       validtime,");
		sql.append("       priorlevel,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=n.priorlevel) priorlevelname,");
		sql.append("       remark,");
		sql.append("       usercode,");
		sql.append("       (select u.username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("       messagetype,");
		sql.append("       commentopen,");
		sql.append("       readcount,");
		sql.append("       messageflag,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESENDMSG' and t.status=0  and t.code=n.messageflag) messageflagname,");
		sql.append("       createtime,");
		
		//获取数据库oracle版本
		Map<String,String> getVersion = this.getOracleVersion();
		String version = getVersion.get("version");
		if("10".equals(version))
		{
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusername,");
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusercode");
		}
		else if("11".equals(version))
		{
			sql.append(" (select listagg(t.username,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusername,");
			sql.append(" (select listagg(t.usercode,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusercode");
		}
		sql.append("  from sys_notice n where 1=1 ");
		String where ="";
		// 拼接查询条件
		//公告留言创建人
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		if(StringUtil.isNotBlank(usercode)){
			where +=" and usercode='"+usercode+"'";
		}
		//开始时间
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		//结束时间
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if(StringUtil.isNotBlank(starttime)){
			where +=" and substr(n.releasetime,0,10) >= '"+starttime+"'";
		}
		if(StringUtil.isNotBlank(endtime)){
			where +=" and substr(n.releasetime,0,10) <='"+endtime+"'";
		}
		//标题
		String title = StringUtil.stringConvert(param.get("title")).trim();
		if(StringUtil.isNotBlank(title)){
			where +=" and  title like '%"+title+"%'";
		}
		//公告留言类型
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  messageType ="+messageType;
		}
		//状态
		String status =StringUtil.stringConvert(param.get("status"));//4:待发送 1已发送
		String substatus = StringUtil.stringConvert(param.get("substatus"));//状态0保存，1发布，2撤销发布，3作废'
		if(StringUtil.isNotBlank(substatus) && StringUtil.isNotBlank(status)){
			if("4".equals(status))
			{
				if(!"5".equals(substatus))
				{
					where +=" and status = "+substatus;
				}
				else if("5".equals(substatus))
				{
					where +=" and status in ('0','2')";
				}
			}
			else if("1".equals(status))
			{
				where +=" and status = "+substatus;
			}
			
		}
		
		where += " order by n.createtime desc";
		sql.append(where);
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 获取服务器oracle版本
	 * @return
	 */
	private Map<String,String> getOracleVersion()
	{
		Map<String,String> rtnMap = new HashMap<String,String>();
		String sql = "select a.BANNER BANNER from v$version a";
		List<Map<String,Object>> versionList = mapDataDao.queryListBySQL(sql);
		//获取第三选项code
		Map<String, Object> versionMap = versionList.get(2);
		String value = versionMap.get("banner").toString();
		if(value.contains("11."))
		{
			rtnMap.put("version", "11");
		}
		else if(value.contains("10."))
		{
			rtnMap.put("version", "10");
		}
		
		return rtnMap;
	}
	
	
	/**
	 * 新增留言公告信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addNoticeMsg(SysNotice sysNotice,String itemids) throws Exception {
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//设置创建人编码
		sysNotice.setUsercode(user.getUsercode());
		//创建时间
		sysNotice.setCreatetime(DateUtil.getCurrentDateTime());
		//评论公开：是
		sysNotice.setCommentopen("1");
		//阅读次数
		sysNotice.setReadcount(0);
		//如果是发送操作，设置发送时间
		if("1".equals(sysNotice.getStatus()))
		{
			//发送时间
			sysNotice.setReleasetime(DateUtil.getCurrentDateTime());
		}
		
		//插入留言公告记录
		noticeDaoImpl.save(sysNotice);
		
		//插入clob内容
		insertClobVal(sysNotice.getContent(),sysNotice.getNoticeid());
		
		//保存附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(sysNotice.getNoticeid()), l);
		}
		
		//接收人
		String userCodes = sysNotice.getReceiveusercode();
		//如果状态为1发送，则在接收表添加信息的记录  
		if(StringUtils.isNotBlank(userCodes))
		{
			//根据接收者的人数插入对应的接收人记录
			String[] receiveAry = userCodes.split(",");
			for(int i=0;i<receiveAry.length;i++)
			{
				this.insertNoticeReceive(sysNotice.getNoticeid(),receiveAry[i]);
			}
			
		}
		
	}
	
	/**
	 * 发送短信
	 */
	public void sendMessage(String noticeId, String operType,String messageFlag,String messageType) {
		
		if("redo".equals(operType) && "2".equals(messageType))
		{
			//发送短信
			//将拼接的编码以“,”分割
			String[] noticeIdAry = noticeId.split(",");
			String[] msgFlags = messageFlag.split(",");
			
			//循环编码，发送留言公告
			for(int i=0;i<noticeIdAry.length;i++)
			{
				for(int j=0;j<msgFlags.length;j++)
				{
					String id = noticeIdAry[i];
					String msgFlag = msgFlags[j];
					
					if("1".equals(msgFlag))
					{
						messageComponent.saveMessageForNotice(id);
					}
				}
				
			}
		}
	}

	/**
	 * 创建留言公告接收人记录
	 * @param noticeId
	 * @param usercode
	 */
	public void insertNoticeReceive(Long noticeId,String usercode)
	{
		SysNoticeReceive receiveBean = new SysNoticeReceive();
		//留言公告编码
		receiveBean.setNoticeid(noticeId);
		//接收人编码
		receiveBean.setReceiveuser(usercode);
		//阅读标志
		receiveBean.setReadflag("0");//未阅读
		
		//保存记录
		receiveDao.save(receiveBean);
	}

	/**
	 * 插入公告留言内容
	 * 
	 * @param content
	 * @param noticeid
	 */
	public void insertClobVal(String content, Long noticeid) {
		
		// 将string字符串转成clob对象
		Clob clob = noticeDaoImpl.createClob(content);

		// 将clob对象更新到表中
		HashMap<String, Object> temp = new HashMap<String, Object>();
		temp.put("content", clob);
		temp.put("noticeid", noticeid);
		noticeDaoImpl.updateByMap(temp, "sys_notice");
	}

	/**
    * 发送选中的留言公告
    * @param noticeIds
    */
	public void sendNoticeMsgById(String noticeIds) {
		if(StringUtil.isNotBlank(noticeIds))
		{
			//将拼接的编码以“,”分割
			String[] noticeIdAry = noticeIds.split(",");
			
			//循环编码，发送留言公告
			for(int i=0;i<noticeIdAry.length;i++)
			{
				String noticeId = noticeIdAry[i];
				Long getId = Long.valueOf(noticeId);
				//根据编码获取留言公告信息
				SysNotice noticeBean = noticeDaoImpl.get(getId);
				
				//更新数据设置
				noticeBean.setStatus("1");//将原来status=0改为status=1
				//发送时间
				noticeBean.setReleasetime(DateUtil.getCurrentDateTime());
			
				//更新留言公告信息
				noticeDaoImpl.update(noticeBean);
			}
		}
	}

	/**
    * 删除选中的留言公告
    * @param noticeIds
    */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delNoticeMsgById(String delIds) {
		if(StringUtil.isNotBlank(delIds))
		{
			//获取删除编码列表
			List<Long> idList = this.getNoticeIdList(delIds);
			
			for(Long id : idList)
			{
				//根据编码删除留言公告主表信息
				noticeDaoImpl.delete(id);
				
 			    //删除留言公告接收人信息
				this.delNoticeReceive(id);
				//删除附件
				this.delFileByDelOper(Long.valueOf(id));
			}
		}
	} 
	
	/**
	 * 根据公告留言编码删除接收人信息
	 * @return
	 */
	public boolean delNoticeReceive(Long noticeId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("noticeid="+noticeId);
		return mapDataDao.delete("sys_notice_receive", sql.toString());
	}
	
	/**
	 * 删除的时候，删除上传附件
	 * @param noticeId
	 * @return
	 */
	public boolean delFileByDelOper(Long noticeId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("keyid="+noticeId);
		sql.append(" and elementcode='NOTICE'");
		return mapDataDao.delete("sys_filemanage", sql.toString());
	}

	/**
    *  修改公告留言时，将接收人记录全部删除，重新插入
    * @param noticeid
    */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealwithReceiveRecord(SysNotice sysnotice) {
		
		Long noticeId = Long.valueOf(sysnotice.getNoticeid());
		 //删除留言公告接收人信息
		this.delNoticeReceive(noticeId);
		
		//以“,”拼接的字符串
		String receiveUser = sysnotice.getReceiveusercode();
		
		if(StringUtil.isNotBlank(receiveUser))
		{
			String[] usercodes = receiveUser.split(",");
			
			for(int i=0;i<usercodes.length;i++)
			{
				this.insertNoticeReceive(noticeId, usercodes[i]);
			}
		}
	}
	
	/**
	 * 保存上传附件
	 */
	public void saveFile(String itemids,Long noticeid) {
		//保存附件
		if (itemids != null && !"".equals(itemids)) {
			List<Long> l = new ArrayList<Long>();
			String[] it = itemids.split(",");
			for (int i = 0; i < it.length; i++) {
				l.add(Long.valueOf(it[i]));
			}
			fileManageComponent.updateKeyid(
					String.valueOf(noticeid), l);
		}
	}

	/**
    *  更新留言公告信息
    * @param sysNotice
    * @param status
    */
	public void updateSysNotice(SysNotice sysNotice, String status) {

		//设置公告留言状态
		sysNotice.setStatus(status);
		
		//点击发送，设置发送时间
		if("1".equals(status))
		{
			sysNotice.setReleasetime(DateUtil.getCurrentDateTime());
		}
		//更新留言公告信息
		noticeDaoImpl.update(sysNotice);
	}

	/**
    *  撤回公告留言时，将接收人记录全部删除
    * @param noticeid
    */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String dealBackNoticeMsg(String backIds) {
		
		StringBuffer sb = new StringBuffer();
		if(StringUtil.isNotBlank(backIds))
		{
			List<Long> idList = this.getNoticeIdList(backIds);
			//循环撤销的留言公告
			for(Long id : idList)
			{	
				//查询阅读列表
				List noticeReadList = mapDataDao.queryList("sys_notice_read", "noticeid="+id);
				
				//根据编码查询留言公告主体信息
				SysNotice noticeBean = noticeDaoImpl.get(id);
				
				//公告发布不插入sys_notice_receive表，以sys_notice_read阅读表记录判断，是够进行过阅读
				if(noticeReadList.size() == 0)
				{
					//将公告主表信息中status设置为撤回，发布时间清空
					//将clob对象更新到表中
					HashMap<String,Object> temp = new HashMap<String,Object>();
					temp.put("status", 2);
					temp.put("releasetime", "");
					temp.put("noticeid", id);
					noticeDaoImpl.updateByMap(temp, "sys_notice");
					
					if(idList.size()==1)
					{
						sb.append("“"+noticeBean.getTitle()+"”撤回成功!</br>");
					}
					
				}
				else
				{
					sb.append("“"+noticeBean.getTitle()+"”已被阅读,不允许撤回!</br>");
				}
			}
		}
		else
		{
			sb.append("撤回失败！");
		}
		return sb.toString().trim();
	}
	
	
	/**
	 * 将“,”拼接起来的公告留言编码，放在list中
	 * @param noticeIds
	 * @return
	 */
	private List<Long> getNoticeIdList(String noticeIds)
	{
		ArrayList<Long> idList = new ArrayList<Long>();
		
		if(StringUtil.isNotBlank(noticeIds))
		{
			//将拼接的编码以“,”分割
			String[] idArray = noticeIds.split(",");
			
			//循环编码，发送留言公告
			for(int i=0;i<idArray.length;i++)
			{
				idList.add(Long.valueOf(idArray[i]));
			}
		}
		
		return idList;
		
	}

	/**
	 * 查询留言阅读列表
	 * @param param
	 * @return
	 */
	public PaginationSupport qryReadAllMessage(Map<String, Object> param)
			throws AppException {
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.noticeid noticeid,");
		sql.append("       n.title title,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status) status,");
		sql.append("       n.releasetime releasetime,");
		sql.append("       n.validtime validtime,");
		sql.append("       n.priorlevel priorlevel,");
		sql.append("       n.remark remark,");
		sql.append("       n.usercode usercode,");
		sql.append("       (select username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("       n.readcount readcount,");
		sql.append("       (case when (select count(*) from sys_notice_read t2 where t2.noticeid=n.noticeid and t2.readuser='"+usercode+"') >0 then '是' else '否' end) flag,");
		sql.append("       n.createtime createtime,");
		//获取数据库oracle版本
		Map<String,String> getVersion = this.getOracleVersion();
		String version = getVersion.get("version");
		if("10".equals(version))
		{
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusername,");
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusercode,");
		}
		else if("11".equals(version))
		{
			sql.append(" (select listagg(t.username,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusername,");
			sql.append(" (select listagg(t.usercode,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusercode,");
		}
		sql.append("       r.receiveid receiveid,");
		sql.append("       r.receiveuser receiveuser,");
		sql.append("       r.readflag readflag,");
		sql.append("       r.readtime readtime");
		sql.append("  from sys_notice n,sys_notice_receive r where 1=1 ");
		sql.append("  and n.noticeid = r.noticeid ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//公告留言创建人
		if(StringUtil.isNotBlank(usercode)){
			where +=" and r.receiveuser='"+usercode+"'";
		}
		//开始时间
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		//结束时间
		String endtime = StringUtil.stringConvert(param.get("endtime"));
//		if(StringUtil.isNotBlank(starttime)&& StringUtil.isNotBlank(endtime)){
//			where +=" and substr(n.releasetime,0,10) between '"+starttime+"' and '"+endtime+"'";
//		}
		if(StringUtil.isNotBlank(starttime)){
			where +=" and substr(n.releasetime,0,10) >='"+starttime+"'";
		}
		if(StringUtil.isNotBlank(endtime)){
			where +=" and substr(n.releasetime,0,10) <= '"+endtime+"'";
		}
		String title = StringUtil.stringConvert(param.get("title")).trim();
		if(StringUtil.isNotBlank(title)){
			where +=" and  n.title like '%"+title+"%'";
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		
		where += " order by n.createtime desc";
		sql.append(where);
//		System.err.println("qryReadAllMessage---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}
	
	/**
	 * 查询首页留言阅读列表
	 */
	public PaginationSupport qryReadAllMessageIndex(Map<String, Object> param)
			throws AppException {
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.noticeid noticeid,");
		sql.append("       n.title title,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status) status,");
		sql.append("       n.releasetime releasetime,");
		sql.append("       n.validtime validtime,");
		sql.append("       n.priorlevel priorlevel,");
		sql.append("       n.remark remark,");
		sql.append("       n.usercode usercode,");
		sql.append("       (select username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("       n.readcount readcount,");
		sql.append("       (case when (select count(*) from sys_notice_read t2 where t2.noticeid=n.noticeid and t2.readuser='"+usercode+"') >0 then '是' else '否' end) flag,");
		sql.append("       n.createtime createtime,");
		//判断oracle版本
		Map<String,String> versionMap = this.getOracleVersion();
		String version = versionMap.get("version");
		if("10".equals(version))
		{
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusername,");
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusercode,");
		}
		else if("11".equals(version))
		{
			sql.append(" (select listagg(t.username,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusername,");
			sql.append(" (select listagg(t.usercode,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusercode,");
		}
		sql.append("       r.receiveid receiveid,");
		sql.append("       r.receiveuser receiveuser,");
		sql.append("       r.readflag readflag,");
		sql.append("       r.readtime readtime");
		sql.append("  from sys_notice n,sys_notice_receive r where 1=1 ");
		sql.append("  and n.noticeid = r.noticeid ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//公告留言创建人
		if(StringUtil.isNotBlank(usercode)){
			where +=" and r.receiveuser='"+usercode+"'";
		}
		
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " and (select count(*) from sys_notice_read t2 where t2.noticeid=n.noticeid and t2.readuser='"+usercode+"') =0";
		
		where += " order by n.createtime desc";
		sql.append(where);
//		System.err.println("qryReadAllMessageIndex---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 查询首页留言和公告列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllMessageAndNoticeIndex(Map<String, Object> param) throws AppException {
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		/*留言代码*/
		StringBuffer message_sql = new StringBuffer();
		message_sql.append("select n.noticeid noticeid,");
		message_sql.append(" n.title indextitle,");
		message_sql.append("       n.title title,");
		message_sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status) status,");
		message_sql.append("       n.releasetime releasetime,");
		message_sql.append("       n.validtime validtime,");
		message_sql.append("       n.priorlevel priorlevel,");
		message_sql.append("       '' priorlevelname,");
		message_sql.append("       n.remark remark,");
		message_sql.append("       n.usercode usercode,");
		message_sql.append("       (select username from sys_user u where u.usercode=n.usercode) username,");
		message_sql.append("       n.readcount readcount,");
		message_sql.append("       (case when (select count(*) from sys_notice_read t2 where t2.noticeid=n.noticeid and t2.readuser='"+usercode+"') >0 then '是' else '否' end) flag,");
		message_sql.append("       n.createtime createtime,");
		message_sql.append(" n.messagetype,");
		//判断oracle版本
		Map<String,String> versionMap = this.getOracleVersion();
		String version = versionMap.get("version");
		if("10".equals(version))
		{
			message_sql.append(" (select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusername,");
			message_sql.append(" (select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusercode,");
		}
		else if("11".equals(version))
		{
			message_sql.append(" (select listagg(t.username,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusername,");
			message_sql.append(" (select listagg(t.usercode,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusercode,");
		}
		message_sql.append("       r.receiveid receiveid,");
		message_sql.append("       r.receiveuser receiveuser,");
		message_sql.append("       r.readflag readflag,");
		message_sql.append("       r.readtime readtime");
		message_sql.append("  from sys_notice n,sys_notice_receive r where 1=1 ");
		message_sql.append("  and n.noticeid = r.noticeid ");
		message_sql.append("  and n.status = 1 ");
		String message_where ="";
		// 拼接查询条件
		//公告留言创建人
		if(StringUtil.isNotBlank(usercode)){
			message_where +=" and r.receiveuser='"+usercode+"'";
		}
		message_where += " and n.messagetype =2";//消息类型
		message_where += " and (select count(*) from sys_notice_read t2 where t2.noticeid=n.noticeid and t2.readuser='"+usercode+"') =0";
//		message_where += " order by n.createtime desc";
		message_sql.append(message_where);
		
		/*公告代码*/
		String path = param.get("path").toString();
		StringBuffer notice_sql = new StringBuffer();
		notice_sql.append("select n.noticeid noticeid,");
		notice_sql.append("(case when(floor(sysdate-to_date(substr(n.releasetime,0,10),'yyyy-mm-dd'))) <=5 then concat(n.title,'<img src=\""+path+"/images/new0.gif\">') else n.title end) indextitle,");
		notice_sql.append("n.title title,");
		notice_sql.append("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status) status,");
		notice_sql.append("n.releasetime releasetime,");
		notice_sql.append("n.validtime validtime,");
		notice_sql.append("n.priorlevel priorlevel,");
		notice_sql.append("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=n.priorlevel) priorlevelname,");
		notice_sql.append("n.remark remark,");
		notice_sql.append("n.usercode usercode,");
		notice_sql.append("(select username from sys_user u where u.usercode=n.usercode) username,");
		notice_sql.append("n.readcount readcount,");
		notice_sql.append("'' flag,");
		notice_sql.append("n.createtime createtime,");
		notice_sql.append("n.messagetype,");
		notice_sql.append(" '' receiveusername,");
		notice_sql.append(" '' receiveusercode,");
		notice_sql.append(" null receiveid,");
		notice_sql.append(" '' receiveuser,");
		notice_sql.append(" '' readflag,");
		notice_sql.append(" '' readtime");
		notice_sql.append("  from sys_notice n where 1=1 ");
		notice_sql.append("  and n.status = 1 ");
		notice_sql.append("  and to_date(n.validtime,'YYYY-MM-DD') >= trunc(SYSDATE) ");
		String notice_where ="";
		// 拼接查询条件
		notice_where +=" and  n.messagetype =1";
		notice_where += " and (select count(*) from sys_notice_comment t2 where t2.noticeid=n.noticeid and t2.comment_user='"+usercode+"') =0";
//		notice_where += " order by n.releasetime desc";
		notice_sql.append(notice_where);
		String sql = "select * from (" + message_sql.toString() + " union " + notice_sql.toString() + ") order by releasetime desc";
		return mapDataDao.queryPageBySQLForConvert(sql, pageIndex, pageSize);
	}
	
	/**
	 * 查询未选中接收人的所有列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryUnselectedUser(Map<String, Object> param)
			throws AppException {
		
		String userCode = (String)param.get("userCode");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select userid,");
		sql.append("       username,"); 
		sql.append("       usercode,");
		sql.append("       orgcode,");
		sql.append("       (select d.name from sys_dept d where d.code =u.orgcode) orgname");
		sql.append("  from sys_user u where 1=1 ");
		sql.append("  and status =0 ");
		String where="";
		
	    //获取查询条件
	    String type = (String)param.get("type");
	    String selVal =(String)param.get("selVal");
		if(StringUtil.isNotBlank(type) && StringUtil.isNotBlank(selVal))
		{
			if("usercode".equals(type))
			{
				where += " and upper(u.usercode) like upper('"+ selVal+"%')";
			}
			else if("username".equals(type))
			{
				where += " and u.username like '%"+selVal+"%'";
			}
		}
		
		boolean pand = true; //字符串 元素个数是否小于1000
		
		if(StringUtil.isNotBlank(userCode)) {
			if (userCode.split(",").length > 1000) 
				pand = false;
			
			if (pand) {
				userCode = this.dealUserCode(userCode);
				where += " and u.usercode not in ("+userCode+")";
			} else { //传参
				where += " and not exists (select distinct column_value from table(CAST(SEND_SPLIT(?, ',') as mytable)) where u.usercode=column_value)";
			}
		}
		// in/not in (select distinct column_value from table(CAST(SEND_SPLIT(?, ',') as mytable) 
		// exists/ not exists (select distinct column_value from table(CAST(SEND_SPLIT(?, ',') as mytable) 主表.字段=column_value))
		where += " order by u.username";
		sql.append(where);
//		System.err.println("queryUnselectedUser---------------"+sql.toString());
		if (pand)
			 return mapDataDao.queryListBySQL(sql.toString());
		else
			return mapDataDao.queryListBySQLParam(sql.toString(), new Object[] {userCode});

	}
	
	/**
	 * 把usercode大于999的分开查询
	 * @param userCode
	 * @return
	 */
	private Map<String,String> splitUsercode(String userCode)
	{
		Map<String,String> map = new HashMap<String,String>();
		String[] usercodes = userCode.split(",");
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		if(usercodes.length > 9)
		{
			for(int i=0;i<9;i++)
			{
				if(i == 8)
				{
					sb1.append(usercodes[i]);
				}
				else
				{
					sb1.append(usercodes[i]).append(",");
				}
				
			}
			map.put("first", sb1.toString());
			
			
			for(int j=0;j<usercodes.length-9;j++)
			{
				if(j == usercodes.length-10)
				{
					sb2.append(usercodes[j+9]);
				}
				else
				{
					sb2.append(usercodes[j+9]).append(",");
				}
			}
			map.put("last",sb2.toString());
			return map;
		}
		else 
		{
			return null;
		}
	}
	
	
	private String dealUserCode(String oldUserCode)
	{
		String newCode="";
		if(StringUtil.isNotBlank(oldUserCode))
		{
			String[] oldCodeAry = oldUserCode.split(",");
			
			for(int i=0;i<oldCodeAry.length;i++)
			{
				String getVal = oldCodeAry[i];
				//拼接接收人编码
				if(i==0){
					newCode="'"+getVal;
				}
				else
				{
					newCode+="','"+getVal;
				}
				if(i==(oldCodeAry.length-1))
				{
					newCode +="'";
				}
				
			}
		}
		return newCode;
	}
	
	/**
	 * 查询已选中的接收人列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List querySelectedUser(Map<String, Object> param)
			throws AppException {
		String userCode = (String)param.get("userCode");
		
	  //  userCode = this.dealUserCode(userCode);
		StringBuffer sql = new StringBuffer();
		sql.append("select userid,");
		sql.append("       username,");
		sql.append("       usercode,");
		sql.append("       orgcode,");
		sql.append("       (select d.name from sys_dept d where d.code =u.orgcode) orgname");
		sql.append("  from sys_user u where 1=1 ");
		sql.append("  and status =0 ");
		String where="";
		
		boolean pand = true;
		if(StringUtil.isNotBlank(userCode)) {
			String[] usercodes = userCode.split(",");
			if (usercodes.length > 1000) 
				pand = false;
			
			if (pand) {
				userCode = this.dealUserCode(userCode);
				where += " and u.usercode in ("+userCode+")";
			} else {
				where += " and u.usercode in (select distinct column_value from table(CAST(SEND_SPLIT(?, ',') as mytable)))";
			}
		}
		else
		{
			where += " and 1=0";
		}
		where += " order by u.username";
		sql.append(where);
//		System.err.println("querySelectedUser---------------"+sql.toString());
		if (pand)
			 return mapDataDao.queryListBySQL(sql.toString());
		else
			return mapDataDao.queryListBySQLParam(sql.toString(), new Object[] {userCode});
	}

	/**
	 * 查询公告阅读列表
	 * @param param
	 * @return
	 */
	public PaginationSupport qryReadAllNotice(Map<String, Object> param)
			throws AppException {
		String path = param.get("path").toString();
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.noticeid noticeid,");
		sql.append("(case when(floor(sysdate-to_date(substr(n.releasetime,0,10),'yyyy-mm-dd'))) <=5 then concat(n.title,'<img src=\""+path+"/images/new0.gif\">') else n.title end) indextitle,");
		sql.append("n.title title,");
		sql.append("(case when to_date(n.validtime, 'yyyy-mm-dd') >= trunc(sysdate) then (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status)");
		sql.append(" else (select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = '4') end ) status,");
		sql.append("n.releasetime releasetime,");
		sql.append("n.validtime validtime,");
		sql.append("n.priorlevel priorlevel,");
		sql.append("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=n.priorlevel) priorlevelname,");
		sql.append("n.remark remark,");
		sql.append("n.usercode usercode,");
		sql.append("(select username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("n.readcount readcount,");
		sql.append("n.createtime createtime");
		sql.append("  from sys_notice n where 1=1 ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//开始时间
		String starttime = StringUtil.stringConvert(param.get("starttime"));
		//结束时间
		String endtime = StringUtil.stringConvert(param.get("endtime"));
		if(StringUtil.isNotBlank(starttime)){
			where +=" and substr(n.releasetime,0,10) >= '"+starttime+"'";
		}
		if(StringUtil.isNotBlank(endtime)){
			where +=" and substr(n.releasetime,0,10) <= '"+endtime+"'";
		}
		String title = StringUtil.stringConvert(param.get("title")).trim();
		if(StringUtil.isNotBlank(title)){
			where +=" and  n.title like '%"+title+"%'";
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " order by n.releasetime desc";
		sql.append(where);
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * 首页查询公告阅读列表
	 * @param param
	 * @return
	 */
	public PaginationSupport qryReadAllNoticeIndex(Map<String, Object> param)
			throws AppException {
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		String path = param.get("path").toString();
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.noticeid noticeid,");
		sql.append("(case when(floor(sysdate-to_date(substr(n.releasetime,0,10),'yyyy-mm-dd'))) <=5 then concat(n.title,'<img src=\""+path+"/images/new0.gif\">') else n.title end) indextitle,");
		sql.append("n.title title,");
		sql.append("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode) = 'NOTICESTATUS' and t.status = 0 and t.code = n.status) status,");
		sql.append("n.releasetime releasetime,");
		sql.append("n.validtime validtime,");
		sql.append("n.priorlevel priorlevel,");
		sql.append("(select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=n.priorlevel) priorlevelname,");
		sql.append("n.remark remark,");
		sql.append("n.usercode usercode,");
		sql.append("(select username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("n.readcount readcount,");
		sql.append("n.createtime createtime");
		sql.append("  from sys_notice n where 1=1 ");
		sql.append("  and n.status = 1 ");
		sql.append("  and to_date(n.validtime,'YYYY-MM-DD') >= trunc(SYSDATE) ");
		String where ="";
		// 拼接查询条件
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " and (select count(*) from sys_notice_comment t2 where t2.noticeid=n.noticeid and t2.comment_user='"+usercode+"') =0";
		where += " order by n.releasetime desc";
		sql.append(where);
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 查询公告阅读历史
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryhisNoticeList(Map<String, Object> param)
			throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select (select username from sys_user u where u.usercode=r.readuser) readuser,");
		sql.append("  n.title title,");
		sql.append("  r.readtime readtime");
		sql.append("  from sys_notice n,sys_notice_read r where 1=1 ");
		sql.append("  and r.noticeid = n.noticeid ");
		String where ="";
		// 拼接查询条件
		//公告留言创建人
		String noticeid = StringUtil.stringConvert(param.get("noticeid"));
		if(StringUtil.isNotBlank(noticeid)){
			where +=" and n.noticeid="+noticeid;
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));
		if(StringUtil.isNotBlank(messageType)){
			where +=" and n.messageType='"+messageType+"'";
		}
		where += " order by r.readtime desc";
		sql.append(where);
//		System.err.println("qryhisNoticeList ---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
		
	}

	/**
	 * 更新接收人表中的阅读标示
	 * @param readflag
	 * @param noticeid
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealReceiveNotice(String readflag, String noticeid) {
		
		 //获取当前登录用户
		SysUser user = SecureUtil.getCurrentUser();
		String userCode = user.getUsercode();
		//未阅读更新为已阅读
		if("0".equals(readflag))
		{
			StringBuffer sql = new StringBuffer();
			sql.append("update sys_notice_receive r set r.readflag='1',");
			sql.append("  r.readtime=sysdate");
			sql.append(" where r.noticeid="+noticeid);
			sql.append(" and r.receiveuser='"+userCode+"'");
//			System.err.println("updateReadFlag---------------"+sql.toString());
			mapDataDao.updateTX(sql.toString());
		}
		//添加阅读记录
		//当前系统时间
		String nowDate = DateUtil.getCurrentDateTime();
		
		SysNoticeRead noticeRead = new SysNoticeRead();
		noticeRead.setNoticeid(Long.valueOf(noticeid));
		noticeRead.setReadtime(nowDate);
		noticeRead.setReaduser(userCode);
		//保存阅读记录表
		readNoticeDao.save(noticeRead);
		
		
		//每次进行阅读，更新sys_notice表中的阅读次数字段
		//获取公告留言主表
		SysNotice sysNoticePo = noticeDaoImpl.get(Long.valueOf(noticeid));
		//阅读次数每次加1
		int readCount = sysNoticePo.getReadcount();
		readCount += 1;
		sysNoticePo.setReadcount(readCount);
		
		//更新到公告留言主表中
		noticeDaoImpl.update(sysNoticePo);
		
	}

	/**
	 * 处理公告留言阅读
	 * @param sysNotice
	 * @param noticeReceive
	 * @param noticeComment
	 * @param type 'common'反馈 'read'查阅
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dealReadNotice(Map<String, Object> map) {
		
		//在阅读记录表和评论表中添加新记录
		//当前系统时间
		String nowDate = DateUtil.getCurrentDateTime();
		String noticeId = (String) map.get("noticeid");
		
		 //获取当前登录用户
		SysUser user = SecureUtil.getCurrentUser();
		String userCode = user.getUsercode();
		
		//评论信息
		String commonCxt = (String) map.get("commentContent");
		SysNoticeComment noticeComment = new SysNoticeComment();
		//接收编码
		String receiveid = (String) map.get("receiveid");
		if(StringUtil.isNotBlank(receiveid))
		{
			noticeComment.setReceiveid(Long.valueOf(receiveid));
		}
		
		//判断反馈信息不为空，插入评论信息
		if(StringUtil.isNotBlank(commonCxt))
		{
			noticeComment.setCommentTime(nowDate);
			noticeComment.setCommentUser(userCode);
			noticeComment.setNoticeid(Long.valueOf(noticeId));
			noticeComment.setCommentContent(commonCxt);
			noticeComment.setReadFlag("0");//未阅读
			//保存评论记录表
			commentDao.save(noticeComment);
		}
		
	}


	/**
	 * 查询留言评论列表
	 * @param param
	 * @return
	 */
	public PaginationSupport qryCommonList(Map<String, Object> param)
			throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.title title,");
		sql.append("  c.comment_content commentContent,");
		sql.append("  (select username from sys_user u where u.usercode = c.comment_user) commentUserName,");
		sql.append("  c.comment_time commentTime");
		sql.append("  from sys_notice n,sys_notice_receive r,sys_notice_comment c where 1=1 ");
		sql.append("  and n.noticeid = r.noticeid ");
		sql.append("  and c.receiveid = r.receiveid ");
		sql.append("  and c.noticeid = n.noticeid ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//公告留言创建人
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		if(StringUtil.isNotBlank(usercode)){
			where +=" and r.receiveuser='"+usercode+"'";
		}
		//公告留言编码
		String noticeid = StringUtil.stringConvert(param.get("noticeid"));
		if(StringUtil.isNotBlank(noticeid)){
			where +=" and n.noticeid="+noticeid;
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " order by c.comment_time desc";
		sql.append(where);
//		System.err.println("qryCommonList ---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 查询自己评论别人的公告列表
	 * @param param
	 * @return
	 */
	public PaginationSupport showCommonBySelf(Map<String, Object> param)
			throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.title title,");
		sql.append("  c.comment_content commentContent,");
		sql.append("  (select u.username from sys_user u where u.usercode = c.comment_user) commentUserName,");
		sql.append("  c.comment_time commentTime");
		sql.append("  from sys_notice n,sys_notice_comment c where 1=1 ");
		sql.append("  and c.noticeid = n.noticeid ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//评论人
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		if(StringUtil.isNotBlank(usercode)){
			where +=" and c.comment_user='"+usercode+"'";
		}
		//公告留言编码
		String noticeid = StringUtil.stringConvert(param.get("noticeid"));
		if(StringUtil.isNotBlank(noticeid)){
			where +=" and n.noticeid="+noticeid;
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " order by c.comment_time desc";
		sql.append(where);
//		System.err.println("showCommonBySelf ---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 查询公告评论列表
	 * @param param
	 * @return
	 */
	public PaginationSupport qryNoticeCommonList(Map<String, Object> param)
			throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select n.title title,");
		sql.append("  c.comment_content commentContent,");
		sql.append("  (select username from sys_user u where u.usercode = c.comment_user) commentUserName,");
		sql.append("  c.comment_time commentTime");
		sql.append("  from sys_notice n,sys_notice_comment c where 1=1 ");
		sql.append("  and c.noticeid = n.noticeid ");
		sql.append("  and n.status = 1 ");
		String where ="";
		// 拼接查询条件
		//公告留言编码
		String noticeid = StringUtil.stringConvert(param.get("noticeid"));
		if(StringUtil.isNotBlank(noticeid)){
			where +=" and n.noticeid="+noticeid;
		}
		String messageType = StringUtil.stringConvert(param.get("messageType"));//类型：1：公告 2：留言
		if(StringUtil.isNotBlank(messageType)){
			where +=" and  n.messageType ="+messageType;
		}
		where += " order by c.comment_time desc";
		sql.append(where);
//		System.err.println("qryNoticeCommonList---------------"+sql.toString());
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 根据编码获取详细信息
	 */
	public SysNotice getSysNoticeBean(String noticeid) {
		return noticeDaoImpl.get(Long.valueOf(noticeid));
	}

	/**
	 * 获取clob字段内容
	 */
	public String getClobContentVal(String noticeid) {
		return mapDataDao.getContentClob("content", "noticeid", Integer.parseInt(noticeid), "sys_notice");
	}

	/**
	 * 判断是否上传附件
	 */
	@SuppressWarnings("unchecked")
	public int selectFileCount(String noticeid) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as count");
		sql.append(" from sys_filemanage t");
		sql.append(" where t.keyid ="+noticeid);
		sql.append(" and t.elementcode = 'NOTICE'");
//		System.err.println("selectFileCount---------------"+sql.toString());
		Map map = (Map) mapDataDao.queryListBySQL(sql.toString()).get(0);
		return Integer.valueOf(map.get("count").toString());
	}

	/**
	 * 判断创建留言人发送留言时是否发送给自己了
	 */
	@SuppressWarnings("unchecked")
	public int getMsgCountByMyself(String noticeId) {
		//获取当前登录用户编码
		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) as count");
		sql.append(" from sys_notice n ");
		sql.append(" where n.usercode ='"+user.getUsercode()+"'");
		sql.append(" and n.noticeid ="+noticeId);
//		System.err.println("getMsgCountByMyself---------------"+sql.toString());
		Map map = (Map) mapDataDao.queryListBySQL(sql.toString()).get(0);
		return Integer.valueOf(map.get("count").toString());
	}

	/**
	 * 更新评论表阅读标示为已阅读
	 */
	public void updateReadFlagByNoticeid(String noticeid) {
		StringBuffer sql = new StringBuffer();
		sql.append("update sys_notice_comment c set c.read_flag='1' where c.noticeid="+noticeid);
//		System.err.println("updateReadFlagByNoticeid---------------"+sql.toString());
		mapDataDao.updateTX(sql.toString());
	}

	/**
	 * 查询未阅读的评论列表
	 */
	@SuppressWarnings("unchecked")
	public String getCommonUnreadList(Map<String, Object> param) {
		
		StringBuffer sb = new StringBuffer();
		
		//获取登录用户编码
		String usercode = (String) param.get("usercode");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct n.noticeid noticeid,");
		sql.append("       n.title title,");
		sql.append("       n.messagetype messagetype,");
		sql.append(" 	   (select u.username from sys_user u where n.usercode=u.usercode) username,");
		sql.append("       (select u.username from sys_user u where c.comment_user=u.usercode) commentusername");
		sql.append(" from  sys_notice n, sys_notice_comment c ");
		sql.append(" where c.noticeid = n.noticeid ");
		sql.append("   and c.read_flag = '0'");
		sql.append("   and n.usercode ='"+usercode+"'");
		
//		System.err.println("getCommonUnreadList---------------"+sql.toString());
		//查询符合条件列表
		List getQryList =  mapDataDao.queryListBySQL(sql.toString());
		
		List noticeList = new ArrayList();
		List messageList = new ArrayList();
		//将公告留言分开
		for(int i=0;i<getQryList.size();i++)
		{
			Map castMap = (Map) getQryList.get(i);
			
			String messgeType = (String) castMap.get("messagetype");
			
			if("2".equals(messgeType))
			{		
				messageList.add(castMap);
			}
			else
			{
				noticeList.add(castMap);
				
			}
		}
		//拼接留言
		if(messageList.size() > 0)
		{
			sb.append("<span style='font-weight:bold;'>留言</span></br>");
			for(int n=0;n<messageList.size();n++)
			{
				Map msgMap = (Map)messageList.get(n);
				BigDecimal msgId = (BigDecimal) msgMap.get("noticeid");
				sb.append("<a href='#' class='unvisited' onClick='commentDetailIndex(this,2,"+msgId+")'>"+msgMap.get("commentusername")+"评论了标题为“"+msgMap.get("title")+"”的留言</a></br>");
			}
		}
		//拼接公告
		if(noticeList.size() > 0)
		{
			sb.append("<span style='font-weight:bold;'>公告</span></br>");
			for(int j=0;j<noticeList.size();j++)
			{
				Map noticeMap = (Map)noticeList.get(j);
				BigDecimal noticeIdVal = (BigDecimal) noticeMap.get("noticeid");
				sb.append("<a href='#' class='unvisited'  onClick='commentDetailIndex(this,1,"+noticeIdVal+")'>"+noticeMap.get("commentusername")+"评论了“"+noticeMap.get("title")+"”的公告</a></br>");
			}
		}
		return sb.toString();
	}

	/**
	 * 查询公告留言信息
	 */
	@SuppressWarnings("unchecked")
	public Map getNoticeInfo(Map<String, Object> param) {
		
		//编码
		String noticeId = (String) param.get("noticeid");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select noticeid,");
		sql.append("       title,");
		sql.append("       status,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESTATUS' and t.status=0  and t.code=n.status) statusname,");
		sql.append("       releasetime,");
		sql.append("       validtime,");
		sql.append("       priorlevel,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICELEVEL' and t.status=0  and t.code=n.priorlevel) priorlevelname,");
		sql.append("       remark,");
		sql.append("       usercode,");
		sql.append("       (select u.username from sys_user u where u.usercode=n.usercode) username,");
		sql.append("       messagetype,");
		sql.append("       commentopen,");
		sql.append("       readcount,");
		sql.append("       messageflag,");
		sql.append("       (select t.name from SYS_DICENUMITEM t where upper(t.elementcode)='NOTICESENDMSG' and t.status=0  and t.code=n.messageflag) messageflagname,");
		sql.append("       createtime,");
		//获取数据库oracle版本
		Map<String,String> getVersion = this.getOracleVersion();
		String version = getVersion.get("version");
		if("10".equals(version))
		{
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.username),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusername,");
			sql.append(" (select replace(WMSYS.WM_CONCAT(t.usercode),',',',') from sys_user t,sys_notice_receive r where t.usercode=r.receiveuser and r.noticeid=n.noticeid) receiveusercode");
		}
		else if("11".equals(version))
		{
			sql.append(" (select listagg(t.username,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusername,");
			sql.append(" (select listagg(t.usercode,',') within group ( ORDER  BY t.username) as aa from sys_user t,sys_notice_receive r where t.usercode = r.receiveuser and r.noticeid=n.noticeid ) receiveusercode");
		}
		sql.append("  from sys_notice n where n.noticeid= "+noticeId);
		
		List getQryList =  mapDataDao.queryListBySQL(sql.toString());
		return (Map) (getQryList.size()>0?getQryList.get(0):new HashMap());
	}

	/**
	 * 修改留言公告信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editNoticeMsg(SysNotice sysnotice, String status, String itemids) {
		//操作接收人记录表
		this.dealwithReceiveRecord(sysnotice);
		//更新公告留言主表信息
		this.updateSysNotice(sysnotice, status);
		//保存附件
		this.saveFile(itemids, sysnotice.getNoticeid());
	}
	
	
}

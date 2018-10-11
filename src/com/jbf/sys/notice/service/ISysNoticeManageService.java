package com.jbf.sys.notice.service;

import java.util.List;
import java.util.Map;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.sys.notice.po.SysNotice;

/**
 * 留言公告管理service接口类
 * @ClassName: ISysNoticeManageService 
 * @Description: (留言公告管理service接口类) 
 * @author songxiaojie
 * @date 2015年5月8日 
 */
public interface ISysNoticeManageService {

	/**
	 * @Title: qrySendAllNotice 
	 * @Description: 查询已发送的公告留言
	 * @param @param param
	 * @param @return 设定文件 
	 * @return PaginationSupport 返回类型 
	 * @throws
	 */
	public PaginationSupport qrySendAllNotice(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询留言列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllMessage(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询留言列表首页
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllMessageIndex(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询留言公告列表首页
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllMessageAndNoticeIndex(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询公告列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllNotice(Map<String, Object> param) throws AppException;
	
	
	/**
	 * 首页查询公告列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryReadAllNoticeIndex(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询留言评论列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryCommonList(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询自己评论别人的公告列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport showCommonBySelf(Map<String, Object> param) throws AppException;
	/**
	 * 查询公告评论列表
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryNoticeCommonList(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询公告阅读历史
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport qryhisNoticeList(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询未选中的所有接收人
	 * @return
	 * @throws AppException
	 */
	public List queryUnselectedUser(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询已选中的接收人
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public List querySelectedUser(Map<String, Object> param) throws AppException;
	
	
	/**
	 * 添加留言公告
	 * @param sysNotice
	 * @param noticeReceive
	 */
	public void addNoticeMsg(SysNotice sysNotice,String itemids)throws Exception;
	
	/**
	 * 发送短信
	 * @param noticeid
	 * @param opertype
	 */
	public void sendMessage(String noticeId,String operType,String messageFlag,String messageType);
	
	/**
	 * 获取选中留言公告的编码发送留言公告
	 * @param noticeIds
	 * @throws Exception
	 */
	public void sendNoticeMsgById(String noticeIds) throws Exception;
	
	/**
	 * 根据公告留言编码，删除留言公告主表以及 留言公告接收表
	 * @param delIds
	 */
	public void delNoticeMsgById(String delIds);
	
	/**
	 * 修改公告留言时，将接收人记录全部删除，重新插入
	 * @param noticeid
	 */
	public void dealwithReceiveRecord(SysNotice sysnotice);
	
	/**
	 * 更新留言公告信息
	 * @param status
	 */
	public void updateSysNotice(SysNotice sysNotice,String status);
	
	/**
	 * 处理撤回的留言公告
	 * @param backIds
	 */
	public String dealBackNoticeMsg(String backIds);
	
	/**
	 * 处理公告留言阅读
	 * @param noticeReceive
	 * @param noticeComment
	 * @return
	 */
	public void dealReadNotice(Map<String, Object> map);
	

	/**
	 * 根据公告编码查询详细信息
	 * @param noticeid
	 * @return
	 */
	public SysNotice getSysNoticeBean(String noticeid);
	
	/**
	 * 获取clob字段内容
	 * @param noticeid
	 * @return
	 */
	public String getClobContentVal(String noticeid);
	
	/**
	 * 保存文件
	 * @param itemids
	 */
	public void saveFile(String itemids,Long noticeid);
	
	/**
	 * 判断是否上传附件
	 * @param noticeid
	 * @return
	 */
	public int selectFileCount(String noticeid);
	
	/**
	 * 删除的时候，删除上传附件
	 * @param noticeId
	 * @return
	 */
	public boolean delFileByDelOper(Long noticeId);
	
	/**
	 * 判断创建留言人发送留言时是否发送给自己了
	 * @param noticeId
	 * @return
	 */
	public int getMsgCountByMyself(String noticeId);
	
	/**
	 * 更新接收人表中的阅读标示
	 * @param readflag
	 * @param noticeid
	 * @return
	 */
	public void dealReceiveNotice(String readflag,String noticeid);
	
	/**
	 * 更新评论表中的阅读标示为已阅读
	 * @param noticeid
	 */
	public void updateReadFlagByNoticeid(String noticeid);

	/**
	 * 查询未阅读的反馈列表
	 * @param param
	 * @return
	 */
	public String getCommonUnreadList(Map<String, Object> param);
	
	/**
	 * 查询留言公告详情
	 * @param param
	 * @return
	 */
	public Map getNoticeInfo(Map<String, Object> param);
	
	/**
	 * 修改留言公告
	 * @param sysnotice
	 * @param status
	 * @param itemids
	 */
	public void editNoticeMsg(SysNotice sysnotice,String status,String itemids);
}

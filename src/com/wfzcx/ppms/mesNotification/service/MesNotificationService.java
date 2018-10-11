package com.wfzcx.ppms.mesNotification.service;

import java.util.List;
import java.util.Map;

import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;

public interface MesNotificationService {

	/**
	 * 消息查询
	 * @param map
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryMessage(Map<String, Object> map) throws AppException;
	
	/**
	 * 消息添加
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String messageAddCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 消息修改
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String messageEditCommit(Map<String, Object> param) throws Exception;
	
	/**
	 * 消息删除
	 * @param messageid
	 * @throws AppException
	 */
	public void messageDelete(String messageid) throws AppException;
	
	/**
	 * 消息发送
	 * @param messageid
	 * @throws AppException
	 */
	public void messageSend(String messageid) throws AppException;
	
	/**
	 * 查询未选中的所有接收人
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List queryUnselectedUser(Map<String, Object> param) throws AppException;
	
	/**
	 * 查询已选中的接收人
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List querySelectedUser(Map<String, Object> param) throws AppException;
	
	
}

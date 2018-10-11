package com.wfzcx.fam.common;

import java.util.List;


/**
 * 
 * @ClassName: MessageComponent 
 * @Description: TODO消息公用方法
 * @author XinPeng
 * @date 2015年4月25日23:12:53
 */
public interface MessageComponent {
	
	/**
	 * 消息保存并发送
	 * 不要跟业务放在同一个事务里，否则消息无法实时发送
	 * @Title: saveMessage 
	 * @Description: TODO消息保存并发送，至返回保存成功的消息条数，消息是否发送成功待消息中心返回后更新
	 * @param @param applicationId
	 * @param @param phone
	 * @param @param message
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int saveMessage(Integer applicationId,String phone,String message,String activityId);
	/**
	 * 
	 * @Title: sendMessage 
	 * @Description: TODO消息发送
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void sendMessage(List zhList) throws Exception;
	public Configuration getConfiguration(); 
	/**
	 * 留言发送短信
	 * @Title: saveMessageForNotice 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param noticeId
	 * @return 设定文件
	 */
	public int saveMessageForNotice(String noticeId) ;
	/**
	 * 待发送的消息
	 * @Title: getSendMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return 设定文件
	 */
	public List getSendMessage();
}

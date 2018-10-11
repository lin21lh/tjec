package com.wfzcx.fam.common;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.freework.msg.socket.client.SendConnPool;
import com.freework.msg.socket.client.SendRequsetMsg;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.manage.dao.FaMessageDao;
import com.wfzcx.fam.manage.po.FaMessage;
/**
 * 
 * @ClassName: MessageComponent 
 * @Description: TODO消息公用方法
 * @author XinPeng
 * @date 2015年4月25日23:12:53
 */
@Scope("prototype")
@Component
@Repository("com.wfzcx.fam.common.MessageComponent")
public class MessageComponentImpl implements MessageComponent{
	@Autowired
    FaMessageDao faMessageDao;
	@Autowired
	MapDataDaoI mapDataDao;
	
	//初始化消息中心
		static {
			Configuration prop =getProperties();
			String	centerIp = prop.getValue("centerIp");   
			int	centerPort = Integer.parseInt(prop.getValue("centerPort"));   
			String	appId = prop.getValue("appId");  
			String	userCode = prop.getValue("userCode");   
			long sendtimeout =Long.parseLong(prop.getValue("sendtimeout"));
		    int connsize = Integer.parseInt(prop.getValue("connsize"));   ;
			int gettimeout = Integer.parseInt(prop.getValue("gettimeout"));
			int idletime = Integer.parseInt(prop.getValue("idletime"));
			SendConnPool.init(sendtimeout, centerIp, centerPort, appId, userCode, connsize, gettimeout, idletime);
		}
	    //读取配置文件
	    static Configuration getProperties() {
	    	Configuration rc = new Configuration("messageClient.properties");
	        return rc;
	    }
	  public Configuration getConfiguration(){
		  return getProperties();
	  }
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
	public int saveMessage(Integer applicationId,String phone,String message,String activityId) {
		FaMessage faMessage = new FaMessage();
		faMessage.setApplicationId(applicationId);
		faMessage.setMessage(message);
		faMessage.setPhone(phone);
		faMessage.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		SysUser user = SecureUtil.getCurrentUser();
		faMessage.setCreateUser(user.getUserid().toString());
		faMessage.setSendflag("0");
		faMessage.setActivityId(activityId);
		//faMessage.setBackflag("0");
		//保存消息
		final Integer a = (Integer) faMessageDao.save(faMessage);
		//消息保存成功后，启用线程发送消息
		Thread aThread=	new Thread(){
			   public void run(){
				   try {
					   String sql = "select itemid,phone,message msg from fa_message t where t.sendflag in(0,2)"
					   		+ " and (t.backflag<>0 or t.backflag is null) and t.itemid ='"+a+"'";
						List zhList = faMessageDao.findMapBySql(sql);
						sendMessage(zhList);
					} catch (Exception e) {
						e.printStackTrace();
					}
			   }
			};
			aThread.start();
		return a;	
	}
	/**
	 * 
	 * @Title: sendMessage 
	 * @Description: TODO消息发送
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void sendMessage(List zhList) throws Exception {
		SendRequsetMsg req = null;
		Configuration prop = getProperties();
		int messagesize = Integer.parseInt(prop.getValue("messagesize"));
		String smsUserCode =prop.getValue("smsUserCode");  
		String smsAppId = prop.getValue("smsAppId");
		String dxsystem = prop.getValue("dxsystem");
		if(zhList.isEmpty()){
			System.out.println("没有要发送的消息！");
			return ;
		}
		try {
			req = SendConnPool.get();
			List splitList = new ArrayList();
			for (int i = 1; i <=zhList.size(); i++) {
				//按多少条一次发送messagesize从配置文件中读取
				if(i%messagesize==0||i==zhList.size()){
					splitList.add(zhList.get(i-1));
					//将要发送的消息转换为json
					String messageJson =JSONArray.toJSON(splitList).toString();
					String itemids = returnItemids(splitList);
					try {
						//先将消息状态改为已发送消息中心
						String updateSql ="update fa_message set sendflag=3 ,SENDTIME=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss')   where itemid in("+itemids+")";
						mapDataDao.updateTX(updateSql);
						System.out.println(new java.util.Date()+"-----往消息中心发送消息----共计---"+splitList.size()+"条。");
						System.out.println("-----发送内容为----"+messageJson);
						//发送消息
						req.dstUserApp(smsAppId,smsUserCode, dxsystem, messageJson.getBytes("UTF-8"));
					} catch (Exception e) {
						String errorMsg = e.getMessage();
						//异常发送失败
						String errorSql ="update fa_message set sendflag=2 ,SENDTIME=to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') ,failurecause = '"+errorMsg+"'  where  itemid in("+itemids+")";
						mapDataDao.updateTX(errorSql);
					}
					splitList.clear();
				}else{
					splitList.add(zhList.get(i-1));
				}
			}
		} catch (Throwable  e1) {
			throw new Exception("消息服务器连接失败！");
		} finally{
			try {
				SendConnPool.release(req);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 将list中所有itemid用逗号分隔好返回字符串
	 * @param splitList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String returnItemids(List splitList){
		String itemids ="";
		for (int j = 0; j < splitList.size(); j++) {
			Map splitMap = (Map) splitList.get(j);
			if(j == splitList.size()-1){
				itemids = itemids+splitMap.get("itemid");
			}else{
				itemids =splitMap.get("itemid")+","+ itemids;
			}
		}
		return itemids;
	}
	/**
	 * 待发送消息
	 * @Title: getSendMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return 设定文件
	 */
	@SuppressWarnings("unchecked")
	public List getSendMessage(){
		 String sql = "select itemid,phone,message msg from fa_message t where t.sendflag in(0,2)"
			   		+ " and (t.backflag<>0 or t.backflag is null) ";
		return faMessageDao.findMapBySql(sql);
	}
	
	
	/**
	 * 留言发送短信
	 * @Title: saveMessageForNotice 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param noticeId
	 * @return 设定文件
	 */
	@SuppressWarnings("unchecked")
	public int saveMessageForNotice(final String noticeId) {
	
		//查询发送消息所需信息
		List getMsgInfoList = this.getMessageInfo(noticeId);
		List<Map> messageList= new ArrayList<Map>();
		for(int i=0;i<getMsgInfoList.size();i++)
		{
			Map map = (Map)getMsgInfoList.get(i);
			
			//用户编码
			String usercode = (String)map.get("usercode");
			//消息主体
			String message = (String)map.get("message");
			//用户标示
			BigDecimal userid = (BigDecimal)map.get("userid");
			//手机号
			String phone = (String) map.get("phone");
			
			if(StringUtil.isNotBlank(phone))
			{
				//手机拼接方式为XX,XX
				String[] phoneAry = phone.split(",");
				Map value = null;
				for(int j=0;j<phoneAry.length;j++) {
					value =  new HashMap();
					value.put("itemid", this.getItemid());
					value.put("application_id", noticeId);
					value.put("message", message);
					value.put("phone", phoneAry[j]);
					value.put("sendflag", "0");
					value.put("activity_id", usercode);
					value.put("create_time", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
					value.put("create_user", userid);
					
					//放在list
					messageList.add(value);
				}
			}
		}
		 mapDataDao.addBatch(messageList, "fa_message");
		
		//根据noticeId查询出写入的消息，并发送
		//消息保存成功后，启用线程发送消息
		Thread aThread=	new Thread(){
			   public void run(){
				   try {
					   String sql = "select itemid,phone,message msg from fa_message t where t.sendflag in(0,2)"
					   		+ " and (t.backflag<>0 or t.backflag is null) and t.application_id ='"+noticeId+"'";
						List zhList = faMessageDao.findMapBySql(sql);
						sendMessage(zhList);
					} catch (Exception e) {
						e.printStackTrace();
					}
			   }
			};
			aThread.start();
		return 0;
	}
	
	/**
	 * 获取发送消息的信息
	 * @param noticeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List getMessageInfo(String noticeId)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select  n.noticeid  noticeid,");
		sql.append("        r.receiveuser usercode,");
		sql.append("        (select e.phone from sys_userexp e where e.userid=u.userid) phone,");
		sql.append("        u.userid userid,");
		sql.append("        '尊敬的'|| (select u.username from sys_user u where r.receiveuser=u.usercode and r.noticeid="+noticeId+") || ");
		sql.append(" '，'||(select u.username from sys_user u where n.usercode=u.usercode and n.noticeid="+noticeId+")|| '给您发送了一条标题为'''''|| n.title|| '''''的留言，请您尽快登陆系统查看。' message");
		sql.append("  from  sys_notice n, sys_notice_receive r, sys_user u ");
		sql.append(" where  n.noticeid = r.noticeid");
		sql.append("   and  r.receiveuser = u.usercode");
		sql.append("   and  n.noticeid="+noticeId);
		System.err.println("getMessageInfo---------------"+sql.toString());
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	/**
	 * 查询消息标示
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getItemid()
	{
		String sql= "select (SEQ_FA_MESSAGE.nextval) itemid from dual";
		Map map = (Map)mapDataDao.queryListBySQL(sql).get(0);
		return  map.get("itemid").toString();
	}
}

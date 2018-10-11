package com.wfzcx.fam.common;
import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.freework.msg.receive.CallBack;
import com.freework.msg.receive.Result;
import com.freework.msg.socket.client.SendConnPool;
import com.freework.msg.socket.client.SocketReceiveMsg;
import com.freework.msg.vo.Message;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.util.WebContextFactoryUtil;
import com.wfzcx.fam.manage.po.MessageBean;
/**
 * 
 * @ClassName: MessageLongConnection 
 * @Description: 系统启动时加载，用于接收消息中心返回的处理结果
 * @author XinPeng
 * @date 2015年4月25日23:11:36
 */
public class MessageLongConnection  {
	static MapDataDaoI mapDataDao;
	//读取配置文件
	 Configuration  prop =getProperties();
	 static Configuration getProperties() {
	    	Configuration rc = new Configuration("messageClient.properties");
	        return rc;
	    }
	//初始化
	public static void init() {
		if (mapDataDao == null) {
			mapDataDao = (MapDataDaoI) WebContextFactoryUtil.getBean("com.jbf.common.dao.MapDataDao");
		}
		MessageLongConnection mlc = new MessageLongConnection();
		mlc.load();
	} 
	//加载
	public void load() {
			//初始化参数
			String	centerIp = prop.getValue("centerIp");   
			int	centerPort = Integer.parseInt(prop.getValue("centerPort"));   
			String	appId = prop.getValue("appId");  
			String	userCode = prop.getValue("userCode");   
			long sendtimeout =Long.parseLong(prop.getValue("sendtimeout"));
		    int connsize = Integer.parseInt(prop.getValue("connsize"));   ;
			int gettimeout = Integer.parseInt(prop.getValue("gettimeout"));
			int idletime = Integer.parseInt(prop.getValue("idletime"));

			SendConnPool.init(sendtimeout, centerIp, centerPort, appId, userCode, connsize, gettimeout, idletime);
			
			SocketReceiveMsg client = new SocketReceiveMsg(centerIp, centerPort, appId, userCode, 5);
			try {
				client.connect(new CallBack() {
					public void call(Result result) {
						if(result.isSuccess()){
							Message msg = (Message) result.getMsg();
							//callbackSms 为与消息中心约定的值，其他情况不做处理
							if("callbackSms".equals(msg.getType())){
								System.out.println("接收消息为----------"+msg.getMsg());
								//将接收到的消息转换为list
								List list = JSONArray.parseArray(msg.getMsg(), MessageBean.class);
								//更改消息状态
								messageUpdate(list);
							}
						}
					}
				});
			} catch (IOException e) {
				System.err.println("与消息服务中心建立接收消息长连接失败！");
				e.printStackTrace();
			}
	}
	/**
	 * 根据返回的消息是否成功更新相应消息状态
	 * @param list
	 */
	public void messageUpdate(List list){
		for (int i = 0; i < list.size(); i++) {
			MessageBean map = (MessageBean) list.get(i);
			int itemid = map.getItemid();
			int code = map.getCode();
			String tm = map.getTm();
			String failurecause ="";
			String sendFlag ="1";
			String backFlagString ="0";//用于记录该消息是否可以重新发送 0不可以，1，可以重新发送
			if(code>0){
				sendFlag = "1";
				backFlagString ="0";
			}else if(code==-1){
				sendFlag = "2";
				backFlagString ="1";
				failurecause ="消息中心数据库连接异常";
			}else if(code==-2){
				sendFlag = "2";
				backFlagString ="0";
				failurecause ="消息中心数据库操作失败";
			}
			if(!"".equals(itemid)){
				String updateSql = "update fa_message set sendflag="+sendFlag+" ,SENDTIME='"+tm+"' ,failurecause = '";
				updateSql = updateSql+failurecause;
				updateSql = updateSql+ "' ,backflag='"+backFlagString+"' ";
				updateSql = updateSql+" where itemid ="+itemid;
				mapDataDao.updateTX(updateSql);
			}
		}
	}
	public void testMap(){
		//测试数据库能否执行成功
		String updateSql = "update fa_message set sendflag=2 ";
		updateSql = updateSql+" where itemid =1";
		mapDataDao.updateTX(updateSql);
	}
	
}

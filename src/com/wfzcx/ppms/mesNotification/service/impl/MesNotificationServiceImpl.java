package com.wfzcx.ppms.mesNotification.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.fam.common.MessageComponent;
import com.wfzcx.ppms.mesNotification.dao.ProMessageCbDao;
import com.wfzcx.ppms.mesNotification.dao.ProMessageZbDao;
import com.wfzcx.ppms.mesNotification.po.ProMessageCb;
import com.wfzcx.ppms.mesNotification.po.ProMessageZb;
import com.wfzcx.ppms.mesNotification.service.MesNotificationService;

/**
 * 消息通知Service服务类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.mesNotification.service.impl.MesNotificationServiceImpl")
public class MesNotificationServiceImpl implements MesNotificationService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProMessageZbDao proMessageZbDao;
	@Autowired
	ProMessageCbDao proMessageCbDao;
	@Autowired
	MessageComponent messageComponent;
	
	@Override
	public PaginationSupport queryMessage(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		SysUser user = SecureUtil.getCurrentUser();
		StringBuffer sql = new StringBuffer();
		sql.append("select t.messageid,t.message_type,t.message_context,t.status,t.remark,t.create_time,t.create_user,t.update_time,t.update_user,")
		.append("(select listagg(a.receive_lb || '#' || a.receive_id,';') within group (order by a.msgcbid) from pro_message_cb a where t.messageid=a.messageid) view_id,")
		.append("(select listagg(a.receive_id,';') within group (order by a.msgcbid) from pro_message_cb a where t.messageid=a.messageid) receive_id,")
		.append("(select listagg(a.receive_name,';') within group (order by a.msgcbid) from pro_message_cb a where t.messageid=a.messageid) receive_name,")
		.append("(select listagg(a.receive_lb,';') within group (order by a.msgcbid) from pro_message_cb a where t.messageid=a.messageid) receive_lb,")
		.append("(select listagg(a.receive_phone,';') within group (order by a.msgcbid) from pro_message_cb a where t.messageid=a.messageid) receive_phone,")
		.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='MESSAGE_TYPE' and a.status=0  and a.code=t.message_type) message_type_name,")
		.append("(select a.username from sys_user a where a.userid=t.create_user) create_user_name,")
		.append("(select a.username from sys_user a where a.userid=t.update_user) update_user_name")
		.append(" from pro_message_zb t")
		.append(" where 1=1 and t.create_user = " + user.getUserid());
		String status = StringUtil.stringConvert(param.get("status"));
		if(!"".equals(status)){
			sql.append(" and t.status = '").append(status.trim()).append("'");
		}
		String createTimeBegin = StringUtil.stringConvert(param.get("createTimeBegin"));
		if(!"".equals(createTimeBegin)){
			sql.append(" and substr(t.create_time,0,10) >= '").append(createTimeBegin.trim()).append("'");
		}
		String createTimeEnd = StringUtil.stringConvert(param.get("createTimeEnd"));
		if(!"".equals(createTimeEnd)){
			sql.append(" and substr(t.create_time,0,10) <= '").append(createTimeEnd.trim()).append("'");
		}
		String messageContext = StringUtil.stringConvert(param.get("messageContext"));
		if(!"".equals(messageContext)){
			sql.append(" and t.message_context like '%").append(messageContext.trim()).append("%'");
		}
		String receive = StringUtil.stringConvert(param.get("receive"));
		if(!"".equals(receive)){
			sql.append(" and t.messageid in ( select distinct messageid from pro_message_cb a where a.receive_name like '%"+receive.trim()
					+"%') ");
		}
		sql.append(" order by t.messageid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String messageAddCommit(Map<String, Object> param) throws Exception{
		String msg = "";
		ProMessageZb proMessageZb = new ProMessageZb();
		BeanUtils.populate(proMessageZb, param);
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//保存主表数据
		proMessageZb.setCreateUser(user.getUserid().toString());
		proMessageZb.setCreateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		proMessageZbDao.save(proMessageZb);
		saveCb(proMessageZb.getMessageid(),param);
		
		//接收人手机号码
		String phone = StringUtil.stringConvert(param.get("receivePhone"));
		String[] phones = phone.split(";");
		
		if ("1".equals(proMessageZb.getStatus())){//发送
			if("0".equals(proMessageZb.getMessageType())){//短信
				sendDx(proMessageZb.getMessageid(),phones, proMessageZb.getMessageContext());
			} else if ("1".equals(proMessageZb.getMessageType())){//微信
				
			} else if ("2".equals(proMessageZb.getMessageType())){//短信和微信
				sendDx(proMessageZb.getMessageid(),phones, proMessageZb.getMessageContext());
			}
		}
		msg = "success";
		
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String messageEditCommit(Map<String, Object> param) throws Exception {
		ProMessageZb proMessageZb = proMessageZbDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("messageid"))));
		BeanUtils.populate(proMessageZb, param);
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		proMessageZb.setUpdateUser(user.getUserid().toString());
		proMessageZb.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		proMessageZbDao.update(proMessageZb);
		deleteCb(proMessageZb.getMessageid());
		saveCb(proMessageZb.getMessageid(), param);
		
		//接收人手机号码
		String phone = StringUtil.stringConvert(param.get("receivePhone"));
		String[] phones = phone.split(";");
		
		if ("1".equals(proMessageZb.getStatus())){//发送
			if("0".equals(proMessageZb.getMessageType())){//短信
				sendDx(proMessageZb.getMessageid(),phones, proMessageZb.getMessageContext());
			} else if ("1".equals(proMessageZb.getMessageType())){//微信
				
			} else if ("2".equals(proMessageZb.getMessageType())){//短信和微信
				sendDx(proMessageZb.getMessageid(),phones, proMessageZb.getMessageContext());
			}
		}
		return "success";
	}
	
	/**
	 * 从表删除
	 * @param messageid
	 */
	private void deleteCb(Integer messageid){
		String where = "messageid = " + messageid;
		proMessageCbDao.deleteBySQL("PRO_MESSAGE_CB", where);
	}
	
	/**
	 * 从表保存
	 * @param messageid
	 * @param param
	 */
	private void saveCb(Integer messageid,Map<String,Object> param){
		String receiveId = StringUtil.stringConvert(param.get("receiveId"));
		String receiveName = StringUtil.stringConvert(param.get("receiveName"));
		String receiveLb = StringUtil.stringConvert(param.get("receiveLb"));
		String receivePhone = StringUtil.stringConvert(param.get("receivePhone"));
		String[] ids = receiveId.split(";");
		String[] names = receiveName.split(";");
		String[] lbs = receiveLb.split(";");
		String[] phones = receivePhone.split(";");
		for (int i=0;i<ids.length;i++){
			ProMessageCb cb = new ProMessageCb();
			cb.setMessageid(messageid);
			cb.setReceiveId(Integer.valueOf(ids[i]));
			cb.setReceiveName(names[i]);
			cb.setReceiveLb(lbs[i]);
			cb.setReceivePhone(phones[i]);
			proMessageCbDao.save(cb);
		}
	}
	
	/**
	 * 发送短信
	 * @param messageid
	 * @param phones
	 * @param content
	 */
	private void sendDx(Integer messageid,String[] phones,String content){
		
		for (int i=0;i<phones.length;i++){
			/*发送消息*/
			messageComponent.saveMessage(messageid, phones[i], content,"");
		}
	}
	
	/**
	 *  根据主键messageid删除指标
	 * @param messageid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void messageDelete(String messageid){

		String where = "messageid = " + messageid;
		proMessageZbDao.deleteBySQL("PRO_MESSAGE_ZB", where);
		proMessageCbDao.deleteBySQL("PRO_MESSAGE_CB", where);

	}
	
	/**
	 * 消息发送
	 * @param messageid
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void messageSend(String messageid){
		ProMessageZb proMessageZb = proMessageZbDao.get(Integer.parseInt(messageid));
		proMessageZb.setStatus("1");//设置状态为发送
		proMessageZbDao.update(proMessageZb);//修改主表状态信息
		
		List list = proMessageCbDao.queryBySQL("PRO_MESSAGE_CB", "messageid="+messageid);
		String[] phones = new String[list.size()];
		for (int i=0;i<list.size();i++){
			phones[i] = (String)((Map)list.get(i)).get("receive_phone");
		}
		String type = proMessageZb.getMessageType();
		if ("0".equals(type)){//短信
			sendDx(proMessageZb.getMessageid(),phones,proMessageZb.getMessageContext());
		} else if ("1".equals(type)){//微信
			
		} else if ("2".equals(type)){//短信和微信
			sendDx(proMessageZb.getMessageid(),phones,proMessageZb.getMessageContext());
		}
	}
	
	/**
	 * 查询未选中接收人的所有列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List queryUnselectedUser(Map<String, Object> param)
			throws AppException {
		//分号分隔的view_mes_receive主键
		String userCode = (String)param.get("userCode");
		userCode = userCode.replaceAll(";", "','");
		StringBuffer sql = new StringBuffer();
		sql.append("select  v.id,v.lb,v.lbname,v.receiveid,v.name,v.phone,v.weixin from view_mes_receive v where 1=1 ");
		
	    //获取查询条件
		String messageLb = StringUtil.stringConvert(param.get("messageLb"));
		if(!"".equals(messageLb)){
			sql.append(" and v.lb = '").append(messageLb.trim()).append("'");
		}
		String messageName = StringUtil.stringConvert(param.get("messageName"));
		if(!"".equals(messageName)){
			sql.append(" and v.name like '%").append(messageName.trim()).append("%'");
		}
		if (!"".equals(userCode)){
			sql.append(" and v.id not in ('" + userCode + "')");
		}
		sql.append(" order by v.id");
		
		return mapDataDao.queryListBySQL(sql.toString());

	}
	
	/**
	 * 查询已选中的接收人列表
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List querySelectedUser(Map<String, Object> param)
			throws AppException {
		
		//分号分隔的view_mes_receive主键
		String userCode = (String)param.get("userCode");
		userCode = userCode.replaceAll(";", "','");
		StringBuffer sql = new StringBuffer();
		sql.append("select  v.id,v.lb,v.lbname,v.receiveid,v.name,v.phone,v.weixin from view_mes_receive v where 1=1 ");
		
	    
		if (!"".equals(userCode)){
			sql.append(" and v.id in ('" + userCode + "')");
		} else {
			sql.append(" and 1=0");
		}
		sql.append(" order by v.id");
		
		return mapDataDao.queryListBySQL(sql.toString());
	}
	
	
	
}

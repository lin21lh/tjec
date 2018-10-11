package com.wfzcx.aros.xzfy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.exception.AppException;
import com.wfzcx.aros.xzfy.dao.AvoidinfoDao;
import com.wfzcx.aros.xzfy.po.Avoidinfo;

/**
 * @ClassName: AvoidinfoService
 * @Description: 处理回避信息相关业务
 * @author ybb
 * @date 2016年8月29日 上午10:18:36
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.AvoidinfoService")
public class AvoidinfoService {

	@Autowired
	private AvoidinfoDao avoidinfoDao;
	
	/**
	 * @Title: queryAvoidinfoByCaseid
	 * @Description: 根据案件ID查询回避业务信息
	 * @author ybb
	 * @date 2016年8月29日 上午10:32:13
	 * @param caseid
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public Avoidinfo queryAvoidinfoByCaseid(String caseid){
		
		//拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.aid, t.caseid, t.appname, t.defname, t.factreason, t.operator, t.opttime, t.lasttime ");
		sql.append("from b_avoidinfo t ");
		sql.append("where t.caseid = '").append(caseid).append("'");
		
		List<?> avoidinfoList = avoidinfoDao.findVoBySql(sql.toString(), Avoidinfo.class);
		Avoidinfo avoidinfo = null;
		
		if(avoidinfoList != null && !avoidinfoList.isEmpty()){
			avoidinfo = (Avoidinfo) avoidinfoList.get(0);
		}
		
		return avoidinfo;
	}
}

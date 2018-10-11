package com.wfzcx.aros.ajbj.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.ajbj.dao.CasechangereqDao;
import com.wfzcx.aros.ajbj.po.Casechangereq;
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.util.FlowUtil;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;

/**
 * @ClassName: FytjService
 * @Description: 复议调解业务操作类
 * @author ybb
 * @date 2017年3月23日 下午3:51:40
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.ajbj.service.FytjService")
public class FytjService {

	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private CasechangereqDao casechangereqDao;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private CasebaseinfoDao casebaseinfoDao;
	
	/**
	 * @Title: queryFytjList
	 * @Description: 案件调解-分页查询案件调解列表
	 * @author ybb
	 * @date 2017年3月23日15:52:44
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryFytjList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.ccrid, t.caseid, t.csaecode, t.intro, t.protype, t.appname, t.defname, t.suspendreason, ");
		sql.append("t.suspenddate, t.restorereason, t.delayreason, t.delaydate, t.withdrawreason, t.reqmanrequest, ");
		sql.append("t.reqmansay, t.reqedmansay, t.thirdsay, t.ascertain, t.settlement, t.nodeid, t.opttype, t.state, ");
		sql.append("t.remark, t.userid, t.operator, t.opttime, t.region, t.lasttime, t.endreason  ");
		sql.append(" from B_CASECHANGEREQ t");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append(" where t.region = '" + user.getArea() + "'");
		} else {
			sql.append(" where t.region is null");
		}
		
		//处理状态
		sql.append(" and t.state = '").append(GCC.CASECHANGEREQ_STATE_TRANSIT).append("' ");
		sql.append(" and t.protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION).append("' ");
		
		//节点
		sql.append(" and t.nodeid in (select nodeid from pub_pronodebaseinfo t1, sys_user_role t2");
		sql.append(" where t1.roleid = t2.roleid and t2.userid ='").append(user.getUserid()).append("'");
		sql.append(" and t1.protype ='").append(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION).append("'");
		sql.append(" ) ");
		
		// 案件编号
		String csaecode = StringUtil.stringConvert(param.get("csaecode"));
		if (!StringUtils.isBlank(csaecode)) {
			sql.append(" and t.csaecode = '").append(csaecode).append("'");
		}
		
		// 申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		// 被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Title: queryCasechangereqByCcrid
	 * @Description: 根据申请ID获取案件变更申请信息
	 * @author ybb
	 * @date 2017年3月23日15:52:50
	 * @param ccrid
	 * @return
	 */
	public Casechangereq queryCasechangereqByCcrid(String ccrid) {
		
		 return casechangereqDao.get(ccrid);
	}
	
	/**
	 * @Title: addFytj
	 * @Description: 新增行政复议调解申请
	 * @author ybb
	 * @date 2017年3月23日15:52:53
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String addFytj(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件信息是否正常
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：请选择案件信息");
		}
		
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (casebaseinfo.getNodeid().compareTo(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_END)) == 0) {
			throw new AppException("该案件已审批结束，不能发起调解申请");
		}
		
			//获取行政复议变更申请PO，并将页面参数转化到该对象中
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		
		Casechangereq casechangereq = null;
		if (StringUtil.isNotBlank(ccrid)) {
			casechangereq = queryCasechangereqByCcrid(ccrid);
		} else {
			casechangereq = new Casechangereq();
		}
		BeanUtils.populate(casechangereq, param);
		
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//封装案件变更申请信息
		casechangereq.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION);	
		casechangereq.setNodeid(new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT));   
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);	
		casechangereq.setState(GCC.CASECHANGEREQ_STATE_TRANSIT);
		casechangereq.setUserid(user.getUserid());											 
		casechangereq.setOperator(user.getUsername());   
		casechangereq.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          
		casechangereq.setRegion(user.getArea());												  
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")); 
			
			//修改案件基本信息表
		casebaseinfo.setOldprotype(casebaseinfo.getProtype());
		casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION);
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		
		casebaseinfoDao.update(casebaseinfo);
		
			//判断新增或修改复议变更申请
		if (StringUtil.isBlank(ccrid)) {
			ccrid = (String)casechangereqDao.save(casechangereq);
		} else {
			casechangereqDao.update(casechangereq);
		}
		
		//4、记录流程日志
			//获取变更申请对应的流程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");  
		logSql.append(" and t.nodeid = " + GCC.CASECHANGEREQ_NODEID_ONEAUDIT);                                    
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		Probaseinfo probaseinfo = new Probaseinfo();
		if (null != probaseins && !probaseins.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		}
		
			//承办人意见
		String agentRemark = StringUtil.stringConvert(param.get("agentRemark"));
			//转化提交操作日志
		probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, new BigDecimal(GCC.CASECHANGEREQ_NODEID_ONEAUDIT),
				casechangereq.getCaseid(), probaseinfo, user, null, agentRemark, null);
		
		probaseinfo.setProcessid(ccrid);
		
			//新增或修改流程日志
		probaseinfoDao.saveOrUpdate(probaseinfo);
		
		return ccrid;
	}
	
	/**
	 * @Title: fytjAddFlow
	 * @Description: 复议调解发起发送
	 * @author ybb
	 * @date 2017年3月23日15:53:03
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void fytjAddFlow(String ccrid) throws AppException {
		
		//1、入口参数判断
			//判断变更申请ID是否为空
		if(StringUtils.isBlank(ccrid)){
			throw new AppException("变更申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断调解申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("未找到对应的复议调解申请");
		}

		BigDecimal nodeid = casechangereq.getNodeid();
			//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");    
		logSql.append(" and t.nodeid = " + nodeid);                                     	
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED + "'");      
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存调解发起申请，请保存后再发送");
		}
		
		//3、业务逻辑处理
			// 更新案件变更信息
		casechangereq.setNodeid(nodeid.add(BigDecimal.TEN));                                  
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);                          	// 已提交
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));   // 数据最后更新时间
		
		casechangereqDao.update(casechangereq);
				
		//4、记录流程操作日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, nodeid,
				casechangereq.getCaseid(), probaseinfo, user, null, probaseinfo.getRemark());
		
		probaseinfo.setProcessid(ccrid);
		probaseinfoDao.update(probaseinfo);
		
			// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = new BigDecimal(GCC.CASECHANGEREQ_NODEID_TWOAUDIT);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");
		logSql.append(" and t.nodeid = " + newNodeid);
		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, newNodeid,
				casechangereq.getCaseid(), new_probaseinfo, user, null, null, null);
		
		new_probaseinfo.setProcessid(ccrid);
		
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
	}
	
	/**
	 * @Title: updateFytjAudit
	 * @Description: 复议调解审核
	 * @author ybb
	 * @date 2017年3月23日15:53:26
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateFytjAudit(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断申请ID是否为空
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}
		
		//3、业务逻辑处理
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);	
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")); 
		casechangereqDao.update(casechangereq);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.processid = '" + ccrid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");
		sql.append(" and t.nodeid = " + casechangereq.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该调解申请未发起流程");
		}
		
		BigDecimal nodeid = casechangereq.getNodeid();
		String remark = "";
		if (GCC.CASECHANGEREQ_NODEID_TWOAUDIT == nodeid.intValue()) {
			//科室处理意见
			remark = StringUtil.stringConvert(param.get("sectionRemark"));
		}
		else if (GCC.CASECHANGEREQ_NODEID_THREEAUDIT == nodeid.intValue()) {
			//机构处理意见
			remark = StringUtil.stringConvert(param.get("organRemark"));
		}
		else if (GCC.CASECHANGEREQ_NODEID_FOURAUDIT == nodeid.intValue()) {
			//机关处理意见
			remark = StringUtil.stringConvert(param.get("officeRemark"));
		}
		
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		probaseinfo.setRemark(remark);
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION,
				nodeid, casechangereq.getCaseid(), probaseinfo, user);
		
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Title: fytjAuditFlow
	 * @Description: 发送审核流程
	 * @author ybb
	 * @date 2017年3月23日15:53:34
	 * @param ccrid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void fytjAuditFlow(String ccrid) throws AppException {
		
		//1、入口参数判断
			//判断申请ID是否为空
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}

		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'"); // 复议审批
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = " + casechangereq.getNodeid());
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议调解审核，请保存后再发送");
		}
		
		//3、记录流程操作日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, casechangereq.getNodeid(),
				casechangereq.getCaseid(), probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加10，新增流程日志_已提交
		BigDecimal newNodeid = casechangereq.getNodeid().add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");
		logSql.append(" and t.nodeid = " + newNodeid);
		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, newNodeid,
				casechangereq.getCaseid(), new_probaseinfo, user, null, null, null);
		new_probaseinfo.setProcessid(casechangereq.getCcrid());
		
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		//4、业务逻辑处理
			// 更新案件变更申请信息
		casechangereq.setNodeid(casechangereq.getNodeid().add(BigDecimal.TEN));                   // 节点加10
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));     // 数据最后更新时间
		
		casechangereqDao.update(casechangereq);
	}
	
	/**
	 * @Title: updateFytjDecide
	 * @Description: 保存复议调解决定
	 * @author ybb
	 * @date 2017年3月23日15:53:38
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateFytjDecide(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断申请ID是否为空
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
		
			//判断处理结果是否为空
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择处理结果");
		}
		
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}
		
		// 3、业务逻辑处理
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);	
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")); 
		casechangereq.setResult(result);
		casechangereqDao.update(casechangereq);
			
		//4、记录流程日志
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.processid = '" + ccrid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");  
		sql.append(" and t.nodeid = " + GCC.CASECHANGEREQ_NODEID_DECISION);          
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：请选择处理决定");
		}
		else {
			Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
			
			probaseinfo.setResult(result); // 受理结果
			probaseinfo.setRemark(StringUtil.stringConvert(param.get("remark"))); // 说明
			probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION,
				 new BigDecimal(GCC.CASECHANGEREQ_NODEID_DECISION), casechangereq.getCaseid(), probaseinfo, user);
			
			//修改过程日志_已接收
			probaseinfoDao.update(probaseinfo);
		}
	}
	
	/**
	 * @Title: fytjDecideFlow
	 * @Description: 发送决定流程
	 * @author ybb
	 * @date 2017年3月23日15:53:42
	 * @param ccrid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void fytjDecideFlow(String ccrid) throws AppException {
		
		//1、入口参数判断
			//判断申请ID是否为空
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}
		
			//判断案件信息是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(casechangereq.getCaseid());
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}

			//判断该案件是否已提交
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.processid = '" + ccrid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + GCC.CASECHANGEREQ_NODEID_DECISION);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
		
			//判断处理结果是否为空
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
				
		//3、业务逻辑操作
			//根据处理结果，处理变更申请和案件信息
		if (GCC.PROBASEINFO_OPTTYPE_PASS.equals(result)) { 	//同意
			
			// 更新案件变更信息
			casechangereq.setState(GCC.CASECHANGEREQ_STATE_COMPLETE);
			casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
			
			casechangereqDao.update(casechangereq);
			
		} else { 	//不同意
			
			casebaseinfo.setProtype(casebaseinfo.getOldprotype());
			casebaseinfo.setOldprotype("");
			casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
			
			casebaseinfoDao.update(casebaseinfo);
			
			// 更新案件变更信息
			casechangereq.setState(GCC.CASECHANGEREQ_STATE_NOPASS);
			casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
			
			casechangereqDao.update(casechangereq);
		}
	
		//4、记录流程操作日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION, casechangereq.getNodeid(),
				casechangereq.getCaseid(), probaseinfo, user, null, probaseinfo.getRemark());
			// 更新流程信息
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Title: fytjReturnFlow
	 * @Description: 流程退回
	 * @author ybb
	 * @date 2017年3月23日15:53:48
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void fytjReturnFlow(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断申请ID是否为空
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}
	
			//判断流程节点是否可退回
		BigDecimal nodeid_old = casechangereq.getNodeid();
		if (null == nodeid_old
			|| 1 >= nodeid_old.intValue()
			|| GCC.CASECHANGEREQ_NODEID_ONEAUDIT == nodeid_old.intValue()) {
			throw new AppException("保存失败：该调解申请所处环节不能退回，请刷新页面再退回");
		}
		
		//3、业务逻辑处理
			// 获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
			// 回退后流程节点
		BigDecimal nodeid = nodeid_old.subtract(BigDecimal.TEN );
		
			// 修改案件申请表信息
		casechangereq.setNodeid(nodeid);			                                              // 流程节点退一步
		casechangereq.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);   						      // 处理标志：0_已提交
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));     // 数据最后更新时间
		
		casechangereqDao.update(casechangereq);
			
		// 4、记录流程日志
			// 删除回退之前的过程记录
		String protype = casechangereq.getProtype();
		StringBuffer where = new StringBuffer();
		where.append(" processid = '" + ccrid + "'")
			 .append(" and protype = '").append(protype)
			 .append("' and nodeid > ").append(nodeid);
		probaseinfoDao.deleteBySQL("PUB_PROBASEINFO", where.toString());
		
		//生成回退日志
		String remark = StringUtil.stringConvert(param.get("htyj"));
		
		Probaseinfo probaseinfo_return = new Probaseinfo();
		probaseinfo_return.setNodeid(nodeid_old);
		probaseinfo_return = FlowUtil.genReturnedOperationData(protype, casechangereq.getCaseid(), probaseinfo_return,
				user, "", remark);
		probaseinfo_return.setProcessid(ccrid);
		
		probaseinfoDao.save(probaseinfo_return);
	}
	
	/**
	 * @Title: queryCaseList
	 * @Description: 根据条件查询案件列表信息
	 * @author ybb
	 * @date 2017年3月23日15:53:53
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryCaseList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, ");
		sql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		sql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		sql.append("t.admtype, t.casetype, t.ifcompensation, t.amount, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		sql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, t.delaydays, t.region, t.intro, t.key, t.state, t.isgreat, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");
		sql.append("from B_CASEBASEINFO t ");
		
		sql.append("where t.protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("'");
		sql.append("and t.nodeid != ").append(GCC.PRONODEBASEINFO_NODEID_END);
		
		// 申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		// 被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		// 行政管理类型
		String csaecode = StringUtil.stringConvert(param.get("csaecode"));
		if (!StringUtils.isBlank(csaecode)) {
			sql.append(" and t.csaecode like '%").append(csaecode).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Title: deleteFytjByCcrid
	 * @Description: 根据申请ID删除复议调解申请
	 * @author ybb
	 * @date 2017年3月23日15:53:58
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteFytjByCcrid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断申请ID是否为空
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		if (StringUtils.isBlank(ccrid)) {
			throw new AppException("保存失败：申请ID为空");
		}
	
		//2、业务逻辑判断
			//判断复议变更申请是否正常
		Casechangereq casechangereq = casechangereqDao.get(ccrid);
		if(casechangereq == null){
			throw new AppException("保存失败：未找到对应的复议调解申请");
		}
		
			//判断案件信息是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(casechangereq.getCaseid());
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
	
		//3、业务逻辑处理
		
			// 修改调解申请表信息
		casechangereq.setState(GCC.CASECHANGEREQ_STATE_CANCEL);
		casechangereq.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));     // 数据最后更新时间
		
		casechangereqDao.update(casechangereq);
		
			//修改复议案件信息
		casebaseinfo.setProtype(casebaseinfo.getOldprotype());
		casebaseinfo.setOldprotype("");
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		
		casebaseinfoDao.update(casebaseinfo);
			
		// 4、记录流程日志
	}
	
	/**
	 * @Title: queryPronodebaseinfoByUserid
	 * @Description: 判断角色是否存在第一节点的权限
	 * @author ybb
	 * @date 2017年3月25日 上午10:24:43
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int queryPronodebaseinfoByUserid(){
		
		// 获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select count(id) nodeidcount from pub_pronodebaseinfo ");
		sql.append("where protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYMEDIATION).append("'");
		sql.append("and nodeid = ").append(GCC.CASECHANGEREQ_NODEID_ONEAUDIT);
		sql.append("and roleid in (select roleid from sys_user_role where userid = ")
		   .append(user.getUserid()).append(")");
		
		int count = 0;
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(sql.toString());
		if (probaseins != null && probaseins.size() > 0){
			
			count = probaseins.get(0).getIntValue("nodeidcount");
		}
		
		return count;
	}
	
	/**
	 * @Title: queryFyzzListByCcrid
	 * @Description: 根据申请ID查询变更申请信息
	 * @author ybb
	 * @date 2017年3月28日11:55:33
	 * @param ccrid
	 * @return
	 * @throws AppException
	 */
	public Casechangereq queryFyzzListByCcrid(String ccrid) throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.ccrid, t.caseid, t.csaecode, t.intro, t.protype, t.appname, t.defname, ");
		sql.append("t.suspenddate, t.restorereason, t.delayreason, t.delaydate, t.withdrawreason, t.reqmanrequest, ");
		sql.append("t.reqmansay, t.reqedmansay, t.thirdsay, t.ascertain, t.settlement, t.nodeid, t.opttype, t.state, ");
		sql.append("t.remark, t.userid, t.operator, t.opttime, t.region, t.lasttime, t.endreason,  ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='AUDITRESULT' and a.status=0 and a.code = t.result) result ");
		sql.append(" from B_CASECHANGEREQ t");
		sql.append(" where t.ccrid = '").append(ccrid).append("'");
		
		List<?> casechangereqList = casechangereqDao.findVoBySql(sql.toString(), Casechangereq.class);
		if(casechangereqList != null && !casechangereqList.isEmpty()){
			return (Casechangereq) casechangereqList.get(0);
		}
		
		return null;
	}
}

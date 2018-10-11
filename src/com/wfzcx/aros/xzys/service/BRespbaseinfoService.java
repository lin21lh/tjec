package com.wfzcx.aros.xzys.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import org.springframework.util.CollectionUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.base.filemanage.dao.impl.SysFileManageDaoImpl;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.util.FlowUtil;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.xzfy.dao.BCasenoticerelainfoDao;
import com.wfzcx.aros.xzfy.po.BCasenoticerelainfo;
import com.wfzcx.aros.xzys.dao.BRespbaseinfoDAO;
import com.wfzcx.aros.xzys.dao.RespreviewinfoDao;
import com.wfzcx.aros.xzys.dao.ResptrialinfoDao;
import com.wfzcx.aros.xzys.po.BRespbaseinfo;
import com.wfzcx.aros.xzys.po.Respreviewinfo;
import com.wfzcx.aros.xzys.po.Resptrialinfo;

/**
 * 
 * @author LinXF
 * @date 2016年8月13日 下午9:59:02
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzys.service.BRespbaseinfoService")
public class BRespbaseinfoService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BRespbaseinfoDAO dao;
	@Autowired
	RespreviewinfoDao reviewDao;
	@Autowired
	ResptrialinfoDao trialDao;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private BCasenoticerelainfoDao casenoticerelainfoDao;
	@Autowired
	SysFileManageDaoImpl sysfileDao;
	/**
	 * 查询应诉案件列表
	 * @param map
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id,t.caseid,t.casecode,t.carriedstatus,t.regtype,t.recdate,t.suetype,t.caseseqno, ");
		sql.append(" t.lawid,t.caseinnerid,t.jurilaw,t.expresscom,t.expressid,t.resdept,t.deptlevel,t.deptname, ");
		sql.append(" t.adminlevel,t.adminbehavior,t.regiontype,t.fringereq,t.suerequest,t.evidence,t.nodeid,t.processid,t.protype,");
		sql.append(" t.suecontents,t.lawcontents,t.constatus,t.ifincourt,t.recreport,t.recmanage,t.operator,t.opttime,t.ifappeal,t.appealtime,t.remark,t.annex,  ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CARRIEDSTATUS' and a.status=0  and a.code=t.carriedstatus) carriedstatusname,");
		sql.append(" (select a.lawid from B_RESPBASEINFO a where a.id=t.caseid) glahlawid,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='REGTYPE' and a.status=0  and a.code=t.regtype) regtypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SUETYPE' and a.status=0  and a.code=t.suetype) suetypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JURILAW' and a.status=0  and a.code=t.jurilaw) jurilawname ");
		sql.append(" from B_RESPBASEINFO t where 1=1");
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空  regiontype
//		if (!StringUtils.isBlank(user.getArea())) {
//			sql.append(" where t.regiontype = '" + user.getArea() + "'");
//		} else {
//			sql.append(" where t.regiontype is null");
//		}
		//节点
		sql.append(" and t.nodeid in (select nodeid from pub_pronodebaseinfo t1, sys_user_role t2");
		sql.append(" where t1.roleid = t2.roleid and t2.userid ='").append(user.getUserid()).append("'");
		sql.append(" and t1.protype ='").append(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO).append("'");
		sql.append(" ) ");
		
		//案号
		String lawid = StringUtil.stringConvert(param.get("lawid"));
		if (lawid != null && !lawid.trim().equals("")) {
			sql.append(" and t.lawid='" + lawid + "'");
		}
		//原告情况
		String suerequest = StringUtil.stringConvert(param.get("suerequest"));
		if (suerequest != null && !suerequest.trim().equals("")) {
			sql.append(" and t.suerequest='" + suerequest + "'");
		}
		//收案时间
		String srecdate = StringUtil.stringConvert(param.get("srecdate"));
		String erecdate = StringUtil.stringConvert(param.get("erecdate"));
		if (srecdate != null && !srecdate.trim().equals("") && erecdate != null && !erecdate.trim().equals("")) {
			sql.append(" and t.recdate between '" + srecdate + "' and '"+erecdate+"' ");
		}
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * @Description: 根据当前登录用户id获取节点id
	 * @author czp
	 * @date 2017年3月24日
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String queryNodeidsByUserid(Long userid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select roleid from sys_user_role where userid ='").append(userid).append("'");
		List<JSONObject> roleidList = mapDataDao.queryListBySQL(sql.toString());
		HashSet<String> set = new HashSet<String>();
		for (JSONObject json : roleidList) {
			set.add(json.getString("roleid"));
		}
		String roleids = generateStr(set);
		
		sql.setLength(0);
		sql.append("select nodeid from PUB_PRONODEBASEINFO where roleid in (").append(roleids).append(")");
		List<JSONObject> nodeidList = mapDataDao.queryListBySQL(sql.toString());
		set.clear();
		for (JSONObject json : nodeidList) {
			set.add(json.getString("nodeid"));
		}
		String nodeids = generateStr(set);
		
		return nodeids;
	}
	
	/**
	 * @Description: 生成字符串
	 * @author ztt
	 * @date 2016年11月29日
	 * @param list
	 * @param key
	 * @return
	 */
	private String  generateStr (HashSet<String> set) {
		int index = set.size();
		int i = 1;
		StringBuffer str = new StringBuffer();
		for (String s : set) {
			if (i == index) {
				str.append("'").append(s).append("'");
			}
			else
			{
				str.append("'").append(s).append("', ");
			}
			i++;
		}
		return str.toString();
	}
	
	
	
	
	
	/**
	 * @Title: relatedListQuery
	 * @Description: 查询选择关联案号
	 * @author czp
	 * @date 2017年2月8日 
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport relatedListQuery(Map<String, Object> param) throws AppException{
		
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id,t.caseid,t.casecode,t.carriedstatus,t.regtype,t.recdate,t.suetype,t.caseseqno, ");
		sql.append(" t.lawid,t.caseinnerid,t.jurilaw,t.expresscom,t.expressid,t.resdept,t.deptlevel,t.deptname, ");
		sql.append(" t.adminlevel,t.adminbehavior,t.regiontype,t.fringereq,t.suerequest,t.evidence,t.nodeid,t.processid,t.protype,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CARRIEDSTATUS' and a.status=0  and a.code=t.carriedstatus) carriedstatusname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='REGTYPE' and a.status=0  and a.code=t.regtype) regtypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SUETYPE' and a.status=0  and a.code=t.suetype) suetypename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='JURILAW' and a.status=0  and a.code=t.jurilaw) jurilawname ");
		sql.append(" from B_RESPBASEINFO t where 1=1 ");
		//案号
		String lawid = StringUtil.stringConvert(param.get("lawid"));
		if (!StringUtils.isBlank(lawid)) {
			sql.append(" and t.lawid ='"+lawid+"' ");
		}
		//审理阶段
		String regtype = StringUtil.stringConvert(param.get("regtype"));
		if (!StringUtils.isBlank(regtype) && "02".equals(regtype)) {
			sql.append(" and t.regtype = '01' ");
		}else if(!StringUtils.isBlank(regtype) && "03".equals(regtype)){
			sql.append(" and t.regtype = '02' ");
		}
		sql.append(" order by t.lawid ");
		System.out.println("--sql:"+sql);
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	/**
	 * @Title: queryCasebaseinfoByID
	 * @Description: 查询应诉案件基本信息
	 * @author czp
	 * @date 2017.03.29
	 * @return
	 */
	public BRespbaseinfo queryCasebaseinfoByID(String id){
		return dao.get(id);
	}
	
	
	
	
	/**
	 * @Title: queryCaseReviewinfoById
	 * @Description: 查询案件审查信息
	 * @author czp
	 * @date 2017年3月24日
	 * @return
	 */
	public Respreviewinfo queryCaseReviewinfoById(String id)
	{
		Respreviewinfo info = new Respreviewinfo();
		info.setLawid(id);
		List<Respreviewinfo> list =  reviewDao.findByExample(info);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * @Title: queryCaseAppeartinfoById
	 * @Description: 查询案件出庭信息
	 * @author czp
	 * @date 2017年3月24日
	 * @return
	 */
	public Resptrialinfo queryCaseAppeartinfoById(String id)
	{
		Resptrialinfo info = new Resptrialinfo();
		info.setLawid(id);
		List<Resptrialinfo> list =  trialDao.findByExample(info);
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * 保存行政应诉案件
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Map<String, Object> param) throws Exception {
		String id = (String) param.get("id");
		String nodeid = String.valueOf(GCC.RESPBASEINFO_NODEID_MAINTAIN);
		
		//1、入口参数判断
		//2、业务逻辑判断
		//3、业务逻辑处理
		//获取应诉申请PO，并将页面参数转化到该对象中
		BRespbaseinfo bRespbaseinfo = new BRespbaseinfo();
		
		BeanUtils.populate(bRespbaseinfo, param);
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
		SysUser user = SecureUtil.getCurrentUser();
		bRespbaseinfo.setOperator(user.getUsername());	                                //操作人
		bRespbaseinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));		//操作时间
		bRespbaseinfo.setNodeid(new BigDecimal(nodeid));			                    //节点编号
		bRespbaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);               //流程类型
//		Probaseinfo probaseinfo = probaseinfoDao.get(id);
//		if(probaseinfo !=null && probaseinfo.getId() != null && !bRespbaseinfo.getId().trim().equals("")){
//			bRespbaseinfo.setProcessid(probaseinfo.getProcessid());       //流程ID
//			bRespbaseinfo.setProtype(probaseinfo.getProtype());           //流程类型
//		}

		//新增或行政复议申请
		if (StringUtil.isBlank(id)) {
			id = (String) dao.save(bRespbaseinfo);
		} else {
			dao.update(bRespbaseinfo);
		}
		//4、记录流程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");  // 复议审批
		logSql.append(" and t.nodeid = " + nodeid);                                     // 接收材料
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		Probaseinfo probaseinfo = new Probaseinfo();
		if (null != probaseins && !probaseins.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		}
		//转化提交操作日志
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_MAINTAIN),
				id, probaseinfo, user);
		//新增或修改流程日志
		probaseinfoDao.saveOrUpdate(probaseinfo);
		
		return id;
	}

	/**
	 * @Title: xzfyReceiveFlow
	 * @Description: 行政应诉收案登记：发送
	 * @author ztt
	 * @date 2016年11月8日
	 * @param id
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyReceiveFlow(String id) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(id)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断应诉申请是否正常
		//判断该案件环节是否异常
		//判断该案件处理标志不是【已提交】或【已退回】
		BRespbaseinfo brespbaseinfo = dao.get(id);
		if(brespbaseinfo == null){
			throw new AppException("未找到对应的行政应诉案件");
		}
		if (!brespbaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO)) {
			throw new AppException("该案件不是【行政应诉案件】");
		}
		BigDecimal nodeid = brespbaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = '" + GCC.RESPBASEINFO_NODEID_MAINTAIN + "'");  // 节点编号
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存案件编号，请保存后再发送");
		}
		
		//4、记录提交日志
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
	    SysUser user = SecureUtil.getCurrentUser();
	   // 修改流程日志_已处理
 		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
 		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, nodeid,
 				id, probaseinfo, user, null, probaseinfo.getRemark());
 		probaseinfoDao.update(probaseinfo);
	    
 		
 	    // 节点加1，新增流程日志_已提交
 	    BigDecimal newNodeid = brespbaseinfo.getNodeid().add(BigDecimal.ONE);
	 	logSql.setLength(0);
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.nodeid = '" + newNodeid + "'");  // 节点编号
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_DIVISION),
				id, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		brespbaseinfo.setNodeid(new BigDecimal(GCC.RESPBASEINFO_NODEID_DIVISION));		  	//节点编号
		brespbaseinfo.setOperator(user.getUsername());										  // 操作人
		dao.update(brespbaseinfo);
	}
	
	/**
	 * @Title: queryFileList
	 * @Description: 行政复议接收材料：材料列表分页查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryFileList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		String lawid = StringUtil.stringConvert(param.get("lawid"));
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		String code = "XZYS_AJSC";
		if(StringUtils.equals("30", nodeid)){
			 code = "XZYS_CTYS";
		};
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select itemid, filename, createtime,usercode from SYS_FILEMANAGE where keyid = '")
			.append(lawid)
			.append("' and elementcode = '").append(code)
			.append("' order by createtime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * 保存行政应诉案件审查
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String xzysExamSave(Map<String, Object> param) throws Exception {
		String caseid = StringUtil.stringConvert(param.get("lawid"));
		//1、入口参数判断
		//2、业务逻辑判断
		//3、业务逻辑处理
		Respreviewinfo respreviewinfo =new Respreviewinfo();
		String id = (String) param.get("id");
		if(StringUtils.isEmpty(id)){
			param.remove("id");
		}
		BeanUtils.populate(respreviewinfo, param);
		respreviewinfo.setLawid(caseid);
		
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
		SysUser user = SecureUtil.getCurrentUser();
		respreviewinfo.setOperator(user.getUsername());	                                //操作人
		respreviewinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));		//操作时间

		//新增或行政复议申请
		reviewDao.saveOrUpdate(respreviewinfo);
		String nodeid = String.valueOf(GCC.RESPBASEINFO_NODEID_DIVISION);
		//4、记录流程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");  // 复议审批
		logSql.append(" and t.nodeid = " + nodeid);                                     // 接收材料
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		Probaseinfo probaseinfo = new Probaseinfo();
		if (null != probaseins && !probaseins.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		}
		//转化提交操作日志
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_DIVISION),
				caseid, probaseinfo, user);
		//新增或修改流程日志
		probaseinfoDao.saveOrUpdate(probaseinfo);
		
		return caseid;
	}
	
	/**
	 * @Title: xzfyReceiveFlow
	 * @Description: 行政应诉案件审查：发送
	 * @author czp
	 * @date 2017年3月28日
	 * @param id
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzysExamSend(String id) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(id)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断应诉申请是否正常
		//判断该案件环节是否异常
		//判断该案件处理标志不是【已提交】或【已退回】
		BRespbaseinfo brespbaseinfo = dao.get(id);
		if(brespbaseinfo == null){
			throw new AppException("未找到对应的行政应诉案件");
		}
		if (!brespbaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO)) {
			throw new AppException("该案件不是【行政应诉案件】");
		}
		BigDecimal nodeid = brespbaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");  //流程类型
		logSql.append(" and t.nodeid = '" + GCC.RESPBASEINFO_NODEID_DIVISION + "'");  // 节点编号
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
		logSql.append(" order by t.endtime desc");
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存案件编号，请保存后再发送");
		}
		
		//4、记录提交日志
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
	    SysUser user = SecureUtil.getCurrentUser();
	   // 修改流程日志_已处理
 		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
 		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, nodeid,
 				id, probaseinfo, user, null, probaseinfo.getRemark());
 		probaseinfoDao.update(probaseinfo);
	    
 		
 	    // 节点加1，新增流程日志_已提交
 	    BigDecimal newNodeid = brespbaseinfo.getNodeid().add(BigDecimal.ONE);
	 	logSql.setLength(0);
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");  //流程类型
		logSql.append(" and t.nodeid = '" + newNodeid + "'");  // 节点编号
		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_DISPOSE),
				id, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		brespbaseinfo.setNodeid(new BigDecimal(GCC.RESPBASEINFO_NODEID_DISPOSE));		  	//节点编号
		brespbaseinfo.setOperator(user.getUsername());										  // 操作人
		dao.update(brespbaseinfo);
	}
	
	
	
	/**
	 * 保存行政应诉案件出庭
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String xzysAppeartSave(Map<String, Object> param) throws Exception {
		String caseid = StringUtil.stringConvert(param.get("lawid"));
		//1、入口参数判断
		//2、业务逻辑判断
		//3、业务逻辑处理
		Resptrialinfo resptrialinfo =new Resptrialinfo();
		String id = (String) param.get("id");
		if(StringUtils.isEmpty(id)){
			param.remove("id");
		}
		BeanUtils.populate(resptrialinfo, param);
		resptrialinfo.setLawid(caseid);
		
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
		SysUser user = SecureUtil.getCurrentUser();
		resptrialinfo.setOperator(user.getUsername());	                                //操作人
		resptrialinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));		//操作时间

		//新增或行政复议申请
		trialDao.saveOrUpdate(resptrialinfo);
		String nodeid = String.valueOf(GCC.RESPBASEINFO_NODEID_DISPOSE);
		//4、记录流程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");  // 复议审批
		logSql.append(" and t.nodeid = " + nodeid);                                     // 接收材料
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		Probaseinfo probaseinfo = new Probaseinfo();
		if (null != probaseins && !probaseins.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		}
		//转化提交操作日志
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_DISPOSE),
				caseid, probaseinfo, user);
		//新增或修改流程日志
		probaseinfoDao.saveOrUpdate(probaseinfo);
		
		return caseid;
	}
	
	
	
	/**
	 * @Title: xzysAppeartSend
	 * @Description: 行政应诉案件出庭：发送
	 * @author czp
	 * @date 2016年11月8日
	 * @param id
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzysAppeartSend(String id) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(id)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断应诉申请是否正常
		//判断该案件环节是否异常
		//判断该案件处理标志不是【已提交】或【已退回】
		BRespbaseinfo brespbaseinfo = dao.get(id);
		if(brespbaseinfo == null){
			throw new AppException("未找到对应的行政应诉案件");
		}
		if (!brespbaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO)) {
			throw new AppException("该案件不是【行政应诉案件】");
		}
		BigDecimal nodeid = brespbaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.nodeid = '" + GCC.RESPBASEINFO_NODEID_DISPOSE + "'");  // 节点编号
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存案件编号，请保存后再发送");
		}
		
		//4、记录提交日志
		//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
	    SysUser user = SecureUtil.getCurrentUser();
	   // 修改流程日志_已处理
 		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
 		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, nodeid,
 				id, probaseinfo, user, null, probaseinfo.getRemark());
 		probaseinfoDao.update(probaseinfo);
	    
 		
 	    // 节点加1，新增流程日志_已提交
 	    BigDecimal newNodeid = brespbaseinfo.getNodeid().add(BigDecimal.ONE);
	 	logSql.setLength(0);
		logSql.append(" select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");  //案件ID
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.nodeid = '" + newNodeid + "'");  // 节点编号
		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
//		logSql.append(" and t.processid = '" + processid + "'");  // 流程ID
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, new BigDecimal(GCC.RESPBASEINFO_NODEID_FILING),
				id, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		brespbaseinfo.setNodeid(new BigDecimal(GCC.RESPBASEINFO_NODEID_FILING));		  	//节点编号
		brespbaseinfo.setOperator(user.getUsername());										  // 操作人
		dao.update(brespbaseinfo);
	}
	
	
	
	/**
	 * @Title: updateXzysReturnByLawid
	 * @Description: 复议回退：根据案件ID退回行政应诉处理信息，并记录处理日志
	 * @author czp
	 * @date 2017年3月24日 
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzysReturnByLawid(Map<String, Object> param) throws Exception{
		// 1、入口参数判断
		// 判断案件ID是否为空
		String lawid = StringUtil.stringConvert(param.get("lawid"));
		if (StringUtils.isBlank(lawid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		// 2、业务逻辑判断
		// 判断行政复议申请是否正常
		BRespbaseinfo brespbaseinfo = dao.get(lawid);
		// 原流程节点
		BigDecimal nodeid_old = brespbaseinfo.getNodeid();
		if (null == brespbaseinfo){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (null == nodeid_old
			|| 10 >= nodeid_old.intValue()
			|| GCC.PRONODEBASEINFO_NODEID_HEAR == nodeid_old.intValue()) {
			throw new AppException("保存失败：该案件所处环节不能退回，请刷新页面再退回");
		}
		
		// 获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 回退后流程节点
		BigDecimal nodeid = nodeid_old.subtract(BigDecimal.TEN);
		// 修改案件申请表信息
		brespbaseinfo.setNodeid(nodeid);			                                              // 流程节点退一步
		brespbaseinfo.setOperator(user.getUsername());                                         // 操作人
		dao.update(brespbaseinfo);
			
		// 4、记录流程日志
		// 删除回退之前的过程记录
		String protype = brespbaseinfo.getProtype();
		StringBuffer where = new StringBuffer();
		where.append(" caseid = '" + lawid + "'")
			 .append(" and protype = '").append(protype)
			 //.append("' and opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED)
			 .append("' and nodeid > ").append(nodeid);
		probaseinfoDao.deleteBySQL("PUB_PROBASEINFO", where.toString());
		
		// 修改回退后节点流程记录的处理标志为已接收,也就是未发送至下一节点还在编辑
		/*StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ")
			.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ")
			.append(" from PUB_PROBASEINFO t ")
			.append(" where t.caseid = '").append(caseid).append("'")
			.append(" and t.protype = '").append(protype).append("'")                     // 复议案件流程类型
			.append(" and t.nodeid = ").append(nodeid)                                    // 回退后节点
			.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		probaseinfo.setOpttype("1");
		// 修改过程日志
		probaseinfoDao.update(probaseinfo);*/
		
		String htyj = (String) param.get("htyj");
		// 已回退
		Probaseinfo probaseinfo_return = new Probaseinfo();
		probaseinfo_return.setNodeid(nodeid_old);
		probaseinfo_return = FlowUtil.genReturnedOperationData(protype, lawid, probaseinfo_return, user, "", htyj);
		probaseinfoDao.save(probaseinfo_return);
	}
	
	
	
	
	
	/**
	 * 
	 * @param speid
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String delete(String id) throws Exception {
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select t.opttype from PUB_PROBASEINFO t where t.caseid='"+id+"' ");
//		List<JSONObject> opttypeList = mapDataDao.queryListBySQL(sql.toString());
//		String opttype = (String) opttypeList.get(0).get("opttype");
//		
//		if("0".equals(opttype) || "2".equals(opttype) || "9".equals(opttype) ){
//			throw new AppException("此案件已在流程中，不允许删除");
//		 }
		try {
			BRespbaseinfo bRespbaseinfo = new BRespbaseinfo();
			bRespbaseinfo.setId(id);

			dao.delete(bRespbaseinfo);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}
	
	/**
	 * @Title: queryNoticeList
	 * @Description: 文书制作：已有文书分页查询
	 * @author ztt
	 * @date 2016年11月15日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryNoticeList(Map<String, Object> param) throws AppException {
		// 页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		// 起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		// 案件ID
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		// 流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		// 节点ID
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		// 受理流程类型只有01
//		if (Integer.valueOf(nodeid) < GCC.PRONODEBASEINFO_NODEID_RESRECEIVE) {
//			protype = GCC.PROBASEINFO_PROTYPE_XZFYAUDIT;
//		}
		// type: 01文书制作，02文书送达，03回访单，04备考表
		// 获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.tempid, t.caseid, t.noticename, t.noticecode, t.buildtime, t.operator, t.opttime")
		   .append(" from b_casenoticerelainfo t")
		   .append(" where t.caseid ='").append(caseid)
//		   .append("' and t.nodeid =").append(new BigDecimal(nodeid))
		   .append("' and t.protype ='").append(protype)
		   .append("' order by t.buildtime desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * @Title: queryNoticeTmpList
	 * @Description: 文书制作：文书模板分页查询
	 * @author ztt
	 * @date 2016年11月16日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public PaginationSupport queryNoticeTmpList(Map<String, Object> param) throws AppException {
		
		// 页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		// 起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		// 案件ID
		String id = StringUtil.stringConvert(param.get("id"));
		// 节点ID
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		// 流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		// 获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select tempid from b_casenoticerelainfo where caseid='").append(id)
		.append("' and nodeid =").append(new BigDecimal(nodeid))
		.append(" and protype ='").append(protype).append("'");
		
		// 查询已有文书所用模板
		List<Map<String, Object>> tempidList = mapDataDao.queryListBySQL(sql.toString());
		HashSet<String> tempidSet = new HashSet<String>();
		for (Map<String, Object> map : tempidList) {
			tempidSet.add((String)map.get("tempid"));
		}
		String tempids = generateStr(tempidSet);
		// type: 01文书制作，02文书送达，03回访单，04备考表
		sql.setLength(0);
		sql.append("select t.tempid, t.noticename, t.buildtime, t.operator, t.opttime ")
			.append(" from b_casenoticetempinfo t ")
			.append(" where t.protype ='").append(protype).append("'");
		if (StringUtil.isNotBlank(tempids)) {
			sql.append(" and t.tempid not in(").append(tempids).append(")");
		}
		sql.append(" order by t.buildtime desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentVal(String tempid, String id, String tableFlag)
	{
		String table = "b_casenoticerelainfo";  //获取自身模板信息
		String column = "id";
		if ("tmp".equals(tableFlag)) {
			table = "b_casenoticetempinfo";
			column = "tempid";
		}
		else if ("delivery".equals(tableFlag))
		{
			table = "b_noticedeliveryrelainfo";
			column = "tempid";
		}
		
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select contents from ").append(table).append(" where ").append(column).append("='").append(tempid).append("'");
		
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			Clob contents = (Clob)list.get(0).get("contents");
			if (null != contents) {
				xml = UtilTool.clobToString(contents);
			}
		}
		//
		
		if ("tmp".equals(tableFlag)) {
			// 查询案件信息
			BRespbaseinfo bRespbaseinfo = dao.get(id);
		}
		
		if ("delivery".equals(tableFlag) && StringUtils.isBlank(xml)) {
			sql.setLength(0);
//		 	sql.append("select contents from b_casenoticetempinfo where type ='02'");
		 	sql.append("select contents from b_casenoticetempinfo where tempid ='402880075ad9da44015ada460cc20004'");
		 	list = mapDataDao.queryListBySQL(sql.toString());
		 	if (null != list && !CollectionUtils.isEmpty(list))
		 	{
		 		 Clob contents = (Clob)list.get(0).get("contents");
				 if (null != contents) {
					 xml = UtilTool.clobToString(contents);
				 }
			 }
		 	System.out.println("xml--1--"+xml);
		 			// 查询案件信息
		 	       BRespbaseinfo bRespbaseinfo = dao.get(id);
		 				 			
		 	        Field [] fields = bRespbaseinfo.getClass().getDeclaredFields();
		 	        for(int i=0; i<fields.length; i++)
		 	        {
		 	            Field f = fields[i];
		 	            String name = f.getName();
		 	            String value = getFieldValue(bRespbaseinfo, name);
		 				String regex = "$"+name+"$";
		 				xml = xml.replace(regex, value);
		 	        }
		 	       System.out.println("xml--2--"+xml);
		 }
		System.out.println("sql-2---"+sql);
		return xml;
	}
	
	/**
	 * 查询第三人信息
	 * @author ztt
	 * @date 2016年11月10日 下午3:16:19
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> queryThirdInfo(String caseid)
	{
		// 查询第三人姓名和地址
		StringBuffer sql = new StringBuffer();
		sql.append("select thname, thaddress from b_thirdbaseinfo where caseid='").append(caseid).append("'");
		
		Map<String, Object> map = null;
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			map = list.get(0);
		}
		return map;
	}
	
	/**
     * 
     * 执行某个Field的getField方法
     * 
     * @param owner 类
     * @param fieldName 类的属性名称
     * @return 
     */
	private  String getFieldValue(Object owner, String fieldName)
	{
		Object value = invokeMethod(owner, fieldName, null);
		String result = "";
		if (null != value) {
			result = value.toString();
		} 
	    return result;
	}
    /**
     * 
     * 执行某个Field的getField方法
     * 
     * @param owner 类
     * @param fieldName 类的属性名称
     * @param args 参数，默认为null
     * @return 
     */
    private  Object invokeMethod(Object owner, String fieldName, Object[] args)
    {
        Class<? extends Object> ownerClass = owner.getClass();
        
        //fieldName -> FieldName  
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        
        Method method = null;
        try 
        {
            method = ownerClass.getMethod("get" + methodName);
        } 
        catch (SecurityException e) 
        {

            //e.printStackTrace();
        } 
        catch (NoSuchMethodException e) 
        {
            // e.printStackTrace();
            return "";
        }
        
        //invoke getMethod
        try
        {
            return method.invoke(owner);
        } 
        catch (Exception e)
        {
            return "";
        }
    }
	
	/**
	 * @Title: noticeDownload
	 * @Description: 文书制作：下载
	 * @author ztt
	 * @date 2016年11月21日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public Map<String, Object> noticeDownload(Map<String, Object> param) throws AppException {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		String id = StringUtil.stringConvert(param.get("id"));
		BCasenoticerelainfo relainfo = casenoticerelainfoDao.get(id);
		String contents = relainfo.getContents();
		String noticename = relainfo.getNoticename();
		String tmpName = UtilTool.getRandomString(16);
		BufferedWriter output =  null;
		File file = null;
		 try {
			 file = new File("E:/uploadFiles/" + tmpName + "Tmp.bak");
	        if (!file.getParentFile().exists()) {
	        	file.getParentFile().mkdirs();
			}
	        file.createNewFile();
	        output = new BufferedWriter(new FileWriter(file));
	        output.write(contents);
         } catch (IOException e) {
        	 throw new AppException("下载失败：" + e.getMessage());
         }
		 finally {
			 if (null != output) {
				 try {
					output.close();
				} catch (IOException e) {
					throw new AppException("下载失败：" + e.getMessage());
				}
			}
		}
		 resultMap.put("file", file);
		 resultMap.put("fileName", noticename);
		 return resultMap;
	}
	
	/**
	 * @Title: updateNoticeInfo
	 * @Description: 文书制作：保存或修改
	 * @author ztt
	 * @date 2016年11月17日
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateNoticeInfo(Map<String, Object> param) throws Exception{
		String id = StringUtil.stringConvert(param.get("id"));
		BCasenoticerelainfo noticerelainfo = casenoticerelainfoDao.get(id);
		if (null == noticerelainfo) {
			noticerelainfo = new BCasenoticerelainfo();
			param.remove("id");
		}
		BigDecimal nodeid = new BigDecimal((String)param.get("nodeid"));
		param.put("nodeid", nodeid);
		BeanUtils.populate(noticerelainfo, param);
		// 01文书制作，02文书送达，03回访单，04备考表
		noticerelainfo.setType("01");
		noticerelainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		noticerelainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		//操作人信息
		SysUser user = SecureUtil.getCurrentUser();
		noticerelainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(noticerelainfo);
	}
	/**
	 * @Title: deleteNoticeInfoByCaseid
	 * @Description: 文书制作：删除
	 * @author ztt
	 * @date 2016年11月17日
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteNoticeInfoByCaseid(Map<String, Object> param) throws Exception{
		String id = StringUtil.stringConvert(param.get("id"));
		casenoticerelainfoDao.delete(id);
	}
    
	/**
	 * 结案归档
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyCaseEndNoticePlaceFlow(String id) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(id)){
			
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		 BRespbaseinfo bRespbaseinfo = dao.get(id);
		if(bRespbaseinfo == null){
			throw new AppException("未找到对应的行政应诉案件");
		}
		
		String protype = bRespbaseinfo.getProtype();
		BigDecimal nodeid = bRespbaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至结案归档");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				id, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = nodeid.add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + id + "'");
		logSql.append(" and t.protype = '" + protype + "'");
		logSql.append(" and t.nodeid = " + newNodeid);
//		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
				id, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		bRespbaseinfo.setNodeid(new BigDecimal(99));                                         // 节点加1
		bRespbaseinfo.setOperator(user.getUsername());                                         // 操作人
		dao.update(bRespbaseinfo);
	}
	
	/**
	 * @Title: queryPronodebaseinfoByUserid
	 * @Description: 判断角色是否存在第一节点的权限
	 * @author czp
	 * @date 2017年3月25日 上午10:24:43
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int queryPronodebaseinfoByUserid(){
		
		// 获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(id) nodeidcount from pub_pronodebaseinfo ");
		sql.append(" where protype = '").append(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO).append("'");
		sql.append(" and nodeid = ").append(GCC.RESPBASEINFO_NODEID_MAINTAIN);
		sql.append(" and roleid in (select roleid from sys_user_role where userid = ")
		   .append(user.getUserid()).append(")");
		int count = 0;
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(sql.toString());
		if (probaseins != null && probaseins.size() > 0){
			
			count = probaseins.get(0).getIntValue("nodeidcount");
		}
		
		return count;
	}
	
	
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	
	@SuppressWarnings("rawtypes")
	public  List queryNodeidsByCase(Map<String, Object> param){
		String id = StringUtil.stringConvert(param.get("id"));
		String protype = StringUtil.stringConvert(param.get("protype"));
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		
		StringBuffer logSql = new StringBuffer();
			logSql.append("select distinct t.nodeid ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.caseid = '" + id + "'");
			logSql.append(" and t.protype = '" + protype + "'"); 
			logSql.append(" and t.nodeid <= '" + nodeid + "'");
			logSql.append(" order by t.nodeid desc");
		List prolist = mapDataDao.queryListBySQL(logSql.toString());
		
		return prolist;
	}
	
	/**
	 * @Title: queryProbaseinfoListByParam
	 * @Description: 查询流程列表信息（通过案件ID）
	 * @author ybb
	 * @date 2016年8月15日 下午3:23:58
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryProbaseinfoListByParam(String id, String protype) throws AppException{
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PRONODEBASEINFO_NODENAME' and a.status = 0 and a.code = nvl(t.nodeid,0)) nodeid_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PROBASEINFO_OPTTYPE' and a.status = 0 and a.code = t.opttype) opttype_mc ");
		sql.append(" from pub_probaseinfo t ");
		sql.append(" where t.caseid = '" + id + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.nodeid asc, t.opttype desc");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: queryNodeidByCaseid
	 * @Description: 根据案件ID查询案件当前节点号
	 * @author ybb
	 * @date 2016年9月6日 下午2:22:35
	 * @param caseid
	 * @return
	 * @throws AppException
	 */
	public BigDecimal queryNodeidById(String id) throws AppException{
		
		//节点编号
		BigDecimal nodeid = null;
		
		//根据案件ID查询
		BRespbaseinfo brespbaseinfo = dao.get(id);
		if (brespbaseinfo == null) {
			nodeid = BigDecimal.ONE;
		}
		if (brespbaseinfo.getNodeid() == null){
			nodeid = BigDecimal.ONE;
		}
		
		//查询该案件是否已完结
			//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_RESULTBASEINFO t ");
		sql.append(" where t.caseid = '" + id + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		
		List<?> probaseinfoList = probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {	//已完结
			nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAREND);
		} else {
			nodeid = brespbaseinfo.getNodeid();
		}
		
		return nodeid;
	}
	
}

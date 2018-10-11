package com.wfzcx.aros.xzfy.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.base.filemanage.po.SysFileManage;
import com.jbf.common.TableNameConst;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.paramCfg.component.ParamCfgComponent;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.dao.ResultbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.po.Resultbaseinfo;
import com.wfzcx.aros.print.po.NoticeContentInfo;
import com.wfzcx.aros.sqbl.dao.ApplyRecordDao;
import com.wfzcx.aros.util.FlowUtil;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.xzfy.dao.BCaseaccbasisinfoDao;
import com.wfzcx.aros.xzfy.dao.BCasenoticerelainfoDao;
import com.wfzcx.aros.xzfy.dao.BCasesperelabaseinfoDao;
import com.wfzcx.aros.xzfy.dao.BNoticedeliveryrelainfoDao;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.dao.ThirdbaseinfoDao;
import com.wfzcx.aros.xzfy.po.AcceptNoticeInfo;
import com.wfzcx.aros.xzfy.po.BCaseaccbasisinfo;
import com.wfzcx.aros.xzfy.po.BCasenoticerelainfo;
import com.wfzcx.aros.xzfy.po.BCasesperelabaseinfo;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.po.Thirdbaseinfo;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;
import com.wfzcx.aros.zjgl.dao.BGroupbaseinfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpegrouprelainfoDAO;
import com.wfzcx.aros.zjgl.po.BGroupbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpegrouprelainfo;

/**
 * @ClassName: CasebaseinfoService
 * @Description: 用来处理行政复议案件具体业务
 * @author ybb
 * @date 2016年8月12日 上午9:35:54
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.CasebaseinfoService")
public class CasebaseinfoService {

	@Autowired
	private CasebaseinfoDao casebaseinfoDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private FileManageComponent fileManageComponent;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private ThirdbaseinfoDao thirdbaseinfoDao;
	@Autowired
	private ResultbaseinfoDao resultbaseinfoDao;
	@Autowired
	private BCasesperelabaseinfoDao casespeDao;
	@Autowired
	private ParamCfgComponent pcfg;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private BGroupbaseinfoDAO bGroupbaseinfoDAO;
	@Autowired
	private BCasesperelabaseinfoDao bCasesperelabaseinfoDao;
	@Autowired
	private BSpegrouprelainfoDAO bSpegrouprelainfoDAO;
	@Autowired
	private SysFileManageDao sysFileManageDao;
	@Autowired
	private BCaseaccbasisinfoDao accbasisDao;
	@Autowired
	private BCasenoticerelainfoDao casenoticerelainfoDao;
	@Autowired
	private BNoticedeliveryrelainfoDao noticedeliveryrelainfoDao;
	@Autowired
	private ApplyRecordDao applyRecordDao;
	
	/**
	 * @Title: queryXzfyList
	 * @Description: 行政复议案件：分页查询
	 * @author ztt
	 * @date 2016年11月29日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryXzfyList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.*, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");
		sql.append(" from B_CASEBASEINFO t");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append(" where t.region = '" + user.getArea() + "'");
		} else {
			sql.append(" where t.region is null");
		}
		sql.append(" and t.protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("' ");
		String nodeids = queryNodeidsByUserid(user.getUserid());
		sql.append(" and t.caseid not in (select caseid from pub_resultbaseinfo where protype != '")
		   .append(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND).append("')")
		   .append(" and t.nodeid in (").append(nodeids).append(")");
		
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
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		// 申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		// 被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		// 申请人类型
		String apptype = StringUtil.stringConvert(param.get("apptype"));
		if (!StringUtils.isBlank(apptype)) {
			sql.append(" and t.apptype = '").append(apptype).append("'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Title: xzfyReceiveSave
	 * @Description: 行政复议接收材料：保存
	 * @author ztt
	 * @date 2016年11月8日
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String updateXzfyReceiveByCaseid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		//3、业务逻辑处理
		//获取行政复议申请PO，并将页面参数转化到该对象中
		Casebaseinfo casebaseinfo = null;
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String nodeid = String.valueOf(GCC.PRONODEBASEINFO_NODEID_REQRECEIVE);
		if (StringUtil.isNotBlank(caseid)) {
			casebaseinfo = queryCasebaseinfoByCaseid(caseid);
		} else {
			casebaseinfo = new Casebaseinfo();
		}
		BeanUtils.populate(casebaseinfo, param);
		
		//获取系统当前登录用户信息，并将用户信息转到到行政复议申请PO中
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		casebaseinfo.setState(GCC.RCASEBASEINFO_STATE_REQ);                                   // 状态：收案未立案
		casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);							  // 01_案件审批
		casebaseinfo.setNodeid(new BigDecimal(nodeid));        // 1/11_接收材料
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		if("appapply".equals(user.getUsercode())){
			casebaseinfo.setRegion((String)param.get("orgcode"));
		}else{
			casebaseinfo.setRegion(user.getArea());												  // 所属区域
		}
		casebaseinfo.setAppdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 申请日期
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		
		//新增或行政复议申请
		if (StringUtil.isBlank(caseid)) {
			caseid = (String)casebaseinfoDao.save(casebaseinfo);
		} else {
			casebaseinfoDao.update(casebaseinfo);
		}
		
		// 设置笔录中的caseid
		String arid = (String) param.get("arid");
		String clearSql = "update B_APPLYRECORD set caseid='' where caseid='" +caseid +"'";
		if(StringUtils.isEmpty(arid)){
			applyRecordDao.updateBySql(clearSql);
		}else{
			String setCaseSql = "update B_APPLYRECORD set caseid='" + caseid + "' where arid='"+ arid +"'";
			applyRecordDao.updateBySql(clearSql);
			applyRecordDao.updateBySql(setCaseSql);
		}
		//4、记录流程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");  // 复议审批
		logSql.append(" and t.nodeid = " + nodeid);                                     // 接收材料
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		Probaseinfo probaseinfo = new Probaseinfo();
		if (null != probaseins && !probaseins.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		}
		//转化提交操作日志
		probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, new BigDecimal(nodeid),
				caseid, probaseinfo, user, null, null, null);
		//新增或修改流程日志
		probaseinfoDao.saveOrUpdate(probaseinfo);
		
		return caseid;
	}
	
	/**
	 * @Title: xzfyReceiveFlow
	 * @Description: 行政复议接收材料：发送
	 * @author ztt
	 * @date 2016年11月8日
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyReceiveFlow(Map<String, Object> param) throws AppException {
		
		String caseid = (String) param.get("caseid");
		String csaecode= (String) param.get("csaecode");
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}

		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");  // 复议审批
		logSql.append(" and t.nodeid = " + nodeid);                                     // 接收材料
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED + "'");  // 已提交
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存案件编号，请保存后再发送");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		//
		if(!StringUtils.isEmpty(csaecode)){
			casebaseinfo.setCsaecode(csaecode);
		}
		casebaseinfo.setNodeid(nodeid.add(BigDecimal.TEN));                                   // 案件登记或审理承办人
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);                          // 已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());										  // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
		// 设置笔录中的caseid
		String arid = (String) param.get("arid");
		String clearSql = "update B_APPLYRECORD set caseid='' where caseid='" +caseid +"'";
		if(StringUtils.isEmpty(arid)){
			applyRecordDao.updateBySql(clearSql);
		}else{
			String setCaseSql = "update B_APPLYRECORD set caseid='" + caseid + "' where arid='"+ arid +"'";
			applyRecordDao.updateBySql(clearSql);
			applyRecordDao.updateBySql(setCaseSql);
		}
	}
	
	/**
	 * @Title: queryFileList
	 * @Description: 附件列表
	 * @author ztt
	 * @date 2016年11月8日
	 * @param fjkeyid
	 */
	@SuppressWarnings("unchecked")
	public List<?> queryFileList(String fjkeyid) {
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		return fileList;
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
		
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		String code = "SQCL";
		// 答复材料
		if (GCC.PRONODEBASEINFO_NODEID_RESRECEIVE == Integer.valueOf(nodeid)) {
			code = "DFCL";
		}
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select itemid, filename, createtime from SYS_FILEMANAGE where keyid = '")
			.append(caseid)
			.append("' and elementcode = '").append(code)
			.append("' order by createtime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: queryXzfyReqList
	 * @Description: 行政复议案件登记：分页查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryXzfyReqList(Map<String, Object> param) throws AppException {
		
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
		sql.append(" from B_CASEBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		// 流程类型,节点编号案件登记
		/*sql.append(" and t.caseid not in(select caseid from pub_resultbaseinfo where protype != '")
		   .append(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND).append("')")
		   .append(" and t.nodeid < ").append(GCC.PRONODEBASEINFO_NODEID_HEAR);*/
		sql.append(" and t.nodeid <= ").append(GCC.PRONODEBASEINFO_NODEID_REQ);
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (!StringUtils.isBlank(opttype)) {
			sql.append(" and t.opttype = '").append(opttype).append("'");
		} 
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
				
		sql.append(" order by t.lasttime desc ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateXzfyReqByCaseid
	 * @Description: 行政复议案件登记： 保存
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyReqByCaseid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件登记失败：案件ID为空");
		}
		
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("案件登记失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("案件登记失败：该案件不是【行政复议审批】");
		}
		//3、业务逻辑处理
		//转化页面表单属性
		if (null == param.get("amount") || StringUtils.isEmpty((param.get("amount").toString()))) {
			param.put("amount", BigDecimal.ZERO);
			BeanUtils.populate(casebaseinfo, param);
			casebaseinfo.setAmount(null);
		}else{
			BeanUtils.populate(casebaseinfo, param);
		}
		
		//转化操作人信息
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);                            // 已接收
		casebaseinfo.setAppdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));			  // 申请日期
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());										  // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		String key = UtilTool.getRandomString(5);
		casebaseinfo.setKey(key);               												//查询码
		
		//修改行政复议案件登记信息
		casebaseinfoDao.update(casebaseinfo);
		
		//4、记录流程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_REQ + "");
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		//流程日志未记录，更新流程日志_已接收
		Probaseinfo probaseinfo = new Probaseinfo();
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		}
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REQ),
				caseid, probaseinfo, user);
		probaseinfoDao.saveOrUpdate(probaseinfo);
	}
	
	/**
	 * @Title: delXzfyReqByCaseid
	 * @Description: 行政复议申请： 删除行政复议申请
	 * @author ybb
	 * @date 2016年8月12日 下午4:38:45
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delXzfyReqByCaseid(String caseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		//判断该案件处理标志不是【已提交】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("修改失败：该案件不是【行政复议审批】");
		}
		if(!(casebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				casebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	//判断处理标志是否是【已提交】或【已退回】
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}
	
		//3、业务逻辑处理		
			//删除新增复议申请信息
		String where = "caseid = '" + caseid + "'";
		casebaseinfoDao.deleteBySQL("b_casebaseinfo", where);
		
		//判断是否上传了附件
		//上传，删除上传附件
		//未上传，不做处理
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + caseid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			//删除上传附件
			fileManageComponent.deleteFilesByKeyId(caseid);
		}
		
		// 删除案件时  删除案件小组关系表中相应数据
		String sql = " select * from b_casesperelabaseinfo t where t.caseid = '"+caseid+"' ";
		List<?> list = casespeDao.findVoBySql(sql, BCasesperelabaseinfo.class);
		if(list != null && list.size() > 0) {
			for(Object obj: list) {
				BCasesperelabaseinfo rela = (BCasesperelabaseinfo)obj;
				casespeDao.delete(rela);
			}
		}
	}
	
	/**
	 * @Title: xzfyReqFlow
	 * @Description: 行政复议案件登记：发送
	 * @author ztt
	 * @date 2016年11月2日 
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyReqFlow(Map<String, Object> param) throws AppException {
		
		String caseid = (String) param.get("caseid");
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}
		
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'"); // 复议审批
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_REQ);
		logSql.append(" order by t.endtime desc");
		System.out.println("--sql:"+logSql);
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存案件登记，请保存后再发送");
		}
		
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REQ),
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = casebaseinfo.getNodeid().add(BigDecimal.ONE);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT),
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT));            // 3_受理承办人审批
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: queryXzfyAcceptList
	 * @Description: 行政复议受理：分页查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryXzfyAcceptList(Map<String, Object> param) throws AppException {
		
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
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");
		} else {
			sql.append("where t.region is null ");
		}
		
		// 复议审批
		// sql.append(" and protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("'");
		
		// 处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			sql.append(" and t.nodeid > " + param.get("nodeid")); // 大于受理承办人/科室/机构/机关当前节点
		} else { //未处理
			sql.append(" and t.nodeid = " + param.get("nodeid"))
				.append(" and t.opttype != '3'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Title: updateXzfyAcceptByCaseid
	 * @Description: 行政复议受理：保存
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyAcceptByCaseid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String slcasesort = StringUtil.stringConvert(param.get("slcasesort"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		casebaseinfo.setSlcasesort(slcasesort);                	                               // 案件流程类型
		if (StringUtils.isEmpty(slcasesort)){
			String casesort = StringUtil.stringConvert(param.get("casesort"));
			casebaseinfo.setCasesort(casesort);
			casebaseinfo.setSlcasesort(casesort);
		}
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);							  // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交受理");
		}
		
		BigDecimal nodeid = casebaseinfo.getNodeid();
		String remark = "";
		if (GCC.PRONODEBASEINFO_NODEID_ACCEPT == nodeid.intValue())
		{
			//承办人处理意见
			remark = StringUtil.stringConvert(param.get("agentRemark"));
			// 查询当前案件受理依据
			StringBuilder basisSql = new StringBuilder();
			basisSql.append("select t.basisid from b_caseaccbasisinfo t")
					.append(" where t.caseid = '").append(caseid).append("'");
			List<JSONObject> list = mapDataDao.queryListBySQL(basisSql.toString());
			// 更新
			if (null != list && !list.isEmpty()) {
				BCaseaccbasisinfo accbasis = accbasisDao.get(list.get(0).getString("basisid"));
				BeanUtils.populate(accbasis, param);
				accbasisDao.update(accbasis);
			} else { // 新增
				BCaseaccbasisinfo accbasis = new BCaseaccbasisinfo();
				BeanUtils.populate(accbasis, param);
				accbasis.setProcessid(probaseinfoList.get(0).getString("processid"));
				accbasisDao.save(accbasis);
			}
		}
		else if (GCC.PRONODEBASEINFO_NODEID_ACCSECTION == nodeid.intValue())
		{
			//科室处理意见
			remark = StringUtil.stringConvert(param.get("sectionRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_ACCORGAN == nodeid.intValue())
		{
			//机构处理意见
			remark = StringUtil.stringConvert(param.get("organRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_ACCOFFICE == nodeid.intValue())
		{
			//机关处理意见
			remark = StringUtil.stringConvert(param.get("officeRemark"));
		}
		
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		probaseinfo.setRemark(remark);
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				nodeid, caseid, probaseinfo, user);
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Title: xzfyAcceptFlow
	 * @Description: 行政复议受理：发送
	 * @author ztt
	 * @date 2016年11月2日
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyAcceptFlow(Map<String, Object> param) throws AppException {
		String caseid = (String) param.get("caseid");
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}

		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'"); // 复议审批
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		if(null == user){
			user = sysUserDao.get(Long.valueOf((String)param.get("userid")));
		}
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, casebaseinfo.getNodeid(),
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		
		// 节点加10，新增流程日志_已提交
		BigDecimal newNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(casebaseinfo.getNodeid().add(BigDecimal.TEN));                 // 节点加1
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	
	public  List queryNodeidsByCase(Map<String, Object> param){
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String protype = StringUtil.stringConvert(param.get("protype"));
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		String ccrid = StringUtil.stringConvert(param.get("ccrid"));
		
		StringBuffer logSql = new StringBuffer();
	
		if(caseid.equals("-1")){
			logSql.append("select distinct t.nodeid ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.processid = '" + ccrid + "'");
			logSql.append(" and t.protype = '" + protype + "'"); 
			logSql.append(" and t.nodeid <= '" + nodeid + "'");
			logSql.append(" order by t.nodeid desc");
		}else{
			logSql.append("select distinct t.nodeid ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.caseid = '" + caseid + "'");
			logSql.append(" and t.protype = '" + protype + "'"); 
			logSql.append(" and t.nodeid <= '" + nodeid + "'");
			logSql.append(" order by t.nodeid desc");
		}
//		System.out.println("logsql---"+logSql);	    
		
		List prolist = mapDataDao.queryListBySQL(logSql.toString());
		
		return prolist;
	}
	/**
	 * @Title: queryXzfyAccendList
	 * @Description: 行政复议受理决定：分页查询
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryXzfyAccendList(Map<String, Object> param) throws AppException {
		
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
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");
		} else {
			sql.append("where t.region is null ");
		}
		
		// 复议审批
		sql.append(" and protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("'");
		
		// 处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			sql.append(" and t.nodeid > " + GCC.PRONODEBASEINFO_NODEID_ACCEND); // 大于受理决定节点
		} else { //未处理
			sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_ACCEND);
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateXzfyAccendByCaseid
	 * @Description: 行政复议受理决定：保存
	 * @author ztt
	 * @date 2016年11月2日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyAccendByCaseid(Map<String, Object> param, JSONObject remarks) throws Exception{
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		//判断处理结果是否为空
		//判断处理结果为【01 受理】、【02 不予受理】、【 03 转送】、【04 补正】
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择处理结果");
		}
	
		//2、业务逻辑判断
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 3、业务逻辑处理
		// 更新案件处理标志为：1_已接收
		casebaseinfo.setIsgreat(StringUtil.stringConvert(param.get("isgreat")));              // 是否重大案件
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);							  // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");  // 复议审批
		sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_ACCEND);          // 受理决定
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交受理决定");
		}
		else {
			Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
			probaseinfo.setResult(result); // 受理结果
			probaseinfo.setRemark(StringUtil.stringConvert(param.get("remark"))); // 说明
			probaseinfo.setSendunit(StringUtil.stringConvert(param.get("sendunit")));
			probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				 new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEND), caseid, probaseinfo, user);
			//修改过程日志_已接收
			probaseinfoDao.update(probaseinfo);
		}
		// 生成受理审批表
		generateAcceptNotice(caseid, "05", "", casebaseinfo, user, remarks);
		// 受理通知书
		createDoc("16",casebaseinfo, user);
	}
	
	/**
	 * 生成提出行政复议答复通知书
	 * //TODO
	 */
	public void generateAcceptReplyNotice(Casebaseinfo casebaseinfo, SysUser user) {}

	/**
	 * @Title: xzfyAccendFlow
	 * @Description: 行政复议受理决定：发送
	 * @author ztt
	 * @date 2016年11月2日 
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyAccendFlow(String caseid) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}

		//判断该案件是否已提交
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_ACCEND);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
		
		//处理结果
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
				
		//判断是否生成了通知书
		String flag = queryNoticebaseinfo(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, casebaseinfo.getNodeid());
		if (flag != null) {
			throw new AppException(flag);
		}
		
		//3、业务逻辑处理
		//4、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo.setResult(result);
		probaseinfo.setRemark(StringUtil.stringConvert(probaseins.get(0).get("remark"))); // 说明
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED);
		// 更新流程日志_受理决定_已处理
		probaseinfoDao.update(probaseinfo);
		
		// 修改案件节点信息    已提交
		BigDecimal caseNextNodeid = null;
		if ("01".equals(result)){
			// 受理时向下节点走
			caseNextNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		}else if("04".equals(result)){
			// 材料补正转到接收申请材料节点
			caseNextNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REQRECEIVE);
		}else{
			// 转送 不于受理时直接结束案件
			caseNextNodeid = new BigDecimal(GCC.END_NODEID);
		}

		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" and t.nodeid = " + caseNextNodeid);
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, caseNextNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		// 新增结果日志_文书制作_已提交
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		casebaseinfo.setNodeid(caseNextNodeid);                                                // 8_受理制作文书
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: queryXzfyList
	 * @Description: 行政复议审理：分页查询
	 * @author ztt
	 * @date 2016年11月3日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryXzfyReviewList(Map<String, Object> param) throws AppException {
		
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
		sql.append(" from B_CASEBASEINFO t ");
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append(" where t.region = '" + user.getArea() + "' ");	
		} else {
			sql.append(" where t.region is null ");	
		}
		
		// 节点编号
		BigDecimal nodeid = new BigDecimal(StringUtil.stringConvert(param.get("nodeid")));
		
		// 处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			sql.append(" and t.nodeid > ").append(nodeid); // 大于审理承办人/科室/机构/机关当前节点
		} else { //未处理
			if (GCC.PRONODEBASEINFO_NODEID_HEAR == nodeid.intValue()) {
				sql.append(" and t.caseid not in(select caseid from pub_resultbaseinfo where protype != '")
				   .append(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND).append("')")
				   .append( "and nodeid >= ").append(GCC.PRONODEBASEINFO_NODEID_HEAR);
			}
			else
			{
				sql.append(" and t.nodeid = ").append(nodeid);
			}
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateXzfyReviewByCaseid
	 * @Description: 行政复议审理：保存
	 * @author ztt
	 * @date 2016年11月3日 
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyReviewByCaseid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		//2、业务逻辑判断
		//判断该案件是不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if (casebaseinfo == null) {
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		
		// 审结结果
		String result = StringUtil.stringConvert(param.get("result")).replace("n", "");
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择审结结果");
		}
		
		// 流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		String protype_old = casebaseinfo.getProtype();
		// 节点编号
		BigDecimal nodeid = new BigDecimal((String)param.get("nodeid"));
		// 流程信息
		Probaseinfo probaseinfo = new Probaseinfo();
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.nodeid = " + nodeid);
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		if (null != probaseinfoList && !probaseinfoList.isEmpty()) {
			probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		}
		// 判断该案件是否在更改流程, 若变更流程新发起一个流程
		if (!protype.equals(protype_old)) {
			// 判断该案件是不是已经中止
			if (GCC.PROBASEINFO_PROTYPE_XZFYRECOVER.equals(protype))
			{
				if (!GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND.equals(protype_old) || !GCC.PROBASEINFO_OPTTYPE_END.equals(casebaseinfo.getOpttype())) {
					throw new AppException("保存失败：该案件未中止");
				}
			}
			else if (!GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype_old))//判断该案件是不是在途其他流程
			{
				throw new AppException("保存失败：该案件正在审批其他流程");
			}
			nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_HEAR);
		}
		else {
			if (nodeid.intValue() == GCC.PRONODEBASEINFO_NODEID_HEAR && casebaseinfo.getNodeid().intValue() != GCC.PRONODEBASEINFO_NODEID_HEAR) {
				throw new AppException("保存失败：该案件正在审批中");
			}
		}
		
		// 审理意见
		String remark = "";
		if (GCC.PRONODEBASEINFO_NODEID_HEAR == nodeid.intValue())
		{
			//承办人处理意见
			remark = StringUtil.stringConvert(param.get("agentRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_HEARSECTION == nodeid.intValue())
		{
			//科室处理意见
			remark = StringUtil.stringConvert(param.get("sectionRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_HEARORGAN == nodeid.intValue())
		{
			//机构处理意见
			remark = StringUtil.stringConvert(param.get("organRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_HEAROFFICE == nodeid.intValue())
		{
			//机关处理意见
			remark = StringUtil.stringConvert(param.get("officeRemark"));
		}
		
		// 判断是否输入审理意见
		if (StringUtils.isBlank(remark)) {
			throw new AppException("保存失败：请输入审理意见");
		}
		
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//3、业务逻辑处理
		// 延期天数
		String delaydays = StringUtil.stringConvert(param.get("delaydays"));
		casebaseinfo.setProtype(protype);														// 流程类型
		casebaseinfo.setNodeid(nodeid);															// 节点
		casebaseinfo.setDelaydays(delaydays);													// 延期天数
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED); 							    // 处理标志：1_已接收
		casebaseinfo.setUserid(user.getUserid());											    // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                           // 操作人
		casebaseinfo.setRegion(user.getArea());												    // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	            // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));    // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		probaseinfo.setRemark(remark);
		probaseinfo.setResult(result);
		probaseinfo = FlowUtil.genAcceptedOperationData(protype, nodeid, caseid, probaseinfo, user);
		// 新增或更新过程日志_已接收
		probaseinfoDao.saveOrUpdate(probaseinfo);
	}
	
	/**
	 * @Title: xzfyReviewFlow
	 * @Description: 行政复议审理：发送
	 * @author ztt
	 * @date 2016年11月3日
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyReviewFlow(String caseid, String protype) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}

		//3、记录结果日志
		// 获取过程日志
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议审理，请保存后再发送");
		}
		
		//处理结果
		String remark = probaseins.get(0).getString("remark");
		if (StringUtils.isBlank(remark)) {
			throw new AppException("未保存复议审理，请保存后再发送");
		}
				
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, casebaseinfo.getNodeid(),
				caseid, probaseinfo, user, probaseinfo.getResult(), probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
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
		// 新增审理决定流程时清空审理决定结果
		if (GCC.PRONODEBASEINFO_NODEID_HEAROFFICE == casebaseinfo.getNodeid().intValue()) {
			new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
					caseid, new_probaseinfo, user, null, null, null);
		} else {
			new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
					caseid, new_probaseinfo, user, probaseinfo.getResult(), null, probaseinfo.getReason());
		}
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(casebaseinfo.getNodeid().add(BigDecimal.ONE));                 // 节点加1
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);                           // 已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: queryXzfyDecisionList
	 * @Description: 行政复议审理决定：分页查询
	 * @author ztt
	 * @date 2016年11月3日
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryXzfyDecisionList(Map<String, Object> param) throws AppException {
		
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
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	
		} else {
			sql.append("where t.region is null ");	
		}
		
		// 处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			sql.append(" and t.nodeid > " + GCC.PRONODEBASEINFO_NODEID_HEAREND); // 大于审理决定节点
		} else { //未处理
			sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_HEAREND); // 审理决定节点
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateXzfyDecisionByCaseid
	 * @Description: 行政复议审理决定：保存
	 * @author ztt
	 * @date 2016年11月3日 
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyDecisionByCaseid(Map<String, Object> param, JSONObject remarks) throws Exception{
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		//判断决定类型是否为空
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择审批结果");
		}
		
		//判断决定类型是否为空
		String remark = StringUtil.stringConvert(param.get("remark"));
		if (StringUtils.isBlank(remark)) {
			throw new AppException("保存失败：请输入审批结果说明");
		}
				
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		
		// 流程类型
		String protype = casebaseinfo.getProtype();
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//3、业务逻辑处理
		// 更新案件处理标志为：1_已接收
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);							  // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_DECISION);
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交审理决定");
		}
		else {
			Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
			probaseinfo.setResult(result);
			probaseinfo.setRemark(remark);
			probaseinfo = FlowUtil.genAcceptedOperationData(protype, probaseinfo.getNodeid(), caseid, probaseinfo, user);
			
			// 修改过程日志_已接收
			probaseinfoDao.update(probaseinfo);
		}
		
		// 生成决定审批表
		generateAcceptNotice(caseid, "06", result, casebaseinfo, user, remarks);
	}
	
	/**
	 * @Title: xzfyDecisionFlow
	 * @Description: 行政复议审理决定：发送
	 * @author ztt
	 * @date 2016年11月3日 
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyDecisionFlow(String caseid) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		// 流程类型
		String protype = casebaseinfo.getProtype();

		//4、记录结果日志
		//获取过程日志
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_DECISION);
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议决定，请保存后再发送");
		}
		
		//处理结果
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未保存复议决定，请保存后再发送");
		}
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 流程信息
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED);
		// 更新流程日志_审理决定_已处理
		probaseinfoDao.update(probaseinfo);
		
		// 新增结果日志_审理文书制作_已提交
		BigDecimal nextNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
		logSql.append(" and t.nodeid = " + nextNodeid);
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, nextNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		casebaseinfo.setNodeid(nextNodeid);           // 审理文书制作
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
		
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
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		// 节点ID
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		// 流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		// 获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select tempid from b_casenoticerelainfo where caseid='").append(caseid)
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
		if (Integer.valueOf(nodeid) < GCC.PRONODEBASEINFO_NODEID_RESRECEIVE) {
			protype = GCC.PROBASEINFO_PROTYPE_XZFYAUDIT;
		}
		// type: 01文书制作，02文书送达，03回访单，04备考表
		// 获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.tempid, t.caseid, t.noticename, t.noticecode, t.buildtime, t.operator, t.opttime")
		   .append(" from b_casenoticerelainfo t")
		   .append(" where t.caseid ='").append(caseid)
//		   .append("' and t.nodeid =").append(new BigDecimal(nodeid))
		   .append("' and t.protype ='").append(protype)
		   .append("' order by t.buildtime desc");
	System.out.println("已有文书列表----------"+sql);	
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
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
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentVal(String tempid, String caseid, String tableFlag)
	{
		String table = "b_casenoticerelainfo";  //获取自身模板信息
		String column = "id";
		System.out.println("tableFlag---1:"+tableFlag);
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
			Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
			// 第三人信息
			Map<String, Object> thirdinfo = queryThirdInfo(caseid);
			NoticeContentInfo info = getNoticeContentInfoBean(casebaseinfo, thirdinfo);
			
	        Field [] fields = info.getClass().getDeclaredFields();
	        for(int i=0; i<fields.length; i++)
	        {
	            Field f = fields[i];
	            String name = f.getName();
	            String value = getFieldValue(info, name);
				String regex = "$"+name+"$";
				xml = xml.replace(regex, value);
	        }
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
		 			Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		 				 			
		 	        Field [] fields = casebaseinfo.getClass().getDeclaredFields();
		 	        for(int i=0; i<fields.length; i++)
		 	        {
		 	            Field f = fields[i];
		 	            String name = f.getName();
		 	            String value = getFieldValue(casebaseinfo, name);
		 				String regex = "$"+name+"$";
		 				xml = xml.replace(regex, value);
		 	        }
		 	       System.out.println("xml--2--"+xml);
		 }
		System.out.println("sql-2---"+sql);
		return xml;
	}
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getClobContentValue(String caseid, String nodeid, String protype, String typeFlag)
	{
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select contents from b_casenoticerelainfo where caseid='").append(caseid);
		if("transregist".equals(typeFlag)){
			sql.append("' and type = '11' ");
		}else if("decisiondoc".equals(typeFlag)){
			sql.append("' and type = '12' ");
		}else{
			sql.append("' and nodeid = ").append(new BigDecimal(nodeid));
		}
		sql.append(" and protype ='").append(protype).append("'");
		
		String xml = "";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			Clob contents = (Clob)list.get(0).get("contents");
			if (null != contents) {
				xml = UtilTool.clobToString(contents);
			}
		}
		
		if (StringUtils.isBlank(xml)) {
			String type = "03";
			if ("summary".equals(typeFlag)) {
				type = "04";
			}else if("transregist".equals(typeFlag)){
				type = "11";
			}else if("decisiondoc".equals(typeFlag)){
				type = "12";
			}
			sql.setLength(0);
		 	sql.append("select contents from b_casenoticetempinfo where type ='").append(type).append("'");
		 	list = mapDataDao.queryListBySQL(sql.toString());
		 	if (null != list && !CollectionUtils.isEmpty(list))
		 	{
		 		 Clob contents = (Clob)list.get(0).get("contents");
				 if (null != contents) {
					 xml = UtilTool.clobToString(contents);
					 if(!StringUtils.isEmpty(caseid)){
						 Casebaseinfo info =  getCaseInfoByCaseId(caseid);
						 if(info != null){
							xml=  xml.replace("$casecode$", info.getCsaecode());
						 }
					 }
				 }
			 }
		 }
		 
		return xml;
	}
	
	/**
	 * 获取clob字段内容
	 */
	@SuppressWarnings("unchecked")
	public String getContentsForDetail(String tempid, String tableFlag)
	{
		String table = "b_casenoticerelainfo";
		String column = "id";
		if ("delivery".equals(tableFlag))
		{
			table = "b_noticedeliveryrelainfo";
			column = "tempid";
		}
		
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select contents from ").append(table).append(" where ").append(column).append("='").append(tempid).append("'");
		
		String xml = "无信息";
		List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
		if (null != list && !CollectionUtils.isEmpty(list))
		{
			Clob contents = (Clob)list.get(0).get("contents");
			if (null != contents) {
				xml = UtilTool.clobToString(contents);
			}
		}
		return xml;
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
	 * @Title: xzfyNoticeFlow
	 * @Description: 文书制作：发送
	 * @author ztt
	 * @date 2016年11月17日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyNoticeFlow(String caseid) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至文书制作");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = nodeid.add(BigDecimal.ONE);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(nodeid.add(BigDecimal.ONE));                                   // 节点加1
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: updateDelivery
	 * @Description: 文书送达：保存或修改
	 * @author ztt
	 * @date 2016年11月21日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateDelivery(Map<String, Object> param) throws Exception {
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		
		SysUser user = SecureUtil.getCurrentUser();
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPT);						      // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交文书送达");
		}
		
		BigDecimal nodeid = casebaseinfo.getNodeid();
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				nodeid, caseid, probaseinfo, user);
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Title: xzfyDeliveryFlow
	 * @Description: 文书送达：发送
	 * @author ztt
	 * @date 2016年11月21日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyDeliveryFlow(String caseid) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至文书送达");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加1，新增流程日志_已提交
		BigDecimal newNodeid = nodeid.add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(nodeid.add(BigDecimal.TEN));                                   // 节点加1
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: updateReview
	 * @Description: 回访单：保存或修改
	 * @author ztt
	 * @date 2016年11月22日
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateReview(Map<String, Object> param) throws Exception {
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
		noticerelainfo.setType("03");
		noticerelainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		noticerelainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		//操作人信息
		SysUser user = SecureUtil.getCurrentUser();
		noticerelainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(noticerelainfo);
	}
	
	/**
	 * @Title: xzfyDocReviewFlow
	 * @Description: 回访单：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyDocReviewFlow(String caseid) throws Exception {
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至回访单");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		if (GCC.PRONODEBASEINFO_NODEID_ACCBACKDOC == nodeid.intValue()) {
			// 查询受理结果
			StringBuffer sql = new StringBuffer();
			sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
			sql.append(" from PUB_PROBASEINFO t ");
			sql.append(" where t.caseid = '" + caseid + "'");
			sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
			sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_ACCEND);
			sql.append(" order by t.endtime desc");
			String result = "";
			List<Map<String, Object>> probaseinfos = mapDataDao.queryListBySQL(sql.toString());
			if (null != probaseinfos && !probaseinfos.isEmpty()) {
				result = (String)probaseinfos.get(0).get("result");
			}
			if (result.equals(GCC.PROBASEINFO_OPTTYPE_ACCEPT)) {  //受理结果：01_受理
				Probaseinfo new_probaseinfo = new Probaseinfo();
				new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESRECEIVE),
						caseid, new_probaseinfo, user, null, null, null);
				// 新增结果日志_审理_已提交
				probaseinfoDao.save(new_probaseinfo);
				
				// 节点加1
				casebaseinfo.setNodeid(nodeid.add(BigDecimal.ONE));
				// 修改案件信息_制作文书_已提交
				casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
				// 立案未结案
				casebaseinfo.setState(GCC.RCASEBASEINFO_STATE_NOCLOSURE);
				
			} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_NOTACCEPT)) {	//受理结果：02_不予受理
				// 修改案件信息处理标志不予受理
				casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_REFUSE);
				// 结案未归档
				casebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);
				
				// 新增结果日志
				Resultbaseinfo rbi = new Resultbaseinfo();
				rbi.setProcessid(probaseins.get(0).getString("processid"));
				rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
				rbi.setResult(result);
				rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_REFUSE);
				rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
						new BigDecimal(probaseins.get(0).getString("nodeid")), caseid, rbi, user);
				resultbaseinfoDao.save(rbi);
				
			} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_FORWARD)) {//受理结果：03_转送其他机构
				// 修改案件信息处理标志转送其他机构
				casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_FORWARDING);
				// 结案未归档
				casebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);
				
				// 新增结果日志
				Resultbaseinfo rbi = new Resultbaseinfo();
				rbi.setProcessid(probaseins.get(0).getString("processid"));
				rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
				rbi.setResult(result);
				rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_FORWARDING);
				rbi.setSendunit(probaseins.get(0).getString("sendunit")); //转送机关
				rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
						new BigDecimal(probaseins.get(0).getString("nodeid")), caseid, rbi, user);
				resultbaseinfoDao.save(rbi);
				
			}
		} else {
			// 节点加1_备考表_已提交
			BigDecimal newNodeid = nodeid.add(BigDecimal.ONE);
			logSql.setLength(0);
			logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.caseid = '" + caseid + "'");
			logSql.append(" and t.protype = '" + protype + "'");
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
			new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
					caseid, new_probaseinfo, user, null, null, null);
			probaseinfoDao.saveOrUpdate(new_probaseinfo);
			
			// 节点加1
			casebaseinfo.setNodeid(nodeid.add(BigDecimal.ONE));
			// 0_已提交
			casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
		}
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: updateSummary
	 * @Description: 备考表：保存或修改
	 * @author ztt
	 * @date 2016年11月22日
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateSummary(Map<String, Object> param) throws Exception {
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
		noticerelainfo.setType("04");
		noticerelainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		noticerelainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		//操作人信息
		SysUser user = SecureUtil.getCurrentUser();
		noticerelainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(noticerelainfo);
	}
	
	/**
	 * @Title: xzfySummaryFlow
	 * @Description: 备考表：发送
	 * @author ztt
	 * @date 2016年11月22日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfySummaryFlow(String caseid) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至备考表");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 查询审理结果
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" and t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_HEAREND);
		sql.append(" order by t.endtime desc");
		String result = "";
		List<Map<String, Object>> probaseinfos = mapDataDao.queryListBySQL(sql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			result = (String)probaseinfos.get(0).get("result");
		}
		
		//将处理结果记录到结果表中
		Resultbaseinfo rbi = new Resultbaseinfo();
		rbi.setProcessid(probaseins.get(0).getString("processid"));
		rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
		rbi.setResult(result);
		rbi.setReason(probaseins.get(0).getString("reason"));
		rbi.setRemark(probaseins.get(0).getString("remark"));
		rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);
		rbi = FlowUtil.genResultOperationData(protype, casebaseinfo.getNodeid(), caseid, rbi, user);
		
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		// 修改流程信息处理标志_已办
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);
		
		// 修改案件信息处理标志_已办
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);
		
		if (GCC.PROBASEINFO_PROTYPE_XZFYAUDIT.equals(protype))// 审理
		{
			// 结案未归档 
			casebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);
			// 新增结果记录
			resultbaseinfoDao.save(rbi);
		}
		else {
			// 同意
			if (GCC.PROBASEINFO_OPTTYPE_PASS.equals(result))
			{
				// 恢复或延期
				if (GCC.PROBASEINFO_PROTYPE_XZFYRECOVER.equals(protype) || GCC.PROBASEINFO_PROTYPE_XZFYDELAY.equals(protype))
				{
					// 获取原流程最后的节点id和处理标志
					List<JSONObject> list = queryProbaseinfoList(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
					String nodeidtmp = list.get(0).getString("nodeid");
					String opttype = list.get(0).getString("opttype");
					
					// 修改案件状态
					casebaseinfo.setNodeid(new BigDecimal(nodeidtmp));
					casebaseinfo.setOpttype(opttype);
					casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
					
					// 若恢复
					if (GCC.PROBASEINFO_PROTYPE_XZFYRECOVER.equals(protype) )
					{
						// 删除中止结果记录
						deleteStopResultbaseinfo(caseid, GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND);
					}
				}
				else if (GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND.equals(protype)) // 中止
				{
					// 新增结果记录
					resultbaseinfoDao.save(rbi);
				}
				else // 终止、和解、撤销
				{
					// 结案未归档 
					casebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);
					// 新增结果记录
					resultbaseinfoDao.save(rbi);
				}
			}
			else // 不同意
			{
				// 不同意
				probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_REFUSE);
				if (GCC.PROBASEINFO_PROTYPE_XZFYRECOVER.equals(protype)) // 恢复
				{
					// 修改案件状态_中止
					casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND);
				}
				else  // 中止、终止、和解、撤销、延期
				{
					// 获取原流程最后的节点id和处理标志
					List<JSONObject> list = queryProbaseinfoList(caseid, GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
					String nodeidtmp = list.get(0).getString("nodeid");
					String opttype = list.get(0).getString("opttype");
					
					// 修改案件状态
					casebaseinfo.setNodeid(new BigDecimal(nodeidtmp));
					casebaseinfo.setOpttype(opttype);
					casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
					if (GCC.PROBASEINFO_PROTYPE_XZFYDELAY.equals(protype))
					{
						// 延期天数
						casebaseinfo.setDelaydays("0");
					}
				}
			}
		}
		
		// 更新流程信息
		probaseinfoDao.update(probaseinfo);
		// 更新案件信息
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: updateXzfyReturnByCaseid
	 * @Description: 复议回退：根据案件ID退回行政复议处理信息，并记录处理日志
	 * @author ztt
	 * @date 2016年11月14日 
	 * @param param
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyReturnByCaseid(Map<String, Object> param) throws Exception{
		// 1、入口参数判断
		// 判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		// 2、业务逻辑判断
		// 判断行政复议申请是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		// 原流程节点
		BigDecimal nodeid_old = casebaseinfo.getNodeid();
		if (null == casebaseinfo){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (null == nodeid_old
			|| 1 >= nodeid_old.intValue()
			|| GCC.PRONODEBASEINFO_NODEID_HEAR == nodeid_old.intValue()) {
			throw new AppException("保存失败：该案件所处环节不能退回，请刷新页面再退回");
		}
		
		// 获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 回退后流程节点
		BigDecimal nodeid = nodeid_old.subtract(BigDecimal.TEN);
		// 集体讨论节点
		if (nodeid_old.intValue() == 140 && "01".equals(casebaseinfo.getProtype())){
			if (!"1".equals(casebaseinfo.getIsreview())){
				nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_TRIAL);
			}
		}
		// 科室审理
		if (nodeid_old.intValue() == 150 && "01".equals(casebaseinfo.getProtype())){
			// 简单流程退回至承办人审核
			if("01".equals(casebaseinfo.getCasesort())){
				nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR);
			}else if("02".equals(casebaseinfo.getCasesort())){
				//普通流程
				// 1.如果集体讨论 退回集体讨论
				if( "1".equals(casebaseinfo.getIsdiscuss())){
					nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISCUSS);
				}else{
					if ("1".equals(casebaseinfo.getIsreview())){
						nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REVIEW);
					}else{
						nodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_TRIAL);
					}
				}
			}
		}
		// 修改案件申请表信息
		casebaseinfo.setNodeid(nodeid);			                                              // 流程节点退一步
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);   						  // 处理标志：0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		// 4、记录流程日志
		// 删除回退之前的过程记录
		String protype = casebaseinfo.getProtype();
		StringBuffer where = new StringBuffer();
		where.append(" caseid = '" + caseid + "'")
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
		// 修改过程日志.
		probaseinfoDao.update(probaseinfo);*/
		
		String htyj = (String) param.get("htyj");
		// 已回退
		Probaseinfo probaseinfo_return = new Probaseinfo();
		probaseinfo_return.setNodeid(nodeid_old);
		probaseinfo_return = FlowUtil.genReturnedOperationData(protype, caseid, probaseinfo_return, user, "", htyj);
		probaseinfoDao.save(probaseinfo_return);
	}
	
	/**
	 * @Title: queryNoticeInfoByCaseid
	 * @Description: 查询回访单、备考表信息
	 * @author ztt
	 * @date 2016年11月22日
	 * @param caseid 案件ID
	 * @param nodeid 节点ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryNoticeInfoByCaseid(String caseid, String nodeid, String protype)
	{
		// 受理流程类型只有01
		if (Integer.valueOf(nodeid) < 90) {
			protype = GCC.PROBASEINFO_PROTYPE_XZFYAUDIT;
		}
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.nodeid, t.noticecode, t.noticename, t.contents from b_casenoticerelainfo t where t.caseid='").append(caseid)
		   .append("' and t.nodeid = ").append(new BigDecimal(nodeid))
		   .append(" and t.protype = '").append(protype).append("'");
		
		List<JSONObject> list = mapDataDao.queryListBySQL(sql.toString());
		return list;
	}
	
	/**
	 * @Title: queryXzfyProcessList
	 * @Description: 专家小组管理： 专家小组管理grid列表
	 * @author ybb
	 * @date 2016年8月16日 上午11:00:01
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryXzfyProcessList(Map<String, Object> param) throws AppException {
		
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
		
		//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	
		} else {
			sql.append("where t.region is null ");	
		}
				
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "' "); //流程类型
		sql.append(" and t.opttype in( '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED 
				+ "','" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED
				+ "','" + GCC.PROBASEINFO_OPTTYPE_PROCESSED + "')"); // 处理标志
		sql.append(" and caseid not in (select caseid from PUB_RESULTBASEINFO)");	//案件不在流程结果表中
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (!StringUtils.isBlank(opttype)) {
			sql.append(" and t.opttype = '").append(opttype).append("'");
		} 
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		sql.append(" and t.nodeid in (1,2,3) ");
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收流程处理
	 * @author ybb
	 * @date 2016年8月16日 上午11:42:03
	 * @param param
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateOpttypeByCaseid(Map<String, Object> param) throws AppException, IllegalAccessException, InvocationTargetException{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if(StringUtils.isBlank(caseid)){
			throw new AppException("修改失败：案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		
		//3、业务逻辑处理
		if (casebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				casebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED)) {
			
			casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);            //处理标志：1_已接收
			casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
			
			//修改行政复议申请处理标志为：1_已接收
			casebaseinfoDao.update(casebaseinfo);
			
			//4、记录【已接收】过程日志
			SysUser user = SecureUtil.getCurrentUser();
			StringBuffer sql = new StringBuffer();
			sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			sql.append(" from PUB_PROBASEINFO t ");
			sql.append(" where t.caseid = '" + caseid + "'");
			sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");// 复议案件
			sql.append(" and t.nodeid = '" + GCC.PRONODEBASEINFO_NODEID_ACCEPT + "'"); // 需承办人审批
			sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED + "'");// 已提交
			sql.append(" order by t.endtime desc");
			List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
			
			if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
				
				Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
				probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
						new BigDecimal(GCC.PRONODEBASEINFO_NODEID_ACCEPT), caseid, probaseinfo, user);
				
				probaseinfoDao.update(probaseinfo);
			}
		}
	}

	/**
	 * @Title: queryXzfyListForView
	 * @Description: 案件查询：分页查询复议申请信息
	 * @author ybb
	 * @date 2016年8月26日 上午10:30:43
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryXzfyListForView(Map<String, Object> param) throws AppException {
		
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
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_PROTYPE' and a.status=0 and a.code = t.protype) protype_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");
		sql.append("from B_CASEBASEINFO t where 1=1");
		
		//流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		if (!StringUtils.isBlank(protype)) {
			sql.append(" and t.protype = '").append(protype).append("'");
		} 
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		//被申请人
		String defname = StringUtil.stringConvert(param.get("defname"));
		if (!StringUtils.isBlank(defname)) {
			sql.append(" and t.defname like '%").append(defname).append("%'");
		}
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		}
		
		//申请复议事项
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		}
		
		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: querySimcaseManageForView
	 * @Description: 案件查询：分页查询相似案件列表信息
	 * @author ybb
	 * @date 2016年8月26日 上午10:30:43
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport querySimcaseManageForView(Map<String, Object> param) throws AppException {
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
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_PROTYPE' and a.status=0 and a.code = t.protype) protype_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc ");

		sql.append("from B_CASEBASEINFO t   ");
		
//		sql.append(" t.opttype in('0','1', '2')");                          //opttype='2'

/*		//被申请人类型
		String deftype = StringUtil.stringConvert(param.get("deftype"));
		if (!StringUtils.isBlank(deftype)) {
			sql.append(" and t.deftype = '").append(deftype).append("'");
		} 
		
		//行政管理类型
		String admtype = StringUtil.stringConvert(param.get("admtype"));
		if (!StringUtils.isBlank(admtype)) {
			sql.append(" and t.admtype = '").append(admtype).append("'");
		} 
		
		//申请复议事项类型
		String casetype = StringUtil.stringConvert(param.get("casetype"));
		if (!StringUtils.isBlank(casetype)) {
			sql.append(" and t.casetype = '").append(casetype).append("'");
		} 
*/		
		String keyval = StringUtil.stringConvert(param.get("keyval")).trim();

		if (keyval.length() > 0) {

			if (!keyval.startsWith("%")) {
				keyval = "%" + keyval;
			}
			if (!keyval.endsWith("%")) {
				keyval += "%";
			}
			
			sql.append(" where  t.csaecode like '").append(keyval).append("'");  // 案件编号
			sql.append(" or t.appname like '").append(keyval).append("'");  	// 申请人名称
			sql.append(" or t.intro like '").append(keyval).append("'");		// 案件名称
			sql.append(" or t.defname like '").append(keyval).append("'");		//被申请人名称
			
		}	
			
		sql.append(" order by t.lasttime desc ");
//	System.out.println(pageIndex+"pageIndex"+pageSize+"pageSize"+"sinilerCase--:"+sql);
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	
	/**
	 * 案件小组grid
	 * @param map
	 * @return
	 */
	public PaginationSupport queryGroupList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select t.groupid,t.groupname,t.casedesc,t.question,t.opttime,t.ifcheck,t.operator,");
		sql.append(" (select a.username from sys_user a where  a.userid=t.operator) cjr_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.ifcheck) ifcheckname");
		sql.append(" from b_groupbaseinfo t");
		
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if("yx".equals(operflag)) {
			//已选专家查询
			sql.append(" inner join b_casesperelabaseinfo t1 on t1.groupid = t.groupid and t1.caseid = '"+caseid+"' ");
		}else {
			//可选专家查询
			sql.append(" where not exists (select 1 from b_casesperelabaseinfo t1 where t1.groupid = t.groupid and t1.caseid = '"+caseid+"') ");
		}
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 *  保存案件小组信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String saveAjxz(Map<String, Object> param) throws Exception {
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String groupid = StringUtil.stringConvert(param.get("groupid"));
		
		//参数校验
		if("".equals(operflag)) {
			throw new Exception("操作失败：参数未获取到：operflag");
		}
		if("".equals(caseid)) {
			throw new Exception("操作失败：参数未获取到：caseid");
		}
		if("".equals(groupid)) {
			throw new Exception("操作失败：参数未获取到：groupid");
		}
		
		String flowSql = " select processid from pub_probaseinfo t where t.caseid = '"+caseid+"' ";
		List<?> flowlist = mapDataDao.queryListBySQL(flowSql);
		String processid = "";
		if(flowlist!=null && flowlist.size()>0) {
			processid = StringUtil.stringConvert(((Map)flowlist.get(0)).get("processid"));
		}
		if("".equals(processid)) {
			throw new Exception("操作失败：流程id未获取到：processid");
		}
		String existSql = " select * from b_casesperelabaseinfo t where t.caseid = '"+caseid
				+"' and t.groupid = '"+groupid+"' ";
		List<?> list = casespeDao.findVoBySql(existSql,BCasesperelabaseinfo.class);
		if("add".equals(operflag)) {
			//新增
				//验证是否已存在
					//已存在 不需要插入
					//已存在  插入
			if(list != null && list.size() > 0) {
				//已存在
				return "";
			}
			BCasesperelabaseinfo rela = new BCasesperelabaseinfo();
			rela.setCaseid(caseid);
			rela.setGroupid(groupid);
			rela.setProcessid(processid);
			
			casespeDao.save(rela);
			
		}else {
			//删除
				//验证是否已存在
					//已存在 删除
					//已存在  不需要删除
			if(list != null && list.size() > 0) {
				//已存在
				BCasesperelabaseinfo oldinfo = (BCasesperelabaseinfo)list.get(0);
				casespeDao.delete(oldinfo);
			}
		}

		return "";
	}

	/**
	 * @Title: queryNoticebaseinfo
	 * @Description: 判断是否生成了通知书
	 * @author ybb
	 * @date 2016年8月31日 下午1:09:18
	 * @param protype 流程类型
	 * @param nodeid  节点编号
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String queryNoticebaseinfo(String caseid, String protype, BigDecimal nodeid){
		
		//判断是否生成了通知书
		String fjts = pcfg.findGeneralParamValue("SYSTEM", "ISPRINT");
		if (!StringUtils.isBlank(fjts) && Boolean.parseBoolean(fjts)) {
			
			//查询是否生成了通知凭证
			StringBuffer sql = new StringBuffer();
			sql.append("select noticeid from B_NOTICEBASEINFO ");
			sql.append("where caseid = '").append(caseid).append("' ");
			sql.append("and doctype in (").
				append("select doctype from PUB_NOTICECONFIG where protype = '").append(protype).append("'").
				append("and nodeid = ").append(nodeid).
				append(") ");
			
			List<?> noticebaseinfos = mapDataDao.queryListBySQL(sql.toString());
			if (noticebaseinfos == null || noticebaseinfos.isEmpty()) {
				return "该案件处未生成通知书，不能发送";
			}
		} 
		
		return null;
	}
	
	/**
	 * @Title: queryUserListForSend
	 * @Description: 行政复议受理转发：分页查询用户列表
	 * @author ybb
	 * @date 2016年9月1日 上午11:49:27
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryUserListForSend(Map<String, Object> param) throws AppException {
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct b.name  as orgcode_mc,b.code  ");
		sql.append("from SYS_USER t  left join SYS_DEPT b on b.code = t.orgcode ");
		sql.append("where t.usertype != 2 and t.status = 0 and t.isca = 0  ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//节点ID
		String nodeid = StringUtil.stringConvert(param.get("nodeid"));
		
		// 查询对应的角色ID
		StringBuffer nodeid_sql = new StringBuffer();
		nodeid_sql.append("select t.roleid from PUB_PRONODEBASEINFO t ");
		nodeid_sql.append("where t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "' ");
		nodeid_sql.append("and t.nodeid = " + nodeid);
		
		List<JSONObject> nodeidList = mapDataDao.queryListBySQL(nodeid_sql.toString());
		
		//拼接节点编号
		String roleid = "";
		if (nodeidList != null && !nodeidList.isEmpty()){
			roleid += nodeidList.get(0).getString("roleid");
			sql.append("and t.userid in (select userid from sys_user_role where roleid = " + roleid + ")");
		}
		sql.append("and t.userid != " + user.getUserid());	
		sql.append(" order by t.b.code ");
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	
	
	/**
	 * @Title: queryRoleListForSend
	 * @Description: 行政复议受理转发：分页查询用户列表
	 * @author ybb
	 * @date 2016年9月1日 上午11:49:27
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryRoleListForSend(Map<String, Object> param) throws AppException {
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取code
		String code = StringUtil.stringConvert(param.get("code"));
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.userid,a.usercode, a.username, a.createtime ");
		sql.append(" from SYS_USER a left join SYS_DEPT b on a.orgcode = b.code where b.code = "+ code);
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	/**
	 * @Title: addSendCaseToOther
	 * @Description: 行政复议受理：转发
	 * @author ybb
	 * @date 2016年9月1日 下午4:11:56
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addSendCaseToOther(Map<String, Object> param) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
		
			//判断用户ID是否为空
		String userid = StringUtil.stringConvert(param.get("userid"));
		if(StringUtils.isBlank(caseid)){
			throw new AppException("用户ID为空");
		}
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//申请环节，判断案件处理标志不是【已接收】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}

		//判断日志信息是否正常
		//判断该案件是否已提交
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(sql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未提交复议申请");
		}
		
		//判断用户信息是否正常
		SysUser user = sysUserDao.get(Long.parseLong(userid));
		if (user == null) {
			throw new AppException("未找到转发用户，请刷新页面后重新转发");
		}
		
		//3、业务逻辑处理
		//更新行政复议所属区域
		casebaseinfo.setRegion(user.getArea());
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		
		casebaseinfoDao.update(casebaseinfo);
		
		//更新流程过程表中的接收人信息
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo.setUserid(user.getUserid());
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(probaseins.get(0).getString("starttime"));
		probaseinfo.setEndtime(probaseins.get(0).getString("endtime"));
		
		probaseinfoDao.update(probaseinfo);
	}

	/**
	 * 保存案件研讨主题相关信息
	 * @param param
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveAjZt(Map<String, Object> param) throws Exception{
		String defaddress = (String) param.get("defaddress");
		String speids = (String) param.get("speids");
		String jzids = (String) param.get("zjids");
		String caseid = (String) param.get("caseid");
		String timeLong = (String) param.get("timeLong");
		
		// 小组信息
		BGroupbaseinfo bean = new BGroupbaseinfo();
		SysUser user = SecureUtil.getCurrentUser();
		bean.setOperator(user.getUserid().toString());
		bean.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
		bean.setGroupname(defaddress);
		bGroupbaseinfoDAO.save(bean);
		String groupid = bGroupbaseinfoDAO.findByExample(bean).get(0).getGroupid();
		
		// 小组对应案件
		BCasesperelabaseinfo bCasesperelabaseinfo = new BCasesperelabaseinfo();
		bCasesperelabaseinfo.setCaseid(caseid);
		bCasesperelabaseinfo.setGroupid(groupid);
		bCasesperelabaseinfoDao.save(bCasesperelabaseinfo);
		
		// 小组对应专家信息
		String[] speIds =  speids.split(";");
        for (String speid : speIds) {
			if(StringUtils.isNotEmpty(speid)){
				BSpegrouprelainfo bSpegrouprelainfo = new BSpegrouprelainfo();
				bSpegrouprelainfo.setGroupid(groupid);
				bSpegrouprelainfo.setSpeid(speid);
				bSpegrouprelainfoDAO.save(bSpegrouprelainfo);
			}
		}
        
        // 附件信息
        SysFileManage sysFileManage = new SysFileManage();
        sysFileManage.setKeyid(caseid + "_" + timeLong);
        sysFileManage.setElementcode("XZFY_ZT_FJ");
        List<SysFileManage> sysFileManageList = sysFileManageDao.findByExample(sysFileManage);
        if(!CollectionUtils.isEmpty(sysFileManageList)){
        	for (SysFileManage sysFileManageTemp : sysFileManageList) {
        		sysFileManageTemp.setKeyid(groupid);
        		sysFileManageDao.update(sysFileManageTemp);
        	}
        }
        // 主题研讨成员可看卷宗信息
        String[] jzidsArray= jzids.split(";");
        for (String jzid : jzidsArray) {
        	if(StringUtils.isNotEmpty(jzid)){
        		SysFileManage jzInfo = new SysFileManage();
        		jzInfo.setElementcode("XZFY_ZT_JZ");
    			jzInfo.setCreatetime(DateUtil.getCurrentDateTime());
    			jzInfo.setKeyid(groupid);
    			jzInfo.setUsercode(user.getUserid().toString());
        		if(StringUtils.endsWithIgnoreCase(jzid, "_JZ_FJ")){
        			String itemid = jzid.split("_JZ_FJ")[0];
        			SysFileManage temp = sysFileManageDao.get(Long.valueOf(itemid));
        			jzInfo.setRemark(itemid);
        			jzInfo.setOriginalfilename(temp.getOriginalfilename());
        			jzInfo.setFilename(temp.getFilename());
        			jzInfo.setFilesize(temp.getFilesize());
        			jzInfo.setFilepath(temp.getFilepath());
        			jzInfo.setStepid("JZ_FJ");
        			jzInfo.setTitle(temp.getTitle());
        			jzInfo.setSavemode(0);
        		}else{
        			jzInfo.setRemark(jzid);
        			jzInfo.setOriginalfilename("卷宗文件");
        			jzInfo.setFilename("卷宗文件");
        			jzInfo.setFilesize((long) 0);
        			jzInfo.setSavemode(0);
        		}
				mapDataDao.add(BeanUtils.describe(jzInfo), TableNameConst.SYS_FILEMANAGE);
        	}
		}
	}
	
	/**
	 * @Title: queryXzfyreqByCaseid
	 * @Description: 行政复议查看-查看案件基本信息
	 * @author ybb
	 * @date 2016年9月19日 下午3:07:37
	 * @param caseid
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CasebaseinfoVo queryXzfyreqByCaseid(String caseid) throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.* ,t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, t.reqmansum,");
		sql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		sql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		sql.append("t.admtype, t.casetype, t.ifcompensation, to_char(t.amount) amountstr, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		sql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, t.delaydays, t.region, t.intro, t.key, t.state, t.isgreat, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0 and a.code = t.sex) sexname, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SYS_AREA' and a.status=0 and a.code = t.region) regionname, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PUB_PROBASEINFO_RECEIVEWAY' and a.status=0 and a.code = t.receiveway) receivewayname, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='CASEREASONTYPE' and a.status=0 and a.code = t.type) typename, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensationname ");
		sql.append("from B_CASEBASEINFO t ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		
		List<CasebaseinfoVo> casebaseinfos = (List<CasebaseinfoVo>) casebaseinfoDao.findVoBySql(sql.toString(), CasebaseinfoVo.class);
		if (casebaseinfos == null || casebaseinfos.isEmpty()) {
			return null;
		}
		
		return casebaseinfos.get(0);
	} 
	
	/**
	 * @Title: queryNodeidByCurrentuser
	 * @Description: 查询当前用户对应节点
	 * @author ztt
	 * @date 2016年11月2日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryNodeidByCurrentuser()
	{
		SysUser user = SecureUtil.getCurrentUser();
		//根据用户ID查询角色对应的节点编号
		StringBuffer nodeid_sql = new StringBuffer();
		nodeid_sql.append("select t.nodeid from PUB_PRONODEBASEINFO t ");
		nodeid_sql.append("where t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "' ");
		nodeid_sql.append("and t.roleid in (select distinct roleid from SYS_USER_ROLE where userid = " + user.getUserid() + ")");
		List<JSONObject> nodeidList = mapDataDao.queryListBySQL(nodeid_sql.toString());
		//拼接节点编号
		String nodeid = "";
		if (nodeidList != null && !nodeidList.isEmpty()){
			nodeid = nodeidList.get(0).getString("nodeid");
		}
		
		return nodeid;
	}
	
	/**
	 * @Title: queryCasebaseinfoByCaseid
	 * @Description: 查询案件信息
	 * @return
	 */
	public Casebaseinfo queryCasebaseinfoByCaseid(String caseid)
	{
		 return casebaseinfoDao.get(caseid);
	}
	
	/**
	 * @Title: queryCaseaccbasisByCaseid
	 * @Description: 查询案件依据信息
	 * @author ztt
	 * @date 2016年11月2日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryCaseaccbasisByCaseid(String caseid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select t.b1, t.b2, t.b3, t.b4, t.b5, t.b6, t.b7, t.b8, t.b9, t.b10 from b_caseaccbasisinfo t ");
		sql.append("where t.caseid = '" + caseid + "' ");
		List<JSONObject> list = mapDataDao.queryListBySQL(sql.toString());
		return list;
	}
	
	/**
	 * @Title: queryAccbasisByCaseid
	 * @Description: 查询案件依据信息
	 * @author ztt
	 * @date 2016年11月28日
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryAccbasisByCaseid(String caseid)
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b1) b1,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b2) b2,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b3) b3,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b4) b4,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b5) b5,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b6) b6,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b7) b7,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b8) b8,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b9) b9,")
		.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode) = 'SYS_TRUE_FALSE' and a.status=0 and a.code = t.b10) b10")
		.append(" from b_caseaccbasisinfo t")
		.append(" where t.caseid = '" + caseid + "'");
		List<JSONObject> list = mapDataDao.queryListBySQL(sql.toString());
		return list;
	}
	
	/**
	 * @Title: queryProbaseinfoListByParam
	 * @Description: 查询流程列表信息（通过案件ID）
	 * @author ztt
	 * @date 2016年11月3日
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> queryProbaseinfoList(String caseid, String protype) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from VIEW_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + protype + "'");
		sql.append(" order by t.nodeid desc");
		
		//执行查询
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		return probaseinfoList;
	}
	
	/**
	 * @Title: deleteProbaseinfoList
	 * @Description: 删除流程列表信息（通过案件ID）
	 * @author ztt
	 * @date 2016年11月3日
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 * @param nodeid 节点编号
	 */
	public void deleteProbaseinfoList(String caseid, String protype, BigDecimal nodeid)
	{
		//获取StringBuffer对象，用来拼接where语句
		StringBuffer where = new StringBuffer();
		
		where.append(" caseid = '").append(caseid).append("'")
		    .append(" and protype = '").append(protype).append("'")
		    .append(" and nodeid >= ").append(nodeid);
		
		probaseinfoDao.deleteBySQL("PUB_PROBASEINFO", where.toString());
	}
	
	/**
	 * @Title: deleteStopResultbaseinfo
	 * @Description: 删除中止结果信息（通过案件ID）
	 * @author ztt
	 * @date 2016年11月3日
	 * @param caseid 案件ID
	 * @param protype 流程类型
	 */
	public void deleteStopResultbaseinfo(String caseid, String protype)
	{
		//获取StringBuffer对象，用来拼接where语句
		StringBuffer where = new StringBuffer();
		
		where.append(" caseid = '").append(caseid).append("'")
		     .append(" and protype = '").append(protype).append("'");
		
		resultbaseinfoDao.deleteBySQL("PUB_RESULTBASEINFO", where.toString());
	}
	
	/**
	 * @Title: queryThirdByCaseid
	 * @Description: 根据案件ID查询第三人信息
	 * @author ztt
	 * @date 2016年11月10日 下午3:16:19
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Thirdbaseinfo queryThirdByCaseid(String caseid){
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.thid, t.thname, t.thtype, t.thidtype, t.thidcode, t.thphone, t.thaddress, ");
		sql.append("t.thpostcode, t.caseid ");
		sql.append("from B_THIRDBASEINFO t ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		
		List<Thirdbaseinfo> thirdbaseinfos = (List<Thirdbaseinfo>) thirdbaseinfoDao.findVoBySql(sql.toString(), Thirdbaseinfo.class);
		if (thirdbaseinfos == null || thirdbaseinfos.isEmpty()) {
			return null;
		}
		
		return thirdbaseinfos.get(0);
	}
	
	/**
	 * @Title: getNameByCode
	 * @Description: 查询字典值
	 * @author ztt
	 * @date 2016年11月10日 下午3:16:19
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNameByCode(String table, String elementcode, String code) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.name from ").append(table).append(" a where upper(a.elementcode)='").append(elementcode)
				.append("' and a.status=0 and a.code = '").append(code).append("'");
		String name = "";
		if (null != code)
		{
			List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
			if (null != probaseinfoList && !probaseinfoList.isEmpty()) {
				name = probaseinfoList.get(0).getString("name");
			} 
		}
		return name;
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
	 * 组装bean
	 * @author ztt
	 * @date 2016年11月10日 下午3:16:19
	 * @param casebaseinfo
	 * @param cause
	 * @param third
	 * @return
	 */
	private NoticeContentInfo getNoticeContentInfoBean(Casebaseinfo casebaseinfo, Map<String, Object> third)
	{
		NoticeContentInfo info = new NoticeContentInfo();
		// 申请人
		info.setAppName(casebaseinfo.getAppname());
		// 申请人通讯地址
		info.setAppAddress(casebaseinfo.getMailaddress());
		// 被申请人
		info.setDefName(casebaseinfo.getDefname());
		// 被申请人通讯地址
		info.setDefAddress(casebaseinfo.getDefmailaddress());
		// 申请事项
		info.setAppCase(casebaseinfo.getAppcase());
		// 申请日期
		String appdate = casebaseinfo.getAppdate().replace("-", "");
		info.setAppYear(appdate.substring(0,4));
		info.setAppMonth(appdate.substring(4,6));
		info.setAppDay(appdate.substring(6));
		if (null != third && !third.isEmpty())
		{
			// 第三人
			Object thname = third.get("thname");
			info.setThName(null == thname ? "" : (String)thname);
			// 第三人通讯地址
			Object thaddress = third.get("thaddress");
			info.setThAddress(null == thaddress ? "" :(String)thaddress);
		}
		
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		info.setSystemYear(date.substring(0,4));
		info.setSystemMonth(date.substring(4,6));
		info.setSystemDay(date.substring(6));
		
		return info;
	}
	
	/**
	 * 生成审批表  05 受理审批表  06  审理审批表
	 * @param caseid
	 * @param type
	 * @param result
	 * @param casebaseinfo
	 * @param user
	 * @param remarks
	 */
	@SuppressWarnings("unchecked")
	private void generateAcceptNotice(String caseid, String type, String result, Casebaseinfo casebaseinfo, SysUser user, JSONObject remarks) {
		String xml = "";
		String tempid = "";
		String noticeName = "";
		BCasenoticerelainfo relainfo = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id from b_casenoticerelainfo t where t.caseid='").append(caseid)
			.append("' and t.type = '").append(type).append("'");
		List<JSONObject> relaList = mapDataDao.queryListBySQL(sql.toString());
		// 查询是否已经生成过审批表
		if (null != relaList && !CollectionUtils.isEmpty(relaList))
		{
			relainfo = casenoticerelainfoDao.get((String)relaList.get(0).get("id"));
			xml = relainfo.getContents();
			tempid = relainfo.getTempid();
			noticeName = relainfo.getNoticename();
		}
		else {
			relainfo = new BCasenoticerelainfo();
			// 查询审批表模板
			sql.setLength(0);
			sql.append("select tempid, noticename, contents from b_casenoticetempinfo where type='").append(type).append("'");
			
			List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
			if (null != list && !CollectionUtils.isEmpty(list))
			{
				Clob contents = (Clob)list.get(0).get("contents");
				if (null != contents) {
					xml = UtilTool.clobToString(contents);
				}
				tempid = (String)list.get(0).get("tempid");
				noticeName = (String)list.get(0).get("noticename");
			}
		}
		
		// 受理依据
		JSONObject accbasis = null;
		if ("05".equals(type)) {
			List<JSONObject> accbasisList = queryAccbasisByCaseid(caseid);
			if (!accbasisList.isEmpty()) {
				accbasis = accbasisList.get(0);
			}
		}
		
		// 第三人信息
		Map<String, Object> thirdinfo = queryThirdInfo(caseid);
		AcceptNoticeInfo noticeInfo = getAcceptNoticeInfoBean(casebaseinfo, thirdinfo, accbasis, remarks, result);
		
		// 反射替换模板编码值
		Field [] fields = noticeInfo.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++)
        {
            Field f = fields[i];
            String name = f.getName();
            String value = getFieldValue(noticeInfo, name);
			String regex = "$"+name+"$";
			xml = xml.replace(regex, value);
        }
		
		BigDecimal nodeid = casebaseinfo.getNodeid().add(BigDecimal.ONE);
		relainfo.setTempid(tempid);
		relainfo.setCaseid(caseid);
		relainfo.setNodeid(nodeid);
		relainfo.setNoticecode(casebaseinfo.getCsaecode());
		relainfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
		relainfo.setType(type);
		relainfo.setNoticename(noticeName);
		relainfo.setContents(xml);
		relainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		relainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		relainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(relainfo);
	}
	
	/**
	 * 组装bean
	 * @author ztt
	 * @date 2016年11月10日 下午3:16:19
	 * @param casebaseinfo
	 * @param cause
	 * @param third
	 * @return
	 */
	private AcceptNoticeInfo getAcceptNoticeInfoBean(Casebaseinfo casebaseinfo, Map<String, Object> third, JSONObject accbasis, JSONObject remarks, String result)
	{
		AcceptNoticeInfo info = new AcceptNoticeInfo();
		// 案件编标
		info.setCaseCode(casebaseinfo.getCsaecode());
		// 申请人
		info.setAppName(casebaseinfo.getAppname());
		// 被申请人
		info.setDefName(casebaseinfo.getDefname());
		// 申请事项
		info.setAppCase(casebaseinfo.getAppcase());
		// 审结结果
		if (StringUtil.isNotBlank(result)) {
			result = getNameByCode("SYS_YW_DICENUMITEM", "HEAR", result);
		}
		info.setResult(result);
		if (null != third && !third.isEmpty())
		{
			// 第三人
			Object thname = third.get("thname");
			info.setThName(null == thname ? "" : (String)thname);
		}
		if (null != accbasis)
		{
			// 依据
			String b1 = accbasis.getString("b1");
			info.setB1(null == b1 ? "" : b1);
			String b2 = accbasis.getString("b2");
			info.setB2(null == b2 ? "" : b2);
			String b3 = accbasis.getString("b3");
			info.setB3(null == b3 ? "" : b3);
			String b4 = accbasis.getString("b4");
			info.setB4(null == b4 ? "" : b4);
			String b5 = accbasis.getString("b5");
			info.setB5(null == b5 ? "" : b5);
			String b6 = accbasis.getString("b6");
			info.setB6(null == b6 ? "" : b6);
			String b7 = accbasis.getString("b7");
			info.setB7(null == b7 ? "" : b7);
			String b8 = accbasis.getString("b8");
			info.setB8(null == b8 ? "" : b8);
			String b9 = accbasis.getString("b9");
			info.setB9(null == b9 ? "" : b9);
			String b10 = accbasis.getString("b10");
			info.setB10(null == b10 ? "" : b10);
		}
		
		// 受理承办人意见
		info.setAgentRemark(remarks.getString("agentRemark"));
		// 受理科室负责人意见
		info.setSectionRemark(remarks.getString("sectionRemark"));
		// 机构负责人意见
		info.setOrganRemark(remarks.getString("organRemark"));
		// 机关负责人意见
		info.setOfficeRemark(remarks.getString("officeRemark"));
		
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		info.setSystemYear(date.substring(0,4));
		info.setSystemMonth(date.substring(4,6));
		info.setSystemDay(date.substring(6));
		
		return info;
	}
	
	/**
	 * @Description: 根据当前登录用户id获取节点id
	 * @author ztt
	 * @date 2016年11月29日
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

	public List<JSONObject> queryTransregistByCaseid(Map<String, Object> param) {
		// 案件ID
		String docType = "11";
		String caseid  = (String) param.get("caseid");
		String nodeid  = (String) param.get("nodeid");
		String protype  = (String) param.get("protype");
		Casebaseinfo casebaseinfo = getCaseInfoByCaseId(caseid);
		if(casebaseinfo.getNodeid().intValue() == 190){
			docType = "12";
		}
		
		// 查询通知书内容
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id, t.nodeid, t.noticecode, t.noticename, t.contents from b_casenoticerelainfo t where t.caseid='").append(caseid)
		   .append("' and t.nodeid = ").append(new BigDecimal(nodeid))
		   .append(" and t.protype = '").append(protype).append("'")
		   .append(" and t.type = '").append(docType).append("'");
		
		List<JSONObject> list = mapDataDao.queryListBySQL(sql.toString());
		return list;
	}

	// 保存更新转送登记表
	public void updateTransregist(Map<String, Object> param) throws Exception {
		
		 String caseid = (String) param.get("caseid");
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		// 保存文书信息
		String id = StringUtil.stringConvert(param.get("id"));
		BCasenoticerelainfo noticerelainfo = casenoticerelainfoDao.get(id);
		if (null == noticerelainfo) {
			noticerelainfo = new BCasenoticerelainfo();
			param.remove("id");
		}
		BigDecimal nowNodeid = new BigDecimal((String)param.get("nodeid"));
		param.put("nodeid", nowNodeid);
		BeanUtils.populate(noticerelainfo, param);
		// 转送登记表
		if(nowNodeid.intValue() == 90){
			noticerelainfo.setType("11");
			noticerelainfo.setNoticename("行政复议案件转送登记表");
		}
		// 审理决定表
		if(nowNodeid.intValue() == 190){
			noticerelainfo.setType("12");
			noticerelainfo.setNoticename("行政复议决定书");
		}
		noticerelainfo.setNoticecode(casebaseinfo.getCsaecode());
		noticerelainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		noticerelainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		//操作人信息
		SysUser user = SecureUtil.getCurrentUser();
		noticerelainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(noticerelainfo);
			
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至转送登记表");
		}
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		// 更新案件信息
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}

	/**
	 * 转送登记表发送
	 * @param caseid
	 * @throws AppException 
	 */
	public void transregistFlow(String caseid) throws AppException {

		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至转送登记表");
		}
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 生成下个节点流程信息
		// 节点加10，新增流程日志_已提交
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" and t.nodeid = " + nodeid.add(BigDecimal.TEN));
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, nodeid.add(BigDecimal.TEN),
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		// 更新案件信息
		casebaseinfo.setNodeid(nodeid.add(BigDecimal.TEN));
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	
	}

	/**
	 * 保存承办人信息
	 * @param param
	 * @throws AppException 
	 */
	public void updateslundertaker(Map<String, Object> param) throws AppException {

       String caseid = (String) param.get("caseid");
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至转送登记表");
		}
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		// 更新案件信息
		String slundertakerid = (String) param.get("slundertakerid");
		String slundertaker = (String) param.get("slundertaker");
		String slcoorganiser = (String) param.get("slcoorganiser");
		String remark = (String) param.get("remark");
		casebaseinfo.setSlcoorganiser(slcoorganiser);
		casebaseinfo.setSlundertaker(slundertaker);
		casebaseinfo.setSlundertakerid(Long.valueOf(slundertakerid));
		casebaseinfo.setRemark(remark);
		casebaseinfo.setNodeid(nodeid);
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	
	
	}

	/**
	 * 通过caseid获取caseinfo
	 * @param caseid
	 * @return
	 */
	public Casebaseinfo getCaseInfoByCaseId(String caseid) {
		return casebaseinfoDao.get(caseid);
	}

	/**
	 * 指定承办人发送
	 * @param caseid
	 * @throws AppException
	 */
	public void slundertakerFlow(String caseid) throws AppException {

			//1、入口参数判断
			//判断案件ID是否为空
			if(StringUtils.isBlank(caseid)){
				throw new AppException("案件ID为空");
			}
		
			//2、业务逻辑判断
			//判断是否是【行政复议审批】
			Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
			if(casebaseinfo == null){
				throw new AppException("未找到对应的行政复议申请");
			}
			
			String protype = casebaseinfo.getProtype();
			BigDecimal nodeid = casebaseinfo.getNodeid();
			//判断该案件是否已接收
			StringBuffer logSql = new StringBuffer();
			logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.caseid = '" + caseid + "'");
			logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
			logSql.append(" and t.nodeid = " + nodeid);
			logSql.append(" order by t.endtime desc");
				
			List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
			if (probaseins == null || probaseins.isEmpty()) {
				throw new AppException("发送失败，未提交至指定承办人");
			}
			//3、记录流程操作日志
			//获取系统当前登录用户信息
			SysUser user = SecureUtil.getCurrentUser();
			// 修改流程日志_已处理
			Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
			probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
					caseid, probaseinfo, user, null, probaseinfo.getRemark());
			
			probaseinfoDao.update(probaseinfo);
			
			// 节点加10，新增流程日志_已提交
			logSql.setLength(0);
			logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			logSql.append(" from PUB_PROBASEINFO t ");
			logSql.append(" where t.caseid = '" + caseid + "'");
			logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
			logSql.append(" and t.nodeid = " + nodeid.add(BigDecimal.TEN));
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
			new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, nodeid.add(BigDecimal.TEN),
					caseid, new_probaseinfo, user, null, null, null);
			probaseinfoDao.saveOrUpdate(new_probaseinfo);
			
			// 更新案件信息
			casebaseinfo.setNodeid(nodeid.add(BigDecimal.TEN));
			casebaseinfo.setUserid(user.getUserid());											  // 用户ID
			casebaseinfo.setOperator(user.getUsername());                                         // 操作人
			casebaseinfo.setRegion(user.getArea());												  // 所属区域
			casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
			casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
			casebaseinfoDao.update(casebaseinfo);
	}

	/**
	 * 审理审核
	 * @param param
	 * @throws AppException 
	 */
	public void updateXzfyHearByCaseid(Map<String, Object> param) throws Exception {
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		String casesort = StringUtil.stringConvert(param.get("casesort"));
		String isreview = StringUtil.stringConvert(param.get("isreview"));
		String isdiscuss = StringUtil.stringConvert(param.get("isdiscuss"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		
		SysUser user = SecureUtil.getCurrentUser();
		casebaseinfo.setCasesort(casesort);                                                   // 案件流程类型
		casebaseinfo.setIsreview(isreview);													  // 是否委员讨论
		casebaseinfo.setIsdiscuss(isdiscuss);  												  // 是否集体
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPT);							  // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交受理");
		}
		
		BigDecimal nodeid = casebaseinfo.getNodeid();
		String remark = "";
		if (GCC.PRONODEBASEINFO_NODEID_SLUNDERTAKERHEAR == nodeid.intValue())
		{
			//承办人处理意见
			remark = StringUtil.stringConvert(param.get("agentRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR == nodeid.intValue())
		{
			//科室处理意见
			remark = StringUtil.stringConvert(param.get("sectionRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_ORGANREMARKHEAR == nodeid.intValue())
		{
			//机构处理意见
			remark = StringUtil.stringConvert(param.get("organRemark"));
		}
		else if (GCC.PRONODEBASEINFO_NODEID_OFFICEREMARKHEAR == nodeid.intValue())
		{
			//机关处理意见
			remark = StringUtil.stringConvert(param.get("officeRemark"));
		}
		
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		probaseinfo.setRemark(remark);
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				nodeid, caseid, probaseinfo, user);
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Description: 行政复议受理：发送
	 * @param caseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyHearFlow(String caseid) throws AppException {
		
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}

		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'"); // 复议审批
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, casebaseinfo.getNodeid(),
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		BigDecimal newNodeid = casebaseinfo.getNodeid();
		String casesort = casebaseinfo.getCasesort();
		
		newNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
		if(casebaseinfo.getNodeid().intValue() == 110){
			if( "02".equals(casesort)){
				newNodeid = casebaseinfo.getNodeid().add(BigDecimal.TEN);
			}else if("01".equals(casesort)){
				newNodeid = new BigDecimal(150);
			}
		}
		// 节点加10，新增流程日志_已提交
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(newNodeid);                 // 下一个节点
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}

	/**
	 * 保存庭审记录
	 * @param param
	 * @throws AppException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateXzfyTrialByCaseid(Map<String, Object> param) throws AppException {

		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		
		SysUser user = SecureUtil.getCurrentUser();
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPT);						      // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交受理");
		}
		
		BigDecimal nodeid = casebaseinfo.getNodeid();
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				nodeid, caseid, probaseinfo, user);
		probaseinfoDao.update(probaseinfo);
	
		
	}

	/**
	 * 庭审发送
	 * @param caseid
	 * @throws AppException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyTrialFlow(String caseid) throws AppException {

		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("该案件不是【行政复议审批】");
		}

		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'"); // 复议审批
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");  // 已接受
		logSql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, casebaseinfo.getNodeid(),
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		BigDecimal newNodeid = casebaseinfo.getNodeid();
		int nodeid = casebaseinfo.getNodeid().intValue();
		String isreview = casebaseinfo.getIsreview();
		String isdiscuss = casebaseinfo.getIsdiscuss();
		if(nodeid == GCC.PRONODEBASEINFO_NODEID_TRIAL){
			if("1".equals(isreview)){
				// 委员审议
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REVIEW);
			}else if("1".equals(isdiscuss)){
				// 集体讨论
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISCUSS);
			}else {
				//科室审批
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR);
			}
		}else if( nodeid == GCC.PRONODEBASEINFO_NODEID_REVIEW) {
			if("1".equals(isdiscuss)){
				// 集体讨论
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISCUSS);
			}else {
				//科室审批
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR);
			}
		}else if( nodeid == GCC.PRONODEBASEINFO_NODEID_DISCUSS) {
				//科室审批
				newNodeid = new BigDecimal(GCC.PRONODEBASEINFO_NODEID_SECTIONREMARKHEAR);
		}
		// 节点加10，新增流程日志_已提交
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" and t.nodeid = " + newNodeid);
		logSql.append(" and t.opttype != '").append(GCC.PROBASEINFO_OPTTYPE_RETURNED).append("'");
		logSql.append(" order by t.endtime desc");
		Probaseinfo new_probaseinfo = null;
		@SuppressWarnings("unchecked")
		List<JSONObject> probaseinfos = mapDataDao.queryListBySQL(logSql.toString());
		if (null != probaseinfos && !probaseinfos.isEmpty()) {
			new_probaseinfo = probaseinfoDao.get(probaseinfos.get(0).getString("id"));
		}
		else {
			new_probaseinfo = new Probaseinfo();
		}
		new_probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(newNodeid);                 // 下一个节点
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);                           // 0_已提交
		casebaseinfo.setUserid(user.getUserid());                       					  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * @Title: updateDelivery
	 * @Description: 文书送达：保存或修改
	 * @author ztt
	 * @date 2016年11月21日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatecallback(Map<String, Object> param) throws Exception {
		
		//1、入口参数判断
		//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
	
		//2、业务逻辑判断
		//判断行政复议申请是否正常
		//判断该案件不是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("保存失败：该案件不是【行政复议审批】");
		}
		
		//获取系统当前登录用户信息
		
		SysUser user = SecureUtil.getCurrentUser();
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPT);						      // 1_已接收
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
			
		//4、记录流程日志
		//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.nodeid = " + casebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未提交廉政回访");
		}
		
		BigDecimal nodeid = casebaseinfo.getNodeid();
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseinfoList.get(0).getString("id"));
		// 修改过程日志_已接收
		probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				nodeid, caseid, probaseinfo, user);
		probaseinfoDao.update(probaseinfo);
	}
	
	/**
	 * @Title: xzfyDeliveryFlow
	 * @Description: 文书送达：发送
	 * @author ztt
	 * @date 2016年11月21日
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfycallbackFlow(String caseid) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'"); // 流程类型
		logSql.append(" and t.nodeid = " + nodeid);
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("发送失败，未提交至廉政回访");
		}
		
		//3、记录流程操作日志
		//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		// 修改流程日志_已处理
		Probaseinfo probaseinfo = probaseinfoDao.get(probaseins.get(0).getString("id"));
		probaseinfo = FlowUtil.genProcessedOperationData(protype, nodeid,
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 节点加10，新增流程日志_已提交
		BigDecimal newNodeid = nodeid.add(BigDecimal.TEN);
		logSql.setLength(0);
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + protype + "'");
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
		new_probaseinfo = FlowUtil.genSubmittedOperationData(protype, newNodeid,
				caseid, new_probaseinfo, user, null, null, null);
		probaseinfoDao.saveOrUpdate(new_probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(nodeid.add(BigDecimal.TEN));                                   // 节点加1
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}
	
	/**
	 * 结案归档
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void xzfyCaseEndNoticePlaceFlow(String caseid) throws Exception{
		//1、入口参数判断
		//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
		//判断是否是【行政复议审批】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("未找到对应的行政复议申请");
		}
		
		String protype = casebaseinfo.getProtype();
		BigDecimal nodeid = casebaseinfo.getNodeid();
		//判断该案件是否已接收
		StringBuffer logSql = new StringBuffer();
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
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
				caseid, probaseinfo, user, null, probaseinfo.getRemark());
		probaseinfoDao.update(probaseinfo);
		
		// 更新案件信息
		casebaseinfo.setNodeid(new BigDecimal(99));                                   // 节点加1
		casebaseinfo.setIsplaceonfile("1");
		casebaseinfo.setState("04");
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							  // 0_已提交
		casebaseinfo.setUserid(user.getUserid());											  // 用户ID
		casebaseinfo.setOperator(user.getUsername());                                         // 操作人
		casebaseinfo.setRegion(user.getArea());												  // 所属区域
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	          // 操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  // 数据最后更新时间
		casebaseinfoDao.update(casebaseinfo);
	}

	/**
	 * 通过用户ID 获取用户所有角色IDList
	 * @param userid
	 * @return
	 */
	public List<String> getRoleidByuserid(Long userid) {
		List<String> resultList = new ArrayList<String>();
		String sql = "select rolecode from sys_user_role ur , sys_role r where r.roleid=ur.roleid and ur.userid='" + userid+"'";
		List<Map<String,String>> list = (List<Map<String, String>>) sysUserDao.findMapBySql(sql);
		for (Map<String, String> map : list) {
			resultList.add(map.get("rolecode"));
		}
		return resultList;
	}
	
	/**
	 * 生成文书
	 * @param param
	 * @param casebaseinfo
	 * @param user
	 * @return
	 */
	public void createDoc(String type, Casebaseinfo casebaseinfo, SysUser user){
		String xml = "";
		String tempid = "";
		String noticeName = "";
		String caseid = casebaseinfo.getCaseid();
		BCasenoticerelainfo relainfo = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select t.id from b_casenoticerelainfo t where t.caseid='").append(caseid)
			.append("' and t.type = '").append(type).append("'");
		List<JSONObject> relaList = mapDataDao.queryListBySQL(sql.toString());
		// 查询是否已经生成过该文书
		if (null != relaList && !CollectionUtils.isEmpty(relaList))
		{
			relainfo = casenoticerelainfoDao.get((String)relaList.get(0).get("id"));
			xml = relainfo.getContents();
			tempid = relainfo.getTempid();
			noticeName = relainfo.getNoticename();
		}
		else {
			relainfo = new BCasenoticerelainfo();
			sql.setLength(0);
			sql.append("select tempid, noticename, contents from b_casenoticetempinfo where type='").append(type).append("'");
			
			List<Map<String, Object>> list = mapDataDao.queryListBySQL(sql.toString());
			if (null != list && !CollectionUtils.isEmpty(list))
			{
				Clob contents = (Clob)list.get(0).get("contents");
				if (null != contents) {
					xml = UtilTool.clobToString(contents);
				}
				tempid = (String)list.get(0).get("tempid");
				noticeName = (String)list.get(0).get("noticename");
			}
		}
		
		// 反射替换模板编码值
		Field [] fields = casebaseinfo.getClass().getDeclaredFields();
        for(int i=0; i<fields.length; i++)
        {
            Field f = fields[i];
            String name = f.getName();
            String value = getFieldValue(casebaseinfo, name);
			String regex = "$"+name+"$";
			xml = xml.replace(regex, value);
        }
		
        Calendar cal = Calendar.getInstance();
        String systemYear = "$systemYear$";
        String systemMonth = "$systemMonth$";
        String systemDay = "$systemDay$";
        
        String  year = ""+cal.get(Calendar.YEAR); 
        String month = (cal.get(Calendar.MONTH) + 1)+""; 
        String day = ""+cal.get(Calendar.DAY_OF_MONTH); 
        
		xml = xml.replace(systemYear, year);
		xml = xml.replace(systemMonth, month);
		xml = xml.replace(systemDay, day);
        
		relainfo.setTempid(tempid);
		relainfo.setCaseid(caseid);
		relainfo.setNoticecode(casebaseinfo.getCsaecode());
		relainfo.setNodeid(casebaseinfo.getNodeid());
		relainfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);
		relainfo.setType(type);
		relainfo.setNoticename(noticeName);
		relainfo.setContents(xml);
		relainfo.setBuildtime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		relainfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		relainfo.setOperator(user.getUsername());
		casenoticerelainfoDao.saveOrUpdate(relainfo);
	}

	/**
	 * 自动生成案件code
	 */
	public String getcaseCodeAuto() {
		String code = "淄博政复（"+Calendar.getInstance().get(Calendar.YEAR) + "）";
		String casecode = code + "001号";
		try{
			String  sql  = "select csaecode from b_casebaseinfo where csaecode like '"+code + "%' order by csaecode desc";
			List<Map<String, String>> list = (List<Map<String, String>>) casebaseinfoDao.findMapBySql(sql);
			if (!CollectionUtils.isEmpty(list)){
				String findCode = list.get(0).get("csaecode");
				String[] codes = findCode.split(code);
				if(codes.length > 1 ){
					String[] add = {"","0","00"};
					int  num = Integer.valueOf(codes[1].split("号")[0].trim());
					String numStr = String.valueOf(num+1);
					casecode = code + add[3-numStr.length()] + numStr +"号";
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return casecode;
	}
	
	/**
	 * 查询待办任务数
	 * @return
	 */
	public String queryTodoCaseNum(){
		String result = "0";
		StringBuffer sql = new StringBuffer();
		sql.append("select t.caseid as num from B_CASEBASEINFO t ");
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append(" where t.region = '" + user.getArea() + "'");
		} else {
			sql.append(" where t.region is null");
		}
		sql.append(" and t.protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("' ");
		String nodeids = queryNodeidsByUserid(user.getUserid());
		if(!StringUtils.isEmpty(nodeids)){
		sql.append(" and t.caseid not in (select caseid from pub_resultbaseinfo where protype != '")
		   .append(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND).append("')")
		   .append(" and t.nodeid in (").append(nodeids).append(")");
		}
		List<Map<String, String>> list = (List<Map<String, String>>) casebaseinfoDao.findMapBySql(sql.toString());
		if(!CollectionUtils.isEmpty(list)){
			result = list.size() + "";
		}
		return result;
	}

	/**
	 * 删除案件
	 * @param caseid
	 */
	public void deleteCase(String caseid) {
		casebaseinfoDao.delete(caseid);
	}
}
package com.wfzcx.aros.bxzfy.service;

import java.lang.reflect.InvocationTargetException;
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
import com.jbf.base.filemanage.component.FileManageComponent;
import com.jbf.base.filemanage.dao.SysFileManageDao;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.bxzfy.dao.RcasebaseinfoDao;
import com.wfzcx.aros.bxzfy.po.Rcasebaseinfo;
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.dao.ResultbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.po.Resultbaseinfo;
import com.wfzcx.aros.util.FlowUtil;
import com.wfzcx.aros.util.GCC;

/**
 * @ClassName: RcasebaseinfoService
 * @Description: 用来处理被复议案件具体业务
 * @author ybb
 * @date 2016年9月20日 下午4:23:24
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.bxzfy.service.RcasebaseinfoService")
public class RcasebaseinfoService {

	@Autowired
	private RcasebaseinfoDao rcasebaseinfoDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private FileManageComponent fileManageComponent;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private SysFileManageDao sysFileManageDao;
	@Autowired
	private ResultbaseinfoDao resultbaseinfoDao;
	
	/**
	 * @Title: queryRcasebaseinfoReqList
	 * @Description: 案件维护-查询被复议案件维护列表信息
	 * @author ybb
	 * @date 2016年9月20日 下午5:32:19
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoReqList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		sql.append(" where t.nodeid = " + GCC.PRONODEBASEINFO_NODEID_MAINTAIN);
		sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
			append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		sql.append(" and t.userid = '" + user.getUserid() + "' "); 	//操作人ID
		
		//判断所属区域是否为空
		if (StringUtils.isNotBlank(user.getArea())) {
			sql.append("and t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("and t.region is null ");	//所属区域
		}
		
		//复议机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (StringUtils.isNotBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//本机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (StringUtils.isNotBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (StringUtils.isNotBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addRcasebaseinfoReq
	 * @Description: 案件维护-新增被复议案件信息
	 * @author ybb
	 * @date 2016年9月21日 上午9:13:25
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String addRcasebaseinfoReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取被复议申请PO，并将页面参数转化到该对象中
		Rcasebaseinfo rcasebaseinfo = new Rcasebaseinfo();
		
		BeanUtils.populate(rcasebaseinfo, param);
		
			//获取系统当前登录用户信息，并将用户信息转到到行政复议申请PO中
		SysUser user = SecureUtil.getCurrentUser();
		
		rcasebaseinfo.setOperator(user.getUsername());	                                    	//操作人
		rcasebaseinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));				//操作时间
		rcasebaseinfo.setRegion(user.getArea());												//所属区域
		rcasebaseinfo.setState(GCC.RCASEBASEINFO_STATE_REQ);									//状态
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));	//数据最后更新时间
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN));			//环节
		rcasebaseinfo.setUserid(user.getUserid());												//申请人ID
		
			//新增被复议案件申请
		String rcaseid = (String) rcasebaseinfoDao.save(rcasebaseinfo);
		
			//判断是否上传了附件
				//上传，将案件ID更新至附件表的keyid中
				//未长传，不做处理
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			String updFjKeySql = "update sys_filemanage t set t.keyid = '" + rcaseid + "' where t.keyid = '" + fjkeyid + "'";
			
			sysFileManageDao.updateBySql(updFjKeySql);
		}
		
		//4、记录流程日志
		
		return rcaseid;
	}
	
	/**
	 * @Title: updateRcasebaseinfoReq
	 * @Description: 案件维护-修改被复议案件信息
	 * @author ybb
	 * @date 2016年9月21日 上午9:30:39
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateRcasebaseinfoReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的被复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}
		
		
		//3、业务逻辑处理
			//转化页面表单属性
		BeanUtils.populate(rcasebaseinfo, param);
		
			//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		rcasebaseinfo.setOperator(user.getUsername());											//操作人
		rcasebaseinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));				//操作日期
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		rcasebaseinfo.setUserid(user.getUserid());												//用户ID
		
			//修改被复议案件信息
		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录流程日志
	}
	
	/**
	 * @Title: delRcasebaseinfoReq
	 * @Description: 案件维护-删除被复议案件信息
	 * @author ybb
	 * @date 2016年9月21日 上午9:48:14
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delRcasebaseinfoReq(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}
	
		//3、业务逻辑处理		
			//删除新增复议申请信息
		String where = "rcaseid = '" + rcaseid + "'";
		rcasebaseinfoDao.deleteBySQL("B_RCASEBASEINFO", where);
		
			//判断是否上传了附件
				//上传，删除上传附件
				//未上传，不做处理
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + rcaseid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			//删除上传附件
			fileManageComponent.deleteFilesByKeyId(rcaseid);
		}
	}
	
	/**
	 * @Title: queryRcasebaseinfoByRcaseid
	 * @Description: 案件查看-查询被复议案件详细信息
	 * @author ybb
	 * @date 2016年9月21日 上午10:03:05
	 * @param rcaseid
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public Rcasebaseinfo queryRcasebaseinfoByRcaseid(String rcaseid) throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		sql.append("where t.rcaseid = '" + rcaseid + "' "); //案件ID
		
		List<Rcasebaseinfo> rcasebaseinfos = (List<Rcasebaseinfo>) rcasebaseinfoDao.findVoBySql(sql.toString(), Rcasebaseinfo.class);
		if (rcasebaseinfos == null || rcasebaseinfos.isEmpty()) {
			return null;
		}
		
		return rcasebaseinfos.get(0);
	}
	
	/**
	 * @Title: addFlowForReq
	 * @Description: 案件维护-发送
	 * @author ybb
	 * @date 2016年9月21日 上午10:32:19
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForReq(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}

		//3、业务逻辑处理
			//更新被复议案件处理标志、环节编号
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DIVISION));		  	//节点编号
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息，并将用户信息转到到行政复议申请PO中
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
				
			//判断日志信息是否正常
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			
				//转化提交操作日志
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
					new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN), rcaseid, probaseinfo, user, null, null, null);
				
				//新增流程日志
			probaseinfoDao.save(probaseinfo);
			
		} else {
			
			probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
			probaseinfo.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
					new BigDecimal(GCC.PRONODEBASEINFO_NODEID_MAINTAIN), rcaseid, probaseinfo, user, null, null, null);
			
				//新增流程日志
			probaseinfoDao.save(probaseinfo);
		}
	}
	
	/**
	 * @Title: queryRcasebaseinfoDivisionList
	 * @Description: 分案审批-查询分案审批列表信息
	 * @author ybb
	 * @date 2016年9月21日 下午1:45:21
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoDivisionList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			
			sql.append("and rcaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
				append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and nodeid = " + GCC.PRONODEBASEINFO_NODEID_DIVISION);
			sql.append(" and t.rcaseid not in (").
				append(" select caseid from PUB_PROBASEINFO where opttype in('" + GCC.PROBASEINFO_OPTTYPE_PROCESSED + "','" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "')").
				append(" and protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'").
				append(" and userid != " + user.getUserid()).
				append(" and nodeid = ").append(GCC.PRONODEBASEINFO_NODEID_DIVISION).
				append(")");
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//复议机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收处理
	 * @author ybb
	 * @date 2016年9月21日 下午2:59:54
	 * @param param
	 * @throws AppException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateOpttypeByCaseid(Map<String, Object> param)
			throws AppException, IllegalAccessException, InvocationTargetException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("接收失败：案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		
		//3、业务逻辑处理
		if (rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) ||
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED)) {
			
			rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);            					//处理标志：1_已接收
			rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));   //数据最后更新时间
			
				//修改行政复议申请处理标志为：1_已接收
			rcasebaseinfoDao.update(rcasebaseinfo);
			
			//4、记录【已接收】过程日志
			SysUser user = SecureUtil.getCurrentUser();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			sql.append(" from PUB_PROBASEINFO t ");
			sql.append(" where t.caseid = '" + rcaseid + "'");
			sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
			sql.append(" order by t.endtime desc");
			List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
			
			if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
				
				Probaseinfo probaseinfo = new Probaseinfo();
				probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
				probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
				probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
						rcasebaseinfo.getNodeid(), rcaseid, probaseinfo, user);
				
				probaseinfoDao.save(probaseinfo);
			}
		}
	}
	
	/**
	 * @Title: queryUserList
	 * @Description: 分案处理-返回用户列表
	 * @author ybb
	 * @date 2016年9月21日 下午3:47:42
	 * @param param
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryUserList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
//		sql.append("select t.userid, t.usercode, t.username, t.userpswd, t.usertype, t.orgcode, t.grpcode, t.status, ");
//		sql.append("t.createtime, t.overduedate, t.isca, t.remark, t.createuser, t.updatetime, t.modifyuser, t.modifytime, ");
//		sql.append(" (select b.name from SYS_DEPT b where b.code = t.orgcode) orgcode_mc, ");
//		sql.append("from SYS_USER t ");
		
		
		sql.append(" select t.userid, t.usercode, t.username, t.userpswd, t.usertype, t.orgcode, t.grpcode, t.status,  ");
		sql.append(" t.createtime, t.overduedate, t.isca, t.remark, t.createuser, t.updatetime, t.modifyuser, t.modifytime ");
		sql.append(" from SYS_USER t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();

		//判断用户是否是超级管理员
		if (!GCC.SYS_ADMINISTER.equals(user.getModifyuser())) {
			sql.append("where t.usertype != 2 and t.status = 0 and t.isca = 0");
			sql.append("and t.userid != " + user.getUserid());	
		} else {
			sql.append("where t.userid = " + user.getUserid());	
		}
		    sql.append(" order by  t.usercode ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addRcasebaseinfoDivision
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月21日 下午5:01:21
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRcasebaseinfoDivision(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
			//判断承办人是否为空
		String undertakeid = StringUtil.stringConvert(param.get("undertakeid"));
		if (StringUtils.isBlank(undertakeid)) {
			throw new AppException("保存失败：请选择承办人");
		}
		
		String undertaker = StringUtil.stringConvert(param.get("undertaker"));
		if (StringUtils.isBlank(undertaker)) {
			throw new AppException("保存失败：请选择承办人");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的被复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DIVISION))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(rcasebaseinfo, param);
		
		rcasebaseinfo.setAuditmanid(user.getUserid());											//审核人ID
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		rcasebaseinfoDao.update(rcasebaseinfo);
			
		//4、记录流程日志
			/*//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		//probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(rcaseid);
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(probaseinfoList.get(0).getString("starttime"));
		probaseinfo.setEndtime(probaseinfoList.get(0).getString("endtime"));
		probaseinfo.setResult("");
		probaseinfo.setRemark("");
		probaseinfo.setUserid(user.getUserid());
		probaseinfo.setResultmsg("");
		
		//修改过程日志
		probaseinfoDao.update(probaseinfo); */
	}
	
	/**
	 * @Title: addFlowForDivision
	 * @Description: 分案处理-发送
	 * @author ybb
	 * @date 2016年9月21日 下午5:59:05
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForDivision(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DIVISION))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}

		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
		//更新被复议案件处理标志、环节编号
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISPOSE));		  	//节点编号
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		rcasebaseinfo.setState(GCC.RCASEBASEINFO_STATE_NOCLOSURE);                              //状态

		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
				new BigDecimal(probaseins.get(0).getString("nodeid")), rcaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);	
	}
	
	/**
	 * @Title: addFlowForReturn
	 * @Description: 被复议案件-退回
	 * @author ybb
	 * @date 2016年9月22日 上午8:50:24
	 * @param param
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForReturn(Map<String, Object> param) throws AppException{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (rcasebaseinfo.getNodeid() == null || 
				rcasebaseinfo.getNodeid().compareTo(BigDecimal.ONE) <= 0 ) {
			throw new AppException("保存失败：该案件所处环节不能退回，请刷新页面再退回");
		}
		
		BigDecimal nodeid = rcasebaseinfo.getNodeid();	//流程节点
		
			//修改案件信息，处理标志：9_已退回
		rcasebaseinfo.setNodeid(rcasebaseinfo.getNodeid().subtract(BigDecimal.ONE));		   //流程节点退一步
		if (rcasebaseinfo.getNodeid().equals(BigDecimal.ONE)) {
			rcasebaseinfo.setState(GCC.RCASEBASEINFO_STATE_REQ);
		}
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_RETURNED);   						   //处理标志：9_已退回
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		
		rcasebaseinfoDao.update(rcasebaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：未找到该案件流程信息");
		}
		
			//新增退回日志
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setNodeid(nodeid);
		probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		
		probaseinfo = FlowUtil.genReturnedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO, rcaseid, 
				probaseinfo, user, "", "");
		
		//修改过程日志
		probaseinfoDao.save(probaseinfo); 
	}
	
	/**
	 * @Title: queryRcasebaseinfoDisposeList
	 * @Description: 案件处理-查询案件处理列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:10:54
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoDisposeList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			
			sql.append("and rcaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
				append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.PRONODEBASEINFO_NODEID_DISPOSE);
			sql.append(" and t.undertakeid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//复议机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addRcasebaseinfoDispose
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:18:37
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRcasebaseinfoDispose(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
			//判断委托律师是否为空
		String lawyer = StringUtil.stringConvert(param.get("lawyer"));
		if (StringUtils.isBlank(lawyer)) {
			throw new AppException("保存失败：请输入委托律师");
		}
		
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的被复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISPOSE))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(rcasebaseinfo, param);
		
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		rcasebaseinfoDao.update(rcasebaseinfo);
			
		//4、记录流程日志
			/*//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		//probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(rcaseid);
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(probaseinfoList.get(0).getString("starttime"));
		probaseinfo.setEndtime(probaseinfoList.get(0).getString("endtime"));
		probaseinfo.setResult("");
		probaseinfo.setRemark("");
		probaseinfo.setUserid(user.getUserid());
		probaseinfo.setResultmsg("");
		
		//修改过程日志
		probaseinfoDao.update(probaseinfo); */
	}
	
	/**
	 * @Title: addFlowForDispose
	 * @Description: 案件处理-发送
	 * @author ybb
	 * @date 2016年9月22日 上午9:21:18
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForDispose(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISPOSE))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}

		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
			//更新被复议案件处理标志、环节编号
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT));		  		//节点编号
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
				new BigDecimal(probaseins.get(0).getString("nodeid")), rcaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);	
	}
	
	/**
	 * @Title: queryRcasebaseinfoAuditList
	 * @Description: 案件审批-查询案件审批列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:30:31
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoAuditList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		sql.append(" where t.state = '").append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("' ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("and t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("and t.region is null ");	//所属区域
		}
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			
			sql.append("and rcaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
				append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.PRONODEBASEINFO_NODEID_AUDIT);
			sql.append(" and t.auditmanid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//复议机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addRcasebaseinfoAudit
	 * @Description: 案件审批-保存案件审批信息
	 * @author ybb
	 * @date 2016年9月22日 上午9:32:27
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRcasebaseinfoAudit(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
			//判断承办人是否为空
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择处理结果");
		}
		
			//处理意见
		String remark = StringUtil.stringConvert(param.get("remark"));
			//结果内容
		String resultmsg = StringUtil.stringConvert(param.get("resultmsg"));
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的被复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(rcasebaseinfo, param);
		
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		rcasebaseinfoDao.update(rcasebaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		//获取最大的流程序列号
		sql.setLength(0);
		sql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(sql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseinfoList.get(0).getString("seqno"));
		}
				
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(rcaseid);
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(probaseinfoList.get(0).getString("starttime"));
		probaseinfo.setEndtime(probaseinfoList.get(0).getString("endtime"));
		probaseinfo.setResult(result);
		probaseinfo.setRemark(remark);
		probaseinfo.setUserid(user.getUserid());
		probaseinfo.setResultmsg(resultmsg);
		
		//修改过程日志
		probaseinfoDao.update(probaseinfo); 
	}
	
	/**
	 * @Title: addFlowForAudit
	 * @Description: 案件审阅-发送
	 * @author ybb
	 * @date 2016年9月22日 上午9:39:48
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForAudit(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}
		
			//判断审批结果是否为空
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}
				
		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
		if (result.equals(GCC.PROBASEINFO_OPTTYPE_PASS)) { //审批结果：01_同意
			
			rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT));		  		
			
		} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_NOPASS)) { //审批结果：02_不同意
			
			rcasebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_DISPOSE));		  	
			
		} else {
			throw new AppException("审批结果错误");
		}
		
			//更新被复议案件处理标志、环节编号
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setResult(result);
		probaseinfo.setRemark(probaseins.get(0).getString("remark"));
		probaseinfo.setResultmsg(probaseins.get(0).getString("resultmsg"));
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_AUDIT), rcaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);
	}
	
	/**
	 * @Title: queryRcasebaseinfoResultList
	 * @Description: 案件结果-查询案件结果列表信息
	 * @author ybb
	 * @date 2016年9月22日 上午10:05:02
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoResultList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		//处理标志
		String opttype = StringUtil.stringConvert(param.get("opttype"));
		if (opttype.equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED)) { //已处理
			
			sql.append("and rcaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
				append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.PRONODEBASEINFO_NODEID_RESULT);
			sql.append(" and t.undertakeid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//复议机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addRcasebaseinfoResult
	 * @Description: 案件结果-保存案件结果信息
	 * @author ybb
	 * @date 2016年9月22日 上午10:06:46
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addRcasebaseinfoResult(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String rcaseid = StringUtil.stringConvert(param.get("rcaseid"));
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
			//判断承办人是否为空
		String result = StringUtil.stringConvert(param.get("result"));
		if (StringUtils.isBlank(result)) {
			throw new AppException("保存失败：请选择处理结果");
		}
		
			//处理意见
		String remark = StringUtil.stringConvert(param.get("resultremark"));
			//结果内容
		String resultmsg = StringUtil.stringConvert(param.get("resultmsg"));
				
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的被复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(rcasebaseinfo, param);
		
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		rcasebaseinfoDao.update(rcasebaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		//获取最大的流程序列号
		sql.setLength(0);
		sql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		sql.append(" where t.caseid = '" + rcaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		sql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(sql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseinfoList.get(0).getString("seqno"));
		}
				
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(rcaseid);
		probaseinfo.setOperator(user.getUsername());
		probaseinfo.setStarttime(probaseinfoList.get(0).getString("starttime"));
		probaseinfo.setEndtime(probaseinfoList.get(0).getString("endtime"));
		probaseinfo.setResult(result);
		probaseinfo.setRemark(remark);
		probaseinfo.setUserid(user.getUserid());
		probaseinfo.setResultmsg(resultmsg);
	
		//修改过程日志
		probaseinfoDao.update(probaseinfo); 
	}
	
	/**
	 * @Title: addFlowForResult
	 * @Description: 案件结果-发送
	 * @author ybb
	 * @date 2016年9月22日 上午10:07:54
	 * @param rcaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForResult(String rcaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(rcaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断被复议申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Rcasebaseinfo rcasebaseinfo = rcasebaseinfoDao.get(rcaseid);
		if(rcasebaseinfo == null){
			throw new AppException("修改失败：未找到对应的行政复议申请");
		}
		if (!rcasebaseinfo.getNodeid().equals(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				rcasebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + rcasebaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}

			//处理结果
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未处理该被复议案件，请处理后再发送");
		}
				
		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + rcaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
			//更新被复议案件处理标志、环节编号
		rcasebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);						  			//处理标志
		rcasebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		rcasebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);                             //状态

		rcasebaseinfoDao.update(rcasebaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//将处理结果记录到流程结果表中
		Resultbaseinfo rbi = new Resultbaseinfo();
		rbi.setProcessid(probaseins.get(0).getString("processid"));
		rbi.setSeqno(seqno);
		rbi.setResult(result);
		rbi.setRemark(probaseins.get(0).getString("remark"));
		rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);
		rbi.setResultmsg(probaseins.get(0).getString("resultmsg"));
		
		rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_RCASEBASEINFO,
				new BigDecimal(GCC.PRONODEBASEINFO_NODEID_RESULT), rcaseid, rbi, user);
		
		//新增结果日志
		resultbaseinfoDao.save(rbi);
	}
	
	/**
	 * @Title: queryRcasebaseinfoViewList
	 * @Description: 案件查询-查询案件查询列表信息
	 * @author ybb
	 * @date 2016年9月24日 下午4:21:01
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryRcasebaseinfoViewList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.rcaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.appname, t.thirdname, t.receiver, ");
		sql.append(" t.rectime, t.annex, t.undertaker, t.undertakedep, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, ");
		sql.append(" t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, t.auditmanid ");
		sql.append(" from B_RCASEBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//复议机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//申请人
		String appname = StringUtil.stringConvert(param.get("appname"));
		if (!StringUtils.isBlank(appname)) {
			sql.append(" and t.appname like '%").append(appname).append("%'");
		}
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
}
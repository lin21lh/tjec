package com.wfzcx.aros.ysaj.service;

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
import com.wfzcx.aros.flow.dao.ProbaseinfoDao;
import com.wfzcx.aros.flow.dao.ResultbaseinfoDao;
import com.wfzcx.aros.flow.po.Probaseinfo;
import com.wfzcx.aros.flow.po.Resultbaseinfo;
import com.wfzcx.aros.util.FlowUtil;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.ysaj.dao.AdmlitbaseinfoDao;
import com.wfzcx.aros.ysaj.po.Admlitbaseinfo;

/**
 * @ClassName: AdmlitbaseinfoService
 * @Description: 用来处理应诉案件具体业务
 * @author ybb
 * @date 2016年9月22日 下午1:57:15
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.ysaj.service.AdmlitbaseinfoService")
public class AdmlitbaseinfoService {

	@Autowired
	private AdmlitbaseinfoDao admlitbaseinfoDao;
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
	 * @Title: queryAdmlitbaseinfoReqList
	 * @Description: 案件维护-查询应诉案件维护列表信息
	 * @author ybb
	 * @date 2016年9月22日14:02:38
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoReqList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		sql.append(" where t.nodeid = " + GCC.ADMLITBASEINFO_NODEID_MAINTAIN);
		sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
			append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		sql.append(" and t.userid = '" + user.getUserid() + "' "); 	//操作人ID
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("and t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("and t.region is null ");	//所属区域
		}
		
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
/*		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}*/
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addAdmlitbaseinfoReq
	 * @Description: 案件维护-新增应诉案件信息
	 * @author ybb
	 * @date 2016年9月22日14:03:01
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String addAdmlitbaseinfoReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取应诉申请PO，并将页面参数转化到该对象中
		Admlitbaseinfo admlitbaseinfo = new Admlitbaseinfo();
		
		BeanUtils.populate(admlitbaseinfo, param);
		
			//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
		SysUser user = SecureUtil.getCurrentUser();
		
		admlitbaseinfo.setOperator(user.getUsername());	                                    	//操作人
		admlitbaseinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));				//操作时间
		admlitbaseinfo.setRegion(user.getArea());												//所属区域
		admlitbaseinfo.setState(GCC.RCASEBASEINFO_STATE_REQ);									//状态
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));	//数据最后更新时间
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN));			//环节
		admlitbaseinfo.setUserid(user.getUserid());												//受案法院ID
		
			//一审设置关联案件ID为空
		if (admlitbaseinfo.getStage().equals(GCC.ADMLITBASEINFO_STAGE_FIRSTINSTANCE)) {
			admlitbaseinfo.setRelaacaseid("");
		}
			//判断是否是单独被告
		if (admlitbaseinfo.getPartytype().equals(GCC.ADMLITBASEINFO_PARTYTYPE_ALONE)) {
			admlitbaseinfo.setDefendant("");
		}
		
			//新增应诉案件
		String acaseid = (String) admlitbaseinfoDao.save(admlitbaseinfo);
		
			//判断是否上传了附件
				//上传，将案件ID更新至附件表的keyid中
				//未长传，不做处理
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			String updFjKeySql = "update sys_filemanage t set t.keyid = '" + acaseid + "' where t.keyid = '" + fjkeyid + "'";
			
			sysFileManageDao.updateBySql(updFjKeySql);
		}
		
		//4、记录流程日志
		
		return acaseid;
	}
	
	/**
	 * @Title: updateAdmlitbaseinfoReq
	 * @Description: 案件维护-修改应诉案件信息
	 * @author ybb
	 * @date 2016年9月22日14:03:43
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateAdmlitbaseinfoReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉申请");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}
		
		
		//3、业务逻辑处理
			//转化页面表单属性
		BeanUtils.populate(admlitbaseinfo, param);
		
			//获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		admlitbaseinfo.setOperator(user.getUsername());											//操作人
		admlitbaseinfo.setOpttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));				//操作日期
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		admlitbaseinfo.setUserid(user.getUserid());												//用户ID
		//一审设置关联案件ID为空
		if (admlitbaseinfo.getStage().equals(GCC.ADMLITBASEINFO_STAGE_FIRSTINSTANCE)) {
			admlitbaseinfo.setRelaacaseid("");
		}
		//判断是否是单独被告
		if (admlitbaseinfo.getPartytype().equals(GCC.ADMLITBASEINFO_PARTYTYPE_ALONE)) {
			admlitbaseinfo.setDefendant("");
		}
		
			//修改应诉案件信息
		admlitbaseinfoDao.update(admlitbaseinfo);
		
		//4、记录流程日志
	}
	
	/**
	 * @Title: delAdmlitbaseinfoReq
	 * @Description: 案件维护-删除应诉案件信息
	 * @author ybb
	 * @date 2016年9月22日14:03:50
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delAdmlitbaseinfoReq(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}
	
		//3、业务逻辑处理		
			//删除应诉案件
		String where = "acaseid = '" + acaseid + "'";
		admlitbaseinfoDao.deleteBySQL("B_ADMLITBASEINFO", where);
		
			//判断是否上传了附件
				//上传，删除上传附件
				//未上传，不做处理
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + acaseid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			//删除上传附件
			fileManageComponent.deleteFilesByKeyId(acaseid);
		}
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoByAcaseid
	 * @Description: 案件查看-查询应诉案件详细信息
	 * @author ybb
	 * @date 2016年9月22日14:05:16
	 * @param acaseid
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public Admlitbaseinfo queryAdmlitbaseinfoByAcaseid(String acaseid) throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECCASETYPE' and a.status=0 and a.code = t.rectype) rectype, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifincourt) ifincourt ");
		sql.append(" from B_ADMLITBASEINFO t ");
		sql.append("where t.acaseid = '" + acaseid + "' "); //案件ID
		
		List<Admlitbaseinfo> admlitbaseinfos = (List<Admlitbaseinfo>) admlitbaseinfoDao.findVoBySql(sql.toString(), Admlitbaseinfo.class);
		if (admlitbaseinfos == null || admlitbaseinfos.isEmpty()) {
			return null;
		}
		
		return admlitbaseinfos.get(0);
	}
	
	/**
	 * @Title: addFlowForReq
	 * @Description: 案件维护-发送
	 * @author ybb
	 * @date 2016年9月22日14:05:30
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForReq(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已提交】或【已退回】");
		}

		//3、业务逻辑处理
			//更新应诉案件处理标志、环节编号
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DIVISION));		  	//节点编号
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		admlitbaseinfoDao.update(admlitbaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息，并将用户信息转到到应诉案件PO中
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
				
			//判断日志信息是否正常
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			
				//转化提交操作日志
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
					new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN), acaseid, probaseinfo, user, null, null, null);
				
				//新增流程日志
			probaseinfoDao.save(probaseinfo);
			
		} else {
			
			probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
			probaseinfo.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
					new BigDecimal(GCC.ADMLITBASEINFO_NODEID_MAINTAIN), acaseid, probaseinfo, user, null, null, null);
			
				//新增流程日志
			probaseinfoDao.save(probaseinfo);
		}
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoDivisionList
	 * @Description: 分案审批-查询分案审批列表信息
	 * @author ybb
	 * @date 2016年9月22日14:05:47
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoDivisionList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
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
			
			sql.append("and acaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
			append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = " + GCC.ADMLITBASEINFO_NODEID_DIVISION);
			sql.append(" and t.acaseid not in (").
				append(" select caseid from PUB_PROBASEINFO where opttype in('" + GCC.PROBASEINFO_OPTTYPE_PROCESSED + "','" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "')").
				append(" and protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'").
				append(" and userid != " + user.getUserid()).
				append(" and nodeid = ").append(GCC.ADMLITBASEINFO_NODEID_DIVISION).
				append(")");
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: updateOpttypeByCaseid
	 * @Description: 案件接收处理
	 * @author ybb
	 * @date 2016年9月22日14:06:00
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
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("接收失败：案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		
		//3、业务逻辑处理
		if (admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_SUBMITTED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED)) {
			
			admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);            					//处理标志：1_已接收
			admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));   //数据最后更新时间
			
				//修改应诉案件处理标志为：1_已接收
			admlitbaseinfoDao.update(admlitbaseinfo);
			
			//4、记录【已接收】过程日志
			SysUser user = SecureUtil.getCurrentUser();
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
			sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
			sql.append(" from PUB_PROBASEINFO t ");
			sql.append(" where t.caseid = '" + acaseid + "'");
			sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
			sql.append(" order by t.endtime desc");
			List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
			
			if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
				
				Probaseinfo probaseinfo = new Probaseinfo();
				probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
				probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
				probaseinfo = FlowUtil.genAcceptedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
						admlitbaseinfo.getNodeid(), acaseid, probaseinfo, user);
				
				probaseinfoDao.save(probaseinfo);
			}
		}
	}
	
	/**
	 * @Title: queryUserList
	 * @Description: 分案处理-返回用户列表
	 * @author ybb
	 * @date 2016年9月22日14:06:07
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
		
		sql.append(" order by t.usercode  ");
		
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
//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
//	public PaginationSupport queryRoleListForSend(Map<String, Object> param) throws AppException {
//		//页面条数
//		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
//				: PaginationSupport.PAGESIZE;
//		//起始条数
//		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
//		
//		//获取code
//		String code = StringUtil.stringConvert(param.get("code"));
//		//获取StringBuffer对象，用来拼接sql语句
//		StringBuffer sql = new StringBuffer();
//		sql.append(" select a.userid,a.usercode, a.username, a.createtime ");
//		sql.append(" from SYS_USER a left join SYS_DEPT b on a.orgcode = b.code where b.code = "+ code);
//		
//		//获取当前节点 
//		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
//	}
	
	
	
	/**
	 * @Title: addAdmlitbaseinfoDivision
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月22日14:06:12
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAdmlitbaseinfoDivision(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
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
			//判断应诉申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉申请");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DIVISION))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(admlitbaseinfo, param);
		
		admlitbaseinfo.setAuditmanid(user.getUserid());											//审核人ID
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		admlitbaseinfoDao.update(admlitbaseinfo);
			
		//4、记录流程日志
			/*//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		//probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(acaseid);
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
	 * @date 2016年9月22日14:06:18
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForDivision(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DIVISION))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}
		
		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}

		//3、业务逻辑处理
			//更新应诉案件处理标志、环节编号
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DISPOSE));		  	//节点编号
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		admlitbaseinfo.setState(GCC.RCASEBASEINFO_STATE_NOCLOSURE);                              //状态

		admlitbaseinfoDao.update(admlitbaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
				new BigDecimal(probaseins.get(0).getString("nodeid")), acaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);	
	}
	
	/**
	 * @Title: addFlowForReturn
	 * @Description: 应诉案件-退回
	 * @author ybb
	 * @date 2016年9月22日14:06:23
	 * @param param
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForReturn(Map<String, Object> param) throws AppException{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (admlitbaseinfo.getNodeid() == null || 
				admlitbaseinfo.getNodeid().compareTo(BigDecimal.ONE) <= 0 ) {
			throw new AppException("保存失败：该案件所处环节不能退回，请刷新页面再退回");
		}
		
		BigDecimal nodeid = admlitbaseinfo.getNodeid();	//流程节点
		
			//修改案件信息，处理标志：9_已退回
		admlitbaseinfo.setNodeid(admlitbaseinfo.getNodeid().subtract(BigDecimal.ONE));		   //流程节点退一步
		if (admlitbaseinfo.getNodeid().equals(BigDecimal.ONE)) {
			admlitbaseinfo.setState(GCC.RCASEBASEINFO_STATE_REQ);
		}
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_RETURNED);   						   //处理标志：9_已退回
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		
		admlitbaseinfoDao.update(admlitbaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
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
		
		probaseinfo = FlowUtil.genReturnedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO, acaseid, 
				probaseinfo, user, "", "");
		
		//修改过程日志
		probaseinfoDao.save(probaseinfo); 
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoDisposeList
	 * @Description: 案件处理-查询案件处理列表信息
	 * @author ybb
	 * @date 2016年9月22日14:06:28
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoDisposeList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
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
			
			sql.append("and acaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
			append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.ADMLITBASEINFO_NODEID_DISPOSE);
			sql.append(" and t.undertakeid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addAdmlitbaseinfoDispose
	 * @Description: 案件处理-保存案件处理信息
	 * @author ybb
	 * @date 2016年9月22日14:06:33
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAdmlitbaseinfoDispose(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("修改失败：案件ID为空");
		}
		
			//判断出庭负责人是否为空
		String courthead = StringUtil.stringConvert(param.get("courthead"));
		if (StringUtils.isBlank(courthead)) {
			throw new AppException("保存失败：请输入出庭负责人");
		}
		
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉申请");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【 立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DISPOSE))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(admlitbaseinfo, param);
		
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		admlitbaseinfoDao.update(admlitbaseinfo);
			
		//4、记录流程日志
			/*//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		Probaseinfo probaseinfo = new Probaseinfo();
		
		probaseinfo.setId(probaseinfoList.get(0).getString("id"));
		probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		//probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(acaseid);
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
	 * @date 2016年9月22日14:06:39
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForDispose(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DISPOSE))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}

		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
			//更新应诉案件处理标志、环节编号
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_AUDIT));		  		//节点编号
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		admlitbaseinfoDao.update(admlitbaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
				new BigDecimal(probaseins.get(0).getString("nodeid")), acaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);	
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoAuditList
	 * @Description: 案件审阅-查询案件审阅列表信息
	 * @author ybb
	 * @date 2016年9月22日14:06:44
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoAuditList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
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
			
			sql.append("and acaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
			append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.ADMLITBASEINFO_NODEID_AUDIT);
			sql.append(" and t.auditmanid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addAdmlitbaseinfoAudit
	 * @Description: 案件审批-保存案件审批信息
	 * @author ybb
	 * @date 2016年9月22日14:06:50
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAdmlitbaseinfoAudit(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
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
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉申请");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【 立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_AUDIT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(admlitbaseinfo, param);
		
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		admlitbaseinfoDao.update(admlitbaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		//获取最大的流程序列号
		sql.setLength(0);
		sql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
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
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(acaseid);
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
	 * @Description: 案件审批-发送
	 * @author ybb
	 * @date 2016年9月22日14:07:06
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForAudit(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_AUDIT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}
		
			//判断审批结果是否为空
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}
				
		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
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
			
			admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_RESULT));		  		
			
		} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_NOPASS)) { //审批结果：02_不同意
			
			admlitbaseinfo.setNodeid(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_DISPOSE));		  	
			
		} else {
			throw new AppException("审批结果错误");
		}
		
			//更新应诉案件处理标志、环节编号
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);						  	//处理标志
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间

		admlitbaseinfoDao.update(admlitbaseinfo);
		
		//4、记录提交日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
				
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setResult(result);
		probaseinfo.setRemark(probaseins.get(0).getString("remark"));
		probaseinfo.setResultmsg(probaseins.get(0).getString("resultmsg"));
		
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
				new BigDecimal(GCC.ADMLITBASEINFO_NODEID_AUDIT), acaseid, probaseinfo, user, "", "");
			
		//新增结果日志
		probaseinfoDao.save(probaseinfo);
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoResultList
	 * @Description: 案件结果-查询案件结果列表信息
	 * @author ybb
	 * @date 2016年9月22日14:07:12
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoResultList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
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
			
			sql.append("and acaseid in (select caseid from VIEW_PROBASEINFO where protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO  +
					"' and opttype in( '" + GCC.PROBASEINFO_OPTTYPE_PROCESSED 
					+ "','" + GCC.PROBASEINFO_OPTTYPE_RETURNED + "') and userid = '" + user.getUserid()+ "')");	
			
		} else {
			
			sql.append(" and t.state in ('").append(GCC.RCASEBASEINFO_STATE_REQ).append("','").
				append(GCC.RCASEBASEINFO_STATE_NOCLOSURE).append("')");
			sql.append(" and t.nodeid = ").append(GCC.ADMLITBASEINFO_NODEID_RESULT);
			sql.append(" and t.undertakeid = ").append(user.getUserid());
		}
				
		//本机关案号
		String lcasecode = StringUtil.stringConvert(param.get("lcasecode"));
		if (!StringUtils.isBlank(lcasecode)) {
			sql.append(" and t.lcasecode like '%").append(lcasecode).append("%'");
		} 
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addAdmlitbaseinfoResult
	 * @Description: 案件结果-保存案件结果信息
	 * @author ybb
	 * @date 2016年9月22日14:07:17
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addAdmlitbaseinfoResult(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String acaseid = StringUtil.stringConvert(param.get("acaseid"));
		if(StringUtils.isBlank(acaseid)){
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
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉申请");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【 立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_RESULT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		
		//3、业务逻辑处理
			//获取系统当前登录用户信息
		//SysUser user = SecureUtil.getCurrentUser();
				
		BeanUtils.populate(admlitbaseinfo, param);
		
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_PROCESSED); 							//处理标志：2_已处理
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		
		admlitbaseinfoDao.update(admlitbaseinfo);
			
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
			//获取接收过程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		sql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		if (probaseinfoList == null || probaseinfoList.isEmpty()) {
			throw new AppException("保存失败：该案件未接收");
		}
		
		//获取最大的流程序列号
		sql.setLength(0);
		sql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		sql.append(" where t.caseid = '" + acaseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
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
		probaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO);
		probaseinfo.setNodeid(new BigDecimal(probaseinfoList.get(0).getString("nodeid")));
		probaseinfo.setSeqno(seqno);
		probaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_ACCEPTED);
		probaseinfo.setCaseid(acaseid);
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
	 * @date 2016年9月22日14:07:27
	 * @param acaseid
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addFlowForResult(String acaseid) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(acaseid)){
			throw new AppException("案件ID为空");
		}
	
		//2、业务逻辑判断
			//判断应诉申请是否正常
			//判断该案件状态不是【 收案未立案】
			//判断该案件环节是否异常
			//判断该案件处理标志不是【已提交】或【已退回】
		Admlitbaseinfo admlitbaseinfo = admlitbaseinfoDao.get(acaseid);
		if(admlitbaseinfo == null){
			throw new AppException("修改失败：未找到对应的应诉案件");
		}
		if (!admlitbaseinfo.getState().equals(GCC.RCASEBASEINFO_STATE_NOCLOSURE)) {
			throw new AppException("修改失败：该案件状态不是【立案未结案】");
		}
		if (!admlitbaseinfo.getNodeid().equals(new BigDecimal(GCC.ADMLITBASEINFO_NODEID_RESULT))) {
			throw new AppException("修改失败：该案件环节异常");
		}
		if(!(admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_PROCESSED) || 
				admlitbaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED))){	
			throw new AppException("送审失败：该案件处理标志不是【已处理】或【已退回】");
		}
		
			//判断日志信息是否正常
			//判断该案件是否被接收
			//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_ACCEPTED + "'");
		logSql.append(" and t.nodeid = " + admlitbaseinfo.getNodeid());
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}

			//处理结果
		String result = probaseins.get(0).getString("result");
		if (StringUtils.isBlank(result)) {
			throw new AppException("未处理该应诉案件，请处理后再发送");
		}
		
		//获取最大的流程序列号
		logSql.setLength(0);
		logSql.append("select max(seqno) seqno from PUB_PROBASEINFO t");
		logSql.append(" where t.caseid = '" + acaseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO + "'");
		logSql.append(" order by t.endtime desc");
		
		BigDecimal seqno = null;
		List<JSONObject> seqnoList = mapDataDao.queryListBySQL(logSql.toString());
		if (seqnoList != null && !seqnoList.isEmpty()) {
			seqno = new BigDecimal(seqnoList.get(0).getString("seqno"));
		} else {
			seqno = new BigDecimal(probaseins.get(0).getString("seqno"));
		}
				
		//3、业务逻辑处理
			//更新应诉案件处理标志、环节编号
		admlitbaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);						  			//处理标志
		admlitbaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		admlitbaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);                             //状态

		admlitbaseinfoDao.update(admlitbaseinfo);
		
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
		
		rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_ADMLITBASEINFO,
				new BigDecimal(GCC.ADMLITBASEINFO_NODEID_RESULT), acaseid, rbi, user);
		
		//新增结果日志
		resultbaseinfoDao.save(rbi);
	}
	
	/**
	 * @Title: queryAdmlitbaseinfoViewList
	 * @Description: 案件查询-查询案件列表信息
	 * @author ybb
	 * @date 2016年9月24日 下午3:21:58
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryAdmlitbaseinfoViewList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
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
		
		//司法机关案号
		String rcasecode = StringUtil.stringConvert(param.get("rcasecode"));
		if (!StringUtils.isBlank(rcasecode)) {
			sql.append(" and t.rcasecode like '%").append(rcasecode).append("%'");
		}
		
		//受案法院
		String rdeptname = StringUtil.stringConvert(param.get("rdeptname"));
		if (!StringUtils.isBlank(rdeptname)) {
			sql.append(" and t.rdeptname like '%").append(rdeptname).append("%'");
		}
		
		//原告
		String plaintiff = StringUtil.stringConvert(param.get("plaintiff"));
		if (!StringUtils.isBlank(plaintiff)) {
			sql.append(" and t.plaintiff like '%").append(plaintiff).append("%'");
		}
				
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: queryCaseList
	 * @Description: 案件维护-根据条件查询关联应诉案件列表
	 * @author ybb
	 * @date 2016年9月27日 下午1:50:58
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryCaseList(Map<String, Object> param, String stage) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.acaseid, t.relaacaseid, t.stage, t.lcasecode, t.rcasecode, t.rdeptname, t.casereason, t.partytype, ");
		sql.append(" t.plaintiff, t.defendant, t.thirdname, t.rectype, t.receiver, t.rectime, t.heartime, t.hearplace, t.annex, ");
		sql.append(" t.undertaker, t.assessor, t.lawyer, t.noticeid, t.operator, t.opttime, t.courthead, t.ifappeal, t.appealtime, ");
		sql.append(" t.remark, t.region, t.registerdate, t.solveddate, t.state, t.lasttime, t.opttype, t.nodeid, t.userid, t.undertakeid, ");
		sql.append(" t.auditmanid, t.headpost, t.ifincourt, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='STAGETYPE' and a.status=0 and a.code = t.stage) stage_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PARTYTYPE' and a.status=0 and a.code = t.partytype) partytype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='RECTYPETYPE' and a.status=0 and a.code = t.rectype) rectype_mc ");
		sql.append(" from B_ADMLITBASEINFO t ");
		
		// 获取当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		//判断所属区域是否为空
		if (!StringUtils.isBlank(user.getArea())) {
			sql.append("where t.region = '" + user.getArea() + "' ");	//所属区域
		} else {
			sql.append("where t.region is null ");	//所属区域
		}
		
		//判断审理阶段
		if (stage.equals(GCC.ADMLITBASEINFO_STAGE_SECONDINSTANCE)) {
			sql.append("and t.stage = '" + GCC.ADMLITBASEINFO_STAGE_FIRSTINSTANCE + "' ");	
		} else if (stage.equals(GCC.ADMLITBASEINFO_STAGE_FINALINSTANCE)) {
			sql.append("and t.stage = '" + GCC.ADMLITBASEINFO_STAGE_SECONDINSTANCE + "' ");	
		}
		sql.append(" order by t.lasttime desc ");
		System.out.println(sql);
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
}
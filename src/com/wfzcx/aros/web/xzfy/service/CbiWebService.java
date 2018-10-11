package com.wfzcx.aros.web.xzfy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.po.SysDicElement;
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
import com.wfzcx.aros.util.UtilTool;
import com.wfzcx.aros.xzfy.dao.CasebaseinfoDao;
import com.wfzcx.aros.xzfy.dao.ThirdbaseinfoDao;
import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.xzfy.po.Thirdbaseinfo;
import com.wfzcx.aros.xzfy.vo.CasebaseinfoVo;

/**
 * @ClassName: CbiWebService
 * @Description: 行政复议外网service：用来处理行政复议外网部分功能
 * @author ybb
 * @date 2016年9月8日 上午10:28:20
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.web.xzfy.service.CbiWebService")
public class CbiWebService {

	@Autowired
	private CasebaseinfoDao casebaseinfoDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private SysDicElementDao dicElementDao;
	@Autowired
	private ProbaseinfoDao probaseinfoDao;
	@Autowired
	private ResultbaseinfoDao resultbaseinfoDao;
	@Autowired
	private ThirdbaseinfoDao thirdbaseinfoDao;
	
	/**
	 * @Title: queryCbiList
	 * @Description: 外网案件查询- 分页查询申请的案件列表
	 * @author ybb
	 * @date 2016年9月8日 上午11:39:36
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryCbiList(Map<String, Object> param) throws AppException {
		
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
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, t.delaydays, t.region, t.intro, t.key, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetype_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DATASOURCE' and a.status=0 and a.code = t.datasource) datasource_mc, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensation_mc, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode) = 'PUB_PRONODEBASEINFO_NODENAME' and a.status = 0 and a.code = nvl(t.nodeid,0)) nodeid_mc ");
		sql.append(" from B_CASEBASEINFO t  where 1 = 1 ");
		
		//申请人证件号码
		String idcode = StringUtil.stringConvert(param.get("idcode"));
		sql.append(" and t.idcode = '").append(idcode).append("'");
		
		//查询码
		String key = StringUtil.stringConvert(param.get("key"));
		sql.append(" and t.key = '").append(key).append("'");
		
		sql.append(" order by t.lasttime desc ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Description: 外网案件查询-获取通知书列表
	 * @author zhangtiantian
	 * @date 2016-09-09
	 */
	public PaginationSupport queryNoticeList(Map<String, Object> param)
	{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		String caseid = (String) param.get("caseid");
		StringBuffer sql = new StringBuffer();
		sql.append("select b.noticeid, b.buildtime,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='DOCTYPE' and a.status=0 and a.code = b.doctype) doctype");
		sql.append(" from B_NOTICEBASEINFO b");
		sql.append(" where b.caseid='");
		sql.append(caseid);
		sql.append("'");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	/**
	 * @Title: queryDicByElemenetcode
	 * @Description: 通过字典编码查询字典列表
	 * @author ybb
	 * @date 2016年9月9日 上午9:37:16
	 * @param elementcode
	 * @return
	 * @throws AppException
	 */
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public List queryDicByElemenetcode(String elementcode) throws AppException {
		
		//1、入口参数判断
			//判断字典编码是否为空
		if (StringUtils.isBlank(elementcode)) {
			throw new AppException("数据项为空");
		}
		
		//2、业务逻辑判断
			//判断系统字典配置是否正常
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode);
		if (dicElement == null) {
			throw new AppException("数据项：" + elementcode + "未定义");
		}
			//判断表编码（tablecode）是否为空	
		if (StringUtils.isBlank(dicElement.getTablecode())) {
			throw new AppException("数据项：" + elementcode + "对应的表编码为空");
		}
		
		//3、业务处理
			//查询字典编码对应的字典列表
		StringBuffer sql = new StringBuffer();
		sql.append("select code as id, name as text from  ").append(dicElement.getTablecode()).append(" t ");
		sql.append("where upper(elementcode) = '").append(elementcode).append("' ");
		sql.append("and status = 0 ");
		sql.append("order by code");
		
		return casebaseinfoDao.findMapBySql(sql.toString());
	}
	
	/**
	 * @Title: addXzfyReq
	 * @Description: 外网行政复议申请-新增行政复议申请
	 * @author ybb
	 * @date 2016年9月9日 下午2:17:11
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String addXzfyReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取行政复议申请PO，并将页面参数转化到该对象中
		Casebaseinfo casebaseinfo = new Casebaseinfo();
		if (StringUtils.isBlank(param.get("amount").toString())) {
			param.put("amount", BigDecimal.ZERO);
			BeanUtils.populate(casebaseinfo, param);
			casebaseinfo.setAmount(null);
		}else{
			BeanUtils.populate(casebaseinfo, param);
		}
		Thirdbaseinfo thirdbaseinfo =new Thirdbaseinfo();
		String key = UtilTool.getRandomString(5);											//外网用户查询秘钥
		casebaseinfo.setAppdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));			//申请日期
		casebaseinfo.setOperator(StringUtil.stringConvert(param.get("appname")));	        //操作人
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	        //操作日期
		casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);							//流程类型
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		casebaseinfo.setNodeid(new BigDecimal(GCC.PRONODEBASEINFO_NODEID_REQ));				//环节编号
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//数据最后更新时间
		casebaseinfo.setDatasource(GCC.CASEBASEINFO_PROTYPE_NETWORK);                   	//数据来源：1_网上申报
		thirdbaseinfo.setThname(StringUtil.stringConvert(param.get("thname")));             //第三人称名称
		
		casebaseinfo.setUserid(null);														//用户ID
		casebaseinfo.setKey(key);
		
			//新增行政复议申请
		String caseid = (String) casebaseinfoDao.save(casebaseinfo);
		thirdbaseinfo.setCaseid(caseid);                                                   //获取caseid
			//判断是否上传了附件
				//上传，将案件ID更新至附件表的keyid中
				//未长传，不做处理
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String fileSql = "select itemid from SYS_FILEMANAGE where keyid = '" + fjkeyid + "'";
		List<JSONObject> fileList = mapDataDao.queryListBySQL(fileSql);
		if (fileList != null && !fileList.isEmpty()) {
			
			String updFjKeySql = "update sys_filemanage t set t.keyid = '" + caseid + "' where t.keyid = '" + fjkeyid + "'";
			
			casebaseinfoDao.updateBySql(updFjKeySql);
		}
		
		SysUser user = SecureUtil.getCurrentUser();
		//4、记录流程日志
			//转化提交操作日志
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, BigDecimal.ONE, caseid,
				probaseinfo, user, null, null, null);
			
			//新增流程日志
		probaseinfoDao.save(probaseinfo);
		thirdbaseinfoDao.save(thirdbaseinfo);
		return caseid;
	}
	
	/**
	 * @Title: queryXzfyreqByCaseid
	 * @Description: 外网行政复议申请-根据案件ID查询行政复议申请信息
	 * @author ybb
	 * @date 2016年9月9日 下午5:32:37
	 * @param caseid
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public CasebaseinfoVo queryXzfyreqByCaseid(String caseid) throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.caseid, t.csaecode, t.appname, t.apptype, t.idtype, t.idcode, t.mobile, t.phone, ");
		sql.append("t.address, t.email, t.postcode, t.deftype, t.defname, t.depttype, t.defidtype, t.defidcode, t.defmobile, ");
		sql.append("t.defphone, t.defmailaddress, t.defaddress, t.defemail, t.defpostcode, t.noticeddate, t.actnoticeddate, ");
		sql.append("t.admtype, t.casetype, t.ifcompensation, to_char(t.amount) amountstr, t.appcase, t.factreason, t.annex, t.appdate, t.operator, ");
		sql.append("t.optdate, t.protype, t.opttype, t.nodeid, t.lasttime, t.userid, t.oldprotype, t.receivedate, t.receiveway, ");
		sql.append("t.expresscom, t.couriernum, t.datasource, t.mailaddress, t.delaydays, t.region, t.intro, t.key,b.thname, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.apptype) apptypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.idtype) idtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_DEFTYPE' and a.status=0 and a.code = t.deftype) deftypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ADMINLEVEL' and a.status=0 and a.code = t.admtype) admtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_CASETYPE' and a.status=0 and a.code = t.casetype) casetypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SYS_AREA' and a.status=0 and a.code = t.region) regionname, ");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifcompensation) ifcompensationname ");
		sql.append("from B_CASEBASEINFO t left join b_thirdbaseinfo b on b.CASEID = t.CASEID ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		
		List<CasebaseinfoVo> casebaseinfos = (List<CasebaseinfoVo>) casebaseinfoDao.findVoBySql(sql.toString(), CasebaseinfoVo.class);
		if (casebaseinfos == null || casebaseinfos.isEmpty()) {
			return null;
		}
		
		return casebaseinfos.get(0);
	} 
	
	/**
	 * @Title: updateXzfyReqByCaseid
	 * @Description: 案件查询（外网）-补正
	 * @author ybb
	 * @date 2016年9月12日 下午3:38:59
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String updateXzfyReqByCaseid(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if(StringUtils.isBlank(caseid)){
			throw new AppException("提交失败：案件ID为空");
		}
		
		//2、业务逻辑判断
			//判断行政复议申请是否正常
			//判断该案件不是【行政复议审批】
			//判断该案件处理标志不是【已提交】或【已退回】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if (casebaseinfo == null) {
			throw new AppException("提交失败：未找到对应的行政复议申请");
		}
		if (!casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT)) {
			throw new AppException("提交失败：该案件不是【行政复议审批】");
		}
		if (!casebaseinfo.getOpttype().equals(GCC.PROBASEINFO_OPTTYPE_RETURNED)) {	
			throw new AppException("提交失败：该案件处理标志不是【已退回】");
		}
		
		//3、业务逻辑处理
			//转化页面表单属性
		if (StringUtils.isBlank(param.get("amount").toString())) {
			param.put("amount", BigDecimal.ZERO);
			BeanUtils.populate(casebaseinfo, param);
			casebaseinfo.setAmount(null);
		}else{
			BeanUtils.populate(casebaseinfo, param);
		}
		
			//转化操作人信息
		String key = UtilTool.getRandomString(5);
		casebaseinfo.setOperator(StringUtil.stringConvert(param.get("appname")));				//操作人
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));				//操作日期
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  	//数据最后更新时间
		casebaseinfo.setKey(key);
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
		casebaseinfo.setNodeid(
				casebaseinfo.getNodeid() == null ? BigDecimal.ONE : casebaseinfo.getNodeid().add(BigDecimal.ONE));
		
			//修改行政复议申请信息
		casebaseinfoDao.update(casebaseinfo);
		
		//4、记录流程日志
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		//流程日志未记录，新增流程日志
		Probaseinfo probaseinfo = null;
		
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
			
			probaseinfo = new Probaseinfo();
			probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
			probaseinfo.setSeqno(new BigDecimal(probaseinfoList.get(0).getString("seqno")));
		}
		SysUser user = SecureUtil.getCurrentUser();
		probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, BigDecimal.ONE, caseid,
				probaseinfo, user, null, null, null);
		
		probaseinfoDao.save(probaseinfo);
		
		return key;
	}
	
	/**
	 * @Title: addXzfyCancelReq
	 * @Description: 案件查询（外网）-撤销
	 * @author ybb
	 * @date 2016年9月12日 下午4:52:12
	 * @param param
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addXzfyCancelReq(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断案件ID是否为空
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if (StringUtils.isBlank(caseid)) {
			throw new AppException("保存失败：案件ID为空");
		}
		
		//处理意见
		String remark = StringUtil.stringConvert(param.get("remark"));
		if (StringUtils.isBlank(remark)) {
			throw new AppException("保存失败：请输入撤销原因");
		}
	
		//2、业务逻辑判断
			//判断行政复议申请是否正常
			//判断该案件不是【行政复议审批】、【行政复议中止】、【行政复议恢复】
		Casebaseinfo casebaseinfo = casebaseinfoDao.get(caseid);
		if(casebaseinfo == null){
			throw new AppException("保存失败：未找到对应的行政复议申请");
		}
		if (!(casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT) || 
				casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYSUSPEND) ||
				casebaseinfo.getProtype().equals(GCC.PROBASEINFO_PROTYPE_XZFYRECOVER))) {
			throw new AppException("保存失败：该案件不是【复议审批】、【复议中止】、【复议恢复】");
		}
		
		//3、业务逻辑处理
			//更新行政复议申请流程类型为：04_终止，并记录原流程类型
		casebaseinfo.setOldprotype(casebaseinfo.getProtype());
		casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYCANCEL);
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间

		casebaseinfoDao.update(casebaseinfo);
		
		//4、记录流程日志
			//获取系统当前登录用户信息
		SysUser user = SecureUtil.getCurrentUser();
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		sql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.opttype = '" + GCC.PROBASEINFO_OPTTYPE_SUBMITTED + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYCANCEL + "'");
		sql.append(" and t.nodeid = " + BigDecimal.ONE);
		sql.append(" order by t.endtime desc");
		List<JSONObject> probaseinfoList = mapDataDao.queryListBySQL(sql.toString());
		
		Probaseinfo probaseinfo = null;
		if (probaseinfoList != null && !probaseinfoList.isEmpty()) {
			
			probaseinfo = new Probaseinfo();
			
			probaseinfo.setId(probaseinfoList.get(0).getString("id"));
			probaseinfo.setProcessid(probaseinfoList.get(0).getString("processid"));
			
			//转化提交操作日志
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYCANCEL, BigDecimal.ONE, caseid,
					probaseinfo, user,  null, null, null);
			
			//修改过程日志
			probaseinfoDao.update(probaseinfo);
			
		} else {
			
			//转化提交操作日志
			probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYCANCEL, BigDecimal.ONE, caseid,
					probaseinfo, user, null, null, null);
				
				//新增流程日志
			probaseinfoDao.save(probaseinfo);
		}
	}
	
	/**
	 * @Title: addXzfyReqForApp
	 * @Description: 案件申请（APP）-新增案件申请
	 * @author ybb
	 * @date 2016年9月20日 下午5:45:00
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Casebaseinfo addXzfyReqForApp(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取行政复议申请PO，并将页面参数转化到该对象中
		Casebaseinfo casebaseinfo = new Casebaseinfo();
		
		if (StringUtils.isBlank(StringUtil.stringConvert(param.get("amount")))) {
			param.put("amount", BigDecimal.ZERO);
			BeanUtils.populate(casebaseinfo, param);
			casebaseinfo.setAmount(null);
		}else{
			BeanUtils.populate(casebaseinfo, param);
		}
		
		String key = UtilTool.getRandomString(5);											//外网用户查询秘钥
		casebaseinfo.setAppdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));			//申请日期
		casebaseinfo.setOperator(StringUtil.stringConvert(param.get("appname")));	        //操作人
		casebaseinfo.setOptdate(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));	        //操作日期
		casebaseinfo.setProtype(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT);							//流程类型
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);							//处理标志
		casebaseinfo.setNodeid(BigDecimal.ONE);												//环节编号
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));//数据最后更新时间
		casebaseinfo.setDatasource(GCC.CASEBASEINFO_PROTYPE_APPREQ);                   		//数据来源：3_手机端申请
		casebaseinfo.setUserid(null);														//用户ID
		casebaseinfo.setKey(key);
		
			//新增行政复议申请
		String caseid = (String) casebaseinfoDao.save(casebaseinfo);
		SysUser user = SecureUtil.getCurrentUser();
		//4、记录流程日志
			//转化提交操作日志
		Probaseinfo probaseinfo = null;
		probaseinfo = FlowUtil.genSubmittedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, BigDecimal.ONE, caseid,
				probaseinfo, user, null, null, null);
			
			//新增流程日志
		probaseinfoDao.save(probaseinfo);

		return casebaseinfo;
	}
	
	/**
	 * @Title: addCasebaseinfoAcceptForApp
	 * @Description: 新增行政复议受理（APP）
	 * @author ybb
	 * @date 2016年9月24日 上午10:07:15
	 * @param caseid  案件ID
	 * @param nodeid  环节ID
	 * @param result  处理结果字典
	 * @param resultmsg 处理结果中文
	 * @param remark 处理意见
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Casebaseinfo addCasebaseinfoAcceptForApp(String caseid, String nodeid, String result, String resultmsg,
			String remark) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
	
			//处理结果
		if (StringUtils.isBlank(result)) {
			throw new AppException("未保存复议处理，请保存后再发送");
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

			//判断日志信息是否正常
				//判断该案件是否被接收
				//判断该案件是否已处理
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" order by t.endtime desc");
			
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议处理，请保存后再发送");
		}
				
		//3、业务逻辑处理
		
		//4、记录流程操作日志
			//获取系统当前登录用户信息
		if (result.equals(GCC.PROBASEINFO_OPTTYPE_ACCEPT)) {  //受理结果：01_受理
			
			Probaseinfo probaseinfo = new Probaseinfo();
			probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
			probaseinfo.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			probaseinfo.setResultmsg(resultmsg);
			
			probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					casebaseinfo.getNodeid(), caseid, probaseinfo, null, result,
					remark);
				
			//新增结果日志
			probaseinfoDao.save(probaseinfo);
			
			//修改案件信息为已提交
			casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
			casebaseinfo.setNodeid(
					casebaseinfo.getNodeid() == null ? BigDecimal.ONE : casebaseinfo.getNodeid().add(BigDecimal.ONE));
			casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
			casebaseinfo.setState(GCC.RCASEBASEINFO_STATE_NOCLOSURE);                             //状态：收案未立案
			
			//更新行政复议申请，处理标志、环节编号
			casebaseinfoDao.update(casebaseinfo);
			
		} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_NOTACCEPT)) {	//受理结果：02_不予受理
			
			Resultbaseinfo rbi = new Resultbaseinfo();
			rbi.setProcessid(probaseins.get(0).getString("processid"));
			rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			rbi.setResult(result);
			rbi.setRemark(remark);
			rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_REFUSE);
			rbi.setResultmsg(resultmsg);
			
			rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					casebaseinfo.getNodeid(), caseid, rbi, null);
			
			//新增结果日志
			resultbaseinfoDao.save(rbi);
			
		} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_FORWARD)) {	//受理结果：03_转送
			
			Resultbaseinfo rbi = new Resultbaseinfo();
			rbi.setProcessid(probaseins.get(0).getString("processid"));
			rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			rbi.setResult(result);
			rbi.setRemark(remark);
			rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_REFUSE);
			rbi.setResultmsg(resultmsg);
			
			rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
					casebaseinfo.getNodeid(), caseid, rbi, null);
			
			//新增结果日志
			resultbaseinfoDao.save(rbi);
			
		} else if (result.equals(GCC.PROBASEINFO_OPTTYPE_EOFFSET)) {	//受理结果：04_补正
			
			Probaseinfo probaseinfo = new Probaseinfo();
			probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
			probaseinfo.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
			probaseinfo.setResultmsg(resultmsg);
			
			probaseinfo = FlowUtil.genReturnedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, caseid, probaseinfo,
					null, result, remark);
			
			//新增操作日志
			probaseinfoDao.save(probaseinfo);
			
			//修改案件信息为已提交
			casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
			casebaseinfo.setNodeid(
					casebaseinfo.getNodeid() == null ? BigDecimal.ONE : casebaseinfo.getNodeid().subtract(BigDecimal.ONE));
			casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
			
			//更新行政复议申请，处理标志、环节编号
			casebaseinfoDao.update(casebaseinfo);
			
		} else {
			throw new AppException("受理结果错误");
		}
		
		return casebaseinfo;
	}
	
	/**
	 * @Title: addCasebaseinfoReviewForApp
	 * @Description: 新增行政复议审理（APP）
	 * @author ybb
	 * @date 2016年9月24日 上午10:21:24
	 * @param caseid  案件ID
	 * @param nodeid  环节ID
	 * @param result  处理结果字典
	 * @param resultmsg 处理结果中文
	 * @param remark 处理意见
	 * @throws AppException
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Casebaseinfo addCasebaseinfoReviewForApp(String caseid, String nodeid, String result, String resultmsg,
			String remark) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
		
			//处理结果
		if (StringUtils.isBlank(remark)) {
			throw new AppException("未保存复议审理，请保存后再发送");
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

		//3、记录结果日志
			//获取过程日志
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议审理，请保存后再发送");
		}
		
			//处理结果记录到流程过程表中
		Probaseinfo probaseinfo = new Probaseinfo();
		probaseinfo.setProcessid(probaseins.get(0).getString("processid"));
		probaseinfo.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
		probaseinfo.setResult("");
		probaseinfo.setResult(remark);
		probaseinfo.setResultmsg(resultmsg);
		
			//获取当前登录用户信息
		probaseinfo = FlowUtil.genProcessedOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT,
				casebaseinfo.getNodeid(), caseid, probaseinfo, null, "",
				remark);
			
		
		probaseinfoDao.save(probaseinfo);
		
			//修改案件信息为已提交
		casebaseinfo.setOpttype(GCC.PROBASEINFO_OPTTYPE_SUBMITTED);
		casebaseinfo.setNodeid(
				casebaseinfo.getNodeid() == null ? BigDecimal.ONE : casebaseinfo.getNodeid().add(BigDecimal.ONE));
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		
			//更新行政复议申请，处理标志、环节编号
		casebaseinfoDao.update(casebaseinfo);
		
		return casebaseinfo;
	}
	
	/**
	 * @Title: addCasebaseinfoDecisionForApp
	 * @Description: 新增行政复议决定（APP）
	 * @author ybb
	 * @date 2016年9月24日 上午10:27:50
	 * @param caseid  案件ID
	 * @param nodeid  环节ID
	 * @param result  处理结果字典
	 * @param resultmsg 处理结果中文
	 * @param remark 处理意见
	 * @throws AppException
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Casebaseinfo addCasebaseinfoDecisionForApp(String caseid, String nodeid, String result, String resultmsg,
			String remark) throws AppException {
		
		//1、入口参数判断
			//判断案件ID是否为空
		if(StringUtils.isBlank(caseid)){
			throw new AppException("案件ID为空");
		}
		
			//处理结果
		if (StringUtils.isBlank(result)) {
			throw new AppException("未保存复议决定，请保存后再发送");
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
		
		//3、逻辑处理
			//修改案件状态
		casebaseinfo.setState(GCC.RCASEBASEINFO_PSTATE_NOARCHIVE);
		casebaseinfo.setLasttime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));  //数据最后更新时间
		
		casebaseinfoDao.update(casebaseinfo);

		//4、记录结果日志
			//获取过程日志
		StringBuffer logSql = new StringBuffer();
		
		logSql.append("select t.id, t.processid, t.protype, t.nodeid, t.seqno, t.opttype, t.caseid, ");
		logSql.append(" t.operator, t.starttime, t.endtime, t.result, t.remark, t.reason, t.userid, t.sendunit, t.resultmsg ");
		logSql.append(" from PUB_PROBASEINFO t ");
		logSql.append(" where t.caseid = '" + caseid + "'");
		logSql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		logSql.append(" order by t.endtime desc");
		
		List<JSONObject> probaseins = mapDataDao.queryListBySQL(logSql.toString());
		if (probaseins == null || probaseins.isEmpty()) {
			throw new AppException("未保存复议决定，请保存后再发送");
		}
		
			//将处理结果记录到流程结果表中
		Resultbaseinfo rbi = new Resultbaseinfo();
		rbi.setProcessid(probaseins.get(0).getString("processid"));
		rbi.setSeqno(new BigDecimal(probaseins.get(0).getString("seqno")));
		rbi.setResult(result);
		rbi.setRemark(remark);
		rbi.setOpttype(GCC.PROBASEINFO_OPTTYPE_END);
		rbi.setResultmsg(resultmsg);
		
		rbi = FlowUtil.genResultOperationData(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT, casebaseinfo.getNodeid(), caseid, rbi,
				null);
		
		//新增结果日志
		resultbaseinfoDao.save(rbi);
		
		return casebaseinfo;
	}
	
	/**
	 * @Title: queryProbaseinfoListForApp
	 * @Description: 返回处理意见列表
	 * @author ybb
	 * @date 2016年9月24日 上午10:37:58
	 * @param caseid 案件ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Probaseinfo>  queryProbaseinfoListForApp(String caseid) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select t.operator, t.starttime, t.endtime, t.remark, t.resultmsg ");
		sql.append(" from PUB_PROBASEINFO t ");
		sql.append(" where t.caseid = '" + caseid + "'");
		sql.append(" and t.protype = '" + GCC.PROBASEINFO_PROTYPE_XZFYAUDIT + "'");
		sql.append(" and t.opttype in ('" + GCC.PROBASEINFO_OPTTYPE_PROCESSED + "','" + GCC.PROBASEINFO_OPTTYPE_END + "')");
		sql.append(" order by t.endtime "); 
		
		List<Probaseinfo> probaseinfoList = (List<Probaseinfo>) probaseinfoDao.findVoBySql(sql.toString(), Probaseinfo.class);
		if(probaseinfoList == null || probaseinfoList.isEmpty() ){
			return new ArrayList<Probaseinfo>();
		} 
		
		return probaseinfoList;
	}
}

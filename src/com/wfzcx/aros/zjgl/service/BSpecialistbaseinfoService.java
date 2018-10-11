package com.wfzcx.aros.zjgl.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.dao.SysUserDao;
import com.jbf.sys.user.po.SysUser;
import com.jbf.sys.user.service.SysUserService;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.zjgl.dao.BSpecialistbaseinfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpegrouprelainfoDAO;
import com.wfzcx.aros.zjgl.po.BSpecialistbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpegrouprelainfo;

/**
 * 
 * @author LinXF
 * @date 2016年8月11日 下午3:09:07
 */
@Scope("prototype")
@Service("com.wfzcx.aros.zjgl.service.BSpecialistbaseinfoService")
public class BSpecialistbaseinfoService {
	
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BSpecialistbaseinfoDAO dao;
	@Autowired
	BSpegrouprelainfoDAO reldao;
	@Autowired
	SysUserDao userDao;
	@Autowired
	private SysUserService userService;

	/**
	 * 
	 * @param map
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select t.speid,t.title,t.spename,t.post,t.degree,t.address,t.workunits,t.spedesc,t.phone,t.postcode,t.intro,t.membertype,");
		sql.append("  t1.userid, t1.usercode, t1.orgcode, t1.createuser, t1.createtime, t1.modifyuser, t1.modifytime, ");
		sql.append(" (select pt.name from sys_dept pt where pt.code=t1.orgcode) orgname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPETITLE' and a.status=0  and a.code=t.title) titlename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEPOST' and a.status=0  and a.code=t.post) postname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEDEGREE' and a.status=0  and a.code=t.degree) degreename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEMEMBERTYPE' and a.status=0  and a.code=t.membertype) membertypename ");
		sql.append(" from b_specialistbaseinfo t");
		//关联用户表
		sql.append(" left join Sys_User t1 on t1.userid = t.userid ");
		sql.append(" where 1=1 ");

		String title = StringUtil.stringConvert(param.get("title"));
		if (title != null && !title.trim().equals("")) {
			sql.append(" and t.title='" + title + "'");
		}
		String spename = StringUtil.stringConvert(param.get("spename"));
		if (spename != null && !spename.trim().equals("")) {
			sql.append(" and t.spename like '%" + spename + "%'");
		}
		// 工作单位
		String workunits = (String)param.get("workunits");
		if (workunits != null && !workunits.trim().equals("")) {
			sql.append(" and t.workunits like '%" + workunits + "%'");
		}
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Map<String, Object> param) throws Exception {
		//保存用户信息
		long uid = this.saveUser(param);
		//保存专家信息
		BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
		BeanUtils.populate(bSpecialistbaseinfo, param);
		bSpecialistbaseinfo.setUserid(uid);
		String id = "";
		if (bSpecialistbaseinfo != null && bSpecialistbaseinfo.getSpeid() != null && !bSpecialistbaseinfo.getSpeid().trim().equals("")) {
			dao.update(bSpecialistbaseinfo);
		} else {
			id = (String) dao.save(bSpecialistbaseinfo);
		}

		return id;
	}

	/**
	 * 
	 * @param speid
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String delete(String speid) throws Exception {
		try {
			
			//add by wangzk 删除专家时删除相应的小组专家关系表
			String sql = " select * from b_spegrouprelainfo t where t.speid = '"+speid+"' ";
			List relList = reldao.findVoBySql(sql, BSpegrouprelainfo.class);
			if(relList != null && relList.size() > 0) {
				for(Object obj: relList) {
					BSpegrouprelainfo rel = (BSpegrouprelainfo)obj;
					reldao.delete(rel);
				}
			}
			//删除专家时删除专家对应的用户
			String sql2 = " select * from sys_user t where t.userid = (select t1.userid from B_SPECIALISTBASEINFO t1 where t1.speid = '"+speid+"') ";
			List userList = reldao.findVoBySql(sql2, SysUser.class);
			if(userList != null && userList.size() > 0) {
				for(Object obj: userList) {
					SysUser user = (SysUser)obj;
					userDao.delete(user);
				}
			}
			//删除专家
			BSpecialistbaseinfo bSpecialistbaseinfo = new BSpecialistbaseinfo();
			bSpecialistbaseinfo.setSpeid(speid);
			dao.delete(bSpecialistbaseinfo);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	/**
	 * 保存专家的用户信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public long saveUser(Map<String, Object> param) throws Exception {
		//保存用户信息
		SysUser user = new SysUser();
		long uid = 0L;
		String userid = StringUtil.stringConvert(param.get("userid"));
		if(!userid.equals("")) {
			uid = Long.valueOf(userid);
		}
		//验证用户编号是否为空
		String usercode = StringUtil.stringConvert(param.get("usercode"));
		if(usercode.equals("")) {
			throw new Exception("参数usercode获取为空！");
		}
		//验证用户编号是否已存在
		String sql = "select * from sys_user t where t.usercode = '"+usercode+"' ";
		if(uid > 0) {
			//该专家已有用户信息
			sql = sql + " and t.userid <> "+uid;
		}
		List userlist = userDao.findVoBySql(sql, SysUser.class);
		if(userlist != null && userlist.size() > 0) {
			throw new Exception("用户编号"+usercode+"已存在，请重新录入！");
		}
		//保存用户信息
		user.setUsercode(usercode);
		user.setUsername(StringUtil.stringConvert(param.get("spename")));
		user.setUserpswd(StringUtil.stringConvert(param.get("userpswd")));
		user.setOrgcode(StringUtil.stringConvert(param.get("orgcode")));
		user.setCreateuser(StringUtil.stringConvert(param.get("createuser")));
		user.setCreatetime(StringUtil.stringConvert(param.get("createtime")));
		user.setModifyuser(StringUtil.stringConvert(param.get("modifyuser")));
		user.setModifytime(StringUtil.stringConvert(param.get("modifytime")));
		user.setUsertype(new Byte("0"));
		if(uid<=0) {
			//新增				
			uid = userService.add(user);
		}else {
			//修改
			user.setUserid(uid);
			userService.edit(user);
		}
		
		return uid;
	}

	/**
	 * 通过用户ID查询专家信息
	 * @param userid
	 * @return
	 */
	public BSpecialistbaseinfo getSpeInfoByUserId(Long userid) {
		BSpecialistbaseinfo bean = new 	BSpecialistbaseinfo();
		bean.setUserid(userid);
		List<BSpecialistbaseinfo> speList = dao.findByExample(bean);
		if(!CollectionUtils.isEmpty(speList)){
			bean =  speList.get(0);
		}
		return bean;
	}
	
	/**
	 * 查询专家信息
	 * @param speid
	 * @return
	 */
	public BSpecialistbaseinfo querySpeInfo(String speid)
	{
		return dao.get(speid);
	}
	
	/**
	 * @Title: queryXzfyProcessList
	 * @Description: 复议研讨发起：分页查询
	 * @author ybb
	 * @date 2016年11月15日10:57:14
	 * @param param
	 * @return
	 * @throws AppException
	 */
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
		
		// 复议审批
		sql.append(" and t.protype = '").append(GCC.PROBASEINFO_PROTYPE_XZFYAUDIT).append("'");
		// 状态
		sql.append(" and t.state not in ('").append(GCC.PROBASEINFO_OPTTYPE_END).append("', '")
		   .append(GCC.PROBASEINFO_OPTTYPE_REFUSE).append("')");
		
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
}

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

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.xzfy.dao.BCasesperelabaseinfoDao;
import com.wfzcx.aros.xzfy.po.BCasesperelabaseinfo;
import com.wfzcx.aros.zjgl.dao.BGroupbaseinfoDAO;
import com.wfzcx.aros.zjgl.dao.BSpegrouprelainfoDAO;
import com.wfzcx.aros.zjgl.po.BGroupbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpegrouprelainfo;

@Scope("prototype")
@Service("com.wfzcx.aros.zjgl.service.BGroupbaseinfoService")
public class BGroupbaseinfoService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BGroupbaseinfoDAO dao;
	@Autowired
	BSpegrouprelainfoDAO relaDao;
	@Autowired
	BCasesperelabaseinfoDao caserelaDao;


	/**
	 * 
	 * @param map
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select t.groupid,t.groupname,t.casedesc,t.question,t.opttime,t.ifcheck,t.operator,");
		sql.append(" (select a.username from sys_user a where  a.userid=t.operator) cjr_mc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=t.ifcheck) ifcheckname");
		sql.append(" from b_groupbaseinfo t");

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
		BGroupbaseinfo bGroupbaseinfo = new BGroupbaseinfo();
		BeanUtils.populate(bGroupbaseinfo, param);
		SysUser user = SecureUtil.getCurrentUser();
		String id = "";

		bGroupbaseinfo.setOperator(user.getUserid().toString());// 创建人
		bGroupbaseinfo.setOpttime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss").toString());// 创建时间

		if (bGroupbaseinfo != null && bGroupbaseinfo.getGroupid() != null && !bGroupbaseinfo.getGroupid().trim().equals("")) {
			dao.update(bGroupbaseinfo);
		} else {
			id = (String) dao.save(bGroupbaseinfo);
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
	public String delete(String groupid) throws Exception {
		try {
			BGroupbaseinfo bGroupbaseinfo = new BGroupbaseinfo();
			bGroupbaseinfo.setGroupid(groupid);
			dao.delete(bGroupbaseinfo);
			//add by wangzk 删除小组时 删除案件小组关系表相应数据
			String sql = " select * from b_casesperelabaseinfo t where t.groupid = '"+groupid+"' ";
			List list = caserelaDao.findVoBySql(sql, BCasesperelabaseinfo.class);
			if(list != null && list.size() > 0) {
				for(Object obj: list) {
					BCasesperelabaseinfo rela = (BCasesperelabaseinfo)obj;
					caserelaDao.delete(rela);
				}
			}
			//add by wangzk 删除小组时 删除小组专家关系表相应数据
			String sql2 = " select * from b_spegrouprelainfo t where t.groupid = '"+groupid+"' ";
			List list2 = relaDao.findVoBySql(sql2, BSpegrouprelainfo.class);
			if(list2 != null && list2.size() > 0) {
				for(Object obj: list2) {
					BSpegrouprelainfo rela = (BSpegrouprelainfo)obj;
					relaDao.delete(rela);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "";
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public List querySpeList(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		sql.append("select t.speid,t.title,t.spename,t.post,t.degree,t.address,t.workunits,t.spedesc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPETITLE' and a.status=0  and a.code=t.title) titlename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEPOST' and a.status=0  and a.code=t.post) postname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEDEGREE' and a.status=0  and a.code=t.degree) degreename");
		sql.append(" from b_specialistbaseinfo t");
		
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		String groupid = StringUtil.stringConvert(param.get("groupid"));
		if("group".equals(operflag)) {
			//已选专家查询
			sql.append(" inner join B_SPEGROUPRELAINFO t1 on t1.speid = t.speid and t1.groupid = '"+groupid+"' ");
		}else if("all".equals(operflag)){
			//可选专家查询
			if(StringUtils.isNotEmpty(groupid)){
				sql.append(" where not exists (select 1 from B_SPEGROUPRELAINFO t1 where t1.speid = t.speid");
				sql.append(" and t1.groupid = '"+groupid+"')");
			}		
		}
		return mapDataDao.queryListBySQLForConvert(sql.toString());
	}
	
	
	/**
	 *  保存小组专家信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String saveZjxz(Map<String, Object> param) throws Exception {
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		String speid = StringUtil.stringConvert(param.get("speid"));
		String groupid = StringUtil.stringConvert(param.get("groupid"));
		
		//参数校验
		if("".equals(operflag)) {
			throw new Exception("操作失败：参数未获取到：operflag");
		}
		if("".equals(speid)) {
			throw new Exception("操作失败：参数未获取到：speid");
		}
		if("".equals(groupid)) {
			throw new Exception("操作失败：参数未获取到：groupid");
		}
		
		String existSql = " select * from B_SPEGROUPRELAINFO t where t.speid = '"+speid
				+"' and t.groupid = '"+groupid+"' ";
		List list = relaDao.findVoBySql(existSql,BSpegrouprelainfo.class);
		if("add".equals(operflag)) {
			//新增
				//验证是否已存在
					//已存在 不需要插入
					//已存在  插入
			if(list != null && list.size() > 0) {
				//已存在
				return "";
			}
			BSpegrouprelainfo rela = new BSpegrouprelainfo();
			rela.setSpeid(speid);
			rela.setGroupid(groupid);
			relaDao.save(rela);
			
		}else {
			//删除
				//验证是否已存在
					//已存在 删除
					//已存在  不需要删除
			if(list != null && list.size() > 0) {
				//已存在
				BSpegrouprelainfo oldinfo = (BSpegrouprelainfo)list.get(0);
				relaDao.delete(oldinfo);
			}
		}

		return "";
	}
	
	/**
	 * @Title: querySpecialists
	 * @Description: 复议研讨发起-查询所有专家列表
	 * @author ybb
	 * @date 2016年11月15日 下午4:24:11
	 * @param param
	 * @return
	 */
	public List<?> querySpecialists(Map<String, Object> param) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.speid,t.title,t.spename,t.post,t.degree,t.address,t.workunits,t.spedesc,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPETITLE' and a.status=0  and a.code=t.title) titlename,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEPOST' and a.status=0  and a.code=t.post) postname,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SPEDEGREE' and a.status=0  and a.code=t.degree) degreename");
		sql.append(" from B_SPECIALISTBASEINFO t");
		
		String operflag = StringUtil.stringConvert(param.get("operflag"));
		String caseid = StringUtil.stringConvert(param.get("caseid"));
		if("group".equals(operflag)) {
			
			//已选专家查询
			sql.append(" ,B_SPEGROUPRELAINFO t1, B_CASESPERELABASEINFO t2");
			sql.append(" where t.speid = t1.speid and t1.groupid = t2.groupid");
			sql.append(" and t2.caseid = '").append(caseid).append("'");
		
		}else if("all".equals(operflag)){
			
			//可选专家查询
			sql.append(" where not exists (select distinct t.speid from B_SPECIALISTBASEINFO t1, B_SPEGROUPRELAINFO t2, B_CASESPERELABASEINFO t3");
			sql.append(" where t1.speid = t2.speid and t2.groupid = t3.groupid and t3.caseid = '").append(caseid).append("'");
			sql.append(" and t1.speid = t.speid)");
		}
		
		return mapDataDao.queryListBySQLForConvert(sql.toString());
	}
}

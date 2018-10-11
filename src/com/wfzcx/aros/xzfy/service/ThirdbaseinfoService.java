package com.wfzcx.aros.xzfy.service;

import java.lang.reflect.InvocationTargetException;
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
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.xzfy.dao.ThirdbaseinfoDao;
import com.wfzcx.aros.xzfy.po.Thirdbaseinfo;
import com.wfzcx.aros.xzfy.vo.ThirdbaseinfoVo;

/**
 * @ClassName: ThirdbaseinfoService
 * @Description: 处理第三人基本信息相关业务
 * @author ybb
 * @date 2016年8月16日 下午3:14:25
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.xzfy.service.ThirdbaseinfoService")
public class ThirdbaseinfoService {

	@Autowired
	private ThirdbaseinfoDao thirdbaseinfoDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	
	/**
	 * @Title: queryThirdByCaseid
	 * @Description: 根据案件ID查询第三人信息
	 * @author ybb
	 * @date 2016年8月16日 下午3:16:19
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public ThirdbaseinfoVo queryThirdByCaseid(String caseid){
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.thid, t.thname, t.thtype, t.thidtype, t.thidcode, t.thphone, t.thaddress, ");
		sql.append("t.thpostcode, t.caseid, t.thidproxyman, t.thidproxyphone,t.thidproxyaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.thtype) thtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.thidtype) thidtypename ");
		sql.append("from B_THIRDBASEINFO t ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		
		List<ThirdbaseinfoVo> thirdbaseinfos = (List<ThirdbaseinfoVo>) thirdbaseinfoDao.findVoBySql(sql.toString(), ThirdbaseinfoVo.class);
		if (thirdbaseinfos == null || thirdbaseinfos.isEmpty()) {
			return null;
		}
		
		return thirdbaseinfos.get(0);
	}
	
	/**
	 * @Description: 根据案件ID查询第三人所有名称
	 * @param caseid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public String queryThirdNamesByCaseid(String caseid){
		
		String result = "";
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.thname, t.thtype, t.thidtype, t.thidcode, t.thphone, t.thaddress, ");
		sql.append("t.thpostcode, t.caseid, t.thidproxyman, t.thidproxyphone,t.thidproxyaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.thtype) thtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.thidtype) thidtypename ");
		sql.append("from B_THIRDBASEINFO t ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		
		List<ThirdbaseinfoVo> thirdbaseinfos = (List<ThirdbaseinfoVo>) thirdbaseinfoDao.findVoBySql(sql.toString(), ThirdbaseinfoVo.class);
		for (ThirdbaseinfoVo thirdbaseinfoVo : thirdbaseinfos) {
			String name = thirdbaseinfoVo.getThname();
			if(!StringUtils.isEmpty(name)){
				result  = result + "["+ name + "]";
			}
		}
		return result;
	}

	@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
	public PaginationSupport queryThirdByCaseid(Map<String, Object> param) {

		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		String caseid = (String) param.get("caseid");
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.thid, t.thname, t.thtype, t.thidtype, t.thidcode, t.thphone, t.thaddress, ");
		sql.append("t.thpostcode, t.caseid, t.thidproxyman, t.thidproxyphone,t.thidproxyaddress, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_APPTYPE' and a.status=0 and a.code = t.thtype) thtypename, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='B_CASEBASEINFO_IDTYPE' and a.status=0 and a.code = t.thidtype) thidtypename ");
		sql.append("from B_THIRDBASEINFO t ");
		sql.append("where t.caseid = '" + caseid + "' "); //案件ID
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}

	/**
	 * 保存第三人信息
	 * @param param
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void save(Map<String, Object> param) throws Exception {
		String thid = (String) param.get("thid");
		Thirdbaseinfo info = new Thirdbaseinfo();
		BeanUtils.populate(info, param);
		if (StringUtils.isEmpty(thid)){
			thirdbaseinfoDao.save(info);
		}else{
			thirdbaseinfoDao.update(info);
		}
	}

	/**
	 * 删除
	 * @param param
	 */
	public void delete(Map<String, Object> param) {
		String id = (String) param.get("id");
		thirdbaseinfoDao.delete(id);
	}

	/**
	 * 查询用户列表
	 * @param param
	 * @return
	 */
	public PaginationSupport queryUserList(Map<String, Object> param) {
		String rolecode = (String) param.get("rolecode");
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		sql.append("select t.userid, t.username, t.usercode ");
		sql.append("from sys_user t ,sys_user_role r, sys_role sl where r.userid=t.userid and r.roleid= sl.roleid  and  sl.rolecode='").append(rolecode).append("'");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	
	
	
}

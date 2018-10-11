package com.wfzcx.aros.wtcl.service;

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

import com.jbf.base.dic.po.SysYwDiccodeitem;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.FormatUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.wtcl.dao.BWebquesbaseinfoDAO;
import com.wfzcx.aros.wtcl.po.BWebquesbaseinfo;
import com.wfzcx.aros.zjgl.po.BSpecialistbaseinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.wtcl.service.BWebquesbaseinfoService")
public class BWebquesbaseinfoService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	BWebquesbaseinfoDAO dao;
	
	/**
	 * 
	 * @return
	 */
	public List<BWebquesbaseinfo> getListW() {		
		String sql = "select * from b_webquesbaseinfo";
		List<BWebquesbaseinfo> list = (List<BWebquesbaseinfo>)dao.findVoBySql(sql, BWebquesbaseinfo.class);
		return list;
	}
	
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport queryListW(Map<String, Object> param,String quesType) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select quesid,quetype,caseid,askername,asktime,phone,quesdesc,answer,operator,opttime,ifanswer ");		
		sql.append(" from b_webquesbaseinfo where 1=1 and ifanswer='1' ");

		String quesdesc = StringUtil.stringConvert(param.get("quesdesc"));
		if (quesdesc != null && !quesdesc.trim().equals("")) {
			sql.append(" and quesdesc like '%" + quesdesc + "%'");
		}
		
		if (!StringUtils.isBlank(quesType)) {
			sql.append(" and queType = '").append(quesType).append("'");
		} 
		
		sql.append("  order by opttime desc,asktime desc");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}


	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String saveW(Map<String, Object> param) throws Exception {
		// 保存专家信息
		BWebquesbaseinfo bWebquesbaseinfo = new BWebquesbaseinfo();
		BeanUtils.populate(bWebquesbaseinfo, param);
		

		//获取系统当前日期
		String asktime = FormatUtil.stringDate2();
		bWebquesbaseinfo.setAsktime(asktime);
		//是否回答默认0-否
		bWebquesbaseinfo.setIfanswer("0");
		//默认问题类型=网上答疑
		bWebquesbaseinfo.setQuetype("01");
		
		String id = "";
		if (bWebquesbaseinfo != null && bWebquesbaseinfo.getQuesid() != null && !bWebquesbaseinfo.getQuesid().trim().equals("")) {
			dao.update(bWebquesbaseinfo);
		} else {
			id = (String) dao.save(bWebquesbaseinfo);
		}
		return id;
	}


	/**
	 * 
	 * @param param
	 * @return
	 */
	public PaginationSupport queryList(Map<String, Object> param,String quesType) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select quesid,quetype,caseid,askername,asktime,phone,quesdesc,answer,operator,opttime,ifanswer, ");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='ISORNOT' and a.status=0  and a.code=ifanswer) ifanswername");
		sql.append(" from b_webquesbaseinfo where 1=1 ");

		String quesdesc = StringUtil.stringConvert(param.get("quesdesc"));
		if (quesdesc != null && !quesdesc.trim().equals("")) {
			sql.append(" and quesdesc like '%" + quesdesc + "%'");
		}
		
		// 是否已回复
		if (quesType != null && quesType.trim().equals("01")) {
			String ifanswer = StringUtil.stringConvert(param.get("ifanswer"));
			if (!StringUtils.isBlank(ifanswer)) {
				sql.append(" and ifanswer = '").append(ifanswer).append("'");
			} else {
				sql.append(" and ifanswer = '0'");
			}
		} else if (quesType != null && quesType.trim().equals("02")) {
			String caseid = StringUtil.stringConvert(param.get("caseid"));
			if (!StringUtils.isBlank(caseid)) {
				sql.append(" and caseid = '").append(caseid).append("'");
			}
		}
		
		if (!StringUtils.isBlank(quesType)) {
			sql.append(" and quetype = '").append(quesType).append("'");
		}
		
		
		sql.append("  order by opttime desc,asktime desc");
		
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}


	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Map<String, Object> param) throws Exception {
		// 保存专家信息
		BWebquesbaseinfo bWebquesbaseinfo = new BWebquesbaseinfo();
		BeanUtils.populate(bWebquesbaseinfo, param);

		SysUser user = SecureUtil.getCurrentUser();
		bWebquesbaseinfo.setOperator(user.getUserid().toString());
		// 获取系统当前日期
		String opttime = FormatUtil.stringDate2();
		bWebquesbaseinfo.setOpttime(opttime);
		// 是否回答默认1-是
		bWebquesbaseinfo.setIfanswer("1");

		String id = "";
		if (bWebquesbaseinfo != null && bWebquesbaseinfo.getQuesid() != null && !bWebquesbaseinfo.getQuesid().trim().equals("")) {
			dao.update(bWebquesbaseinfo);
		} else {
			id = (String) dao.save(bWebquesbaseinfo);
		}
		return id;
	}

	/**
	 * 
	 * @param param
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save_web(Map<String, Object> param) throws IllegalAccessException, InvocationTargetException {
		// 保存专家信息
		BWebquesbaseinfo bWebquesbaseinfo = new BWebquesbaseinfo();
		BeanUtils.populate(bWebquesbaseinfo, param);
		
		// 获取系统当前日期
		String asktime = FormatUtil.stringDate2();
		bWebquesbaseinfo.setAsktime(asktime);
		// 是否回答默认0-否
		bWebquesbaseinfo.setIfanswer("0");
		bWebquesbaseinfo.setQuetype("02");

		String id = "";
		if (bWebquesbaseinfo != null && bWebquesbaseinfo.getQuesid() != null && !bWebquesbaseinfo.getQuesid().trim().equals("")) {
			dao.update(bWebquesbaseinfo);
		} else {
			id = (String) dao.save(bWebquesbaseinfo);
		}
		return id;
	}


	/**
	 * 按照quesid删除反馈意见
	 * 
	 * @Title: deleteById
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 * @throws Exception
	 *             设定文件
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteById(String quesid) throws Exception {
		if (quesid == null || "".equals(quesid)) {
			throw new Exception("参数quesid的值未获取到！");
		}

		BWebquesbaseinfo bWebquesbaseinfo = new BWebquesbaseinfo();
		bWebquesbaseinfo.setQuesid(quesid);
		dao.delete(bWebquesbaseinfo);

		return "";
	}
	
}

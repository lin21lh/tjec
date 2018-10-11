package com.wfzcx.aros.dxal.service;

import java.math.BigDecimal;
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
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.aros.util.GCC;
import com.wfzcx.aros.dxal.dao.DxalDao;
import com.wfzcx.aros.dxal.po.Dxalinfo;

@Scope("prototype")
@Service("com.wfzcx.aros.dxal.service.DxalWebService")
public class DxalService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	DxalDao dao;
	
	/**
	 * 查询典型案例返回gridlist
	 * @param param
	 * @return
	 */
	public PaginationSupport queryDxalList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append(" select id,casetitle,casedesc,startdate,enddate,operator,opttime, t.ifpublish, ");	
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.ifpublish) ifpublishname ");
		sql.append(" from b_classiccasebaseinfo t where 1=1  ");

		String casetitle = StringUtil.stringConvert(param.get("casetitle"));
		if (casetitle != null && !casetitle.trim().equals("")) {
			sql.append(" and casetitle like '%" + casetitle + "%'");
		}
		
		sql.append("  order by opttime desc ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 典型案例保存方法save
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Map<String, Object> param) throws Exception {
		Dxalinfo dxalinfo=new Dxalinfo();
		BeanUtils.populate(dxalinfo, param);
		String id="";
		if (dxalinfo != null && dxalinfo.getId() != null && !dxalinfo.getId().trim().equals("")) {
			dao.update(dxalinfo);
		} else {
			id = (String) dao.save(dxalinfo);
		}
		return id;
	}
	/**
	 * 典型案例修改方法updateById
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateDxalById(Map<String, Object> param) throws Exception {
		//1、入口参数判断
		//判断案件ID是否为空
		String id = StringUtil.stringConvert(param.get("id"));
		if(StringUtils.isBlank(id)){
			throw new AppException("修改失败：案件ID为空");
		}
		//业务逻辑处理
		Dxalinfo dxalxinfo=dao.get(id);
		if (StringUtils.isBlank(param.get("id").toString())) {
			param.put("id", BigDecimal.ZERO);
			BeanUtils.populate(dxalxinfo, param);
			dxalxinfo.setId(id);
		}else{
			BeanUtils.populate(dxalxinfo, param);
		}
		
		//修改行政复议申请信息
		dao.update(dxalxinfo);
		
	}
	/**
	 * 典型案例删除方法deleteById
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteDxalById(String id) throws Exception {
		try {
			Dxalinfo dxalinfo=new Dxalinfo();
			dxalinfo.setId(id);
			dao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return id;
	}
	
	
	
	
}

package com.wfzcx.ppms.library.dsfjgk.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.wfzcx.ppms.library.dsfjgk.dao.ProDsfjgkDao;
import com.wfzcx.ppms.library.dsfjgk.po.ProDsfjgk;
import com.wfzcx.ppms.library.dsfjgk.service.DsfjgkService;

@Scope("prototype")
@Service("com.wfzcx.ppms.library.dsfjgk.service.impl.DsfjgkServiceImpl")
public class DsfjgkServiceImpl implements DsfjgkService{

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProDsfjgkDao proDsfjgkDao;
	
	@Override
	public PaginationSupport queryDsfjgk(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select t.dsfjgid,t.organ_code,t.organ_name,t.consignor,t.project_manager,")
		.append("t.phone,t.mobile,t.content,t.weixin,t.sfyx, ")
		.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sfyx) sfyx_name, ")
		.append("(select count(1) from pro_approrgan a where a.dsfjgid=t.dsfjgid) is_delete ")
		.append(" from pro_dsfjgk t where 1=1");
		String organCode = StringUtil.stringConvert(param.get("organCode"));
		if(!"".equals(organCode)){
			sql.append(" and t.organ_code like '%").append(organCode.trim()).append("%'");
		}
		String organName = StringUtil.stringConvert(param.get("organName"));
		if(!"".equals(organName)){
			sql.append(" and t.organ_name like '%").append(organName.trim()).append("%'");
		}
		String phone = StringUtil.stringConvert(param.get("phone"));
		if(!"".equals(phone)){
			sql.append(" and t.phone like '%").append(phone.trim()).append("%'");
		}
		String weixin = StringUtil.stringConvert(param.get("weixin"));
		if(!"".equals(weixin)){
			sql.append(" and t.weixin like '%").append(weixin.trim()).append("%'");
		}
		sql.append(" order by t.dsfjgid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String dsfjgkAddCommit(Map<String, Object> param) throws Exception{
		ProDsfjgk dsfjg = new ProDsfjgk();
		BeanUtils.populate(dsfjg, param);
		String msg = "";
		try {
			proDsfjgkDao.save(dsfjg);
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String dsfjgkEditCommit(Map<String, Object> param) throws Exception {
		ProDsfjgk dsfjgk = proDsfjgkDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("dsfjgid"))));
		BeanUtils.populate(dsfjgk, param);
		proDsfjgkDao.update(dsfjgk);
		return "success";
	}
	
	/**
	 *  根据主键dsfjgid删除第三方机构
	 * @param dsfjgid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void dsfjgkDelete(String dsfjgid){
		
		String where = "dsfjgid = " + dsfjgid;
		proDsfjgkDao.deleteBySQL("PRO_DSFJGK", where);
		

	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String dsfjgkCheck(Map<String, Object> param) throws Exception{
		String dsfjgid = StringUtil.stringConvert(param.get("dsfjgid"));
		String attributeName = StringUtil.stringConvert(param.get("attributeName"));
		String attributeCode = StringUtil.stringConvert(param.get("attributeCode"));
		String flag = StringUtil.stringConvert(param.get("flag"));
		Map map = null;
		String msg = "success";
		if ("add".equals(flag)){//新增页面验证
			String sql = "select count(1) c from pro_dsfjgk where "+attributeName+" = '"+attributeCode+"' ";
			List list = proDsfjgkDao.findMapBySql(sql);
			map =  (Map) list.get(0);
			BigDecimal num =(BigDecimal)map.get("c");
			if (num.intValue()> 0){
				msg = "fail";
			} 
		} else {//修改页面验证
			String sql = "select dsfjgid from pro_dsfjgk where "+attributeName+" = '"+attributeCode+"' ";
			List list = proDsfjgkDao.findMapBySql(sql);
			for(int i=0;i<list.size();i++){
				map =  (Map) list.get(i);
				BigDecimal dsfid =(BigDecimal)map.get("dsfjgid");
				
				if (!(dsfid.toString().equals(dsfjgid))){
					msg = "fail";
					break;
				}
			}
		}
		return msg;
	}
	
}

package com.wfzcx.ppms.library.zbk.service.impl;

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
import com.wfzcx.ppms.library.zbk.dao.ProZbkDao;
import com.wfzcx.ppms.library.zbk.po.ProZbk;
import com.wfzcx.ppms.library.zbk.service.ZbkService;

@Scope("prototype")
@Service("com.wfzcx.ppms.library.zbk.service.impl.ZbkServiceImpl")
public class ZbkServiceImpl implements ZbkService{

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProZbkDao proZbkDao;
	
	@Override
	public PaginationSupport queryZbk(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select t.zbkid,t.zbmc,t.zbms,t.sfyx,")
		.append("t.zblb, ")
		.append("(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROZBLB' and a.status=0  and a.code=t.zblb) zblb_name,")
		.append("(select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0  and a.code=t.sfyx) sfyx_name, ")
		.append("(select count(1) from pro_pszb_zb a where a.zbkid=t.zbkid) is_delete ")
		.append(" from pro_zbk t where 1=1 ");
		String zbmc = StringUtil.stringConvert(param.get("zbmc"));
		if(!"".equals(zbmc)){
			sql.append(" and t.zbmc like '%").append(zbmc.trim()).append("%'");
		}
		String zblb = StringUtil.stringConvert(param.get("zblb"));
		if(!"".equals(zblb)){
			sql.append(" and t.zblb = '").append(zblb.trim()).append("'");
		}
		String sfyx = StringUtil.stringConvert(param.get("sfyx"));
		if(!"".equals(sfyx)){
			sql.append(" and t.sfyx = '").append(sfyx.trim()).append("'");
		}
		sql.append(" order by t.zbkid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String zbkAddCommit(Map<String, Object> param) throws Exception{
		ProZbk zbk = new ProZbk();
		BeanUtils.populate(zbk, param);
		String msg = "";
		try {
			proZbkDao.save(zbk);
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String zbkEditCommit(Map<String, Object> param) throws Exception {
		ProZbk zbk = proZbkDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("zbkid"))));
		BeanUtils.populate(zbk, param);
		proZbkDao.update(zbk);
		return "success";
	}
	
	/**
	 *  根据主键zbkid删除指标
	 * @param zbkid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void zbkDelete(String zbkid){

		String where = "zbkid = " + zbkid;
		proZbkDao.deleteBySQL("PRO_ZBK", where);

	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String zbkCheck(Map<String, Object> param) throws Exception{
		String zbkid = StringUtil.stringConvert(param.get("zbkid"));
		String attributeName = StringUtil.stringConvert(param.get("attributeName"));
		String attributeCode = StringUtil.stringConvert(param.get("attributeCode"));
		String flag = StringUtil.stringConvert(param.get("flag"));
		Map map = null;
		String msg = "success";
		if ("add".equals(flag)){//新增页面验证
			String sql = "select count(1) c from pro_zbk where "+attributeName+" = '"+attributeCode+"' ";
			List list = proZbkDao.findMapBySql(sql);
			map =  (Map) list.get(0);
			BigDecimal num =(BigDecimal)map.get("c");
			if (num.intValue()> 0){
				msg = "fail";
			} 
		} else {//修改页面验证
			String sql = "select zbkid from pro_zbk where "+attributeName+" = '"+attributeCode+"' ";
			List list = proZbkDao.findMapBySql(sql);
			for(int i=0;i<list.size();i++){
				map =  (Map) list.get(i);
				BigDecimal orid =(BigDecimal)map.get("zbkid");
				
				if (!(orid.toString().equals(zbkid))){
					msg = "fail";
					break;
				}
			}
		}
		return msg;
	}
}

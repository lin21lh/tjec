package com.wfzcx.ppms.wdjg.service.impl;

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
import com.wfzcx.ppms.wdjg.dao.ProWdjgDao;
import com.wfzcx.ppms.wdjg.po.ProWdjg;
import com.wfzcx.ppms.wdjg.service.WdjgService;

/**
 * 文档结构Service类
 * @author wang_yliang
 *
 */
@Scope("prototype")
@Service("com.wfzcx.ppms.wdjg.service.impl.WdjgServiceImpl")
public class WdjgServiceImpl implements WdjgService {

	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	ProWdjgDao proWdjgDao;
	
	@Override
	public PaginationSupport queryWdjg(Map<String, Object> param) throws AppException {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;

		StringBuffer sql = new StringBuffer();
		sql.append("select t.wdjgid,t.xmhj,t.hjfl,t.wdmc,")
		.append("t.glbm,t.glzd,t.bz,t.plsx ")
		.append(",(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROXMDQHJ' and a.status=0  and a.code=t.xmhj) xmhj_name")
		.append(",(select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='PROXMHJFL' and a.status=0  and a.code=t.hjfl) hjfl_name")
		.append(" from pro_wdjg t where 1=1 ");
		String xmhj = StringUtil.stringConvert(param.get("xmhj"));
		if(!"".equals(xmhj)){
			sql.append(" and t.xmhj = '").append(xmhj.trim()).append("'");
		}
		String hjfl = StringUtil.stringConvert(param.get("hjfl"));
		if(!"".equals(hjfl)){
			sql.append(" and t.hjfl = '").append(hjfl.trim()).append("'");
		}
		String wdmc = StringUtil.stringConvert(param.get("wdmc"));
		if(!"".equals(wdmc)){
			sql.append(" and t.wdmc like '%").append(wdmc.trim()).append("%'");
		}
		sql.append(" order by t.wdjgid desc");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);

	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String wdjgAddCommit(Map<String, Object> param) throws Exception{
		ProWdjg proWdjg = new ProWdjg();
		BeanUtils.populate(proWdjg, param);
		String msg = "";
		try {
			proWdjgDao.save(proWdjg);
			msg = "success";
		} catch (Exception e) {
			msg = "fail";
			throw new Exception();
		}
		return msg;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String wdjgEditCommit(Map<String, Object> param) throws Exception {
		ProWdjg po = proWdjgDao.get(Integer.parseInt(StringUtil.stringConvert(param.get("wdjgid"))));
		BeanUtils.populate(po, param);
		proWdjgDao.update(po);
		return "success";
	}
	
	/**
	 *  根据主键wdjgid删除
	 * @param wdjgid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void wdjgDelete(String wdjgid){

		String where = "wdjgid = " + wdjgid;
		proWdjgDao.deleteBySQL("PRO_WDJG", where);

	}
	
}

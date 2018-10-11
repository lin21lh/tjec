package com.wfzcx.demo.service;

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
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;
import com.wfzcx.demo.dao.DemoDao;
import com.wfzcx.demo.po.Demo;

@Scope("prototype")
@Service("com.wfzcx.demo.service.DemoService")
public class DemoService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	DemoDao dao;
	public PaginationSupport queryProject(Map<String, Object> param)   throws AppException{
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()): PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();

		sql.append("select t.id,t.name,t.sex,t.remark,t.ssjg,");
		sql.append(" (select a.name from SYS_YW_DICENUMITEM a where upper(a.elementcode)='SEX' and a.status=0  and a.code=t.sex) sex_mc,");
		sql.append(" (select b.name from sys_dept b where b.code=t.ssjg) ssjg_mc");
		sql.append(" from demo t");

		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Integer demoSave(Map<String, Object> param) throws Exception{
		/**
		 * 1、保存项目主表
		 */
		Demo demo = new Demo();
		BeanUtils.populate(demo, param);
		SysUser user = SecureUtil.getCurrentUser();
		demo.setCjr(user.getUserid().toString());//创建人
		demo.setCjsj(DateUtil.getDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		demo.setSsjg(user.getOrgcode());//登录人所属机构代码
		Integer id =(Integer) dao.save(demo);
		//项目保存完成后，将项目id更新至附件中的keyid中
		String fjkeyid = StringUtil.stringConvert(param.get("fjkeyid"));
		String updFjKeySql =" update sys_filemanage t set t.keyid='"+id+"' where t.keyid='"+fjkeyid+"' ";
		dao.updateBySql(updFjKeySql);
		return id;
	}
}

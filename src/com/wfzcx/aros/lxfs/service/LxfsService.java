package com.wfzcx.aros.lxfs.service;

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
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.lxfs.dao.LxfsDao;
import com.wfzcx.aros.lxfs.po.LxfsInfo;

@Scope("prototype")
@Service("com.wfzcx.aros.lxfs.service.LxfsService")
public class LxfsService {
	@Autowired
	MapDataDaoI mapDataDao;
	@Autowired
	LxfsDao dao;
	/**
	 * 查询联系方式返回gridlist
	 * @param param
	 * @return
	 */
	public PaginationSupport queryLxfsList(Map<String, Object> param) {
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString()) : PaginationSupport.PAGESIZE;
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.id,t.deptname,t.address,t.person,t.phone,t.xcoor,t.ycoor from b_contactbaseinfo t where 1=1 ");
		//查询的时候所用的方法
		String person = StringUtil.stringConvert(param.get("person"));
 		if (person != null && !person.trim().equals("")) {
			sql.append(" and t.person like '%" + person + "%'");
		}
		sql.append("  order by deptname desc ");
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	}
	/**
	 * 联系方式的保存方法save
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String save(Map<String, Object> param) throws Exception {
		LxfsInfo lxfsInfo =new LxfsInfo();
		BeanUtils.populate(lxfsInfo, param);
		String id="";
		if (lxfsInfo != null && lxfsInfo.getId() != null && !lxfsInfo.getId().trim().equals("")) {
			dao.update(lxfsInfo);
		} else {
			id = (String) dao.save(lxfsInfo);
		}
		return id;
	}
	/**
	 * 联系方式修改方法updateById
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateLxfsById(Map<String, Object> param) throws Exception {
		//1、入口参数判断，判断案件ID是否为空
		String id = StringUtil.stringConvert(param.get("id"));
		if(StringUtils.isBlank(id)){
			throw new AppException("修改失败：案件ID为空");
		}
		//2、业务逻辑处理
		LxfsInfo lxfsInfo=dao.get(id);
		if (StringUtils.isBlank(param.get("id").toString())) {
			param.put("id", BigDecimal.ZERO);
			BeanUtils.populate(lxfsInfo, param);
			lxfsInfo.setId(id);
		}else{
			BeanUtils.populate(lxfsInfo, param);
		}
		//3、修改行政复议申请信息
		dao.update(lxfsInfo);
		
	}
	/**
	 * 联系方式删除方法deleteById
	 * @param param
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String deleteLxfsById(String id) throws Exception {
		try {
			LxfsInfo lxfsInfo =new LxfsInfo();
			lxfsInfo.setId(id);
			dao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return id;
	}
	
	
}

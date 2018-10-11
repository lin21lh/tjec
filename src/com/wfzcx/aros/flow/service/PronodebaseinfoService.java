package com.wfzcx.aros.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.wfzcx.aros.flow.dao.PronodebaseinfoDao;
import com.wfzcx.aros.flow.po.Pronodebaseinfo;

/**
 * @ClassName: PronodebaseinfoService
 * @Description: 流程节点配置信息业务实现类
 * @author ybb
 * @date 2016年11月28日 上午9:53:03
 * @version V1.0
 */
@Scope("prototype")
@Service("com.wfzcx.aros.flow.service.PronodebaseinfoService")
public class PronodebaseinfoService {

	@Autowired
	private MapDataDaoI mapDataDao;
	@Autowired
	private PronodebaseinfoDao pronodebaseinfoDao;
	
	/**
	 * @Title: queryPronodebaseinfoList
	 * @Description: 流程配置——根据条件分页查询流程配置列表
	 * @author ybb
	 * @date 2016年11月28日 上午10:11:46
	 * @param param
	 * @return
	 * @throws AppException
	 */
	public PaginationSupport queryPronodebaseinfoList(Map<String, Object> param) throws AppException {
		
		//页面条数
		Integer pageSize = StringUtil.isNotNull(param.get("rows")) ? Integer.valueOf(param.get("rows").toString())
				: PaginationSupport.PAGESIZE;
		//起始条数
		Integer pageIndex = StringUtil.isNotNull(param.get("page")) ? Integer.valueOf(param.get("page").toString()) : 1;
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.id, t.proname, t.protype, t.nodename, t.nodeid, t.roleid, t.rolename, t.menuurl,t.optional,");
		sql.append(" (select a.name from SYS_DICENUMITEM a where upper(a.elementcode)='SYS_TRUE_FALSE' and a.status=0 and a.code = t.optional) optional_mc ");
		sql.append(" from PUB_PRONODEBASEINFO t");
		
		//流程类型
		String protype = StringUtil.stringConvert(param.get("protype"));
		if (StringUtils.isNotBlank(protype)) {
			sql.append(" where t.protype = '").append(protype).append("'");
		} 
		
		sql.append(" order by t.protype,t.nodeid ");
		
		//获取当前节点 
		return mapDataDao.queryPageBySQLForConvert(sql.toString(), pageIndex, pageSize);
	} 
	
	/**
	 * @Title: addPronodebaseinfo
	 * @Description: 流程配置——新增流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:22:28
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void addPronodebaseinfo(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
		//2、业务逻辑判断
		
		//3、业务逻辑处理
			//获取流程配置PO，并将页面参数转化到该对象中
		Pronodebaseinfo pronodebaseinfo = new Pronodebaseinfo();
		
		BeanUtils.populate(pronodebaseinfo, param);
		
			//新增被复议案件申请
		pronodebaseinfoDao.save(pronodebaseinfo);
	}
	
	/**
	 * @Title: updatePronodebaseinfo
	 * @Description: 流程配置——修改流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:29:06
	 * @param param
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updatePronodebaseinfo(Map<String, Object> param) throws Exception{
		
		//1、入口参数判断
			//判断流程配置ID是否为空
		String id = StringUtil.stringConvert(param.get("id"));
		if(StringUtils.isBlank(id)){
			throw new AppException("修改失败：流程配置ID为空");
		}
		
		//2、业务逻辑判断
			//判断流程配置是否正常
		Pronodebaseinfo pronodebaseinfo = pronodebaseinfoDao.get(id);
		if(pronodebaseinfo == null){
			throw new AppException("修改失败：未找到对应的流程配置信息");
		}
		
		//3、业务逻辑处理
			//转化页面表单属性
		BeanUtils.populate(pronodebaseinfo, param);
		
			//修改流程配置信息
		pronodebaseinfoDao.update(pronodebaseinfo);
	}
	
	/**
	 * @Title: delPronodebaseinfo
	 * @Description: 流程配置——删除流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:33:55
	 * @param id
	 * @throws AppException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delPronodebaseinfo(String id) throws AppException {
		
		//1、入口参数判断
			//判断流程配置ID是否为空
		if(StringUtils.isBlank(id)){
			throw new AppException("修改失败：流程配置ID为空");
		}
		
		//2、业务逻辑判断
			//判断流程配置是否正常
		Pronodebaseinfo pronodebaseinfo = pronodebaseinfoDao.get(id);
		if(pronodebaseinfo == null){
			throw new AppException("修改失败：未找到对应的流程配置信息");
		}
	
		//3、业务逻辑处理		
			//删除流程配置信息
		String where = "id = '" + id + "'";
		pronodebaseinfoDao.deleteBySQL("PUB_PRONODEBASEINFO", where);
	}
	
	/**
	 * @Title: queryPronodebaseinfoById
	 * @Description: 流程配置——根据流程配置ID查询流程配置信息
	 * @author ybb
	 * @date 2016年11月28日 上午10:39:08
	 * @param id
	 * @return
	 * @throws AppException
	 */
	public Pronodebaseinfo queryPronodebaseinfoById(String id) throws AppException {
		
		//判断流程配置是否正常
		Pronodebaseinfo pronodebaseinfo = pronodebaseinfoDao.get(id);
		
		return pronodebaseinfo;
	}
	
	/**
	 * @Title: queryRoleList
	 * @Description: 流程配置——查询角色列表
	 * @author ybb
	 * @date 2016年11月28日 下午5:05:44
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryRoleList() throws AppException {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		sql.append("select roleid, rolename from SYS_ROLE t where status = '0' ");
		List<JSONObject> roles = mapDataDao.queryListBySQL(sql.toString());
		if (roles != null && !roles.isEmpty()) {
			
			for (JSONObject obj : roles) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("roleid", obj.getString("roleid"));
				map.put("rolename", obj.getString("rolename"));
				
				list.add(map);
			}
		}
		
		return list;
	}
	
	/**
	 * @Title: queryPronodebaseinfos
	 * @Description: 查询角色对应流程列表
	 * @author ybb
	 * @date 2017年3月21日 上午11:46:04
	 * @param userid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> queryPronodebaseinfos(Long userid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		sql.append("select distinct t.proname,t.protype from pub_pronodebaseinfo t ");
		sql.append(" where t.roleid in (select roleid from SYS_USER_ROLE where userid = ").append(userid).append(")");
		sql.append(" order by protype ");
		System.out.println(sql.toString());
		List<JSONObject> roles = mapDataDao.queryListBySQL(sql.toString());
		if (roles != null && !roles.isEmpty()) {
			
			for (JSONObject obj : roles) {
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("proname", obj.getString("proname"));
				map.put("protype", obj.getString("protype"));
				
				list.add(map);
			}
		}
		
		return list;
	}
}

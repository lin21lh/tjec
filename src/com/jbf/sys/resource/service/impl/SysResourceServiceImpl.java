package com.jbf.sys.resource.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.TreeVoUtil;
import com.jbf.sys.resource.vo.ResourceTreeVo;
import com.jbf.sys.resource.po.SysResource;
import com.jbf.sys.resource.service.SysResourceService;
import com.jbf.sys.resource.dao.SysResourceDao;
import com.jbf.sys.role.po.SysRoleResource;
import com.jbf.workflow.component.SysWorkflowManageComponent;
import com.wfzcx.aros.flow.po.Probaseinfo;

@Scope("prototype")
@Service
public class SysResourceServiceImpl implements SysResourceService {

	@Autowired
	SysResourceDao sysResourceDao;
	@Autowired
	private MapDataDaoI mapDataDao;
	
	@Autowired
	SysWorkflowManageComponent workflowManageComponent;

	public SysResource get(Long id) {
		
		SysResource resource = sysResourceDao.get(id);
		if (StringUtil.isNotBlank(resource.getWfkey()) && StringUtil.isNotBlank(resource.getActivityid())) {
			List<HashMap> list = null;
			if (resource.getWfversion() != null) {
				list = workflowManageComponent.getWorkflowDefinitionActivities(resource.getWfkey(), resource.getWfversion());
			} else { //未指定版本号默认以当前运行版本号处理
				Integer currentVersion = workflowManageComponent.getValidVersionByDate(resource.getWfkey(), new Date());
				if (currentVersion != -1)
					list = workflowManageComponent.getWorkflowDefinitionActivities(resource.getWfkey(), currentVersion);
				else
					list = new ArrayList<HashMap>();
			}
			if (!list.isEmpty()) {				
				for (HashMap value : list) {
					if (resource.getActivityid().equalsIgnoreCase((String) value.get("actiId"))) {
						resource.setActivityname((String) value.get("actiName"));
						break;
					}
				}
			}
		}
		
		return resource;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long save(SysResource sysResource) {

		Long id = sysResource.getResourceid();
		if (null == sysResource.getStatus()) {
			sysResource.setStatus((byte) 0);
		}
		if (id != null) {
			sysResourceDao.update(sysResource);
		} else {
			Integer levelno = 1;
			if (sysResource.getParentresid() != null && sysResource.getParentresid() != 0) {
				SysResource parentResource = sysResourceDao.get(sysResource.getParentresid());
				levelno = levelno + Integer.valueOf(parentResource.getLevelno().toString());
				if (parentResource.getIsleaf().equals((byte)1)) {
					parentResource.setIsleaf((byte)0);
					sysResourceDao.update(parentResource);
				}
			}
			sysResource.setLevelno(Byte.valueOf(levelno.toString()));
			sysResource.setIsleaf((byte)1);
			id = (Long) sysResourceDao.save(sysResource);
		}
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long id) throws Exception {
		SysResource res = sysResourceDao.get(id);
		if (res == null) {
			throw new AppException("permissions.rs.not.exist");
		}
		// 判断菜单是否有子菜单
		List<SysResource> subRes = (List<SysResource>) sysResourceDao.find(
				" from SysResource where parentresid= ?", id);

		if (subRes != null && subRes.size() > 0) {
			throw new AppException("permissions.rs.has.children");
		}
		sysResourceDao.delete(res);
		//检查父节点是否有子节点,如果没有了，则修改isleaf 为1
		Long pid=res.getParentresid();
		SysResource p=sysResourceDao.get(pid);
		if(p!=null){
			List<SysResource> ss = (List<SysResource>) sysResourceDao.find(
					" from SysResource where parentresid= ?", p.getResourceid());
			if(ss.size()==0){
				p.setIsleaf((byte)1);
				sysResourceDao.update(p);
			}
		}
	}

	@Override
	public List query() throws Exception {
		List<SysResource> list = (List<SysResource>) sysResourceDao
				.find(" from SysResource order by levelno,resorder");

		List<ResourceTreeVo> reslist = new ArrayList<ResourceTreeVo>();
		for (SysResource res : list) {
			ResourceTreeVo vo = new ResourceTreeVo();
			vo.setId("" + res.getResourceid());
			vo.setPid("" + res.getParentresid());
			vo.setText(res.getName());

			vo.setWebpath(res.getWebpath());
			vo.setLevelno(0 + res.getLevelno());
			vo.setIconCls(res.getIconCls());
			vo.setResorder(res.getResorder());
			vo.setIsLeaf(res.getIsleaf().equals(Byte.valueOf("1")));
			vo.setStatus(res.getStatus());
			vo.setWfkey(res.getWfkey());
			reslist.add(vo);
		}
		return TreeVoUtil.toBornTree(reslist, "0", true);
	}
	
	public List queryBusinessList() {
		DetachedCriteria dc = DetachedCriteria.forClass(SysResource.class);
		dc.add(Property.forName("restype").eq(Byte.valueOf("1")));
		List<SysResource> list = (List<SysResource>) sysResourceDao.findByCriteria(dc);
		List<ResourceTreeVo> reslist = new ArrayList<ResourceTreeVo>();
		for (SysResource resource : list) {
			ResourceTreeVo vo = new ResourceTreeVo();
			vo.setId("" + resource.getResourceid());
			vo.setPid("" + resource.getParentresid());
			vo.setText(resource.getName());

			vo.setWebpath(resource.getWebpath());
			vo.setLevelno(0 + resource.getLevelno());
			vo.setIconCls(resource.getIconCls());
			vo.setResorder(resource.getResorder());
			vo.setIsLeaf(resource.getIsleaf().equals(Byte.valueOf("1")));
			vo.setStatus(resource.getStatus());
			vo.setWfkey(resource.getWfkey());
			reslist.add(vo);
		}
		
		return TreeVoUtil.toBornTree(reslist, "0", false);
	}

	@Override
	public List queryResourceTreeByRole(Long roleid) throws Exception {

		DetachedCriteria dcCriteria = DetachedCriteria
				.forClass(SysResource.class);
		DetachedCriteria dcCriteriaSub = DetachedCriteria
				.forClass(SysRoleResource.class);
		dcCriteriaSub.add(Restrictions.eq("roleid", roleid));
		dcCriteriaSub.setProjection(Property.forName("resourceid"));
		dcCriteria.add(Property.forName("resourceid").in(dcCriteriaSub));

		List<SysResource> resList = (List<SysResource>) sysResourceDao
				.findByCriteria(dcCriteria);
		List<ResourceTreeVo> treeList = new ArrayList<ResourceTreeVo>();
		for (SysResource res : resList) {
			ResourceTreeVo vo = new ResourceTreeVo();
			vo.setId("" + res.getResourceid());
			vo.setPid("" + res.getParentresid());
			vo.setText(res.getName());
			vo.setWebpath(res.getWebpath());
			treeList.add(vo);
		}
		return TreeVoUtil.toBornTree(treeList, "0", false);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void resourceReorder(Long pid, Long srcid, Long tgtid, String point)
			throws Exception {
		// 查询父节点下面的所有节点

		List<SysResource> reslist = (List<SysResource>) sysResourceDao.find(
				"from SysResource where parentresid = ? order by  resorder",
				pid);

		if (reslist.size() == 0) {
			throw new AppException("param.invalid");
		}
		// 只有一个节点，不需要排序
		if (reslist.size() == 1) {
			return;
		}
		SysResource src = null;

		for (int i = 0; i < reslist.size(); i++) {
			if (srcid.equals(reslist.get(i).getResourceid())) {
				src = reslist.get(i);
				reslist.remove(src);
			}
		}
		if (src == null) {
			throw new AppException("param.invalid");
		}
		int tgtPosition = -1;
		// 取得目标位置
		for (int i = 0; i < reslist.size(); i++) {
			if (tgtid.equals(reslist.get(i).getResourceid())) {
				tgtPosition = i;
			}
		}
		if (tgtPosition == -1) {
			throw new AppException("param.invalid");
		}
		// 插入
		if ("top".equals(point)) {
			reslist.add(tgtPosition, src);
		} else {
			reslist.add(tgtPosition + 1, src);
		}
		// 重新设置位置
		for (int i = 0; i < reslist.size(); i++) {
			reslist.get(i).setResorder(i + 1);
			sysResourceDao.update(reslist.get(i));
		}

	}

	/**
	 * @Title: queryResource
	 * @Description: 根据用户ID查询角色对应的菜单列表
	 * @author ybb
	 * @date 2017年3月25日 下午3:32:10
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> queryResource(Long userid) {
		
		//获取StringBuffer对象，用来拼接sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append("select t.name,t.webpath,t.remark,t.toremindwhere from sys_resource t where t.parentresid = 0 and t.status =1 ");
		sql.append("and t.resourceid in ");
		sql.append("(select resourceid from sys_role_resource t1,sys_user_role t2 where t1.roleid = t2.roleid ");
		sql.append(" and t2.userid = ").append(userid).append(")");
		sql.append(" order by t.resorder ");
		
		List<JSONObject> resources = mapDataDao.queryListBySQL(sql.toString());
		
		return resources;
	}
}

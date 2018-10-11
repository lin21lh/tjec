package com.jbf.sys.resource.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.dic.po.SysDicEnumItem;
import com.jbf.common.exception.AppException;
import com.jbf.sys.resource.dao.SysResourceOperDao;
import com.jbf.sys.resource.po.SysResourceOper;
import com.jbf.sys.resource.service.SysResourceOperService;

@Scope("prototype")
@Service
public class SysResourceOperServiceImpl implements SysResourceOperService {

	@Autowired
	SysResourceOperDao sysResourceOperDao;

	public SysResourceOper get(Long id) {
		return sysResourceOperDao.get(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void save(SysResourceOper sysResourceOper) {
		Long id = sysResourceOper.getResourceid();
		if (id != null) {
			sysResourceOperDao.update(sysResourceOper);
		} else {
			sysResourceOperDao.save(sysResourceOper);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long id) throws Exception {
		SysResourceOper res = sysResourceOperDao.get(id);
		if (res == null) {
			throw new AppException("permissions.rs.not.exist");
		}
		// 查询是否已被引用，如果已被引用，不允许删除
		List list = sysResourceOperDao.find(
				" from SysRoleResourceOper where resoperid = ?", id);
		if (list.size() > 0) {
			throw new AppException("crud.delerr.objrefered");
		}
		sysResourceOperDao.delete(res);
	}


	@Override
	public List queryPresetOper(Long resourceid) {
		// 查询所有的预定义oper

		String hql = "  from SysDicEnumItem  where  elementcode= ?";
		List<SysDicEnumItem> presets = (List<SysDicEnumItem>) sysResourceOperDao
				.find(hql, "SYS_RESOURCE_OPERATION");

		// 查询菜单拥有的操作

		String hql2 = " from SysResourceOper where resourceid = ?";
		List<SysResourceOper> opers = (List<SysResourceOper>) sysResourceOperDao
				.find(hql2, resourceid);
		Set<String> set = new HashSet<String>();

		for (SysResourceOper o : opers) {
			set.add(o.getCode());
		}

		List<HashMap> toadd = new ArrayList<HashMap>();
		for (SysDicEnumItem o : presets) {
			if (!set.contains(o.getCode())) {
				HashMap map = new HashMap();
				map.put("code", o.getCode());
				map.put("name", o.getName());
				toadd.add(map);
			}
		}
		return toadd;
	}

	public List queryOper(Long resourceid) {
		return sysResourceOperDao.find(
				"from SysResourceOper where resourceid =?  order by position,code", resourceid);
	}

	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	// public void addCustomOper(SysResourceOper oper) throws Exception {
	//
	// sysResourceOperDao.save(oper);
	// }
	//
	// @Override
	// @Transactional(propagation = Propagation.REQUIRED, rollbackFor =
	// Exception.class)
	// public void addPresetOper(String codeArray, Long resourceid)
	// throws Exception {
	// // 查询预定义的Oper
	//
	// if (codeArray == null || codeArray.trim().length() == 0
	// || resourceid == null) {
	// throw new AppException("param.invalid");
	// }
	//
	// String[] codes = codeArray.split(";");
	// for (String code : codes) {
	// code = code.trim();
	// String pair[] = code.split(",");
	// if (code.length() > 0) {
	// SysResourceOper op = new SysResourceOper();
	// op.setResourceid(resourceid);
	// op.setCode(pair[0]);
	// op.setName(pair[1]);
	// sysResourceOperDao.save(op);
	// }
	// }
	// }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveResourceOper(SysResourceOper oper) throws Exception {
		if (null == oper.getResourceid()) {
			throw new AppException("crud.saveerr");
		}
		if (oper.getId() == null) {
			// 添加时检查code是否重复
			List list = sysResourceOperDao.find(
					" from SysResourceOper where resourceid= ? and code= ?",
					oper.getResourceid(), oper.getCode());
			if (list.size() > 0) {
				throw new AppException("crud.saveerr.code.repeat",
						new String[] { oper.getCode() });
			}
		}
		sysResourceOperDao.saveOrUpdate(oper);
	}

	
}

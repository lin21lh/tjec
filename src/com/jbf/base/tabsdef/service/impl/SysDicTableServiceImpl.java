package com.jbf.base.tabsdef.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.base.tabsdef.service.SysDicTableService;
import com.jbf.common.TableNameConst;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.exception.AppException;
import com.jbf.common.security.SecureUtil;
import com.jbf.common.util.DateUtil;
import com.jbf.common.util.StringUtil;
import com.jbf.sys.user.po.SysUser;

/**
 * 
 * @ClassName: SysDicTableServiceImpl
 * @Description: 数据表业务逻辑操作实现类
 * @author 无敌威猛の赵小布
 * @date 2014年9月5日 下午3:16:33
 * 
 */
@Scope("prototype")
@Service
public class SysDicTableServiceImpl implements SysDicTableService {

	@Autowired
	SysDicTableDao dicTableDao;
	@Autowired
	SysDicColumnDao sysDicColumnDao;

	/**
	 * 查询数据表记录
	 * @return 数据表分页数据
	 * @throws AppException 
	 */
	public PaginationSupport query(Map<String, Object> params) throws AppException {
		return dicTableDao.query(params);
	}

	public List<SysDicTable> searchTables(Map<String, Object> params) {
		return dicTableDao.searchTables(params);
	}

	/**
	 * 修改数据表记录
	 * 
	 * @param dicTable
	 *            数据表对象
	 * @throws Exception
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Long edit(SysDicTable dicTable) throws Exception {

		// 改成大写
		dicTable.setTablecode(dicTable.getTablecode().toUpperCase());

		// 验证表是否存在
		boolean bl = dicTableDao.isExistTable(dicTable.getTablecode());
		if (!bl) {
			throw new AppException("datatable.table.not.exists",
					new String[] { dicTable.getTablecode() });
		}
		if (null == dicTable.getTableid()) {
			// 添加
			SysUser curUser = SecureUtil.getCurrentUser();
			dicTable.setCreateuser(curUser.getUsername());
			dicTable.setCreatetime(DateUtil.getCurrentDate());
			return (Long) dicTableDao.save(dicTable);
		} else {
			// 修改
			SysDicTable t = dicTableDao.get(dicTable.getTableid());
			if (t == null) {
				throw new AppException("crud.saveerr");
			}
			SysDicTable t1 = t.getClass().newInstance();
			// 仅更新4个字段
			t.setTablename(dicTable.getTablename());
			t.setTablecode(dicTable.getTablecode());
			t.setTabletype(dicTable.getTabletype());
			t.setRemark(dicTable.getRemark());
			t1.setDbversion(dicTable.getDbversion());
			dicTableDao.update(t, t1);
			return dicTable.getTableid();
		}

	}

	/**
	 * 删除数据表记录
	 * 
	 * @param tableid
	 *            数据表ID
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long tableid) throws Exception {
		SysDicTable tab = dicTableDao.get(tableid);
		if (tab == null) {
			throw new AppException("param.invalid");
		}
		// 系统内置表，不允许删除
		if (tab.getSystempretag() == 1) {
			throw new AppException("datatable.preset.table.delete.denied");
		}
		dicTableDao.delete(tab);

		List<SysDicColumn> list = (List<SysDicColumn>) sysDicColumnDao.find(
				" from SysDicColumn where tablecode= ?", tab.getTablecode());

		for (SysDicColumn c : list) {
			sysDicColumnDao.delete(c);
		}
	}

	/**
	 * 查询所有的数据表
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List queryAllTables(String tablecode, String tablename) {
		String where = "";
		if (StringUtil.isNotBlank(tablecode))
			where += "tablecode like '" + tablecode.toUpperCase() + "%'";
		
		if (StringUtil.isNotBlank(tablename)) {
			if (where.length() > 0)
				where += " and ";
			where += "tablename like '%" + tablename +"%'";
		}
		if (where.length() > 0)
			where = " where " + where;
		
		String hql = " from SysDicTable " + where + " order by createtime desc";
		return dicTableDao.find(hql);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List queryTabsToCopyCol(String tablecode, String tablename) {
		String where = "";
		if (StringUtil.isNotBlank(tablecode))
			where += " and tab.tablecode like '" + tablecode.toUpperCase() + "%'";
		
		if (StringUtil.isNotBlank(tablename)) {
			if (where.length() > 0)
				where += " and ";
			where += "tab.tablename like '%" + tablename +"%'";
		}
		
		String sql = "select tab.tableid as tableid, tab.tablecode as tablecode, tab.tablename as tablename from " + TableNameConst.SYS_DICTABLE + " tab, (select distinct tablecode from " 
		+ TableNameConst.SYS_DICCOLUMN + ") col  where tab.tablecode=col.tablecode" + where + " order by createtime desc";
		return dicTableDao.findMapBySql(sql);
	}

	@Override
	public SysDicTable get(Long tableid) {
		return dicTableDao.get(tableid);
	}

	public SysDicTableDao getDicTableDao() {
		return dicTableDao;
	}

	public void setDicTableDao(SysDicTableDao dicTableDao) {
		this.dicTableDao = dicTableDao;
	}

	public SysDicColumnDao getSysDicColumnDao() {
		return sysDicColumnDao;
	}

	public void setSysDicColumnDao(SysDicColumnDao sysDicColumnDao) {
		this.sysDicColumnDao = sysDicColumnDao;
	}

}

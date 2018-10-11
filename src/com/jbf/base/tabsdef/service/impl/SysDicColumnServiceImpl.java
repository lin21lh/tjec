package com.jbf.base.tabsdef.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jbf.base.tabsdef.dao.SysDicColumnDao;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicColumn;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.base.tabsdef.service.SysDicColumnService;
import com.jbf.base.tabsdef.vo.TableColumnVo;
import com.jbf.common.dao.PaginationSupport;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.exception.AppException;
import com.jbf.common.util.StringUtil;

@Scope("prototype")
@Service
public class SysDicColumnServiceImpl implements SysDicColumnService {

	@Autowired
	SysDicTableDao dicTableDao;

	@Autowired
	SysDicColumnDao dicColumnDao;

	@Override
	public SysDicColumn get(Long id) {
		return dicColumnDao.get(id);
	}

	@Override
	public PaginationSupport queryDbTableColumns(String tablecode,
			Integer pageNumber, Integer pageSize) {
		SqlVO sqlvo = dicTableDao.getTableInfoRT(tablecode);

		List<ColVO> cols = sqlvo.getCols();

		int start = (pageNumber - 1) * pageSize;
		int end = start + pageSize;
		int index = start;
		List<TableColumnVo> columns = new ArrayList<TableColumnVo>();
		while (index <= end && index < cols.size()) {
			ColVO vo = cols.get(index);
			TableColumnVo v = new TableColumnVo();
			v.setColname(vo.getName().toUpperCase());
			String datatype = vo.getTypename();
			if ("NUMBER".equals(datatype) && vo.getDecimalDigits() > 0) {
				datatype += "(" + vo.getLength() + "," + vo.getDecimalDigits()
						+ ")";
			} else {
				datatype += "(" + vo.getLength() + ")";
			}

			v.setDatatype(datatype);
			v.setRemarks(vo.getRemarks());
			columns.add(v);
			index++;
		}

		// 查询已经定义的字段
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		List<SysDicColumn> list = (List<SysDicColumn>) dicColumnDao.findByCriteria(dc);
		List<String> colnames = new ArrayList<String>();
		for (SysDicColumn c : list) {
			colnames.add(c.getColumncode().toUpperCase());
		}

		// 设置已定义的字段属性used为1
		for (TableColumnVo vo : columns) {
			String colname = vo.getColname().toUpperCase();
			if (colnames.contains(colname)) {
				vo.setUsed((byte) 1);
			} else {
				vo.setUsed((byte) 0);
			}
		}

		PaginationSupport ps = new PaginationSupport(columns, cols.size(),
				pageNumber, pageSize);
		return ps;
	}

	@Override
	public PaginationSupport query(String tablecode, Integer pageNumber,
			Integer pageSize) {

		// 配置的字段
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		PaginationSupport ps = dicColumnDao.findByCriteria(dc, pageSize,
				pageNumber);
		List<SysDicColumn> cfgcols = ps.getItems();
		// 原始字段
		SqlVO sqlvo = dicTableDao.getTableInfoRT(tablecode);
		List<ColVO> orgcols = sqlvo.getCols();
		HashMap<String, ColVO> map = new HashMap<String, ColVO>();
		for (ColVO v : orgcols) {
			map.put(v.getName().toUpperCase(), v);
		}
		// 开始比较
		for (SysDicColumn c : cfgcols) {
			c.setColumncode(c.getColumncode().toUpperCase());
			if (null == map.get(c.getColumncode())) {
				c.setDisagree((byte) 1);
				c.setRemark("该列为虚拟字段，在数据库中不存在!");
				continue;
			}
			ColVO cv = map.get(c.getColumncode());
			String result = compareType(cv.getTypename(), cv.getLength(),
					c.getColumntype(),
					c.getColumnlength() == null ? 0 : c.getColumnlength());
			if ("OK".equals(result)) {
				c.setDisagree((byte) 0);

			} else {
				c.setDisagree((byte) 1);
				c.setRemark(result);
			}
		}
		return ps;
	}

	public String compareType(String dbtype, int dblen, String cfgtype,
			int cfglen) {
		// 数据库为
		if ("S".equals(cfgtype)) { // 配置为字符
			if ("VARCHAR2".equals(dbtype)) {
				if (dblen < cfglen) {
					return "字段长度超过数据库字段长!";
				}else{
					return "OK";
				}
			} else {
				// 数据库不为字符
				return "数据库字段类型为" + dbtype + ",不能存储字符!";
			}
		} else if ("N".equals(cfgtype)) {
			if ("NUMBER".equals(dbtype)) {
				if (dblen < cfglen) {
					return "字段长度超过数据库字段长!";
				}
				return "OK";
			} else if ("VARCHAR2".equals(dbtype)) {
				return "OK";
			} else {
				return "数据库字段类型为" + dbtype + ",不能存储数字!";
			}
		} else if ("D".equals(cfgtype) || "T".equals(cfgtype)) {
			if ("DATE".equals(dbtype) || "TIMESTAMP".equals(dbtype) ||"VARCHAR2".equals(dbtype)) {
				return "OK";
			} else {
				return "数据库字段类型为" + dbtype + ",不能存储日期!";
			}
		}else if("B".equals(cfgtype)){
			if("NUMBER".equals(dbtype)||"INTEGER".equals(dbtype)||"LONG".equals(dbtype)||"VARCHAR2".equals(dbtype)){
				return "OK";
			}else{
				return "数据库字段类型为" + dbtype + ",不能存储逻辑型数据!";
			}
		}
		return "数据库字段类型与配置的类型不匹配!";
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void edit(SysDicColumn dicColumn) throws Exception {

		// 验证字段是否存在
		/**
		SqlVO sqlvo = dicTableDao.getTableInfoRT(dicColumn.getTablecode());
		List<ColVO> colvolist = sqlvo.getCols();
		String colname = dicColumn.getColumncode().toUpperCase();

		boolean exists = false;
		for (ColVO v : colvolist) {
			System.out.println(v.getName().toUpperCase());
			if (colname.equals(v.getName().toUpperCase())) {
				exists = true;
				break;
			}
		}
		if (!exists) {
			throw new AppException("datatable.column.add.virtual", new String[] {
					dicColumn.getTablecode(), colname });
		}*/

		// 解决checkbox控件问题，并更新字段类型信息
		if (dicColumn.getIscodecol() == null) {
			dicColumn.setIscodecol((byte) 0);
		}

		if (dicColumn.getIsnamecol() == null) {
			dicColumn.setIsnamecol((byte) 0);
		}

		if (dicColumn.getIskeycol() == null) {
			dicColumn.setIskeycol((byte) 0);
		}

		if (dicColumn.getIspkeycol() == null) {
			dicColumn.setIspkeycol((byte) 0);
		}

		if (dicColumn.getIslevelnocol() == null) {
			dicColumn.setIslevelnocol((byte) 0);
		}

		if (dicColumn.getIsleafcol() == null) {
			dicColumn.setIsleafcol((byte) 0);
		}

		if (dicColumn.getIsunqcol() == null) {
			dicColumn.setIsunqcol((byte) 0);
		}
		if (dicColumn.getNullable() == null) {
			dicColumn.setNullable((byte) 0);
		}
		if (dicColumn.getStartmark() == null) {
			dicColumn.setStartmark((byte) 0);
		}
		dicColumnDao.saveOrUpdate(dicColumn);
		List<SysDicColumn> diccols = (List<SysDicColumn>) dicColumnDao.find(
				" from SysDicColumn where tablecode=?",
				dicColumn.getTablecode());

		// 取得主表数据
		SysDicTable tab = null;
		List<SysDicTable> tabs = (List<SysDicTable>) dicTableDao
				.find(" from SysDicTable where tablecode=?",
						dicColumn.getTablecode());
		if (tabs.size() > 0) {
			tab = tabs.get(0);
		} else {
			throw new AppException("crud.editerr");
		}
		HashMap<String, String> orgcols = new HashMap<String, String>();
		for (SysDicColumn c : diccols) {
			if (c.getIscodecol() == 1) {
				setColumnByCode(orgcols, "编码字段", c.getColumncode());
			}
			if (c.getIskeycol() == 1) {
				setColumnByCode(orgcols, "关键字字段", c.getColumncode());
			}
			if (c.getIspkeycol() == 1) {
				setColumnByCode(orgcols, "上级关键字字段", c.getColumncode());
			}
			if (c.getIsnamecol() == 1) {
				setColumnByCode(orgcols, "名称字段", c.getColumncode());
			}
			if (c.getIsleafcol() == 1) {
				setColumnByCode(orgcols, "是否末级字段", c.getColumncode());
			}
			if (c.getIslevelnocol() == 1) {
				setColumnByCode(orgcols, "层级字段", c.getColumncode());
			}
		}
		// 回填主表
		tab.setCodecolumn(StringUtil.isNotBlank(orgcols.get("编码字段")) ? orgcols.get("编码字段").toLowerCase() : orgcols.get("编码字段"));
		tab.setKeycolumn(StringUtil.isNotBlank(orgcols.get("关键字字段")) ? orgcols.get("关键字字段").toLowerCase() : orgcols.get("关键字字段"));
		tab.setSupercolumn(StringUtil.isNotBlank(orgcols.get("上级关键字字段")) ? orgcols.get("上级关键字字段").toLowerCase() : orgcols.get("上级关键字字段"));
		tab.setLevelnocolumn(StringUtil.isNotBlank(orgcols.get("层级字段")) ? orgcols.get("层级字段").toLowerCase() : orgcols.get("层级字段"));
		tab.setIsleafcolumn(StringUtil.isNotBlank(orgcols.get("是否末级字段")) ? orgcols.get("是否末级字段").toLowerCase() : orgcols.get("是否末级字段"));
		tab.setNamecolumn(StringUtil.isNotBlank(orgcols.get("名称字段")) ? orgcols.get("名称字段").toLowerCase() : orgcols.get("名称字段"));

		dicTableDao.update(tab);

	}

	private void setColumnByCode(HashMap<String, String> map, String key,
			String value) throws AppException {
		if (map.get(key) == null) {
			map.put(key, value);
		} else if (!value.equals(map.get(key))) {
			throw new AppException("datatable.columntype.conflict",
					new String[] { key, map.get(key), value });
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void delete(Long columnid) {
		dicColumnDao.delete(dicColumnDao.get(columnid));

		// 更新主表的字段缓存
	}

	@Override
	public SysDicColumn getCodeColumnDetail(String tablecode) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysDicColumn.class);
		dc.add(Property.forName("tablecode").eq(tablecode));
		dc.add(Property.forName("iscodecol").eq((byte) 1));
		List<SysDicColumn> list = (List<SysDicColumn>) dicColumnDao
				.findByCriteria(dc);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	// @Override
	// public String getTableColumnFormat(String tableName, String columnName) {
	// String hql = " from SysDicColumn where tablecode= ? and columncode = ?";
	// List<SysDicColumn> columns = (List<SysDicColumn>) dicColumnDao.find(
	// hql, tableName, columnName);
	// if (columns.size() > 0) {
	// return columns.get(0).getCodeformat();
	// }
	// return null;
	// }

	// public PaginationSupport queryTableColumns(Long id, Map<String, Object>
	// params) {
	// PaginationSupport columns = new PaginationSupport(0, 0);
	// if (id == null) {
	// return columns;
	// }
	//
	// SysDicTable tab = dicTableDao.get(id);
	// if (tab == null) {
	// return columns;
	// }
	//
	// Map<String, Object> filterMap = (Map<String, Object>)
	// params.get("dataFilter");
	// filterMap.put("tablecode", tab.getTablecode());
	//
	// // DataOrder order = (DataOrder) params.get("dataOrder");
	// //order.desc("columncode");
	//
	// System.out.println(filterMap);
	// return null;
	// //return dicColumnDao.find(filterMap, order, (DataPager)
	// params.get("dataPager"));
	//
	// }

	// @Override
	// public PaginationSupport getDicTables(String tablecode, String tablename,
	// Integer page, Integer rows) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public void copyColToTargetTable(String sourceTablecode, String targetTablecode) {
		
		List<SysDicColumn> list = dicColumnDao.findColumnsByTablecode(sourceTablecode);
		for (SysDicColumn column : list) {
			column.setColumnid(null);
			column.setTablecode(targetTablecode.toUpperCase());
			dicColumnDao.save(column);
		}
	}
}

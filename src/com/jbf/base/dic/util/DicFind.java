package com.jbf.base.dic.util;

import java.util.List;
import java.util.Map;

import com.jbf.base.dic.dao.SysDicElementDao;
import com.jbf.base.dic.po.SysDicElement;
import com.jbf.base.tabsdef.dao.SysDicTableDao;
import com.jbf.base.tabsdef.po.SysDicTable;
import com.jbf.common.dao.MapDataDaoI;
import com.jbf.common.dao.util.ColVO;
import com.jbf.common.dao.util.SqlVO;
import com.jbf.common.util.StringUtil;
import com.jbf.common.util.WebContextFactoryUtil;
/**
 * 字典查询工具类
 * @ClassName: DicFind 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月18日 下午3:56:36
 */
public class DicFind {
	
	private static SysDicTableDao dicTableDao = null;
	
	private static SysDicElementDao dicElementDao = null;

	private static MapDataDaoI mapDataDao = null;
	
	
	static void loadDao() {
		
		if (dicTableDao == null)
			dicTableDao = (SysDicTableDao)WebContextFactoryUtil.getBean("com.base.tabsdef.dao.SysDicTableDao");
		
		if (dicElementDao == null)
			dicElementDao = (SysDicElementDao)WebContextFactoryUtil.getBean("com.base.dic.dao.SysDicElementDao");
		
		if (mapDataDao == null)
			mapDataDao = (MapDataDaoI) WebContextFactoryUtil.getBean("com.jbf.common.dao.MapDataDao");

	}
	
	/**
	 * 借助数据项编码 通过ID查询字典CODE值
	 * @Title: findDicCodeByIdElementcode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param elementcode
	 * @param @param itemid
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public static String findDicCodeByIdElementcode(String elementcode, Long itemid) {
		loadDao();
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode.toUpperCase());

		return findDicCodeById(dicElement.getTablecode(), itemid);
	}
	
	/**
	 * 通过数据项编码和字典编码查询ID值
	 * @param elementcode
	 * @param code
	 * @return
	 */
	public static String findDicIDByCodeElementcode(String elementcode, String code) {
		loadDao();
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode.toUpperCase());
		
		return findDicIDByCode(dicElement.getTablecode(), code, elementcode);
	}
	
	/**
	 * 借助字典表 通过ID查询字典CODE值
	 * @Title: findDicCodeById 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param tablecode
	 * @param @param itemid
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 */
	public static String findDicCodeById(String tablecode, Long itemid) {
		loadDao();
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		List<Map> list = mapDataDao.queryList(tablecode, dicTable.getKeycolumn() + "='" + itemid + "'");
		
		if (list == null || list.size() == 0 || list.get(0) == null)
			return null;
		
		Map value = list.get(0);
		
		return (String)value.get(dicTable.getCodecolumn().toLowerCase());
	}
	
	/**
	 * 借助字典表 通过数据项编码和字典编码查询ID值
	 * @param tablecode
	 * @param code
	 * @param elementcode
	 * @return
	 */
	public static String findDicIDByCode(String tablecode, String code, String elementcode) {
		loadDao();
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		
		SqlVO sqlVO = dicTableDao.getTableInfo(tablecode);
		List<ColVO> cols = sqlVO.getCols();
		boolean pand = false;
		for (ColVO colVO : cols) {
			if (colVO.getName().equals("elementcode")) {
				pand = true;
				break;
			}
		}
		
		String where = "";
		if (pand)
			where = "upper(elementcode)='" + elementcode.toUpperCase() + "'";
		
		if (StringUtil.isNotBlank(where))
			where += " and ";
		where += dicTable.getCodecolumn() + "='" + code + "'";
		List<Map> list = mapDataDao.queryList(tablecode, where);
		
		if (list == null || list.size() == 0 || list.get(0) == null)
			return null;
		Map value = list.get(0);
		
		return value.get(dicTable.getKeycolumn().toLowerCase()).toString();
	}
	
	public static String findCodeNameByIDElementcode(String elementcode, Long itemid) {
		loadDao();
		SysDicElement dicElement = dicElementDao.getByElementcode(elementcode.toUpperCase());
		
		return findCodeNameByID(dicElement.getTablecode(), itemid);
	}
	
	public static String findCodeNameByID(String tablecode, Long itemid) {
		loadDao();
		SysDicTable dicTable = dicTableDao.getByTablecode(tablecode);
		List<Map> list = mapDataDao.queryList(tablecode, dicTable.getKeycolumn() + "='" + itemid + "'");
		
		if (list == null || list.size() == 0 || list.get(0) == null)
			return null;
		
		Map value = list.get(0);
		
		return value.get(dicTable.getCodecolumn().toLowerCase()) + "-" + value.get(dicTable.getNamecolumn().toLowerCase());
	}
	
}

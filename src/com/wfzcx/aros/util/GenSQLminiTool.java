package com.wfzcx.aros.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Table;

import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.cglib.beans.BeanMap;

import com.wfzcx.aros.xzfy.po.Casebaseinfo;
import com.wfzcx.aros.ysaj.po.Admlitbaseinfo;

/**
 * @ClassName: GenSQLminiTool
 * @Description: 生成sql语句
 * @author ybb
 * @date 2016年8月12日 上午10:04:43
 * @version V1.0
 */
public class GenSQLminiTool {

	public static final String INSERT = "insert" ;
	public static final String SELECT = "select" ;
	public static final String SELECT_CLEAR = "selectclear" ;
	public static final String UPDATE = "update" ;
	public static final String DELETE = "delete" ;
	
	private static Map<String, Object> golMap = null;
	
	/**
	 * @Title: main
	 * @Description: 打出sql语句
	 * @author ybb
	 * @date 2016年8月12日 上午10:05:11
	 * @param args
	 */
	public static void main(String[] args){
		
		//生成 指定select语句, 列顺序按照po的字段顺序.
		System.out.println(miniSQL(Admlitbaseinfo.class, SELECT_CLEAR, false)) ;
		System.out.println("\n");
		
		//生成 指定insert语句, 列顺序按照po的字段顺序.
		System.out.println(miniSQL(Casebaseinfo.class, INSERT, false)) ;
		System.out.println("\n");
		
		//生成 指定UPDATE语句, 列顺序按照po的字段顺序.
		System.out.println(miniSQL(Casebaseinfo.class, UPDATE, false)) ;
		System.out.println("\n");
		
		//生成 指定delete语句, 列顺序按照po的字段顺序.
		System.out.println(miniSQL(Casebaseinfo.class, DELETE, false)) ;
	}

	/**
	 * @Title: miniSQL
	 * @Description: 生成sql语句
	 * @author ybb
	 * @date 2016年8月12日 上午10:06:45
	 * @param pos
	 * @param category
	 * @param com
	 * @return
	 */
	@SuppressWarnings({"unchecked" })
	public static String miniSQL(Class pos, String category, boolean com) {
		
		GenSQLminiTool sql = new GenSQLminiTool();
		String tableName = sql.getTableName(pos);
		golMap = sql.genFieldCos(pos, com);
		String retstr = miniSQL(pos, category, tableName);
		/*if (category.equals(SELECT_CLEAR)) {
			retstr = retstr.replace(",", ",\n");
		}*/
		
		return retstr.toLowerCase();
	}
	
	/**
	 * @Title: miniSQL
	 * @Description: 生成对应的sql语句 <br/>
	 * example: GenSQLminiTool.miniSQL(new BeanPO(), GenSQLminiTool.UPDATE, "exp_tableinfo") ;
	 * @author ybb
	 * @param bean 实体类对象  推荐PO
	 * @param category sql语句种类  详见 .INSERT .UPDATE
	 * @param tableName 执行操作对应的表名   
	 * @return
	 */
	public static String miniSQL(Object bean, String category, String tableName) {
		
		StringBuilder preffix = new StringBuilder();
		if (!strIsEmpty(category)) {
			if (category.equalsIgnoreCase(SELECT)) {
				preffix.append("SELECT t.* FROM ").append(tableName).append(" t");
				return preffix.toString();
			}
			if (category.equalsIgnoreCase(SELECT_CLEAR)) {
				preffix.append("SELECT ").append(childSelect(bean, golMap)).append(" FROM ").append(tableName)
						.append(" t");
				return preffix.toString();
			}
			if (category.equalsIgnoreCase(UPDATE)) {
				preffix.append("UPDATE ").append(tableName).append(" SET");
				return preffix.toString() + childUpdate(bean, golMap);
			}
			if (category.equalsIgnoreCase(INSERT)) {
				preffix.append("INSERT INTO ").append(tableName);
				return preffix.toString() + childInsert(bean, golMap);
			}
			if (category.equalsIgnoreCase(DELETE)) {
				preffix.append("DELETE FROM ").append(tableName).append(" t WHERE ");
				return preffix.toString();
			}
		}
		return "Advice You Suicide Now";
	}
	
	/**
	 * @Title: childSelect
	 * @Description: 生成查询语句
	 * @author ybb
	 * @date 2016年8月12日 上午10:13:57
	 * @param bean
	 * @param reMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static String childSelect(Object bean, Map<String, Object> reMap) {
		
		Map<String, Object> bm;
		if (null != reMap)
			bm = reMap;
		else
			bm = (Map<String, Object>) BeanMap.create(bean);
		
		StringBuilder bufSel = new StringBuilder();
		int first = 0;
		for (Iterator<?> iter = bm.keySet().iterator(); iter.hasNext();) {
			Object objkey = iter.next();
			if (objkey.toString().equals("class")) {
				continue;
			}
			if (0 != first) {
				bufSel.append(", ");
			}
			first++;
			bufSel.append("t.").append(objkey.toString().toUpperCase());
		}
		return bufSel.toString();
	}
	
	/**
	 * @Title: childUpdate
	 * @Description: 生成修改语句
	 * @author ybb
	 * @date 2016年8月12日 上午10:14:46
	 * @param bean
	 * @param reMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static String childUpdate(Object bean, Map<String, Object> reMap) {
		
		Map<String, Object> bm;
		if (null != reMap)
			bm = reMap;
		else
			bm = (Map<String, Object>) BeanMap.create(bean);
		
		StringBuilder bufSet = new StringBuilder();
		for (Iterator<?> iter = bm.keySet().iterator(); iter.hasNext();) {
			Object objkey = iter.next();
			if (objkey.toString().equals("class")) {
				continue;
			}

			bufSet.append(" ").append(objkey.toString().toUpperCase()).append("=").append("#{")
					.append(objkey.toString()).append("},");
		}
		
		return bufSet.toString().substring(0, bufSet.length() - 1);
	}
	
	/**
	 * @Title: childInsert
	 * @Description: 生成新增语句
	 * @author ybb
	 * @date 2016年8月12日 上午10:15:33
	 * @param bean
	 * @param reMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected static String childInsert(Object bean, Map<String, Object> reMap) {
		
		StringBuilder bufSep1 = new StringBuilder("(");
		StringBuilder bufSep2 = new StringBuilder("(");
		
		Map<String, Object> bm;
		if (null != reMap)
			bm = reMap;
		else
			bm = (Map<String, Object>) BeanMap.create(bean);
		
		boolean first = true;
		for (Iterator<?> iter = bm.keySet().iterator(); iter.hasNext();) {
			Object objkey = iter.next();
			if (objkey.toString().equals("class")) {
				continue;
			}
			if (!first) {
				bufSep1.append(",");
				bufSep2.append(",");
			}
			first = false;
			// bufSep1
			bufSep1.append(objkey.toString().toUpperCase());
			// bufSep2
			bufSep2.append("#{" + objkey.toString() + "}");
		}
		
		bufSep1.append(")");
		bufSep2.append(")");
		
		return bufSep1.toString() + " VALUES " + bufSep2.toString();
	}
	
	/**
	 * @Title: strIsEmpty
	 * @Description: 是否为空
	 * @author ybb
	 * @date 2016年8月12日 上午10:16:20
	 * @param value
	 * @return
	 */
	public static boolean strIsEmpty(String value) {
		if (null == value)
			return true;
		if (value.length() == 4)
			return "NULL".equalsIgnoreCase(value);
		return "".equals(value) || value.length() <= 0;
	}
	
	/**
	 * @Title: getTableName
	 * @Description: 获取table名称
	 * @author ybb
	 * @date 2016年8月12日 上午10:07:34
	 * @param cls
	 * @return
	 */
	public String getTableName(Class<T> cls) {
		
		Table table = cls.getAnnotation(Table.class);
		return table.name().toLowerCase();
	}
	
	/**
	 * @Title: genFieldCos
	 * @Description: 获取实体类中的属性
	 * @author ybb
	 * @date 2016年8月12日 上午10:10:08
	 * @param cls
	 * @param compare
	 * @return
	 */
	public Map<String, Object> genFieldCos(Class<T> cls, final boolean compare) {
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Field[] fields = cls.getDeclaredFields();
		Arrays.sort(fields, new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				if (!compare)
					return 0;
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for (Field f : fields) {
			if ("serialVersionUID".equalsIgnoreCase(f.getName()))
				continue;
			map.put(f.getName(), f.getType());
		}

		return map;
	}
}

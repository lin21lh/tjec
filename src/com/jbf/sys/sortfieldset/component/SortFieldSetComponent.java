package com.jbf.sys.sortfieldset.component;

public interface SortFieldSetComponent {

	/**
	 * 根据排序字段设置ID获取排序SQL语句
	 * @Title: getSortSql 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortid 排序字段设置ID
	 * @param @return 设定文件 
	 * @return String 返回类型 order by + {设置排序模式}
	 * @throws
	 */
	public String getSortSql(Long sortid);
	
	/**
	 * 替换排序字符串中特殊字符
	 * @Title: replaceSortSql 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param sortSql 排序字符串
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String replaceSortSql(String sortSql);
}

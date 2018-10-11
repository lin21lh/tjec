/************************************************************
 * 类名：DbScriptManager
 *
 * 类别：通用类
 * 功能：数据库脚本管理，提供访问保存在resource/query下在数据库脚本的通用接口,供业务开发人员使用
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dbscript;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jbf.common.dbscript.model.DbScript;
import com.jbf.common.dbscript.model.DbScriptParser;
import com.jbf.common.dbscript.model.ScriptPieceI;
import com.jbf.common.util.XMLUtil;

public class DbScriptManager {
	/**
	 * 按脚本名字取得使用?占位符的sql或hql语句 <br/>
	 * 例如file1.script1代表从file1.xml文件中取得名为script1的脚本<br/>
	 * 可用于hql及sql
	 * 
	 * @param name
	 *            脚本名
	 * @param paramNames
	 *            查询参数名列表
	 * @return
	 * @throws Exception
	 */
	public static String getPreparedScript(String name,
			Collection<String> paramNames) throws Exception {
		DbScript ds = getRawScript(name);
		List<ScriptPieceI> spList = DbScriptParser.parse(ds.getScript());
		StringBuilder builder = new StringBuilder();
		for (ScriptPieceI sp : spList) {
			builder.append(sp.getCompiledSqlOrHql(paramNames));
		}
		return builder.toString();
	}

	/**
	 * 按脚本名字取得使用 ':'+paramName 占位符的sql或hql语句 <br/>
	 * 例如file1.script1代表从file1.xml文件中取得名为script1的脚本<br/>
	 * 只用于hql(受hibernate查询 API限制)
	 * 
	 * @param name
	 *            脚本名
	 * @param paramNames
	 *            查询参数名列表
	 * @return
	 * @throws Exception
	 */
	public static String getNamedParamScript(String name,
			Collection<String> paramNames) throws Exception {
		DbScript ds = getRawScript(name);
		List<ScriptPieceI> spList = DbScriptParser.parse(ds.getScript());
		StringBuilder builder = new StringBuilder();
		for (ScriptPieceI sp : spList) {
			builder.append(sp.getParametricHql(paramNames));
		}
		return builder.toString();
	}

	/**
	 * 按名字从部署的resource/query目录中取得查询语句
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	private static DbScript getRawScript(String name) throws IOException {
		// 文件路径
		String[] path = name.split("\\.");
		String envPath = DbScriptManager.class.getClassLoader()
				.getResource("resource/query/").toString();
		envPath = envPath.substring(6);
		String fileName = path[0] + ".xml";
		String scriptName = path[1];
		// 解析文件取得脚本内容
		Document doc = XMLUtil.readDocumentFromFile(envPath + fileName);

		Element root = doc.getRootElement();
		Iterator iter = root.elementIterator("query");
		while (iter.hasNext()) {
			Element ele = (Element) iter.next();
			String nodeName = ele.attribute("name").getValue();
			if (scriptName.equalsIgnoreCase(nodeName)) {
				DbScript ds = new DbScript(nodeName, ele.attribute("type")
						.getValue(), ele.attribute("remark").getValue(), null);
				ds.setScript(ele.getTextTrim());
				return ds;
			}
		}
		return null;
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		HashSet<String> set = new HashSet<String>();
		set.add("name");
		set.add("code");
		// 访问resource/query目录下的example.xml文件
		System.out.println(getPreparedScript("example.TEST_QUERY1", set));
		System.out.println(getNamedParamScript("example.TEST_QUERY1", set));
	}
}

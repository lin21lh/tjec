/************************************************************
 * 类名：ReadExcelFactory.java
 *
 * 类别：工厂类
 * 功能：创建excel读取工具
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-03-03  CFIT-PM   hyf         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.excel.core.factory;

import java.io.InputStream;
import java.io.Reader;

import com.jbf.base.excel.core.excel.ReadExcelI;
import com.jbf.base.excel.core.excel.ReadExcelImp;
import com.jbf.base.excel.core.xml.XmlCfgI;
import com.jbf.base.excel.core.xml.XmlCfgImp;

public class ReadExcelFactory {

	/**
	 * 由配置文件路径创建ReadExcelI
	 * 
	 * @param cfgFilePath
	 *            配置文件路径
	 * @return ReadExcelI实例
	 * @throws Exception
	 */
	public static ReadExcelI createByCfgFilePath(String cfgFilePath)
			throws Exception {
		return new ReadExcelImp(cfgFilePath);
	}

	/**
	 * 由输入流创建ReadExcelI
	 * 
	 * @param inputStream
	 *            输入流
	 * @param encoding
	 *            文件编码
	 * @return ReadExcelI实例
	 * @throws Exception
	 */
	public static ReadExcelI createByCfgInputStream(InputStream inputStream,
			String encoding) throws Exception {
		return new ReadExcelImp(inputStream, encoding);

	}

	/**
	 * 由Reader创建ReadExcelI
	 * 
	 * @param reader
	 *            输入流reader
	 * @return ReadExcelI实例
	 * @throws Exception
	 */
	public static ReadExcelI createByCfgReader(Reader reader) throws Exception {
		return new ReadExcelImp(reader);
	}

	/**
	 * 由Reader创建XmlCfgI
	 * 
	 * @param reader
	 * @return XmlCfgI实例
	 * @throws Exception
	 */
	public static XmlCfgI createCfgByReader(Reader reader) throws Exception {
		return new XmlCfgImp(reader);
	}
}

/************************************************************
 * 类名：DbScriptParser
 *
 * 类别：工具类
 * 功能：解析配置在classpath:reousrce/query目录中的数据库脚本配置文件，从中取得数据库脚本的工具类
 * 
 *   Ver     变更日期               部门            担当者        变更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-9-12  CFIT-PM   hyf         初版
 *   
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.common.dbscript.model;

import java.util.ArrayList;
import java.util.List;

import com.jbf.common.dbscript.model.scriptpiece.ParametricScriptPiece;
import com.jbf.common.dbscript.model.scriptpiece.PlainScriptPiece;

public class DbScriptParser {

	/**
	 * 解析脚本用于参数填入
	 * 
	 * @param rawScript
	 * @return
	 * @throws Exception
	 */
	public static List<ScriptPieceI> parse(String rawScript) throws Exception {
		List<ScriptPieceI> list = new ArrayList<ScriptPieceI>();

		int type = -1; // -1代表非法 1代表文本， 2代表参数条件 ，3代表数据权限条件
		int start = 0;

		char c = rawScript.charAt(0);
		if (c == '[') {
			type = 2;
		} else {
			type = 1;
		}
		for (int i = 1; i < rawScript.length(); i++) {
			char nc = rawScript.charAt(i);
			switch (type) {
			case 1: // 1代表文本
				if (nc == '[') {
					// 结束文本段
					String text = rawScript.substring(start, i);
					PlainScriptPiece p = new PlainScriptPiece(text);
					list.add(p);
					// 开始参数段
					type = 2;
					start = i + 1;
				} else if (nc == ']') {
					throw new Exception("解析异常!");
				} else {

				}
				break;
			case 2:// 2代表参数条件

				if (nc == '[') {
					throw new Exception("解析异常!");
				} else if (nc == ']') {
					// 参数段结束
					String text = rawScript.substring(start, i);
					ParametricScriptPiece p = new ParametricScriptPiece(text);
					list.add(p);
					// 开始文本段
					type = 1;
					start = i + 1;
				} else {

				}
				break;
			case 3: // 3代表数据权限条件
				break;
			}
		}

		if (start != rawScript.length() - 1) {
			// 结束文本段
			String text = rawScript.substring(start, rawScript.length());
			PlainScriptPiece p = new PlainScriptPiece(text);
			list.add(p);
		}

		return list;
	}

	/**
	 * 打印，用于调试
	 * 
	 * @param list
	 */
	public static void print(List<ScriptPieceI> list) {
		for (ScriptPieceI p : list) {
			if (p instanceof PlainScriptPiece) {
				System.out.println("PLN\t" + p.getRawScript());
			} else if (p instanceof ParametricScriptPiece) {
				System.out.println("PAM\t" + p.getRawScript());
			} else {
				System.out.println("SCP\t" + p.getRawScript());
			}
		}
	}
}

/************************************************************
 * 类名：EasyUITreeSchemeResolve.java
 *
 * 类别：解析类
 * 功能：EasyUITree界面解析类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.ui.resolve;



public class EasyUITreeSchemeResolve {

	String treeID = "tree_elementVal";
	
	String contextmenu = "contextmenu";
	
	/**
	 * 获取Tree界面方案
	 * @param tablecode 表编码
	 * @param elementcode 数据项编码
	 * @return Tree界面方案字符串
	 */
	public String getTreeScheme(String tablecode, String elementcode) {
		
		String params = "elementcode=" + elementcode;
		
		StringBuffer loadDataGridFunction = new StringBuffer();
		loadDataGridFunction.append("function loadDataFunction() {");
		loadDataGridFunction.append("$(\"#" + treeID + "\").tree(");
		loadDataGridFunction.append(new EasyUITree(params, tablecode).toString());
		loadDataGridFunction.append(");}");
		return loadDataGridFunction.toString();
	}
	
	/**
	 * 获取Tree界面代码
	 * @return
	 */
	public String getTreeSchemeVM() {
		return "<ul id=\"" + treeID  + "\" ></ul>";
	}
	
	/**
	 * 
	  *    
	  * 项目名称：jbf   
	  * 类名称：EasyUITree   
	  * 类描述：   内部类 EasyUITree组装类
	  * 创建人：maqs
	  * @version    
	  *
	 */
	public class EasyUITree {
		String tablecode;
		String url = "base/dic/dicElementValSetController/queryDicTreeElementVals.do";
		String params;
		String method = "post";
		boolean animate = true;
		boolean checkbox = false;
		boolean cascadeCheck = false;
		boolean onlyLeafCheck = false;
		boolean lines = false;
		boolean dnd = false;
		String formatter;
		String onContextMenu = "function (e, node) { if(validFormEdit()) {e.preventDefault(); $(this).tree('select', node.target);"
			+"$('#" + contextmenu + "').menu('show', { left : e.pageX, top : e.pageY});}} " ;
		
		String onSelect = "function (node) { var n_id = node.id; $('#contentTree').form('load', contextpath + 'base/dic/dicElementValSetController/getDetailById.do?tablecode=${tablecode}&id=' + n_id); last_node_handle = n_id; }";
		String onBeforeSelect = "function(node) { if (flag_edit) {easyui_solicit('当前有数据未保存，是否保存数据？', function(c) { if (c != undefined) { if (c) { saveEdit();} else { rejectEdit(); }} else {}}); return false;} else {return true;} }";
		public EasyUITree() {}
		
		public EasyUITree(String params, String tablecode) {
			this.params = params;
			this.tablecode = tablecode;
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("{url:contextpath+\"").append(url).append("?").append(params).append("\"");
			sb.append(", method:\"").append(method).append("\"");
			sb.append(", animate:").append(animate);
			sb.append(", checkbox:").append(checkbox);
			sb.append(", cascadeCheck:").append(cascadeCheck);
			sb.append(", onlyLeafCheck:").append(onlyLeafCheck);
			sb.append(", lines:").append(lines);
			sb.append(", dnd:").append(dnd);
			sb.append(", onContextMenu:").append(onContextMenu);
			sb.append(",onSelect:").append(onSelect.replace("${tablecode}", tablecode));
			sb.append(", onBeforeSelect:").append(onBeforeSelect);
			sb.append("}");
			return sb.toString();
		}
		
		public String getTablecode() {
			return tablecode;
		}
		public void setTablecode(String tablecode) {
			this.tablecode = tablecode;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getParams() {
			return params;
		}
		public void setParams(String params) {
			this.params = params;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public boolean isAnimate() {
			return animate;
		}
		public void setAnimate(boolean animate) {
			this.animate = animate;
		}
		public boolean isCheckbox() {
			return checkbox;
		}
		public void setCheckbox(boolean checkbox) {
			this.checkbox = checkbox;
		}
		public boolean isCascadeCheck() {
			return cascadeCheck;
		}
		public void setCascadeCheck(boolean cascadeCheck) {
			this.cascadeCheck = cascadeCheck;
		}
		public boolean isOnlyLeafCheck() {
			return onlyLeafCheck;
		}
		public void setOnlyLeafCheck(boolean onlyLeafCheck) {
			this.onlyLeafCheck = onlyLeafCheck;
		}
		public boolean isLines() {
			return lines;
		}
		public void setLines(boolean lines) {
			this.lines = lines;
		}
		public boolean isDnd() {
			return dnd;
		}
		public void setDnd(boolean dnd) {
			this.dnd = dnd;
		}
		public String getFormatter() {
			return formatter;
		}
		public void setFormatter(String formatter) {
			this.formatter = formatter;
		}
	}
}

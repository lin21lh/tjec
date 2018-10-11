/************************************************************
 * 类名：PaginationSupport.java
 *
 * 类别：通用类
 * 功能：分页支持
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2010-9-24  CFIT-PM   赵胜运         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/

package com.jbf.common.dao;

import java.util.List;

public class PaginationSupport {
	public final static int PAGESIZE = 15;

	private int pageSize = PAGESIZE;// 每页条数

	private int totalCount;// 总记录数

	private List items = null;

	private int startIndex;// 起始条

	private int nextIndex;// 下一页起始条 

	private int previousIndex;// 上一页起始条

	private int lastIndex;// 最后页起始条

	private int pageCount;// 总页数

	private int currentPage;// 当前页

	private int prePage;// 上一页

	private int nextPage;// 下一页

	private Boolean viewPrevious;// 可上一页

	private Boolean viewNext;// 可下一页

	private String pagefooter = "";

	private String pagefooter_T = "";
	
	private List sumFooter;

	public PaginationSupport(int pageSize, int startIndex) {
		setPageSize(pageSize);
		setStartIndex(startIndex);

	}

	public PaginationSupport(List items, int totalCount) {
		setPageSize(PAGESIZE);
		setItems(items);
		setTotalCount(totalCount);
		setStartIndex(0);

	}

	public PaginationSupport(List items, int totalCount, int startIndex) {
		setPageSize(PAGESIZE);
		setItems(items);
		setTotalCount(totalCount);
		setStartIndex(startIndex);

	}

	public PaginationSupport(List items, int totalCount, int pageSize,
			int startIndex) {
		setPageSize(pageSize);
		setItems(items);
		setTotalCount(totalCount);
		setStartIndex(startIndex);
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		// int count = totalCount / pageSize;
		// if (totalCount % pageSize > 0)
		// count++;
		// return count;
		//
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize
				: totalCount / pageSize + 1;
		return totalPage;
	}

	public int getCurrentPage() {
		return getStartIndex() / pageSize + 1;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPrePage(int prePage) {
		this.prePage = prePage;
	}

	public int getPrePage() {
		if (getCurrentPage() > 1)
			return getCurrentPage() - 1;
		else
			return 1;
	}

	public int getNextPage() {
		if (getCurrentPage() < getPageCount())
			return getCurrentPage() + 1;
		else
			return getPageCount();
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public void setViewPrevious(Boolean viewPrevious) {
		this.viewPrevious = viewPrevious;
	}

	public Boolean getViewPrevious() {
		if (getCurrentPage() == getPrePage() || getPageCount() == 0)
			return false;
		else
			return true;
	}

	public void setViewNext(Boolean viewNext) {
		this.viewNext = viewNext;
	}

	public Boolean getViewNext() {
		if (getCurrentPage() == getNextPage() || getNextPage() == 0)
			return false;
		else
			return true;
	}

	public void setItems(List items) {
		this.items = items;
	}

	public List getItems() {
		return items;
	}

	public void setNextIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}

	public int getNextIndex() {
		int nextIndex = getStartIndex() + getPageSize();
		if (nextIndex >= getTotalCount())
			return getStartIndex();
		else
			return nextIndex;
	}

	public void setPreviousIndex(int previousIndex) {
		this.previousIndex = previousIndex;
	}

	public int getPreviousIndex() {
		int previousIndex = getStartIndex() - getPageSize();
		if (previousIndex < 0)
			return 0;
		else
			return previousIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getLastIndex() {
		int lastIndex = getPageCount() * getPageSize() + 1;
		if (lastIndex >= getTotalCount())
			return lastIndex - getPageSize();
		else
			return nextIndex;
	}

	public void setPagefooter(String pagefooter) {
		this.pagefooter = pagefooter;
	}

	/**
	 * 分页栏函数 必需被包含在某个Form之中
	 * 
	 * @return String 分栏字符串
	 */

	public String getPagefooter_T() {
		StringBuilder str = new StringBuilder();
		if (this.getViewPrevious()) {
			str.append("<INPUT  type=submit value=首页 name=firs onclick=\"this.form.currentPage.value=1\"  >");
		} else {
			str.append("<INPUT   type=submit value=首页 name=firs  disabled>");
		}
		if (this.getViewPrevious()) {
			str.append("<INPUT  type=submit value=上页 name=prev onclick=\"this.form.currentPage.value='");
			str.append(this.getPreviousIndex() + 1);
			str.append("'\" >");
		} else {
			str.append("<INPUT   type=submit value=上页 name=prev  disabled>");
		}

		if (this.getPageCount() > 1 && this.getViewNext()) {
			str.append("<INPUT  type=submit value=下页 name=next onclick=\"this.form.currentPage.value='");
			str.append(this.getNextIndex() + 1);
			str.append("'\" >");
		} else {
			str.append("<INPUT   type=submit value=下页 name=next  disabled>");
		}
		if (this.getPageCount() > 1 && this.getViewNext()) {
			str.append("<INPUT   type=submit value=末页 name=last onclick=\"this.form.currentPage.value='");
			str.append(this.getPageCount());
			str.append("'\" >");
		} else {
			str.append("<INPUT type=submit value=末页 name=last    disabled>");
		}
		str.append(" 共").append(this.getTotalCount()).append("条记录");

		str.append("  每页").append(this.getPageSize());

		str.append("条 分").append(this.getPageCount()).append("页显示 转到");

		str.append("<SELECT size=1 name=Pagelist onchange='this.form.currentPage.value=this.value;this.form.submit();this.disabled=true;'>");
		for (int i = 1; i < this.getPageCount() + 1; i++) {
			if (i == this.getCurrentPage()) {
				str.append("<OPTION value=").append(i).append(" selected>")
						.append(i).append("</OPTION>");
			} else {
				str.append("<OPTION value=")
						.append((i - 1) * this.getPageSize() + 1).append(">")
						.append(i).append("</OPTION>");
			}
		}
		str.append("</SELECT>页");
		str.append("<INPUT type=hidden  value=").append(this.getCurrentPage())
				.append(" name=\"currentPage\" id=\"currentPage\"> ");
		str.append("<INPUT type=hidden  value=").append(this.getPageSize())
				.append(" name=\"pageSize\" id=\"pageSize\"> ");

		return str.toString();
	}

	/**
	 * 分页栏函数 必需被包含在某个Form之中
	 * 
	 * @return String 分栏字符串
	 */

	public String getPagefooter() {
		StringBuilder str = new StringBuilder("");
		if (this.getPageCount() >= 1) {

			if (this.getViewPrevious()) {
				str.append("<a onclick=\"formlist.currentPage.value=1\"  href=\"javascript:formlist.submit();\">首 页</a>");
				str.append(
						"&nbsp;<a href=\"javascript:formlist.submit();\"  onclick=\"formlist.currentPage.value='")
						.append(this.getCurrentPage() - 1)
						.append("'\" >上一页</a>");
			} else {
				str.append("首 页  上一页 ");
			}
			if (this.getViewNext()) {
				str.append("&nbsp;<a    onclick=\"formlist.currentPage.value=")
						.append(this.getCurrentPage() + 1)
						.append("\"   href=\"javascript:formlist.submit();\">下一页</a> ");
				str.append("&nbsp;<a  onclick=\"formlist.currentPage.value='")
						.append(this.getPageCount())
						.append("'\"  href=\"javascript:formlist.submit();\">末 页</a>");
			} else {
				str.append(" 下一页  末 页");
			}
			str.append(" 转到");

			str.append("<SELECT size=1 name=Pagelist onchange='this.form.currentPage.value=this.value;this.form.submit();this.disabled=true;'>");
			for (int i = 1; i < this.getPageCount() + 1; i++) {
				if (i == this.getCurrentPage()) {
					str.append("<OPTION value=").append(i).append(" selected>")
							.append(i).append("</OPTION>");
				} else {
					str.append("<OPTION value=").append(i).append(">")
							.append(i).append("</OPTION>");
				}
			}
			str.append("</SELECT>页");

			str.append("【第<span>").append(this.getCurrentPage())
					.append(" </span>页 , 共<span>");
			str.append(this.getPageCount()).append(" </span>页 】【<span>")
					.append(this.getPageSize()).append("</span>条/页 , 共<span>")
					.append(this.getTotalCount()).append("</span>条记录】");
			str.append("<INPUT type=hidden  value=")
					.append(this.getCurrentPage())
					.append(" name=\"currentPage\" id=\"currentPage\"> ");
		}
		return str.toString();

	}

	public void setPagefooter_T(String pagefooter_T) {
		this.pagefooter_T = pagefooter_T;
	}

	public void setSumFooter(List sumFooter) {
		this.sumFooter = sumFooter;
	}

	public List getSumFooter() {
		return sumFooter;
	}
}

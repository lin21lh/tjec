package com.freework.freedbm.util;

import java.util.Collection;
import java.util.List;


public class PageTotalResult<T> implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int page=0;
	int total=0;
	Collection<T> rows;
	public PageTotalResult(){
		
	}
	public PageTotalResult(TotalResult<T> r,int page){
		this.total=r.getResults();
		this.rows=r.getItems();
		this.page=page;
		
		
	}
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Collection<T> getRows() {
		return rows;
	}
	public void setRows(Collection rows) {
		this.rows = rows;
	}
	
}

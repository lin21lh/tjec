package com.freework.freedbm.util;

import java.util.Collection;


public class GeneralTotalResult<T> implements java.io.Serializable{

	/**
	 * add by xinpeng 
	 * 2016Äê4ÔÂ12ÈÕ14:19:14
	 */
	private static final long serialVersionUID = 1L;
	Boolean isSuc = false;
	String failReason = "";
	Collection<T> resultList;
	public GeneralTotalResult(){
		
	}
	public GeneralTotalResult(TotalResult<T> r,Boolean isSuc,String failReason){
		this.isSuc=isSuc;
		this.failReason=failReason;
		this.resultList=r.getItems();
	}
	public Boolean getIsSuc() {
		return isSuc;
	}
	public void setIsSuc(Boolean isSuc) {
		this.isSuc = isSuc;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}	
	public Collection<T> getResultList() {
		return resultList;
	}
	public void setResultList(Collection<T> resultList) {
		this.resultList = resultList;
	}
}

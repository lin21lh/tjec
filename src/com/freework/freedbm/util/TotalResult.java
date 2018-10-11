package com.freework.freedbm.util;

import java.util.List;


public class TotalResult<T> {

	

	private int results;

	private List<T> items;

	public TotalResult(List<T> items,int results){
		this.items=items;
		this.results=results;
		
	}
	public TotalResult(){
	}
	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {

		this.items = items;

	}
	public int getResults() {

		return results;

	}
	public void setResults(int results) {

		this.results = results;

	}
	public String toString() {
		String jsonList =null;
//		if (items instanceof JSONArray)
//			 jsonList =items.toString();
//			 else{
				 jsonList=items.toString();
			 //}
		
		
		return new StringBuffer("{\"items\":").append(jsonList).append(
				",\"results\":").append(results).append("}").toString();

	}
	
}

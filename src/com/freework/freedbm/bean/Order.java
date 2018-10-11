package com.freework.freedbm.bean;

import java.util.LinkedList;
import java.util.List;

import com.freework.freedbm.cfg.fieldcfg.FieldInfoEnum;
import com.freework.freedbm.dao.OrderInfo;
import com.freework.freedbm.dao.OrderInfo.Sort;

public class Order {
	private  List<OrderInfo> orders=new LinkedList<OrderInfo>(); 
	
	public Order asc(FieldInfoEnum colName){
		orders.add(new OrderInfo(colName,Sort.asc));
		return  this;
	}
	public Order asc(String colName){
		orders.add(new OrderInfo(colName,Sort.asc));
		return  this;
	}

	public Order desc(FieldInfoEnum colName){
		orders.add(new OrderInfo(colName,Sort.desc));
		return  this;
	}
	public Order desc(String colName){
		orders.add(new OrderInfo(colName,Sort.desc));
		return  this;
	}
	public Order order(String colName,String sort){
		orders.add(new OrderInfo(colName,sort==null||"".equals(sort)||"asc".equals(sort)?"asc":"desc"));
		return  this;
	}
	
	public Order order(Order order){
		for (OrderInfo orderInfo : order.orders) {
			if(!contains(orderInfo))
				orders.add(orderInfo);
		}
		return  this;
	}
	
	private boolean contains(OrderInfo info){
		for (OrderInfo orderInfo : orders) {
			if(orderInfo.getColName().equals(info.getColName())){
				return true;
			}
		}
		return false;
	}
	
	public String getOrder(){
		if(orders.size()==0)
			return "";
		boolean isFirst=true;
		StringBuilder orderStr=new StringBuilder(orders.size()+1*10);
		for (OrderInfo orderInfo : orders) {
			if(isFirst){
				orderStr.append(" order by ");
				isFirst=false;
			}else{
				orderStr.append(",");
			}
			orderStr.append(" ").append(orderInfo.getColName()).append(" ").append(orderInfo.getSortorder());
		}
		return orderStr.toString();
	}
}

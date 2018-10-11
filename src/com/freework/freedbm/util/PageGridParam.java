package com.freework.freedbm.util;

import java.util.Arrays;
import java.util.List;

import com.freework.freedbm.bean.Order;
import com.freework.freedbm.cfg.tablecfg.TableDataManager;
import com.freework.freedbm.cfg.tablecfg.TableQuery;
import com.freework.freedbm.dao.OrderInfo;
import com.freework.freedbm.dao.OrderInfo.Sort;
import com.freework.freedbm.dao.jdbcm.JdbcForDTO;



public class PageGridParam {
	private int page=1;
	private int rp=20;
	private String sortname;
	private String sortorder="";
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRp() {
		return rp;
	}
	public void setRp(int rp) {
		this.rp = rp;
	}
	public String getSortname() {
		return sortname;
	}
	public void setSortname(String sortname) {
		this.sortname = sortname;
	}
	public String getSortorder() {
		return sortorder;
	}
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
	
	
	public int getStartNum(){
		return page==1||page==0?0:(page-1)*rp;
	}
	
	public Order getOrder(TableQuery cfg){
		Order order=new Order();
		String keyname=cfg.getPKey()[0].getColName();
		if(sortname==null)
			return order.desc(keyname);

		JdbcForDTO f=cfg.getField(sortname);
		if(f==null){
			return order.desc(keyname);
		}else{
			JdbcForDTO key=cfg.getPKey()[0];
			if(key.getName().equals(f.getName())){
				return order.order(f.getName(), sortorder);
			}else{
				return order.order(f.getName(), sortorder).order(keyname, sortorder);
			}
		}
		
	}
	
}

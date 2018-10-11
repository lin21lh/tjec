package com.wfzcx.fam.manage.po;

/**
 * 
 * @ClassName: MessageBean 
 * @Description: 接收消息中心返回的对应bean
 * @author XinPeng
 * @date 2015年4月25日 下午11:13:41
 */
public class MessageBean {
	private int itemid;
	private int code;
	private String tm;
	public int getItemid() {
		return itemid;
	}
	public void setItemid(int itemid) {
		this.itemid = itemid;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTm() {
		return tm;
	}
	public void setTm(String tm) {
		this.tm = tm;
	}
}

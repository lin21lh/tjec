package com.jbf.sys.toRemind.vo;
/**
 * 待办提醒VO类
 * @ClassName: ToRemindVo 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年5月18日 上午10:39:13
 */
public class ToRemindVo {

	private Long resourceid;
	
	private String title;
	
	private String webpath;
	
	private String wholename;
	
	private Integer count;
	
	public ToRemindVo() {
		
	}
	
	public ToRemindVo(Long resourceid, String title, String webpath, String wholename, Integer count) {
		this.resourceid = resourceid;
		this.title = title;
		this.webpath = webpath;
		this.wholename = wholename;
		this.count = count;
	}
	
	public Long getResourceid() {
		return resourceid;
	}
	
	public void setResourceid(Long resourceid) {
		this.resourceid = resourceid;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getWebpath() {
		return webpath;
	}
	
	public void setWebpath(String webpath) {
		this.webpath = webpath;
	}
	
	public String getWholename() {
		return wholename;
	}
	
	public void setWholename(String wholename) {
		this.wholename = wholename;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public void setCount(Integer count) {
		this.count = count;
	}
}

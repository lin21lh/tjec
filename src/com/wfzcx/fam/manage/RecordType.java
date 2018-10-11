package com.wfzcx.fam.manage;


/**
 * 备案类型 枚举类
 * @ClassName: RecordType 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author MaQingShuang
 * @date 2015年4月16日 下午4:44:38
 */
public enum RecordType {

	REGISTER(1, "新开立"),
	CHANGE(2, "变更"),
	REVOKE(3, "撤销");
	
	private String name; //名称
	private Integer index; //实际数据库中值
	
	private RecordType(Integer index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public String getNameByIndex(Integer index) {
		
		for (RecordType t : RecordType.values()) {
			if (index == t.getIndex())
				return t.getName();
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getIndex() {
		return index;
	}
	
	public void setIndex(Integer index) {
		this.index = index;
	}
}

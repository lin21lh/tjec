/************************************************************
 * 类名：CodeType.java
 *
 * 类别：枚举类
 * 功能：编码类型枚举类
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-10-29  CFIT-PM   maqs         初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.dic.dto;

public enum CodeType {
	
	NoneCode("无编码格式", Byte.parseByte("0")), 
	OrderCode("顺序码", Byte.parseByte("1")), 
	LayerCode("层码", Byte.parseByte("2"));
	
	private String name;
	private Byte index;
	
	private CodeType(String name, Byte index) {
		this.name = name;
		this.index = index;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getIndex() {
		return index;
	}
	public void setIndex(Byte index) {
		this.index = index;
	}
	
	public String getNameByIndex(Byte index) {
		for(CodeType ct : CodeType.values()) {
			if (index == ct.getIndex())
				return ct.getName();
		}
		return null;
	}
	
    public static CodeType getCodeType(Byte index) {  
        switch (index) {  
        case 0:  
            return CodeType.NoneCode;  
        case 1:  
            return CodeType.OrderCode;  
        case 2:  
            return CodeType.LayerCode;
        default:  
            return CodeType.NoneCode;  
        }  
    } 
	
}

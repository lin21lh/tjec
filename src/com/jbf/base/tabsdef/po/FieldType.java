/************************************************************
 * 类名：FieldType.java
 *
 * 类别：枚举类
 * 功能：字段类型枚举项 
 * 
 *   Ver     変更日               部门            担当者        変更内容
 * ──────────────────────────────────────────────
 *   V1.00  2014-6-19  CFIT-PM   maqs        初版
 *
 * Copyright (c) 2014 CFIT-Weifang Company All Rights Reserved.
 ************************************************************/
package com.jbf.base.tabsdef.po;

public enum FieldType {
    
    COMMON_COLUMN("commoncolumn", Byte.valueOf("0")),
    KEY_COLUMN("Keycolumn", Byte.valueOf("1")),
    CODE_COLUMN("Codecolumn", Byte.valueOf("2")),
    NAME_COLUMN("Namecolumn", Byte.valueOf("3")),
    SUPER_COLUMN("Supercolumn", Byte.valueOf("4")),
    LEVELNO_COLUMN("Levelnocolumn", Byte.valueOf("5")),
    ISLEAF_COLUMN("Isleafcolumn", Byte.valueOf("6")),
    STATUS_COLUMN("Statuscolumn", Byte.valueOf("7")),
    STARTDATE_COLUMN("Startdatecolumn", Byte.valueOf("8")),
    ENDDATE_COLUMN("Enddatecolumn", Byte.valueOf("9"));
    
   
    
    private String name;
    private Byte index;
    
    /**
     * 
     */
    private FieldType(String name, Byte index) {
        this.name = name;
        this.index = index;
    }
    
    public String getNameByIndex(Byte index) {
        
        if(index == 0){
            return null;
        }
        
        for (FieldType t : FieldType.values()) {
            if (index.byteValue() == t.getIndex().byteValue())
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

    public Byte getIndex() {
        return index;
    }

    public void setIndex(Byte index) {
        this.index = index;
    }
   
    
}

package com.freework.freedbm.cfg.fieldcfg.type;

import java.lang.reflect.Field;
/**
 * @author �̷���
 * @category
 */
public interface Type extends java.io.Serializable{
 public Class getReturnedClass();
 public String getName();
 public boolean isCollectionType();
 

 
 
}

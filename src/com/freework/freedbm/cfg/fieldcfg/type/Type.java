package com.freework.freedbm.cfg.fieldcfg.type;

import java.lang.reflect.Field;
/**
 * @author ≥Ã∑Á¡Î
 * @category
 */
public interface Type extends java.io.Serializable{
 public Class getReturnedClass();
 public String getName();
 public boolean isCollectionType();
 

 
 
}

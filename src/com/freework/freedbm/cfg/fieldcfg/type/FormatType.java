package com.freework.freedbm.cfg.fieldcfg.type;
/**
 * @author �̷���
 * @category
 */
public interface FormatType<T> {

	public T stringToObject(String str);
	public String toString(T value);
	
}

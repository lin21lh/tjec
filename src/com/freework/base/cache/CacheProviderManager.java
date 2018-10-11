package com.freework.base.cache;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

public class CacheProviderManager {

	
	
	private static 	CacheManager manager;
	@Autowired
	public void  setManage(CacheManager manager){
		CacheProviderManager.manager=manager;
	}
	/**
	 * 
	 * 获取缓存中的数据
	 * 
	 * @param <T>
	 * 
	 * @param name
	 * 
	 * @param key
	 * 
	 * @return
	 */
	public final static Object get(String name, Serializable key) {
		if (name != null && key != null){
			Cache cache=manager.getCache(name);
			if(cache!=null){
				ValueWrapper value=cache.get(key);
				if(value!=null)
				return value.get();
			}
			else
				return null;
		}
		return null;
	}
	/**
	 * 
	 * 获取缓存中的数据
	 * 
	 * @param <T>
	 * 
	 * @param name
	 * 
	 * @param key
	 * 
	 * @return
	 */
	public final static <T> T get(Class<T> resultClass, String name,
			Serializable key)  {
		if (name != null && key != null){
			Cache cache=manager.getCache(name);
			if(cache!=null){
				ValueWrapper value=cache.get(key);
				if(value!=null)
				return (T)value.get();
			}else
				return null;
		}
		return null;
	}
	
	
	
	
	/**
	 * 
	 * 写入缓存
	 * 
	 * @param name
	 * 
	 * @param key
	 * 
	 * @param value
	 */

	public final static void set(String name, Serializable key,
			Serializable value) {
		if (name != null && key != null){
			Cache cache=manager.getCache(name);
			if(cache!=null)
				 cache.put(key, value);
			else
				new CacheException("缓存不存在"+name);
		}

	}

	/**
	 * 
	 * 清除缓冲中的某个数据
	 * 
	 * @param name
	 * 
	 * @param key
	 */

	public final static void evict(String name, Serializable key) {

		if (name != null && key != null){
			Cache cache=manager.getCache(name);
			if(cache!=null)
				 cache.evict(key);
			else
				new CacheException("缓存不存在"+name);
		}
	}

	
	
	/**
	 * 
	 * 清除缓冲中的某个数据
	 * 
	 * @param name
	 * 
	 * @param key
	 */

	public final static void evict(String name, Serializable key[]) {

		if (name != null && key != null){
			
			Cache cache=manager.getCache(name);
			if(cache!=null){
				for (int i = 0; i < key.length; i++) {
					 cache.evict(key[i]);
					 
				}
			}
			else
				new CacheException("缓存不存在"+name);
		}
	}
	/**
	 * 
	 * 清除缓冲中的某个数据
	 * 
	 * @param name
	 * 
	 * @param key
	 */

	public final static void clear(String name) {

		if (name != null ) {
			
			Cache cache=manager.getCache(name);
			if (cache != null)
					cache.clear();
			else
				new CacheException("缓存不存在"+name);
		}
	}
	
}

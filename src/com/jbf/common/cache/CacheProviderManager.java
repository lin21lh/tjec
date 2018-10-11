package com.jbf.common.cache;

import java.io.Serializable;

import net.sf.ehcache.CacheException;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

public class CacheProviderManager {

    private static CacheManager manager;

    public static CacheManager getManager() {
        return manager;
    }

    public static void setManager(CacheManager manager) {
        CacheProviderManager.manager = manager;
    }

    // @Autowired
    // public void setManage(CacheManager manager) {
    // CacheProviderManager.manager = manager;
    // }

    public final static Object get(String name, String key) {
        if (name != null && key != null) {
            Cache cache = manager.getCache(name);
            if (cache != null) {
                ValueWrapper value = cache.get(key);
                if (value != null)
                    return value.get();
            } else
                return null;
        }
        return null;
    }
    
    public final static Object get(String name) {
    	if (name != null && name.length() > 0) {
    		return manager.getCache(name);
    	} else 
    		return null;
    }

    public final static void set(String name, String key, Object value) {
        if (name != null && key != null) {
            Cache cache = manager.getCache(name);
            if (cache != null)
                cache.put(key, value);
            else
                new CacheException("缓存不存在" + name);
        }

    }

    public final static void evict(String name, Serializable key) {

        if (name != null && key != null) {
            Cache cache = manager.getCache(name);
            if (cache != null)
                cache.evict(key);
            else
                new CacheException("缓存不存在" + name);
        }
    }

    public final static void evict(String name, Serializable key[]) {

        if (name != null && key != null) {

            Cache cache = manager.getCache(name);
            if (cache != null) {
                for (int i = 0; i < key.length; i++) {
                    cache.evict(key[i]);

                }
            } else
                new CacheException("缓存不存在" + name);
        }
    }

    public final static void clear(String name) {

        if (name != null) {

            Cache cache = manager.getCache(name);
            if (cache != null)
                cache.clear();
            else
                new CacheException("缓存不存在" + name);
        }
    }
    
    /**
     * 获取缓存中的数据
     * @param resultClass
     * @param name
     * @param key
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
}

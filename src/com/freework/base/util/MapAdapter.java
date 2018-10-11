package com.freework.base.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;

public class MapAdapter extends XmlAdapter<MapAdapter.AdaptedMap, Map<String, String>> {
	public static class AdaptedMap {
	    @XmlAnyAttribute
		 private Map<QName,String> any;
    }
	@Override
	public Map<String, String> unmarshal(AdaptedMap adaptedMap) throws Exception {
		 
		Map<String, String> map = new HashMap<String, String>();
		Set<Entry<QName,String> > entrys=adaptedMap.any.entrySet();
        for(Entry<QName,String> item :entrys) {
            map.put(item.getKey().toString(), item.getValue());
        }
        return map;
    }
	@Override
	public AdaptedMap marshal(Map<String, String> map) throws Exception {
		System.out.println(map);
		if(map==null)
			return null;
		AdaptedMap adaptedMap = new AdaptedMap();
		adaptedMap.any= new HashMap<QName, String>();
        for(Entry<String, String> entry : map.entrySet()) {
        	adaptedMap.any.put(new QName(entry.getKey()), entry.getValue());
        }
        return adaptedMap;
	}
}

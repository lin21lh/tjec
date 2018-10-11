package com.freework.common.bean;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.beans.factory.FactoryBean;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
public class FastJsonSerializeConfig  implements FactoryBean<FastJsonSerializeConfig> {
	static public final SerializeConfig config=SerializeConfig.getGlobalInstance();
	public FastJsonSerializeConfig(){
	}
	public void setObjectSerializer(Map<Class<?>,ObjectSerializer > map){
		 Set<Entry<Class<?>, ObjectSerializer>> entrys=map.entrySet();
		for (Entry<Class<?>, ObjectSerializer> entry : entrys) {
			config.put(entry.getKey(), entry.getValue());
		}
	}
	@Override
	public FastJsonSerializeConfig getObject() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Class<FastJsonSerializeConfig> getObjectType() {
		// TODO Auto-generated method stub
		return FastJsonSerializeConfig.class;
	}
	@Override
	public boolean isSingleton() {
		return true;
	}
}

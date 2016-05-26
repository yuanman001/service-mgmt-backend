package com.ai.paas.ipaas.dbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.ibatis.cache.CacheException;

import com.ai.paas.ipaas.PaasRuntimeException;

public class SystemConfig {
	
	 private static final Map<String, String> map = new HashMap<String, String>();

	    static {
	        InputStream in = null;
	        try {
	            in = SystemConfig.class.getClassLoader().getResourceAsStream("conf.properties");
	        } catch (Exception ex) {
	            throw new PaasRuntimeException("","读取配置文件失败");
	        }
	        if (in == null)
	            throw new PaasRuntimeException("","读取配置文件失败");
	        Properties p = new Properties();
	        try {
	            p.load(in);
	        } catch (IOException e) {
	            throw new CacheException(e);
	        }
	        Iterator<Entry<Object, Object>> it = p.entrySet().iterator();
	        while (it.hasNext()) {
	            Entry<Object, Object> entry = it.next();
	            Object key = entry.getKey();
	            Object value = entry.getValue();
	            map.put(key.toString(), value.toString());
	        }
	    }

	    public static String getString(String key) {
	        return map.get(key);
	    }

	    public static Integer getInteger(String key) {
	        return Integer.valueOf(map.get(key));
	    }

	    public static Long getLong(String key) {
	        return Long.valueOf(map.get(key));
	    }

	    public static Boolean getBoolean(String key) {
	        return Boolean.valueOf(map.get(key));
	    }

	    public static Map<String, String> readConf() {
	        return map;
	    }
	    
	   public static void main(String[] args) {
		  System.out.println(SystemConfig.getString("inner_user_name"));
		   
		   
	   }

	
}

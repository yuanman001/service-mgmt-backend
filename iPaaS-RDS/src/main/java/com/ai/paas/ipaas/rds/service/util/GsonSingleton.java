package com.ai.paas.ipaas.rds.service.util;


import org.springframework.stereotype.Service;

import com.google.gson.Gson;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午2:08:14 
 * @version 
 * @since  
 */
@Service
public class GsonSingleton {
	private Gson g;
	public Gson getGson(){
		if(null == g){
			g = new Gson();
		}
		return g;
	}
}

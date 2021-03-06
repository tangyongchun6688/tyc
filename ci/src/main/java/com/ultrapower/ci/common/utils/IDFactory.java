package com.ultrapower.ci.common.utils;


import java.util.UUID;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
/**
 * 
 * @author yangbin6
 * Description : 
 * 2017年12月1日
 */
public class IDFactory {
	
	private static WebApplicationContext wac;
	
	/**
	 * 获取spring 的 web容器
	 * @return
	 */
	public static synchronized WebApplicationContext getWebContext() {
		if (wac == null) {
			wac = ContextLoader.getCurrentWebApplicationContext();
		}
		return wac;
	}
		
	/**
	 * 返回32位不重复码
	 * @return
	 */
	public  static String getIDStr()
	{
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replace("-", "");
		return id;
	}
}

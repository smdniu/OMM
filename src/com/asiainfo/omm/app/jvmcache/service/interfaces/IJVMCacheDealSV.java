package com.asiainfo.omm.app.jvmcache.service.interfaces;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * JVMCache查询类接口
 * 
 * @author oswin
 *
 */
public interface IJVMCacheDealSV {

	/**
	 * 根据本条件获取JVM缓存信息
	 * 
	 * @param condition 条件
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJVMCacheInfoByCondition(Map<String, Object> condition) throws Exception;
	
	/**
	 * 根据本条件更新JVM缓存信息
	 * 
	 * @param condition 条件
	 * @return
	 * @throws Exception
	 */
	public JSONObject updateJVMCacheInfoByCondition(Map<String, Object> condition) throws Exception;
}

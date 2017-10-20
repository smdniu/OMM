package com.asiainfo.omm.app.jvmcache.service.interfaces;

/**
 * jvmcache操作<br>
 * appframe缓存查询以及缓存更新
 * 
 * @author oswin
 *
 */
public interface IOmmJVMCacheSV {
	
	/**
	 * 新增jvmcache分组数据
	 * 
	 * @param groupCode
	 * @param groupName
	 * @param className
	 * @throws Exception
	 */
	public void addJVMCacheGroup(String groupCode, String groupName, String className, String ipPort) throws Exception;
}

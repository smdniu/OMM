package com.asiainfo.omm.service.interfaces;

/**
 * jvmcache应用操作
 * 
 * @author oswin
 *
 */
public interface IJVMCacheSV {
	
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

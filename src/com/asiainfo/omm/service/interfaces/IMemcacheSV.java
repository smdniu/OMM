package com.asiainfo.omm.service.interfaces;

/**
 * memcache操作类
 * 
 * @author oswin
 *
 */
public interface IMemcacheSV {
	
	/**
	 * 新增memcache的ip:port
	 * 
	 * @param memcacheIpPorts
	 */
	public void addMemcahceConfig(String[] memcacheIpPorts)throws Exception;

}

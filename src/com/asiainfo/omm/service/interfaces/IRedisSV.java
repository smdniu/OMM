package com.asiainfo.omm.service.interfaces;

public interface IRedisSV {
	/**
	 * 新增redis的ip:port
	 * 
	 * @param redisIpPorts
	 */
	public void addRedisConfig(String[] redisIpPorts)throws Exception;
}

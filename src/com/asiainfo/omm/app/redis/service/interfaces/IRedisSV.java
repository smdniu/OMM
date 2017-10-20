package com.asiainfo.omm.app.redis.service.interfaces;

import java.util.List;
import java.util.Map;

public interface IRedisSV {
	
	//根据key值获取value
	public Object getValue(String ip,int port,String key);
	
	//获取主机信息详情
	public Map getInfo(String ip,int port);
	
	//获取所有主机信息
	public Map getInfos();

}

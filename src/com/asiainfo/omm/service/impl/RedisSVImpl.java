package com.asiainfo.omm.service.impl;

import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.bo.BOOmmMenuBean;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.service.interfaces.IRedisSV;
import com.asiainfo.omm.utils.StringUtils;

public class RedisSVImpl implements IRedisSV {

	public void addRedisConfig(String[] redisIpPorts)throws Exception{
		TreeSet<String> ipPortSets = new TreeSet<String>();
		for(String redisIpPort: redisIpPorts){
			String[] ipPorts = redisIpPort.split(":");
			if(ipPorts != null && ipPorts.length == 3){
				ipPortSets.add(redisIpPort);
			}else{
				throw new Exception("新增redis地址不正确:" + redisIpPort);
			}
		}
		StringBuffer sb = new StringBuffer();
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		IBOOmmMenuValue[] rediss = menuSV.getMenuByMenuId1("1");
		if(rediss != null && rediss.length > 0 ){
			for(IBOOmmMenuValue redis: rediss){
				if(MENUENUM.REDIS.getMenuType().equals(redis.getMenuType())){//放入memcache可操作信息
					String redisAuth = redis.getMenuAuth();
					if(StringUtils.isNotBlank(redisAuth)){
						String[] limit = redisAuth.split(",");
						for(String key: limit){
							String[] keyTmp = key.split(":");
							if(keyTmp != null && keyTmp.length == 3){
								ipPortSets.add(key);
							}
						}
					}
				}
			}
			for(String ipPortSet: ipPortSets){
				sb.append(ipPortSet).append(",");
			}
			for(IBOOmmMenuValue redis: rediss){
				redis.setMenuAuth(sb.toString());
			}
			menuSV.updateMenu(rediss);
		}else{
			IBOOmmMenuValue newRediss = new BOOmmMenuBean();
			newRediss = new BOOmmMenuBean();
			newRediss.setId(1);
			newRediss.setName("redis应用");
			newRediss.setPath("/redis");
			newRediss.setMenuType(MENUENUM.REDIS.getMenuType());
			newRediss.setMenuCode("REDIS");
			for(String ipPortSet: ipPortSets){
				sb.append(ipPortSet).append(",");
			}
			newRediss.setMenuAuth(sb.toString());
			menuSV.addMenu(newRediss);
		}
	
	}
}

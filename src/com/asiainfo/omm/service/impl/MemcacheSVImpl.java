package com.asiainfo.omm.service.impl;

import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.bo.BOOmmMenuBean;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.service.interfaces.IMemcacheSV;
import com.asiainfo.omm.utils.StringUtils;

public class MemcacheSVImpl implements IMemcacheSV {

	public void addMemcahceConfig(String[] memcacheIpPorts)throws Exception{
		TreeSet<String> ipPortSets = new TreeSet<String>();
		for(String memcacheIpPort: memcacheIpPorts){
			String[] ipPorts = memcacheIpPort.split(":");
			if(ipPorts != null && ipPorts.length == 3){
				ipPortSets.add(memcacheIpPort);
			}else{
				throw new Exception("新增memcache地址不正确:" + memcacheIpPort);
			}
		}
		StringBuffer sb = new StringBuffer();
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		IBOOmmMenuValue[] memcaches = menuSV.getMenuByMenuId1("2");
		if(memcaches != null && memcaches.length > 0 ){
			for(IBOOmmMenuValue memcache: memcaches){
				if(MENUENUM.MEMCACHE.getMenuType().equals(memcache.getMenuType())){//放入memcache可操作信息
					String memcacheAuth = memcache.getMenuAuth();
					if(StringUtils.isNotBlank(memcacheAuth)){
						String[] limit = memcacheAuth.split(",");
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
			for(IBOOmmMenuValue memcache: memcaches){
				memcache.setMenuAuth(sb.toString());
			}
			menuSV.updateMenu(memcaches);
		}else{
			IBOOmmMenuValue newMemcaches = new BOOmmMenuBean();
			newMemcaches = new BOOmmMenuBean();
			newMemcaches.setId(2);
			newMemcaches.setName("memcache应用");
			newMemcaches.setPath("/memcache");
			newMemcaches.setMenuType(MENUENUM.MEMCACHE.getMenuType());
			newMemcaches.setMenuCode("MEMCACHE");
			for(String ipPortSet: ipPortSets){
				sb.append(ipPortSet).append(",");
			}
			newMemcaches.setMenuAuth(sb.toString());
			menuSV.addMenu(newMemcaches);
		}
	}
}

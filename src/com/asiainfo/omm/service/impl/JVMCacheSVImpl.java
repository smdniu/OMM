package com.asiainfo.omm.service.impl;

import java.util.Map;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.jvmcache.service.interfaces.IJVMCacheDealSV;
import com.asiainfo.omm.app.jvmcache.service.interfaces.IOmmJVMCacheSV;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IJVMCacheSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

public class JVMCacheSVImpl implements IJVMCacheSV {

	public void addJVMCacheGroup(String groupCode, String groupName, String className, String ipPortStr) throws Exception {
		IOmmJVMCacheSV ommJVMCacheSV = (IOmmJVMCacheSV)ServiceFactory.getService(IOmmJVMCacheSV.class);
		Map<String, Object> ipPorts = OMMEnumUtils.getJVMCacheIpPortInfoByGroupCode(groupCode);
		if(ipPorts != null){
			throw new OMMException("分组code重复!");
		}
		try {
			Class<?> clazz = Class.forName(className);
			if(!IJVMCacheDealSV.class.isAssignableFrom(clazz)){
				throw new OMMException("分组处理类不正确!");
			}
		} catch (Exception e) {
			throw new OMMException("分组处理类不正确!");
		}
		if(StringUtils.isNotBlank(ipPortStr)){
			String[] ipPortStrArr = ipPortStr.split(",");
			for(String ipPort: ipPortStrArr){
				String[] ipPortArr = ipPort.split(":");
				if(ipPortArr.length != 2 || StringUtils.isBlank(ipPortArr[0]) || !isIp(ipPortArr[0].trim())){
					throw new OMMException("分组分组包含ip:port[" + ipPort + "]格式不正确!");
				}
			}
		}
		ommJVMCacheSV.addJVMCacheGroup(groupCode, groupName, className, ipPortStr);
	}
	
	private static boolean isIp(String IP){//判断是否是一个IP  
        boolean flag = false;  
        if(IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")){  
            String s[] = IP.split("\\.");  
            if(Integer.parseInt(s[0])<255)  
                if(Integer.parseInt(s[1])<255)  
                    if(Integer.parseInt(s[2])<255)  
                        if(Integer.parseInt(s[3])<255)  
                        	flag = true;  
        }  
        return flag;  
    }  
}

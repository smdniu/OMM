package com.asiainfo.omm.app.redis.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.controller.RedisController;

/**
 * 获取所有的server
 * @author Administrator
 *
 */
public class ServersUtil {
	private static final Logger logger = Logger.getLogger(ServersUtil.class);

	public final static List<String> servers = new ArrayList<String>();
	// 这里从配置文件读取所有ip:port
	static {
		IOmmMenuSV sv = (IOmmMenuSV) ServiceFactory.getService(IOmmMenuSV.class);
		try {
			TreeSet<String> redisTree = new TreeSet<String>();
			IBOOmmMenuValue[] redis = sv.getMenuByMenuId1("1");
			if(redis != null && redis.length > 0){
				for(IBOOmmMenuValue redisTmp: redis){
					String ipPortStr = redisTmp.getMenuAuth();
					String[] ipPortArr = ipPortStr.split(",");
					for(String tmp: ipPortArr){
						if(tmp.endsWith("U")){
							redisTree.add(tmp.substring(0, tmp.length() - 2));
						}
					}
				}
			}
			servers.addAll(redisTree);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		servers.add("192.168.163.133:6379");
//		servers.add("192.168.163.132:6379");
//		servers.add("192.168.163.131:6379");
		logger.debug("OMM平台全量redis地址为:" + servers);
	}
}

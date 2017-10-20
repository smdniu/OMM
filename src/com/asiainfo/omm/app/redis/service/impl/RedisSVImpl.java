package com.asiainfo.omm.app.redis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import redis.clients.jedis.Jedis;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.memchace.client.validate.ValidateFactory;
import com.asiainfo.omm.app.redis.service.interfaces.IRedisSV;
import com.asiainfo.omm.app.redis.utils.JedisUtil;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.utils.SessionFactory;

public class RedisSVImpl implements IRedisSV {

	private static transient Log log = LogFactory.getLog(RedisSVImpl.class);
	// 获取连接池
	Map<String, Jedis> pool = JedisUtil.pool;
		
	// 根据key获取value
	@SuppressWarnings("unchecked")
	public Object getValue(String ip, int port, String key) {
		Object value = null;
		// 获取连接
		String ip_port = ip + ":" + port;
		try {
			//可以访问的连接
			List<String> redis = (List<String>) SessionFactory.getSession().getAttribute(OMMConstantEnum.OMM_MENU_REDIS_KEY);
			if(redis.contains(ip_port)){
				// 监测连接是否正常
				Jedis client = pool.get(ip_port);
				if(client==null || !client.isConnected()){
					client = new Jedis(ip,port);
				}
				
				try {
					if (client.exists(key)) {
						// 判断value类型
						String type = client.type(key);
						// string类型
						if ("string".equals(type)) {

							value = client.get(key);

						} else if ("list".equals(type)) {

							value = client.lrange(key, 0, -1);

						} else if ("hash".equals(type)) {

							value = client.hgetAll(key);
						}

					} 
				} catch (Exception e) {
					log.error(ip+":"+port+"下不存在key值"+key);
					//e.printStackTrace();
					value = "该key值不存在";
				}finally {
					pool.put(ip+":"+port, client);
				}
				
			}else{
				value="不存在该连接";
			}
		
		} catch (OMMException e1) {
			e1.printStackTrace();
		}
		
		return value;

	}

	// 1.监测所有的连接并返回连接主机信息
	public Map getInfo(String ip, int port) {
		Map<String, String> infoMap = new HashMap<String, String>();
		String message = null;
		String[] str = null;
		String key = ip + ":" + port;
		// 监测连接是否正常
		Jedis client = pool.get(key);
		if(client==null || !client.isConnected()){
			client = new Jedis(ip,port);
		}
		
		try {
			String info = client.info();
			str = info.split("\r\n");
			for (String s : str) {
				if (s.contains(":")) {
					String info_key = s.split(":")[0];
					String info_value = s.split(":")[1];
					infoMap.put(info_key, info_value);
					infoMap.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
					infoMap.put(OMMConstantEnum.RESULT_MSG, "连接正常");
				}

			}
		} catch (Exception e) {
			//log.error(ip+":"+port+"连接已经关闭或者连接不存在");
			//e.printStackTrace();
			client = null;
			infoMap.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			infoMap.put(OMMConstantEnum.RESULT_MSG, "连接异常");
		}finally {
			pool.put(key, client);
		}
		
		return infoMap;
	}

	@SuppressWarnings("unchecked")
	//获取所有的redis连接信息
	public Map getInfos() {
		
		Map<String,Map<String,String>> infos = new HashMap<String, Map<String,String>>();
		try {
			//可以访问的连接
			List<String> redis = (List<String>) SessionFactory.getSession().getAttribute(OMMConstantEnum.OMM_MENU_REDIS_KEY);

			for(String key : pool.keySet()){
				if(redis.contains(key)){
					Map infoMap = getInfo(key.split(":")[0], Integer.parseInt(key.split(":")[1]));
					infos.put(key, infoMap);
				}
			}
		} catch (OMMException e) {
			e.printStackTrace();
		}
		
		return infos;
	}
	
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("10.218.34.15",28001);
		System.out.println(jedis.exists("AOP_DEV_AD_ABILITYS"));
		
		
	}
	
	
}

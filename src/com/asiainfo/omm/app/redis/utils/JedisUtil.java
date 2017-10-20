package com.asiainfo.omm.app.redis.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.asiainfo.omm.app.redis.service.impl.RedisSVImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	
	private static transient Log log = LogFactory.getLog(JedisUtil.class);
	//所有主机连接池
	public static Map<String,Jedis> pool = new HashMap<String, Jedis>();
	
	//初始化连接池
	static{
		//获取所有的server(ip:port),从配置文件读取，或者别的地方获取
		List<String> servers = new ArrayList<String>();
		servers = ServersUtil.servers;
		for (String server: servers) {
			String[] arr = server.split(":");
			try {
				Jedis client = new Jedis(arr[0], Integer.valueOf(arr[1]));
				pool.put(server, client);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(server+"连接已经关闭或者连接不存在");
				e.printStackTrace();
				pool.put(server, null);
			}
		}
		
	}
	
	//从池中获取连接
	public static Jedis getClient(String ip,int port){
		Jedis client = pool.get(ip+":"+port);
		return client;
	}
	
	public static void main(String[] args) {
		Jedis client = JedisUtil.getClient("192.168.163.132", 6379);
		System.out.println(client.info());
	}
}
